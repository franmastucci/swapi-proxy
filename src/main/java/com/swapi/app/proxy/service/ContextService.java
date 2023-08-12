package com.swapi.app.proxy.service;


import com.swapi.app.proxy.dto.swapi.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class ContextService {

    @Autowired
    private WebClient webClient;

    @Value("${external-api-swapi-films}")
    private String swapiFilmsUrl;

    @Value("${external-api-swapi-people-by-name}")
    private String swapiPeopleByIdUrl;


    @Cacheable("films-swapi")
    public FilmListApiResponse getFilms() {
        return getInfoFromSwapi(swapiFilmsUrl, FilmListApiResponse.class).getBody();
    }

    @Cacheable("people-by-name-swapi")
    public PersonInfoListApiResponse getPeopleByName(String name) {
        return getInfoFromSwapi(swapiPeopleByIdUrl + name, PersonInfoListApiResponse.class).getBody();
    }

    @Cacheable("planet-swapi")
    public PlanetApiResponse getPlanet(String url) {
        return getInfoFromSwapi(url, PlanetApiResponse.class).getBody();
    }

    @Cacheable("starship-swapi")
    public StarshipApiResponse getStarship(String url) {
        return getInfoFromSwapi(url, StarshipApiResponse.class).getBody();
    }

    @Cacheable("vehicle-swapi")
    public VehicleApiResponse getVehicle(String url) {
        return getInfoFromSwapi(url, VehicleApiResponse.class).getBody();
    }

    public <T> ResponseEntity<T> getInfoFromSwapi(String uri, Class<T> responseType) {
        return webClient.get()
                .uri(uri)
                .retrieve()
                .toEntity(responseType)
                .block();
    }

}
