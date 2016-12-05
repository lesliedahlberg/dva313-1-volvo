package com.volvo.softproduct.emulatorextensionlibrary.helpers;

/**
 * Created by a227304 on 2016-10-12.
 */
public class can_id_map {
    private int _canId;
    private int _storageid;

    /**
     * Constructor can_id_map()
     *
     * @param    canId - ID of the CAN Frame.
     * @param    storageid - Storage row name.
     */
    public can_id_map(int canId, int storageid)
    {
        _canId = canId;
        _storageid = storageid;
    }

    /**
     * getCanId()
     *
     * @return    ID of the CAN Frame.
     */
    public int getCanId(){return _canId;};

    /**
     * getStorageId()
     *
     * @return    Storage of the signal value
     */
    public int getStorageId(){return _storageid;};

    /**
     * setCanId()
     *
     * @param    value ID of the CAN Frame.
     */
    public void setCanId(int value){_canId = value;};

    /**
     * setStorageId()
     *
     * @param    value Storage of the signal value.
     */
    public void setStorageId(int value){_storageid = value;};
}
