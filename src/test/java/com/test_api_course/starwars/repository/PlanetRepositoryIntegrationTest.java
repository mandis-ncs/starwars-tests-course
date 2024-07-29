package com.test_api_course.starwars.repository;

import com.test_api_course.starwars.domain.Planet;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

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

    @Test
    void createPlanetInvalid() {
        //constraints on database assure that invalid data will not be saved
        //with the test pass, is incorrect, as constraints are not functioning
        // planetRepository.save(EMPTY_PLANET);
        // planetRepository.save(INVALID_PLANET);

        assertThatThrownBy(() -> planetRepository.save(EMPTY_PLANET)).isInstanceOf(RuntimeException.class);
        assertThatThrownBy(() -> planetRepository.save(INVALID_PLANET)).isInstanceOf(RuntimeException.class);
    }

//    @Test
//    void createPlanetNameAlreadyExists() {
//        Planet planet = testEntityManager.persistFlushFind(PLANET); //persist and return created planet
//        testEntityManager.detach(planet); //detach planet from entityManager
//        planet.setId(null); //so save method is going to try save the same planet
//        assertThatThrownBy(() -> planetRepository.save(PLANET)).isInstanceOf(RuntimeException.class);
//    }



}