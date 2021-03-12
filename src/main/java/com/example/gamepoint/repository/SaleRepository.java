package com.example.gamepoint.repository;

import com.example.gamepoint.model.Sale;
import com.example.gamepoint.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SaleRepository extends JpaRepository<Sale, Integer> {
    List<Sale> getSaleByUser(User user);
}
