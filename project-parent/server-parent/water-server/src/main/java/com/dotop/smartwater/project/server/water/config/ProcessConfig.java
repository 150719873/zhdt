package com.dotop.smartwater.project.server.water.config;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.dotop.smartwater.project.module.api.workcenter.IWorkCenterBuildFactory;
import com.dotop.smartwater.project.module.api.workcenter.IWorkCenterFeedbackFactory;
import com.dotop.smartwater.project.module.core.water.constants.WaterConstants;

@Configuration
public class ProcessConfig {

    @Resource(name = "OrderAuditingBuildFactoryImpl")
    private IWorkCenterBuildFactory orderAuditingBuildFactoryImpl;

    /**
     * 库存管理
     */
    @Resource(name = "StorageBuildFactoryImpl")
    private IWorkCenterBuildFactory storageBuildFactoryImpl;
    @Resource(name = "OutStorageBuildFactoryImpl")
    private IWorkCenterBuildFactory outStorageBuildFactoryImpl;

    /**
     * 财务系统--账单对账
     */
    @Resource(name = "FinanceBuildFactoryImpl")
    private IWorkCenterBuildFactory financeBuildFactoryImpl;

    /**
     * 运维系统--运维日志
     */
    @Resource(name = "OperationBuildFactoryImpl")
    private IWorkCenterBuildFactory operationBuildFactoryImpl;

    @Resource(name = "DeviceWarningBuildFactoryImpl")
    private IWorkCenterBuildFactory deviceWarningBuildFactoryImpl;

    @Resource(name = "TestBuildFactoryImpl")
    private IWorkCenterBuildFactory testBuildFactoryImpl;

    @Resource(name = "PipeBuildFactoryImpl")
    private IWorkCenterBuildFactory pipeBuildFactoryImpl;
    
    @Resource(name = "OaBuildFactoryImpl")
    private IWorkCenterBuildFactory oaBuildFactoryImpl;
    /**
     * 注册工作中心构建业务子模块集合
     */
    @Bean(name = "IWorkCenterBuildFactoryMap")
    public Map<String, IWorkCenterBuildFactory> buildMap() {
        Map<String, IWorkCenterBuildFactory> map = new HashMap<>();
        map.put(WaterConstants.WORK_CENTER_BUSINESS_TYPE_TEST, testBuildFactoryImpl);
        map.put(WaterConstants.WORK_CENTER_BUSINESS_TYPE_REVENUE_ORDER_AUDITING, orderAuditingBuildFactoryImpl);
        // 财务系统 -- 账单对账
        map.put(WaterConstants.WORK_CENTER_BUSINESS_TYPE_FINANCIAL, financeBuildFactoryImpl);

        map.put(WaterConstants.WORK_CENTER_BUSINESS_TYPE_OPERATION, operationBuildFactoryImpl);

        map.put(WaterConstants.WORK_CENTER_BUSINESS_TYPE_DEVICE_ALARM, deviceWarningBuildFactoryImpl);

        map.put(WaterConstants.WORK_CENTER_BUSINESS_TYPE_STORAGE, storageBuildFactoryImpl);

        map.put(WaterConstants.WORK_CENTER_BUSINESS_TYPE_STORAGE_OUT, outStorageBuildFactoryImpl);

        map.put(WaterConstants.WORK_CENTER_BUSINESS_TYPE_PIPE, pipeBuildFactoryImpl);
        
        map.put(WaterConstants.WORK_CENTER_BUSINESS_TYPE_OA, oaBuildFactoryImpl);
        return map;
    }

    @Resource(name = "OrderAuditingFactoryImpl")
    private IWorkCenterFeedbackFactory orderAuditingFactoryImpl;

    /**
     * 运维系统--运维日志
     */
    @Resource(name = "OperationFeedbackFactoryImpl")
    private IWorkCenterFeedbackFactory operationFeedbackFactoryImpl;

    /**
     * 库存管理
     */
    @Resource(name = "StorageFeedbackFactoryImpl")
    private IWorkCenterFeedbackFactory storageFeedbackFactoryImpl;
    @Resource(name = "OutStorageFeedbackFactoryImpl")
    private IWorkCenterFeedbackFactory outStorageFeedbackFactoryImpl;

    @Resource(name = "DeviceWarningFeedbackFactoryImpl")
    private IWorkCenterFeedbackFactory deviceWarningFeedbackFactoryImpl;
    /**
     * 财务系统--账单对账
     */
    @Resource(name = "FinanceFeedBackFactoryImpl")
    private IWorkCenterFeedbackFactory financeFeedBackFactoryImpl;

    @Resource(name = "TestFeedbackFactoryImpl")
    private IWorkCenterFeedbackFactory testFeedbackFactoryImpl;

    @Resource(name = "PipeFeedBackFactoryImpl")
    private IWorkCenterFeedbackFactory pipeFeedBackFactoryImpl;
    
    // OA办公
    @Resource(name = "OaFactoryImpl")
    private IWorkCenterFeedbackFactory oaFactoryImpl;
    /**
     * 注册工作中心反馈业务子模块集合
     */
    @Bean(name = "IWorkCenterFeedbackFactoryMap")
    public Map<String, IWorkCenterFeedbackFactory> feedbackMap() {
        Map<String, IWorkCenterFeedbackFactory> map = new HashMap<>();
        map.put(WaterConstants.WORK_CENTER_BUSINESS_TYPE_TEST, testFeedbackFactoryImpl);
        // 财务系统- 账单对账
        map.put(WaterConstants.WORK_CENTER_BUSINESS_TYPE_FINANCIAL, financeFeedBackFactoryImpl);

        map.put(WaterConstants.WORK_CENTER_BUSINESS_TYPE_OPERATION, operationFeedbackFactoryImpl);

        map.put(WaterConstants.WORK_CENTER_BUSINESS_TYPE_REVENUE_ORDER_AUDITING, orderAuditingFactoryImpl);

        map.put(WaterConstants.WORK_CENTER_BUSINESS_TYPE_DEVICE_ALARM, deviceWarningFeedbackFactoryImpl);

        map.put(WaterConstants.WORK_CENTER_BUSINESS_TYPE_STORAGE, storageFeedbackFactoryImpl);

        map.put(WaterConstants.WORK_CENTER_BUSINESS_TYPE_STORAGE_OUT, outStorageFeedbackFactoryImpl);
        map.put(WaterConstants.WORK_CENTER_BUSINESS_TYPE_PIPE, pipeFeedBackFactoryImpl);
        map.put(WaterConstants.WORK_CENTER_BUSINESS_TYPE_OA, oaFactoryImpl);
        return map;
    }
}
