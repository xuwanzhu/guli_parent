package com.atguigu.question.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 日期操作工具类
 */
public class DateUtil {

    private static final String dateFormat = "yyyy-MM-dd HH:mm";

    /**
     * 格式化日期
     *
     * @param date
     * @return
     */
    public static String formatDate(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        return sdf.format(date);

    }

    /**
     * 在日期date上增加amount天 。
     *
     * @param date   处理的日期，非null
     * @param amount 要加的天数，可能为负数
     */
    public static Date addDays(Date date, int amount) {
        Calendar now =Calendar.getInstance();
        now.setTime(date);
        now.set(Calendar.DATE,now.get(Calendar.DATE)+amount);
        return now.getTime();
    }

    //在日期date上增加minutes分钟，统计多少分钟之前
    public static Date addMinutes (Date date, int minutes ) {
        Calendar now =Calendar.getInstance();
        now.setTime(date);
        now.set(Calendar.MINUTE,now.get(Calendar.MINUTE)+minutes);
        return now.getTime();
    }


    //主函数
    public static void main(String[] args) {
        //System.out.println(DateUtil.formatDate(new Date())); //当天
        //System.out.println(DateUtil.formatDate(DateUtil.addDays(new Date(), -1))); //前一天
        System.out.println(DateUtil.formatDate(DateUtil.addMinutes(new Date(), -1))); //前一分钟

    }
}
