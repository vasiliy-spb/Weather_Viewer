package dev.cheercode.weather_viewer.repository;

import dev.cheercode.weather_viewer.model.Location;
import dev.cheercode.weather_viewer.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LocationRepository extends JpaRepository<Location, Long> {
    List<Location> findByUser(User user);

    Location save(Location location);

    void deleteByIdAndUser(Long id, User user);
}
