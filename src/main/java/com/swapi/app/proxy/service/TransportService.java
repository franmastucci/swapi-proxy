package com.swapi.app.proxy.service;

import com.swapi.app.proxy.entity.Person;
import com.swapi.app.proxy.entity.Transport;
import com.swapi.app.proxy.exception.InternalProxyException;
import lombok.extern.log4j.Log4j2;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

/**
 * <h1>Transport Service</h1>
 * <p>Abstract class that serves as a template method for processing information about vehicles and starships.
 * Classes implementing this template are {@link StarshipService} and {@link VehicleService}.</p> <br>
 * <p>It provides a common structure for calculating the fastest transport based on a person's information.</p> <br>
 *
 * @param <T> The type of transport entity.
 * @author Francisco Mastucci
 * @version 1.0
 * @since 2023-08-12
 */

@Log4j2
public abstract class TransportService<T extends Transport> {

    protected abstract List<T> getTransportInfo(Person personInfo);

    public Optional<T> calculateFastestTransportByPerson(Person personInfo) {

        try{
            List<T> transportInfo = getTransportInfo(personInfo);
            Optional<T> transportWithMaxSpeed = null;

            if (!transportInfo.isEmpty()) {
                transportWithMaxSpeed = transportInfo.stream()
                        .max(Comparator.comparingInt(transport ->
                                Integer.parseInt(transport.getMax_atmosphering_speed())));
            }

            return transportWithMaxSpeed;

        } catch (Exception e) {
            log.error("unexpected error calculating the fastest transport. Stack trace: {}", e.getStackTrace());
            throw new InternalProxyException("unexpected error calculating the fastest transport");
        }

    }

}