package com.java.player.service;

import com.java.player.model.Player;
import com.java.player.model.Session;
import com.java.player.repository.PlayerRepository;
import com.java.player.repository.SessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class PlayerService {
    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private SessionRepository sessionRepository;

    @Autowired
    private SessionService sessionService;

    public void registerPlayer(Player player) {

        if (playerRepository.findByEmail(player.getEmail()).isPresent()) {
            throw new RuntimeException("Player with this email already exists.");
        }
        player.setPassword(player.getPassword());
        playerRepository.save(player);
    }

    public void logout(Player player) {
        Optional<Player> alreadyExistingPlayer = playerRepository.findById(player.getId());
        if (alreadyExistingPlayer.isPresent()) {
            player.setActiveSessionDuration(0L); // Reset session duration
            playerRepository.save(player);
        }
    }

    public Player getPlayerByEmail(String email) {
        return playerRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Player not found"));
    }

    // Validating the player
    public boolean validatePlayerCredentials(String email, String password) {
        Player player = getPlayerByEmail(email);
        return player.getPassword().equals(password);
    }

    // Setting time limit for an active player
    public void setTimeLimitForPlayer(Long playerId, Long timeLimit) {
        Player player = playerRepository.findById(playerId)
                .orElseThrow(() -> new RuntimeException("Player not found"));

        // Check if the player has an active session
        if (player.getSession() == null || player.getSession().getLogoutTime() != null) {
            throw new RuntimeException("Player must be active to set a time limit.");
        }
        sessionService.updateTimeLimit(player.getSession(), timeLimit);
    }
}

