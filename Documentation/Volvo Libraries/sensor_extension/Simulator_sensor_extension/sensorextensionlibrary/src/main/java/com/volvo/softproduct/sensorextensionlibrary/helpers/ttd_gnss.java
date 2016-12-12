package com.volvo.softproduct.sensorextensionlibrary.helpers;

import se.cpacsystems.rawcan.CanFrame;

/**
 * Created by a227304 on 2016-10-10.
 */
public class ttd_gnss {
    public long Timestamp;
    public long GnssTime;
    public byte GnssQuality;
    public double AgeOfDiff;
    public byte LongLatSqi;
    public double Latitude;
    public double Longitude;
    public double Altitude;
    public byte HeadingSqi;
    public double Heading;
    public byte CogSogSqi;
    public double CourseOverGround;
    public double SpeedOverGround;

    /**
     * Constructor ttd_gnss()
     *
     * @param gnssTime  - time when GNSS position was set.
     * @param gnssQuality  - .
     * @param ageOfDiff  - t.
     * @param longLatSqi  - .
     * @param latitude  - .
     * @param longitude  - .
     * @param altitude - .
     * @param headingSqi - .
     * @param heading - .
     * @param cogSogSqi - .
     * @param courceOverGround- .
     * @param speedOverGround- .
     * @param timestamp - time when saving.
     */
    public ttd_gnss(long gnssTime, byte gnssQuality, double ageOfDiff, byte longLatSqi, double latitude, double longitude,
                    double altitude, byte headingSqi, double heading, byte cogSogSqi, double courseOverGround,
                    double speedOverGround, long timestamp) {
        Timestamp = timestamp;
        GnssTime = gnssTime;
        GnssQuality = gnssQuality;
        AgeOfDiff = ageOfDiff;
        Longitude = longitude;
        Latitude = latitude;
        LongLatSqi = longLatSqi;
        Altitude = altitude;
        HeadingSqi = headingSqi;
        Heading = heading;
        CogSogSqi = cogSogSqi;
        CourseOverGround = courseOverGround;
        SpeedOverGround = speedOverGround;
    }
}
