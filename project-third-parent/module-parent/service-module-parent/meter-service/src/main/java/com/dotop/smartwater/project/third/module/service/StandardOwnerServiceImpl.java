package com.dotop.smartwater.project.third.module.service;

import com.dotop.smartwater.project.third.module.api.dao.IWaterDeviceDao;
import com.dotop.smartwater.project.third.module.api.dao.IWaterOwnerDao;
import com.dotop.smartwater.dependence.cache.StringValueCache;
import com.dotop.smartwater.dependence.core.common.RootModel;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.dependence.core.utils.BeanUtils;
import com.dotop.smartwater.dependence.core.utils.DateUtils;
import com.dotop.smartwater.project.third.module.api.service.IStandardOwnerService;
import com.dotop.smartwater.project.third.module.core.third.standard.vo.StandardOwnerVo;
import com.dotop.smartwater.project.third.module.core.water.bo.DeviceUplinkBo;
import com.dotop.smartwater.project.third.module.core.water.dto.DeviceUplinkDto;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;
import java.util.stream.Collectors;

/**
 *
 */
@Component
public class StandardOwnerServiceImpl implements IStandardOwnerService {


    @Autowired
    private StringValueCache svc;
    @Autowired
    private IWaterOwnerDao iWaterOwnerDao;
    @Autowired
    private IWaterDeviceDao iWaterDeviceDao;


    @Override
    public Pagination<StandardOwnerVo> pageOwner(DeviceUplinkBo deviceUplinkBo, Integer page, Integer pageCount) throws FrameworkRuntimeException {

        DateTime starDate = new DateTime(DateUtils.parse(deviceUplinkBo.getStartMonth(), DateUtils.YYYYMM).getTime());
        DateTime endDate = new DateTime(DateUtils.parse(deviceUplinkBo.getEndMonth(), DateUtils.YYYYMM).getTime());
        Integer between = (endDate.getYear() - starDate.getYear()) * 12 + endDate.getMonthOfYear() - starDate.getMonthOfYear();
        List<StandardOwnerVo> allList = new ArrayList<>();
        int totalPageSize = 0;
        for (Integer i = 0; i <= between; i++) {
            DateTime temp = new DateTime(DateUtils.month(starDate.toDate(), i));
            deviceUplinkBo.setYearMonth(DateUtils.format(temp.toDate(), DateUtils.YYYYMM));
            DeviceUplinkDto deviceUplinkDto = BeanUtils.copy(deviceUplinkBo, DeviceUplinkDto.class);
            deviceUplinkDto.setIsDel(RootModel.NOT_DEL);
            List<StandardOwnerVo> standardOwnerVos = iWaterOwnerDao.listInfo(deviceUplinkDto);
            allList.addAll(standardOwnerVos);
        }


        List<StandardOwnerVo> resultList = new ArrayList<>();
        // 过滤时间为null的用户
        List<StandardOwnerVo> uplintimeSortList = allList.stream().filter(o -> o.getUplinkTime() != null).collect(Collectors.toList());
        List<StandardOwnerVo> sortList = uplintimeSortList.stream().sorted(Comparator.comparing(StandardOwnerVo::getUplinkTime).reversed()).collect(Collectors.toList());
        resultList.addAll(sortList);
        resultList = resultList.stream().collect(Collectors.collectingAndThen(Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(o -> o.getUserno()))), ArrayList::new));

        totalPageSize = resultList.size();
        if (resultList.size() > pageCount) {
            int startFrom = (page - 1) * pageCount;
            int endFrom = page * pageCount;
            resultList = resultList.subList(startFrom, endFrom);

        }
        return new Pagination<>(page, pageCount, resultList, totalPageSize);
    }


}
