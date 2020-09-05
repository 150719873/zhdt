package com.dotop.pipe.web.factory.rundata;

import com.dotop.pipe.web.api.factory.runingdata.IRuningDataFactory;
import com.dotop.pipe.api.service.runingdata.IRuningDataService;
import com.dotop.pipe.auth.cas.web.IAuthCasWeb;
import com.dotop.pipe.auth.core.vo.auth.LoginCas;
import com.dotop.pipe.core.bo.runingdata.RuningDataBo;
import com.dotop.pipe.core.exception.PipeExceptionConstants;
import com.dotop.pipe.core.form.RuningDataForm;
import com.dotop.pipe.core.vo.runingdata.RuningDataVo;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.log.LogMsg;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.dependence.core.utils.BeanUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
public class RunDataFactoryImpl implements IRuningDataFactory {
    private final static Logger logger = LogManager.getLogger(RunDataFactoryImpl.class);

    @Autowired
    private IRuningDataService iRuningDataService;

    @Autowired
    private IAuthCasWeb iAuthCasApi;

    @Override
    public RuningDataVo add(RuningDataForm runingDataForm) throws FrameworkRuntimeException {
        Date curr = new Date();
        // 转换deviceIds
        StringBuffer stringBuffer = new StringBuffer();
        LoginCas loginCas = iAuthCasApi.get();
        String operEid = loginCas.getOperEid();
        String userBy = loginCas.getUserName();
        RuningDataBo runingDataBo = BeanUtils.copyProperties(runingDataForm, RuningDataBo.class);
        runingDataBo.setEnterpriseId(operEid);
        runingDataBo.setCurr(curr);
        runingDataBo.setUserBy(userBy);
        List<RuningDataVo> list = iRuningDataService.getRuningTask(runingDataBo);
        if (list != null && list.size() > 10) {
            // 定时任务太多 允许
            logger.error(LogMsg.to("ex", PipeExceptionConstants.DEVICE_CODE_EXIST, "msg",
                    "定时任务数量太多影响程序运行，暂不允许新增"));
            throw new FrameworkRuntimeException(PipeExceptionConstants.DEVICE_CODE_EXIST,
                    "定时任务数量太多影响程序运行，暂不允许新增");
        }
        if (runingDataForm.getDeviceIds() != null && !runingDataForm.getDeviceIds().isEmpty()) {
            stringBuffer.append("(");
            for (String deviceId : runingDataForm.getDeviceIds()) {
                stringBuffer.append("'").append(deviceId).append("',");
                if (list != null) {
                    for (RuningDataVo runingDataVo : list) {
                        if (runingDataVo.getDeviceIdStr().indexOf(deviceId) != -1) {
                            // 此设备被执行中 抛出异常
                            logger.error(LogMsg.to("ex", PipeExceptionConstants.DEVICE_CODE_EXIST, "msg",
                                    "选择的设备已在定时任务中存在，为不影响程序运行,请更换设备"));
                            throw new FrameworkRuntimeException(PipeExceptionConstants.DEVICE_CODE_EXIST,
                                    "选择的设备已在定时任务中存在，为不影响程序运行,请更换设备");
                        }
                    }
                }
            }
            // 完成sql in的数据数据格式
            stringBuffer.append("'0000000' )");
            runingDataBo.setDeviceIdStr(stringBuffer.toString());
        } else {
            runingDataBo.setDeviceIdStr(null);
        }
        // 判断是否有设备已存在


        RuningDataVo runingDataVo = iRuningDataService.add(runingDataBo);
        return runingDataVo;
    }

    @Override
    public Pagination<RuningDataVo> page(RuningDataForm runingDataForm) throws FrameworkRuntimeException {
        LoginCas loginCas = iAuthCasApi.get();
        String operEid = loginCas.getOperEid();
        // 封装参数
        RuningDataBo runingDataBo = BeanUtils.copyProperties(runingDataForm, RuningDataBo.class);
        runingDataBo.setEnterpriseId(operEid);
        Pagination<RuningDataVo> pagination = iRuningDataService.page(runingDataBo);
        return pagination;
    }

    @Override
    public void makeData(RuningDataForm runingDataForm) throws FrameworkRuntimeException {
        synchronized (this) {
            // 子线程不能获取到当前的企业id
            String operEid = runingDataForm.getEnterpriseId();
            RuningDataBo runingDataBo = BeanUtils.copyProperties(runingDataForm, RuningDataBo.class);
            runingDataBo.setEnterpriseId(operEid);
            // 转换deviceIds
//        StringBuffer stringBuffer = new StringBuffer();
//        if (runingDataForm.getDeviceIds() != null && !runingDataForm.getDeviceIds().isEmpty()) {
//            stringBuffer.append("(");
//            for (String deviceId : runingDataForm.getDeviceIds()) {
//                stringBuffer.append("'").append(deviceId).append("',");
//            }
//            // 完成sql in的数据数据格式
//            stringBuffer.append("'0000000' )");
//            runingDataBo.setDeviceIdStr(stringBuffer.toString());
//        } else {
//            runingDataBo.setDeviceIdStr(null);
//        }
            iRuningDataService.makeData(runingDataBo);
        }
    }

    @Override
    public String del(RuningDataForm runingDataForm) throws FrameworkRuntimeException {
        RuningDataBo runingDataBo = BeanUtils.copyProperties(runingDataForm, RuningDataBo.class);
        return iRuningDataService.del(runingDataBo);
    }

    @Override
    public List<RuningDataVo> getRuningTask(RuningDataForm runingDataForm) throws FrameworkRuntimeException {
        RuningDataBo runingDataBo = BeanUtils.copyProperties(runingDataForm, RuningDataBo.class);
        return iRuningDataService.getRuningTask(runingDataBo);
    }

    @Override
    public RuningDataVo edit(RuningDataForm runingDataForm) throws FrameworkRuntimeException {
        RuningDataBo runingDataBo = BeanUtils.copyProperties(runingDataForm, RuningDataBo.class);
        return iRuningDataService.edit(runingDataBo);
    }
}
