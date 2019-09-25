package com.github.dantin.webster.common.base;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import org.junit.Test;

/** Unit test for {@link InputStreamHelper}. */
public class InputStreamHelperTest {

  @Test
  public void testReadAllBytes() throws IOException {
    byte[] byteArray = new byte[7];
    byteArray[2] = (byte) 22;
    InputStream byteArrayInputStream = new ByteArrayInputStream(byteArray);
    byte[] byteArrayTwo = InputStreamHelper.readAllBytes(byteArrayInputStream);

    assertArrayEquals(
        new byte[] {(byte) 0, (byte) 0, (byte) 22, (byte) 0, (byte) 0, (byte) 0, (byte) 0},
        byteArray);
    assertArrayEquals(
        new byte[] {(byte) 0, (byte) 0, (byte) 22, (byte) 0, (byte) 0, (byte) 0, (byte) 0},
        byteArrayTwo);

    assertNotSame(byteArray, byteArrayTwo);
    assertNotSame(byteArrayTwo, byteArray);

    assertEquals(7, byteArray.length);
    assertEquals(7, byteArrayTwo.length);
    assertEquals(0, byteArrayInputStream.available());
  }
}
