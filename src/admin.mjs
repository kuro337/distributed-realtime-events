import { Kafka } from "kafkajs";

const redpanda = new Kafka({
  brokers: ["redpanda-0.redpanda.redpanda.svc.cluster.local.:9093"],
});

const admin = redpanda.admin();

export async function createTopic(topic, partitions, replicas) {
  await admin.connect();
  const existingTopics = await admin.listTopics();

  if (!!existingTopics) {
    console.log(`We have ${existingTopics.length} topics`);

    console.log();

    existingTopics.forEach((element) => {
      console.log("   > " + element);
    });
  } else {
    console.log("We don't have any topics!!");
  }

  if (!existingTopics.includes(topic)) {
    await admin.createTopics({
      topics: [
        {
          topic: topic,
          numPartitions: partitions ? partitions : 1,
          replicationFactor: replicas ? replicas : 1,
        },
      ],
    });
  }
  await admin.disconnect();
}
