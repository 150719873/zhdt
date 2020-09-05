package com.dotop.smartwater.view.server.dao.pipe.area;

import com.dotop.smartwater.view.server.core.area.form.AreaForm;
import com.dotop.smartwater.view.server.core.area.vo.AreaVo;

import java.util.List;

/**
 *
 */
public interface IViewAreaDao {

    List<AreaVo> listDma(AreaForm areaForm);
}
