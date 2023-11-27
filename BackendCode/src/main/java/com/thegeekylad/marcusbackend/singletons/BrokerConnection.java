package com.thegeekylad.marcusbackend.singletons;

import com.thegeekylad.marcusbackend.model.power.State;
import com.thegeekylad.marcusbackend.store.Tasks;
import com.thegeekylad.marcusbackend.util.Helpers;
import org.eclipse.paho.client.mqttv3.*;

import java.util.Calendar;
import java.util.UUID;

import static com.thegeekylad.marcusbackend.store.Constants.DEVICE_CLOUD_IP;

public class BrokerConnection {

    private IMqttClient client;

    // single object
    private static BrokerConnection brokerConnection;

    public BrokerConnection() {
        try {
//            String clientId = UUID.randomUUID().toString();
            String clientId = "marcuse-device-cloud";
            client = new MqttClient("tcp://" + DEVICE_CLOUD_IP + ":1883", clientId);

            MqttConnectOptions options = new MqttConnectOptions();
            options.setAutomaticReconnect(true);
            options.setCleanSession(true);

            client.connect();

            Helpers.log("Connected to MQTT broker.", false);

            // (re)register listeners
            registerListener_time();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static BrokerConnection getInstance() {
        if (brokerConnection == null
                || brokerConnection.getClient() == null
                || !brokerConnection.getClient().isConnected()) brokerConnection = new BrokerConnection();
        return brokerConnection;
    }

    public void registerListener_time() throws Exception {
        client.subscribe("marcus-server-time", new IMqttMessageListener() {
            @Override
            public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {

                client.unsubscribe("marcus-server-time");  // prevents spam

                String boardTopic = new String(mqttMessage.getPayload());

                String taskCommand = Tasks.TASK_TIME_REPORT + "::" + (isDayTime() ? "1" : "0");
                MqttMessage message = new MqttMessage(taskCommand.getBytes());
                message.setQos(0);
                message.setRetained(false);

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            client.publish(boardTopic, message);
                            Helpers.log("Served a 'time of day' request and it's '" + isDayTime() + "'.", true);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
                registerListener_time();
            }
        });

        Helpers.log("Registered time listener.", true);

    }

    private boolean isDayTime() {
        Calendar calendar = Calendar.getInstance();
//        return !(calendar.get(Calendar.HOUR_OF_DAY) > 17 || calendar.get(Calendar.HOUR_OF_DAY) == 17 && calendar.get(Calendar.MINUTE) >= 30);
        return calendar.get(Calendar.HOUR_OF_DAY) > 6
                && (calendar.get(Calendar.HOUR_OF_DAY) < 17
                || calendar.get(Calendar.HOUR_OF_DAY) == 17 && calendar.get(Calendar.MINUTE) < 30);
    }

    // getters and setters

    public IMqttClient getClient() {
        return client;
    }
}
