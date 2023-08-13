package com.swapi.app.proxy.service;

import com.swapi.app.proxy.entity.FilmList;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Data
public class FilmService {

    SwapiProxyService swapiProxyService;


    @Autowired
    public FilmService(SwapiProxyService swapiProxyService) {
        this.swapiProxyService = swapiProxyService;
    }

    public FilmList getFilms() {
        return swapiProxyService.getFilms();
    }

}
