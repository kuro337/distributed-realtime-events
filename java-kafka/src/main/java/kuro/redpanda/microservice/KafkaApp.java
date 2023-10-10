package kuro.redpanda.microservice;
import kuro.redpanda.microservice.kafkautils.KafkaConsumerWrapper;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import kuro.redpanda.microservice.kafkautils.Producer;

import java.util.Scanner;

public class KafkaApp {
    private static final Logger log = LoggerFactory.getLogger(KafkaApp.class);

    public static String brokerURL = "redpanda-0.redpanda.redpanda.svc.cluster.local.:9093";

    public static void main(String[] args) {
        log.info("Hello World");

        Scanner scanner = new Scanner(System.in);
        log.info("Welcome to the Kafka Message Sender!");
        log.info("Enter your username:");
        String username = scanner.nextLine();

        Producer app = new Producer();
        app.connect(brokerURL);

        // can be any groupId
        KafkaConsumerWrapper consumer = new KafkaConsumerWrapper(brokerURL, "some-group-id", "chat-room");


        log.info("Send Event to Topic from Producer");

        while (true) {
            log.info("Enter a message, type 'poll' to poll events, or type 'exit' to quit:");
            String input = scanner.nextLine();

            if ("exit".equalsIgnoreCase(input)) {
                app.disconnect();
                log.info("Exiting application...");
                break;
            }

            if ("poll".equalsIgnoreCase(input)) {
                log.info("Polling events...");
                consumer.poll();
                // ... any additional logic
            } else {
                app.sendMessage("chat-room", username, input);
                log.info("Message sent!");
            }
        }
    }

}


// docker build -t redpanda-java-app:latest .
