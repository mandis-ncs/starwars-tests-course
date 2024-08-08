package com.test_api_course.starwars.repository;

import com.test_api_course.starwars.domain.Planet;
import org.springframework.data.domain.Example;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;

import java.util.List;
import java.util.Optional;

//QueryByExampleExecutor<Planet> is for QueryBuilder

public interface PlanetRepository extends CrudRepository<Planet, Long>, QueryByExampleExecutor<Planet> {
    Optional<Planet> findByName(String name);

    //receive a query builder and return a planet list
    @Override
    <S extends Planet> List<S> findAll(Example<S> example);
}
