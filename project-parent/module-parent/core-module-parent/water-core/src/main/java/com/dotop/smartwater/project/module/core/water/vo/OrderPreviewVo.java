package com.dotop.smartwater.project.module.core.water.vo;

import com.dotop.smartwater.dependence.core.common.BaseVo;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @program: project-parent
 * @description: 账单预览

 * @create: 2019-02-26 16:57
 **/
@Data
@EqualsAndHashCode(callSuper = false)
public class OrderPreviewVo extends BaseVo {
    private String ownerid;

    private String enterpriseid;

    private String tradeno;

    private String year;

    private String month;

    private String communityid;

    private String communityno;

    private String communityname;

    private String userno;

    private String username;

    private String phone;

    private String cardid;

    private String addr;

    private String pricetypeid;

    private String pricetypename;

    private String deveui;

    private String devno;

    private Integer devstatus;
    
    private String devstatusText;

    private String explain;

    private Integer tapstatus;

    private Integer taptype;

    private String upreadtime;

    private Double upreadwater;

    private String readtime;

    private Double readwater;

    private Integer day;

    private Double water;

    private String payno;

    private BigDecimal original;

    private Double amount;

    private Double balance;

    private Double realamount;

    private Integer paytype;

    private Integer paystatus;

    private Date paytime;

    private String operatorid;

    private String operatorname;

    private Date operatortime;

    private Integer tradestatus;

    private String tradestatusText;
    
    private String describe;

    private Integer errortype;

    private String generateuserid;

    private String generateusername;

    private String generatetime;

    /*减免id*/
    private String reduceid;

    /*水费用途id*/
    private String purposeid;

    //区域集合
    private String cids;

    //审核状态
    private String approval_status;

    private String approvalStatusText;
    
    //审核结果
    private String approval_result;
    
    private String approvalResultText;

    //水表类型
    private String typeid;
}
