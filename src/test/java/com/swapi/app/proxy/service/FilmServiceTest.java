package com.swapi.app.proxy.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import com.swapi.app.proxy.entity.FilmList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class FilmServiceTest {

    @Mock
    private SwapiProxyService swapiProxyService;

    private FilmService filmService;


    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        filmService = new FilmService(swapiProxyService);
    }

    @Test
    public void should_return_a_film_list() {

        FilmList expectedFilmList = new FilmList();
        when(swapiProxyService.getFilms()).thenReturn(expectedFilmList);

        FilmList result = filmService.getFilms();

        assertEquals(expectedFilmList, result);
        verify(swapiProxyService).getFilms();

    }

}
