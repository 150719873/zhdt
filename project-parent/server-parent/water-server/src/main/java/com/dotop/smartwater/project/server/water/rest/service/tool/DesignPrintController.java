package com.dotop.smartwater.project.server.water.rest.service.tool;

import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.dotop.smartwater.dependence.core.common.BaseController;
import com.dotop.smartwater.dependence.core.global.GlobalContext;
import com.dotop.smartwater.dependence.core.log.LogMsg;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.dependence.core.utils.VerificationUtils;
import com.dotop.smartwater.project.module.api.revenue.IDesignPrintFactory;
import com.dotop.smartwater.project.module.core.water.constants.ResultCode;
import com.dotop.smartwater.project.module.core.water.form.BillForm;
import com.dotop.smartwater.project.module.core.water.form.DesignPrintForm;
import com.dotop.smartwater.project.module.core.water.form.PrintBindForm;
import com.dotop.smartwater.project.module.core.water.vo.DesignPrintVo;

/**
 * @program: project-parent
 * @description: 打印设计

 * @create: 2019-03-13 09:27
 **/
@RestController

@RequestMapping("/design")
public class DesignPrintController implements BaseController<PrintBindForm> {

	private static final Logger LOGGER = LogManager.getLogger(DesignPrintController.class);

	@Autowired
	private IDesignPrintFactory iDesignPrintFactory;

	private static final String TEMPID = "tempid";

	@PostMapping(value = "/templatelist", produces = GlobalContext.PRODUCES)
	public String templateList(@RequestBody PrintBindForm printBindForm) {
		return resp(ResultCode.Success, ResultCode.SUCCESS, iDesignPrintFactory.templateList(printBindForm));
	}

	/**
	 * 打印预览
	 * 
	 * @param printBindForm
	 * @return
	 */
	@GetMapping(value = "/view", produces = "application/json;charset=utf-8")
	@ResponseBody
	public String view(DesignPrintForm designPrintForm) {

		String tempid = designPrintForm.getTempid();
		VerificationUtils.string(TEMPID, tempid);
		String contont = iDesignPrintFactory.view(designPrintForm);
		return resp(ResultCode.Success, ResultCode.SUCCESS, contont);

	}

	@GetMapping(value = "/viewDetail", produces = "application/json;charset=utf-8")
	@ResponseBody
	public String viewDetail(BillForm bill) {
		String contont = iDesignPrintFactory.viewDetail(bill);
		return resp(ResultCode.Success, ResultCode.SUCCESS, contont);
	}

	@GetMapping(value = "/preview", produces = "application/json;charset=utf-8")
	@ResponseBody
	public String preview(DesignPrintForm designPrintForm) {

		String tempid = designPrintForm.getTempid();
		VerificationUtils.string(TEMPID, tempid);
		Map<String, String> data = iDesignPrintFactory.preview(designPrintForm);
		return resp(ResultCode.Success, ResultCode.SUCCESS, data);
	}

	/**
	 * 打印模板分页查询
	 */
	@GetMapping(value = "/find", produces = "application/json;charset=utf-8")
	@ResponseBody
	public String page(DesignPrintForm designPrintForm) {
		LOGGER.info(LogMsg.to("msg:", " 分页查询开始", "分页参数designPrintForm", designPrintForm));
		Integer page = designPrintForm.getPage();
		Integer pageCount = designPrintForm.getPageCount();
		// 验证
		VerificationUtils.integer("page", page);
		VerificationUtils.integer("pageCount", pageCount);
		Pagination<DesignPrintVo> pagination = iDesignPrintFactory.getDesignPrintList(designPrintForm);
		LOGGER.info(LogMsg.to("msg:", " 分页查询查询结束"));
		return resp(ResultCode.Success, ResultCode.Success, pagination);
	}

	@PostMapping(value = "/save", produces = "application/json;charset=utf-8")
	@ResponseBody
	public String save(DesignPrintForm designPrintForm) {
		LOGGER.info(LogMsg.to("msg:", "打印模板新增功能开始", "参数designPrintForm", designPrintForm));
		iDesignPrintFactory.saveDesignPrint(designPrintForm);
		LOGGER.info(LogMsg.to("msg:", "保存结束"));
		return resp(ResultCode.Success, ResultCode.Success, null);
	}

	@GetMapping(value = "/get", produces = "application/json;charset=utf-8")
	@ResponseBody
	public String get(DesignPrintForm designPrintForm) {
		LOGGER.info(LogMsg.to("msg:", "打印模板查询详情开始", "参数designPrintForm", designPrintForm));
		DesignPrintVo temp = iDesignPrintFactory.get(designPrintForm);
		LOGGER.info(LogMsg.to("msg:", "打印模板查询详情开始", "DesignPrintVo", temp));
		return resp(ResultCode.Success, ResultCode.Success, temp);
	}

	@PostMapping(value = "/delete", produces = "application/json;charset=utf-8")
	@ResponseBody
	public String delete(DesignPrintForm designPrintForm) {
		LOGGER.info(LogMsg.to("msg:", "打印模板删除功能开始", "id:", designPrintForm.getId()));
		String id = designPrintForm.getId();
		VerificationUtils.string("id", id);
		iDesignPrintFactory.delete(designPrintForm);
		LOGGER.info(LogMsg.to("msg:", "打印模板删除功能结束"));
		return resp(ResultCode.Success, ResultCode.Success, null);
	}

	@PostMapping(value = "/addTemplate", produces = GlobalContext.PRODUCES)
	public String addTemplate(@RequestBody DesignPrintForm designPrintForm) {
		LOGGER.info(LogMsg.to("msg:", "新增模板功能开始", "DesignPrintForm:", designPrintForm));
		iDesignPrintFactory.addTemplate(designPrintForm);
		LOGGER.info(LogMsg.to("msg:", "新增模板功能开始"));
		return resp(ResultCode.Success, ResultCode.Success, null);
	}

	@PostMapping(value = "/updateTemplate", produces = GlobalContext.PRODUCES)
	public String updateTemplate(@RequestBody DesignPrintForm designPrintForm) {
		LOGGER.info(LogMsg.to("msg:", "addTemplate开始", "designPrintForm", designPrintForm));
		iDesignPrintFactory.updateTemplate(designPrintForm);
		LOGGER.info(LogMsg.to("msg:", "addTemplate结束"));
		return resp(ResultCode.Success, "SUCCESS", null);
	}

	/**
	 * 获取打印模板id
	 * 
	 * @param designPrintForm
	 * @return
	 */
	@PostMapping(value = "/getprint", produces = GlobalContext.PRODUCES)
	public String getprint(@RequestBody DesignPrintForm designPrintForm) {
		LOGGER.info(LogMsg.to("msg:", "addTemplate开始", "designPrintForm", designPrintForm));
		Integer type = designPrintForm.getType();
		VerificationUtils.integer("type", type);
		DesignPrintVo temp = iDesignPrintFactory.getPrintStatus(designPrintForm);
		LOGGER.info(LogMsg.to("msg:", "addTemplate结束"));
		return resp(ResultCode.Success, "SUCCESS", temp);
	}

	/**
	 * 水务系统 需要要鉴权
	 * 
	 * @param printBindForm
	 * @return
	 */
	@PostMapping(value = "/getprintview", produces = "application/json;charset=utf-8")
	@ResponseBody
	public String getPrintView(DesignPrintForm designPrintForm) {
		String tempid = designPrintForm.getTempid();
		VerificationUtils.string(TEMPID, tempid);
		String contont = iDesignPrintFactory.getPrintView(designPrintForm);
		return resp(ResultCode.Success, ResultCode.SUCCESS, contont);
	}

	/**
	 * 水务系统 预览 不需要鉴权
	 * 
	 * @param printBindForm
	 * @return
	 */
	@PostMapping(value = "/getPreview", produces = "application/json;charset=utf-8")
	@ResponseBody
	public String getPreview(DesignPrintForm designPrintForm) {

		String tempid = designPrintForm.getId();
		VerificationUtils.string(TEMPID, tempid);
		String contont = iDesignPrintFactory.getPreview(designPrintForm);
		return resp(ResultCode.Success, ResultCode.SUCCESS, contont);
	}

}
