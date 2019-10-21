package com.test.ble.constants;

import java.util.UUID;

/**
 * Copyright (C), 2013-2019, 深圳市浩瀚卓越科技有限公司
 * Author: Abraham.ai@hohem-tech.com
 * Date: 2019/9/25 17:22
 * Description:
 * History:
 */
public class PubConstants {

    //hgs
//    public static final UUID RX_SERVICE_UUID = UUID.fromString("6e400001-b5a3-f393-e0a9-e50e24dcca9b");
//    public static final UUID RX_CHAR_UUID = UUID.fromString("6e400002-b5a3-f393-e0a9-e50e24dcca9b");
//    public static final UUID TX_CHAR_UUID = UUID.fromString("6e400003-b5a3-f393-e0a9-e50e24dcca9b");
    //00001800-0000-1000-8000-00805f9b34fb
    //蓝牙控制器
    public static final UUID RX_SERVICE_UUID = UUID.fromString("0000ffe0-0000-1000-8000-00805f9b34fb");
    public static final UUID TX_CHAR_UUID = UUID.fromString("0000ffe2-0000-1000-8000-00805f9b34fb");
    public static final UUID RX_CHAR_UUID = UUID.fromString("0000ffe1-0000-1000-8000-00805f9b34fb");

//    FFE0  服务
//    FFE1  写入
//    FFE2  通知
    public static final UUID CCCD = UUID.fromString("00002902-0000-1000-8000-00805f9b34fb");



    public  final  static String BLE_DEVICES_ADDRESS="BLE_DEVICES_ADDRESS";
    public  final  static String BLE_NAME="BLE_NAME";


    public  final  static byte[] data={(byte) 0xAA,(byte) 0x55,(byte) 0x09,(byte) 0x00,(byte) 0xAA,(byte) 0x55,(byte) 0x07,(byte) 0x00};
}
