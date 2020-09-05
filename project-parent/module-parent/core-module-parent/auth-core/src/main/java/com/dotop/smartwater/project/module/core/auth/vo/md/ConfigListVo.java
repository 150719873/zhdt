package com.dotop.smartwater.project.module.core.auth.vo.md;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @program: project-parent
 * @description:

 * @create: 2020-06-01 09:59
 **/
@Data
@EqualsAndHashCode(callSuper = false)
public class ConfigListVo {
    private List<ConfigListBaseVo> systemList;
    private List<ConfigListBaseVo> productList;
    private List<ConfigListBaseVo> modeList;
}
