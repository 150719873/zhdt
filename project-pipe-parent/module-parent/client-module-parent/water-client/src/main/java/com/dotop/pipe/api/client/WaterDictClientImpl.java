package com.dotop.pipe.api.client;

import com.alibaba.fastjson.TypeReference;
import com.dotop.pipe.api.client.core.PlsDictionaryCode;
import com.dotop.pipe.api.client.fegin.water.IWaterFeginClient;
import com.dotop.pipe.auth.cas.web.IAuthCasWeb;
import com.dotop.pipe.auth.core.vo.auth.LoginCas;
import com.dotop.pipe.core.vo.common.DictionaryVo;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.log.LogMsg;
import com.dotop.smartwater.dependence.core.utils.JSONObjects;
import com.dotop.smartwater.dependence.core.utils.JSONUtils;
import com.dotop.smartwater.project.module.core.water.constants.ResultCode;
import com.dotop.smartwater.project.module.core.water.form.DictionaryForm;
import com.dotop.smartwater.project.module.core.water.vo.DictionaryChildVo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class WaterDictClientImpl implements IWaterDictClient {

	private final static Logger logger = LogManager.getLogger(WaterDictClientImpl.class);

	@Autowired
	private IAuthCasWeb iAuthCasApi;

	@Autowired
	private IWaterFeginClient iWaterFeginClient;

	@Override
	public DictionaryVo get(String category) throws FrameworkRuntimeException {
		LoginCas loginCas = iAuthCasApi.get(false);
		if (loginCas != null) {
			// 调用接口
			DictionaryForm dictionaryForm = new DictionaryForm();
			dictionaryForm.setDictionaryId(PlsDictionaryCode.getDictionaryId(category));
			String result = iWaterFeginClient.dictList(dictionaryForm, loginCas.getUserId(), loginCas.getTicket());
			JSONObjects jsonObjects = JSONUtils.parseObject(result);
			String code = jsonObjects.getString("code");
			if (!ResultCode.Success.equals(code)) {
				return null;
			}
			List<com.dotop.smartwater.project.module.core.water.vo.DictionaryVo> datas = jsonObjects.getObject(
					"data",
					new TypeReference<List<com.dotop.smartwater.project.module.core.water.vo.DictionaryVo>>() {
					});
			logger.info(LogMsg.to("datas", datas));
			if (datas != null && !datas.isEmpty()) {
				return build(datas.get(0));
			}
			return null;
		} else {
			return null;
		}
	}

	private DictionaryVo build(com.dotop.smartwater.project.module.core.water.vo.DictionaryVo wd) {
		// 父类组装
		DictionaryVo d = new DictionaryVo();
		d.setId(wd.getDictionaryId());
		d.setName(wd.getDictionaryName());
		d.setVal(wd.getDictionaryValue());
		// 子类组装
		List<DictionaryVo> children = new ArrayList<>();
		d.setChildren(children);
		List<DictionaryChildVo> wdcs = wd.getChildren();
		for (DictionaryChildVo wdc : wdcs) {
			DictionaryVo dc = new DictionaryVo();
			children.add(dc);
			dc.setId(wdc.getChildId());
			dc.setName(wdc.getChildName());
			dc.setVal(wdc.getChildValue());
		}
		return d;
	}
}
