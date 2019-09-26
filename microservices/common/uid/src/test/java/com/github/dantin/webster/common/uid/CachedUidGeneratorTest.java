package com.github.dantin.webster.common.uid;

import static org.junit.Assert.*;

import com.google.common.base.Strings;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.Before;
import org.junit.Test;

/** Unit test for {@link CachedUidGenerator}. */
public class CachedUidGeneratorTest extends UidGeneratorTest {

  @Before
  public void setUp() {
    uidGenerator = UidGenerator.cachedUidGeneratorBuilder(1L).build();
  }

  @Test
  public void testGenerate() {
    long uid = uidGenerator.getUID();
    String parsedInfo = uidGenerator.parseUID(uid);

    assertTrue(uid > 0L);
    assertFalse(Strings.isNullOrEmpty(parsedInfo));

    if (VERBOSE) {
      System.out.println(parsedInfo);
    }
  }

  @Test
  public void testSerialGenerate() {
    // Generate UID serially.
    Set<Long> uidSet = new HashSet<>(SIZE);

    for (int i = 0; i < SIZE; i++) {
      doGenerate(uidSet, i);
    }

    checkUniqueID(uidSet);
  }

  @Test
  public void testParallelGenerate() throws InterruptedException {
    // Generate UID parallel.
    AtomicInteger control = new AtomicInteger(-1);
    Set<Long> uidSet = new ConcurrentSkipListSet<>();

    List<Thread> threads = new ArrayList<>(THREADS);
    for (int i = 0; i < THREADS; i++) {
      Thread thread = new Thread(() -> workRun(uidSet, control));
      thread.setName("UID-Generator-" + i);

      threads.add(thread);
      thread.start();
    }

    // wait till done.
    for (Thread thread : threads) {
      thread.join();
    }

    assertEquals(SIZE, control.get());

    checkUniqueID(uidSet);
  }
}
