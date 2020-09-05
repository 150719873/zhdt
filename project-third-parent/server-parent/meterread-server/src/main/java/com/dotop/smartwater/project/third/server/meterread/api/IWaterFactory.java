package com.dotop.smartwater.project.third.server.meterread.api;

import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.project.module.core.auth.form.EnterpriseForm;
import com.dotop.smartwater.project.module.core.water.form.DeviceUplinkForm;
import com.dotop.smartwater.project.module.core.water.form.OwnerForm;
import com.dotop.smartwater.project.module.core.water.vo.DeviceUplinkVo;
import com.dotop.smartwater.project.module.core.water.vo.OwnerVo;

/**
 * 水务系统业务逻辑处理层
 *
 *
 */
public interface IWaterFactory {




    /**
     * 查询最新抄表数据并分页
     * @param deviceUplinkForm
     * @return
     */
    Pagination<DeviceUplinkVo> page(DeviceUplinkForm deviceUplinkForm);

    /**
     * 获取水务库中的业主信息(分页)
     *
     * @throws FrameworkRuntimeException
     * @param ownerForm
     */
    Pagination<OwnerVo> getOwnerPage(OwnerForm ownerForm) throws FrameworkRuntimeException;

    /**
     * 查询企业id是否存在
     * @param enterpriseForm
     * @return
     * @throws FrameworkRuntimeException
     */
    String checkEnterpriseId(EnterpriseForm enterpriseForm) throws FrameworkRuntimeException;

}
