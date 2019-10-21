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
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.test.ble.PreferencesUtils;
import com.test.ble.bean.BleDevice;
import com.test.ble.bean.BleMessage;
import com.test.ble.constants.PubConstants;
import org.greenrobot.eventbus.EventBus;
import java.util.ArrayList;
import java.util.List;


/**
 * Copyright (C), 2013-2019, 深圳市浩瀚卓越科技有限公司
 * Author: Abraham.ai@hohem-tech.com
 * Date: 2019/9/25 11:18
 * Description:   监听蓝牙链接状态，和搜索附近蓝牙
 * History:
 */
public class BleScanService extends Service {
    private final static String TAG = "BleScanService";
    private   BluetoothManager mBluetoothManager=null;
    private  BluetoothAdapter mBluetoothAdapter=null;
    private  BluetoothLeScanner mBluetoothLeScanner = null;

    private   List<BleDevice> bluetoothDeviceList=new ArrayList<>();
    private  List<BleMessage> bleMessageList=new ArrayList<>();
    //当前蓝牙设备Mac地址
    private String mBluetoothDeviceAddress=null;

  //  private BluetoothGatt mBluetoothGatt;
  //   private boolean isStartThread=true;
  //    private  ExecutorService cachedThreadPool;

    public void addMessage(BleMessage bleMessage)
    {
        bleMessageList.add(bleMessage);
    }
    public void remove(BleMessage bleMessage)
    {
        bleMessageList.remove(bleMessage);
    }
    public   void startScanBle() {
        if (null != scanCallback && null != mBluetoothLeScanner) {
            mBluetoothLeScanner.startScan(scanCallback);
        }
    }
    public   void stopScanBle() {
        if (null != scanCallback && null != mBluetoothLeScanner&& mBluetoothAdapter.getState()== BluetoothAdapter.STATE_ON) {
               Log.i(TAG, "停止蓝牙扫描 ");
                mBluetoothLeScanner.stopScan(scanCallback);
        }
        else {
            Log.i(TAG, "该手机检测到没有开启蓝牙设备 ");
        }
    }
    // 蓝牙扫描回调
    private ScanCallback scanCallback = new ScanCallback() {
        @Override
        public void onScanResult(int callbackType, final ScanResult result) {
            super.onScanResult(callbackType, result);
            BleDevice bleDevice = new BleDevice(false, result.getRssi(),result.getDevice());
            if (!TextUtils.isEmpty(result.getDevice().getName())) {
                Log.i(TAG, "result.getRssi is " + result.getRssi());
                Log.i(TAG, "result.getRssi is " + bleDevice.toString());
                updateBluetoothDeviceList(bleDevice);

            }
        }
        @Override
        public void onBatchScanResults(List<ScanResult> results) {
            super.onBatchScanResults(results);
            Log.i(TAG, "results is " + results.size());
            for (int i = 0; i < results.size(); i++) {
                Log.i(TAG, "results is " + results.get(i).getDevice().getName() + "");
            }
        }
        @Override
        public void onScanFailed(int errorCode) {
            super.onScanFailed(errorCode);

            Log.i(TAG, " Bluetooth  onScanFailed  is   errorCode "+ errorCode);
        }
    };
    //刷新 子控件
    private void updateBluetoothDeviceList(BleDevice device) {

        boolean  isExistDevice=isExistDevice(device);
        if(isExistDevice)
        {
            updateDeviceInfo(device);
        }
        else{
            bluetoothDeviceList.add(device);
        }

        boolean isConnectDevices=false;
        mBluetoothDeviceAddress= PreferencesUtils.getString(this, PubConstants.BLE_DEVICES_ADDRESS,"" );

        if(!TextUtils.isEmpty(mBluetoothDeviceAddress))
        {
            isConnectDevices=    isConnectDevice(mBluetoothDeviceAddress);
        }
     //  通过EventBus 传输数据给view层。
        EventBus.getDefault().post(isConnectDevices);
        EventBus.getDefault().post(bluetoothDeviceList);
    }
    private boolean  isExistDevice(BleDevice device)
    {
        for (int i = 0; i < bluetoothDeviceList.size(); i++) {
            BluetoothDevice bluetoothDevice = bluetoothDeviceList.get(i).getBluetoothDevice();
            if (device.getBluetoothDevice().getAddress().equals(bluetoothDevice.getAddress())) {
                bluetoothDeviceList.set(i,device);
                return true;
            }
        }
        return false;
    }

    private boolean  isConnectDevice(String  address)
    {
        for (int i = 0; i < bluetoothDeviceList.size(); i++) {
            BluetoothDevice bluetoothDevice = bluetoothDeviceList.get(i).getBluetoothDevice();
            if (address.equals(bluetoothDevice.getAddress())) {

                return true;
            }
        }
        return false;
    }

    private void   updateDeviceInfo(BleDevice device)
    {
        for (int i = 0; i < bluetoothDeviceList.size(); i++) {
            BluetoothDevice bluetoothDevice = bluetoothDeviceList.get(i).getBluetoothDevice();
            if (device.getBluetoothDevice().getAddress().equals(bluetoothDevice.getAddress())) {
                Log.i(TAG, " 更新信号灯指示 address : " +bluetoothDevice.getAddress() );
                bluetoothDeviceList.set(i,device);
                return ;
            }
        }
    }


//    private final BluetoothGattCallback mGattCallback = new BluetoothGattCallback() {
//        @Override
//        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
//            Log.i(TAG, " onConnectionStateChange"  );
//
//            if (newState == BluetoothProfile.STATE_CONNECTED) {
//             mBluetoothGatt.discoverServices();
//            } else if (newState == BluetoothProfile.STATE_DISCONNECTED)
//            {
//                mBluetoothGatt.discoverServices();
//            }
//        }
//
//        @Override
//        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
//            Log.i(TAG, " onServicesDiscovered"  );
//            if (status == BluetoothGatt.GATT_SUCCESS)
//            {
//
//                   onRestartBluetoothGattService();
//            } else {
//               Log.i(TAG, "onServicesDiscovered received: " + status);
//            }
//        }
//
//        @Override
//        public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
//        	 Log.i(TAG, "onCharacteristicRead");
//            if (status == BluetoothGatt.GATT_SUCCESS) {
//             //   eventBusPost(ACTION_DATA_AVAILABLE, characteristic);
//                Log.i(TAG," 接收到的数据：" + characteristic.getValue() );
//
//            }
//        }
//
//        @Override
//        public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
//         //   eventBusPost(ACTION_DATA_AVAILABLE, characteristic);
//            Log.i(TAG," 接收到的数据：" + characteristic.getValue() );
//        }
//    };
    public void clear() {
        if(null!=bleMessageList)
        {
            bleMessageList.clear();
            bleMessageList=null;
        }
    }
    BroadcastReceiver receiver=null;
    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        //监听手机蓝牙操作
        receiver=new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.i(TAG,    "蓝牙设备: ==> onReceive ");
                String action = intent.getAction();
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                switch (action){
                    case BluetoothDevice.ACTION_ACL_CONNECTED:
                        bleMessageList.clear();
                        Log.i(TAG,    "蓝牙设备:" + device.getName() + "已链接");
                        break;
                    case BluetoothDevice.ACTION_ACL_DISCONNECTED:
                        bleMessageList.clear();
                        Log.i(TAG,    "蓝牙设备:" + device.getName() + "已断开");
                        break;
                    case BluetoothAdapter.ACTION_STATE_CHANGED:
                        bleMessageList.clear();
                        Log.i(TAG,    "蓝牙设备: 发生变化" );
                        int blueState = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, 0);
                        switch (blueState){
                            case BluetoothAdapter.STATE_OFF:
                                Log.i(TAG,    "蓝牙已关闭");
                                //     BleManager.getDefault().onDestroy();
                                stopScanBle();
                                break;
                            case BluetoothAdapter.STATE_ON:
                                Log.i(TAG,    "蓝牙已开启");
                                //      BleManager.getDefault().init(context);
                                if (initialize()) {
                                    Log.i(TAG, " Bluetooth  initialize is success ==> onStartCommand ");
                                    startScanBle();
                                }
                                break;
                        }
                        break;
                }
            }
        };
        if (initialize()) {
            Log.i(TAG, " Bluetooth  initialize is success ==> onStartCommand ");
             startScanBle();
        }
        else{
            Log.i(TAG,  "手机未开启蓝牙操作！" );
        }
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        intentFilter.addAction(BluetoothDevice.ACTION_ACL_CONNECTED);
        intentFilter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED);
        intentFilter.addAction("android.bluetooth.BluetoothAdapter.STATE_OFF");
        intentFilter.addAction("android.bluetooth.BluetoothAdapter.STATE_ON");
        registerReceiver(receiver, intentFilter);

         return Service.START_STICKY;
    }
    @Override
    public void onDestroy() {
        Log.i(TAG, " onDestroy ");
        stopScanBle();
//        if(null!=mBluetoothGatt)
//        {
//            mBluetoothGatt.close();
//        }

        unregisterReceiver(receiver);
        super.onDestroy();
    }
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    /**
     * 常量初始化
     * @return false
     */
    public boolean initialize() {
        // For API level 18 and above, get a reference to BluetoothAdapter through
        if (mBluetoothManager == null) {
            mBluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
            if (mBluetoothManager == null) {
                Toast.makeText(this, "设备不支持蓝牙功能", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        mBluetoothAdapter = mBluetoothManager.getAdapter();
        if (mBluetoothAdapter == null) {
            Log.i(TAG, "Unable to obtain a BluetoothAdapter.");
            return false;
        }

        // 打开蓝牙
        else if (!mBluetoothAdapter.isEnabled())
        {
            Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            // 设置蓝牙可见性，最多300秒
            intent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
            startActivity(intent);
           return false;
        }
        mBluetoothLeScanner=mBluetoothAdapter.getBluetoothLeScanner();

      return true;
    }
//    /**
//     *  检测蓝牙蓝牙操作
//     * @param address
//     * @return
//     */
//    public boolean connect(Context context,final String address) {
//        stopScanBle();
//        if(mBluetoothAdapter==null)
//        {
//           initialize();
//        }
//
//        Log.i(TAG, "Log.i(TAG, \"Device not found.  Unable to connect.\");: "+  address);
//        final BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
//
//        if (device == null) {
//            Log.i(TAG, "Device not found.  Unable to connect.");
//            return false;
//        }
//        mBluetoothGatt = device.connectGatt(this, false, mGattCallback);
//
//
//        return true;
//    }

//    /**
//     * Disconnects an existing connection or cancel a pending connection. The disconnection result
//     * is reported asynchronously through the
//     * {@code BluetoothGattCallback#onConnectionStateChange(android.bluetooth.BluetoothGatt, int, int)}
//     * callback.
//     */
//    public void disconnect() {
//        if (mBluetoothAdapter == null || mBluetoothGatt == null) {
//            Log.i(TAG, "BluetoothAdapter not initialized");
//            return;
//        }
//        mBluetoothGatt.disconnect();
//
//    }

//    /**
//     * After using a given BLE device, the app must call this method to ensure resources are
//     * released properly.
//     */
//    public void close() {
//        if (mBluetoothGatt == null) {
//            return;
//        }
//        Log.i(TAG, "mBluetoothGatt closed");
//        mBluetoothDeviceAddress = null;
//        mBluetoothGatt.close();
//        mBluetoothGatt = null;
//    }

//    /**
//     * 蓝牙读取数据
//     * @param characteristic
//     */
//
//    public void readCharacteristic(BluetoothGattCharacteristic characteristic) {
//        if (mBluetoothAdapter == null || mBluetoothGatt == null) {
//            Log.i(TAG, "BluetoothAdapter not initialized");
//            return;
//        }
//        mBluetoothGatt.readCharacteristic(characteristic);
//    }
//
//    /**
//     * Enables or disables notification on a give characteristic.
//     *
//     * @param characteristic Characteristic to act on.
//     * @param enabled If true, enable notification.  False otherwise.
//     */
//    public void setCharacteristicNotification(BluetoothGattCharacteristic characteristic, boolean enabled) {
//        if (mBluetoothAdapter == null || mBluetoothGatt == null) {
//           Log.i(TAG, "BluetoothAdapter not initialized");
//            return;
//        }
//        mBluetoothGatt.setCharacteristicNotification(characteristic, enabled);
//        // 这是专门针对心率测量服务
////        if (UUID_HEART_RATE_MEASUREMENT.equals(characteristic.getUuid())) {
////            BluetoothGattDescriptor descriptor = characteristic.getDescriptor(
////                    UUID.fromString(CLIENT_CHARACTERISTIC_CONFIG));
////            descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
////            mBluetoothGatt.writeDescriptor(descriptor);
////        }
//    }

//    /**
//     *  获取蓝牙长链接服务
//     * @return
//     */
//    public List<BluetoothGattService> getSupportedGattServices() {
//        if (mBluetoothGatt == null) return null;
//
//        return mBluetoothGatt.getServices();
//    }

//    /**
//     *  重新快开始蓝牙长链接服务
//     */
//    public void onRestartBluetoothGattService()
//    {
//
//    	BluetoothGattService RxService = mBluetoothGatt.getService(PubConstants.RX_SERVICE_UUID);
//    	if (RxService == null) {
//            Log.i(TAG,"Rx service not found!");
//
//            return;
//        }
//    	BluetoothGattCharacteristic TxChar = RxService.getCharacteristic(PubConstants.TX_CHAR_UUID);
//        if (TxChar == null) {
//            Log.i(TAG,"Tx charateristic not found!");
//            return;
//        }
//        mBluetoothGatt.setCharacteristicNotification(TxChar,true);
//
//        BluetoothGattDescriptor descriptor = TxChar.getDescriptor(PubConstants.CCCD);
//        descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
//        mBluetoothGatt.writeDescriptor(descriptor);
//
//    }
//    boolean isWriteCharacteristic=false;
//    public boolean writeRXCharacteristic(byte[] value)
//    {
//         Log.d(TAG, "蓝牙 读取数据 ：writeRXCharacteristic: "+ PubUtils.getInstance().bytes2HexString(value));
//
////    	if(DataUtils.getmState() != 20){
////    		return false;
////    	}
//    	BluetoothGattService RxService = mBluetoothGatt.getService(PubConstants.RX_SERVICE_UUID);
//    	if (RxService == null) {
//            Log.e(TAG, "Rx charateristic not found!");
//            return false;
//        }
//    	BluetoothGattCharacteristic RxChar = RxService.getCharacteristic(PubConstants.RX_CHAR_UUID);
//        if (RxChar == null) {
//            Log.e(TAG, "Rx charateristic not found!");
//            return false;
//        }
//        	RxChar.setValue(value);
//        isWriteCharacteristic = mBluetoothGatt.writeCharacteristic(RxChar);
//    	if(!isWriteCharacteristic){
//	        Log.i(TAG, "isWriteCharacteristic: fail");
//    	}
//    	return isWriteCharacteristic;
//    }
//	public void stopLooperThread(){
//
//        isStartThread=false;
//	}
//	public void threadPoolShutdown()
//    {
//        if(null!=cachedThreadPool)
//        {
//            cachedThreadPool.shutdown();
//        }
//    }
//	public void startThreadPool()
//    {
//        cachedThreadPool = Executors.newCachedThreadPool();
//        cachedThreadPool.execute(new Runnable() {
//                @Override
//                public void run() {
//           while (isStartThread) {
//               try {
//                   for (int i = 0; i < bleMessageList.size(); i++) {
//                      BleMessage bleMessage= bleMessageList.get(i);
//                      if(!bleMessage.isSuccess())
//                      {
//                          //发送蓝牙
//                          writeRXCharacteristic(bleMessage.getData());
//                        }
//                        }
//                            Thread.sleep(30);
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }
//
//            });
//        }

}
