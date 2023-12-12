
package com.loshchin.vladimir.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import org.mockito.Mock;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.assertThrows;

import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.assertThat;

import java.security.Principal;

import com.loshchin.vladimir.exceptions.DeviceBookedBySomeoneElseException;

@SpringBootTest
public class DeviceControllerTest {

    @Mock
    private Principal bob;

    @Mock
    private Principal alice;

    @Autowired
    private DeviceController controller;

    @BeforeEach void before() {
        when(bob.getName()).thenReturn("bob");
        when(alice.getName()).thenReturn("alice");
    }

    @Test void testList() {
        controller.list();
    }

    @Test void testBookReleaseIdempotent() {
        var d = controller.book(1l, bob);

        assertThat(d.getBookingTs()).isNotNull();
        assertThat(d.getBookedBy()).isEqualTo("bob");

        var dnew = controller.book(1l, bob);
        assertThat(d).isEqualTo(dnew); // idempotence check

        d = controller.release(1l, bob);

        assertThat(d.getBookingTs()).isNull();
        assertThat(d.getBookedBy()).isNull();

        dnew = controller.release(1l, bob);

        assertThat(d).isEqualTo(dnew); // idempotence check
    }

    @Test
    void testForbiddenToBookAlreadyBooked() {
        var d = controller.book(1l, bob);

        assertThat(d.getBookingTs()).isNotNull();
        assertThat(d.getBookedBy()).isEqualTo("bob");

        assertThrows(DeviceBookedBySomeoneElseException.class, () -> controller.book(1l, alice));
    }

    @Test
    void testForbiddenToReleaseAlreadyBooked() {
        var d = controller.book(2l, bob);

        assertThat(d.getBookingTs()).isNotNull();
        assertThat(d.getBookedBy()).isEqualTo("bob");

        assertThrows(DeviceBookedBySomeoneElseException.class, () -> controller.release(2l, alice));
    }
}
