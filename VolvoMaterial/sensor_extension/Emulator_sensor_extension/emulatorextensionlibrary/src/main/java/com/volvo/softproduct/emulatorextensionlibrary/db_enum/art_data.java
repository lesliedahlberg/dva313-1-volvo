package com.volvo.softproduct.emulatorextensionlibrary.db_enum;

/**
 * Class used as a ENUM to describe the status of a value
 */
public enum art_data {
    Red_central_warning,
    Yellow_central_warning,
    Parking_break_applied,
    Active_gear,
    Vehicle_speed,
    Engine_speed,
    Fuel_level,
    Instant_fuel_consumption,
    Weight_in_bucket,
    Current_load,
    Machine_hours,
    Unloaded,
    Dump_body_up,
    Steering_angle,
    Outdoor_temp;

    public static final int BASE_ORDINAL = 100;

    public int getCode() {
        return ordinal() + BASE_ORDINAL;
    }
}