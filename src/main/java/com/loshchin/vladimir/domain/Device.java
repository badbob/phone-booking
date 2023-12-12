package com.loshchin.vladimir.domain;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Version;
import javax.persistence.Column;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Device {

    @Id
    private Long id;

    @Version
    private Integer version;

    @NotNull
    private Availability availability;

    @Column(name = "booking_ts", nullable = true)
    private Timestamp bookingTs;

    @Column(name = "booked_by", nullable = true)
    private String bookedBy;

    @ManyToOne
    @JoinColumn(name = "model_id", nullable = false)
    private Model model;

    public Device(Long id, Model model) {
        this.id = id;
        this.availability = Availability.AVAILABLE;
        this.model = model;
    }

    public boolean isAvailable() {
        return availability.equals(Availability.AVAILABLE);
    }
}
