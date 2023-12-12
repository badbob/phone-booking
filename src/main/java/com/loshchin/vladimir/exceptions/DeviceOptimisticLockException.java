package com.loshchin.vladimir.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class DeviceOptimisticLockException extends ResponseStatusException {

    public DeviceOptimisticLockException(Long id) {
        super(HttpStatus.CONFLICT, String.format("Device with id=%d updated concurrently, retry please", id));
    }
}
