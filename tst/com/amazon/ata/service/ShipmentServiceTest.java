package com.amazon.ata.service;

import com.amazon.ata.cost.CostStrategy;
import com.amazon.ata.cost.MonetaryCostStrategy;
import com.amazon.ata.dao.PackagingDAO;
import com.amazon.ata.datastore.PackagingDatastore;
import com.amazon.ata.types.FulfillmentCenter;
import com.amazon.ata.types.Item;
import com.amazon.ata.types.ShipmentOption;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.InjectMocks;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.mockito.ArgumentMatchers.eq;


class ShipmentServiceTest {

    private Item smallItem = Item.builder()
            .withHeight(BigDecimal.valueOf(1))
            .withWidth(BigDecimal.valueOf(1))
            .withLength(BigDecimal.valueOf(1))
            .withAsin("abcde")
            .build();

    private Item largeItem = Item.builder()
            .withHeight(BigDecimal.valueOf(1000))
            .withWidth(BigDecimal.valueOf(1000))
            .withLength(BigDecimal.valueOf(1000))
            .withAsin("12345")
            .build();

    @Mock
    private FulfillmentCenter existentFC;
    @Mock
    private FulfillmentCenter nonExistentFC;
    @InjectMocks
    ShipmentService shipmentService;
    @Mock
    private PackagingDAO packagingDAO;
    @Mock
    private PackagingDatastore packagingDatastore;
    @Mock
    private MonetaryCostStrategy monetaryCostStrategy;
    @Mock
    private CostStrategy costStrategy;
    @Mock
    private ShipmentOption shipmentOption;
    @BeforeEach
    void setup(){
        initMocks(this);
        shipmentService = new ShipmentService(packagingDAO, costStrategy);
    }

//    private FulfillmentCenter existentFC = new FulfillmentCenter("ABE2");
//    private FulfillmentCenter nonExistentFC = new FulfillmentCenter("NonExistentFC");
//    private ShipmentService shipmentService = new ShipmentService(new PackagingDAO(new PackagingDatastore()),
//            new MonetaryCostStrategy());

//    @Test
//    void findBestShipmentOption_existentFCAndItemCanFit_returnsShipmentOption() {
//
//        // GIVEN & WHEN
//
//        when(shipmentService.findShipmentOption(smallItem, existentFC)).thenReturn(shipmentOption);
//
//        ShipmentOption shipmentOption = shipmentService.findShipmentOption(smallItem, existentFC);
//
//        //Need to find out what the result that i want to mock for?
//
//        // THEN
//        assertNotNull(shipmentOption);
//    }

    @Test
    void findBestShipmentOption_existentFCAndItemCannotFit_returnsShipmentOption() {
        // GIVEN & WHEN
        when(shipmentService.findShipmentOption(largeItem, existentFC)).thenReturn(null);

        ShipmentOption shipmentOption = shipmentService.findShipmentOption(largeItem, existentFC);
        // THEN
        assertNull(shipmentOption);
    }

    @Test
    void findBestShipmentOption_nonExistentFCAndItemCanFit_returnsShipmentOption() {
        // GIVEN & WHEN
        when(shipmentService.findShipmentOption(smallItem, nonExistentFC)).thenReturn(null);

        ShipmentOption shipmentOption = shipmentService.findShipmentOption(smallItem, nonExistentFC);
        // THEN
        assertNull(shipmentOption);
    }

    @Test
    void findBestShipmentOption_nonExistentFCAndItemCannotFit_returnsShipmentOption() {
        // GIVEN & WHEN
        when(shipmentService.findShipmentOption(largeItem, nonExistentFC)).thenReturn(null);

        ShipmentOption shipmentOption = shipmentService.findShipmentOption(largeItem, nonExistentFC);
        // THEN
        assertNull(shipmentOption);
    }
}