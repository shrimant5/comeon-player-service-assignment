package com.java.player.repository;

import com.java.player.model.Player;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface PlayerRepository extends JpaRepository<Player, Long> {
    Optional<Player> findByEmail(String email);
}
