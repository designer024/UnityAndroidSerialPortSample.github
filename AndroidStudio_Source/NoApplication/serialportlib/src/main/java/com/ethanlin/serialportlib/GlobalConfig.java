package com.ethanlin.serialportlib;

public class GlobalConfig {
    public final static String DEBUG_TAG = "EthanLinSerialPortLib";

    /**
     * Unity GameObject name
     */
    public final static String UNITY_GAME_OBJECT_NAME = "NativeBridge";

    public final static String ACTION_USB_READY = "com.ethanlin.serialportlibrary.USB_READY";
    public final static String ACTION_USB_ATTACHED = "android.hardware.usb.action.USB_DEVICE_ATTACHED";
    public final static String ACTION_USB_DETACHED = "android.hardware.usb.action.USB_DEVICE_DETACHED";
    public final static String ACTION_USB_NOT_SUPPORTED = "com.ethanlin.serialportlibrary.USB_NOT_SUPPORTED";
    public final static String ACTION_NO_USB = "com.ethanlin.serialportlibrary.NO_USB";
    public final static String ACTION_USB_PERMISSION_GRANTED = "com.ethanlin.serialportlibrary.USB_PERMISSION_GRANTED";
    public final static String ACTION_USB_PERMISSION_NOT_GRANTED = "com.ethanlin.serialportlibrary.USB_PERMISSION_NOT_GRANTED";
    public final static String ACTION_USB_DISCONNECTED = "com.ethanlin.serialportlibrary.USB_DISCONNECTED";
    public final static String ACTION_CDC_DRIVER_NOT_WORKING = "com.ethanlin.serialportlibrary.ACTION_CDC_DRIVER_NOT_WORKING";
    public final static String ACTION_USB_DEVICE_NOT_WORKING = "com.ethanlin.serialportlibrary.ACTION_USB_DEVICE_NOT_WORKING";
    public final static int MESSAGE_FROM_SERIAL_PORT = 0;
    public final static int CTS_CHANGE = 1;
    public final static int DSR_CHANGE = 2;

    public final static String ACTION_USB_PERMISSION = "com.ethanlin.serialportlibrary.USB_PERMISSION";
    public final static String ACTION_ATTACHED = "UsbAttached";
    public final static String ACTION_DETACHED = "UsbDetached";

}
