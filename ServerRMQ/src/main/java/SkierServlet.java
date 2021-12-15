import com.google.gson.Gson;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.ObjectPool;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;


@WebServlet(name = "SkierServlet")
public class SkierServlet extends HttpServlet {

    private Gson gson;
    private Connection connection;
    private ObjectPool<Channel> channelPool;

    private static String HostName = "35.163.51.25";
    private static int PortName = 5672;
    private static String UserName = "admin";
    private static String PassWord = "admin";
    private static int MAX_OBJ = 128;
    private static int MAX_IDLE = 128;
    private static String QueueName = "post_queue";

    public static class LiftRide {
        private int time;
        private int liftID;
        private int skierID;
        private int resortID;
        private int seasonID;
        private int dayID;


        public LiftRide(int time, int liftID, int skierID, int resortID, int seasonID, int dayID) {
            this.time = time;
            this.liftID = liftID;
            this.skierID = skierID;
            this.resortID = resortID;
            this.seasonID = seasonID;
            this.dayID = dayID;
        }

        public void setTime(int time) {
            this.time = time;
        }

        public void setLiftID(int liftID) {
            this.liftID = liftID;
        }

        public void setSkierID(int skierID) {
            this.skierID = skierID;
        }

        public void setResortID(int resortID) {
            this.resortID = resortID;
        }

        public void setSeasonID(int seasonID) {
            this.seasonID = seasonID;
        }

        public void setDayID(int dayID) {
            this.dayID = dayID;
        }
    }

    public class ChannelFactory extends BasePooledObjectFactory<Channel> {

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
    }

    @Override
    public void init() throws ServletException {
        gson = new Gson();
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(HostName);
        factory.setPort(PortName);
        factory.setUsername(UserName);
        factory.setPassword(PassWord);
        try {
            connection = factory.newConnection();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }

        GenericObjectPoolConfig<Channel> config = new GenericObjectPoolConfig<>();
        config.setMaxTotal(MAX_OBJ);
        config.setMaxIdle(MAX_IDLE);
        channelPool = new GenericObjectPool<>(new ChannelFactory(), config);
//        channelPool = new GenericObjectPool<>(new ChannelFactory());

    }

    protected void doPost(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        String urlPath = request.getPathInfo();

        // check we have a URL!
        if (urlPath == null || urlPath.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            response.getWriter().write("Missing Paramterers");
            return;
        }

        String[] urlParts = urlPath.split("/");
        // and now validate url path and return the response status code
        // (and maybe also some value if input is valid)

        if (!isPostUrlValid(urlParts)) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("Invalid URL Address");
        } else {
            Channel channel = null;
            try {
                channel = channelPool.borrowObject();
//                channel = connection.createChannel();
                // valid post url pattern: /{resortID}/seasons/{seasonID}/days/{dayID}/skiers/{skierID}
                LiftRide liftRide = gson.fromJson(request.getReader(), LiftRide.class);
                liftRide.setResortID(Integer.parseInt(urlParts[1]));
                liftRide.setSeasonID(Integer.parseInt(urlParts[3]));
                liftRide.setDayID(Integer.parseInt(urlParts[5]));
                liftRide.setSkierID(Integer.parseInt(urlParts[7]));
                String message = gson.toJson(liftRide);

                channel.queueDeclare(QueueName, true, false, false, null);
                channel.basicPublish("", QueueName, null, message.getBytes(StandardCharsets.UTF_8));

                response.getWriter().write(gson.toJson(liftRide));
                response.setStatus(HttpServletResponse.SC_CREATED);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (null != channel) {
                        channelPool.returnObject(channel);
//                        channel.close();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }
    }

    protected void doGet(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {
            response.setContentType("text/plain");
            String urlPath = request.getPathInfo();

            // check we have a URL!
            if (urlPath == null || urlPath.isEmpty()) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                response.getWriter().write("Missing Paramterers");
                return;
            }

            String[] urlParts = urlPath.split("/");
            // and now validate url path and return the response status code
            // (and maybe also some value if input is valid)

            if (!isGetUrlValid(urlParts)) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("Invalid URL Address");
            } else {
                response.setStatus(HttpServletResponse.SC_OK);
                // do any sophisticated processing with urlParts which contains all the url params
                // TODO: process url params in `urlParts`
                response.getWriter().write("It works!");
            }
    }

    private boolean isPostUrlValid(String[] urlParts) {
        // TODO: validate the request post url path according to the API spec
        // valid post url pattern: /{resortID}/seasons/{seasonID}/days/{dayID}/skiers/{skierID}
        // urlPath  = "/1/seasons/2019/days/1/skiers/123"
        // urlParts = [, 1, seasons, 2019, days, 1, skiers, 123]
        if (urlParts.length != 8) {
            return false;
        }
        try {
            int resortId = Integer.parseInt(urlParts[1]);
            int season = Integer.parseInt(urlParts[3]);
            int day = Integer.parseInt(urlParts[5]);
            int skierId = Integer.parseInt(urlParts[7]);
            return urlParts[2].equals("seasons") && urlParts[4].equals("days") && urlParts[6].equals("skiers")
                    && day >= 1 && day <= 366 && season >= 1990 && season <= 2999;
        } catch (NumberFormatException nfe){
            return false;
        }
    }

    private boolean isGetUrlValid(String[] urlParts) {
        // TODO: validate the request get url path according to the API spec
        // valid get url pattern: 1. /{resortID}/seasons/{seasonID}/days/{dayID}/skiers/{skierID}
        //                         2. /{skierID}/vertical
        if (urlParts.length == 8) {
            return isPostUrlValid(urlParts);
        } else if (urlParts.length == 3) {
            try {
                Integer.parseInt(urlParts[1]);
                return urlParts[2].equals("vertical");
            } catch (NumberFormatException nfe) {
                return false;
            }
        } else {
            return false;
        }
    }

}
