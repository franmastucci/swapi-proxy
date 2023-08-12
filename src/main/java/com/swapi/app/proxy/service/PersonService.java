package com.swapi.app.proxy.service;

import com.swapi.app.proxy.dto.FilmResponse;
import com.swapi.app.proxy.dto.PersonInfoResponse;
import com.swapi.app.proxy.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

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

    @Autowired
    private PlanetService planetService;


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

    private List<FilmResponse> getFilms(Person personInfo) {

        FilmList swapiFilmList = filmService.getFilms();

        List<FilmResponse> filmsInfoResponse = new ArrayList<>();

        personInfo.getFilms()
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

        vehicleWithMaxSpeed = vehicleWithMaxSpeed == null
                ? Optional.of(Vehicle.builder().max_atmosphering_speed("0").build())
                : vehicleWithMaxSpeed;

        starshipWithMaxSpeed = starshipWithMaxSpeed == null
                ? Optional.of(Starship.builder().max_atmosphering_speed("0").build())
                : starshipWithMaxSpeed;

        return Stream.of(starshipWithMaxSpeed, vehicleWithMaxSpeed)
                .filter(Optional::isPresent)
                .max(Comparator.comparingInt(transport ->
                        Integer.parseInt(transport.get().getMax_atmosphering_speed())))
                .map(Optional::get)
                .map(transport -> transport.getName())
                .orElse(null);

    }

    private String getPlanetName(Person personInfo) {
        return planetService.getPlanet(personInfo.getHomeworld()).getName();
    }

}
