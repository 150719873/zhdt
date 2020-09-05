package com.dotop.deyang.dc.service.deyang;



import com.dotop.deyang.dc.model.deyang.MeterArchive;
import com.dotop.deyang.dc.model.deyang.MeterChangeLog;
import com.dotop.deyang.dc.model.deyang.MeterValue;

import java.util.List;

public interface IThirdFactory {
    List<MeterArchive> GetMeterArchive(String FromDate, String ToDate);

    int SyncCustomerID(String MeterNoCustomerIDList);

    List<MeterChangeLog> QueryMeterChangeLog(String FromDate, String ToDate);

    /**
     * 根据水表编号获取水表读数
     */
    List<MeterValue> ReadMeterValue0(List<String> ids, String readMeterDate);

    /**
     * 根据收费号获取水表读数
     */
    List<MeterValue> ReadMeterValue1(List<String> ids, String readMeterDate);
}
