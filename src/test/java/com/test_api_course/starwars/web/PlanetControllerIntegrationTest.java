package com.test_api_course.starwars.web;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.test_api_course.starwars.domain.PlanetService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static com.test_api_course.starwars.common.PlanetConstants.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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

}