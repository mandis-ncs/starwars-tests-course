package com.test_api_course.starwars.domain;

import com.test_api_course.starwars.repository.PlanetRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Example;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.test_api_course.starwars.common.PlanetConstants.INVALID_PLANET;
import static com.test_api_course.starwars.common.PlanetConstants.PLANET;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

//@SpringBootTest(classes = PlanetService.class)    too heavy to use on unit tests
@ExtendWith(MockitoExtension.class)
class PlanetServiceTest {

    @InjectMocks
    private PlanetService planetService;

    @Mock
    private PlanetRepository planetRepository;

    @Test
    void creataPlanet_WithValidData_ReturnsPlanet() {
        when(planetRepository.save(PLANET)).thenReturn(PLANET);

        Planet sut  = planetService.create(PLANET );

        assertThat(sut).isEqualTo(PLANET);
    }

    @Test
    void createPlanet_WithInvalidData_ThrowsException() {
        when(planetRepository.save(INVALID_PLANET)).thenThrow(RuntimeException.class);

        assertThatThrownBy(() -> planetService.create(INVALID_PLANET)).isInstanceOf(RuntimeException.class);
    }

    @Test
    void getPlanetByExistentIdReturnsPlanet() {

        //arrange
        when(planetRepository.findById(1L)).thenReturn(Optional.of(PLANET));

        //act
        Optional<Planet> sut = planetService.get(1L);

        //assert
        assertThat(sut).isNotEmpty();
        assertThat(sut.get()).isEqualTo(PLANET);
    }

    @Test
    void getPlanetByNonexistentIdReturnsEmpty() {

        when(planetRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<Planet> sut = planetService.get(1L);

        assertThat(sut).isEmpty();
    }

    @Test
    void getByNameOk() {
        when(planetRepository.findByName("planet")).thenReturn(Optional.of(PLANET));
        Optional<Planet> sut = planetService.getByName("planet");
        assertThat(sut.get()).isEqualTo(PLANET);
        assertThat(sut).isNotEmpty();
    }

    @Test
    void getByNameError() {
        when(planetRepository.findByName("planet")).thenReturn(Optional.empty());
        Optional<Planet> sut = planetService.getByName("planet");
        assertThat(sut).isEmpty();
    }

    @Test
    void findAllAndReturnPlanets() {
        //arrange planet list
        List<Planet> planets = new ArrayList<>() {
            {
                add(PLANET);
            }
        };

        //arrange new query with params climate and terrain
        Example<Planet> query = QueryBuilder.makeQuery(new Planet(PLANET.getClimate(), PLANET.getTerrain()));

        when(planetRepository.findAll(query)).thenReturn(planets);

        List<Planet> sut = planetService.list(PLANET.getClimate(), PLANET.getTerrain());

        assertThat(sut).isNotEmpty();
        assertThat(sut).hasSize(1);
        assertThat(sut.get(0)).isEqualTo(PLANET);
    }

    @Test
    void findAllAndReturnEmpty() {
        when(planetRepository.findAll(any())).thenReturn(Collections.emptyList());

        List<Planet> sut = planetService.list(PLANET.getTerrain(), PLANET.getClimate());

        assertThat(sut).isEmpty();
    }

    @Test
    void deleteDoNotThrow() {
        assertThatCode(() -> planetService.remove(1L)).doesNotThrowAnyException();
    }

    @Test
    void deleteThrow() {
        doThrow(new RuntimeException()).when(planetRepository).deleteById(999L);
        assertThatThrownBy(() -> planetService.remove(999L)).isInstanceOf(RuntimeException.class);
    }


}