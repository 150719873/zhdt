package com.dotop.smartwater.project.server.water.rest.service.workcenter;

import com.dotop.smartwater.project.module.core.water.vo.customize.WorkCenterTmplNodeGraphVo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.dotop.smartwater.dependence.core.common.BaseController;
import com.dotop.smartwater.dependence.core.global.GlobalContext;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.dependence.core.utils.VerificationUtils;
import com.dotop.smartwater.project.module.api.workcenter.ITmplNodeFactory;
import com.dotop.smartwater.project.module.core.water.constants.ResultCode;
import com.dotop.smartwater.project.module.core.water.form.WorkCenterTmplNodeForm;
import com.dotop.smartwater.project.module.core.water.vo.WorkCenterTmplNodeVo;

/**
 * 工作中心模板节点管理
 * 

 * @date 2019年4月17日
 * @description
 */
@RestController("WorkCenterTmplNodeController")
@RequestMapping("/workcenter/tmpl/node")

public class TmplNodeController implements BaseController<WorkCenterTmplNodeForm> {

	private final static Logger logger = LogManager.getLogger(TmplNodeController.class);

	@Autowired
	private ITmplNodeFactory iTmplNodeFactory;

	/**
	 * 查询分页
	 */
	@PostMapping(value = "/page", produces = GlobalContext.PRODUCES)
	public String page(@RequestBody WorkCenterTmplNodeForm tmplNodeForm) {
		String tmplId = tmplNodeForm.getTmplId();
		Integer page = tmplNodeForm.getPage();
		Integer pageCount = tmplNodeForm.getPageCount();
		// 验证
		VerificationUtils.string("tmplId", tmplId);
		VerificationUtils.integer("page", page);
		VerificationUtils.integer("pageCount", pageCount);
		Pagination<WorkCenterTmplNodeVo> pagination = iTmplNodeFactory.page(tmplNodeForm);
		return resp(ResultCode.Success, ResultCode.SUCCESS, pagination);
	}

	@PostMapping(value = "/graph", produces = GlobalContext.PRODUCES)
	public String graph(@RequestBody WorkCenterTmplNodeForm tmplNodeForm) {
		String tmplId = tmplNodeForm.getTmplId();
		// 验证
		VerificationUtils.string("tmplId", tmplId);
		WorkCenterTmplNodeGraphVo graphVo = iTmplNodeFactory.graph(tmplNodeForm);
		return resp(ResultCode.Success, ResultCode.SUCCESS, graphVo);
	}

	/**
	 * 查询详情
	 */
	@PostMapping(value = "/get", produces = GlobalContext.PRODUCES)
	public String get(@RequestBody WorkCenterTmplNodeForm tmplNodeForm) {
		String id = tmplNodeForm.getId();
		// 验证
		VerificationUtils.string("id", id);
		WorkCenterTmplNodeVo get = iTmplNodeFactory.get(tmplNodeForm);
		return resp(ResultCode.Success, ResultCode.SUCCESS, get);
	}

}
