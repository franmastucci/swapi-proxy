package com.swapi.app.proxy.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Planet {

    private String climate;
    private String created;
    private String diameter;
    private String edited;
    private List<String> films;
    private String gravity;
    private String name;
    private String orbital_period;
    private String population;
    private List<String> residents;
    private String rotation_period;
    private String surface_water;
    private String terrain;
    private String url;

}
