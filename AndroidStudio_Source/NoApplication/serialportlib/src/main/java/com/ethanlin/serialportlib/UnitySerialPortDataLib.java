package com.ethanlin.serialportlib;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbManager;
import android.util.Base64;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.hoho.android.usbserial.driver.UsbSerialDriver;
import com.hoho.android.usbserial.driver.UsbSerialPort;
import com.hoho.android.usbserial.driver.UsbSerialProber;
import com.hoho.android.usbserial.util.SerialInputOutputManager;
import com.unity3d.player.UnityPlayer;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class UnitySerialPortDataLib {
    /**
     * 實例
     */
    @SuppressLint("StaticFieldLeak")
    private static UnitySerialPortDataLib mInstance;
    /**
     * 實例
     */
    public static UnitySerialPortDataLib getInstance() {
        if (mInstance == null) {
            mInstance = new UnitySerialPortDataLib();
        }
        return mInstance;
    }

    private Context mContext;

    private UsbManager mUsbManager;
    private UsbSerialDriver mUsbSerialDriver;
    private UsbDevice mUsbDevice;

    private UsbDeviceConnection mConnection;
    private UsbSerialPort mSerialPort;

    private byte[] mSerialData;

    public static String deviceInfo = "";

    private final IntentFilter mReceiverFilter = new IntentFilter();

    /**
     * init UsbManager and broad receiver
     */
    public void initSerialPortManagerAndReceiver() {
        mContext = UnityPlayer.currentContext;
        mUsbManager = (UsbManager) mContext.getSystemService(Context.USB_SERVICE);

        setReceiverFilter();
        mContext.registerReceiver(mUSBStatusReceiver, mReceiverFilter);

        android.util.Log.d(GlobalConfig.DEBUG_TAG, "init SerialPortManagerAndReceiver done");
    }

    /**
     * set broad receiver filter
     */
    private void setReceiverFilter() {
        mReceiverFilter.addAction(UsbManager.ACTION_USB_DEVICE_ATTACHED);
        mReceiverFilter.addAction(UsbManager.ACTION_USB_DEVICE_DETACHED);
        mReceiverFilter.addAction(GlobalConfig.ACTION_NO_USB);
        mReceiverFilter.addAction(GlobalConfig.ACTION_USB_PERMISSION);
        android.util.Log.d(GlobalConfig.DEBUG_TAG, "set ReceiverFilter");
    }


    /**
     * find serial port device
     */
    public void findSerialPortDevice() {
        List<UsbSerialDriver> availableDrivers = UsbSerialProber.getDefaultProber().findAllDrivers(mUsbManager);
        if (availableDrivers.isEmpty()) {
            UnityPlayer.UnitySendMessage(GlobalConfig.UNITY_GAME_OBJECT_NAME, "getScanResult", "0");
            Toast.makeText(mContext, mContext.getString(R.string.there_is_no_serial_port_device), Toast.LENGTH_SHORT).show();
            android.util.Log.w(GlobalConfig.DEBUG_TAG, mContext.getString(R.string.there_is_no_serial_port_device));
            return;
        }

        // UnityPlayer.UnitySendMessage(GlobalConfig.UNITY_GAME_OBJECT_NAME, "getScanResult", String.valueOf(availableDrivers.size()));
        mUsbSerialDriver = availableDrivers.get(0);
        mUsbDevice = mUsbSerialDriver.getDevice();
        deviceInfo = String.format("VID: %s, PID: %s, ManufacturerName: %s, ProduceName: %s", String.format(Locale.US, "%04X", mUsbDevice.getVendorId()), String.format(Locale.US, "%04X", mUsbDevice.getProductId()), mUsbDevice.getManufacturerName(), mUsbDevice.getProductName());
        boolean hasPermission = mUsbManager.hasPermission(mUsbDevice);
        if (!hasPermission) {
            PermissionHelper.requestUsbPermission(mContext, mUsbManager, mUsbDevice, GlobalConfig.ACTION_USB_PERMISSION);
            return;
        }

        UnityPlayer.UnitySendMessage(GlobalConfig.UNITY_GAME_OBJECT_NAME, "getScanResult", deviceInfo);
        android.util.Log.d(GlobalConfig.DEBUG_TAG, deviceInfo);
        Toast.makeText(mContext, deviceInfo, Toast.LENGTH_SHORT).show();
    }

    public void requestUsbPermission() {
        boolean hasPermission = getUsbPermission();
        if (!hasPermission) {
            PermissionHelper.requestUsbPermission(mContext, mUsbManager, mUsbDevice, GlobalConfig.ACTION_USB_PERMISSION);
        }
    }
    public boolean getUsbPermission() { return mUsbManager.hasPermission(mUsbDevice); }

    /**
     * open serial port
     */
    public void openSerialPort(int aBaudRate, int aDataBits, int aStopBits, int aParity) {
        if (mUsbDevice == null) {
            findSerialPortDevice();
        }

        boolean hasPermission = mUsbManager.hasPermission(mUsbDevice);
        if (!hasPermission) {
            PermissionHelper.requestUsbPermission(mContext, mUsbManager, mUsbDevice, GlobalConfig.ACTION_USB_PERMISSION);
            return;
        }

        mConnection = mUsbManager.openDevice(mUsbDevice);
        mSerialPort = mUsbSerialDriver.getPorts().get(0);

        if (mSerialPort.isOpen()) {
            return;
        }

        try {
            mSerialPort.open(mConnection);
            mSerialPort.setParameters(aBaudRate, aDataBits, aStopBits, aParity);

        } catch (IOException aIOException) {
            android.util.Log.e(GlobalConfig.DEBUG_TAG, String.format("IOException: %s", aIOException.getMessage()));
            throw new RuntimeException(aIOException);
        }

        SerialInputOutputManager serialInputOutputManager = getSerialInputOutputManager();

        android.util.Log.d(GlobalConfig.DEBUG_TAG, String.format("is serial port opened? %b -----> ", mSerialPort.isOpen()));
        Toast.makeText(mContext, String.format("is serial port opened? %b -----> ", mSerialPort.isOpen()), Toast.LENGTH_SHORT).show();
        if (mSerialPort.isOpen()) {
            serialInputOutputManager.start();
        } else {
            Toast.makeText(mContext, "Serial port is not opened!!!", Toast.LENGTH_SHORT).show();
            android.util.Log.d(GlobalConfig.DEBUG_TAG, "Serial port is not opened!!!");
        }
    }


    private @NonNull SerialInputOutputManager getSerialInputOutputManager() {
        SerialInputOutputManager serialInputOutputManager = new SerialInputOutputManager(mSerialPort, new SerialInputOutputManager.Listener() {
            @Override
            public void onNewData(byte[] aDataBytes) {
                if (aDataBytes != null && aDataBytes.length > 0) {
                    mSerialData = aDataBytes;
                    String dataString = Base64.encodeToString(mSerialData, Base64.DEFAULT);
                    UnityPlayer.UnitySendMessage(GlobalConfig.UNITY_GAME_OBJECT_NAME, "onSerialDataReceived", dataString);
                    android.util.Log.d(GlobalConfig.DEBUG_TAG, String.format("onNewData: %s", dataString));
                }
            }

            @Override
            public void onRunError(Exception aException) {
                android.util.Log.e(GlobalConfig.DEBUG_TAG, String.format("error: %s", aException.getMessage()));
            }
        });
        return serialInputOutputManager;
    }


    private final BroadcastReceiver mUSBStatusReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context aContext, Intent aIntent) {
            if (aIntent.getAction() != null) {
                switch (aIntent.getAction()) {
                    case GlobalConfig.ACTION_USB_PERMISSION:
                        if (mUsbDevice != null) {
                            boolean hasPermission = mUsbManager.hasPermission(mUsbDevice);
                            if (!hasPermission) {
                                PermissionHelper.requestUsbPermission(mContext, mUsbManager, mUsbDevice, GlobalConfig.ACTION_USB_PERMISSION);
                                return;
                            }
                        }

                        android.util.Log.d(GlobalConfig.DEBUG_TAG, "usb device has authorized");
                        Toast.makeText(mContext, mContext.getString(R.string.usb_device_has_permission), Toast.LENGTH_SHORT).show();
                        break;

                    case UsbManager.ACTION_USB_DEVICE_ATTACHED:
                        Toast.makeText(mContext, mContext.getString(R.string.usb_device_attached), Toast.LENGTH_SHORT).show();
                        android.util.Log.d(GlobalConfig.DEBUG_TAG, "usb device attached");
                        findSerialPortDevice();
                        break;

                    case UsbManager.ACTION_USB_DEVICE_DETACHED:
                        Toast.makeText(mContext, mContext.getString(R.string.usb_device_detached), Toast.LENGTH_SHORT).show();
                        android.util.Log.d(GlobalConfig.DEBUG_TAG, "usb device detached");
                        if (mSerialPort != null && mSerialPort.isOpen()) {
                            try {
                                mSerialPort.close();
                            } catch (IOException aIOException) {
                                throw new RuntimeException(aIOException);
                            }
                        } else {
                            android.util.Log.e(GlobalConfig.DEBUG_TAG, "mSerialPort is null");
                        }
                        break;
                }
            } else {
                android.util.Log.e(GlobalConfig.DEBUG_TAG, "Intent.getAction is null!!!");
            }
        }
    };
}
