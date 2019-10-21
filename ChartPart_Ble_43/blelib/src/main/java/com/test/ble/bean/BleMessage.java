package com.test.ble.bean;


import com.test.ble.PubUtils;

/**
 * Copyright (C), 2013-2019, 深圳市浩瀚卓越科技有限公司
 * Author: Abraham.ai@hohem-tech.com
 * Date: 2019/9/26 10:53
 * Description: 发送蓝牙的对象
 * History:
 */
public class BleMessage {
    //是否发送成功
    boolean isSuccess=false;
    byte data[];
    public BleMessage(boolean isSuccess, byte[] data) {
        this.isSuccess = isSuccess;
        this.data = data;
    }
    public boolean isSuccess() {
        return isSuccess;
    }
    public void setSuccess(boolean success) {
        isSuccess = success;
    }
    public byte[] getData() {
        return data;
    }
    public void setData(byte[] data) {
        this.data = data;
    }
    @Override
    public String toString() {
        return "BleMessage ==>" + "isSuccess :  " + isSuccess + ", data :   " + PubUtils.getInstance().bytes2HexString(data) + '}';
    }
}
