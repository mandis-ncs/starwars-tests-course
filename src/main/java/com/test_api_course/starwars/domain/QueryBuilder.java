package com.test_api_course.starwars.domain;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;

//Dynamic query
//If none of the parameters were given, the query is going to search for all
//If only one of the parameters were given, the query will adapt and search for this unique param
//QueryByExampleExecutor<Planet> goes on repository to use Query Builder
//Override the method find all

public class QueryBuilder {
    public static Example<Planet> makeQuery(Planet planet) {
        ExampleMatcher exampleMatcher = ExampleMatcher.matchingAll().withIgnoreCase().withIgnoreNullValues();
        return Example.of(planet, exampleMatcher);
    }

}
