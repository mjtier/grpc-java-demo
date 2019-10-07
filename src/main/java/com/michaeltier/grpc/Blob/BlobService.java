package com.michaeltier.grpc.Blob;


import com.microsoft.azure.storage.CloudStorageAccount;
import com.microsoft.azure.storage.StorageException;
import com.microsoft.azure.storage.blob.CloudBlob;
import com.microsoft.azure.storage.blob.CloudBlobClient;
import com.microsoft.azure.storage.blob.CloudBlobContainer;
import com.microsoft.azure.storage.blob.CloudBlockBlob;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URISyntaxException;
import java.security.InvalidKeyException;


public class BlobService {

    private static final Logger logger = LoggerFactory.getLogger(BlobService.class);

    public static CloudBlob getBlobItem(String key, String blobAccount, String blobContainer) throws URISyntaxException, InvalidKeyException, StorageException {

        String storageConnectionString = "DefaultEndpointsProtocol=https;"
            + "AccountName="+ blobAccount+ ";";

        logger.debug("Creating storage account for storage connection string " + storageConnectionString);

        CloudStorageAccount storageAccount = CloudStorageAccount.parse(storageConnectionString);
        // Create the blob client.
        CloudBlobClient blobClient = storageAccount.createCloudBlobClient();
        // Retrieve reference to a previously created container.

        CloudBlobContainer container = blobClient.getContainerReference(blobContainer);
        CloudBlockBlob blob = container.getBlockBlobReference(key);

        return blob;
    }


}
