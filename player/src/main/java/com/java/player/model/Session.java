package com.java.player.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class Session {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Player player;

    private LocalDateTime loginTime;
    private LocalDateTime logoutTime;
    private int timeLimit; // in minutes
}

