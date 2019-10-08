package com.github.dantin.webster.support.oauth.repository.handlers;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.springframework.security.oauth2.common.OAuth2RefreshToken;
import org.springframework.util.SerializationUtils;

public class OAuth2RefreshTokenTypeHandler extends BaseTypeHandler<OAuth2RefreshToken> {

  @Override
  public void setNonNullParameter(
      PreparedStatement ps, int i, OAuth2RefreshToken parameter, JdbcType jdbcType)
      throws SQLException {
    byte[] bytes = SerializationUtils.serialize(parameter);
    ps.setBytes(i, bytes);
  }

  @Override
  public OAuth2RefreshToken getNullableResult(ResultSet rs, String columnName) throws SQLException {
    byte[] bytes = rs.getBytes(columnName);
    return (OAuth2RefreshToken) SerializationUtils.deserialize(bytes);
  }

  @Override
  public OAuth2RefreshToken getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
    byte[] bytes = rs.getBytes(columnIndex);
    return (OAuth2RefreshToken) SerializationUtils.deserialize(bytes);
  }

  @Override
  public OAuth2RefreshToken getNullableResult(CallableStatement cs, int columnIndex)
      throws SQLException {
    byte[] bytes = cs.getBytes(columnIndex);
    return (OAuth2RefreshToken) SerializationUtils.deserialize(bytes);
  }
}
