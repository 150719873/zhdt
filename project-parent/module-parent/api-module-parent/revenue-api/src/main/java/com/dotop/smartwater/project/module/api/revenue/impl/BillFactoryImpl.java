package com.dotop.smartwater.project.module.api.revenue.impl;

import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.project.module.api.revenue.IBillFactory;
import com.dotop.smartwater.project.module.core.auth.local.AuthCasClient;
import com.dotop.smartwater.project.module.core.auth.vo.UserVo;
import com.dotop.smartwater.project.module.core.water.bo.BillBo;
import com.dotop.smartwater.project.module.core.water.bo.OwnerBo;
import com.dotop.smartwater.project.module.core.water.form.BillForm;
import com.dotop.smartwater.project.module.core.water.form.customize.BalanceChangeParamForm;
import com.dotop.smartwater.project.module.core.water.vo.BillVo;
import com.dotop.smartwater.project.module.core.water.vo.OwnerVo;
import com.dotop.smartwater.project.module.core.water.vo.customize.PayDetailRecord;
import com.dotop.smartwater.project.module.service.revenue.IBillService;
import com.dotop.smartwater.project.module.service.revenue.IOwnerService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 票据管理
 *

 * @date 2019年2月25日
 */
@Component
public class BillFactoryImpl implements IBillFactory {

    @Autowired
    private IBillService iBillService;
    @Autowired
    private IOwnerService iOwnerService;

    @Override
    public Pagination<BillVo> getList(BillForm billForm) throws FrameworkRuntimeException {
        UserVo user = AuthCasClient.getUser();

        BillBo billBo = new BillBo();
        BeanUtils.copyProperties(billForm, billBo);
        billBo.setEnterpriseid(user.getEnterpriseid());

        return iBillService.getList(billBo);

    }

    @Override
    public BillVo add(BillForm billForm) throws FrameworkRuntimeException {
        /*
         * 记录打印数据 查询是否已经存在打印信息
         *
         * 1 保存用户信息 2 查询 owner 相关的信息
         */

        UserVo user = AuthCasClient.getUser();
        BillBo billBo = new BillBo();
        BeanUtils.copyProperties(billForm, billBo);
        billForm.setEnterpriseid(user.getEnterpriseid());
        // 操作人id
        billBo.setOperauserid(user.getUserid());
        // 操作人姓名
        billBo.setOperaname(user.getName());
        // 打印时间
        billBo.setPrintTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        billBo.setEnterpriseid(user.getEnterpriseid());

        if (StringUtils.isNotBlank(billForm.getOwnerid())) {
            OwnerBo ownerBo = new OwnerBo();
            ownerBo.setOwnerid(billForm.getOwnerid());
            OwnerVo owner = iOwnerService.findByOwnerId(ownerBo);
            billBo.setUserno(owner.getUserno());
            billBo.setUsername(owner.getUsername());
            billBo.setOwnerid(owner.getOwnerid());
            billBo.setStatus(1);
            billBo.setFrequency(1); // 打印次数一次
            billBo.setTempcontent(billForm.getTempcontent());
            billBo.setDatacontent(billForm.getTempcontent());
        }
        // 查询是否已经有打印信息
        Boolean flag = iBillService.isExist(billBo);
        if (flag) { // 存在即更新打印次数
            iBillService.edit(billBo);
        } else {
            iBillService.add(billBo);
        }
        return null;
    }

    @Override
    public Pagination<PayDetailRecord> balanceFind(BalanceChangeParamForm balanceChangeParam) throws FrameworkRuntimeException {
        return iBillService.balanceFind(balanceChangeParam);
    }
}
