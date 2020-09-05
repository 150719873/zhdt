package com.dotop.smartwater.project.server.water.rest.service.tool;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.dotop.smartwater.dependence.core.common.BaseController;
import com.dotop.smartwater.dependence.core.global.GlobalContext;
import com.dotop.smartwater.dependence.core.log.LogMsg;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.dependence.core.utils.VerificationUtils;
import com.dotop.smartwater.project.module.api.tool.IPrintBindFactory;
import com.dotop.smartwater.project.module.core.water.constants.ResultCode;
import com.dotop.smartwater.project.module.core.water.form.DesignPrintForm;
import com.dotop.smartwater.project.module.core.water.form.PrintBindForm;
import com.dotop.smartwater.project.module.core.water.vo.DesignPrintVo;
import com.dotop.smartwater.project.module.core.water.vo.PrintBindVo;

/**
 * 打印模板绑定
 * 

 * @date 2019年4月1日
 */
@RestController()

@RequestMapping("/auth/design")
public class PrintBindController implements BaseController<PrintBindForm> {

	private static final Logger LOGGER = LogManager.getLogger(PrintBindController.class);

	@Autowired
	private IPrintBindFactory iPrintBindFactory;

	/**
	 * 打印模板绑定 分页模糊查询
	 */
	@Override
	@PostMapping(value = "/bindlist", produces = GlobalContext.PRODUCES)
	public String page(@RequestBody PrintBindForm printBindForm) {
		LOGGER.info(LogMsg.to("msg:", " 分页查询开始", "deviceForm", printBindForm));
		Integer page = printBindForm.getPage();
		Integer pageCount = printBindForm.getPageCount();
		// 验证
		VerificationUtils.integer("page", page);
		VerificationUtils.integer("pageCount", pageCount);
		Pagination<PrintBindVo> pagination = iPrintBindFactory.page(printBindForm);
		LOGGER.info(LogMsg.to("msg:", " 分页查询查询结束"));
		return resp(ResultCode.Success, ResultCode.Success, pagination);
	}

	/**
	 * 模板列表 查询所有的打印模板
	 */
	@PostMapping(value = "/templatelist", produces = GlobalContext.PRODUCES)
	public String templateList(@RequestBody PrintBindForm printBindForm) {
		LOGGER.info(LogMsg.to("msg:", " 模板列表查询开始", printBindForm));
		List<PrintBindVo> list = iPrintBindFactory.listAll(printBindForm);
		LOGGER.info(LogMsg.to("msg:", " 模板列表查询结束"));
		return resp(ResultCode.Success, ResultCode.Success, list);
	}

	/**
	 * 打印模板绑定
	 */
	@Override
	@PostMapping(value = "/bind", produces = GlobalContext.PRODUCES, consumes = GlobalContext.CONSUMES)
	public String add(@RequestBody PrintBindForm printBindForm) {

		LOGGER.info(LogMsg.to("msg:", "打印模板绑定功能开始", printBindForm));
		String enterpriseid = printBindForm.getEnterpriseid();
		Integer smsType = printBindForm.getSmstype();
		// 校验
		VerificationUtils.string("enterpriseid", enterpriseid);
		VerificationUtils.integer("smsType", smsType);
		PrintBindVo printBindVo = iPrintBindFactory.add(printBindForm);
		LOGGER.info(LogMsg.to("msg:", "打印模板绑定功能结束", printBindForm));
		return resp(ResultCode.Success, ResultCode.Success, printBindVo);
	}

	/**
	 * 打印模板 删除记录
	 */
	@Override
	@PostMapping(value = "/removebind", produces = GlobalContext.PRODUCES, consumes = GlobalContext.CONSUMES)
	public String del(@RequestBody PrintBindForm printBindForm) {
		LOGGER.info(LogMsg.to("msg:", "删除内容开始", printBindForm));
		String id = printBindForm.getId();
		VerificationUtils.string("id", id);
		String idstr = iPrintBindFactory.del(printBindForm);
		LOGGER.info(LogMsg.to("msg:", "删除内容结束", "id", idstr));
		return resp(ResultCode.Success, ResultCode.Success, id);
	}

	/**
	 * 打印模板详情
	 */
	@PostMapping(value = "/templateDetail", produces = GlobalContext.PRODUCES)
	public String get(@RequestBody DesignPrintForm designPrintForm) {
		LOGGER.info(LogMsg.to("msg:", "查询详情开始", "designPrintForm", designPrintForm));
		String id = designPrintForm.getId();
		VerificationUtils.string("id", id);
		DesignPrintVo designPrintVo = iPrintBindFactory.get(designPrintForm);
		LOGGER.info(LogMsg.to("msg:", "查询详情结束", designPrintForm));
		return resp(ResultCode.Success, "SUCCESS", designPrintVo);
	}

	@PostMapping(value = "/getRelationDesign", produces = GlobalContext.PRODUCES, consumes = GlobalContext.CONSUMES)
	public String getRelationDesign(@RequestBody DesignPrintForm designPrintForm) {
		LOGGER.info(LogMsg.to("msg:", "查询内容开始", "designPrintForm", designPrintForm));
		String id = designPrintForm.getId();
		VerificationUtils.string("id", id);
		DesignPrintVo designPrintVo = iPrintBindFactory.getRelationDesign(designPrintForm);
		LOGGER.info(LogMsg.to("msg:", "查询内容结束"));
		return resp(ResultCode.Success, "SUCCESS", designPrintVo);
	}
}
