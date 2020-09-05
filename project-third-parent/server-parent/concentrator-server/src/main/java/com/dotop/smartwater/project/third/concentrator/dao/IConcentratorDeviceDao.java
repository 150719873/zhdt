package com.dotop.smartwater.project.third.concentrator.dao;

import com.dotop.smartwater.project.third.concentrator.core.bo.TerminalMeterReadBo;
import com.dotop.smartwater.project.third.concentrator.core.dto.ConcentratorDeviceDto;
import com.dotop.smartwater.project.third.concentrator.core.vo.ConcentratorDeviceVo;
import com.dotop.smartwater.dependence.core.common.BaseDao;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 集中器设备数据库层接口
 *
 *
 */
public interface IConcentratorDeviceDao extends BaseDao<ConcentratorDeviceDto, ConcentratorDeviceVo> {

    /**
     * 获取一条集中器设备信息
     *
     * @param concentratorDeviceDto
     * @return
     */
    @Override
    ConcentratorDeviceVo get(ConcentratorDeviceDto concentratorDeviceDto);

    /**
     * 查询集中器设备列表
     *
     * @param concentratorDeviceDto
     * @return
     */
    @Override
    List<ConcentratorDeviceVo> list(ConcentratorDeviceDto concentratorDeviceDto);

    /**
     * 添加一条集中器设备
     *
     * @param concentratorDeviceDto
     */
    void addConcentratorDevice(ConcentratorDeviceDto concentratorDeviceDto);

    /**
     * 更新一条集中器设备信息
     *
     * @param concentratorDeviceDto
     */
    Integer updateConcentratorDevice(ConcentratorDeviceDto concentratorDeviceDto);

    /**
     * 删除一条集中器设备记录
     *
     * @param concentratorDeviceDto
     */
    int delete(ConcentratorDeviceDto concentratorDeviceDto);

    /**
     * 统计一个集中器下挂了多少个水表
     *
     * @param id
     * @param devno
     * @return
     */
    Integer countConcentratorDevice(@Param("id") String id, @Param("devno") String devno, @Param("enterpriseid") String enterpriseid);

    /**
     * 统计一个集中器下挂了多少个采集器通道
     *
     * @param id
     * @param code
     * @return
     */
    Integer countCollectorChannel(@Param("id") String id, @Param("code") String code, @Param("enterpriseid") String enterpriseid);

    /**
     * 统计一个采集器下挂了多少个水表
     *
     * @param id
     * @param devno
     * @return
     */
    Integer countCollectorDevice(@Param("id") String id, @Param("devno") String devno, @Param("enterpriseid") String enterpriseid);


    //  List<ConcentratorDeviceVo> findByCodeAndNo(@Param("terminalMeterReadBos") List<TerminalMeterReadBo> terminalMeterReadBos);

    int updateNo(List<ConcentratorDeviceDto> concentratorDeviceDtos);

    List<ConcentratorDeviceVo> findByCodeAndNo(@Param("enterpriseid") String enterpriseid, @Param("terminalMeterReadBos") List<TerminalMeterReadBo> terminalMeterReadBos, @Param("concentratorCode") String concentratorCode);

    int setTapstatus(ConcentratorDeviceDto concentratorDeviceDto);

    @Override
    void adds(List<ConcentratorDeviceDto> concentratorDeviceDtos);

    List<ConcentratorDeviceVo> findByConcentratorCode(@Param("enterpriseid")String enterpriseid,  @Param("concentratorCode")String concentratorCode);
}
