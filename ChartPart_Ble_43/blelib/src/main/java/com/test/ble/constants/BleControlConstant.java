package com.test.ble.constants;

/**
 * Copyright (C), 2013-2019, 深圳市浩瀚卓越科技有限公司
 * Author: Abraham.ai@hohem-tech.com
 * Date: 2019/9/25 10:44
 * Description:  蓝牙操控器常量配置
 * History:
 */
public class BleControlConstant {

   //升级请求及响应
   public final  static byte REQUEST_ID = (byte) 0x84;
   public final  static byte REQUEST_CMD = (byte) 0xE0;
   //升级范围
   public final  static byte UPGRADE_SCOPE= (byte) 0x93;

   //响应（遥控器MCU发出） 请求成功
   public final  static byte REQUEST_SUCCES_TAG = (byte) 0Xff;

}
