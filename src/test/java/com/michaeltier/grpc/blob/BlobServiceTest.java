package com.michaeltier.grpc.blob;

import com.microsoft.azure.storage.StorageException;
import com.microsoft.azure.storage.blob.CloudBlockBlob;
import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;

import static org.junit.Assert.*;

public class BlobServiceTest {

  @Test
  public void getBlobItemTextFile() throws InvalidKeyException, StorageException, URISyntaxException, IOException {

      String blobAccount = "cudacodingexercise";
      String blobContainer = "files";
      CloudBlockBlob blob  = BlobService.getBlobItem("lorum.txt", blobAccount, blobContainer);
      String fileString = blob.downloadText();
      Assert.assertThat(fileString, CoreMatchers.containsString("Lorem ipsum dolor"));
  }

    @Test
    public void getBlobItemBinaryFile() throws InvalidKeyException, StorageException, URISyntaxException, IOException {

        String blobAccount = "cudacodingexercise";
        String blobContainer = "files";
        CloudBlockBlob blob  = BlobService.getBlobItem("cat.jpg", blobAccount, blobContainer);
        blob.downloadToFile("cat.downnload.jpg");


        //Assert.assertThat(fileString, CoreMatchers.containsString("Lorem ipsum dolor"));
    }
}
