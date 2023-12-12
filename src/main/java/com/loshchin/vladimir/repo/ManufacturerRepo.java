package com.loshchin.vladimir.repo;

import com.loshchin.vladimir.domain.Manufacturer;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ManufacturerRepo extends JpaRepository<Manufacturer, Long> {

}
