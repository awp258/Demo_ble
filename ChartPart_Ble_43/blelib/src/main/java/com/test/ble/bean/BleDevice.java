package com.test.ble.bean;

import android.bluetooth.BluetoothDevice;

/**
 * Copyright (C), 2013-2019, 深圳市浩瀚卓越科技有限公司
 * Author: Abraham.ai@hohem-tech.com
 * Date: 2019/9/25 14:58
 * Description:
 * History:
 */
public class BleDevice {
    //链接状态
    private  boolean isConnection;
    private  int rssi;
    private  BluetoothDevice bluetoothDevice;
    public boolean isConnection() {
        return isConnection;
    }

    public BleDevice(boolean isConnection, int rssi, BluetoothDevice bluetoothDevice) {
        this.isConnection = isConnection;
        this.rssi = rssi;
        this.bluetoothDevice = bluetoothDevice;
    }

    public void setConnection(boolean connection) {
        isConnection = connection;
    }

    public int getRssi() {
        return rssi;
    }

    public void setRssi(int rssi) {
        this.rssi = rssi;
    }

    public BluetoothDevice getBluetoothDevice() {
        return bluetoothDevice;
    }

    public void setBluetoothDevice(BluetoothDevice bluetoothDevice) {
        this.bluetoothDevice = bluetoothDevice;
    }

    @Override
    public String toString() {
        return "BleDevice{" +
                "isConnection=" + isConnection +
                ", rssi=" + rssi +
                ", bluetoothDevice=" + bluetoothDevice.toString() +
                '}';
    }
}
