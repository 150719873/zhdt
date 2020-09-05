package com.dotop.smartwater.project.third.meterread.client.core.third.bo;

import com.dotop.smartwater.dependence.core.common.BaseBo;
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
public class RemoteCustomerBo extends BaseBo {

    /**
     * 厂家ID(否)厂家ID对应tRemote_Factory表的编码字段
     */
    private Integer factoryId;
    /**
     * 水表编码(否)水表编码为每个厂家的唯一标识
     */
    private String meterAddr;
    /**
     * 水表ID(是)业务软件里面客户的唯一标识,与收费系统的水表ID对应关联字段
     */
    private Integer meterId;
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
    private Integer ifCtrlValve;
    /**
     * 备注
     */
    private String remark;
    /**
     * 是否导入(否)默认值为：0，导入之后变为：1
     */
    private Integer imported;
    /**
     * 旧表ID
     */
    private Integer oldMeterID;
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
     * 标志	INT	(否) 0：关阀标志；1：开阀标志
     * 该字段保存为null
     */
    private String valveFlag;


}
