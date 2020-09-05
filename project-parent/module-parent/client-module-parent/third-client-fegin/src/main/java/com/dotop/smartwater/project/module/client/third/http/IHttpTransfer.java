package com.dotop.smartwater.project.module.client.third.http;

import com.dotop.smartwater.project.module.core.auth.bo.UserLoraBo;
import com.dotop.smartwater.project.module.core.water.bo.DeviceBo;
import com.dotop.smartwater.project.module.core.water.bo.customize.DownLinkDataBo;
import com.dotop.smartwater.project.module.core.water.vo.customize.IotMsgEntityVo;

public interface IHttpTransfer {

	/**
	 * 获取登陆信息
	 * @param userLora
	 * @return
	 */
	IotMsgEntityVo getLoginInfo(UserLoraBo userLora);

	/**
	 * 设备添加到IOT平台
	 * @param token
	 * @param device
	 * @param userLora
	 * @return
	 */
	IotMsgEntityVo addDevice(String token, DeviceBo device, UserLoraBo userLora);

	/**
	 * 设备从IOT平台删除（解绑）
	 * @param token
	 * @param device
	 * @param userLora
	 * @return
	 */
	IotMsgEntityVo delDevice(String token, DeviceBo device, UserLoraBo userLora);

	
	/**
	 * 设备从IOT平台删除（彻底删除设备）
	 * @param token
	 * @param device
	 * @param userLora
	 * @return
	 */
	IotMsgEntityVo cleanDevice(String token, DeviceBo device, UserLoraBo userLora);
	
	
	/**
	 * 发送下载请求
	 * @param expire
	 * @param device
	 * @param command
	 * @param value
	 * @param token
	 * @param userLora
	 * @param downLinkData
	 * @return
	 */
	IotMsgEntityVo sendDownLoadRequest(Integer expire, DeviceBo device, int command, String value, String token,
			UserLoraBo userLora, DownLinkDataBo downLinkData);

}
