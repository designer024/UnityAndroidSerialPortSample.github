package com.ethanlin.serialportlib;

import android.icu.text.SimpleDateFormat;

import java.util.Date;
import java.util.Locale;

public class Utils {

    public static String byteArrayToHexString(byte[] aBytes) {
        StringBuilder sb = new StringBuilder(aBytes.length * 2);
        for (byte b : aBytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString().toUpperCase();
    }


    public static byte[] hexStringToByteArray(String aHex) {
        aHex = aHex.length() % 2 != 0 ? "0" + aHex : aHex;

        byte[] b = new byte[aHex.length() / 2];

        for (int i = 0; i < b.length; i++) {
            int index = i * 2;
            int v = Integer.parseInt(aHex.substring(index, index + 2), 16);
            b[i] = (byte) v;
        }
        return b;
    }
}
