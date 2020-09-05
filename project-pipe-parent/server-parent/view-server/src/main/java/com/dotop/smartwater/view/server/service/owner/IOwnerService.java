package com.dotop.smartwater.view.server.service.owner;

import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.view.server.core.owner.form.OwnerForm;
import com.dotop.smartwater.view.server.core.owner.vo.OwnerVo;

import java.util.List;

/**
 *
 */
public interface IOwnerService {

    List<OwnerVo> list(OwnerForm ownerForm) throws FrameworkRuntimeException;

    Integer count(OwnerForm ownerForm) throws FrameworkRuntimeException;
}
