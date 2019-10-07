package com.michaeltier.grpc.blob;



import com.microsoft.azure.storage.StorageException;
import com.microsoft.azure.storage.blob.CloudBlob;
import com.microsoft.azure.storage.blob.CloudBlobClient;
import com.microsoft.azure.storage.blob.CloudBlobContainer;
import com.microsoft.azure.storage.blob.CloudBlockBlob;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;


public class BlobService {

    private static final Logger logger = LoggerFactory.getLogger(BlobService.class);

    public static CloudBlockBlob getBlobItem(String key, String blobAccount, String blobContainer) throws URISyntaxException, InvalidKeyException, StorageException {

        // Create the blob client.
        String uriString = String.format("https://%s.blob.core.windows.net", blobAccount);
        logger.debug("Creating Azure Blob  client for  base URI " + uriString);

        URI baseuri = new URI(uriString);
        CloudBlobClient blobClient = new CloudBlobClient(baseuri);
        logger.debug("Successfully create Azure Blob  client");
        // Retrieve reference to a previously created container.
        CloudBlobContainer container = blobClient.getContainerReference(blobContainer);
        logger.debug("Successfully got a handle to Container {}", container.getName());

        logger.debug("Attempting to retrieve the blob for key {}", key);
        CloudBlockBlob blob = container.getBlockBlobReference(key);

        logger.debug("Retrieved  the blob {} with URI {}", blob.getName(), blob.getUri());
        return blob;
    }


}
