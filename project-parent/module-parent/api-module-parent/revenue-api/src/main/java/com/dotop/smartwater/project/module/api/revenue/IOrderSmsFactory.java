package com.dotop.smartwater.project.module.api.revenue;

import com.dotop.smartwater.dependence.core.common.BaseFactory;
import com.dotop.smartwater.project.module.core.auth.vo.UserVo;
import com.dotop.smartwater.project.module.core.water.form.customize.RemindersForm;
import com.dotop.smartwater.project.module.core.water.vo.OrderVo;

/**
 * 账单相关
 * 催缴
 */
public interface IOrderSmsFactory extends BaseFactory<RemindersForm, OrderVo> {

    /**
     * 催缴
     * @param remindersForm 催缴对象
     * @param user 用户
     */
    void reminders(RemindersForm remindersForm, UserVo user);
}
