package org.drive;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class App {
    private static final Logger logger = LoggerFactory.getLogger(App.class);

    public static void main(String[] args) {
//        if (args.length < 1) {
//            logger.error("Usage: java -jar serviceRegistry.jar <Port>");
//            System.exit(1);
//        }
//
//        String port = args[0];
//
//        logger.info("Starting Service Registry...");
//        logger.info("Service Registry running on port: {}", port);

        // logic

        int registoryPort = 7070;
        Registory registory = new Registory(registoryPort);
        registory.start();
    }
}