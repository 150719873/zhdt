package com.dotop.pipe.server.rest.service.alarm;

import com.dotop.pipe.core.form.AlarmNoticeForm;
import com.dotop.pipe.core.vo.alarm.AlarmNoticeRuleVo;
import com.dotop.pipe.web.api.factory.alarm.IAlarmNoticeRuleFactory;
import com.dotop.smartwater.dependence.core.common.BaseController;
import com.dotop.smartwater.dependence.core.global.GlobalContext;
import com.dotop.smartwater.dependence.core.log.LogMsg;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.dependence.core.utils.VerificationUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController()
@RequestMapping("/alarmNotice")
public class AlarmNoticeRuleController implements BaseController<AlarmNoticeForm> {

    private static final Logger logger = LogManager.getLogger(AlarmNoticeRuleController.class);

    @Autowired
    private IAlarmNoticeRuleFactory iAlarmNoticeRuleFactory;


    /**
     * 分页查询
     */
    @Override
    @PostMapping(value = "/page", produces = GlobalContext.PRODUCES, consumes = GlobalContext.CONSUMES)
    public String page(@RequestBody AlarmNoticeForm alarmNoticeForm) {
        logger.info(LogMsg.to("msg:", "告警通知规则分页查询开始"));
        logger.info(LogMsg.to("page", alarmNoticeForm.getPage(), "pageSize", alarmNoticeForm.getPageSize()));
        // 验证
        VerificationUtils.integer("page", alarmNoticeForm.getPage());
        VerificationUtils.integer("pageSize", alarmNoticeForm.getPageSize());
        Pagination<AlarmNoticeRuleVo> pagination = iAlarmNoticeRuleFactory.page(alarmNoticeForm);
        logger.info(LogMsg.to("msg:", "告警通知规则分页查询结束"));
        return resp(pagination);
    }

    /**
     * 新增
     */
    @Override
    @PostMapping(value = "/add", produces = GlobalContext.PRODUCES, consumes = GlobalContext.CONSUMES)
    public String add(@RequestBody AlarmNoticeForm alarmForm) {
        logger.info(LogMsg.to("msg:", "告警通知规则新增处理开始", "alarmForm", alarmForm));
        iAlarmNoticeRuleFactory.add(alarmForm);
        logger.info(LogMsg.to("msg:", "告警通知规则新增处理结束", "更新数据"));
        return resp();
    }


    /**
     * 删除
     */
    @Override
    @DeleteMapping(value = "/{id}", produces = GlobalContext.PRODUCES, consumes = GlobalContext.CONSUMES)
    public String del(AlarmNoticeForm alarmNoticeForm) {
        logger.info(LogMsg.to("msg:", "删除 开始", alarmNoticeForm, alarmNoticeForm));
        String noticeId = alarmNoticeForm.getId();
        // 校验
        VerificationUtils.string("noticeId", noticeId);
        String count = iAlarmNoticeRuleFactory.del(alarmNoticeForm);
        logger.info(LogMsg.to("msg:", "删除 结束", "更新数据", count));
        return resp();
    }


    /**
     * 告警通知日志分页查询
     */
    @PostMapping(value = "/log/page", produces = GlobalContext.PRODUCES, consumes = GlobalContext.CONSUMES)
    public String logPage(@RequestBody AlarmNoticeForm alarmNoticeForm) {
        logger.info(LogMsg.to("msg:", "告警通知日志分页查询开始"));
        logger.info(LogMsg.to("page", alarmNoticeForm.getPage(), "pageSize", alarmNoticeForm.getPageSize()));
        // 验证
        VerificationUtils.integer("page", alarmNoticeForm.getPage());
        VerificationUtils.integer("pageSize", alarmNoticeForm.getPageSize());
        Pagination<AlarmNoticeRuleVo> pagination = iAlarmNoticeRuleFactory.logPage(alarmNoticeForm);
        logger.info(LogMsg.to("msg:", "告警通知日志分页查询结束"));
        return resp(pagination);
    }


}
