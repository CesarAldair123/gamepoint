package com.example.gamepoint.repository;

import com.example.gamepoint.model.Game;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GameRepository extends JpaRepository<Game, Integer> {
    @Query(nativeQuery=true, value="SELECT * FROM game WHERE game.name LIKE %?1%")
    List<Game> findGamesByExpression(String expression);
    Optional<Game> findGameByName(String name);
}
