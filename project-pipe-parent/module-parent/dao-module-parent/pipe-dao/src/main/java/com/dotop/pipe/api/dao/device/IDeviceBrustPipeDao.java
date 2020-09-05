package com.dotop.pipe.api.dao.device;

import com.dotop.pipe.core.dto.report.DeviceBrustPipeDto;
import com.dotop.pipe.core.vo.report.DeviceBrustPipeVo;
import com.dotop.smartwater.dependence.core.common.BaseDao;
import org.springframework.dao.DataAccessException;

import java.util.List;

public interface IDeviceBrustPipeDao extends BaseDao<DeviceBrustPipeDto, DeviceBrustPipeVo> {

    List<DeviceBrustPipeVo> listPipe(DeviceBrustPipeDto deviceBrustPipeDto) throws DataAccessException;

    List<DeviceBrustPipeVo> listArea(DeviceBrustPipeDto deviceBrustPipeDto) throws DataAccessException;
}
