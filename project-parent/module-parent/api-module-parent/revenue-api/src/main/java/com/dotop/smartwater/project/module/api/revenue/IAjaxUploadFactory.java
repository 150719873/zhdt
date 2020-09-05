package com.dotop.smartwater.project.module.api.revenue;

import com.dotop.smartwater.dependence.core.common.BaseFactory;
import com.dotop.smartwater.dependence.core.common.BaseForm;
import com.dotop.smartwater.dependence.core.common.BaseVo;
import com.dotop.smartwater.project.module.core.water.form.customize.ImportFileForm;

import javax.servlet.http.HttpServletRequest;

/**
 * 统一处理导入
 *

 * @date 2019年2月25日
 */
public interface IAjaxUploadFactory extends BaseFactory<BaseForm, BaseVo> {

    /**
     * 上传文件
     *
     * @param request 请求
     * @return 结果
     */
    String upload(HttpServletRequest request);

    /**
     * 导入数据
     *
     * @param request    请求
     * @param importFile 参数对象
     */
    void importOwner(HttpServletRequest request, ImportFileForm importFile);

}
