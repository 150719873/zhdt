package com.dotop.pipe.web.api.factory.common;

import com.dotop.pipe.core.form.CommonUploadForm;
import com.dotop.pipe.core.vo.common.CommonUploadVo;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 *
 */
public interface ICommonUploadFactory {


    /**
     * 上传文件
     *
     * @param file
     * @return
     */
    CommonUploadVo uploadImg(MultipartFile file);

    /**
     * 删除图片
     */
    boolean delete(CommonUploadForm commonUploadForm);

    /**
     * 获取图片集合
     */
    List<CommonUploadVo> get(CommonUploadForm commonUploadForm);

}
