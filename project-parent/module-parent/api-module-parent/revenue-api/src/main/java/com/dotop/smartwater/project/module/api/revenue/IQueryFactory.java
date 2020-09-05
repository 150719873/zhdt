package com.dotop.smartwater.project.module.api.revenue;

import com.dotop.smartwater.dependence.core.common.BaseFactory;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.project.module.core.water.form.DeviceUplinkForm;
import com.dotop.smartwater.project.module.core.water.form.OwnerForm;
import com.dotop.smartwater.project.module.core.water.form.customize.QueryForm;
import com.dotop.smartwater.project.module.core.water.form.customize.StatisticsWaterForm;
import com.dotop.smartwater.project.module.core.water.vo.DeviceDownlinkVo;
import com.dotop.smartwater.project.module.core.water.vo.OwnerVo;
import com.dotop.smartwater.project.module.core.water.vo.customize.OriginalVo;
import com.dotop.smartwater.project.module.core.water.vo.customize.QueryVo;
import com.dotop.smartwater.project.module.core.water.vo.customize.StatisticsWaterVo;

import java.util.List;
import java.util.Map;

/**

 * @date 2019/2/28.
 */
public interface IQueryFactory extends BaseFactory<QueryForm, QueryVo> {

    Map<String, Object> getDataTotal() ;

    Pagination<OriginalVo> original(QueryForm queryForm) ;

    List<StatisticsWaterVo> dailyGetStatisticsWater(StatisticsWaterForm statisticsWaterForm) ;

    List<StatisticsWaterVo> monthGetStatisticsWater(StatisticsWaterForm statisticsWaterForm) ;

    OwnerVo ownerinfo(OwnerForm ownerForm) ;

    OriginalVo getOriginalByIdAndDate(DeviceUplinkForm deviceUplinkForm) ;

    Pagination<DeviceDownlinkVo> getHistory(QueryForm queryForm);
}
