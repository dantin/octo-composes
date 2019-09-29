package com.github.dantin.webster.support.oauth;

import java.io.IOException;
import java.io.Reader;
import java.sql.Connection;
import java.sql.SQLException;
import javax.sql.DataSource;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.jdbc.ScriptRunner;

public class BaseDataTest {

  private BaseDataTest() {
    throw new AssertionError();
  }

  public static void runScript(DataSource ds, String resource) throws IOException, SQLException {
    try (Connection connection = ds.getConnection()) {
      ScriptRunner runner = new ScriptRunner(connection);
      runner.setAutoCommit(true);
      runner.setStopOnError(false);
      runner.setLogWriter(null);
      runner.setErrorLogWriter(null);
      runScript(runner, resource);
    }
  }

  public static void runScript(ScriptRunner runner, String resource) throws IOException {
    try (Reader reader = Resources.getResourceAsReader(resource)) {
      runner.runScript(reader);
    }
  }
}
