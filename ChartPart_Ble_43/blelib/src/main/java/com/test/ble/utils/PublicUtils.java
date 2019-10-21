package com.test.ble.utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import com.test.ble.R;
import com.test.ble.bean.BleDevice;
import com.test.ble.bean.PRODUCT;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Copyright (C), 2013-2019, 深圳市浩瀚卓越科技有限公司
 * Author: Abraham.ai@hohem-tech.com
 * Date: 2019/8/20 14:34
 * Description:  view层辅助类
 * History:
 */
public class PublicUtils {
    private volatile static  PublicUtils instance=null;
    private static final String TAG = PublicUtils.class.getSimpleName();
    public static PublicUtils getInstance() {
        if(instance==null)
        {
            synchronized (PublicUtils.class)
            {
                if(instance==null)
                {
                    instance=new PublicUtils();
                }
            }
        }
        return instance;
    }

    /**
     * 图片沉浸式
     * @param mContext
     */
    public  void setWindowPictureStatusBarColor(Context mContext)
    {
        Activity activity= (Activity) mContext;
        //最小版本在21以下
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = activity.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }

    }

    /**
     * 设置手机系统状态栏颜色
     *
     * @param mContext
     * @param color
     */
    public void setNoActionBarColor(Context mContext, int color) {

        Activity activity = (Activity) mContext;
        try {
            //9.0适配
            if (Build.VERSION.SDK_INT >= 28) {
                activity.requestWindowFeature(Window.FEATURE_NO_TITLE);
                WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
          //      lp.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
                activity.getWindow().setAttributes(lp);
                View decorView = activity.getWindow().getDecorView();
                int systemUiVisibility = decorView.getSystemUiVisibility();
                int flags = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN;
                systemUiVisibility |= flags;
                activity.getWindow().getDecorView().setSystemUiVisibility(systemUiVisibility);

                //顶部导航栏
                activity.getWindow().setNavigationBarColor(activity.getResources().getColor(color));
            }
         //适配5.0手机
           else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            {
                Window window = activity.getWindow();
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(activity.getResources().getColor(color));
                //底部导航栏
                window.setNavigationBarColor(activity.getResources().getColor(color));
            }






        } catch (Exception e) {
            e.printStackTrace();

        }

    }

    /**
     * 获取手机状态栏高度
     *
     * @param context
     * @return
     */
    public   int getStatusBarHeight(Context context) {
        int statusBarHeight = 0;
        Resources res = context.getResources();
        int resourceId = res.getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            statusBarHeight = res.getDimensionPixelSize(resourceId);
        }
        return statusBarHeight;
    }

    public int getNavigationBarHeight(Context context) {
        Resources resources = context.getResources();
        int resourceId = resources.getIdentifier("navigation_bar_height","dimen", "android");
        int height = resources.getDimensionPixelSize(resourceId);
        Log.v("dbw", "Navi height:" + height);
        return height;
    }

    /**
     * dip转换px
     */
    public  int dip2px(Context context, int dip) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dip * scale + 0.5f);
    }

    /**
     * px转换dip
     */
    public  int px2dip(Context context, int px) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (px / scale + 0.5f);
    }

    /**
     * sp值转换为px值
     */
    public  int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    /**
     * 设置view显示or隐藏
     *
     * @param v
     * @param visibility
     */
    public  void updateVisibility(View v, int visibility) {
        if (v != null && v.getVisibility() != visibility) {
            v.setVisibility(visibility);
        }
    }


    /**
     * 获取屏幕高（像素）
     * @return
     */
    public  int getScreenHeight(Context context) {
        int screenHeight =0;
        if (screenHeight <= 0) {
            WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            DisplayMetrics outMetrics = new DisplayMetrics();
            wm.getDefaultDisplay().getMetrics(outMetrics);
            screenHeight = outMetrics.heightPixels;
        }
        return screenHeight;
    }

    /**
     * 获取屏幕宽（像素）
     * @return
     */
    public  int getScreenWidth(Context context) {
        int screenWidth=0;
        if (screenWidth <= 0) {
            WindowManager wm = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
            DisplayMetrics outMetrics = new DisplayMetrics();
            wm.getDefaultDisplay().getMetrics(outMetrics);
            screenWidth = outMetrics.widthPixels;
        }
        return screenWidth;
    }

    /**
     * 判断是否有虚拟按键
     */
    public  boolean checkDeviceHasNavigationBar(Context context) {
        boolean hasNavigationBar = false;
        Resources rs = context.getResources();
        int id = rs.getIdentifier("config_showNavigationBar", "bool", "android");
        if (id > 0) {
            hasNavigationBar = rs.getBoolean(id);
        }
        try {
            Class systemPropertiesClass = Class.forName("android.os.SystemProperties");
            Method m = systemPropertiesClass.getMethod("get", String.class);
            String navBarOverride = (String) m.invoke(systemPropertiesClass, "qemu.hw.mainkeys");
            if ("1".equals(navBarOverride)) {
                hasNavigationBar = false;
            } else if ("0".equals(navBarOverride)) {
                hasNavigationBar = true;
            }
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return hasNavigationBar;
    }

    public void setRssTextViewAndImageView(int rssival ,TextView textView, ImageView imageView){
        if (rssival != 0) {
            textView.setText("Rssi = " + String.valueOf(rssival));
            if(rssival >= -50){
                imageView.setImageResource(R.drawable.ble04);
            }else if(rssival < -50 && rssival >= -75){
                imageView.setImageResource(R.drawable.ble03);
            }else if(rssival < -75 && rssival >= -90){
                imageView.setImageResource(R.drawable.ble02);
            }else if(rssival < -90){
                imageView.setImageResource(R.drawable.ble01);
            }
        }
    }

    /**
     * 设置 dialog 的位置
     *
     * @param context 当前的activity
     */
    public void settingDialogPostion(Context  context) {

        Activity activity= (Activity) context;
        Window dialogWindow = activity.getWindow();
        WindowManager windowManager = activity.getWindowManager();
        Display d = windowManager.getDefaultDisplay(); // 获取屏幕宽、高用
        WindowManager.LayoutParams p = dialogWindow.getAttributes(); // 获取对话框当前的参数值
        // 获取屏幕的80%
        p.width = (int) (d.getWidth() * 0.70);
        p.x = (int) (d.getWidth() * 0.15);
        p.y = (int) (d.getHeight() * 1) / 3;
        p.alpha = 2f;// 透明度80%
        dialogWindow.setGravity(Gravity.LEFT | Gravity.TOP);
        dialogWindow.setAttributes(p);
    }


    /**
     *
     * @param bleName 蓝牙名称统一大写
     * @return  产品名称
     */
    public String bleNameToProName(BleDevice  bleName)
    {
        PRODUCT product=PRODUCT.HGS;
        return product.getName();

    }

}
