package com.dotop.smartwater.project.third.concentrator.api;

import com.dotop.smartwater.project.third.concentrator.core.vo.ConcentratorDeviceVo;
import com.dotop.smartwater.dependence.core.common.BaseFactory;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.project.third.concentrator.core.form.ConcentratorDeviceForm;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.util.List;

/**
 * 集中器设备业务逻辑接口
 *
 *
 */
public interface IConcentratorDeviceFactory extends BaseFactory<ConcentratorDeviceForm, ConcentratorDeviceVo> {

    /**
     * 分页查询集中器设备相关数据
     *
     * @param concentratorDeviceForm
     * @return
     * @throws FrameworkRuntimeException
     */
    @Override
    Pagination<ConcentratorDeviceVo> page(ConcentratorDeviceForm concentratorDeviceForm) throws FrameworkRuntimeException;

    /**
     * 添加一条集中器设备数据
     *
     * @param concentratorDeviceForm
     * @throws FrameworkRuntimeException
     */
    @Override
    ConcentratorDeviceVo add(ConcentratorDeviceForm concentratorDeviceForm) throws FrameworkRuntimeException;

    /**
     * 更新一条集中器设备数据
     *
     * @param concentratorDeviceForm
     * @throws FrameworkRuntimeException
     */
    @Override
    ConcentratorDeviceVo edit(ConcentratorDeviceForm concentratorDeviceForm) throws FrameworkRuntimeException;

    /**
     * 获取一条集中器设备数据
     *
     * @param concentratorDeviceForm
     * @return
     * @throws FrameworkRuntimeException
     */
    @Override
    ConcentratorDeviceVo get(ConcentratorDeviceForm concentratorDeviceForm) throws FrameworkRuntimeException;

    /**
     * 删除一条集中器设备数据（调用水务平台原有的）
     *
     * @param concentratorDeviceForm
     * @throws FrameworkRuntimeException
     */
    @Override
    String del(ConcentratorDeviceForm concentratorDeviceForm) throws FrameworkRuntimeException;

    /**
     * 删除一条集中器设备数据（只删除集中器中的水表表）
     *
     * @param concentratorDeviceForm
     * @return
     */
    String delete(ConcentratorDeviceForm concentratorDeviceForm);

    /**
     * 查询集中器设备列表
     *
     * @param concentratorDeviceForm
     * @return
     * @throws FrameworkRuntimeException
     */
    @Override
    List<ConcentratorDeviceVo> list(ConcentratorDeviceForm concentratorDeviceForm) throws FrameworkRuntimeException;

    /**
     * 查询设备档案并分页
     *
     * @param concentratorDeviceForm
     * @return
     * @throws FrameworkRuntimeException
     */
    Pagination<ConcentratorDeviceVo> pageArchive(ConcentratorDeviceForm concentratorDeviceForm) throws FrameworkRuntimeException;


    /**
     * 导出集中器设备
     *
     * @param concentratorDeviceForm
     * @throws FrameworkRuntimeException
     */
    XSSFWorkbook export(ConcentratorDeviceForm concentratorDeviceForm) throws FrameworkRuntimeException;

    /**
     * 根据集中器id重排序
     *
     * @param concentratorDeviceForm
     * @return
     * @throws FrameworkRuntimeException
     */
    List<ConcentratorDeviceVo> reordering(ConcentratorDeviceForm concentratorDeviceForm) throws FrameworkRuntimeException;

    /**
     * 修改阀门状态
     *
     * @param concentratorDeviceForm
     * @return
     * @throws FrameworkRuntimeException
     */
    String setTapstatus(ConcentratorDeviceForm concentratorDeviceForm) throws FrameworkRuntimeException;

    /**
     * 查询设备日用水量
     *
     * @param concentratorDeviceForm
     * @return
     * @throws FrameworkRuntimeException
     */
    Pagination<ConcentratorDeviceVo> pageUpLinkLogDate(ConcentratorDeviceForm concentratorDeviceForm) throws FrameworkRuntimeException;

    /**
     * 查询设备月用水量
     *
     * @param concentratorDeviceForm
     * @return
     * @throws FrameworkRuntimeException
     */
    Pagination<ConcentratorDeviceVo> pageUpLinkLogMonth(ConcentratorDeviceForm concentratorDeviceForm) throws FrameworkRuntimeException;

    XSSFWorkbook exportUpLinkLogDate(ConcentratorDeviceForm concentratorDeviceForm) throws FrameworkRuntimeException;

    XSSFWorkbook exportUpLinkLogMonth(ConcentratorDeviceForm concentratorDeviceForm) throws FrameworkRuntimeException;
}
