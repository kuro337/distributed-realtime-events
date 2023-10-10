package kuro.redpanda.microservice.kafkautils;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

public class Producer {
    private static final Logger log = LoggerFactory.getLogger(Producer.class);

    private KafkaProducer<String, String> producer;

    public void connect(String brokerURL) {
        producer = Producer.createProducer(brokerURL);
    }

    public void sendMessage(String topic, String user, String message) {
        String jsonMessage = String.format("{\"message\":\"%s\", \"user\":\"%s\"}", message, user);
        ProducerRecord<String, String> record = Producer.createProducerRecord(topic, jsonMessage);
        Producer.sendDataAsync(producer, record);
    }

    public void disconnect() {
        producer.close();
    }


    public  static KafkaProducer<String, String> createProducer(String brokerURL) {
        // create Producer properties
        Properties properties = new Properties();
        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, brokerURL);
        properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

        // create the producer
        KafkaProducer<String, String> producer = new KafkaProducer<>(properties);
        return producer;
    }

    public static ProducerRecord<String, String> createProducerRecord(String topicName , String event) {
        ProducerRecord<String, String> producerRecord =
                new ProducerRecord<>(topicName, event);
        return producerRecord;
    }

    public static void sendDataAsync( KafkaProducer<String, String> producer ,
                               ProducerRecord<String, String> producerRecord) {
        // send data - asynchronous
        producer.send(producerRecord);

        // flush data - synchronous
        producer.flush();

    }
}
