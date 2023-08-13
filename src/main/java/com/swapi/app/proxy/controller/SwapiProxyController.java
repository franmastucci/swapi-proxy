package com.swapi.app.proxy.controller;

import com.swapi.app.proxy.service.SwapiProxyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <h1>SWAPI Proxy Controller</h1>
 *
 * <p>Controller class that provides an endpoint for initializing and preloading data from the SWAPI (Star Wars API).
 * The endpoint allows preloading of film data to enhance performance and responsiveness.
 * In future releases, this endpoint could be extended to load the entire Star Wars universe data. </p> <br>
 * @author Francisco Mastucci
 * @version 1.0
 * @since 2023-08-12
 */

@Log4j2
@RestController
@Tag(name = "init", description = "Resource for preloading SWAPI data to optimize responsiveness.")
public class SwapiProxyController {

    @Autowired
    private SwapiProxyService swapiProxyService;


    /**
     * Initializes and preloads data from the SWAPI by making a request to fetch film information.
     * This endpoint can be used to load film data into the cache for improved performance.
     *
     * @return A ResponseEntity with a success status if the initialization is successful.
     */

    @GetMapping(path = "/init")
    @Operation(summary = "Initialize the SWAPI data.")
    public ResponseEntity<Void> init() {
        swapiProxyService.getFilms();
        return ResponseEntity.ok().build();

    }

}
