package com.example.device.entities;

import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;

import java.util.UUID;

@Entity
@Table(name = "user_device")
public class UserDevice {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name="id_user_device")
    private UUID idUserDevice;

    @Column(name = "id_user", nullable = false)
    private UUID idUser;

    @Column(name = "id_device", nullable = false)
    private UUID idDevice;

    public UserDevice() {
    }

    public UserDevice(UUID idUser, UUID idDevice) {
        this.idUser = idUser;
        this.idDevice = idDevice;
    }

    public UUID getIdUserDevice() {
        return idUserDevice;
    }

    public void setIdUserDevice(UUID idUserDevice) {
        this.idUserDevice = idUserDevice;
    }

    public UUID getIdUser() {
        return idUser;
    }

    public void setIdUser(UUID idUser) {
        this.idUser = idUser;
    }

    public UUID getIdDevice() {
        return idDevice;
    }

    public void setIdDevice(UUID idDevice) {
        this.idDevice = idDevice;
    }
}
