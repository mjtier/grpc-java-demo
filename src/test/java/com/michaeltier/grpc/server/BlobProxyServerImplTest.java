package com.michaeltier.grpc.server;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class BlobProxyServerImplTest {

  @Before
  public void setUp() throws Exception {}

  @After
  public void tearDown() throws Exception {}

  @Test
  public void readBlobTest() {}

  @Test(expected = IllegalArgumentException.class)
  public void nullBlobAccountcConstructionTest() {
    BlobProxyServerImpl server = new BlobProxyServerImpl(null, "BLOB_CONTAINER");
  }

  @Test(expected = IllegalArgumentException.class)
  public void emptyStringBlobContainerConstructionTest() {
    BlobProxyServerImpl server = new BlobProxyServerImpl("BLOB_ACCOUNT", "");
  }
}
