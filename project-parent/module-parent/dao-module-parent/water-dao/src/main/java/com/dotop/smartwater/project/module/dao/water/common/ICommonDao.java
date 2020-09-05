package com.dotop.smartwater.project.module.dao.water.common;

import java.util.List;
import java.util.Map;

public interface ICommonDao {

	List<Map<String, Object>> select(Map<String, String> params);

}
