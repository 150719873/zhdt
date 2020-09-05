package com.dotop.smartwater.project.third.server.meterread.core.third.vo;

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
     * 厂家ID(否)厂家ID对应tRemote_Factory表的编码字段
     */
    private int factoryId;
    /**
     * 水表编码(否)水表编码为每个厂家的唯一标识
     */
    private String meterAddr;

    /**
     * 最新读数
     */
    private int readNumber;
    /**
     * 抄表读数
     */
    private Date readDate;

    /**
     * 数据库自增长，insert时不用赋值
     */
    private int id;
    /**
     * 最后更新时间
     */
    private Date lastUpdateTime;


}
