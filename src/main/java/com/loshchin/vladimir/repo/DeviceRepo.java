package com.loshchin.vladimir.repo;

import com.loshchin.vladimir.domain.Device;

import org.springframework.data.jpa.repository.JpaRepository;

public interface DeviceRepo extends JpaRepository<Device, Long> {

}
