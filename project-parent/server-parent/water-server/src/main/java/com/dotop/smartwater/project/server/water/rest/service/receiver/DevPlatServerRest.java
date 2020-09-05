package com.dotop.smartwater.project.server.water.rest.service.receiver;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.dotop.smartwater.dependence.core.global.GlobalContext;
import com.dotop.smartwater.dependence.core.utils.JSONUtils;
import com.dotop.smartwater.project.module.api.receiver.DevPlatHandler;
import com.dotop.smartwater.project.module.core.third.form.iot.DownlinkDataStatusForm;
import com.dotop.smartwater.project.module.core.third.form.iot.PushDataForm;

/**
 * 
 * @ClassName: DevPlatServerRest
 * @Description: 接收开发者平台

 * @date 2018年4月16日 上午9:53:45
 * 
 *       原com.dotop.water.rest.DevPlatServerRest
 */
@RestController

@RequestMapping()
public class DevPlatServerRest {

	private static final Logger LOGGER = LogManager.getLogger(DevPlatServerRest.class);

	public static final String REGISTDEVICE = "registDevice";// 注册
	public static final String ACTIVEDEVICE = "activeDevice";// 激活
	public static final String DEVICEDATACHANGED = "deviceDataChanged";// 上行数据
	public static final String COMMANDRESPONSE = "commandResponse";// 下行反馈

	public static final String REGISTDEVICE_NO = "1";// 注册
	public static final String ACTIVEDEVICE_NO = "2";// 激活
	public static final String DEVICEDATACHANGED_NO = "3";// 上行数据
	public static final String COMMANDRESPONSE_NO = "4";// 下行反馈

	@Autowired
	private DevPlatHandler devPlatHandler;

	/**
	 * 
	 * <p>
	 * Company:dotop
	 * </p>
	 * 
	 * @Title: ReceiveData
	 * @Description: 接收开发者平台发送过来数据-上行推送数

	 * @date 2018年4月16日 上午9:57:11
	 * @param subscribeType
	 * @param dataString
	 * @return
	 * @return String
	 * @throws @version V1.0.0
	 */
	@PostMapping(value = "/pushData", produces = GlobalContext.PRODUCES)
	public String pushData(@RequestBody String dataString, @RequestHeader("subscribeType") String subscribeType) {
		long time = System.currentTimeMillis();
		try {
			LOGGER.info("推送类型：" + subscribeType);
			LOGGER.info(dataString);

			// 上行的数据处理
			if (subscribeType.equals(DEVICEDATACHANGED) || subscribeType.equals(DEVICEDATACHANGED_NO)) {
				// 阀门类型:0:不带阀 1:带阀,-1默认不存在
				Integer taptype = null;
				PushDataForm data;
				data = JSONUtils.parseObject(dataString, PushDataForm.class);
				LOGGER.info("uplink serialize success!");
				if (data.getBase() != null) {
					// 不带阀
					if (data.getBase().getActDevmod().equals("00")) {
						taptype = 0;
						// 带阀
					} else if (data.getBase().getActDevmod().equals("01")) {
						taptype = 1;
					}

				}
				devPlatHandler.pushData(data, taptype);
			}
			// 下行的数据处理
			if (subscribeType.equals(COMMANDRESPONSE) || subscribeType.equals(COMMANDRESPONSE_NO)) {
				DownlinkDataStatusForm downlinkDataStatus = JSONUtils.parseObject(dataString,
						DownlinkDataStatusForm.class);
				LOGGER.info("downlink serialize success!");
				devPlatHandler.handle(downlinkDataStatus);
			}
		} catch (Exception e) {
			LOGGER.info("exception :" + e.getMessage());
			LOGGER.error("ReceiveData error", e);
		} finally {
			LOGGER.info(System.currentTimeMillis() - time);
		}

		return "0";
	}
}
