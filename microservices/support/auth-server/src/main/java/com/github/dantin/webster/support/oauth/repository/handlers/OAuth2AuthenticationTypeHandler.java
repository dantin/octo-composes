package com.github.dantin.webster.support.oauth.repository.handlers;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.util.SerializationUtils;

public class OAuth2AuthenticationTypeHandler extends BaseTypeHandler<OAuth2Authentication> {

  @Override
  public void setNonNullParameter(
      PreparedStatement ps, int i, OAuth2Authentication parameter, JdbcType jdbcType)
      throws SQLException {
    byte[] bytes = SerializationUtils.serialize(parameter);
    ps.setBytes(i, bytes);
  }

  @Override
  public OAuth2Authentication getNullableResult(ResultSet rs, String columnName)
      throws SQLException {
    byte[] bytes = rs.getBytes(columnName);
    return (OAuth2Authentication) SerializationUtils.deserialize(bytes);
  }

  @Override
  public OAuth2Authentication getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
    byte[] bytes = rs.getBytes(columnIndex);
    return (OAuth2Authentication) SerializationUtils.deserialize(bytes);
  }

  @Override
  public OAuth2Authentication getNullableResult(CallableStatement cs, int columnIndex)
      throws SQLException {
    byte[] bytes = cs.getBytes(columnIndex);
    return (OAuth2Authentication) SerializationUtils.deserialize(bytes);
  }
}
