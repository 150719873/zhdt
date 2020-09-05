package com.dotop.smartwater.project.third.concentrator.service;

import com.dotop.smartwater.project.third.concentrator.core.bo.ConcentratorDeviceBo;
import com.dotop.smartwater.project.third.concentrator.core.vo.ConcentratorDeviceVo;
import com.dotop.smartwater.dependence.core.common.BaseService;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.pagination.Pagination;

import java.util.List;

/**
 * 集中器设备数据获取层接口
 *
 *
 */
public interface IConcentratorDeviceService extends BaseService<ConcentratorDeviceBo, ConcentratorDeviceVo> {

    /**
     * 根据code查询集中器设备
     *
     * @param concentratorDeviceBo
     * @return
     * @throws FrameworkRuntimeException
     */
    @Override
    ConcentratorDeviceVo getByCode(ConcentratorDeviceBo concentratorDeviceBo) throws FrameworkRuntimeException;

    /**
     * 查询集中器设备是否存在
     *
     * @param concentratorDeviceBo
     * @return
     * @throws FrameworkRuntimeException
     */
    @Override
    boolean isExist(ConcentratorDeviceBo concentratorDeviceBo) throws FrameworkRuntimeException;

    /**
     * 分页查询集中器设备数据
     *
     * @param concentratorDeviceBo
     * @return
     * @throws FrameworkRuntimeException
     */
    @Override
    Pagination<ConcentratorDeviceVo> page(ConcentratorDeviceBo concentratorDeviceBo) throws FrameworkRuntimeException;

    /**
     * 查询集中器设备数据列表
     * @param concentratorDeviceBo
     * @return
     * @throws FrameworkRuntimeException
     */
    @Override
    List<ConcentratorDeviceVo> list(ConcentratorDeviceBo concentratorDeviceBo) throws FrameworkRuntimeException;

    /**
     * 新增一条集中器设备数据
     *
     * @param concentratorDeviceBo
     * @return
     * @throws FrameworkRuntimeException
     */
    @Override
    ConcentratorDeviceVo add(ConcentratorDeviceBo concentratorDeviceBo) throws FrameworkRuntimeException;

    /**
     * 更新一条集中器设备数据
     *
     * @param concentratorDeviceBo
     * @throws FrameworkRuntimeException
     */
    @Override
    ConcentratorDeviceVo edit(ConcentratorDeviceBo concentratorDeviceBo) throws FrameworkRuntimeException;

    /**
     * 获取一条集中器设备数据
     *
     * @param concentratorDeviceBo
     * @return
     * @throws FrameworkRuntimeException
     */
    @Override
    ConcentratorDeviceVo get(ConcentratorDeviceBo concentratorDeviceBo) throws FrameworkRuntimeException;

    /**
     * 删除一条集中器设备数据
     *
     * @param concentratorDeviceBo
     * @return
     * @throws FrameworkRuntimeException
     */
    void delete(ConcentratorDeviceBo concentratorDeviceBo) throws FrameworkRuntimeException;

    /**
     * 根据企业id，集中器id，设备编号计算设备数量
     * @param enterpriseid
     * @param id
     * @param devno
     * @return
     * @throws FrameworkRuntimeException
     */
    Integer countConcentratorDevice(String enterpriseid, String id, String devno) throws FrameworkRuntimeException;

    /**
     * 根据企业id，集中器id，集中器编号统计一个集中器下挂了多少个采集器通道
     * @param enterpriseid
     * @param id
     * @param code
     * @return
     * @throws FrameworkRuntimeException
     */
    Integer countCollectorChannel(String enterpriseid, String id, String code) throws FrameworkRuntimeException;

    /**
     * 根据企业id， 采集器id，水表号统计一个采集器下挂了多少个水表
     * @param enterpriseid
     * @param id
     * @param code
     * @return
     * @throws FrameworkRuntimeException
     */
    Integer countCollectorDevice(String enterpriseid, String id, String code) throws FrameworkRuntimeException;

    /**
     * 查询集中器设备列表，并排序
     * @param concentratorDeviceBo
     * @return
     * @throws FrameworkRuntimeException
     */
    List<ConcentratorDeviceVo> getConcentratorDeviceSort(ConcentratorDeviceBo concentratorDeviceBo) throws FrameworkRuntimeException;

    /**
     * 更新序号
     * @param concentratorDeviceBo
     * @throws FrameworkRuntimeException
     */
    void updateNo(List<ConcentratorDeviceBo> concentratorDeviceBo) throws FrameworkRuntimeException;

    /**
     * 修改阀门状态
     * @param concentratorDeviceBo
     * @return
     * @throws FrameworkRuntimeException
     */
    Integer setTapstatus(ConcentratorDeviceBo concentratorDeviceBo) throws FrameworkRuntimeException;

    /**
     * 批量添加水表
     * @param concentratorDeviceBos
     * @throws FrameworkRuntimeException
     */
    @Override
    void adds(List<ConcentratorDeviceBo> concentratorDeviceBos) throws FrameworkRuntimeException;
}
