package com.dotop.smartwater.project.server.schedule.schedule;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSON;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.log.LogMsg;
import com.dotop.smartwater.dependence.core.utils.JSONUtils;
import com.dotop.smartwater.dependence.core.utils.StrUtils;
import com.dotop.smartwater.dependence.core.utils.UuidUtils;
import com.dotop.smartwater.dependence.core.utils.VerificationUtils;
import com.dotop.smartwater.project.module.api.device.IDeviceWarningFactory;
import com.dotop.smartwater.project.module.api.device.IDeviceWarningSettingFactory;
import com.dotop.smartwater.project.module.api.workcenter.IProcessHandleFactory;
import com.dotop.smartwater.project.module.api.workcenter.IWorkCenterBuildFactory;
import com.dotop.smartwater.project.module.core.auth.vo.UserVo;
import com.dotop.smartwater.project.module.core.water.bo.MessageBo;
import com.dotop.smartwater.project.module.core.water.config.Config;
import com.dotop.smartwater.project.module.core.water.constants.WaterConstants;
import com.dotop.smartwater.project.module.core.water.form.DeviceWarningForm;
import com.dotop.smartwater.project.module.core.water.form.DeviceWarningSettingForm;
import com.dotop.smartwater.project.module.core.water.form.WorkCenterProcessHandleForm;
import com.dotop.smartwater.project.module.core.water.form.customize.WorkCenterBuildForm;
import com.dotop.smartwater.project.module.core.water.vo.DeviceWarningSettingVo;
import com.dotop.smartwater.project.module.core.water.vo.DeviceWarningVo;
import com.dotop.smartwater.project.module.core.water.vo.customize.WorkCenterBuildVo;
import com.dotop.smartwater.project.module.service.tool.IEmailService;
import com.dotop.smartwater.project.module.service.tool.ISmsToolService;
import com.dotop.water.tool.service.BaseInf;

/**

 * @date 2019/4/3.
 */
//@Component
public class WarningNotifySchedule {

	private static final Logger LOGGER = LogManager.getLogger(WarningNotifySchedule.class);


	@Resource(name = "IWorkCenterBuildFactoryMap")
	private Map<String, IWorkCenterBuildFactory> iWorkCenterBuildFactoryMap;

	@Autowired
	private IProcessHandleFactory iProcessHandleFactory;

	@Autowired
	private IDeviceWarningSettingFactory iDeviceWarningSettingFactory;

	@Autowired
	private IDeviceWarningFactory iDeviceWarningFactory;

	@Autowired
	private ISmsToolService iSmsService;

	@Autowired
	private IEmailService iEmailService;

	// @Scheduled(cron = "0 0 6-12 * * ?")
	@Scheduled(initialDelay = 10000, fixedRate = 3600000)
	public void warningNotify() {
		LOGGER.info("warningNotify start ...");
		Long start = System.currentTimeMillis();
		try {
//			iDeviceWarningSettingFactory.warningNotify();
			warning();
		} catch (FrameworkRuntimeException e) {
			LOGGER.error("warningNotify error", e);
		} finally {
			LOGGER.info("发送告警通知完成, 耗时: {} ms", (System.currentTimeMillis() - start));
		}
	}

	private void warning() {
		//查询配置
		DeviceWarningSettingForm deviceWarningSettingForm = new DeviceWarningSettingForm();
		deviceWarningSettingForm.setWarningType(-1);
		deviceWarningSettingForm.setNotifyType(-1);
		List<DeviceWarningSettingVo> warningSettingVoList = iDeviceWarningSettingFactory.list(deviceWarningSettingForm);

		Map<String, List<DeviceWarningSettingVo>> enterMap = makeGroup(warningSettingVoList);

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		//查找未处理告警
		for (Map.Entry<String, List<DeviceWarningSettingVo>> entry : enterMap.entrySet()) {
			String enterpriseId = entry.getKey();
			List<DeviceWarningSettingVo> settingList = entry.getValue();
			DeviceWarningForm deviceWarningForm = new DeviceWarningForm();
			deviceWarningForm.setEnterpriseid(enterpriseId);
			//未处理的告警
			deviceWarningForm.setStatus(DeviceWarningVo.DEVICE_WARNING_STATUS_WAIT);
			List<DeviceWarningVo> warningVoList = iDeviceWarningFactory.getDeviceWarningList(deviceWarningForm);

			List<UserVo> userVoList = BaseInf.getUserList(enterpriseId);
			//没有配置或告警则结束
			if (CollectionUtils.isEmpty(warningVoList) || CollectionUtils.isEmpty(settingList) || CollectionUtils.isEmpty(userVoList)) {
				continue;
			}

			//找出符合配置的告警发送通知
			for (DeviceWarningVo warningVo : warningVoList) {
				Set<Integer> typeSet = changeType(warningVo.getWarningType());
				// 添加模板中属性
				Map<String, String> param = new HashMap<>(16);
				param.put("deveui", warningVo.getDeveui());
				param.put("title", "预警通知");
				param.put("useraddr", warningVo.getAddress());
				param.put("description", warningVo.getWarningType() == null ? "" : warningVo.getWarningType());
				param.put("warningTypeStr", StrUtils.removeMoreSplit(warningVo.getWarningType(), ","));
				param.put("ctime", sdf.format(warningVo.getCtime()));
				param.put("countNum", String.valueOf(warningVo.getWarningNum()));
				for (DeviceWarningSettingVo settingVo : settingList) {

					List<UserVo> notifyUserList = findUser(settingVo.getNotifyUserType(), settingVo.getNotifyUser(), userVoList);
					MessageBo messageBo = new MessageBo();
					messageBo.setEnterpriseid(enterpriseId);
					messageBo.setModeltype(settingVo.getModelType());
					messageBo.setMessagetype(settingVo.getNotifyType());
					messageBo.setParams(JSON.toJSONString(param));
					messageBo.setTitle("预警通知");
					if (typeSet.contains(settingVo.getWarningType()) && warningVo.getWarningNum() >= settingVo.getWarningNum()) {
						//发送通知
						if (settingVo.getNotifyType() == 1) {
							//发送短信通知
							List<String> phones = notifyUserList.stream().map(UserVo::getPhone).collect(Collectors.toList());
							if(!CollectionUtils.isEmpty(phones)){
								List<String> auth = new ArrayList<>();
								for(String phone : phones){
									if(VerificationUtils.regex(phone, VerificationUtils.REG_PHONE)){
										auth.add(phone);
									}
								}

								String useraddr = param.get("useraddr");
								if(StringUtils.isEmpty(useraddr)){
									param.put("useraddr","系统没录入");
								}
								try{
									iSmsService.sendSMS(messageBo.getEnterpriseid(), messageBo.getModeltype(),
											auth.toArray(new String[auth.size()]), param, String.valueOf(Config.Generator.nextId()));
								}catch(Exception e){
									LOGGER.error(LogMsg.to(e));
								}
							}

						} else if (settingVo.getNotifyType() == 2) {
							//发送邮件通知
							StringBuilder emails = new StringBuilder();
							for (UserVo userVo : notifyUserList) {
								if (userVo != null) {
									emails.append(userVo.getEmail()).append(";");
								}
							}
							messageBo.setReceiveaddress(emails.toString());
							try{
								iEmailService.sendEmailWithoutAuth(messageBo);
							}catch(Exception e){
								LOGGER.error(LogMsg.to(e));
							}
						}
						//发起工单
						if (!StringUtils.isEmpty(settingVo.getTmplId())) {
							IWorkCenterBuildFactory iWorkCenterBuildFactory = iWorkCenterBuildFactoryMap.get(WaterConstants.WORK_CENTER_BUSINESS_TYPE_DEVICE_ALARM);
							if (iWorkCenterBuildFactory != null) {

								WorkCenterBuildForm buildForm = new WorkCenterBuildForm();
								buildForm.setBusinessId(UuidUtils.getUuid());
								buildForm.setBusinessType(WaterConstants.WORK_CENTER_BUSINESS_TYPE_DEVICE_ALARM);
								buildForm.setTmplId(settingVo.getTmplId());

								Map<String, String> showParams = new HashMap<>(16);
								showParams.put("createTime", sdf.format(new Date()));
								String useraddr = StringUtils.isEmpty(param.get("useraddr"))?"系统未录入":param.get("useraddr");
								String desc = "设备号：【" + warningVo.getDeveui() + "】发生【" + StrUtils.removeMoreSplit(warningVo.getWarningType(), ",") + "】，地址：【" + useraddr + "】，预警时间：【" + sdf.format(warningVo.getCtime()) + "】";
								showParams.put("desc", desc);
								buildForm.setShowParams(showParams);

								Map<String, String> sqlParams = new HashMap<>(16);
								String [] ids = {warningVo.getId()};
								sqlParams.put("ids", JSONUtils.toJSONString(ids));
								buildForm.setSqlParams(sqlParams);

								WorkCenterBuildVo init = iWorkCenterBuildFactory.init(buildForm);
								WorkCenterProcessHandleForm processHandleForm = new WorkCenterProcessHandleForm();
								BeanUtils.copyProperties(init, processHandleForm);
								processHandleForm.setTitle("设备【"+warningVo.getDeveui()+"】发生预警");
								Map<String, String> fillParams = new HashMap<>(16);
								fillParams.put("type", "28,100005,3");
								processHandleForm.setFillParams(fillParams);
								processHandleForm.setEnterpriseid(settingVo.getEnterpriseid());
								iProcessHandleFactory.add(processHandleForm);

								deviceWarningForm.setEnterpriseid(settingVo.getEnterpriseid());
								deviceWarningForm.setNodeIds(Arrays.asList(ids));
								// 数据处理
								iDeviceWarningFactory.warninghandle(deviceWarningForm);
							}
						}
					}
				}
			}
		}
	}

	/**
	 * 获取发送用户
	 *
	 * @param type
	 * @param notifyUser
	 * @param userVoList
	 * @return
	 */
	private List<UserVo> findUser(String type, String notifyUser, List<UserVo> userVoList) {


		List<UserVo> notifyUserList = new ArrayList<>();

		Map<String, UserVo> idMap = new HashMap<>(16);
		for (UserVo u : userVoList) {
			idMap.put(u.getUserid(), u);
		}
		Map<String, UserVo> roleMap = new HashMap<>(16);
		for (UserVo u : userVoList) {
			roleMap.put(u.getRoleid(), u);
		}
		//获取发送用户
		if ("users".equals(type)) {
			//通知到用户
			String[] ids = notifyUser.split(",");
			for (String id : ids) {
				notifyUserList.add(idMap.get(id));
			}
		} else if ("roles".equals(type)) {
			//通知到角色
			String[] roleIds = notifyUser.split(",");
			for (String id : roleIds) {
				notifyUserList.add(roleMap.get(id));
			}
		}
		return notifyUserList;
	}

	/**
	 * 根据企业ID分组
	 *
	 * @param warningSettingVoList
	 * @return
	 */
	private Map<String, List<DeviceWarningSettingVo>> makeGroup(List<DeviceWarningSettingVo> warningSettingVoList) {
		Map<String, List<DeviceWarningSettingVo>> enterMap = new HashMap<>(16);
		for (DeviceWarningSettingVo warningSettingVo : warningSettingVoList) {
			String eid = warningSettingVo.getEnterpriseid();
			if (enterMap.containsKey(eid)) {
				List<DeviceWarningSettingVo> enterList = enterMap.get(eid);
				enterList.add(warningSettingVo);
				enterMap.put(eid, enterList);
			} else {
				List<DeviceWarningSettingVo> enterList = new ArrayList<>();
				enterList.add(warningSettingVo);
				enterMap.put(eid, enterList);
			}
		}
		return enterMap;
	}

	/**
	 * 类型转换
	 *
	 * @param str
	 * @return
	 */
	private Set<Integer> changeType(String str) {
		Set<Integer> typeSet = new HashSet<>();
		if (!StringUtils.isEmpty(str)) {
			String[] types = str.split(",");
			for (String type : types) {
				if (WaterConstants.WARNING_TYPE_VALUE_MAP.containsKey(type)) {
					typeSet.add(WaterConstants.WARNING_TYPE_VALUE_MAP.get(type));
				}
			}
		}
		return typeSet;
	}
}
