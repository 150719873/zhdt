package com.dotop.pipe.api.dao.device;

import com.dotop.pipe.core.dto.report.NightReadingDto;
import com.dotop.pipe.core.vo.report.NightReadingVo;
import com.dotop.smartwater.dependence.core.common.BaseDao;
import org.springframework.dao.DataAccessException;

import java.util.List;

public interface INightReadingDao extends BaseDao<NightReadingDto, NightReadingVo> {

    List<NightReadingVo> listDevices(NightReadingDto nightReadingDto) throws DataAccessException;
}
