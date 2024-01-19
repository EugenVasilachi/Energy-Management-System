package com.example.monitoring.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import java.util.UUID;

@Entity
@Table(name = "sensor")
@NoArgsConstructor
@Data
public class Sensor {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name="id")
    private UUID id;

    @Column(name = "id_user", nullable = false)
    private UUID idUser;

    @Column(name = "id_device", nullable = false)
    private UUID idDevice;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "max", nullable = false)
    private Double max;

    public Sensor(UUID idUser, UUID idDevice, String description, Double max) {
        this.idUser = idUser;
        this.idDevice = idDevice;
        this.description = description;
        this.max = max;
    }
}
