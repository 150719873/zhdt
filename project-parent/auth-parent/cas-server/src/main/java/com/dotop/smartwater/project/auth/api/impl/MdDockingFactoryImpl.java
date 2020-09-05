package com.dotop.smartwater.project.auth.api.impl;

import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.dependence.core.utils.BeanUtils;
import com.dotop.smartwater.dependence.core.utils.UuidUtils;
import com.dotop.smartwater.project.auth.service.IMdDockingService;
import com.dotop.smartwater.project.auth.api.IMdDockingFactory;
import com.dotop.smartwater.project.auth.service.IMdDockingEnterpriseService;
import com.dotop.smartwater.project.auth.service.IMdDockingTempService;
import com.dotop.smartwater.project.module.core.auth.bo.MdDockingBo;
import com.dotop.smartwater.project.module.core.auth.bo.MdDockingEnterpriseBo;
import com.dotop.smartwater.project.module.core.auth.bo.MdDockingTempBo;
import com.dotop.smartwater.project.module.core.auth.form.MdDockingEnterpriseForm;
import com.dotop.smartwater.project.module.core.auth.form.MdDockingForm;
import com.dotop.smartwater.project.module.core.auth.local.AuthCasClient;
import com.dotop.smartwater.project.module.core.auth.vo.MdDockingEnterpriseVo;
import com.dotop.smartwater.project.module.core.auth.vo.MdDockingTempVo;
import com.dotop.smartwater.project.module.core.auth.vo.MdDockingVo;
import com.dotop.smartwater.project.module.core.auth.vo.md.ConfigListVo;
import com.dotop.smartwater.project.module.core.auth.vo.md.MdDockingExtForm;
import com.dotop.smartwater.project.module.core.auth.vo.md.MdDockingExtVo;
import com.dotop.smartwater.project.module.core.water.constants.ResultCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**

 */
@Component
public class MdDockingFactoryImpl implements IMdDockingFactory {

    @Autowired
    private IMdDockingService iMdDockingService;

    @Autowired
    private IMdDockingTempService iMdDockingTempService;

    @Autowired
    private IMdDockingEnterpriseService iMdDockingEnterpriseService;

    @Override
    public Pagination<MdDockingVo> page(MdDockingForm form) {
        MdDockingBo bo = BeanUtils.copy(form, MdDockingBo.class);
        return iMdDockingService.page(bo);
    }

    @Override
    public MdDockingVo add(MdDockingForm form) {


        MdDockingBo bo = BeanUtils.copy(form, MdDockingBo.class);

        MdDockingVo vo = iMdDockingService.get(bo);
        if (vo != null) {
            throw new FrameworkRuntimeException(ResultCode.ParamIllegal, "主键已被使用");
        }
        bo.setIsDel(0);
        bo.setCreateBy(AuthCasClient.getUser().getAccount());
        bo.setCreateDate(new Date());
        return iMdDockingService.add(bo);
    }

    @Override
    public MdDockingVo edit(MdDockingForm form) {
        MdDockingBo bo = BeanUtils.copy(form, MdDockingBo.class);
        bo.setLastBy(AuthCasClient.getUser().getAccount());
        bo.setLastDate(new Date());
        return iMdDockingService.edit(bo);
    }

    @Override
    public String del(MdDockingForm form) {
        MdDockingBo bo = new MdDockingBo();
        bo.setIsDel(1);
        bo.setId(form.getId());
        bo.setLastBy(AuthCasClient.getUser().getAccount());
        bo.setLastDate(new Date());
        iMdDockingService.edit(bo);
        return null;
    }

    @Override
    public MdDockingVo get(MdDockingForm form) {
        MdDockingBo bo = new MdDockingBo();
        bo.setId(form.getId());
        return iMdDockingService.get(bo);
    }

    @Override
    public List<MdDockingVo> list(MdDockingForm form) {
        MdDockingBo bo = BeanUtils.copy(form, MdDockingBo.class);
        return iMdDockingService.list(bo);
    }

    @Override
    public List<MdDockingExtVo> load(MdDockingEnterpriseForm form) {
        MdDockingEnterpriseVo mdeVo = iMdDockingEnterpriseService.get(BeanUtils.copy(form, MdDockingEnterpriseBo.class));
        if (mdeVo == null) {
            throw new FrameworkRuntimeException(ResultCode.ParamIllegal, "没有该水司接口配置");
        }
        if (mdeVo.getFactoryId() == null) {
            throw new FrameworkRuntimeException(ResultCode.ParamIllegal, "该水司没有配置开通厂家");
        }
        String[] factorys = mdeVo.getFactoryId().split(",");

        List<MdDockingExtVo> result = new ArrayList<>();

        MdDockingBo bo = new MdDockingBo();
        bo.setMdeId(form.getId());
        List<MdDockingVo> list = iMdDockingService.list(bo);
        //加载原有数据
        if (list.size() > 0) {
            for (String factory : factorys) {
                /******* 模板开始  ***************/
                MdDockingTempBo mdDockingTempBo = new MdDockingTempBo();
                mdDockingTempBo.setFactory(factory);
                List<MdDockingTempVo> temps = iMdDockingTempService.list(mdDockingTempBo);
                if (temps.size() == 0) {
                    continue;
                }
                /*********模板结束*************/

                MdDockingExtVo data = new MdDockingExtVo();
                data.setFactory(factory);

                bo.setFactory(factory);
                list = iMdDockingService.list(bo);

                MdDockingVo mdDockingVo = new MdDockingVo();
                if (list.size() > 0) {
                    mdDockingVo.setUsername(list.get(0).getUsername());
                    mdDockingVo.setPassword(list.get(0).getPassword());
                    mdDockingVo.setWaterUsername(list.get(0).getWaterUsername());
                    mdDockingVo.setWaterPassword(list.get(0).getWaterPassword());
                    mdDockingVo.setWaterHost(list.get(0).getWaterHost());
                    mdDockingVo.setSystem(list.get(0).getSystem());
                    mdDockingVo.setMode(list.get(0).getMode());
                    mdDockingVo.setProductName(list.get(0).getProductName());
                    mdDockingVo.setCode(list.get(0).getCode());
                    data.setMdDockingVo(mdDockingVo);

                    Map<String, MdDockingVo> mdDockingMap = list.stream().collect(Collectors.toMap(MdDockingVo::getType, a -> a, (k1, k2) -> k1));

                    List<MdDockingTempVo> mdDockingTempVos = new ArrayList<>();
                    for (MdDockingTempVo value : temps) {
                        MdDockingVo obj = mdDockingMap.get(value.getType());
                        if (obj == null) {
                            value.setChecked(false);
                        } else {
                            value.setChecked(true);
                            org.springframework.beans.BeanUtils.copyProperties(obj, value);
                        }
                        mdDockingTempVos.add(value);
                    }
                    data.setList(mdDockingTempVos);
                    result.add(data);
                } else {
                    mdDockingVo.setUsername("");
                    mdDockingVo.setPassword("");
                    mdDockingVo.setWaterUsername("");
                    mdDockingVo.setWaterPassword("");
                    mdDockingVo.setWaterHost(mdeVo.getWaterHost());
                    mdDockingVo.setSystem("");
                    mdDockingVo.setMode("");
                    mdDockingVo.setProductName("");
                    mdDockingVo.setCode("");
                    data.setMdDockingVo(mdDockingVo);

                    for (MdDockingTempVo value : temps) {
                        value.setChecked(value.getStatus());
                    }
                    data.setList(temps);
                    result.add(data);
                }
            }
            return result;
        } else {
            //初始化数据
            for (String factory : factorys) {
                MdDockingExtVo data = new MdDockingExtVo();
                data.setFactory(factory);

                MdDockingVo mdDockingVo = new MdDockingVo();
                mdDockingVo.setUsername("");
                mdDockingVo.setPassword("");
                mdDockingVo.setWaterUsername("");
                mdDockingVo.setWaterPassword("");
                mdDockingVo.setWaterHost(mdeVo.getWaterHost());
                mdDockingVo.setSystem("");
                mdDockingVo.setMode("");
                mdDockingVo.setProductName("");
                mdDockingVo.setCode("");

                data.setMdDockingVo(mdDockingVo);

                MdDockingTempBo mdDockingTempBo = new MdDockingTempBo();
                mdDockingTempBo.setFactory(factory);

                List<MdDockingTempVo> temps = iMdDockingTempService.list(mdDockingTempBo);
                if (temps.size() > 0) {
                    for (MdDockingTempVo value : temps) {
                        value.setChecked(value.getStatus());
                    }
                }
                data.setList(temps);
                result.add(data);
            }
            return result;
        }
    }

    /******
     *  批量插入,采用事务保存
     * *****/
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
    public void save(MdDockingExtForm form) {
        if (form.getList() == null || form.getList().size() == 0) {
            throw new FrameworkRuntimeException(ResultCode.ParamIllegal, "没有配置信息");
        }
        MdDockingEnterpriseBo bo = new MdDockingEnterpriseBo();
        bo.setId(form.getMdeId());
        MdDockingEnterpriseVo mdeVo = iMdDockingEnterpriseService.get(bo);
        if (mdeVo == null) {
            throw new FrameworkRuntimeException(ResultCode.ParamIllegal, "没有该水司接口配置");
        }

        String account = AuthCasClient.getUser().getAccount();
        Date date = new Date();

        List<MdDockingVo> list = new ArrayList<>();
        for (MdDockingExtVo vo : form.getList()) {
            MdDockingVo header = vo.getMdDockingVo();
            header.setMdeId(form.getMdeId());
            header.setFactory(vo.getFactory());
            header.setWaterHost(mdeVo.getWaterHost());

            if (vo.getList() == null || vo.getList().size() == 0) {
                continue;
            }
            for (MdDockingTempVo value : vo.getList()) {
                if (value.getChecked()) {
                    MdDockingVo child = new MdDockingVo();
                    org.springframework.beans.BeanUtils.copyProperties(header, child);
                    org.springframework.beans.BeanUtils.copyProperties(value, child);
                    child.setId(UuidUtils.getUuid(true));
                    child.setIsDel(mdeVo.getStatus() ? 0 : 1);
                    child.setCreateBy(account);
                    child.setCreateDate(date);
                    child.setLastBy(account);
                    child.setLastDate(date);
                    child.setEnterpriseid(mdeVo.getEnterpriseid());
                    list.add(child);
                }
            }
            //先删除,再插入
            iMdDockingService.save(form.getMdeId(), list);
        }

    }

    @Override
    public ConfigListVo loadConfigList(MdDockingEnterpriseForm form) {
        String systemCode = "28,990004";
        String modeCode = "28,300001";
        return iMdDockingService.loadConfigList(form.getEnterpriseid(), systemCode, modeCode);
    }
}
