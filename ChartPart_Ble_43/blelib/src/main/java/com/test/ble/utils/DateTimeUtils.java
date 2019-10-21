package com.test.ble.utils;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Copyright (C), 2013-2019, 深圳市浩瀚卓越科技有限公司
 * Author: Abraham.ai@hohem-tech.com
 * Date: 2019/8/20 14:34
 * Description:  时间戳处理工具类
 * History:
 */
public class DateTimeUtils {

    private  volatile  static  DateTimeUtils instance;

    private static final String TAG = DateTimeUtils.class.getCanonicalName();

    public final static String FORMAT_YEAR = "yyyy";
    public final static String FORMAT_MONTH_DAY = "MM月dd日";
    public final static String FORMAT_DATE_01 = "yyyy-MM-dd";
    public final static String FORMAT_DATE_02 = "yyyy/MM/dd";
    public final static String FORMAT_DATE = "yyyy年MM月dd日";
    public final static String FORMAT_DATE_YEAR = "yyyy年";
    public final static String MONTH_DATE = "MM月dd日";
    public final static String FORMAT_TIME = "HH:mm";
    public final static String FORMAT_MONTH_DAY_TIME = "MM月dd日  hh:mm";

    public final static String FORMAT_DATE_TIME = "yyyy-MM-dd HH:mm";
    public final static String FORMAT_DATE1_TIME = "yyyy/MM/dd HH:mm";
    public final static String FORMAT_DATE2_TIME = "yyyy.MM.dd HH:mm";
    public final static String FORMAT_DATE3_TIME = "yyyy.MM.dd";
    public final static String FORMAT_DATE_TIME_SECOND = "yyyy/MM/dd HH:mm:ss";
    public final static String FORMAT_DATE_TIME_SECOND_down = "yyyy-MM-dd HH:mm:ss";

    private static SimpleDateFormat sdf = new SimpleDateFormat();
    private static final int YEAR = 365 * 24 * 60 * 60;// 年
    private static final int MONTH = 30 * 24 * 60 * 60;// 月
    private static final int DAY = 24 * 60 * 60;// 天
    private static final int HOUR = 60 * 60;// 小时
    private static final int MINUTE = 60;// 分钟

    public static DateTimeUtils getInstance() {
        if(instance==null)
        {
            synchronized (DateTimeUtils.class)
            {
                if (instance==null){
                    instance=new  DateTimeUtils();
                }
            }

        }
        return instance;
    }

    public  double getHourInterval(Date start, Date end) {
        /*Date1间隔=Date1.getTime()-Date2.getTime();得出来的是毫秒数.
                除1000是秒,再除60是分,再除60是小时*/
        long ms = end.getTime() - start.getTime();
        double result = (ms * 1.0 / (1000 * 60 * 60));
        return result;
    }

    /**
     * 根据时间戳获取描述性时间，如3分钟前，1天前
     *
     * @param timestamp 时间戳 单位为毫秒
     * @return 时间字符串
     */
    public String getDescriptionTimeFromTimestamp(long timestamp) {
        long currentTime = System.currentTimeMillis();
        long timeGap = (currentTime - timestamp) / 1000;// 与现在时间相差秒数
        System.out.println("timeGap: " + timeGap);
        String timeStr = null;
        if (timeGap > YEAR) {
            timeStr = timeGap / YEAR + "年前";
        } else if (timeGap > MONTH) {
            timeStr = timeGap / MONTH + "个月前";
        } else if (timeGap > DAY) {// 1天以上
            timeStr = timeGap / DAY + "天前";
        } else if (timeGap > HOUR) {// 1小时-24小时
            timeStr = timeGap / HOUR + "小时前";
        } else if (timeGap > MINUTE) {// 1分钟-59分钟
            timeStr = timeGap / MINUTE + "分钟前";
        } else {// 1秒钟-59秒钟
            timeStr = "刚刚";
        }
        return timeStr;
    }

    /**
     * 获取当前日期的指定格式的字符串
     *
     * @param format 指定的日期时间格式，若为null或""则使用指定的格式"yyyy-MM-dd HH:MM"
     * @return
     */
    public String getCurrentTime(String format) {
        if (format == null || format.trim().equals("")) {
            sdf.applyPattern(FORMAT_DATE_TIME);
        } else {
            sdf.applyPattern(format);
        }
        return sdf.format(new Date());
    }

    public String getcurentTime() {
        long currentTime = System.currentTimeMillis();
        Date date = new Date(currentTime);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy_MM_dd-HH_mm");
        return sdf.format(date);
    }

    public String toStringFormat(String format, String date) {
        sdf.applyLocalizedPattern(format);
        try {
            return dateToString(sdf.parse(date), format);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }

    // date类型转换为String类型
    // formatType格式为yyyy-MM-dd HH:mm:ss//yyyy年MM月dd日 HH时mm分ss秒
    // data Date类型的时间
    public String dateToString(Date data, String formatType) {
        return new SimpleDateFormat(formatType).format(data);
    }

    /**
     * date 转换成 时间戳
     *
     * @param date
     * @return
     */

    public String DateToLongTime(Date date) {
        String longTime = "";
        long startTimeLong = dateToLong(date);
        longTime = String.valueOf(startTimeLong);
        if (longTime.length() == 13) {
            longTime = longTime.substring(0, 10);
            Log.i(TAG, "时间戳  longTime is " + longTime);
            return longTime;
        } else {
            longTime = longTime.substring(0, 10);
            return longTime;
        }

    }

    // long类型转换为String类型
    // currentTime要转换的long类型的时间
    // formatType要转换的string类型的时间格式
//    public static String longToString(long currentTime, String formatType) {
//        String strTime = "";
//        Date date = longToDate(currentTime, formatType);// long类型转成Date类型
//        strTime = dateToString(date, formatType); // date类型转成String
//        return strTime;
//    }

    // string类型转换为date类型
    // strTime要转换的string类型的时间，formatType要转换的格式yyyy-MM-dd HH:mm:ss//yyyy年MM月dd日
    // HH时mm分ss秒，
    // strTime的时间格式必须要与formatType的时间格式相同
    public Date stringToDate(String strTime, String formatType) {
        SimpleDateFormat formatter = new SimpleDateFormat(formatType);
        Date date = null;
        try {
            date = formatter.parse(strTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    // long转换为Date类型
    // currentTime要转换的long类型的时间
    // formatType要转换的时间格式yyyy-MM-dd HH:mm:ss//yyyy年MM月dd日 HH时mm分ss秒
    public Date longToDate(long currentTime, String formatType) {
        Date dateOld = new Date(currentTime); // 根据long类型的毫秒数生命一个date类型的时间
        String sDateTime = dateToString(dateOld, formatType); // 把date类型的时间转换为string
        Date date = stringToDate(sDateTime, formatType); // 把String类型转换为Date类型
        return date;
    }


    public static String longToString(long seconds, String format) {
        if (format == null || format.isEmpty()) format = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        String format1 = sdf.format(new Date(Long.valueOf(seconds + "000")));
        return format1;
    }


    // string类型转换为long类型
    // strTime要转换的String类型的时间
    // formatType时间格式
    // strTime的时间格式和formatType的时间格式必须相同
    public  long stringToLong(String strTime, String formatType) {
        Date date = stringToDate(strTime, formatType); // String类型转成date类型
        if (date == null) {
            return 0;
        } else {
            long currentTime = dateToLong(date); // date类型转成long类型
            return currentTime;
        }
    }

    // date类型转换为long类型
    // date要转换的date类型的时间
    public  long dateToLong(Date date) {
        return date.getTime();
    }

    public String getTime(long time) {
        return getTime(time, "yyyy-MM-dd HH:mm");
    }

    public String getTime(long time, String formatType) {
        SimpleDateFormat format = new SimpleDateFormat(formatType);
        return format.format(new Date(time));
    }

    public String getHourAndMin(long time) {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        return format.format(new Date(time));
    }

    /**
     * 获取聊天时间：因为sdk的时间默认到秒故应该乘1000
     *
     * @param @param  timesamp
     * @param @return
     * @return String
     * @throws
     * @Title: getChatTime
     * @Description:
     */
    public String getChatTime(long timesamp) {

        return getChatTime(timesamp, "yyyy-MM-dd HH:mm");
    }


    public String getChatTime(long timesamp, String format) {
        long clearTime = timesamp * 1000;
        String result = "";
        SimpleDateFormat sdf = new SimpleDateFormat("dd");
        Date today = new Date(System.currentTimeMillis());
        Date otherDay = new Date(clearTime);
        int temp = Integer.parseInt(sdf.format(today)) - Integer.parseInt(sdf.format(otherDay));

        switch (temp) {
            case 0:
                result = "今天 " + getHourAndMin(clearTime);
                break;
            case 1:
                result = "昨天 " + getHourAndMin(clearTime);
                break;
            case 2:
                result = "前天 " + getHourAndMin(clearTime);
                break;

            default:
                result = getTime(clearTime, format);
                break;
        }

        return result;
    }

    /**
     * 获取当前系统时间
     *
     * @return
     */
    public String getCurrentTime() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 设置日期格式
        return df.format(new Date());// new Date()为获取当前系统时间
    }

    public String getCurrentYearMonthDayTime() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");// 设置日期格式
        return df.format(new Date());// new Date()为获取当前系统时间
    }

    /**
     * 获取当前系统时间
     *
     * @return
     */
    public String getIntervalDay(long beginTime, long endTime) {
        long betweenDays = endTime - beginTime;
        Log.i(TAG, betweenDays + "");
        betweenDays = betweenDays / (60 * 60 * 24) + 1;
        Log.i(TAG, "---------->");
        Log.i(TAG, betweenDays + "");
        return "约" + betweenDays + "天";
    }

    /**
     * @param startDate 开始时间
     * @param stopDate  结束时间
     * @return
     */

    public String getIntervalDay(Date startDate, Date stopDate) {
        long startTimeLong = DateTimeUtils.getInstance().dateToLong(startDate);
        String startTime = String.valueOf(startTimeLong);
        startTime = startTime.substring(0, 10);
        long startTimeLog = Long.parseLong(startTime);
        long endTimeLong = DateTimeUtils.getInstance().dateToLong(stopDate);
        String stopTime = String.valueOf(endTimeLong);
        stopTime = stopTime.substring(0, 10);
        long stopTimeLog = Long.parseLong(stopTime);
        long betweenDays = stopTimeLog - startTimeLog;

        if (betweenDays < 0) {
            return "";
        } else {
            Log.i(TAG, betweenDays + "");
            betweenDays = betweenDays / (60 * 60 * 24) + 1;
            return betweenDays + "";
        }
    }

    /**
     * 根据时间戳求间隔天数
     *
     * @param beginTime
     * @param endTime
     * @return
     */
    public  long getIntervalDays(long beginTime, long endTime) {
        long intervalMilli = beginTime - endTime;
        return (intervalMilli / (24 * 60 * 60 * 1000));
    }

    /**
     * 获取几天后的时间戳
     *
     * @param date 传入的日期
     * @param day  天数
     * @return
     */
    public String getAfterTime(Date date, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) + day);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String endDateStr = sdf.format(calendar.getTime());
        Log.i(TAG, " 结束时间: " + endDateStr);
        Date afterDate = calendar.getTime();
        long afterDateTime = DateTimeUtils.getInstance().dateToLong(afterDate);
        String afterDateTimeInfor = String.valueOf(afterDateTime);
        afterDateTimeInfor = afterDateTimeInfor.substring(0, 10);
        Log.i(TAG, " 结束时间戳 : " + afterDateTimeInfor);
        return afterDateTimeInfor;
    }


    /**
     * 获取几天后的时间戳
     *
     * @param date 传入的日期
     * @param day  天数
     * @return
     */
    public Date getAfterDateTime(Date date, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) + day);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String endDateStr = sdf.format(calendar.getTime());
        Log.i(TAG, " 结束时间: " + endDateStr);
        Date afterDate = calendar.getTime();

        return afterDate;
    }

    private  final int invalidAge = -1;//非法的年龄，用于处理异常。

    public  int getAgeByDate(Date birthday) {
        Calendar calendar = Calendar.getInstance();

        //calendar.before()有的点bug
        if (calendar.getTimeInMillis() - birthday.getTime() < 0L) {
            return invalidAge;
        }
        int yearNow = calendar.get(Calendar.YEAR);
        int monthNow = calendar.get(Calendar.MONTH);
        int dayOfMonthNow = calendar.get(Calendar.DAY_OF_MONTH);
        calendar.setTime(birthday);
        int yearBirthday = calendar.get(Calendar.YEAR);
        int monthBirthday = calendar.get(Calendar.MONTH);
        int dayOfMonthBirthday = calendar.get(Calendar.DAY_OF_MONTH);
        int age = yearNow - yearBirthday;
        if (monthNow <= monthBirthday && monthNow == monthBirthday && dayOfMonthNow < dayOfMonthBirthday || monthNow < monthBirthday) {
            age--;
        }
        return age;
    }
}
