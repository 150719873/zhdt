package com.dotop.smartwater.project.third.server.meterread.api.impl;

import com.dotop.smartwater.project.third.server.meterread.api.IWaterFactory;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.log.LogMsg;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.dependence.core.utils.BeanUtils;
import com.dotop.smartwater.project.module.core.auth.bo.EnterpriseBo;
import com.dotop.smartwater.project.module.core.auth.form.EnterpriseForm;
import com.dotop.smartwater.project.module.core.auth.vo.EnterpriseVo;
import com.dotop.smartwater.project.module.core.water.bo.DeviceUplinkBo;
import com.dotop.smartwater.project.module.core.water.bo.OwnerBo;
import com.dotop.smartwater.project.module.core.water.constants.ResultCode;
import com.dotop.smartwater.project.module.core.water.form.DeviceUplinkForm;
import com.dotop.smartwater.project.module.core.water.form.OwnerForm;
import com.dotop.smartwater.project.module.core.water.vo.DeviceUplinkVo;
import com.dotop.smartwater.project.module.core.water.vo.OwnerVo;
import com.dotop.smartwater.project.third.server.meterread.service.water.IWaterDeviceService;
import com.dotop.smartwater.project.third.server.meterread.service.water.IWaterDeviceUplinkService;
import com.dotop.smartwater.project.third.server.meterread.service.water.IWaterOwnerService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 业主、上行数据更新定时任务
 *
 */
@Service
public class WaterFactoryImpl implements IWaterFactory {

    private static final Logger LOGGER = LogManager.getLogger(WaterFactoryImpl.class);

    @Autowired
    IWaterDeviceUplinkService iWaterDeviceUplinkService;
    @Autowired
    IWaterDeviceService iWaterDeviceService;
    @Autowired
    private IWaterOwnerService waterOwnerService;

    /**
     * 查询最新抄表数据并分页
     *
     * @param deviceUplinkForm
     * @return
     */
    @Override
    public Pagination<DeviceUplinkVo> page(DeviceUplinkForm deviceUplinkForm) {

        DeviceUplinkBo deviceUplinkBo = BeanUtils.copy(deviceUplinkForm, DeviceUplinkBo.class);
        //查询上行数据
        return iWaterDeviceUplinkService.page(deviceUplinkBo);
    }

    @Override
    public Pagination<OwnerVo> getOwnerPage(OwnerForm ownerForm) throws FrameworkRuntimeException {
        OwnerBo ownerBo = new OwnerBo();
        ownerBo.setEnterpriseid(ownerForm.getEnterpriseid());
        ownerBo.setPage(ownerForm.getPage());
        ownerBo.setPageCount(ownerForm.getPageCount());
        Pagination<OwnerVo> ownerPage = waterOwnerService.page(ownerBo);
        return ownerPage;
    }

    /**
     * 查询企业id是否存在
     *
     * @param enterpriseForm
     * @return
     * @throws FrameworkRuntimeException
     */
    @Override
    public String checkEnterpriseId(EnterpriseForm enterpriseForm) throws FrameworkRuntimeException {
        try {
            EnterpriseBo enterpriseBo = BeanUtils.copy(enterpriseForm, EnterpriseBo.class);
            EnterpriseVo enterpriseVo = iWaterDeviceService.checkEnterpriseId(enterpriseBo);
            if (enterpriseVo == null) {
                return ResultCode.Fail;
            } else {
                return ResultCode.Success;
            }
        } catch (FrameworkRuntimeException e) {
            LOGGER.error(LogMsg.to(e));
            throw e;
        }
    }
}
