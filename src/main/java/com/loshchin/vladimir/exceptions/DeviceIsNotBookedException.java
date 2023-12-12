package com.loshchin.vladimir.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class DeviceIsNotBookedException extends ResponseStatusException {

    public DeviceIsNotBookedException(Long id) {
        super(HttpStatus.BAD_REQUEST, String.format("Device with id=%d is not booked", id));
    }
}
