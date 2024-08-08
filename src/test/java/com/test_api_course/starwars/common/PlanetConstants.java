package com.test_api_course.starwars.common;

import com.test_api_course.starwars.domain.Planet;

import java.util.ArrayList;
import java.util.List;

public class PlanetConstants {

    public static final Planet PLANET = new Planet("planet", "climate", "terrain");
    public static final Planet INVALID_PLANET = new Planet("", "", "");
    public static final Planet EMPTY_PLANET = new Planet();

    public static final Planet TATOOINE = new Planet(1L, "Tatooine", "arid", "desert");
    public static final Planet ALDERAAN = new Planet(2L, "Alderaan", "temperate", "grasslands, mountains");
    public static final Planet YAVINIV = new Planet(3L, "Yavin IV", "temperate, tropical", "jungle, rainforests");
    public static final List<Planet> PLANETS = new ArrayList<>() {
        {
            add(TATOOINE);
            add(ALDERAAN);
            add(YAVINIV);
        }
    };
}
