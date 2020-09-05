package com.dotop.smartwater.project.auth.api.impl;

import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.dependence.core.utils.BeanUtils;
import com.dotop.smartwater.dependence.core.utils.StrUtils;
import com.dotop.smartwater.dependence.core.utils.UuidUtils;
import com.dotop.smartwater.project.auth.service.IMdDockingService;
import com.dotop.smartwater.project.auth.api.IMdDockingEnterpriseFactory;
import com.dotop.smartwater.project.auth.service.IMdDockingEnterpriseService;
import com.dotop.smartwater.project.module.core.auth.bo.MdDockingEnterpriseBo;
import com.dotop.smartwater.project.module.core.auth.form.MdDockingEnterpriseForm;
import com.dotop.smartwater.project.module.core.auth.local.AuthCasClient;
import com.dotop.smartwater.project.module.core.auth.vo.EnterpriseVo;
import com.dotop.smartwater.project.module.core.auth.vo.MdDockingEnterpriseVo;
import com.dotop.smartwater.project.module.core.water.constants.ResultCode;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;


/**

 */
@Component
public class MdDockingEnterpriseFactoryImpl implements IMdDockingEnterpriseFactory {

    @Autowired
    private IMdDockingEnterpriseService iMdDockingEnterpriseService;

    @Autowired
    private IMdDockingService iMdDockingService;

    @Override
    public Pagination<MdDockingEnterpriseVo> page(MdDockingEnterpriseForm form) {
        MdDockingEnterpriseBo bo = BeanUtils.copy(form, MdDockingEnterpriseBo.class);
        return iMdDockingEnterpriseService.page(bo);
    }

    @Override
    public MdDockingEnterpriseVo add(MdDockingEnterpriseForm form) {
        if(form.getFactoryId() == null){
            throw new FrameworkRuntimeException(ResultCode.ParamIllegal, "请最少选择一个平台开通");
        }

        MdDockingEnterpriseBo bo = BeanUtils.copy(form, MdDockingEnterpriseBo.class);
        bo.setIsDel(0);
        bo.setCreateBy(AuthCasClient.getUser().getAccount());
        bo.setCreateDate(new Date());
        bo.setId(UuidUtils.getUuid(true));

        bo.setLastBy(bo.getCreateBy());
        bo.setLastDate(bo.getCreateDate());
        return iMdDockingEnterpriseService.add(bo);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
    public MdDockingEnterpriseVo edit(MdDockingEnterpriseForm form) {
        MdDockingEnterpriseBo bo = BeanUtils.copy(form, MdDockingEnterpriseBo.class);
        MdDockingEnterpriseVo vo = iMdDockingEnterpriseService.get(bo);
        if (vo == null) {
            throw new FrameworkRuntimeException(ResultCode.ParamIllegal, "没有该水司权限信息");
        }

        assert bo != null;
        if(StringUtils.isNotBlank(bo.getFactoryId())){
            //把相同的保留，不同的去除
            String[] old = vo.getFactoryId().split(",");
            String[] now = bo.getFactoryId().split(",");

            String[] chaJi = StrUtils.getDifferenceSet(old, now);
            if (chaJi.length > 0) {
                for (String factory : chaJi) {
                    iMdDockingService.delByMdeIdAndFactory(vo.getId(), factory);
                }
            }
        }

        iMdDockingService.updateByMdeId(bo.getId(), bo.getStatus());
        bo.setLastBy(AuthCasClient.getUser().getAccount());
        bo.setLastDate(new Date());
        return iMdDockingEnterpriseService.edit(bo);
    }

    @Override
    public String del(MdDockingEnterpriseForm form) {
        MdDockingEnterpriseBo bo = new MdDockingEnterpriseBo();
        bo.setId(form.getId());
        iMdDockingService.delByMdeId(form.getId());
        iMdDockingEnterpriseService.del(bo);
        return null;
    }

    @Override
    public MdDockingEnterpriseVo get(MdDockingEnterpriseForm form) {
        MdDockingEnterpriseBo bo = new MdDockingEnterpriseBo();
        bo.setId(form.getId());
        return iMdDockingEnterpriseService.get(bo);
    }

    @Override
    public List<MdDockingEnterpriseVo> list(MdDockingEnterpriseForm form) {
        MdDockingEnterpriseBo bo = BeanUtils.copy(form, MdDockingEnterpriseBo.class);
        return iMdDockingEnterpriseService.list(bo);
    }

    @Override
    public List<EnterpriseVo> enterpriseList() {
        return iMdDockingEnterpriseService.enterpriseList();
    }
}
