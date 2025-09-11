package dev.cheercode.weather_viewer.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Session {
    private UUID id;
    private Long userId;
    private LocalDateTime expiresAt;
}
