package com.michaeltier.grpc.blob;

import com.microsoft.azure.storage.StorageException;
import com.microsoft.azure.storage.blob.CloudBlockBlob;
import org.hamcrest.CoreMatchers;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.io.File;
import static org.junit.Assert.*;

public class BlobServiceTest {
    @Before
    public void setUp() throws Exception {

    }

    @After
    public void tearDown() throws Exception {
        File file = new File("cat.downnload.jpg");
        file.delete();

    }


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
        File file = new File("cat.downnload.jpg");
        Assert.assertTrue(file.exists());
        //Assert.assertThat(fileString, CoreMatchers.containsString("Lorem ipsum dolor"));
    }
}
