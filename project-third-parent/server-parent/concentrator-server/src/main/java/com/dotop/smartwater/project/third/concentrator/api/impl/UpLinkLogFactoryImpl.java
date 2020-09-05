package com.dotop.smartwater.project.third.concentrator.api.impl;

import com.dotop.smartwater.project.third.concentrator.core.bo.CollectorBo;
import com.dotop.smartwater.project.third.concentrator.core.bo.ConcentratorBo;
import com.dotop.smartwater.project.third.concentrator.core.bo.ConcentratorDeviceBo;
import com.dotop.smartwater.project.third.concentrator.core.bo.UpLinkLogBo;
import com.dotop.smartwater.project.third.concentrator.core.vo.ConcentratorDeviceVo;
import com.dotop.smartwater.project.third.concentrator.core.vo.UpLinkLogVo;
import com.dotop.smartwater.project.third.concentrator.service.IUpLinkLogService;
import com.dotop.smartwater.dependence.core.exception.BaseExceptionConstants;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.log.LogMsg;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.dependence.core.utils.BeanUtils;
import com.dotop.smartwater.dependence.core.utils.DateUtils;
import com.dotop.smartwater.project.module.core.auth.local.IAuthCasClient;
import com.dotop.smartwater.project.third.concentrator.api.IConcentratorDeviceFactory;
import com.dotop.smartwater.project.third.concentrator.api.IUpLinkLogFactory;
import com.dotop.smartwater.project.third.concentrator.core.constants.ConcentratorConstants;
import com.dotop.smartwater.project.third.concentrator.core.form.CollectorForm;
import com.dotop.smartwater.project.third.concentrator.core.form.ConcentratorDeviceForm;
import com.dotop.smartwater.project.third.concentrator.core.form.ConcentratorForm;
import com.dotop.smartwater.project.third.concentrator.core.form.UpLinkLogForm;
import com.dotop.smartwater.project.third.concentrator.core.utils.ConcentratorUtils;
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

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 上行日志查询入口
 *
 *
 */
@Component
public class UpLinkLogFactoryImpl implements IUpLinkLogFactory, IAuthCasClient {

    private final static Logger LOGGER = LogManager.getLogger(UpLinkLogFactoryImpl.class);

    @Autowired
    IUpLinkLogService iUpLinkLogService;
    @Autowired
    IConcentratorDeviceFactory iConcentratorDeviceFactory;

    /**
     * 导出设备上行记录详情
     *
     * @param upLinkLogForm
     * @return
     * @throws FrameworkRuntimeException
     */
    @Override
    public Pagination<UpLinkLogVo> page(UpLinkLogForm upLinkLogForm) throws FrameworkRuntimeException {
        try {
            UpLinkLogBo upLinkLogBo = BeanUtils.copy(upLinkLogForm, UpLinkLogBo.class);
            upLinkLogBo.setEnterpriseid(getEnterpriseid());
            return iUpLinkLogService.page(upLinkLogBo);
        } catch (DataAccessException e) {
            LOGGER.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    /**
     * 导出设备上行记录详情
     *
     * @param upLinkLogForm
     * @throws FrameworkRuntimeException
     */
    @Override
    public XSSFWorkbook export(UpLinkLogForm upLinkLogForm) throws FrameworkRuntimeException {
        upLinkLogForm.setEnterpriseid(getEnterpriseid());
        UpLinkLogBo upLinkLogBo = BeanUtils.copy(upLinkLogForm, UpLinkLogBo.class);
        List<UpLinkLogVo> list = iUpLinkLogService.list(upLinkLogBo);
        if (list.isEmpty()) {
            throw new FrameworkRuntimeException(BaseExceptionConstants.UNDEFINED_ERROR, "该集中器下无设备上行");
        }
//        try {
        XSSFWorkbook workBook = new XSSFWorkbook();
        XSSFSheet sheet = workBook.createSheet("集中器：" + list.get(0).getConcentrator().getCode());
        // 构建表头
        XSSFRow headRow = sheet.createRow(0);
        sheet.setDefaultColumnWidth(20);
        XSSFCell cell = null;
        String[] titles = {"集中器编号", "采集器通道号", "采集器编号", "水表通道号", "水表编号", "抄表读数", "抄表时间", "抄表结果"};
        for (int i = 0; i < titles.length; i++) {
            cell = headRow.createCell(i);
            cell.setCellValue(titles[i]);
        }
        // 构建表体数据
        for (int j = 0; j < list.size(); j++) {
            XSSFRow bodyRow = sheet.createRow(j + 1);
            UpLinkLogVo upLinkLogVo = list.get(j);
            cell = bodyRow.createCell(0);
            if (upLinkLogVo.getConcentrator() == null) {
                upLinkLogVo.setConcentrator(new ConcentratorBo());
            }
            if (upLinkLogVo.getCollector() == null) {
                upLinkLogVo.setCollector(new CollectorBo());
            }
            if (upLinkLogVo.getConcentratorDevice() == null) {
                upLinkLogVo.setConcentratorDevice(new ConcentratorDeviceBo());
            }

            cell.setCellValue(upLinkLogVo.getConcentrator().getCode());
            cell = bodyRow.createCell(1);
            cell.setCellValue(upLinkLogVo.getCollector().getChannel());
            cell = bodyRow.createCell(2);
            String collectorCode = ConcentratorUtils.getCollectorCode(upLinkLogVo.getCollector().getCode(), upLinkLogVo.getConcentratorDevice().getChannel());
            cell.setCellValue(collectorCode);
            cell = bodyRow.createCell(3);
            cell.setCellValue(upLinkLogVo.getConcentratorDevice().getChannel());
            cell = bodyRow.createCell(4);
            cell.setCellValue(upLinkLogVo.getConcentratorDevice().getDevno());
            cell = bodyRow.createCell(5);
            cell.setCellValue(upLinkLogVo.getWater());
            cell = bodyRow.createCell(6);
            cell.setCellValue(DateUtils.format(upLinkLogVo.getReceiveDate(), DateUtils.DATETIME));
            cell = bodyRow.createCell(7);
            String result = "";
            if (ConcentratorConstants.RESULT_SUCCESS.equals(upLinkLogVo.getResult())) {
                result = "成功";
            } else if (ConcentratorConstants.RESULT_FAIL.equals(upLinkLogVo.getResult())) {
                result = "失败";
            } else if (ConcentratorConstants.RESULT_WAIT.equals(upLinkLogVo.getResult())) {
                result = "等待";
            }
            cell.setCellValue(result);
        }
        return workBook;
//            workBook.write(outputStream);
//            outputStream.flush();
//            outputStream.close();
//        } catch (IOException e) {
//            LOGGER.error(LogMsg.to(e));
//            throw new FrameworkRuntimeException(BaseExceptionConstants.UNDEFINED_ERROR, e.getMessage(), e);
//        } finally {
//            try {
//                outputStream.close();
//            } catch (IOException e) {
//                LOGGER.error(LogMsg.to(e));
//            }
//        }
    }

    @Override
    public List<UpLinkLogVo> getUplinkLogDateList(UpLinkLogForm upLinkLogForm) throws FrameworkRuntimeException {
        try {
            UpLinkLogBo upLinkLogBo = BeanUtils.copy(upLinkLogForm, UpLinkLogBo.class);
            return iUpLinkLogService.getUplinkLogDateList(upLinkLogBo);
        } catch (DataAccessException e) {
            LOGGER.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    @Override
    public List<UpLinkLogVo> getUplinkLogMonthList(UpLinkLogForm upLinkLogForm) throws FrameworkRuntimeException {
        try {
            UpLinkLogBo upLinkLogBo = BeanUtils.copy(upLinkLogForm, UpLinkLogBo.class);
            return iUpLinkLogService.getUplinkLogMonthList(upLinkLogBo);
        } catch (DataAccessException e) {
            LOGGER.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    @Override
    public Pagination<UpLinkLogVo> upLinkJump(UpLinkLogForm upLinkLogForm) throws FrameworkRuntimeException {
        try {
            upLinkLogForm.setEnterpriseid(getEnterpriseid());
            ConcentratorDeviceForm concentratorDeviceForm = new ConcentratorDeviceForm();
            concentratorDeviceForm.setDevno(upLinkLogForm.getConcentratorDevice().getDevno());
            concentratorDeviceForm.setCollector(BeanUtils.copy(upLinkLogForm.getCollector(), CollectorForm.class));
            concentratorDeviceForm.setConcentrator(BeanUtils.copy(upLinkLogForm.getConcentrator(), ConcentratorForm.class));
            List<ConcentratorDeviceVo> list = iConcentratorDeviceFactory.list(concentratorDeviceForm);
            Pagination<UpLinkLogVo> pagination = new Pagination<>();
            pagination.setPageNo(upLinkLogForm.getPage());
            pagination.setPageSize(upLinkLogForm.getPageCount());
            List<UpLinkLogVo> upLinkLogVos = new ArrayList<>();
            int count = 0;
            for (ConcentratorDeviceVo concentratorDeviceVo : list) {
                ConcentratorDeviceBo concentratorDeviceBo = BeanUtils.copy(concentratorDeviceVo, ConcentratorDeviceBo.class);
                upLinkLogForm.setConcentratorDevice(concentratorDeviceBo);
                List<UpLinkLogVo> uplinkLogDateList = getUplinkLogDateList(upLinkLogForm);
                for (int i = 0; i < uplinkLogDateList.size(); i++) {
                    if (uplinkLogDateList.size() > i + 1 && StringUtils.isNotBlank(uplinkLogDateList.get(i).getWater()) && StringUtils.isNotBlank(uplinkLogDateList.get(i + 1).getWater())) {
                        Integer day1 = Integer.parseInt(DateUtils.format(uplinkLogDateList.get(i).getReceiveDate(), "dd"));
                        Integer day2 = Integer.parseInt(DateUtils.format(uplinkLogDateList.get(i + 1).getReceiveDate(), "dd"));
                        if (day2 - day1 == 1) {
                            double cut = Double.parseDouble(uplinkLogDateList.get(i + 1).getWater()) - Double.parseDouble(uplinkLogDateList.get(i).getWater());
                            BigDecimal b = BigDecimal.valueOf(cut);
                            double water = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                            uplinkLogDateList.get(i).setWater(water + "");
                            uplinkLogDateList.get(i).setConcentrator(BeanUtils.copy(concentratorDeviceVo.getConcentrator(), ConcentratorBo.class));
                            uplinkLogDateList.get(i).setCollector(BeanUtils.copy(concentratorDeviceVo.getCollector(), CollectorBo.class));
                            uplinkLogDateList.get(i).setConcentratorDevice(BeanUtils.copy(concentratorDeviceVo, ConcentratorDeviceBo.class));
                            if ((upLinkLogForm.getStatus() == null || ConcentratorConstants.DEVICE_ABNORMAIL.equals(upLinkLogForm.getStatus())) && (StringUtils.isBlank(upLinkLogForm.getWater()) || water >= Double.parseDouble(upLinkLogForm.getWater()))) {
                                count++;
                                if (count <= upLinkLogForm.getPage() * upLinkLogForm.getPageCount() && count > (upLinkLogForm.getPage() - 1) * upLinkLogForm.getPageCount()) {
                                    String desc = day1 + "号到" + day2 + "号的用水量异常，跳变值为" + water;
                                    uplinkLogDateList.get(i).setDesc(desc);
                                    uplinkLogDateList.get(i).setStatus("异常");
                                    upLinkLogVos.add(uplinkLogDateList.get(i));
                                }
                            }
                            if ((upLinkLogForm.getStatus() == null || ConcentratorConstants.DEVICE_NORMAIL.equals(upLinkLogForm.getStatus())) && (StringUtils.isBlank(upLinkLogForm.getWater()) || water < Double.parseDouble(upLinkLogForm.getWater()))) {
                                count++;
                                if (count <= upLinkLogForm.getPage() * upLinkLogForm.getPageCount() && count > (upLinkLogForm.getPage() - 1) * upLinkLogForm.getPageCount()) {
                                    String desc = day1 + "号到" + day2 + "号的用水量正常，跳变值为" + water;
                                    uplinkLogDateList.get(i).setDesc(desc);
                                    uplinkLogDateList.get(i).setStatus("正常");
                                    upLinkLogVos.add(uplinkLogDateList.get(i));
                                }
                            }
                        }
                    }
                }
            }
            pagination.setTotalPageSize(count);
            pagination.setData(upLinkLogVos);
            return pagination;
        } catch (DataAccessException e) {
            LOGGER.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    @Override
    public XSSFWorkbook exportUpLinkJump(UpLinkLogForm upLinkLogForm) throws FrameworkRuntimeException {
        upLinkLogForm.setPage(1);
        upLinkLogForm.setPageCount(10000);
        List<UpLinkLogVo> list = upLinkJump(upLinkLogForm).getData();
        if (list.isEmpty()) {
            throw new FrameworkRuntimeException(BaseExceptionConstants.UNDEFINED_ERROR, "无数据");
        }
        XSSFWorkbook workBook = new XSSFWorkbook();
        XSSFSheet sheet = workBook.createSheet("水表跳变变化对比");
        // 构建表头
        XSSFRow headRow = sheet.createRow(0);
        sheet.setDefaultColumnWidth(18);
        XSSFCell cell = null;
        String[] titles = {"序号", "水表编号", "集中器编号", "采集器编号", "地址", "状态", "结果"};
        for (int i = 0; i < titles.length; i++) {
            cell = headRow.createCell(i);
            cell.setCellValue(titles[i]);
        }
        // 构建表体数据
        for (int j = 0; j < list.size(); j++) {
            XSSFRow bodyRow = sheet.createRow(j + 1);
            UpLinkLogVo upLinkLogVo = list.get(j);
            if(upLinkLogVo.getConcentratorDevice() == null){
                upLinkLogVo.setConcentratorDevice(new ConcentratorDeviceBo());
            }
            if(upLinkLogVo.getCollector() == null){
                upLinkLogVo.setCollector(new CollectorBo());
            }
            if(upLinkLogVo.getConcentrator() == null){
                upLinkLogVo.setConcentrator(new ConcentratorBo());
            }
            cell = bodyRow.createCell(0);
            cell.setCellValue(j + 1);
            cell = bodyRow.createCell(1);
            cell.setCellValue(upLinkLogVo.getConcentratorDevice().getDevno());
            cell = bodyRow.createCell(2);
            cell.setCellValue(upLinkLogVo.getConcentrator().getCode());
            cell = bodyRow.createCell(3);
            String collectorCode = ConcentratorUtils.getCollectorCode(upLinkLogVo.getCollector().getCode(), upLinkLogVo.getConcentratorDevice().getChannel());
            cell.setCellValue(collectorCode);
            cell = bodyRow.createCell(4);
            cell.setCellValue(upLinkLogVo.getConcentratorDevice().getDevaddr());
            cell = bodyRow.createCell(5);
            cell.setCellValue(upLinkLogVo.getStatus());
            cell = bodyRow.createCell(6);
            cell.setCellValue(upLinkLogVo.getDesc());
        }
        return workBook;
    }

    @Override
    public XSSFWorkbook exportRecord(UpLinkLogForm upLinkLogForm) throws FrameworkRuntimeException {
        upLinkLogForm.setPage(1);
        upLinkLogForm.setPageCount(10000);
        List<UpLinkLogVo> list = page(upLinkLogForm).getData();
        if (list.isEmpty()) {
            throw new FrameworkRuntimeException(BaseExceptionConstants.UNDEFINED_ERROR, "无数据");
        }
        XSSFWorkbook workBook = new XSSFWorkbook();
        XSSFSheet sheet = workBook.createSheet("上报记录");
        // 构建表头
        XSSFRow headRow = sheet.createRow(0);
        sheet.setDefaultColumnWidth(18);
        XSSFCell cell = null;
        String[] titles = {"水表编号", "是否带阀", "业主编号", "阀门状态", "读数", "抄表时间", "抄表状态"};
        for (int i = 0; i < titles.length; i++) {
            cell = headRow.createCell(i);
            cell.setCellValue(titles[i]);
        }
        Map<String, String> map = new HashMap<>();
        map.put(ConcentratorConstants.RESULT_SUCCESS, "成功");
        map.put(ConcentratorConstants.RESULT_FAIL, "失败");
        map.put(ConcentratorConstants.RESULT_WAIT, "等待");
        // 构建表体数据
        for (int j = 0; j < list.size(); j++) {
            XSSFRow bodyRow = sheet.createRow(j + 1);
            UpLinkLogVo upLinkLogVo = list.get(j);
            if(upLinkLogVo.getConcentratorDevice() == null){
                upLinkLogVo.setConcentratorDevice(new ConcentratorDeviceBo());
            }
            if(upLinkLogVo.getCollector() == null){
                upLinkLogVo.setCollector(new CollectorBo());
            }
            if(upLinkLogVo.getConcentrator() == null){
                upLinkLogVo.setConcentrator(new ConcentratorBo());
            }
            cell = bodyRow.createCell(0);
            cell.setCellValue(upLinkLogVo.getConcentratorDevice().getDevno());
            cell = bodyRow.createCell(1);
            cell.setCellValue(upLinkLogVo.getConcentratorDevice().getTaptype() == null?"": (upLinkLogVo.getConcentratorDevice().getTaptype() == 1 ? "是" : "否"));
            cell = bodyRow.createCell(2);
            cell.setCellValue(upLinkLogVo.getConcentratorDevice().getUserno());
            cell = bodyRow.createCell(3);
            cell.setCellValue(upLinkLogVo.getConcentratorDevice().getTapstatus() == null?"-": (upLinkLogVo.getConcentratorDevice().getTapstatus() == 1 ? "开阀" : "关阀"));
            cell = bodyRow.createCell(4);
            cell.setCellValue(upLinkLogVo.getWater());
            cell = bodyRow.createCell(5);
            cell.setCellValue(DateUtils.formatDatetime(upLinkLogVo.getReceiveDate()));
            cell = bodyRow.createCell(6);
            cell.setCellValue(map.get(upLinkLogVo.getResult()));
        }
        return workBook;
    }
}
