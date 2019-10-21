package com.test.ble.adapter;

import android.bluetooth.BluetoothDevice;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.test.ble.PubUtils;
import com.test.ble.R;
import com.test.ble.bean.BleDevice;
import com.test.ble.utils.PublicUtils;

import java.util.ArrayList;
import java.util.List;


/**
 * Copyright (C), 2013-2019, 深圳市浩瀚卓越科技有限公司
 * Author: Abraham.ai@hohem-tech.com
 * Date: 2019/9/25 14:35
 * Description:
 * History:
 */
public class BluetoothDeviceAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    List<BleDevice> bluetoothDeviceList=new ArrayList<>();

    BelInfoAdapterListener belInfoAdapterListener;
    public BluetoothDeviceAdapter(List<BleDevice> bluetoothDeviceList, BelInfoAdapterListener belInfoAdapterListener)
    {
        this.bluetoothDeviceList = bluetoothDeviceList;
        this.belInfoAdapterListener=belInfoAdapterListener;
    }
    //刷新 子控件
    public void updateBluetoothDeviceList(BleDevice device) {


       boolean  isExistDevice=isExistDevice(device);

       if(isExistDevice)
       {
           updateDeviceInfo(device);
       }
       else{
           bluetoothDeviceList.add(device);
       }
    }
    private boolean  isExistDevice(BleDevice device)
    {
        boolean isExistDevice=false;
        for (int i = 0; i < bluetoothDeviceList.size(); i++) {
            BluetoothDevice bluetoothDevice = bluetoothDeviceList.get(i).getBluetoothDevice();
            if (device.getBluetoothDevice().getAddress().equals(bluetoothDevice.getAddress())) {
                Log.i("textLog", " 更新信号灯指示 ");
                bluetoothDeviceList.set(i,device);
                return true;
            }
        }
        return isExistDevice;
    }
    private void   updateDeviceInfo(BleDevice device)
    {
        for (int i = 0; i < bluetoothDeviceList.size(); i++) {
            BluetoothDevice bluetoothDevice = bluetoothDeviceList.get(i).getBluetoothDevice();
            if (device.getBluetoothDevice().getAddress().equals(bluetoothDevice.getAddress())) {
                Log.i("textLog", " 更新信号灯指示 ");
                bluetoothDeviceList.set(i,device);
                return ;
            }
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ble_device_element,parent,false );
        return new MyViewHolder(view);
    }
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position)
    {

        if(holder instanceof  MyViewHolder )
        {
            BleDevice device = bluetoothDeviceList.get(position);
            MyViewHolder myViewHolder= (MyViewHolder) holder;
            myViewHolder.mTvRss.setVisibility(View.VISIBLE);
            int  deviceSignal = device.getRssi();
            PublicUtils.getInstance().setRssTextViewAndImageView(deviceSignal , myViewHolder.mTvRss,myViewHolder.mIvState );
             myViewHolder.mTvAddress.setText(device.getBluetoothDevice().getAddress());
            String devicesName=device.getBluetoothDevice().getName().trim();
                   devicesName =devicesName.toUpperCase();
                   devicesName= PubUtils.getInstance().bleNameToProName(devicesName);
            myViewHolder.mTvName.setText(devicesName);

            if(device.isConnection())
            {
                myViewHolder.mTvState.setText("已连接");
            }
            else{
                myViewHolder.mTvState.setText("未连接");
            }
            myViewHolder.mRlMain.setOnClickListener(v -> belInfoAdapterListener.onClickBleItem(position));

        }
    }
    @Override
    public int getItemCount() {
        return bluetoothDeviceList.size();
    }
    static class MyViewHolder extends RecyclerView.ViewHolder
    {
        private RelativeLayout mRlMain;
        private ImageView mIvState;
        private TextView mTvState;
        private TextView mTvRss;
        private TextView mTvName;
        private TextView mTvAddress;
        public MyViewHolder(View itemView) {
            super(itemView);
            mRlMain = itemView.findViewById(R.id.rl_main);
            mIvState = itemView.findViewById(R.id.iv_state);
            mTvState =itemView. findViewById(R.id.tv_ble_state);
            mTvRss = itemView.findViewById(R.id.tv_rss);
            mTvName = itemView.findViewById(R.id.tv_name);
            mTvAddress = itemView.findViewById(R.id.tv_address);

        }
    }
    public interface BelInfoAdapterListener {
        void onClickBleItem(int position);
    }
}
