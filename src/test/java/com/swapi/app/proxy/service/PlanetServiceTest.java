package com.swapi.app.proxy.service;

import com.swapi.app.proxy.entity.Planet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PlanetServiceTest {

    @Mock
    private SwapiProxyService swapiProxyService;

    private PlanetService planetService;


    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        planetService = new PlanetService(swapiProxyService);
    }

    @Test
    public void should_return_a_planet() {

        Planet expectedPlanet = TestUtils.getPlanet();
        when(swapiProxyService.getPlanet(TestConstants.PLANET_URL))
                .thenReturn(expectedPlanet);

        Planet result = planetService.getPlanet(TestConstants.PLANET_URL);

        assertEquals(expectedPlanet, result);
        verify(swapiProxyService).getPlanet(TestConstants.PLANET_URL);

    }

}