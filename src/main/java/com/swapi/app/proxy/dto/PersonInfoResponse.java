package com.swapi.app.proxy.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class PersonInfoResponse {

    @JsonProperty("name")
    private String name;
    @JsonProperty("birth_year")
    private String birthYear;
    @JsonProperty("gender")
    private String gender;
    @JsonProperty("planet_name")
    private String planetName;
    @JsonProperty("fastest_vehicle_driven")
    private String fastestVehicleDriven;
    @JsonProperty("films")
    private List<FilmResponse> films;

}
