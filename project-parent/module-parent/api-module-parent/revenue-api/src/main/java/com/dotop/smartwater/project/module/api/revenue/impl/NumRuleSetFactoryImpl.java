package com.dotop.smartwater.project.module.api.revenue.impl;

import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.dependence.core.utils.BeanUtils;
import com.dotop.smartwater.dependence.lock.IDistributedLock;
import com.dotop.smartwater.project.module.api.revenue.INumRuleSetFactory;
import com.dotop.smartwater.project.module.core.auth.local.AuthCasClient;
import com.dotop.smartwater.project.module.core.auth.vo.UserVo;
import com.dotop.smartwater.project.module.core.water.bo.NumRuleSetBo;
import com.dotop.smartwater.project.module.core.water.config.Config;
import com.dotop.smartwater.project.module.core.water.constants.NumRuleSetCode;
import com.dotop.smartwater.project.module.core.water.constants.ResultCode;
import com.dotop.smartwater.project.module.core.water.form.NumRuleSetForm;
import com.dotop.smartwater.project.module.core.water.form.customize.MakeNumRequest;
import com.dotop.smartwater.project.module.core.water.vo.NumRuleSetVo;
import com.dotop.smartwater.project.module.core.water.vo.customize.MakeNumVo;
import com.dotop.smartwater.project.module.service.tool.INumRuleSetService;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 票据管理
 *

 * @date 2019年2月25日
 */
@Component
public class NumRuleSetFactoryImpl implements INumRuleSetFactory {

	@Autowired
	private INumRuleSetService iNumRuleSetService;

	@Autowired
	private IDistributedLock iDistributedLock;

	@Override
	public List<NumRuleSetVo> base() throws FrameworkRuntimeException {
		return iNumRuleSetService.getBaseRuleList();
	}

	@Override
	public void changeStatus(NumRuleSetForm numRuleSetForm) throws FrameworkRuntimeException {

		UserVo userVo = AuthCasClient.getUser();
		NumRuleSetBo numRuleSetBo = new NumRuleSetBo();
		numRuleSetBo.setEnterpriseid(userVo.getEnterpriseid());
		numRuleSetBo.setRuleid(numRuleSetForm.getRuleid());
		NumRuleSetVo oldNumRuleSet = iNumRuleSetService.findByEnterpriseIdAndRuleId(numRuleSetBo);
		if (oldNumRuleSet == null) {
			throw new FrameworkRuntimeException(ResultCode.ParamIllegal, "不存在该规则类型", null);
		}

		oldNumRuleSet.setStatus(numRuleSetForm.getStatus());
		iNumRuleSetService.edit(BeanUtils.copy(oldNumRuleSet, NumRuleSetBo.class));
	}

	@Override
	public MakeNumVo makeNo(MakeNumRequest makeNumRequest) throws FrameworkRuntimeException {
		String enterpriseid = makeNumRequest.getEnterpriseid();
		if(StringUtils.isEmpty(enterpriseid)){
			UserVo userVo = AuthCasClient.getUser();
			enterpriseid = userVo.getEnterpriseid();
		}
		
		NumRuleSetBo numRuleSetBo = new NumRuleSetBo();
		numRuleSetBo.setRuleid(makeNumRequest.getRuleid());
		numRuleSetBo.setEnterpriseid(enterpriseid);
		NumRuleSetVo numRuleSet = iNumRuleSetService.findByEnterpriseIdAndRuleId(numRuleSetBo);
		MakeNumVo makeNumVo = new MakeNumVo();
		List<String> list = new ArrayList<>();
		if (numRuleSet == null) {
			// 没有规则,则使用默认规则
			makeNumVo.setStatus(0);
			if (makeNumRequest.getCount() > 0) {
				for (int i = 0; i < makeNumRequest.getCount(); i++) {
					list.add(String.valueOf(Config.Generator.nextId()));
				}
				makeNumVo.setNumbers(list);
			}
			return makeNumVo;
		}

		String key = "makeNo_" + enterpriseid + "_" + makeNumRequest.getRuleid();
		// 加锁解决并发问题
		boolean flag = iDistributedLock.lock(key, 3);
		try {
			if (flag) {
				String number = "";
				if (NumRuleSetCode.NumRuleSet_SATAUS_TES == numRuleSet.getStatus()) {
					if (StringUtils.isNotBlank(numRuleSet.getTitle())) {
						number += numRuleSet.getTitle();
					}
					if (StringUtils.isNotBlank(numRuleSet.getTimesformat())) {
						number += DateFormatUtils.format(new Date(), numRuleSet.getTimesformat());
					}
					Long maxValue = 0L;
					for (int i = 0; i < makeNumRequest.getCount(); i++) {
						String temp;
						maxValue = Long.parseLong(numRuleSet.getMax_value() == null ? "0" : numRuleSet.getMax_value())
								+ 1L + i;
						temp = number + maxValue;
						list.add(temp);
					}

					// 更新当前最大值
					numRuleSet.setMax_value(String.valueOf(maxValue));

					iNumRuleSetService.edit(BeanUtils.copy(numRuleSet, NumRuleSetBo.class));

					makeNumVo.setStatus(NumRuleSetCode.NumRuleSet_SATAUS_TES);
					makeNumVo.setNumbers(list);
					return makeNumVo;
				} else {
					makeNumVo.setStatus(0);
					if (makeNumRequest.getCount() > 0) {
						for (int i = 0; i < makeNumRequest.getCount(); i++) {
							list.add(String.valueOf(Config.Generator.nextId()));
						}
						makeNumVo.setNumbers(list);
					}
					return makeNumVo;
				}
			} else {
				throw new FrameworkRuntimeException(ResultCode.NumberLocking,
						ResultCode.getMessage(ResultCode.NumberLocking));
			}
		} catch (Exception e) {
			throw new FrameworkRuntimeException(ResultCode.Fail, e.getMessage(), null);
		} finally {
			if (flag) {
				iDistributedLock.releaseLock(key);
			}
		}
	}

	@Override
	public MakeNumVo wechatMakeNo(MakeNumRequest makeNumRequest) throws FrameworkRuntimeException {
		NumRuleSetBo numRuleSetBo = new NumRuleSetBo();
		numRuleSetBo.setRuleid(makeNumRequest.getRuleid());
		numRuleSetBo.setEnterpriseid(makeNumRequest.getEnterpriseid());
		NumRuleSetVo numRuleSet = iNumRuleSetService.findByEnterpriseIdAndRuleId(numRuleSetBo);
		MakeNumVo makeNumVo = new MakeNumVo();
		List<String> list = new ArrayList<>();
		if (numRuleSet == null) {
			// 没有规则,则使用默认规则
			makeNumVo.setStatus(0);
			if (makeNumRequest.getCount() > 0) {
				for (int i = 0; i < makeNumRequest.getCount(); i++) {
					list.add(String.valueOf(Config.Generator.nextId()));
				}
				makeNumVo.setNumbers(list);
			}
			return makeNumVo;
		}

		String key = "makeNo_" + makeNumRequest.getEnterpriseid() + "_" + makeNumRequest.getRuleid();
		// 加锁解决并发问题
		boolean flag = iDistributedLock.lock(key, 3);
		try {
			if (flag) {
				String number = "";
				if (NumRuleSetCode.NumRuleSet_SATAUS_TES == numRuleSet.getStatus()) {
					if (StringUtils.isNotBlank(numRuleSet.getTitle())) {
						number += numRuleSet.getTitle();
					}
					if (StringUtils.isNotBlank(numRuleSet.getTimesformat())) {
						number += DateFormatUtils.format(new Date(), numRuleSet.getTimesformat());
					}
					Long maxValue = 0L;
					for (int i = 0; i < makeNumRequest.getCount(); i++) {
						String temp;
						maxValue = Long.parseLong(numRuleSet.getMax_value() == null ? "0" : numRuleSet.getMax_value())
								+ 1L + i;
						temp = number + maxValue;
						list.add(temp);
					}

					// 更新当前最大值
					numRuleSet.setMax_value(String.valueOf(maxValue));

					iNumRuleSetService.edit(BeanUtils.copy(numRuleSet, NumRuleSetBo.class));

					makeNumVo.setStatus(NumRuleSetCode.NumRuleSet_SATAUS_TES);
					makeNumVo.setNumbers(list);
					return makeNumVo;
				} else {
					makeNumVo.setStatus(0);
					if (makeNumRequest.getCount() > 0) {
						for (int i = 0; i < makeNumRequest.getCount(); i++) {
							list.add(String.valueOf(Config.Generator.nextId()));
						}
						makeNumVo.setNumbers(list);
					}
					return makeNumVo;
				}
			} else {
				throw new FrameworkRuntimeException(ResultCode.NumberLocking,
						ResultCode.getMessage(ResultCode.NumberLocking));
			}
		} catch (Exception e) {
			throw new FrameworkRuntimeException(ResultCode.Fail, e.getMessage(), null);
		} finally {
			if (flag) {
				iDistributedLock.releaseLock(key);
			}
		}
	}

	@Override
	public NumRuleSetVo add(NumRuleSetForm numRuleSetForm) throws FrameworkRuntimeException {
		UserVo userVo = AuthCasClient.getUser();
		NumRuleSetBo numRuleSetBo = BeanUtils.copy(numRuleSetForm, NumRuleSetBo.class);
		if(numRuleSetBo != null){
			numRuleSetBo.setEnterpriseid(userVo.getEnterpriseid());
			numRuleSetBo.setCtime(new Date());
		}

		if (iNumRuleSetService.findByEnterpriseIdAndRuleId(numRuleSetBo) != null) {
			throw new FrameworkRuntimeException(ResultCode.Fail, "该类型规则存在,不能重复添加");
		}

		iNumRuleSetService.add(numRuleSetBo);
		return BeanUtils.copy(numRuleSetBo, NumRuleSetVo.class);
	}

	@Override
	public NumRuleSetVo get(NumRuleSetForm numRuleSetForm) throws FrameworkRuntimeException {
		UserVo userVo = AuthCasClient.getUser();
		NumRuleSetBo numRuleSetBo = new NumRuleSetBo();
		numRuleSetBo.setRuleid(numRuleSetForm.getRuleid());
		numRuleSetBo.setEnterpriseid(userVo.getEnterpriseid());
		return iNumRuleSetService.findByEnterpriseIdAndRuleId(numRuleSetBo);
	}

	@Override
	public List<NumRuleSetVo> list(NumRuleSetForm numRuleSetForm) throws FrameworkRuntimeException {
		UserVo userVo = AuthCasClient.getUser();
		NumRuleSetBo numRuleSetBo = new NumRuleSetBo();
		numRuleSetBo.setEnterpriseid(userVo.getEnterpriseid());
		return iNumRuleSetService.findByEnterpriseId(numRuleSetBo);
	}

	@Override
	public Pagination<NumRuleSetVo> page(NumRuleSetForm numRuleSetForm) throws FrameworkRuntimeException {
		UserVo userVo = AuthCasClient.getUser();
		NumRuleSetBo numRuleSetBo = BeanUtils.copy(numRuleSetForm, NumRuleSetBo.class);
		numRuleSetBo.setEnterpriseid(userVo.getEnterpriseid());
		return iNumRuleSetService.page(numRuleSetBo);
	}

	@Override
	public NumRuleSetVo edit(NumRuleSetForm numRuleSetForm) throws FrameworkRuntimeException {
		UserVo userVo = AuthCasClient.getUser();
		NumRuleSetBo numRuleSetBo = BeanUtils.copy(numRuleSetForm, NumRuleSetBo.class);
		numRuleSetBo.setEnterpriseid(userVo.getEnterpriseid());
		numRuleSetBo.setRuleid(numRuleSetForm.getRuleid());
		NumRuleSetVo oldNumRuleSet = iNumRuleSetService.findByEnterpriseIdAndRuleId(numRuleSetBo);
		if (oldNumRuleSet == null) {
			throw new FrameworkRuntimeException(ResultCode.Fail, "不存在该规则类型");
		}

		if (Long.parseLong(oldNumRuleSet.getMax_value()) > Long.parseLong(numRuleSetForm.getMax_value())) {
			throw new FrameworkRuntimeException(ResultCode.Fail, "当前记录最大值不能改小");
		}

		numRuleSetBo.setCtime(new Date());
		numRuleSetBo.setEnterpriseid(userVo.getEnterpriseid());
		iNumRuleSetService.edit(numRuleSetBo);

		return BeanUtils.copy(numRuleSetBo, NumRuleSetVo.class);
	}
}
