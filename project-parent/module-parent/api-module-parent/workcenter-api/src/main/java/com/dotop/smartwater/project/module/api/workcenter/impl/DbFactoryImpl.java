package com.dotop.smartwater.project.module.api.workcenter.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.dotop.smartwater.project.module.api.workcenter.IDbFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.dotop.smartwater.dependence.core.exception.BaseExceptionConstants;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.dependence.core.utils.BeanUtils;
import com.dotop.smartwater.dependence.core.utils.UuidUtils;
import com.dotop.smartwater.project.module.api.revenue.INumRuleSetFactory;
import com.dotop.smartwater.project.module.api.tool.IDictionaryFactory;
import com.dotop.smartwater.project.module.core.auth.local.IAuthCasClient;
import com.dotop.smartwater.project.module.core.water.bo.DictionaryBo;
import com.dotop.smartwater.project.module.core.water.bo.WorkCenterDbBo;
import com.dotop.smartwater.project.module.core.water.bo.WorkCenterDbFieldBo;
import com.dotop.smartwater.project.module.core.water.constants.DictionaryCode;
import com.dotop.smartwater.project.module.core.water.constants.NumRuleSetCode;
import com.dotop.smartwater.project.module.core.water.constants.WaterConstants;
import com.dotop.smartwater.project.module.core.water.form.WorkCenterDbFieldForm;
import com.dotop.smartwater.project.module.core.water.form.WorkCenterDbForm;
import com.dotop.smartwater.project.module.core.water.form.customize.MakeNumRequest;
import com.dotop.smartwater.project.module.core.water.vo.DictionaryVo;
import com.dotop.smartwater.project.module.core.water.vo.WorkCenterDbFieldVo;
import com.dotop.smartwater.project.module.core.water.vo.WorkCenterDbVo;
import com.dotop.smartwater.project.module.service.tool.IDictionaryService;
import com.dotop.smartwater.project.module.service.workcenter.IDbFieldService;
import com.dotop.smartwater.project.module.service.workcenter.IDbService;

@Component("IWorkCenterDbFactory")
public class DbFactoryImpl implements IDbFactory, IAuthCasClient {

	@Autowired
	private IDbService iDbService;

	@Autowired
	private IDbFieldService iDbFieldService;

	// 该功能分布式可以改为fegin调用，脱离业务系统
	@Autowired
	private INumRuleSetFactory iNumRuleSetFactory;

	@Autowired
	private IDictionaryService iDictionaryService;

	// 该功能分布式可以改为fegin调用，脱离业务系统
	@Autowired
	private IDictionaryFactory iDictionaryFactory;

	@Override
	public Pagination<WorkCenterDbVo> page(WorkCenterDbForm dbForm) throws FrameworkRuntimeException {
		WorkCenterDbBo dbBo = BeanUtils.copy(dbForm, WorkCenterDbBo.class);
		dbBo.setEnterpriseid(getEnterpriseid());
		return iDbService.page(dbBo);
	}

	@Override
	public WorkCenterDbVo get(WorkCenterDbForm dbForm) throws FrameworkRuntimeException {
		WorkCenterDbBo dbBo = new WorkCenterDbBo();
		dbBo.setId(dbForm.getId());
		dbBo.setEnterpriseid(getEnterpriseid());
		return iDbService.get(dbBo);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
	public WorkCenterDbVo add(WorkCenterDbForm dbForm) throws FrameworkRuntimeException {
		// 系统生成编号
		String code = iNumRuleSetFactory
				.makeNo(BeanUtils.news(MakeNumRequest.class, "count", 1, "ruleid", NumRuleSetCode.SOURCE_NUM_SET))
				.getNumbers().get(0);
		// 新增数据源
		WorkCenterDbBo dbBo = BeanUtils.copy(dbForm, WorkCenterDbBo.class);
		dbBo.setId(UuidUtils.getUuid());
		dbBo.setCode(code);
		dbBo.setIfEffect(WaterConstants.WORK_CENTER_EFFECT);
		dbBo.setEnterpriseid(getEnterpriseid());
		dbBo.setUserBy(getName());
		dbBo.setCurr(getCurr());
		WorkCenterDbVo dbVo = iDbService.add(dbBo);
		// 新增属性
		List<WorkCenterDbFieldBo> dbFieldBoAdds = new ArrayList<>();
		List<WorkCenterDbFieldForm> dbFieldForms = dbForm.getDbFields();
		for (WorkCenterDbFieldForm dbFieldForm : dbFieldForms) {
			WorkCenterDbFieldBo dbFieldBo = BeanUtils.copy(dbFieldForm, WorkCenterDbFieldBo.class);
			dbFieldBo.setId(UuidUtils.getUuid());
			dbFieldBo.setDbId(dbVo.getId());
			dbFieldBo.setEnterpriseid(getEnterpriseid());
			dbFieldBo.setUserBy(getName());
			dbFieldBo.setCurr(getCurr());
			dbFieldBoAdds.add(dbFieldBo);
		}
		iDbFieldService.adds(dbFieldBoAdds);
		return dbVo;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
	public WorkCenterDbVo edit(WorkCenterDbForm dbForm) throws FrameworkRuntimeException {
		WorkCenterDbVo dbVo = iDbService.get(BeanUtils.news(WorkCenterDbBo.class, dbForm.getId(), getEnterpriseid()));
		if (dbVo == null) {
			throw new FrameworkRuntimeException(BaseExceptionConstants.BASE_ERROR, "数据源不存在");
		}
		// 传递数据
		List<WorkCenterDbFieldForm> dbFieldForms = dbForm.getDbFields();
		Map<String, WorkCenterDbFieldForm> dbFieldFormMap = dbFieldForms.stream()
				.collect(Collectors.toMap(WorkCenterDbFieldForm::getAttribute, a -> a, (k1, k2) -> k1));
		// 获取数据库数据源属性列表
		WorkCenterDbFieldBo dbFieldBo = new WorkCenterDbFieldBo();
		dbFieldBo.setDbId(dbForm.getId());
		List<WorkCenterDbFieldVo> dbFieldVos = iDbFieldService.list(dbFieldBo);
		Map<String, WorkCenterDbFieldVo> dbFieldVoMap = dbFieldVos.stream()
				.collect(Collectors.toMap(WorkCenterDbFieldVo::getAttribute, a -> a, (k1, k2) -> k1));
		// 过滤
		List<WorkCenterDbFieldBo> dbFieldBoAdds = new ArrayList<>();
		List<WorkCenterDbFieldBo> dbFieldBoDels = new ArrayList<>();
		List<WorkCenterDbFieldBo> dbFieldBoEdits = new ArrayList<>();
		// 从已有数据中查找传递
		for (String dbFieldAttributeVo : dbFieldVoMap.keySet()) {
			WorkCenterDbFieldVo wcdfv = dbFieldVoMap.get(dbFieldAttributeVo);
			WorkCenterDbFieldForm wcdff = dbFieldFormMap.get(dbFieldAttributeVo);
			if (wcdff == null) {
				// 不存在则删除
				WorkCenterDbFieldBo wcdfb = BeanUtils.copy(wcdfv, WorkCenterDbFieldBo.class);
				wcdfb.setEnterpriseid(getEnterpriseid());
				wcdfb.setUserBy(getName());
				wcdfb.setCurr(getCurr());
				dbFieldBoDels.add(wcdfb);
			} else {
				// 存在则编辑或不变
				// 属性、名字、类型、内容、关联关系判断是否编辑
				WorkCenterDbFieldBo wcdfb = BeanUtils.copy(wcdff, WorkCenterDbFieldBo.class);
				wcdfb.setEnterpriseid(getEnterpriseid());
				wcdfb.setUserBy(getName());
				wcdfb.setCurr(getCurr());
				dbFieldBoEdits.add(wcdfb);
			}
		}
		// 从传递数据中查找已有数据
		for (String dbFieldAttributeForm : dbFieldFormMap.keySet()) {
			WorkCenterDbFieldForm wcdff = dbFieldFormMap.get(dbFieldAttributeForm);
			WorkCenterDbFieldVo wcdfv = dbFieldVoMap.get(dbFieldAttributeForm);
			if (wcdfv == null) {
				// 不存在则新增
				WorkCenterDbFieldBo wcdfb = BeanUtils.copy(wcdff, WorkCenterDbFieldBo.class);
				wcdfb.setId(UuidUtils.getUuid());
				wcdfb.setDbId(dbForm.getId());
				wcdfb.setEnterpriseid(getEnterpriseid());
				wcdfb.setUserBy(getName());
				wcdfb.setCurr(getCurr());
				dbFieldBoAdds.add(wcdfb);
			}
		}
		// 过滤新增
		iDbFieldService.adds(dbFieldBoAdds);
		// 过滤编辑
		iDbFieldService.edits(dbFieldBoEdits);
		// 过滤删除
		iDbFieldService.dels(dbFieldBoDels);

		// 修改数据源
		WorkCenterDbBo dbBo = BeanUtils.copy(dbForm, WorkCenterDbBo.class);
		String ifEffect = dbVo.getIfEffect();
		if (!WaterConstants.WORK_CENTER_EFFECT.equals(ifEffect)) {
			// 编辑数据源为有效数据源和数据源修改
			dbBo.setIfEffect(WaterConstants.WORK_CENTER_EFFECT);
		}
		dbBo.setSqlStr(dbForm.getSqlStr());
		dbBo.setEnterpriseid(getEnterpriseid());
		dbBo.setUserBy(getName());
		dbBo.setCurr(getCurr());
		iDbService.edit(dbBo);
		return null;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
	public String del(WorkCenterDbForm dbForm) throws FrameworkRuntimeException {
		// 性能优化
		WorkCenterDbBo dbBo = new WorkCenterDbBo();
		dbBo.setId(dbForm.getId());
		dbBo.setEnterpriseid(getEnterpriseid());
		iDbService.del(dbBo);
		// 子类级联删除
		WorkCenterDbFieldBo dbFieldBo = new WorkCenterDbFieldBo();
		dbFieldBo.setDbId(dbForm.getId());
		dbFieldBo.setEnterpriseid(getEnterpriseid());
		iDbFieldService.del(dbFieldBo);
		return null;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
	public void load(WorkCenterDbForm dbForm) throws FrameworkRuntimeException {
		WorkCenterDbBo dbBo = new WorkCenterDbBo();
		dbBo.setId(dbForm.getId());
		dbBo.setLoadStatus(dbForm.getLoadStatus());
		dbBo.setEnterpriseid(getEnterpriseid());
		iDbService.edit(dbBo);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
	public WorkCenterDbVo copy(WorkCenterDbForm dbForm) throws FrameworkRuntimeException {
		// 校验数据源是否存在
		WorkCenterDbVo dbVo = iDbService.get(BeanUtils.news(WorkCenterDbBo.class, dbForm.getId()));
		if (dbVo == null) {
			throw new FrameworkRuntimeException(BaseExceptionConstants.BASE_ERROR, "数据源不存在");
		}
		// 系统生成编号
		String code = iNumRuleSetFactory
				.makeNo(BeanUtils.news(MakeNumRequest.class, "count", 1, "ruleid", NumRuleSetCode.SOURCE_NUM_SET))
				.getNumbers().get(0);
		String dbId = UuidUtils.getUuid();
		// 复制数据源
		WorkCenterDbBo dbBo = BeanUtils.copy(dbVo, WorkCenterDbBo.class);
		dbBo.setId(dbId);
		dbBo.setCode(code);
		dbBo.setIfEffect(WaterConstants.WORK_CENTER_NO_EFFECT);
		dbBo.setEnterpriseid(getEnterpriseid());
		dbBo.setUserBy(getName());
		dbBo.setCurr(getCurr());
		iDbService.add(dbBo);
		// 复制数据源字段
		List<WorkCenterDbFieldBo> dbFieldBoAdds = new ArrayList<>();
		List<WorkCenterDbFieldVo> dbFieldVos = dbVo.getDbFields();
		// 置换字典
		Set<String> dictionarySet = new HashSet<>();
		for (WorkCenterDbFieldVo dbFieldVo : dbFieldVos) {
			WorkCenterDbFieldBo dbFieldBo = BeanUtils.copy(dbFieldVo, WorkCenterDbFieldBo.class);
			dbFieldBo.setId(UuidUtils.getUuid());
			dbFieldBo.setDbId(dbId);
			dbFieldBo.setEnterpriseid(getEnterpriseid());
			dbFieldBo.setUserBy(getName());
			dbFieldBo.setCurr(getCurr());
			dbFieldBoAdds.add(dbFieldBo);
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
		iDbFieldService.adds(dbFieldBoAdds);
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
		return BeanUtils.news(WorkCenterDbVo.class, dbId);
	}

}
