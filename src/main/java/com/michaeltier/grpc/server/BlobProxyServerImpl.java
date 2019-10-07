package com.michaeltier.grpc.server;

import com.barracuda.proto.BlobProxyGrpc.BlobProxyImplBase;
import com.barracuda.proto.ReadBlobRequest;
import com.barracuda.proto.ReadBlobResponse;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.protobuf.ByteString;
import com.michaeltier.grpc.blob.BlobService;
import com.microsoft.azure.storage.StorageException;
import com.microsoft.azure.storage.blob.CloudBlob;
import com.microsoft.azure.storage.blob.CloudBlockBlob;
import io.grpc.stub.StreamObserver;
import io.grpc.Status;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.InvalidKeyException;

public class BlobProxyServerImpl extends BlobProxyImplBase {

    private String blobAccount;
    private String blobContainer;

    private static final Logger logger = LoggerFactory.getLogger(BlobProxyServerImpl.class);

    public BlobProxyServerImpl(String blobAccount, String blobContainer) {
        Preconditions.checkArgument(!Strings.isNullOrEmpty(blobAccount), "Invalid Blob Account specified." ) ;
        Preconditions.checkArgument(!Strings.isNullOrEmpty(blobContainer), "Invalid Blob Container specified." ) ;
        this.blobAccount  = blobAccount;
        this.blobContainer = blobContainer;
    }

    @Override
    public void readBlob(ReadBlobRequest request, StreamObserver<ReadBlobResponse> responseObserver) {
        //super.readBlob(request, responseObserver);
        String key = request.getKey();
        logger.debug("readBlog request key: {}", key);
        logger.debug("readBlog Blob Account: {}" , blobAccount);
        logger.debug("readBlog Blob Container: {}" , blobContainer);

        try {
            CloudBlockBlob blob = BlobService.getBlobItem(key, blobAccount, blobContainer);
            logger.debug("Successfully retrieved Blob");

            OutputStream out = new FileOutputStream(key);
            blob.download(out);

            byte[] data = Files.readAllBytes(Paths.get(key));

            ReadBlobResponse response = ReadBlobResponse.newBuilder()
                .setData(ByteString.copyFrom(data))
                .build();

            // sends the response back to the client
            responseObserver.onNext(response);

        } catch (URISyntaxException e) {
            logger.error("Invalidconnection string URI to CloudStorageAccount", e);
            responseObserver.onError(
                Status.NOT_FOUND
                    .withDescription("The Blob storage Container holding the blob was not found.")
                    .augmentDescription(e.getLocalizedMessage())
                    .asRuntimeException()
            );
        } catch (InvalidKeyException e) {
            logger.error("Issue with connection credentials to CloudStorageAccount", e);
            responseObserver.onError(
                Status.NOT_FOUND
                    .withDescription("The Blob storage Container holding the blob was not found.")
                    .augmentDescription(e.getLocalizedMessage())
                    .asRuntimeException()
            );
        } catch (StorageException e) {
            logger.error("Issue with connection to Blob Container", e);
            responseObserver.onError(
                Status.NOT_FOUND
                    .withDescription("The Blob storage Container holding the blob was not found.")
                    .augmentDescription(e.getLocalizedMessage())
                    .asRuntimeException()
            );
        } catch (FileNotFoundException e) {
            logger.error("Failed to create output stream file.", e);
        }  catch (java.io.IOException e) {
            logger.error("Failed to read downloaded fle bytes.", e);
        }



    }
}
