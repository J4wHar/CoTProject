package com.waspsecurity.waspsecurity.mqtt;


import com.waspsecurity.waspsecurity.Resources.AlertResource;
import com.waspsecurity.waspsecurity.entities.Alert;
import com.waspsecurity.waspsecurity.repositories.AlertRepository;
import jakarta.annotation.PostConstruct;
import jakarta.ejb.Singleton;
import jakarta.ejb.Startup;
import jakarta.inject.Inject;
import jakarta.json.bind.JsonbBuilder;
import org.eclipse.microprofile.config.Config;
import org.eclipse.microprofile.config.ConfigProvider;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import jakarta.json.bind.Jsonb;
import javax.net.ssl.SSLSocketFactory;
import java.util.UUID;

@Singleton
@Startup
public class MqttConnection {

    @Inject
    AlertRepository alertRepository;
    private static final Config config = ConfigProvider.getConfig();
    private final String uri = config.getValue("mqtt.uri", String.class);
    private final String username = config.getValue("mqtt.username", String.class);
    private final String password = config.getValue("mqtt.password", String.class);

    public void sendMessage(MqttClient client, String msg, String topic) throws MqttException {
        MqttMessage message = new MqttMessage(msg.getBytes());
        client.publish(topic,message);
    }

    @PostConstruct
    public void start() {
        try {
            MqttClient client = new MqttClient(
                    uri,
                    MqttClient.generateClientId(),
                    new MemoryPersistence());

            MqttConnectOptions mqttConnectOptions = new MqttConnectOptions();
            mqttConnectOptions.setUserName(username);
            mqttConnectOptions.setPassword(password.toCharArray());
            mqttConnectOptions.setConnectionTimeout(0);
            mqttConnectOptions.setSocketFactory(null);
            client.connect(mqttConnectOptions);
            //subscribing to all topics
            client.subscribe("#");
            client.setCallback(new MqttCallback() {
                @Override
                public void connectionLost(Throwable throwable) {
                }

                @Override
                public void messageArrived(String topic, MqttMessage mqttMessage) {
                    if (topic.equals("topic/camera")) {
                        System.out.println("Topic " + topic + " well received");
                        System.out.println("Message " + mqttMessage);
                        handleAlertMsg(mqttMessage);
                    }
                }
                private void handleAlertMsg(MqttMessage mqttMessage){
                    Jsonb jsonb = JsonbBuilder.create();
                    String jsonPayload = new String(mqttMessage.getPayload());
                    Alert alert = jsonb.fromJson(jsonPayload, Alert.class);
                    alertRepository.save(alert);
                }
                @Override
                public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
                    System.out.println("\n **************** \n");
                    System.out.println("delivery complete " + iMqttDeliveryToken);
                }
            });

            client.subscribe("test");
        }
        catch (MqttException e) {
            System.out.println(e.getMessage());
        }
    }

}
