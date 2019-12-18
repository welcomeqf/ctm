package com.yq.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author qf
 * @date 2019/11/26
 * @vesion 1.0
 **/
public class WeekUtils {

   private static Map weekNumberMap = new HashMap();
   static {
      weekNumberMap.put(0,1);
      weekNumberMap.put(1,2);
      weekNumberMap.put(2,3);
      weekNumberMap.put(3,4);
      weekNumberMap.put(4,5);
      weekNumberMap.put(5,6);
      weekNumberMap.put(6,7);


   }

   /**
    * 查询一段时间的星期
    * @param dateFrom 起始日期
    * @param dateEnd 结束日期
    * @param weekDays 0-周天  1-周一  ... 6-周六
    * @return
    */
   public static List getDates(String dateFrom, String dateEnd, List weekDays) {
      long time;
      long perDayMilSec = 24L * 60 * 60 * 1000;
      List dateList = new ArrayList();
      SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
      // 需要查询的星期系数
      String strWeekNumber = weekForNum(weekDays);
      try {
         dateFrom = sdf.format(sdf.parse(dateFrom).getTime() - perDayMilSec);
         while (true) {
            time = sdf.parse(dateFrom).getTime();
            time = time + perDayMilSec;
            Date date = new Date(time);
            dateFrom = sdf.format(date);
            if (dateFrom.compareTo(dateEnd) <= 0) {
               // 查询的某一时间的星期系数
               Integer weekDay = dayForWeek(date);
               // 判断当期日期的星期系数是否是需要查询的
               if (strWeekNumber.contains(weekDay.toString())) {
                  dateList.add(dateFrom);
               }
            } else {
               break;
            }
         }
      } catch (ParseException e) {
         e.printStackTrace();
      }
      return dateList;
   }


   /**
    * 等到当期时间的周系数。星期日：0，星期一：1，星期二：2，星期三：3，星期四：4，星期五：5，星期六：6
    * @param date
    * @return
    */
   public static Integer dayForWeek(Date date) {
      Calendar calendar = Calendar.getInstance();
      calendar.setTime(date);
      return calendar.get(Calendar.DAY_OF_WEEK);
   }


   /**
    * 得到对应星期的系数 0：1，星期
    */
   public static String weekForNum(List weekDays) {
      // 返回结果为组合的星期系数
      String weekNumber = "";


      for (Object weekDay : weekDays) {
         weekNumber = weekNumber + "" + getWeekNum((Integer) weekDay).toString();
      }
      return weekNumber;


   }


   // 将星期转换为对应的系数 0,星期日; 1,星期一; 2....
   public static Integer getWeekNum(int strWeek) {
      return (Integer) weekNumberMap.get(strWeek);
   }

}
