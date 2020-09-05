package com.dotop.smartwater.project.module.api.workcenter.impl;

import com.dotop.smartwater.dependence.core.exception.BaseExceptionConstants;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.log.LogMsg;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.dependence.core.utils.*;
import com.dotop.smartwater.project.module.api.revenue.INumRuleSetFactory;
import com.dotop.smartwater.project.module.api.workcenter.IProcessHandleFactory;
import com.dotop.smartwater.project.module.api.workcenter.IWorkCenterBuildFactory;
import com.dotop.smartwater.project.module.api.workcenter.IWorkCenterFeedbackFactory;
import com.dotop.smartwater.project.module.service.workcenter.*;
import com.dotop.smartwater.project.module.api.tool.INoticeFactory;
import com.dotop.smartwater.project.module.core.auth.local.IAuthCasClient;
import com.dotop.smartwater.project.module.core.auth.vo.UserVo;
import com.dotop.smartwater.project.module.core.water.bo.*;
import com.dotop.smartwater.project.module.core.water.bo.customize.WorkCenterBuildBo;
import com.dotop.smartwater.project.module.core.water.bo.customize.WorkCenterFeedbackBo;
import com.dotop.smartwater.project.module.core.water.constants.DictionaryCode;
import com.dotop.smartwater.project.module.core.water.constants.NumRuleSetCode;
import com.dotop.smartwater.project.module.core.water.constants.WaterConstants;
import com.dotop.smartwater.project.module.core.water.form.WorkCenterProcessHandleForm;
import com.dotop.smartwater.project.module.core.water.form.customize.MakeNumRequest;
import com.dotop.smartwater.project.module.core.water.model.BodyMap;
import com.dotop.smartwater.project.module.core.water.utils.WorkCenterTmplUtils;
import com.dotop.smartwater.project.module.core.water.vo.*;
import com.dotop.smartwater.project.module.service.tool.IDictionaryService;
import com.dotop.smartwater.project.module.service.water.common.ICommonService;
import com.dotop.smartwater.project.module.service.workcenter.*;
import com.dotop.water.tool.service.BaseInf;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

@Component("IProcessHandleFactory")
public class ProcessHandleFactoryImpl implements IProcessHandleFactory, IAuthCasClient {

	private static final Logger logger = LogManager.getLogger(ProcessHandleFactoryImpl.class);

	@Autowired
	private ITmplService iTmplService;

	@Autowired
	private IDictionaryService iDictionaryService;

	// @Autowired
	// private IDictionaryChildService iDictionaryChildService;

	@Autowired
	private IProcessService iProcessService;

	@Autowired
	private IProcessMsgService iProcessMsgService;

	@Autowired
	private IProcessNodeService iProcessNodeService;

	@Autowired
	private IProcessFormService iProcessFormService;

	@Autowired
	private IProcessDbService iProcessDbService;

	@Autowired
	private IProcessDbFieldService iProcessDbFieldService;

	@Resource(name = "IWorkCenterBuildFactoryMap")
	private Map<String, IWorkCenterBuildFactory> iWorkCenterBuildFactoryMap;

	@Resource(name = "IWorkCenterFeedbackFactoryMap")
	private Map<String, IWorkCenterFeedbackFactory> iWorkCenterFeedbackFactoryMap;

	// 该功能分布式可以改为fegin调用，脱离业务系统
	@Autowired
	private INumRuleSetFactory iNumRuleSetFactory;

	@Autowired
	private ICommonService iCommonService;

	// 该功能分布式可以改为fegin调用，脱离业务系统
	@Autowired
	private INoticeFactory iNoticeFactory;

	@Override
	public WorkCenterProcessHandleVo show(WorkCenterProcessHandleForm processHandleForm)
			throws FrameworkRuntimeException {
		String enterpriseid = getEnterpriseid();
		String businessId = processHandleForm.getBusinessId();
		String businessType = processHandleForm.getBusinessType();
		// 业务模块
		IWorkCenterBuildFactory iWorkCenterBuildFactory = iWorkCenterBuildFactoryMap.get(businessType);
		if (iWorkCenterBuildFactory == null) {
			throw new FrameworkRuntimeException(BaseExceptionConstants.BASE_ERROR, "业务模块不存在");
		}
		WorkCenterBuildBo buildBo = iWorkCenterBuildFactory.get(businessId);
		if (buildBo == null) {
			throw new FrameworkRuntimeException(BaseExceptionConstants.BASE_ERROR, "业务模块参数不存在");
		}
		// 缓存参数
		String tmplId = buildBo.getTmplId();
		Map<String, String> showParams = buildBo.getShowParams();
		// 查询模板
		WorkCenterTmplBo tmplBo = new WorkCenterTmplBo();
		tmplBo.setId(tmplId);
		tmplBo.setEnterpriseid(enterpriseid);
		WorkCenterTmplVo tmplVo = iTmplService.get(tmplBo);
		if (tmplVo == null) {
			throw new FrameworkRuntimeException(BaseExceptionConstants.BASE_ERROR, "模板不存在");
		}
		// 表单数据
		WorkCenterFormVo formVo = tmplVo.getForm();
		String body = formVo.getBody();
		List<WorkCenterDbVo> dbVos = formVo.getDbs();
		// 渲染集合
		List<String> inputs = new ArrayList<>();
		Map<String, List<DictionaryChildBo>> selects = new HashMap<>();
		List<String> dates = new ArrayList<>();
		List<String> texts = new ArrayList<>();
		// 返回已载入的自动数据源
		List<WorkCenterDbVo> dbAutoVos = new ArrayList<>();
		for (WorkCenterDbVo dbVo : dbVos) {
			String loadType = dbVo.getLoadType();
			if (WaterConstants.WORK_CENTER_DB_LOAD_TYPE_EXTERNAL.equals(loadType)) {
				// 外部载入
				List<WorkCenterDbFieldVo> dbFieldVos = dbVo.getDbFields();
				for (WorkCenterDbFieldVo dbFieldVo : dbFieldVos) {
					// 获取字典类型
					// DictionaryChildVo dictionaryChildVo = iDictionaryChildService
					// .get(BeanUtils.copy(dbFieldVo.getTypeDictChild(), DictionaryChildBo.class));
					// String childValue = dictionaryChildVo.getChildValue();
					String childValue =
							DictionaryCode.getChildValue(dbFieldVo.getTypeDictChild().getChildId());
					switch (childValue) {
						case DictionaryCode.DICTIONARY_DATASOURCE_TYPR_INPUT:
							// 输入框
							inputs.add(dbFieldVo.getAttribute());
							break;
						case DictionaryCode.DICTIONARY_DATASOURCE_TYPR_SELECT:
							// 下拉控件
							DictionaryVo dictionaryVo =
									iDictionaryService.getByDictionaryId(
											dbFieldVo.getContentDict().getDictionaryId());
							selects.put(
									dbFieldVo.getAttribute(),
									BeanUtils.copy(dictionaryVo.getChildren(), DictionaryChildBo.class));
							break;
						case DictionaryCode.DICTIONARY_DATASOURCE_TYPR_DATE:
							// 时间控件
							dates.add(dbFieldVo.getAttribute());
							break;
						case DictionaryCode.DICTIONARY_DATASOURCE_TYPR_TEXT:
							// 文本
							texts.add(dbFieldVo.getAttribute());
							break;
						case DictionaryCode.DICTIONARY_DATASOURCE_TYPR_SQL:
							// sql参数
							break;
						default:
							break;
					}
				}
			} else if (WaterConstants.WORK_CENTER_DB_LOAD_TYPE_AUTO.equals(loadType)) {
				// 自动载入
				String loadStatus = dbVo.getLoadStatus();
				if (WaterConstants.WORK_CENTER_DB_LOAD_STATUS_LOAD.equals(loadStatus)) {
					dbAutoVos.add(dbVo);
				}
			}
		}
		// 输入框
		body = WorkCenterTmplUtils.inputs(body, inputs);
		// 下拉控件
		body = WorkCenterTmplUtils.selects(body, selects);
		// 时间控件
		body = WorkCenterTmplUtils.dates(body, dates);
		// 文本
		body = WorkCenterTmplUtils.texts(body, texts, showParams);

		String appBody = formVo.getAppBody();

		if(!StringUtils.isEmpty(appBody)){
			// 输入框
			appBody = WorkCenterTmplUtils.inputs(appBody, inputs);
			// 下拉控件
			appBody = WorkCenterTmplUtils.selects(appBody, selects);
			// 时间控件
			appBody = WorkCenterTmplUtils.dates(appBody, dates);
			// 文本
			appBody = WorkCenterTmplUtils.texts(appBody, texts, showParams);
		}

		WorkCenterProcessHandleVo processHandleVo = new WorkCenterProcessHandleVo();
		processHandleVo.setBody(body);
		processHandleVo.setAppBody(appBody);
		processHandleVo.setDbAutos(dbAutoVos);
		return processHandleVo;
	}

	@Override
	public WorkCenterProcessHandleVo listTmplDbAuto(WorkCenterProcessHandleForm processHandleForm)
			throws FrameworkRuntimeException {
		String enterpriseid = getEnterpriseid();
		String businessId = processHandleForm.getBusinessId();
		String businessType = processHandleForm.getBusinessType();
		String dbId = processHandleForm.getDbId();
		Integer page = processHandleForm.getPage();
		Integer pageCount = processHandleForm.getPageCount();

		// 业务模块
		IWorkCenterBuildFactory iWorkCenterBuildFactory = iWorkCenterBuildFactoryMap.get(businessType);
		if (iWorkCenterBuildFactory == null) {
			throw new FrameworkRuntimeException(BaseExceptionConstants.BASE_ERROR, "业务模块不存在");
		}
		WorkCenterBuildBo buildBo = iWorkCenterBuildFactory.get(businessId);
		if (buildBo == null) {
			throw new FrameworkRuntimeException(BaseExceptionConstants.BASE_ERROR, "业务模块参数不存在");
		}
		// 缓存参数
		String tmplId = buildBo.getTmplId();
		Map<String, String> sqlParams = buildBo.getSqlParams();
		// 查询模板
		WorkCenterTmplBo tmplBo = new WorkCenterTmplBo();
		tmplBo.setId(tmplId);
		tmplBo.setEnterpriseid(enterpriseid);
		WorkCenterTmplVo tmplVo = iTmplService.get(tmplBo);
		if (tmplVo == null) {
			throw new FrameworkRuntimeException(BaseExceptionConstants.BASE_ERROR, "模板不存在");
		}
		// 表单数据源
		WorkCenterFormVo formVo = tmplVo.getForm();
		List<WorkCenterDbVo> dbVos = formVo.getDbs();
		Map<String, WorkCenterDbVo> dbVoMap =
				dbVos.stream().collect(Collectors.toMap(WorkCenterDbVo::getId, a -> a, (k1, k2) -> k1));
		WorkCenterDbVo dbVo = dbVoMap.get(dbId);
		if (dbVo == null) {
			throw new FrameworkRuntimeException(BaseExceptionConstants.BASE_ERROR, "模板数据源不存在");
		}
		// 分为公共和关联关系
		List<WorkCenterDbFieldVo> dbFields = dbVo.getDbFields();
		List<WorkCenterDbFieldVo> commons = new ArrayList<>();
		List<WorkCenterDbFieldVo> relations = new ArrayList<>();
		for (WorkCenterDbFieldVo dbField : dbFields) {
			String fieldType = dbField.getFieldType();
			if (WaterConstants.WORK_CENTER_DB_FIELD_TYPE_COMMON.equals(fieldType)) {
				commons.add(dbField);
			} else if (WaterConstants.WORK_CENTER_DB_FIELD_TYPE_RELATION.equals(fieldType)) {
				relations.add(dbField);
			}
		}
		// String aaa = sqlParams.get("aaa");
		// List parseObject = JSONUtils.parseObject(aaa, List.class);

		// 过滤无用的sqlParams数据
		Map<String, String> relationSqlParams = new HashMap<>();
		for (WorkCenterDbFieldVo relation : relations) {
			String attribute = relation.getAttribute();
			String value = sqlParams.get(relation.getAttribute());
			if (StringUtils.isNotBlank(value)) {
				relationSqlParams.put(attribute, value);
			}
		}
		Map<String, WorkCenterDbFieldVo> relationVoMap =
				relations.stream()
						.collect(Collectors.toMap(WorkCenterDbFieldVo::getAttribute, a -> a, (k1, k2) -> k1));
		// relationSqlParams.put("limit", 100);
		// relationSqlParams.put("offset", 0);
		// 查询数据
		String sqlStr = dbVo.getSqlStr();
		// 模拟请求参数数据 模拟数据返回 id code
		// sqlStr = "select id,code from wc_process where id=#{id}";
		// relationSqlParams.put("id", "d307e956-7897-4f9b-af8a-656f98e49998");
		// relationSqlParams.put("sqlname", "超级管理员");
		// List<String> ids = BeanUtils.list("1", "2", "3");
		// relationSqlParams.put("ids", ids);
		Map<String, String> relationExSqlParams = new HashMap<>();
		for (String attribute : relationSqlParams.keySet()) {
			String value = relationSqlParams.get(attribute);
			WorkCenterDbFieldVo workCenterDbFieldVo = relationVoMap.get(attribute);
			String childValue =
					DictionaryCode.getChildValue(workCenterDbFieldVo.getRelationDictChild().getChildId());
			if (DictionaryCode.DICTIONARY_DATASOURCE_SQL_TYPR_STRING.equals(childValue)) {
				relationExSqlParams.put(attribute, value);
			} else if (DictionaryCode.DICTIONARY_DATASOURCE_SQL_TYPR_LIST.equals(childValue)) {
				// list
				List<String> vs = JSONUtils.parseArray(value, String.class);
				if (vs != null && !vs.isEmpty()) {
					// 采用简单方式直接替换，可以优化为占位符(需要考虑占位符相同和参数顺序)
					StringBuilder sb = new StringBuilder();
					for (String v : vs) {
						sb.append("'");
						sb.append(v);
						sb.append("',");
					}
					sb.deleteCharAt(sb.length() - 1);
					sqlStr = sqlStr.replaceAll("\\#\\{" + attribute + "\\}", sb.toString());
				}
			} else if (DictionaryCode.DICTIONARY_DATASOURCE_SQL_TYPR_DATE.equals(childValue)) {
				relationExSqlParams.put(attribute, value);
			} else {
				relationExSqlParams.put(attribute, null);
			}
		}

		List<Map<String, Object>> list = new ArrayList<>();
		long count = 0L;
		try {
			Pagination<Map<String, Object>> pagination =
					iCommonService.page(sqlStr, relationExSqlParams, page, pageCount);
			logger.info(LogMsg.to("pagination", pagination));
			list = pagination.getData();
			count = pagination.getTotalPageSize();
		} catch (Throwable e) {
			logger.error(LogMsg.to("ex", e));
		}
		// 返回结果集
		List<List<String>> columns = new ArrayList<>();
		List<List<String>> returns = new ArrayList<>();
		// 列头
		List<String> heads = new ArrayList<>();
		// 模拟数据内容
		// commons.add(BeanUtils.news(WorkCenterDbFieldVo.class, "attribute", "id",
		// "name", "序号"));
		// commons.add(BeanUtils.news(WorkCenterDbFieldVo.class, "attribute", "code",
		// "name", "编号"));
		// commons.add(BeanUtils.news(WorkCenterDbFieldVo.class, "attribute", "title",
		// "name", "标题"));
		for (WorkCenterDbFieldVo common : commons) {
			heads.add(common.getName());
		}
		columns.add(heads);
		// 内容
		// 过滤无用的返回结果
		for (Map<String, Object> map : list) {
			List<String> contents = new ArrayList<>();
			for (WorkCenterDbFieldVo common : commons) {
				String attribute = common.getAttribute();
				Object value = map.get(attribute);
				if (value != null) {
					if (value instanceof String) {
						contents.add(String.valueOf(value));
					} else if (value instanceof Timestamp) {
						contents.add(DateUtils.formatDatetime((Date) value));
					} else if (value instanceof Date) {
						contents.add(DateUtils.formatDatetime((Date) value));
					} else {
						contents.add(String.valueOf(value));
					}
				} else {
					contents.add("");
				}
			}
			returns.add(contents);
		}
		WorkCenterProcessHandleVo processHandleVo = new WorkCenterProcessHandleVo();
		processHandleVo.setDbAutoColumns(columns);
		processHandleVo.setDbAutoReturns(returns);
		processHandleVo.setDbAutoCount(count);
		return processHandleVo;
	}

	// 数据库操作可以修改优化
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
	public WorkCenterProcessHandleVo add(WorkCenterProcessHandleForm processHandleForm) {
		String enterpriseid = processHandleForm.getEnterpriseid();
		if (StringUtils.isEmpty(enterpriseid)) {
			enterpriseid = getEnterpriseid();
		}

		String userId = "";
		String username = "";
		Date curr = new Date();

		try{
			userId = getUserid();
			username = getName();
		}catch(Exception e){
			logger.error(e.getMessage());
		}

		if(StringUtils.isEmpty(userId) || StringUtils.isEmpty(username)){
			List<UserVo> userList = BaseInf.getUserList(enterpriseid);
			for(UserVo user:userList){
				if(user.getType() == WaterConstants.USER_TYPE_ADMIN_ENTERPRISE){
					userId = user.getUserid();
					username = user.getName();
					break;
				}
			}
		}
		String businessId = processHandleForm.getBusinessId();
		String businessType = processHandleForm.getBusinessType();
		// 业务模块
		IWorkCenterFeedbackFactory iWorkCenterFeedbackFactory =
				iWorkCenterFeedbackFactoryMap.get(businessType);
		if (iWorkCenterFeedbackFactory == null) {
			throw new FrameworkRuntimeException(BaseExceptionConstants.BASE_ERROR, "业务模块不存在");
		}
		// 业务模块
		IWorkCenterBuildFactory iWorkCenterBuildFactory = iWorkCenterBuildFactoryMap.get(businessType);
		if (iWorkCenterBuildFactory == null) {
			throw new FrameworkRuntimeException(BaseExceptionConstants.BASE_ERROR, "业务模块不存在");
		}
		WorkCenterBuildBo buildBo = iWorkCenterBuildFactory.get(businessId);
		if (buildBo == null) {
			throw new FrameworkRuntimeException(BaseExceptionConstants.BASE_ERROR, "业务模块参数不存在");
		}
		// 缓存参数
		String tmplId = buildBo.getTmplId();
		String title = processHandleForm.getTitle();
		Map<String, String> sqlParams = buildBo.getSqlParams();
		Map<String, String> showParams = buildBo.getShowParams();
		Map<String, String> fillParams = processHandleForm.getFillParams();
		Map<String, String> carryParams = buildBo.getCarryParams();
		String processId = UuidUtils.getUuid();
		String processFormId = UuidUtils.getUuid();
		// 查询模板表单数据
		WorkCenterTmplBo tmplBo = new WorkCenterTmplBo();
		tmplBo.setId(tmplId);
		WorkCenterTmplVo tmplVo = iTmplService.get(tmplBo);
		if(tmplVo == null){
			throw new FrameworkRuntimeException(BaseExceptionConstants.BASE_ERROR, "流程模板不存在");
		}
		// 系统生成编号
		MakeNumRequest makeNumRequest = new MakeNumRequest(1, NumRuleSetCode.PROCESS_NUM_SET);
		makeNumRequest.setEnterpriseid(enterpriseid);
		String processCode = iNumRuleSetFactory.makeNo(makeNumRequest).getNumbers().get(0);
		// 新增流程(复制模板)
		WorkCenterProcessBo processBo = new WorkCenterProcessBo();
		processBo.setId(processId);
		processBo.setCode(processCode);
		processBo.setTitle(title);
		processBo.setBusinessId(businessId);
		processBo.setBusinessType(businessType);
		processBo.setSqlParams(sqlParams);
		processBo.setShowParams(showParams);
		processBo.setFillParams(fillParams);
		processBo.setCarryParams(carryParams);
		processBo.setStatus(WaterConstants.WORK_CENTER_PROCESS_APPLY);
		processBo.setApplicant(userId);
		processBo.setApplicantName(username);
		processBo.setApplicationDate(curr);
		processBo.setProcessFormId(processFormId);
		processBo.setTmplId(tmplId);
		processBo.setUserBy(username);
		processBo.setCurr(curr);
		processBo.setEnterpriseid(enterpriseid);
		iProcessService.add(processBo);
		// 新增流程节点(复制模板节点)
		List<WorkCenterTmplNodePointVo> tmplNodePointVos = tmplVo.getNodes();
		WorkCenterProcessNodeBo processFirstNodeBo = null;
		for (WorkCenterTmplNodePointVo tmplNodePointVo : tmplNodePointVos) {
			WorkCenterTmplNodeVo tmplNodeVo = tmplNodePointVo.getParams();
			boolean firstNodeFlag = false;
			String processNodeId = MD5Util.encode(processId + tmplNodeVo.getId());
			WorkCenterProcessNodeBo processNodeBo = new WorkCenterProcessNodeBo();
			processNodeBo.setId(processNodeId);
			processNodeBo.setProcessId(processId);
			processNodeBo.setName(tmplNodeVo.getName());
			processNodeBo.setType(tmplNodeVo.getType());
			processNodeBo.setSort(tmplNodeVo.getSort());
			if (tmplNodeVo.getType().equals(WaterConstants.WORK_CENTER_NODE_TYPE_FIRST)) {
				firstNodeFlag = true;
			}
			processNodeBo.setProcessNodeParentId(WaterConstants.WORK_CENTER_NODE_PARENT);
			processNodeBo.setHandlers(tmplNodeVo.getHandlers());
			processNodeBo.setCarbonCopyers(tmplNodeVo.getCarbonCopyers());
			processNodeBo.setHandlerRoles(tmplNodeVo.getHandlerRoles());
			processNodeBo.setCarbonCopyerRoles(tmplNodeVo.getCarbonCopyerRoles());
			processNodeBo.setHandleDict(BeanUtils.copy(tmplNodeVo.getHandleDict(), DictionaryBo.class));
			processNodeBo.setIfVerify(tmplNodeVo.getIfVerify());
			if (StringUtils.isBlank(tmplNodeVo.getVerifyTmplNodeId())) {
				processNodeBo.setVerifyProcessNodeId(null);
			} else {
				processNodeBo.setVerifyProcessNodeId(
						MD5Util.encode(processId + tmplNodeVo.getVerifyTmplNodeId()));
			}
			if (StringUtils.isBlank(tmplNodeVo.getNoVerifyTmplNodeId())) {
				processNodeBo.setNoVerifyProcessNodeId(null);
			} else {
				processNodeBo.setNoVerifyProcessNodeId(
						MD5Util.encode(processId + tmplNodeVo.getNoVerifyTmplNodeId()));
			}
			processNodeBo.setIfNotice(tmplNodeVo.getIfNotice());
			processNodeBo.setNoticers(tmplNodeVo.getNoticers());
			processNodeBo.setNoticerRoles(tmplNodeVo.getNoticerRoles());
			processNodeBo.setIfUpdate(tmplNodeVo.getIfUpdate());
			processNodeBo.setUpdateDictChild(
					BeanUtils.copy(tmplNodeVo.getUpdateDictChild(), DictionaryChildBo.class));
			processNodeBo.setIfPhoto(tmplNodeVo.getIfPhoto());
			processNodeBo.setPhotoNum(tmplNodeVo.getPhotoNum());
			processNodeBo.setIfUpload(tmplNodeVo.getIfUpload());
			processNodeBo.setUploadNum(tmplNodeVo.getUploadNum());
			processNodeBo.setIfOpinion(tmplNodeVo.getIfOpinion());
			processNodeBo.setOpinionNum(tmplNodeVo.getOpinionNum());
			processNodeBo.setTmplNodeId(tmplNodeVo.getId());
			processNodeBo.setUserBy(username);
			processNodeBo.setCurr(curr);
			processNodeBo.setEnterpriseid(enterpriseid);
			iProcessNodeService.add(processNodeBo);
			// 流程记录下一个节点
			if (firstNodeFlag) {
				DictionaryChildBo currHandleDictChild = new DictionaryChildBo();
				currHandleDictChild.setChildId(
						DictionaryCode.getChildId(
								WaterConstants.ADMIN_ENTERPRISE_ID,
								DictionaryCode.JUDGE_CODE,
								WaterConstants.DICTIONARY_1)); // 标记 通过
				processFirstNodeBo = processNodeBo;
				// 记录开始节点为下一个节点
				WorkCenterProcessBo processNextBo = new WorkCenterProcessBo();
				processNextBo.setId(processId);
				processNextBo.setCurrHandleDictChild(currHandleDictChild);
				processNextBo.setNextProcessNodeId(processNodeId);
				processNextBo.setNextStatus(WaterConstants.WORK_CENTER_PROCESS_APPLY);
				processNextBo.setNextHandlers(processFirstNodeBo.getHandlers());
				processNextBo.setNextCarbonCopyers(processFirstNodeBo.getCarbonCopyers());
				processNextBo.setNextHandlerRoles(processFirstNodeBo.getHandlerRoles());
				processNextBo.setNextCarbonCopyerRoles(processFirstNodeBo.getCarbonCopyerRoles());
				processNextBo.setAssignHandler(null);
				processNextBo.setAssignHandlerName(null);
				processNextBo.setUserBy(username);
				processNextBo.setCurr(curr);
				processNextBo.setEnterpriseid(enterpriseid);
				iProcessService.editNext(processNextBo);
				// 新增申请节点为模板节点，位于开始节点之前
				WorkCenterProcessNodeBo processApplyNodeBo = new WorkCenterProcessNodeBo();
				processNodeId = UuidUtils.getUuid();
				processApplyNodeBo.setId(processNodeId);
				processApplyNodeBo.setProcessId(processId);
				processApplyNodeBo.setName(WaterConstants.WORK_CENTER_NODE_NAME_APPLY);
				processApplyNodeBo.setType(WaterConstants.WORK_CENTER_NODE_TYPE_APPLY);
				processApplyNodeBo.setSort(WaterConstants.WORK_CENTER_NODE_SORT);
				processApplyNodeBo.setProcessNodeParentId(WaterConstants.WORK_CENTER_NODE_PARENT);
				// 初始化处理字典
				DictionaryBo dictionaryBo = new DictionaryBo();
				dictionaryBo.setDictionaryId(
						DictionaryCode.getDictionaryId(
								WaterConstants.ADMIN_ENTERPRISE_ID, DictionaryCode.JUDGE_CODE));
				processApplyNodeBo.setHandleDict(dictionaryBo);
				processApplyNodeBo.setIfVerify(WaterConstants.WORK_CENTER_NODE_NO_USE);
				processApplyNodeBo.setVerifyProcessNodeId(null);
				processApplyNodeBo.setNoVerifyProcessNodeId(processFirstNodeBo.getId());
				processApplyNodeBo.setIfNotice(WaterConstants.WORK_CENTER_NODE_USE); // 通知申请人
				processApplyNodeBo.setNoticers(BeanUtils.list(userId));
				processApplyNodeBo.setIfUpdate(WaterConstants.WORK_CENTER_NODE_NO_USE);
				processApplyNodeBo.setIfPhoto(WaterConstants.WORK_CENTER_NODE_USE);
				processApplyNodeBo.setPhotoNum("5");
				processApplyNodeBo.setIfUpload(WaterConstants.WORK_CENTER_NODE_USE);
				processApplyNodeBo.setUploadNum("5");
				processApplyNodeBo.setIfOpinion(WaterConstants.WORK_CENTER_NODE_USE);
				processApplyNodeBo.setOpinionNum("2000");
				processApplyNodeBo.setTmplNodeId(null);
				processApplyNodeBo.setUserBy(username);
				processApplyNodeBo.setCurr(curr);
				processApplyNodeBo.setEnterpriseid(enterpriseid);
				iProcessNodeService.add(processApplyNodeBo);
				// 新增申请节点作为第一个流程信息
				WorkCenterProcessMsgBo processMsgBo = new WorkCenterProcessMsgBo();
				processMsgBo.setProcessId(processId);
				processMsgBo.setHandleDictChild(currHandleDictChild);
				processMsgBo.setStatus(WaterConstants.WORK_CENTER_PROCESS_APPLY);
				processMsgBo.setProcessNodeId(processNodeId);
				processMsgBo.setOpinionContent(WaterConstants.WORK_CENTER_NODE_NAME_APPLY);
				processMsgBo.setCompleter(userId);
				processMsgBo.setCompleterName(username);
				processMsgBo.setCompleteDate(curr);
				processMsgBo.setEnterpriseid(enterpriseid);
				processMsgBo.setUserBy(username);
				processMsgBo.setCurr(curr);
				iProcessMsgService.add(processMsgBo);
			}
		}
		// 复制表单
		WorkCenterFormVo formVo = tmplVo.getForm();
		WorkCenterProcessFormBo processFormBo = new WorkCenterProcessFormBo();
		processFormBo.setId(processFormId);
		processFormBo.setProcessId(processId);
		processFormBo.setCode(formVo.getCode());
		processFormBo.setName(formVo.getName());
		processFormBo.setBody(formVo.getBody());
		processFormBo.setAppBody(formVo.getAppBody());
		processFormBo.setBodyMap(formVo.getBodyMap());
		processFormBo.setFormId(formVo.getId());
		processFormBo.setUserBy(username);
		processFormBo.setCurr(curr);
		processFormBo.setEnterpriseid(enterpriseid);
		iProcessFormService.add(processFormBo);
		// 复制表单数据源
		List<WorkCenterDbVo> dbVos = formVo.getDbs();
		for (WorkCenterDbVo dbVo : dbVos) {
			String processDbId = UuidUtils.getUuid();
			WorkCenterProcessDbBo processDbBo = new WorkCenterProcessDbBo();
			processDbBo.setId(processDbId);
			processDbBo.setProcessId(processId);
			processDbBo.setProcessFormId(processFormId);
			processDbBo.setName(dbVo.getName());
			processDbBo.setLoadType(dbVo.getLoadType());
			processDbBo.setLoadStatus(dbVo.getLoadStatus());
			processDbBo.setSqlStr(dbVo.getSqlStr());
			processDbBo.setDbId(dbVo.getId());
			processDbBo.setUserBy(username);
			processDbBo.setCurr(curr);
			processDbBo.setEnterpriseid(enterpriseid);
			iProcessDbService.add(processDbBo);
			// 复制表单数据源字段
			List<WorkCenterDbFieldVo> dbFieldVos = dbVo.getDbFields();
			for (WorkCenterDbFieldVo dbFieldVo : dbFieldVos) {
				WorkCenterProcessDbFieldBo processDbFieldBo = new WorkCenterProcessDbFieldBo();
				processDbFieldBo.setId(UuidUtils.getUuid());
				processDbFieldBo.setProcessId(processId);
				processDbFieldBo.setProcessDbId(processDbId);
				processDbFieldBo.setAttribute(dbFieldVo.getAttribute());
				processDbFieldBo.setName(dbFieldVo.getName());
				processDbFieldBo.setFieldType(dbFieldVo.getFieldType());
				processDbFieldBo.setTypeDictChild(dbFieldVo.getTypeDictChild());
				processDbFieldBo.setContentDict(dbFieldVo.getContentDict());
				processDbFieldBo.setRelationDictChild(dbFieldVo.getRelationDictChild());
				processDbFieldBo.setDbFieldId(dbFieldVo.getId());
				processDbFieldBo.setUserBy(username);
				processDbFieldBo.setCurr(curr);
				processDbFieldBo.setEnterpriseid(enterpriseid);
				iProcessDbFieldService.add(processDbFieldBo);
			}
		}
		// 通知业务模块
		// if
		// (WaterConstants.WORK_CENTER_NODE_USE.equals(processFirstNodeBo.getIfUpdate()))
		// {
		WorkCenterFeedbackBo feedbackBo = new WorkCenterFeedbackBo();
		feedbackBo.setProcessId(processId);
		feedbackBo.setProcessCode(processCode);
		feedbackBo.setBusinessId(businessId);
		feedbackBo.setBusinessType(businessType);
		feedbackBo.setSqlParams(sqlParams);
		feedbackBo.setShowParams(showParams);
		feedbackBo.setFillParams(fillParams);
		feedbackBo.setCarryParams(carryParams);
		feedbackBo.setUserBy(username);
		feedbackBo.setProcessStatus(WaterConstants.WORK_CENTER_NODE_TYPE_APPLY);
		logger.info(LogMsg.to("feedbackBo", feedbackBo));
		logger.info(LogMsg.to("msg", " 通知业务模块"));
		iWorkCenterFeedbackFactory.add(feedbackBo);
		// }

		// 发起消息通知
		// 通知开始节点处理人
		logger.info(LogMsg.to("msg", "发起消息通知"));
		NoticeBo noticeBo = new NoticeBo();
		noticeBo.setTitle("流程：" + processBo.getTitle() + "," + "有新的消息");
		noticeBo.setBody("流程：" + processBo.getTitle() + "," + "已提交新流程，请尽快处理");
		noticeBo.setSendWayList(BeanUtils.list(NoticeVo.NOTICE_SENDWAY_SYS));
		if (processFirstNodeBo.getNoticers() != null && !processFirstNodeBo.getNoticers().isEmpty()) {
			// 发送用户
			noticeBo.setReceiveWay(NoticeVo.NOTICE_RECEIVEWAY_USER);
			List<ReceiveObjectVo> users = new ArrayList<>();
			for (String userid : processFirstNodeBo.getNoticers()) {
				ReceiveObjectVo receiveObject = new ReceiveObjectVo();
				receiveObject.setId(userid);
				users.add(receiveObject);
			}
			noticeBo.setReceiveObjList(users);
			iNoticeFactory.sendNotice(noticeBo, enterpriseid, userId, username);
		} else if (processFirstNodeBo.getNoticerRoles() != null
				&& !processFirstNodeBo.getNoticerRoles().isEmpty()) {
			// 发送角色
			noticeBo.setReceiveWay(NoticeVo.NOTICE_RECEIVEWAY_ROLE);
			List<ReceiveObjectVo> roles = new ArrayList<>();
			for (String roleid : processFirstNodeBo.getNoticerRoles()) {
				ReceiveObjectVo receiveObject = new ReceiveObjectVo();
				receiveObject.setId(roleid);
				roles.add(receiveObject);
			}
			noticeBo.setReceiveObjList(roles);
			iNoticeFactory.sendNotice(noticeBo, enterpriseid, userId, username);
		}
		return null;
	}

	// 显示填充数据表单
	@Override
	public WorkCenterProcessHandleVo get(WorkCenterProcessHandleForm processHandleForm)
			throws FrameworkRuntimeException {
		String processId = processHandleForm.getProcessId();
		// 查询流程
		WorkCenterProcessBo processBo = new WorkCenterProcessBo();
		processBo.setId(processId);
		processBo.setEnterpriseid(getEnterpriseid());
		WorkCenterProcessVo processVo = iProcessService.get(processBo);

		// 表单数据
		WorkCenterProcessFormVo processFormVo = processVo.getProcessForm();
		String body = processFormVo.getBody();
		List<BodyMap> bodyMap = processFormVo.getBodyMap();
		List<WorkCenterProcessDbVo> processDbVos = processFormVo.getProcessDbs();
		// 渲染集合
		List<String> inputs = new ArrayList<>();
		Map<String, List<DictionaryChildBo>> selects = new HashMap<>();
		List<String> dates = new ArrayList<>();
		List<String> texts = new ArrayList<>();
		// 返回已载入的自动数据源
		List<WorkCenterProcessDbVo> processDbAutoVos = new ArrayList<>();
		for (WorkCenterProcessDbVo processDbVo : processDbVos) {
			if (WaterConstants.WORK_CENTER_DB_LOAD_TYPE_EXTERNAL.equals(processDbVo.getLoadType())) {
				List<WorkCenterProcessDbFieldVo> processDbFieldVos = processDbVo.getProcessDbFields();
				for (WorkCenterProcessDbFieldVo processDbFieldVo : processDbFieldVos) {
					// 获取字典类型
					// DictionaryChildVo dictionaryChildVo = iDictionaryChildService
					// .get(BeanUtils.copy(processDbFieldVo.getTypeDictChild(),
					// DictionaryChildBo.class));
					// String childValue = dictionaryChildVo.getChildValue();
					String childValue =
							DictionaryCode.getChildValue(processDbFieldVo.getTypeDictChild().getChildId());
					switch (childValue) {
						case DictionaryCode.DICTIONARY_DATASOURCE_TYPR_INPUT:
							// 输入框
							inputs.add(processDbFieldVo.getAttribute());
							break;
						case DictionaryCode.DICTIONARY_DATASOURCE_TYPR_SELECT:
							// 下拉控件
							DictionaryVo dictionaryVo =
									iDictionaryService.getByDictionaryId(
											processDbFieldVo.getContentDict().getDictionaryId());
							selects.put(
									processDbFieldVo.getAttribute(),
									BeanUtils.copy(dictionaryVo.getChildren(), DictionaryChildBo.class));
							break;
						case DictionaryCode.DICTIONARY_DATASOURCE_TYPR_DATE:
							// 时间控件
							dates.add(processDbFieldVo.getAttribute());
							break;
						case DictionaryCode.DICTIONARY_DATASOURCE_TYPR_TEXT:
							// 文本
							texts.add(processDbFieldVo.getAttribute());
							break;
						case DictionaryCode.DICTIONARY_DATASOURCE_TYPR_SQL:
							// sql参数
							break;
						default:
							break;
					}
				}
			} else if (WaterConstants.WORK_CENTER_DB_LOAD_TYPE_AUTO.equals(processDbVo.getLoadType())) {
				// 自动载入
				String loadStatus = processDbVo.getLoadStatus();
				if (WaterConstants.WORK_CENTER_DB_LOAD_STATUS_LOAD.equals(loadStatus)) {
					processDbAutoVos.add(processDbVo);
				}
			}
		}
		// 输入框
		body = WorkCenterTmplUtils.inputs(body, inputs, processVo.getFillParams());
		// 下拉控件
		body = WorkCenterTmplUtils.selects(body, selects, processVo.getFillParams());
		// 时间控件
		body = WorkCenterTmplUtils.dates(body, dates, processVo.getFillParams());
		// 文本
		body = WorkCenterTmplUtils.texts(body, texts, processVo.getShowParams());

		String appBody = processFormVo.getAppBody();

		if(!StringUtils.isEmpty(appBody)){
			// 输入框
			appBody = WorkCenterTmplUtils.inputs(appBody, inputs, processVo.getFillParams());
			// 下拉控件
			appBody = WorkCenterTmplUtils.selects(appBody, selects, processVo.getFillParams());
			// 时间控件
			appBody = WorkCenterTmplUtils.dates(appBody, dates, processVo.getFillParams());
			// 文本
			appBody = WorkCenterTmplUtils.texts(appBody, texts, processVo.getShowParams());
		}

		// bodyMap
		bodyMap =
				WorkCenterTmplUtils.bodyMap(
						bodyMap,
						inputs,
						selects,
						dates,
						texts,
						processVo.getFillParams(),
						processVo.getShowParams());
		WorkCenterProcessHandleVo processHandleVo = new WorkCenterProcessHandleVo();
		processHandleVo.setBody(body);
		processHandleVo.setAppBody(appBody);
		processHandleVo.setBodyMap(bodyMap);
		processHandleVo.setProcessDbAutos(processDbAutoVos);
		return processHandleVo;
	}

	@Override
	public WorkCenterProcessHandleVo listProcessDbAuto(WorkCenterProcessHandleForm processHandleForm)
			throws FrameworkRuntimeException {
		String processId = processHandleForm.getProcessId();
		String processDbId = processHandleForm.getProcessDbId();
		Integer page = processHandleForm.getPage();
		Integer pageCount = processHandleForm.getPageCount();
		// 查询流程
		WorkCenterProcessBo processBo = new WorkCenterProcessBo();
		processBo.setId(processId);
		processBo.setEnterpriseid(getEnterpriseid());
		WorkCenterProcessVo processVo = iProcessService.get(processBo);
		// 表单数据源
		WorkCenterProcessFormVo processFormVo = processVo.getProcessForm();
		List<WorkCenterProcessDbVo> processDbVos = processFormVo.getProcessDbs();
		Map<String, WorkCenterProcessDbVo> processDbVoMap =
				processDbVos.stream()
						.collect(Collectors.toMap(WorkCenterProcessDbVo::getId, a -> a, (k1, k2) -> k1));
		WorkCenterProcessDbVo processDbVo = processDbVoMap.get(processDbId);
		if (processDbVo == null) {
			throw new FrameworkRuntimeException(BaseExceptionConstants.BASE_ERROR, "流程数据源不存在");
		}
		// 分为公共和关联关系
		List<WorkCenterProcessDbFieldVo> processDbFields = processDbVo.getProcessDbFields();
		List<WorkCenterProcessDbFieldVo> commons = new ArrayList<>();
		List<WorkCenterProcessDbFieldVo> relations = new ArrayList<>();
		for (WorkCenterProcessDbFieldVo processDbField : processDbFields) {
			String fieldType = processDbField.getFieldType();
			if (WaterConstants.WORK_CENTER_DB_FIELD_TYPE_COMMON.equals(fieldType)) {
				commons.add(processDbField);
			} else if (WaterConstants.WORK_CENTER_DB_FIELD_TYPE_RELATION.equals(fieldType)) {
				relations.add(processDbField);
			}
		}
		// 过滤无用的sqlParams数据
		Map<String, String> sqlParams = processVo.getSqlParams();
		Map<String, String> relationSqlParams = new HashMap<>();
		for (WorkCenterProcessDbFieldVo relation : relations) {
			String attribute = relation.getAttribute();
			String value = sqlParams.get(relation.getAttribute());
			if (StringUtils.isNotBlank(attribute)) {
				relationSqlParams.put(attribute, value);
			}
		}
		Map<String, WorkCenterProcessDbFieldVo> relationVoMap =
				relations.stream()
						.collect(
								Collectors.toMap(WorkCenterProcessDbFieldVo::getAttribute, a -> a, (k1, k2) -> k1));
		// relationSqlParams.put("limit", 100);
		// relationSqlParams.put("offset", 0);
		// 查询数据
		String sqlStr = processDbVo.getSqlStr();
		// 模拟请求参数数据 模拟数据返回 id code
		// sqlStr = "select id,code from wc_process where id=#{id}";
		// relationSqlParams.put("id", "d307e956-7897-4f9b-af8a-656f98e49998");
		// relationSqlParams.put("sqlname", "超级管理员");
		// List<String> ids = BeanUtils.list("1", "2", "3");
		// relationSqlParams.put("ids", ids);
		Map<String, String> relationExSqlParams = new HashMap<>();
		for (String attribute : relationSqlParams.keySet()) {
			String value = relationSqlParams.get(attribute);
			WorkCenterProcessDbFieldVo workCenterDbFieldVo = relationVoMap.get(attribute);
			String childValue =
					DictionaryCode.getChildValue(workCenterDbFieldVo.getRelationDictChild().getChildId());
			if (DictionaryCode.DICTIONARY_DATASOURCE_SQL_TYPR_STRING.equals(childValue)) {
				relationExSqlParams.put(attribute, value);
			} else if (DictionaryCode.DICTIONARY_DATASOURCE_SQL_TYPR_LIST.equals(childValue)) {
				// list
				List<String> vs = JSONUtils.parseArray(value, String.class);
				if (vs != null && !vs.isEmpty()) {
					// 采用简单方式直接替换，可以优化为占位符(需要考虑占位符相同和参数顺序)
					StringBuilder sb = new StringBuilder();
					for (String v : vs) {
						sb.append("'");
						sb.append(v);
						sb.append("',");
					}
					sb.deleteCharAt(sb.length() - 1);
					sqlStr = sqlStr.replaceAll("\\#\\{" + attribute + "\\}", sb.toString());
				}
			} else if (DictionaryCode.DICTIONARY_DATASOURCE_SQL_TYPR_DATE.equals(childValue)) {
				relationExSqlParams.put(attribute, value);
			} else {
				relationExSqlParams.put(attribute, null);
			}
		}

		List<Map<String, Object>> list = new ArrayList<>();
		long count = 0L;
		try {
			Pagination<Map<String, Object>> pagination =
					iCommonService.page(sqlStr, relationExSqlParams, page, pageCount);
			logger.info(LogMsg.to("pagination", pagination));
			list = pagination.getData();
			count = pagination.getTotalPageSize();
		} catch (Throwable e) {
			logger.error(LogMsg.to("ex", e));
		}
		// 返回结果集
		List<List<String>> columns = new ArrayList<>();
		List<List<String>> returns = new ArrayList<>();
		// 列头
		List<String> heads = new ArrayList<>();
		// 模拟数据内容
		// commons.add(BeanUtils.news(WorkCenterProcessDbFieldVo.class, "attribute",
		// "id", "name", "序号"));
		// commons.add(BeanUtils.news(WorkCenterProcessDbFieldVo.class, "attribute",
		// "code", "name", "编号"));
		// commons.add(BeanUtils.news(WorkCenterProcessDbFieldVo.class, "attribute",
		// "title", "name", "标题"));
		for (WorkCenterProcessDbFieldVo common : commons) {
			heads.add(common.getName());
		}
		columns.add(heads);
		// 内容
		// 过滤无用的返回结果
		for (Map<String, Object> map : list) {
			List<String> contents = new ArrayList<>();
			for (WorkCenterProcessDbFieldVo common : commons) {
				String attribute = common.getAttribute();
				Object value = map.get(attribute);
				if (value != null) {
					if (value instanceof String) {
						contents.add(String.valueOf(value));
					} else if (value instanceof Timestamp) {
						contents.add(DateUtils.formatDatetime((Date) value));
					} else if (value instanceof Date) {
						contents.add(DateUtils.formatDatetime((Date) value));
					} else {
						contents.add(String.valueOf(value));
					}
				} else {
					contents.add("");
				}
			}
			returns.add(contents);
		}
		WorkCenterProcessHandleVo processHandleVo = new WorkCenterProcessHandleVo();
		processHandleVo.setDbAutoColumns(columns);
		processHandleVo.setDbAutoReturns(returns);
		processHandleVo.setDbAutoCount(count);
		return processHandleVo;
	}

	@Override
	public WorkCenterProcessHandleVo getCurrNode(WorkCenterProcessHandleForm processHandleForm)
			throws FrameworkRuntimeException {
		String processId = processHandleForm.getProcessId();
		// 查询流程
		WorkCenterProcessBo processBo = new WorkCenterProcessBo();
		processBo.setId(processId);
		processBo.setEnterpriseid(getEnterpriseid());
		WorkCenterProcessVo processVo = iProcessService.get(processBo);
		WorkCenterProcessHandleVo processHandleVo = new WorkCenterProcessHandleVo();
		// 下个节点信息(当前需要处理的节点信息)
		if(processVo != null && !StringUtils.isEmpty(processVo.getNextProcessNodeId())){
			String nextProcessNodeId = processVo.getNextProcessNodeId();
			WorkCenterProcessNodeBo processNodeBo = new WorkCenterProcessNodeBo();
			processNodeBo.setId(nextProcessNodeId);
			processNodeBo.setEnterpriseid(getEnterpriseid());
			WorkCenterProcessNodeVo processNodeVo = iProcessNodeService.get(processNodeBo);
			// 处理结果字典

			DictionaryVo handleDict = processNodeVo.getHandleDict();
			handleDict = iDictionaryService.getByDictionaryId(handleDict.getDictionaryId());
			processNodeVo.setHandleDict(handleDict);
			processHandleVo.setCurrProcessNode(processNodeVo);
		}
		return processHandleVo;
	}
}
