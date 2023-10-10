package kuro.redpanda.microservice.kafkautils;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

public class KafkaConsumerWrapper {
    private static final Logger log = LoggerFactory.getLogger(KafkaConsumerWrapper.class);

    private final String topic;
    private final Consumer<String, String> consumer;
    private final List<String> messages;

    public KafkaConsumerWrapper(String brokerURL, String groupId, String topic) {
        this.topic = topic;
        this.consumer = createConsumer(brokerURL, groupId);
        this.messages = new ArrayList<>();
        this.consumer.subscribe(Collections.singletonList(this.topic));
        this.consumer.poll(Duration.ofMillis(1000));
    }

    public void poll() {
        ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(1000));
        records.forEach(record -> {
            log.info("Received record with key {} and value {}", record.key(), record.value());
            messages.add(record.value());
        });
    }

    public List<String> getMessages() {
        return messages;
    }

    public void close() {
        consumer.close();
    }

    private static Consumer<String, String> createConsumer(String brokerURL, String groupId) {
        Properties properties = new Properties();
        properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, brokerURL);
        properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest"); // Read from the beginning of the topic

        return new KafkaConsumer<>(properties);
    }
}
