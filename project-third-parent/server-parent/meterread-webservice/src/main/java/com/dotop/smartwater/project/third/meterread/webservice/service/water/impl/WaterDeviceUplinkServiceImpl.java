package com.dotop.smartwater.project.third.meterread.webservice.service.water.impl;

import com.dotop.smartwater.project.third.meterread.webservice.service.water.IWaterDeviceUplinkService;
import com.dotop.smartwater.dependence.core.exception.BaseExceptionConstants;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.project.module.core.water.vo.DeviceUplinkVo;
import com.dotop.smartwater.project.module.core.water.vo.DeviceVo;
import com.dotop.smartwater.project.third.meterread.webservice.dao.water.IWaterDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

/**
 * 对接水务系统的设备上行数据数据接口
 *
 *
 */

@Service
public class WaterDeviceUplinkServiceImpl implements IWaterDeviceUplinkService {

    @Autowired
    private IWaterDao iWaterDao;

    /**
     * 根据设备水表号查询最新的用水量和最新上传时间
     *
     * @param deviceVos
     * @return
     * @throws FrameworkRuntimeException
     */
    @Override
    public List<DeviceUplinkVo> list(List<DeviceVo> deviceVos) throws FrameworkRuntimeException {
        try {
            List<DeviceUplinkVo> list;
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM", Locale.CANADA);
            Calendar calendar = Calendar.getInstance();
            // 这个月
            String thisMonth = sdf.format(calendar.getTime());
            // 上个月
            calendar.add(Calendar.MONTH, -1);
            String preMonth = sdf.format(calendar.getTime());
            // 上上个月
            calendar.add(Calendar.MONTH, -1);
            String ppreMonth = sdf.format(calendar.getTime());
            // 上上上个月
            calendar.add(Calendar.MONTH, -1);
            String pppreMonth = sdf.format(calendar.getTime());

            List<String> devids = new ArrayList<>();
            for (int i = 0; i < deviceVos.size(); i++) {
                devids.add(deviceVos.get(i).getDevid());
            }
            list = iWaterDao.getUplinkData(devids, pppreMonth, ppreMonth, preMonth, thisMonth);
            return list;
        } catch (DataAccessException e) {
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }


}
