package com.dotop.smartwater.project.module.core.auth.enums;
import java.util.ArrayList;
import java.util.List;

import com.dotop.smartwater.project.module.core.auth.sms.TemplateType;

/**
 *
 * @ClassName: SmsEnum
 * @Description: TODO

 * @date 2018年9月4日 下午2:17:04
 *
 */
public enum SmsEnum {

    owner_open(1, "业主开户"),
    owner_close(2, "业主销户"),
    owner_change_meter(3, "业主换表"),
    owner_transfer_ownership(4, "业主过户"),
    pay_fee(5, "缴费成功"),
    wrong_account(6, "错账处理"),
    pay_bill(7, "生成账单"),
    recharge(8, "充值成功"),
    dunning(9, "催缴"),
    installwork(10 , "报装工单"),
    repairwork(11 , "报修工单"),
    inspectingwork(12 , "巡检工单"),
    product_storage(13 , "产品入库"),
    product_out_storage(14 , "产品出库"),
	device_warning(15 , "设备预警"),
	balance_notice(16 , "余额不足通知"),
    close_valve_notice(17 , "欠费关阀通知");

    private int intValue;

    private String text;

    SmsEnum(int intValue, String text) {
        this.intValue = intValue;
        this.text = text;
    }

    public int intValue() {
        return intValue;
    }

    public String getText() {
        return text;
    }

    public static String getText(Integer intValue) {
        if(intValue == null){
            return "";
        }

        for (SmsEnum enums : SmsEnum.values()) {
            if (enums.intValue == intValue) {
                return enums.getText();
            }
        }
        return "";
    }

    public static List<TemplateType> listAll(){
        List<TemplateType>  list = new ArrayList<TemplateType>();

        for (SmsEnum enums : SmsEnum.values()) {
            TemplateType templateType = new TemplateType();
            templateType.setId(enums.intValue);
            templateType.setName(enums.getText());

            list.add(templateType);
        }

        return list;
    }

    public static List<Integer> listKey(){

        List<Integer> list = new ArrayList<Integer>();
        for (SmsEnum enums : SmsEnum.values()) {
            list.add(enums.intValue);
        }

        return list;
    }
}
