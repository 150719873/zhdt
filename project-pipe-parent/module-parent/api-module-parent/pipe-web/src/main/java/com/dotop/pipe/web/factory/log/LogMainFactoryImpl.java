package com.dotop.pipe.web.factory.log;

import com.dotop.pipe.web.api.factory.point.IPointFactory;
import com.dotop.pipe.api.service.device.IDeviceService;
import com.dotop.pipe.api.service.log.ILogDeviceService;
import com.dotop.pipe.api.service.log.ILogMainService;
import com.dotop.pipe.api.service.log.ILogPointMapService;
import com.dotop.pipe.api.service.log.ILogPointService;
import com.dotop.pipe.api.service.point.IPointMapService;
import com.dotop.pipe.auth.cas.web.IAuthCasWeb;
import com.dotop.pipe.auth.core.vo.auth.LoginCas;
import com.dotop.pipe.core.bo.device.DeviceBo;
import com.dotop.pipe.core.bo.log.LogDeviceBo;
import com.dotop.pipe.core.bo.log.LogMainBo;
import com.dotop.pipe.core.bo.log.LogPointBo;
import com.dotop.pipe.core.bo.log.LogPointMapBo;
import com.dotop.pipe.core.dto.point.PointMapDto;
import com.dotop.pipe.core.form.LogMainForm;
import com.dotop.pipe.core.form.PointForm;
import com.dotop.pipe.core.vo.device.DeviceVo;
import com.dotop.pipe.core.vo.log.LogMainVo;
import com.dotop.pipe.core.vo.point.PointMapVo;
import com.dotop.pipe.core.vo.point.PointVo;
import com.dotop.pipe.web.api.factory.log.ILogDeviceFactory;
import com.dotop.pipe.web.api.factory.log.ILogMainFactory;
import com.dotop.smartwater.dependence.core.exception.BaseExceptionConstants;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.dependence.core.utils.BeanUtils;
import com.dotop.smartwater.dependence.core.utils.UuidUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 */
@Component
public class LogMainFactoryImpl implements ILogMainFactory {

    private final static Logger logger = LogManager.getLogger(LogMainFactoryImpl.class);

    @Autowired
    ILogMainService iLogMainService;
    @Autowired
    private IAuthCasWeb iAuthCasApi;
    @Autowired
    ILogDeviceFactory iLogDeviceFactory;
    @Autowired
    ILogDeviceService iLogDeviceService;
    @Autowired
    IDeviceService iDeviceService;
    @Autowired
    IPointFactory iPointFactory;
    @Autowired
    ILogPointService iLogPointService;
    @Autowired
    IPointMapService iPointMapService;
    @Autowired
    ILogPointMapService iLogPointMapService;

    @Override
    public Pagination<LogMainVo> page(LogMainForm logMainForm) throws FrameworkRuntimeException {
        LogMainBo logMainBo = BeanUtils.copy(logMainForm, LogMainBo.class);
        LoginCas loginCas = iAuthCasApi.get();
        logMainBo.setEnterpriseId(loginCas.getEnterpriseId());
        return iLogMainService.page(logMainBo);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
    public LogMainVo add(LogMainForm logMainForm) throws FrameworkRuntimeException {
        try {
            Date curr = new Date();
            LoginCas loginCas = iAuthCasApi.get();
            // 新建版控
            LogMainBo logMainBo = BeanUtils.copy(logMainForm, LogMainBo.class);
            logMainBo.setId(UuidUtils.getUuid());
            logMainBo.setCurr(curr);
            logMainBo.setIsDel(0);
            logMainBo.setIsShow(0);
            logMainBo.setEnterpriseId(loginCas.getEnterpriseId());
            logMainBo.setUserBy(loginCas.getUserName());
            // 查询是否有重复编号
            LogMainBo logMainBo1 = new LogMainBo();
            logMainBo1.setEnterpriseId(loginCas.getEnterpriseId());
            logMainBo1.setCode(logMainBo.getCode());
            LogMainVo logMainVo1 = iLogMainService.get(logMainBo1);
            if (logMainVo1 != null) {
                throw new FrameworkRuntimeException(BaseExceptionConstants.DUPLICATE_KEY_ERROR, "编号重复");
            }
            // 获取该企业最大版本号并加1
            logMainBo.setVersion(iLogMainService.getMaxVersion(logMainBo) + 1);
            LogMainVo logMainVo = iLogMainService.add(logMainBo);
            // 迁移所有设备
            DeviceBo deviceBo = new DeviceBo();
            deviceBo.setEnterpriseId(logMainBo.getEnterpriseId());
            List<DeviceVo> deviceVos = iDeviceService.list(deviceBo);
            Map<String, DeviceVo> deviceVoMap = deviceVos.stream().collect(Collectors.toMap(DeviceVo::getDeviceId, t -> t));
            List<LogDeviceBo> logDeviceBos = BeanUtils.copy(deviceVos, LogDeviceBo.class);
            logDeviceBos.forEach(t -> {
                t.setLogMainId(logMainBo.getId());
                t.setId(UuidUtils.getUuid());
                t.setCurr(curr);
                t.setUserBy(logMainBo.getUserBy());
                t.setIsDel(0);
                t.setProductType(deviceVoMap.get(t.getDeviceId()).getProductType() == null ? "" : deviceVoMap.get(t.getDeviceId()).getProductType());
                t.setProductCategory(deviceVoMap.get(t.getDeviceId()).getProductCategory() == null ? "" : deviceVoMap.get(t.getDeviceId()).getProductCategory());
                t.setProductId(deviceVoMap.get(t.getDeviceId()).getProduct() == null ? "" : deviceVoMap.get(t.getDeviceId()).getProduct().getProductId());
                t.setLaying(deviceVoMap.get(t.getDeviceId()).getLaying() == null ? "" : deviceVoMap.get(t.getDeviceId()).getLaying().getId());
                t.setAreaId(deviceVoMap.get(t.getDeviceId()).getArea() == null ? "" : deviceVoMap.get(t.getDeviceId()).getArea().getAreaId());
            });
            iLogDeviceService.adds(logDeviceBos);
            // 迁移point表
            PointForm pointForm = new PointForm();
            pointForm.setEnterpriseId(logMainBo.getEnterpriseId());
            List<PointVo> pointVos = iPointFactory.realList(pointForm);
            List<LogPointBo> logPointBos = BeanUtils.copy(pointVos, LogPointBo.class);
            logPointBos.forEach(t -> {
                t.setLogMainId(logMainBo.getId());
                t.setId(UuidUtils.getUuid());
                t.setUserBy(logMainBo.getUserBy());
                t.setCurr(curr);
                t.setEnterpriseId(logMainBo.getEnterpriseId());
                t.setIsDel(0);
            });
            iLogPointService.adds(logPointBos);
            // 签名pointMap表
            PointMapDto pointMapDto = new PointMapDto();
            pointMapDto.setEnterpriseId(logMainBo.getEnterpriseId());
            List<PointMapVo> pointMapVos = iPointMapService.list(pointMapDto);
            List<LogPointMapBo> logPointMapBos = BeanUtils.copy(pointMapVos, LogPointMapBo.class);
            logPointMapBos.forEach(t -> {
                t.setId(UuidUtils.getUuid());
                t.setUserBy(logMainBo.getUserBy());
                t.setCurr(curr);
                t.setLogMainId(logMainBo.getId());
                t.setEnterpriseId(logMainBo.getEnterpriseId());
                t.setIsDel(0);
            });
            iLogPointMapService.adds(logPointMapBos);
            return logMainVo;
        } catch (Exception e) {
            logger.error(e);
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    @Override
    public LogMainVo get(LogMainForm logMainForm) throws FrameworkRuntimeException {
        LogMainBo logMainBo = BeanUtils.copy(logMainForm, LogMainBo.class);
        LoginCas loginCas = iAuthCasApi.get();
        logMainBo.setEnterpriseId(loginCas.getEnterpriseId());
        return iLogMainService.get(logMainBo);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
    public LogMainVo edit(LogMainForm logMainForm) throws FrameworkRuntimeException {
        LogMainBo logMainBo = BeanUtils.copy(logMainForm, LogMainBo.class);
        LoginCas loginCas = iAuthCasApi.get();
        LogMainBo logMainBo1 = new LogMainBo();
        logMainBo1.setEnterpriseId(loginCas.getEnterpriseId());
        logMainBo1.setCode(logMainBo.getCode());
        LogMainVo logMainVo1 = iLogMainService.get(logMainBo1);
        if (logMainVo1 != null && !logMainVo1.getId().equals(logMainBo.getId())) {
            throw new FrameworkRuntimeException(BaseExceptionConstants.DUPLICATE_KEY_ERROR, "编号重复");
        }
        return iLogMainService.edit(logMainBo);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
    public String del(LogMainForm logMainForm) throws FrameworkRuntimeException {
        try {
            LogMainBo logMainBo = BeanUtils.copy(logMainForm, LogMainBo.class);
            LogPointBo logPointBo = new LogPointBo();
            logPointBo.setEnterpriseId(logMainBo.getEnterpriseId());
            logPointBo.setLogMainId(logMainBo.getId());
            iLogPointService.dels(logPointBo);
            LogPointMapBo logPointMapBo = new LogPointMapBo();
            logPointMapBo.setEnterpriseId(logMainBo.getEnterpriseId());
            logPointMapBo.setLogMainId(logMainBo.getId());
            iLogPointMapService.dels(logPointMapBo);
            LogDeviceBo logDeviceBo = new LogDeviceBo();
            logDeviceBo.setEnterpriseId(logMainBo.getEnterpriseId());
            logDeviceBo.setLogMainId(logMainBo.getId());
            iLogDeviceService.dels(logDeviceBo);
            return iLogMainService.del(logMainBo);
        } catch (Exception e) {
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    @Override
    public Integer getMaxVersion(LogMainForm logMainForm) throws FrameworkRuntimeException {
        LogMainBo logMainBo = BeanUtils.copy(logMainForm, LogMainBo.class);
        LoginCas loginCas = iAuthCasApi.get();
        logMainBo.setEnterpriseId(loginCas.getEnterpriseId());
        return iLogMainService.getMaxVersion(logMainBo);
    }
}
