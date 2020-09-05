package com.dotop.smartwater.project.server.schedule.schedule;

import com.dotop.smartwater.dependence.core.log.LogMsg;
import com.dotop.smartwater.dependence.core.utils.DateUtils;
import com.dotop.smartwater.dependence.core.utils.StrUtils;
import com.dotop.smartwater.project.module.api.revenue.ICalPreviewBillsFactory;
import com.dotop.smartwater.project.module.core.auth.vo.AreaNodeVo;
import com.dotop.smartwater.project.module.core.auth.vo.UserVo;
import com.dotop.smartwater.project.module.core.water.bo.PayTypeBo;
import com.dotop.smartwater.project.module.core.water.constants.OrderGenerateType;
import com.dotop.smartwater.project.module.core.water.constants.WaterConstants;
import com.dotop.smartwater.project.module.core.water.form.customize.PreviewForm;
import com.dotop.smartwater.project.module.core.water.vo.OrderVo;
import com.dotop.smartwater.project.module.core.water.vo.PayTypeVo;
import com.dotop.smartwater.project.module.core.water.vo.customize.LastUpLinkVo;
import com.dotop.smartwater.project.module.core.water.vo.customize.OwnerDeviceVo;
import com.dotop.smartwater.project.module.service.revenue.IOrderService;
import com.dotop.smartwater.project.module.service.revenue.IOwnerService;
import com.dotop.smartwater.project.module.service.revenue.IPayTypeService;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.*;
import java.util.concurrent.Future;

/**
 * 监听每月自动生成账单的设置任务

 */
public class MakeOrderScheduled {

    private static final Logger LOGGER = LogManager.getLogger(MakeOrderScheduled.class);

    @Autowired
    private IPayTypeService iPayTypeService;

    @Autowired
    private IOrderService iOrderService;

    @Autowired
    private IOwnerService iOwnerService;

    @Autowired
    private ICalPreviewBillsFactory iCalPreviewBillsFactory;

    /**
     * 每天凌晨1点执行
     */
    //@Scheduled(initialDelay = 5000, fixedRate = 300000)
    @Scheduled(cron = "0 0 1 * * ?")
    public void init() {
        LOGGER.info("开始按收费种类开启了定时任务生成账单.....");
        List<PayTypeVo> list;
        PayTypeBo payTypeBo = new PayTypeBo();
        payTypeBo.setTaskSwitch(true);

        list = iPayTypeService.noPagingFind(payTypeBo);
        LOGGER.info("共[" + list.size() + "]个收费种类开启了定时任务...");
        if (list.size() > 0) {
            Date date = new Date();
            for (PayTypeVo vo : list) {
                if (vo.getUpdated() != null && StringUtils.isNotBlank(vo.getTaskDay())
                        && StringUtils.isNotBlank(vo.getTaskMonth())
                        && vo.getTaskIntervalDay() != null
                        && vo.getEnterpriseid() != null) {
                    String day = DateUtils.format(date,"dd");
                    if (!vo.getTaskDay().equals(String.valueOf(day))) {
                        continue;
                    }
                    int month = date.getMonth();
                    Date date1 = DateUtils.month(vo.getUpdated(),Integer.parseInt(vo.getTaskMonth()));
                    int monthNew = date1.getMonth();
                    if(month != monthNew){
                        continue;
                    }

                    List<String> ownerNos = iOwnerService.getOwnersByTypeId(vo.getTypeid(),WaterConstants.OWNER_STATUS_CREATE);
                    if(ownerNos != null && ownerNos.size() > 0){
                        String nos = StrUtils.join(",",ownerNos);
                        PreviewForm previewForm = new PreviewForm();
                        previewForm.setUsernos(nos);
                        previewForm.setMonth(DateUtils.format(date,"yyyyMM"));
                        previewForm.setMetertime(DateUtils.format(date,"yyyy-MM-dd"));
                        previewForm.setIntervalday(vo.getTaskIntervalDay());

                        makeOrderByPayType(previewForm, vo);
                    }


                    //更新updated
                    payTypeBo.setTypeid(vo.getTypeid());
                    payTypeBo.setUpdated(date);
                    iPayTypeService.updatePayType(payTypeBo);
                }
            }
        } else {
            LOGGER.info("无收费种类开启了定时任务");
        }
    }

    private void makeOrderByPayType(PreviewForm previewForm, PayTypeVo payTypeVo) {
        List<OwnerDeviceVo> ownerDevices;

        String year = previewForm.getMonth().substring(0, 4);
        String month = previewForm.getMonth().substring(4, 6);

        // 截止时间
        String[] data = previewForm.getMetertime().split("-");
        if (data.length != OrderGenerateType.DATE_STRING_ARRAY_LENGTH) {
            LOGGER.info("预览账单截止时间格式错误");
            return;
        }
        // 得到一个Calendar的实例
        Calendar ca = Calendar.getInstance();
        // 月份是从0开始的，所以11表示12月
        ca.set(Integer.parseInt(data[0]), Integer.parseInt(data[1]) - 1, Integer.parseInt(data[2]));
        // 月份减1
        ca.add(Calendar.MONTH, -1);
        // 结果
        String CurrentMonth = data[0] + data[1];
        String LastMonth = DateUtils.format(ca.getTime(), DateUtils.YYYYMM);
        String meterTime = previewForm.getMetertime() + " 23:59:59";

        ownerDevices = iOwnerService.findOwnerByOwnernos(previewForm.getUsernos(), payTypeVo.getEnterpriseid());

        if (ownerDevices == null || ownerDevices.isEmpty()) {
            LOGGER.info("没有获取到业主信息或者业主已生成预览");
            return;
        }

        Map<String, AreaNodeVo> map = iPayTypeService.getAreasForSchedule(payTypeVo.getEnterpriseid());

        if (map != null && map.size() != 0) {
            for (OwnerDeviceVo o : ownerDevices) {
                AreaNodeVo areaNode = map.get(String.valueOf(o.getCommunityid()));
                if (areaNode == null) {
                    continue;
                }
                o.setComname(areaNode.getTitle());
            }
        }

        try {
            // 批量获取当前业主水表读数,写入map
            long tt = System.currentTimeMillis();
            UserVo user = new UserVo();
            user.setUserid("");
            user.setAccount("定时任务");
            user.setEnterpriseid(payTypeVo.getEnterpriseid());

            Map<String, LastUpLinkVo> waterMap = iOrderService.findLastUplinkList(previewForm, CurrentMonth, LastMonth,
                    meterTime);
            LOGGER.info("waterMap:{}", (System.currentTimeMillis() - tt));

            tt = System.currentTimeMillis();
            Map<String, OrderVo> orderMap = iOrderService.findByUserNoMap(payTypeVo.getEnterpriseid(), previewForm.getUsernos());

            LOGGER.info("orderMap:{}", (System.currentTimeMillis() - tt));
            int size = ownerDevices.size();
            int unitNum = 2000;
            int startIndex = 0;
            int endIndex;

            List<Future<Integer>> futurelist = new ArrayList<>();

            // TODO 生成账单 异步任务
            while (size > 0) {
                if (size > unitNum) {
                    endIndex = startIndex + unitNum;
                } else {
                    endIndex = startIndex + size;
                }
                Future<Integer> future = iCalPreviewBillsFactory.asyncCalPreviewBill(previewForm,
                        ownerDevices.subList(startIndex, endIndex), waterMap, orderMap, year, month, user);
                futurelist.add(future);
                size = size - unitNum;
                startIndex = endIndex;
            }
            for (Future<Integer> f : futurelist) {
                f.get();
            }
        } catch (Exception e) {
            LOGGER.info(LogMsg.to("ex", e, "MakeOrderScheduled -> makeOrderByPayType", e.getMessage()));
        }
    }
}
