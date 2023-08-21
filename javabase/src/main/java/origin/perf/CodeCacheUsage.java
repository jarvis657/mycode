package origin.perf;

//import com.sun.tools.attach.VirtualMachine;

//import reactor.tools.shaded.net.bytebuddy.agent.VirtualMachine;

import javax.management.AttributeList;
import javax.management.MBeanServerConnection;
import javax.management.ObjectName;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;
import java.io.File;

/**
 * @Author:qishan
 * @Date: Thu
 * @Desc:
 **/
public class CodeCacheUsage {
    private static final String CONNECTOR_ADDRESS = "com.sun.management.jmxremote.localConnectorAddress";

    public static void main(String[] args) throws Exception {
//        if (args.length != 1) {
//            System.err.println("Must enter one arg: pid");
//            System.exit(0);
//        }
//        VirtualMachine vm = VirtualMachine.attach(args[0]);
//        JMXConnector connector = null;
//        try {
//            String connectorAddress = vm.getAgentProperties().getProperty(CONNECTOR_ADDRESS);
//            if (connectorAddress == null) {
//                String agent = vm.getSystemProperties().getProperty("java.home")
//                        + File.separator
//                        + "lib"
//                        + File.separator + "management-agent.jar";
//                vm.loadAgent(agent);
//                connectorAddress = vm.getAgentProperties().getProperty(CONNECTOR_ADDRESS);
//            }
//            JMXServiceURL url = new JMXServiceURL(connectorAddress);
//            connector = JMXConnectorFactory.connect(url);
//            MBeanServerConnection mbeanConn = connector.getMBeanServerConnection();
////            ObjectName name = new ObjectName("java.lang:type=MemoryPool,name=Code Cache");
//            ObjectName name = new ObjectName("java.lang:type=Memory");
////            System.out.println(mbeanConn.getAttribute(name, "Usage"));
//            AttributeList attributes = mbeanConn.getAttributes(name, new String[]{"NonHeapMemoryUsage", "FreeNonHeapMemory", "HeapMemoryUsagePercent"});
//            System.out.println(attributes);
////            System.out.println(mbeanConn.getAttribute(name, "HeapMemoryUsagePercent"));
//        } finally {
//            if (connector != null) {
//                connector.close();
//            }
//            vm.detach();
//        }
    }

}
