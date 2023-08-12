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
            throw new InternalProxyException("unexpected error getting data from Swapi " + e.getMessage());
        }

    }

}
