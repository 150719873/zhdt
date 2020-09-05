package com.dotop.smartwater.project.module.api.revenue.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.project.module.api.revenue.IEverydayWaterRecordFactory;
import com.dotop.smartwater.project.module.core.auth.local.AuthCasClient;
import com.dotop.smartwater.project.module.core.auth.vo.AreaNodeVo;
import com.dotop.smartwater.project.module.core.auth.vo.UserVo;
import com.dotop.smartwater.project.module.core.water.bo.EverydayWaterRecordBo;
import com.dotop.smartwater.project.module.core.water.form.EverydayWaterRecordForm;
import com.dotop.smartwater.project.module.core.water.vo.customize.CommunityDeviceCountVo;
import com.dotop.smartwater.project.module.core.water.vo.customize.CommunityOwnpayVo;
import com.dotop.smartwater.project.module.core.water.vo.customize.CommunityWaterVo;
import com.dotop.smartwater.project.module.core.water.vo.customize.StatisticsVo;
import com.dotop.smartwater.project.module.service.revenue.IEverydayWaterRecordService;
import com.dotop.water.tool.service.BaseInf;

/**
 * 每日用水量
 * 

 * @date 2019/2/26.
 */
@Component
public class EverydayWaterRecordFactoryImpl implements IEverydayWaterRecordFactory {

	@Autowired
	private IEverydayWaterRecordService iEverydayWaterRecordService;

	@Override
	public List<CommunityWaterVo> getWaterByMonth() {
		UserVo user = AuthCasClient.getUser();

		Map<String, AreaNodeVo> map = BaseInf.getAreaMaps(user.getUserid(), user.getTicket());
		if (map.isEmpty()) {
			return Collections.emptyList();
		}
		StringBuilder ids = new StringBuilder();
		for (String key : map.keySet()) {
			ids.append(key).append(",");
		}
		String str = ids.toString();
		str = str.substring(0, str.length() - 1);

		return iEverydayWaterRecordService.getWaterByMonth(user.getEnterpriseid(), str);
	}

	public List<StatisticsVo> getDeviceModes() {
		UserVo user = AuthCasClient.getUser();
		List<StatisticsVo> list = iEverydayWaterRecordService.getDeviceModes(user.getEnterpriseid());
		if (list != null && list.size() > 0) {
			for (StatisticsVo vo : list) {
				if (StringUtils.isEmpty(vo.getName())) {
					vo.setName("传统表");
				}
			}
		}
		return list;
	}
	
	
	public List<StatisticsVo> getDeviceWarns() {
		UserVo user = AuthCasClient.getUser();
		return iEverydayWaterRecordService.getDeviceWarns(user.getEnterpriseid());
	}
	
	
	
	public List<StatisticsVo> getDeviceModels() {
		UserVo user = AuthCasClient.getUser();
		return iEverydayWaterRecordService.getDeviceModels(user.getEnterpriseid());
	}
	
	public List<StatisticsVo> getDevicePurposes() {
		UserVo user = AuthCasClient.getUser();
		return iEverydayWaterRecordService.getDevicePurposes(user.getEnterpriseid());
	}
	
	
	
	@Override
	public List<CommunityDeviceCountVo> getDeviceCount() {
		UserVo user = AuthCasClient.getUser();

		Map<String, AreaNodeVo> map = BaseInf.getAreaMaps(user.getUserid(), user.getTicket());
		if (map == null || map.size() == 0) {
			return Collections.emptyList();
		}

		List<CommunityDeviceCountVo> list = iEverydayWaterRecordService.getDeviceCount(user.getEnterpriseid());

		if (list.isEmpty()) {
			return Collections.emptyList();
		}

		List<CommunityDeviceCountVo> list2 = new ArrayList<>();
		for (CommunityDeviceCountVo cd : list) {
			// 0不显示
			// 找不到说明没有权限
			AreaNodeVo areaNode = map.get(String.valueOf(cd.getCommunityid()));
			if (0 == cd.getCount() || areaNode == null) {
				continue;
			}
			cd.setName(areaNode.getTitle());
			list2.add(cd);
		}

		return list2;
	}

	@Override
	public Map<String, Object> getCommunityOwnpay() {
		UserVo user = AuthCasClient.getUser();
		Map<String, Object> map = new HashMap<>(16);
		final String OWNPAY_MONTH_STR = "ownpayMonthList";
		// 当月欠费列表数据
		List<CommunityOwnpayVo> list = iEverydayWaterRecordService.getCommunityOwnpay(user.getEnterpriseid());
		if (list.isEmpty()) {
			map.put(OWNPAY_MONTH_STR, list);
			return map;
		}
		Map<String, AreaNodeVo> areaMap = BaseInf.getAreaMaps(String.valueOf(user.getUserid()), user.getTicket());
		if (areaMap.isEmpty()) {
			map.put(OWNPAY_MONTH_STR, list);
			return map;
		}

		List<CommunityOwnpayVo> list2 = new ArrayList<>();
		for (CommunityOwnpayVo cd : list) {
			// 找不到说明没有权限
			AreaNodeVo areaNode = areaMap.get(String.valueOf(cd.getCommunityid()));
			if (areaNode == null) {
				continue;
			}
			cd.setCommunityname(areaNode.getTitle());
			list2.add(cd);
		}

		map.put(OWNPAY_MONTH_STR, list2);

		return map;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
	public int insert(EverydayWaterRecordForm everydayWaterRecordForm) {
		UserVo user = AuthCasClient.getUser();
		String userBy = user.getAccount();
		Date curr = new Date();
		// 业务逻辑
		EverydayWaterRecordBo everydayWaterRecordBo = new EverydayWaterRecordBo();
		BeanUtils.copyProperties(everydayWaterRecordForm, everydayWaterRecordBo);
		everydayWaterRecordBo.setEnterpriseid(user.getEnterpriseid());
		everydayWaterRecordBo.setUserBy(userBy);
		everydayWaterRecordBo.setCurr(curr);
		return iEverydayWaterRecordService.insert(everydayWaterRecordBo);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
	public int addEveryDayWaterRecord() {
		return iEverydayWaterRecordService.addEveryDayWaterRecord();
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
	public int updateEveryDayWaterRecord() {
		return iEverydayWaterRecordService.updateEveryDayWaterRecord();
	}
}
