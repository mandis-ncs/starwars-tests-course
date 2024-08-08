package com.test_api_course.starwars.repository;

import com.test_api_course.starwars.domain.Planet;
import com.test_api_course.starwars.domain.QueryBuilder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Example;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static com.test_api_course.starwars.common.PlanetConstants.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

//@SpringBootTest(classes = PlanetRepository.class)
@DataJpaTest //load components that interact with repository
class PlanetRepositoryIntegrationTest {

    @Autowired
    private PlanetRepository planetRepository;

    //as you can't use the repository to test repository,
    //use testEntityManager to do so
    @Autowired
    private TestEntityManager testEntityManager;

    @AfterEach
    void afterEach() {
        PLANET.setId(null); //clear id "cache"
    }

    @Test
    void createPlanetValid() {
        Planet planet = planetRepository.save(PLANET);

        Planet sut = testEntityManager.find(Planet.class, planet.getId());

        assertThat(sut).isNotNull();

        //as sut does not have id, we can not compare both entities, because it will return conflict
        assertThat(sut.getName()).isEqualTo(PLANET.getName());
        assertThat(sut.getClimate()).isEqualTo(PLANET.getClimate());
        assertThat(sut.getTerrain()).isEqualTo(PLANET.getTerrain());
    }

    @ParameterizedTest
    @MethodSource("providesInvalidPlanets")
    void createPlanetInvalid(Planet planet) {
        //constraints on database assure that invalid data will not be saved
        //with the test pass, is incorrect, as constraints are not functioning

//        assertThatThrownBy(() -> planetRepository.save(EMPTY_PLANET)).isInstanceOf(RuntimeException.class);
//        assertThatThrownBy(() -> planetRepository.save(INVALID_PLANET)).isInstanceOf(RuntimeException.class);
        assertThatThrownBy(() -> planetRepository.save(planet)).isInstanceOf(RuntimeException.class);
    }

    //Parametrized Tests
    private static Stream<Arguments> providesInvalidPlanets() {
        return Stream.of(
                Arguments.of(new Planet(null, "", "")),
                Arguments.of(new Planet(null, null, "")),
                Arguments.of(new Planet(null, "", null))
        );
    }

//    @Test
//    void createPlanetNameAlreadyExists() {
//        Planet planet = testEntityManager.persistFlushFind(PLANET); //persist and return created planet
//        testEntityManager.detach(planet); //detach planet from entityManager
//        planet.setId(null); //so save method is going to try save the same planet
//        assertThatThrownBy(() -> planetRepository.save(PLANET)).isInstanceOf(RuntimeException.class);
//    }

    @Test
    void getPlanetByExistentIdReturnsPlanet() {
        Planet planet = testEntityManager.persistAndFlush(PLANET);

        Optional<Planet> sut = planetRepository.findById(planet.getId());

        assertThat(planet).isNotNull();
        assertThat(sut.get()).isEqualTo(planet);
    }

    @Test
    void getPlanetByNonexistentIdReturnsEmpty() {
        Optional<Planet> sut = planetRepository.findById(1L);
        assertThat(sut).isEmpty();
    }

    @Test
    void getPlanetByExistentNameReturnsPlanet() throws Exception {
        Planet planet = testEntityManager.persistAndFlush(PLANET);

        Optional<Planet> sut = planetRepository.findByName(planet.getName());
        assertThat(planet).isNotNull();
        assertThat(sut.get()).isEqualTo(planet);
    }

    @Test
    void getPlanetByNonexistentNameReturnsIsEmpty() throws Exception {
        Optional<Planet> sut = planetRepository.findByName("name");
        assertThat(sut).isEmpty();
    }

    @Sql(scripts = "/import_planets.sql")
    @Test
    void listPlanets_ReturnsFilteredPlanets() throws Exception {
        Example<Planet> queryWithoutFilters = QueryBuilder.makeQuery(new Planet());
        Example<Planet> queryWithFilters = QueryBuilder.makeQuery(
                new Planet(TATOOINE.getClimate(), TATOOINE.getTerrain()));

        List<Planet> responseWithoutFilters = planetRepository.findAll(queryWithoutFilters); //list receive a query builder
        List<Planet> responseWithFilters = planetRepository.findAll(queryWithFilters);

        assertThat(responseWithFilters).isNotEmpty();
        assertThat(responseWithFilters).hasSize(1); //returns only the TATOOINE planet
        assertThat(responseWithFilters.get(0)).isEqualTo(TATOOINE);

        assertThat(responseWithoutFilters).isNotEmpty();
        assertThat(responseWithoutFilters).hasSize(3); //returns all the planet on sql script

    }

    @Test
    void listPlanets_ReturnsNoPlanets() throws Exception {
        Example<Planet> query = QueryBuilder.makeQuery(new Planet());

        List<Planet> response = planetRepository.findAll(query);

        assertThat(response).isEmpty();
    }

    @Test
    void deletePlanets_WithExistentId_ReturnsNoContent() {
        Planet planet = testEntityManager.persistAndFlush(PLANET);

        planetRepository.deleteById(planet.getId()); //can not go into a var because returns void

        Planet removedPlanet = testEntityManager.find(Planet.class, planet.getId());

        assertThat(removedPlanet).isNull();
    }

//    @Test
//    void deletePlanets_WithNonexistentId_ReturnsNotFound() {
//        assertThatThrownBy(() -> planetRepository.deleteById(1L)).isInstanceOf(EmptyResultDataAccessException.class);
//    }

}