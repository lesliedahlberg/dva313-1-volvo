package com.volvo.softproduct.sensorextensionlibrary.helpers;
import android.util.Log;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.channels.FileChannel;

public class usbhandler {

    private final static String LOG_TAG = "usbhandler";

    protected final static String usb1 = "/mnt/udisk";
    protected final static String usb2 = "/mnt/udisk1";

    private boolean isPathMounted(String path) {
        boolean res = false;
        try {
            // Open file that holds all mounted volumes
            FileInputStream fs = new FileInputStream("/proc/mounts");

            DataInputStream in = new DataInputStream(fs);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));

            String strLine;
            while ((strLine = br.readLine()) != null)   {
                // Check if path is mounted
                if (strLine.contains(path)) {
                    res = true;
                    break;
                }
            }

            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

    public String getActivePath()
    {
        // check usb mounted
        String path = null;
        if (isPathMounted(usb2)) {
            Log.d(LOG_TAG, "usb2 mounted");
            path = usb2;
        } else if (isPathMounted(usb1)) {
            Log.d(LOG_TAG, "usb1 mounted");
            path = usb1;
        }
        if (path != null)
        {
            return path;
        }
        else
            return "";
    }

    public boolean copyFile(File src, File dst){
        try {
            InputStream in = new FileInputStream(src);
            OutputStream out = new FileOutputStream(dst);

            // Transfer bytes from in to out
            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            in.close();
            out.close();
            return true;
        }
        catch (Exception e) {
            Log.e(LOG_TAG , "copyFile Exception: " + e.toString());
            return false;
        }
    }

    public boolean moveFile(File src, String filename) {
        // check usb mounted
        String path = null;
        if (isPathMounted(usb2)) {
            Log.d(LOG_TAG, "usb2 mounted");
            path = usb2;
        } else if (isPathMounted(usb1)) {
            Log.d(LOG_TAG, "usb1 mounted");
            path = usb1;
        }

        if (path != null)
        {
            try {
                Log.d(LOG_TAG, "Move file " + src.getAbsolutePath());
                Log.d(LOG_TAG, "Move to  " + path + filename);
                File usbFile = new File(path , filename);//The name of output file
                Log.d(LOG_TAG, "Move file " + src.getAbsolutePath() + " to " + usbFile.getAbsolutePath());
                boolean res = copyFile(src,usbFile);
                src.delete();
                return res;
            }
            catch (Exception e) {
                Log.e(LOG_TAG , "moveFile Exception: " + e.toString());
            }
            return false;
        }
        return false;
    }
}
