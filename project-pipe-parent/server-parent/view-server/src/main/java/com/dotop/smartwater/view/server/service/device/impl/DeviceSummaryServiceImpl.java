package com.dotop.smartwater.view.server.service.device.impl;

import com.dotop.smartwater.view.server.service.device.IDeviceSummaryService;
import com.dotop.smartwater.dependence.core.exception.BaseExceptionConstants;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.dependence.core.utils.BeanUtils;
import com.dotop.smartwater.view.server.core.device.form.DeviceSummaryForm;
import com.dotop.smartwater.view.server.core.device.vo.DeviceSummaryVo;
import com.dotop.smartwater.view.server.dao.pipe.device.IDeviceSummaryDao;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import static com.dotop.smartwater.view.server.constants.ViewConstants.*;

/**
 *
 */
@Service
public class DeviceSummaryServiceImpl implements IDeviceSummaryService {

    @Autowired
    IDeviceSummaryDao iDeviceSummaryDao;


    @Override
    public List list(DeviceSummaryForm deviceSummaryForm) throws FrameworkRuntimeException {
        return iDeviceSummaryDao.list(deviceSummaryForm);
    }

    @Override
    public List<DeviceSummaryVo> listFactory(DeviceSummaryForm deviceSummaryForm) throws FrameworkRuntimeException {
        return iDeviceSummaryDao.listFactory(deviceSummaryForm);
    }

    @Override
    public List<DeviceSummaryVo> listFactoryNotGroup(DeviceSummaryForm deviceSummaryForm) throws FrameworkRuntimeException {
        return iDeviceSummaryDao.listFactoryNotGroup(deviceSummaryForm);
    }

    @Override
    public List<DeviceSummaryVo> listCurr(DeviceSummaryForm deviceSummaryForm) throws FrameworkRuntimeException {
        return iDeviceSummaryDao.listCurr(deviceSummaryForm);
    }

    @Override
    public List<DeviceSummaryVo> listFactoryCurr(DeviceSummaryForm deviceSummaryForm) throws FrameworkRuntimeException {
        return iDeviceSummaryDao.listFactoryCurr(deviceSummaryForm);
    }

    @Override
    public void edits(List<DeviceSummaryForm> deviceSummaryForms) throws FrameworkRuntimeException {
        iDeviceSummaryDao.edits(deviceSummaryForms);
    }

    @Override
    public void dels(List<DeviceSummaryForm> deviceSummaryForms) throws FrameworkRuntimeException {
        iDeviceSummaryDao.dels(deviceSummaryForms);
    }

    @Override
    public void delByType(List<String> list, Date date) throws FrameworkRuntimeException {
        iDeviceSummaryDao.delByType(list, date);
    }

    @Override
    public Boolean isInit(DeviceSummaryForm deviceSummaryForm) throws FrameworkRuntimeException {
        return iDeviceSummaryDao.isInit(deviceSummaryForm) != null;
    }

    @Override
    public void adds(List<DeviceSummaryForm> deviceSummaryForms) throws FrameworkRuntimeException {
        // 一次插入100条
        int once = 1000;
        for (int i = 0; i * once < deviceSummaryForms.size(); i++) {
            List<DeviceSummaryForm> adds;
            if (deviceSummaryForms.size() <= once) {
                adds = deviceSummaryForms;
            } else {
                int count = (i + 1) * once;
                if (count > deviceSummaryForms.size()) {
                    count = deviceSummaryForms.size();
                }
                adds = deviceSummaryForms.subList(i * once, count);
            }
            Integer count = iDeviceSummaryDao.adds(adds);
            if (count != adds.size()) {
                throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, "数据添加失败");
            }
        }
    }

    @Override
    public void addCuurs(List<DeviceSummaryForm> deviceSummaryForms) throws FrameworkRuntimeException {
        // 一次插入100条
        int once = 100;
        for (int i = 0; i * once < deviceSummaryForms.size(); i++) {
            List<DeviceSummaryForm> adds;
            if (deviceSummaryForms.size() <= once) {
                adds = deviceSummaryForms;
            } else {
                int count = (i + 1) * once;
                if (count > deviceSummaryForms.size()) {
                    count = deviceSummaryForms.size();
                }
                adds = deviceSummaryForms.subList(i * once, count);
            }
            Integer count = iDeviceSummaryDao.addCuurs(adds);
//            if (count == adds.size() || count * 2 == adds.size()) {
//                throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, "数据添加失败");
//            }
        }
    }

    @Override
    public List<DeviceSummaryVo> getCurr(DeviceSummaryForm deviceSummaryForm) throws FrameworkRuntimeException {
        List<DeviceSummaryVo> list = iDeviceSummaryDao.getCurr(deviceSummaryForm);
        return list;
    }

    @Override
    public List<DeviceSummaryVo> listGroup(DeviceSummaryForm deviceSummaryForm) throws FrameworkRuntimeException {
        List<DeviceSummaryVo> list = iDeviceSummaryDao.listGroup(deviceSummaryForm);
        return list;
    }


    public List<DeviceSummaryVo> inOutWaterListGroup(DeviceSummaryForm deviceSummaryForm) throws FrameworkRuntimeException {
        // 已device_id 作为分组条件 求同一个时间内的不同deviceId = sum max值
        List<DeviceSummaryVo> list = iDeviceSummaryDao.inOutWaterListGroup(deviceSummaryForm);
        // 分组
        Map<String, List<DeviceSummaryVo>> groupBy = list.stream().collect(Collectors.groupingBy(DeviceSummaryVo::getGroupDate));

        List<DeviceSummaryVo> result = new ArrayList<>();
        for (String key : groupBy.keySet()) {//keySet获取map集合key的集合  然后在遍历key即可
            List<DeviceSummaryVo> values = groupBy.get(key);//
            if (values.size() > 0) {
                BigDecimal result2 = values.stream().map(DeviceSummaryVo::getMaxBigDecimal).reduce(BigDecimal.ZERO, BigDecimal::add);
                DeviceSummaryVo temp = BeanUtils.copyProperties(values.get(0), DeviceSummaryVo.class);
                temp.setSumval(result2.toString());
                temp.setMaxval(result2.toString());
                result.add(temp);
            }
        }
        result = result.stream().sorted(Comparator.comparing(DeviceSummaryVo::getGroupDate).reversed()).collect(Collectors.toList());//根据创建时间倒排
        return result;
    }

    @Override
    public Pagination<Map<String, DeviceSummaryVo>> pagePower(DeviceSummaryForm deviceForm) throws FrameworkRuntimeException {
        // 分页条件特殊处理
        int tempPageSize = deviceForm.getPageSize() * 5;
        Page<Object> pageHelper = PageHelper.startPage(deviceForm.getPage(), tempPageSize);
        List<DeviceSummaryVo> list = iDeviceSummaryDao.listGroup(deviceForm);
        // 处理数据 将同一个分组日期的数据 处理成一个维度
        List<Map<String, DeviceSummaryVo>> list1 = new ArrayList<Map<String, DeviceSummaryVo>>();
        // 分组
        Map<String, List<DeviceSummaryVo>> groupBy = list.stream().collect(Collectors.groupingBy(DeviceSummaryVo::getGroupDate));
        // 排序
        LinkedHashMap<String, List<DeviceSummaryVo>> result = new LinkedHashMap<>();
        groupBy.entrySet().stream()
                .sorted(Map.Entry.<String, List<DeviceSummaryVo>>comparingByKey().reversed())
//                .sorted(Map.Entry.comparingByKey())
                .forEachOrdered(x -> result.put(x.getKey(), x.getValue()));
        for (List<DeviceSummaryVo> temp : result.values()) {
            list1.add(temp.stream().collect(Collectors.toMap(DeviceSummaryVo::getSummaryCategory, a -> a, (k1, k2) -> k1)));
        }
        Pagination<Map<String, DeviceSummaryVo>> pagination = new Pagination<>(deviceForm.getPageSize(), deviceForm.getPage());
        pagination.setData(list1);
        // pagination.setTotalPageSize(pageHelper.getTotal());
        long temp = (pageHelper.getTotal() / 5) + (pageHelper.getTotal() % 5 > 0 ? 1 : 0);
        pagination.setTotalPageSize(temp);
        return pagination;
    }

    @Override
    public Pagination<Map<String, DeviceSummaryVo>> pageInOutWater(DeviceSummaryForm deviceForm) throws FrameworkRuntimeException {

        Page<Object> pageHelper = PageHelper.startPage(deviceForm.getPage(), deviceForm.getPageSize());
//        List<DeviceSummaryVo> inWaterList = new ArrayList<>();
//        List<DeviceSummaryVo> outWaterList = new ArrayList<>();
//        if (StringUtils.isEmpty(deviceForm.getDeviceId()) && !"day".equals(deviceForm.getGroup())) {
//            // 查询进水
//            deviceForm.setSummaryType(WATER_FACTORY_IN_WATER);
//            inWaterList = this.inOutWaterListGroup(deviceForm);
//            pageHelper = PageHelper.startPage(deviceForm.getPage(), deviceForm.getPageSize() + 1);
//            // 查询出水
//            deviceForm.setSummaryType(WATER_FACTORY_OUT_WATER);
//            outWaterList = this.inOutWaterListGroup(deviceForm);
//        } else {
//            // 查询进水
//            deviceForm.setSummaryType(WATER_FACTORY_IN_WATER);
//            inWaterList = iDeviceSummaryDao.listGroup(deviceForm);
//            pageHelper = PageHelper.startPage(deviceForm.getPage(), deviceForm.getPageSize() + 1);
//            // 查询出水
//            deviceForm.setSummaryType(WATER_FACTORY_OUT_WATER);
//            outWaterList = iDeviceSummaryDao.listGroup(deviceForm);
//        }
        deviceForm.setSummaryType(WATER_FACTORY_IN_WATER_CURR);
        List<DeviceSummaryVo> inWaterList = iDeviceSummaryDao.listGroup(deviceForm);
        // 查询出水
        pageHelper = PageHelper.startPage(deviceForm.getPage(), deviceForm.getPageSize());
        deviceForm.setSummaryType(WATER_FACTORY_OUT_WATER_CURR);
        List<DeviceSummaryVo> outWaterList = iDeviceSummaryDao.listGroup(deviceForm);

        if (inWaterList.size() != outWaterList.size()) {
            throw new RuntimeException("数据有误");
        }
        List<Map<String, DeviceSummaryVo>> list = new ArrayList<>();
//        if (inWaterList.size() >= 1 && outWaterList.size() >= 1) {
//
//            // 计算 比例  自用率 等于 （进水-出水）/ 进水
//            for (int i = 0; i < inWaterList.size() - 1; i++) {
//                Map<String, DeviceSummaryVo> map = new HashMap<>();
//                DeviceSummaryVo inWater = inWaterList.get(i);
//                DeviceSummaryVo outWater = outWaterList.get(i);
//                BigDecimal in = new BigDecimal(inWater.getMaxval().toString());
//                BigDecimal out = new BigDecimal(outWater.getMaxval().toString());
//
//                // 前一天的用水量
//                BigDecimal inOld = new BigDecimal(inWaterList.get(i + 1).getMaxval().toString());
//                BigDecimal outOld = new BigDecimal(outWaterList.get(i + 1).getMaxval().toString());
//
//
//                DeviceSummaryVo inWaterTemp = BeanUtils.copyProperties(inWater, DeviceSummaryVo.class);
//                BigDecimal inWaterCurr = in.subtract(inOld);
//                inWaterTemp.setVal(inWaterCurr.doubleValue());
//
//                DeviceSummaryVo outWaterTemp = BeanUtils.copyProperties(outWater, DeviceSummaryVo.class);
//                BigDecimal outWaterCurr = out.subtract(outOld);
//                outWaterTemp.setVal(outWaterCurr.doubleValue());
//
//                BigDecimal result = inWaterCurr.subtract(outWaterCurr).divide(inWaterCurr, 4);
//                result.setScale(4, BigDecimal.ROUND_HALF_UP);
//                DeviceSummaryVo proportionVo = new DeviceSummaryVo();
//                proportionVo.setVal(result.doubleValue());
//                proportionVo.setSummaryDate(inWater.getSummaryDate());
//
//                map.put("inWater", inWaterTemp);
//                map.put("outWater", outWaterTemp);
//                map.put("proportion", proportionVo);
//                list.add(map);
//            }
//        }


        for (int i = 0; i < inWaterList.size(); i++) {
            Map<String, DeviceSummaryVo> map = new HashMap<>();
            DeviceSummaryVo in = inWaterList.get(i);
            in.setVal(Double.valueOf(in.getSumval()));
            DeviceSummaryVo out = outWaterList.get(i);
            out.setVal(Double.valueOf(out.getSumval()));

            BigDecimal inWaterCurr = new BigDecimal(in.getSumval());
            BigDecimal outWaterCurr = new BigDecimal(out.getSumval());

            BigDecimal result = inWaterCurr.subtract(outWaterCurr).divide(inWaterCurr, 4);
            result.setScale(4, BigDecimal.ROUND_HALF_UP);
            DeviceSummaryVo deviceSummaryVo = new DeviceSummaryVo();
            deviceSummaryVo.setVal(Double.valueOf(result.toString()));
            deviceSummaryVo.setSummaryDate(in.getSummaryDate());

            // proportionList.add(deviceSummaryVo);
            map.put("inWater", in);
            map.put("outWater", out);
            map.put("proportion", deviceSummaryVo);
            list.add(map);
        }


        Pagination<Map<String, DeviceSummaryVo>> pagination = new Pagination<>(deviceForm.getPageSize(), deviceForm.getPage());
        pagination.setData(list);
        pagination.setTotalPageSize(pageHelper.getTotal());
        return pagination;
    }

    @Override
    public DeviceSummaryVo getSummary(DeviceSummaryForm deviceSummaryForm) throws FrameworkRuntimeException {
        return iDeviceSummaryDao.getSummary(deviceSummaryForm);
    }

}
