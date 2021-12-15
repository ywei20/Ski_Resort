package Model;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;

public class ChannelFactory extends BasePooledObjectFactory<Channel> {

    private Connection connection;

    public ChannelFactory(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Channel create() throws Exception {
        return connection.createChannel();
    }

    /**
     * Use the default PooledObject implementation
     */
    @Override
    public PooledObject<Channel> wrap(Channel channel) {
        return new DefaultPooledObject<>(channel);
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

//    public ChannelFactory() {
//        super();
//    }
//
//    /**
//     * When a channel is returned to the pool, close the channel
//     */
//    @Override
//    public void passivateObject(PooledObject<Channel> pooledObject) throws Exception {
//        Channel channel = pooledObject.getObject();
//        if (channel.isOpen()) {
//            try {
//                channel.close();
//            } catch (Exception e) {
//
//            }
//        }    }

//    @Override
//    public void destroyObject(PooledObject<Channel> pooledObject) throws Exception {
//        Channel channel = pooledObject.getObject();
//        if (channel.isOpen()) {
//            try {
//                channel.close();
//            } catch (Exception e) {
//
//            }
//        }
//    }
}
