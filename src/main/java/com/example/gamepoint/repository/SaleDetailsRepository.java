package com.example.gamepoint.repository;

import com.example.gamepoint.model.SaleDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SaleDetailsRepository extends JpaRepository<SaleDetails, Integer> {
    List<SaleDetails> findSaleDetailsByGame_Id(int gameId);
}
