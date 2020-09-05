package com.dotop.smartwater.view.server.service.device.impl;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dotop.smartwater.view.server.service.device.IWaterMeterService;
import com.dotop.pipe.core.bo.device.DeviceBo;
import com.dotop.pipe.core.dto.decive.DeviceDto;
import com.dotop.pipe.core.vo.brustpipe.BrustPipeVo;
import com.dotop.pipe.core.vo.device.DevicePropertyVo;
import com.dotop.pipe.core.vo.workorder.WorkOrderVo;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.utils.BeanUtils;
import com.dotop.smartwater.view.server.dao.pipe.brustpipe.IBrustPipeOperationsDao;
import com.dotop.smartwater.view.server.dao.pipe.device.IWaterMeterDao;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class WaterMeterServiceImpl implements IWaterMeterService {
    private final static Logger logger = LogManager.getLogger(WaterMeterServiceImpl.class);

    @Autowired
    private IWaterMeterDao waterMeterDao;

    @Autowired
    private IBrustPipeOperationsDao iBrustPipeOperationsDao;

    @Override
    public DevicePropertyVo waterMeterData(DeviceBo deviceBo) throws FrameworkRuntimeException {
        DeviceDto deviceDto = BeanUtils.copy(deviceBo, DeviceDto.class);
        return waterMeterDao.waterMeterData(deviceDto);
    }

    @Override
    public List<String> brustPipeUnHandler(DeviceBo deviceBo) throws FrameworkRuntimeException {
        DeviceDto deviceDto = BeanUtils.copy(deviceBo, DeviceDto.class);
        List<WorkOrderVo> workOrderList = waterMeterDao.workOrderList(deviceDto);
        List<String> brustPipelist = new ArrayList<>();
        for (WorkOrderVo workOrderVo : workOrderList) {
            JSONObject parseObject = JSON.parseObject(workOrderVo.getRecordData());
            String brustPipeId = parseObject.getString("id");
            brustPipelist.add(brustPipeId);
        }
        List<String> ids = new ArrayList<>();
        if (!brustPipelist.isEmpty()) {
           List<BrustPipeVo> listBrustPipe = iBrustPipeOperationsDao.getBrustPipeByIdList(brustPipelist, deviceDto.getEnterpriseId());
           if (!listBrustPipe.isEmpty()) {
               for (BrustPipeVo brustPipeVo : listBrustPipe) {
                   List<String> deviceIds = brustPipeVo.getDeviceIds();
                   for (String deviceId : deviceIds) {
                       ids.add(deviceId);
                   }
               }
           }
        }
        return ids;
    }
}
