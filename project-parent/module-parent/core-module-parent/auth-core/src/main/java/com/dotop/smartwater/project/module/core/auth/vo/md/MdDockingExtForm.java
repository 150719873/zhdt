package com.dotop.smartwater.project.module.core.auth.vo.md;


import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
public class MdDockingExtForm{
    private String mdeId;
    private List<MdDockingExtVo> list;


}