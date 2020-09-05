package com.dotop.pipe.api.service.point;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.dotop.pipe.core.dto.point.PointDto;
import com.dotop.pipe.core.form.PointForm;
import com.dotop.pipe.core.vo.point.PointVo;
import com.dotop.smartwater.dependence.core.common.BaseBo;
import com.dotop.smartwater.dependence.core.common.BaseService;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.pagination.Pagination;

// 坐标
public interface IPointService extends BaseService<BaseBo, PointVo> {

	// 新增坐标
	public PointVo add(String enterpriseId, String code, String name, String des, BigDecimal longitude,
			BigDecimal latitude, String remark, Date curr, String userBy) throws FrameworkRuntimeException;

	// 获取坐标
	public PointVo get(String enterpriseId, String pointId) throws FrameworkRuntimeException;

	// 通过编码获取坐标
	public PointVo getByCode(String enterpriseId, String code) throws FrameworkRuntimeException;

	// 获取坐标分页
	public Pagination<PointVo> page(String enterpriseId, Integer page, Integer pageSize)
			throws FrameworkRuntimeException;

	// 获取坐标列表(主要用在地图显示)
	public List<PointVo> page(String enterpriseId, Integer limit) throws FrameworkRuntimeException;

	// 更新坐标
	public PointVo edit(String enterpriseId, String pointId, String code, String name, String des, BigDecimal longitude,
			BigDecimal latitude, String remark, Date curr, String userBy) throws FrameworkRuntimeException;

	// 删除坐标
	public String del(String enterpriseId, String pointId, Date curr, String userBy) throws FrameworkRuntimeException;

	public Map<String, PointVo> mapAll(String operEid) throws FrameworkRuntimeException;

	public void addList(String operEid, Date curr, String userBy, List<PointForm> points);

	List<PointVo> realList(PointDto pointDto);

}
