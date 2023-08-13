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

/**
 * <h1>Person Service</h1>
 * Service class responsible for retrieving information about a person from the Star Wars API.
 * This class utilizes various other services to gather information such as films, planets, starships,
 * and vehicles associated with the person.<p> <br>
 *
 * This service class interacts with the SWAPI (Star Wars API) proxy service to fetch data about a person.
 * It provides methods to retrieve a person's information, including their name, birth year, gender,
 * associated planet, fastest driven transport, and films they appeared in.</p> <br>
 *
 * @author Francisco Mastucci
 * @version 1.0
 * @since 2023-08-13
 */

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

    /**
     * Retrieves information about a person by their name.
     *
     * @param name The name of the person to retrieve information for.
     * @return The PersonInfoResponse containing information about the person.
     * @throws NotFoundException If the person is not found.
     */

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
                .fastestVehicleDriven(getFastestTransportDriven(personInfo))
                .films(getFilms(personInfo))
                .build();

    }

    /**
     * Load all films at once instead of making excessive REST requests to retrieve individual films.
     * Retrieves a list of FilmResponse objects based on the films associated with a person.
     *
     * @param personInfo The Person object containing information about the person.
     * @return A list of FilmResponse objects representing films associated with the person.
     * @throws InternalProxyException If an unexpected error occurs while matching films.
     */

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

    /**
     * Gets the name of the fastest transport (vehicles & starships) driven by the person.
     *
     * @param personInfo The person to determine the fastest vehicle driven.
     * @return The name of the fastest transport driven by the person, or null if no transport is found.
     * @throws InternalProxyException If an unexpected error occurs while comparing fastest transport speeds.
     */

    private String getFastestTransportDriven(Person personInfo) {

        Optional<Vehicle> vehicleWithMaxSpeed =
                vehicleService.calculateFastestTransportByPerson(personInfo);

        Optional<Starship> starshipWithMaxSpeed =
                starshipService.calculateFastestTransportByPerson(personInfo);

        return compareFastestSpeeds(vehicleWithMaxSpeed, starshipWithMaxSpeed);

    }

    /**
     * Compares the maximum atmosphering speeds of a vehicle and a starship and returns the name
     * of the faster transport. This method uses a stream to perform the comparison.
     *
     * @param vehicleWithMaxSpeed  Optional containing the vehicle with the maximum atmosphering speed, or null .
     * @param starshipWithMaxSpeed  Optional containing the starship with the maximum atmosphering speed, or null .
     * @return The name of the faster transport (vehicle or starship), or null if neither is available.
     * @throws InternalProxyException If an unexpected error occurs while comparing the fastest transport speeds.
     */

    private String compareFastestSpeeds(Optional<Vehicle> vehicleWithMaxSpeed,
                                        Optional<Starship> starshipWithMaxSpeed) {

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
