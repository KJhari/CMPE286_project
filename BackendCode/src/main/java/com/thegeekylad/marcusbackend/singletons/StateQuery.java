package com.thegeekylad.marcusbackend.singletons;

import com.thegeekylad.marcusbackend.model.power.State;
import com.thegeekylad.marcusbackend.util.Helpers;
import org.eclipse.paho.client.mqttv3.IMqttMessageListener;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class StateQuery {

    private static StateQuery stateQuery;

    public StateQuery(State queryState) {
    }

    public static StateQuery getInstance() {
//        if (stateQuery == null) stateQuery = new StateQuery();
        return stateQuery;
    }

//    private void registerStateReportListener(State queryState) throws Exception {
//
//        BrokerConnection brokerConnection = BrokerConnection.getInstance();
//
//        if (brokerConnection.isRegisteredListener_stateQuery) return;
//        brokerConnection.getClient().subscribe("marcus-server-query", new IMqttMessageListener() {
//            @Override
//            public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {
//
//                String msg = new String(mqttMessage.getPayload());
//                String[] msgArr = msg.split("::");
//                String boardTopic = msgArr[0];
//                String statesStr = msgArr[1];
//                String[] states = msgArr[1].split(",");
//
//                Helpers.log("State report received from '" + boardTopic + "' switchboard: " + statesStr, true);
//                Helpers.log("Device of interest is '" + queryState.getDeviceId() +"' on '" + queryState.getBoardTopic() + "'", false);
//
//                // cache their current power states
//                for (String state : states) {
//                    String[] stateArr = state.split(":");
//                    int deviceId = Integer.parseInt(stateArr[0]);
//                    int powerState = Integer.parseInt(stateArr[1]);
//
//                    if (deviceId == queryState.getDeviceId()) {
//                        synchronized (queryState) {
//                            queryState.setState(powerState);
//                            queryState.notify();
//                        }
//                    }
//                }
//            }
//        });
//        isRegisteredListener_stateReport = true;
//        Helpers.log("Registered a listener for state report.", false);
//    }
}
