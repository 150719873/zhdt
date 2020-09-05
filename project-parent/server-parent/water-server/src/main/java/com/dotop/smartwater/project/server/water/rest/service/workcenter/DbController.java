package com.dotop.smartwater.project.server.water.rest.service.workcenter;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.dotop.smartwater.dependence.core.common.BaseController;
import com.dotop.smartwater.dependence.core.exception.BaseExceptionConstants;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.global.GlobalContext;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.dependence.core.utils.VerificationUtils;
import com.dotop.smartwater.project.module.api.workcenter.IDbFactory;
import com.dotop.smartwater.project.module.core.water.constants.DictionaryCode;
import com.dotop.smartwater.project.module.core.water.constants.ResultCode;
import com.dotop.smartwater.project.module.core.water.constants.WaterConstants;
import com.dotop.smartwater.project.module.core.water.form.DictionaryChildForm;
import com.dotop.smartwater.project.module.core.water.form.DictionaryForm;
import com.dotop.smartwater.project.module.core.water.form.WorkCenterDbFieldForm;
import com.dotop.smartwater.project.module.core.water.form.WorkCenterDbForm;
import com.dotop.smartwater.project.module.core.water.vo.WorkCenterDbVo;

/**
 * 工作中心数据源管理
 * 

 * @date 2019年4月17日
 * @description
 */
@RestController("WorkCenterDbController")
@RequestMapping("/workcenter/db")

public class DbController implements BaseController<WorkCenterDbForm> {

	@Autowired
	private IDbFactory iDbFactory;

	private static final int MAXLENGTH = 10240;

	private static final String ATTRIBUTE = "attribute";

	/**
	 * 查询分页
	 */
	@Override
	@PostMapping(value = "/page", produces = GlobalContext.PRODUCES)
	public String page(@RequestBody WorkCenterDbForm dbForm) {
		String formId = dbForm.getFormId();
		String name = dbForm.getName();
		String code = dbForm.getCode();
		Integer page = dbForm.getPage();
		Integer pageCount = dbForm.getPageCount();
		// 验证
		VerificationUtils.string("formId", formId);
		VerificationUtils.string("name", name, true);
		VerificationUtils.string("code", code, true);
		VerificationUtils.integer("page", page);
		VerificationUtils.integer("pageCount", pageCount);
		Pagination<WorkCenterDbVo> pagination = iDbFactory.page(dbForm);
		return resp(ResultCode.Success, ResultCode.SUCCESS, pagination);
	}

	/**
	 * 查询详情
	 */
	@Override
	@PostMapping(value = "/get", produces = GlobalContext.PRODUCES)
	public String get(@RequestBody WorkCenterDbForm dbForm) {
		String id = dbForm.getId();
		// 验证
		VerificationUtils.string("id", id);
		WorkCenterDbVo get = iDbFactory.get(dbForm);
		return resp(ResultCode.Success, ResultCode.SUCCESS, get);
	}

	/**
	 * 新增
	 */
	@Override
	@PostMapping(value = "/add", produces = GlobalContext.PRODUCES)
	public String add(@RequestBody WorkCenterDbForm dbForm) {
		String formId = dbForm.getFormId();
		String name = dbForm.getName();
		String loadType = dbForm.getLoadType();
		String sqlStr = dbForm.getSqlStr();
		List<WorkCenterDbFieldForm> dbFields = dbForm.getDbFields();
		// 验证
		VerificationUtils.string("formId", formId);
		VerificationUtils.string("name", name);
		VerificationUtils.string("loadType", loadType);
		if (WaterConstants.WORK_CENTER_DB_LOAD_TYPE_AUTO.equals(loadType)) {
			// 如果类型为自动载入
			// sql格式校验
			VerificationUtils.string("sqlStr", sqlStr, false, 10240, VerificationUtils.REG_SQL_SELECT);
		} else {
			dbForm.setSqlStr(null);
		}
		VerificationUtils.objList("dbFields", dbFields);
		Set<String> attributes = new HashSet<>();
		for (WorkCenterDbFieldForm dbField : dbFields) {
			String attribute = dbField.getAttribute();
			VerificationUtils.string(ATTRIBUTE, attribute);
			String fieldName = dbField.getName();
			VerificationUtils.string("fieldName", fieldName);
			String fieldType = dbField.getFieldType();
			VerificationUtils.string("fieldType", fieldType);
			if (WaterConstants.WORK_CENTER_DB_FIELD_TYPE_COMMON.equals(fieldType)) {
				// 普通类型
				DictionaryChildForm typeDictChild = dbField.getTypeDictChild();
				VerificationUtils.obj("typeDictChild", typeDictChild);
				String typeDictChildId = typeDictChild.getChildId();
				VerificationUtils.string("typeDictChildId", typeDictChildId);
				String typeDictChildValue = DictionaryCode.getChildValue(typeDictChildId);
				if (DictionaryCode.DICTIONARY_DATASOURCE_TYPR_SELECT.equals(typeDictChildValue)) {
					DictionaryForm contentDict = dbField.getContentDict();
					VerificationUtils.obj("contentDict", contentDict);
					String contentDictId = contentDict.getDictionaryId();
					VerificationUtils.string("contentDictId", contentDictId);
				}else{
					dbField.setContentDict(null);
				}
			} else if (WaterConstants.WORK_CENTER_DB_FIELD_TYPE_RELATION.equals(fieldType)) {
				// 筛选类型
				DictionaryChildForm typeDictChild = new DictionaryChildForm();
				typeDictChild.setChildId(DictionaryCode.getChildId(WaterConstants.ADMIN_ENTERPRISE_ID,
						DictionaryCode.DICTIONARY_DATASOURCE_TYPR, DictionaryCode.DICTIONARY_DATASOURCE_TYPR_SQL));
				dbField.setTypeDictChild(typeDictChild);
				DictionaryChildForm relationDictChild = dbField.getRelationDictChild();
				VerificationUtils.obj("relationDictChild", relationDictChild);
				String relationDictChildId = relationDictChild.getChildId();
				VerificationUtils.string("relationDictChildId", relationDictChildId);
			}
			attributes.add(attribute);
		}
		if (attributes.size() != dbFields.size()) {
			// 属性名不能相同
			throw new FrameworkRuntimeException(BaseExceptionConstants.PARAM_FORMAT_ERROR, new String[] { ATTRIBUTE });
		}
		WorkCenterDbVo add = iDbFactory.add(dbForm);
		return resp(ResultCode.Success, ResultCode.SUCCESS, add);
	}

	/**
	 * 修改
	 */
	@Override
	@PostMapping(value = "/edit", produces = GlobalContext.PRODUCES)
	public String edit(@RequestBody WorkCenterDbForm dbForm) {
		String id = dbForm.getId();
		String name = dbForm.getName();
		String loadType = dbForm.getLoadType();
		String sqlStr = dbForm.getSqlStr();
		List<WorkCenterDbFieldForm> dbFields = dbForm.getDbFields();
		// 验证
		VerificationUtils.string("id", id);
		VerificationUtils.string("name", name);
		if (WaterConstants.WORK_CENTER_DB_LOAD_TYPE_AUTO.equals(loadType)) {
			// 如果类型为自动载入
			VerificationUtils.string("sqlStr", sqlStr, false, MAXLENGTH, VerificationUtils.REG_SQL_SELECT);
		} else {
			dbForm.setSqlStr(null);
		}
		VerificationUtils.objList("dbFields", dbFields);
		Set<String> attributes = new HashSet<>();
		for (WorkCenterDbFieldForm dbField : dbFields) {
			String attribute = dbField.getAttribute();
			VerificationUtils.string(ATTRIBUTE, attribute);
			String fieldName = dbField.getName();
			VerificationUtils.string("fieldName", fieldName);
			String fieldType = dbField.getFieldType();
			VerificationUtils.string("fieldType", fieldType);
			if (WaterConstants.WORK_CENTER_DB_FIELD_TYPE_COMMON.equals(fieldType)) {
				// 普通类型
				DictionaryChildForm typeDictChild = dbField.getTypeDictChild();
				VerificationUtils.obj("typeDictChild", typeDictChild);
				String typeDictChildId = typeDictChild.getChildId();
				VerificationUtils.string("typeDictChildId", typeDictChildId);
				String typeDictChildValue = DictionaryCode.getChildValue(typeDictChildId);
				if (DictionaryCode.DICTIONARY_DATASOURCE_TYPR_SELECT.equals(typeDictChildValue)) {
					DictionaryForm contentDict = dbField.getContentDict();
					VerificationUtils.obj("contentDict", contentDict);
					String contentDictId = contentDict.getDictionaryId();
					VerificationUtils.string("contentDictId", contentDictId);
				}else{
					dbField.setContentDict(null);
				}
			} else if (WaterConstants.WORK_CENTER_DB_FIELD_TYPE_RELATION.equals(fieldType)) {
				// 筛选类型
				DictionaryChildForm typeDictChild = new DictionaryChildForm();
				typeDictChild.setChildId(DictionaryCode.getChildId(WaterConstants.ADMIN_ENTERPRISE_ID,
						DictionaryCode.DICTIONARY_DATASOURCE_TYPR, DictionaryCode.DICTIONARY_DATASOURCE_TYPR_SQL));
				dbField.setTypeDictChild(typeDictChild);
				DictionaryChildForm relationDictChild = dbField.getRelationDictChild();
				VerificationUtils.obj("relationDictChild", relationDictChild);
				String relationDictChildId = relationDictChild.getChildId();
				VerificationUtils.string("relationDictChildId", relationDictChildId);
			}
			attributes.add(attribute);
		}
		if (attributes.size() != dbFields.size()) {
			throw new FrameworkRuntimeException(BaseExceptionConstants.PARAM_FORMAT_ERROR, new String[] { ATTRIBUTE });
		}
		WorkCenterDbVo edit = iDbFactory.edit(dbForm);
		return resp(ResultCode.Success, ResultCode.SUCCESS, edit);
	}

	/**
	 * 删除
	 */
	@Override
	@PostMapping(value = "/del", produces = GlobalContext.PRODUCES)
	public String del(@RequestBody WorkCenterDbForm dbForm) {
		String id = dbForm.getId();
		// 验证
		VerificationUtils.string("id", id);
		iDbFactory.del(dbForm);
		return resp(ResultCode.Success, ResultCode.SUCCESS, null);
	}

	/**
	 * 设置自动加载数据源
	 */
	@PostMapping(value = "/load", produces = GlobalContext.PRODUCES)
	public String load(@RequestBody WorkCenterDbForm dbForm) {
		String id = dbForm.getId();
		String loadStatus = dbForm.getLoadStatus();
		// 验证
		VerificationUtils.string("id", id);
		VerificationUtils.string("loadStatus", loadStatus);
		iDbFactory.load(dbForm);
		return resp(ResultCode.Success, ResultCode.SUCCESS, null);
	}

	/**
	 * 数据源复制
	 */
	@PostMapping(value = "/copy", produces = GlobalContext.PRODUCES)
	public String copy(@RequestBody WorkCenterDbForm dbForm) {
		String id = dbForm.getId();
		// 验证
		VerificationUtils.string("id", id);
		WorkCenterDbVo copy = iDbFactory.copy(dbForm);
		return resp(ResultCode.Success, ResultCode.SUCCESS, copy);
	}

}
