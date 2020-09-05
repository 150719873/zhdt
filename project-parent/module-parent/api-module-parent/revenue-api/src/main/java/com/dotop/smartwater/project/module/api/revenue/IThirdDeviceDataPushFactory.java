package com.dotop.smartwater.project.module.api.revenue;

import java.util.List;

import com.dotop.smartwater.project.module.core.auth.vo.UserVo;
import com.dotop.smartwater.project.module.core.water.form.DeviceUplinkForm;

/**
 * 推送处理
+YangKe
 * @date 2019年8月16日
 */
public interface IThirdDeviceDataPushFactory{

	String batchUplink(List<DeviceUplinkForm> list, UserVo user);
}
