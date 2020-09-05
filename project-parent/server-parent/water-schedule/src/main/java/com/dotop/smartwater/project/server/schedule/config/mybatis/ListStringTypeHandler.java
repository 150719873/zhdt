package com.dotop.smartwater.project.server.schedule.config.mybatis;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

/**
 * 

 * @date 2019年3月16日
 * @description list转string 格式,使用;#;分隔: a;#;b;#;c 参考例子：
 *              ProcessMapper.xml中的add方法;返回会根据格式自动组装
 */
public class ListStringTypeHandler extends BaseTypeHandler<List<String>> {

	@Override
	public void setNonNullParameter(PreparedStatement ps, int i, List<String> parameter, JdbcType jdbcType)
			throws SQLException {
		if (parameter != null && parameter.size() > 0) {
			ps.setString(i, String.join(";#;", parameter));
		} else {
			ps.setString(i, null);
		}
	}

	@Override
	public List<String> getNullableResult(ResultSet rs, String columnName) throws SQLException {
		String str = rs.getString(columnName);
		if (rs.wasNull()) {
			return null;
		}
		return Arrays.asList(str.split(";#;"));
	}

	@Override
	public List<String> getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
		String str = rs.getString(columnIndex);
		if (rs.wasNull()) {
			return null;
		}
		return Arrays.asList(str.split(";#;"));
	}

	@Override
	public List<String> getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
		String str = cs.getString(columnIndex);
		if (cs.wasNull()) {
			return null;
		}
		return Arrays.asList(str.split(";#;"));
	}

}
