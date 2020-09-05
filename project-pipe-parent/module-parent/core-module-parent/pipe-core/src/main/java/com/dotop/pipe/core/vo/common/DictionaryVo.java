package com.dotop.pipe.core.vo.common;

import com.dotop.smartwater.dependence.core.common.pipe.BasePipeVo;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
public class DictionaryVo extends BasePipeVo {

    private String id;

    private String name;

    private String val;

    private String des;
    // 类型
    private String type;
    // 单位
    private String unit;

    private Integer isDel;

    private List<DictionaryVo> children;

}
