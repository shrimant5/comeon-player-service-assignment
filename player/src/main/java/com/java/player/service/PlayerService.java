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

    public Player registerPlayer(Player player) {
        // You should hash the password here
        return playerRepository.save(player);
    }

    public Optional<Player> login(String email, String password) {
        Optional<Player> playerOpt = playerRepository.findByEmail(email);
        if (playerOpt.isPresent()) {
            Player player = playerOpt.get();
            if (player.getPassword().equals(password)) {
                if (canLogin(player)) {
                    // Create a session
                    Session session = new Session();
                    session.setPlayer(player);
                    session.setLoginTime(LocalDateTime.now());
                    sessionRepository.save(session);

                    player.setLastLoginDate(LocalDate.now());
                    player.setActiveSessionDuration(0L); // Reset session duration
                    return Optional.of(playerRepository.save(player));
                }
            }
        }
        return Optional.empty();
    }

    public boolean canLogin(Player player) {
        return player.getActiveSessionDuration() < Player.DAILY_LIMIT;
    }

    public void logout(Long playerId) {
        Optional<Player> playerOpt = playerRepository.findById(playerId);
        if (playerOpt.isPresent()) {
            // Mark session as logged out
            // Additional logic to handle session
        }
    }

    public void logout(Player player) {
        Optional<Player> playerOpt = playerRepository.findById(player.getId());
        if (playerOpt.isPresent()) {
            player.setActiveSessionDuration(0L); // Reset session duration
            playerRepository.save(player);
        }
    }

    public void updateSessionDuration(Player player, long duration) {
        player.setActiveSessionDuration(player.getActiveSessionDuration() + duration);
        if (player.getActiveSessionDuration() >= Player.DAILY_LIMIT) {
            logout(player);
        }
        playerRepository.save(player);
    }

    public Optional<Player> getPlayerById(Long playerId) {
        return playerRepository.findById(playerId);
    }
}

