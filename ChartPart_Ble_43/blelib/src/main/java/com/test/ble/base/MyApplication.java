package com.test.ble.base;

import android.app.Application;
import android.content.Context;

public class MyApplication extends Application {

	static	MyApplication  mInstance=null;
	Context mContext;
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		mContext = getApplicationContext();

		mInstance = this;
	}
	public static MyApplication getInstance() {
		return mInstance;
	}
}
