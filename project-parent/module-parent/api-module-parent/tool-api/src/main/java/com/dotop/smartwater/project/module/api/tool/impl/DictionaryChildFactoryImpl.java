package com.dotop.smartwater.project.module.api.tool.impl;

import com.dotop.smartwater.project.module.api.tool.IDictionaryChildFactory;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.dependence.core.utils.BeanUtils;
import com.dotop.smartwater.project.module.core.auth.local.AuthCasClient;
import com.dotop.smartwater.project.module.core.auth.vo.UserVo;
import com.dotop.smartwater.project.module.core.water.bo.DictionaryBo;
import com.dotop.smartwater.project.module.core.water.bo.DictionaryChildBo;
import com.dotop.smartwater.project.module.core.water.constants.DictionaryCode;
import com.dotop.smartwater.project.module.core.water.constants.ResultCode;
import com.dotop.smartwater.project.module.core.water.form.DictionaryChildForm;
import com.dotop.smartwater.project.module.core.water.vo.DictionaryChildVo;
import com.dotop.smartwater.project.module.core.water.vo.DictionaryVo;
import com.dotop.smartwater.project.module.service.tool.IDictionaryChildService;
import com.dotop.smartwater.project.module.service.tool.IDictionaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * 字典配置
 *

 * @date 2019年2月23日
 */

@Component
public class DictionaryChildFactoryImpl implements IDictionaryChildFactory {

    @Autowired
    private IDictionaryChildService iDictionaryChildService;

    @Autowired
    private IDictionaryService iDictionaryService;

    @Override
    public Pagination<DictionaryChildVo> page(DictionaryChildForm dictionaryChildForm)  {
        return iDictionaryChildService.page(BeanUtils.copy(dictionaryChildForm, DictionaryChildBo.class));
    }

    @Override
    public DictionaryChildVo get(DictionaryChildForm dictionaryChildForm)  {
        return iDictionaryChildService.get(BeanUtils.copy(dictionaryChildForm, DictionaryChildBo.class));
    }

    @Override
    public DictionaryChildVo add(DictionaryChildForm dictionaryChildForm)  {
        UserVo userVo = AuthCasClient.getUser();
        DictionaryChildBo dictionaryChildBo = BeanUtils.copy(dictionaryChildForm, DictionaryChildBo.class);

        Date date = new Date();
        dictionaryChildBo.setCreateBy(userVo.getUserid());
        dictionaryChildBo.setLastBy(userVo.getUserid());
        dictionaryChildBo.setCreateDate(date);
        dictionaryChildBo.setLastDate(date);
        return iDictionaryChildService.add(dictionaryChildBo);
    }

    @Override
    public DictionaryChildVo edit(DictionaryChildForm dictionaryChildForm)  {
        UserVo userVo = AuthCasClient.getUser();
        DictionaryChildBo dictionaryChildBo = BeanUtils.copy(dictionaryChildForm, DictionaryChildBo.class);

        canNotDo(dictionaryChildForm.getChildId());

        Date date = new Date();
        dictionaryChildBo.setLastBy(userVo.getUserid());
        dictionaryChildBo.setLastDate(date);
        return iDictionaryChildService.edit(dictionaryChildBo);
    }

    @Override
    public String del(DictionaryChildForm dictionaryChildForm)  {
        canNotDo(dictionaryChildForm.getChildId());
        return iDictionaryChildService.del(BeanUtils.copy(dictionaryChildForm, DictionaryChildBo.class));
    }

    private void canNotDo(String childId) {
        DictionaryChildBo dictionaryChildBo = new DictionaryChildBo();
        dictionaryChildBo.setChildId(childId);
        DictionaryChildVo dictionaryChildVo = iDictionaryChildService.get(dictionaryChildBo);

        if (dictionaryChildVo == null) {
            throw new FrameworkRuntimeException(ResultCode.ILLEGAL_EXCEPTION, "没有找到该记录");
        }

        DictionaryBo dictionaryBo = new DictionaryBo();
        dictionaryBo.setDictionaryId(dictionaryChildVo.getDictionaryId());
        DictionaryVo dictionaryVo = iDictionaryService.get(dictionaryBo);

        if (dictionaryVo != null && DictionaryCode.DICTIONARY_PROPERTY_PUBLIC.equals(dictionaryVo.getDictionaryType())) {
            throw new FrameworkRuntimeException(ResultCode.ILLEGAL_EXCEPTION, "公有记录不允许操作");
        }

    }
}
