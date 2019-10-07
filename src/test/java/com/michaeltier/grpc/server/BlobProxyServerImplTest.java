package com.michaeltier.grpc.server;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import com.michaeltier.grpc.server.BlobProxyServerImpl;


import static org.junit.Assert.*;

public class BlobProxyServerImplTest {

  @Before
  public void setUp() throws Exception {}

  @After
  public void tearDown() throws Exception {}

  @Test
  public void readBlobTest() {}

  @Test(expected = IllegalArgumentException.class)
  public void constructionTest() {

    BlobProxyServerImpl server = new BlobProxyServerImpl(null, "BLOB_CONTAINER");


  }
}
