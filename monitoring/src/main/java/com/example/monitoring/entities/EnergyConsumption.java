package com.example.monitoring.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import java.sql.Timestamp;
import java.util.UUID;

@Entity
@Table(name = "energy_consumption")
@Data
@NoArgsConstructor
public class EnergyConsumption {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name = "id")
    private UUID id;

    @Column(name = "id_device")
    private UUID idDevice;

    @Column
    private Timestamp timestamp;

    @Column
    private Double value;

    public EnergyConsumption(UUID idDevice, Timestamp timestamp, Double value) {
        this.idDevice = idDevice;
        this.timestamp = timestamp;
        this.value = value;
    }
}
