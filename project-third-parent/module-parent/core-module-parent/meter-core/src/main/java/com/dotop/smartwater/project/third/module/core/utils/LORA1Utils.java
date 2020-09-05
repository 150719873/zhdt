package com.dotop.smartwater.project.third.module.core.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.dotop.smartwater.project.third.module.core.constants.DockingConstants;
import com.dotop.smartwater.project.third.module.core.utils.webservice.WebServiceUtils;
import com.dotop.smartwater.project.third.module.core.water.form.CommandForm;
import com.dotop.smartwater.project.third.module.core.water.form.DeviceForm;
import com.dotop.smartwater.project.third.module.core.water.form.DeviceUplinkForm;
import com.dotop.smartwater.project.third.module.core.water.form.DockingForm;
import com.dotop.smartwater.project.third.module.core.water.vo.DeviceVo;
import com.dotop.smartwater.project.third.module.core.water.vo.DockingVo;
import com.dotop.smartwater.project.third.module.core.water.vo.OwnerVo;
import com.dotop.smartwater.dependence.core.exception.BaseExceptionConstants;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.utils.BeanUtils;
import com.dotop.smartwater.dependence.core.utils.DateUtils;
import com.dotop.smartwater.dependence.core.utils.MD5Util;
import com.dotop.smartwater.dependence.core.utils.UuidUtils;
import com.dotop.smartwater.project.module.core.water.constants.TxCode;
import com.dotop.smartwater.project.third.module.core.third.lora.vo.MeterChangeVo;
import com.dotop.smartwater.project.third.module.core.third.lora.vo.MeterInfoVo;
import com.dotop.smartwater.project.third.module.core.third.lora.vo.MeterValueVo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 *
 */
public class LORA1Utils {

    private static final Logger LOGGER = LogManager.getLogger(LORA1Utils.class);

    private static final Integer PAGESIZE = 1000;
    public static final Integer NORMAL = 1;
    public static final Integer ADD = 2;
    public static final Integer EDIT = 3;
    public static final Integer DELETE = 4;
    public static final Integer ALL = 5;
    public static final Integer OPT_TYPE_ADD = 1;
    public static final Integer OPT_TYPE_EDIT = 2;
    public static final Integer OPT_TYPE_DELETE = 3;
    /**
     * 是否带阀，1：带阀， 0：不带阀
     */
    public static final Integer HAVE_VALVE = 1;
    public static final Integer NOT_VALVE = 0;
    /**
     * 水务系统修改数据是否会同步到第三方
     */
    public static final boolean SYNC = false;
    /**
     * 抄表命令下发回调时，睡眠多少毫秒后再去抄表
     */
    public static final Integer SLEEP = 3000;

    /**
     * 获取水表号，再获取水表详情
     *
     * @param dockingFrom
     * @param detailAddVo
     * @param loginDocking
     * @param type
     * @return
     */
    public static List<DeviceVo> getMeterInfo(DockingForm dockingFrom, DockingVo detailAddVo, DockingForm loginDocking, Integer type) {
        Object[] objects = new Object[3];
        objects[0] = loginDocking.getUsername();
        objects[1] = loginDocking.getPassword();
        objects[2] = type;
        Object[] result = WebServiceUtils.client(dockingFrom.getHost(), dockingFrom.getUrl(), Arrays.asList(objects));
        List<MeterInfoVo> meterInfoVos = JSONObject.parseObject(result[0].toString(), new TypeReference<List<MeterInfoVo>>() {
        });
        List<DeviceVo> deviceVos = new ArrayList<>();
        meterInfoVos.forEach(meterInfoVo -> {
            // 获取水表详情
            MeterInfoVo detailInfo = LORA1Utils.getDetailInfo(BeanUtils.copy(detailAddVo, DockingForm.class), loginDocking, meterInfoVo.getMeterId());
            DeviceVo deviceVo = new DeviceVo();
            deviceVo.setId(UuidUtils.getUuid());
            deviceVo.setDevid(UuidUtils.getUuid());
            deviceVo.setDevno(detailInfo.getMeterNo());
            deviceVo.setWater(Double.valueOf(detailInfo.getShowValue().replace(",", "")));
            deviceVo.setJson(detailInfo.toString());
            // 没有deveui，用编号md5作为deveui
            deviceVo.setDeveui(MD5Util.encode(detailInfo.getMeterNo()));
            deviceVo.setThirdid(meterInfoVo.getMeterId().toString());
            deviceVo.setDevaddr(meterInfoVo.getMeterAddr());
            deviceVo.setFactory(loginDocking.getFactory());
            deviceVo.setProductName(loginDocking.getProductName());
            deviceVo.setMode(loginDocking.getMode());
            deviceVo.setEnterpriseid(loginDocking.getEnterpriseid());
            deviceVo.setBeginvalue(Double.valueOf(detailInfo.getBeginValue().replace(",", "")));
            deviceVo.setTaptype(HAVE_VALVE);
            deviceVo.setTapstatus(1);
            //设置业主信息
            OwnerVo ownerVo = new OwnerVo();
            ownerVo.setEnterpriseid(loginDocking.getEnterpriseid());
            ownerVo.setId(UuidUtils.getUuid());
            ownerVo.setOwnerid(UuidUtils.getUuid());
            ownerVo.setThirdid(meterInfoVo.getMeterId().toString());
            ownerVo.setDevid(deviceVo.getDevid());
            ownerVo.setUserno(detailInfo.getUserNo());
            ownerVo.setUsername(detailInfo.getUserName());
            ownerVo.setUseraddr(detailInfo.getUserAddr());
            ownerVo.setUserphone(detailInfo.getMobileNum());
            ownerVo.setRemark(detailInfo.getRemark());
            deviceVo.setOwner(ownerVo);
            deviceVos.add(deviceVo);
        });
        return deviceVos;
    }

    /**
     * 获取水表详情
     *
     * @param dockingFrom
     * @param loginDocking
     * @param meterId
     * @return
     */
    private static MeterInfoVo getDetailInfo(DockingForm dockingFrom, DockingForm loginDocking, Integer meterId) {
        Object[] objects = new Object[3];
        objects[0] = meterId;
        objects[1] = loginDocking.getUsername();
        objects[2] = loginDocking.getPassword();
        Object[] result = WebServiceUtils.client(dockingFrom.getHost(), dockingFrom.getUrl(), Arrays.asList(objects));
        JSONArray jsonArray = JSONObject.parseArray(result[0].toString());
        for (Object o : jsonArray) {
            MeterInfoVo copy = BeanUtils.copy(o, MeterInfoVo.class);
            if (meterId.toString().equals(copy.getMeterNo())) {
                return copy;
            }
        }
        throw new FrameworkRuntimeException(BaseExceptionConstants.UNDEFINED_ERROR, "查询详情失败");
    }

    /**
     * 获取换表记录
     *
     * @param dockingForm
     * @param loginDocking
     * @return
     */
    public static List<MeterChangeVo> getMeterChange(DockingForm dockingForm, DockingForm loginDocking) {
        Date last = new Date();
        Date start = DateUtils.month(last, -2);
        Object[] objects = new Object[5];
        objects[0] = loginDocking.getUsername();
        objects[1] = loginDocking.getPassword();
        objects[2] = 2;
        objects[3] = DateUtils.format(start, "yyyyMMdd");
        objects[4] = DateUtils.format(last, "yyyyMMdd");
        Object[] result = WebServiceUtils.client(dockingForm.getHost(), dockingForm.getUrl(), Arrays.asList(objects));
        return JSONObject.parseObject(result[0].toString(), new TypeReference<List<MeterChangeVo>>() {
        });
    }

    /**
     * 获取抄表历史，若本月无记录，则取上个月最后一天
     *
     * @param dockingForm
     * @param loginDocking
     * @param meterId
     * @return
     */
    public static List<DeviceUplinkForm> getMeterValue(DockingForm dockingForm, DockingForm loginDocking, Integer meterId) {
        Date now = new Date();
        Object[] objects = new Object[4];
        objects[0] = DateUtils.format(now, "yyyyMMdd");
        objects[1] = meterId;
        objects[2] = loginDocking.getUsername();
        objects[3] = loginDocking.getPassword();
        Object[] result = WebServiceUtils.client(dockingForm.getHost(), dockingForm.getUrl(), Arrays.asList(objects));
        List<MeterValueVo> meterValueVos = JSONObject.parseObject(result[0].toString(), new TypeReference<List<MeterValueVo>>() {
        });
        if (meterValueVos.isEmpty()) {
            String ym = DateUtils.format(now, "yyyy-MM") + "-01";
            Date temp = DateUtils.parse(ym, "yyyy-MM-dd");
            Date day = DateUtils.day(temp, -1);
            objects[0] = DateUtils.format(day, "yyyyMMdd");
            result = WebServiceUtils.client(dockingForm.getHost(), dockingForm.getUrl(), Arrays.asList(objects));
            meterValueVos = JSONObject.parseObject(result[0].toString(), new TypeReference<List<MeterValueVo>>() {
            });
        }
        List<DeviceUplinkForm> deviceUplinkForms = new ArrayList<>();
        meterValueVos.forEach(meterValueVo -> {
            DeviceUplinkForm deviceUplinkFrom = new DeviceUplinkForm();
            deviceUplinkFrom.setId(UuidUtils.getUuid());
            deviceUplinkFrom.setDevno(meterValueVo.getMeterNo());
            deviceUplinkFrom.setDeveui(MD5Util.encode(meterValueVo.getMeterNo()));
            deviceUplinkFrom.setDevid("");
            deviceUplinkFrom.setThirdid(meterValueVo.getMeterNo());
            deviceUplinkFrom.setUplinkDate(DateUtils.parseDatetime(DateUtils.formatDatetime(meterValueVo.getReadTime())));
            deviceUplinkFrom.setEnterpriseid(loginDocking.getEnterpriseid());
            deviceUplinkFrom.setJson(meterValueVo.toString());
            deviceUplinkFrom.setWater(Double.valueOf(meterValueVo.getMeterValue().replace(",", "")));
            deviceUplinkFrom.setAgreement("");
            deviceUplinkForms.add(deviceUplinkFrom);
        });
        return deviceUplinkForms;
    }


    /**
     * 下发命令
     *
     * @param dockingForm
     * @param loginDocking
     * @param commandForm
     * @return
     */
    public static String setMeterValve(DockingForm dockingForm, DockingForm loginDocking, CommandForm commandForm) {
        Integer command;
        if (TxCode.CloseCommand == commandForm.getCommand()) {
            command = 1;
        } else if (TxCode.OpenCommand == commandForm.getCommand()) {
            command = 2;
        } else if (TxCode.GetWaterCommand == commandForm.getCommand()) {
            command = 3;
        } else {
            throw new FrameworkRuntimeException(BaseExceptionConstants.UNDEFINED_ERROR, "不存在此命令");
        }
        Object[] objects = new Object[5];
        objects[0] = Integer.parseInt(commandForm.getDevno());
        objects[1] = command;
        objects[2] = loginDocking.getUsername();
        objects[3] = loginDocking.getPassword();
        objects[4] = "2";
        Object[] result = WebServiceUtils.client(dockingForm.getHost(), dockingForm.getUrl(), Arrays.asList(objects));
        if ("Success".equals(result[0].toString())) {
            return DockingConstants.RESULT_SUCCESS;
        } else {
            return DockingConstants.RESULT_FAIL;
        }
    }

    /**
     * 修水表改业主信息
     *
     * @param dockingForm
     * @param loginDocking
     * @param deviceForm
     * @param type
     * @return
     */
    public static boolean setUserInfo(DockingForm dockingForm, DockingForm loginDocking, DeviceForm deviceForm, Integer type) {
        Object[] objects = new Object[8];
        objects[0] = Integer.parseInt(deviceForm.getDevno());
        objects[1] = deviceForm.getOwner().getUserno();
        objects[2] = deviceForm.getOwner().getUsername();
        objects[3] = deviceForm.getOwner().getUseraddr();
        objects[4] = deviceForm.getOwner().getUserphone();
        objects[5] = type;
        objects[6] = loginDocking.getUsername();
        objects[7] = loginDocking.getPassword();
        Object[] result = WebServiceUtils.client(dockingForm.getHost(), dockingForm.getUrl(), Arrays.asList(objects));
        return "Success".equals(result[0].toString());
    }
}
