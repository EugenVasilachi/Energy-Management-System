package com.example.monitoring.repositories;

import com.example.monitoring.entities.EnergyConsumption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface EnergyConsumptionRepository extends JpaRepository<EnergyConsumption, UUID> {
    Optional<List<EnergyConsumption>> findAllByIdDevice(UUID id);
}
