package com.dotop.smartwater.project.module.api.revenue;

import com.dotop.smartwater.dependence.core.common.BaseFactory;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.project.module.core.auth.vo.AreaNodeVo;
import com.dotop.smartwater.project.module.core.auth.vo.UserVo;
import com.dotop.smartwater.project.module.core.third.form.iot.MeterDataForm;
import com.dotop.smartwater.project.module.core.water.form.OrderExtForm;
import com.dotop.smartwater.project.module.core.water.form.OrderForm;
import com.dotop.smartwater.project.module.core.water.form.OrderPreviewForm;
import com.dotop.smartwater.project.module.core.water.form.OwnerForm;
import com.dotop.smartwater.project.module.core.water.form.customize.PreviewForm;
import com.dotop.smartwater.project.module.core.water.vo.*;

import java.util.Map;

/**
 * 账单相关
 */
public interface IOrderFactory extends BaseFactory<OrderForm, OrderVo> {
    OwnerVo getOwnerUser(OwnerForm ownerForm) ;

    Pagination<OrderPreviewVo> OrderPreviewList(PreviewForm previewForm) ;

    void generateOrder(PreviewForm previewForm) ;

    void deletePreview(OwnerForm ownerForm) ;

    void clearPreview(PreviewForm previewForm, boolean flag) ;

    void makeOrder(PreviewForm previewForm) ;

    Pagination<OrderVo> bills(OrderPreviewForm orderPreviewForm) ;

    Pagination<OrderVo> orderList(OrderPreviewForm orderPreviewForm) ;

    void deleteOrder(OrderForm orderForm) ;

    void signNormal(OrderForm orderForm) ;

    PayTypeVo findOrderPayTypeDetail(OrderForm orderForm) ;

    OrderExtVo loadOrderExt(OrderForm orderForm) ;

    void updatePayStatus(OrderExtForm orderExtForm) ;

    Map<String, Object> orderPay(OrderExtForm orderExtForm) ;

    Map<String, Object> supplementPrint(OrderExtForm orderExtForm,PrintBindVo printBindVo) ;

    void manualMeter(MeterDataForm meterDataForm) ;

    Integer singleBuildOrder(OwnerVo owner, AreaNodeVo community, UserVo user, DeviceVo device, PayTypeVo payType)
            ;

    void readMeterAndBuildOrder(MeterDataForm meterDataForm) ;

    Pagination<OrderPreviewVo> AuditingOrderPreviewList(PreviewForm previewForm) ;

    void makeOrderByAuditing(PreviewForm previewForm) ;
}
