package akkas;

import java.util.Properties;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.ActorSelection;
import akka.actor.ActorSystem;
import akka.actor.Props;

public class Server extends AbstractActor {

    @Override
    public Receive createReceive() {
        return receiveBuilder().matchAny(msg -> {
            System.out.println(self() + " 收到消息 from " + sender() + " :" + msg);
//格式：akka.<protocol>://<actor system name>@<hostname>:<port>/<actor path>
            ActorSelection as = getContext().actorSelection("akka.tcp://test-client@127.0.0.1:6666/user/client");
            as.tell("server >>>>>>>>>>>>>>>>> " + System.currentTimeMillis(), self());
        }).build();
    }

    public static void main(String[] args) {
        Properties p = new Properties();
        p.setProperty("akka.actor.provider", "akka.remote.RemoteActorRefProvider");
        p.setProperty("akka.remote.netty.tcp.hostname", "127.0.0.1");
        p.setProperty("akka.remote.netty.tcp.port", "5555");
        Config conf = ConfigFactory.parseProperties(p);
        ActorSystem system = ActorSystem.create("test-server", conf);
        // ActorSystem system = ActorSystem.create("test-server");
        ActorRef ref = system.actorOf(Props.create(Server.class), "server");
        ref.tell("server ....................", ActorRef.noSender());
    }

}
