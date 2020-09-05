package com.dotop.smartwater.view.server.dao.pipe.brustpipe;


import com.dotop.pipe.core.dto.brustpipe.BrustPipeDto;
import com.dotop.pipe.core.dto.decive.DeviceDto;
import com.dotop.pipe.core.vo.brustpipe.BrustPipeVo;
import com.dotop.pipe.core.vo.device.DeviceVo;
import com.dotop.smartwater.view.server.core.brustpipe.vo.BrustPipeOperationsVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 *
 */
public interface IBrustPipeOperationsDao {


    BrustPipeOperationsVo getNumByDeviceId(DeviceDto deviceDto);

    List<BrustPipeOperationsVo> getBrustPipe(DeviceDto deviceDto);

    List<BrustPipeVo> listBrustPipe(BrustPipeDto brustPipeDto);

    List<DeviceVo> listDevice(DeviceDto deviceDto);

    BrustPipeOperationsVo getWorkOrderByBrustPipeId(@Param("brustPipeId") String brustPipeId, @Param("enterpriseId") String enterpriseId);

    BrustPipeOperationsVo getBrustPipeById(@Param("brustPipeId") String brustPipeId, @Param("enterpriseId") String enterpriseId);

    List<BrustPipeVo> getBrustPipeByIdList(@Param("brustPipelist") List<String> brustPipelist, @Param("enterpriseId") String enterpriseId);
}
