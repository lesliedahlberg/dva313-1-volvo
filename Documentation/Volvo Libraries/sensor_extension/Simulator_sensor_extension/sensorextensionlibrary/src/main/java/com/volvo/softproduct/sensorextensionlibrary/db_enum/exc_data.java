package com.volvo.softproduct.sensorextensionlibrary.db_enum;

/**
 * Class used as a ENUM to describe the status of a value
 */

public enum exc_data {
    Red_central_warning,
    Yellow_central_warning,
    Tool_lock_active,
    Parking_break_applied,
    Vehicle_speed,
    Engine_speed,
    Fuel_level,
    Instant_fuel_consumption,
    Weight_in_bucket,
    Current_load,
    Machine_hours,
    Outdoor_temp;

    public static final int BASE_ORDINAL = 200;

    public int getCode() {
        return ordinal() + BASE_ORDINAL;
    }
}