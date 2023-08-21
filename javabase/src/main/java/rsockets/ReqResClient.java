package rsockets;

import io.rsocket.Payload;
import io.rsocket.RSocket;
//import io.rsocket.RSocketFactory;
import io.rsocket.transport.netty.client.TcpClientTransport;
import io.rsocket.util.DefaultPayload;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @Author:qishan
 * @Date: 2019-08-08
 * @Desc:
 **/
public class ReqResClient {

    private static final int TCP_PORT = 7777;
//    private final RSocket socket;

    public ReqResClient() {
//        this.socket = RSocketFactory.connect()
//                .transport(TcpClientTransport.create("localhost", TCP_PORT))
//                .start()
//                .block();
//        RSocket rsocket2 = RSocketFactory.connect()
//                .dataMimeType("application/json")
//                .metadataMimeType("application/json")
//                .transport(TcpClientTransport.create("localhost", 42252))
//                .start()
//                .block();
//        rsocket2.requestResponse(DefaultPayload.create("xxxx"));
    }

//    public String callBlocking(String string) {
//        return socket
//                .requestResponse(DefaultPayload.create(string))
//                .map(Payload::getDataUtf8)
//                .block();
//    }
//
//    public void dispose() {
//        this.socket.dispose();
//    }

//    @Test
//    public void whenSendingAString_thenRevceiveTheSameString() {
//        ReqResClient client = new ReqResClient();
//        String string = "Hello RSocket";
//        String actual = client.callBlocking(string);
//        System.out.println("!!!!!!!!!!!!!"+actual);
//        client.dispose();
//    }
}
