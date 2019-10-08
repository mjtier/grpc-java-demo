package com.michaeltier.grpc.client;

import com.barracuda.proto.BlobProxyGrpc;
import com.barracuda.proto.ReadBlobRequest;
import com.barracuda.proto.ReadBlobResponse;
import com.google.protobuf.ByteString;
import com.michaeltier.grpc.server.BlobProxyServer;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class BlobClient {

    private static final Logger logger = LoggerFactory.getLogger(BlobClient.class);

    public static void main(String[] args) throws SSLException, IOException {
       logger.info("Hello I'm a gRPC client for Blob server");

        BlobClient main = new BlobClient();
        main.run();
    }

    private void run() throws SSLException, IOException {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 50051)
            .usePlaintext()
            .build();

        logger.debug("Successfully constructed the Managed Chanel");
        doUnaryCall(channel);

        logger.info("Shutting down channel");
        channel.shutdown();

    }

    private void doUnaryCall(ManagedChannel channel) throws IOException {
        // created a service client (blocking - synchronous)
        BlobProxyGrpc.BlobProxyBlockingStub blobClient = BlobProxyGrpc.newBlockingStub(channel);

        logger.debug("Successfully created a service client.");
        // The file we want to download
        String key = "lorum.txt";

        // create a protocol buffer ReadBlobRequest message to download thefile
        ReadBlobRequest blobReadRequest =  ReadBlobRequest.newBuilder()
            .setKey(key)
            .build();

        // Call the RPC and get back a ReadBlobResponse (protocol buffers)
        // The binary file data should be in the response.
        logger.debug("Calling RPC to Read Blob.");
        ReadBlobResponse readBlobResponse = blobClient.readBlob(blobReadRequest);

        FileOutputStream fout = new FileOutputStream(key);
        readBlobResponse.writeTo(fout);
        fout.close();
        logger.debug("Closed File Output Stream {}", key);
    }

}
