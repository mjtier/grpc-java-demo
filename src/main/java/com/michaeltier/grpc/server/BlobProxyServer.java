package com.michaeltier.grpc.server;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;


public class BlobProxyServer {

    private static int SERVER_PORT = 50051;
    private static final Logger logger = LoggerFactory.getLogger(BlobProxyServer.class);

    public static void main(String[] args) throws InterruptedException, IOException {

        // Retrieve the Blob Store Account and Container information
        String blobAccount = System.getenv("BLOB_ACCOUNT");
        String blobContainer = System.getenv("BLOB_CONTAINER");

        logger.info(String.format("Starting BlobProxyServer on port %d", SERVER_PORT));

        Server server = ServerBuilder.forPort(SERVER_PORT)
                                     .addService(new BlobProxyServerImpl(blobAccount, blobContainer))
                                     .build()
                                     .start();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            logger.info("Received Shutdown Request");
            server.shutdown();
            logger.info("Successfully stopped the server");
        }));

        server.awaitTermination();
    }
}
