package com.dotop.pipe.web.api.factory.point;

import java.util.List;

import com.dotop.pipe.core.form.PointForm;
import com.dotop.pipe.core.vo.point.PointVo;
import com.dotop.smartwater.dependence.core.common.BaseFactory;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.pagination.Pagination;

// 坐标
public interface IPointFactory extends BaseFactory<PointForm, PointVo> {

	// 新增坐标
	public PointVo add(PointForm pointForm) throws FrameworkRuntimeException;

	// 获取坐标
	public PointVo get(PointForm pointForm) throws FrameworkRuntimeException;

	// 获取坐标分页
	public Pagination<PointVo> page(PointForm pointForm) throws FrameworkRuntimeException;

	// 获取坐标列表(主要用在地图显示)
	public List<PointVo> list(PointForm pointForm) throws FrameworkRuntimeException;

	// 更新坐标
	public PointVo edit(PointForm pointForm) throws FrameworkRuntimeException;

	// 删除坐标
	public String del(PointForm pointForm) throws FrameworkRuntimeException;

	public List<PointVo> realList(PointForm pointForm) throws FrameworkRuntimeException;

}
