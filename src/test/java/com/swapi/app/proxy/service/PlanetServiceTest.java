package com.swapi.app.proxy.service;

import com.swapi.app.proxy.entity.Planet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PlanetServiceTest {

    @Mock
    private SwapiProxyService swapiProxyService;

    private PlanetService planetService;

    private final String PLANET_URL = "planet url";


    @BeforeEach
    public void setUp() {
        swapiProxyService = mock(SwapiProxyService.class);
        planetService = new PlanetService(swapiProxyService);
    }

    @Test
    public void should_return_a_planet() {

        Planet expectedPlanet = new Planet();
        when(swapiProxyService.getPlanet(PLANET_URL)).thenReturn(expectedPlanet);

        Planet result = planetService.getPlanet(PLANET_URL);

        assertEquals(expectedPlanet, result);
        verify(swapiProxyService).getPlanet(PLANET_URL);

    }

}