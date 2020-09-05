package com.dotop.smartwater.project.module.core.water.utils;

import com.dotop.smartwater.project.module.core.auth.vo.MenuVo;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @program: project-parent
 * @description: 菜单有关的公共方法

 * @create: 2020-05-25 14:57
 **/
public class MenuUtils {
    public static List<MenuVo> filterChild(List<MenuVo> menus){
        if(CollectionUtils.isEmpty(menus)){
            return Collections.emptyList();
        }
        List<MenuVo> results = new ArrayList<>();
        for(MenuVo m:menus){
            if("0".equals(m.getParentid())){
                results.add(m);
            }
        }
        return results;
    }
}
