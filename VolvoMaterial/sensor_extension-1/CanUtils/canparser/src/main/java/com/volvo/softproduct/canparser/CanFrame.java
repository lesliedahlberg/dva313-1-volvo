package com.volvo.softproduct.canparser;

/**
 * CanFrame class (copy of the one in CPAC SDK)
 * Created to get rid of the dependency to CPAC SDK
 */
public class CanFrame
{
    public int channel;
    public int id;
    public int dlc;
    public byte[] data;

    public CanFrame(int _ch, int _id, int _dlc, byte[] _data)
    {
        channel = _ch;
        id = _id;
        dlc = _dlc;
        data = _data;
    }
}
