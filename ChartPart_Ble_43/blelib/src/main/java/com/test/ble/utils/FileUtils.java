package com.test.ble.utils;

import android.os.Message;
import android.util.Log;

import java.io.BufferedReader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


/**
 * Copyright (C), 2013-2019, 深圳市浩瀚卓越科技有限公司
 * Author: Abraham.ai@hohem-tech.com
 * Date: 2019/9/17 13:39
 * Description: 文件操作类型
 * History:
 */
public class FileUtils {

 String TAG=FileUtils.class.getSimpleName();
    private static volatile  FileUtils instance=null;

    public static FileUtils getInstance() {
        if(instance==null)
        {
            synchronized (FileUtils.class)
            {
                instance=new FileUtils();
            }
        }
        return instance;
    }
    public boolean copyFile(String oldFilePath, String newFilePath) {
        try {
            FileInputStream fileInputStream = new FileInputStream(oldFilePath);
            FileOutputStream fileOutputStream = new FileOutputStream(newFilePath);
            byte[] buffer = new byte[1024];
            int byteRead;
            while (-1 != (byteRead = fileInputStream.read(buffer))) {
                fileOutputStream.write(buffer, 0, byteRead);
            }
            fileInputStream.close();
            fileOutputStream.flush();
            fileOutputStream.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    public void readFile(File file)
    {
        try {
            FileReader fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);
            String b="";
            while ((b = br.readLine()) != null) {
                Log.i(TAG, " 读取文件内容  ： " +b);
            }
            fr.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Byte char2Byte(Character src) {
        return Integer.valueOf((int) src).byteValue();
    }

    public static String byte2hex(byte[] b) {
        String hs = "";
        String stmp = "";
        for (int n = 0; n < b.length; n++) {
            stmp = (Integer.toHexString(b[n] & 0XFF));
            if (stmp.length() == 1) {
                hs = hs + "0" + stmp;
            } else {
                hs = hs + stmp;
            }
        }
        return hs.toUpperCase();
    }
    public static String bytes2HexString(byte[] b) {
        StringBuffer result = new StringBuffer();
        String hex;
        for (int i = 0; i < b.length; i++) {
            hex = Integer.toHexString(b[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            result.append(hex.toUpperCase());
        }
        return result.toString();
    }

    public static long getFileSize(File file) throws Exception {
        long size = 0;
        if (file.exists()) {
            FileInputStream fis = null;
            fis = new FileInputStream(file);
            size = fis.available();
        } else {
            file.createNewFile();

        }
        return size;
    }

    private static File[] getAllFilesInPath(String path) {
        Message msg;
        File[] files = null;
        // File file = new File(Tools.PHOTOS_PATH);
        File file = new File(path);

        if (!file.isDirectory()) {

            return null;
        }

        if (!file.exists()) {



        } else {
            files = file.listFiles();
            Log.v("trans", "files:" + files);
        }
        return files;
    }

    private String getFileName(File[] files) {
        String str = "";
        String fileNametxt = null;
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    getFileName(file.listFiles());

                } else {
                    fileNametxt = file.getName();

                }
            }

        }
        return fileNametxt;
    }


    protected String getFileContent(File[] files) {
        String content = "";
        if (files != null) {
            for (File file : files) {

                if (file.isDirectory()) {

                    getFileContent(file.listFiles());

                } else {
                    if (file.getName().endsWith(".3vast")) {
                        try {
                            InputStream instream = new FileInputStream(file);
                            if (instream != null) {
                                InputStreamReader inputreader = new InputStreamReader(instream, "GBK");
                                BufferedReader buffreader = new BufferedReader(inputreader);
                                String line = "";

                                while ((line = buffreader.readLine()) != null) {
                                    content += line + "\n";
                                }
                                instream.close();
                            }
                        } catch (FileNotFoundException e) {
                            Log.d("TestFile", "The File doesn't not exist.");
                        } catch (IOException e) {
                            Log.d("TestFile", e.getMessage());
                        }

                    }
                }
            }

        }

        return content;

    }

    public String downLoadText(String urlStr) {
        StringBuffer sb = new StringBuffer();
        String line = null;
        BufferedReader buffer = null;

        try {
            URL url = new URL(urlStr);
            try {
                HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
                buffer = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));
                while ((line = buffer.readLine()) != null) {
                    sb.append(line);
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return sb.toString();
    }
}
