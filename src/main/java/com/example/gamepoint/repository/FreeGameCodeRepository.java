package com.example.gamepoint.repository;

import com.example.gamepoint.model.FreeGameCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FreeGameCodeRepository extends JpaRepository<FreeGameCode, Integer> {
    Optional<FreeGameCode> findFreeGameCodeByGameCode(String gameCode);
}
