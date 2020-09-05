package com.dotop.smartwater.project.module.core.auth.vo;

import java.util.List;

import com.dotop.smartwater.dependence.core.common.BaseVo;

import lombok.Data;
import lombok.EqualsAndHashCode;
@Data
@EqualsAndHashCode(callSuper = false)
public class MenuVo extends BaseVo {
 	private String menuid;

    private String parentid;

    private String name;

    private String uri;

    private String iconuri;
    
    private String introduction;
    
    private Integer level;
    
    private Integer type;
    
    private Integer isselect;

    private List<MenuVo> child;

    private String modelid;

    private String systype;
    //标识是操作谁的菜单(如运维/水司)
    private Integer flag;
    // 顶级路由标识
    private String routenode;
}
