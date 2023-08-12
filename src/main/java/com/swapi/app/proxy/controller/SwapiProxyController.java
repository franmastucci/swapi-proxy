package com.swapi.app.proxy.controller;

import com.swapi.app.proxy.service.SwapiProxyService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Log4j2
public class SwapiProxyController {

    @Autowired
    private SwapiProxyService swapiProxyService;


    @GetMapping(path = "/init")
    public ResponseEntity<Void> init() {
        swapiProxyService.getFilms();
        return ResponseEntity.ok().build();

    }

}
