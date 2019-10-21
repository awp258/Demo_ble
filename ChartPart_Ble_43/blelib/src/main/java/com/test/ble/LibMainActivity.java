package com.test.ble;

import android.Manifest;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import com.test.ble.View.BluetoothDeviceDialog;
import com.test.ble.base.BleManager;
import com.test.ble.bean.BleDevice;
import com.test.ble.constants.BleCtrlCommand;
import com.test.ble.constants.PubConstants;
import com.test.ble.permission.PermissionsManager;
import com.test.ble.permission.PermissionsResultAction;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.lang.ref.WeakReference;


public class LibMainActivity extends AppCompatActivity implements View.OnClickListener, BluetoothDeviceDialog.BluetoothChoiceListener  {

    private static final String TAG =LibMainActivity.class.getSimpleName() ;
    private Button mBtnOpen;
    private Button mBtnSend;
    private Button mBtnClosed;
    private TextView mTvName;
    private TextView mTvBelAddress;
    private TextView mTxBleRequest;
    private TextView mTxBleCallback;
    String address="",devicesName="";

    MyHandler mHandler=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        data = BleCtrlCommand.getInstance().testCommand();
        EventBus.getDefault().register(this);
        mHandler=new MyHandler(this);
        mBtnOpen = findViewById(R.id.btn_open);
        mBtnSend = findViewById(R.id.btn_send);
        mBtnClosed = findViewById(R.id.btn_closed);
        mTvName = findViewById(R.id.tv_name);
        mTvBelAddress = findViewById(R.id.tv_bel_address);
        mTxBleRequest = findViewById(R.id.tx_ble_request);
        mTxBleCallback = findViewById(R.id.tx_ble_callback);
        mBtnOpen.setOnClickListener(this);
        mBtnClosed.setOnClickListener(this);
        mBtnSend.setOnClickListener(this);
        requestPermission();

        address= PreferencesUtils.getString(LibMainActivity.this, PubConstants.BLE_DEVICES_ADDRESS,"");
        devicesName= PreferencesUtils.getString(LibMainActivity.this, PubConstants.BLE_NAME,"");



    }
    private String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.BLUETOOTH,
            Manifest.permission.BLUETOOTH_ADMIN,Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WAKE_LOCK};
    private void requestPermission() {
        PermissionsManager.getInstance().requestPermissionsIfNecessaryForResult(LibMainActivity.this, permissions, new PermissionsResultAction() {
            @Override
            public void onGranted() {

                Log.i("textLog","initBle >>>>>");
                BleManager.getDefault().start(LibMainActivity.this);
                if(address.length()!=0)
                {
                    try {
                        Log.i(TAG, "address : " +address + "   devicesName : "+devicesName);
                        Boolean isBleConnect   = BleManager.getDefault().isCheckBleConnect(LibMainActivity.this, address);
                        Log.i(TAG, "data :  " +PubUtils.getInstance().bytesToHexString(data)+"isBleConnect  "+isBleConnect);
                        BleManager.getDefault().sendMessage(LibMainActivity.this,address,data );
                    }
                    catch (Exception e)
                    {
                        Log.i(TAG, "Exception : " +e);
                    }
                }
            }

            @Override
            public void onDenied(String permission) {
                Log.i("textLog","permission >>>>>" + permission );

                Toast.makeText(LibMainActivity.this, " 请申请："+permission+"权限", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        PermissionsManager.getInstance().notifyPermissionsChange(permissions, grantResults);
    }


    @Override
    public void onClick(View v) {

      if(v.getId()==R.id.btn_closed)
        {
            Message message = mHandler.obtainMessage();
            message.what = 1;
            message.obj="1123445";
            mHandler.sendMessage(message);
            if(bluetoothDeviceDialog!=null){
                bluetoothDeviceDialog.dismiss();
            }
        }
        else if(v.getId()==R.id.btn_open)
        {
            bluetoothDeviceDialog=new BluetoothDeviceDialog(LibMainActivity.this,this);
            bluetoothDeviceDialog.show();
        }
      else if(v.getId()==R.id.btn_send)
      {
          if(bleDevice==null){
              Toast.makeText(LibMainActivity.this, " 请选择蓝牙设备", Toast.LENGTH_SHORT).show();
              return;
          }
          BleManager.getDefault().sendMessage(LibMainActivity.this,bleDevice.getBluetoothDevice().getAddress(),data );
      }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onGetMessage(String  cmdText) {
        Log.i(TAG," 返回的串口指令： : " + cmdText );
    //    mTxBleCallback.setText(" 返回的串口指令： :  "+ cmdText);


        Message message = mHandler.obtainMessage();
        message.what = 2;
        message.obj=cmdText;
        mHandler.sendMessage(message);
        Log.i(TAG," 返回的串口指令111： : " + cmdText );

        // String command= PubUtils.getInstance().bytesToHexString(BleCtrlCommand.getInstance().testCommand());
    }



    BleDevice bleDevice=null;
    BluetoothDeviceDialog bluetoothDeviceDialog=null;
    byte []data=null;
    @Override
    public void choiceBluetoothDevice(BleDevice bleDevice) {
        this.bleDevice=bleDevice;
        mTvBelAddress.setText(bleDevice.getBluetoothDevice().getAddress());
        mTvName.setText(bleDevice.getBluetoothDevice().getName());
        String commandTxt= PubUtils.getInstance().bytes2HexString(data);
        mTxBleRequest.setText("发送指令："+commandTxt);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }


    /**
     * weakReference 防止内存泄漏
     */
    private static class MyHandler extends Handler {
        private WeakReference<LibMainActivity> weakReference;
        MyHandler(LibMainActivity activity) {
            weakReference = new WeakReference<>(activity);
        }
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Log.e(TAG, "handleMessage: ");
            LibMainActivity activity = weakReference.get();
              if (activity != null) {
                        String cmd= (String) msg.obj;
                        activity.mTxBleCallback.setText("蓝牙回复： "+cmd);
                    }
            }
        }

}
