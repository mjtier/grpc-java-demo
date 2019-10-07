package com.michaeltier.grpc.client;

import com.barracuda.proto.BlobProxyGrpc;
import com.barracuda.proto.ReadBlobRequest;
import com.barracuda.proto.ReadBlobResponse;
import com.google.protobuf.ByteString;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

import javax.net.ssl.SSLException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class BlobClient {

    public static void main(String[] args) throws SSLException, IOException {
        System.out.println("Hello I'm a gRPC client for Blob server");

        BlobClient main = new BlobClient();
        main.run();
    }

    private void run() throws SSLException, IOException {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 50051)
            .usePlaintext()
            .build();

        doUnaryCall(channel);

        System.out.println("Shutting down channel");
        channel.shutdown();

    }

    private void doUnaryCall(ManagedChannel channel) throws IOException {
        // created a greet service client (blocking - synchronous)
        BlobProxyGrpc.BlobProxyBlockingStub blobClient = BlobProxyGrpc.newBlockingStub(channel);

        // The file we want to download
        String key = "lorum.txt";

        // create a protocol buffer ReadBlobRequest message to download thefile
        ReadBlobRequest blobReadRequest =  ReadBlobRequest.newBuilder()
            .setKey(key)
            .build();

        // Call the RPC and get back a ReadBlobResponse (protocol buffers)
        // The binary file data should be in the response.
        ReadBlobResponse readBlobResponse = blobClient.readBlob(blobReadRequest);

        byte[] bytes = readBlobResponse.getData().toByteArray();
        Files.write(Paths.get(key), bytes);

    }

}
