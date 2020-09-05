package com.dotop.smartwater.project.module.core.water.form;

import com.dotop.smartwater.dependence.core.common.BaseForm;
import com.dotop.smartwater.project.module.core.water.model.SortModel;
import com.dotop.smartwater.project.module.core.water.form.customize.ExportFieldForm;
import com.dotop.smartwater.project.module.core.water.model.SortModel;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;
import java.util.List;

/**

 */
@Data
@EqualsAndHashCode(callSuper = true)
public class OwnerForm extends BaseForm {
    /** 表册号 */
    private String bookNum;

    private String ownerType;
    
    private String devno;

    private String deveui;

    private String ownerid;

    private String userno;

    private String username;

    private String useraddr;

    private String userphone;

    private Integer status;

    private String remark;

    private String devid;

    private String pricetypeid;

    private String communityid;

    private String createuser;

    private Date createtime;

    private String installmonth;

    private Double alreadypay;

    private String communityno;
    /** 区域名称 */
    private String communityname;
    /** 上期抄表时间 */
    private String upreadtime;
    /** 上期读数 */
    private String upreadwater;

    /** 收费种类ID */
    private String paytypeid;
    /** 收费种类 */
    private String paytypename;
    /** 水表用途 ID */
    private String purposeid;
    /** 水表用途 */
    private String purposename;

    /** 用水减免 */
    private String reduceid;

    private String reducename;

    /** 证件类型，1-身份证 2-护照 */
    private Integer cardtype;
    /** 证件号 */
    private String cardid;
    /** 是否自动扣费，1-扣费 0-不扣费 */
    private Integer ischargebacks;
    /** 设备类型 */
    private String modelid;

    private String modelname;

    private String enterpriseid;

    private Integer devStatus;

    private String tradeno;
    private String readtime;
    private String orderdate;
    private Integer timeinterval;
    private Double arrears = 0.0;

    // 水表读数
    private Double water;

    private String uplinktime;

    /** 此参数用于列表查询时根据分配区域读取 */
    private List<String> nodeIds;

    /** 关键字查询*/
    private String keywords;

    /** 是否在线 */
    private Integer isonline;

    private String reason;

    private String newdevno;

    private String descr;

    private String typeid;

    private String newuserno;

    private OwnerExtForm ownerExtForm;

    private String province;
    private String city;
    private String district;
    private String building;
    private String unit;
    private String room;
    private String box;
    
    //导出字段
 	private List<ExportFieldForm> fields;
 	
 	//二维码条码内容
 	private String barCode;
 	// 是否开户  1-开户 0-不开户
 	private String isOpen;
 	// 是否开户  1-更新 0-不更新
 	private String isUpdate;
 	
 	/**排序实体类*/
	private List<SortModel> sortList;
}
