package com.dotop.deyang.dc.service.deyang.impl;

import com.dotop.deyang.dc.service.deyang.IThirdFactory;
import com.dotop.deyang.util.DateUtils;
import com.dotop.deyang.dc.mapper.deyang.ThirdMapper;
import com.dotop.deyang.dc.model.deyang.MeterArchive;
import com.dotop.deyang.dc.model.deyang.MeterChangeLog;
import com.dotop.deyang.dc.model.deyang.MeterValue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

@Component
public class ThirdFactoryImpl implements IThirdFactory {

    @Value("${server.enterpriseid}")
    private String enterpriseid;

    @Resource
    private ThirdMapper iThirdDao;

    @Override
    public List<MeterArchive> GetMeterArchive(String FromDate, String ToDate) {
        System.out.println("enterpriseid:" + enterpriseid);
        List<MeterArchive> list = iThirdDao.GetMeterArchive(enterpriseid, FromDate, ToDate);
        return list;
    }

    @Override
    public int SyncCustomerID(String MeterNoCustomerIDList) {
        return 0;
    }

    @Override
    public List<MeterValue> ReadMeterValue0(List<String> ids, String ReadMeterDate) {
        String ctime = DateUtils.format(ReadMeterDate, DateUtils.DATE, DateUtils.YYYYMM);
        List<MeterValue> list = iThirdDao.ReadMeterValue0(enterpriseid, ctime, ids, ReadMeterDate);
        return list;
    }

    @Override
    public List<MeterValue> ReadMeterValue1(List<String> ids, String ReadMeterDate) {
        String ctime = DateUtils.format(ReadMeterDate, DateUtils.DATE, DateUtils.YYYYMM);
        List<MeterValue> list = iThirdDao.ReadMeterValue1(enterpriseid, ctime, ids, ReadMeterDate);
        return list;
    }

    @Override
    public List<MeterChangeLog> QueryMeterChangeLog(String FromDate, String ToDate) {
        List<MeterChangeLog> list = iThirdDao.QueryMeterChangeLog(enterpriseid, FromDate, ToDate);
        return list;
    }

}
