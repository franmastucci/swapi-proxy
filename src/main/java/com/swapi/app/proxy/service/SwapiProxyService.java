package com.swapi.app.proxy.service;

import com.swapi.app.proxy.dto.proxy.FilmResponse;
import com.swapi.app.proxy.dto.proxy.PersonInfoResponse;
import com.swapi.app.proxy.dto.swapi.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class SwapiProxyService {

    @Autowired
    private ContextService contextService;


    @Cacheable("person-info")
    public PersonInfoResponse getPersonInfo(String name) {

        PersonInfoApiResponse personInfo =
                getPersonInfoApiResponseEntity(name, contextService.getPeople());

        if (personInfo == null)
            throw new RuntimeException("Not Found");

        return buildPersonInfoResponse(personInfo);

    }

    private PersonInfoApiResponse getPersonInfoApiResponseEntity(String name, PersonInfoListApiResponse peopleList) {

        boolean haveNextPage = null != peopleList.getNext();

        PersonInfoApiResponse personInfo = peopleList.getResults().stream()
                .filter(p -> p.getName().equals(name))
                .findFirst()
                .orElse(null);

        if (personInfo == null && haveNextPage) {
            PersonInfoApiResponse recursiveResult =
                    getPersonInfoApiResponseEntity(name, contextService.getPeople(peopleList.getNext()));

            if (recursiveResult != null)
                return recursiveResult;

        }

        return personInfo;
    }
    private PersonInfoResponse buildPersonInfoResponse(PersonInfoApiResponse personInfo) {

        return PersonInfoResponse.builder()
                .name(personInfo.getName())
                .birthYear(personInfo.getBirth_year())
                .gender(personInfo.getGender())
                .planetName(getPlanetName(personInfo))
                .fastestVehicleDriven(getVehicleWithMaxSpeed(personInfo))
                .films(getFilms(personInfo))
                .build();

    }

    private List<FilmResponse> getFilms(PersonInfoApiResponse responseBody) {

        FilmListApiResponse swapiFilmList = contextService.getFilms();

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

    private String getVehicleWithMaxSpeed(PersonInfoApiResponse responseBody) {

        List<VehicleApiResponse> vehiclesInfo = contextService.getVehicles().getResults();

        List<VehicleApiResponse> filteredVehiclesInfo = new ArrayList<>();

        responseBody.getVehicles()
                .stream().forEach(v -> {

                    vehiclesInfo.stream().forEach(w -> {
                        if(w.getUrl().equals(v))
                            filteredVehiclesInfo.add(w);
                    });
                });

        Optional<VehicleApiResponse> vehicleWithMaxSpeed = filteredVehiclesInfo.stream()
                .max(Comparator.comparingInt(veh -> Integer.parseInt(veh.getMax_atmosphering_speed())));

        if(vehicleWithMaxSpeed.isEmpty()) return null;
        return vehicleWithMaxSpeed.get().getName();

    }

    private String getPlanetName(PersonInfoApiResponse responseBody) {

        return contextService.getPlanets().getResults().stream()
                .filter(p -> p.getUrl().equals(responseBody.getHomeworld()))
                .findFirst().get().getName();

    }

}
