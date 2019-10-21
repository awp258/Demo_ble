package com.hohem.dem.ble;

import android.Manifest;
import android.app.Activity;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.test.ble.PreferencesUtils;
import com.test.ble.PubUtils;
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


public class MainActivity extends Activity implements View.OnClickListener, BluetoothDeviceDialog.BluetoothChoiceListener  {

    private static final String TAG =MainActivity.class.getSimpleName() ;
    private Button mBtnOpen;
    private Button mBtnSend;
    private Button mBtnClosed;
    private TextView mTvName;
    private TextView mTvBelAddress;
    private TextView mTxBleRequest;
    private TextView mTxBleCallback;
    String address="",devicesName="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        data = BleCtrlCommand.getInstance().bleContrlCommand();
        EventBus.getDefault().register(this);

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
        address= PreferencesUtils.getString(MainActivity.this, PubConstants.BLE_DEVICES_ADDRESS,"");
        devicesName= PreferencesUtils.getString(MainActivity.this, PubConstants.BLE_NAME,"");
    }

    private String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.BLUETOOTH,
            Manifest.permission.BLUETOOTH_ADMIN,Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WAKE_LOCK};
    private void requestPermission() {
        PermissionsManager.getInstance().requestPermissionsIfNecessaryForResult(MainActivity.this, permissions, new PermissionsResultAction() {
            @Override
            public void onGranted() {

                Log.i("textLog","initBle >>>>>");
                BleManager.getDefault().start(MainActivity.this);
                if(address.length()!=0)
                {
                    try {
                        Log.i(TAG, "address : " +address + "   devicesName : "+devicesName);
                        Boolean isBleConnect   = BleManager.getDefault().isCheckBleConnect(MainActivity.this, address);
                        Log.i(TAG, "data :  " +PubUtils.getInstance().bytesToHexString(data)+"isBleConnect  "+isBleConnect);
                        BleManager.getDefault().sendMessage(MainActivity.this,address,data );
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

                Toast.makeText(MainActivity.this, " 请申请："+permission+"权限", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        PermissionsManager.getInstance().notifyPermissionsChange(permissions, grantResults);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId())
        {
            case R.id.btn_closed:

                if(bluetoothDeviceDialog!=null){
                    bluetoothDeviceDialog.dismiss();
                }

                if(bleDevice==null){
                    Toast.makeText(MainActivity.this, " 请选择蓝牙设备", Toast.LENGTH_SHORT).show();
                    return;
                }
                BleManager.getDefault().disconnect();
                break;
            case R.id.btn_open:
                bluetoothDeviceDialog=new BluetoothDeviceDialog(MainActivity.this,this);
                bluetoothDeviceDialog.show();
                break;
            case R.id.btn_send:
                if(bleDevice==null){
                    Toast.makeText(MainActivity.this, " 请选择蓝牙设备", Toast.LENGTH_SHORT).show();
                    return;
                }

                BleManager.getDefault().sendMessage(MainActivity.this,bleDevice.getBluetoothDevice().getAddress(),data );
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onGetMessage(String  cmdText) {
        Log.i(TAG," 返回的串口指令： : " + cmdText );
        mTxBleCallback.setText(" 返回的串口指令： : " + cmdText );
        //    String command= PubUtils.getInstance().bytesToHexString(BleCtrlCommand.getInstance().testCommand());
    }

    BleDevice bleDevice=null;
    BluetoothDeviceDialog bluetoothDeviceDialog=null;
    byte []data=null;
    @Override
    public void choiceBluetoothDevice(BleDevice bleDevice) {
        this.bleDevice=bleDevice;
        mTvBelAddress.setText(bleDevice.getBluetoothDevice().getAddress());
        mTvName.setText(bleDevice.getBluetoothDevice().getName());

        BleManager.getDefault().start(MainActivity.this);

        String commandTxt= PubUtils.getInstance().bytes2HexString(data);
        mTxBleRequest.setText("发送指令："+commandTxt);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        EventBus.getDefault().unregister(this);
    }
}
