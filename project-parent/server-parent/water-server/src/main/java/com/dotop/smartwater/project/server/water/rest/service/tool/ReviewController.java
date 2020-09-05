package com.dotop.smartwater.project.server.water.rest.service.tool;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.dotop.smartwater.dependence.core.common.BaseController;
import com.dotop.smartwater.dependence.core.global.GlobalContext;
import com.dotop.smartwater.dependence.core.utils.VerificationUtils;
import com.dotop.smartwater.project.module.api.device.IDeviceFactory;
import com.dotop.smartwater.project.module.api.revenue.IReviewDeviceFactory;
import com.dotop.smartwater.project.module.core.auth.form.AreaNodeForm;
import com.dotop.smartwater.project.module.core.water.constants.ResultCode;
import com.dotop.smartwater.project.module.core.water.form.ReviewDeviceForm;
import com.dotop.smartwater.project.module.core.water.form.customize.RemindersForm;

/**
 * @program: project-parent
 * @description: 设备复核

 * @create: 2019-04-18 09:27
 **/
@RestController

@RequestMapping("/device/review")
public class ReviewController implements BaseController<RemindersForm> {

	@Autowired
	private IDeviceFactory iDeviceFactory;

	@Resource
	private IReviewDeviceFactory factory;

	/**
	 * @return 获取区域设备数量
	 */
	@PostMapping(value = "/getDeviceCount", produces = GlobalContext.PRODUCES)
	public String getDeviceCount(@RequestBody List<AreaNodeForm> list) {
		if (list.isEmpty()) {
			return resp(ResultCode.Success, ResultCode.SUCCESS, 0);
		}
		List<String> areaIds = new ArrayList<>();
		for (AreaNodeForm a : list) {
			areaIds.add(a.getKey());
		}
		return resp(ResultCode.Success, ResultCode.SUCCESS, iDeviceFactory.getDeviceCountByAreaIds(areaIds));
	}

	/***
	 * 提交设备复核
	 **/
	@PostMapping(value = "/addDeviceReview", produces = GlobalContext.PRODUCES)
	public String addDeviceReview(@RequestBody ReviewDeviceForm form) {

		VerificationUtils.string("title", form.getTitle());
		VerificationUtils.string("communityIds", form.getCommunityIds(), false, Integer.MAX_VALUE);
		VerificationUtils.string("communityNames", form.getCommunityNames(), false, Integer.MAX_VALUE);
		VerificationUtils.string("userIds", form.getUserIds(), false, Integer.MAX_VALUE);
		VerificationUtils.string("userNames", form.getUserNames(), false, Integer.MAX_VALUE);
		VerificationUtils.string("number", form.getNumber());
		VerificationUtils.string("endTime", form.getEndTime());
		VerificationUtils.string("diff", form.getDiff());
		VerificationUtils.string("devNumber", form.getDevNumber());

		factory.addDeviceReview(form);
		return resp(ResultCode.Success, ResultCode.SUCCESS, null);

	}

}
