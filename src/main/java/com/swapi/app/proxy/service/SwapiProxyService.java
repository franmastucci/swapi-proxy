package com.swapi.app.proxy.service;

import com.swapi.app.proxy.entity.*;
import com.swapi.app.proxy.exception.InternalProxyException;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * <h1>SWAPI Proxy Service</h1>
 *
 * <p>The SWAPI Proxy Service acts as an intermediary for consuming the SWAPI (Star Wars API),
 * providing an abstraction layer to access different types of data related to the
 * Star Wars universe. </p>
 *
 * <br><p>This service offers methods to retrieve information about movies, characters, planets, starships,
 * and vehicles from the Star Wars saga.</p>
 *
 * <p>It uses WebClient to make requests to the SWAPI and handles caching to improve performance.
 * Cached data is stored under the following cache names: "films-swapi", "person-by-name-swapi",
 * "planet-swapi", "starship-swapi", and "vehicle-swapi". Caching helps reduce the number of
 * API requests and enhances responsiveness. </p>
 *
 * <br><p>To use this service, simply inject an instance of {@code SwapiProxyService} and use
 * the provided methods to retrieve the data you need from the Star Wars universe.</p> <br>
 *
 * @author Francisco Mastucci
 * @version 1.0
 * @since 2023-08-12
 */

@Service
@Log4j2
public class SwapiProxyService {

    @Autowired
    private WebClient webClient;

    @Value("${external-api-swapi-films}")
    private String swapiFilmsUrl;

    @Value("${external-api-swapi-people-by-name}")
    private String swapiPeopleByIdUrl;


    @Cacheable("films-swapi")
    public FilmList getFilms() {
        return getDataFromSwapi(swapiFilmsUrl, FilmList.class).getBody();
    }

    @Cacheable("person-by-name-swapi")
    public PersonList getPersonByName(String name) {
        return getDataFromSwapi(swapiPeopleByIdUrl + name, PersonList.class).getBody();
    }

    @Cacheable("planet-swapi")
    public Planet getPlanet(String url) {
        return getDataFromSwapi(url, Planet.class).getBody();
    }

    @Cacheable("starship-swapi")
    public Starship getStarship(String url) {
        return getDataFromSwapi(url, Starship.class).getBody();
    }

    @Cacheable("vehicle-swapi")
    public Vehicle getVehicle(String url) {
        return getDataFromSwapi(url, Vehicle.class).getBody();
    }

    public <T> ResponseEntity<T> getDataFromSwapi(String uri, Class<T> responseType) {

        try {
            return webClient.get()
                    .uri(uri)
                    .retrieve()
                    .toEntity(responseType)
                    .block();

        } catch (Exception e) {
            log.error("unexpected error getting data from Swapi. Stack Trace: " + e.getStackTrace());
            throw new InternalProxyException("unexpected error getting data from Swapi");
        }

    }

}