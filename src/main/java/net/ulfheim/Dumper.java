package net.ulfheim;

import net.ulfheim.jmxstack.StackPrinter;

import javax.management.MBeanServerConnection;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;
import java.io.IOException;
import java.io.PrintStream;

public class Dumper {
    public static void main(String[] args) {
        if (args.length != 2) {
            System.err.println("Usage: jmxstack host port");
            System.exit(1);
        }
        String host = args[0];
        Integer port = Integer.valueOf(args[1]);
        Dumper dumper = new Dumper(host, port);
        dumper.dump(System.out);
    }

    private MBeanServerConnection connection;

    private Dumper(String hostname, int port) {
        String urlPath = "/jndi/rmi://" + hostname + ":" + port + "/jmxrmi";
        connect(urlPath);
    }

    private void dump(PrintStream out) {
        try {
            StackPrinter stackPrinter = new StackPrinter(connection, out);
            stackPrinter.doPrint();
        } catch (IOException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }

    private void connect(String urlPath) {
        try {
            JMXServiceURL url = new JMXServiceURL("rmi", "", 0, urlPath);
            JMXConnector jmxConnector = JMXConnectorFactory.connect(url);
            this.connection = jmxConnector.getMBeanServerConnection();
        } catch (IOException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }

}
