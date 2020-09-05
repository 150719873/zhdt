package com.dotop.smartwater.project.third.server.meterread.service.water.impl;

import com.dotop.smartwater.dependence.core.exception.BaseExceptionConstants;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.log.LogMsg;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.dependence.core.utils.BeanUtils;
import com.dotop.smartwater.project.module.core.water.bo.DeviceUplinkBo;
import com.dotop.smartwater.project.module.core.water.dto.DeviceUplinkDto;
import com.dotop.smartwater.project.module.core.water.vo.DeviceUplinkVo;
import com.dotop.smartwater.project.module.core.water.vo.DeviceVo;
import com.dotop.smartwater.project.third.server.meterread.api.impl.WaterFactoryImpl;
import com.dotop.smartwater.project.third.server.meterread.dao.water.IWaterDao;
import com.dotop.smartwater.project.third.server.meterread.service.water.IWaterDeviceUplinkService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
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

    private static final Logger LOGGER = LogManager.getLogger(WaterFactoryImpl.class);

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
            String thisMonth = sdf.format(calendar.getTime());
            calendar.add(Calendar.MONTH, -1);
            String preMonth = sdf.format(calendar.getTime());
            String[] deveuis = new String[deviceVos.size()];
            for (int i = 0; i < deviceVos.size(); i++) {
                deveuis[i] = deviceVos.get(i).getDeveui();
            }
            list = iWaterDao.getUplinkData(deveuis, preMonth, thisMonth);
            return list;
        } catch (DataAccessException e) {
            LOGGER.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    /**
     * 查询最新抄表数据并分页
     *
     * @param deviceUplinkBo
     * @return
     * @throws FrameworkRuntimeException
     */
    @Override
    public Pagination<DeviceUplinkVo> page(DeviceUplinkBo deviceUplinkBo) throws FrameworkRuntimeException {
        try {
            // 参数转换
            DeviceUplinkDto deviceUplinkDto = BeanUtils.copy(deviceUplinkBo, DeviceUplinkDto.class);

            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM", Locale.CANADA);
            Calendar calendar = Calendar.getInstance();
            String thisMonth = sdf.format(calendar.getTime());
            calendar.add(Calendar.MONTH, -1);
            String preMonth = sdf.format(calendar.getTime());
            // 操作数据
            Page<Object> pageHelper = PageHelper.startPage(deviceUplinkBo.getPage(), deviceUplinkBo.getPageCount());
            List<DeviceUplinkVo> list = iWaterDao.getUplinkList(deviceUplinkDto, preMonth, thisMonth);
            // 拼接数据返回
            return new Pagination<>(deviceUplinkBo.getPage(), deviceUplinkBo.getPageCount(), list, pageHelper.getTotal());
        } catch (DataAccessException e) {
            LOGGER.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }
}
