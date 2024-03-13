package com.amazon.ata.dao;

import com.amazon.ata.datastore.PackagingDatastore;
import com.amazon.ata.exceptions.NoPackagingFitsItemException;
import com.amazon.ata.exceptions.UnknownFulfillmentCenterException;
import com.amazon.ata.types.FcPackagingOption;
import com.amazon.ata.types.FulfillmentCenter;
import com.amazon.ata.types.Item;
import com.amazon.ata.types.Packaging;
import com.amazon.ata.types.ShipmentOption;

import java.util.*;

/**
 * Access data for which packaging is available at which fulfillment center.
 */
public class PackagingDAO {
    private Map<FulfillmentCenter, Set<FcPackagingOption>> fcPackagingOptionsMap;


    public PackagingDAO(PackagingDatastore datastore) {
        this.fcPackagingOptionsMap = new HashMap<>();
        for (FcPackagingOption fcPackagingOption : datastore.getFcPackagingOptions()) {
            FulfillmentCenter fc = fcPackagingOption.getFulfillmentCenter();
            fcPackagingOptionsMap.computeIfAbsent(fc, k -> new HashSet<>()).add(fcPackagingOption);
        }
    }

    public List<ShipmentOption> findShipmentOptions(Item item, FulfillmentCenter fulfillmentCenter)
            throws UnknownFulfillmentCenterException, NoPackagingFitsItemException {

        if (!fcPackagingOptionsMap.containsKey(fulfillmentCenter)) {
            throw new UnknownFulfillmentCenterException(
                    String.format("Unknown FC: %s!", fulfillmentCenter.getFcCode()));
        }

        Set<FcPackagingOption> options = fcPackagingOptionsMap.get(fulfillmentCenter);
        Set<Packaging> uniquePackagings = new HashSet<>();  // Use a set to filter out duplicates
        List<ShipmentOption> result = new ArrayList<>();

        for (FcPackagingOption fcPackagingOption : options) {
            Packaging packaging = fcPackagingOption.getPackaging();

            if (packaging.canFitItem(item) && uniquePackagings.add(packaging)) {
                // Only add to result if the packaging was successfully added to the set (i.e., it's not a duplicate)
                result.add(ShipmentOption.builder()
                        .withItem(item)
                        .withPackaging(packaging)
                        .withFulfillmentCenter(fulfillmentCenter)
                        .build());
            }
        }

        if (result.isEmpty()) {
            throw new NoPackagingFitsItemException(
                    String.format("No packaging at %s fits %s!", fulfillmentCenter.getFcCode(), item));
        }

        return result;
    }
}
