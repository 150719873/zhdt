package com.dotop.smartwater.project.module.api.workcenter.impl;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.dotop.smartwater.project.module.api.workcenter.IFormFactory;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.dotop.smartwater.dependence.core.exception.BaseExceptionConstants;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.log.LogMsg;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.dependence.core.utils.Base64Utils;
import com.dotop.smartwater.dependence.core.utils.BeanUtils;
import com.dotop.smartwater.dependence.core.utils.JSONUtils;
import com.dotop.smartwater.dependence.core.utils.MD5Util;
import com.dotop.smartwater.dependence.core.utils.UuidUtils;
import com.dotop.smartwater.dependence.core.utils.VerificationUtils;
import com.dotop.smartwater.project.module.api.revenue.INumRuleSetFactory;
import com.dotop.smartwater.project.module.api.tool.IDictionaryFactory;
import com.dotop.smartwater.project.module.core.auth.local.IAuthCasClient;
import com.dotop.smartwater.project.module.core.water.bo.DictionaryBo;
import com.dotop.smartwater.project.module.core.water.bo.DictionaryChildBo;
import com.dotop.smartwater.project.module.core.water.bo.WorkCenterDbBo;
import com.dotop.smartwater.project.module.core.water.bo.WorkCenterDbFieldBo;
import com.dotop.smartwater.project.module.core.water.bo.WorkCenterFormBo;
import com.dotop.smartwater.project.module.core.water.bo.WorkCenterTmplBo;
import com.dotop.smartwater.project.module.core.water.config.Config;
import com.dotop.smartwater.project.module.core.water.constants.DictionaryCode;
import com.dotop.smartwater.project.module.core.water.constants.NumRuleSetCode;
import com.dotop.smartwater.project.module.core.water.constants.WaterConstants;
import com.dotop.smartwater.project.module.core.water.form.WorkCenterFormForm;
import com.dotop.smartwater.project.module.core.water.form.customize.MakeNumRequest;
import com.dotop.smartwater.project.module.core.water.vo.DictionaryChildVo;
import com.dotop.smartwater.project.module.core.water.vo.DictionaryVo;
import com.dotop.smartwater.project.module.core.water.vo.WorkCenterDbFieldVo;
import com.dotop.smartwater.project.module.core.water.vo.WorkCenterDbVo;
import com.dotop.smartwater.project.module.core.water.vo.WorkCenterFormVo;
import com.dotop.smartwater.project.module.core.water.vo.WorkCenterTmplVo;
import com.dotop.smartwater.project.module.core.water.vo.customize.WorkCenterExportBo;
import com.dotop.smartwater.project.module.core.water.vo.customize.WorkCenterExportVo;
import com.dotop.smartwater.project.module.service.tool.IDictionaryChildService;
import com.dotop.smartwater.project.module.service.tool.IDictionaryService;
import com.dotop.smartwater.project.module.service.workcenter.IDbFieldService;
import com.dotop.smartwater.project.module.service.workcenter.IDbService;
import com.dotop.smartwater.project.module.service.workcenter.IFormService;
import com.dotop.smartwater.project.module.service.workcenter.ITmplService;

@Component("IWorkCenterFormFactory")
public class FormFactoryImpl implements IFormFactory, IAuthCasClient {

	private static final Logger logger = LogManager.getLogger(FormFactoryImpl.class);

	@Autowired
	private ITmplService iTmplService;

	@Autowired
	private IFormService iFormService;

	@Autowired
	private IDbService iDbService;

	@Autowired
	private IDbFieldService iDbFieldService;

	// 该功能分布式可以改为fegin调用，脱离业务系统
	@Autowired
	private INumRuleSetFactory iNumRuleSetFactory;

	@Autowired
	private IDictionaryService iDictionaryService;

	@Autowired
	private IDictionaryChildService iDictionaryChildService;

	// 该功能分布式可以改为fegin调用，脱离业务系统
	@Autowired
	private IDictionaryFactory iDictionaryFactory;

	@Override
	public Pagination<WorkCenterFormVo> page(WorkCenterFormForm formForm) throws FrameworkRuntimeException {
		WorkCenterFormBo formBo = BeanUtils.copy(formForm, WorkCenterFormBo.class);
		formBo.setEnterpriseid(getEnterpriseid());
		return iFormService.page(formBo);
	}

	@Override
	public Pagination<WorkCenterFormVo> select(WorkCenterFormForm formForm) throws FrameworkRuntimeException {
		// 此处优化可以查询返回请求id和name
		WorkCenterFormBo formBo = BeanUtils.copy(formForm, WorkCenterFormBo.class);
		formBo.setIfEffect(WaterConstants.WORK_CENTER_EFFECT);
		formBo.setEnterpriseid(getEnterpriseid());
		return iFormService.page(formBo);
	}

	@Override
	public WorkCenterFormVo get(WorkCenterFormForm formForm) throws FrameworkRuntimeException {
		WorkCenterFormBo formBo = new WorkCenterFormBo();
		formBo.setId(formForm.getId());
		formBo.setEnterpriseid(getEnterpriseid());
		return iFormService.get(formBo);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
	public WorkCenterFormVo add(WorkCenterFormForm formForm) throws FrameworkRuntimeException {
		// 系统生成编号
		String code = iNumRuleSetFactory
				.makeNo(BeanUtils.news(MakeNumRequest.class, "count", 1, "ruleid", NumRuleSetCode.FORM_NUM_SET))
				.getNumbers().get(0);
		// 新增数据源
		WorkCenterFormBo formBo = BeanUtils.copy(formForm, WorkCenterFormBo.class);
		formBo.setId(UuidUtils.getUuid());
		formBo.setCode(code);
		formBo.setIfEffect(WaterConstants.WORK_CENTER_EFFECT);
		formBo.setEnterpriseid(getEnterpriseid());
		formBo.setUserBy(getName());
		formBo.setCurr(getCurr());
		return iFormService.add(formBo);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
	public WorkCenterFormVo edit(WorkCenterFormForm formForm) throws FrameworkRuntimeException {
		// 校验表单是否存在
		WorkCenterFormVo formVo = iFormService.get(BeanUtils.news(WorkCenterFormBo.class, formForm.getId()));
		if (formVo == null) {
			throw new FrameworkRuntimeException(BaseExceptionConstants.BASE_ERROR, "表单不存在");
		}
		boolean flag = true;
		List<WorkCenterDbVo> dbVos = formVo.getDbs();
		for (WorkCenterDbVo dbVo : dbVos) {
			String ifEffect = dbVo.getIfEffect();
			if (WaterConstants.WORK_CENTER_NO_EFFECT.equals(ifEffect)) {
				flag = false;
				break;
			}
		}
		WorkCenterFormBo formBo = BeanUtils.copy(formForm, WorkCenterFormBo.class);
		if (flag) {
			formBo.setIfEffect(WaterConstants.WORK_CENTER_EFFECT);
		} else {
			formBo.setIfEffect(WaterConstants.WORK_CENTER_NO_EFFECT);
		}
		formBo.setEnterpriseid(getEnterpriseid());
		formBo.setUserBy(getName());
		formBo.setCurr(getCurr());
		return iFormService.edit(formBo);
	}

	// TODO 性能优化
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
	public String del(WorkCenterFormForm formForm) throws FrameworkRuntimeException {
		WorkCenterFormBo formBo = new WorkCenterFormBo();
		formBo.setId(formForm.getId());
		formBo.setEnterpriseid(getEnterpriseid());

		// 判断是否有模板使用，如果使用需要提示不能删除
		WorkCenterTmplBo tmplBo = new WorkCenterTmplBo();
		tmplBo.setForm(formBo);
		tmplBo.setEnterpriseid(getEnterpriseid());
		List<WorkCenterTmplVo> tmplVos = iTmplService.list(tmplBo);
		if (tmplVos != null && !tmplVos.isEmpty()) {
			throw new FrameworkRuntimeException(BaseExceptionConstants.BASE_ERROR, "表单被模板使用中，请检查后再删除");
		}

		// 删除表单
		iFormService.del(formBo);
		// 级联删除数据源
		WorkCenterDbBo dbBo = new WorkCenterDbBo();
		dbBo.setFormId(formForm.getId());
		dbBo.setEnterpriseid(getEnterpriseid());
		List<WorkCenterDbVo> dbVos = iDbService.list(dbBo);
		List<WorkCenterDbBo> dbBos = new ArrayList<>();
		for (WorkCenterDbVo dbVo : dbVos) {
			dbBo = new WorkCenterDbBo();
			dbBo.setId(dbVo.getId());
			dbBo.setEnterpriseid(getEnterpriseid());
			dbBo.setUserBy(getName());
			dbBo.setCurr(getCurr());
			dbBos.add(dbBo);
		}
		iDbService.dels(dbBos);
		// 级联删除数据源字段
		for (WorkCenterDbVo dbVo : dbVos) {
			WorkCenterDbFieldBo dbFieldBo = new WorkCenterDbFieldBo();
			dbFieldBo.setDbId(dbVo.getId());
			dbFieldBo.setEnterpriseid(getEnterpriseid());
			dbFieldBo.setUserBy(getName());
			dbFieldBo.setCurr(getCurr());
			iDbFieldService.del(dbFieldBo);
		}
		return null;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
	public WorkCenterFormVo copy(WorkCenterFormForm formForm) throws FrameworkRuntimeException {
		// 校验表单是否存在
		WorkCenterFormVo formVo = iFormService.get(BeanUtils.news(WorkCenterFormBo.class, formForm.getId()));
		if (formVo == null) {
			throw new FrameworkRuntimeException(BaseExceptionConstants.BASE_ERROR, "表单不存在");
		}
		String ifEffect = formVo.getIfEffect();
		if (!WaterConstants.WORK_CENTER_EFFECT.equals(ifEffect)) {
			throw new FrameworkRuntimeException(BaseExceptionConstants.BASE_ERROR, "表单校验无效,请重新编辑表单和数据源后再试");
		}
		// 参数准备
		String formId = UuidUtils.getUuid();
		List<WorkCenterDbVo> dbVos = formVo.getDbs();
		String formCode = iNumRuleSetFactory
				.makeNo(BeanUtils.news(MakeNumRequest.class, "count", 1, "ruleid", NumRuleSetCode.FORM_NUM_SET))
				.getNumbers().get(0);
		List<String> dbCodes = iNumRuleSetFactory.makeNo(
				BeanUtils.news(MakeNumRequest.class, "count", dbVos.size(), "ruleid", NumRuleSetCode.SOURCE_NUM_SET))
				.getNumbers();
		// 表单复制
		WorkCenterFormBo formBo = BeanUtils.copy(formVo, WorkCenterFormBo.class);
		formBo.setId(formId);
		formBo.setCode(formCode);
		formBo.setIfEffect(WaterConstants.WORK_CENTER_NO_EFFECT);
		formBo.setEnterpriseid(getEnterpriseid());
		formBo.setUserBy(getName());
		formBo.setCurr(getCurr());
		iFormService.add(formBo);
		// 数据源和数据源字段复制
		List<WorkCenterDbBo> addDbBos = new ArrayList<>();
		List<WorkCenterDbFieldBo> addDbFieldBos = new ArrayList<>();
		// 置换字典
		Set<String> dictionarySet = new HashSet<>();
		for (int i = 0; i < dbVos.size(); i++) {
			WorkCenterDbBo dbBo = BeanUtils.copy(dbVos.get(i), WorkCenterDbBo.class);
			List<WorkCenterDbFieldBo> dbFieldBos = dbBo.getDbFields();
			// 数据源复制
			String dbId = UuidUtils.getUuid();
			dbBo.setId(dbId);
			dbBo.setFormId(formId);
			dbBo.setCode(dbCodes.get(i));
			dbBo.setIfEffect(WaterConstants.WORK_CENTER_NO_EFFECT);
			dbBo.setEnterpriseid(getEnterpriseid());
			dbBo.setUserBy(getName());
			dbBo.setCurr(getCurr());
			addDbBos.add(dbBo);
			// 数据源字段复制
			for (WorkCenterDbFieldBo dbFieldBo : dbFieldBos) {
				String dbFieldId = UuidUtils.getUuid();
				dbFieldBo.setId(dbFieldId);
				dbFieldBo.setDbId(dbId);
				dbFieldBo.setEnterpriseid(getEnterpriseid());
				dbFieldBo.setUserBy(getName());
				dbFieldBo.setCurr(getCurr());
				addDbFieldBos.add(dbFieldBo);
				// 过滤置换字典类别
				DictionaryBo contentDict = dbFieldBo.getContentDict();
				if (contentDict != null) {
					dictionarySet.add(contentDict.getDictionaryId());
					// 置换字典类别
					DictionaryBo copyContentDict = new DictionaryBo();
					copyContentDict.setDictionaryId(
							DictionaryCode.copyDictionaryId(contentDict.getDictionaryId(), getEnterpriseid()));
					dbFieldBo.setContentDict(copyContentDict);
				}
			}
		}
		iDbService.adds(addDbBos);
		iDbFieldService.adds(addDbFieldBos);
		// 填充字典
		List<DictionaryBo> dictionaryBos = new ArrayList<>();
		for (String dictionaryId : dictionarySet) {
			DictionaryVo dictionaryVo = iDictionaryService.getByDictionaryId(dictionaryId);
			if (DictionaryCode.DICTIONARY_PROPERTY_PRIVATE.equals(dictionaryVo.getDictionaryType())) {
				// 运维私有才需要导出,默认理解为公有字典同版本一致
				dictionaryBos.add(BeanUtils.copy(dictionaryVo, DictionaryBo.class));
			}
		}
		// 私有字典复制
		iDictionaryFactory.copy(dictionaryBos, getEnterpriseid());
		return BeanUtils.news(WorkCenterFormVo.class, formId);
	}

	@Override
	public String export(WorkCenterFormForm formForm) throws FrameworkRuntimeException {
		// 校验数据源是否存在
		WorkCenterFormVo formVo = iFormService
				.get(BeanUtils.news(WorkCenterFormBo.class, formForm.getId(), getEnterpriseid()));
		if (formVo == null) {
			throw new FrameworkRuntimeException(BaseExceptionConstants.BASE_ERROR, "表单不存在");
		}
		String ifEffect = formVo.getIfEffect();
		if (!WaterConstants.WORK_CENTER_EFFECT.equals(ifEffect)) {
			throw new FrameworkRuntimeException(BaseExceptionConstants.BASE_ERROR, "表单校验无效,请重新编辑表单和数据源后再试");
		}
		// 过滤字典
		Set<String> dictionarySet = new HashSet<>();
		Set<String> dictionaryChildSet = new HashSet<>();
		List<WorkCenterDbVo> dbVos = formVo.getDbs();
		for (WorkCenterDbVo dbVo : dbVos) {
			List<WorkCenterDbFieldVo> dbFieldVos = dbVo.getDbFields();
			for (WorkCenterDbFieldVo dbFieldVo : dbFieldVos) {
				DictionaryChildVo typeDictChildVo = dbFieldVo.getTypeDictChild();
				DictionaryVo contentDictVo = dbFieldVo.getContentDict();
				DictionaryChildVo relationDictChildVo = dbFieldVo.getRelationDictChild();
				if (typeDictChildVo != null && StringUtils.isNoneBlank(typeDictChildVo.getChildId())) {
					dictionaryChildSet.add(typeDictChildVo.getChildId());
				}
				if (contentDictVo != null && StringUtils.isNoneBlank(contentDictVo.getDictionaryId())) {
					dictionarySet.add(contentDictVo.getDictionaryId());
				}
				if (relationDictChildVo != null && StringUtils.isNoneBlank(relationDictChildVo.getChildId())) {
					dictionaryChildSet.add(relationDictChildVo.getChildId());
				}
			}
		}
		// 子类查询父类
		for (String childId : dictionaryChildSet) {
			DictionaryChildBo dictionaryChildBo = new DictionaryChildBo();
			dictionaryChildBo.setChildId(childId);
			DictionaryChildVo dictionaryChildVo = iDictionaryChildService.get(dictionaryChildBo);
			dictionarySet.add(dictionaryChildVo.getDictionaryId());
		}
		// 填充字典
		List<DictionaryVo> dictionaryVos = new ArrayList<>();
		for (String dictionaryId : dictionarySet) {
			DictionaryVo dictionaryVo = iDictionaryService.getByDictionaryId(dictionaryId);
			if (DictionaryCode.DICTIONARY_PROPERTY_PRIVATE.equals(dictionaryVo.getDictionaryType())) {
				// 运维私有才需要导出,默认理解为公有字典同版本一致
				dictionaryVos.add(dictionaryVo);
			}
		}
		// 返回内容
		WorkCenterExportVo exportVo = new WorkCenterExportVo();
		exportVo.setEnterpriseid(getEnterpriseid());
		exportVo.setForm(formVo);
		exportVo.setDictionarys(dictionaryVos);
		exportVo.setExportDate(getCurr());
		exportVo.setVersion(Config.VERSION);
		exportVo.setSign(MD5Util.encode(Base64Utils.encoder(getEnterpriseid() + Config.VERSION)));
		return JSONUtils.toJSONString(exportVo);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
	public void imports(MultipartFile file) throws FrameworkRuntimeException {
		String str = null;
		try {
			byte[] buffer = file.getBytes();
			str = new String(buffer, StandardCharsets.UTF_8);
		} catch (IOException e) {
			logger.info(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.ILLEGAL_EXCEPTION, "文件读取有误");
		}
		// 上传转换
		WorkCenterExportBo exportBo = JSONUtils.parseObject(str, WorkCenterExportBo.class);
		logger.info(LogMsg.to("exportBo", JSONUtils.toJSONString(exportBo)));
		// 非空校验
		String enterpriseid = exportBo.getEnterpriseid();
		WorkCenterFormBo formBo = exportBo.getForm();
		List<DictionaryBo> dictionaryBos = exportBo.getDictionarys();
		Date exportDate = exportBo.getExportDate();
		String sign = exportBo.getSign();
		VerificationUtils.string("enterpriseid", enterpriseid);
		VerificationUtils.obj("formBo", formBo);
		VerificationUtils.obj("dictionaryBos", dictionaryBos);
		VerificationUtils.date("exportDate", exportDate);
		VerificationUtils.string("sign", sign);
		// 上传校验
		String encode = MD5Util.encode(Base64Utils.encoder(enterpriseid + Config.VERSION));
		if (!sign.equals(encode)) {
			throw new FrameworkRuntimeException(BaseExceptionConstants.ILLEGAL_EXCEPTION, "文件校验有误");
		}
		if (WaterConstants.ADMIN_ENTERPRISE_ID.equals(enterpriseid)) {
			// 导出表单为运维
			if (enterpriseid.equals(getEnterpriseid())) {
				// 运维->运维 不允许
				throw new FrameworkRuntimeException(BaseExceptionConstants.ILLEGAL_EXCEPTION, "运维平台不允许导入表单");
			} else {
				// 运维->水司 允许

			}
		} else {
			// 导出表单为水司
			if (WaterConstants.ADMIN_ENTERPRISE_ID.equals(getEnterpriseid())) {
				// 水司->运维 不允许
				throw new FrameworkRuntimeException(BaseExceptionConstants.ILLEGAL_EXCEPTION, "运维平台不允许导入表单！");
			} else if (enterpriseid.equals(getEnterpriseid())) {
				// 水司-> 同一水司 允许

			} else {
				// 水司-> 不同一水司 不允许
				throw new FrameworkRuntimeException(BaseExceptionConstants.ILLEGAL_EXCEPTION, "运维平台不允许导入表单！！");
			}
		}
		// 导入准备
		String formId = UuidUtils.getUuid();
		List<WorkCenterDbBo> dbBos = formBo.getDbs();
		String formCode = iNumRuleSetFactory
				.makeNo(BeanUtils.news(MakeNumRequest.class, "count", 1, "ruleid", NumRuleSetCode.FORM_NUM_SET))
				.getNumbers().get(0);
		List<String> dbCodes = iNumRuleSetFactory.makeNo(
				BeanUtils.news(MakeNumRequest.class, "count", dbBos.size(), "ruleid", NumRuleSetCode.SOURCE_NUM_SET))
				.getNumbers();
		// 表单导入
		formBo.setId(formId);
		formBo.setCode(formCode);
		formBo.setIfEffect(WaterConstants.WORK_CENTER_NO_EFFECT);
		formBo.setEnterpriseid(getEnterpriseid());
		formBo.setUserBy(getName());
		formBo.setCurr(getCurr());
		iFormService.add(formBo);
		// 数据源和 数据源字段导入
		List<WorkCenterDbBo> addDbBos = new ArrayList<>();
		List<WorkCenterDbFieldBo> addDbFieldBos = new ArrayList<>();
		for (int i = 0; i < dbBos.size(); i++) {
			WorkCenterDbBo dbBo = dbBos.get(i);
			List<WorkCenterDbFieldBo> dbFieldBos = dbBo.getDbFields();
			// 数据源导入
			String dbId = UuidUtils.getUuid();
			dbBo.setId(dbId);
			dbBo.setFormId(formId);
			dbBo.setCode(dbCodes.get(i));
			dbBo.setIfEffect(WaterConstants.WORK_CENTER_NO_EFFECT);
			dbBo.setEnterpriseid(getEnterpriseid());
			dbBo.setUserBy(getName());
			dbBo.setCurr(getCurr());
			addDbBos.add(dbBo);
			// 数据源字段导入
			for (WorkCenterDbFieldBo dbFieldBo : dbFieldBos) {
				String dbFieldId = UuidUtils.getUuid();
				dbFieldBo.setId(dbFieldId);
				dbFieldBo.setDbId(dbId);
				dbFieldBo.setEnterpriseid(getEnterpriseid());
				dbFieldBo.setUserBy(getName());
				dbFieldBo.setCurr(getCurr());
				addDbFieldBos.add(dbFieldBo);
				// 置换字典类别
				DictionaryBo contentDict = dbFieldBo.getContentDict();
				if (contentDict != null) {
					contentDict.setDictionaryId(
							DictionaryCode.copyDictionaryId(contentDict.getDictionaryId(), getEnterpriseid()));
					dbFieldBo.setContentDict(contentDict);
				}
			}
		}
		iDbService.adds(addDbBos);
		iDbFieldService.adds(addDbFieldBos);
		// 私有字典复制
		iDictionaryFactory.copy(dictionaryBos, getEnterpriseid());
	}

	@Override
	public Pagination<WorkCenterFormVo> pageByAdmin(WorkCenterFormForm formForm) throws FrameworkRuntimeException {
		WorkCenterFormBo formBo = BeanUtils.copy(formForm, WorkCenterFormBo.class);
		formBo.setEnterpriseid(WaterConstants.ADMIN_ENTERPRISE_ID);
		return iFormService.page(formBo);
	}
}