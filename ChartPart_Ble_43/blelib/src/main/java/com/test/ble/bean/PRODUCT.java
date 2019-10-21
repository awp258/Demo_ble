package com.test.ble.bean;

import com.test.ble.R;

/**
 * Copyright (C), 2013-2019, 深圳市浩瀚卓越科技有限公司
 * Author: Abraham.ai@hohem-tech.com
 * Date: 2019/9/29 18:14
 * Description:  产品信息绑定
 * History:
 */
public enum PRODUCT {
       HGS("HGS", R.mipmap.ic_product_hgs) ,
       DG1("DG1", R.mipmap.ic_product_dg1) ,
       HGS1_5("HGS1.5", R.mipmap.ic_product_dg1) ,
       HGS2("HGS2", R.mipmap.ic_product_hgs) ,
       MULTI("Multi", R.mipmap.ic_product_hg5) ,
       HGS7("DGS", R.mipmap.ic_product_dg1) ;
    private String name;

    PRODUCT(String name, int iconRes) {
        this.name = name;
        this.iconRes = iconRes;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIconRes() {
        return iconRes;
    }

    public void setIconRes(int iconRes) {
        this.iconRes = iconRes;
    }

    private int iconRes;

}
