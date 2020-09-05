package com.dotop.pipe.server.rest.service.runingdata;

import com.dotop.pipe.core.form.RuningDataForm;
import com.dotop.pipe.server.config.TimerConfig;
import com.dotop.pipe.web.api.factory.runingdata.IRuningDataFactory;
import com.dotop.smartwater.dependence.core.log.LogMsg;
import com.dotop.smartwater.dependence.core.utils.DateUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Date;

public class RunDataRunnable implements Runnable {
    private static final Logger LOGGER_SYSOUT = LogManager.getLogger("SYSOUT");
    private static final Logger LOGGER = LogManager.getLogger(RunDataRunnable.class);

    // 此线程不是一个bean 参数不能引入 只能传参
    private String threadName;

    private IRuningDataFactory iRuningDataFactory;

    private RuningDataForm runingDataForm;

    private TimerConfig timerConfig;

    public RunDataRunnable(String threadName, IRuningDataFactory iRuningDataFactory, RuningDataForm runingDataForm, TimerConfig timerConfig) {
        this.threadName = threadName;
        this.iRuningDataFactory = iRuningDataFactory;
        this.runingDataForm = runingDataForm;
        this.timerConfig = timerConfig;
    }


    @Override
    public void run() {
        try {
            LOGGER_SYSOUT.info("定时生成官网数据开始");
            LOGGER_SYSOUT.info(this.runingDataForm);
            iRuningDataFactory.makeData(this.runingDataForm);
            System.out.println(Thread.currentThread().getName());
            System.out.println(new Date());
            System.out.println(Thread.currentThread().getId());
            System.out.println("线程执行结束: 回收线程 或者 其他操作 如果只执行一次 需要结束 定时任务");
            if ("once".equals(this.runingDataForm.getType())) {  // 如果是执行一次的定时任务  在执行完成后需要 关闭任务以及线程
                TimerConfig.stopCron(this.runingDataForm.getTaskId());
            } else { // TODO 执行多次的定时任务需要视情况而定
                // 执行完毕后需要修改时间 下次以这次的时间+时间间隔 为开始时间
                runingDataForm.setNextStartDate(DateUtils.second(runingDataForm.getNextStartDate(), runingDataForm.getInterval() * 60));
                System.out.println(runingDataForm.getTaskId() + ":" + runingDataForm.getTaskName());
                if (runingDataForm.getNextStartDate().getTime() > runingDataForm.getEndDate().getTime()) {
                    // 已超过最后日期 不在继续执行
                    this.timerConfig.stopCron(this.runingDataForm.getTaskId());
                } else if (runingDataForm.getNextStartDate().getTime() <= new Date().getTime()) {
                    // 下次执行时间还在 当前时间之前 改变定时器的时间
                    this.timerConfig.changeCron(runingDataForm);
                }
            }
        } catch (Exception e) {
            LOGGER_SYSOUT.info("定时生成官网数据失败");
            RuningDataForm runingDataForm = new RuningDataForm();
            runingDataForm.setTaskId(this.runingDataForm.getTaskId());
            runingDataForm.setStatus("4");
            iRuningDataFactory.edit(runingDataForm);
            TimerConfig.stopCron(this.runingDataForm.getTaskId()); // 异常时停止定时任务
            LOGGER.error(LogMsg.to(e));
        }
    }
}
