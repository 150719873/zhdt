package com.dotop.smartwater.project.module.core.auth.vo.select;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @program: project-parent
 * @description:

 * @create: 2019-03-15 14:40
 **/
@Data
@EqualsAndHashCode(callSuper = false)
public class UserAndRoleObj {
    private String type;
    private String name;
    private List<Obj> list;
}
