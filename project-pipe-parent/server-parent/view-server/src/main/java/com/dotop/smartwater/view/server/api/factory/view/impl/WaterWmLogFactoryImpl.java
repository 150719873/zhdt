package com.dotop.smartwater.view.server.api.factory.view.impl;

import com.dotop.pipe.auth.cas.web.IAuthCasWeb;
import com.dotop.pipe.auth.core.vo.auth.LoginCas;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.dependence.core.utils.DateUtils;
import com.dotop.smartwater.dependence.core.utils.UuidUtils;
import com.dotop.smartwater.view.server.api.factory.view.IWaterWmFactory;
import com.dotop.smartwater.view.server.core.device.form.WaterWmLogForm;
import com.dotop.smartwater.view.server.core.device.vo.WaterWmLogVo;
import com.dotop.smartwater.view.server.service.waterwmlog.IWaterWmLogService;
import com.dotop.smartwater.view.server.utils.CalculationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

import static com.dotop.smartwater.dependence.core.common.RootModel.NOT_DEL;

@Component
public class WaterWmLogFactoryImpl implements IWaterWmFactory {

    @Autowired
    private IWaterWmLogService iWaterWmLogService;

    @Autowired
    private IAuthCasWeb iAuthCasApi;

    @Override
    public String updateTask(WaterWmLogForm waterWmLogFormParam) throws FrameworkRuntimeException {
        // 取样点
        List<String> addressList = Arrays.asList("沉淀池", "反应池", "协管滤池", "溢流池", "清水池", "沉淀池", "加压泵池", "进水口", "出水口");
        Random random = new Random();
        Date curr = new Date();
        List<WaterWmLogForm> list = new ArrayList<>();
        for (String str : addressList) {
            // 字段
            WaterWmLogForm waterWmLogForm = new WaterWmLogForm();
            waterWmLogForm.setId(UuidUtils.getUuid());
            waterWmLogForm.setAddress(str);
            Double val = (0.1 + random.nextDouble() * (0.7));
            waterWmLogForm.setTurbid(val.toString());
            waterWmLogForm.setChroma(val > 0.6 ? "浑浊" : "无色");
            waterWmLogForm.setOdor(val > 0.7 ? "异臭" : "无异味");
            waterWmLogForm.setAlarm(val > 0.6 ? "异常" : "正常");
            val = (0.3 + random.nextDouble() * (0.2));
            waterWmLogForm.setChlorine(val.toString());
            val = (0.01 + random.nextDouble() * (0.01));
            waterWmLogForm.setFlora(val.toString());
            waterWmLogForm.setUserBy("system");
            waterWmLogForm.setCurr(curr);
            // 过去一小时的时间
            Date temp = DateUtils.hour(curr, -1);
            long time = CalculationUtils.randomDate(temp.getTime(), curr.getTime());
            waterWmLogForm.setSummaryDate(new Date(time));
            waterWmLogForm.setIsDel(NOT_DEL);
            waterWmLogForm.setEnterpriseId(waterWmLogFormParam.getEnterpriseId());
            list.add(waterWmLogForm);
        }
        iWaterWmLogService.adds(list);
        return null;
    }


    @Override
    public Pagination<WaterWmLogVo> page(WaterWmLogForm waterWmLogForm) throws FrameworkRuntimeException {
        LoginCas loginCas = iAuthCasApi.get();
        waterWmLogForm.setEnterpriseId(loginCas.getEnterpriseId());
        waterWmLogForm.setIsDel(NOT_DEL);
        return this.iWaterWmLogService.page(waterWmLogForm);
    }
}
