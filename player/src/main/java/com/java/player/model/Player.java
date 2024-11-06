package com.java.player.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Data
public class Player {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;
    private String password;
    private String name;
    private String surname;
    private LocalDate dateOfBirth;
    private String address;
    private LocalDate lastLoginDate;
    private Long activeSessionDuration; // in milliseconds

    // New code
    @OneToOne(mappedBy = "player")
    private Session session;
}
