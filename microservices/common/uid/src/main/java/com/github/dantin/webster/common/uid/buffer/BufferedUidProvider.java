package com.github.dantin.webster.common.uid.buffer;

import java.util.List;

/** Buffered UID provider(Lambda supported), which provides UID in the same one second. */
@FunctionalInterface
public interface BufferedUidProvider {

  /**
   * Provide UIDs in one second.
   *
   * @param momentInSecond timestamp
   * @return UID list
   */
  List<Long> provide(long momentInSecond);
}
