package com.ethanlin.serialportlib;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;

public class PermissionHelper {

    public static void requestUsbPermission(Context aContext, UsbManager aUsbManager, UsbDevice aUsbDevice, String aIntentString) {
        aUsbManager.requestPermission(aUsbDevice, PendingIntent.getBroadcast(aContext, 0, new Intent(aIntentString), PendingIntent.FLAG_IMMUTABLE));
    }
}
