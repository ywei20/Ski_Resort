package RMQUtils;

import com.rabbitmq.client.Channel;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

/**
 * Manage RabbitMQ Channel Service
 **/
public class RMQChannelService {

    private GenericObjectPool<Channel> pool;

    public static RMQChannelService getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public Channel getChannel() throws Exception {
        return pool.borrowObject();
    }

    public void returnChannel(Channel channel) {
        pool.returnObject(channel);
    }

    private static class SingletonHolder {
        private final static RMQChannelService INSTANCE = new RMQChannelService();
    }

    private RMQChannelService() {
        initPool();
    }

    private void initPool() {
        RMQChannelPoolFactory factory = new RMQChannelPoolFactory();
        GenericObjectPoolConfig config = new GenericObjectPoolConfig();

        config.setMaxTotal(128);
        config.setMaxIdle(20);
        config.setMinIdle(10);

        pool = new GenericObjectPool<>(new RMQChannelPoolFactory(), config);
    }
}

