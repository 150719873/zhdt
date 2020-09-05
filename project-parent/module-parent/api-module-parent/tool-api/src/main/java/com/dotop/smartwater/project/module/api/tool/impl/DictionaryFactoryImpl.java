package com.dotop.smartwater.project.module.api.tool.impl;

import com.dotop.smartwater.project.module.api.tool.IDictionaryFactory;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.dependence.core.utils.BeanUtils;
import com.dotop.smartwater.project.module.core.auth.local.AuthCasClient;
import com.dotop.smartwater.project.module.core.auth.local.IAuthCasClient;
import com.dotop.smartwater.project.module.core.auth.vo.UserVo;
import com.dotop.smartwater.project.module.core.water.bo.DictionaryBo;
import com.dotop.smartwater.project.module.core.water.bo.DictionaryChildBo;
import com.dotop.smartwater.project.module.core.water.constants.DictionaryCode;
import com.dotop.smartwater.project.module.core.water.constants.ResultCode;
import com.dotop.smartwater.project.module.core.water.constants.WaterConstants;
import com.dotop.smartwater.project.module.core.water.form.DictionaryForm;
import com.dotop.smartwater.project.module.core.water.vo.DictionaryChildVo;
import com.dotop.smartwater.project.module.core.water.vo.DictionaryVo;
import com.dotop.smartwater.project.module.service.tool.IDictionaryChildService;
import com.dotop.smartwater.project.module.service.tool.IDictionaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * 字典配置
 *

 * @date 2019年2月23日
 */

@Component
public class DictionaryFactoryImpl implements IDictionaryFactory, IAuthCasClient {

	@Autowired
	private IDictionaryService iDictionaryService;

	@Autowired
	private IDictionaryChildService iDictionaryChildService;

	@Override
	public Pagination<DictionaryVo> page(DictionaryForm dictionaryForm) {
		UserVo user = AuthCasClient.getUser();

		if (UserVo.USER_TYPE_ADMIN == user.getType()) {
			dictionaryForm.setEnterpriseid(DictionaryCode.DICTIONARY_PROPERTY_ADMIN);
		} else {
			dictionaryForm.setEnterpriseid(user.getEnterpriseid());
		}
		return iDictionaryService.page(BeanUtils.copy(dictionaryForm, DictionaryBo.class));
	}

	@Override
	public List<DictionaryVo> list(DictionaryForm dictionaryForm) {
		UserVo user = AuthCasClient.getUser();
		if (UserVo.USER_TYPE_ADMIN == user.getType()) {
			//如果是最高级管理员，就是运维的企业id
			dictionaryForm.setEnterpriseid(DictionaryCode.DICTIONARY_PROPERTY_ADMIN);
		} else {
			dictionaryForm.setEnterpriseid(user.getEnterpriseid());
		}
        return iDictionaryService.list(BeanUtils.copy(dictionaryForm, DictionaryBo.class));
	}

	@Override
	public DictionaryVo get(DictionaryForm dictionaryForm) {

		return iDictionaryService.get(BeanUtils.copy(dictionaryForm, DictionaryBo.class));
	}

	@Override
	public DictionaryVo add(DictionaryForm dictionaryForm) {
		// 获取用户信息
		UserVo user = AuthCasClient.getUser();

		if (UserVo.USER_TYPE_ADMIN == user.getType()) {
			dictionaryForm.setEnterpriseid(DictionaryCode.DICTIONARY_PROPERTY_ADMIN);
		} else {
			dictionaryForm.setEnterpriseid(user.getEnterpriseid());
			dictionaryForm.setDictionaryType(DictionaryCode.DICTIONARY_PROPERTY_PRIVATE);
		}

		if (WaterConstants.USER_TYPE_ADMIN != user.getType()
				&& DictionaryCode.DICTIONARY_PROPERTY_PUBLIC.equals(dictionaryForm.getDictionaryType())) {
			throw new FrameworkRuntimeException(ResultCode.ParamIllegal, "系统管理员才能新增公有属性的字典！");
		}

		Date date = new Date();

		DictionaryBo dictionaryBo = BeanUtils.copy(dictionaryForm, DictionaryBo.class);
		dictionaryBo.setCreateDate(date);

		dictionaryBo.setCreateBy(user.getUserid());
		dictionaryBo.setLastDate(date);
		dictionaryBo.setLastBy(user.getUserid());

		if (iDictionaryService.isExist(dictionaryBo)) {
			throw new FrameworkRuntimeException(ResultCode.ParamIllegal, "类别编码已存在！");
		}

		return iDictionaryService.add(dictionaryBo);
	}

	@Override
	public DictionaryVo edit(DictionaryForm dictionaryForm) {
		// 获取用户信息
		UserVo user = AuthCasClient.getUser();

		DictionaryBo dictionaryBo = BeanUtils.copy(dictionaryForm, DictionaryBo.class);
		dictionaryBo.setLastBy(user.getUserid());
		dictionaryBo.setLastDate(new Date());
		return iDictionaryService.edit(dictionaryBo);
	}

	@Override
	public String del(DictionaryForm dictionaryForm) {

		DictionaryBo dictionaryBo = new DictionaryBo();
		dictionaryBo.setDictionaryId(dictionaryForm.getDictionaryId());
		DictionaryVo dictionaryVo = iDictionaryService.get(dictionaryBo);

		if (dictionaryVo != null
				&& DictionaryCode.DICTIONARY_PROPERTY_PUBLIC.equals(dictionaryVo.getDictionaryType())) {
			throw new FrameworkRuntimeException(ResultCode.ParamIllegal, "类别公有属性的不能删除！");
		}

		return iDictionaryService.del(dictionaryBo);
	}

	@Override
	public List<DictionaryChildVo> getChildren(DictionaryForm dictionaryForm) {
		UserVo user = AuthCasClient.getUser();

		DictionaryBo dictionaryBo = new DictionaryBo();
		dictionaryBo.setDictionaryCode(dictionaryForm.getDictionaryCode());
		dictionaryBo.setEnterpriseid(user.getEnterpriseid());
		List<DictionaryVo> list = iDictionaryService.getByDictionaryCode(dictionaryBo);

		if (list == null) {
			return null;
		}

		if (list.isEmpty()) {
			throw new FrameworkRuntimeException(ResultCode.ParamIllegal, "没有该字典码的属性");
		}
		if (list.size() > 1) {
			throw new FrameworkRuntimeException(ResultCode.Fail, "字典码有重复,请联系管理员修改");
		}

		DictionaryVo dictionaryVo = list.get(0);
		DictionaryChildBo dictionaryChildBo = new DictionaryChildBo();
		dictionaryChildBo.setDictionaryId(dictionaryVo.getDictionaryId());
		List<DictionaryChildVo> childVoList = iDictionaryChildService.list(dictionaryChildBo);
		if (childVoList != null && !list.isEmpty()) { // 非空 代替 list.size>0
			return childVoList;
		} else {
			throw new FrameworkRuntimeException(ResultCode.ParamIllegal, "该字典码没有配置子属性项");
		}
	}

	public List<DictionaryChildVo> getWechatChildren(DictionaryForm dictionaryForm) {
		DictionaryBo dictionaryBo = new DictionaryBo();
		dictionaryBo.setDictionaryCode(dictionaryForm.getDictionaryCode());
		List<DictionaryVo> list = iDictionaryService.getByDictionaryCode(dictionaryBo);

		if (list == null) {
			return null;
		}

		if (list.isEmpty()) {
			throw new FrameworkRuntimeException(ResultCode.ParamIllegal, "没有该字典码的属性");
		}
		if (list.size() > 1) {
			throw new FrameworkRuntimeException(ResultCode.Fail, "字典码有重复,请联系管理员修改");
		}

		DictionaryVo dictionaryVo = list.get(0);
		DictionaryChildBo dictionaryChildBo = new DictionaryChildBo();
		dictionaryChildBo.setDictionaryId(dictionaryVo.getDictionaryId());
		List<DictionaryChildVo> childVoList = iDictionaryChildService.list(dictionaryChildBo);
		if (childVoList != null && !childVoList.isEmpty()) {
			return childVoList;
		} else {
			throw new FrameworkRuntimeException(ResultCode.ParamIllegal, "该字典码没有配置子属性项");
		}
	}

	@Override
	public List<DictionaryVo> syncList(DictionaryForm dictionaryForm) {

		DictionaryBo dictionaryBo = new DictionaryBo();
		dictionaryBo.setEnterpriseid(DictionaryCode.DICTIONARY_PROPERTY_ADMIN);
		dictionaryBo.setDictionaryType(DictionaryCode.DICTIONARY_PROPERTY_PRIVATE);

		return iDictionaryService.list(dictionaryBo);
	}

	@Override
	public void sync(DictionaryForm dictionaryForm) {

		for(String did : dictionaryForm.getDictionaryIds()) {
			DictionaryBo dictionaryBo = new DictionaryBo();
			dictionaryBo.setDictionaryId(did);

			DictionaryVo dictionaryVo = iDictionaryService.get(dictionaryBo);
			if (dictionaryVo == null) {
				continue;
			}

			UserVo user = AuthCasClient.getUser();
			if (UserVo.USER_TYPE_ADMIN == user.getType()) {
				dictionaryBo.setEnterpriseid(DictionaryCode.DICTIONARY_PROPERTY_ADMIN);
			} else {
				dictionaryBo.setEnterpriseid(user.getEnterpriseid());
			}

			// 判断该字典在本水司是否存在
			dictionaryBo.setDictionaryCode(dictionaryVo.getDictionaryCode());
			if (iDictionaryService.isExist(dictionaryBo)) {
				continue;
			}

			Date date = new Date();

			dictionaryBo.setCreateDate(date);
			dictionaryBo.setCreateBy(user.getUserid());
			dictionaryBo.setLastDate(date);
			dictionaryBo.setLastBy(user.getUserid());

			// 同步字典开始
			dictionaryBo.setDictionaryValue(dictionaryVo.getDictionaryValue());
			dictionaryBo.setDictionaryName(dictionaryVo.getDictionaryName());
			dictionaryBo.setDictionaryType(dictionaryVo.getDictionaryType());
			dictionaryBo.setRemark(dictionaryVo.getRemark());

			DictionaryVo dictionaryVoCopy = iDictionaryService.add(dictionaryBo);

			DictionaryChildBo dictionaryChildBo = new DictionaryChildBo();
			dictionaryChildBo.setDictionaryId(dictionaryVo.getDictionaryId());
			List<DictionaryChildVo> list = iDictionaryChildService.list(dictionaryChildBo);
			if (list != null && !list.isEmpty()) { //!list.isEmpty() 代替 list.size()>0

				for (DictionaryChildVo dictionaryChildVo : list) {
					dictionaryChildBo = BeanUtils.copy(dictionaryChildVo, DictionaryChildBo.class);
					dictionaryChildBo.setDictionaryId(dictionaryVoCopy.getDictionaryId());
					dictionaryChildBo.setCreateDate(date);
					dictionaryChildBo.setCreateBy(user.getUserid());
					dictionaryChildBo.setLastDate(date);
					dictionaryChildBo.setLastBy(user.getUserid());

					iDictionaryChildService.add(dictionaryChildBo);
				}
			}
		}
	}
	
	@Override
	public void initialize(DictionaryForm dictionaryForm) {

		for(String did : dictionaryForm.getDictionaryIds()) {
			DictionaryBo dictionaryBo = new DictionaryBo();
			dictionaryBo.setDictionaryId(did);

			DictionaryVo dictionaryVo = iDictionaryService.get(dictionaryBo);
			if (dictionaryVo == null) {
				continue;
			}

			UserVo user = AuthCasClient.getUser();
			dictionaryBo.setEnterpriseid(dictionaryForm.getEnterpriseid());

			// 判断该字典在本水司是否存在
			dictionaryBo.setDictionaryCode(dictionaryVo.getDictionaryCode());
			if (iDictionaryService.isExist(dictionaryBo)) {
				continue;
			}

			Date date = new Date();
			dictionaryBo.setCreateDate(date);
			dictionaryBo.setCreateBy(user.getUserid());
			dictionaryBo.setLastDate(date);
			dictionaryBo.setLastBy(user.getUserid());

			// 同步字典开始
			dictionaryBo.setDictionaryValue(dictionaryVo.getDictionaryValue());
			dictionaryBo.setDictionaryName(dictionaryVo.getDictionaryName());
			dictionaryBo.setDictionaryType(DictionaryCode.DICTIONARY_PROPERTY_PRIVATE);
			dictionaryBo.setRemark(dictionaryVo.getRemark());

			DictionaryVo dictionaryVoCopy = iDictionaryService.add(dictionaryBo);

			DictionaryChildBo dictionaryChildBo = new DictionaryChildBo();
			dictionaryChildBo.setDictionaryId(dictionaryVo.getDictionaryId());
			List<DictionaryChildVo> list = iDictionaryChildService.list(dictionaryChildBo);
			if (list != null && !list.isEmpty()) { //!list.isEmpty() 代替 list.size()>0
				for (DictionaryChildVo dictionaryChildVo : list) {
					dictionaryChildBo = BeanUtils.copy(dictionaryChildVo, DictionaryChildBo.class);
					dictionaryChildBo.setDictionaryId(dictionaryVoCopy.getDictionaryId());
					dictionaryChildBo.setCreateDate(date);
					dictionaryChildBo.setCreateBy(user.getUserid());
					dictionaryChildBo.setLastDate(date);
					dictionaryChildBo.setLastBy(user.getUserid());

					iDictionaryChildService.add(dictionaryChildBo);
				}
			}
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
	public void copy(List<DictionaryBo> dictionaryBos, String targetEnterpriseid) {
		if (dictionaryBos == null || dictionaryBos.isEmpty()) {
			return;
		}
		for (DictionaryBo dictionaryBo : dictionaryBos) {
			if (DictionaryCode.DICTIONARY_PROPERTY_PUBLIC.equals(dictionaryBo.getDictionaryType())) {
				// 公有字典跳过
				continue;
			}
			// 目标字典类别
			DictionaryBo targetDictionaryBo = BeanUtils.copy(dictionaryBo, DictionaryBo.class);
			targetDictionaryBo.setEnterpriseid(targetEnterpriseid);
			if (iDictionaryService.isExist(targetDictionaryBo)) {
				// 目标字典存在私有字典跳过
				continue;
			}
			// 新增目标字典类别
			targetDictionaryBo.setCreateDate(getCurr());
			targetDictionaryBo.setCreateBy(getUserid());
			targetDictionaryBo.setLastDate(getCurr());
			targetDictionaryBo.setLastBy(getUserid());
			DictionaryVo targetDictionaryVo = iDictionaryService.add(targetDictionaryBo);
			// 目标字典类型
			List<DictionaryChildBo> targetChildren = targetDictionaryBo.getChildren();
			if (targetChildren != null && !targetChildren.isEmpty()) {
				for (DictionaryChildBo targetChildBo : targetChildren) {
					targetChildBo.setDictionaryId(targetDictionaryVo.getDictionaryId());
					targetChildBo.setEnterpriseid(targetEnterpriseid);
					targetChildBo.setCreateDate(getCurr());
					targetChildBo.setCreateBy(getUserid());
					targetChildBo.setLastDate(getCurr());
					targetChildBo.setLastBy(getUserid());
					iDictionaryChildService.add(targetChildBo);
				}
			}
		}
	}
}
