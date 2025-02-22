package org.drive;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.drive.db.DatabaseUtil;
import org.drive.utilities.LoadMatrix;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.drive.Token.TokenManager;
import org.drive.db.User;
import org.drive.db.UserDAO;
import org.drive.headers.Request;
import org.drive.headers.RequestType;
import org.drive.headers.Response;
import org.drive.headers.StatusCode;
import org.drive.utilities.NodeType;
import org.drive.utilities.PeerRequest;

// TODO: data security while transferring data between nodes and while stored

public class LoadBalancer {

    private static final Logger logger = LoggerFactory.getLogger(LoadBalancer.class);


    private final ServerSocket serverSocket;
    private final UserDAO userDAO;
    private final TokenManager tokenManager;
    private final ExecutorService threadPool;
    private final String REGISTORYIP;
    private final int REGISTORYPORT;

    // TODO: update list to data structure that supports concurrent access
    private List<InetSocketAddress> storageNodes = new ArrayList<>();
    private int currentIndex = 0;

    // TODO
    /*
     * Put a time to leave on generated tokens.
     * If there is no response from registry, then try to PING Registory-
     * on time interval.
     */

    public LoadBalancer(int port, String registryIP, int registryPort) {
        this.REGISTORYIP = registryIP;
        this.REGISTORYPORT = registryPort;
        try {
            serverSocket = new ServerSocket(port);
            int poolSize = Runtime.getRuntime().availableProcessors();
            this.threadPool = Executors.newCachedThreadPool();
            logger.info("Thread pool initialized with size: " + poolSize * 4);
            userDAO = new UserDAO(DatabaseUtil.getConnection());
            tokenManager = new TokenManager();
            // Runtime.getRuntime().addShutdownHook(new Thread(this::shutdown));
            registerWithRegistory();
        } catch (IOException e) {
            logger.error(e.getMessage());
            throw new RuntimeException("Failed to initialize LoadBalancer", e);
        } catch (SQLException e) {
            logger.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private void registerWithRegistory() {
        try (Socket registrySocket = new Socket(REGISTORYIP, REGISTORYPORT)) {
            ObjectOutputStream out = new ObjectOutputStream(registrySocket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(registrySocket.getInputStream());

            // Create a request to register the load balancer with the registry
            PeerRequest regiPeerRequest = PeerRequest.builder()
                    .requestType(RequestType.REGISTER)
                    .nodeType(NodeType.LOAD_BALANCER)
                    .socketAddress(new InetSocketAddress("localhost", serverSocket.getLocalPort()))
                    .build();
            out.writeObject(regiPeerRequest);
            out.flush();

            // Receive and handle the response from the registry
            Response response = (Response) in.readObject();
            // out.writeObject(PeerRequest.builder().requestType(RequestType.DISCONNECT));
            // out.flush();
            if (response.getStatusCode() == StatusCode.SUCCESS) {
                logger.info("LoadBalancer successfully registered with Registory");

                // Expecting a payload with the list of active storage nodes
                this.storageNodes = (List<InetSocketAddress>) response.getPayload();
                logger.info("Active storage nodes: " + storageNodes);
            } else {
                logger.error("Failed to register LoadBalancer with Registory");
            }

            out.writeObject(PeerRequest.builder()
                    .requestType(RequestType.DISCONNECT)
                    .build());
            out.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException("Registration with Registory failed", e);
        }
    }

    public void start() {
        while (true) {
            try {
                Socket clientSocket = serverSocket.accept();
                threadPool.submit(new ClientHandler(clientSocket, userDAO));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private class ClientHandler implements Runnable {
        private Socket clientSocket;
        private ObjectOutputStream out;
        private ObjectInputStream in;
        private final UserDAO userDAO;

        public ClientHandler(Socket socket, UserDAO userDAO) {
            this.clientSocket = socket;
            this.userDAO = userDAO;
            try {
                out = new ObjectOutputStream(clientSocket.getOutputStream());
                in = new ObjectInputStream(clientSocket.getInputStream());
            } catch (IOException e) {
                logger.error("Exception During Creation Of Out/In stream in ClientHandler");
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            try {
                while (true) {
                    Request request = (Request) in.readObject();

                    switch (request.getRequestType()) {
                        case GET_USER_DETAILS:
                            handleGetUserDetails(request);
                            break;
                        case PING:
                            handlePingRequest();
                            clientSocket.close();
                            return;
                        case UPDATE:
                            // TODO: This operation should handle concurrent access
                            storageNodes = (List<InetSocketAddress>) request.getPayload();
                            logger.info("Updated storage nodes: " + storageNodes);
                            clientSocket.close();
                            return;
                        case AUTHENTICATE:
                            handleAuthenticate(request);
                            break;
                        case SIGNUP:
                            logger.debug("Received signup request");
                            handleSignup(request);
                            break;
                        case FORWARD_REQUEST:
                            handleForwardRequest(request);
                            break;
                        case VALIDATE_TOKEN:
                            handleValidateToken(request);
                            break;
                        case DISCONNECT:
                            clientSocket.close();
                            return;
                        default:
                            Response response = new Response(StatusCode.UNKNOWN_REQUEST, "Unknown request type");
                            out.writeObject(response);
                    }
                }
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

        private void handleGetUserDetails(Request request){
            try{
                String username = (String)request.getPayload();
                if (username == null || username.isEmpty()){
                    out.writeObject(Response.builder().statusCode(StatusCode.NOT_FOUND).build());
                    out.flush();
                    return;
                }
                User user = userDAO.getUserByUsername(username);
                if(user == null){
                    out.writeObject(Response.builder().statusCode(StatusCode.NOT_FOUND).build());
                    out.flush();
                    return;
                }
                out.writeObject(Response.builder()
                        .statusCode(StatusCode.SUCCESS)
                        .payload(user)
                        .build());
                out.flush();

            }catch (IOException e){
                e.printStackTrace();
            }
        }

        private void handleSignup(Request request) {
            try {
                // Get user name and password from the request payload
                logger.debug("Received signup request");
                User user = ((User) request.getPayload());

                // check any user with same user name exists or not
                User existingUser = userDAO.getUserByUsername(user.getUsername());
                Response response = null;
                if (existingUser != null) {
                    logger.debug("User with same username already exists");
                    response = Response.builder()
                            .statusCode(StatusCode.AUTHENTICATION_FAILED)
                            .payload("User with same username already exists")
                            .build();
                } else {
                    if(this.userDAO.insertUser(user)){
                        response = Response.builder()
                                .statusCode(StatusCode.SUCCESS)
                                .payload("User signed up successfully")
                                .build();
                        logger.info("User signed up successfully: " + user);
                    }
                    else{
                        response = Response.builder()
                                .statusCode(StatusCode.INTERNAL_SERVER_ERROR)
                                .build();
                        logger.error("Error during creating user: " + user);
                    }
                }

                out.writeObject(response);
                out.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private void handleAuthenticate(Request request) throws IOException {
            String[] credentials = ((String) request.getPayload()).split(":");
            String username = credentials[0];
            String password = credentials[1];
            User user;
            try {
                user = userDAO.getUserByUsername(username);
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }

            if (user != null && user.getPasswordHash().equals(password)) {
                String token = tokenManager.generateToken(username);
                Response response = Response.builder()
                        .statusCode(StatusCode.SUCCESS)
                        .payload(token)
                        .build();
                out.writeObject(response);
            } else {
                Response response = Response.builder()
                        .statusCode(StatusCode.AUTHENTICATION_FAILED)
                        .payload("Invalid credentials")
                        .build();
                out.writeObject(response);
                out.flush();
            }
        }

        private void handleForwardRequest(Request request) throws IOException {
            String token = (String) request.getToken();
            // System.out.println("[LoadBalancer]: Validation for forward request");
            if (tokenManager.validateToken(token)) {
                InetSocketAddress storageNode = selectStorageNode();
                Response response = Response.builder()
                        .statusCode(StatusCode.SUCCESS)
                        .payload(storageNode)
                        .build();
                out.writeObject(response);
            } else {
                Response response = new Response(StatusCode.AUTHENTICATION_FAILED, "Invalid or expired token");
                out.writeObject(response);
            }
        }

        private InetSocketAddress selectStorageNode() {
            synchronized (this) {
                if (storageNodes.isEmpty()) {
                    return null;
                }
                // Round-robin selection with thread safety
                InetSocketAddress selectedNode = storageNodes.get(currentIndex);
                currentIndex = (currentIndex + 1) % storageNodes.size();
                return selectedNode;
            }
        }

        private void handleValidateToken(Request request) throws IOException {
            String token = request.getToken();
            boolean isValid = tokenManager.validateToken(token); // impliment different for storage node
            // System.out.println("[LoadBalancer]: Is token valid " + isValid)
            Response response;
            if (isValid) {
                User user = this.userDAO.getUserByUsername(tokenManager.getUsernameFromToken(token));
                response = Response.builder()
                        .statusCode(StatusCode.SUCCESS)
                        .payload(user)
                        .build();
            } else {
                response = Response.builder()
                        .statusCode(StatusCode.AUTHENTICATION_FAILED)
                        .payload("Token is invalid or expired")
                        .build();
            }
            out.writeObject(response);
        }

        private void handlePingRequest() {
            try {
                // Send a simple PONG response back to the client
                // logger.info("Received PING request from registry");
                LoadMatrix loadMatrix = AppLoadMatrix.getLoadMatrix();
                out.writeObject(Response.builder()
                        .statusCode(StatusCode.PONG)
                        .payload(loadMatrix)
                        .build());
                out.flush();
            } catch (IOException e) {
                logger.error("Exception occurred while sending response of PING request");
                e.printStackTrace();
            }
        }
    }
}