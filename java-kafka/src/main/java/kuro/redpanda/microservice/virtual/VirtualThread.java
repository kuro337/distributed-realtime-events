package kuro.redpanda.microservice.virtual;

    public class VirtualThread {

        public static void runSimpleThread() {
              Thread thread = Thread
                    .ofVirtual()
                    .start(() -> System.out.println("Hello World!"));
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        public static void runNamedThread() {
            Thread.Builder builder = Thread.ofVirtual().name("MyThread");
            Runnable task = () -> {
                System.out.println("Running thread");
            };
            Thread t = builder.start(task);
            System.out.println("Thread t name: " + t.getName());
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        public static void executeTwoThreads() {
            Thread.Builder builder = Thread.ofVirtual().name("worker-", 0);
            Runnable task = () -> {
                System.out.println("Thread ID: " + Thread.currentThread().threadId());
            };

            // name "worker-0"
            Thread t1 = builder.start(task);
            try {
                t1.join();
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            System.out.println(t1.getName() + " terminated");

            // name "worker-1"
            Thread t2 = builder.start(task);
            try {
                t2.join();
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            System.out.println(t2.getName() + " terminated");
        }
    }


