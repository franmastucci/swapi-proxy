package com.swapi.app.proxy.dto.proxy;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class FilmResponse {

    @JsonProperty("name")
    private String name;
    @JsonProperty("release_date")
    private String releaseDate;

}
