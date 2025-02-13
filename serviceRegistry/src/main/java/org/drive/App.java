package org.drive;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class App {
    private static final Logger logger = LoggerFactory.getLogger(App.class);

    public static void main(String[] args) {
        if (args.length < 1) {
            logger.error("Usage: java -jar serviceRegistry.jar <Port>");
            System.exit(1);
        }

        int port;
        try{
            port = Integer.parseInt(args[0]);
        }
        catch (NumberFormatException e){
            logger.error("Invalid port number: {}", args[0]);
            return;
        }

        logger.info("Starting Service Registry...");
        logger.info("Service Registry running on port: {}", port);

        Registory registory = new Registory(port);
        registory.start();
    }
}