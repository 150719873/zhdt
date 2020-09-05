package com.dotop.smartwater.view.server.dao.pipe.waterwmlog;

import com.dotop.smartwater.view.server.core.device.form.WaterWmLogForm;
import com.dotop.smartwater.view.server.core.device.vo.WaterWmLogVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 *
 */
public interface IWaterWmLogDao {

    List<WaterWmLogVo> list(WaterWmLogForm maintainLogForm);

    Integer adds(@Param("waterWmLogForms") List<WaterWmLogForm> waterWmLogForms);
}
