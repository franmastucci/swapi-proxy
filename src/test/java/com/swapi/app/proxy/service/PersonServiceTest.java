package com.swapi.app.proxy.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.swapi.app.proxy.dto.*;
import com.swapi.app.proxy.entity.*;
import com.swapi.app.proxy.exception.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class PersonServiceTest {

    @Mock
    private SwapiProxyService swapiProxyService;

    private StarshipService starshipService;
    private VehicleService vehicleService;
    private FilmService filmService;
    private PlanetService planetService;
    private PersonService personService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        filmService = mock(FilmService.class);
        planetService = mock(PlanetService.class);
        starshipService = mock(StarshipService.class);
        vehicleService = mock(VehicleService.class);
        personService = new PersonService(
                swapiProxyService,
                starshipService,
                vehicleService,
                filmService,
                planetService
        );
    }
    @Test
    public void testGetPersonInfo_PersonFound() {

        PersonList personList = new PersonList();
        List<String> vehicles = new ArrayList<>();
        List<String> filmList = new ArrayList<>();
        filmList.add("filmUri");

        Person person = Person.builder()
                .name("Luke Skywalker")
                .birth_year("19BBY")
                .gender("male")
                .vehicles(vehicles)
                .films(filmList)
                .homeworld("planetUri")
                .build();

        personList.setResults(Collections.singletonList(person));

        when(swapiProxyService.getPersonByName("Luke Skywalker")).thenReturn(personList);

        Planet planet = new Planet();
        planet.setName("Tatooine");
        when(planetService.getPlanet("planetUri")).thenReturn(planet);


        when(swapiProxyService.getVehicle("vehicleUri")).thenReturn(
                Vehicle.builder()
                        .url("VEHICLE_URL")
                        .build());

        when(vehicleService.calculateFastestTransportByPerson(person)).thenReturn(null);
        when(starshipService.calculateFastestTransportByPerson(person)).thenReturn(null);

        FilmList filmObjectList = new FilmList();
        Film film = new Film();
        film.setTitle("A New Hope");
        film.setRelease_date("1977-05-25");
        film.setUrl("filmUri");
        filmObjectList.setResults(Collections.singletonList(film));
        when(filmService.getFilms()).thenReturn(filmObjectList);


        PersonInfoResponse result = personService.getPersonInfo("Luke Skywalker");

        assertEquals("Luke Skywalker", result.getName());
        assertEquals("19BBY", result.getBirthYear());
        assertEquals("male", result.getGender());
        assertEquals("Tatooine", result.getPlanetName());
        assertEquals("A New Hope", result.getFilms().get(0).getName());
        assertEquals("1977-05-25", result.getFilms().get(0).getReleaseDate());
        assertNull(result.getFastestVehicleDriven());
    }

    @Test
    public void should_throw_not_found_exception() {

        when(swapiProxyService.getPersonByName("Unknown Person")).
                thenReturn(PersonList.builder()
                        .results(new ArrayList<Person>())
                        .build());

        assertThrows(NotFoundException.class,
                () -> personService.getPersonInfo("Unknown Person"));

    }

}
