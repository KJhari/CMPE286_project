package com.thegeekylad.marcusbackend;

import com.thegeekylad.marcusbackend.singletons.BrokerConnection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MarcusBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(MarcusBackendApplication.class, args);

		// connect to broker and subscribe to some listeners
		BrokerConnection.getInstance();

		// TODO enable this you're caching
		// listen for power state / query update broadcasts
//		try {
//			IMqttClient client = createMqttConnection();
//
//			client.subscribe("marcus-server-query", new IMqttMessageListener() {
//				@Override
//				public synchronized void messageArrived(String s, MqttMessage mqttMessage) throws Exception {
//
//					String msg = new String(mqttMessage.getPayload());
//					String[] msgArr = msg.split("::");
//					String boardTopic = msgArr[0];
//					String statesStr = msgArr[1];
//					String[] states = msgArr[1].split(",");
//
//					Helpers.log("Received a state report from '" + boardTopic + "' switchboard: " + statesStr, true);
//
//					// cache their current power states
//					for (String state : states) {
//						String[] stateArr = state.split(":");
//						int deviceId = Integer.parseInt(stateArr[0]);
//						int powerState = Integer.parseInt(stateArr[1]);
//
//						StatesCache.getInstance().states.put(
//								getUniqueDeviceIdentifier(boardTopic, deviceId),
//								powerState);
//
//
//					}
//				}
//			});
//		} catch (MqttException e) {
//			e.printStackTrace();
//		}

		// start with polling all switchboards and caching the current / default power state of all switches
//		requestStatesFromSwitchboards(deviceRepository);
	}

}
