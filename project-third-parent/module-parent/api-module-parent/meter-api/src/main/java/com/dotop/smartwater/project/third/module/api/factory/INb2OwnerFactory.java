package com.dotop.smartwater.project.third.module.api.factory;

import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.project.third.module.core.third.nb2.form.UserSynForm;
import com.dotop.smartwater.project.third.module.core.third.nb2.vo.UserSynVo;

public interface INb2OwnerFactory extends IWaterObtainFactory<UserSynForm, UserSynVo> {

    @Override
    UserSynVo addOwner(UserSynForm userSynForm) throws FrameworkRuntimeException;
}
