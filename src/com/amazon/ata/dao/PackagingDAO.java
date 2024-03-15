package com.amazon.ata.dao;

import com.amazon.ata.datastore.PackagingDatastore;
import com.amazon.ata.exceptions.NoPackagingFitsItemException;
import com.amazon.ata.exceptions.UnknownFulfillmentCenterException;
import com.amazon.ata.types.FcPackagingOption;
import com.amazon.ata.types.FulfillmentCenter;
import com.amazon.ata.types.Item;
import com.amazon.ata.types.Packaging;
import com.amazon.ata.types.ShipmentOption;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * Access data for which packaging is available at which fulfillment center.
 */
public class PackagingDAO {
    private Map<FulfillmentCenter, Set<FcPackagingOption>> fcPackagingOptionsMap;

    /**
     * PackagingDAO.
     * @param datastore something.
     */
    public PackagingDAO(PackagingDatastore datastore) {
        this.fcPackagingOptionsMap = new HashMap<>();
        for (FcPackagingOption fcPackagingOption : datastore.getFcPackagingOptions()) {
            FulfillmentCenter fc = fcPackagingOption.getFulfillmentCenter();
            fcPackagingOptionsMap.computeIfAbsent(fc, k -> new HashSet<>()).add(fcPackagingOption);
        }
    }

    /**
     * findShipmentOptions.
     * @param item something.
     * @param fulfillmentCenter something.
     * @return something.
     * @throws UnknownFulfillmentCenterException something.
     * @throws NoPackagingFitsItemException something.
     */
    public List<ShipmentOption> findShipmentOptions(Item item, FulfillmentCenter fulfillmentCenter)
            throws UnknownFulfillmentCenterException, NoPackagingFitsItemException {

        if (!fcPackagingOptionsMap.containsKey(fulfillmentCenter)) {
            throw new UnknownFulfillmentCenterException(
                    String.format("Unknown FC: %s!", fulfillmentCenter.getFcCode()));
        }

        Set<FcPackagingOption> options = fcPackagingOptionsMap.get(fulfillmentCenter);
        List<ShipmentOption> result = new ArrayList<>();

        for (FcPackagingOption fcPackagingOption : options) {
            Packaging packaging = fcPackagingOption.getPackaging();

            if (packaging.canFitItem(item)) {
                ShipmentOption shipmentOption = ShipmentOption.builder()
                        .withItem(item)
                        .withPackaging(packaging)
                        .withFulfillmentCenter(fulfillmentCenter)
                        .build();

                // Check if the shipment option is unique before adding it to the result
                if (!result.contains(shipmentOption)) {
                    result.add(shipmentOption);
                }
            }
        }

        if (result.isEmpty()) {
            throw new NoPackagingFitsItemException(
                    String.format("No packaging at %s fits %s!", fulfillmentCenter.getFcCode(), item));
        }

        return result;
    }
}
