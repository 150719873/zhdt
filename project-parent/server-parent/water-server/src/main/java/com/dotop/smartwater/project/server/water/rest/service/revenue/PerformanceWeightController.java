package com.dotop.smartwater.project.server.water.rest.service.revenue;

import javax.annotation.Resource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.*;

import com.dotop.smartwater.dependence.core.common.BaseController;
import com.dotop.smartwater.dependence.core.global.GlobalContext;
import com.dotop.smartwater.dependence.core.log.LogMsg;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.dependence.core.utils.VerificationUtils;
import com.dotop.smartwater.project.module.api.revenue.IPerformanceWeightFactory;
import com.dotop.smartwater.project.module.core.water.constants.ResultCode;
import com.dotop.smartwater.project.module.core.water.form.PerforWeightForm;
import com.dotop.smartwater.project.module.core.water.form.WrongAccountForm;
import com.dotop.smartwater.project.module.core.water.vo.PerforWeightVo;

/**
 * 绩效考核-权重管理
 * 

 * @date 2019年2月26日
 * 
 */
@RestController

@RequestMapping("/perforWeight")
public class PerformanceWeightController implements BaseController<WrongAccountForm> {

	private static final Logger LOGGER = LogManager.getLogger(PerformanceWeightController.class);

	@Resource
	private IPerformanceWeightFactory iPerformanceWeightFactory;

	// 权重分页查询
	@PostMapping(value = "/page", produces = GlobalContext.PRODUCES)
	public String page(@RequestBody PerforWeightForm form) {
		LOGGER.info(LogMsg.to("msg:", " 权重分页查询开始", form));
		Pagination<PerforWeightVo> pagination = iPerformanceWeightFactory.page(form);
		LOGGER.info(LogMsg.to("msg:", " 权重分页查询查询结束"));
		return resp(ResultCode.Success, ResultCode.SUCCESS, pagination);
	}

	// 新增权重
	@PostMapping(value = "/add", produces = GlobalContext.PRODUCES)
	public String add(@RequestBody PerforWeightForm form) {
		LOGGER.info(LogMsg.to("msg:", " 新增权重开始", form));
		// 参数校验
		String title = form.getTitle();
		String describe = form.getDescribe();
		String score = form.getScore();
		VerificationUtils.string("title", title);
		VerificationUtils.string("describe", describe);
		VerificationUtils.string("score", score);

		iPerformanceWeightFactory.save(form);
		LOGGER.info(LogMsg.to("msg:", " 新增权重结束", form));
		return resp(ResultCode.Success, ResultCode.SUCCESS, null);
	}

	// 修改权重
	@PostMapping(value = "/edit", produces = GlobalContext.PRODUCES)
	public String edit(@RequestBody PerforWeightForm form) {
		LOGGER.info(LogMsg.to("msg:", " 修改权重开始", form));
		// 参数校验
		String id = form.getId();
		String title = form.getTitle();
		String describe = form.getDescribe();
		String score = form.getScore();

		VerificationUtils.string("id", id);
		VerificationUtils.string("title", title);
		VerificationUtils.string("describe", describe);
		VerificationUtils.string("score", score);
		iPerformanceWeightFactory.update(form);
		LOGGER.info(LogMsg.to("msg:", " 修改权重结束", form));
		return resp(ResultCode.Success, ResultCode.SUCCESS, null);
	}

	// 详情
	@PostMapping(value = "/get", produces = GlobalContext.PRODUCES)
	public String get(@RequestBody PerforWeightForm form) {
		LOGGER.info(LogMsg.to("msg:", " 获取权重开始", form));
		// 参数校验
		String id = form.getId();
		VerificationUtils.string("id", id);

		PerforWeightVo perforWeightVo = iPerformanceWeightFactory.getWeight(form);
		LOGGER.info(LogMsg.to("msg:", " 获取权重结束", form));
		return resp(ResultCode.Success, ResultCode.SUCCESS, perforWeightVo);
	}

	// 删除
	@PostMapping(value = "/delete", produces = GlobalContext.PRODUCES)
	public String delete(@RequestBody PerforWeightForm form) {
		LOGGER.info(LogMsg.to("msg:", " 删除权重开始", form));
		// 参数校验
		String id = form.getId();
		VerificationUtils.string("id", id);

		iPerformanceWeightFactory.delete(form);
		LOGGER.info(LogMsg.to("msg:", " 删除权重结束", form));
		return resp(ResultCode.Success, ResultCode.SUCCESS, null);
	}

}
