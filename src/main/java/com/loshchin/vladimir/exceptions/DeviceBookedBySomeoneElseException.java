package com.loshchin.vladimir.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class DeviceBookedBySomeoneElseException  extends ResponseStatusException {

    public DeviceBookedBySomeoneElseException(Long id) {
        super(HttpStatus.FORBIDDEN, String.format(
                  "Device with id=%d already booked by someone else", id));
    }
}
