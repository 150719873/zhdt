package com.dotop.pipe.api.dao.point;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.dotop.pipe.core.form.PointForm;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;
import org.springframework.dao.DataAccessException;

import com.dotop.pipe.core.dto.point.PointDto;
import com.dotop.pipe.core.vo.point.PointVo;
import com.dotop.smartwater.dependence.core.common.BaseDao;

// 坐标
public interface IPointDao extends BaseDao<PointDto, PointVo> {

	// 新增坐标
	public void add(PointDto pointDto) throws DataAccessException;

	// 获取坐标
	public PointVo get(PointDto pointDto) throws DataAccessException;

	// 获取坐标列表
	public List<PointVo> list(PointDto pointDto) throws DataAccessException;

	// 获取坐标列表总数
	public Integer listCount(PointDto pointDto) throws DataAccessException;

	// 更新坐标
	public Integer edit(PointDto pointDto) throws DataAccessException;

	// 删除坐标
	public Integer del(PointDto pointDto) throws DataAccessException;

	@MapKey("code")
	public Map<String, PointVo> mapAll(@Param("operEid") String operEid, @Param("isDel") Integer isDel)
			throws DataAccessException;

	public void addList(@Param("operEid") String operEid, @Param("curr") Date curr, @Param("userBy") String userBy,
			@Param("isDel") Integer isDel, @Param("points") List<PointForm> points);

}
