package com.dotop.deyang.dc.model.deyang;

public class MeterValue {
    private String MeterNo;
    private String CustomerID;
    private String Display;
    private String Status;
    private String MakeMeter;

    public String getMeterNo() {
        return MeterNo;
    }

    public void setMeterNo(String meterNo) {
        MeterNo = meterNo;
    }

    public String getCustomerID() {
        return CustomerID;
    }

    public void setCustomerID(String customerID) {
        CustomerID = customerID;
    }

    public String getDisplay() {
        return Display;
    }

    public void setDisplay(String display) {
        Display = display;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getMakeMeter() {
        return MakeMeter;
    }

    public void setMakeMeter(String makeMeter) {
        MakeMeter = makeMeter;
    }
}
