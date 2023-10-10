
import * as readline from "node:readline/promises";
import { stdin as input, stdout as output } from 'node:process';



const rlx = new readline.Readline(output);
const rl = readline.createInterface({ input, output });
let answer = await rl.question('What do you think of Node.js? ');

console.log(`Thank you for your valuable feedback: ${answer}`);

answer = await rl.question('Again? \n');


rlx.moveCursor(0,-1);
console.log("Moved!!!", answer);
rl.close();