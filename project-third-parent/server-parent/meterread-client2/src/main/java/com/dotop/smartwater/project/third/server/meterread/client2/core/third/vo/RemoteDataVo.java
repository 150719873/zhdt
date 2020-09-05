package com.dotop.smartwater.project.third.server.meterread.client2.core.third.vo;

import com.dotop.smartwater.dependence.core.common.BaseVo;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 抄表数据
 *
 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class RemoteDataVo extends BaseVo {

    /**
     * 主键
     */
    private int id;

    /**
     * 水表编码(否)水表编码为每个厂家的唯一标识
     */
    private String meterId;

    /**
     * 业主编码
     */
    private String userCode;

    /**
     * 抄表时间
     */
    private Date readDate;
    /**
     * 最新读数
     */
    private String readNumber;

}
