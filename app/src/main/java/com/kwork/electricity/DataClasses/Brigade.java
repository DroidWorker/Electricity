package com.kwork.electricity.DataClasses;

public class Brigade {
    String brigadeId;
    String brigadeStatus;

    public String getBrigadeId() {
        return brigadeId;
    }

    public void setBrigadeId(String brigadeId) {
        this.brigadeId = brigadeId;
    }

    public String getBrigadeStatus() {
        return brigadeStatus;
    }

    public void setBrigadeStatus(String brigadeStatus) {
        this.brigadeStatus = brigadeStatus;
    }

    public Brigade(String id, String status){
        this.brigadeId = id;
        this.brigadeStatus = status;
    }
}
