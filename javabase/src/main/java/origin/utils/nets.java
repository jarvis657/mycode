package origin.utils;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

/**
 * @Author:lmq
 * @Date: 2020/8/14
 * @Desc:
 **/
public class nets {
    public static void main(String[] args) throws SocketException {
        String a = "1";
        String b = "2";

        a = b = "3";
        System.out.println(a + ":" + b);
        test(a);
        System.out.println("-------------------------");
        test("00");

        Enumeration<NetworkInterface> networkinterface = NetworkInterface.getNetworkInterfaces();
        while (networkinterface.hasMoreElements()) {
            NetworkInterface eachNetworkinterface = networkinterface.nextElement();
            System.out.println("getName 获得网络设备名称 =" + eachNetworkinterface.getName());
            System.out.println("getDisplayName 获得网络设备显示名称=" + eachNetworkinterface.getDisplayName());
            System.out.println("getIndex获得网络接口的索引= " + eachNetworkinterface.getIndex());
            System.out.println("isUp是否已经开启并运行=" + eachNetworkinterface.isUp());
            System.out.println("isLoopback是否为回调接口=" + eachNetworkinterface.isLoopback());
            System.out.println("最大传输MTU=" + eachNetworkinterface.getMTU());
            for (Enumeration<InetAddress> addrs = eachNetworkinterface.getInetAddresses(); addrs.hasMoreElements(); ) {
                InetAddress address = addrs.nextElement();
//                if (address instanceof Inet4Address && !address.isLoopbackAddress() ) {
                System.out.print(">>address 地址" + address.getHostAddress() + " ");
//                }
            }
            System.out.println();
            System.out.println();

        }
    }

    public static void test(String la) {
        switch (la) {
            case "2":
                System.out.println("ddddd");break;
            case "3":
                System.out.println("vvvvv");break;
            default: return;
        }
        System.out.println("mmmmmmmmmmmmmmmmmmmm");
    }
}
