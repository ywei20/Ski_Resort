import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import java.io.IOException;
import java.util.concurrent.TimeoutException;

        publicclassSenderTest{
        publicstaticvoidmain(String[]args)throwsIOException,TimeoutException{
        ConnectionFactoryfactory=newConnectionFactory();
        factory.setHost("
        ");
        factory.setPort(5672);
        factory.setUsername("admin");
        factory.setPassword("admin");

        try(Connectionconnection=factory.newConnection()){
        for(inti=0;i<5;i++){
        Channelchannel=connection.createChannel();

//declareaqueue
        channel.queueDeclare("hello",false,false,false,null);

//sendmessagetothequeue
        Stringmessage="the"+i+"thmessage";

        channel.basicPublish("","hello",false,null,message.getBytes());
        System.out.println(i+"thmessagehasbeensent");
        }

        }catch(IOExceptione){
        e.printStackTrace();
        }
        }
        }
