package com.thegeekylad.marcusbackend.util;

import org.eclipse.paho.client.mqttv3.*;

import java.sql.Timestamp;
import java.util.UUID;

import static com.thegeekylad.marcusbackend.store.Constants.DEVICE_CLOUD_IP;

public class Helpers {

    // TODO remove exception from inside and throw it from the method defintion
//    public static void requestStatesFromSwitchboards(DeviceRepository deviceRepository) {
//
//        // maybe it's a new home!
//        if (deviceRepository == null) {
//            Helpers.log("Empty device repository.", false);
//            return;
//        }
//
//        // get all board topics
//        List<Device> devices = deviceRepository.findAll();
//        List<String> boardTopics = new ArrayList<>();
//        for (Device device : devices)
//            if (!boardTopics.contains(device.getBoardTopic()))
//                boardTopics.add(device.getBoardTopic());
//
//        try {
//            IMqttClient client = createMqttConnection();
//
//            // decide what command needs to be run
//            String taskCommand = String.valueOf(Tasks.TASK_STATE_REPORT);
//            MqttMessage message = new MqttMessage(taskCommand.getBytes());
//            message.setQos(0);
//            message.setRetained(false);
//
//            // publish to all boards asynchronously
//            for (String boardTopic : boardTopics)
//                client.publish(boardTopic, message);
//
//            Helpers.log("Sent report state query to all " + boardTopics.size() + " switchboards.", false);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    public static String getTimestamp() {
        return new Timestamp(System.currentTimeMillis()).toString();
    }

    public static String getUniqueDeviceIdentifier(String boardTopic, int deviceId) {
        return boardTopic + ":" + deviceId;
    }

    public static void log(Object msg, boolean isMain) {
        if (isMain) System.out.println();
        System.out.printf("[%s] %s%n", getTimestamp(), msg);
    }
}
