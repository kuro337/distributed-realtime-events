import * as readline from "node:readline/promises";
import { stdin as input, stdout as output } from "node:process";

import * as Admin from "./admin.mjs";
import * as Producer from "./producer.mjs";
import * as Consumer from "./consumer.mjs";

// const rl = readline.createInterface({
//   input: process.stdin,
//   output: process.stdout,
// });

const rl = readline.createInterface({ input, output });

async function start() {
  const topic = "chat-room";
  console.log(`Creating topic: ${topic}`);
  await Admin.createTopic(topic);

  console.log("Connecting...");
  await Consumer.connect();

  const username = await rl.question("Enter user name: ");

  const sendMessage = await Producer.getConnection(username);
  if (sendMessage) {
    console.log("Connected, press Ctrl+C to exit");
    rl.on("line", (input) => {
      // readline.moveCursor(process.stdout, 0, -1);
      sendMessage(input);
    });
  } else {
    console.error("Failed to initialize sendMessage function");
  }
}

start();
process.on("SIGINT", async () => {
  console.log("Closing app...");
  try {
    await Producer.disconnect();
    await Consumer.disconnect();
    rl.close();
  } catch (err) {
    console.error("Error during cleanup:", err);
    process.exit(1);
  } finally {
    console.log("Cleanup finished. Exiting");
    process.exit(0);
  }
});
