package com.dotop.smartwater.project.auth.api.impl;

import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.dependence.core.utils.BeanUtils;
import com.dotop.smartwater.dependence.core.utils.UuidUtils;
import com.dotop.smartwater.project.auth.api.IMdDockingTempFactory;
import com.dotop.smartwater.project.auth.service.IMdDockingService;
import com.dotop.smartwater.project.auth.service.IMdDockingTempService;
import com.dotop.smartwater.project.module.core.auth.bo.MdDockingBo;
import com.dotop.smartwater.project.module.core.auth.bo.MdDockingTempBo;
import com.dotop.smartwater.project.module.core.auth.form.MdDockingTempForm;
import com.dotop.smartwater.project.module.core.auth.local.AuthCasClient;
import com.dotop.smartwater.project.module.core.auth.vo.MdDockingTempVo;
import com.dotop.smartwater.project.module.core.water.constants.ResultCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;


/**

 */
@Component
public class MdDockingTempFactoryImpl implements IMdDockingTempFactory {

    @Autowired
    private IMdDockingTempService iMdDockingTempService;

    @Autowired
    private IMdDockingService iMdDockingService;

    @Override
    public Pagination<MdDockingTempVo> page(MdDockingTempForm form) {
        MdDockingTempBo bo = BeanUtils.copy(form, MdDockingTempBo.class);
        return iMdDockingTempService.page(bo);
    }

    @Override
    public MdDockingTempVo add(MdDockingTempForm form) {
        MdDockingTempBo mdDockingTempBo = new MdDockingTempBo();
        mdDockingTempBo.setFactory(form.getFactory());
        mdDockingTempBo.setType(form.getType());
        List<MdDockingTempVo> list = iMdDockingTempService.list(mdDockingTempBo);
        if (list.size() > 0) {
            throw new FrameworkRuntimeException(ResultCode.ParamIllegal, "该属性已经存在");
        }

        MdDockingTempBo bo = BeanUtils.copy(form, MdDockingTempBo.class);
        bo.setIsDel(0);
        bo.setCreateBy(AuthCasClient.getUser().getAccount());
        bo.setCreateDate(new Date());
        bo.setId(UuidUtils.getUuid(true));
        return iMdDockingTempService.add(bo);
    }

    @Override
    public MdDockingTempVo edit(MdDockingTempForm form) {
        MdDockingTempBo bo = BeanUtils.copy(form, MdDockingTempBo.class);
        bo.setLastBy(AuthCasClient.getUser().getAccount());
        bo.setLastDate(new Date());

        if(!bo.getStatus()){
            MdDockingTempVo vo = iMdDockingTempService.get(bo);
            if(vo == null){
                throw new FrameworkRuntimeException(ResultCode.ParamIllegal, "没有该模板");
            }

            MdDockingBo mdDockingBo = new MdDockingBo();
            mdDockingBo.setFactory(vo.getFactory());
            mdDockingBo.setType(vo.getType());
            iMdDockingService.delByFactoryAndType(mdDockingBo);
        }

        return iMdDockingTempService.edit(bo);
    }

    @Override
    public String del(MdDockingTempForm form) {
        MdDockingTempBo bo = new MdDockingTempBo();
        bo.setId(form.getId());

        MdDockingTempVo vo = iMdDockingTempService.get(bo);
        if(vo == null){
            throw new FrameworkRuntimeException(ResultCode.ParamIllegal, "没有该模板");
        }

        MdDockingBo mdDockingBo = new MdDockingBo();
        mdDockingBo.setFactory(vo.getFactory());
        mdDockingBo.setType(vo.getType());
        iMdDockingService.delByFactoryAndType(mdDockingBo);

        iMdDockingTempService.del(bo);
        return null;
    }

    @Override
    public MdDockingTempVo get(MdDockingTempForm form) {
        MdDockingTempBo bo = new MdDockingTempBo();
        bo.setId(form.getId());
        return iMdDockingTempService.get(bo);
    }

    @Override
    public List<MdDockingTempVo> list(MdDockingTempForm form) {
        MdDockingTempBo bo = BeanUtils.copy(form, MdDockingTempBo.class);
        return iMdDockingTempService.list(bo);
    }

}
