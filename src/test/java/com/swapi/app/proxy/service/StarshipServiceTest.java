package com.swapi.app.proxy.service;

import com.swapi.app.proxy.entity.Person;
import com.swapi.app.proxy.entity.Starship;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class StarshipServiceTest {

    @Mock
    private SwapiProxyService swapiProxyService;

    private StarshipService starshipService;


    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        starshipService = new StarshipService(swapiProxyService);
    }

    @Test
    public void should_return_an_empty_list() {

        List<String> starships = new ArrayList<>();
        Person person = Person.builder().starships(starships).build();

        List<Starship> result = starshipService.getTransportInfo(person);

        assertEquals(Collections.emptyList(), result);

    }

    @Test
    public void should_return_the_same_uri() {

        List<String> starships = List.of(TestConstants.STARSHIP_URL);
        Person person = Person.builder().starships(starships).build();

        when(swapiProxyService.getStarship(TestConstants.STARSHIP_URL))
                .thenReturn(TestUtils.getStarship());

        List<Starship> result = starshipService.getTransportInfo(person);

        assertEquals(starships.get(0), result.get(0).getUrl());

    }

}

