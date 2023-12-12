package com.loshchin.vladimir.repo;

import com.loshchin.vladimir.domain.Model;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ModelRepo extends JpaRepository<Model, Long> {

}
