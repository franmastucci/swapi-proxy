package com.swapi.app.proxy.service;

import com.swapi.app.proxy.entity.Person;
import com.swapi.app.proxy.entity.Starship;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class StarshipService extends TransportService<Starship> {

    @Autowired
    private SwapiProxyService swapiProxyService;

    @Override
    protected List<Starship> getTransportInfo(Person personInfo) {

        List<Starship> starshipInfo = new ArrayList<>();

        if (!personInfo.getStarships().isEmpty()) {
            personInfo.getStarships().forEach(s -> {
                starshipInfo.add(swapiProxyService.getStarship(s));
            });
        }

        return starshipInfo;
    }

}