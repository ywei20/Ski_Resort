import com.google.gson.Gson;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicInteger;


public class Consumer {

    private static Gson gson;
    private static Map<Integer, List<LiftRide>> map;

    private static String HostName = "3.83.23.114";
    private static int PortName = 5672;
    private static String UserName = "admin";
    private static String PassWord = "admin";
    private static String QueueName = "skiers_queue";
    private static int numThreads;

    public static void main(String[] args)
            throws IOException, TimeoutException, InterruptedException {
        map = new ConcurrentHashMap<Integer, List<LiftRide>>();
        gson = new Gson();
        numThreads = Integer.parseInt(args[0]);
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(HostName);
        factory.setPort(PortName);
        factory.setUsername(UserName);
        factory.setPassword(PassWord);
        Connection connection = factory.newConnection();
        AtomicInteger count = new AtomicInteger(0);

        for (int i = 0; i < numThreads; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Channel channel = connection.createChannel();
                        channel.queueDeclare(QueueName, true, false, false, null);
                        channel.basicQos(1);

                        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                            String message = new String(delivery.getBody(), "UTF-8");
//                        System.out.println("message is: " + message);
                            channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
                            LiftRide liftRide = gson.fromJson(message, LiftRide.class);
                            int skierID = liftRide.getSkierID();
                            if (!map.containsKey(skierID)) {
                                map.put(skierID, new ArrayList<>());
                            }
                            map.get(skierID).add(liftRide);
                        };

                        channel.basicConsume(QueueName, false, deliverCallback, consumertag -> {
                        });

                        count.getAndIncrement();
                        if (count.get() % 1000 == 0) {
                            System.out.println("pulling " + count.get() + "th message");
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }

    }
}
