package com.amazon.ata.dao;

import com.amazon.ata.datastore.PackagingDatastore;
import com.amazon.ata.exceptions.NoPackagingFitsItemException;
import com.amazon.ata.exceptions.UnknownFulfillmentCenterException;
import com.amazon.ata.types.*;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PackagingDAOTest {

    private Item testItem = createItem("30", "30", "30");
    private Item smallItem = createItem("5", "5", "5");

    private FulfillmentCenter ind1 = new FulfillmentCenter("IND1");
    private FulfillmentCenter abe2 = new FulfillmentCenter("ABE2");
    private FulfillmentCenter iad2 = new FulfillmentCenter("IAD2");

    private PackagingDatastore datastore = new PackagingDatastore();

    private PackagingDAO packagingDAO;

    @Test
    public void findShipmentOptions_unknownFulfillmentCenter_throwsUnknownFulfillmentCenterException() {
        // GIVEN
        packagingDAO = new PackagingDAO(datastore);
        FulfillmentCenter fulfillmentCenter = new FulfillmentCenter("nonExistentFcCode");

        // WHEN + THEN
        assertThrows(UnknownFulfillmentCenterException.class, () -> {
            packagingDAO.findShipmentOptions(testItem, fulfillmentCenter);
        }, "When asked to ship from an unknown fulfillment center, throw UnknownFulfillmentCenterException.");
    }

    @Test
    public void findShipmentOptions_packagingDoesntFit_throwsNoPackagingFitsItemException() {
        // GIVEN
        packagingDAO = new PackagingDAO(datastore);

        // WHEN + THEN
        assertThrows(NoPackagingFitsItemException.class, () -> {
            packagingDAO.findShipmentOptions(testItem, ind1);
        }, "When no packaging can fit the item, throw NoPackagingFitsItemException.");
    }

    @Test
    public void findShipmentOptions_onePackagingAvailableAndFits_singlePackaging() throws Exception {
        // GIVEN
        packagingDAO = new PackagingDAO(datastore);

        // WHEN
        List<ShipmentOption> shipmentOptions = packagingDAO.findShipmentOptions(smallItem, ind1);

        // THEN
        assertEquals(1, shipmentOptions.size(),
            "When fulfillment center has packaging that can fit item, return a ShipmentOption with the item, "
                + "fulfillment center, and packaging that can fit the item.");
    }

    @Test
    public void findShipmentOptions_twoPackagingAvailableAndOneFits_singlePackaging() throws Exception {
        // GIVEN
        packagingDAO = new PackagingDAO(datastore);

        // WHEN
        List<ShipmentOption> shipmentOptions = packagingDAO.findShipmentOptions(testItem, abe2);

        // THEN
        assertEquals(1, shipmentOptions.size(),
            "When fulfillment center has packaging that can fit item, return a ShipmentOption with the item, "
                + "fulfillment center, and packaging that can fit the item.");
    }

    @Test
    public void findShipmentOptions_twoPackagingAvailableAndBothFit_twoPackagingOptions() throws Exception {
        // GIVEN
        packagingDAO = new PackagingDAO(datastore);

        // WHEN
        List<ShipmentOption> shipmentOptions = packagingDAO.findShipmentOptions(smallItem, abe2);

        // THEN
        assertEquals(2, shipmentOptions.size(),
            "When fulfillment center has multiple packaging that can fit item, return a ShipmentOption "
                + "for each.");
    }

    private Item createItem(String length, String width, String height) {
        return Item.builder()
                .withAsin("B00TEST")
                .withDescription("Test Item")
                .withHeight(new BigDecimal(length))
                .withWidth(new BigDecimal(width))
                .withLength(new BigDecimal(height))
                .build();
    }

//    @Test
//    public void findShipmentOptions_duplicatePackagingOptionsForIAD2_returnSingleOption() throws Exception {
//        // GIVEN
//        // Create a PackagingDatastore with data containing duplicate packaging options for IAD2
//        PackagingDatastore datastore = new PackagingDatastore();
//        // Intentionally adding a duplicate for testing purposes
//        datastore.addFcPackagingOption("IAD2", Material.CORRUGATE, "20", "20", "20");
//
//        packagingDAO = new PackagingDAO(datastore);
//
//        // WHEN
//        // Find shipment options for an item at IAD2
//        List<ShipmentOption> shipmentOptions = packagingDAO.findShipmentOptions(smallItem, iad2);
//
//        // THEN
//        // Assert that only one option is returned despite the duplicates in the datastore
//        assertEquals(1, shipmentOptions.size(),
//                "When fulfillment center has duplicate packaging options, only one should be returned.");
//    }

//    // Mock PackagingDatastore class with duplicate packaging options for IAD2 for testing purposes
//    private static class MockPackagingDatastoreWithDuplicates extends PackagingDatastore {
//        @Override
//        public List<FcPackagingOption> getFcPackagingOptions() {
//            // Mock data for IAD2 with duplicate packaging options
//            FulfillmentCenter iad2 = new FulfillmentCenter("IAD2");
//
//            // Create duplicate packaging options for IAD2
//            Box box = new Box(new BigDecimal("20"), new BigDecimal("20"), new BigDecimal("20"));
//            PolyBag polyBag = new PolyBag(new BigDecimal("10000"));
//
//            // Create FcPackagingOption instances
//            FcPackagingOption option1 = new FcPackagingOption(iad2, box);
//            FcPackagingOption option2 = new FcPackagingOption(iad2, polyBag);
//
//            return List.of(option1, option2);
//        }
//    }
}
