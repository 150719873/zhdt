package com.dotop.smartwater.project.module.core.water.vo;

import com.dotop.smartwater.dependence.core.common.BaseVo;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @program: project-parent
 * @description: 收费种类

 * @create: 2019-02-26 09:18
 **/
@Data
@EqualsAndHashCode(callSuper = false)
public class CompriseVo extends BaseVo {
    private String id;

    private String typeid;

    private String name;

    private String ratio;

    private double price;

    private double unitprice;

    private String starttime;

    private Integer enable;

    private Integer print;

    private String remark;

    private String createtime;
}
