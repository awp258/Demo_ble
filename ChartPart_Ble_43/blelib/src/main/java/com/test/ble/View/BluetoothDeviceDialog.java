package com.test.ble.View;

import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.test.ble.PreferencesUtils;
import com.test.ble.R;
import com.test.ble.adapter.BluetoothDeviceAdapter;
import com.test.ble.bean.BleDevice;
import com.test.ble.constants.PubConstants;

import java.util.ArrayList;
import java.util.List;

/**
 * Copyright (C), 2013-2019, 深圳市浩瀚卓越科技有限公司
 * Author: Abraham.ai@hohem-tech.com
 * Date: 2019/9/25 14:29
 * Description:
 * History:
 */
public class BluetoothDeviceDialog extends Dialog  implements BluetoothDeviceAdapter.BelInfoAdapterListener
{

    private static final String TAG ="BluetoothDeviceDialog" ;
    private RelativeLayout rlTop;
    private TextView tvAddress;
    private RelativeLayout rlClose;
    private RecyclerView rvDevices;
    BluetoothAdapter mBluetoothAdapter = null;
    BluetoothManager mBluetoothManager = null;
    BluetoothLeScanner mBluetoothLeScanner = null;
    private BleDevice bleDevice;

    BluetoothChoiceListener mBluetoothChoiceListener;

    public BleDevice getBleDevice() {
        return bleDevice;
    }

    private void setBleDevice(BleDevice bleDevice) {
        this.bleDevice = bleDevice;
    }


    List<BleDevice> mBleDeviceList;
    Context mContext;
    public BluetoothDeviceDialog(@NonNull Context context  ,BluetoothChoiceListener bluetoothChoiceListener )
    {
        super(context, R.style.style_dialog);
        mBleDeviceList=new ArrayList<>();
        this.mBluetoothChoiceListener=bluetoothChoiceListener;
        this.mContext=context;
         startScanBle();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_bluetooth_device);
        initView();
        setCanceledOnTouchOutside(false);;
 //       PublicUtils.getInstance().settingDialogPostion(mContext);


    }

    BluetoothDeviceAdapter bluetoothDeviceAdapter=null;
    private void initView() {

        rlTop = findViewById(R.id.rl_top);
        tvAddress = findViewById(R.id.tv_address);
        rlClose = findViewById(R.id.rl_close);
        rvDevices = findViewById(R.id.rv_devices);

        bluetoothDeviceAdapter=new BluetoothDeviceAdapter(mBleDeviceList,this);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(mContext);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvDevices.setLayoutManager(linearLayoutManager);
        rvDevices.setAdapter(bluetoothDeviceAdapter);

        rlClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dismiss();
            }
        });

    }

    @Override
    public void dismiss() {
        if(scanCallback!=null)
        {
           stopScanBle();
        }
        super.dismiss();
    }

    /**
     * 常量初始化
     * @return false
     */
    public boolean initialize() {
        // For API level 18 and above, get a reference to BluetoothAdapter through
        if (mBluetoothManager == null) {
            mBluetoothManager = (BluetoothManager) mContext. getSystemService(Context.BLUETOOTH_SERVICE);
            if (mBluetoothManager == null) {
                Toast.makeText(mContext, "设备不支持蓝牙功能", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        mBluetoothAdapter = mBluetoothManager.getAdapter();
        if (mBluetoothAdapter == null) {
            Log.i(TAG, "Unable to obtain a BluetoothAdapter.");
            return false;
        }
        mBluetoothLeScanner=mBluetoothAdapter.getBluetoothLeScanner();
        return true;
    }
    private void startScanBle()
    {
        mBluetoothManager = (BluetoothManager)  mContext.getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = mBluetoothManager.getAdapter();
        mBluetoothLeScanner = mBluetoothAdapter.getBluetoothLeScanner();
        if(mBluetoothManager==null)
        {
            Toast.makeText(mContext, "设备不支持蓝牙功能", Toast.LENGTH_SHORT).show();
        }
        // 打开蓝牙
      else if (!mBluetoothAdapter.isEnabled())
        {
            Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            // 设置蓝牙可见性，最多300秒
            intent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
            mContext.startActivity(intent);

        }
       else if(mBluetoothLeScanner!=null)
        {
            mBluetoothLeScanner.startScan(scanCallback);

        }
    }
    private    void stopScanBle() {
        if (null != scanCallback && null != mBluetoothLeScanner) {
            mBluetoothLeScanner.stopScan(scanCallback);
        }
    }

    // 蓝牙扫描回调
    private ScanCallback scanCallback = new ScanCallback() {
        @Override
        public void onScanResult(int callbackType, final ScanResult result) {   
            super.onScanResult(callbackType, result);

            BleDevice bleDevice = new BleDevice(false, result.getRssi(),result.getDevice());
            if (!TextUtils.isEmpty(result.getDevice().getName())) {
                Log.i("textLog", "result.getRssi is " + result.getRssi());
                Log.i("textLog", "result.getRssi is " + bleDevice.toString());
                bluetoothDeviceAdapter.updateBluetoothDeviceList(bleDevice);
                bluetoothDeviceAdapter.notifyDataSetChanged();
            }
            if(mBleDeviceList.size()==0)
            {
                rvDevices.setVisibility(View.GONE);
            }
            else{
                rvDevices.setVisibility(View.VISIBLE);
            }
        }
        @Override
        public void onBatchScanResults(List<ScanResult> results) {
            super.onBatchScanResults(results);
            Log.i("textLog", "results is " + results.size());
            for (int i = 0; i < results.size(); i++) {
                Log.i("textLog", "results is " + results.get(i).getDevice().getName() + "");
            }
        }
        @Override
        public void onScanFailed(int errorCode) {
            Log.i("textLog", "errorCode is " + errorCode);
            super.onScanFailed(errorCode);
        }
    };

    @Override
    public void onClickBleItem(int position) {

        mBluetoothChoiceListener.choiceBluetoothDevice(mBleDeviceList.get(position));
        setBleDevice(mBleDeviceList.get(position));
        tvAddress.setText(mBleDeviceList.get(position).getBluetoothDevice().getAddress());
        PreferencesUtils.putString(mContext, PubConstants.BLE_DEVICES_ADDRESS, mBleDeviceList.get(position).getBluetoothDevice().getAddress());
        PreferencesUtils.putString(mContext, PubConstants.BLE_NAME, mBleDeviceList.get(position).getBluetoothDevice().getName());
        dismiss();
    }

    public interface BluetoothChoiceListener {
        void choiceBluetoothDevice(BleDevice  bleDevice);
    }
}
