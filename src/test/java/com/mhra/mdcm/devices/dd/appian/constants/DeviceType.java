package com.mhra.mdcm.devices.dd.appian.constants;

/**
 * Created by TPD_Auto on 22/12/2016.
 */
public enum DeviceType {

    GENERAL_MEDICAL_DEVICE("General Medical Device"),
    GENERAL_MEDICAL_DEVICE_SHORT("GMD"),
    IN_VITRO_DIAGNOSTIC_DEVICE("In-Vitro Diagnostic Device"),
    IN_VITRO_DIAGNOSTIC_DEVICE_SHORT("IVD"),
    ACTIVE_IMPLANTABLE_MEDICAL_DEVICE("Active Implantable Medical Device"),
    ACTIVE_IMPLANTABLE_MEDICAL_DEVICE_SHORT("AIMD"),
    SYSTEM_PROCEDURE_PACK("System or Procedure Pack"),
    SYSTEM_PROCEDURE_PACK_SHORT("SPP");

    private String deviceType;

    DeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public String getDeviceType(){
        return this.deviceType;
    }
}
