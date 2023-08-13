package com.swapi.app.proxy.service;

import com.swapi.app.proxy.entity.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TestUtils {

    public static PersonList getPersonList(Person person) {
        return PersonList.builder()
                .results(Collections.singletonList(person))
                .build();
    }

    public static Person getPerson() {
        return Person.builder()
                .name(TestConstants.PERSON_NAME)
                .birth_year(TestConstants.BIRTH_YEAR)
                .gender(TestConstants.GENDER)
                .vehicles(new ArrayList<>())
                .films(getfilmStringList())
                .homeworld(TestConstants.PLANET_URL)
                .build();
    }

    private static List<String> getfilmStringList() {
        return List.of(TestConstants.FILM_URL);
    }

    public static Planet getPlanet() {
       return  Planet.builder()
               .name(TestConstants.PLANET_NAME)
               .build();
    }

    public static FilmList getFilmList() {
        return  FilmList.builder()
                .results(Collections.singletonList(getFilm()))
                .build();
    }

    private static Film getFilm() {
        return Film.builder()
                .title(TestConstants.FILM_NAME)
                .release_date(TestConstants.RELEASE_DATE)
                .url(TestConstants.FILM_URL)
                .build();
    }

    public static Vehicle getVehicle() {
        return Vehicle.builder()
                .url(TestConstants.VEHICLE_URL)
                .build();
    }

    public static Starship getStarship() {
        return Starship.builder()
                .url(TestConstants.STARSHIP_URL)
                .build();
    }

}
