package com.java.player.controller;

import com.java.player.model.Player;
import com.java.player.model.Session;
import com.java.player.service.PlayerService;
import com.java.player.service.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/players")
public class PlayerController {
    @Autowired
    PlayerService playerService;
    @Autowired
    SessionService sessionService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody Player player) {
        playerService.registerPlayer(player);
        return new ResponseEntity<>("Player registered successfully", HttpStatus.CREATED);
    }

    @GetMapping("/login")
    public ResponseEntity<String> login(@RequestParam String email, @RequestParam String password) {

        if (playerService.validatePlayerCredentials(email, password)) {
            Player player = playerService.getPlayerByEmail(email);
            Session session = sessionService.createSession(player);

            // If the player has a time limit, we check it
            sessionService.logoutIfTimeLimitExceeded(session);

            return new ResponseEntity<>("Login successful. Session ID: " + session.getId(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Invalid credentials", HttpStatus.UNAUTHORIZED);
        }
    }

    @GetMapping("/logout")
    public ResponseEntity<String> logout(@RequestParam Long sessionId) {
        sessionService.logout(sessionId);
        return new ResponseEntity<>("Logout successful", HttpStatus.OK);
    }

    @GetMapping("/set-time-limit")
    public ResponseEntity<String> setTimeLimit(@RequestParam Long playerId, @RequestParam String limit) {
       // LocalDateTime timeLimit = LocalDateTime.parse(limit);
        playerService.setTimeLimitForPlayer(playerId, Long.valueOf(limit));
        return new ResponseEntity<>("Time limit set successfully", HttpStatus.OK);
    }
}


