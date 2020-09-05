package com.dotop.smartwater.view.server.api.factory.view.impl;

import com.dotop.smartwater.view.server.core.area.form.AreaForm;
import com.dotop.smartwater.view.server.core.area.vo.AreaVo;
import com.dotop.pipe.auth.cas.web.IAuthCasWeb;
import com.dotop.pipe.auth.core.vo.auth.LoginCas;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.view.server.api.factory.view.IAreaFactory;
import com.dotop.smartwater.view.server.service.area.IAreaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.dotop.smartwater.dependence.core.common.RootModel.NOT_DEL;

/**
 *
 */
@Component("AreaFactoryImpl2")
public class AreaFactoryImpl implements IAreaFactory {

    @Autowired
    IAreaService iAreaService;
    @Autowired
    IAuthCasWeb iAuthCasWeb;

    @Override
    public List<AreaVo> listDma(AreaForm areaForm) throws FrameworkRuntimeException {
        LoginCas loginCas = iAuthCasWeb.get();
        areaForm.setEnterpriseId(loginCas.getEnterpriseId());
        areaForm.setIsDel(NOT_DEL);
        return iAreaService.listDma(areaForm);
    }
}
