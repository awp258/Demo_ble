package com.test.ble.bean;

import android.support.annotation.NonNull;

import java.io.File;
import java.net.URI;
import java.util.List;

/**
 * Copyright (C), 2013-2019, 深圳市浩瀚卓越科技有限公司
 * Author: Abraham.ai@hohem-tech.com
 * Date: 2019/9/24 11:43
 * Description:  固件升级类
 * History:
 */
public class FirmUpgradeInfor extends File
{    //电机信息
    List<MachineInfo> machineInfoList;
    /**
     * 版本号
     */
    double versionCode;



    //   电机
    public FirmUpgradeInfor(@NonNull String pathname) {
        super(pathname);
    }

    public FirmUpgradeInfor(@NonNull URI uri) {
        super(uri);
    }
}
