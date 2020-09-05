package com.dotop.smartwater.project.module.api.mode.inf;

import com.dotop.smartwater.project.module.core.water.bo.customize.DownLinkDataBo;
import com.dotop.smartwater.project.module.core.water.form.DeviceForm;
import com.dotop.smartwater.project.module.core.water.vo.DeviceVo;

import java.util.Map;

/**
 * @program: project-parent
 * @description: 东信协议接口

 * @create: 2019-10-15 09:50
 **/
public interface DongXinInf{
    /**
     * 执行命令
     *
     * @param deviceVo 设备对象
     * @param command 命令
     * @param value 值
     * @param expire 过期时间
     * @param downLinkDataBo 下发对象
     * @return 结果Map
     */
    Map<String, String> deviceDownLink(DeviceVo deviceVo, int command, String value, Integer expire,
                                       DownLinkDataBo downLinkDataBo);
}
