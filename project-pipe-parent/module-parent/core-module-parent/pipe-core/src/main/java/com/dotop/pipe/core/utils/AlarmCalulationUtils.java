package com.dotop.pipe.core.utils;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.dotop.pipe.core.vo.alarm.AlarmSettingVo;
import com.dotop.pipe.core.vo.device.DevicePropertyVo;
import com.dotop.pipe.core.vo.device.DeviceVo;
import org.apache.commons.lang3.StringUtils;

public class AlarmCalulationUtils {

    public static boolean alarmCalulation(AlarmSettingVo alarmSettingVo, String currenValue) {
        BigDecimal value = new BigDecimal(currenValue);
        // 判断值是否在规定的范围之内 如果不在 则添加警告
        BigDecimal maxVal = new BigDecimal(alarmSettingVo.getMaxValue());
        BigDecimal minVal = new BigDecimal(alarmSettingVo.getMinValue());
        String maxCompare = alarmSettingVo.getMaxCompare();
        String minCompare = alarmSettingVo.getMinCompare();
        // 预警值设置的是异常值范围 校验时需 分情况
        // -1表示小于，0是等于，1是大于
        //  maxCompare : <
        if (maxCompare.equals("<")) {
            if (minCompare.equals("<")) {
                // 如果两个符号都是小于号 取并集  即
                if (value.compareTo(maxVal) < 0 || value.compareTo(minVal) < 0) {
                    return true;
                }
            } else {
                //  minCompare : >
                if (maxVal.compareTo(minVal) < 0) {
                    //   maxVal < minVal  取并集  value < maxVal  || minVal < value
                    if (value.compareTo(maxVal) < 0 || value.compareTo(minVal) > 0) {
                        return true;
                    }
                } else {
                    //   分情况: maxVal > minVal  取交集    minVal < value && value < maxVal
                    if (value.compareTo(maxVal) < 0 && value.compareTo(minVal) > 0) {
                        return true;
                    }
                }
            }
        } else { // maxCompare : >
            if (minCompare.equals(">")) {
                // 如果两个符号都是大于号 取并集  即
                if (value.compareTo(maxVal) > 0 || value.compareTo(minVal) > 0) {
                    return true;
                }
            } else {
                //  minCompare : <
                //
                if (maxVal.compareTo(minVal) < 0) {
                    //   maxVal < X < minVal
                    if (value.compareTo(maxVal) > 0 && value.compareTo(minVal) < 0) {
                        return true;
                    }
                } else {
                    //    X < minVal ||  maxVal < X
                    if (value.compareTo(maxVal) > 0 || value.compareTo(minVal) < 0) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * 计算接近预警值得数据
     *
     * @param alarmSetting
     * @param devicePropertyVo
     * @return
     */
    public static boolean getAlarmCalulation(List<AlarmSettingVo> alarmSetting, DevicePropertyVo devicePropertyVo, String type) {

        boolean flag = false;

        for (int i = 0; i < alarmSetting.size(); i++) {
            AlarmSettingVo alarmSettingVo = alarmSetting.get(i);
            String tag = alarmSettingVo.getField();
            String currenValue = null;
            try {
                Field field = devicePropertyVo.getClass().getDeclaredField(tag);
                // 允许访问私有属性
                field.setAccessible(true);
                if (field == null) {
                    continue;
                }
                Object object = field.get(devicePropertyVo);
                if (object == null) {
                    continue;
                }
                currenValue = object.toString();
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }

            if (currenValue != null) {
                if ("near".equals(type)) {
                    if (!alarmCalulation(alarmSettingVo, currenValue)) {  // 所有条件都不报警时 才能满足
                        if (nearAlarmCalulation(alarmSettingVo, currenValue)) {
                            flag = true;
                        }
                    } else { // 只要有一个条件是报警的 就返回false
                        flag = false;
                        return false;
                    }
                } else {
                    if (alarmCalulation(alarmSettingVo, currenValue)) {
                        return true;
                    }
                }
            }
        }
        if ("near".equals(type)) {
            return flag;
        } else {
            return false;
        }
    }

    public static boolean nearAlarmCalulation(AlarmSettingVo alarmSettingVo, String currenValue) {
        BigDecimal value = new BigDecimal(currenValue);
        BigDecimal maxVal = new BigDecimal(alarmSettingVo.getMaxValue());
        BigDecimal minVal = new BigDecimal(alarmSettingVo.getMinValue());
        // BigDecimal coefficientMaxVal = value.multiply(new BigDecimal(1.2));
        // BigDecimal coefficientMinVal = value.multiply(new BigDecimal(0.8));
        // bigDecimal1.multiply(bigDecimal2)
        String maxCompare = alarmSettingVo.getMaxCompare();
        String minCompare = alarmSettingVo.getMinCompare();
        // 预警值设置的是异常值范围 校验时需 分情况
        // -1表示小于，0是等于，1是大于
        //  maxCompare : <
        if (maxCompare.equals("<")) {
            if (minCompare.equals("<")) {
                // 如果两个符号都是小于号 找到较小的那个值
                if (maxVal.compareTo(minVal) > 0) {
                    if (value.compareTo(maxVal) >= 0 && value.compareTo(maxVal.multiply(new BigDecimal(1.2))) <= 0) {  // 大于最小值 并且 小于 1.2
                        return true;
                    }
                } else {
                    if (value.compareTo(minVal) >= 0 && value.compareTo(minVal.multiply(new BigDecimal(1.2))) <= 0) {  // 大于最小值 并且 小于 1.2
                        return true;
                    }
                }

            } else {
                //  minCompare : >
                if (maxVal.compareTo(minVal) < 0) {
                    //   maxVal < minVal  取并集  value < maxVal  || minVal <
                    if ((value.compareTo(maxVal) >= 0 && value.compareTo(maxVal.multiply(new BigDecimal(1.2))) <= 0) // 在最小值 到1.2 之间
                            || (value.compareTo(minVal) <= 0 && value.compareTo(minVal.multiply(new BigDecimal(0.8))) >= 0)) { // 在最大值 到0.8 之间
                        return true;
                    }
                } else {
                    //   分情况: maxVal > minVal  取交集    minVal < value && value < maxVal
                    if ((value.compareTo(maxVal) >= 0 && value.compareTo(maxVal.multiply(new BigDecimal(1.2))) <= 0) // 大于最大值 并且小于*1.2
                            || (value.compareTo(minVal) <= 0 && value.compareTo(minVal.multiply(new BigDecimal(0.8))) >= 0)) {  // 小于最小值 并且大于*0.8
                        return true;
                    }
                }
            }
        } else { // maxCompare : >
            if (minCompare.equals(">")) {
                // 如果两个符号都是大于号
                if (maxVal.compareTo(minVal) > 0) {
                    if (value.compareTo(minVal) <= 0 && value.compareTo(minVal.multiply(new BigDecimal(0.8))) >= 0) {  // 大于最小值 并且 小于 1.2
                        return true;
                    }
                } else {
                    if (value.compareTo(maxVal) <= 0 && value.compareTo(maxVal.multiply(new BigDecimal(0.8))) >= 0) {  // 大于最小值 并且 小于 1.2
                        return true;
                    }
                }
            } else {
                //  minCompare : <
                if (maxVal.compareTo(minVal) < 0) {
                    //   maxVal < X < minVal
                    if ((value.compareTo(maxVal) <= 0 && value.compareTo(maxVal.multiply(new BigDecimal(0.8))) >= 0)
                            || (value.compareTo(minVal) >= 0 && value.compareTo(minVal.multiply(new BigDecimal(1.2))) <= 0)) {
                        return true;
                    }
                } else {
                    //    X < minVal ||  maxVal < X
                    if ((value.compareTo(maxVal) <= 0 && value.compareTo(maxVal.multiply(new BigDecimal(0.8))) >= 0)
                            || (value.compareTo(minVal) >= 0 && value.compareTo(minVal.multiply(new BigDecimal(1.2))) <= 0)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static List<AlarmSettingVo> alarmChangeToSettingList(DeviceVo deviceVo) {

        List<AlarmSettingVo> resultList = new ArrayList<>();

        String properties = deviceVo.getAlarmProperties();
        if (StringUtils.isBlank(properties)) {
            return resultList;
        }
        List<AlarmSettingVo> list = com.alibaba.fastjson.JSON.parseArray(properties, AlarmSettingVo.class);
        // JSONObject rddata = com.alibaba.fastjson.JSON.parseObject(properties);
        DevicePropertyVo devicePropertyVo = deviceVo.getDeviceProperty();

        // 流速
        AlarmSettingVo alarmSettingVo1 = list.get(0);
        if (StringUtils.isNotBlank(alarmSettingVo1.getMaxValue())
                && StringUtils.isNotBlank(alarmSettingVo1.getMinValue())
                && StringUtils.isNotBlank(devicePropertyVo.getFlwRate())
                ) {
            BigDecimal maxVal = new BigDecimal(alarmSettingVo1.getMaxValue());
            BigDecimal minVal = new BigDecimal(alarmSettingVo1.getMinValue());
            BigDecimal val = new BigDecimal(devicePropertyVo.getFlwRate());
            // 不在预测的范围 即 大于较大值 小于较小值
            // 较大值
            alarmSettingVo1.setMaxValue(val.multiply(maxVal).divide(new BigDecimal(100)).toString());
            alarmSettingVo1.setMaxCompare(">");
            // 较小值
            alarmSettingVo1.setMinValue(val.multiply(minVal).divide(new BigDecimal(100)).toString());
            alarmSettingVo1.setMinCompare("<");
            resultList.add(alarmSettingVo1);
        }

        // 压力
        AlarmSettingVo alarmSettingVo2 = list.get(1);
        if (StringUtils.isNotBlank(alarmSettingVo2.getMaxValue())
                && StringUtils.isNotBlank(alarmSettingVo2.getMinValue())
                && StringUtils.isNotBlank(devicePropertyVo.getPressureValue())
                ) {
            BigDecimal maxVal = new BigDecimal(alarmSettingVo2.getMaxValue());
            BigDecimal minVal = new BigDecimal(alarmSettingVo2.getMinValue());
            BigDecimal val = new BigDecimal(devicePropertyVo.getPressureValue());
            // 不在预测的范围 即 大于较大值 小于较小值
            // 较大值
            alarmSettingVo2.setMaxValue(val.multiply(maxVal).divide(new BigDecimal(100)).toString());
            alarmSettingVo2.setMaxCompare(">");
            // 较小值
            alarmSettingVo2.setMinValue(val.multiply(minVal).divide(new BigDecimal(100)).toString());
            alarmSettingVo2.setMinCompare("<");
            resultList.add(alarmSettingVo2);
        }
        return resultList;
    }
}
