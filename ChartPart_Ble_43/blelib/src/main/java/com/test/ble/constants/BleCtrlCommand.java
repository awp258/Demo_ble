package com.test.ble.constants;

/**
 * Copyright (C), 2013-2019, 深圳市浩瀚卓越科技有限公司
 * Author: Abraham.ai@hohem-tech.com
 * Date: 2019/9/25 11:06
 * Description: 指令集封装类
 * History:
 */
public class BleCtrlCommand {

    private static  BleCtrlCommand instance=null;

     public static BleCtrlCommand getInstance() {

        if(instance==null)
        {
            instance=new BleCtrlCommand();
        }
        return instance;
    }
    /**
     * 升级请求指令
     * @return
     */
    public byte[] belUpgradeCommand()
    {
        byte[] command = new byte[5];
        command[0] = BleControlConstant.REQUEST_ID;
        command[1] = BleControlConstant.REQUEST_CMD;
        command[2] = BleControlConstant.UPGRADE_SCOPE;
        command[3] = (byte) 0x00;
        command[4] = (byte) 0x00;
        return command;
    }


    public byte[] testCommand1()
    {
        byte[] command = new byte[10];
        command[0] = (byte) 0xAA;// ID
        command[1] = (byte) 0x55;// ID
        command[2] =(byte) 0x07;// ID
        command[3] = (byte) 0x00;// ID
        command[4] = (byte) 0x01;// ID
        command[5] = (byte) 0xE3;// ID
        command[6] = (byte) 0x00;// ID
        command[7] = (byte) 0xEB;// ID
        command[9] = (byte) 0xFF;// ID
        //aa 55 07 00 01 e3 00 eb ff


        return command;
    }
    public byte[] testCommand()
    {
      //  AA 55 06 00 03 84 8D FF
        // 回复： AA550E0083841B00E0FF60FA5300BCFF
        byte[] command = new byte[8];
        command[0] = (byte) 0xAA;// ID
        command[1] = (byte) 0x55;// ID
        command[2] =(byte) 0x06;// ID
        command[3] = (byte) 0x00;// ID
        command[4] = (byte) 0x03;// ID
        command[5] = (byte) 0x84;// ID
        command[6] = (byte) 0x8D;// ID
        command[7] = (byte) 0xFF;// ID
        //aa 55 07 00 01 e3 00 eb ff

        return command;
    }


    public byte[] upgradeCommand()
    {
      //  aa 55 0e 00 80 e0 93 01 00 01 08 29 26 2a 84 ff


        byte[] command = new byte[16];
        command[0] = (byte) 0xAA;// ID
        command[1] = (byte) 0x55;// ID
        command[2] =(byte) 0x0E;// ID
        command[3] = (byte) 0x00;// ID
        command[4] = (byte) 0x80;// ID
        command[5] = (byte) 0xE0;// ID
        command[6] = (byte) 0x93;// ID
        command[7] = (byte) 0x01;// ID
        command[8] = (byte) 0x00;// ID
        command[9] = (byte) 0x01;// ID
        command[10] = (byte) 0x08;// ID
        command[11] = (byte) 0x29;// ID
        command[12] = (byte) 0x26;// ID
        command[13] = (byte) 0x2A;// ID
        command[14] = (byte) 0x84;// ID
        command[15] = (byte) 0xFF;// ID
        return command;
    }

    //

   //查询模块当前模式
    public byte[] bleContrlCommand()
    {
        //  AA 55 05 00 80 11 02 00 00
        // 回复：  AA 55 05 00 4D 00 01 02 54
        byte[] command = new byte[9];
        command[0] = (byte) 0xAA;
        command[1] = (byte) 0x55;
        command[2] =(byte) 0x05;
        command[3] = (byte) 0x00;
        command[4] = (byte) 0x80;
        command[5] = (byte) 0x11;
        command[6] = (byte) 0x02;
        command[7] = (byte) 0x00;
        command[8] = (byte) 0x00;

        return command;
    }
//AA 55 0A 00 aa 55 08 00 80 64 4d f9 32 ff



    /**
     *  AA 55 0A 00 aa 55 08 00 80 64 4d f9 32 ff
     * @return  向上 指令
     */
    public byte[] CMD_UP()
    {
        byte[] command = new byte[14];
        command[0] = (byte) 0xAA;
        command[1] = (byte) 0x55;
        command[2] =(byte) 0x0A;
        command[3] = (byte) 0x00;
        command[4] = (byte) 0xAA;
        command[5] = (byte) 0x55;
        command[6] = (byte) 0x08;
        command[7] = (byte) 0x00;
        command[8] = (byte) 0x80;
        command[9] = (byte) 0x64;
        command[10] = (byte) 0x4D;
        command[11] = (byte) 0xF9;
        command[12] = (byte) 0x32;
        command[13] = (byte) 0xFF;
        return command;
    }

    /**
     *  AA 55 0A 00 aa 55 08 00 80 64 4d f9 32 ff
     * @return  向下 指令
     */
    public byte[] CMD_DOWN1()
    {
        byte[] command = new byte[14];
        command[0] = (byte) 0xAA;
        command[1] = (byte) 0x55;
        command[2] =(byte) 0x0A;
        command[3] = (byte) 0x00;
        command[4] = (byte) 0xAA;
        command[5] = (byte) 0x55;
        command[6] = (byte) 0x08;
        command[7] = (byte) 0x00;
        command[8] = (byte) 0x80;
        command[9] = (byte) 0x64;
        command[10] = (byte) 0x4D;
        command[11] = (byte) 0xF9;
        command[12] = (byte) 0x32;
        command[13] = (byte) 0xFF;
        return command;
    }


    /**
     *  AA 55 0A 00 aa 55 08 00 80 64 4d f9 32 ff
     * @return  向下 指令
     */
    public byte[] CMD_DOWN()
    {
        byte[] command = new byte[14];
        command[0] = (byte) 0xAA;
        command[1] = (byte) 0x55;
        command[2] =(byte) 0x0A;
        command[3] = (byte) 0x00;
        command[4] = (byte) 0xAA;
        command[5] = (byte) 0x55;
        command[6] = (byte) 0x08;
        command[7] = (byte) 0x00;
        command[8] = (byte) 0x80;
        command[9] = (byte) 0x64;
        command[10] = (byte) 0x4D;
        command[11] = (byte) 0xF9;
        command[12] = (byte) 0x32;
        command[13] = (byte) 0xFF;
        return command;
    }


    /**
     *  AA 55 09 00 AA 55 07 00 01 A5 01 AE FF
     * @return   拍照 指令
     */
    public byte[] CMD_TAKE_PICTURE()
    {
        byte[] command = new byte[13];
        command[0] = (byte) 0xAA;
        command[1] = (byte) 0x55;
        command[2] =(byte) 0x09;
        command[3] = (byte) 0x00;
        command[4] = (byte) 0xAA;
        command[5] = (byte) 0x55;
        command[6] = (byte) 0x07;
        command[7] = (byte) 0x00;
        command[8] = (byte) 0x01;
        command[9] = (byte) 0xA5;
        command[10] = (byte) 0x01;
        command[11] = (byte) 0xAE;
        command[12] = (byte) 0xEF;
        return command;
    }

    /**
     *  AA 55 09 00 AA 55 07 00 01 A5 02 AF FF
     * @return 录像
     */
    public byte[] CMD_VIDEO()
    {
        byte[] command = new byte[13];
        command[0] = (byte) 0xAA;
        command[1] = (byte) 0x55;
        command[2] =(byte) 0x09;
        command[3] = (byte) 0x00;
        command[4] = (byte) 0xAA;
        command[5] = (byte) 0x55;
        command[6] = (byte) 0x07;
        command[7] = (byte) 0x00;
        command[8] = (byte) 0x01;
        command[9] = (byte) 0xA5;
        command[10] = (byte) 0x02;
        command[11] = (byte) 0xAF;
        command[12] = (byte) 0xFF;
        return command;
    }

    /**
     *  AA 55 09 00 AA 55 07 00 01 A5 03 B0 FF
     * @return 放大
     */
    public byte[] CMD_ZOOM_IN()
    {
        byte[] command = new byte[13];
        command[0] = (byte) 0xAA;
        command[1] = (byte) 0x55;
        command[2] =(byte) 0x09;
        command[3] = (byte) 0x00;
        command[4] = (byte) 0xAA;
        command[5] = (byte) 0x55;
        command[6] = (byte) 0x07;
        command[7] = (byte) 0x00;
        command[8] = (byte) 0x01;
        command[9] = (byte) 0xA5;
        command[10] = (byte) 0x03;
        command[11] = (byte) 0xB0;
        command[12] = (byte) 0xFF;
        return command;
    }

    /**
     *  AA 55 09 00 AA 55 07 00 01 A5 04 B1 FF
     * @return 缩小
     */
    public byte[] CMD_ZOOM_OUT()
    {
        byte[] command = new byte[13];
        command[0] = (byte) 0xAA;
        command[1] = (byte) 0x55;
        command[2] =(byte) 0x09;
        command[3] = (byte) 0x00;
        command[4] = (byte) 0xAA;
        command[5] = (byte) 0x55;
        command[6] = (byte) 0x07;
        command[7] = (byte) 0x00;
        command[8] = (byte) 0x01;
        command[9] = (byte) 0xA5;
        command[10] = (byte) 0x04;
        command[11] = (byte) 0xB1;
        command[12] = (byte) 0xFF;
        return command;
    }

    /**
     *  AA 55 09 00 AA 55 07 00 01 A5 05 B2 FF
     * @return 停止
     */
    public byte[] CMD_ZOOM_STOP()
    {
        byte[] command = new byte[13];
        command[0] = (byte) 0xAA;
        command[1] = (byte) 0x55;
        command[2] =(byte) 0x09;
        command[3] = (byte) 0x00;
        command[4] = (byte) 0xAA;
        command[5] = (byte) 0x55;
        command[6] = (byte) 0x07;
        command[7] = (byte) 0x00;
        command[8] = (byte) 0x01;
        command[9] = (byte) 0xA5;
        command[10] = (byte) 0x05;
        command[11] = (byte) 0xB2;
        command[12] = (byte) 0xFF;
        return command;
    }

}
