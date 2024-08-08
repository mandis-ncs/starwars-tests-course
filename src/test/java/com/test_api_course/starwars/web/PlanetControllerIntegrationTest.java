package com.test_api_course.starwars.web;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.test_api_course.starwars.domain.PlanetService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.test_api_course.starwars.common.PlanetConstants.*;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PlanetController.class) //load this controller on context
class PlanetControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc; //client to interact with controller

    @Autowired
    private ObjectMapper objectMapper; //have to use this to convert object to a string!!!

    @MockBean
    private PlanetService planetService;

    @Test
    void createPlanetWithValidDataReturnsCreated() throws Exception {
        when(planetService.create(PLANET)).thenReturn(PLANET);

        mockMvc.perform(post("/planets") //this is the uri of post
                        .content(objectMapper.writeValueAsString(PLANET))//this is the request body
                        .contentType(MediaType.APPLICATION_JSON))//declares the type of body
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$") //json root is represented by $
                        .value(PLANET)); //json is expecting a value type of PLANET
    }

    @Test
    void createPlanetWithInvalidDataReturnsBadRequest() throws Exception {

        mockMvc
                .perform(post("/planets")
                        .content(objectMapper.writeValueAsString(INVALID_PLANET))
                        .contentType(MediaType.APPLICATION_JSON));

        mockMvc
                .perform(post("/planets")
                        .content(objectMapper.writeValueAsString(EMPTY_PLANET))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnprocessableEntity()); //handle in GeneralExceptionHandler
    }

    @Test
    void createPlanetWithExistingNameReturnsConflict() throws Exception {
        when(planetService.create(any())).thenThrow(DataIntegrityViolationException.class);
        mockMvc
                .perform(post("/planets")
                        .content(objectMapper.writeValueAsString(PLANET))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict()); //handle in GeneralExceptionHandler
    }

    @Test
    void getPlanetByExistentIdReturnsPlanet() throws Exception {
        when(planetService.get(any())).thenReturn(Optional.of(PLANET));
        mockMvc
                .perform(get("/planets"))
                .andExpect(jsonPath("$").value(PLANET)) //expect to return the planet
                .andExpect(status().isOk());
    }

    @Test
    void getPlanetByNonexistentIdReturnsEmpty() throws Exception {
        mockMvc
                .perform(get("/planets/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getPlanetByExistentNameReturnsPlanet() throws Exception {
        when(planetService.getByName(any())).thenReturn(Optional.of(PLANET));
        mockMvc
                .perform(get("/planets/name/" + PLANET.getName())) //to concatenate uri + name param
                .andExpect(jsonPath("$").value(PLANET))
                .andExpect(status().isOk());
    }

    @Test
    void getPlanetByNonexistentNameNotFound() throws Exception {
        mockMvc
                .perform(get("/planets/name/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void listPlanets_ReturnsFilteredPlanets() throws Exception {
        when(planetService.list(null, null)).thenReturn(PLANETS);
        when(planetService.list(TATOOINE.getTerrain(), TATOOINE.getClimate())).thenReturn(List.of(TATOOINE));

        //without filters
        mockMvc
                .perform(
                        get("/planets"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)));

        //with filters terrain and climate
        mockMvc
                .perform(get("/planets?" +
                        String.format("terrain=%s&climate=%s", TATOOINE.getTerrain(), TATOOINE.getClimate())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0]").value(TATOOINE)); //gets the first index of json

    }

    @Test
    void listPlanets_ReturnsNoPlanets() throws Exception {
        when(planetService.list(null, null)).thenReturn(Collections.emptyList());

        mockMvc
                .perform(get("/planets"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    void deletePlanets_WithExistentId_ReturnsNoContent() throws Exception {
        mockMvc
                .perform(delete("/planets/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void deletePlanets_WithNonexistentId_ReturnsNotFound() throws Exception {
        final Long planetId = 1L;
        //when() method do not support methods that returns void
        doThrow(new EmptyResultDataAccessException(1)).when(planetService).remove(planetId);
        mockMvc
                .perform(delete("/planets/13456"))
                .andExpect(status().isNotFound());
    }


}