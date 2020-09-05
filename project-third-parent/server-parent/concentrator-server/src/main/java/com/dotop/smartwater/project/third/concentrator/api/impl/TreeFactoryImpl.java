package com.dotop.smartwater.project.third.concentrator.api.impl;

import com.dotop.smartwater.project.third.concentrator.core.vo.CollectorVo;
import com.dotop.smartwater.project.third.concentrator.core.vo.ConcentratorVo;
import com.dotop.smartwater.project.third.concentrator.service.ICollectorService;
import com.dotop.smartwater.project.third.concentrator.service.IConcentratorDeviceService;
import com.dotop.smartwater.dependence.cache.StringValueCache;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.log.LogMsg;
import com.dotop.smartwater.project.module.core.auth.local.IAuthCasClient;
import com.dotop.smartwater.project.third.concentrator.api.ICollectorFactory;
import com.dotop.smartwater.project.third.concentrator.api.IConcentratorFactory;
import com.dotop.smartwater.project.third.concentrator.api.ITreeFactory;
import com.dotop.smartwater.project.third.concentrator.core.constants.CacheKey;
import com.dotop.smartwater.project.third.concentrator.core.constants.ConcentratorConstants;
import com.dotop.smartwater.project.third.concentrator.core.form.CollectorForm;
import com.dotop.smartwater.project.third.concentrator.core.form.ConcentratorForm;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;


@Component
public class TreeFactoryImpl implements ITreeFactory, IAuthCasClient {

    private final static Logger LOGGER = LogManager.getLogger(TreeFactoryImpl.class);

    @Autowired
    private StringValueCache svc;

    @Autowired
    private IConcentratorFactory iConcentratorFactory;

    @Autowired
    private ICollectorFactory iCollectorFactory;

    @Autowired
    private ICollectorService iCollectorService;

    @Autowired
    private IConcentratorDeviceService iConcentratorDeviceService;

    @Override
    public Map<String, List<ConcentratorVo>> tree() throws FrameworkRuntimeException {
        // TODO 根据areaid过滤集中器，areaid可以存在多个
        // TODO 性能问题
        Map<String, List<ConcentratorVo>> treeMap = new LinkedHashMap<>();
        // 查询采集器
        List<CollectorVo> collectors = iCollectorFactory.list(new CollectorForm());
        for (CollectorVo collector : collectors) {
            // 查询水表挂载数
            collector.setDeviceMountAmount(iConcentratorDeviceService.countCollectorDevice(getEnterpriseid(), collector.getId(), null));
        }
        Map<String, List<CollectorVo>> collectorMap = collectors.stream().collect(
                Collectors.groupingBy(p -> p.getConcentrator().getId(), HashMap::new, Collectors.toCollection(ArrayList::new)));
        // 查询集中器
        List<ConcentratorVo> concentrators = iConcentratorFactory.list(new ConcentratorForm());
        for (ConcentratorVo concentrator : concentrators) {
            // 在线状态判断
            String concentratorCode = concentrator.getCode();
            concentrator.setIsOnline(ConcentratorConstants.ONLINE_OFFLINE);
            if (StringUtils.isNotBlank(svc.get(CacheKey.HEARTBEAT + concentratorCode))) {
                concentrator.setIsOnline(ConcentratorConstants.ONLINE_ONLINE);
            }
            // 查询采集器挂载数
            concentrator.setCollectorMountAmount(iCollectorService.countCollector(concentrator.getId(), null));
            // 查询采集器通道数
            concentrator.setCollectorChannelAmount(iConcentratorDeviceService.countCollectorChannel(getEnterpriseid(), concentrator.getId(), null));
            // 查询水表挂载数
            concentrator.setDeviceMountAmount(iConcentratorDeviceService.countConcentratorDevice(getEnterpriseid(), concentrator.getId(), null));
            // 过滤区域
//            AreaVo area = concentrator.getArea();
//            if (area != null && StringUtils.isNotBlank(area.getId())) {
//                String areaId = area.getId();
//                List<ConcentratorVo> vos = treeMap.get(areaId);
//                if (vos == null) {
//                    vos = new ArrayList<>();
//                    treeMap.put(area.getId(), vos);
//                }
//                vos.add(concentrator);
//            } else {
//                List<ConcentratorVo> vos = treeMap.get("-1");
//                if (vos == null) {
//                    vos = new ArrayList<>();
//                    treeMap.put("-1", vos);
//                }
//                vos.add(concentrator);
//            }
            List<String> areaIds = concentrator.getAreaIds();
            if (areaIds != null && !areaIds.isEmpty()) {
                for (String areaId : areaIds) {
                    List<ConcentratorVo> vos = treeMap.get(areaId);
                    if (vos == null) {
                        vos = new ArrayList<>();
                        treeMap.put(areaId, vos);
                    }
                    vos.add(concentrator);
                }
            } else {
                List<ConcentratorVo> vos = treeMap.get("-1");
                if (vos == null) {
                    vos = new ArrayList<>();
                    treeMap.put("-1", vos);
                }
                vos.add(concentrator);
            }
            //过滤采集器
            String concentratorId = concentrator.getId();
            List<CollectorVo> cs = collectorMap.get(concentratorId);
            concentrator.setCollectors(cs);
        }
        LOGGER.info(LogMsg.to("treeMap", treeMap));

        return treeMap;
    }
}
