package com.dotop.smartwater.project.third.server.meterread.client2.api.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.dotop.smartwater.project.third.server.meterread.client2.config.Config;
import com.dotop.smartwater.project.third.server.meterread.client2.core.third.bo.RemoteDataBo;
import com.dotop.smartwater.project.third.server.meterread.client2.core.third.vo.RemoteDataVo;
import com.dotop.smartwater.project.third.server.meterread.client2.service.IThirdDataService;
import com.dotop.smartwater.project.third.server.meterread.client2.utils.HttpClientUtils;
import com.dotop.smartwater.dependence.core.exception.BaseExceptionConstants;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.log.LogMsg;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.dependence.core.utils.JSONUtils;
import com.dotop.smartwater.project.third.server.meterread.client2.api.IThirdFactory;
import com.dotop.smartwater.project.third.server.meterread.client2.core.water.vo.WaterOwnerVo;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.message.BasicHeader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component("ThirdFactoryImpl")
public class ThirdFactoryImpl implements IThirdFactory {

    private static final Logger LOGGER = LogManager.getLogger(ThirdFactoryImpl.class);

    @Autowired
    private IThirdDataService iThirdDataService;

    /**
     * 刷新设备上行读数
     *
     * @throws FrameworkRuntimeException
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
    public void refreshDeviceUplinks() throws FrameworkRuntimeException {
        try {
            String ticket = getTicket();
            int page = 1;
            // 水务平台采集
            Pagination<WaterOwnerVo> pagination = getUplinkPage(ticket, page, Config.FIND_PAGESIZE);

            // 计算设备数据页数
            long totalPage = pagination.getTotalPageSize() / Config.FIND_PAGESIZE;
            totalPage = totalPage + (pagination.getTotalPageSize() % Config.FIND_PAGESIZE == 0 ? 0 : 1);
            // 循环查询每一页
            for (int no = 1; no < totalPage + 1; no++) {
                if (no > 1) {
                    pagination = getUplinkPage(ticket, no, Config.FIND_PAGESIZE);
                }
                List<WaterOwnerVo> waterDatas = pagination.getData();
                if (!waterDatas.isEmpty()) {
                    List<RemoteDataBo> waterList = new ArrayList<>();
                    waterDatas.forEach(waterOwner -> {
                        if (StringUtils.isBlank(waterOwner.getWater())) {
                            return;
                        }
                        RemoteDataBo remoteDataBo = new RemoteDataBo();
                        remoteDataBo.setMeterId(waterOwner.getDevno());
                        remoteDataBo.setUserCode(waterOwner.getUserno());
                        remoteDataBo.setReadDate(waterOwner.getUplinkTime());
                        remoteDataBo.setReadNumber(waterOwner.getWater());
                        waterList.add(remoteDataBo);
                    });
                    // 查询本地数据库的业主最后读数
                    List<RemoteDataVo> localList = iThirdDataService.list(waterList);
                    Map<String, RemoteDataVo> localMap = localList.stream().collect(Collectors.toMap(RemoteDataVo::getUserCode, p -> p));

                    List<RemoteDataBo> updates = waterList.stream().filter(i -> localMap.get(i.getUserCode()) != null &&
                            localMap.get(i.getUserCode()).getReadDate().getTime() < i.getReadDate().getTime()).collect(Collectors.toList());
                    List<RemoteDataBo> inserts = waterList.stream().filter(i -> localMap.get(i.getUserCode()) == null).collect(Collectors.toList());

                    // 不存在的新增
                    iThirdDataService.adds(inserts);
                    // 存在但读数已经不同的新增
                    iThirdDataService.adds(updates);
                }
            }
        } catch (FrameworkRuntimeException e) {
            LOGGER.error(LogMsg.to(e));
            throw e;
        } catch (Exception e) {
            LOGGER.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.UNDEFINED_ERROR, e.getMessage(), e);
        }
    }

    /**
     * 获取上行分页数据
     */

    private Pagination<WaterOwnerVo> getUplinkPage(String ticket, Integer page, Integer pageCount) throws FrameworkRuntimeException {
        try {
            String url = Config.SERVER_HOST + "remote/owners";
            JSONObject jsonContent = new JSONObject();
            jsonContent.put("page", page);
            jsonContent.put("pageCount", pageCount);
            String post = HttpClientUtils.post(url, new BasicHeader("ticket", ticket), jsonContent, Config.TIME_OUT);
            JSONObject result = JSONUtils.parseObject(post);
            LOGGER.info(LogMsg.to("result", result));
            Pagination<WaterOwnerVo> pagination = new Pagination<>();
            pagination.setTotalPageSize(result.getInteger("count"));
            pagination.setPageNo(page);
            List<WaterOwnerVo> list = JSON.parseObject(result.getString("data"), new TypeReference<List<WaterOwnerVo>>() {
            });
            pagination.setData(list);
            return pagination;
        } catch (Exception e) {
            LOGGER.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.UNDEFINED_ERROR, e.getMessage(), e);
        }
    }


    private String getTicket() throws FrameworkRuntimeException {
        try {
            String url = Config.SERVER_HOST + "remote/auth";
            JSONObject jsonContent = new JSONObject();
            jsonContent.put("code", "suining");
            jsonContent.put("username", "snremote");
            jsonContent.put("password", "sn123456");
            String post = HttpClientUtils.post(url, jsonContent, Config.TIME_OUT);
            JSONObject result = JSONUtils.parseObject(post);
            String data = result.getString("data");
            JSONObject datas = JSONUtils.parseObject(data);
            String ticket = datas.getString("ticket");
            return ticket;
        } catch (Exception e) {
            LOGGER.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.UNDEFINED_ERROR, e.getMessage(), e);
        }
    }

}
