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

    private LocalDateTime loginTime;
    private LocalDateTime logoutTime;
    private Long timeLimit;

    @ManyToOne
    @JoinColumn(name = "player_id")
    private Player player;
}

