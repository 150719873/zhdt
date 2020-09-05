package com.dotop.smartwater.project.third.concentrator.api.impl;

import com.dotop.smartwater.project.third.concentrator.core.bo.CollectorBo;
import com.dotop.smartwater.project.third.concentrator.core.bo.ConcentratorBo;
import com.dotop.smartwater.project.third.concentrator.core.bo.ConcentratorDeviceBo;
import com.dotop.smartwater.project.third.concentrator.core.bo.ConcentratorFileBo;
import com.dotop.smartwater.project.third.concentrator.core.vo.CollectorVo;
import com.dotop.smartwater.project.third.concentrator.core.vo.ConcentratorDeviceVo;
import com.dotop.smartwater.project.third.concentrator.core.vo.ConcentratorFileVo;
import com.dotop.smartwater.project.third.concentrator.core.vo.ConcentratorVo;
import com.dotop.smartwater.project.third.concentrator.service.ICollectorService;
import com.dotop.smartwater.project.third.concentrator.service.IConcentratorDeviceService;
import com.dotop.smartwater.project.third.concentrator.service.IConcentratorService;
import com.dotop.smartwater.dependence.cache.StringValueCache;
import com.dotop.smartwater.dependence.core.common.RootModel;
import com.dotop.smartwater.dependence.core.exception.BaseExceptionConstants;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.log.LogMsg;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.dependence.core.utils.BeanUtils;
import com.dotop.smartwater.dependence.core.utils.DateUtils;
import com.dotop.smartwater.dependence.core.utils.UuidUtils;
import com.dotop.smartwater.project.module.api.device.IDeviceFactory;
import com.dotop.smartwater.project.module.core.auth.bo.AreaBo;
import com.dotop.smartwater.project.module.core.auth.form.AreaForm;
import com.dotop.smartwater.project.module.core.auth.local.IAuthCasClient;
import com.dotop.smartwater.project.module.core.auth.vo.AreaVo;
import com.dotop.smartwater.project.module.core.water.form.DeviceForm;
import com.dotop.smartwater.project.module.core.water.utils.ExcelUtil;
import com.dotop.smartwater.project.third.concentrator.api.IConcentratorFactory;
import com.dotop.smartwater.project.third.concentrator.client.netty.dc.model.code.ResultCode;
import com.dotop.smartwater.project.third.concentrator.core.constants.CacheKey;
import com.dotop.smartwater.project.third.concentrator.core.constants.ConcentratorConstants;
import com.dotop.smartwater.project.third.concentrator.core.form.ConcentratorForm;
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
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 集中器/中继器业务逻辑接口
 *
 *
 */
@Component
public class ConcentratorFactoryImpl implements IConcentratorFactory, IAuthCasClient {

    private static final Logger LOGGER = LogManager.getLogger(ConcentratorFactoryImpl.class);

    @Autowired
    private StringValueCache svc;
    @Autowired
    private IConcentratorService iConcentratorService;
    @Autowired
    private ICollectorService iCollectorService;
    @Autowired
    private IConcentratorDeviceService iConcentratorDeviceService;
    @Autowired
    private IDeviceFactory iDeviceFactory;

    @Override
    public ConcentratorVo add(ConcentratorForm concentratorForm) throws FrameworkRuntimeException {
        try {
            ConcentratorBo concentratorBo = BeanUtils.copy(concentratorForm, ConcentratorBo.class);
            concentratorBo.setEnterpriseid(getEnterpriseid());
            concentratorBo.setUserBy(getAccount());
            concentratorBo.setCurr(getCurr());
            concentratorBo.setIsDel(RootModel.NOT_DEL);
            return iConcentratorService.add(concentratorBo);
        } catch (DataAccessException e) {
            LOGGER.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    @Override
    public ConcentratorVo get(ConcentratorForm concentratorForm) throws FrameworkRuntimeException {
        try {
            ConcentratorBo concentratorBo = BeanUtils.copy(concentratorForm, ConcentratorBo.class);
            concentratorBo.setEnterpriseid(getEnterpriseid());
            return iConcentratorService.get(concentratorBo);
        } catch (DataAccessException e) {
            LOGGER.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    @Override
    public Pagination<ConcentratorVo> page(ConcentratorForm concentratorForm) throws FrameworkRuntimeException {
        try {
            ConcentratorBo concentratorBo = BeanUtils.copy(concentratorForm, ConcentratorBo.class);
            concentratorBo.setEnterpriseid(getEnterpriseid());
            return iConcentratorService.page(concentratorBo);
        } catch (DataAccessException e) {
            LOGGER.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    @Override
    public List<ConcentratorVo> list(ConcentratorForm concentratorForm) throws FrameworkRuntimeException {
        try {
            ConcentratorBo concentratorBo = BeanUtils.copy(concentratorForm, ConcentratorBo.class);
            concentratorBo.setEnterpriseid(getEnterpriseid());
            return iConcentratorService.list(concentratorBo);
        } catch (DataAccessException e) {
            LOGGER.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    @Override
    public ConcentratorVo edit(ConcentratorForm concentratorForm) throws FrameworkRuntimeException {
        try {
            ConcentratorBo concentratorBo = BeanUtils.copy(concentratorForm, ConcentratorBo.class);
            concentratorBo.setEnterpriseid(getEnterpriseid());
            concentratorBo.setUserBy(getAccount());
            concentratorBo.setCurr(getCurr());
            return iConcentratorService.edit(concentratorBo);
        } catch (DataAccessException e) {
            LOGGER.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    @Override
    public ConcentratorVo editStatus(ConcentratorForm concentratorForm) throws FrameworkRuntimeException {
        try {
            //？？？by
            if ("DISABLE".equals(concentratorForm.getStatus()) || concentratorForm.getStatus() == null) {
                concentratorForm.setStatus(ConcentratorConstants.STATUS_USE);
            } else {
                concentratorForm.setStatus(ConcentratorConstants.STATUS_DISABLE);
            }
            ConcentratorBo concentratorBo = BeanUtils.copy(concentratorForm, ConcentratorBo.class);
            concentratorBo.setEnterpriseid(getEnterpriseid());
            return iConcentratorService.editStatus(concentratorBo);
        } catch (DataAccessException e) {
            LOGGER.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    @Override
    public String del(ConcentratorForm concentratorForm) throws FrameworkRuntimeException {
        try {
            ConcentratorBo concentratorBo = BeanUtils.copy(concentratorForm, ConcentratorBo.class);
            concentratorBo.setEnterpriseid(getEnterpriseid());
            return iConcentratorService.del(concentratorBo);
        } catch (DataAccessException e) {
            LOGGER.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    @Override
    public Pagination<ConcentratorVo> pageArchive(ConcentratorForm concentratorForm) throws FrameworkRuntimeException {
        try {
            ConcentratorBo concentratorBo = BeanUtils.copy(concentratorForm, ConcentratorBo.class);
            concentratorBo.setEnterpriseid(getEnterpriseid());
            Pagination<ConcentratorVo> page = iConcentratorService.pageArchive(concentratorBo);
            for (ConcentratorVo concentratorVo : page.getData()) {
                //查询采集器挂载数
                Integer collectorAmount = iCollectorService.countCollector(concentratorVo.getId(), null);
                concentratorVo.setCollectorMountAmount(collectorAmount);
                //查询采集器通道数
                Integer collectorChannelAmount = iConcentratorDeviceService.countCollectorChannel(getEnterpriseid(), concentratorVo.getId(), null);
                concentratorVo.setCollectorChannelAmount(collectorChannelAmount);
                //查询水表挂载数
                Integer deviceMountAmount = iConcentratorDeviceService.countConcentratorDevice(getEnterpriseid(), concentratorVo.getId(), null);
                concentratorVo.setDeviceMountAmount(deviceMountAmount);
            }
            return page;
        } catch (DataAccessException e) {
            LOGGER.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    @Override
    public void editReordering(ConcentratorForm concentratorForm) throws FrameworkRuntimeException {
        try {
            ConcentratorBo concentratorBo = BeanUtils.copy(concentratorForm, ConcentratorBo.class);
            concentratorBo.setEnterpriseid(getEnterpriseid());
            int count = iConcentratorService.editReordering(concentratorBo);
            if (count != 1) {
                throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, "更新重排序错误");
            }
        } catch (DataAccessException e) {
            LOGGER.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    @Override
    public void needReordering(String enterpriseid, String concentratorId) throws FrameworkRuntimeException {
        ConcentratorForm concentratorForm = new ConcentratorForm();
        concentratorForm.setId(concentratorId);
        concentratorForm.setReordering(ConcentratorConstants.NEED_REORDERING);
        ConcentratorBo concentratorBo = BeanUtils.copy(concentratorForm, ConcentratorBo.class);
        concentratorBo.setEnterpriseid(getEnterpriseid());
        iConcentratorService.editReordering(concentratorBo);
    }

    @Override
    public ConcentratorVo getByCode(ConcentratorForm concentratorForm) throws FrameworkRuntimeException {
        try {
            ConcentratorBo concentratorBo = BeanUtils.copy(concentratorForm, ConcentratorBo.class);
            concentratorBo.setEnterpriseid(getEnterpriseid());
            return iConcentratorService.getByCode(concentratorBo);
        } catch (DataAccessException e) {
            LOGGER.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    @Override
    public boolean isExist(ConcentratorForm concentratorForm) throws FrameworkRuntimeException {
        try {
            ConcentratorBo concentratorBo = BeanUtils.copy(concentratorForm, ConcentratorBo.class);
            concentratorBo.setEnterpriseid(getEnterpriseid());
            Boolean exist = iConcentratorService.isExist(concentratorBo);
            return exist;
        } catch (DataAccessException e) {
            LOGGER.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    @Override
    public XSSFWorkbook export(ConcentratorForm concentratorForm) throws FrameworkRuntimeException {
//        try {
        ConcentratorFileBo concentratorFileBo = new ConcentratorFileBo();
        concentratorFileBo.setEnterpriseid(getEnterpriseid());
        concentratorFileBo.setConcentratorCode(concentratorForm.getCode());
        List<ConcentratorFileVo> list = iConcentratorService.listFile(concentratorFileBo);
        if (list.isEmpty()) {
            throw new FrameworkRuntimeException(BaseExceptionConstants.UNDEFINED_ERROR, "该集中器无档案");
        }
        XSSFWorkbook workBook = new XSSFWorkbook();
        XSSFSheet sheet = workBook.createSheet("集中器：" + concentratorForm.getCode());
        // 构建表头
        XSSFRow headRow = sheet.createRow(0);
        sheet.setDefaultColumnWidth(18);
        XSSFCell cell = null;
        String[] titles = {"序号", "集中器编号", "采集器通道号", "采集器编号", "水表通道号", "水表编号"};
        for (int i = 0; i < titles.length; i++) {
            cell = headRow.createCell(i);
            cell.setCellValue(titles[i]);
        }
        // 构建表体数据
        for (int j = 0; j < list.size(); j++) {
            XSSFRow bodyRow = sheet.createRow(j + 1);
            ConcentratorFileVo concentratorFileVo = list.get(j);
            cell = bodyRow.createCell(0);
            cell.setCellValue(concentratorFileVo.getNo());
            cell = bodyRow.createCell(1);
            cell.setCellValue(concentratorFileVo.getConcentratorCode());
            cell = bodyRow.createCell(2);
            cell.setCellValue(concentratorFileVo.getCollectorCode().substring(1, 2));
            cell = bodyRow.createCell(3);
            cell.setCellValue(concentratorFileVo.getCollectorCode());
            cell = bodyRow.createCell(4);
            if (concentratorFileVo.getCollectorCode().endsWith(ConcentratorConstants.COLLECTOR_ABSTRACT)) {
                cell.setCellValue(concentratorFileVo.getCollectorCode().substring(1, 2));
            } else {
                cell.setCellValue(concentratorFileVo.getCollectorCode().substring(11, 12));
            }
            cell = bodyRow.createCell(5);
            cell.setCellValue(concentratorFileVo.getDevno());
        }
        return workBook;
//            OutputStream outputStream = response.getOutputStream();
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
     * 导入设备档案
     *
     * @param multipartFile
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
    @Override
    public String analyseFile(MultipartFile multipartFile) {
        try {
            if (multipartFile != null) {
                InputStream ins = multipartFile.getInputStream();
                String[][] map;
                if(multipartFile.getOriginalFilename().contains(".xlsx")){
                    map = ExcelUtil.read2007ExcelContent(ins, 0, 12);
                }else if(multipartFile.getOriginalFilename().contains(".xls")){
                    map = ExcelUtil.readExcelContent(ins, 0, 12);
                }else{
                    throw new FrameworkRuntimeException(BaseExceptionConstants.UNDEFINED_ERROR, "文件格式错误");
                }
                //获取当前用户下所有集中器
                ConcentratorBo concentratorBo = new ConcentratorBo();
                concentratorBo.setEnterpriseid(getEnterpriseid());
                List<ConcentratorVo> concentratorVos = iConcentratorService.list(concentratorBo);
                //获取当前用户下该集中器下的所有采集器
                CollectorBo collectorBo = new CollectorBo();
                collectorBo.setEnterpriseid(getEnterpriseid());
                concentratorBo.setCode(map[1][1]);
                collectorBo.setConcentrator(concentratorBo);
                List<CollectorVo> collectorVos = iCollectorService.list(collectorBo);
                //获取所有水表
                ConcentratorDeviceBo concentratorDeviceBo = new ConcentratorDeviceBo();
                concentratorDeviceBo.setEnterpriseid(getEnterpriseid());
                List<ConcentratorDeviceVo> concentratorDeviceVos = iConcentratorDeviceService.list(concentratorDeviceBo);
                //转map
                Map<String, ConcentratorVo> concentratorMap = concentratorVos.stream().collect(Collectors.toMap(ConcentratorVo::getCode, x -> x));
                Map<String, CollectorVo> collectorMap = collectorVos.stream().collect(Collectors.toMap(CollectorVo::getCode, x -> x));
                Map<String, ConcentratorDeviceVo> concentratorDeviceMap = concentratorDeviceVos.stream().collect(Collectors.toMap(ConcentratorDeviceVo::getDevno, x -> x));
                String[] titles = {"序号", "集中器编号", "采集器通道号", "采集器编号", "水表通道号", "水表编号", "口径", "是否带阀", "开阀状态", "安装时间", "安装位置"};
                //可录入列表
                List<ConcentratorDeviceBo> newConcentratorDeviceBos = new ArrayList<>();
                List<DeviceForm> deviceForms = new ArrayList<>();
                String concentratorCode = map[1][1];
                for (int i = 1; i < map.length; i++) {
                    if (StringUtils.isBlank(map[i][0])) {
                        break;
                    }
                    for (int j = 0; j < titles.length - 3; j++) {
                        if (StringUtils.isBlank(map[i][j])) {
                            throw new FrameworkRuntimeException(BaseExceptionConstants.UNDEFINED_ERROR, titles[j] + "不能为空");
                        }
                    }
                    String collectorCode = map[i][3];
                    if (map[i][3].endsWith(map[i][4]) || map[i][3].endsWith("0") || map[i][3].endsWith(ConcentratorConstants.COLLECTOR_ABSTRACT)) {
                        collectorCode = collectorCode.substring(0, collectorCode.length() - 1) + "0";
                    } else {
                        throw new FrameworkRuntimeException(BaseExceptionConstants.UNDEFINED_ERROR, "采集器编号与水表通道号不对应");
                    }
                    if(!concentratorCode.equals(map[i][1])){
                        throw new FrameworkRuntimeException(BaseExceptionConstants.UNDEFINED_ERROR, "一次只能导入一个集中器数据");
                    }
                    if (concentratorMap.get(map[i][1]) == null) {
                        throw new FrameworkRuntimeException(BaseExceptionConstants.UNDEFINED_ERROR, "集中器：" + map[i][1] + "不存在");
                    } else if (collectorMap.get(collectorCode) == null) {
                        throw new FrameworkRuntimeException(BaseExceptionConstants.UNDEFINED_ERROR, "采集器：" + map[i][3] + "不存在");
                    } else if (concentratorDeviceMap.get(map[i][5]) != null) {
                        throw new FrameworkRuntimeException(BaseExceptionConstants.UNDEFINED_ERROR, "水表编号：" + map[i][5] + "已存在");
                    } else {
                        //需要重排序
                        needReordering(getEnterpriseid(), concentratorMap.get(map[i][1]).getId());
                        //可录入:水表
                        ConcentratorDeviceBo newConcentratorDeviceBo = new ConcentratorDeviceBo();
                        newConcentratorDeviceBo.setChannel(map[i][4]);
                        newConcentratorDeviceBo.setDevno(map[i][5]);
                        newConcentratorDeviceBo.setCaliber(map[i][6]);
                        newConcentratorDeviceBo.setUserBy(getAccount());
                        newConcentratorDeviceBo.setEnterpriseid(getEnterpriseid());
                        newConcentratorDeviceBo.setCurr(getCurr());
                        newConcentratorDeviceBo.setId(UuidUtils.getUuid());
                        newConcentratorDeviceBo.setDevid(UuidUtils.getUuid());
                        newConcentratorDeviceBo.setNo(0);
                        int tapType = 0;
                        if ("是".equals(map[i][7])) {
                            tapType = 1;
                        }
                        newConcentratorDeviceBo.setTaptype(tapType);
                        int tapStatus = 1;
                        if ("关阀".equals(map[i][8])) {
                            tapStatus = 0;
                        }
                        newConcentratorDeviceBo.setTapstatus(tapStatus);
                        newConcentratorDeviceBo.setConcentrator(BeanUtils.copy(concentratorMap.get(map[i][1]), ConcentratorBo.class));
                        newConcentratorDeviceBo.setCollector(BeanUtils.copy(collectorMap.get(collectorCode), CollectorBo.class));
                        newConcentratorDeviceBo.setKind("real");
                        newConcentratorDeviceBo.setTypeid("2");
                        newConcentratorDeviceBo.setBeginvalue(0.0);
                        newConcentratorDeviceBo.setMode("28,300001,6");
                        if(!StringUtils.isBlank(map[i][9])) {
                            newConcentratorDeviceBo.setAccesstime(DateUtils.parseDatetime(map[i][9].replaceAll("/","-")));
                        }
                        if(!StringUtils.isBlank(map[i][10])){
                            newConcentratorDeviceBo.setDevaddr(map[i][10]);
                        }
                        DeviceForm deviceForm = BeanUtils.copy(newConcentratorDeviceBo, DeviceForm.class);
                        deviceForm.setId(UuidUtils.getUuid());
                        deviceForms.add(deviceForm);
                        //添加水表到新增list
                        if (newConcentratorDeviceBos.stream().filter(con -> con.getDevno().equals(newConcentratorDeviceBo.getDevno())).count() == 0) {
                            newConcentratorDeviceBos.add(newConcentratorDeviceBo);
                        } else {
                            throw new FrameworkRuntimeException(BaseExceptionConstants.UNDEFINED_ERROR, "导入列表中存在相同水表号");
                        }
                    }
                }
                //开始保存
                //批量添加水表
                deviceForms.forEach(df -> {
                    iDeviceFactory.addDeviceByWeb(df);
                });
                iConcentratorDeviceService.adds(newConcentratorDeviceBos);
            }
            return ResultCode.SUCCESS;
        } catch (FrameworkRuntimeException e) {
            LOGGER.error(LogMsg.to(e));
            throw e;
        } catch (Exception e) {
            LOGGER.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.UNDEFINED_ERROR, e.getMessage(), e);
        }
    }

    /**
     * 获取区域列表
     *
     * @param areaForm
     * @return
     * @throws FrameworkRuntimeException
     */
    @Override
    public List<AreaVo> getAreaList(AreaForm areaForm) throws FrameworkRuntimeException {
        if (StringUtils.isBlank(areaForm.getEnterpriseid())) {
            areaForm.setEnterpriseid(getEnterpriseid());
        }
        AreaBo areaBo = BeanUtils.copy(areaForm, AreaBo.class);
        return iConcentratorService.getAreaList(areaBo);
    }

    @Override
    public Pagination<ConcentratorVo> onlinePage(ConcentratorForm concentratorForm) {
        ConcentratorBo concentratorBo = BeanUtils.copy(concentratorForm, ConcentratorBo.class);
        concentratorBo.setEnterpriseid(getEnterpriseid());
        Pagination<ConcentratorVo> page = iConcentratorService.page(concentratorBo);
        List<ConcentratorVo> list = page.getData();
        for (ConcentratorVo c : list) {
            String concentratorCode = c.getCode();
            String heartbeat = svc.get(CacheKey.HEARTBEAT + concentratorCode);
            if (StringUtils.isNotBlank(heartbeat)) {
                c.setIsOnline(ConcentratorConstants.ONLINE_ONLINE);
            } else {
                c.setIsOnline(ConcentratorConstants.ONLINE_OFFLINE);
            }
        }
        return page;
    }

    @Override
    public XSSFWorkbook exportOnline(ConcentratorForm concentratorForm) throws FrameworkRuntimeException {
        concentratorForm.setPage(1);
        concentratorForm.setPageCount(10000);
        List<ConcentratorVo> list = onlinePage(concentratorForm).getData();
        if (list.isEmpty()) {
            throw new FrameworkRuntimeException(BaseExceptionConstants.UNDEFINED_ERROR, "无数据");
        }
        XSSFWorkbook workBook = new XSSFWorkbook();
        XSSFSheet sheet = workBook.createSheet("集中器是否在线全览图");
        // 构建表头
        XSSFRow headRow = sheet.createRow(0);
        sheet.setDefaultColumnWidth(18);
        XSSFCell cell = null;
        String[] titles = {"序号", "集中器编号", "安装位置", "描述", "在线状态"};
        for (int i = 0; i < titles.length; i++) {
            cell = headRow.createCell(i);
            cell.setCellValue(titles[i]);
        }
        // 构建表体数据
        for (int j = 0; j < list.size(); j++) {
            XSSFRow bodyRow = sheet.createRow(j + 1);
            ConcentratorVo concentratorVo = list.get(j);
            cell = bodyRow.createCell(0);
            cell.setCellValue(j + 1);
            cell = bodyRow.createCell(1);
            cell.setCellValue(concentratorVo.getCode());
            cell = bodyRow.createCell(2);
            cell.setCellValue(concentratorVo.getInstallAddress());
            cell = bodyRow.createCell(3);
            cell.setCellValue(concentratorVo.getDesc());
            cell = bodyRow.createCell(4);
            String online = "";
            if (ConcentratorConstants.ONLINE_ONLINE.equals(concentratorVo.getIsOnline())) {
                online = "在线";
            } else if (ConcentratorConstants.ONLINE_OFFLINE.equals(concentratorVo.getIsOnline())) {
                online = "不在线";
            }
            cell.setCellValue(online);
        }
        return workBook;
    }
}
