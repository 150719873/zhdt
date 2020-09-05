package com.dotop.pipe.api.dao.third;

import java.util.List;

import org.springframework.dao.DataAccessException;

import com.dotop.pipe.core.dto.third.ThirdMapDto;
import com.dotop.pipe.core.vo.third.ThirdMapVo;
import com.dotop.smartwater.dependence.core.common.BaseDao;

public interface IThirdMapDao extends BaseDao<ThirdMapDto, ThirdMapVo> {

	// 新增设备与第三方关系
	public void add(ThirdMapDto thirdMapDto) throws DataAccessException;

	// 删除设备与第三方关系
	public Integer del(ThirdMapDto thirdMapDto) throws DataAccessException;

	// 设备与第三方关系是否存在
	public Boolean isExist(ThirdMapDto thirdMapDto) throws DataAccessException;

	// 获取设备与第三方关系
	public ThirdMapVo get(ThirdMapDto thirdMapDto) throws DataAccessException;

	// 获取列表
	public List<ThirdMapVo> list(ThirdMapDto thirdMapDto) throws DataAccessException;

	public Integer edit(ThirdMapDto thirdMapDto) throws DataAccessException;
}
