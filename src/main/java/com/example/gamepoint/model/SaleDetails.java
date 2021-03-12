package com.example.gamepoint.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class SaleDetails {
    @Id
    @GeneratedValue(generator="increment")
    @GenericGenerator(name="increment", strategy = "increment")
    private int id;
    @ManyToOne
    private Game game;
    @ManyToOne
    private Sale sale;
    private int quantity;
    private double total;
}
