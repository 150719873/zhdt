package com.dotop.pipe.api.service.third;

import com.dotop.pipe.core.bo.third.ThirdMapBo;
import com.dotop.pipe.core.vo.third.ThirdMapVo;
import com.dotop.smartwater.dependence.core.common.BaseBo;
import com.dotop.smartwater.dependence.core.common.BaseService;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.pagination.Pagination;

import java.util.Date;
import java.util.List;

public interface IThirdMapService extends BaseService<BaseBo, ThirdMapVo> {

    // 新增第三方与设备关系
    public void add(String enterpriseId, String deviceId, String deviceCode, String thirdId, String thirdCode,
                    Date curr, String userBy, String deviceType, String protocol) throws FrameworkRuntimeException;

    // 更新第三方与设备关系
    public void edit(String enterpriseId, String mapId, String deviceCode, String thirdCode, Date curr, String userBy, String deviceType, String protocol) throws FrameworkRuntimeException;


    // 删除第三方与设备关系
    public void del(String enterpriseId, String deviceId, Date curr, String userBy) throws FrameworkRuntimeException;

    // 删除第三方与设备关系 code删除
    public void delByCode(String enterpriseId, String deviceCode, Date curr, String userBy)
            throws FrameworkRuntimeException;

    // 设备是否存在第三方关系
    public Boolean isExist(String enterpriseId, String deviceId) throws FrameworkRuntimeException;

    // 获取第三方与设备关系
    public ThirdMapVo get(String enterpriseId, String deviceId) throws FrameworkRuntimeException;

    // 获取第三方与设备关系
    public ThirdMapVo get(String enterpriseId, String deviceId, String deviceCode) throws FrameworkRuntimeException;

    // 设备是否存在第三方关系
    public Boolean isExistByThirdId(String enterpriseId, String thirdId) throws FrameworkRuntimeException;

    // 设备是否存在第三方关系
    public Boolean isExistByThirdCode(String enterpriseId, String thirdCode) throws FrameworkRuntimeException;

    // 关联分页查询设备与第三方关系
    public Pagination<ThirdMapVo> page(String enterpriseId, String factoryCode, Integer page, Integer pageSize)
            throws FrameworkRuntimeException;

    public List<ThirdMapVo> list(ThirdMapBo thirdMapBo) throws FrameworkRuntimeException;

    public ThirdMapVo edit(ThirdMapBo thirdMapBo) throws FrameworkRuntimeException;
}
