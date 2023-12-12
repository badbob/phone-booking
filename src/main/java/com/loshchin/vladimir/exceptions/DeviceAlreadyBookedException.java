package com.loshchin.vladimir.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class DeviceAlreadyBookedException extends ResponseStatusException {

    public DeviceAlreadyBookedException(Long id) {
        super(HttpStatus.BAD_REQUEST, String.format("Device with id=%d already booked", id));
    }
}
