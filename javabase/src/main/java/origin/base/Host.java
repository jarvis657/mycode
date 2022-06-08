package origin.base;

import java.io.UnsupportedEncodingException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author:qishan
 * @Date: 2019-04-01
 * @Desc:
 **/
public class Host {

    public static void main(String[] args) throws UnsupportedEncodingException {
        Map<String, String> map = new ConcurrentHashMap<>();
        String a = map.computeIfAbsent("a", d -> d + ":value");
        System.out.println(a);
        System.out.println(map);
        String a2 = map.computeIfAbsent("a", d -> d + ":222222222value");
        System.out.println(a2);
        System.out.println(map);

        System.out.println(true | false);
        System.out.println(true | true);
        System.out.println(true & false);
        System.out.println(true & true);
        System.out.println(Integer.toBinaryString(-1));
        System.out.println(Integer.toBinaryString(-2));
        System.out.println(Integer.toBinaryString(-3));
        System.out.println(Integer.toBinaryString(-4));
        String s = "张z国😀";
        System.out.println(s.getBytes("utf-8").length);
        System.out.println(s.charAt(1));
        System.out.println(s.charAt(2));
    }

    private static void resolveHost() throws Exception {
        InetAddress addr = InetAddress.getLocalHost();
        String hostName = addr.getHostName();
        String ip = addr.getHostAddress();
        if (addr.isLoopbackAddress()) {
            // find the first IPv4 Address that not loopback
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            while (interfaces.hasMoreElements()) {
                NetworkInterface in = interfaces.nextElement();
                Enumeration<InetAddress> addrs = in.getInetAddresses();
                while (addrs.hasMoreElements()) {
                    InetAddress address = addrs.nextElement();
                    if (!address.isLoopbackAddress() && address instanceof Inet4Address) {
                        ip = address.getHostAddress();
                    }
                }
            }
        }
    }
}
