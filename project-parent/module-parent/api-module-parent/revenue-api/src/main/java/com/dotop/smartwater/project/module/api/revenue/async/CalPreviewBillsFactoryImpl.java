package com.dotop.smartwater.project.module.api.revenue.async;

import com.dotop.smartwater.dependence.core.utils.BeanUtils;
import com.dotop.smartwater.dependence.core.utils.DateUtils;
import com.dotop.smartwater.dependence.core.utils.JSONUtils;
import com.dotop.smartwater.project.module.api.revenue.impl.OrderFactoryImpl;
import com.dotop.smartwater.project.module.api.revenue.ICalPreviewBillsFactory;
import com.dotop.smartwater.project.module.core.auth.vo.UserVo;
import com.dotop.smartwater.project.module.core.water.bo.LadderBo;
import com.dotop.smartwater.project.module.core.water.bo.OrderExtBo;
import com.dotop.smartwater.project.module.core.water.bo.OrderPreviewBo;
import com.dotop.smartwater.project.module.core.water.bo.ReduceBo;
import com.dotop.smartwater.project.module.core.water.config.Config;
import com.dotop.smartwater.project.module.core.water.constants.WaterConstants;
import com.dotop.smartwater.project.module.core.water.enums.ErrorType;
import com.dotop.smartwater.project.module.core.water.form.customize.PreviewForm;
import com.dotop.smartwater.project.module.core.water.utils.BusinessUtil;
import com.dotop.smartwater.project.module.core.water.utils.CalUtil;
import com.dotop.smartwater.project.module.core.water.vo.OrderVo;
import com.dotop.smartwater.project.module.core.water.vo.PayTypeVo;
import com.dotop.smartwater.project.module.core.water.vo.PurposeVo;
import com.dotop.smartwater.project.module.core.water.vo.ReduceVo;
import com.dotop.smartwater.project.module.core.water.vo.customize.LadderPriceDetailVo;
import com.dotop.smartwater.project.module.core.water.vo.customize.LastUpLinkVo;
import com.dotop.smartwater.project.module.core.water.vo.customize.OwnerDeviceVo;
import com.dotop.smartwater.project.module.service.revenue.IOrderService;
import com.dotop.smartwater.project.module.service.revenue.IPayTypeService;
import com.dotop.smartwater.project.module.service.revenue.IPurposeService;
import com.dotop.smartwater.project.module.service.revenue.IReduceService;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.Future;

/**

 */
@Component
public class CalPreviewBillsFactoryImpl implements ICalPreviewBillsFactory {

    private final static Logger logger = LogManager.getLogger(OrderFactoryImpl.class);

    @Autowired
    private IPayTypeService iPayTypeService;
    @Autowired
    private IReduceService iReduceService;
    @Autowired
    private IOrderService iOrderService;
    @Autowired
    private IPurposeService iPurposeService;

    /**
     * 计算账单
     *
     * @param view     账单预览对象
     * @param owners   业主列表
     * @param waterMap 上行MAP
     * @param orderMap 账单MAP
     * @param year     年
     * @param month    月
     * @param user     用户
     * @return Future
     */
    @Async("calExecutor")
    @Override
    public Future<Integer> asyncCalPreviewBill(PreviewForm view, List<OwnerDeviceVo> owners, Map<String, LastUpLinkVo> waterMap,
                                               Map<String, OrderVo> orderMap, String year, String month, UserVo user) {
        long time = System.currentTimeMillis();

        Map<String, PayTypeVo> payTypeMap = new HashMap<>();
        Map<String, ReduceVo> reduceMap = new HashMap<>();
        Map<String, PurposeVo> purposeMap = new HashMap<>();

        List<OrderPreviewBo> list = new ArrayList<>();
        List<OrderExtBo> extList = new ArrayList<>();
        for (OwnerDeviceVo owner : owners) {
            OrderPreviewBo orderPreview = new OrderPreviewBo();
            OrderExtBo orderExt = new OrderExtBo();

            PayTypeVo payType = null;
            ReduceVo reduce = null;

            orderPreview.setOwnerid(owner.getOwnerid());
            orderPreview.setTradestatus(WaterConstants.ORDER_TRADESTATUS_ABNORMAL);
            orderPreview.setCommunityid(owner.getCommunityid());
            OrderVo oldOrder = orderMap.get(owner.getUserno());
            LastUpLinkVo lastUplink = waterMap.get(owner.getDevid());

            //未激活，不计算水费
            if(WaterConstants.DEVICE_STATUS_NOACTIVE == (owner.getDevStatus() == null ? -1:owner.getDevStatus())){
                orderPreview.setDescribe(ErrorType.NOACTIVE.text());
                orderPreview.setErrortype(ErrorType.NOACTIVE.intValue());
            }else{
                if (oldOrder == null) {
                    orderPreview.setUpreadtime(null);
                    orderPreview.setUpreadwater(null);
                    // 计算用水量,第一次:截止时间的上行读数减初始读数
                    if (lastUplink != null) {
                        orderPreview.setReadtime(lastUplink.getRxtime());
                        orderPreview.setReadwater(lastUplink.getWater());
                        orderPreview.setWater(CalUtil.sub(lastUplink.getWater(), owner.getBeginvalue()));

                        if (lastUplink.getDays() <= view.getIntervalday()) {
                            orderPreview.setTradestatus(WaterConstants.ORDER_TRADESTATUS_NORMAL);
                        } else {
                            orderPreview.setDescribe("超过[" + lastUplink.getDays() + "]天没有抄表");
                            orderPreview.setErrortype(ErrorType.OFFLINEDEVICE.intValue());
                        }
                        // 这才算钱
                        payType = payTypeMap.get(owner.getPaytypeid());
                        payType = getPayTypeVo(payTypeMap, owner, orderPreview, payType);
                        reduce = getReduceVo(reduceMap, owner, orderPreview, orderExt, payType, reduce);
                        orderPreview.setDay(lastUplink.getDays());
                    } else {
                        if (owner.getUplinktime() != null) {
                            orderPreview.setDescribe("上次抄表时间:" + DateUtils.formatDatetime(owner.getUplinktime()) + ",时间间隔过长！");
                        } else {
                            orderPreview.setDescribe(ErrorType.NOWATERDATA.text());
                        }
                        orderPreview.setErrortype(ErrorType.NOWATERDATA.intValue());
                    }
                } else {
                    orderPreview.setUpreadtime(oldOrder.getReadtime());
                    orderPreview.setUpreadwater(oldOrder.getReadwater());
                    // 计算用水量
                    if (lastUplink != null) {
                        orderPreview.setReadtime(lastUplink.getRxtime());
                        orderPreview.setReadwater(lastUplink.getWater());
                        orderPreview.setWater(CalUtil.sub(lastUplink.getWater(), oldOrder.getReadwater()));

                        if (lastUplink.getDays() <= view.getIntervalday()) {
                            orderPreview.setTradestatus(WaterConstants.ORDER_TRADESTATUS_NORMAL);
                        } else {
                            orderPreview.setDescribe("超过[" + lastUplink.getDays() + "]天没有抄表");
                            orderPreview.setErrortype(ErrorType.OFFLINEDEVICE.intValue());
                        }

                        // 这才算钱
                        payType = payTypeMap.get(owner.getPaytypeid());
                        payType = getPayTypeVo(payTypeMap, owner, orderPreview, payType);
                        reduce = getReduceVo(reduceMap, owner, orderPreview, orderExt, payType, reduce);

                        orderPreview.setDay(lastUplink.getDays());
                    } else {
                        if (owner.getUplinktime() != null) {
                            orderPreview.setDescribe("上次抄表时间:" + DateUtils.formatDatetime(owner.getUplinktime()) + ",时间间隔过长！");
                        } else {
                            orderPreview.setDescribe(ErrorType.NOWATERDATA.text());
                        }
                        orderPreview.setErrortype(ErrorType.NOWATERDATA.intValue());
                    }
                }
            }

            orderPreview.setEnterpriseid(user.getEnterpriseid());
            orderPreview.setTradeno(String.valueOf(Config.Generator.nextId()));
            orderPreview.setYear(year);
            orderPreview.setMonth(month);

            orderPreview.setCommunityname(owner.getComname());
            orderPreview.setCommunityno(owner.getComno());
            orderPreview.setPricetypeid(owner.getPaytypeid());
            orderPreview.setPricetypename(owner.getTypename());
            orderPreview.setReduceid(owner.getReduceid());
            orderPreview.setPurposeid(owner.getPurposeid());

            orderPreview.setUsername(owner.getUsername());
            orderPreview.setUserno(owner.getUserno());
            orderPreview.setPhone(owner.getUserphone());
            orderPreview.setCardid(owner.getCardid());
            orderPreview.setAddr(owner.getUseraddr());

            orderPreview.setDeveui(owner.getDeveui());
            orderPreview.setDevno(owner.getDevno());
            orderPreview.setDevstatus(owner.getDevStatus());
            orderPreview.setTapstatus(owner.getTapstatus());
            orderPreview.setTaptype(owner.getTaptype());
            orderPreview.setPaystatus(WaterConstants.ORDER_PAYSTATUS_NOTPAID);
            orderPreview.setExplain(owner.getExplain());

            orderPreview.setGenerateuserid(user.getUserid());
            orderPreview.setGenerateusername(user.getAccount());
            orderPreview.setGeneratetime(DateUtils.formatDatetime(new Date()));

            if (orderPreview.getWater() != null && orderPreview.getWater() < 0) {
                orderPreview.setDescribe(ErrorType.WATEREXCEPTION.text());
                orderPreview.setTradestatus(WaterConstants.ORDER_TRADESTATUS_ABNORMAL);
                orderPreview.setErrortype(ErrorType.WATEREXCEPTION.intValue());
            }

            if (!"0".equals(owner.getPid())) {
                orderPreview.setDescribe(ErrorType.NOBIND.text());
                orderPreview.setErrortype(ErrorType.NOBIND.intValue());
                orderPreview.setTradestatus(WaterConstants.ORDER_TRADESTATUS_ABNORMAL);
            }

            list.add(orderPreview);

            // 水费用途
            PurposeVo purpose = null;
            if (StringUtils.isNotBlank(owner.getPurposeid())) {
                purpose = purposeMap.get(owner.getPurposeid());
                if (purpose == null) {
                    purpose = iPurposeService.findById(owner.getPurposeid());
                    purposeMap.put(owner.getPurposeid(), purpose);
                }
            }
            orderExt.setTradeno(orderPreview.getTradeno());
            if(purpose != null){
                orderExt.setPurposeinfo(JSONUtils.toJSONString(purpose));
            }

            if(reduce != null){
                orderExt.setReduceinfo(JSONUtils.toJSONString(reduce));
            }


            if(payType != null){
                orderExt.setPaytypeinfo(JSONUtils.toJSONString(payType));
            }

            extList.add(orderExt);
        }
        iOrderService.addOrderPreviewList(list);
        iOrderService.addOrderExtList(extList);
        logger.info("[{}]异步耗时:{}", Thread.currentThread().getName(), System.currentTimeMillis() - time);
        return new AsyncResult<>(0);
    }

    private ReduceVo getReduceVo(Map<String, ReduceVo> reduceMap, OwnerDeviceVo owner, OrderPreviewBo orderPreview, OrderExtBo orderExt, PayTypeVo payType, ReduceVo reduce) {
        if (payType != null) {
            // 减免类型
            reduce = reduceMap.get(owner.getReduceid());
            if (reduce == null) {
                ReduceBo reduceBo = new ReduceBo();
                reduceBo.setReduceid(owner.getReduceid());
                reduce = iReduceService.findById(reduceBo);
                reduceMap.put(owner.getReduceid(), reduce);
            }

            Double realAmount = BusinessUtil.getRealPayV2(
                    BeanUtils.copy(payType.getLadders(), LadderBo.class), orderPreview.getWater());

            orderPreview.setOriginal(new BigDecimal(realAmount));

            // 水费组成明细
            List<LadderPriceDetailVo> payDetailList = BusinessUtil.getLadderPriceDetailVo(
                    payType.getLadders(), orderPreview.getWater());

            // 计算是否设置保底收费，如果最终应缴金额小于保底收费则将保底收费赋给最终应缴金额
            realAmount = getRealAmount(payType, realAmount, payDetailList);

            orderExt.setChargeinfo(JSONUtils.toJSONString(payDetailList));

            if (reduce != null) {
                // 单位类型 1为吨 2为元
                if (reduce.getUnit() == 1) {
                    Double realwater = CalUtil.sub(orderPreview.getWater(), reduce.getRvalue());
                    // 减出优惠的吨数后重计水费
                    if (realwater > 0.0) {
                        realAmount = BusinessUtil.getRealPayV2(
                                BeanUtils.copy(payType.getLadders(), LadderBo.class), realwater);
                    } else {
                        realAmount = 0.0;
                    }
                }
                // 减出优惠钱数
                if (reduce.getUnit() == 2) {
                    realAmount = CalUtil.sub(realAmount, reduce.getRvalue());
                    if (realAmount < 0.0) {
                        realAmount = 0.0;
                    }
                }
            }

            orderPreview.setAmount(realAmount);
        } else {
            orderPreview.setDescribe(ErrorType.NOSETLADDER.text());
            orderPreview.setErrortype(ErrorType.NOSETLADDER.intValue());
        }
        return reduce;
    }

    public static Double getRealAmount(PayTypeVo payType, Double realAmount, List<LadderPriceDetailVo> payDetailList) {
        if (payType.getGuarantees() != null && payType.getGuarantees() > realAmount) {

            //只有保底了
            payDetailList.clear();

            realAmount = payType.getGuarantees();
            LadderPriceDetailVo lv = new LadderPriceDetailVo();
            lv.setName("保底收费");
            lv.setAmount(payType.getGuarantees());
            payDetailList.add(lv);
        }
        return realAmount;
    }

    private PayTypeVo getPayTypeVo(Map<String, PayTypeVo> payTypeMap, OwnerDeviceVo owner, OrderPreviewBo orderPreview, PayTypeVo payType) {
        if (payType == null) {
            payType = iPayTypeService.get(owner.getPaytypeid());
            if (payType == null) {
                orderPreview.setDescribe(ErrorType.NOSETPAYTYPE.text());
                orderPreview.setTradestatus(WaterConstants.ORDER_TRADESTATUS_ABNORMAL);
                orderPreview.setErrortype(ErrorType.NOSETPAYTYPE.intValue());
            }

            payTypeMap.put(owner.getPaytypeid(), payType);

        }
        return payType;
    }
}
