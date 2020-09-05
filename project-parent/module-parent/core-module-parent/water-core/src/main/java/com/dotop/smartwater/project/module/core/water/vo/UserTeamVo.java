package com.dotop.smartwater.project.module.core.water.vo;

import com.dotop.smartwater.dependence.core.common.BaseVo;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class UserTeamVo extends BaseVo {
	private String userid;

    private String name;
    
    private String account;

	private String worknum;

	private Integer usertype;

    private String description;

    private String password;

//    private String enterpriseid;
    
    private String roleid;
    
    private Integer type;

    private String phone;

	private String email;
  
    private String createtime;

    private String creater;
    
    private String accounttype;
}
