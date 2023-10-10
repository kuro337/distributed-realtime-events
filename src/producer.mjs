import { Kafka } from "kafkajs";

const redpanda = new Kafka({
  brokers: ["redpanda-0.redpanda.redpanda.svc.cluster.local.:9093"],
});

const producer = redpanda.producer();
export async function getConnection(user) {
  try {
    await producer.connect();
    return async (message) => {
      await producer.send({
        topic: "chat-room",
        messages: [{ value: JSON.stringify({ message, user }) }],
      });
    };
  } catch (error) {
    console.error("Error:", error);
  }
}
export async function disconnect() {
  try {
    await producer.disconnect();
  } catch (error) {
    console.error("Error:", error);
  }
}
