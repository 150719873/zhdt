package com.dotop.smartwater.project.server.water.rest.service.workcenter;

import java.io.BufferedOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.dotop.smartwater.dependence.core.common.BaseController;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.global.GlobalContext;
import com.dotop.smartwater.dependence.core.log.LogMsg;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.dependence.core.utils.DateUtils;
import com.dotop.smartwater.dependence.core.utils.VerificationUtils;
import com.dotop.smartwater.project.module.api.workcenter.IFormFactory;
import com.dotop.smartwater.project.module.core.water.constants.ResultCode;
import com.dotop.smartwater.project.module.core.water.form.WorkCenterFormForm;
import com.dotop.smartwater.project.module.core.water.model.BodyMap;
import com.dotop.smartwater.project.module.core.water.vo.WorkCenterFormVo;

/**
 * 工作中心表单管理
 * 

 * @date 2019年4月17日
 * @description
 */
@RestController("WorkCenterFormController")
@RequestMapping("/workcenter/form")

public class FormController implements BaseController<WorkCenterFormForm> {

	private static final Logger LOGGER = LogManager.getLogger(FormController.class);

	@Autowired
	private IFormFactory iFormFactory;

	private static final String PAGECOUNT = "pageCount";

	private static final int MAXLENGTH1 = 1024;

	private static final int MAXLENGTH10 = 10240;

	/**
	 * 查询分页
	 */
	@Override
	@PostMapping(value = "/page", produces = GlobalContext.PRODUCES)
	public String page(@RequestBody WorkCenterFormForm formForm) {
		String name = formForm.getName();
		String code = formForm.getCode();
		Integer page = formForm.getPage();
		Integer pageCount = formForm.getPageCount();
		// 验证
		VerificationUtils.string("name", name, true);
		VerificationUtils.string("code", code, true);
		VerificationUtils.integer("page", page);
		VerificationUtils.integer(PAGECOUNT, pageCount);
		Pagination<WorkCenterFormVo> pagination = iFormFactory.page(formForm);
		return resp(ResultCode.Success, ResultCode.SUCCESS, pagination);
	}

	/**
	 * 查询表单选择下拉
	 */
	@PostMapping(value = "/select", produces = GlobalContext.PRODUCES)
	public String select(@RequestBody WorkCenterFormForm formForm) {
		Integer page = formForm.getPage();
		Integer pageCount = formForm.getPageCount();
		// 验证
		VerificationUtils.integer("page", page);
		VerificationUtils.integer(PAGECOUNT, pageCount);
		Pagination<WorkCenterFormVo> pagination = iFormFactory.select(formForm);
		return resp(ResultCode.Success, ResultCode.SUCCESS, pagination);
	}

	/**
	 * 查询详情
	 */
	@Override
	@PostMapping(value = "/get", produces = GlobalContext.PRODUCES)
	public String get(@RequestBody WorkCenterFormForm formForm) {
		String id = formForm.getId();
		// 验证
		VerificationUtils.string("id", id);
		WorkCenterFormVo get = iFormFactory.get(formForm);
		return resp(ResultCode.Success, ResultCode.SUCCESS, get);
	}

	/**
	 * 新增
	 */
	@Override
	@PostMapping(value = "/add", produces = GlobalContext.PRODUCES)
	public String add(@RequestBody WorkCenterFormForm formForm) {
		String name = formForm.getName();
		String desc = formForm.getDesc();
		// 验证
		VerificationUtils.string("name", name);
		VerificationUtils.string("desc", desc, true, MAXLENGTH1);
		WorkCenterFormVo add = iFormFactory.add(formForm);
		return resp(ResultCode.Success, ResultCode.SUCCESS, add);
	}

	/**
	 * 修改
	 */
	@Override
	@PostMapping(value = "/edit", produces = GlobalContext.PRODUCES)
	public String edit(@RequestBody WorkCenterFormForm formForm) {
		String id = formForm.getId();
		String name = formForm.getName();
		String desc = formForm.getDesc();
		String body = formForm.getBody();
		String appBody = formForm.getAppBody();
		List<BodyMap> bodyMap = formForm.getBodyMap();
		// 验证
		VerificationUtils.string("id", id);
		VerificationUtils.string("name", name);
		VerificationUtils.string("desc", desc, true, MAXLENGTH1);
		VerificationUtils.string("body", body, false, MAXLENGTH10);
		VerificationUtils.string("appBody", appBody, true, MAXLENGTH10);
//		VerificationUtils.objList("bodyMap", bodyMap);
		for (BodyMap map : bodyMap) {
			String key = map.getKey();
			String val = map.getVal();
			VerificationUtils.string("key", key);
			VerificationUtils.string("val", val, false, MAXLENGTH1);
		}
		WorkCenterFormVo edit = iFormFactory.edit(formForm);
		return resp(ResultCode.Success, ResultCode.SUCCESS, edit);
	}

	/**
	 * 删除
	 */
	@Override
	@PostMapping(value = "/del", produces = GlobalContext.PRODUCES)
	public String del(@RequestBody WorkCenterFormForm formForm) {
		String id = formForm.getId();
		// 验证
		VerificationUtils.string("id", id);
		iFormFactory.del(formForm);
		return resp(ResultCode.Success, ResultCode.SUCCESS, null);
	}

	/**
	 * 数据源复制
	 */
	@PostMapping(value = "/copy", produces = GlobalContext.PRODUCES)
	public String copy(@RequestBody WorkCenterFormForm formForm) {
		String id = formForm.getId();
		// 验证
		VerificationUtils.string("id", id);
		WorkCenterFormVo copy = iFormFactory.copy(formForm);
		return resp(ResultCode.Success, ResultCode.SUCCESS, copy);
	}

	/**
	 * 表单导出
	 */
	@PostMapping(value = "/export")
	public void export(@RequestBody WorkCenterFormForm formForm, HttpServletResponse response)
			throws UnsupportedEncodingException {
		try {
			String id = formForm.getId();
			VerificationUtils.string("id", id);
			String export = iFormFactory.export(formForm);
			String fileName = URLEncoder.encode("表单导出", "UTF-8") + "-"
					+ DateUtils.format(new Date(), DateUtils.YYYYMMDDHHMMSS) + ".dx";
			response.setContentType("application/octet-stream; charset=UTF-8");
			response.setHeader("Content-Disposition", "filename=" + fileName);
			try (ServletOutputStream output = response.getOutputStream();
					BufferedOutputStream buff = new BufferedOutputStream(output);) {
				buff.write(export.getBytes(StandardCharsets.UTF_8));
				buff.flush();
			} catch (Exception e) {
				LOGGER.error("导出文件文件出错:{}", e);
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
				LOGGER.error("导出文件文件出错:{}", e);
			}
		}
	}

	/**
	 * 表单导入
	 */
	@PostMapping(value = "/import", consumes = "multipart/form-data")
	public String imports(@RequestParam("file") MultipartFile file) {
		LOGGER.info(LogMsg.to("file", file));
		if (file.getSize() > 1 * 1024 * 1024) { // 1M
			return resp(ResultCode.Fail, "上传的文件过大", null);
		}
		iFormFactory.imports(file);
		return resp(ResultCode.Success, ResultCode.SUCCESS, null);
	}

	/**
	 * 查询运维表单下载模板分页
	 */
	@PostMapping(value = "/page/admin", produces = GlobalContext.PRODUCES)
	public String pageByAdmin(@RequestBody WorkCenterFormForm formForm) {
		String name = formForm.getName();
		String code = formForm.getCode();
		Integer page = formForm.getPage();
		Integer pageCount = formForm.getPageCount();
		// 验证
		VerificationUtils.string("name", name, true);
		VerificationUtils.string("code", code, true);
		VerificationUtils.integer("page", page);
		VerificationUtils.integer(PAGECOUNT, pageCount);
		Pagination<WorkCenterFormVo> pagination = iFormFactory.pageByAdmin(formForm);
		return resp(ResultCode.Success, ResultCode.SUCCESS, pagination);
	}
}
