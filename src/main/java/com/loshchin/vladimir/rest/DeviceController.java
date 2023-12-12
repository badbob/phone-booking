package com.loshchin.vladimir.rest;

import java.security.Principal;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

import com.loshchin.vladimir.domain.Availability;
import com.loshchin.vladimir.domain.Device;
import com.loshchin.vladimir.repo.DeviceRepo;
import com.loshchin.vladimir.exceptions.DeviceAlreadyBookedException;
import com.loshchin.vladimir.exceptions.DeviceBookedBySomeoneElseException;
import com.loshchin.vladimir.exceptions.DeviceIsNotBookedException;
import com.loshchin.vladimir.exceptions.DeviceOptimisticLockException;
import com.loshchin.vladimir.exceptions.NoSuchDeviceException;

import org.hibernate.StaleStateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.kafka.core.KafkaTemplate;

import static com.loshchin.vladimir.domain.Availability.BOOKED;
import static com.loshchin.vladimir.domain.Availability.AVAILABLE;

@RestController
@RequestMapping("/device")
public class DeviceController {

    @Autowired
    private DeviceRepo deviceRepo;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @GetMapping("/list")
    public List<Device> list() {
        return deviceRepo.findAll();
    }

    @Transactional
    @PutMapping("/{id}/book")
    public Device book(@PathVariable Long id, Principal principal) {
        var d = deviceRepo.findById(id).orElseThrow(() -> new NoSuchDeviceException(id));
        if (d.isAvailable()) {
            d.setBookedBy(principal.getName());
            return changeAvailability(d, BOOKED);
        }
        else {
            if (principal.getName().equals(d.getBookedBy())) {
                // Idempotent for double submit
                return d;
            } else {
                throw new DeviceBookedBySomeoneElseException(id);
            }
        }
    }

    @Transactional
    @PutMapping("/{id}/release")
    public Device release(@PathVariable Long id, Principal principal) {
        var d = deviceRepo.findById(id).orElseThrow(() -> new NoSuchDeviceException(id));

        if (!d.isAvailable()) {
            if (principal.getName().equals(d.getBookedBy())) {
                d.setBookedBy(null);
                return changeAvailability(d, AVAILABLE);
            } else {
                throw new DeviceBookedBySomeoneElseException(id);
            }
        }
        else {
            // Idempotent for double submit
            return d;
        }
    }

    private Device changeAvailability(Device d, Availability availability) {
        d.setAvailability(availability);
        d.setBookingTs(availability.equals(BOOKED) ? Timestamp.from(Instant.now()) : null);
        try {
            deviceRepo.saveAndFlush(d);
            kafkaTemplate.send("device-topic", availability.name() + ":" + d.getId());
            return d;
        } catch (ObjectOptimisticLockingFailureException ex) {
            throw new DeviceOptimisticLockException(d.getId());
        }
    }
}
