import com.google.gson.Gson;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import java.io.IOException;
import java.util.concurrent.TimeoutException;


public class Consumer {

    private static Gson gson;
    private static LiftRideDao liftRideDao;

    private static String HostName = "35.163.51.25";
    private static int PortName = 5672;
    private static String UserName = "admin";
    private static String PassWord = "admin";
    private static String QueueName = "skiers_queue";
    private static String ExchangeName = "lift_exchange";
    private static int numThreads;

    public static void main(String[] args)
            throws IOException, TimeoutException, InterruptedException {
        gson = new Gson();
        liftRideDao = new LiftRideDao();
        numThreads = Integer.parseInt(args[0]);
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(HostName);
        factory.setPort(PortName);
        factory.setUsername(UserName);
        factory.setPassword(PassWord);
        Connection connection = factory.newConnection();

        for (int i = 0; i < numThreads; i++) {
            Thread thread = new Thread(new Runnable() {
                Channel channel = null;
                @Override
                public void run() {
                    try {
                        channel = connection.createChannel();
                        channel.exchangeDeclare(ExchangeName, BuiltinExchangeType.FANOUT);
                        channel.queueDeclare(QueueName, true, false, false, null);
                        channel.queueBind(QueueName, ExchangeName, "");
                        channel.basicQos(1);

                        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                            String message = new String(delivery.getBody(), "UTF-8");
                            channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);

                            LiftRide liftRide = gson.fromJson(message, LiftRide.class);
                            liftRide.setVertical(100); // manually set vertical value to 100 based on Piazza post
                            liftRideDao.createLiftRide(liftRide);

                        };

                        channel.basicConsume(QueueName, false, deliverCallback, consumertag -> {});

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            thread.start();
        }

    }
}
