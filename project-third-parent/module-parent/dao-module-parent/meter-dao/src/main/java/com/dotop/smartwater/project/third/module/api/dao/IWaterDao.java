package com.dotop.smartwater.project.third.module.api.dao;

import com.dotop.smartwater.dependence.core.common.BaseDao;
import com.dotop.smartwater.dependence.core.common.BaseDto;
import com.dotop.smartwater.dependence.core.common.BaseVo;
import com.dotop.smartwater.project.module.core.auth.dto.EnterpriseDto;
import com.dotop.smartwater.project.module.core.auth.vo.EnterpriseVo;
import com.dotop.smartwater.project.module.core.water.bo.DeviceDownlinkBo;
import com.dotop.smartwater.project.module.core.water.dto.DeviceDto;
import com.dotop.smartwater.project.module.core.water.dto.DeviceUplinkDto;
import com.dotop.smartwater.project.module.core.water.dto.OwnerDto;
import com.dotop.smartwater.project.module.core.water.vo.DeviceDownlinkVo;
import com.dotop.smartwater.project.module.core.water.vo.DeviceUplinkVo;
import com.dotop.smartwater.project.module.core.water.vo.DeviceVo;
import com.dotop.smartwater.project.module.core.water.vo.OwnerVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface IWaterDao extends BaseDao<BaseDto, BaseVo> {

    /**
     * 根据水表号和月份查询最新上行数据
     * @return
     */
    List<DeviceUplinkVo> getUplinkData(@Param("deveuis") String[] deveuis, @Param("preMonth") String preMonth, @Param("thisMonth") String thisMonth);

    /**
     * 获取设备列表
     * @param deviceDto
     * @return
     */
    List<DeviceVo> getList(DeviceDto deviceDto);

    /**
     * 获取用户的信息列表
     * @param ownerDto
     * @return
     */
    List<OwnerVo> getOwnerList(OwnerDto ownerDto);

    /**
     * 根据ownerid在用户列表中查找对应的owner
     * @param ownerid
     * @return
     */
    OwnerVo findByOwnerId(String ownerid);

    /**
     * 获取下行数据
     * @param clientIds
     * @return
     */
    List<DeviceDownlinkVo> getDownLinkData(@Param("clientIds") List<String> clientIds);

    /**
     * 查询最新抄表数据并分页
     * @param deviceUplinkDto
     * @param preMonth
     * @param thisMonth
     * @return
     */
    List<DeviceUplinkVo> getUplinkList(@Param("deviceUplinkDto") DeviceUplinkDto deviceUplinkDto, @Param("preMonth") String preMonth, @Param("thisMonth") String thisMonth);

    EnterpriseVo checkEnterpriseId(EnterpriseDto enterpriseDto);

    DeviceVo findByDevNo(@Param("devno") String devno);
}
