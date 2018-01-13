package com.likeit.sous_vide.model;

import com.machtalk.sdk.domain.DeviceAid;

/**
 * Created by Administrator on 2018/1/13.
 */

public class DeviceAidDisplay {
    DeviceAid aidInfo;
    String currentValue;

    public DeviceAid getAidInfo() {
        return aidInfo;
    }

    public void setAidInfo(DeviceAid aidInfo) {
        this.aidInfo = aidInfo;
    }

    public String getCurrentValue() {
        return currentValue;
    }

    public void setCurrentValue(String currentValue) {
        this.currentValue = currentValue;
    }
}
