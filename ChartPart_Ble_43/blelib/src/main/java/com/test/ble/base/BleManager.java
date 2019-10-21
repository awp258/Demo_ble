package com.test.ble.base;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.text.TextUtils;
import android.util.Log;

import com.test.ble.PubUtils;
import com.test.ble.bean.BleDevice;
import com.test.ble.bean.BleMessage;
import com.test.ble.service.BleSocketService;
import com.test.ble.service.BleScanService;

import java.util.List;

/**
 * Copyright (C), 2013-2019, 深圳市浩瀚卓越科技有限公司
 * Author: Abraham.ai@hohem-tech.com
 * Date: 2019/9/26 9:18
 * Description:  蓝牙管理：开启蓝牙，关闭蓝牙，
 * History:
 */
public class BleManager {
    private static final String TAG ="BleManager" ;
    private   volatile   static  BleManager instance=null;
    //负责蓝牙数据传输
    BleSocketService mService=null;
    Intent belSockeIntent=null, bleScanIntent=null;
   BleScanService bleScanService=null;
    Intent intent=null;
   Context mContext;



    public static BleManager getDefault() {
        if(instance==null)
        {
            synchronized (BleManager.class){
                if(instance==null)
                {
                    instance=new BleManager();
                }
            }
            return  instance;
        }
        return  instance;
    }



    /**
     * 启动服务，扫描蓝牙
     * @param context
     */
    public void start(Context context)
    {
        Intent bindIntent = new Intent(context, BleSocketService.class);
        context. bindService(bindIntent, mServiceConnection, Context.BIND_AUTO_CREATE);
     }

    public void stop(Context context)
    {
        context. unbindService( mServiceConnection);
    }


     public void clearList(){
        if(null!=bleScanService)
        {
            bleScanService.clear();
        }
     }
    /**
     * 断开链接
     */
    public void disconnect()
     {
         if(null!=mService)
         {
             mService.disconnect();
         }
     }
     public void sendMessage(Context context,String address,byte[] data)
     {
         Log.i(TAG," 发送消息给数据 : " + PubUtils.getInstance().bytes2HexString(data)) ;
         boolean isCheck=isCheckBleConnect(context, address);

         Log.i(TAG," isCheck : " + isCheck) ;
         if(isCheck)
         {
             BleMessage bleMessage=new BleMessage(false,data);
             mService.addMessage(address,bleMessage);
             mService.startThreadPool();
         }
         else{
             Log.i(TAG," 发送消息给数据 : 蓝牙 未连接 " ) ;
         }

     }
    /**
     *  检测蓝牙是否链接
     */
    public boolean isCheckBleConnect(Context context,String devicesAddress )
    {
        boolean isConnect= false;
        if(mService!=null)
        {
            if(!TextUtils.isEmpty(devicesAddress)){
                isConnect = mService.connect(context,devicesAddress);
                return  isConnect;
            }
        }
        else{
            Log.i(TAG, " ：bleSocketService is null ");
        }
        return isConnect;
    }

  public void stopScanBle(){
        if(mService!=null)
        {
            mContext.stopService(intent);
        }
  }

  public void  onDestroy()
  {
      try {
          if(mService!=null&&mContext!=null)
          {
               stopScanBle();
              mService.clear();
              mService=null;
          }
          else{
              Log.i(TAG, "onDestroy ==>  蓝牙资源回收异常： "  );
          }
      }
      catch (Exception e){
          mService=null;
      }
  }


    private ServiceConnection mServiceConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder rawBinder) {
            mService = ((BleSocketService.LocalBinder) rawBinder).getService();
        }

        public void onServiceDisconnected(ComponentName classname) {
            //// mService.disconnect(mDevice);
            mService = null;
        }
    };

}
