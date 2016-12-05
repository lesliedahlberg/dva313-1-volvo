package com.volvo.softproduct.sensorextensionlibrary.helpers;

import se.cpacsystems.rawcan.CanFrame;

/**
 * Created by a227304 on 2016-10-10.
 */
public class ttd {
    private CanFrame _canframe;
    private long _timestamp;

    /**
     * Constructor ttd()
     *
     * @param canframe  - the CAN Frame.
     * @param timestamp - time when saving.
     */
    public ttd(CanFrame canframe, long timestamp) {
        _canframe = canframe;
        _timestamp = timestamp;
    }

    /**
     * getCanFrame()
     *
     * @return the CAN Frame.
     */
    public CanFrame getCanFrame() {
        return _canframe;
    }

    /**
     * getCanFrame()
     *
     * @return the CAN Frame.
     */
    public long getTimeStamp() {
        return _timestamp;
    }
}
