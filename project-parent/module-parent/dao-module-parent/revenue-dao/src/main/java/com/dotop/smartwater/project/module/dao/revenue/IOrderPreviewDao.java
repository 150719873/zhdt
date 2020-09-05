package com.dotop.smartwater.project.module.dao.revenue;

import com.dotop.smartwater.dependence.core.common.BaseDao;
import com.dotop.smartwater.project.module.core.water.dto.OrderPreviewDto;
import com.dotop.smartwater.project.module.core.water.form.customize.PreviewForm;
import com.dotop.smartwater.project.module.core.water.vo.OrderPreviewVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**

 * @date 2019年2月25日
 */
public interface IOrderPreviewDao extends BaseDao<OrderPreviewDto, OrderPreviewVo> {


	List<OrderPreviewVo> findOrderPreview(PreviewForm previewForm);

	void deletePreview(String ownerid);

	int clearPreview(@Param("condition") String condition);

	void update(OrderPreviewDto orderPreviewDto);

	List<OrderPreviewVo> AuditingOrderPreviewList(PreviewForm previewForm);

	void updateOrderPreviewApprovalResult(PreviewForm previewForm);
}
