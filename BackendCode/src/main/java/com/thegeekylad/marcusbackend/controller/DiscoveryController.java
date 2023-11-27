package com.thegeekylad.marcusbackend.controller;

import com.thegeekylad.marcusbackend.dao.DeviceRepository;
import com.thegeekylad.marcusbackend.model.Device;
import com.thegeekylad.marcusbackend.model.alexa.discovery.Capability;
import com.thegeekylad.marcusbackend.model.alexa.discovery.Property;
import com.thegeekylad.marcusbackend.model.alexa.discovery.Supported;
import com.thegeekylad.marcusbackend.model.google_assistant.discovery.Attributes;
import com.thegeekylad.marcusbackend.model.google_assistant.discovery.Name;
import com.thegeekylad.marcusbackend.util.Helpers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

import static com.thegeekylad.marcusbackend.util.Helpers.getTimestamp;

@RestController()
@RequestMapping("/discover")
public class DiscoveryController {

    @Autowired
    private DeviceRepository deviceRepository;

    @GetMapping("/")
    public List<Device> discover() {
        return deviceRepository.findAll();
    }

    @GetMapping("/alexa")
    public List<com.thegeekylad.marcusbackend.model.alexa.discovery.Device> alexa() {

        Helpers.log("Discovery request received from Alexa.", true);

        List<Device> devices = deviceRepository.findAll();
        List<com.thegeekylad.marcusbackend.model.alexa.discovery.Device> alexaDevices = new ArrayList<>();
        for (Device device : devices) {
            com.thegeekylad.marcusbackend.model.alexa.discovery.Device alexaDevice = new com.thegeekylad.marcusbackend.model.alexa.discovery.Device(
                    device.boardTopic + ":" + device.deviceId,
                    "Marcus Smart Home",
                    device.board + " " + device.device,
                    device.description,
                    new String[] {
                            device.type
                    },
                    new Capability[]{
                            new Capability(
                                    "AlexaInterface",
                                    "Alexa.PowerController",
                                    "3",
                                    new Property(
                                            new Supported[] {
                                                    new Supported("powerState")
                                            },
                                            true
                                    )
                            )
                    }
            );
            alexaDevices.add(alexaDevice);
        }

        return alexaDevices;
    }

    @GetMapping("/googleAssistant")
    public List<com.thegeekylad.marcusbackend.model.google_assistant.discovery.Device> googleAssistant() {

        Helpers.log("Discovery request received from Google Assistant.", true);

        List<Device> devices = deviceRepository.findAll();
        List<com.thegeekylad.marcusbackend.model.google_assistant.discovery.Device> assistantDevices = new ArrayList<>();
        for (Device device : devices) {
            com.thegeekylad.marcusbackend.model.google_assistant.discovery.Device assistantDevice = new com.thegeekylad.marcusbackend.model.google_assistant.discovery.Device(
                    device.boardTopic + ":" + device.deviceId,
                    "action.devices.types." + device.type,
                    new String[] {
                            "action.devices.traits.OnOff"
                    },
                    new Name(
                            new String[] {
                                    device.getBoard() + " " + device.getDevice()
                            },
                            device.getBoard() + " " + device.getDevice(),
                            new String[] {
                                    device.getBoard() + " " + device.getDevice()
                            }
                    ),
                    true,
                    new Attributes(true)
            );
            assistantDevices.add(assistantDevice);
        }

        return assistantDevices;
    }
}
