package com.swapi.app.proxy.controller.service;

import com.swapi.app.proxy.entity.FilmList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FilmService {

    @Autowired
    SwapiProxyService swapiProxyService;


    public FilmList getFilms() {
        return swapiProxyService.getFilms();
    }

}
