package com.dotop.smartwater.project.third.module.core.third.video.vo;

import com.dotop.smartwater.dependence.core.common.BaseVo;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = true)
public class RecordVo  extends BaseVo {
    /**
     * 抄表记录id
     */
    private Integer id;

    /**
     * 客户编号
     */
    private String ccid;


    /**
     * 水表编号
     */
    private String meterid;

    /**
     * 设备编号
     */
    private String deviceid;

    /**
     * 核对状态
     */
    private String checked;

    /**
     * 核对者
     */
    private String checker;

    /**
     * 抄表时间
     */
    private Date readtime;

    /**
     * 抄表读数
     */
    private Double readcount;

    /**
     * 是否识别成功
     */
    private Boolean isready;

    /**
     * 图片url
     */
    private String pic;


}
