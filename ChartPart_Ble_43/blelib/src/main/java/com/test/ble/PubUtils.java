package com.test.ble;

/**
 * Copyright (C), 2013-2019, 深圳市浩瀚卓越科技有限公司
 * Author: Abraham.ai@hohem-tech.com
 * Date: 2019/9/27 16:00
 * Description:
 * History:
 */
public class PubUtils {

   private static  PubUtils instance=new PubUtils();

    public static PubUtils getInstance() {
        return instance;
    }

    public  String toHexString(int num){
        int sc = 0xF;
        int length = 0;
        char[] cs1 = new char[Integer.SIZE/4];
        do{
            int temp = num & sc;
            num >>= 4;
            cs1[length] =
                    (char)(temp >= 10 ? temp-10+'A' : temp+'0');
            length ++;
        }while( num > 0);
        System.out.println(length);
        char[] cs2 = new char[length];
        for(int i=0;i<length;i++){
            cs2[i] = cs1[length-1-i];
        }



        return new String(cs2);
    }

    public  String bytesToHexString(byte[] src){
        StringBuilder stringBuilder = new StringBuilder("");
        if (src == null || src.length <= 0) {
            return null;
        }
        for (int i = 0; i < src.length; i++) {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }
    /**
     * Convert hex string to byte[]
     * @param hexString the hex string
     * @return byte[]
     */
    public  byte[] hexStringToBytes(String hexString) {
        if (hexString == null || hexString.equals("")) {
            return null;
        }
        hexString = hexString.toUpperCase();
        int length = hexString.length() / 2;
        char[] hexChars = hexString.toCharArray();
        byte[] d = new byte[length];
        for (int i = 0; i < length; i++) {
            int pos = i * 2;
            d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
        }
        return d;
    }
    /**
     * Convert char to byte
     * @param c char
     * @return byte
     */
    private byte charToByte(char c) {
        return (byte) "0123456789ABCDEF".indexOf(c);
    }

    //将指定byte数组以16进制的形式打印到控制台
    public  void printHexString( byte[] b) {
        for (int i = 0; i < b.length; i++) {
            String hex = Integer.toHexString(b[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            System.out.print(hex.toUpperCase() );
        }

    }

    public  String bytes2HexString(byte[] b) {
        String ret = "";
        for (int i = 0; i < b.length; i++) {
            String hex = Integer.toHexString(b[ i ] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            ret += hex.toUpperCase();
        }
        return ret;
    }

    /**
     *
     * @param bleName 蓝牙名称统一大写
     * @return  产品名称
     */
    public String bleNameToProName(String bleName)
    {
        String productName="";
        if(bleName.equals("GIMBAL_11")||bleName.equals("MINIGIMBAL_R"))
        {
            productName="HG5";
        }
        else if(bleName.equals("GIMBAL_12"))
        {
            productName="XG1";
        }
        else if(bleName.equals("GIMBAL_14")||bleName.equals("GIMBAL_15"))
        {
            productName="HGS";
        }else if(bleName.equals("GIMBAL_16"))
        {
            productName="HGS1.5";
        }
        else if(bleName.equals("GIMBAL_17"))
        {
            productName="HGS2";
        }
        else if(bleName.equals("GIMBAL_31"))
        {
            productName="DG1";
        }
        else if(bleName.equals("GIMBAL_32"))
        {
            productName="DGS";
        }
        else if(bleName.equals("GIMBAL_32"))
        {
            productName="DG1";
        }
        else if(bleName.equals("GIMBAL_22")||bleName.equals("STABILILZER_A001")||bleName.equals("STABILILZER_A002"))
        {
            productName="MGS";
        }
        else if(bleName.equals("STABILILZER_A003"))
        {
            productName="MGS+";
        }
        else if(bleName.equals("ZSBLE_A003"))
        {
            productName="MGS+";
        }
        else if(bleName.equals("ZSBLE_A004"))
        {
            productName="Multi";
        }
        else if(bleName.equals("GIMBALREMOTE"))
        {
            productName="Remote";
        }
        else{
            productName=bleName;
        }
        return productName;
    }
}
