package com.michaeltier.grpc.server;

import com.barracuda.proto.BlobProxyGrpc.BlobProxyImplBase;
import com.barracuda.proto.ReadBlobRequest;
import com.barracuda.proto.ReadBlobResponse;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.protobuf.ByteString;
import com.michaeltier.grpc.Blob.BlobService;
import com.microsoft.azure.storage.StorageException;
import com.microsoft.azure.storage.blob.CloudBlob;
import io.grpc.stub.StreamObserver;
import io.grpc.Status;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URISyntaxException;
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
        logger.debug("readBlog request key:" + key);
        logger.debug("readBlog Blob Account:" + blobAccount);
        logger.debug("readBlog Blob Container:" + blobAccount);

        try {
            CloudBlob blob = BlobService.getBlobItem(key, blobAccount, blobContainer);

            ByteString.Output data = ByteString.newOutput();

            blob.download(data);

            ReadBlobResponse response = ReadBlobResponse.newBuilder()
                .setData(data.toByteString())
                .build();

            // sends the response back to the client
            responseObserver.onNext(response);

        } catch (URISyntaxException e) {
            logger.error("Invalidconnection string URI to CloudStorageAccount", e);
        } catch (InvalidKeyException e) {
            logger.error("Issue with connection credentials to CloudStorageAccount", e);
        } catch (StorageException e) {
            logger.error("Issue with connection to Blob Container", e);
            responseObserver.onError(
                Status.NOT_FOUND
                    .withDescription("The Blob storage Container holding the blob was not found.")
                    .augmentDescription(e.getLocalizedMessage())
                    .asRuntimeException()
            );
        }



    }
}
