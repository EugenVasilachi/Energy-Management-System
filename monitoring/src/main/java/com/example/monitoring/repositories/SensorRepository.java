package com.example.monitoring.repositories;

import com.example.monitoring.entities.Sensor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SensorRepository extends JpaRepository<Sensor, UUID> {
    Optional<Sensor> findByIdDevice(UUID id);
    Optional<List<Sensor>> findAllByIdUser(UUID id);
}
