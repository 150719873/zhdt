package com.dotop.smartwater.project.module.api.workcenter.impl;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.dotop.smartwater.project.module.api.workcenter.ITmplFactory;
import com.dotop.smartwater.project.module.service.workcenter.*;
import com.dotop.smartwater.project.module.core.water.bo.*;
import com.dotop.smartwater.project.module.core.water.form.WorkCenterTmplNodeEdgeForm;
import com.dotop.smartwater.project.module.core.water.form.WorkCenterTmplNodePointForm;
import com.dotop.smartwater.project.module.core.water.vo.*;
import com.dotop.smartwater.project.module.service.workcenter.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
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
import com.dotop.smartwater.project.module.core.water.config.Config;
import com.dotop.smartwater.project.module.core.water.constants.DictionaryCode;
import com.dotop.smartwater.project.module.core.water.constants.NumRuleSetCode;
import com.dotop.smartwater.project.module.core.water.constants.WaterConstants;
import com.dotop.smartwater.project.module.core.water.form.WorkCenterTmplForm;
import com.dotop.smartwater.project.module.core.water.form.customize.MakeNumRequest;
import com.dotop.smartwater.project.module.core.water.vo.customize.WorkCenterExportBo;
import com.dotop.smartwater.project.module.core.water.vo.customize.WorkCenterExportVo;
import com.dotop.smartwater.project.module.service.tool.IDictionaryChildService;
import com.dotop.smartwater.project.module.service.tool.IDictionaryService;

@Component("IWorkCenterTmplFactory")
public class TmplFactoryImpl implements ITmplFactory, IAuthCasClient {

	private static final Logger logger = LogManager.getLogger(TmplFactoryImpl.class);

	@Autowired
	private ITmplService iTmplService;

	@Autowired
	private ITmplNodeService iTmplNodeService;

	@Autowired
	private ITmplNodePointService iTmplNodePointService;

	@Autowired
	private ITmplNodeEdgeService iTmplNodeEdgeService;

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
	public Pagination<WorkCenterTmplVo> page(WorkCenterTmplForm tmplForm) {
		WorkCenterTmplBo tmplBo = BeanUtils.copy(tmplForm, WorkCenterTmplBo.class);
		tmplBo.setEnterpriseid(getEnterpriseid());
		Pagination<WorkCenterTmplVo> pagination = iTmplService.page(tmplBo);
		List<WorkCenterTmplVo> tmpls = pagination.getData();
		for (WorkCenterTmplVo tmpl : tmpls) {
			WorkCenterTmplNodeBo tmplNodeBo = new WorkCenterTmplNodeBo();
			tmplNodeBo.setTmplId(tmpl.getId());
			tmplNodeBo.setEnterpriseid(getEnterpriseid());
			List<WorkCenterTmplNodeVo> tmplNodes = iTmplNodeService.list(tmplNodeBo);
			tmpl.setTmplNodes(tmplNodes);
		}
		return pagination;
	}

	@Override
	public Pagination<WorkCenterTmplVo> select(WorkCenterTmplForm tmplForm) {
		// 此处优化可以查询返回请求id和name
		WorkCenterTmplBo tmplBo = BeanUtils.copy(tmplForm, WorkCenterTmplBo.class);
		tmplBo.setIfEffect(WaterConstants.WORK_CENTER_EFFECT);
		tmplBo.setEnterpriseid(getEnterpriseid());
		return iTmplService.page(tmplBo);
	}

	@Override
	public WorkCenterTmplVo get(WorkCenterTmplForm tmplForm) {
		WorkCenterTmplBo tmplBo = new WorkCenterTmplBo();
		tmplBo.setId(tmplForm.getId());
		tmplBo.setEnterpriseid(getEnterpriseid());
		return iTmplService.get(tmplBo);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
	public WorkCenterTmplVo add(WorkCenterTmplForm tmplForm) {
		// 系统生成编号
		String code = iNumRuleSetFactory
				.makeNo(BeanUtils.news(MakeNumRequest.class, "count", 1, "ruleid", NumRuleSetCode.MODEL_NUM_SET))
				.getNumbers().get(0);
		// 新增模板
		WorkCenterTmplBo tmplBo = BeanUtils.copy(tmplForm, WorkCenterTmplBo.class);
		tmplBo.setId(UuidUtils.getUuid());
		tmplBo.setCode(code);
		tmplBo.setIfEffect(WaterConstants.WORK_CENTER_NO_EFFECT);
		tmplBo.setEnterpriseid(getEnterpriseid());
		tmplBo.setUserBy(getName());
		tmplBo.setCurr(getCurr());
		WorkCenterTmplVo tmplVo = iTmplService.add(tmplBo);

		// 初始化处理字典
		DictionaryBo dictionaryBo = new DictionaryBo();
		dictionaryBo.setDictionaryId(
				DictionaryCode.getDictionaryId(WaterConstants.ADMIN_ENTERPRISE_ID, DictionaryCode.JUDGE_CODE));
		// 新增开始模板节点
		WorkCenterTmplNodeBo firstTmplNodeBo = new WorkCenterTmplNodeBo();
		firstTmplNodeBo.setName(WaterConstants.WORK_CENTER_NODE_NAME_FIRST);
		firstTmplNodeBo.setType(WaterConstants.WORK_CENTER_NODE_TYPE_FIRST);
		firstTmplNodeBo.setTmplId(tmplVo.getId());
		firstTmplNodeBo.setHandleDict(dictionaryBo);
		firstTmplNodeBo.setParentId(WaterConstants.WORK_CENTER_NODE_PARENT);
		firstTmplNodeBo.setIfUpdate(WaterConstants.WORK_CENTER_NODE_NO_USE);
		firstTmplNodeBo.setIfNotice(WaterConstants.WORK_CENTER_NODE_NO_USE);
		firstTmplNodeBo.setIfVerify(WaterConstants.WORK_CENTER_NODE_NO_USE);
		firstTmplNodeBo.setIfPhoto(WaterConstants.WORK_CENTER_NODE_NO_USE);
		firstTmplNodeBo.setIfUpload(WaterConstants.WORK_CENTER_NODE_NO_USE);
		firstTmplNodeBo.setIfOpinion(WaterConstants.WORK_CENTER_NODE_NO_USE);
		firstTmplNodeBo.setEnterpriseid(getEnterpriseid());
		firstTmplNodeBo.setUserBy(getName());
		firstTmplNodeBo.setCurr(getCurr());
		WorkCenterTmplNodeVo firstTmplNodeVo = iTmplNodeService.add(firstTmplNodeBo);

		// 新增开始模板节点图形属性
		WorkCenterTmplNodePointBo firstTmplNodePointBo = new WorkCenterTmplNodePointBo();
		firstTmplNodePointBo.setColor(WaterConstants.WORK_CENTER_NODE_POINT_START_COLOR);
		firstTmplNodePointBo.setLabel("开始");
		firstTmplNodePointBo.setShape(WaterConstants.WORK_CENTER_NODE_POINT_SHAPE);
		firstTmplNodePointBo.setSize(WaterConstants.WORK_CENTER_NODE_POINT_SIZE);
		firstTmplNodePointBo.setType(WaterConstants.WORK_CENTER_NODE_POINT_TYPE);
		firstTmplNodePointBo.setX(180);
		firstTmplNodePointBo.setY(150);
		firstTmplNodePointBo.setTmplId(tmplVo.getId());
		firstTmplNodePointBo.setNodeId(firstTmplNodeVo.getId());
		firstTmplNodePointBo.setEnterpriseid(getEnterpriseid());
		iTmplNodePointService.add(firstTmplNodePointBo);

		// 新增结束模板节点
		WorkCenterTmplNodeBo endTmplNodeBo = new WorkCenterTmplNodeBo();
		endTmplNodeBo.setName(WaterConstants.WORK_CENTER_NODE_NAME_END);
		endTmplNodeBo.setType(WaterConstants.WORK_CENTER_NODE_TYPE_END);
		endTmplNodeBo.setTmplId(tmplVo.getId());
		endTmplNodeBo.setHandleDict(dictionaryBo);
		endTmplNodeBo.setParentId(WaterConstants.WORK_CENTER_NODE_PARENT);
		endTmplNodeBo.setIfUpdate(WaterConstants.WORK_CENTER_NODE_NO_USE);
		endTmplNodeBo.setIfNotice(WaterConstants.WORK_CENTER_NODE_NO_USE);
		endTmplNodeBo.setIfVerify(WaterConstants.WORK_CENTER_NODE_NO_USE);
		endTmplNodeBo.setIfPhoto(WaterConstants.WORK_CENTER_NODE_NO_USE);
		endTmplNodeBo.setIfUpload(WaterConstants.WORK_CENTER_NODE_NO_USE);
		endTmplNodeBo.setIfOpinion(WaterConstants.WORK_CENTER_NODE_NO_USE);
		endTmplNodeBo.setEnterpriseid(getEnterpriseid());
		endTmplNodeBo.setUserBy(getName());
		endTmplNodeBo.setCurr(getCurr());
		WorkCenterTmplNodeVo endTmplNodeVo = iTmplNodeService.add(endTmplNodeBo);

		// 新增结束模板节点图形属性
		WorkCenterTmplNodePointBo endTmplNodePointBo = new WorkCenterTmplNodePointBo();
		endTmplNodePointBo.setColor(WaterConstants.WORK_CENTER_NODE_POINT_END_COLOR);
		endTmplNodePointBo.setLabel("结束");
		endTmplNodePointBo.setShape(WaterConstants.WORK_CENTER_NODE_POINT_SHAPE);
		endTmplNodePointBo.setSize(WaterConstants.WORK_CENTER_NODE_POINT_SIZE);
		endTmplNodePointBo.setType(WaterConstants.WORK_CENTER_NODE_POINT_TYPE);
		endTmplNodePointBo.setX(600);
		endTmplNodePointBo.setY(150);
		endTmplNodePointBo.setTmplId(tmplVo.getId());
		endTmplNodePointBo.setNodeId(endTmplNodeVo.getId());
		endTmplNodePointBo.setEnterpriseid(getEnterpriseid());
		iTmplNodePointService.add(endTmplNodePointBo);

		return tmplVo;
	}


	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
	public WorkCenterTmplVo edit(WorkCenterTmplForm tmplForm) {
		// 编辑数据源
		WorkCenterTmplBo tmplBo = BeanUtils.copy(tmplForm, WorkCenterTmplBo.class);
		tmplBo.setEnterpriseid(getEnterpriseid());
		tmplBo.setUserBy(getName());
		tmplBo.setCurr(getCurr());
		iTmplService.edit(tmplBo);
		return null;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
	public String del(WorkCenterTmplForm tmplForm) {
		WorkCenterTmplBo tmplBo = new WorkCenterTmplBo();
		tmplBo.setId(tmplForm.getId());
		tmplBo.setEnterpriseid(getEnterpriseid());
		iTmplService.del(tmplBo);
		// 级联删除
		WorkCenterTmplNodeBo tmplNodeBo = new WorkCenterTmplNodeBo();
		tmplNodeBo.setTmplId(tmplForm.getId());
		tmplNodeBo.setEnterpriseid(getEnterpriseid());
		iTmplNodeService.del(tmplNodeBo);

		WorkCenterTmplNodePointBo tmplNodePointBo = new WorkCenterTmplNodePointBo();
		tmplNodePointBo.setTmplId(tmplForm.getId());
		tmplNodePointBo.setEnterpriseid(getEnterpriseid());
		iTmplNodePointService.del(tmplNodePointBo);

		WorkCenterTmplNodeEdgeBo tmplNodeEdgeBo = new WorkCenterTmplNodeEdgeBo();
		tmplNodeEdgeBo.setTmplId(tmplForm.getId());
		tmplNodeEdgeBo.setEnterpriseid(getEnterpriseid());
		iTmplNodeEdgeService.del(tmplNodeEdgeBo);
		return null;
	}

//	@Override
//	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
//	public void editNodes(WorkCenterTmplForm tmplForm) {
//		// 校验模板是否存在
//		WorkCenterTmplVo tmplVo = iTmplService
//				.get(BeanUtils.news(WorkCenterTmplBo.class, tmplForm.getId(), getEnterpriseid()));
//		if (tmplVo == null) {
//			throw new FrameworkRuntimeException(BaseExceptionConstants.BASE_ERROR, "模板不存在");
//		}
//		// 传递参数转换
//		List<WorkCenterTmplNodeForm> tmplNodeForms = tmplForm.getTmplNodes();
//		// 节点名称唯一校验(节点id由前端维护)
//		Set<String> names = new HashSet<>();
//		for (WorkCenterTmplNodeForm tmplNodeForm : tmplNodeForms) {
//			names.add(tmplNodeForm.getName());
//			if (StringUtils.isBlank(tmplNodeForm.getId())) {
//				throw new FrameworkRuntimeException(BaseExceptionConstants.BASE_ERROR, "模板子节点节点不唯一");
//			}
//		}
//		if (tmplNodeForms.size() != names.size()) {
//			throw new FrameworkRuntimeException(BaseExceptionConstants.BASE_ERROR, "模板子节点节点名称不唯一");
//		}
//		// 传递参数(名字与对象)
//		Map<String, WorkCenterTmplNodeForm> tmplNodeFormMap = tmplNodeForms.stream()
//				.collect(Collectors.toMap(WorkCenterTmplNodeForm::getId, a -> a, (k1, k2) -> k1));
//		// 查询原始数据
//		WorkCenterTmplNodeBo tmplNodeBo = new WorkCenterTmplNodeBo();
//		tmplNodeBo.setTmplId(tmplForm.getId());
//		List<WorkCenterTmplNodeVo> tmplNodeVos = iTmplNodeService.list(tmplNodeBo);
//		logger.info(LogMsg.to("tmplNodeVos", tmplNodeVos));
//		// 查询数据(名字与对象)
//		Map<String, WorkCenterTmplNodeVo> tmplNodeVoMap = tmplNodeVos.stream()
//				.collect(Collectors.toMap(WorkCenterTmplNodeVo::getId, a -> a, (k1, k2) -> k1));
//		// 过滤
//		List<WorkCenterTmplNodeBo> tmplNodeBoAdds = new ArrayList<>();
//		List<WorkCenterTmplNodeBo> tmplNodeBoDels = new ArrayList<>();
//		List<WorkCenterTmplNodeBo> tmplNodeBoEdits = new ArrayList<>();
//		// 从已有数据中查找传递
//		for (String tmplNodeId : tmplNodeVoMap.keySet()) {
//			WorkCenterTmplNodeVo wctnv = tmplNodeVoMap.get(tmplNodeId);
//			WorkCenterTmplNodeForm wctnf = tmplNodeFormMap.get(tmplNodeId);
//			if (wctnf == null) {
//				// 不存在则删除
//				WorkCenterTmplNodeBo wctnb = BeanUtils.copy(wctnv, WorkCenterTmplNodeBo.class);
//				wctnb.setEnterpriseid(getEnterpriseid());
//				wctnb.setUserBy(getName());
//				wctnb.setCurr(getCurr());
//				tmplNodeBoDels.add(wctnb);
//			} else {
//				// 存在则编辑或不变
//				// 属性、名字、类型、内容、关联关系判断是否编辑
//				WorkCenterTmplNodeBo wctnb = BeanUtils.copy(wctnf, WorkCenterTmplNodeBo.class);
//				wctnb.setEnterpriseid(getEnterpriseid());
//				wctnb.setUserBy(getName());
//				wctnb.setCurr(getCurr());
//				tmplNodeBoEdits.add(wctnb);
//			}
//		}
//		// 从传递数据中查找已有数据
//		for (String tmplNodeId : tmplNodeFormMap.keySet()) {
//			WorkCenterTmplNodeForm wctnf = tmplNodeFormMap.get(tmplNodeId);
//			WorkCenterTmplNodeVo wctnv = tmplNodeVoMap.get(tmplNodeId);
//			if (wctnv == null) {
//				// 不存在则新增
//				WorkCenterTmplNodeBo wcdfb = BeanUtils.copy(wctnf, WorkCenterTmplNodeBo.class);
//				wcdfb.setType(WaterConstants.WORK_CENTER_NODE_TYPE_MIDDLE);
//				wcdfb.setTmplId(tmplForm.getId());
//				wcdfb.setEnterpriseid(getEnterpriseid());
//				wcdfb.setUserBy(getName());
//				wcdfb.setCurr(getCurr());
//				tmplNodeBoAdds.add(wcdfb);
//			}
//		}
//		// 整合有用节点,校验是否有效tmplNodeId是否有子类
//		List<WorkCenterTmplNodeBo> tmplNodeBos = new ArrayList<>();
//		tmplNodeBos.addAll(tmplNodeBoAdds);
//		tmplNodeBos.addAll(tmplNodeBoEdits);
//		Map<String, List<WorkCenterTmplNodeBo>> tmplNodeBoMap = tmplNodeBos.stream().collect(Collectors
//				.groupingBy(WorkCenterTmplNodeBo::getType, HashMap::new, Collectors.toCollection(ArrayList::new)));
//		logger.info(LogMsg.to("tmplNodeBoMap", tmplNodeBoMap));
//		// first
//		List<WorkCenterTmplNodeBo> firstTmplNodes = tmplNodeBoMap.get(WaterConstants.WORK_CENTER_NODE_TYPE_FIRST);
//		WorkCenterTmplNodeBo firstTmplNode = firstTmplNodes.get(0);
//		String fvtnId = firstTmplNode.getVerifyTmplNodeId();
//		String fnvtnId = firstTmplNode.getNoVerifyTmplNodeId();
//		// end
//		List<WorkCenterTmplNodeBo> endTmplNodes = tmplNodeBoMap.get(WaterConstants.WORK_CENTER_NODE_TYPE_END);
//		WorkCenterTmplNodeBo endTmplNode = endTmplNodes.get(0);
//		String etnId = endTmplNode.getId();
//		// middle
//		List<WorkCenterTmplNodeBo> middleTmplNodes = tmplNodeBoMap.get(WaterConstants.WORK_CENTER_NODE_TYPE_MIDDLE);
//		Map<String, WorkCenterTmplNodeBo> middleTmplNodeMap = new HashMap<>();
//		if (middleTmplNodes != null) {
//			middleTmplNodeMap = middleTmplNodes.stream()
//					.collect(Collectors.toMap(WorkCenterTmplNodeBo::getId, a -> a, (k1, k2) -> k1));
//		}
//		// 校验模板节点是否归于结束
//		boolean verify = verifyTmplNodes(fvtnId, fnvtnId, etnId, middleTmplNodeMap);
//		if (!verify) {
//			throw new FrameworkRuntimeException(BaseExceptionConstants.BASE_ERROR, "模板子节点没有结束");
//		}
//		// 校验模板节点是否有处理结果字典
//		verify = verifyTmplNodes(firstTmplNode, endTmplNode, middleTmplNodeMap);
//		if (!verify) {
//			throw new FrameworkRuntimeException(BaseExceptionConstants.BASE_ERROR, "模板子节点没有配置处理结果");
//		}
//		// 过滤新增
//		iTmplNodeService.adds(tmplNodeBoAdds);
//		// 过滤编辑
//		iTmplNodeService.edits(tmplNodeBoEdits);
//		// 过滤删除
//		iTmplNodeService.dels(tmplNodeBoDels);
//
//		// 修改模板
//		String ifEffect = tmplVo.getIfEffect();
//		if (!WaterConstants.WORK_CENTER_EFFECT.equals(ifEffect)) {
//			// 修改为有效模板
//			WorkCenterTmplBo tmplBo = new WorkCenterTmplBo();
//			tmplBo.setId(tmplForm.getId());
//			tmplBo.setIfEffect(WaterConstants.WORK_CENTER_EFFECT);
//			tmplBo.setEnterpriseid(getEnterpriseid());
//			tmplBo.setUserBy(getName());
//			tmplBo.setCurr(getCurr());
//			iTmplService.edit(tmplBo);
//		}
//	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
	public void editNodes(WorkCenterTmplForm tmplForm) {
		// 校验模板是否存在
		WorkCenterTmplVo tmplVo = iTmplService
				.get(BeanUtils.news(WorkCenterTmplBo.class, tmplForm.getId(), getEnterpriseid()));
		if (tmplVo == null) {
			throw new FrameworkRuntimeException(BaseExceptionConstants.BASE_ERROR, "模板不存在");
		}
		// 传递参数转换
		List<WorkCenterTmplNodePointForm> tmplNodeForms = tmplForm.getNodes();
		// 节点名称唯一校验(节点id由前端维护)
		Set<String> names = new HashSet<>();
		for (WorkCenterTmplNodePointForm tmplNodeForm : tmplNodeForms) {
			names.add(tmplNodeForm.getLabel());
			if (StringUtils.isBlank(tmplNodeForm.getId())) {
				throw new FrameworkRuntimeException(BaseExceptionConstants.BASE_ERROR, "模板子节点不唯一");
			}
		}
		if (tmplNodeForms.size() != names.size()) {
			throw new FrameworkRuntimeException(BaseExceptionConstants.BASE_ERROR, "模板子节点名称不唯一");
		}
		// 传递参数(名字与对象)
		Map<String, WorkCenterTmplNodePointForm> tmplNodeFormMap = tmplNodeForms.stream()
				.collect(Collectors.toMap(WorkCenterTmplNodePointForm::getId, a -> a, (k1, k2) -> k1));
		// 查询原始数据
		WorkCenterTmplNodePointBo tmplNodeBo = new WorkCenterTmplNodePointBo();
		tmplNodeBo.setTmplId(tmplForm.getId());
		List<WorkCenterTmplNodePointVo> tmplNodeVos = iTmplNodePointService.list(tmplNodeBo);
		logger.info(LogMsg.to("tmplNodeVos", tmplNodeVos));
		// 查询数据(名字与对象)
		Map<String, WorkCenterTmplNodePointVo> tmplNodeVoMap = tmplNodeVos.stream()
				.collect(Collectors.toMap(WorkCenterTmplNodePointVo::getId, a -> a, (k1, k2) -> k1));
		// 过滤
		List<WorkCenterTmplNodeBo> tmplNodeBoAdds = new ArrayList<>();
		List<WorkCenterTmplNodeBo> tmplNodeBoDels = new ArrayList<>();
		List<WorkCenterTmplNodeBo> tmplNodeBoEdits = new ArrayList<>();

		List<WorkCenterTmplNodePointBo> tmplNodePointBoAdds = new ArrayList<>();
		List<WorkCenterTmplNodePointBo> tmplNodePointBoDels = new ArrayList<>();
		List<WorkCenterTmplNodePointBo> tmplNodePointBoEdits = new ArrayList<>();

		// 从已有数据中查找传递
		for (String tmplNodeId : tmplNodeVoMap.keySet()) {
			WorkCenterTmplNodePointVo wctnv = tmplNodeVoMap.get(tmplNodeId);
			WorkCenterTmplNodePointForm wctnf = tmplNodeFormMap.get(tmplNodeId);
			if (wctnf == null) {
				WorkCenterTmplNodePointBo wctnpb = BeanUtils.copy(wctnv, WorkCenterTmplNodePointBo.class);
				wctnpb.setEnterpriseid(getEnterpriseid());
				wctnpb.setTmplId(tmplForm.getId());
				tmplNodePointBoDels.add(wctnpb);

				// 不存在则删除
				WorkCenterTmplNodeBo wctnb = BeanUtils.copy(wctnv.getParams(), WorkCenterTmplNodeBo.class);
				wctnb.setEnterpriseid(getEnterpriseid());
				wctnb.setUserBy(getName());
				wctnb.setCurr(getCurr());
				tmplNodeBoDels.add(wctnb);

			} else {
				WorkCenterTmplNodePointBo wctnpb = BeanUtils.copy(wctnf, WorkCenterTmplNodePointBo.class);
				wctnpb.setEnterpriseid(getEnterpriseid());
				wctnpb.setNodeId(wctnf.getParams().getId());
				wctnpb.setTmplId(wctnv.getTmplId());
				tmplNodePointBoEdits.add(wctnpb);
				// 存在则编辑或不变
				// 属性、名字、类型、内容、关联关系判断是否编辑
				WorkCenterTmplNodeBo wctnb = BeanUtils.copy(wctnf.getParams(), WorkCenterTmplNodeBo.class);
				wctnb.setTmplId(wctnv.getTmplId());
				wctnb.setEnterpriseid(getEnterpriseid());
				wctnb.setUserBy(getName());
				wctnb.setCurr(getCurr());
				tmplNodeBoEdits.add(wctnb);
			}
		}
		// 从传递数据中查找已有数据
		for (String tmplNodeId : tmplNodeFormMap.keySet()) {
			WorkCenterTmplNodePointForm wctnf = tmplNodeFormMap.get(tmplNodeId);
			WorkCenterTmplNodePointVo wctnv = tmplNodeVoMap.get(tmplNodeId);
			if (wctnv == null) {
				WorkCenterTmplNodePointBo wctnpb = BeanUtils.copy(wctnf, WorkCenterTmplNodePointBo.class);
				wctnpb.setTmplId(tmplForm.getId());
				wctnpb.setNodeId(wctnf.getParams().getId());
				wctnpb.setEnterpriseid(getEnterpriseid());
				tmplNodePointBoAdds.add(wctnpb);

				// 不存在则新增
				WorkCenterTmplNodeBo wcdfb = BeanUtils.copy(wctnf.getParams(), WorkCenterTmplNodeBo.class);
				wcdfb.setType(WaterConstants.WORK_CENTER_NODE_TYPE_MIDDLE);
				wcdfb.setTmplId(tmplForm.getId());
				wcdfb.setEnterpriseid(getEnterpriseid());
				wcdfb.setUserBy(getName());
				wcdfb.setCurr(getCurr());
				tmplNodeBoAdds.add(wcdfb);
			}
		}
		// 整合有用节点,校验是否有效tmplNodeId是否有子类
		List<WorkCenterTmplNodeBo> tmplNodeBos = new ArrayList<>();
		tmplNodeBos.addAll(tmplNodeBoAdds);
		tmplNodeBos.addAll(tmplNodeBoEdits);
		Map<String, List<WorkCenterTmplNodeBo>> tmplNodeBoMap = tmplNodeBos.stream().collect(Collectors
				.groupingBy(WorkCenterTmplNodeBo::getType, HashMap::new, Collectors.toCollection(ArrayList::new)));
		logger.info(LogMsg.to("tmplNodeBoMap", tmplNodeBoMap));
		// first
		List<WorkCenterTmplNodeBo> firstTmplNodes = tmplNodeBoMap.get(WaterConstants.WORK_CENTER_NODE_TYPE_FIRST);
		WorkCenterTmplNodeBo firstTmplNode = firstTmplNodes.get(0);
		String fvtnId = firstTmplNode.getVerifyTmplNodeId();
		String fnvtnId = firstTmplNode.getNoVerifyTmplNodeId();
		// end
		List<WorkCenterTmplNodeBo> endTmplNodes = tmplNodeBoMap.get(WaterConstants.WORK_CENTER_NODE_TYPE_END);
		WorkCenterTmplNodeBo endTmplNode = endTmplNodes.get(0);
		String etnId = endTmplNode.getId();
		// middle
		List<WorkCenterTmplNodeBo> middleTmplNodes = tmplNodeBoMap.get(WaterConstants.WORK_CENTER_NODE_TYPE_MIDDLE);
		Map<String, WorkCenterTmplNodeBo> middleTmplNodeMap = new HashMap<>();
		if (middleTmplNodes != null) {
			middleTmplNodeMap = middleTmplNodes.stream()
					.collect(Collectors.toMap(WorkCenterTmplNodeBo::getId, a -> a, (k1, k2) -> k1));
		}
		// 校验模板节点是否归于结束
		boolean verify = verifyTmplNodes(fvtnId, fnvtnId, etnId, middleTmplNodeMap);
		if (!verify) {
			throw new FrameworkRuntimeException(BaseExceptionConstants.BASE_ERROR, "模板子节点没有结束");
		}
		// 校验模板节点是否有处理结果字典
//		verify = verifyTmplNodes(firstTmplNode, endTmplNode, middleTmplNodeMap);
//		if (!verify) {
//			throw new FrameworkRuntimeException(BaseExceptionConstants.BASE_ERROR, "模板子节点没有配置处理结果");
//		}
		// 过滤新增
		iTmplNodeService.adds(tmplNodeBoAdds);
		// 过滤编辑
		iTmplNodeService.edits(tmplNodeBoEdits);
		// 过滤删除
		iTmplNodeService.dels(tmplNodeBoDels);

		iTmplNodePointService.adds(tmplNodePointBoAdds);
		iTmplNodePointService.edits(tmplNodePointBoEdits);
		iTmplNodePointService.dels(tmplNodePointBoDels);

		WorkCenterTmplNodeEdgeBo tmplNodeEdgeBo = new WorkCenterTmplNodeEdgeBo();
		tmplNodeEdgeBo.setTmplId(tmplForm.getId());
		tmplNodeEdgeBo.setEnterpriseid(getEnterpriseid());
		iTmplNodeEdgeService.del(tmplNodeEdgeBo);

		if(!CollectionUtils.isEmpty(tmplForm.getEdges())){
			List<WorkCenterTmplNodeEdgeBo> tmplNodeEdgeBos = new ArrayList<>();
			for(WorkCenterTmplNodeEdgeForm tmplNodeEdgeForm:tmplForm.getEdges()){
				WorkCenterTmplNodeEdgeBo edgeBo = BeanUtils.copy(tmplNodeEdgeForm,WorkCenterTmplNodeEdgeBo.class);
				edgeBo.setTmplId(tmplForm.getId());
				edgeBo.setEnterpriseid(getEnterpriseid());
				tmplNodeEdgeBos.add(edgeBo);
			}
			iTmplNodeEdgeService.adds(tmplNodeEdgeBos);
		}

		// 修改模板
		String ifEffect = tmplVo.getIfEffect();
		if (!WaterConstants.WORK_CENTER_EFFECT.equals(ifEffect)) {
			// 修改为有效模板
			WorkCenterTmplBo tmplBo = new WorkCenterTmplBo();
			tmplBo.setId(tmplForm.getId());
			tmplBo.setIfEffect(WaterConstants.WORK_CENTER_EFFECT);
			tmplBo.setEnterpriseid(getEnterpriseid());
			tmplBo.setUserBy(getName());
			tmplBo.setCurr(getCurr());
			iTmplService.edit(tmplBo);
		}
	}

	private boolean verifyTmplNodes(String vtnId, String nvtnId, String etnId,
	                                Map<String, WorkCenterTmplNodeBo> middleTmplNodeMap) {
		// 判断验证子节点是否与结束节点关联
		if (StringUtils.isBlank(vtnId) && StringUtils.isBlank(nvtnId)) {
			// 开始节点没有结束
			// throw new FrameworkRuntimeException(BaseExceptionConstants.BASE_ERROR,
			// "子节点没有结束");
			return false;
		}
		WorkCenterTmplNodeBo tn = null;
		boolean vf = false;
		boolean nvf = false;
		// 判断中间节点是否月结束节点关联
		if (StringUtils.isBlank(vtnId) || vtnId.equals(etnId)) {
			// 验证节点有结束
			return true;
		} else {
			// 判断开始节点是否与中间节点关联
			// 判断中间节点是否与中间节点关联
			tn = middleTmplNodeMap.get(vtnId);
			if (tn == null) {
				return false;
			}
			vf = verifyTmplNodes(tn.getVerifyTmplNodeId(), tn.getNoVerifyTmplNodeId(), etnId, middleTmplNodeMap);
		}
		// 判断中间节点是否月结束节点关联
		if (StringUtils.isBlank(nvtnId) || nvtnId.equals(etnId)) {
			// 不验证节点有结束
			return true;
		} else {
			// 判断开始节点是否与中间节点关联
			// 判断中间节点是否与中间节点关联
			tn = middleTmplNodeMap.get(nvtnId);
			if (tn == null) {
				return false;
			}
			nvf = verifyTmplNodes(tn.getVerifyTmplNodeId(), tn.getNoVerifyTmplNodeId(), etnId, middleTmplNodeMap);
		}
		return vf && nvf;
	}

	private boolean verifyTmplNodes(WorkCenterTmplNodeBo firstTmplNode, WorkCenterTmplNodeBo endTmplNode,
	                                Map<String, WorkCenterTmplNodeBo> middleTmplNodeMap) {
		if (firstTmplNode.getHandleDict() == null) {
			return false;
		}
		if (endTmplNode.getHandleDict() == null) {
			return false;
		}
		for (String key : middleTmplNodeMap.keySet()) {
			if (middleTmplNodeMap.get(key).getHandleDict() == null) {
				return false;
			}
		}
		return true;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
	public WorkCenterTmplVo copy(WorkCenterTmplForm tmplForm) {
		// 校验模板是否存在
		WorkCenterTmplVo tmplVo = iTmplService.get(BeanUtils.news(WorkCenterTmplBo.class, tmplForm.getId()));
		if (tmplVo == null) {
			throw new FrameworkRuntimeException(BaseExceptionConstants.BASE_ERROR, "模板不存在");
		}
		String ifEffect = tmplVo.getIfEffect();
		if (!WaterConstants.WORK_CENTER_EFFECT.equals(ifEffect)) {
			throw new FrameworkRuntimeException(BaseExceptionConstants.BASE_ERROR, "模板校验无效,请重新编辑模板后再试");
		}
		// 参数准备
		String tmplId = UuidUtils.getUuid();
		String formId = UuidUtils.getUuid();
		WorkCenterFormVo formVo = tmplVo.getForm();
		List<WorkCenterDbVo> dbVos = formVo.getDbs();
		String formCode = iNumRuleSetFactory
				.makeNo(BeanUtils.news(MakeNumRequest.class, "count", 1, "ruleid", NumRuleSetCode.FORM_NUM_SET))
				.getNumbers().get(0);
		List<String> dbCodes = iNumRuleSetFactory.makeNo(
				BeanUtils.news(MakeNumRequest.class, "count", dbVos.size(), "ruleid", NumRuleSetCode.SOURCE_NUM_SET))
				.getNumbers();
		String tmplCode = iNumRuleSetFactory
				.makeNo(BeanUtils.news(MakeNumRequest.class, "count", 1, "ruleid", NumRuleSetCode.MODEL_NUM_SET))
				.getNumbers().get(0);
		// 表单复制
		WorkCenterFormBo formBo = BeanUtils.copy(formVo, WorkCenterFormBo.class);
		formBo.setId(formId);
		formBo.setCode(formCode);
		formBo.setIfEffect(WaterConstants.WORK_CENTER_NO_EFFECT);// 设置为无效模板
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
			dbBo.setIfEffect(WaterConstants.WORK_CENTER_NO_EFFECT);// 设置为无效模板
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
		// 复制模板
		WorkCenterTmplBo tmplBo = BeanUtils.copy(tmplVo, WorkCenterTmplBo.class);
		tmplBo.setId(tmplId);
		tmplBo.setForm(formBo);
		tmplBo.setIfEffect(WaterConstants.WORK_CENTER_NO_EFFECT);// 设置为无效模板
		tmplBo.setCode(tmplCode);
		tmplBo.setEnterpriseid(getEnterpriseid());
		tmplBo.setUserBy(getName());
		tmplBo.setCurr(getCurr());
		iTmplService.add(tmplBo);
		// 复制模板节点
		List<WorkCenterTmplNodeBo> tmplNodeAdds = new ArrayList<>();
		List<WorkCenterTmplNodePointBo> tmplNodePointAdds = new ArrayList<>();
		WorkCenterTmplNodePointBo tmplNodePointBoParm = new WorkCenterTmplNodePointBo();
		tmplNodePointBoParm.setTmplId(tmplForm.getId());
		List<WorkCenterTmplNodePointVo> tmplNodePointVos = iTmplNodePointService.list(tmplNodePointBoParm);

		// 查找边
		WorkCenterTmplNodeEdgeBo tmplNodeEdgeBoParm = new WorkCenterTmplNodeEdgeBo();
		tmplNodeEdgeBoParm.setTmplId(tmplForm.getId());
		List<WorkCenterTmplNodeEdgeVo> tmplNodeEdgeVos = iTmplNodeEdgeService.list(tmplNodeEdgeBoParm);
		List<WorkCenterTmplNodeEdgeBo> tmplNodeEdgeAdds = new ArrayList<>();
		for (WorkCenterTmplNodePointVo tmplNodePointVo : tmplNodePointVos) {
			WorkCenterTmplNodeVo tmplNodeVo = tmplNodePointVo.getParams();
			WorkCenterTmplNodeBo tmplNodeBo = BeanUtils.copy(tmplNodeVo, WorkCenterTmplNodeBo.class);
			String tmplNodeId = MD5Util.encode(tmplId + tmplNodeVo.getId());
			tmplNodeBo.setId(tmplNodeId);
			tmplNodeBo.setTmplId(tmplId);
			if (StringUtils.isBlank(tmplNodeVo.getVerifyTmplNodeId())) {
				tmplNodeBo.setVerifyTmplNodeId(null);
			} else {
				tmplNodeBo.setVerifyTmplNodeId(MD5Util.encode(tmplId + tmplNodeVo.getVerifyTmplNodeId()));
			}
			if (StringUtils.isBlank(tmplNodeVo.getNoVerifyTmplNodeId())) {
				tmplNodeBo.setNoVerifyTmplNodeId(null);
			} else {
				tmplNodeBo.setNoVerifyTmplNodeId(MD5Util.encode(tmplId + tmplNodeVo.getNoVerifyTmplNodeId()));
			}
			tmplNodeBo.setEnterpriseid(getEnterpriseid());
			tmplNodeBo.setUserBy(getName());
			tmplNodeBo.setCurr(getCurr());
			tmplNodeAdds.add(tmplNodeBo);
			// 过滤置换字典类别
			DictionaryBo handleDict = tmplNodeBo.getHandleDict();
			if (handleDict != null && !StringUtils.isEmpty(handleDict.getDictionaryId())) {
				dictionarySet.add(handleDict.getDictionaryId());
				// 置换字典类别
				DictionaryBo copyHandleDict = new DictionaryBo();
				copyHandleDict.setDictionaryId(
						DictionaryCode.copyDictionaryId(handleDict.getDictionaryId(), getEnterpriseid()));
				tmplNodeBo.setHandleDict(copyHandleDict);
			}

			WorkCenterTmplNodePointBo tmplNodePointBo = BeanUtils.copy(tmplNodePointVo, WorkCenterTmplNodePointBo.class);
			String pointId = UuidUtils.getUuid();
			tmplNodePointBo.setId(pointId);
			tmplNodePointBo.setTmplId(tmplId);
			tmplNodePointBo.setNodeId(tmplNodeId);
			tmplNodePointBo.setEnterpriseid(getEnterpriseid());
			tmplNodePointAdds.add(tmplNodePointBo);
			//复制边
			for(WorkCenterTmplNodeEdgeVo edgeVo:tmplNodeEdgeVos){
				if(tmplNodePointVo.getId().equals(edgeVo.getSource())){
					edgeVo.setSource(pointId);
				}
				if(tmplNodePointVo.getId().equals(edgeVo.getTarget())){
					edgeVo.setTarget(pointId);
				}
				edgeVo.setTmplId(tmplId);
				edgeVo.setEnterpriseid(getEnterpriseid());
				edgeVo.setId(UuidUtils.getUuid());
			}

		}
		for(WorkCenterTmplNodeEdgeVo vo:tmplNodeEdgeVos){
			WorkCenterTmplNodeEdgeBo tmplNodeEdgeBo = BeanUtils.copy(vo,WorkCenterTmplNodeEdgeBo.class);
			tmplNodeEdgeAdds.add(tmplNodeEdgeBo);
		}
		iTmplNodeService.adds(tmplNodeAdds);
		iTmplNodePointService.adds(tmplNodePointAdds);
		iTmplNodeEdgeService.adds(tmplNodeEdgeAdds);
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
		return BeanUtils.news(WorkCenterTmplVo.class, tmplId);
	}

	@Override
	public String export(WorkCenterTmplForm tmplForm) {
		// 校验数据源是否存在
		WorkCenterTmplVo tmplVo = iTmplService
				.get(BeanUtils.news(WorkCenterTmplBo.class, tmplForm.getId(), getEnterpriseid()));
		if (tmplVo == null) {
			throw new FrameworkRuntimeException(BaseExceptionConstants.BASE_ERROR, "模板不存在");
		}
		String ifEffect = tmplVo.getIfEffect();
		if (!WaterConstants.WORK_CENTER_EFFECT.equals(ifEffect)) {
			throw new FrameworkRuntimeException(BaseExceptionConstants.BASE_ERROR, "模板校验无效,请重新编辑模板后再试");
		}
		// 过滤字典
		Set<String> dictionarySet = new HashSet<>();
		Set<String> dictionaryChildSet = new HashSet<>();
		// 模板节点
		List<WorkCenterTmplNodePointVo> tmplNodePointVos = tmplVo.getNodes();
		for(WorkCenterTmplNodePointVo tmplNodePointVo:tmplNodePointVos){
			WorkCenterTmplNodeVo tmplNodeVo = tmplNodePointVo.getParams();
			if(tmplNodeVo != null){
				if(tmplNodeVo.getHandleDict() != null && StringUtils.isNoneBlank(tmplNodeVo.getHandleDict().getDictionaryId())){
					dictionarySet.add(tmplNodeVo.getHandleDict().getDictionaryId());
				}
				if(tmplNodeVo.getUpdateDictChild() != null && StringUtils.isNoneBlank(tmplNodeVo.getUpdateDictChild().getChildId())){
					dictionaryChildSet.add(tmplNodeVo.getUpdateDictChild().getChildId());
				}
			}
		}

		// 表单
		WorkCenterFormVo formVo = tmplVo.getForm();
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
		exportVo.setTmpl(tmplVo);
		exportVo.setDictionarys(dictionaryVos);
		exportVo.setExportDate(getCurr());
		exportVo.setVersion(Config.VERSION);
		exportVo.setSign(MD5Util.encode(Base64Utils.encoder(getEnterpriseid() + Config.VERSION)));
		return JSONUtils.toJSONString(exportVo);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
	public void imports(MultipartFile file) {
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
		WorkCenterTmplBo tmplBo = exportBo.getTmpl();
		List<WorkCenterTmplNodePointBo> tmplNodePointBos = tmplBo.getNodes();
		List<WorkCenterTmplNodeEdgeBo> tmplNodeEdgeBos = tmplBo.getEdges();
		WorkCenterFormBo formBo = tmplBo.getForm();
		List<WorkCenterDbBo> dbBos = formBo.getDbs();
		List<DictionaryBo> dictionaryBos = exportBo.getDictionarys();
		Date exportDate = exportBo.getExportDate();
		String sign = exportBo.getSign();
		VerificationUtils.string("enterpriseid", enterpriseid);
		VerificationUtils.obj("tmplBo", tmplBo);
		VerificationUtils.objList("tmplNodePointBos", tmplNodePointBos);
		VerificationUtils.obj("formBo", formBo);
//		VerificationUtils.objList("dbBos", dbBos);
		VerificationUtils.obj("dictionaryBos", dictionaryBos);
		VerificationUtils.date("exportDate", exportDate);
		VerificationUtils.string("sign", sign);
		// 上传校验
		String encode = MD5Util.encode(Base64Utils.encoder(enterpriseid + Config.VERSION));
		if (!sign.equals(encode)) {
			throw new FrameworkRuntimeException(BaseExceptionConstants.ILLEGAL_EXCEPTION, "文件校验有误");
		}
		if (WaterConstants.ADMIN_ENTERPRISE_ID.equals(enterpriseid)) {
			// 导出模板为运维
			if (enterpriseid.equals(getEnterpriseid())) {
				// 运维->运维 不允许
				throw new FrameworkRuntimeException(BaseExceptionConstants.ILLEGAL_EXCEPTION, "运维平台不允许导入模板");
			} else {
				// 运维->水司 允许


			}
		} else {
			// 导出模板为水司
			if (WaterConstants.ADMIN_ENTERPRISE_ID.equals(getEnterpriseid())) {
				// 水司->运维 不允许
				throw new FrameworkRuntimeException(BaseExceptionConstants.ILLEGAL_EXCEPTION, "运维平台不允许导入模板！");
			} else if (enterpriseid.equals(getEnterpriseid())) {
				// 水司-> 同一水司 允许

			} else {
				// 水司-> 不同一水司 不允许
				throw new FrameworkRuntimeException(BaseExceptionConstants.ILLEGAL_EXCEPTION, "运维平台不允许导入模板！！");
			}
		}
		// 私有字典复制
		iDictionaryFactory.copy(dictionaryBos, getEnterpriseid());
		// 导入准备
		String tmplId = UuidUtils.getUuid();
		String formId = UuidUtils.getUuid();
		String tmplCode = iNumRuleSetFactory
				.makeNo(BeanUtils.news(MakeNumRequest.class, "count", 1, "ruleid", NumRuleSetCode.MODEL_NUM_SET))
				.getNumbers().get(0);
		String formCode = iNumRuleSetFactory
				.makeNo(BeanUtils.news(MakeNumRequest.class, "count", 1, "ruleid", NumRuleSetCode.FORM_NUM_SET))
				.getNumbers().get(0);

		// 表单导入
		formBo.setId(formId);
		formBo.setCode(formCode);
		formBo.setIfEffect(WaterConstants.WORK_CENTER_NO_EFFECT);// 设置为无效模板
		formBo.setEnterpriseid(getEnterpriseid());
		formBo.setUserBy(getName());
		formBo.setCurr(getCurr());
		iFormService.add(formBo);

		if(!CollectionUtils.isEmpty(dbBos)){
			// 数据源和 数据源字段导入
			List<String> dbCodes = iNumRuleSetFactory.makeNo(
					BeanUtils.news(MakeNumRequest.class, "count", dbBos.size(), "ruleid", NumRuleSetCode.SOURCE_NUM_SET))
					.getNumbers();
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
				dbBo.setIfEffect(WaterConstants.WORK_CENTER_NO_EFFECT);// 设置为无效模板
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
		}

		// 模板导入
		tmplBo.setId(tmplId);
		tmplBo.setCode(tmplCode);
		tmplBo.setForm(formBo);
		tmplBo.setIfEffect(WaterConstants.WORK_CENTER_NO_EFFECT);// 设置为无效模板
		tmplBo.setEnterpriseid(getEnterpriseid());
		tmplBo.setUserBy(getName());
		tmplBo.setCurr(getCurr());
		iTmplService.add(tmplBo);
		// 模板节点导入

		List<WorkCenterTmplNodeBo> tmplNodeAdds = new ArrayList<>();
		List<WorkCenterTmplNodePointBo> tmplNodePointAdds = new ArrayList<>();

		for (WorkCenterTmplNodePointBo tmplNodePointBo : tmplNodePointBos) {
			WorkCenterTmplNodeBo tmplNodeBo = tmplNodePointBo.getParams();
			WorkCenterTmplNodeBo cpTmplNodeBo = BeanUtils.copy(tmplNodePointBo.getParams(), WorkCenterTmplNodeBo.class);
			String tmplNodeId = MD5Util.encode(tmplId + tmplNodeBo.getId());
			cpTmplNodeBo.setId(tmplNodeId);
			cpTmplNodeBo.setTmplId(tmplId);
			if (StringUtils.isBlank(tmplNodeBo.getVerifyTmplNodeId())) {
				cpTmplNodeBo.setVerifyTmplNodeId(null);
			} else {
				cpTmplNodeBo.setVerifyTmplNodeId(MD5Util.encode(tmplId + tmplNodeBo.getVerifyTmplNodeId()));
			}
			if (StringUtils.isBlank(tmplNodeBo.getNoVerifyTmplNodeId())) {
				cpTmplNodeBo.setNoVerifyTmplNodeId(null);
			} else {
				cpTmplNodeBo.setNoVerifyTmplNodeId(MD5Util.encode(tmplId + tmplNodeBo.getNoVerifyTmplNodeId()));
			}
			cpTmplNodeBo.setEnterpriseid(getEnterpriseid());
			cpTmplNodeBo.setUserBy(getName());
			cpTmplNodeBo.setCurr(getCurr());
			tmplNodeAdds.add(cpTmplNodeBo);
			// 过滤置换字典类别
			DictionaryBo handleDict = tmplNodeBo.getHandleDict();
			if (handleDict != null && !StringUtils.isEmpty(handleDict.getDictionaryId())) {
				// 置换字典类别
				DictionaryBo copyHandleDict = new DictionaryBo();
				copyHandleDict.setDictionaryId(
						DictionaryCode.copyDictionaryId(handleDict.getDictionaryId(), getEnterpriseid()));
				cpTmplNodeBo.setHandleDict(copyHandleDict);
			}

			WorkCenterTmplNodePointBo cpTmplNodePointBo = BeanUtils.copy(tmplNodePointBo, WorkCenterTmplNodePointBo.class);
			String pointId = UuidUtils.getUuid();
			cpTmplNodePointBo.setId(pointId);
			cpTmplNodePointBo.setTmplId(tmplId);
			cpTmplNodePointBo.setNodeId(tmplNodeId);
			cpTmplNodePointBo.setEnterpriseid(getEnterpriseid());
			tmplNodePointAdds.add(cpTmplNodePointBo);
			//复制边
			for(WorkCenterTmplNodeEdgeBo edgeBo:tmplNodeEdgeBos){
				if(tmplNodePointBo.getId().equals(edgeBo.getSource())){
					edgeBo.setSource(pointId);
				}
				if(tmplNodePointBo.getId().equals(edgeBo.getTarget())){
					edgeBo.setTarget(pointId);
				}
				edgeBo.setTmplId(tmplId);
				edgeBo.setId(UuidUtils.getUuid());
			}

		}
		iTmplNodeService.adds(tmplNodeAdds);
		iTmplNodePointService.adds(tmplNodePointAdds);
		iTmplNodeEdgeService.adds(tmplNodeEdgeBos);

	}

	@Override
	public Pagination<WorkCenterTmplVo> pageByAdmin(WorkCenterTmplForm tmplForm) {
		WorkCenterTmplBo tmplBo = BeanUtils.copy(tmplForm, WorkCenterTmplBo.class);
		tmplBo.setEnterpriseid(WaterConstants.ADMIN_ENTERPRISE_ID);
		return iTmplService.page(tmplBo);
	}

}
