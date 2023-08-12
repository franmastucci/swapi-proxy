package com.swapi.app.proxy.service;

import com.swapi.app.proxy.entity.Planet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PlanetService {

    @Autowired
    private SwapiProxyService swapiProxyService;

    public Planet getPlanet(String url) {
        return swapiProxyService.getPlanet(url);
    }

}
