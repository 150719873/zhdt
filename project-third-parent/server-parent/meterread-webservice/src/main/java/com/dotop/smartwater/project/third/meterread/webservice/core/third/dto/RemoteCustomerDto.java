package com.dotop.smartwater.project.third.meterread.webservice.core.third.dto;

import com.dotop.smartwater.dependence.core.common.BaseDto;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 客户资料
 *
 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class RemoteCustomerDto extends BaseDto {

    /**
     * 业主id
     */
    private String ownerid;
    /**
     * 厂家ID(否)厂家ID对应tRemote_Factory表的编码字段
     */
    private int factoryId;
    /**
     * 水表编码(否)水表编码为每个厂家的唯一标识
     */
    private String meterAddr;

    /**
     * 客户名称(否)
     */
    private String userName;
    /**
     * 联系人
     */
    private String linkman;
    /**
     * 联系电话
     */
    private String phone;
    /**
     * 身份证号码
     */
    private String paperNo;
    /**
     * 详细地址
     */
    private String address;
    /**
     * 水表口径
     */
    private String caliber;
    /**
     * 装表时间
     */
    private Date installDate;
    /**
     * 是否阀控表(否)0：否；1：是
     */
    private int ifCtrlValve;
    /**
     * 备注
     */
    private String remark;

    /**
     * 旧表ID
     */
//    private Integer oldMeterID;
    /**
     * 客户编码(是)在远程水表系统里面的客户编码
     */
    private String userCode;
    /**
     * 旧水表编码
     */
    private String oldMeterAddr;
    /**
     * 扩展字段，水务系统的ownerid
     */
    private String extendData1;
    /**
     * 扩展字段,中间库的meterAddr+水务系统的ownerid的md5
     * 该值与水务系统的devno+水务系统的ownerid的md5比较，如果相同不做换表
     */
    private String extendData2;
    /**
     * 扩展字段，更新时间，yyyy-MM-dd HH:mm:ss
     */
    private String extendData3;
    /**
     * 最新表数
     */
    private String latestNumber;

    /**
     * 换表水量
     */
    private String wateyield;



}
