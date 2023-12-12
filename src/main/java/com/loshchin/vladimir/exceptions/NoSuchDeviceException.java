package com.loshchin.vladimir.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class NoSuchDeviceException extends ResponseStatusException {

    public NoSuchDeviceException(Long id) {
        super(HttpStatus.NOT_FOUND, String.format("No such device with id=%d", id));
    }
}
