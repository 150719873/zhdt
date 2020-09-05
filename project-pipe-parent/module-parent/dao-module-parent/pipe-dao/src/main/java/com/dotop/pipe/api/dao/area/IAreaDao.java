package com.dotop.pipe.api.dao.area;

import com.dotop.pipe.core.dto.area.AreaDto;
import com.dotop.pipe.core.vo.area.AreaModelVo;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;
import org.springframework.dao.DataAccessException;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface IAreaDao {

	/**
	 * 新增区域
	 *
	 * @param areaCode
	 * @param name
	 * @param des
	 * @param isLeaf
	 * @param parentCode
	 * @param createBy
	 * @param createDate
	 * @param isDel
	 * @param enterpriseId
	 * @param isParent
	 * @param uuid
	 * @throws DataAccessException
	 */
	public void add(@Param("areaCode") String areaCode, @Param("name") String name, @Param("des") String des,
			@Param("isLeaf") Integer isLeaf, @Param("parentCode") String parentCode,
			@Param("areaColorNum") String areaColorNum, @Param("scale") String scale,
			@Param("createBy") String createBy, @Param("createDate") Date createDate, @Param("isDel") Integer isDel,
			@Param("enterpriseId") String enterpriseId, @Param("isParent") Integer isParent, @Param("uuid") String uuid)
			throws DataAccessException;

	/**
	 * 查询父节点下最大的子节点 已查
	 *
	 * @param parentCode
	 * @return
	 */
	public String selectMaxAreaCodeByParentCode(String parentCode) throws DataAccessException;

	/**
	 * 校验areaCode 是否存在 已查
	 *
	 * @param uUid
	 *
	 * @param newAreaCode
	 * @return
	 */
	public AreaModelVo getByAreaCode(@Param("newAreaCode") String newAreaCode, @Param("uuid") String uuid)
			throws DataAccessException;

	/**
	 * 更新节点为父节点 已查
	 *
	 * @param parentCode
	 * @param isParent
	 * @param enterpriseId
	 * @return
	 * @throws DataAccessException
	 */
	public int updateIsParentByAreaCode(@Param("parentCode") String parentCode, @Param("isParent") Integer isParent,
			@Param("enterpriseId") String enterpriseId,@Param("date") Date date) throws DataAccessException;

	/**
	 * 区域停用 已查
	 *
	 * @param areaCode
	 * @param isDel
	 * @param date
	 * @param userBy
	 * @param operEid
	 * @return
	 */
	public int del(@Param("areaId") String areaId, @Param("isDel") Integer isDel, @Param("userBy") String userBy,
			@Param("date") Date date, @Param("operEid") String operEid) throws DataAccessException;

	/**
	 * 区域列表
	 *
	 * @param enterpriseId
	 * @param isDel
	 * @param parentCode
	 * @return
	 * @throws DataAccessException
	 */
	public List<AreaModelVo> list(@Param("enterpriseId") String enterpriseId, @Param("isDel") Integer isDel,
			@Param("parentCode") String parentCode) throws DataAccessException;

	/**
	 * 加载节点信息 已查
	 *
	 * @param operEid
	 * @param areaCode
	 * @param isDel
	 * @return
	 * @throws DataAccessException
	 */
	public AreaModelVo getNodeDetails(@Param("enterpriseId") String operEid, @Param("areaId") String areaId,
			@Param("isDel") Integer isDel) throws DataAccessException;

	/**
	 * 修改区域 已查
	 *
	 * @param areaId
	 * @param name
	 * @param des
	 * @param operEid
	 * @param userBy
	 * @param date
	 * @return
	 * @throws DataAccessException
	 */
	public int edit(AreaDto areaDto) throws DataAccessException;

	/**
	 * 查询父节点下还有几个节点 已查
	 */
	public int selectCountByParentCode(@Param("parentCode") String parentCode, @Param("isDel") Integer isDel,
			@Param("operEid") String operEid) throws DataAccessException;

	/**
	 * 更新isparent 字段 已查
	 *
	 * @param parentCode
	 * @param isNotParent
	 * @param operEid
	 * @return
	 */
	public int updateIsParentByAreaId(@Param("parentCode") String parentCode, @Param("isNotParent") Integer isNotParent,
			@Param("operEid") String operEid,@Param("date") Date date) throws DataAccessException;

	/**
	 * 查询节点列表 已查
	 *
	 * @param operEid
	 * @param isDel
	 * @return
	 */
	public List<AreaModelVo> listAll(AreaDto areaDto) throws DataAccessException;

	public boolean isExist(@Param("newAreaCode") String newAreaCode, @Param("operEid") String operEid,
			@Param("isDel") Integer isDel) throws DataAccessException;

	/**
	 * 查询所有数据 封装成map
	 */
	@MapKey("areaCode")
	public Map<String, AreaModelVo> mapAll(@Param("operEid") String operEid, @Param("isDel") Integer isDel)
			throws DataAccessException;

	public int editNodeParent(@Param("updateList") List<AreaModelVo> updateList, @Param("operEid") String enterpriseId,
			@Param("lastBy") String userBy, @Param("lastDate") Date date, @Param("isDel") Integer isDel);

	/**
	 * 获取地图的数据集合
	 *
	 * @param areaDto
	 * @return
	 */
	public List<AreaModelVo> drawAreaList(AreaDto areaDto);

	boolean isExistChild(@Param("areaId") String areaId, @Param("operEid") String operEid,@Param("isDel") Integer isDel);
}
