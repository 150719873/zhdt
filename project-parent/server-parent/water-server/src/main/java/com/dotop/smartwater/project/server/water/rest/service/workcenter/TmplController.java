package com.dotop.smartwater.project.server.water.rest.service.workcenter;

import java.io.BufferedOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import com.dotop.smartwater.project.module.core.water.form.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.dotop.smartwater.dependence.core.common.BaseController;
import com.dotop.smartwater.dependence.core.exception.BaseExceptionConstants;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.global.GlobalContext;
import com.dotop.smartwater.dependence.core.log.LogMsg;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.dependence.core.utils.DateUtils;
import com.dotop.smartwater.dependence.core.utils.VerificationUtils;
import com.dotop.smartwater.project.module.api.workcenter.ITmplFactory;
import com.dotop.smartwater.project.module.core.water.constants.ResultCode;
import com.dotop.smartwater.project.module.core.water.constants.WaterConstants;
import com.dotop.smartwater.project.module.core.water.vo.WorkCenterTmplVo;

/**
 * 工作中心模板管理
 *

 * @date 2019年4月17日
 * @description
 */
@RestController("WorkCenterTmplController")
@RequestMapping("/workcenter/tmpl")

public class TmplController implements BaseController<WorkCenterTmplForm> {

	private static final Logger logger = LogManager.getLogger(TmplController.class);

	@Autowired
	private ITmplFactory iTmplFactory;

	/**
	 * 查询分页
	 */
	@Override
	@PostMapping(value = "/page", produces = GlobalContext.PRODUCES)
	public String page(@RequestBody WorkCenterTmplForm tmplForm) {
		String name = tmplForm.getName();
		String code = tmplForm.getCode();
		Integer page = tmplForm.getPage();
		Integer pageCount = tmplForm.getPageCount();
		// 验证
		VerificationUtils.string("name", name, true);
		VerificationUtils.string("code", code, true);
		VerificationUtils.integer("page", page);
		VerificationUtils.integer("pageCount", pageCount);
		Pagination<WorkCenterTmplVo> pagination = iTmplFactory.page(tmplForm);
		return resp(ResultCode.Success, ResultCode.SUCCESS, pagination);
	}

	/**
	 * 查询模板选择下拉
	 */
	@PostMapping(value = "/select", produces = GlobalContext.PRODUCES)
	public String select(@RequestBody WorkCenterTmplForm tmplForm) {
		Integer page = tmplForm.getPage();
		Integer pageCount = tmplForm.getPageCount();
		// 验证
		VerificationUtils.integer("page", page);
		VerificationUtils.integer("pageCount", pageCount);
		Pagination<WorkCenterTmplVo> pagination = iTmplFactory.select(tmplForm);
		return resp(ResultCode.Success, ResultCode.SUCCESS, pagination);
	}

	/**
	 * 查询详情
	 */
	@PostMapping(value = "/get", produces = GlobalContext.PRODUCES)
	public String get(@RequestBody WorkCenterTmplForm tmplForm) {
		String id = tmplForm.getId();
		// 验证
		VerificationUtils.string("id", id);
		WorkCenterTmplVo get = iTmplFactory.get(tmplForm);
		return resp(ResultCode.Success, ResultCode.SUCCESS, get);
	}

	/**
	 * 新增
	 */
	@PostMapping(value = "/add", produces = GlobalContext.PRODUCES)
	public String add(@RequestBody WorkCenterTmplForm tmplForm) {
		WorkCenterFormForm form = tmplForm.getForm();
		String name = tmplForm.getName();
		// 验证
		VerificationUtils.obj("form", form);
		VerificationUtils.string("name", name);
		WorkCenterTmplVo add = iTmplFactory.add(tmplForm);
		return resp(ResultCode.Success, ResultCode.SUCCESS, add);
	}

	/**
	 * 修改
	 */
	@PostMapping(value = "/edit", produces = GlobalContext.PRODUCES)
	public String edit(@RequestBody WorkCenterTmplForm tmplForm) {
		String id = tmplForm.getId();
		String name = tmplForm.getName();
		String desc = tmplForm.getDesc();
		WorkCenterFormForm form = tmplForm.getForm();
		// 验证
		VerificationUtils.string("id", id);
		VerificationUtils.string("name", name);
		VerificationUtils.string("desc", desc);
		VerificationUtils.obj("form", form);
		WorkCenterTmplVo edit = iTmplFactory.edit(tmplForm);
		return resp(ResultCode.Success, ResultCode.SUCCESS, edit);
	}

	/**
	 * 删除
	 */
	@PostMapping(value = "/del", produces = GlobalContext.PRODUCES)
	public String del(@RequestBody WorkCenterTmplForm tmplForm) {
		String id = tmplForm.getId();
		// 验证
		VerificationUtils.string("id", id);
		iTmplFactory.del(tmplForm);
		return resp(ResultCode.Success, ResultCode.SUCCESS, null);
	}


	/**
	 * 修改模板的节点集合
	 */
	@PostMapping(value = "/edit/nodes", produces = GlobalContext.PRODUCES)
	public String editNodes(@RequestBody WorkCenterTmplForm tmplForm) {
		String id = tmplForm.getId();
		List<WorkCenterTmplNodePointForm> nodes = tmplForm.getNodes();
		// 验证
		VerificationUtils.string("id", id);
		VerificationUtils.objList("nodes", nodes);
		for (WorkCenterTmplNodePointForm pointNode : nodes) {
			WorkCenterTmplNodeForm tmplNode = pointNode.getParams();
			VerificationUtils.obj("params", tmplNode);
			String type = tmplNode.getType();
			String tmplNodeId = tmplNode.getId();
			VerificationUtils.string("tmplNodeId", tmplNodeId);
			String tmplNodeName = tmplNode.getName();
			VerificationUtils.string("tmplNodeName", tmplNodeName);
			String tmplId = tmplNode.getTmplId();
			VerificationUtils.string("tmplId", tmplId);
			List<String> handlers = tmplNode.getHandlers();
			List<String> handlerRoles = tmplNode.getHandlerRoles();
			if (!WaterConstants.WORK_CENTER_NODE_TYPE_END.equals(type)) {
				if (CollectionUtils.isEmpty(handlers) && CollectionUtils.isEmpty(handlerRoles)) {
					throw new FrameworkRuntimeException(BaseExceptionConstants.PARAM_FORMAT_ERROR, "请正确填写处理人/角色");
				}
			} else {
				tmplNode.setHandlers(null);
				tmplNode.setHandlerRoles(null);
			}
			String ifVerify = tmplNode.getIfVerify();
			String verifyTmplNodeId = tmplNode.getVerifyTmplNodeId();
			String noVerifyTmplNodeId = tmplNode.getNoVerifyTmplNodeId();
			if (WaterConstants.WORK_CENTER_NODE_TYPE_END.equals(type)) {
				tmplNode.setVerifyTmplNodeId(null);
				tmplNode.setNoVerifyTmplNodeId(null);
			} else if (WaterConstants.WORK_CENTER_NODE_NO_USE.equals(ifVerify)) {
				tmplNode.setVerifyTmplNodeId(null);
				VerificationUtils.string("noVerifyTmplNodeId", noVerifyTmplNodeId);
			} else if (WaterConstants.WORK_CENTER_NODE_USE.equals(ifVerify)) {
				VerificationUtils.string("verifyTmplNodeId", verifyTmplNodeId);
				VerificationUtils.string("noVerifyTmplNodeId", noVerifyTmplNodeId);
			} else {
				throw new FrameworkRuntimeException(BaseExceptionConstants.PARAM_FORMAT_ERROR, "请正确填写子节点");
			}
			String ifNotice = tmplNode.getIfNotice();
			List<String> noticers = tmplNode.getNoticers();
			List<String> noticerRoles = tmplNode.getNoticerRoles();
			if (WaterConstants.WORK_CENTER_NODE_NO_USE.equals(ifNotice)) {
				tmplNode.setNoticers(null);
				tmplNode.setNoticerRoles(null);
			} else if (WaterConstants.WORK_CENTER_NODE_USE.equals(ifNotice)) {
				if (CollectionUtils.isEmpty(noticers) && CollectionUtils.isEmpty(noticerRoles)) {
					throw new FrameworkRuntimeException(BaseExceptionConstants.PARAM_FORMAT_ERROR, "请正确填写通知人/角色");
				}
			} else {
				throw new FrameworkRuntimeException(BaseExceptionConstants.PARAM_FORMAT_ERROR, "请正确填写通知人/角色");
			}
			String ifUpdate = tmplNode.getIfUpdate();
			DictionaryChildForm updateDictChild = tmplNode.getUpdateDictChild();
			if (WaterConstants.WORK_CENTER_NODE_NO_USE.equals(ifUpdate)) {
				tmplNode.setUpdateDictChild(null);
			} else {
				VerificationUtils.obj("updateDictChild", updateDictChild);
				String updateDictChildId = updateDictChild.getChildId();
				VerificationUtils.string("updateDictChildId", updateDictChildId);

			}
			String ifPhoto = tmplNode.getIfPhoto();
			String photoNum = tmplNode.getPhotoNum();
			if (WaterConstants.WORK_CENTER_NODE_NO_USE.equals(ifPhoto)) {
				tmplNode.setPhotoNum("0");
			} else {
				VerificationUtils.string("photoNum", photoNum);
			}
			String ifUpload = tmplNode.getIfUpload();
			String uploadNum = tmplNode.getUploadNum();
			if (WaterConstants.WORK_CENTER_NODE_NO_USE.equals(ifUpload)) {
				tmplNode.setUploadNum("0");
			} else {
				VerificationUtils.string("uploadNum", uploadNum);
			}
			String ifOpinion = tmplNode.getIfOpinion();
			String opinionNum = tmplNode.getOpinionNum();
			if (WaterConstants.WORK_CENTER_NODE_NO_USE.equals(ifOpinion)) {
				tmplNode.setOpinionNum("0");
			} else {
				VerificationUtils.string("opinionNum", opinionNum);
			}
		}
		iTmplFactory.editNodes(tmplForm);
		return resp(ResultCode.Success, ResultCode.SUCCESS, null);
	}

	/**
	 * 复制模板
	 */
	@PostMapping(value = "/copy", produces = GlobalContext.PRODUCES)
	public String copy(@RequestBody WorkCenterTmplForm tmplForm) {
		String id = tmplForm.getId();
		// 验证
		VerificationUtils.string("id", id);
		WorkCenterTmplVo copy = iTmplFactory.copy(tmplForm);
		return resp(ResultCode.Success, ResultCode.SUCCESS, copy);
	}

	/**
	 * 模板导出
	 */
	@PostMapping(value = "/export")
	public void export(@RequestBody WorkCenterTmplForm tmplForm, HttpServletResponse response)
			throws UnsupportedEncodingException {
		String id = tmplForm.getId();
		VerificationUtils.string("id", id);
		try {
			String export = iTmplFactory.export(tmplForm);
			String fileName = URLEncoder.encode("模板导出", "UTF-8") + "-"
					+ DateUtils.format(new Date(), DateUtils.YYYYMMDDHHMMSS) + ".dx";
			response.setContentType("application/octet-stream; charset=UTF-8");
			response.setHeader("Content-Disposition", "filename=" + fileName);
			try (ServletOutputStream output = response.getOutputStream();
			     BufferedOutputStream buff = new BufferedOutputStream(output);) {
				buff.write(export.getBytes(StandardCharsets.UTF_8));
				buff.flush();
				// buff.close();
			} catch (Exception e) {
				logger.error("导出文件文件出错:{}", e);
			}
		} catch (FrameworkRuntimeException fe) {
			String msg = fe.getMsg();
			response.setCharacterEncoding("utf-8");
			response.setContentType("application/json;charset=UTF-8");
			try (ServletOutputStream output = response.getOutputStream();
			     BufferedOutputStream buff = new BufferedOutputStream(output);) {
				buff.write(msg.getBytes(StandardCharsets.UTF_8));
				buff.flush();
			} catch (Exception e) {
				logger.error("导出文件文件出错:{}", e);
			}
		}
	}

	/**
	 * 模板导入
	 */
	@PostMapping(value = "/import", consumes = "multipart/form-data")
	public String imports(@RequestParam("file") MultipartFile file) {
		logger.info(LogMsg.to("file", file));
		if (file.getSize() > 1 * 1024 * 1024) { // 1M
			return resp(ResultCode.Fail, "上传的文件过大", null);
		}
		iTmplFactory.imports(file);
		return resp(ResultCode.Success, ResultCode.SUCCESS, null);
	}

	/**
	 * 查询运维表单下载模板分页
	 */
	@PostMapping(value = "/page/admin", produces = GlobalContext.PRODUCES)
	public String pageByAdmin(@RequestBody WorkCenterTmplForm tmplForm) {
		String name = tmplForm.getName();
		String code = tmplForm.getCode();
		Integer page = tmplForm.getPage();
		Integer pageCount = tmplForm.getPageCount();
		// 验证
		VerificationUtils.string("name", name, true);
		VerificationUtils.string("code", code, true);
		VerificationUtils.integer("page", page);
		VerificationUtils.integer("pageCount", pageCount);
		Pagination<WorkCenterTmplVo> pagination = iTmplFactory.pageByAdmin(tmplForm);
		return resp(ResultCode.Success, ResultCode.SUCCESS, pagination);
	}
}
