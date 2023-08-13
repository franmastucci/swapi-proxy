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

public class PersonServiceTest {

    private PersonService personService;

    @Mock
    private SwapiProxyService swapiProxyService;

    @Mock
    private StarshipService starshipService;

    @Mock
    private VehicleService vehicleService;

    @Mock
    private FilmService filmService;

    @Mock
    private PlanetService planetService;


    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        personService = new PersonService(
                swapiProxyService,
                starshipService,
                vehicleService,
                filmService,
                planetService
        );
    }
    @Test
    public void should_return_a_person_info_response() {

        Person person = TestUtils.getPerson();

        PersonList personList = TestUtils.getPersonList(person);

        Planet planet = TestUtils.getPlanet();

        FilmList filmObjectList = TestUtils.getFilmList();

        when(swapiProxyService.getPersonByName(TestConstants.PERSON_NAME))
                .thenReturn(personList);

        when(planetService.getPlanet(TestConstants.PLANET_URL))
                .thenReturn(planet);

        when(swapiProxyService.getVehicle(TestConstants.VEHICLE_URL))
                .thenReturn(Vehicle.builder().url(TestConstants.VEHICLE_URL).build());

        when(vehicleService.calculateFastestTransportByPerson(person))
                .thenReturn(null);

        when(starshipService.calculateFastestTransportByPerson(person))
                .thenReturn(null);

        when(filmService.getFilms())
                .thenReturn(filmObjectList);

        PersonInfoResponse result =
                personService.getPersonInfo(TestConstants.PERSON_NAME);

        assertEquals(TestConstants.PERSON_NAME, result.getName());
        assertEquals(TestConstants.BIRTH_YEAR, result.getBirthYear());
        assertEquals(TestConstants.GENDER, result.getGender());
        assertEquals(TestConstants.PLANET_NAME, result.getPlanetName());
        assertEquals(TestConstants.FILM_NAME, result.getFilms().get(0).getName());
        assertEquals(TestConstants.RELEASE_DATE, result.getFilms().get(0).getReleaseDate());

        assertNull(result.getFastestVehicleDriven());

    }


    @Test
    public void should_throw_not_found_exception() {

        when(swapiProxyService.getPersonByName(TestConstants.UNKNOWN_PERSON)).
                thenReturn(PersonList.builder()
                        .results(new ArrayList<Person>())
                        .build());

        assertThrows(NotFoundException.class,
                () -> personService.getPersonInfo("Unknown Person"));

    }

}
