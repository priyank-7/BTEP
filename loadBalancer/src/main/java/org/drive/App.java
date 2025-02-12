package org.drive;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class App {
    private static final Logger logger = LoggerFactory.getLogger(App.class);

    public static void main(String[] args) {
//        if (args.length < 3) {
//            logger.error("Usage: java -jar loadBalancer.jar <PORT> <SERVICE_REGISTRY_IP> <SERVICE_REGISTRY_PORT>");
//            System.exit(1);
//        }
//
//        int port;
//        try {
//            port = Integer.parseInt(args[0]);
//        } catch (NumberFormatException e) {
//            logger.error("Invalid port number: {}", args[0]);
//            return;
//        }
//
//        String serviceRegistryIP = args[1];
//        int serviceRegistryPort;
//        try {
//            serviceRegistryPort = Integer.parseInt(args[2]);
//        } catch (NumberFormatException e) {
//            logger.error("Invalid service registry port: {}", args[2]);
//            return;
//        }
//
//        logger.info("Load Balancer starting on port: {}", port);
//        logger.info("Connecting to Service Registry at {}:{}", serviceRegistryIP, serviceRegistryPort);

        String registryIP = "localhost";
        int registryPort = 7070;
        int loadBalancerPort = 8080;

        LoadBalancer loadBalancer = new LoadBalancer(loadBalancerPort, registryIP, registryPort);
        loadBalancer.start();

    }
}
