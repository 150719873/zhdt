package com.dotop.pipe.server.rest.service.collection;

import com.dotop.pipe.core.form.CollectionDeviceForm;
import com.dotop.pipe.core.vo.collection.CollectionDeviceVo;
import com.dotop.pipe.web.api.factory.collection.ICollectionDeviceFactory;
import com.dotop.smartwater.dependence.core.common.BaseController;
import com.dotop.smartwater.dependence.core.global.GlobalContext;
import com.dotop.smartwater.dependence.core.log.LogMsg;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.dependence.core.utils.VerificationUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 收藏夹功能
 * 
 *
 *
 */
@RestController()
@RequestMapping("/collection")
public class CollectionDeviceController implements BaseController<CollectionDeviceForm> {

	private static final Logger logger = LogManager.getLogger(CollectionDeviceController.class);

	@Resource
	private ICollectionDeviceFactory iCollectionDeviceFactory;

	@PostMapping(value = "/add", produces = GlobalContext.PRODUCES, consumes = GlobalContext.CONSUMES)
	public String add(@RequestBody CollectionDeviceForm collectionDeviceForm) {
		logger.info(LogMsg.to("msg:", "收藏夹功能新增开始", "collectionDeviceForm", collectionDeviceForm));
		VerificationUtils.string("deviceId", collectionDeviceForm.getDeviceId());
		CollectionDeviceVo collectionDeviceVo = iCollectionDeviceFactory.add(collectionDeviceForm);
		logger.info(LogMsg.to("msg:", "收藏夹功能新增结束", "更新数据"));
		return resp(collectionDeviceVo);
	}

	@PostMapping(value = "/page", produces = GlobalContext.PRODUCES, consumes = GlobalContext.CONSUMES)
	public String page(@RequestBody CollectionDeviceForm collectionDeviceForm) {
		logger.info(LogMsg.to("msg:", "收藏夹功能分页查询开始", "collectionDeviceForm", collectionDeviceForm));
		Integer page = collectionDeviceForm.getPage();
		Integer pageSize = collectionDeviceForm.getPageSize();
		// 验证
		VerificationUtils.integer("page", page);
		VerificationUtils.integer("pageSize", pageSize);
		Pagination<CollectionDeviceVo> pagination = iCollectionDeviceFactory.page(collectionDeviceForm);
		logger.info(LogMsg.to("msg:", "收藏夹功能分页查询结束"));
		return resp(pagination);
	}

	/**
	 * 收藏夹功能删除
	 */
	@Override
	@PutMapping(value = "/del", produces = GlobalContext.PRODUCES, consumes = GlobalContext.CONSUMES)
	public String edit(@RequestBody CollectionDeviceForm collectionDeviceForm) {
		logger.info(LogMsg.to("msg:", "收藏夹功能删除", "collectionDeviceForm", collectionDeviceForm));
		// 验证
		String id = collectionDeviceForm.getDeviceId();
		// 处理状态不能为空
		VerificationUtils.string("id", id);
		iCollectionDeviceFactory.del(collectionDeviceForm);
		logger.info(LogMsg.to("msg:", "收藏夹功能删除结束", "更新数据"));
		return resp();
	}

}
