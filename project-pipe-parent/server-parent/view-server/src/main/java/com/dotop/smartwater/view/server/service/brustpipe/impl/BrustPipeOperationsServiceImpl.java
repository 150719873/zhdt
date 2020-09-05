package com.dotop.smartwater.view.server.service.brustpipe.impl;

import com.alibaba.fastjson.JSONObject;
import com.dotop.pipe.core.bo.brustpipe.BrustPipeBo;
import com.dotop.pipe.core.bo.device.DeviceBo;
import com.dotop.pipe.core.dto.brustpipe.BrustPipeDto;
import com.dotop.pipe.core.dto.decive.DeviceDto;
import com.dotop.pipe.core.vo.brustpipe.BrustPipeVo;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.log.LogMsg;
import com.dotop.smartwater.view.server.core.brustpipe.vo.BrustPipeOperationsVo;
import com.dotop.pipe.core.vo.device.DeviceVo;
import com.dotop.smartwater.view.server.dao.pipe.brustpipe.IBrustPipeOperationsDao;
import com.dotop.smartwater.view.server.service.brustpipe.IBrustPipeOperationsService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.dotop.smartwater.dependence.core.utils.BeanUtils;
import com.alibaba.fastjson.JSON;

import java.util.*;

@Service
public class BrustPipeOperationsServiceImpl implements IBrustPipeOperationsService {
    private final static Logger logger = LogManager.getLogger(BrustPipeOperationsServiceImpl.class);
    @Autowired
    private IBrustPipeOperationsDao iBrustPipeOperationsDao;

    @Override
    public BrustPipeOperationsVo brustPipe(DeviceBo deviceBo) throws FrameworkRuntimeException{

        try {


            DeviceDto deviceDto = BeanUtils.copyProperties(deviceBo, DeviceDto.class);
            List<BrustPipeOperationsVo> brustPipeList = iBrustPipeOperationsDao.getBrustPipe(deviceDto);
            ArrayList<BrustPipeOperationsVo> tempList = new ArrayList<>();
            for (BrustPipeOperationsVo brustPipeOperationsVo : brustPipeList) {
                String brustPipeId = brustPipeOperationsVo.getBrustPipeId();
                BrustPipeOperationsVo brustPipeOperationsTemp = iBrustPipeOperationsDao.getWorkOrderByBrustPipeId(brustPipeId, deviceDto.getEnterpriseId());
                if (brustPipeOperationsTemp != null) {
                    tempList.add(brustPipeOperationsTemp);
                }
            }
            String workOrderstatus = null;
            String workOrderId = null;
            String operationsPeole = null;
            String processId = null;
            if (tempList.size() > 0) {
                for (int i = 0; i < tempList.size() - 1; i++) {
                    for (int j = 1; j < tempList.size() - i; j++) {
                        BrustPipeOperationsVo temp;
                        Integer time1 = Integer.valueOf( (int) tempList.get(j - 1).getWorkOrderCreateDate().getTime());
                        Integer time2 = Integer.valueOf((int) tempList.get(j).getWorkOrderCreateDate().getTime());
                        if (time2.compareTo(time1) > 0) {
                            temp = tempList.get(j - 1);
                            tempList.set((j - 1), tempList.get(j));
                            tempList.set(j, temp);

                        }
                    }
                }
                tempList.sort(Comparator.comparing(BrustPipeOperationsVo::getWorkOrderCreateDate));
                BrustPipeOperationsVo workOrder = tempList.get(tempList.size()-1);
                JSONObject parseObject = JSON.parseObject(workOrder.getRecordData());
                String brustPipeId = parseObject.getString("id");
                BrustPipeOperationsVo fillerTemp = iBrustPipeOperationsDao.getBrustPipeById(brustPipeId, deviceDto.getEnterpriseId());
                workOrderstatus = workOrder.getWorkOrderstatus();
                workOrderId = workOrder.getWorkOrderId();
                processId = workOrder.getProcessId();
                if (fillerTemp != null) {
                    operationsPeole = fillerTemp.getOperationsPeole();
                }
            }


            BrustPipeOperationsVo brustPipeOperationsVo = iBrustPipeOperationsDao.getNumByDeviceId(deviceDto);
            brustPipeOperationsVo.setStatus(workOrderstatus);
            brustPipeOperationsVo.setWorkOrderId(workOrderId);
            brustPipeOperationsVo.setProcessId(processId);
            brustPipeOperationsVo.setOperationsPeole(operationsPeole);

            if (brustPipeOperationsVo.getOperationsNum() > 0) {
                if (deviceBo.getInstallDate() != null) {
                    Date installDate = deviceBo.getInstallDate();
                    long installTime = installDate.getTime();
                    long currTime = System.currentTimeMillis();//得到当前的毫秒
                    int temp = (int) ((currTime - installTime) / 1000 / 60 / 60 / 24);
                    Integer day = temp % 30 > 15 ? temp / 30 + 1 : temp / 30;
                    brustPipeOperationsVo.setOperationsTime(day);
                }
            }
            return brustPipeOperationsVo;


        } catch (Exception e) {
            logger.error(LogMsg.to(e));
            e.printStackTrace();
            throw e;
        }


    }

    @Override
    public List<BrustPipeVo> brustPipeList(BrustPipeBo brustPipeBo) throws FrameworkRuntimeException {
         BrustPipeDto brustPipeDto = BeanUtils.copy(brustPipeBo, BrustPipeDto.class);
        List<BrustPipeVo> list = iBrustPipeOperationsDao.listBrustPipe(brustPipeDto);
        return list;
    }

    @Override
    public List<DeviceVo> listDevice(DeviceBo deviceBo) throws FrameworkRuntimeException {
        DeviceDto deviceDto = BeanUtils.copy(deviceBo, DeviceDto.class);
        return iBrustPipeOperationsDao.listDevice(deviceDto);
    }


}
