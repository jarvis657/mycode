package origin.mbeans;

//import sun.misc.VMSupport;

import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;

/**
 * @Author:lmq
 * @Date: 2020/8/27
 * @Desc:
 **/
public enum JmxConnectorInstance {

    INSTANCE;

    private static JMXConnector jmxConnector;

    JmxConnectorInstance() {

    }

    public JMXConnector getJmxConnector() {
        return jmxConnector;
    }

    public void setJmxConnector(JMXConnector jmxConnector) {
        JmxConnectorInstance.jmxConnector = jmxConnector;
    }

    /**
     * 当前 jvm
     */
    private void buildJmxConnector() {

//        String addr = (String) VMSupport.getAgentProperties().get("com.sun.management.jmxremote.localConnectorAddress");
//
//        if (addr == null) {
//            try {
//                sun.management.Agent.premain("");
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//        addr = (String) VMSupport.getAgentProperties().get("com.sun.management.jmxremote.localConnectorAddress");
//        try {
//            JMXServiceURL jmxServiceURL = new JMXServiceURL(addr);
//            JMXConnector jmxConnector = JMXConnectorFactory.connect(jmxServiceURL, null);
//            JmxConnectorInstance.jmxConnector = jmxConnector;
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }


}
