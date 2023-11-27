package com.thegeekylad.marcusbackend.controller;

import com.thegeekylad.marcusbackend.model.google_assistant.StateReport.Query;
import com.thegeekylad.marcusbackend.singletons.BrokerConnection;
import com.thegeekylad.marcusbackend.store.Actions;
import com.thegeekylad.marcusbackend.store.Tasks;
import com.thegeekylad.marcusbackend.model.SimpleResponse;
import com.thegeekylad.marcusbackend.model.alexa.StateReport.*;
import com.thegeekylad.marcusbackend.model.power.State;
import com.thegeekylad.marcusbackend.singletons.StatesCache;
import com.thegeekylad.marcusbackend.util.Helpers;
import org.eclipse.paho.client.mqttv3.*;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.Map;

@RestController
@RequestMapping("/query")
public class QueryController {

    private static final long TIMEOUT_QUERY_WAIT = 10000;  // sometimes the switchboards don't reply

    private IMqttMessageListener listener;

    @GetMapping("/alexa")
    public synchronized SimpleResponse alexaQuery(@RequestParam String endpointId) {
        try {
            Helpers.log("State report request received from Alexa for '" + endpointId + "'.", true);
            String[] endpointIdArr = endpointId.split(":");
            String boardTopic = endpointIdArr[0];
            int deviceId = Integer.parseInt(endpointIdArr[1]);

            // payload coming from state report
            final State queryState = new State(deviceId, boardTopic);

            // TODO enable if you're caching
            // get cached state from hashmap (or assume device is OFF)
//        int currentState = StatesCache.getInstance().states.getOrDefault(getUniqueDeviceIdentifier(boardTopic, deviceId), -1);

            // TODO enable if you're caching
//        return currentState == -1 ? null : getResponse(endpointId, currentState);

            State stateReport = getStateReport(queryState);
            SimpleResponse response = getAlexaQueryResponse(endpointId, stateReport.getState());
            Helpers.log(String.format("Prepared state report for device '%d' on switchboard '%s'.", stateReport.getDeviceId(), stateReport.getBoardTopic()), false);
            return response;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @GetMapping("/googleAssistant")
    public synchronized SimpleResponse googleAssistantQuery(@RequestParam String id) {
        try {
            Helpers.log("State report request received from Google Assistant for '" + id + "'.", true);
            String[] deviceIdArr = id.split(":");
            String boardTopic = deviceIdArr[0];
            int deviceId = Integer.parseInt(deviceIdArr[1]);

            // payload coming from state report
            final State queryState = new State(deviceId, boardTopic);

            // TODO enable if you're caching
            // get cached state from hashmap (or assume device is OFF)
//        int currentState = StatesCache.getInstance().states.getOrDefault(getUniqueDeviceIdentifier(boardTopic, deviceId), -1);

            // TODO enable if you're caching
//        return currentState == -1 ? null : getResponse(endpointId, currentState);

            State stateReport = getStateReport(queryState);
            SimpleResponse response = getAssistantQueryResponse(id, stateReport.getState());
            Helpers.log(String.format("Prepared state report for device '%d' on switchboard '%s'.", stateReport.getDeviceId(), stateReport.getBoardTopic()), false);
            return response;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @GetMapping("/time")
    public String timeQuery() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.HOUR_OF_DAY) >= 19 ? "0" : "1";
    }

    @GetMapping("/cache")
    public Map<String, Integer> cacheQuery() {
        return StatesCache.getInstance().states;
    }

    private SimpleResponse getAlexaQueryResponse(String endpointId, int currentState) {
        return new SimpleResponse(
                Actions.POWER_STATE_QUERY,
                new StateReport(
                        new Context(
                                new Property[]{
                                        new Property(
                                                "Alexa.PowerController",
                                                "powerState",
                                                currentState == 1
                                                        ? "ON"
                                                        : "OFF",
                                                Instant.from(ZonedDateTime.of(LocalDateTime.now(), ZoneId.systemDefault())).toString(),
                                                50
                                        )
                                }
                        ),
                        new Event(
                                new Endpoint(
                                        endpointId
                                )
                        )
                ),
                null);
    }

    private SimpleResponse getAssistantQueryResponse(String id, int currentState) {
        return new SimpleResponse(
                Actions.POWER_STATE_QUERY,
                new Query(currentState == 1),
                null);
    }

    public State getStateReport(final State queryState) throws Exception {

        IMqttClient client = BrokerConnection.getInstance().getClient();

        // decide what command needs to be run
        String taskCommand = String.valueOf(Tasks.TASK_STATE_REPORT);
        MqttMessage message = new MqttMessage(taskCommand.getBytes());
        message.setQos(0);
        message.setRetained(false);

        Helpers.log("Sent report state query to '" + queryState.getBoardTopic() + "' switchboard.", false);

        client.subscribe("marcus-server-query", new IMqttMessageListener() {
            @Override
            public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {

                String msg = new String(mqttMessage.getPayload());
                String[] msgArr = msg.split("::");
                String boardTopic = msgArr[0];
                String statesStr = msgArr[1];
                String[] states = msgArr[1].split(",");

                Helpers.log("State report received from '" + boardTopic + "' switchboard: " + statesStr, false);
                Helpers.log("Device of interest is '" + queryState.getDeviceId() +"' on '" + queryState.getBoardTopic() + "'", false);

                // notify their current power states
                for (String state : states) {
                    String[] stateArr = state.split(":");
                    int deviceId = Integer.parseInt(stateArr[0]);
                    int powerState = Integer.parseInt(stateArr[1]);

                    if (deviceId == queryState.getDeviceId()) {
                        synchronized (queryState) {
                            client.unsubscribe("marcus-server-query");
                            queryState.setState(powerState);
                            queryState.notifyAll();
                        }
                    }
                }
            }
        });

        // let this device's board know we're looking for a state report
        client.publish(queryState.getBoardTopic(), message);

        synchronized (queryState) {
            queryState.wait(TIMEOUT_QUERY_WAIT);
        }

        return queryState;
    }

//    private static class StateReportListener implements IMqttMessageListener {
//
//        private State state;
//
//        StateReportListener(State state) {
//            this.state = state;
//        }
//
//        @Override
//        public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {
//
//        }
//    }

    // wraps around State just to provide
//    private static class StateReportPayload {
//        private State state;
//        public State getState() {
//            return state;
//        }
//        public void setState(State state) {
//            this.state = state;
//        }
//    }
}
