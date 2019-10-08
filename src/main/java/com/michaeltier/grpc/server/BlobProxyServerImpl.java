package com.michaeltier.grpc.server;

import com.barracuda.proto.BlobProxyGrpc.BlobProxyImplBase;
import com.barracuda.proto.ReadBlobRequest;
import com.barracuda.proto.ReadBlobResponse;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.protobuf.ByteString;
import com.michaeltier.grpc.blob.BlobService;
import com.microsoft.azure.storage.StorageException;
import com.microsoft.azure.storage.blob.CloudBlockBlob;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;

public class BlobProxyServerImpl extends BlobProxyImplBase {

  private static final Logger logger = LoggerFactory.getLogger(BlobProxyServerImpl.class);
  private String blobAccount;
  private String blobContainer;

  public BlobProxyServerImpl(String blobAccount, String blobContainer) {
    Preconditions.checkArgument(
        !Strings.isNullOrEmpty(blobAccount), "Invalid Blob Account specified.");
    Preconditions.checkArgument(
        !Strings.isNullOrEmpty(blobContainer), "Invalid Blob Container specified.");
    this.blobAccount = blobAccount;
    this.blobContainer = blobContainer;
  }

  @Override
  public void readBlob(ReadBlobRequest request, StreamObserver<ReadBlobResponse> responseObserver) {
    // super.readBlob(request, responseObserver);
    String key = request.getKey();
    logger.debug("readBlog request key: {}", key);
    logger.debug("readBlog Blob Account: {}", blobAccount);
    logger.debug("readBlog Blob Container: {}", blobContainer);

    try {
      CloudBlockBlob blob = BlobService.getBlobItem(key, blobAccount, blobContainer);
      logger.debug("Successfully retrieved Blob");

      // Download the Blob to a file temporarily
        blob.downloadToFile(key + ".tmp");
        final File file = new File(key + ".tmp");
        if(!file.exists())
        {
            String downloadErrorString = String.format("Blob for key %s was not successfully downloaded to server from Azure.", key);
            logger.error(downloadErrorString);
            throw new IllegalStateException(downloadErrorString);
        }

        logger.debug("Downloaded a file with path {} and length {}",
                     file.getAbsolutePath(), file.length());
        byte[] bytes = readFileToByteArray(file);
        logger.debug("converted read file {} to {} bytes", file.getName(), bytes.length);

      ReadBlobResponse response =
          ReadBlobResponse.newBuilder()
              .setData(ByteString.copyFrom(bytes))
              .build();
      logger.debug("Succesfully built response");

      // sends the response back to the client
      responseObserver.onNext(response);

       // Complete the RPC Call. If not we would just hang here forever
        // And the client would not get a response.
      responseObserver.onCompleted();

    } catch (URISyntaxException e) {
      logger.error("Invalidconnection string URI to CloudStorageAccount", e);
      responseObserver.onError(
          Status.NOT_FOUND
              .withDescription("The Blob storage Container holding the blob was not found.")
              .augmentDescription(e.getLocalizedMessage())
              .asRuntimeException());
    } catch (InvalidKeyException e) {
      logger.error("Issue with connection credentials to CloudStorageAccount", e);
      responseObserver.onError(
          Status.NOT_FOUND
              .withDescription("The Blob storage Container holding the blob was not found.")
              .augmentDescription(e.getLocalizedMessage())
              .asRuntimeException());
    } catch (StorageException e) {
      logger.error("Issue with connection to Blob Container", e);
      responseObserver.onError(
          Status.NOT_FOUND
              .withDescription("The Blob storage Container holding the blob was not found.")
              .augmentDescription(e.getLocalizedMessage())
              .asRuntimeException());
    } catch (FileNotFoundException e) {
      logger.error("Failed to create output stream file.", e);
    } catch (java.io.IOException e) {
      logger.error("Failed to read downloaded fle bytes.", e);
    }
  }

    /**
     * This method uses java.io.FileInputStream to read
     * file content into a byte array
     * @param file
     * @return
     */
    private static byte[] readFileToByteArray(File file){
        FileInputStream fis = null;
        // Creating a byte array using the length of the file
        // file.length returns long which is cast to int
        byte[] bArray = new byte[(int) file.length()];
        try{
            fis = new FileInputStream(file);
            fis.read(bArray);
            fis.close();

        }catch(IOException ioExp){
            logger.error("Failed to convert file stream to byte array.", ioExp);
            throw new IllegalStateException("Could not convert file " + file.getName() + " at path " + file.getAbsolutePath());
        }
        return bArray;
    }
}
