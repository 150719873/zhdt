package com.dotop.smartwater.project.module.api.revenue;

import com.dotop.smartwater.dependence.core.common.BaseFactory;
import com.dotop.smartwater.dependence.core.common.BaseForm;
import com.dotop.smartwater.dependence.core.common.BaseVo;
import com.dotop.smartwater.project.module.core.water.form.customize.ImportFileForm;

import java.util.List;

/**
 * 批量导入抄表-上传
 *

 * @date 2019年2月25日
 */
public interface IManualFactory extends BaseFactory<BaseForm, BaseVo> {

    /**
     * /批量导入抄表-导入
     *
     * @param importfile 参数对象
     * @return 错误结果List
     */
    List<String> manualImport(ImportFileForm importfile);

    /**
     * 设备导入
     *
     * @param importfile 参数对象
     * @return 错误结果List
     */
    List<String> deviceImport(ImportFileForm importfile);
}
