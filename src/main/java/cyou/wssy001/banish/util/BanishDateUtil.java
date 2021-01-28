package cyou.wssy001.banish.util;

import cn.hutool.core.date.DateUtil;

import java.util.Date;

/**
 * @projectName: lava-banish
 * @className: BanishDateUtil
 * @description:
 * @author: alexpetertyler
 * @date: 2021/1/28
 * @version: v1.0
 */
public class BanishDateUtil {
    public static Date convertToDate(String dateString) {
        int num;
        Date date;
        if (dateString.contains("min")) {
            num = Integer.parseInt(dateString.replace("min", ""));
            date = DateUtil.offsetMinute(new Date(), num);
        } else if (dateString.contains("分钟")) {
            num = Integer.parseInt(dateString.replace("分钟", ""));
            date = DateUtil.offsetMinute(new Date(), num);
        } else if (dateString.contains("h")) {
            num = Integer.parseInt(dateString.replace("h", ""));
            date = DateUtil.offsetMinute(new Date(), num);
        } else if (dateString.contains("小时")) {
            num = Integer.parseInt(dateString.replace("小时", ""));
            date = DateUtil.offsetMinute(new Date(), num);
        } else if (dateString.contains("day")) {
            num = Integer.parseInt(dateString.replace("day", ""));
            date = DateUtil.offsetMinute(new Date(), num);
        } else if (dateString.contains("天")) {
            num = Integer.parseInt(dateString.replace("天", ""));
            date = DateUtil.offsetMinute(new Date(), num);
        } else if (dateString.contains("mon")) {
            num = Integer.parseInt(dateString.replace("mon", ""));
            date = DateUtil.offsetMinute(new Date(), num);
        } else if (dateString.contains("月")) {
            num = Integer.parseInt(dateString.replace("月", ""));
            date = DateUtil.offsetMinute(new Date(), num);
        } else {
            date = DateUtil.nextWeek();
        }
        return date;
    }
}
