package com.dotop.smartwater.project.third.server.meterread.config.mybatis;

import com.dotop.smartwater.dependence.core.utils.BeanUtils;
import com.dotop.smartwater.project.module.core.water.model.BodyMap;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @date 2019年4月29日
 */
public class ListObjectTypeHandler extends BaseTypeHandler<List<Object>> {

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, List<Object> parameter, JdbcType jdbcType)
            throws SQLException {
        if (parameter != null && !parameter.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            Object o0 = parameter.get(0);
            if (o0 instanceof String) {
                List<String> strs = BeanUtils.copy(parameter, String.class);
                ps.setString(i, String.join(";#;", strs));
            } else if (o0 instanceof BodyMap) {
                for (Object para : parameter) {
                    BodyMap map = (BodyMap) para;
                    String key = map.getKey();
                    String val = map.getVal();
                    sb.append(key + ":#:" + val);
                    sb.append(";#;");
                }
                sb.delete(sb.length() - 3, sb.length());
                ps.setString(i, sb.toString());
            }
        } else {
            ps.setString(i, null);
        }
    }

    @Override
    public List<Object> getNullableResult(ResultSet rs, String columnName) throws SQLException {
        List<Object> list = new ArrayList<>();
        String str = rs.getString(columnName);
        if (rs.wasNull()) {
            return null;
        }
        String[] kvMap = str.split(";#;");
        for (String kvs : kvMap) {
            String[] kv = kvs.split(":#:");
            if (kv.length == 1) {
                list.add(kv[0]);
            } else if (kv.length == 2) {
                list.add(new BodyMap(kv[0], kv[1]));
            }
        }
        return list;
    }

    @Override
    public List<Object> getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        List<Object> list = new ArrayList<>();
        String str = rs.getString(columnIndex);
        if (rs.wasNull()) {
            return null;
        }
        String[] kvMap = str.split(";#;");
        for (String kvs : kvMap) {
            String[] kv = kvs.split(":#:");
            if (kv.length == 1) {
                list.add(kv[0]);
            } else if (kv.length == 2) {
                list.add(new BodyMap(kv[0], kv[1]));
            }
        }
        return list;
    }

    @Override
    public List<Object> getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        List<Object> list = new ArrayList<>();
        String str = cs.getString(columnIndex);
        if (cs.wasNull()) {
            return null;
        }
        String[] kvMap = str.split(";#;");
        for (String kvs : kvMap) {
            String[] kv = kvs.split(":#:");
            if (kv.length == 1) {
                list.add(kv[0]);
            } else if (kv.length == 2) {
                list.add(new BodyMap(kv[0], kv[1]));
            }
        }
        return list;
    }

}
