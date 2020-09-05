package com.dotop.smartwater.project.module.api.revenue.impl;

import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.dependence.core.utils.DateUtils;
import com.dotop.smartwater.project.module.api.revenue.IQueryFactory;
import com.dotop.smartwater.project.module.core.auth.local.AuthCasClient;
import com.dotop.smartwater.project.module.core.auth.vo.AreaNodeVo;
import com.dotop.smartwater.project.module.core.auth.vo.UserVo;
import com.dotop.smartwater.project.module.core.water.bo.DictionaryChildBo;
import com.dotop.smartwater.project.module.core.water.bo.OwnerBo;
import com.dotop.smartwater.project.module.core.water.bo.customize.QueryParamBo;
import com.dotop.smartwater.project.module.core.water.bo.customize.StatisticsWaterBo;
import com.dotop.smartwater.project.module.core.water.constants.DictionaryCode;
import com.dotop.smartwater.project.module.core.water.constants.ResultCode;
import com.dotop.smartwater.project.module.core.water.form.DeviceUplinkForm;
import com.dotop.smartwater.project.module.core.water.form.OwnerForm;
import com.dotop.smartwater.project.module.core.water.form.customize.QueryForm;
import com.dotop.smartwater.project.module.core.water.form.customize.StatisticsWaterForm;
import com.dotop.smartwater.project.module.core.water.utils.CalUtil;
import com.dotop.smartwater.project.module.core.water.vo.DeviceDownlinkVo;
import com.dotop.smartwater.project.module.core.water.vo.DictionaryChildVo;
import com.dotop.smartwater.project.module.core.water.vo.OwnerVo;
import com.dotop.smartwater.project.module.core.water.vo.customize.OriginalVo;
import com.dotop.smartwater.project.module.core.water.vo.customize.StatisticsWaterVo;
import com.dotop.smartwater.project.module.service.device.IDeviceDownlinkService;
import com.dotop.smartwater.project.module.service.device.IDeviceService;
import com.dotop.smartwater.project.module.service.device.IDeviceUplinkService;
import com.dotop.smartwater.project.module.service.revenue.IEverydayWaterRecordService;
import com.dotop.smartwater.project.module.service.revenue.IOwnerService;
import com.dotop.smartwater.project.module.service.tool.IDictionaryChildService;
import com.dotop.water.tool.service.BaseInf;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

/**

 * @date 2019/2/28.
 */
@Component
public class QueryFactoryImpl implements IQueryFactory {

    @Value("${param.revenue.excelTempUrl}")
    private String excelTempUrl;

    @Autowired
    private IDeviceService iDeviceService;

    @Autowired
    private IDictionaryChildService iDictionaryChildService;

    @Autowired
    private IOwnerService iOwnerService;

    @Autowired
    private IDeviceUplinkService iDeviceUplinkService;

    @Autowired
    private IEverydayWaterRecordService iEverydayWaterRecordService;

    @Autowired
    private IDeviceDownlinkService iDeviceDownlinkService;

    @Override
    public Map<String, Object> getDataTotal() throws FrameworkRuntimeException {
        UserVo user = AuthCasClient.getUser();
        Map<String, Object> data = new HashMap<>(16);

        // 设备总数
        Long deviceCount = iDeviceService.getDeviceCount(user.getEnterpriseid());
        // 传统表数量
        Long traditions = iDeviceService.traditionCount(user.getEnterpriseid());
        // 远传表数量
        Long remotes = iDeviceService.remoteCount(user.getEnterpriseid());
        // 远传-离线数量
        Long offlineCount = iDeviceService.offlineCount(user.getEnterpriseid());
        // 已开户总数
        Long userCount = iOwnerService.userOpenCount(user.getEnterpriseid());
        // 小区数量
        Long areaCount = iOwnerService.areaCount(user.getEnterpriseid());
        // 月用水量
        Double monthWaters = iEverydayWaterRecordService.monthWater(DateUtils.formatDateMonth(new Date()),user.getEnterpriseid());
        data.put("devices", deviceCount);
        data.put("traditions", traditions);
        data.put("remotes", remotes);
        data.put("offlineCount", offlineCount);
        data.put("onlines", CalUtil.sub(remotes, offlineCount));
        data.put("userCount", userCount);
        data.put("areaCount", areaCount);
        data.put("monthWaters", monthWaters);
        
        /*// 未激活数量
        Long noactives = iDeviceService.noactiveCount(user.getEnterpriseid());
        data.put("noactives", noactives);

        // 储存数量
        Long storages = iDeviceService.storageCount(user.getEnterpriseid());
        data.put("storages", storages);

        // 报废数量
        Long scraps = iDeviceService.scrapCount(user.getEnterpriseid());
        data.put("scraps", scraps);

        // 当天未上报数量
        Long unreporteds = iDeviceService.unreportedsCount(user.getEnterpriseid());
        data.put("unreporteds", unreporteds);*/
        return data;
    }

    @Override
    public Pagination<OriginalVo> original(QueryForm queryForm) throws FrameworkRuntimeException {

        QueryParamBo param = checkQueryForm(queryForm);
        int page = queryForm.getPage();
        int pageCount = queryForm.getPageCount();
        param.setDeveui(queryForm.getDeveui());
        param.setPage(page);
        param.setPageCount(pageCount);

        Pagination<OriginalVo> pagination = null;
        // 当月
        if (param.getStartMonth().equals(param.getEndMonth())) {
            pagination = iDeviceUplinkService.findOriginal(param);
        } else {
            // 跨月
            pagination = iDeviceUplinkService.findOriginalCrossMonth(param);
        }
        if (pagination != null && pagination.getData() != null && pagination.getData().size() > 0) {
            DictionaryChildBo bo = new DictionaryChildBo();
            bo.setDictionaryId(DictionaryCode.DEVICE_UPLOAD_REASON);
            List<DictionaryChildVo> list = iDictionaryChildService.list(bo);
            if (list.size() > 0) {
                Map<String, String> map = list.stream()
                        .collect(Collectors.toMap(x -> x.getChildValue(), x -> x.getChildName(), (oldValue, newValue) -> oldValue));
                for (OriginalVo vo : pagination.getData()) {
                    String reason = map.get(vo.getReason());
                    if (reason != null) {
                        vo.setReason(reason);
                    }
                }
            }
        }
        return pagination;
    }

    @Override
    public Pagination<DeviceDownlinkVo> getHistory(QueryForm queryForm) {
        QueryParamBo param = checkQueryForm(queryForm);

        int page = queryForm.getPage();
        int pageCount = queryForm.getPageCount();

        param.setPage(page);
        param.setPageCount(pageCount);

        return iDeviceDownlinkService.getHistory(param);
    }

    public static QueryParamBo checkQueryForm(QueryForm queryForm) throws FrameworkRuntimeException {
		/*if (queryForm.getNodeIds() == null) {
			throw new FrameworkRuntimeException(ResultCode.ParamIllegal, "请选择区域！");
		}*/

        UserVo user = AuthCasClient.getUser();

        String start = null;
        String end = null;
        String currentDate = DateUtils.format(new Date(), DateUtils.YYYYMM);
        String followedDate = currentDate;

        // 空值则取默认值
        if (StringUtils.isEmpty(queryForm.getTimerange())) {
            start = DateUtils.day(new Date(), 0, DateUtils.DATE);
            end = DateUtils.day(new Date(), 1, DateUtils.DATE);
        } else {
            String[] date = queryForm.getTimerange().split("-");
            start = date[0] + "-" + date[1] + "-" + date[2];
            end = date[3] + "-" + date[4] + "-" + date[5];
            currentDate = date[0] + date[1];

            Date s = DateUtils.parseDate(start);
            Date e = DateUtils.parseDate(end);
            if (e != null) {
                end = DateUtils.day(e, 1, DateUtils.DATE);
                followedDate = end.split("-")[0] + end.split("-")[1];
                if (DateUtils.compare(s, e)) {
                    throw new FrameworkRuntimeException(ResultCode.ParamIllegal, "结束时间应该大于开始时间");
                }

                // 判断跨度是否大于2个月
                if (DateUtils.monthsBetween(s, e) > 1) {
                    throw new FrameworkRuntimeException(ResultCode.ParamIllegal, "查询跨度不能大于1个月");
                }
                // 月末的特殊处理
                if ("01".equals(end.split("-")[2])) {
                    followedDate = DateUtils.format(e, DateUtils.YYYYMM);
                }

                //防止查询到数据库没有的表出错（上行表是分表的）
                Date lowLimit = DateUtils.parseDate("2017-12-31");
                if (!DateUtils.compare(s, lowLimit)) {
                    throw new FrameworkRuntimeException(ResultCode.ParamIllegal, "2018年之前没有数据");
                }

				/*Date day = new Date();
				if(!DateUtils.compare(day,e)){
					throw new FrameworkRuntimeException(ResultCode.ParamIllegal, "不能查找今天之后的数据");
				}*/

            } else {
                throw new FrameworkRuntimeException(ResultCode.ParamIllegal, "结束时间参数格式错误");
            }
        }

        QueryParamBo param = new QueryParamBo();
        param.setEnterpriseid(user.getEnterpriseid());
        param.setList(queryForm.getNodeIds());
        param.setUserno(queryForm.getUserno());
        param.setUsername(queryForm.getUsername());
        param.setDevno(queryForm.getDevno());
        param.setPhone(queryForm.getUserphone());
        param.setStart(start);
        param.setEnd(end);

        param.setSystime(currentDate);
        param.setStartMonth(currentDate);
        param.setEndMonth(followedDate);

        if (StringUtils.isEmpty(queryForm.getCommunityid()) || user.getEnterpriseid().equals(queryForm.getCommunityid())) {
            param.setFlag(null);
        }
        // 搜索出开户的水表上传的数据
        else {
            param.setFlag("1");
        }

        return param;
    }

    @Override
    public List<StatisticsWaterVo> dailyGetStatisticsWater(StatisticsWaterForm statisticsWaterForm)
            throws FrameworkRuntimeException {
        UserVo user = AuthCasClient.getUser();

        StatisticsWaterBo statisticsWaterBo = new StatisticsWaterBo();
        BeanUtils.copyProperties(statisticsWaterForm, statisticsWaterBo);

        if (StringUtils.isEmpty(statisticsWaterBo.getSearchDate())) {
            statisticsWaterBo.setSearchDate(DateUtils.format(new Date(), DateUtils.DATE));
        }

        Map<String, AreaNodeVo> map = BaseInf.getAreaMaps(user.getUserid(), user.getTicket());
        if (map == null || map.size() == 0) {
            return Collections.emptyList();
        }
        StringBuilder ids = new StringBuilder();
        for (String key : map.keySet()) {
            ids.append(key).append(",");
        }
        String str = ids.toString().substring(0, ids.length() - 1);

        statisticsWaterBo.setCommunityids(str);
        statisticsWaterBo.setEnterpriseid(user.getEnterpriseid());

        return iEverydayWaterRecordService.dailyGetStatisticsWater(statisticsWaterBo);

    }

    @Override
    public List<StatisticsWaterVo> monthGetStatisticsWater(StatisticsWaterForm statisticsWaterForm)
            throws FrameworkRuntimeException {
        UserVo user = AuthCasClient.getUser();

        StatisticsWaterBo statisticsWaterBo = new StatisticsWaterBo();
        BeanUtils.copyProperties(statisticsWaterForm, statisticsWaterBo);

        if (StringUtils.isEmpty(statisticsWaterBo.getSearchDate())) {
            statisticsWaterBo.setSearchDate(DateUtils.format(new Date(), DateUtils.YYYY_MM));
        }

        Map<String, AreaNodeVo> map = BaseInf.getAreaMaps(user.getUserid(), user.getTicket());
        if (map == null || map.size() == 0) {
            return Collections.emptyList();
        }
        StringBuilder ids = new StringBuilder();
        for (String key : map.keySet()) {
            ids.append(key).append(",");
        }
        String str = ids.toString().substring(0, ids.length() - 1);

        statisticsWaterBo.setCommunityids(str);
        statisticsWaterBo.setEnterpriseid(user.getEnterpriseid());

        List<StatisticsWaterVo> list = iEverydayWaterRecordService.monthGetStatisticsWater(statisticsWaterBo);
        List<StatisticsWaterVo> list2 = new ArrayList<>();
        for (StatisticsWaterVo cd : list) {
            AreaNodeVo areaNode = map.get(String.valueOf(cd.getCommunityid()));
            if (areaNode != null) {
                cd.setCommunityname(areaNode.getTitle());
                list2.add(cd);
            }
        }

        return list2;
    }

    @Override
    public OwnerVo ownerinfo(OwnerForm ownerForm) throws FrameworkRuntimeException {
        UserVo user = AuthCasClient.getUser();
        OwnerBo ownerBo = new OwnerBo();
        BeanUtils.copyProperties(ownerForm, ownerBo);

        OwnerVo ow = iOwnerService.getByOwner(ownerBo);
        if (ow != null) {
            Map<String, AreaNodeVo> map = BaseInf.getAreaMaps(user.getUserid(), user.getTicket());
            if (map != null) {
                AreaNodeVo a = map.get(String.valueOf(ow.getCommunityid()));
                ow.setCommunityname(a != null ? a.getTitle() : "未知");
            }
        }
        return ow;
    }

    @Override
    public OriginalVo getOriginalByIdAndDate(DeviceUplinkForm deviceUplinkForm) throws FrameworkRuntimeException {

        String date = deviceUplinkForm.getDate().substring(0, 4) + deviceUplinkForm.getDate().substring(5, 7);
        OriginalVo vo = iDeviceUplinkService.getOriginalByIdAndDate(deviceUplinkForm.getId(), date);
        if (vo != null && vo.getReason() != null) {
            DictionaryChildBo bo = new DictionaryChildBo();
            bo.setDictionaryId(DictionaryCode.DEVICE_UPLOAD_REASON);
            List<DictionaryChildVo> list = iDictionaryChildService.list(bo);
            if (list.size() > 0) {
                Map<String, String> map = list.stream()
                        .collect(Collectors.toMap(x -> x.getChildValue(), x -> x.getChildName(), (oldValue, newValue) -> oldValue));
                vo.setReason(map.get(vo.getReason()));
            }
        }
        return vo;
    }
}
