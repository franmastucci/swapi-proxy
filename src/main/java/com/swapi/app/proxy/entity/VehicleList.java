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
public class VehicleList {

    private int count;
    private String next;
    private String previous;
    private List<Vehicle> results;

}
