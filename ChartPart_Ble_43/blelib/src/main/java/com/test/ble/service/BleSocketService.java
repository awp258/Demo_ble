/*
 * Copyright (C) 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.test.ble.service;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;


import com.test.ble.PubUtils;
import com.test.ble.bean.BleMessage;
import com.test.ble.constants.PubConstants;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * Copyright (C), 2013-2019, 深圳市浩瀚卓越科技有限公司
 * Author: Abraham.ai@hohem-tech.com
 * Date: 2019/9/25 11:18
 * Description:  链接发送服务
 * History:
 */
public class BleSocketService extends Service {
    private final static String TAG = "BleSocketService";
     BluetoothManager mBluetoothManager=null;
     BluetoothAdapter mBluetoothAdapter=null;
    private  List<BleMessage> bleMessageList=new ArrayList<>();
    private String bluetoothDeviceAddress;
    private BluetoothGatt mBluetoothGatt;

    private boolean isStartThread=true;
    private  ExecutorService cachedThreadPool;
    private Context mContext;

    @Override
    public boolean bindService(Intent service, ServiceConnection conn, int flags) {
        return super.bindService(service, conn, flags);

    }

    @Override
    public void unbindService(ServiceConnection conn) {
        super.unbindService(conn);

    }

    public void addMessage(String address, BleMessage bleMessage)
    {
        this.bluetoothDeviceAddress=address;
        bleMessageList.add(bleMessage);
    }
    public void remove(BleMessage bleMessage)
    {
        bleMessageList.remove(bleMessage);
    }




    private void eventBusPost(final String txt) {
//        final Intent intent = new Intent(action);
//        LocalBroadcastManager.getInstance(this).sendBroadcast(intent)
   //接收数据后 通过   EventBus刷新界面view层
           EventBus.getDefault().post(txt);
    }

    /**
     * 接收蓝牙发送过来的数据
     * @param action
     * @param characteristic
     */
    private void eventBusPost(final String action, final BluetoothGattCharacteristic characteristic) {
        final Intent intent = new Intent(action);

        // This is special handling for the Heart Rate Measurement profile.  Data parsing is
        // carried out as per profile specifications:
        // http://developer.bluetooth.org/gatt/characteristics/Pages/CharacteristicViewer.aspx?u=org.bluetooth.characteristic.heart_rate_measurement.xml
        if (PubConstants.TX_CHAR_UUID.equals(characteristic.getUuid())) {
        	
           // if(DEBUG)Log.d(TAG, String.format("Received TX: %d",characteristic.getValue() ));
     //       intent.putExtra(EXTRA_DATA, characteristic.getValue());
        }
        EventBus.getDefault().post(action);
    }

    public void clear() {
        if(null!=bleMessageList)
        {
            bleMessageList.clear();
            bleMessageList=null;
        }
    }

    @Override
    public void onDestroy() {
        Log.i(TAG, " onDestroy ");
        if(null!=mBluetoothGatt)
        {
            mBluetoothGatt.close();
        }
        super.onDestroy();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "onCreate  ");
        mContext=this;
        initialize(this);
    }

    /**
     * 常量初始化
     * @return
     */
    private boolean initialize(Context context) {
        // For API level 18 and above, get a reference to BluetoothAdapter through
        if (mBluetoothManager == null) {
            mBluetoothManager = (BluetoothManager) context.getSystemService(Context.BLUETOOTH_SERVICE);
            if (mBluetoothManager == null) {
                Log.i(TAG, "Unable to initialize BluetoothManager.");
                return false;
            }
        }
        mBluetoothAdapter = mBluetoothManager.getAdapter();
        if (mBluetoothAdapter == null) {
            Log.i(TAG, "Unable to obtain a BluetoothAdapter.");
            return false;
        }
        else{
            Log.i(TAG, "mBluetoothManager.getAdapter().getAddress : ."+mBluetoothManager.getAdapter().getAddress());
        }
      return true;
    }
    /**
     *  检测蓝牙蓝牙操作
     * @param address
     * @return
     */
    public boolean connect(Context context,final String address) {

        final BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
        if (device == null) {
            Log.i("textLog", "Device not found.  Unable to connect.");
            return false;
        }

        mBluetoothGatt = device.connectGatt(context, false, mGattCallback);

//        if(mBluetoothGatt.connect()){
//            Log.i("textLog", " 管道链接成功  ");
//
//            if(mBluetoothGatt.discoverServices()){
//                Log.i("textLog", "管道discoverServices已经开始了");
//                    mBluetoothGatt.discoverServices();
//                return  true;
//            }else{
//                Log.i("textLog", "管道discoverServices没有开始");
//            }
//        }
//        else{
//            Log.i("textLog", "链接失败！   "+mBluetoothGatt.discoverServices());
//        }


        return true;
    }


    boolean isConnectFlag=false;

  private   BluetoothGattCallback mGattCallback = new BluetoothGattCallback() {
            @Override
            public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
                if (newState == BluetoothProfile.STATE_CONNECTED) {
                    Log.i(TAG," 蓝牙已链接" );
                    mBluetoothGatt.discoverServices();
                } else if (newState == BluetoothProfile.STATE_DISCONNECTED)
                {
                    Log.i(TAG," 断开链接" );

                }
            }

            @Override
            public void onServicesDiscovered(BluetoothGatt gatt, int status) {
                if (status == BluetoothGatt.GATT_SUCCESS)
                {
                    try {
                        Thread.sleep(1000);
                        Log.i(TAG," 链接蓝牙管道建立成功" );
                        onRestartBluetoothGattService();


                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                } else {
                    Log.i(TAG, "onServicesDiscovered received: " + status);
                }
            }

            @Override
            public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
                Log.i(TAG, "onCharacteristicRead");
                if (status == BluetoothGatt.GATT_SUCCESS) {

                   String callBackCmd=PubUtils.getInstance().bytesToHexString(characteristic.getValue());
                    eventBusPost( callBackCmd);
                    Log.i(TAG," 蓝牙回调数据：" + callBackCmd);
                }
                else if(status==BluetoothGatt.GATT_FAILURE)
                {
                    Log.i(TAG," 蓝牙回调数据：GATT_FAILURE " );
                }
            }

            @Override
            public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
                //   eventBusPost(ACTION_DATA_AVAILABLE, characteristic);
                String callBackCmd=PubUtils.getInstance().bytesToHexString(characteristic.getValue());

                eventBusPost( callBackCmd);
                Log.i(TAG," 蓝牙回调数据：" + callBackCmd);
            }
        };

    /**
     * Disconnects an existing connection or cancel a pending connection. The disconnection result
     * is reported asynchronously through the
     * {@code BluetoothGattCallback#onConnectionStateChange(android.bluetooth.BluetoothGatt, int, int)}
     * callback.
     */
    public void disconnect() {
        if (mBluetoothAdapter == null || mBluetoothGatt == null) {
            Log.i(TAG, "BluetoothAdapter not initialized");
            return;
        }
        mBluetoothGatt.disconnect();

    }

    /**
     * After using a given BLE device, the app must call this method to ensure resources are
     * released properly.
     */
    public void close() {
        if (mBluetoothGatt == null) {
            return;
        }
        Log.i(TAG, "mBluetoothGatt closed");
        mBluetoothGatt.close();
        mBluetoothGatt = null;
    }

    /**
     * 蓝牙读取数据
     * @param characteristic
     */

    public void readCharacteristic(BluetoothGattCharacteristic characteristic) {
        if (mBluetoothAdapter == null || mBluetoothGatt == null) {
            Log.i(TAG, "BluetoothAdapter not initialized");
            return;
        }
        mBluetoothGatt.readCharacteristic(characteristic);
    }

    /**
     * Enables or disables notification on a give characteristic.
     *
     * @param characteristic Characteristic to act on.
     * @param enabled If true, enable notification.  False otherwise.
     */
    public void setCharacteristicNotification(BluetoothGattCharacteristic characteristic, boolean enabled) {
        if (mBluetoothAdapter == null || mBluetoothGatt == null) {
           Log.i(TAG, "BluetoothAdapter not initialized");
            return;
        }
        mBluetoothGatt.setCharacteristicNotification(characteristic, enabled);
        // 这是专门针对心率测量服务
//        if (UUID_HEART_RATE_MEASUREMENT.equals(characteristic.getUuid())) {
//            BluetoothGattDescriptor descriptor = characteristic.getDescriptor(
//                    UUID.fromString(CLIENT_CHARACTERISTIC_CONFIG));
//            descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
//            mBluetoothGatt.writeDescriptor(descriptor);
//        }
    }

    /**
     *  获取蓝牙长链接服务
     * @return
     */
    public List<BluetoothGattService> getSupportedGattServices() {
        if (mBluetoothGatt == null) return null;

        return mBluetoothGatt.getServices();
    }



    /**
     *  重新快开始蓝牙长链接服务
     */
    public void onRestartBluetoothGattService()
    {
        try {
            BluetoothGattService RxService = mBluetoothGatt.getService(PubConstants.RX_SERVICE_UUID);
            if (RxService == null) {
                Log.i(TAG,"Rx service not found!");
                return;
            }
            BluetoothGattCharacteristic TxChar = RxService.getCharacteristic(PubConstants.TX_CHAR_UUID);
            if (TxChar == null) {
                Log.i(TAG,"Tx charateristic not found!");
                return;
            }
            mBluetoothGatt.setCharacteristicNotification(TxChar,true);

            List<BluetoothGattDescriptor> mDescriptors=TxChar.getDescriptors();

            for (int i = 0; i < mDescriptors.size(); i++) {
                BluetoothGattDescriptor descriptor = mDescriptors.get(i);
                descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
                mBluetoothGatt.writeDescriptor(descriptor);
            }
        }
        catch (Exception e){
            Log.i(TAG, "Exception :"+e);
        }


    }

    /**
     *  部分手机会无法接收到onCharacteristicChanged()所返回的消息，使用以下方法能够完美解决问题
     * @param gatt
     * @param serviceUUID
     * @param characteristicUUID
     * @return
     */
    public boolean enableNotification(BluetoothGatt gatt, UUID serviceUUID, UUID characteristicUUID) {
//        BluetoothGattService service = gatt.getService(serviceUUID);
//        BluetoothGattCharacteristic characteristic = findNotifyCharacteristic(service, characteristicUUID);
//        if (characteristic != null) {
//        if(bluetoothGatt.setCharacteristicNotification(characteristic2, true)){
//            //获取到Notify当中的Descriptor通道 然后再进行注册
//            BluetoothGattDescriptor clientConfig = characteristic2 .getDescriptor(UUID.fromString(DESCRIPTOR_UUID));
//            clientConfig.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
//            bluetoothGatt.writeDescriptor(clientConfig);
//        }
        boolean success = false;
        BluetoothGattService service = gatt.getService(serviceUUID);
        if (service != null) {
            BluetoothGattCharacteristic characteristic = findNotifyCharacteristic(service, characteristicUUID);

            if (characteristic != null) {
                success = gatt.setCharacteristicNotification(characteristic, true);
                gatt.readCharacteristic(characteristic);
                if (success) {
                    for(BluetoothGattDescriptor dp: characteristic.getDescriptors()){
                        if (dp != null) {
                            if ((characteristic.getProperties() & BluetoothGattCharacteristic.PROPERTY_NOTIFY) != 0) {
                                dp.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
                            } else if ((characteristic.getProperties() & BluetoothGattCharacteristic.PROPERTY_INDICATE) != 0) {
                                dp.setValue(BluetoothGattDescriptor.ENABLE_INDICATION_VALUE);
                            }
                            int writeType = characteristic.getWriteType();
                            Log.e(TAG, "enableNotification: "+writeType );                            characteristic.setWriteType(BluetoothGattCharacteristic.WRITE_TYPE_DEFAULT);
                             gatt.writeDescriptor(dp);
                            characteristic.setWriteType(writeType);
                        }
                    }
                }
            }
        }
        return success;
    }

    private BluetoothGattCharacteristic findNotifyCharacteristic(BluetoothGattService service, UUID characteristicUUID) {
        BluetoothGattCharacteristic characteristic = null;
        List<BluetoothGattCharacteristic> characteristics = service.getCharacteristics();
        for (BluetoothGattCharacteristic c : characteristics) {
            if ((c.getProperties() & BluetoothGattCharacteristic.PROPERTY_NOTIFY) != 0 && characteristicUUID.equals(c.getUuid())) {
                characteristic = c;
                break;
            }
        }
        if (characteristic != null) {
            return characteristic;
        }
        for (BluetoothGattCharacteristic c : characteristics) {
            if ((c.getProperties() & BluetoothGattCharacteristic.PROPERTY_INDICATE) != 0 && characteristicUUID.equals(c.getUuid())) {
                characteristic = c;
                break;
            }
        }
        return characteristic;
    }
    boolean isWriteCharacteristic=false;
    public boolean writeRXCharacteristic(byte[] value)
    {
        Log.d(TAG, "蓝牙 读取数据 ：writeRXCharacteristic: "+ PubUtils.getInstance().bytes2HexString(value));
    	BluetoothGattService RxService = mBluetoothGatt.getService(PubConstants.RX_SERVICE_UUID);
    	if (RxService == null) {
            Log.e(TAG, "Rx charateristic not found!");
            return false;
        }
    	BluetoothGattCharacteristic RxChar = RxService.getCharacteristic(PubConstants.RX_CHAR_UUID);
        if (RxChar == null) {
            Log.e(TAG, "Rx charateristic not found!");
            return false;
        }
        RxChar.setValue(value);
         isWriteCharacteristic = mBluetoothGatt.writeCharacteristic(RxChar);
     //    boolean isReadCharacteristic = mBluetoothGatt.readCharacteristic(RxChar);
     // Log.i(TAG, "isReadCharacteristic: "+isReadCharacteristic );
	   Log.i(TAG, "isWriteCharacteristic: "+ isWriteCharacteristic);
    	return isWriteCharacteristic;
    }
	public void stopLooperThread(){

        isStartThread=false;
	}
	public void threadPoolShutdown()
    {
        if(null!=cachedThreadPool)
        {
            cachedThreadPool.shutdown();
        }
    }

	public void startThreadPool()
    {
        cachedThreadPool = Executors.newCachedThreadPool();
        cachedThreadPool.execute(new Runnable() {
                @Override
                public void run() {
                    Log.i(TAG, "isStartThread  :"+isStartThread);
           while (isStartThread) {
               try {
                   Log.i(TAG, "bleMessageList  :"+bleMessageList.size());
                   for (int i = 0; i < bleMessageList.size(); i++) {
                      BleMessage bleMessage= bleMessageList.get(i);
                       Log.i(TAG, "发送数据给蓝牙 :"+ PubUtils.getInstance().bytes2HexString(bleMessage.getData()) );
                          //发送蓝牙
                         boolean isLinkBle=writeRXCharacteristic(bleMessage.getData());
                          Log.i(TAG, "蓝牙是否可以读取数据 :"+ isLinkBle );
                        }
                        Thread.sleep(3000*10);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }

            });
        }


    public class LocalBinder extends Binder {
        public   BleSocketService getService() {
            return BleSocketService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        // After using a given device, you should make sure that BluetoothGatt.close() is called
        // such that resources are cleaned up properly.  In this particular example, close() is
        // invoked when the UI is disconnected from the Service.
        close();
        return super.onUnbind(intent);
    }

    private final IBinder mBinder = new LocalBinder();
}
