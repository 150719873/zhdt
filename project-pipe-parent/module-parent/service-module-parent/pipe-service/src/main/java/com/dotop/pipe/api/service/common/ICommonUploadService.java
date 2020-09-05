package com.dotop.pipe.api.service.common;

import com.dotop.pipe.core.bo.common.CommonUploadBo;
import com.dotop.pipe.core.vo.common.CommonUploadVo;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;

import java.util.List;

public interface ICommonUploadService {

    /**
     * 新增图片
     *
     * @param commonUploadBo
     * @return
     * @throws FrameworkRuntimeException
     */
    CommonUploadVo add(CommonUploadBo commonUploadBo) throws FrameworkRuntimeException;

    /**
     * 获取图片
     *
     * @param id
     * @param ids
     * @param thirdId
     * @return
     * @throws FrameworkRuntimeException
     */
    List<CommonUploadVo> get(String id, List<String> ids, String thirdId) throws FrameworkRuntimeException;

    /**
     * 删除图片
     *
     * @param id
     * @param ids
     * @param thirdId
     * @return
     * @throws FrameworkRuntimeException
     */
    boolean del(String id, List<String> ids, String thirdId) throws FrameworkRuntimeException;

    void updateThirdId(List<String> fileIdList, String patrolRouteTaskId) throws FrameworkRuntimeException;
}
