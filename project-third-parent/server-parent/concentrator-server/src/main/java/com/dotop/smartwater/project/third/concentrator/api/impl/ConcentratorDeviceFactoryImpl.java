package com.dotop.smartwater.project.third.concentrator.api.impl;

import com.dotop.smartwater.project.third.concentrator.core.bo.ConcentratorBo;
import com.dotop.smartwater.project.third.concentrator.core.bo.ConcentratorDeviceBo;
import com.dotop.smartwater.project.third.concentrator.core.vo.ConcentratorDeviceVo;
import com.dotop.smartwater.project.third.concentrator.core.vo.ConcentratorVo;
import com.dotop.smartwater.project.third.concentrator.core.vo.UpLinkLogVo;
import com.dotop.smartwater.project.third.concentrator.service.IConcentratorDeviceService;
import com.dotop.smartwater.project.third.concentrator.service.IConcentratorService;
import com.dotop.smartwater.dependence.core.common.RootModel;
import com.dotop.smartwater.dependence.core.exception.BaseExceptionConstants;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.log.LogMsg;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.dependence.core.utils.BeanUtils;
import com.dotop.smartwater.dependence.core.utils.DateUtils;
import com.dotop.smartwater.dependence.core.utils.UuidUtils;
import com.dotop.smartwater.project.module.api.device.IDeviceFactory;
import com.dotop.smartwater.project.module.api.revenue.IInformationFactory;
import com.dotop.smartwater.project.module.core.auth.local.AuthCasClient;
import com.dotop.smartwater.project.module.core.water.constants.ResultCode;
import com.dotop.smartwater.project.module.core.water.form.DeviceForm;
import com.dotop.smartwater.project.module.core.water.vo.DeviceVo;
import com.dotop.smartwater.project.third.concentrator.api.IConcentratorDeviceFactory;
import com.dotop.smartwater.project.third.concentrator.api.IConcentratorFactory;
import com.dotop.smartwater.project.third.concentrator.api.IUpLinkLogFactory;
import com.dotop.smartwater.project.third.concentrator.core.constants.ConcentratorConstants;
import com.dotop.smartwater.project.third.concentrator.core.form.ConcentratorDeviceForm;
import com.dotop.smartwater.project.third.concentrator.core.form.UpLinkLogForm;
import com.dotop.smartwater.project.third.concentrator.core.utils.ConcentratorUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 集中器设备业务逻辑接口
 *
 *
 */
@Component("IConcentratorDeviceFactory")
public class ConcentratorDeviceFactoryImpl extends AuthCasClient implements IConcentratorDeviceFactory {

    private static final Logger LOGGER = LogManager.getLogger(ConcentratorDeviceFactoryImpl.class);

    @Autowired
    private IConcentratorDeviceService iConcentratorDeviceService;
    @Autowired
    private IDeviceFactory iDeviceFactory;
    @Autowired
    IInformationFactory iInformationFactory;
    @Autowired
    private IConcentratorFactory iConcentratorFactory;
    @Autowired
    private IConcentratorService iConcentratorService;
    @Autowired
    private IUpLinkLogFactory iUpLinkLogFactory;

    /**
     * 分页查询集中器设备数据
     *
     * @param concentratorDeviceForm
     * @return
     * @throws FrameworkRuntimeException
     */
    @Override
    public Pagination<ConcentratorDeviceVo> page(ConcentratorDeviceForm concentratorDeviceForm) throws FrameworkRuntimeException {
        try {
            ConcentratorDeviceBo concentratorDeviceBo = BeanUtils.copy(concentratorDeviceForm, ConcentratorDeviceBo.class);
            concentratorDeviceBo.setEnterpriseid(getEnterpriseid());
            return iConcentratorDeviceService.page(concentratorDeviceBo);
        } catch (DataAccessException e) {
            LOGGER.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    /**
     * 查询集中器设备数据不分页
     *
     * @param concentratorDeviceForm
     * @return
     * @throws FrameworkRuntimeException
     */
    @Override
    public List<ConcentratorDeviceVo> list(ConcentratorDeviceForm concentratorDeviceForm) throws FrameworkRuntimeException {
        try {
            ConcentratorDeviceBo concentratorDeviceBo = BeanUtils.copy(concentratorDeviceForm, ConcentratorDeviceBo.class);
            concentratorDeviceBo.setEnterpriseid(getEnterpriseid());
            return iConcentratorDeviceService.list(concentratorDeviceBo);
        } catch (DataAccessException e) {
            LOGGER.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    /**
     * 新增一条集中器设备数据
     *
     * @param concentratorDeviceForm
     * @throws FrameworkRuntimeException
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
    public ConcentratorDeviceVo add(ConcentratorDeviceForm concentratorDeviceForm) throws FrameworkRuntimeException {
        try {
            DeviceForm deviceForm = BeanUtils.copy(concentratorDeviceForm, DeviceForm.class);
            deviceForm.setEnterpriseid(getEnterpriseid());
            iDeviceFactory.addDeviceByWeb(deviceForm);
            DeviceVo d = iDeviceFactory.findByDevNo(deviceForm.getDevno());
            concentratorDeviceForm.setId(UuidUtils.getUuid());
            ConcentratorDeviceBo concentratorDeviceBo = BeanUtils.copy(concentratorDeviceForm, ConcentratorDeviceBo.class);
            concentratorDeviceBo.setEnterpriseid(getEnterpriseid());
            concentratorDeviceBo.setDevid(d.getDevid());
            concentratorDeviceBo.setUserBy(getAccount());
            concentratorDeviceBo.setCurr(getCurr());
            concentratorDeviceBo.setNo(0);
            iConcentratorDeviceService.add(concentratorDeviceBo);

            iConcentratorFactory.needReordering(getEnterpriseid(), concentratorDeviceForm.getConcentrator().getId());

            ConcentratorDeviceVo concentratorDeviceVo = BeanUtils.copy(concentratorDeviceForm, ConcentratorDeviceVo.class);
            return concentratorDeviceVo;
        } catch (DataAccessException e) {
            LOGGER.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    /**
     * 更新一条集中器设备数据
     *
     * @param concentratorDeviceForm
     * @throws FrameworkRuntimeException
     */
    @Override
    public ConcentratorDeviceVo edit(ConcentratorDeviceForm concentratorDeviceForm) throws FrameworkRuntimeException {
        try {
            DeviceForm deviceForm = BeanUtils.copy(concentratorDeviceForm, DeviceForm.class);
            deviceForm.setEnterpriseid(getEnterpriseid());
            iDeviceFactory.update(deviceForm);

            ConcentratorDeviceBo concentratorDeviceBo = BeanUtils.copy(concentratorDeviceForm, ConcentratorDeviceBo.class);
            concentratorDeviceBo.setEnterpriseid(getEnterpriseid());
            concentratorDeviceBo.setUserBy(getAccount());
            concentratorDeviceBo.setCurr(getCurr());
            iConcentratorDeviceService.edit(concentratorDeviceBo);

            iConcentratorFactory.needReordering(getEnterpriseid(), concentratorDeviceForm.getConcentrator().getId());

            ConcentratorDeviceVo concentratorDeviceVo = BeanUtils.copy(concentratorDeviceBo, ConcentratorDeviceVo.class);
            return concentratorDeviceVo;
        } catch (DataAccessException e) {
            LOGGER.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    /**
     * 获取一条集中器设备数据
     *
     * @param concentratorDeviceForm
     * @return
     * @throws FrameworkRuntimeException
     */
    @Override
    public ConcentratorDeviceVo get(ConcentratorDeviceForm concentratorDeviceForm) throws FrameworkRuntimeException {
        try {
            ConcentratorDeviceBo concentratorDeviceBo = BeanUtils.copy(concentratorDeviceForm, ConcentratorDeviceBo.class);
            if (StringUtils.isBlank(concentratorDeviceForm.getEnterpriseid())) {
                concentratorDeviceBo.setEnterpriseid(getEnterpriseid());
            }
            return iConcentratorDeviceService.get(concentratorDeviceBo);
        } catch (DataAccessException e) {
            LOGGER.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    /**
     * 获取一条集中器设备数据
     *
     * @param concentratorDeviceForm
     * @return
     * @throws FrameworkRuntimeException
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
    public String del(ConcentratorDeviceForm concentratorDeviceForm) throws FrameworkRuntimeException {
        try {
            if (StringUtils.isBlank(concentratorDeviceForm.getEnterpriseid())) {
                concentratorDeviceForm.setEnterpriseid(getEnterpriseid());
            }
            ConcentratorDeviceBo concentratorDeviceBo = BeanUtils.copy(concentratorDeviceForm, ConcentratorDeviceBo.class);
            ConcentratorDeviceVo concentratorDeviceVo = iConcentratorDeviceService.get(concentratorDeviceBo);
            if (concentratorDeviceVo == null) {
                throw new FrameworkRuntimeException(BaseExceptionConstants.UNDEFINED_ERROR, "该水表不存在");
            }
            DeviceForm deviceForm = BeanUtils.copy(concentratorDeviceVo, DeviceForm.class);
            iConcentratorFactory.needReordering(concentratorDeviceForm.getEnterpriseid(), concentratorDeviceVo.getConcentrator().getId());
            iInformationFactory.del(deviceForm);
            return ResultCode.SUCCESS;
        } catch (DataAccessException e) {
            LOGGER.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    /**
     * 删除一条集中器设备数据（只删除集中器中的水表表）
     *
     * @param concentratorDeviceForm
     * @return
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
    public String delete(ConcentratorDeviceForm concentratorDeviceForm) {
        try {
            if (StringUtils.isBlank(concentratorDeviceForm.getEnterpriseid())) {
                concentratorDeviceForm.setEnterpriseid(getEnterpriseid());
            }
            ConcentratorDeviceBo concentratorDeviceBo = BeanUtils.copy(concentratorDeviceForm, ConcentratorDeviceBo.class);
            iConcentratorDeviceService.delete(concentratorDeviceBo);
            return ResultCode.SUCCESS;
        } catch (DataAccessException e) {
            LOGGER.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    /**
     * 查询设备档案并分页
     *
     * @param concentratorDeviceForm
     * @return
     * @throws FrameworkRuntimeException
     */
    @Override
    public Pagination<ConcentratorDeviceVo> pageArchive(ConcentratorDeviceForm concentratorDeviceForm) throws FrameworkRuntimeException {
        return null;
    }

    /**
     * 导出设备档案
     *
     * @param concentratorDeviceForm
     * @throws FrameworkRuntimeException
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
    public XSSFWorkbook export(ConcentratorDeviceForm concentratorDeviceForm) throws FrameworkRuntimeException {
        List<ConcentratorDeviceVo> list = reordering(concentratorDeviceForm);
        if (list.isEmpty()) {
            throw new FrameworkRuntimeException(BaseExceptionConstants.UNDEFINED_ERROR, "该集中器下无设备");
        }
        XSSFWorkbook workBook = new XSSFWorkbook();
        XSSFSheet sheet = workBook.createSheet("集中器：" + list.get(0).getConcentrator().getCode());
        // 构建表头
        XSSFRow headRow = sheet.createRow(0);
        sheet.setDefaultColumnWidth(18);
        XSSFCell cell = null;
        String[] titles = {"序号", "集中器编号", "采集器通道号", "采集器编号", "水表通道号"
                , "水表编号", "当前读数", "是否带阀", "开阀状态", "安装时间", "安装位置"};
        for (int i = 0; i < titles.length; i++) {
            cell = headRow.createCell(i);
            cell.setCellValue(titles[i]);
        }
        // 构建表体数据
        for (int j = 0; j < list.size(); j++) {
            XSSFRow bodyRow = sheet.createRow(j + 1);
            ConcentratorDeviceVo concentratorDeviceVo = list.get(j);
            cell = bodyRow.createCell(0);
            cell.setCellValue(concentratorDeviceVo.getNo() + "");
            cell = bodyRow.createCell(1);
            cell.setCellValue(concentratorDeviceVo.getConcentrator().getCode());
            cell = bodyRow.createCell(2);
            cell.setCellValue(concentratorDeviceVo.getCollector().getChannel());
            cell = bodyRow.createCell(3);
            String collectorCode = ConcentratorUtils.getCollectorCode(concentratorDeviceVo.getCollector().getCode(), concentratorDeviceVo.getChannel());
            cell.setCellValue(collectorCode);
            cell = bodyRow.createCell(4);
            cell.setCellValue(concentratorDeviceVo.getChannel());
            cell = bodyRow.createCell(5);
            cell.setCellValue(concentratorDeviceVo.getDevno());
            cell = bodyRow.createCell(6);
            cell.setCellValue(concentratorDeviceVo.getWater() == null ? 0 : concentratorDeviceVo.getWater());
            cell = bodyRow.createCell(7);
            String tapType = concentratorDeviceVo.getTaptype() == 1 ? "是" : "否";
            cell.setCellValue(tapType);
            cell = bodyRow.createCell(8);
            String tapStatus = "";
            if (concentratorDeviceVo.getTaptype() == null || concentratorDeviceVo.getTaptype() == 0) {
                tapStatus = "-";
            } else if (concentratorDeviceVo.getTapstatus() != null && concentratorDeviceVo.getTapstatus() == 1) {
                tapStatus = "开阀";
            } else if (concentratorDeviceVo.getTapstatus() != null && concentratorDeviceVo.getTapstatus() == 0) {
                tapStatus = "关阀";
            }
            cell.setCellValue(tapStatus);
            cell = bodyRow.createCell(9);
            String installDate = concentratorDeviceVo.getAccesstime() == null ? "" : DateUtils.formatDatetime(concentratorDeviceVo.getAccesstime());
            cell.setCellValue(installDate);
            cell = bodyRow.createCell(10);
            cell.setCellValue(concentratorDeviceVo.getDevaddr());
        }
        return workBook;
//        try {
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

    /**
     * 根据集中器id重排序,更新数据库,并返回水表列表
     *
     * @param concentratorDeviceForm
     * @return
     * @throws FrameworkRuntimeException
     */
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
    @Override
    public List<ConcentratorDeviceVo> reordering(ConcentratorDeviceForm concentratorDeviceForm) throws FrameworkRuntimeException {
        try {
            String enterpriseid = concentratorDeviceForm.getEnterpriseid();
            Date date = new Date();
            String account = "系统";
            if (StringUtils.isBlank(enterpriseid)) {
                enterpriseid = getEnterpriseid();
                date = getCurr();
                account = getAccount();
            }
            //查询集中器是否需要重拍字段
            ConcentratorBo concentratorBo = new ConcentratorBo();
            concentratorBo.setId(concentratorDeviceForm.getConcentrator().getId());
            concentratorBo.setEnterpriseid(enterpriseid);
            ConcentratorVo concentratorVo = iConcentratorService.get(concentratorBo);
            //查询该集中器下的所有水表，并按照采集器通道数正序，再以水表通道口正序排列
            concentratorDeviceForm.setEnterpriseid(enterpriseid);
            ConcentratorDeviceBo concentratorDeviceBo = BeanUtils.copy(concentratorDeviceForm, ConcentratorDeviceBo.class);
            List<ConcentratorDeviceVo> concentratorDeviceVos = iConcentratorDeviceService.getConcentratorDeviceSort(concentratorDeviceBo);
            if (concentratorVo.getReordering() == null || concentratorVo.getReordering().equals(ConcentratorConstants.NOT_NEED_REORDERING)) {
                return concentratorDeviceVos;
            }
            List<ConcentratorDeviceBo> concentratorDeviceBos = new ArrayList<>();
            for (int i = 0; i < concentratorDeviceVos.size(); i++) {
                concentratorDeviceVos.get(i).setNo(i + 1);
                ConcentratorDeviceBo bo = BeanUtils.copy(concentratorDeviceVos.get(i), ConcentratorDeviceBo.class);
                bo.setCurr(date);
                bo.setUserBy(account);
                bo.setEnterpriseid(enterpriseid);
                bo.setIsDel(RootModel.NOT_DEL);
                concentratorDeviceBos.add(bo);
            }
            //批量更新水表序号
            if (!concentratorDeviceBos.isEmpty()) {
                iConcentratorDeviceService.updateNo(concentratorDeviceBos);
            }
            //排序完毕后更新集中器重排序字段
            concentratorBo.setReordering(ConcentratorConstants.NOT_NEED_REORDERING);
            iConcentratorService.editReordering(concentratorBo);
            return concentratorDeviceVos;
        } catch (FrameworkRuntimeException e) {
            LOGGER.error(LogMsg.to(e));
            throw e;
        } catch (Exception e) {
            LOGGER.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.BASE_ERROR, e.getMessage(), e);
        }
    }

    /**
     * 根据水表devid更新阀门状态
     *
     * @param concentratorDeviceForm
     * @return
     * @throws FrameworkRuntimeException
     */
    @Override
    public String setTapstatus(ConcentratorDeviceForm concentratorDeviceForm) throws FrameworkRuntimeException {
        try {
            String enterpriseid = concentratorDeviceForm.getEnterpriseid();
            String account = "系统";
            Date curr = new Date();
            if (StringUtils.isBlank(enterpriseid)) {
                enterpriseid = getEnterpriseid();
                account = getAccount();
                curr = getCurr();
            }
            ConcentratorDeviceBo concentratorDeviceBo = BeanUtils.copy(concentratorDeviceForm, ConcentratorDeviceBo.class);
            concentratorDeviceBo.setEnterpriseid(enterpriseid);
            concentratorDeviceBo.setUserBy(account);
            concentratorDeviceBo.setCurr(curr);
            Integer count = iConcentratorDeviceService.setTapstatus(concentratorDeviceBo);
            if (count != 1) {
                throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, "更新失败");
            }
            return ResultCode.SUCCESS;
        } catch (FrameworkRuntimeException e) {
            LOGGER.error(LogMsg.to(e));
            throw e;
        }
    }

    /**
     * 查询设备日用水量
     *
     * @param concentratorDeviceForm
     * @return
     * @throws FrameworkRuntimeException
     */
    @Override
    public Pagination<ConcentratorDeviceVo> pageUpLinkLogDate(ConcentratorDeviceForm concentratorDeviceForm) throws FrameworkRuntimeException {
        try {
            concentratorDeviceForm.setEnterpriseid(getEnterpriseid());
            Pagination<ConcentratorDeviceVo> concentratorDeviceVoPage = page(concentratorDeviceForm);
            if (concentratorDeviceForm.getReceiveDate() == null) {
                concentratorDeviceForm.setReceiveDate(new Date());
            }
            concentratorDeviceVoPage.getData().forEach(concentratorDeviceVo -> {
                UpLinkLogForm upLinkLogForm = new UpLinkLogForm();
                upLinkLogForm.setReceiveDate(concentratorDeviceForm.getReceiveDate());
                upLinkLogForm.setEnterpriseid(getEnterpriseid());
                upLinkLogForm.setConcentratorDevice(BeanUtils.copy(concentratorDeviceVo, ConcentratorDeviceBo.class));
                List<UpLinkLogVo> upLinkLogVos = iUpLinkLogFactory.getUplinkLogDateList(upLinkLogForm);
                concentratorDeviceVo.setUpLinkLogs(upLinkLogVos);
            });
            return concentratorDeviceVoPage;
        } catch (Exception e) {
            LOGGER.error(LogMsg.to(e));
            throw e;
        }
    }

    /**
     * 查询设备月用水量
     *
     * @param concentratorDeviceForm
     * @return
     * @throws FrameworkRuntimeException
     */
    @Override
    public Pagination<ConcentratorDeviceVo> pageUpLinkLogMonth(ConcentratorDeviceForm concentratorDeviceForm) throws FrameworkRuntimeException {
        try {
            concentratorDeviceForm.setEnterpriseid(getEnterpriseid());
            Pagination<ConcentratorDeviceVo> concentratorDeviceVoPage = page(concentratorDeviceForm);
            if (concentratorDeviceForm.getReceiveDate() == null) {
                concentratorDeviceForm.setReceiveDate(new Date());
            }
            concentratorDeviceVoPage.getData().forEach(concentratorDeviceVo -> {
                UpLinkLogForm upLinkLogForm = new UpLinkLogForm();
                upLinkLogForm.setReceiveDate(concentratorDeviceForm.getReceiveDate());
                upLinkLogForm.setEnterpriseid(getEnterpriseid());
                upLinkLogForm.setConcentratorDevice(BeanUtils.copy(concentratorDeviceVo, ConcentratorDeviceBo.class));
                List<UpLinkLogVo> upLinkLogVos = iUpLinkLogFactory.getUplinkLogMonthList(upLinkLogForm);
                concentratorDeviceVo.setUpLinkLogs(upLinkLogVos);
            });
            return concentratorDeviceVoPage;
        } catch (Exception e) {
            LOGGER.error(LogMsg.to(e));
            throw e;
        }
    }

    @Override
    public XSSFWorkbook exportUpLinkLogDate(ConcentratorDeviceForm concentratorDeviceForm) throws FrameworkRuntimeException {
        concentratorDeviceForm.setPage(1);
        concentratorDeviceForm.setPageCount(10000);
        List<ConcentratorDeviceVo> list = pageUpLinkLogDate(concentratorDeviceForm).getData();
        if (list.isEmpty()) {
            throw new FrameworkRuntimeException(BaseExceptionConstants.UNDEFINED_ERROR, "无数据");
        }
        XSSFWorkbook workBook = new XSSFWorkbook();
        XSSFSheet sheet = workBook.createSheet("水表报表日用图表格");
        // 构建表头
        XSSFRow headRow = sheet.createRow(0);
        sheet.setDefaultColumnWidth(18);
        XSSFCell cell = null;
        String[] titles = {"序号", "水表编号", "集中器编号", "采集器编号", "地址"};
        for (int i = 0; i < titles.length; i++) {
            cell = headRow.createCell(i);
            cell.setCellValue(titles[i]);
        }
        //获取某月最后一天
        DateTime dateTime = new DateTime(concentratorDeviceForm.getReceiveDate());
        int days = Integer.parseInt(DateUtils.format(DateUtils.day(DateUtils.month(dateTime.withDayOfMonth(1).toDate(), 1), -1), "dd"));
        for (int i = 1; i <= days; i++) {
            cell = headRow.createCell(i + titles.length - 1);
            cell.setCellValue(i + "号");
        }
        //计算日期
        if (concentratorDeviceForm.getReceiveDate() == null) {
            concentratorDeviceForm.setReceiveDate(new Date());
        }
        // 构建表体数据
        for (int j = 0; j < list.size(); j++) {
            XSSFRow bodyRow = sheet.createRow(j + 1);
            ConcentratorDeviceVo concentratorDeviceVo = list.get(j);
            cell = bodyRow.createCell(0);
            cell.setCellValue(j + 1);
            cell = bodyRow.createCell(1);
            cell.setCellValue(concentratorDeviceVo.getDevno());
            cell = bodyRow.createCell(2);
            cell.setCellValue(concentratorDeviceVo.getConcentrator().getCode());
            cell = bodyRow.createCell(3);
            String collectorCode = ConcentratorUtils.getCollectorCode(concentratorDeviceVo.getCollector().getCode(), concentratorDeviceVo.getChannel());
            cell.setCellValue(collectorCode);
            cell = bodyRow.createCell(4);
            cell.setCellValue(concentratorDeviceVo.getDevaddr());
            for (int i = 1; i <= days; i++) {
                cell = bodyRow.createCell(i + 4);
                String water = "-";
                Map<Integer, String> map = concentratorDeviceVo.getUpLinkLogs().stream().collect(Collectors.toMap(p -> Integer.parseInt(DateUtils.format(p.getReceiveDate(), "dd")), p -> p.getWater()));
                if (!StringUtils.isBlank(map.get(i))) {
                    water = map.get(i);
                }
                cell.setCellValue(water);
            }
        }
        return workBook;
    }

    @Override
    public XSSFWorkbook exportUpLinkLogMonth(ConcentratorDeviceForm concentratorDeviceForm) throws FrameworkRuntimeException {
        concentratorDeviceForm.setPage(1);
        concentratorDeviceForm.setPageCount(10000);
        List<ConcentratorDeviceVo> list = pageUpLinkLogMonth(concentratorDeviceForm).getData();
        if (list.isEmpty()) {
            throw new FrameworkRuntimeException(BaseExceptionConstants.UNDEFINED_ERROR, "无数据");
        }
        XSSFWorkbook workBook = new XSSFWorkbook();
        XSSFSheet sheet = workBook.createSheet("水表报表月用图表格");
        // 构建表头
        XSSFRow headRow = sheet.createRow(0);
        sheet.setDefaultColumnWidth(18);
        XSSFCell cell = null;
        String[] titles = {"序号", "水表编号", "集中器编号", "采集器编号", "地址"};
        for (int i = 0; i < titles.length; i++) {
            cell = headRow.createCell(i);
            cell.setCellValue(titles[i]);
        }
        //获取某月最后一天
        int days = 12;
        for (int i = 1; i <= days; i++) {
            cell = headRow.createCell(i + titles.length - 1);
            cell.setCellValue(i + "月");
        }
        //计算日期
        if (concentratorDeviceForm.getReceiveDate() == null) {
            concentratorDeviceForm.setReceiveDate(new Date());
        }
        // 构建表体数据
        for (int j = 0; j < list.size(); j++) {
            XSSFRow bodyRow = sheet.createRow(j + 1);
            ConcentratorDeviceVo concentratorDeviceVo = list.get(j);
            cell = bodyRow.createCell(0);
            cell.setCellValue(j + 1);
            cell = bodyRow.createCell(1);
            cell.setCellValue(concentratorDeviceVo.getDevno());
            cell = bodyRow.createCell(2);
            cell.setCellValue(concentratorDeviceVo.getConcentrator().getCode());
            cell = bodyRow.createCell(3);
            String collectorCode = ConcentratorUtils.getCollectorCode(concentratorDeviceVo.getCollector().getCode(), concentratorDeviceVo.getChannel());
            cell.setCellValue(collectorCode);
            cell = bodyRow.createCell(4);
            cell.setCellValue(concentratorDeviceVo.getDevaddr());
            for (int i = 1; i <= days; i++) {
                cell = bodyRow.createCell(i + 4);
                String water = "-";
                Map<Integer, String> map = concentratorDeviceVo.getUpLinkLogs().stream().collect(Collectors.toMap(p -> Integer.parseInt(DateUtils.format(p.getReceiveDate(), "MM")), p -> p.getWater()));
                if (!StringUtils.isBlank(map.get(i))) {
                    water = map.get(i);
                }
                cell.setCellValue(water);
            }
        }
        return workBook;
    }
}
