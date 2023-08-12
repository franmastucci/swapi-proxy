package com.swapi.app.proxy.dto.swapi;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class VehicleApiResponse {
    private String cargo_capacity;
    private String consumables;
    private String cost_in_credits;
    private String created;
    private String crew;
    private String edited;
    private String length;
    private String manufacturer;
    private String max_atmosphering_speed;
    private String model;
    private String name;
    private String passengers;
    private List<String> pilots;
    private List<String> films;
    private String url;
    private String vehicle_class;

}
