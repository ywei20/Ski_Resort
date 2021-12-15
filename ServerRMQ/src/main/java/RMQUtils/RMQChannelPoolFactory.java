package RMQUtils;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.DefaultPooledObject;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 Define a rabbitmq channel pool factory
 **/
public class RMQChannelPoolFactory implements PooledObjectFactory<Channel> {

    private Connection connection;

    public RMQChannelPoolFactory(){
        try {
            ConnectionFactory factory = new ConnectionFactory();

            factory.setHost("35.163.51.25");
            factory.setPort(5672);
            factory.setUsername("admin");
            factory.setPassword("admin");
            factory.setVirtualHost("/");
            factory.setConnectionTimeout(5000);
            connection = factory.newConnection();
        } catch (IOException | TimeoutException e) {
            e.printStackTrace();
        }
    }

    @Override
    public PooledObject<Channel> makeObject() throws Exception {
        return new DefaultPooledObject<>(connection.createChannel());
    }

    @Override
    public void destroyObject(PooledObject<Channel> p) throws Exception {
        if (p != null && p.getObject() != null && p.getObject().isOpen()) {
            p.getObject().close();
        }
    }

    @Override
    public boolean validateObject(PooledObject<Channel> p) {
        return p.getObject() != null && p.getObject().isOpen();
    }

    @Override
    public void activateObject(PooledObject<Channel> p) {

    }

    @Override
    public void passivateObject(PooledObject<Channel> p) {

    }
}
