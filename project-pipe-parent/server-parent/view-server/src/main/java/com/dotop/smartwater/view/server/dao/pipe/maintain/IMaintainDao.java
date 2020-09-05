package com.dotop.smartwater.view.server.dao.pipe.maintain;

import com.dotop.smartwater.view.server.core.maintain.form.MaintainLogForm;
import com.dotop.smartwater.view.server.core.maintain.vo.MaintainLogVo;

import java.util.List;

/**
 *
 */
public interface IMaintainDao {

    List<MaintainLogVo> list(MaintainLogForm maintainLogForm);
}
