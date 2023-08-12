package com.swapi.app.proxy.service;

import com.swapi.app.proxy.dto.FilmResponse;
import com.swapi.app.proxy.dto.PersonInfoResponse;
import com.swapi.app.proxy.entity.*;
import com.swapi.app.proxy.exception.InternalProxyException;
import com.swapi.app.proxy.exception.NotFoundException;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Service
@Log4j2
public class PersonService {

    private final SwapiProxyService swapiProxyService;
    private final StarshipService starshipService;
    private final VehicleService vehicleService;
    private final FilmService filmService;
    private final PlanetService planetService;

    @Autowired
    public PersonService(
            SwapiProxyService swapiProxyService,
            StarshipService starshipService,
            VehicleService vehicleService,
            FilmService filmService,
            PlanetService planetService
    ) {
        this.swapiProxyService = swapiProxyService;
        this.starshipService = starshipService;
        this.vehicleService = vehicleService;
        this.filmService = filmService;
        this.planetService = planetService;
    }


    @Cacheable("person-info")
    public PersonInfoResponse getPersonInfo(String name) {

        PersonList personInfo =
                swapiProxyService.getPersonByName(name);

        if (!personInfo.getResults().isEmpty())
            return buildPersonInfoResponse(personInfo.getResults().get(0));

        else throw new NotFoundException("Person Not Found");

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

        try {
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

        } catch (Exception e) {
            log.error("unexpected error matching films. Stack Trace: " + e.getStackTrace());
            throw new InternalProxyException("unexpected error matching films");
        }

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

        try{
            return Stream.of(starshipWithMaxSpeed, vehicleWithMaxSpeed)
                    .filter(Optional::isPresent)
                    .max(Comparator.comparingInt(transport ->
                            Integer.parseInt(transport.get().getMax_atmosphering_speed())))
                    .map(Optional::get)
                    .map(transport -> transport.getName())
                    .orElse(null);

        } catch (Exception e) {
            log.error("unexpected error comparing fastest transport speeds");
            throw new InternalProxyException("unexpected error comparing fastest transports");
        }

    }

    private String getPlanetName(Person personInfo) {
        return planetService.getPlanet(personInfo.getHomeworld()).getName();
    }

}
