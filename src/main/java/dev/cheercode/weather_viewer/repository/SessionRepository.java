package dev.cheercode.weather_viewer.repository;

import dev.cheercode.weather_viewer.model.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface SessionRepository extends JpaRepository<Session, UUID> {
    Session save(Session session);

    Optional<Session> findById(UUID id);

    @Query("SELECT * FROM sessions WHERE user_id = ?")
    Optional<Session> findByUserId(Long userId);

    void deleteById(UUID id);
}
