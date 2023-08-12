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

        PersonInfoListApiResponse personInfo =
                contextService.getPeopleByName(name);

        if (!personInfo.getResults().isEmpty())
            return buildPersonInfoResponse(personInfo.getResults().get(0));

        else throw new RuntimeException("Not Found");

    }

    private PersonInfoResponse buildPersonInfoResponse(PersonInfoApiResponse personInfo) {

        return PersonInfoResponse.builder()
                .name(personInfo.getName())
                .birthYear(personInfo.getBirth_year())
                .gender(personInfo.getGender())
                .planetName(getPlanetName(personInfo))
                .fastestVehicleDriven(getFastestVehicleDriven(personInfo))
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

    private String getFastestVehicleDriven(PersonInfoApiResponse personInfo) {

        Optional<VehicleApiResponse> vehicleWithMaxSpeed = calculateFastestVehicleDriven(personInfo);

        Optional<StarshipApiResponse> starshipWithMaxSpeed = calculateFastestStarshipDriven(personInfo);

        return compareFastestSpeeds(vehicleWithMaxSpeed, starshipWithMaxSpeed);

    }

    private Optional<StarshipApiResponse> calculateFastestStarshipDriven(PersonInfoApiResponse personInfo) {

        List<StarshipApiResponse> starshipInfo = new ArrayList<>();
        Optional<StarshipApiResponse> starshipWithMaxSpeed = null;

        if(!personInfo.getStarships().isEmpty()) {
            personInfo.getStarships()
                    .stream().forEach(s -> {
                        starshipInfo.add(contextService.getStarship(s));
                    });

            starshipWithMaxSpeed = starshipInfo.stream()
                    .max(Comparator.comparingInt(sta -> Integer.parseInt(sta.getMax_atmosphering_speed())));

        }
        return starshipWithMaxSpeed;
    }

    private Optional<VehicleApiResponse> calculateFastestVehicleDriven(PersonInfoApiResponse personInfo) {

        List<VehicleApiResponse> vehiclesInfo = new ArrayList<>();
        Optional<VehicleApiResponse> vehicleWithMaxSpeed = null;

        if(!personInfo.getVehicles().isEmpty()) {
            personInfo.getVehicles()
                    .stream().forEach(v -> {
                        vehiclesInfo.add(contextService.getVehicle(v));
                    });

            vehicleWithMaxSpeed = vehiclesInfo.stream()
                    .max(Comparator.comparingInt(veh -> Integer.parseInt(veh.getMax_atmosphering_speed())));
        }
        return vehicleWithMaxSpeed;
    }

    private String compareFastestSpeeds(Optional<VehicleApiResponse> vehicleWithMaxSpeed, Optional<StarshipApiResponse> starshipWithMaxSpeed) {

        if (null != starshipWithMaxSpeed && null != vehicleWithMaxSpeed) {

            int starshipSpeed = Integer.parseInt(starshipWithMaxSpeed.get().getMax_atmosphering_speed());
            int vehicleSpeed = Integer.parseInt(vehicleWithMaxSpeed.get().getMax_atmosphering_speed());

            if (starshipSpeed > vehicleSpeed) return starshipWithMaxSpeed.get().getName();
            else return vehicleWithMaxSpeed.get().getName();

        } else if (null != starshipWithMaxSpeed) return starshipWithMaxSpeed.get().getName();

        else if (null != vehicleWithMaxSpeed) return vehicleWithMaxSpeed.get().getName();

        else return null;
    }

    private String getPlanetName(PersonInfoApiResponse responseBody) {

        return contextService.getPlanet(responseBody.getHomeworld()).getName();

    }

    private PersonInfoApiResponse getPersonInfoApiResponseEntity(String name, PersonInfoListApiResponse peopleList) {

        boolean haveNextPage = null != peopleList.getNext();

        PersonInfoApiResponse personInfo = peopleList.getResults().stream()
                .filter(p -> p.getName().equals(name))
                .findFirst()
                .orElse(null);
/*
        if (personInfo == null && haveNextPage) {
            PersonInfoApiResponse recursiveResult =
                    getPersonInfoApiResponseEntity(name, contextService.getPeople(peopleList.getNext()));

            if (recursiveResult != null)
                return recursiveResult;

        }


 */
        return personInfo;
    }

}
