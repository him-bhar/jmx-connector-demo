package com.himanshu.jmx.server;

import javax.management.MBeanServer;
import javax.management.remote.JMXAuthenticator;
import javax.management.remote.JMXConnectorServer;
import javax.management.remote.JMXConnectorServerFactory;
import javax.management.remote.JMXServiceURL;
import javax.security.auth.Subject;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.rmi.registry.LocateRegistry;
import java.security.Principal;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

/**
 * Created by himanshu on 08-08-2017.
 */
public class StartNonSecuredMBeanServer {
  private void createJmxConnectorServer() throws IOException {
    LocateRegistry.createRegistry(1234);
    MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
    JMXServiceURL url = new JMXServiceURL("service:jmx:rmi://localhost/jndi/rmi://localhost:1234/jmxrmi");

    JMXConnectorServer svr = JMXConnectorServerFactory.newJMXConnectorServer(url, null, mbs);
    svr.start();
  }

  public static void main(String[] args) {
    StartNonSecuredMBeanServer mbeanServer = new StartNonSecuredMBeanServer();
    try {
      mbeanServer.createJmxConnectorServer();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
