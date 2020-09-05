package com.dotop.deyang.dc.model.deyang;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class DeYangParams {
    private String fromDate;
    private String toDate;
    private List<SyncId> meterNoCustomerIDList;
    private String idList;
    private String readMeterDate;
    private Integer idType;
    private String no;
}
