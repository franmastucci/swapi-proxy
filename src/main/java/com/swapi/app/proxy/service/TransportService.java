package com.swapi.app.proxy.service;

import com.swapi.app.proxy.entity.Person;
import com.swapi.app.proxy.entity.Transport;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public abstract class TransportService<T extends Transport> {

    protected abstract List<T> getTransportInfo(Person personInfo);

    public Optional<T> calculateFastestTransportByPerson(Person personInfo) {
        List<T> transportInfo = getTransportInfo(personInfo);
        Optional<T> transportWithMaxSpeed = null;

        if (!transportInfo.isEmpty()) {
            transportWithMaxSpeed = transportInfo.stream()
                    .max(Comparator.comparingInt(transport -> Integer.parseInt(transport.getMax_atmosphering_speed())));
        }

        return transportWithMaxSpeed;

    }

}