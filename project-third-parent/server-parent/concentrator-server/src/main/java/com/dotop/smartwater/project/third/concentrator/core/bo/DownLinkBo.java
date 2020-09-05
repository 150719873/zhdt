package com.dotop.smartwater.project.third.concentrator.core.bo;

import com.dotop.smartwater.dependence.core.common.BaseBo;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 下发命令
 *
 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class DownLinkBo extends BaseBo {

    /**
     * 集中器
     */
    private ConcentratorBo concentrator;

    /**
     * 任务类型(常量)
     */
    private String taskType;


}
