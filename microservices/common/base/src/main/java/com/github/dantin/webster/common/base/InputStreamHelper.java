package com.github.dantin.webster.common.base;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

/** A set of static utility methods for {@code InputStream}. */
public class InputStreamHelper {
  private static final int MAX_BUFFER_SIZE = Integer.MAX_VALUE - 8;
  private static final int DEFAULT_BUFFER_SIZE = 8192;

  private InputStreamHelper() {
    // suppress default constructor for non-instantiable.
    throw new AssertionError();
  }

  /**
   * Reads all remaining bytes from the input stream. This method blocks until all remaining bytes
   * have been read and end of stream is detected, or an exception is thrown.
   *
   * @param is the {@code InputStream} to use
   * @return a byte array containing the bytes read from this input stream
   * @throws IOException if an I/O error occurs
   * @throws OutOfMemoryError if an array of the required size cannot be allocated. E.g, if an array
   *     larger than {@code 2GB} would be required to store the bytes.
   */
  public static byte[] readAllBytes(InputStream is) throws IOException {
    byte[] buf = new byte[DEFAULT_BUFFER_SIZE];
    int capacity = buf.length;
    int nread = 0;
    int n;
    for (; ; ) {
      // read to EOF which may read more or less than initial buffer size
      while ((n = is.read(buf, nread, capacity - nread)) > 0) {
        nread += n;
      }

      // if the last call to read returned -1, then we're done
      if (n < 0) {
        break;
      }

      // need to allocate a larger buffer
      if (capacity <= MAX_BUFFER_SIZE - capacity) {
        capacity = capacity << 1;
      } else {
        if (capacity == MAX_BUFFER_SIZE) {
          throw new OutOfMemoryError("Required array size too large");
        }
        capacity = MAX_BUFFER_SIZE;
      }
      buf = Arrays.copyOf(buf, capacity);
    }
    return (capacity == nread) ? buf : Arrays.copyOf(buf, nread);
  }
}
