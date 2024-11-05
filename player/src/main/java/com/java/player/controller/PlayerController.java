package com.java.player.controller;

import com.java.player.model.Player;
import com.java.player.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/players")
public class PlayerController {
    @Autowired
    private PlayerService playerService;

    @PostMapping("/register")
    public ResponseEntity<Player> register(@RequestBody Player player) {
        return ResponseEntity.ok(playerService.registerPlayer(player));
    }

    @PostMapping("/login")
    public Optional<ResponseEntity<Player>> login(@RequestParam String email, @RequestParam String password) {
        Optional<Player> playerOpt = playerService.login(email, password);
        return playerOpt.map(ResponseEntity::ok);
                // .orElseGet(() -> ResponseEntity.status(401).build());
    }

    @PostMapping("/logout/{playerId}")
    public ResponseEntity<String> logout(@PathVariable Long playerId) {
        Optional<Player> playerOpt = playerService.getPlayerById(playerId);
        if (playerOpt.isPresent()) {
            playerService.logout(playerOpt.get());
            return ResponseEntity.ok("Logout successful!");
        }
        return ResponseEntity.status(404).body("Player not found.");
    }

    @PostMapping("/set-time-limit")
    public ResponseEntity<String> setTimeLimit(@RequestParam Long playerId, @RequestParam long limit) {
        // Implement time limit setting logic if needed
        return ResponseEntity.ok("Time limit set successfully.");
    }
}


