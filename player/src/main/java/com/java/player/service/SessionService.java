package com.java.player.service;

import com.java.player.model.Player;
import com.java.player.model.Session;
import com.java.player.repository.PlayerRepository;
import com.java.player.repository.SessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
public class SessionService {

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private SessionRepository sessionRepository;

    private static final int MAX_SESSION_TIME_LIMIT_IN_MINUTES = 60;

    // Creating a session
    public Session createSession(Player player) {
        if (player.getSession() != null && player.getSession().getLogoutTime() == null) {
            throw new RuntimeException("Player already has an active session.");
        }

        player.setLastLoginDate(LocalDate.now());
        playerRepository.save(player);

        Session session = new Session();
        session.setLoginTime(LocalDateTime.now());
        session.setPlayer(player);
        sessionRepository.save(session);

        return session;
    }

    public void logout(Long sessionId) {
        Session session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("Session not found"));

        session.setLogoutTime(LocalDateTime.now());
        sessionRepository.save(session);
    }

    // Checking time limit
    public boolean checkIfTimeLimitIsReached(Session session) {
        if (session.getLogoutTime() != null) {
            return false;
        }
        LocalDateTime loginTime = session.getLoginTime();
        LocalDateTime currentTime = LocalDateTime.now();
        long minutes = Duration.between(loginTime, currentTime).toMinutes();

        return minutes > MAX_SESSION_TIME_LIMIT_IN_MINUTES;
    }

    public void logoutIfTimeLimitExceeded(Session session) {
        if (checkIfTimeLimitIsReached(session)) {
            logout(session.getId());
            throw new RuntimeException("Session time limit exceeded. Player logged out.");
        }
    }

    public void updateTimeLimit(Session session, Long timeLimit) {
        session.setTimeLimit(timeLimit);
        sessionRepository.save(session);
    }
}

