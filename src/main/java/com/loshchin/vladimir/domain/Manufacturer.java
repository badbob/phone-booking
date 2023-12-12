package com.loshchin.vladimir.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Manufacturer {

    @Id
    private Long id;

    @Column(nullable = false)
    private String name;
}
