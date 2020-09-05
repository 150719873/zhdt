package com.dotop.pipe.service.alarm;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.dotop.pipe.api.service.alarm.IAlarmNoticeRuleService;
import com.dotop.pipe.api.service.device.IDeviceUpDownStreamService;
import com.dotop.pipe.core.bo.alarm.AlarmNoticeBo;
import com.dotop.pipe.core.bo.device.DeviceUpDownStreamBo;
import com.dotop.pipe.core.vo.common.DictionaryVo;
import com.dotop.pipe.core.vo.device.DeviceVo;
import com.dotop.pipe.core.vo.product.ProductVo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.dotop.pipe.api.client.IWaterNoticeClient;
import com.dotop.pipe.api.dao.alarm.IAlarmDao;
import com.dotop.pipe.api.dao.alarm.IAlarmSettingDao;
import com.dotop.pipe.api.dao.common.ICommonDao;
import com.dotop.pipe.api.service.alarm.IAlarmService;
import com.dotop.pipe.core.bo.alarm.AlarmBo;
import com.dotop.pipe.core.dto.alarm.AlarmDto;
import com.dotop.pipe.core.dto.alarm.AlarmSettingDto;
import com.dotop.pipe.core.utils.AlarmCalulationUtils;
import com.dotop.pipe.core.vo.alarm.AlarmNoticeRuleVo;
import com.dotop.pipe.core.vo.alarm.AlarmSettingVo;
import com.dotop.pipe.core.vo.alarm.AlarmVo;
import com.dotop.smartwater.dependence.core.common.RootModel;
import com.dotop.smartwater.dependence.core.exception.BaseExceptionConstants;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.log.LogMsg;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.dependence.core.utils.BeanUtils;
import com.dotop.smartwater.dependence.core.utils.UuidUtils;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

@Service
public class AlarmServiceImpl implements IAlarmService {

    private final static Logger logger = LogManager.getLogger(AlarmServiceImpl.class);

    @Autowired
    private IAlarmDao iAlarmDao;

    @Autowired
    private IAlarmSettingDao iAlarmSettingDao;

    @Autowired
    private ICommonDao iCommonDao;

    @Autowired
    private IWaterNoticeClient iWaterNoticeClient;

    @Autowired
    private IAlarmNoticeRuleService iAlarmNoticeRuleService;

    @Autowired
    private IDeviceUpDownStreamService iDeviceUpDownStreamService;


    @Override
    public Pagination<AlarmVo> page(AlarmBo alarmBo) throws FrameworkRuntimeException {
        try {
            Integer isDel = RootModel.NOT_DEL;
            AlarmDto alarmDto = BeanUtils.copyProperties(alarmBo, AlarmDto.class);
            alarmDto.setIsDel(isDel);
            Page<Object> pageHelper = PageHelper.startPage(alarmBo.getPage(), alarmBo.getPageSize());
            List<AlarmVo> list = iAlarmDao.list(alarmDto);
            Pagination<AlarmVo> pagination = new Pagination<AlarmVo>(alarmBo.getPageSize(), alarmBo.getPage());
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
    public Pagination<AlarmVo> areaPage(AlarmBo alarmBo) throws FrameworkRuntimeException {
        try {
            Integer isDel = RootModel.NOT_DEL;
            AlarmDto alarmDto = BeanUtils.copyProperties(alarmBo, AlarmDto.class);
            alarmDto.setIsDel(isDel);
            // 封装产品
            ProductVo productVo = new ProductVo();
            DictionaryVo category = new DictionaryVo();
            category.setVal("area");
            productVo.setCategory(category);
            DictionaryVo type = new DictionaryVo();
            type.setVal("area");
            productVo.setType(type);
            Page<Object> pageHelper = PageHelper.startPage(alarmBo.getPage(), alarmBo.getPageSize());
            List<AlarmVo> list = iAlarmDao.areaList(alarmDto);
            if (list != null) {
                for (AlarmVo alarmVo : list) {
                    alarmVo.getDevice().setProduct(productVo);
                }
            }
            Pagination<AlarmVo> pagination = new Pagination<AlarmVo>(alarmBo.getPageSize(), alarmBo.getPage());
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
    public AlarmVo get(AlarmBo alarmBo) throws FrameworkRuntimeException {
        try {
            Integer isDel = RootModel.NOT_DEL;
            AlarmDto alarmDto = BeanUtils.copyProperties(alarmBo, AlarmDto.class);
            alarmDto.setIsDel(isDel);
            if ("area".equals(alarmBo.getProductCategory())) {
                AlarmVo alarmVo = iAlarmDao.getAreaAlarm(alarmDto);
                return alarmVo;
            } else {
                AlarmVo alarmVo = iAlarmDao.get(alarmDto);
                return alarmVo;
            }
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
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
    public int edit(AlarmBo alarmBo) throws FrameworkRuntimeException {
        try {
            Integer isDel = RootModel.NOT_DEL;
            AlarmDto alarmDto = BeanUtils.copyProperties(alarmBo, AlarmDto.class);
            alarmDto.setIsDel(isDel);
            int count = iAlarmDao.edit(alarmDto);
            return count;
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
    // @Transactional(propagation = Propagation.REQUIRED, rollbackFor =
    // FrameworkRuntimeException.class)
    // 2018-12-28 导入不加事务，如需事务，需要考虑重构
    public String add(AlarmBo alarmBo) throws FrameworkRuntimeException {
        try {
            String alarmId = UuidUtils.getUuid();
            Integer isDel = RootModel.NOT_DEL;
            AlarmDto alarmDto = BeanUtils.copyProperties(alarmBo, AlarmDto.class);
            alarmDto.setAlarmId(alarmId);
            alarmDto.setIsDel(isDel);
            iAlarmDao.add(alarmDto);
            return alarmId;
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
    public Pagination<AlarmVo> listByDevice(String operEid, Integer page, Integer pageSize, String status)
            throws FrameworkRuntimeException {
        try {
            Integer isDel = RootModel.NOT_DEL;
            AlarmDto alarmDto = new AlarmDto();
            alarmDto.setEnterpriseId(operEid);
            alarmDto.setIsDel(isDel);
            if (!"all".equals(status)) {
                alarmDto.setStatus(Integer.parseInt(status));
            }
            Page<Object> pageHelper = PageHelper.startPage(page, pageSize);
            List<AlarmVo> list = iAlarmDao.listByDevice(alarmDto);

            Pagination<AlarmVo> pagination = new Pagination<AlarmVo>(pageSize, page);
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
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
    public String add(String deviceId, String enterpriseId, Map<String, Object> devicePropertyLogMap) {

        // 处理设备类型
        String productTypeStr = "设备";
        switch (devicePropertyLogMap.get("productType").toString()) {
            case "fm":
                productTypeStr = "流量计";
                break;
            case "wm":
                productTypeStr = "水质计";
                break;
            case "pm":
                productTypeStr = "压力计";
                break;
            case "water_factory":
                productTypeStr = "水厂";
                break;
            case "area":
                productTypeStr = "区域";
                break;
            case "region":
                productTypeStr = "片区";
                break;
            default:
                productTypeStr = "设备";
                break;
        }
        ;


        /**
         * 1 结合预警设置 查找设置规则 2 增加预警信息 pls_alarm 3
         */

        /*if ("a166c481-c920-42fe-9204-9557d9d03a93".equals(deviceId)) {
            System.out.println("a166c481-c920-42fe-9204-9557d9d03a93");
        }*/
        AlarmSettingDto alarmSettingBo = new AlarmSettingDto();
        alarmSettingBo.setDeviceId(deviceId);
        alarmSettingBo.setEnterpriseId(enterpriseId);
        List<AlarmSettingVo> alarmSettingVoList = iAlarmSettingDao.gets(alarmSettingBo);

        if (alarmSettingVoList.isEmpty()) {
            // 优先级  设备设置的预警 高于 预测的预警值
            // 查找下游的预测预警范围值 deviceId  如果此设备已经设置了预警值  则不执行此
            // 根据deviceId 查询此水表的预警值 即上游的数据 * 百分比
            DeviceUpDownStreamBo deviceUpDownStreamBo = new DeviceUpDownStreamBo();
            deviceUpDownStreamBo.setDeviceId(deviceId);
            deviceUpDownStreamBo.setEnterpriseId(enterpriseId);
            DeviceVo deviceVo = iDeviceUpDownStreamService.getForecast(deviceUpDownStreamBo);
            if (deviceVo == null) {
                return null;
            }
            // 将查询的设备属性 转化成 alarmSettingVo 格式
            alarmSettingVoList = AlarmCalulationUtils.alarmChangeToSettingList(deviceVo);
            if (alarmSettingVoList.isEmpty()) {
                return null;
            }
        }

        StringBuffer stringbuf = new StringBuffer();
        // 查询当前设备 设置的预警信息
        for (AlarmSettingVo alarmSettingVo : alarmSettingVoList) {
            if (devicePropertyLogMap.containsKey(alarmSettingVo.getTag())) {
                Object obj = devicePropertyLogMap.get(alarmSettingVo.getTag());
                if (obj == null) {
                    continue;
                }
                String val = obj.toString();
                boolean flag = AlarmCalulationUtils.alarmCalulation(alarmSettingVo, val);
                if (flag) {
                    // 拼接此次报警的异常信息
                    stringbuf.append(alarmSettingVo.getDes()).append(":").append(val).append(",");
                }
            }
        }


        if (stringbuf.length() > 0) {
            stringbuf.append("预警时间").append(":").append(devicePropertyLogMap.get("dev_send_date")).append(";");
            // add(stringbuf.toString(), stringbuf.toString(), deviceId, 0, enterpriseId,
            // "system");
            Date createDate = new Date();
            // 先判断这个设备是否存在未处理的预警信息
            AlarmBo alarmBo = new AlarmBo();
            alarmBo.setEnterpriseId(enterpriseId);
            alarmBo.setDeviceId(deviceId);
            alarmBo.setStatus(0);
            if ("区域".equals(productTypeStr)) {
                alarmBo.setProductCategory("area");
            }
            AlarmVo alarmVo = this.get(alarmBo);
            alarmBo.setUserBy("system");
            alarmBo.setCurr(createDate);
            if (alarmVo != null) {
                // 存在未处理的信息 则修改原数据
                alarmBo.setAlarmId(alarmVo.getAlarmId());
                Integer alarmCount = Integer.valueOf(alarmVo.getAlarmCount()) + 1;
                alarmBo.setAlarmCount(alarmCount);
                alarmBo.setName(productTypeStr + "(" + devicePropertyLogMap.get("device_code").toString() + ") 数据异常");
                // alarmBo.setDes(alarmVo.getDes() + stringbuf.toString());
                //alarmBo.setName(stringbuf.toString());
                alarmBo.setDes(stringbuf.toString());
                edit(alarmBo);
                // 发送短信通知
                AlarmNoticeRuleVo alarmNoticeRuleVo = iCommonDao.getALarmNoticeRule(enterpriseId, devicePropertyLogMap.get("device_code").toString());
                if (alarmNoticeRuleVo != null && alarmNoticeRuleVo.getAlarmNum() < alarmCount) {
                    alarmNoticeRuleVo.setEnterpriseId(enterpriseId);
                    // 编辑短信内容
                    iWaterNoticeClient.add(alarmBo, alarmNoticeRuleVo);

                    // 记录日志
                    AlarmNoticeBo alarmNoticeBo = new AlarmNoticeBo();
                    alarmNoticeBo.setDeviceCode(alarmNoticeRuleVo.getDeviceCode());
                    alarmNoticeBo.setDeviceName(alarmNoticeRuleVo.getDeviceName());
                    alarmNoticeBo.setNotifyType(alarmNoticeRuleVo.getNotifyType());
                    alarmNoticeBo.setNotifyUserType(alarmNoticeRuleVo.getNotifyUserType());
                    alarmNoticeBo.setNotifyUser(alarmNoticeRuleVo.getNotifyUser());
                    alarmNoticeBo.setNotifyUserId(alarmNoticeRuleVo.getNotifyUserid());
                    alarmNoticeBo.setCurr(new Date());
                    alarmNoticeBo.setUserBy("admin");
                    alarmNoticeBo.setEnterpriseId(enterpriseId);
                    alarmNoticeBo.setAlarmNum(alarmCount);
                    iAlarmNoticeRuleService.addLog(alarmNoticeBo);
                }
            } else {
                // 新增数据
                Random rand = new Random();
                SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
                String code = df.format(createDate) + String.valueOf(rand.nextInt(900) + 100);
                alarmBo.setCode(code);
                alarmBo.setAlarmCount(1);
                alarmBo.setDes(stringbuf.toString());
                alarmBo.setName(productTypeStr + "(" + devicePropertyLogMap.get("device_code").toString() + ") 数据异常");
                String alarmId = this.add(alarmBo);
            }
        }
        return null;
    }

}
