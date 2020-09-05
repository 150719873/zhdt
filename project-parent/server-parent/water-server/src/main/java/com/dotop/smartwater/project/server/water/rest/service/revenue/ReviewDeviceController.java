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
import com.dotop.smartwater.project.module.api.revenue.IReviewDeviceFactory;
import com.dotop.smartwater.project.module.core.water.constants.ResultCode;
import com.dotop.smartwater.project.module.core.water.form.ReviewDetailForm;
import com.dotop.smartwater.project.module.core.water.form.ReviewDeviceForm;
import com.dotop.smartwater.project.module.core.water.vo.ReviewDetailVo;
import com.dotop.smartwater.project.module.core.water.vo.ReviewDeviceVo;

/**
 * 设备复核
 * 

 * @date 2019年4月6日
 *
 */
@RestController

@RequestMapping("/review")
public class ReviewDeviceController implements BaseController<ReviewDeviceForm> {

	private static final Logger LOGGER = LogManager.getLogger(ReviewDeviceController.class);

	@Resource
	private IReviewDeviceFactory factory;

	private static final String BATCHNO = "batchNo";

	// 分页查询
	@Override
	@PostMapping(value = "/page", produces = GlobalContext.PRODUCES)
	public String page(@RequestBody ReviewDeviceForm form) {
		LOGGER.info(LogMsg.to("msg:", " 设备复核分页查询", "form", form));
		Pagination<ReviewDeviceVo> pagination = factory.page(form);
		LOGGER.info(LogMsg.to("msg:", " 设备复核分页查询结束", "form", form));
		return resp(ResultCode.Success, ResultCode.SUCCESS, pagination);
	}

	// 生成设备复核数据
	@PostMapping(value = "/generate", produces = GlobalContext.PRODUCES)
	public String generate(@RequestBody ReviewDeviceForm form) {
		LOGGER.info(LogMsg.to("msg:", "生成设备复核数据开始", "form", form));
		// 参数校验
		VerificationUtils.string("title", form.getTitle());
		VerificationUtils.string("communityIds", form.getCommunityIds());
		VerificationUtils.string("communityNames", form.getCommunityNames());
		VerificationUtils.string("userNames", form.getUserNames());
		VerificationUtils.string("userIds", form.getUserIds());
		VerificationUtils.string("number", form.getNumber());
		VerificationUtils.string("endTime", form.getEndTime());
		VerificationUtils.string("diff", form.getDiff());
		VerificationUtils.string("devNumber", form.getDevNumber());
		factory.generate(form);
		LOGGER.info(LogMsg.to("msg:", "生成设备复核数据结束", "form", form));
		return resp(ResultCode.Success, ResultCode.SUCCESS, null);
	}

	// 任务详情
	@Override
	@PostMapping(value = "/get", produces = GlobalContext.PRODUCES)
	public String get(@RequestBody ReviewDeviceForm form) {
		LOGGER.info(LogMsg.to("msg:", "获取任务详情开始", "form", form));
		// 参数校验
		VerificationUtils.string(BATCHNO, form.getBatchNo());
		ReviewDeviceVo vo = factory.get(form);
		LOGGER.info(LogMsg.to("msg:", "获取任务情结束", "form", form));
		return resp(ResultCode.Success, ResultCode.SUCCESS, vo);
	}

	@Override
	@PostMapping(value = "/edit", produces = GlobalContext.PRODUCES)
	public String edit(@RequestBody ReviewDeviceForm form) {

		// 参数校验
		VerificationUtils.string("id", form.getId());
		factory.edit(form);
		return resp(ResultCode.Success, ResultCode.SUCCESS, null);
	}

	@Override
	@PostMapping(value = "/del", produces = GlobalContext.PRODUCES)
	public String del(@RequestBody ReviewDeviceForm form) {
		// 参数校验
		VerificationUtils.string(BATCHNO, form.getBatchNo());
		factory.del(form);
		return resp(ResultCode.Success, ResultCode.SUCCESS, null);
	}

	// 复核详情
	@PostMapping(value = "/detail", produces = GlobalContext.PRODUCES)
	public String detail(@RequestBody ReviewDetailForm form) {
		LOGGER.info(LogMsg.to("msg:", " 设备复核详情分页查询开始", "form", form));
		VerificationUtils.string(BATCHNO, form.getBatchNo());
		Pagination<ReviewDetailVo> pagination = factory.detailPage(form);
		LOGGER.info(LogMsg.to("msg:", " 设备复核详情分页查询结束", "form", form));
		return resp(ResultCode.Success, ResultCode.SUCCESS, pagination);
	}

}
