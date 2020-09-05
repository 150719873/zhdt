package com.dotop.smartwater.project.module.core.water.dto;

import com.dotop.smartwater.dependence.core.common.BaseDto;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @program: project-parent
 * @description: 构造参数对象

 * @create: 2019-03-02 14:12
 **/
@Data
@EqualsAndHashCode(callSuper = false)
public class OrderLastUplinkDto extends BaseDto {
    private String type;
    private String communityIds;
    private String usernos;
    private String currentMonth;
    private String lastMonth;
    private String meterTime;
}
