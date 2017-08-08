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
public class StartSecuredMBeanServer {
  private void createJmxConnectorServer() throws IOException {
    LocateRegistry.createRegistry(1234);
    MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
    JMXServiceURL url = new JMXServiceURL("service:jmx:rmi://localhost/jndi/rmi://localhost:1234/jmxrmi");

    Map<String, Object> envProperties = new HashMap<String, Object>();
    envProperties.put(JMXConnectorServer.AUTHENTICATOR, new JMXAuthenticator() {
      public Subject authenticate(Object credentials) {
        System.out.println(credentials);
        if (((String[])credentials)[0].equals("abc") && ((String[])credentials)[1].equals("pqr")) {
          return new Subject(true, new HashSet<Principal>(), new HashSet<Object>(), new HashSet<Object>());
        }
        throw new RuntimeException("Auth failure");
      }
    });

    JMXConnectorServer svr = JMXConnectorServerFactory.newJMXConnectorServer(url, envProperties, mbs);
    svr.start();
  }

  public static void main(String[] args) {
    StartSecuredMBeanServer mbeanServer = new StartSecuredMBeanServer();
    try {
      mbeanServer.createJmxConnectorServer();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
