package com.dotop.pipe.auth.web.api.factory.area;

import com.dotop.pipe.core.form.AreaForm;
import com.dotop.pipe.core.vo.area.AreaModelVo;
import com.dotop.pipe.core.vo.area.AreaTreeVo;
import com.dotop.smartwater.dependence.core.common.BaseFactory;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.pagination.Pagination;

import java.util.List;

public interface IAreaFactory extends BaseFactory<AreaForm, AreaModelVo> {

	/**
	 * 添加区域
	 * 
	 * @param areaModel
	 * @throws FrameworkRuntimeException
	 */
	public AreaModelVo add(AreaForm areaForm) throws FrameworkRuntimeException;

	/**
	 * 添加企业时添加 区域的根节点
	 * 
	 * @param name
	 * @param des
	 * @param operEid
	 * @throws FrameworkRuntimeException
	 */
	public void addAreaRoot(String name, String des, String operEid) throws FrameworkRuntimeException;

	/**
	 * 区域停用
	 * 
	 * @param string
	 * 
	 * @param areaCode
	 * @return
	 * @throws FrameworkRuntimeException
	 */
	public String del(AreaForm areaForm) throws FrameworkRuntimeException;

	/**
	 * 展示区域树信息
	 * 
	 * @param enterpriseId
	 * 
	 * @return
	 * @throws FrameworkRuntimeException
	 */
	public AreaTreeVo showTreeDetails(String areaId) throws FrameworkRuntimeException;

	/**
	 * 区域列表查询
	 * 
	 * @param page
	 * @param pageSize
	 * @return
	 */
	public List<AreaModelVo> list(AreaForm areaForm) throws FrameworkRuntimeException;

	/**
	 * 查询节点
	 * 
	 * @param areaCode
	 * @return
	 * 
	 * 		public AreaModelVo getNodeDetails(String areaId) throws
	 *         FrameworkRuntimeException;
	 */

	/**
	 * 修改区域
	 * 
	 * @param areaId
	 * @param name
	 * @param des
	 * @param enterpriseId
	 * @return
	 */
	public AreaModelVo edit(AreaForm areaForm) throws FrameworkRuntimeException;

	/**
	 * 查询所有节点
	 * 
	 * @param areaForm
	 * @return
	 * @throws FrameworkRuntimeException
	 */
	public List<AreaModelVo> listAll(AreaForm areaForm) throws FrameworkRuntimeException;

	/**
	 * 修改区域节点位置
	 * 
	 * @param areaForm
	 * @return
	 * @throws FrameworkRuntimeException
	 */
	public AreaTreeVo editNodeParent(AreaForm areaForm) throws FrameworkRuntimeException;

	/**
	 * 地图描边功能新增
	 * 
	 * @param areaForm
	 */
	AreaModelVo addDrawArea(AreaForm areaForm)throws FrameworkRuntimeException;

	/**
	 * 地图描边功能修改
	 * 
	 * @param areaForm
	 */
	void editDrawArea(AreaForm areaForm)throws FrameworkRuntimeException;

	public List<AreaModelVo> drawAreaList(AreaForm areaForm)throws FrameworkRuntimeException;

	public Pagination<AreaModelVo> page(AreaForm areaForm)throws FrameworkRuntimeException;

}
