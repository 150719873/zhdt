package com.dotop.pipe.server.rest.service.historylog;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dotop.pipe.core.form.HistoryLogForm;
import com.dotop.pipe.core.vo.historylog.HistoryLogVo;
import com.dotop.pipe.web.api.factory.historylog.IHistoryLogFactory;
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
@RestController()
@RequestMapping("/historyLog")
public class HistoryLogController implements BaseController<HistoryLogForm> {

	private final static Logger logger = LogManager.getLogger(HistoryLogController.class);

	@Autowired
	private IHistoryLogFactory iHistoryLogFactory;

	/**
	 * 分页查询
	 */
	@Override
	@GetMapping(value = "/{page}/{pageSize}", produces = GlobalContext.PRODUCES, consumes = GlobalContext.CONSUMES)
	public String page(HistoryLogForm historyLogForm) {
		logger.info(LogMsg.to("msg:", "历史变更记录分页查询开始", "page", historyLogForm.getPage(), "pageSize",
				historyLogForm.getPageSize()));
		// 验证
		VerificationUtils.integer("page", historyLogForm.getPage());
		VerificationUtils.integer("pageSize", historyLogForm.getPageSize());
		Pagination<HistoryLogVo> pagination = iHistoryLogFactory.page(historyLogForm);
		logger.info(LogMsg.to("msg:", "历史变更记录分页查询查询结束"));
		return resp(pagination);
	}

	/**
	 * 详情
	 */
	@Override
	@GetMapping(value = "/{id}", produces = GlobalContext.PRODUCES, consumes = GlobalContext.CONSUMES)
	public String get(HistoryLogForm historyLogForm) {
		logger.info(LogMsg.to("msg:", "详情查询开始"));
		VerificationUtils.string("logId", historyLogForm.getId());
		HistoryLogVo historyLogVo = iHistoryLogFactory.get(historyLogForm);
		logger.info(LogMsg.to("msg:", "详情查询结束"));
		return resp(historyLogVo);
	}

	/**
	 * 测试
	 */
	@Override
	@PostMapping(value = "/add", produces = GlobalContext.PRODUCES, consumes = GlobalContext.CONSUMES)
	public String add(@RequestBody HistoryLogForm historyLogForm) {
		iHistoryLogFactory.add(historyLogForm);
		return resp();
	}

}
