package com.example.gamepoint.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class RentGame {
    @Id
    @GeneratedValue(generator="increment")
    @GenericGenerator(name="increment", strategy = "increment")
    private int id;
    @ManyToOne
    private Game game;
    @Column(nullable = false)
    private Date firstMonth;
    @Column(nullable = false)
    private Date lasthMonth;
    @Column(nullable = false)
    private double total;
    @Column(nullable = false)
    private int quantity;
    @Column(nullable = false)
    private int wasReturned;
    @ManyToOne
    private Sale sale;
}
