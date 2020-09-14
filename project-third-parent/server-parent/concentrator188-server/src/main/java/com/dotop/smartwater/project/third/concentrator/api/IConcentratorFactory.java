package com.dotop.smartwater.project.third.concentrator.api;

import com.dotop.smartwater.project.third.concentrator.core.vo.ConcentratorVo;
import com.dotop.smartwater.dependence.core.common.BaseFactory;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.project.module.core.auth.form.AreaForm;
import com.dotop.smartwater.project.module.core.auth.vo.AreaVo;
import com.dotop.smartwater.project.third.concentrator.core.form.ConcentratorForm;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 集中器/中继器业务逻辑接口
 *
 *
 */
public interface IConcentratorFactory extends BaseFactory<ConcentratorForm, ConcentratorVo> {

    @Override
    ConcentratorVo add(ConcentratorForm concentratorForm) throws FrameworkRuntimeException;

    @Override
    ConcentratorVo get(ConcentratorForm concentratorForm) throws FrameworkRuntimeException;

    @Override
    Pagination<ConcentratorVo> page(ConcentratorForm concentratorForm) throws FrameworkRuntimeException;

    @Override
    List<ConcentratorVo> list(ConcentratorForm concentratorForm) throws FrameworkRuntimeException;

    @Override
    ConcentratorVo edit(ConcentratorForm concentratorForm) throws FrameworkRuntimeException;

    ConcentratorVo editStatus(ConcentratorForm concentratorForm) throws FrameworkRuntimeException;

    @Override
    String del(ConcentratorForm concentratorForm) throws FrameworkRuntimeException;

    Pagination<ConcentratorVo> pageArchive(ConcentratorForm concentratorForm) throws FrameworkRuntimeException;

    void editReordering(ConcentratorForm concentratorForm) throws FrameworkRuntimeException;

    void needReordering(String enterpriseid, String concentratorId) throws FrameworkRuntimeException;

    ConcentratorVo getByCode(ConcentratorForm concentratorForm) throws FrameworkRuntimeException;

    boolean isExist(ConcentratorForm concentratorForm) throws FrameworkRuntimeException;

    XSSFWorkbook export(ConcentratorForm concentratorForm) throws FrameworkRuntimeException;

    /**
     * 导入设备档案
     *
     * @param multipartFile
     * @return
     */
    String analyseFile(MultipartFile multipartFile);

    /**
     * 获取区域列表
     *
     * @param areaForm
     * @return
     * @throws FrameworkRuntimeException
     */
    List<AreaVo> getAreaList(AreaForm areaForm) throws FrameworkRuntimeException;

    /**
     * 集中器在线状态列表
     */
    Pagination<ConcentratorVo> onlinePage(ConcentratorForm concentratorForm) throws FrameworkRuntimeException;

    XSSFWorkbook exportOnline(ConcentratorForm concentratorForm) throws FrameworkRuntimeException;
}
