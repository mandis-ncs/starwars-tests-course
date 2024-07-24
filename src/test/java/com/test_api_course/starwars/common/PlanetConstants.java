package com.test_api_course.starwars.common;

import com.test_api_course.starwars.domain.Planet;

public class PlanetConstants {

    public static final Planet PLANET = new Planet("planet", "climate", "terrain");
    public static final Planet INVALID_PLANET = new Planet("", "", "");

}
