package com.amazon.ata.cost;

import com.amazon.ata.types.ShipmentCost;
import com.amazon.ata.types.ShipmentOption;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * A strategy to calculate the weighted cost of a ShipmentOption.
 *
 * The cost is calculated based on the weighted combination of monetary and environmental costs.
 */
public class WeightedCostStrategy implements CostStrategy {

    private final Map<BigDecimal, CostStrategy> costStrategies = new HashMap<>();
    private double totalWeight = 0.0;

    /**
     * WeightedCost Strategy.
     */
    private WeightedCostStrategy() {}

    /**
    Builder.
    @return something.
     */
    public static Builder builder() {
        return new Builder();
    }

    @Override
    public ShipmentCost getCost(ShipmentOption shipmentOption) {
        BigDecimal totalCostValue = BigDecimal.ZERO;
        for (Map.Entry<BigDecimal, CostStrategy> entry : costStrategies.entrySet()) {
            BigDecimal weight = entry.getKey();
            CostStrategy strategy = entry.getValue();
            totalCostValue = totalCostValue.add(weight.multiply(strategy.getCost(shipmentOption).getCost()));
        }
        return new ShipmentCost(shipmentOption, totalCostValue);
    }

    public static class Builder {
        private final WeightedCostStrategy weightedCostStrategy = new WeightedCostStrategy();

        /**
         * addStrategyWithWeight.
         * @param strategy 123.
         * @param weight 123.
         * @return something.
         */
        public Builder addStrategyWithWeight(CostStrategy strategy, BigDecimal weight) {
            weightedCostStrategy.costStrategies.put(weight, strategy);
            weightedCostStrategy.totalWeight += weight.doubleValue();
            return this;
        }

        /**
         * WeightedCostStrategy.
         * @return something
         */
        public WeightedCostStrategy build() {
            if (weightedCostStrategy.totalWeight != 1.0) {
                throw new IllegalStateException("Total weight must be 1.0");
            }
            return weightedCostStrategy;
        }
    }
}
