package com.dotop.smartwater.project.third.meterread.webservice.dao.water;

import com.dotop.smartwater.dependence.core.common.BaseDao;
import com.dotop.smartwater.dependence.core.common.BaseDto;
import com.dotop.smartwater.dependence.core.common.BaseVo;
import com.dotop.smartwater.project.module.core.water.bo.DeviceDownlinkBo;
import com.dotop.smartwater.project.module.core.water.dto.DeviceDto;
import com.dotop.smartwater.project.module.core.water.dto.OwnerDto;
import com.dotop.smartwater.project.module.core.water.dto.OwnerRecordDto;
import com.dotop.smartwater.project.module.core.water.vo.*;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface IWaterDao extends BaseDao<BaseDto, BaseVo> {

    /**
     * 根据水表号和月份查询最新上行数据
     *
     * @return
     */
    List<DeviceUplinkVo> getUplinkData(@Param("devids") List<String> devids, @Param("pppreMonth") String pppreMonth,
                                       @Param("ppreMonth") String ppreMonth, @Param("preMonth") String preMonth,
                                       @Param("thisMonth") String thisMonth);

    /**
     * 获取设备列表
     *
     * @param deviceDto
     * @return
     */
    List<DeviceVo> getList(DeviceDto deviceDto);

    /**
     * 获取用户的信息列表
     *
     * @param ownerDto
     * @return
     */
    List<OwnerVo> getOwnerList(OwnerDto ownerDto);

    /**
     * 根据ownerid在用户列表中查找对应的owner
     *
     * @param ownerid
     * @return
     */
    OwnerVo findByOwnerId(String ownerid);

    /**
     * 获取下行数据
     *
     * @param deviceDownlinkBos
     * @return
     */
    List<DeviceDownlinkVo> getDownLinkData(@Param("deviceDownlinkBos") List<DeviceDownlinkBo> deviceDownlinkBos);


    /**
     * 水表信息
     *
     * @param deviceDto
     */
    DeviceVo getDevice(DeviceDto deviceDto);

    /**
     * 换表记录
     */
    List<OwnerRecordVo> ownerRecords(OwnerRecordDto ownerRecordDto);
}
