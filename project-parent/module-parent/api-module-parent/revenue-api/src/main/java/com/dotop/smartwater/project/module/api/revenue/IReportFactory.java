package com.dotop.smartwater.project.module.api.revenue;

import java.util.List;

import com.dotop.smartwater.dependence.core.common.BaseFactory;
import com.dotop.smartwater.dependence.core.common.BaseForm;
import com.dotop.smartwater.dependence.core.common.BaseVo;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.project.module.core.water.form.DeviceBatchForm;
import com.dotop.smartwater.project.module.core.water.form.DeviceForm;
import com.dotop.smartwater.project.module.core.water.form.OwnerForm;
import com.dotop.smartwater.project.module.core.water.form.customize.PreviewForm;
import com.dotop.smartwater.project.module.core.water.form.customize.QueryForm;
import com.dotop.smartwater.project.module.core.water.vo.DeviceVo;

/**
 * 报表
 *
 所有导出迁移到这里
 * @date 2019年3月31日
 */
public interface IReportFactory extends BaseFactory<BaseForm, BaseVo> {

    /**
     * 导出 业主报装
     * @param owner
     * @
     */
    String export_owner(OwnerForm owner) ;

    
    /**
     * 导出设备批次信息
     * @param form
     * @return
     */
    String export_device_batch(DeviceBatchForm form);
    
    
    /**
     * 导出用水量
     *
     * @param view
     * @return
     * @
     */
    String exportOwnerwater(PreviewForm view) ;


    /**
     * 导出水表信息
     * @param deviceForm
     * @return
     */
    String exportWaterInfo(DeviceForm deviceForm) ;
    
    /**
     * 导出抄表信息
     * @param deviceForm
     * @return
     */
    String exportDeviceWater(DeviceForm deviceForm) ;

    /**
     * 导出历史水表上传数据
     * @param queryForm
     * @return
     */
    String exportOriginal(QueryForm queryForm);
    
    /**
     * 导出数据监控数据
     * @param pagination
     * @return
     */
    String exportMonitor(Pagination<DeviceVo> pagination, List<String> dates);
    
}
