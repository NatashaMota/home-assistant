package br.com.openlabs.home_assistant.infra.web;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.springframework.stereotype.Service;

@Service
public class MQTTService {

    private static final String BROKER_URL = "tcp://broker.hivemq.com:1883";
    private static final String CLIENT_ID = "home-assistant";

    private MqttClient client;

    public MQTTService() {
        try {
            client = new MqttClient(BROKER_URL, CLIENT_ID);
            client.connect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void publish(String topic, String message) {
        try {
            client.publish(topic, message.getBytes(), 0, false);
            System.out.println("Published: " + message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
