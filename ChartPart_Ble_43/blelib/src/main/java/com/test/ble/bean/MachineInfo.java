package com.test.ble.bean;

import java.util.Arrays;

/**
 * Copyright (C), 2013-2019, 深圳市浩瀚卓越科技有限公司
 * Author: Abraham.ai@hohem-tech.com
 * Date: 2019/9/24 13:37
 * Description: 电机信息
 * History:
 */
public class MachineInfo {
    //第几帧
    int lenght;
    byte[] data;
    int id ; //电机唯一标识  id=0x81  0x82 0x83
    public MachineInfo(int lenght, byte[] data) {
        this.lenght = lenght;
        this.data = data;
    }
    @Override
    public String toString() {
        return "MachineInfo{" +
                "lenght=" + lenght +
                ", data=" + Arrays.toString(data) +
                '}';
    }

    public int getLenght() {
        return lenght;
    }

    public void setLenght(int lenght) {
        this.lenght = lenght;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

   ;
}
