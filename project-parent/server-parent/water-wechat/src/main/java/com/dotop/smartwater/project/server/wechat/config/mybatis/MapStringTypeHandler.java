package com.dotop.smartwater.project.server.wechat.config.mybatis;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

public class MapStringTypeHandler extends BaseTypeHandler<Map<String, String>> {

	@Override
	public void setNonNullParameter(PreparedStatement ps, int i, Map<String, String> map, JdbcType jdbcType)
			throws SQLException {
		if (map != null && map.size() > 0) {
			StringBuffer sb = new StringBuffer();
			for (String key : map.keySet()) {
				String val = map.get(key);
				sb.append(key + ":" + val);
				sb.append(",");
			}
			sb.deleteCharAt(sb.length() - 1);
			ps.setString(i, sb.toString());
		} else {
			ps.setString(i, null);
		}
	}

	@Override
	public Map<String, String> getNullableResult(ResultSet rs, String columnName) throws SQLException {
		String str = rs.getString(columnName);
		if (rs.wasNull()) {
			return null;
		}
		Map<String, String> map = new HashMap<String, String>();
		String[] kvMap = str.split(",");
		for (String kvs : kvMap) {
			String[] kv = kvs.split(":");
			map.put(kv[0], kv[1]);
		}
		return map;
	}

	@Override
	public Map<String, String> getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
		String str = rs.getString(columnIndex);
		if (rs.wasNull()) {
			return null;
		}
		Map<String, String> map = new HashMap<String, String>();
		String[] kvMap = str.split(",");
		for (String kvs : kvMap) {
			String[] kv = kvs.split(":");
			map.put(kv[0], kv[1]);
		}
		return map;
	}

	@Override
	public Map<String, String> getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
		String str = cs.getString(columnIndex);
		if (cs.wasNull()) {
			return null;
		}
		Map<String, String> map = new HashMap<String, String>();
		String[] kvMap = str.split(",");
		for (String kvs : kvMap) {
			String[] kv = kvs.split(":");
			map.put(kv[0], kv[1]);
		}
		return map;
	}

}
