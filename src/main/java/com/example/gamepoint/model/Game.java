package com.example.gamepoint.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Game {
    @Id
    @GeneratedValue(generator="increment")
    @GenericGenerator(name="increment", strategy = "increment")
    private int id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false, columnDefinition="TEXT")
    private String description;
    @Column(nullable = false)
    private String imgUrl;
    @Column(nullable = false)
    private double price;
    @Column(nullable = false)
    private String developer;
    @Column(nullable = false)
    private int stock;
    @Column(nullable = false)
    private int forRental;
    private double pricePerMonth;
    @ManyToOne
    private User provider;
    @OneToMany(mappedBy = "game")
    private List<SaleDetails> saleDetails;
    @OneToMany(mappedBy = "game")
    private List<FreeGameCode> freeGameCodes;
}
