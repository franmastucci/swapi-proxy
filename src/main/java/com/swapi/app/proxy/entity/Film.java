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
public class Film {

    private List<String> characters;
    private String created;
    private String director;
    private String edited;
    private int episode_id;
    private String opening_crawl;
    private List<String> planets;
    private String producer;
    private String release_date;
    private List<String> species;
    private List<String> starships;
    private String title;
    private String url;
    private List<String> vehicles;

}
