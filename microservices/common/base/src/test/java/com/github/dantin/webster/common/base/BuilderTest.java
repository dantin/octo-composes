package com.github.dantin.webster.common.base;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.Test;

/** Unit test for {@link Builder}. */
public class BuilderTest {

  @Test
  public void test1() {
    String message = "Hello world";

    Result1 r = Result1.builder().message(message).build();

    assertThat(r.getMessage(), is(message));
  }

  @Test
  public void test2() {
    String name = "Hello worlds";

    Result2 r = Result2.builder().name(name).build();

    assertThat(r.getName(), is(name));
  }

  private static class Result1 {
    private final String message;

    public Result1(Builder1 builder) {
      this.message = builder.message;
    }

    static Builder1 builder() {
      return new Builder1();
    }

    String getMessage() {
      return message;
    }
  }

  private static class Result2 {
    private final String name;

    public Result2(Builder2 builder) {
      this.name = builder.name;
    }

    static Builder2 builder() {
      return new Builder2();
    }

    String getName() {
      return name;
    }
  }

  private static class Builder1 implements Builder<Result1> {
    private String message;

    private Builder1() {}

    Builder1 message(String message) {
      this.message = message;
      return this;
    }

    @Override
    public Result1 build() {
      return new Result1(this);
    }
  }

  private static class Builder2 implements Builder<Result2> {
    private String name;

    private Builder2() {}

    Builder2 name(String name) {
      this.name = name;
      return this;
    }

    @Override
    public Result2 build() {
      return new Result2(this);
    }
  }
}
