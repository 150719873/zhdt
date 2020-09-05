package com.dotop.deyang.dc.model.deyang;

public class MeterChangeLog {
    private String CustomerID;
    private String OldMeterNo;
    private String OldMeterDisplay;
    private String NewMeterNo;
    private String NewMeterDisplay;
    private String ChangeMeterDate;

    public String getCustomerID() {
        return CustomerID;
    }

    public void setCustomerID(String customerID) {
        CustomerID = customerID;
    }

    public String getOldMeterNo() {
        return OldMeterNo;
    }

    public void setOldMeterNo(String oldMeterNo) {
        OldMeterNo = oldMeterNo;
    }

    public String getOldMeterDisplay() {
        return OldMeterDisplay;
    }

    public void setOldMeterDisplay(String oldMeterDisplay) {
        OldMeterDisplay = oldMeterDisplay;
    }

    public String getNewMeterNo() {
        return NewMeterNo;
    }

    public void setNewMeterNo(String newMeterNo) {
        NewMeterNo = newMeterNo;
    }

    public String getNewMeterDisplay() {
        return NewMeterDisplay;
    }

    public void setNewMeterDisplay(String newMeterDisplay) {
        NewMeterDisplay = newMeterDisplay;
    }

    public String getChangeMeterDate() {
        return ChangeMeterDate;
    }

    public void setChangeMeterDate(String changeMeterDate) {
        ChangeMeterDate = changeMeterDate;
    }
}
