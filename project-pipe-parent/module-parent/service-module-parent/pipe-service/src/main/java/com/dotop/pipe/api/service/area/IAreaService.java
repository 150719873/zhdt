package com.dotop.pipe.api.service.area;

import com.dotop.pipe.core.bo.area.AreaBo;
import com.dotop.pipe.core.form.AreaForm;
import com.dotop.pipe.core.vo.area.AreaModelVo;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.pagination.Pagination;

import java.util.Date;
import java.util.List;
import java.util.Map;

// 区域管理
public interface IAreaService {

    /**
     * 添加区域
     *
     * @param areaForm
     * @param operEid
     * @param userBy
     * @param date
     * @param uUid
     * @return
     * @throws FrameworkRuntimeException
     */
    public AreaModelVo add(AreaForm areaForm, String operEid, String userBy, Date date)
            throws FrameworkRuntimeException;

    /**
     * 查询 父节点下的最大的子节点
     *
     * @param parentCode
     * @return
     * @throws FrameworkRuntimeException
     */
    public String selectMaxAreaCodeByParentCode(String parentCode) throws FrameworkRuntimeException;

    /**
     * 更新其父节点是根节点
     *
     * @param parentCode
     * @param areaForm.getEnterpriseId()
     * @throws FrameworkRuntimeException
     */
    public int updateIsParentByAreaCode(String parentCode, String enterpriseId, Date date) throws FrameworkRuntimeException;

    /**
     * 创建企业时 添加根节点
     *
     * @param areaCode1
     * @param operEid
     * @param userBy
     * @param date
     * @param uuid
     */
    public void addAreaRoot(String areaCode1, String name, String operEid, String userBy, String des, Date date)
            throws FrameworkRuntimeException;

    /**
     * 停用区域
     *
     * @param date
     * @param userBy
     * @param operEid
     * @param areaCode
     * @return
     * @throws FrameworkRuntimeException
     */
    public int del(String areaId, String userBy, Date date, String operEid) throws FrameworkRuntimeException;

    /**
     * 区域查询
     *
     * @param operEid
     * @param page
     * @param pageSize
     * @return
     */
    public List<AreaModelVo> list(String operEid) throws FrameworkRuntimeException;

    public AreaModelVo getNodeDetails(String operEid, String areaId) throws FrameworkRuntimeException;

    /**
     * 修改区域信息
     *
     * @param areaId
     * @param name
     * @param des
     * @param operEid
     * @param userBy
     * @return
     */
    public int edit(AreaBo areabo) throws FrameworkRuntimeException;

    /**
     * 校验新areaCode 是否唯一
     *
     * @param uUid2
     * @param newAreaCode
     * @param operEid
     * @return
     */
    public boolean isExist(String newAreaCode, String operEid) throws FrameworkRuntimeException;

    /**
     * 查询节点的父节点下还有几个没被删除的节点
     *
     * @param parentCode
     * @param operEid
     * @return
     */
    public int selectCountByParentCode(String parentCode, String operEid) throws FrameworkRuntimeException;

    /**
     * 更新isParent字段
     *
     * @param parentCode
     * @param operEid
     * @return
     */
    public int updateIsParentByAreaId(String parentCode, String operEid, Date date) throws FrameworkRuntimeException;

    /**
     * 查询区域节点列表
     *
     * @param enterpriseId
     * @return
     */
    public List<AreaModelVo> listAll(String enterpriseId) throws FrameworkRuntimeException;

    /**
     * 查询区域节点列表
     *
     * @param enterpriseId
     * @return
     */
    public Map<String, AreaModelVo> mapAll(String enterpriseId) throws FrameworkRuntimeException;

    /**
     * 修改节点位置
     *
     * @param updateList
     * @param enterpriseId
     * @param userBy
     * @param date
     * @return
     */
    public int editNodeParent(List<AreaModelVo> updateList, String enterpriseId, String userBy, Date date);

    /**
     * 地图区域描边 获取地图描边的数据
     *
     * @param areaBo
     * @return
     */
    public List drawAreaList(AreaBo areaBo);

    public Pagination<AreaModelVo> page(AreaBo areaBo);

    /**
     * 判断该区域下是否存在子区域
     *
     * @param operEid
     * @param areaId
     * @return
     */
    boolean isExistChild(String operEid, String areaId);
}
