package com.swapi.app.proxy.controller.service;

import com.swapi.app.proxy.entity.Person;
import com.swapi.app.proxy.entity.Vehicle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class VehicleService extends TransportService<Vehicle> {

    @Autowired
    private SwapiProxyService swapiProxyService;

    @Override
    protected List<Vehicle> getTransportInfo(Person personInfo) {
        List<Vehicle> vehiclesInfo = new ArrayList<>();

        if (!personInfo.getVehicles().isEmpty()) {
            personInfo.getVehicles().forEach(v -> {
                vehiclesInfo.add(swapiProxyService.getVehicle(v));
            });
        }

        return vehiclesInfo;
    }
}
