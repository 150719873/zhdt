package com.dotop.smartwater.project.server.water.rest.service.revenue;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.dotop.smartwater.dependence.core.common.BaseController;
import com.dotop.smartwater.dependence.core.global.GlobalContext;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.dependence.core.utils.DateUtils;
import com.dotop.smartwater.dependence.core.utils.VerificationUtils;
import com.dotop.smartwater.project.module.api.revenue.ICouponFactory;
import com.dotop.smartwater.project.module.core.water.constants.ResultCode;
import com.dotop.smartwater.project.module.core.water.form.CouponForm;
import com.dotop.smartwater.project.module.core.water.vo.CouponVo;

/**
 * 

 * @Date 2019年2月25日
 * 
 *       原com.dotop.water.controller.CouponController
 */
@RestController

@RequestMapping(value = "/CouponController")
public class CouponController implements BaseController<CouponForm> {

	@Autowired
	private ICouponFactory iCouponFactory;

	private static final String COUPONID = "couponid";

	@Override
	@PostMapping(value = "/list", produces = GlobalContext.PRODUCES)
	public String list(@RequestBody CouponForm couponForm) {
		// 参数校验
		Pagination<CouponVo> pagination = iCouponFactory.getCouponList(couponForm);
		return resp(ResultCode.Success, ResultCode.SUCCESS, pagination);

	}

	@PostMapping(value = "/add", produces = GlobalContext.PRODUCES)
	public String add(HttpServletRequest request, @RequestBody CouponForm couponForm) {
		// 参数校验
		Integer addType = couponForm.getAddType();
		String communityids = couponForm.getCommunityids();
		String usernos = couponForm.getUsernos();
		String name = couponForm.getName();
		Integer type = couponForm.getType();
		Date starttime = couponForm.getStarttime();
		Date endtime = couponForm.getEndtime();
		Double facevalue = couponForm.getFacevalue();
		Integer unit = couponForm.getUnit();
		VerificationUtils.integer("addType", addType);
		if (addType == 1) {
			// 按区域
			VerificationUtils.string("communityids", communityids, false, 3600);
		} else {
			// 按业主编号
			VerificationUtils.string("usernos", usernos, false, 3600);
		}
		VerificationUtils.string("name", name);
		VerificationUtils.integer("type", type);
		VerificationUtils.date("starttime", starttime);
		VerificationUtils.date("endtime", endtime);
		VerificationUtils.doubles("facevalue", facevalue);
		VerificationUtils.integer("unit", unit);

		iCouponFactory.add(couponForm);
		return resp(ResultCode.Success, ResultCode.SUCCESS, null);
	}

	@PostMapping(value = "/batchAdd", produces = GlobalContext.PRODUCES)
	public String batchAdd(HttpServletRequest request, @RequestBody CouponForm couponForm) {
		// 参数校验
		String communityid = couponForm.getCommunityid();
		String name = couponForm.getName();
		Integer type = couponForm.getType();
		Date starttime = couponForm.getStarttime();
		Date endtime = couponForm.getEndtime();
		Double facevalue = couponForm.getFacevalue();
		Integer unit = couponForm.getUnit();
		VerificationUtils.string("communityid", communityid);
		VerificationUtils.string("name", name);
		VerificationUtils.integer("type", type);
		VerificationUtils.date("starttime", starttime);
		VerificationUtils.date("endtime", endtime);
		VerificationUtils.doubles("facevalue", facevalue);
		VerificationUtils.integer("unit", unit);
		if (facevalue <= 0) {
			return resp(ResultCode.Fail, "面值不能小于等于0", null);
		}
		if (!DateUtils.compare(endtime, new Date())) {
			return resp(ResultCode.Fail, "结束日期必须大于今天", null);
		}

		iCouponFactory.batchAdd(couponForm);
		return resp(ResultCode.Success, ResultCode.SUCCESS, null);
	}

	@PostMapping(value = "/getCoupon", produces = GlobalContext.PRODUCES)
	public String getCoupon(@RequestBody CouponForm couponForm) {
		// 参数校验
		String couponid = couponForm.getCouponid();
		String no = couponForm.getNo();
		VerificationUtils.string(COUPONID, couponid);
		VerificationUtils.string("no", no);

		CouponVo coupon = iCouponFactory.getCoupon(couponForm);
		if (coupon != null) {
			return resp(ResultCode.Success, ResultCode.SUCCESS, coupon);
		} else {
			return resp(ResultCode.Fail, "代金券/抵扣券不存在", null);
		}
	}

	@PostMapping(value = "/delete", produces = GlobalContext.PRODUCES)
	public String delete(@RequestBody CouponForm couponForm) {
		String couponid = couponForm.getCouponid();
		VerificationUtils.string(COUPONID, couponid);

		iCouponFactory.delete(couponForm);
		return resp(ResultCode.Success, ResultCode.SUCCESS, null);
	}

	@PostMapping(value = "/disable", produces = GlobalContext.PRODUCES)
	public String disable(@RequestBody CouponForm couponForm) {
		String couponid = couponForm.getCouponid();
		VerificationUtils.string(COUPONID, couponid);

		iCouponFactory.disable(couponForm);
		return resp(ResultCode.Success, ResultCode.SUCCESS, null);
	}

}
