package com.dotop.smartwater.project.module.api.revenue;

import com.dotop.smartwater.dependence.core.common.BaseFactory;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.project.module.core.water.form.BillForm;
import com.dotop.smartwater.project.module.core.water.form.customize.BalanceChangeParamForm;
import com.dotop.smartwater.project.module.core.water.vo.BillVo;
import com.dotop.smartwater.project.module.core.water.vo.customize.PayDetailRecord;

/**
 * Bill接口
 *
 * @program: project-parent
 * @description: 收费类型

 * @create: 2019-02-26 09:23
 **/

public interface IBillFactory extends BaseFactory<BillForm, BillVo> {

    /**
     * 分页
     *
     * @param billForm billForm对象
     * @return 分页
     */
    Pagination<BillVo> getList(BillForm billForm);

    /**
     * 新增票据打印信息
     *
     * @param billForm billForm对象
     * @return BillVo
     */
    @Override
    BillVo add(BillForm billForm);

    /**
     * 分页
     *
     * @param balanceChangeParam BalanceChangeParamForm对象
     * @return 分页
     */
    Pagination<PayDetailRecord> balanceFind(BalanceChangeParamForm balanceChangeParam);
}
