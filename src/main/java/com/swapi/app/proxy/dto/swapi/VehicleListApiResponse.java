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
public class VehicleListApiResponse {
    private int count;
    private String next;
    private String previous;
    private List<VehicleApiResponse> results;
}

