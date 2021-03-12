package com.example.gamepoint.repository;

import com.example.gamepoint.model.Developer;
import com.example.gamepoint.model.Game;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DeveloperRepository extends JpaRepository<Developer, Integer> {
    @Query(nativeQuery=true, value="SELECT * FROM dev WHERE dev.name LIKE %?1%")
    List<Game> findDevelopersByExpression(String expression);
    Optional<Developer> findDeveloperByName(String name);
}
