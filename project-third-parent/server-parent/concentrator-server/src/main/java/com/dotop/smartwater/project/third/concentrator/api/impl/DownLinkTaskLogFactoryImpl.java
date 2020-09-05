package com.dotop.smartwater.project.third.concentrator.api.impl;

import com.dotop.smartwater.project.third.concentrator.core.bo.ConcentratorBo;
import com.dotop.smartwater.project.third.concentrator.core.bo.DownLinkTaskLogBo;
import com.dotop.smartwater.project.third.concentrator.core.vo.DownLinkTaskLogVo;
import com.dotop.smartwater.project.third.concentrator.service.IDownLinkTaskLogService;
import com.dotop.smartwater.dependence.core.exception.BaseExceptionConstants;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.log.LogMsg;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.dependence.core.utils.BeanUtils;
import com.dotop.smartwater.dependence.core.utils.DateUtils;
import com.dotop.smartwater.project.module.core.auth.form.AreaForm;
import com.dotop.smartwater.project.module.core.auth.vo.AreaVo;
import com.dotop.smartwater.project.third.concentrator.api.IConcentratorFactory;
import com.dotop.smartwater.project.third.concentrator.api.IDownLinkTaskLogFactory;
import com.dotop.smartwater.project.third.concentrator.core.form.DownLinkTaskLogForm;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.dotop.smartwater.project.module.core.auth.local.AuthCasClient.getEnterpriseid;

/**
 *
 * @date 2019-06-25
 */
@Component
public class DownLinkTaskLogFactoryImpl implements IDownLinkTaskLogFactory {

    private final static Logger LOGGER = LogManager.getLogger(DownLinkTaskLogFactoryImpl.class);

    @Autowired
    IDownLinkTaskLogService iDownLinkTaskLogService;
    @Autowired
    IConcentratorFactory iConcentratorFactory;

    @Override
    public DownLinkTaskLogVo get(DownLinkTaskLogForm downLinkTaskLogForm) throws FrameworkRuntimeException {
        return null;
    }

    /**
     * 获取下发任务列表并分页
     * @param downLinkTaskLogForm
     * @return
     * @throws FrameworkRuntimeException
     */
    @Override
    public Pagination<DownLinkTaskLogVo> page(DownLinkTaskLogForm downLinkTaskLogForm) throws FrameworkRuntimeException {
        try {
            DownLinkTaskLogBo downLinkTaskLogBo = BeanUtils.copy(downLinkTaskLogForm, DownLinkTaskLogBo.class);
            downLinkTaskLogBo.setEnterpriseid(getEnterpriseid());
            return iDownLinkTaskLogService.page(downLinkTaskLogBo);
        } catch (DataAccessException e) {
            LOGGER.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    @Override
    public XSSFWorkbook exportConcentratorRecord(DownLinkTaskLogForm downLinkTaskLogForm) throws FrameworkRuntimeException {
        downLinkTaskLogForm.setPage(1);
        downLinkTaskLogForm.setPageCount(10000);
        List<DownLinkTaskLogVo> list = page(downLinkTaskLogForm).getData();
        AreaForm areaForm = new AreaForm();
        areaForm.setEnterpriseid(getEnterpriseid());
        List<AreaVo> areaVos = iConcentratorFactory.getAreaList(areaForm);
        Map<String, String> areaMap = areaVos.stream().collect(Collectors.toMap(AreaVo::getId, AreaVo::getName));
        if (list.isEmpty()) {
            throw new FrameworkRuntimeException(BaseExceptionConstants.UNDEFINED_ERROR, "无数据");
        }
        XSSFWorkbook workBook = new XSSFWorkbook();
        XSSFSheet sheet = workBook.createSheet("集中器：" + (list.get(0).getConcentrator()==null?"":list.get(0).getConcentrator().getCode()));
        // 构建表头
        XSSFRow headRow = sheet.createRow(0);
        sheet.setDefaultColumnWidth(18);
        XSSFCell cell = null;
        String[] titles = {"序号", "集中器编号", "所属小区", "安装位置", "上报时间", "水表数量", "成功抄表数","失败抄表数"};
        for (int i = 0; i < titles.length; i++) {
            cell = headRow.createCell(i);
            cell.setCellValue(titles[i]);
        }
        // 构建表体数据
        for (int j = 0; j < list.size(); j++) {
            XSSFRow bodyRow = sheet.createRow(j + 1);
            DownLinkTaskLogVo downLinkTaskLogVo = list.get(j);
            if(downLinkTaskLogVo.getConcentrator() == null){
                downLinkTaskLogVo.setConcentrator(new ConcentratorBo());
            }
            cell = bodyRow.createCell(0);
            cell.setCellValue(j);
            cell = bodyRow.createCell(1);
            cell.setCellValue(downLinkTaskLogVo.getConcentrator().getCode());
            cell = bodyRow.createCell(2);
            String area = "";
            if(downLinkTaskLogVo.getConcentrator().getAreaIds() != null) {
                for (String str : downLinkTaskLogVo.getConcentrator().getAreaIds()) {
                    area += areaMap.get(str) + ",";
                }
            }
            cell.setCellValue(area);
            cell = bodyRow.createCell(3);
            cell.setCellValue(downLinkTaskLogVo.getConcentrator().getInstallAddress());
            cell = bodyRow.createCell(4);
            cell.setCellValue(DateUtils.formatDatetime(downLinkTaskLogVo.getDeliveryDate()));
            cell = bodyRow.createCell(5);
            if(!StringUtils.isBlank(downLinkTaskLogVo.getSuccessNum()) && !StringUtils.isBlank(downLinkTaskLogVo.getFailNum())){
                cell.setCellValue(Integer.parseInt(downLinkTaskLogVo.getSuccessNum()) + Integer.parseInt(downLinkTaskLogVo.getFailNum()));
            }else{
                cell.setCellValue("");
            }
            cell = bodyRow.createCell(6);
            cell.setCellValue(downLinkTaskLogVo.getSuccessNum());
            cell = bodyRow.createCell(7);
            cell.setCellValue(downLinkTaskLogVo.getFailNum());
        }
        return workBook;
    }
}
