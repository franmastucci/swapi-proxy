package com.swapi.app.proxy.controller.service;

import com.swapi.app.proxy.dto.FilmResponse;
import com.swapi.app.proxy.dto.PersonInfoResponse;
import com.swapi.app.proxy.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PersonService {

    @Autowired
    private SwapiProxyService swapiProxyService;

    @Autowired
    private StarshipService starshipService;

    @Autowired
    private VehicleService vehicleService;

    @Autowired
    private FilmService filmService;


    @Cacheable("person-info")
    public PersonInfoResponse getPersonInfo(String name) {

        PersonList personInfo =
                swapiProxyService.getPersonByName(name);

        if (!personInfo.getResults().isEmpty())
            return buildPersonInfoResponse(personInfo.getResults().get(0));

        else throw new RuntimeException("Not Found");

    }

    private PersonInfoResponse buildPersonInfoResponse(Person personInfo) {

        return PersonInfoResponse.builder()
                .name(personInfo.getName())
                .birthYear(personInfo.getBirth_year())
                .gender(personInfo.getGender())
                .planetName(getPlanetName(personInfo))
                .fastestVehicleDriven(getFastestVehicleDriven(personInfo))
                .films(getFilms(personInfo))
                .build();

    }

    private List<FilmResponse> getFilms(Person responseBody) {

        FilmList swapiFilmList = filmService.getFilms();

        List<FilmResponse> filmsInfoResponse = new ArrayList<>();

        responseBody.getFilms()
                .stream().forEach(f -> {
                    swapiFilmList.getResults().stream().forEach(g -> {
                        if(g.getUrl().equals(f)) {
                            filmsInfoResponse.add(FilmResponse.builder()
                                    .name(g.getTitle())
                                    .releaseDate(g.getRelease_date())
                                    .build());
                        }
                    });
                });

        return filmsInfoResponse;

    }

    private String getFastestVehicleDriven(Person personInfo) {

        Optional<Vehicle> vehicleWithMaxSpeed =
                vehicleService.calculateFastestTransportByPerson(personInfo);

        Optional<Starship> starshipWithMaxSpeed =
                starshipService.calculateFastestTransportByPerson(personInfo);

        return compareFastestSpeeds(vehicleWithMaxSpeed, starshipWithMaxSpeed);

    }

    private String compareFastestSpeeds(Optional<Vehicle> vehicleWithMaxSpeed, Optional<Starship> starshipWithMaxSpeed) {

        if (null != starshipWithMaxSpeed && null != vehicleWithMaxSpeed) {

            int starshipSpeed = Integer.parseInt(starshipWithMaxSpeed.get().getMax_atmosphering_speed());
            int vehicleSpeed = Integer.parseInt(vehicleWithMaxSpeed.get().getMax_atmosphering_speed());

            if (starshipSpeed > vehicleSpeed) return starshipWithMaxSpeed.get().getName();
            else return vehicleWithMaxSpeed.get().getName();

        } else if (null != starshipWithMaxSpeed) return starshipWithMaxSpeed.get().getName();

        else if (null != vehicleWithMaxSpeed) return vehicleWithMaxSpeed.get().getName();

        else return null;
    }

    private String getPlanetName(Person responseBody) {
        return swapiProxyService.getPlanet(responseBody.getHomeworld()).getName();
    }

}
