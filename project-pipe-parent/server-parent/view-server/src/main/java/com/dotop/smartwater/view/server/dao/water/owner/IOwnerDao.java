package com.dotop.smartwater.view.server.dao.water.owner;

import com.dotop.smartwater.view.server.core.owner.form.OwnerForm;
import com.dotop.smartwater.view.server.core.owner.vo.OwnerVo;

import java.util.List;

public interface IOwnerDao {

    List<OwnerVo> list(OwnerForm deviceDataForm);

    Integer count(OwnerForm deviceDataForm);
}
