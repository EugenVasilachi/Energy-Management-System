package com.example.device.dtos;

import java.util.Objects;
import java.util.UUID;

public class UserDeviceDTO {

    private UUID idUserDevice;
    private UUID idUser;
    private UUID idDevice;

    public UserDeviceDTO() {
    }

    public UserDeviceDTO(UUID idUserDevice, UUID idUser, UUID idDevice) {
        this.idUserDevice = idUserDevice;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserDeviceDTO that = (UserDeviceDTO) o;
        return Objects.equals(idUser, that.idUser) && Objects.equals(idDevice, that.idDevice);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idUser, idDevice);
    }
}
