package com.dotop.pipe.api.dao.test;

import org.apache.ibatis.annotations.Param;
import org.springframework.dao.DataAccessException;

import com.dotop.pipe.core.vo.test.TestVo;

import java.util.List;
import java.util.Map;

public interface ITestDao {

	public TestVo get(@Param("id") String id) throws DataAccessException;

	public Integer exchangeEnterpriseId(Map<String,String> map);

	public List<String> getEnterpriseId(Map<String,String> map);

	public List<Map<String, String>> getEidAndEnterpriseId(@Param("tableName") String tableName);
}
