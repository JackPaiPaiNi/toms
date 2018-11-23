package cn.tcl.platform.excel.actions;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import cn.tcl.platform.excel.vo.ImportExcel;

/**
 * 日期处理工具类
 * 
 * @author dylan_xu
 * @date Mar 11, 2012
 * @modified by
 * @modified date
 * @since JDK1.6
 * @see com.util.DateUtil
 */

public class DateUtil {

	static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
	static SimpleDateFormat formatMonth = new SimpleDateFormat("yyyy-MM");
	static SimpleDateFormat sdw = new SimpleDateFormat("E");
	static int year;// 年份、月份
	static int month;
	static int weekend; // 周数(第几周)
	static int leapYear[] = { 0, 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 }; // 闰年每月天数
	static int commonYear[] = { 0, 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30,
			31 }; // 平年每月天数
	static int start, end;

	
	public static Map<String, String>  getSaleDate(){
			Calendar calendar = Calendar.getInstance();
			calendar.add(Calendar.MONTH, -3);//得到前3个月
			Date  formNow3Month = calendar.getTime();
			Map<String, String> map=new HashMap<String, String>();

			String beginDate=format.format(formNow3Month);
			map.put("beginDate", beginDate);
			String endDate=format.format(new Date());
			map.put("endDate", endDate);

			return map;
			
	}
	
	
	
	
	
	public static void ListSort(List<ImportExcel> list) {
        Collections.sort(list, new Comparator<ImportExcel>() {
            @Override
            public int compare(ImportExcel o1, ImportExcel o2) {
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                try {
                    Date dt1 = format.parse(o1.getDataDate());
                    Date dt2 = format.parse(o2.getDataDate());
                    if (dt1.getTime() > dt2.getTime()) {
                        return 1;
                    } else if (dt1.getTime() < dt2.getTime()) {
                        return -1;
                    } else {
                        return 0;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return 0;
            }
        });
    }
	  
	// 判断闰年
	public static boolean isLeapYear(int year) {
		return (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0);
	}

	// 计算
	public static void count() {
		int day, weekday, allDays = 0;
		// 分别表示当月天数、当月一号是星期几、1900.1.1到当前输入日期之间的天数

		day = isLeapYear(year) ? leapYear[month] : commonYear[month];

		// 计算天数
		for (int i = 1900; i < year; i++) {
			allDays += isLeapYear(i) ? 366 : 365;
		}
		for (int i = 1; i < month; i++) {
			allDays += isLeapYear(year) ? leapYear[i] : commonYear[i];
		}
		// 计算星期几
		weekday = 1 + allDays % 7;

		// 计算第weekend周的开始和结束
		if (weekend == 1) {
			start = 1;
			end = 8 - weekday;
		} else {
			start = (weekend - 2) * 7 + (9 - weekday);
			end = start + 6;
			if (end > day)
				end = day;
		}
	}

	// 设置weekend周数
	public static void setWeekend(int wk) {
		weekend = wk;
	}

	// 以下三个是获取属性
	public static int getStart() {
		return start;
	}

	public static int getEnd() {
		return end;
	}

	public static int getWeekend() {
		return weekend;
	}

	public static List<HashMap<String, Object>> Order(
			LinkedList<HashMap<String, Object>> list, final String key) {

		Collections.sort(list, new Comparator<Map<String, Object>>() {
			@Override
			public int compare(Map<String, Object> map1,
					Map<String, Object> map2) {

				if (map1.get(key) == null) {
					map1.put(key, "0%");
				} else if (map2.get(key) == null) {
					map2.put(key, "0%");
				}

				if ((String) map1.get(key) != null
						&& (String) map2.get(key) != null) {
					if (map2.get(key).toString().contains("%")
							&& map1.get(key).toString().contains("%")) {
						String a = map2.get(key).toString();
						String b = map1.get(key).toString();
						// 去掉%
						String tempA = a.substring(0, a.lastIndexOf("%"));
						String tempB = b.substring(0, b.lastIndexOf("%"));
						// 精确表示
						BigDecimal dataA = new BigDecimal(tempA);
						BigDecimal dataB = new BigDecimal(tempB);
						return (dataA).compareTo(dataB);// 按sort字段升序
					} else {
						return ((String) map2.get(key)).compareTo((String) map1
								.get(key));
					}
				}
				return -1;
			}

		});

		return list = list;
	}

	
	
	public static JSONArray OrderName(
			JSONArray array, final String key) {
		Collections.sort(array, new Comparator<JSONObject>() {
			
			@Override
			public int compare(JSONObject map1, JSONObject map2) {
				return ((String) map2.get(key)).compareTo((String) map1
						.get(key));
			}

		});

		return array = array;
	}

	
	
	@SuppressWarnings("unchecked")
	public static JSONArray OrderAmt(
			JSONArray array, final String key) {
		Collections.sort(array, new Comparator<JSONObject>() {
			
			@Override
			public int compare(JSONObject map1, JSONObject map2) {
				return ( (BigDecimal) map2.get(key)).compareTo( (BigDecimal) map1
						.get(key));
			}

		});
		return array = array;
	}
	
	public static List<Date> getDatesBetweenTwoDate(Date beginDate, Date endDate) {
		List<Date> lDate = new ArrayList<Date>();
		lDate.add(beginDate);// 把开始时间加入集合
		Calendar cal = Calendar.getInstance();
		// 使用给定的 Date 设置此 Calendar 的时间
		cal.setTime(beginDate);
		boolean bContinue = true;
		while (bContinue) {
			// 根据日历的规则，为给定的日历字段添加或减去指定的时间量
			cal.add(Calendar.DAY_OF_MONTH, 1);
			// 测试此日期是否在指定日期之后
			if (endDate.after(cal.getTime())) {
				lDate.add(cal.getTime());
			} else {
				break;
			}
		}
		lDate.add(endDate);// 把结束时间加入集合
		return lDate;
	}
	

	private static String format(String str) {

		return str.split(" ")[0];
	}
	private static Calendar beNew(Calendar calendar){
		Calendar c=Calendar.getInstance();
		c.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE));
		return c;
	}
	
	private static Calendar getBegin(Calendar calendar){
		Calendar timeC=beNew(calendar);
		int month = getQuarterInMonth(timeC.get(Calendar.MONTH)+1, true);  
		timeC.set(Calendar.MONTH, month-1);
		timeC.set(Calendar.DATE, 1); 
		return timeC;
	} 
	
	private static Calendar getEnd(Calendar calendar){
		Calendar timeC=beNew(calendar);
		int month = getQuarterInMonth(timeC.get(Calendar.MONTH)+1, false);  
		timeC.set(Calendar.MONTH, month-1);  
		timeC.set(Calendar.DAY_OF_MONTH,  timeC.getActualMaximum(timeC.DAY_OF_MONTH));
		return timeC;
	}
	
	private static Calendar getYearBegin(Calendar calendar){
		Calendar timeC=beNew(calendar);
		timeC.set(timeC.get(Calendar.YEAR), 0, 1);
		return timeC;
	}
	
	private static Calendar getYearEnd(Calendar calendar){
		Calendar timeC=beNew(calendar);
		timeC.set(timeC.get(Calendar.YEAR), 11, 31);
		return timeC;
	}
	private static int getQuarterInMonth(int month, boolean isQuarterStart) {
		int months[] = { 1, 4, 7, 10 };
		if (!isQuarterStart) {
			months = new int[] { 3, 6, 9, 12 };
		}
		if (month >= 1 && month <= 3)
			return months[0];
		else if (month >= 4 && month <= 6)
			return months[1];
		else if (month >= 7 && month <= 9)
			return months[2];
		else
			return months[3];
	}


	

	public static LinkedList<HashMap<String,Object>> splitYear(String b,String e)throws Exception {
		Date beginDate = format.parse(b);
		Date endDate = format.parse(e);
		LinkedList<HashMap<String, Object>> list = new LinkedList<HashMap<String, Object>>();
		
		Calendar begin = Calendar.getInstance();
		begin.setTime(beginDate);
		Calendar end = Calendar.getInstance();
		end.setTime(endDate);
		while(!begin.after(end)){
			HashMap<String, Object> map = new HashMap<String, Object>();
			Calendar timeCalendar = Calendar.getInstance();
			Calendar timeCalendarTwo = Calendar.getInstance();
			timeCalendarTwo = beNew(begin);
			timeCalendar = getYearBegin(timeCalendarTwo);
			map.put("beginDate", format.format(timeCalendar.getTime()));
			timeCalendar = getYearEnd(timeCalendarTwo);
			map.put("endDate", format.format(timeCalendar.getTime()));
			begin.set(begin.get(Calendar.YEAR)+1,0, 1);
			list.add(map);
		}
		Date timeDate=format.parse(list.get(0).get("beginDate").toString());
		if(!timeDate.after(beginDate)){
			list.get(0).put("beginDate", format.format(beginDate));
		}
		timeDate=format.parse(list.get(list.size()-1).get("endDate").toString());
		if(!timeDate.before(endDate)){
			list.get(list.size()-1).put("endDate", format.format(endDate));
		}
		return list;
	}

	public static LinkedList<HashMap<String, Object>> splitSeason(String b,
			String e) throws Exception {

		Date beginDate = format.parse(b);
		Date endDate = format.parse(e);
		LinkedList<HashMap<String, Object>> list = new LinkedList<HashMap<String, Object>>();

		Calendar begin = Calendar.getInstance();
		begin.setTime(beginDate);
		Calendar end = Calendar.getInstance();
		end.setTime(endDate);
		while (begin.before(end)) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			Calendar timeCalendar = Calendar.getInstance();
			Calendar timeCalendarTwo = Calendar.getInstance();
			timeCalendarTwo = beNew(begin);
			timeCalendar = getBegin(timeCalendarTwo);
			map.put("beginDate", format.format(timeCalendar.getTime()));
			timeCalendar = getEnd(timeCalendarTwo);
			map.put("endDate", format.format(timeCalendar.getTime()));
			begin.set(Calendar.MONTH, begin.get(Calendar.MONTH)+3);
			begin.set(Calendar.DATE, 1);
			list.add(map);
		}
		Date timeDate=format.parse(list.get(0).get("beginDate").toString());
		if(!timeDate.after(beginDate)){
			list.get(0).put("beginDate", format.format(beginDate));
		}
		timeDate=format.parse(list.get(list.size()-1).get("endDate").toString());
		if(!timeDate.before(endDate)){
			list.get(list.size()-1).put("endDate", format.format(endDate));
		}
		return list;
	}
	public static LinkedList<HashMap<String, Object>> getWeek(String beginDate,
			String endDate) throws Exception {
		LinkedList<HashMap<String, Object>> dataList = new LinkedList<HashMap<String, Object>>();
		Date b = null;
		Date e = null;
		b = format.parse(beginDate);
		e = format.parse(endDate);
		Calendar rightNow = Calendar.getInstance();
		rightNow.setTime(b);
		Date time = b;
		String timeb = format.format(b);
		String timee = null;
		
		while (time.getTime() <= e.getTime()) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			
			time = format.parse(format.format(rightNow.getTime()));
			String timew = sdw.format(time);
			if ( ("Mon").equals(timew)  || ("星期一").equals(timew) || time.getTime() == b.getTime()) {
				timeb = (format.format(time));

			}
		
			if ((("Sun").equals(timew)||  ("星期日").equals(timew) || time.getTime() == e.getTime())  && time.getTime()<=e.getTime()) {
				timee = (format.format(time));
				map.put("beginDate", timeb);
				map.put("endDate", timee);
				dataList.add(map);
			}
			rightNow.add(Calendar.DAY_OF_YEAR, 1);
			
		}
		
		return dataList;
	}
	
	
	
	public static LinkedList<HashMap<String, Object>> getWeekByPH(String beginDate,
			String endDate) throws Exception {
		LinkedList<HashMap<String, Object>> dataList = new LinkedList<HashMap<String, Object>>();
		Date b = null;
		Date e = null;
		b = format.parse(beginDate);
		e = format.parse(endDate);
		Calendar rightNow = Calendar.getInstance();
		rightNow.setTime(b);
		Date time = b;
		String timeb = format.format(b);
		String timee = null;
		while (time.getTime() <= e.getTime()) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			
			time = format.parse(format.format(rightNow.getTime()));
			String timew = sdw.format(time);
			if ( ("Sat").equals(timew)  || ("星期六").equals(timew) || time.getTime() == b.getTime()) {
				timeb = (format.format(time));

			}
			
			if ((("Fri").equals(timew)||  ("星期五").equals(timew) || time.getTime() == e.getTime())  && time.getTime()<=e.getTime()) {
				timee = (format.format(time));
				map.put("beginDate", timeb);
				map.put("endDate", timee);
				dataList.add(map);
			}
			rightNow.add(Calendar.DAY_OF_YEAR, 1);

		}
		
		
		System.out.println(dataList);
		for (int i = 0; i < dataList.size(); i++) {
			HashMap<String, Object> map=dataList.get(i);
			String [] begin=map.get("beginDate").toString().split("-");
			String [] end=map.get("endDate").toString().split("-");
			if(i==0 && dataList.size()-i!=1){
				if((Integer.parseInt(end[2])-Integer.parseInt(begin[2])+1)<4){
					map=dataList.get(i+1);
					map.put("beginDate",dataList.get(i).get("beginDate"));
					dataList.remove(i);
					/*map.remove(dataList.get(i).get("beginDate"));
					map.remove(dataList.get(i).get("endDate"));
			*/
				}
			}else if(dataList.size()-i==1){
					if((Integer.parseInt(end[2])-Integer.parseInt(begin[2])+1)<4){
						map.put("beginDate",dataList.get(i-1).get("beginDate"));
						dataList.remove(i-1);
						/*map.remove(dataList.get(i-1).get("beginDate"));
						map.remove(dataList.get(i-1).get("endDate"));*/
						
				}
			
			}
		}
		return dataList;
	}
	
	
	
	
	public static List<KeyValueForDate> getKeyValueForDate(String startDate,
			String endDate) {
		List<KeyValueForDate> list = null;
		try {
			list = new ArrayList<KeyValueForDate>();

			String firstDay = "";
			String lastDay = "";
			Date d1 = new SimpleDateFormat("yyyy-MM-dd").parse(startDate);// 定义起始日期

			Date d2 = new SimpleDateFormat("yyyy-MM-dd").parse(endDate);// 定义结束日期

			Calendar dd = Calendar.getInstance();// 定义日期实例
			dd.setTime(d1);// 设置日期起始时间
			Calendar cale = Calendar.getInstance();

			Calendar c = Calendar.getInstance();
			c.setTime(d2);

			int startDay = d1.getDate();
			int endDay = d2.getDate();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

			KeyValueForDate keyValueForDate = null;

			while (dd.getTime().before(d2)) {// 判断是否到结束日期
				keyValueForDate = new KeyValueForDate();
				cale.setTime(dd.getTime());

				if (dd.getTime().equals(d1)) {
					cale.set(Calendar.DAY_OF_MONTH,
							dd.getActualMaximum(Calendar.DAY_OF_MONTH));
					lastDay = sdf.format(cale.getTime());
					keyValueForDate.setStartDate(sdf.format(d1));
					keyValueForDate.setEndDate(lastDay);

				} else if (dd.get(Calendar.MONTH) == d2.getMonth()
						&& dd.get(Calendar.YEAR) == c.get(Calendar.YEAR)) {
					cale.set(Calendar.DAY_OF_MONTH, 1);// 取第一天
					firstDay = sdf.format(cale.getTime());

					keyValueForDate.setStartDate(firstDay);
					keyValueForDate.setEndDate(sdf.format(d2));

				} else {
					cale.set(Calendar.DAY_OF_MONTH, 1);// 取第一天
					firstDay = sdf.format(cale.getTime());

					cale.set(Calendar.DAY_OF_MONTH,
							dd.getActualMaximum(Calendar.DAY_OF_MONTH));
					lastDay = sdf.format(cale.getTime());

					keyValueForDate.setStartDate(firstDay);
					keyValueForDate.setEndDate(lastDay);

				}
				list.add(keyValueForDate);
				dd.add(Calendar.MONTH, 1);// 进行当前日期月份加1

			}

			if (endDay < startDay) {
				keyValueForDate = new KeyValueForDate();

				cale.setTime(d2);
				cale.set(Calendar.DAY_OF_MONTH, 1);// 取第一天
				firstDay = sdf.format(cale.getTime());

				keyValueForDate.setStartDate(firstDay);
				keyValueForDate.setEndDate(sdf.format(d2));
				list.add(keyValueForDate);
			}
		} catch (Exception e) {
			return null;
		}

		return list;
	}
	public static  String getToday(){
		return format.format(new Date());
	}


	public static String getLastDayOfMonth(int year, int month) {
		Calendar cal = Calendar.getInstance();
		// 设置年份
		cal.set(Calendar.YEAR, year);
		// 设置月份
		cal.set(Calendar.MONTH, month - 1);
		// 获取某月最大天数
		int lastDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
		// 设置日历中月份的最大天数
		cal.set(Calendar.DAY_OF_MONTH, lastDay);
		// 格式化日期
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String lastDayOfMonth = sdf.format(cal.getTime());

		return lastDayOfMonth;
	}
	 /**  
     * 计算两个日期之间相差的天数  
     * @param smdate 较小的时间 
     * @param bdate  较大的时间 
     * @return 相差天数 
     * @throws ParseException  
     */    
    public static int daysBetween(Date smdate,Date bdate) throws ParseException    
    {    
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");  
        smdate=sdf.parse(sdf.format(smdate));  
        bdate=sdf.parse(sdf.format(bdate));  
        Calendar cal = Calendar.getInstance();    
        cal.setTime(smdate);    
        long time1 = cal.getTimeInMillis();                 
        cal.setTime(bdate);    
        long time2 = cal.getTimeInMillis();         
        long between_days=(time2-time1)/(1000*3600*24)+1;  
            
       return Integer.parseInt(String.valueOf(between_days));           
    }    
      
/** 
*字符串的日期格式的计算 
*/  
    public static int daysBetween(String smdate,String bdate) throws ParseException{  
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");  
        Calendar cal = Calendar.getInstance();    
        cal.setTime(sdf.parse(smdate));    
        long time1 = cal.getTimeInMillis();                 
        cal.setTime(sdf.parse(bdate));    
        long time2 = cal.getTimeInMillis();         
        long between_days=(time2-time1)/(1000*3600*24);  
            
       return Integer.parseInt(String.valueOf(between_days));     
    }
    
    
    /** 
     * 通过循环删除 
     *  
     * @param list 
     */  
    public static void remove(List<ImportExcel> list) {  
        for (int i = 0; i <list.size()-1 ; i++) {  
            for (int j = list.size() - 1; j > i; j--) {  
                try {
					if (list.get(j).getShopId()==list.get(i).getShopId()
							&&  list.get(j).getModel().equals(list.get(i).getModel())
							&& formatMonth.format( formatMonth.parse(list.get(j).getDataDate())).equals(formatMonth.format(formatMonth.parse( list.get(i).getDataDate())))
							) {  
					    list.remove(j);  
					}
				} catch (ParseException e) {
					e.printStackTrace();
				}  
            }  
        }  
    }  
    
    
    public static void removeCus(List<ImportExcel> list) {  
        for (int i = 0; i <list.size()-1 ; i++) {  
            for (int j = list.size() - 1; j > i; j--) {  
                try {
					if (list.get(j).getCustomerId()==list.get(i).getCustomerId()
							&&  list.get(j).getModel().equals(list.get(i).getModel())
							&& formatMonth.format( formatMonth.parse(list.get(j).getDataDate())).equals(formatMonth.format(formatMonth.parse( list.get(i).getDataDate())))
							) {  
					    list.remove(j);  
					}
				} catch (ParseException e) {
					e.printStackTrace();
				}  
            }  
        }  
    }  
    
  
    
    /** 
     * 通过HashSet删除 
     *  
     * @param list 
     */  
    public static void removeHashSet(List<ImportExcel> list) {  
        HashSet<ImportExcel> h = new HashSet<ImportExcel>(list);  
        list.clear();  
        list.addAll(h);  
    }  
    
    public static String getExcelColumnLabel(int index) 
    {
     String rs = "";
     do {
      index--;
      rs = ((char) (index % 26 + (int) 'A')) + rs;
      index = (int) ((index - index % 26) / 26);
     } while (index > 0);
     return rs;
    }
  public static void main(String[] args) {
	  try {
		  LinkedList<HashMap<String, Object>> 	dateList=	DateUtil.getWeek("2017-01-01", "2017-12-31");
		  for (int i = 0; i < dateList.size(); i++) {
				System.out.println("===beginDate=="+dateList.get(i).get("beginDate").toString());
				System.out.println("===endDate=="+dateList.get(i).get("endDate").toString());
		  }
	} catch (Exception e) {
		e.printStackTrace();
	}
}
  
}








class KeyValueForDate {
	private String startDate;
	private String endDate;

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

}
