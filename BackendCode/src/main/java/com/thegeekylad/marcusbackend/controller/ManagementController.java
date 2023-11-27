package com.thegeekylad.marcusbackend.controller;

import com.thegeekylad.marcusbackend.store.Actions;
import com.thegeekylad.marcusbackend.dao.DeviceRepository;
import com.thegeekylad.marcusbackend.model.Device;
import com.thegeekylad.marcusbackend.model.Devices;
import com.thegeekylad.marcusbackend.model.SimpleResponse;
import com.thegeekylad.marcusbackend.util.Helpers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@RestController
@RequestMapping("/devices")
public class ManagementController {

    @Autowired
    private DeviceRepository deviceRepository;

    @PostMapping("/add")
    public SimpleResponse addDevices(@RequestBody(required = true) Devices devices) {
        SimpleResponse response = new SimpleResponse(Actions.DEVICE_ADDED, null, null);
        List<Device> deviceList = new ArrayList<>();
        try {
            for (Device device : devices.getDevices()) {
                device.setBoardTopic("marcus-switchboard-" + device.getBoard().toLowerCase(Locale.ROOT).replace(' ', '-'));
                deviceList.add(device);
            }
            deviceRepository.saveAll(deviceList);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // TODO enable this if you're caching
        // reload cache as we've just added new devices
//        Helpers.requestStatesFromSwitchboards(deviceRepository);

        return response;
    }
}
