package com.volvo.softproduct.sensorextensionlibrary.db_enum;

/**
 * Class used as a ENUM to describe the status of a value
 */
public enum gnss_data {
    gnssTime,
    gnssQuality,
    ageOfDiff,
    longLatSqi,
    latitude,
    longitude,
    altitude,
    headingSqi,
    heading,
    cogSogSqi,
    courseOverGround,
    speedOverGround;

    public static final int BASE_ORDINAL = 300;

    public int getCode() {
        return ordinal() + BASE_ORDINAL;
    }
}