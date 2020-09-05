package com.dotop.pipe.server.rest.service.alarm;

import com.dotop.pipe.core.form.AlarmSettingTemplateForm;
import com.dotop.pipe.core.vo.alarm.AlarmSettingTemplateVo;
import com.dotop.pipe.web.api.factory.alarm.IAlarmSettingTemplateFactory;
import com.dotop.smartwater.dependence.core.common.BaseController;
import com.dotop.smartwater.dependence.core.global.GlobalContext;
import com.dotop.smartwater.dependence.core.log.LogMsg;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.dependence.core.utils.VerificationUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 *
 */
@RestController()
@RequestMapping("/alarmSettingTemplate")
public class AlarmSettingTemplateController implements BaseController<AlarmSettingTemplateForm> {

    private final static Logger logger = LogManager.getLogger(AlarmSettingTemplateController.class);

    @Autowired
    private IAlarmSettingTemplateFactory iAlarmSettingFactory;

    @Override
    @PostMapping(value = "/add", produces = GlobalContext.PRODUCES, consumes = GlobalContext.CONSUMES)
    public String add(@RequestBody AlarmSettingTemplateForm alarmSettingForm) {
        logger.info(LogMsg.to("msg:", "预警模板设置新增开始", "alarmSettingForm", alarmSettingForm));
        VerificationUtils.string("name", alarmSettingForm.getName());
        VerificationUtils.string("code", alarmSettingForm.getCode());
        // VerificationUtils.string("content", alarmSettingForm.getContent());
        AlarmSettingTemplateVo alarmSettingVo = iAlarmSettingFactory.add(alarmSettingForm);
        logger.info(LogMsg.to("msg:", "预警模板设置新增结束", "更新数据", alarmSettingVo));
        return resp(alarmSettingVo);
    }

    @PostMapping(value = "/page", produces = GlobalContext.PRODUCES, consumes = GlobalContext.CONSUMES)
    public String page(@RequestBody AlarmSettingTemplateForm alarmSettingForm) {
        logger.info(LogMsg.to("msg:", "预警模板设置分页查询开始", "alarmSettingForm", alarmSettingForm));
        Integer page = alarmSettingForm.getPage();
        Integer pageSize = alarmSettingForm.getPageSize();
        String productCategory = alarmSettingForm.getProductCategory();
        // 验证
        VerificationUtils.integer("page", page);
        VerificationUtils.integer("pageSize", pageSize);
        // VerificationUtils.string("productCategory", productCategory);
        Pagination<AlarmSettingTemplateVo> pagination = iAlarmSettingFactory.page(alarmSettingForm);
        logger.info(LogMsg.to("msg:", "预警模板设置分页查询结束"));
        return resp(pagination);
    }

    @PostMapping(value = "/list", produces = GlobalContext.PRODUCES, consumes = GlobalContext.CONSUMES)
    public String list(@RequestBody AlarmSettingTemplateForm alarmSettingForm) {
        logger.info(LogMsg.to("msg:", "预警模板设置分页查询开始", "alarmSettingForm", alarmSettingForm));
        Integer page = alarmSettingForm.getPage();
        Integer pageSize = alarmSettingForm.getPageSize();
        String productCategory = alarmSettingForm.getProductCategory();
        // 验证
        //VerificationUtils.integer("page", page);
        // VerificationUtils.integer("pageSize", pageSize);
        // VerificationUtils.string("productCategory", productCategory);
        List<AlarmSettingTemplateVo> pagination = iAlarmSettingFactory.list(alarmSettingForm);
        logger.info(LogMsg.to("msg:", "预警模板设置分页查询结束"));
        return resp(pagination);
    }

    /**
     * 预警设置详情
     */
    @Override
    @GetMapping(value = "/{id}", produces = GlobalContext.PRODUCES, consumes = GlobalContext.CONSUMES)
    public String get(AlarmSettingTemplateForm alarmSettingForm) {
        logger.info(LogMsg.to("msg:", "预警模板设置详情查询开始"));
        logger.info(LogMsg.to("id", alarmSettingForm.getId()));
        // 验证
        VerificationUtils.string("id", alarmSettingForm.getId());
        AlarmSettingTemplateVo alarmVo = iAlarmSettingFactory.get(alarmSettingForm);
        logger.info(LogMsg.to("msg:", "预警模板设置详情查询结束"));
        return resp(alarmVo);
    }

    /**
     * 预警设置删除
     */
    @Override
    @DeleteMapping(value = "del/{id}", produces = GlobalContext.PRODUCES, consumes = GlobalContext.CONSUMES)
    public String del(AlarmSettingTemplateForm alarmSettingForm) {
        logger.info(LogMsg.to("msg:", "预警模板设置删除开始"));
        logger.info(LogMsg.to("id", alarmSettingForm.getId()));
        // 验证
        VerificationUtils.string("id", alarmSettingForm.getId());
        String alarmVo = iAlarmSettingFactory.del(alarmSettingForm);
        logger.info(LogMsg.to("msg:", "预警模板设置删除结束"));
        return resp(alarmVo);
    }
}
