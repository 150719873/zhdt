package com.dotop.water.tool.service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

import com.dotop.smartwater.dependence.core.utils.JSONObjects;
import com.dotop.smartwater.dependence.core.utils.JSONUtils;
import com.dotop.water.tool.exception.BusinessException;
import com.dotop.smartwater.project.module.core.auth.bo.MakeNumRequestBo;
import com.dotop.smartwater.project.module.core.auth.config.WaterClientConfig;
import com.dotop.smartwater.project.module.core.auth.constants.AuthResultCode;
import com.dotop.smartwater.project.module.core.auth.vo.MakeNumVo;
import com.dotop.water.tool.util.HttpUtil;

/**

 * @date 2019年5月9日
 * @description 调用客户端
 */
public final class NumRuleSetInf {

	private NumRuleSetInf() {

	}

	public static MakeNumVo getSerialNumber(String userid, String ticket, Integer ruleid, Integer count)
			throws BusinessException {
		try {
			HashMap<String, String> map = new HashMap<>();
			map.put("userid", String.valueOf(userid));
			map.put("ticket", ticket);

			MakeNumRequestBo makeNumRequest = new MakeNumRequestBo();
			makeNumRequest.setRuleid(ruleid);
			makeNumRequest.setCount(count);

			String data = HttpUtil.post(WaterClientConfig.WaterCasUrl + "/auth/numRuleSet/makeNo", map,
					JSONUtils.toJSONString(makeNumRequest).getBytes(StandardCharsets.UTF_8));
			JSONObjects parseObject = JSONUtils.parseObject(data);
			String code = parseObject.getString("code");
			if (!AuthResultCode.Success.equals(code)) {
				throw new BusinessException(code);
			}
			return JSONUtils.parseObject(parseObject.getString("data"), MakeNumVo.class);
		} catch (IOException e) {
			throw new BusinessException(AuthResultCode.TimeOut, e);
		} catch (BusinessException e) {
			throw e;
		} catch (Exception e) {
			throw new BusinessException(AuthResultCode.Fail, e.getMessage(), e);
		}
	}

}
