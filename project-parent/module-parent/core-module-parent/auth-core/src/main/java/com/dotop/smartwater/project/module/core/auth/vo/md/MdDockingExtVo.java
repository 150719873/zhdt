package com.dotop.smartwater.project.module.core.auth.vo.md;


import com.dotop.smartwater.dependence.core.common.BaseVo;
import com.dotop.smartwater.project.module.core.auth.vo.MdDockingTempVo;
import com.dotop.smartwater.project.module.core.auth.vo.MdDockingVo;
import com.dotop.smartwater.project.module.core.auth.vo.MdDockingTempVo;
import com.dotop.smartwater.project.module.core.auth.vo.MdDockingVo;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
public class MdDockingExtVo extends BaseVo {
    private String factory;
    private MdDockingVo mdDockingVo;
    private List<MdDockingTempVo> list;

}