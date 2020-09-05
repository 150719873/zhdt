package com.dotop.deyang.dc.mapper.deyang;

import com.dotop.deyang.dc.model.deyang.MeterArchive;
import com.dotop.deyang.dc.model.deyang.MeterChangeLog;
import com.dotop.deyang.dc.model.deyang.MeterValue;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ThirdMapper {
    List<MeterArchive> GetMeterArchive(@Param("enterpriseid") String enterpriseid, @Param("FromDate") String FromDate,
                                       @Param("ToDate") String ToDate);

    List<MeterValue> ReadMeterValue0(@Param("enterpriseid") String enterpriseid, @Param("ctime") String ctime,
                                     @Param("ids") List<String> ids, @Param("ReadMeterDate") String ReadMeterDate);

    List<MeterValue> ReadMeterValue1(@Param("enterpriseid") String enterpriseid, @Param("ctime") String ctime,
                                     @Param("ids") List<String> ids, @Param("ReadMeterDate") String ReadMeterDate);

    List<MeterChangeLog> QueryMeterChangeLog(@Param("enterpriseid") String enterpriseid,
                                             @Param("FromDate") String FromDate, @Param("ToDate") String ToDate);

}
