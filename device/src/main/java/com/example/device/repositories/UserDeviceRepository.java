package com.example.device.repositories;

import com.example.device.entities.UserDevice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

public interface UserDeviceRepository extends JpaRepository<UserDevice, UUID> {

    @Modifying
    @Transactional
    void deleteByIdUser(UUID idUser);

    @Modifying
    @Transactional
    void deleteByIdDevice(UUID idDevice);

    @Query("SELECT e from UserDevice e WHERE e.idUser = :idUser")
    List<UserDevice> findByIdUser(@Param("idUser") UUID idUser);

    @Query("SELECT e from UserDevice e WHERE e.idDevice = :idDevice")
    List<UserDevice> findByIdDevice(@Param("idDevice") UUID idDevice);

}
