package com.dotop.smartwater.project.module.api.revenue;

import com.dotop.smartwater.project.module.core.auth.vo.UserVo;
import com.dotop.smartwater.project.module.core.water.vo.OrderPreviewVo;

import java.util.List;

public interface IMakeOrderFactory {

	void asyncAutoPay(UserVo user);

	void batchSendmakeOrderExecutor(List<OrderPreviewVo> orders);
}
