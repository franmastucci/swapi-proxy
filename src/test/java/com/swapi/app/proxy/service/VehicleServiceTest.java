package com.swapi.app.proxy.service;


import com.swapi.app.proxy.entity.Person;
import com.swapi.app.proxy.entity.Vehicle;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class VehicleServiceTest {

    @Mock
    private SwapiProxyService swapiProxyService;

    private VehicleService vehicleService;


    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        vehicleService = new VehicleService(swapiProxyService);
    }

    @Test
    public void should_return_an_empty_list() {

        List<String> vehicles = new ArrayList<>();
        Person person = Person.builder().vehicles(vehicles).build();

        List<Vehicle> result = vehicleService.getTransportInfo(person);

        assertEquals(Collections.emptyList(), result);

    }

    @Test
    public void should_return_the_same_uri() {

        List<String> vehicles = new ArrayList<>();
        vehicles.add(TestConstants.VEHICLE_URL);
        Person person = Person.builder().vehicles(vehicles).build();

        when(swapiProxyService.getVehicle(TestConstants.VEHICLE_URL)).thenReturn(
                Vehicle.builder()
                        .url(TestConstants.VEHICLE_URL)
                        .build());

        List<Vehicle> result = vehicleService.getTransportInfo(person);

        assertEquals(vehicles.get(0), result.get(0).getUrl());

    }

}
