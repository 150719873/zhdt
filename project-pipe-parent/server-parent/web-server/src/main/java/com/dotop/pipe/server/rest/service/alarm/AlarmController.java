package com.dotop.pipe.server.rest.service.alarm;

import com.dotop.pipe.core.form.AlarmForm;
import com.dotop.pipe.core.vo.alarm.AlarmVo;
import com.dotop.pipe.web.api.factory.alarm.IAlarmFactory;
import com.dotop.smartwater.dependence.core.common.BaseController;
import com.dotop.smartwater.dependence.core.global.GlobalContext;
import com.dotop.smartwater.dependence.core.log.LogMsg;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.dependence.core.utils.VerificationUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

/**
 *
 */
@RestController()
@RequestMapping("/alarm")
public class AlarmController implements BaseController<AlarmForm> {

    private static final Logger logger = LogManager.getLogger(AlarmController.class);

    public AlarmController() {
        super();
    }

    @Autowired
    private IAlarmFactory iAlarmFactory;

    /**
     * 传感器分页查询
     */
    @Override
    @PostMapping(value = "/page", produces = GlobalContext.PRODUCES, consumes = GlobalContext.CONSUMES)
    public String page(@RequestBody AlarmForm alarmForm) {
        logger.info(LogMsg.to("msg:", "报警分页查询开始"));
        logger.info(LogMsg.to("page", alarmForm.getPage(), "pageSize", alarmForm.getPageSize()));
        // 验证
        VerificationUtils.integer("page", alarmForm.getPage());
        VerificationUtils.integer("pageSize", alarmForm.getPageSize());
        VerificationUtils.string("deviceName", alarmForm.getDeviceName(), true);
        VerificationUtils.string("deviceCode", alarmForm.getDeviceName(), true);

        Pagination<AlarmVo> pagination = iAlarmFactory.page(alarmForm);
        logger.info(LogMsg.to("msg:", "报警分页查询结束"));
        return resp(pagination);
    }

    /**
     * 头部报警
     *
     * @param alarmForm
     * @return
     */
    @Override
    @PostMapping(value = "/list", produces = GlobalContext.PRODUCES, consumes = GlobalContext.CONSUMES)
    public String list(@RequestBody AlarmForm alarmForm) {
        logger.info(LogMsg.to("msg:", "报警分页查询开始"));
        logger.info(LogMsg.to("page", alarmForm.getPage(), "pageSize", alarmForm.getPageSize()));
        // 验证
        VerificationUtils.integer("page", alarmForm.getPage());
        VerificationUtils.integer("pageSize", alarmForm.getPageSize());
        VerificationUtils.string("deviceName", alarmForm.getDeviceName(), true);
        VerificationUtils.string("deviceCode", alarmForm.getDeviceName(), true);

        Pagination<AlarmVo> pagination = iAlarmFactory.list(alarmForm);
        logger.info(LogMsg.to("msg:", "报警分页查询结束"));
        return resp(pagination);
    }


    /**
     * 报警详情
     */
    @Override
    @PostMapping(value = "/details", produces = GlobalContext.PRODUCES, consumes = GlobalContext.CONSUMES)
    public String get(@RequestBody AlarmForm alarmForm) {
        logger.info(LogMsg.to("msg:", "报警详情查询开始"));
        logger.info(LogMsg.to("alarmId", alarmForm.getAlarmId()));
        // 验证
        VerificationUtils.string("alarmId", alarmForm.getAlarmId());
        AlarmVo alarmVo = iAlarmFactory.get(alarmForm);
        logger.info(LogMsg.to("msg:", "报警详情查询结束"));
        return resp(alarmVo);
    }

    /**
     * 修改报警
     */
    @Override
    @PutMapping(value = "/upd", produces = GlobalContext.PRODUCES, consumes = GlobalContext.CONSUMES)
    public String edit(@RequestBody AlarmForm alarmForm) {
        logger.info(LogMsg.to("msg:", "报警处理开始", "alarmForm", alarmForm));
        // 验证
        String status = alarmForm.getStatus();
        // 处理状态不能为空
        VerificationUtils.string("status", status);
        AlarmVo alarmVo = iAlarmFactory.edit(alarmForm);
        logger.info(LogMsg.to("msg:", "报警处理结束", "更新数据", alarmVo));
        return resp(alarmVo);
    }

    @Override
    @PutMapping(value = "/add", produces = GlobalContext.PRODUCES, consumes = GlobalContext.CONSUMES)
    public String add(@RequestBody AlarmForm alarmForm) {
        logger.info(LogMsg.to("msg:", "报警处理开始", "alarmForm", alarmForm));
        iAlarmFactory.add(null, null, new HashMap(1));
        logger.info(LogMsg.to("msg:", "报警处理结束", "更新数据"));
        return resp();
    }


    /**
     * 查询即将报警的设备
     *
     * @param alarmForm
     * @return
     */
    @PostMapping(value = "/getNearAlarm", produces = GlobalContext.PRODUCES, consumes = GlobalContext.CONSUMES)
    public String getNearAlarm(@RequestBody AlarmForm alarmForm) {
        logger.info(LogMsg.to("msg:", "报警分页查询开始"));
        return resp(iAlarmFactory.getNearAlarm());
    }

    /**
     * 根据数据的最新值查看设备是否报警
     *
     * @param alarmForm
     * @return
     */
    @PostMapping(value = "/getAlreadyAlarm", produces = GlobalContext.PRODUCES, consumes = GlobalContext.CONSUMES)
    public String getAlarmByData(@RequestBody AlarmForm alarmForm) {
        logger.info(LogMsg.to("msg:", "地图报警分页查询开始"));
        String productCategory = alarmForm.getProductCategory();
//        if (StringUtils.isNotEmpty(productCategory)) {
//            productCategory = CommonConstants.PRODUCT_CATEGORY_SENSOR;
//        }
        return resp(iAlarmFactory.getAlarmByData(productCategory));
    }


}
