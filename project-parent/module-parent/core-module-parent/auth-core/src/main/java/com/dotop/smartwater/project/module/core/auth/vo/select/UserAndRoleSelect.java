package com.dotop.smartwater.project.module.core.auth.vo.select;

import com.dotop.smartwater.dependence.core.common.BaseVo;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @program: project-parent
 * @description: 选择用户或角色的下拉

 * @create: 2019-03-14 15:19
 **/
@Data
@EqualsAndHashCode(callSuper = false)
public class UserAndRoleSelect extends BaseVo {
    private UserAndRoleObj users;
    private UserAndRoleObj roles;
}
