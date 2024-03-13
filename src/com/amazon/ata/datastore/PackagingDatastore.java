package com.amazon.ata.datastore;

import com.amazon.ata.types.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Stores all configured packaging pairs for all fulfillment centers.
 */
public class PackagingDatastore {

    private final List<FcPackagingOption> fcPackagingOptions;

    public PackagingDatastore() {
        fcPackagingOptions = new ArrayList<>();
        // Existing packaging options
        addUniqueFcPackagingOption("IND1", Material.CORRUGATE, "10", "10", "10");
        addUniqueFcPackagingOption("ABE2", Material.CORRUGATE, "20", "20", "20");
        addUniqueFcPackagingOption("ABE2", Material.CORRUGATE, "40", "40", "40");
        addUniqueFcPackagingOption("YOW4", Material.CORRUGATE, "10", "10", "10");
        addUniqueFcPackagingOption("YOW4", Material.CORRUGATE, "20", "20", "20");
        addUniqueFcPackagingOption("YOW4", Material.CORRUGATE, "60", "60", "60");
        addUniqueFcPackagingOption("IAD2", Material.CORRUGATE, "20", "20", "20");
        addUniqueFcPackagingOption("PDX1", Material.CORRUGATE, "40", "40", "40");
        addUniqueFcPackagingOption("PDX1", Material.CORRUGATE, "60", "60", "60");

        // New PolyBag options
        addPolyBagOption("IAD2", "5000");
        addPolyBagOption("YOW4", "2000");
        addPolyBagOption("YOW4", "5000");
        addPolyBagOption("YOW4", "10000");
        addPolyBagOption("IND1", "2000");
        addPolyBagOption("IND1", "5000");
        addPolyBagOption("ABE2", "2000");
        addPolyBagOption("ABE2", "6000");
        addPolyBagOption("PDX1", "5000");
        addPolyBagOption("PDX1", "10000");
        addPolyBagOption("YOW4", "5000");
    }

    private void addUniqueFcPackagingOption(String fcCode, Material material, String length, String width, String height) {
        FcPackagingOption option = createFcPackagingOption(fcCode, material, length, width, height);

        // Avoid adding duplicates by checking if the option already exists
        if (!fcPackagingOptions.contains(option)) {
            fcPackagingOptions.add(option);
        }
    }

    private void addPolyBagOption(String fcCode, String volume) {
        FcPackagingOption option = createPolyBagFcPackagingOption(fcCode, Material.LAMINATED_PLASTIC, volume);
        fcPackagingOptions.add(option);
    }

    private FcPackagingOption createFcPackagingOption(String fcCode, Material material, String length, String width, String height) {
        FulfillmentCenter fulfillmentCenter = new FulfillmentCenter(fcCode);
        Box box = new Box(new BigDecimal(length), new BigDecimal(width), new BigDecimal(height));
        return new FcPackagingOption(fulfillmentCenter, box);
    }

    private FcPackagingOption createPolyBagFcPackagingOption(String fcCode, Material material, String volume) {
        FulfillmentCenter fulfillmentCenter = new FulfillmentCenter(fcCode);
        PolyBag polyBag = new PolyBag(new BigDecimal(volume));
        return new FcPackagingOption(fulfillmentCenter, polyBag);
    }

    public List<FcPackagingOption> getFcPackagingOptions() {
        return fcPackagingOptions;
    }
}
