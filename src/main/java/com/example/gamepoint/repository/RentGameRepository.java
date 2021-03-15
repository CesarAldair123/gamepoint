package com.example.gamepoint.repository;

import com.example.gamepoint.model.Game;
import com.example.gamepoint.model.RentGame;
import com.example.gamepoint.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RentGameRepository extends JpaRepository<RentGame, Integer> {
    @Query("SELECT r FROM RentGame r INNER JOIN Sale s ON r.sale = s INNER JOIN User u ON s.user = u WHERE u = ?1 AND r.wasReturned = 0")
    List<RentGame> findByQuery(User u);

    List<RentGame> findRentGamesByGame(Game game);
}
