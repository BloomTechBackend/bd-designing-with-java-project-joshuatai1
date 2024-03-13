package com.amazon.ata.cost;

import com.amazon.ata.types.Material;
import com.amazon.ata.types.Packaging;
import com.amazon.ata.types.ShipmentCost;
import com.amazon.ata.types.ShipmentOption;

import java.math.BigDecimal;

public class CarbonCostStrategy implements CostStrategy{

    // Sustainability index for each material expressed in carbon units (cu) per gram
    private static final BigDecimal CORRUGATE_CARBON_INDEX = BigDecimal.valueOf(0.017);
    private static final BigDecimal LAMINATED_PLASTIC_CARBON_INDEX = BigDecimal.valueOf(0.00012);
    //TODO need fixing for POLYBAG TESTCASE
    @Override
    public ShipmentCost getCost(ShipmentOption shipmentOption) {
        Packaging packaging = shipmentOption.getPackaging();
        BigDecimal carbonCost = calculateCarbonCost(packaging);

        return new ShipmentCost(shipmentOption, carbonCost);
    }

    /**
     * Calculates the carbon cost in "carbon units" (cu) for the given packaging.
     *
     * @param packaging The packaging for which to calculate the carbon cost.
     * @return The carbon cost in "carbon units" (cu).
     */
    private BigDecimal calculateCarbonCost(Packaging packaging) {
        Material material = packaging.getMaterial();
        BigDecimal mass = packaging.getMass();

        BigDecimal carbonIndex;
        switch (material) {
            case CORRUGATE:
                carbonIndex = CORRUGATE_CARBON_INDEX;
                break;
            case LAMINATED_PLASTIC:
                carbonIndex = LAMINATED_PLASTIC_CARBON_INDEX;
                break;
            default:
                // Handle any other material types here
                carbonIndex = BigDecimal.ZERO; // Default to zero for unknown materials
                break;
        }

        // Calculate the carbon cost: mass * carbon index
        return mass.multiply(carbonIndex);
    }
}
