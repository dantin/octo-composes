package com.github.dantin.webster.common.uid;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.google.common.base.Strings;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

class UidGeneratorTest {

  static final int SIZE = 10000;
  static final boolean VERBOSE = false;
  static final int THREADS = Runtime.getRuntime().availableProcessors() << 1;

  UidGenerator uidGenerator;

  void workRun(Set<Long> uidSet, AtomicInteger control) {
    while (true) {
      int pos = control.updateAndGet(old -> (old == SIZE ? SIZE : old + 1));
      if (pos == SIZE) {
        return;
      }
      doGenerate(uidSet, pos);
    }
  }

  void doGenerate(Set<Long> uidSet, int idx) {
    long uid = uidGenerator.getUID();
    String parsedInfo = uidGenerator.parseUID(uid);
    uidSet.add(uid);

    assertTrue(uid > 0L);
    assertFalse(Strings.isNullOrEmpty(parsedInfo));

    if (VERBOSE) {
      System.out.println(Thread.currentThread().getName() + " No." + idx + " >>> " + parsedInfo);
    }
  }

  void checkUniqueID(Set<Long> uidSet) {
    System.out.println(uidSet.size());
    assertEquals(SIZE, uidSet.size());
  }
}
