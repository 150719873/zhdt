package com.dotop.pipe.server.rest.service.point;

import java.math.BigDecimal;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dotop.pipe.core.form.PointForm;
import com.dotop.pipe.core.vo.point.PointVo;
import com.dotop.pipe.web.api.factory.point.IPointFactory;
import com.dotop.smartwater.dependence.core.common.BaseController;
import com.dotop.smartwater.dependence.core.global.GlobalContext;
import com.dotop.smartwater.dependence.core.log.LogMsg;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.dependence.core.utils.VerificationUtils;

/**
 * 
 *
 * @date 2019年1月16日
 */
@RestController
@RequestMapping("/point")
public class PointController implements BaseController<PointForm> {

	private final static Logger logger = LogManager.getLogger(PointController.class);

	@Autowired
	private IPointFactory iPointFactory;

	/**
	 * 新增坐标
	 */
	@Override
	@PostMapping(produces = GlobalContext.PRODUCES, consumes = GlobalContext.CONSUMES)
	public String add(@RequestBody PointForm pointForm) {
		logger.info(LogMsg.to("pointForm", pointForm));

		String code = pointForm.getCode();
		String name = pointForm.getName();
		String des = pointForm.getDes();
		BigDecimal longitude = pointForm.getLongitude();
		BigDecimal latitude = pointForm.getLatitude();
		String remark = pointForm.getRemark();

		// 验证
		VerificationUtils.string("code", code);
		VerificationUtils.string("name", name);
		VerificationUtils.string("des", des, true, 100);
		VerificationUtils.bigDecimal("longitude", longitude);
		VerificationUtils.bigDecimal("latitude", latitude);
		VerificationUtils.string("remark", remark, true, 500);
		PointVo pointVo = iPointFactory.add(pointForm);

		return resp("pointId", pointVo.getPointId());
	}

	/**
	 * 查询坐标
	 */
	@Override
	@GetMapping(value = "/{pointId}", produces = GlobalContext.PRODUCES, consumes = GlobalContext.CONSUMES)
	public String get(PointForm pointForm) {
		logger.info(LogMsg.to("pointForm", pointForm));
		String pointId = pointForm.getPointId();
		// 验证
		VerificationUtils.string("pointId", pointId);
		PointVo point = iPointFactory.get(pointForm);

		return resp(point);
	}

	/**
	 * 分页坐标
	 */
	@Override
	@GetMapping(value = "/page/{page}/{pageSize}", produces = GlobalContext.PRODUCES, consumes = GlobalContext.CONSUMES)
	public String page(PointForm pointForm) {
		logger.info(LogMsg.to("pointForm", pointForm));
		Integer page = pointForm.getPage();
		Integer pageSize = pointForm.getPageSize();
		// 验证
		VerificationUtils.integer("page", page);
		VerificationUtils.integer("pageSize", pageSize);
		Pagination<PointVo> pagination = iPointFactory.page(pointForm);

		return resp(pagination);
	}

	/**
	 * 列表坐标
	 */
	@Override
	@GetMapping(value = "/list/{limit}", produces = GlobalContext.PRODUCES, consumes = GlobalContext.CONSUMES)
	public String list(PointForm pointForm) {
		logger.info(LogMsg.to("pointForm", pointForm));
		Integer limit = pointForm.getLimit();
		// 验证
		VerificationUtils.integer("limit", limit);
		List<PointVo> list = iPointFactory.list(pointForm);

		return resp(list);
	}

	/**
	 * 更新坐标
	 */
	@Override
	@PutMapping(produces = GlobalContext.PRODUCES, consumes = GlobalContext.CONSUMES)
	public String edit(@RequestBody PointForm pointForm) {
		logger.info(LogMsg.to("pointForm", pointForm));

		String pointId = pointForm.getPointId();
		String code = pointForm.getCode();
		String name = pointForm.getName();
		String des = pointForm.getDes();
		BigDecimal longitude = pointForm.getLongitude();
		BigDecimal latitude = pointForm.getLatitude();
		String remark = pointForm.getRemark();

		// 验证
		VerificationUtils.string("pointId", pointId);
		VerificationUtils.string("code", code, true);
		VerificationUtils.string("name", name, true);
		VerificationUtils.string("des", des, true, 100);
		VerificationUtils.bigDecimal("longitude", longitude, true);
		VerificationUtils.bigDecimal("latitude", latitude, true);
		VerificationUtils.string("remark", remark, true, 500);

		iPointFactory.edit(pointForm);

		return resp();
	}

	/**
	 * 删除坐标
	 */
	@Override
	@DeleteMapping(value = "/{pointId}", produces = GlobalContext.PRODUCES, consumes = GlobalContext.CONSUMES)
	public String del(PointForm pointForm) {
		logger.info(LogMsg.to("pointForm", pointForm));
		String pointId = pointForm.getPointId();
		// 验证
		VerificationUtils.string("pointId", pointId);
		iPointFactory.del(pointForm);

		return resp();
	}
}
