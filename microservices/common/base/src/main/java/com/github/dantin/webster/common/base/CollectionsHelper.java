package com.github.dantin.webster.common.base;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/** A set of static methods for Collections. */
public class CollectionsHelper {
  private CollectionsHelper() {
    // suppress default constructor for non-instantiable.
    throw new AssertionError();
  }

  /**
   * Returns an immutable list containing zero elements.
   *
   * @param <T> the {@code List}'s element type
   * @return an empty {@code List}
   */
  public static <T> List<T> listOf() {
    return Collections.emptyList();
  }

  /**
   * Returns an immutable list containing the given elements.
   *
   * @param <T> the {@code List}'s element type
   * @param elems the elements
   * @return a {@code List} containing the specified elements
   */
  @SafeVarargs
  public static <T> List<T> listOf(T... elems) {
    List<T> list = Arrays.asList(elems);
    return Collections.unmodifiableList(list);
  }

  /**
   * Returns an immutable set containing zero elements.
   *
   * @param <T> the {@code Set}'s element type
   * @return an empty {@code Set}
   */
  public static <T> Set<T> setOf() {
    return Collections.emptySet();
  }

  /**
   * Returns an immutable set containing the given elements.
   *
   * @param <T> the {@code Set}'s element type
   * @param elems the elements
   * @return a {@code Set} containing the specified elements
   */
  @SafeVarargs
  public static <T> Set<T> setOf(T... elems) {
    Set<T> set = new HashSet<>();
    set.addAll(Arrays.asList(elems));
    return Collections.unmodifiableSet(set);
  }

  /**
   * Returns an immutable map containing zero mappings.
   *
   * @param <K> the {@code Map}'s key type
   * @param <V> the {@code Map}'s value type
   * @return an empty {@code Map}
   */
  public static <K, V> Map<K, V> mapOf() {
    return Collections.emptyMap();
  }

  /**
   * Returns an immutable map containing a single mappings.
   *
   * @param <K> the {@code Map}'s key type
   * @param <V> the {@code Map}'s value type
   * @return a {@code Map} containing the specified mappings
   */
  public static <K, V> Map<K, V> mapOf(K k1, V v1) {
    Map<K, V> map = new HashMap<>();
    map.put(k1, v1);
    return Collections.unmodifiableMap(map);
  }

  /**
   * Returns an immutable map containing a single mappings.
   *
   * @param <K> the {@code Map}'s key type
   * @param <V> the {@code Map}'s value type
   * @return a {@code Map} containing the specified mappings
   */
  public static <K, V> Map<K, V> mapOf(K k1, V v1, K k2, V v2) {
    Map<K, V> map = new HashMap<>();
    map.put(k1, v1);
    return Collections.unmodifiableMap(map);
  }
}
