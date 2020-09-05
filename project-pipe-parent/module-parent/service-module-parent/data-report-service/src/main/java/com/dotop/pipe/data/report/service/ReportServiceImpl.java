package com.dotop.pipe.data.report.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import com.dotop.pipe.core.enums.DateTypeEnum;
import com.dotop.pipe.core.enums.FieldTypeEnum;
import com.dotop.pipe.core.vo.area.AreaModelVo;
import com.dotop.pipe.data.report.api.dao.IReportDao;
import com.dotop.pipe.data.report.api.service.IReportService;
import com.dotop.pipe.data.report.core.dto.report.ReportDto;
import com.dotop.pipe.data.report.core.vo.AreaReportVo;
import com.dotop.pipe.data.report.core.vo.RegionReportVo;
import com.dotop.pipe.data.report.core.vo.ReportAreaGroupVo;
import com.dotop.pipe.data.report.core.vo.ReportVo;
import com.dotop.smartwater.dependence.core.common.RootModel;
import com.dotop.smartwater.dependence.core.exception.BaseExceptionConstants;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.log.LogMsg;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

/**
 *
 * @date 2018/11/2.
 */
@Service
public class ReportServiceImpl implements IReportService {

    private final static Logger logger = LogManager.getLogger(ReportServiceImpl.class);

    @Autowired
    private IReportDao iReportDao;

    @Override
    public List<ReportVo> getDeviceReport(String enterpriseId, List<String> deviceIds, DateTypeEnum dateType,
                                          FieldTypeEnum[] fieldType, Date startDate, Date endDate, Set<String> ctimes)
            throws FrameworkRuntimeException {
        try {
            Integer isDel = RootModel.NOT_DEL;
            // 参数转换
            ReportDto reportDto = new ReportDto();
            reportDto.setDeviceIds(deviceIds);
            reportDto.setDateType(dateType);
            reportDto.setFieldType(fieldType);
            reportDto.setStartDate(startDate);
            reportDto.setEndDate(endDate);
            reportDto.setCtimes(ctimes);
            reportDto.setIsDel(isDel);
            reportDto.setEnterpriseId(enterpriseId);
            return iReportDao.getDeviceReport(reportDto);
        } catch (FrameworkRuntimeException e) {
            logger.error(LogMsg.to(e));
            throw e;
        } catch (DataAccessException e) {
            logger.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        } catch (Throwable e) {
            logger.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.BASE_ERROR, e.getMessage(), e);
        }
    }

    @Override
    public Pagination<ReportVo> getDeviceReportPage(String enterpriseId, List<String> deviceIds, DateTypeEnum dateType,
                                                    FieldTypeEnum[] fieldType, Date startDate, Date endDate, Set<String> ctimes, Integer page, Integer pageSize)
            throws FrameworkRuntimeException {
        try {
            Integer isDel = RootModel.NOT_DEL;
            // 参数转换
            ReportDto reportDto = new ReportDto();
            reportDto.setDeviceIds(deviceIds);
            reportDto.setDateType(dateType);
            reportDto.setFieldType(fieldType);
            reportDto.setStartDate(startDate);
            reportDto.setEndDate(endDate);
            reportDto.setCtimes(ctimes);
            reportDto.setIsDel(isDel);
            reportDto.setEnterpriseId(enterpriseId);
            Page<Object> pageHelper = PageHelper.startPage(page, pageSize);
            List<ReportVo> list = iReportDao.getDeviceReport(reportDto);
            Pagination<ReportVo> pagination = new Pagination<>(pageSize, page);
            pagination.setData(list);
            pagination.setTotalPageSize(pageHelper.getTotal());
            return pagination;
        } catch (FrameworkRuntimeException e) {
            logger.error(LogMsg.to(e));
            throw e;
        } catch (DataAccessException e) {
            logger.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        } catch (Throwable e) {
            logger.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.BASE_ERROR, e.getMessage(), e);
        }
    }

    @Override
    public List<ReportVo> getDeviceRealTime(String enterpriseId, List<String> deviceIds, FieldTypeEnum[] fieldType)
            throws FrameworkRuntimeException {
        try {
            Integer isDel = RootModel.NOT_DEL;
            // 参数转换
            ReportDto reportDto = new ReportDto();
            reportDto.setDeviceIds(deviceIds);
            reportDto.setFieldType(fieldType);
            reportDto.setIsDel(isDel);
            reportDto.setEnterpriseId(enterpriseId);
            return iReportDao.getDeviceRealTime(reportDto);
        } catch (FrameworkRuntimeException e) {
            logger.error(LogMsg.to(e));
            throw e;
        } catch (DataAccessException e) {
            logger.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        } catch (Throwable e) {
            logger.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.BASE_ERROR, e.getMessage(), e);
        }
    }

    @Override
    public List<ReportAreaGroupVo> getAreaReport(String enterpriseId, List<String> areaIds, DateTypeEnum dateType,
                                                 Date startDate, Date endDate, Set<String> ctimes) throws FrameworkRuntimeException {
        try {
            Integer isDel = RootModel.NOT_DEL;
            ReportDto reportDto = new ReportDto();
            reportDto.setEnterpriseId(enterpriseId);
            reportDto.setAreaIds(areaIds);
            reportDto.setDateType(dateType);
            reportDto.setStartDate(startDate);
            reportDto.setEndDate(endDate);
            reportDto.setCtimes(ctimes);
            reportDto.setIsDel(isDel);
            reportDto.setEnterpriseId(enterpriseId);
            // 参数转换
            return iReportDao.getAreaReport(reportDto);
        } catch (FrameworkRuntimeException e) {
            logger.error(LogMsg.to(e));
            throw e;
        } catch (DataAccessException e) {
            logger.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        } catch (Throwable e) {
            logger.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.BASE_ERROR, e.getMessage(), e);
        }
    }

    @Override
    public List<ReportVo> getVirtualFlow(String enterpriseId, List<String> positiveIds, List<String> reverseIds,
                                         Date startDate, Date endDate, Set<String> ctimes) throws FrameworkRuntimeException {
        try {
            Integer isDel = RootModel.NOT_DEL;

            // 为了解决分页不支持子查询的问题
            // 正向流量计数据
            List<ReportVo> reportVoPositive = iReportDao.getVirtualFlow(enterpriseId, isDel, positiveIds, null,
                    startDate, endDate, ctimes);
            // 发现流量计数据
            List<ReportVo> reportVoReverse = iReportDao.getVirtualFlow(enterpriseId, isDel, null, reverseIds, startDate,
                    endDate, ctimes);
            ReportVo reportVo = new ReportVo();
            // 数据计算 正向数据减去反向数据
            BigDecimal positiveBigDec = new BigDecimal(0);
            if (reportVoPositive != null && reportVoPositive.size() > 0 && reportVoPositive.get(0) != null) {
                positiveBigDec = new BigDecimal(reportVoPositive.get(0).getSumVal());
                reportVo.setSendDate(reportVoPositive.get(0).getSendDate());
            }
            BigDecimal reverseBigDec = new BigDecimal(0);
            if (reportVoReverse != null && reportVoReverse.size() > 0 && reportVoReverse.get(0) != null) {
                reverseBigDec = new BigDecimal(reportVoReverse.get(0).getSumVal());
            }
            BigDecimal sum = positiveBigDec.subtract(reverseBigDec);
            reportVo.setSumVal(sum.toString());
            List<ReportVo> list = new ArrayList<ReportVo>();
            list.add(reportVo);
            return list;

            // 参数转换
            // return iReportDao.getVirtualFlow(positiveIds, reverseIds, startDate, endDate,
            // str.toString());
        } catch (FrameworkRuntimeException e) {
            logger.error(LogMsg.to(e));
            throw e;
        } catch (DataAccessException e) {
            logger.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        } catch (Throwable e) {
            logger.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.BASE_ERROR, e.getMessage(), e);
        }
    }

    @Override
    public Pagination<RegionReportVo> getRegionReportPage(String operEid, List<String> deviceIds,
                                                          DateTypeEnum dateTypeEnum, Date startDate, Date endDate, Set<String> ctimes, Integer page,
                                                          Integer pageSize) {
        try {
            Integer isDel = RootModel.NOT_DEL;
            // 参数转换
            ReportDto reportDto = new ReportDto();
            reportDto.setDeviceIds(deviceIds);
            reportDto.setDateType(dateTypeEnum);
            reportDto.setStartDate(startDate);
            reportDto.setEndDate(endDate);
            reportDto.setCtimes(ctimes);
            reportDto.setIsDel(isDel);
            reportDto.setEnterpriseId(operEid);
            Page<Object> pageHelper = PageHelper.startPage(page, pageSize);
            List<RegionReportVo> list = iReportDao.getRegionReportPage(reportDto);
            Map<String, ReportVo> map = iReportDao.queryDeviceDateLog(reportDto);
            for (RegionReportVo regionReportVo : list) {
                Double totalVal = 0.0;
                if (!regionReportVo.deviceList.isEmpty()) {
                    for (ReportVo _reportVo : regionReportVo.deviceList) {
                        if (map.containsKey(_reportVo.getDeviceId())) {
                            System.out.println(map.get(_reportVo.getDeviceId()).flwTotalValue);
                            if (map.get(_reportVo.getDeviceId()).flwTotalValue != null) {
                                totalVal = totalVal + Double.valueOf(map.get(_reportVo.getDeviceId()).flwTotalValue);
                            }
                        }
                    }
                }
                regionReportVo.setFlwTotalValue(totalVal);
                regionReportVo.setDeviceCount(regionReportVo.getDeviceList().size());
                regionReportVo.setDeviceList(null);
            }
            Pagination<RegionReportVo> pagination = new Pagination<>(pageSize, page);
            pagination.setData(list);
            pagination.setTotalPageSize(pageHelper.getTotal());
            return pagination;
        } catch (FrameworkRuntimeException e) {
            logger.error(LogMsg.to(e));
            throw e;
        } catch (DataAccessException e) {
            logger.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        } catch (Throwable e) {
            logger.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.BASE_ERROR, e.getMessage(), e);
        }
    }

    @Override
    public List<AreaReportVo> getAreaReportByReading(Date startDate, Date endDate, Set<String> ctimes, String operEid,
                                                     List<AreaModelVo> areaIds) {
        try {

            List<String> deviceIds = new ArrayList<>();
            for (AreaModelVo areaModelVo : areaIds) {
                deviceIds.add(areaModelVo.getAreaId());
            }

            Integer isDel = RootModel.NOT_DEL;
            ReportDto reportDto = new ReportDto();
            reportDto.setDeviceIds(deviceIds);
            reportDto.setStartDate(startDate);
            reportDto.setEndDate(endDate);
            reportDto.setCtimes(ctimes);
            reportDto.setIsDel(isDel);
            reportDto.setEnterpriseId(operEid);
            Map<String, ReportVo> map = iReportDao.queryDeviceDateLog(reportDto);
            List<AreaReportVo> reportList = new ArrayList<>();
            for (AreaModelVo areaModelVo : areaIds) {
                AreaReportVo areaReportVo = new AreaReportVo();
                if (map.containsKey(areaModelVo.getAreaId())) {
                    if(map.get(areaModelVo.getAreaId()).flwTotalValue!= null){
                        areaReportVo.setFlwTotalValue(Double.valueOf(map.get(areaModelVo.getAreaId()).flwTotalValue));
                    }else{
                        areaReportVo.setFlwTotalValue(0.0);
                    }
                } else {
                    areaReportVo.setFlwTotalValue(0.0);
                }
                areaReportVo.setAreaCode(areaModelVo.getAreaCode());
                areaReportVo.setAreaName(areaModelVo.getName());
                areaReportVo.setAreaDes(areaModelVo.getDes());
                areaReportVo.setAreaId(areaModelVo.getAreaId());
                reportList.add(areaReportVo);
            }
            return reportList;
        } catch (FrameworkRuntimeException e) {
            logger.error(LogMsg.to(e));
            throw e;
        } catch (DataAccessException e) {
            logger.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        } catch (Throwable e) {
            logger.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.BASE_ERROR, e.getMessage(), e);
        }
    }

    @Override
    public List<RegionReportVo> getRegionRealTimeReport(Date startDate, Date endDate, Set<String> ctimes,
                                                        String operEid) {
        try {
            Integer isDel = RootModel.NOT_DEL;
            // 参数转换
            ReportDto reportDto = new ReportDto();
            reportDto.setStartDate(startDate);
            reportDto.setEndDate(endDate);
            reportDto.setCtimes(ctimes);
            reportDto.setIsDel(isDel);
            reportDto.setEnterpriseId(operEid);
            List<RegionReportVo> list = iReportDao.getRegionReportPage(reportDto);
            for (RegionReportVo regionReportVo : list) {
                List<String> deviceIds = new ArrayList<>();
                deviceIds.add(regionReportVo.getRegionId());
                reportDto.setDeviceIds(deviceIds);
                List<ReportVo> reportVoList = iReportDao.queryDeviceDateLogs(reportDto);
                regionReportVo.setDeviceList(reportVoList);
            }
            return list;
        } catch (FrameworkRuntimeException e) {
            logger.error(LogMsg.to(e));
            throw e;
        } catch (DataAccessException e) {
            logger.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        } catch (Throwable e) {
            logger.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.BASE_ERROR, e.getMessage(), e);
        }
    }

    @Override
    public List<AreaReportVo> getAreaRealTimeReport(Date startDate, Date endDate, Set<String> ctimes, String operEid) {
        try {
            Integer isDel = RootModel.NOT_DEL;
            // 参数转换
            ReportDto reportDto = new ReportDto();
            reportDto.setStartDate(startDate);
            reportDto.setEndDate(endDate);
            reportDto.setCtimes(ctimes);
            reportDto.setIsDel(isDel);
            reportDto.setEnterpriseId(operEid);
            List<AreaReportVo> list = iReportDao.getAreaReportPage(reportDto);
            for (AreaReportVo areaReportVo : list) {
                List<String> deviceIds = new ArrayList<>();
                deviceIds.add(areaReportVo.getAreaId());
                reportDto.setDeviceIds(deviceIds);
                List<ReportVo> reportVoList = iReportDao.queryDeviceDateLogs(reportDto);
                areaReportVo.setDeviceList(reportVoList);
            }
            return list;
        } catch (FrameworkRuntimeException e) {
            logger.error(LogMsg.to(e));
            throw e;
        } catch (DataAccessException e) {
            logger.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        } catch (Throwable e) {
            logger.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.BASE_ERROR, e.getMessage(), e);
        }
    }
}
