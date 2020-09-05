package com.dotop.deyang.controller;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.dotop.deyang.dc.model.deyang.DeYangParams;
import com.dotop.deyang.dc.service.deyang.IThirdFactory;
import com.dotop.deyang.dc.model.deyang.MeterArchive;
import com.dotop.deyang.dc.model.deyang.MeterChangeLog;
import com.dotop.deyang.dc.model.deyang.MeterValue;
import com.dotop.deyang.dc.model.global.GlobalContext;
import com.dotop.deyang.util.JacksonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.*;


@RestController
@RequestMapping("/auth/deyang")
public class DeYangController{
	@Resource
	private IThirdFactory iThirdFactory;

	private static final Logger logger = LoggerFactory.getLogger(DeYangController.class);
	@PostMapping(value = "/GetMeterArchive", produces = GlobalContext.PRODUCES)
	public String GetMeterArchive(
			@RequestBody DeYangParams deYangParams) {

		logger.info("FromDate:" + deYangParams.getFromDate() + ";" + "ToDate:" + deYangParams.getToDate() + ";");
		List<MeterArchive> list = iThirdFactory.GetMeterArchive(deYangParams.getFromDate(), deYangParams.getToDate());

		Map<String, Object> map = new HashMap<>();
		map.put("meterArchive", list);
		String json = JSON.toJSONString(map, SerializerFeature.DisableCircularReferenceDetect,
				SerializerFeature.WriteMapNullValue);
		logger.info("json:" + json);
		logger.info("");
		return json;
	}

	@PostMapping(value = "/SyncCustomerID", produces = GlobalContext.PRODUCES)
	public String SyncCustomerID(
			@RequestBody DeYangParams deYangParams) {

		logger.info("MeterNoCustomerIDList:" + JacksonUtils.SerializeToString(deYangParams.getMeterNoCustomerIDList()) + ";");
		return "0";
	}

	@PostMapping(value = "/ReadMeterValue", produces = GlobalContext.PRODUCES)
	public String ReadMeterValue(
			@RequestBody DeYangParams deYangParams) {
		logger.info("IDList:" + deYangParams.getIdList() + ";" + "ReadMeterDate:" + deYangParams.getReadMeterDate() + ";"
				+ "IDType:" + deYangParams.getIdType() + ";");
		List<MeterValue> list = new ArrayList<>();
		String[] idsArr = deYangParams.getIdList().split(",");
		List<String> ids = Arrays.asList(idsArr);
		if (deYangParams.getIdType() == 0) {
			list = iThirdFactory.ReadMeterValue0(ids, deYangParams.getReadMeterDate());
		} else if (deYangParams.getIdType() == 1) {
			list = iThirdFactory.ReadMeterValue1(ids, deYangParams.getReadMeterDate());
		}
		Map<String, Object> map = new HashMap<>();
		map.put("meterValue", list);
		String json = JSON.toJSONString(map, SerializerFeature.DisableCircularReferenceDetect,
				SerializerFeature.WriteMapNullValue);
		logger.info("json:" + json);
		logger.info("");
		return json;
	}

	@PostMapping(value = "/ReadOneMeterValue", produces = GlobalContext.PRODUCES)
	public String ReadOneMeterValue(
			@RequestBody DeYangParams deYangParams) {

		logger.info("No:" + deYangParams.getNo() + ";" + "ReadMeterDate:" + deYangParams.getReadMeterDate() + ";"
				+ "IDType:" + deYangParams.getIdType() + ";");
		List<MeterValue> list = new ArrayList<>();
		List<String> ids = new ArrayList<>();
		ids.add(deYangParams.getNo());
		if (deYangParams.getIdType() == 0) {
			list = iThirdFactory.ReadMeterValue0(ids, deYangParams.getReadMeterDate());
		} else if (deYangParams.getIdType() == 1) {
			list = iThirdFactory.ReadMeterValue1(ids, deYangParams.getReadMeterDate());
		}
		Map<String, Object> map = new HashMap<>();
		map.put("meterValue", list);
		String json = JSON.toJSONString(map, SerializerFeature.DisableCircularReferenceDetect,
				SerializerFeature.WriteMapNullValue);
		logger.info("json:" + json);
		logger.info("");
		return json;
	}

	@PostMapping(value = "/QueryMeterChangeLog", produces = GlobalContext.PRODUCES)
	public String QueryMeterChangeLog(
			@RequestBody DeYangParams deYangParams) {

		logger.info("FromDate:" + deYangParams.getFromDate() + ";" + "ToDate:" + deYangParams.getToDate() + ";");
		List<MeterChangeLog> list = iThirdFactory.QueryMeterChangeLog(deYangParams.getFromDate(), deYangParams.getToDate());

		Map<String, Object> map = new HashMap<>();
		map.put("meterChangeLog", list);
		String json = JSON.toJSONString(map, SerializerFeature.DisableCircularReferenceDetect,
				SerializerFeature.WriteMapNullValue);
		logger.info("json:" + json);
		logger.info("");
		return json;
	}
}
