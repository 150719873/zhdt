package com.dotop.smartwater.dependence.core.common.pipe;

import com.dotop.smartwater.dependence.core.common.BaseBo;
import lombok.Getter;
import lombok.Setter;

/**
 * 基础类
 */
@Getter
@Setter
public class BasePipeBo extends BaseBo {

    private String enterpriseId;

    // 页码
    private Integer page;

    // 页数
    private Integer pageSize;

}
