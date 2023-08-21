package akkas;

import java.util.Properties;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.ActorSelection;
import akka.actor.ActorSystem;
import akka.actor.Props;

/**
 * @Author:lmq
 * @Date: 2019/12/31
 * @Desc:
 **/
public class Client extends AbstractActor {
    @Override
    public Receive createReceive() {
        return receiveBuilder().match(String.class, msg -> {
            System.out.println(self() + " 收到消息 from " + sender() + " :" + msg);
//格式：akka.<protocol>://<actor system name>@<hostname>:<port>/<actor path>
            ActorSelection as = getContext().actorSelection("akka.tcp://test-server@127.0.0.1:5555/user/server");
            as.tell("client ..............." + System.currentTimeMillis(), self());
        }).matchAny(msg -> System.out.println(msg)).build();
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        Properties p = new Properties();
        p.setProperty("akka.actor.provider", "akka.remote.RemoteActorRefProvider");
        p.setProperty("akka.remote.netty.tcp.hostname", "127.0.0.1");
        p.setProperty("akka.remote.netty.tcp.port", "6666");
        Config conf = ConfigFactory.parseProperties(p);
        ActorSystem system = ActorSystem.create("test-client", conf);
        ActorRef ref = system.actorOf(Props.create(Client.class), "client");
        ref.tell("client ...............", ActorRef.noSender());
    }
}
