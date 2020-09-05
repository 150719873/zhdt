package com.dotop.water.tool;

import com.dotop.smartwater.dependence.core.utils.JSONUtils;
import com.dotop.water.tool.exception.BusinessException;
import com.dotop.water.tool.service.AppInf;
import com.dotop.smartwater.project.module.core.auth.config.WaterClientConfig;
import com.dotop.smartwater.project.module.core.auth.vo.UserParamVo;

public class AppMain {

	public static void main(String[] args) throws BusinessException {
		WaterClientConfig.WaterCasUrl = "http://localhost:8888";

		// 以下为例子
		try {
			System.out.println("GetAppEnterprises: " + JSONUtils.toJSONString(AppInf.getAppEnterprises()));
			UserParamVo userParamVo = AppInf.appLogin("xxxxx", "xxxx", "44");
			System.out.println("AppLogin: " + JSONUtils.toJSONString(userParamVo));
			System.out.println("Verification: "
					+ JSONUtils.toJSONString(AppInf.verification(userParamVo.getUserid(), userParamVo.getTicket())));

			System.out.println("getUserInfo: " + JSONUtils.toJSONString(AppInf.getUserInfo("179")));
		} catch (BusinessException ex) {
			System.out.println(ex.getErrorCode());
		}

	}
}
