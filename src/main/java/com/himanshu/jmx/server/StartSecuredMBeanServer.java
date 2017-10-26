package com.himanshu.jmx.server;

import com.himanshu.jmx.server.bean.HelloMBean;
import com.himanshu.jmx.server.bean.IHelloMBean;

import javax.management.*;
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
  private void createJmxConnectorServer() throws IOException, MalformedObjectNameException, NotCompliantMBeanException, InstanceAlreadyExistsException, MBeanRegistrationException {
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
    registerBeans(mbs);
    JMXConnectorServer svr = JMXConnectorServerFactory.newJMXConnectorServer(url, envProperties, mbs);
    svr.start();
  }

  private void registerBeans(MBeanServer mbs) throws MalformedObjectNameException, NotCompliantMBeanException, InstanceAlreadyExistsException, MBeanRegistrationException {
    registerHelloMBean(mbs, new HelloMBean());
  }

  private void registerHelloMBean(MBeanServer mbs, IHelloMBean helloMBean) throws MalformedObjectNameException, NotCompliantMBeanException, InstanceAlreadyExistsException, MBeanRegistrationException {
    ObjectName helloBeanName = new ObjectName("com.himanshu.jmx.server.bean:type=HelloMBean");
    StandardMBean mBean = new StandardMBean(helloMBean, IHelloMBean.class);
    mbs.registerMBean(mBean, helloBeanName);
  }

  public static void main(String[] args) {
    StartSecuredMBeanServer mbeanServer = new StartSecuredMBeanServer();
    try {
      mbeanServer.createJmxConnectorServer();
    } catch (IOException e) {
      e.printStackTrace();
    } catch (MalformedObjectNameException e) {
      e.printStackTrace();
    } catch (NotCompliantMBeanException e) {
      e.printStackTrace();
    } catch (InstanceAlreadyExistsException e) {
      e.printStackTrace();
    } catch (MBeanRegistrationException e) {
      e.printStackTrace();
    }
  }
}
