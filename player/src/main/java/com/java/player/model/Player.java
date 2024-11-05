package com.java.player.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Data
public class Player {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;
    private String password; // Store hashed password in production
    private String name;
    private String surname;
    private LocalDate dateOfBirth;
    private String address;
    private LocalDate lastLoginDate;
    private Long activeSessionDuration; // in milliseconds
    public static final long DAILY_LIMIT = 86400000; // 24 hours in milliseconds
}
