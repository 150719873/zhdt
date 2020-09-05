package com.dotop.smartwater.project.third.concentrator.core.vo;

import com.dotop.smartwater.project.third.concentrator.core.bo.ConcentratorBo;
import com.dotop.smartwater.dependence.core.common.BaseVo;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 下发命令
 *
 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class DownLinkVo extends BaseVo {

    /**
     * 集中器
     */
    private ConcentratorBo concentrator;

    /**
     * 任务类型(常量)
     */
    private String taskType;


}
