package com.loshchin.vladimir;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

import com.loshchin.vladimir.domain.Device;
import com.loshchin.vladimir.domain.Manufacturer;
import com.loshchin.vladimir.domain.Model;
import com.loshchin.vladimir.repo.DeviceRepo;
import com.loshchin.vladimir.repo.ManufacturerRepo;
import com.loshchin.vladimir.repo.ModelRepo;

@Configuration
public class InitDatabase {

    @Bean
    CommandLineRunner initDB(ManufacturerRepo manufacturerRepo, ModelRepo modelRepo, DeviceRepo deviceRepo) {
        return args -> {
            var samsung = new Manufacturer(1l, "Samsung");
            var motorola = new Manufacturer(2l, "Motorola");
            var oneplus = new Manufacturer(3l, "OnePlus");
            var apple = new Manufacturer(4l, "Apple");
            var nokia = new Manufacturer(5l, "Nokia");

            manufacturerRepo.saveAll(
                List.of(samsung, motorola, oneplus, apple, nokia));

            var s9 = new Model(1l, "S9", samsung);
            var s8 = new Model(2l, "S8", samsung);
            var nexus_6 = new Model(3l, "Nexus 6", motorola);
            var oneplus_9 = new Model(4l, "9", oneplus);
            var iphone_13 = new Model(5l, "iPhone 13", apple);
            var iphone_12 = new Model(6l, "iPhone 12", apple);
            var iphone_11 = new Model(7l, "iPhone 11", apple);
            var iphone_10 = new Model(8l, "iPhone X", apple);
            var nokia_3310 = new Model(9l, "3310", nokia);

            modelRepo.saveAll(
                List.of(
                    s9, s8, nexus_6, oneplus_9, iphone_13,
                    iphone_12, iphone_11, iphone_10, nokia_3310));

            deviceRepo.save(new Device(1l, s9));
            deviceRepo.save(new Device(2l, s8));
            deviceRepo.save(new Device(3l, s8));
            deviceRepo.save(new Device(4l, nexus_6));
            deviceRepo.save(new Device(5l, oneplus_9));
            deviceRepo.save(new Device(6l, iphone_13));
            deviceRepo.save(new Device(7l, iphone_12));
            deviceRepo.save(new Device(8l, iphone_11));
            deviceRepo.save(new Device(9l, iphone_10));
            deviceRepo.save(new Device(10l, nokia_3310));
        };
    }
}
