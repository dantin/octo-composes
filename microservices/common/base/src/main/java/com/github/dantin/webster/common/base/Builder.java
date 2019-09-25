package com.github.dantin.webster.common.base;

import java.util.function.Supplier;

/**
 * Interface for builders, to be able to accept a builder in addition to an instance.
 *
 * <p>This interface is similar to {@link Supplier} as it provides an instance, only for classes
 * that act as instance builders (fluent API builder pattern), where method {@link Supplier#get()}
 * would be misleading.
 *
 * @param <T> Type of the built instance
 */
@FunctionalInterface
public interface Builder<T> extends Supplier<T> {

  /**
   * Build the instance from this builder.
   *
   * @return instance of the built type
   */
  T build();

  @Override
  default T get() {
    return build();
  }
}
