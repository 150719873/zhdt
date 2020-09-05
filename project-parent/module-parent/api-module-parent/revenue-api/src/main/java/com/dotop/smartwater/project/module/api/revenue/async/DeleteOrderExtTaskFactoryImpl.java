package com.dotop.smartwater.project.module.api.revenue.async;

import com.dotop.smartwater.dependence.cache.StringValueCache;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.utils.VerificationUtils;
import com.dotop.smartwater.project.module.api.mode.IModeFactory;
import com.dotop.smartwater.project.module.api.revenue.IDeleteOrderExtTaskFactory;
import com.dotop.smartwater.project.module.core.auth.vo.SettlementVo;
import com.dotop.smartwater.project.module.core.water.bo.OwnerBo;
import com.dotop.smartwater.project.module.core.water.bo.PaymentTradeOrderBo;
import com.dotop.smartwater.project.module.core.water.bo.SmsTemplateBo;
import com.dotop.smartwater.project.module.core.water.constants.*;
import com.dotop.smartwater.project.module.core.water.form.customize.DownlinkForm;
import com.dotop.smartwater.project.module.core.water.utils.CalUtil;
import com.dotop.smartwater.project.module.core.water.vo.DeviceVo;
import com.dotop.smartwater.project.module.core.water.vo.OwnerVo;
import com.dotop.smartwater.project.module.core.water.vo.PaymentTradeOrderVo;
import com.dotop.smartwater.project.module.core.water.vo.SmsTemplateVo;
import com.dotop.smartwater.project.module.service.device.IDeviceService;
import com.dotop.smartwater.project.module.service.revenue.IOrderService;
import com.dotop.smartwater.project.module.service.revenue.IOwnerService;
import com.dotop.smartwater.project.module.service.revenue.IPaymentTradeOrderService;
import com.dotop.smartwater.project.module.service.tool.ISmsTemplateService;
import com.dotop.smartwater.project.module.service.tool.ISmsToolService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * 账单有关的异步任务 （到时要把 DeleteOrderExtTask 改名）
 *

 */
@Component
public class DeleteOrderExtTaskFactoryImpl implements IDeleteOrderExtTaskFactory {

    private final static Logger LOG = LogManager.getLogger(DeleteOrderExtTaskFactoryImpl.class);

    @Autowired
    private IOrderService iOrderService;

    @Autowired
    private IOwnerService iOwnerService;

    @Autowired
    private IModeFactory iModeFactory;

    @Autowired
    private IPaymentTradeOrderService iPaymentTradeOrderService;

    @Autowired
    private IDeviceService iDeviceService;

    @Autowired
    private ISmsToolService iSmsToolService;

    @Autowired
    private ISmsTemplateService iSmsTemplateService;

    @Autowired
    private StringValueCache svc;

    @Async("calExecutor")
    @Override
    public void clearPreviewByCondition(Object obj) {
        long time = System.currentTimeMillis();
        LOG.info("[{}]异步耗时计算:", "删除Order_Ext数据开始");
        iOrderService.clearOrderPreviewExtByCondition(obj);
        LOG.info("[{}]异步耗时:{}", "删除Order_Ext数据ByCondition", System.currentTimeMillis() - time);
    }

    @Override
    public void sendArrearsCloseNotice(String ownerId, SettlementVo settlementVo) throws FrameworkRuntimeException {
        if(ownerId == null){
            return;
        }

        String flag = svc.get(WaterConstants.WATER_CLOSE_FLAG + ownerId);
        if (flag != null) {
            LOG.info("业主ID [" + ownerId + "]已经因为欠费下发关阀命令");
            return;
        }

        if (settlementVo == null || 0 == settlementVo.getArrearsValve()
                || settlementVo.getArrearsBalance() == null
                || settlementVo.getArrearsBalance().doubleValue() <= 0) {
            LOG.info("[sendArrearsCloseNotice] :系统没有设置欠费关阀 --> " + ownerId);
            return;
        }

        // 统计未缴账单数量和欠费金额
        PaymentTradeOrderBo paymentTradeOrderBo = new PaymentTradeOrderBo();
        paymentTradeOrderBo.setStatus(PaymentConstants.PATMENT_STATUS_NOPAY);
        paymentTradeOrderBo.setBusinessId(PaymentConstants.BUSSINESS_TYPE_WATERFEE);
        paymentTradeOrderBo.setUserid(ownerId);
        List<PaymentTradeOrderVo> list = iPaymentTradeOrderService.list(paymentTradeOrderBo);

        if (list.size() == 0) {
            return;
        }

        double arrears = 0.0;
        for (PaymentTradeOrderVo noPayOrder : list) {
            arrears = CalUtil.add(CalUtil.add(noPayOrder.getPenalty(), noPayOrder.getAmount()).doubleValue(), arrears);
        }

        //欠费不到设置的金额,不用关阀
        if (CalUtil.sub(settlementVo.getArrearsBalance().doubleValue(), arrears) > 0) {
            LOG.info("[sendArrearsCloseNotice] :欠费不到设置的金额,不用关阀 --> " + ownerId);
            return;
        }

        OwnerBo ownerBo = new OwnerBo();
        ownerBo.setOwnerid(ownerId);
        OwnerVo ownerVo = iOwnerService.findByOwnerId(ownerBo);
        if (ownerVo == null || ownerVo.getDevno() == null) {
            return;
        }

        /** 发送关阀命令 **/
        DeviceVo deviceVo = iDeviceService.findByDevNo(ownerVo.getDevno());
        if (deviceVo == null || DeviceCode.DEVICE_TYPE_TRADITIONAL.equals(deviceVo.getTypeid())
                || deviceVo.getTaptype() == null || deviceVo.getTaptype() == 0
                || deviceVo.getTapstatus() == null || deviceVo.getTapstatus() == 0) {
            LOG.info("[sendArrearsCloseNotice] :水表不符合下发条件 --> " + deviceVo.getDevno());
            return;
        }

        String mode = DictionaryCode.getChildValue(deviceVo.getMode());
        if (mode == null) {
            LOG.error("[sendArrearsCloseNotice] :不支持的通讯方式 --> " + deviceVo.getDevno());
            return;
        }
        //获取拥有的下发指令集
        String commandData = ModeConstants.ModeMap.get(mode);
        if (commandData == null) {
            LOG.error("[sendArrearsCloseNotice] :该通讯方式没配置关阀下发指令 --> " + deviceVo.getDevno());
            return;
        }
        String[] arr = commandData.split(",");
        if (arr.length == 0) {
            LOG.error("[sendArrearsCloseNotice] :该通讯方式没配置下发指令 --> " + deviceVo.getDevno());
            return;
        }
        ArrayList<String> arrList = new ArrayList<>(Arrays.asList(arr));
        int command = TxCode.CloseCommand;
        if (!arrList.contains(String.valueOf(command))) {
            LOG.error("[sendArrearsCloseNotice] :该通讯方式没配置该下发指令 --> " + deviceVo.getDevno());
            return;
        }
        try {
            Map<String, String> result;
            switch (mode) {
                case ModeConstants.DX_LORA:
                case ModeConstants.DX_NB_DX:
                case ModeConstants.DX_NB_YD:
                case ModeConstants.DX_NB_LT:
                    result = iModeFactory.dxDeviceTx(deviceVo, command, mode, new DownlinkForm());
                    break;
                case ModeConstants.HAT_NB:
                case ModeConstants.HAT_LORA:
                    result = iModeFactory.hatDeviceTx(deviceVo, command, mode, null);
                    break;
                //没有接入别的协议
                default:
                    LOG.error("[sendArrearsCloseNotice] :没接入的通讯方式 --> " + deviceVo.getDevno());
                    return;
            }

            // 下发成功
            if (ResultCode.Success.equals(result.get(ModeConstants.RESULT_CODE))) {

                svc.set(WaterConstants.WATER_CLOSE_FLAG + ownerId, "1");
                LOG.info("[sendArrearsCloseNotice] :下发成功 --> " + deviceVo.getDevno());

                //发短信
                if (settlementVo.getArrearsValveSms() == null || 0 == settlementVo.getArrearsValveSms()
                        || settlementVo.getValveSmsTemplate() == null) {
                    return;
                }

                SmsTemplateBo smsTemplateBo = new SmsTemplateBo();
                smsTemplateBo.setId(settlementVo.getValveSmsTemplate());
                SmsTemplateVo smsTemplateVo = iSmsTemplateService.getSmsTemplateVo(smsTemplateBo);
                if (smsTemplateVo == null) {
                    return;
                }

                if (ownerVo.getUserphone() == null
                        || !VerificationUtils.regex(ownerVo.getUserphone(), VerificationUtils.REG_PHONE)) {
                    return;
                }

                String[] phoneNumbers = {ownerVo.getUserphone()};
                Map<String, String> params = new HashMap<>(2);
                params.put("money", settlementVo.getArrearsBalance().toString());
                params.put("name", ownerVo.getUsername());
                iSmsToolService.sendSMS(deviceVo.getEnterpriseid(), smsTemplateVo.getSmstype(), phoneNumbers, params, null);

                LOG.info("[sendArrearsCloseNotice] :发短信成功 --> " + deviceVo.getDevno());

            } else {
                LOG.error("[sendArrearsCloseNotice] :下发关阀命令失败 --> " + deviceVo.getDevno());
            }
        } catch (Exception ex) {
            LOG.error("Exception ---[sendArrearsCloseNotice] :" + ex.getMessage() + " --> " + deviceVo.getDevno());
        }
    }

    @Override
    public void checkDeviceOpen(String ownerId) throws FrameworkRuntimeException {
        if(ownerId == null){
            return;
        }
        // 统计未缴账单数量和欠费金额
        PaymentTradeOrderBo paymentTradeOrderBo = new PaymentTradeOrderBo();
        paymentTradeOrderBo.setStatus(PaymentConstants.PATMENT_STATUS_NOPAY);
        paymentTradeOrderBo.setBusinessId(PaymentConstants.BUSSINESS_TYPE_WATERFEE);
        paymentTradeOrderBo.setUserid(ownerId);
        List<PaymentTradeOrderVo> list = iPaymentTradeOrderService.list(paymentTradeOrderBo);

        // 不欠钱了，检查水表阀门是否关闭，关闭了则重新打开
        if (list.size() == 0) {
            svc.del(WaterConstants.WATER_CLOSE_FLAG + ownerId);

            OwnerBo ownerBo = new OwnerBo();
            ownerBo.setOwnerid(ownerId);
            OwnerVo ownerVo = iOwnerService.findByOwnerId(ownerBo);
            if (ownerVo == null || ownerVo.getDevno() == null) {
                return;
            }

            /** 发送开阀命令 **/
            DeviceVo deviceVo = iDeviceService.findByDevNo(ownerVo.getDevno());
            if (deviceVo == null || DeviceCode.DEVICE_TYPE_TRADITIONAL.equals(deviceVo.getTypeid())
                    || deviceVo.getTaptype() == null || deviceVo.getTaptype() == 0
                    || deviceVo.getTapstatus() == 1) {
                LOG.info("[checkDeviceOpen] :水表不符合下发条件 --> " + deviceVo.getDevno());
                return;
            }

            String mode = DictionaryCode.getChildValue(deviceVo.getMode());
            if (mode == null) {
                LOG.error("[checkDeviceOpen] :不支持的通讯方式 --> " + deviceVo.getDevno());
                return;
            }
            //获取拥有的下发指令集
            String commandData = ModeConstants.ModeMap.get(mode);
            if (commandData == null) {
                LOG.error("[checkDeviceOpen] :该通讯方式没配置开阀下发指令 --> " + deviceVo.getDevno());
                return;
            }
            String[] arr = commandData.split(",");
            if (arr.length == 0) {
                LOG.error("[checkDeviceOpen] :该通讯方式没配置下发指令 --> " + deviceVo.getDevno());
                return;
            }
            ArrayList<String> arrList = new ArrayList<>(Arrays.asList(arr));
            int command = TxCode.OpenCommand;
            if (!arrList.contains(String.valueOf(command))) {
                LOG.error("[checkDeviceOpen] :该通讯方式没配置该下发指令 --> " + deviceVo.getDevno());
                return;
            }
            try {
                Map<String, String> result;
                switch (mode) {
                    case ModeConstants.DX_LORA:
                    case ModeConstants.DX_NB_DX:
                    case ModeConstants.DX_NB_YD:
                    case ModeConstants.DX_NB_LT:
                        result = iModeFactory.dxDeviceTx(deviceVo, command, mode, new DownlinkForm());
                        break;
                    case ModeConstants.HAT_NB:
                    case ModeConstants.HAT_LORA:
                        result = iModeFactory.hatDeviceTx(deviceVo, command, mode, null);
                        break;
                    //没有接入别的协议
                    default:
                        LOG.error("[checkDeviceOpen] :没接入的通讯方式 --> " + deviceVo.getDevno());
                        return;
                }

                // 下发成功
                if (ResultCode.Success.equals(result.get(ModeConstants.RESULT_CODE))) {
                    LOG.info("[checkDeviceOpen] :下发开阀命令成功 --> " + deviceVo.getDevno());
                } else {
                    LOG.error("[checkDeviceOpen] :下发开阀命令失败 --> " + deviceVo.getDevno());
                }
            } catch (Exception ex) {
                LOG.error("Exception ---[checkDeviceOpen] :" + ex.getMessage() + " --> " + deviceVo.getDevno());
            }
        }
    }
}
