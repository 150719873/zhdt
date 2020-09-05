package com.dotop.smartwater.project.third.server.meterread.core.third.dto;

import com.dotop.smartwater.dependence.core.common.BaseDto;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 开关阀控制
 *
 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class RemoteValveDto extends BaseDto {

    /**
     * 厂家ID(否)厂家ID对应tRemote_Factory表的编码字段
     */
    private int factoryId;
    /**
     * 水表编码(否)水表编码为每个厂家的唯一标识
     */
    private String meterAddr;

    /**
     * 标志	INT	(否) 0：关阀标志；1：开阀标志
     */
    private int valveFlag;
    /**
     * 提交时间 默认服务器当前时间
     */
    private Date insertDate;
    /**
     * 是否处理 0：未处理；1：正在处理 2：已处理
     */
    private int ifProcess;
    /**
     * 处理时间
     */
    private Date processDate;
    /**
     * 处理结果 0失败；1成功
     */
    private int ProcessResult;
    /**
     * 处理结果描述 成功或者失败等
     */
    private String ProcessDesc;

    /**
     * 数据库自增长，insert时不用赋值
     */
    private int ID;

    /**
     * 扩展字段，水务系统的clientdi
     */
    private String extendData1;
    /**
     * 执行次数
     */
    private Integer extendData2;
}
