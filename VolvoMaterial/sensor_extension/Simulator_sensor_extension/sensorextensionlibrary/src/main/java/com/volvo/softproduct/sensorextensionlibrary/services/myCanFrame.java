package com.volvo.softproduct.sensorextensionlibrary.services;

/**
 * Created by a227304 on 2016-07-08.
 */
public class myCanFrame {
    public int channel;
    public int id;
    public int dlc;
    public byte[] data;

    public myCanFrame(int _ch, int _id, int _dlc, byte[] _data) {
        this.channel = _ch;
        this.id = _id;
        this.dlc = _dlc;
        this.data = _data;
    }
}
