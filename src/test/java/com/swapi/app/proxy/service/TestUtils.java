package com.swapi.app.proxy.service;

import com.swapi.app.proxy.entity.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TestUtils {

    public static PersonList getPersonList(Person person) {
        PersonList personList = new PersonList();
        personList.setResults(Collections.singletonList(person));

        return personList;
    }

    public static Person getPerson() {
        List<String> filmList = getfilmStringList();

        Person person = Person.builder()
                .name(TestConstants.PERSON_NAME)
                .birth_year(TestConstants.BIRTH_YEAR)
                .gender(TestConstants.GENDER)
                .vehicles(new ArrayList<>())
                .films(filmList)
                .homeworld(TestConstants.PLANET_URL)
                .build();

        return person;
    }

    private static List<String> getfilmStringList() {
        List<String> filmList = new ArrayList<>();
        filmList.add(TestConstants.FILM_URL);
        return filmList;
    }

    public static Planet getPlanet() {
        Planet planet = Planet.builder()
                        .name(TestConstants.PLANET_NAME).build();
        return planet;
    }

    public static FilmList getFilmList() {
        FilmList filmObjectList = new FilmList();
        Film film = Film.builder()
                .title(TestConstants.FILM_NAME)
                .release_date(TestConstants.RELEASE_DATE)
                .url(TestConstants.FILM_URL)
                .build();

        filmObjectList.setResults(Collections.singletonList(film));

        return filmObjectList;
    }

}
