package com.dotop.smartwater.project.module.dao.device;

import com.dotop.smartwater.dependence.core.common.BaseDao;
import com.dotop.smartwater.project.module.core.water.dto.DeviceBookManagementDto;
import com.dotop.smartwater.project.module.core.water.vo.DeviceBookManagementVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**

 */
public interface IDeviceBookManagementDao extends BaseDao<DeviceBookManagementDto, DeviceBookManagementVo> {

    @Override
    Integer del(DeviceBookManagementDto deviceBookManagementDto);

    @Override
    void add(DeviceBookManagementDto deviceBookManagementDto);

    @Override
    DeviceBookManagementVo get(DeviceBookManagementDto deviceBookManagementDto);

    @Override
    Integer edit(DeviceBookManagementDto deviceBookManagementDto);

    @Override
    List<DeviceBookManagementVo> list(DeviceBookManagementDto deviceBookManagementDto);

    List<String> findReadersbyAreas(@Param("list") List<String> list,@Param("enterpriseid")String enterpriseid);
    
    Integer judgeIfExistWorker(DeviceBookManagementDto deviceBookManagementDto);
    
    Integer judgeIfExistOwner(DeviceBookManagementDto deviceBookManagementDto);

    List<String> getAreaIdsByUserid(@Param("userid")String userid, @Param("enterpriseid")String enterpriseid);
}