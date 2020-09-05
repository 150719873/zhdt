package com.dotop.pipe.server.rest.service.report;

import com.dotop.pipe.data.report.core.vo.*;
import com.dotop.pipe.core.constants.PipeConstants;
import com.dotop.pipe.data.report.api.factory.IReportFactory;
import com.dotop.pipe.data.report.core.form.AreaReportForm;
import com.dotop.pipe.data.report.core.form.ReportForm;
import com.dotop.pipe.data.report.core.form.VirtualForm;
import com.dotop.smartwater.dependence.core.common.BaseController;
import com.dotop.smartwater.dependence.core.common.pipe.BasePipeForm;
import com.dotop.smartwater.dependence.core.global.GlobalContext;
import com.dotop.smartwater.dependence.core.log.LogMsg;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.dependence.core.utils.DateUtils;
import com.dotop.smartwater.dependence.core.utils.VerificationUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 *
 * @date 2018/11/2.
 */
@RestController()
@RequestMapping("/deviceReport")
public class ReportController implements BaseController<BasePipeForm> {

    private final static Logger logger = LogManager.getLogger(ReportController.class);

    @Autowired
    private IReportFactory iReportFactory;

    @PostMapping(value = "/getDeviceReport", produces = GlobalContext.PRODUCES, consumes = GlobalContext.CONSUMES)
    public String getDeviceReport(@RequestBody ReportForm reportForm) {
        logger.info(LogMsg.to("msg:", "报表统计查询开始"));
        logger.info(LogMsg.to("reportForm", reportForm));
        String ratio = reportForm.getRatio();
        Date startDate = reportForm.getStartDate();
        Date endDate = reportForm.getEndDate();
        // 验证
        // 时间不能为空
        VerificationUtils.date("startDate", startDate);
        VerificationUtils.date("endDate", endDate);
        // 比较
        if (StringUtils.isNotBlank(ratio) && PipeConstants.RING_RATIO.equals(ratio)) {
            // 环比
            // 差的天数
            int diff = DateUtils.daysBetween(startDate, endDate);
            startDate = DateUtils.day(startDate, -diff);
            endDate = DateUtils.day(endDate, -diff);
            reportForm.setStartDate(startDate);
            reportForm.setEndDate(endDate);
            logger.info(LogMsg.to("reportForm", reportForm));
        } else if (StringUtils.isNotBlank(ratio) && PipeConstants.YEAR_RATIO.equals(ratio)) {
            // 同比
            startDate = new DateTime(startDate).plusYears(-1).toDate();
            endDate = new DateTime(endDate).plusYears(-1).toDate();
            reportForm.setStartDate(startDate);
            reportForm.setEndDate(endDate);
            logger.info(LogMsg.to("reportForm", reportForm));
        }

        Map<String, List<ReportGroupVo>> map = iReportFactory.getDeviceReport(reportForm);
        // logger.info(LogMsg.to("map", map));
        logger.info(LogMsg.to("msg:", "报表统计查询结束"));
        return resp(map);

    }

    @PostMapping(value = "/getDeviceReportPage/{page}/{pageSize}", produces = GlobalContext.PRODUCES)
    public String getDeviceReportPage(@PathVariable("page") Integer page, @PathVariable("pageSize") Integer pageSize,
                                      @RequestBody ReportForm reportForm) {
        logger.info(LogMsg.to("msg:", "报表统计分页查询开始"));
        logger.info(LogMsg.to("reportForm", reportForm, "page", page, "pageSize", pageSize));
        Date startDate = reportForm.getStartDate();
        Date endDate = reportForm.getEndDate();
        // 验证
        VerificationUtils.integer("page", page);
        VerificationUtils.integer("pageSize", pageSize);
        // 时间不能为空
        VerificationUtils.date("startDate", startDate);
        VerificationUtils.date("endDate", endDate);

        Pagination<ReportVo> pagination = iReportFactory.getDeviceReportPage(reportForm, page, pageSize);
        logger.info(LogMsg.to("msg:", "报表统计分页查询结束"));
        return resp(pagination);
    }

    @PostMapping(value = "/getDeviceRealTime", produces = GlobalContext.PRODUCES, consumes = GlobalContext.CONSUMES)
    public String getDeviceRealTime(@RequestBody ReportForm reportForm) {
        logger.info(LogMsg.to("msg:", "计量计实时数据查询开始"));
        logger.info(LogMsg.to("reportForm", reportForm));
        System.out.println(reportForm);
        // 验证
        // Map<String, List<ReportGroupVo>> map =
        // iReportFactory.getDeviceRealTime(reportForm);
        List<ReportVo> list = iReportFactory.getDeviceRealTime(reportForm);
        logger.info(LogMsg.to("msg:", "计量计实时数据查询结束"));
        return resp(list);

    }

    @PostMapping(value = "/getAreaReport", produces = GlobalContext.PRODUCES, consumes = GlobalContext.CONSUMES)
    public String getAreaReport(@RequestBody AreaReportForm areaReportForm) {
        logger.info(LogMsg.to("msg:", "区域用水量数据查询开始"));
        logger.info(LogMsg.to("areaReportForm", areaReportForm));
        System.out.println(areaReportForm);
        // 验证
        List<ReportAreaGroupVo> list = iReportFactory.getAreaReport(areaReportForm);
        // logger.info(LogMsg.to("list", list));
        logger.info(LogMsg.to("msg:", "区域用水量数据查询结束"));
        return resp(list);
    }

    @PostMapping(value = "/getVirtualFlow", produces = GlobalContext.PRODUCES, consumes = GlobalContext.CONSUMES)
    public String getVirtualFlow(@RequestBody VirtualForm virtualForm) {
        logger.info(LogMsg.to("msg:", "虚拟流量查询开始"));
        logger.info(LogMsg.to("virtualForm", virtualForm));
        // 验证
        List<ReportVo> list = iReportFactory.getVirtualFlow(virtualForm);
        logger.info(LogMsg.to("msg:", "虚拟流量查询结束"));
        return resp(list);
    }

    /**
     * 查询片区用水量统计报表
     *
     * @param page
     * @param pageSize
     * @param reportForm
     * @return
     */
    @PostMapping(value = "/getRegionReportPage/{page}/{pageSize}", produces = GlobalContext.PRODUCES)
    public String getRegionReportPage(@PathVariable("page") Integer page, @PathVariable("pageSize") Integer pageSize,
                                      @RequestBody ReportForm reportForm) {
        logger.info(LogMsg.to("msg:", "片区用水量报表统计分页查询开始"));
        logger.info(LogMsg.to("reportForm", reportForm, "page", page, "pageSize", pageSize));
        Date startDate = reportForm.getStartDate();
        Date endDate = reportForm.getEndDate();
        // 验证
        VerificationUtils.integer("page", page);
        VerificationUtils.integer("pageSize", pageSize);
        // 时间不能为空
        VerificationUtils.date("startDate", startDate);
        VerificationUtils.date("endDate", endDate);
        Pagination<RegionReportVo> pagination = iReportFactory.getRegionReportPage(reportForm, page, pageSize);
        logger.info(LogMsg.to("msg:", "片区用水量报表统计分页查询结束"));
        return resp(pagination);
    }

    /**
     * 根据抄表信息 统计抄表值
     *
     * @param areaReportForm
     * @return
     */
    @PostMapping(value = "/getAreaReportByReading", produces = GlobalContext.PRODUCES)
    public String getAreaReportByReading(@RequestBody AreaReportForm areaReportForm) {
        logger.info(LogMsg.to("msg:", "区域抄表信息统计开始"));
        Date startDate = areaReportForm.getStartDate();
        Date endDate = areaReportForm.getEndDate();
        // 时间不能为空
        VerificationUtils.date("startDate", startDate);
        // 时间不能为空
        VerificationUtils.date("endDate", endDate);
        List<String> areaIds = null;
        List<AreaReportVo> pagination = iReportFactory.getAreaReportByReading(startDate, endDate, areaIds);
        logger.info(LogMsg.to("msg:", "区域抄表信息统计结束"));
        return resp(pagination);

    }

    /**
     * 已片区的角度统计 流量 流速 行度 数据
     *
     * @param areaReportForm
     * @return
     */
    @PostMapping(value = "/getRegionRealTimeReport", produces = GlobalContext.PRODUCES)
    public String getRegionRealTimeReport(@RequestBody AreaReportForm areaReportForm) {
        logger.info(LogMsg.to("msg:", "分区分级 实时查看流量计设备统计开始"));
        Date startDate = areaReportForm.getStartDate();
        Date endDate = areaReportForm.getEndDate();
        // 时间不能为空
        VerificationUtils.date("startDate", startDate);
        // 时间不能为空
        VerificationUtils.date("endDate", endDate);
        List<String> regionList = null;
        List<RegionReportVo> pagination = iReportFactory.getRegionRealTimeReport(startDate, endDate, regionList);
        logger.info(LogMsg.to("msg:", "分区分级 实时查看流量计设备统计结束"));
        return resp(pagination);

    }

    /**
     * 已区域的角度统计 流量 流速 行度 数据
     *
     * @param areaReportForm
     * @return
     */
    @PostMapping(value = "/getAreaRealTimeReport", produces = GlobalContext.PRODUCES)
    public String getAreaRealTimeReport(@RequestBody AreaReportForm areaReportForm) {
        logger.info(LogMsg.to("msg:", "区域实时查看流量计设备统计开始"));
        Date startDate = areaReportForm.getStartDate();
        Date endDate = areaReportForm.getEndDate();
        // 时间不能为空
        VerificationUtils.date("startDate", startDate);
        // 时间不能为空
        VerificationUtils.date("endDate", endDate);
        List<String> regionList = null;
        List<AreaReportVo> pagination = iReportFactory.getAreaRealTimeReport(startDate, endDate, regionList);
        logger.info(LogMsg.to("msg:", "区域实时查看流量计设备统计结束"));
        return resp(pagination);

    }

}
