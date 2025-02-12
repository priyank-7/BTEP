package org.drive;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class App {
    private static final Logger logger = LoggerFactory.getLogger(App.class);

    public static void main(String[] args) {
//        if (args.length < 2) {
//            logger.error("Usage: java -jar client-drive.jar <LoadBalancerIP> <Port>");
//            System.exit(1);
//        }
//
//        String loadBalancerIP = args[0];
//        String port = args[1];
//
//        logger.info("Client is starting...");
//        logger.info("Connecting to Load Balancer at {}:{}", loadBalancerIP, port);

        String LoadBalancer_Ip = "localhost"; // args[0];
        int LoadBalancer_Port = 8080; // Integer.parseInt(args[1]);
        Client client = new Client(LoadBalancer_Ip, LoadBalancer_Port);
        client.HandelRequest();

    }
}
