package com.jedigames.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;

import com.jedigames.logger.JLogger;

public class TimeManager {
	private static long timeDiff = 0L;

	/**
	 * 这个时间是不变的, 以免在后面被改变
	 */
	private static Calendar fixedCalendar = newCorrectedCalendar();

	/**
	 * 这个时间是不变的, 不要在后面改变(不要调用setTime和setTimeInMillis方法)
	 * 
	 * @return
	 */
	private static Calendar getFixedCalendar() {
		// 更新时间为当前时间
		fixedCalendar.setTimeInMillis(getSystemTimestampLong());
		return fixedCalendar;
	}

	/**
	 * 不通过和core_data库进行校验，使用本地时间
	 * @return
	 */
	private static Calendar getFixedCalendarUseServer() {
		// 更新时间为当前时间
		fixedCalendar.setTimeInMillis(System.currentTimeMillis());
		return fixedCalendar;
	}
	/**
	 * 生成一个新的calendar, 并更新校验时间
	 * 
	 * @return
	 */
	private static Calendar newCorrectedCalendar() {
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(getSystemTimestampLong());
		return cal;
	}


	private static class VariableCalendar extends ThreadLocal<Calendar> {
		@Override
		protected Calendar initialValue() {
			return newCorrectedCalendar();
		}
	}
	
	/**
	 * 可变的, 使用ThreadLocal进行线程安全管理
	 */
	private static VariableCalendar variableCalendar = new VariableCalendar();

	private static Calendar getVariableCalendar() {
		return variableCalendar.get();
	}

	/**
	 * 时间校准
	 * 
	 * @param nowTime
	 */
	public static void correctTime(long nowTime) {
		synchronized (TimeManager.class) {
//			long systemTime = System.currentTimeMillis();
//			timeDiff = nowTime - systemTime;
//			fixedCalendar = newCorrectedCalendar();
//			variableCalendar = new VariableCalendar();
		}
	}

	/**
	 * DateFormat
	 */
	private static ThreadLocal<ConcurrentHashMap<String, DateFormat>> dateFormatMap = new ThreadLocal<>();

	private static DateFormat getFormat(String dateFormat) {
		ConcurrentHashMap<String, DateFormat> localMap = dateFormatMap.get();
		if (localMap == null) {
			localMap = new ConcurrentHashMap<>();
			dateFormatMap.set(localMap);
		}
		DateFormat format = localMap.get(dateFormat);
		if (format == null) {
			format = new SimpleDateFormat(dateFormat);
			localMap.putIfAbsent(dateFormat, format);
		}
		return format;
	}

	/**
	 * 获取和UTC时间的时间差(毫秒)
	 * 
	 * @return
	 */
	public static int getRawTimeZoneOffset() {
		return getFixedCalendar().getTimeZone().getRawOffset();
	}

	/**
	 * 获取java时间戳(秒)
	 * 
	 * @return
	 */
	public static int getSystemTimestamp() {
		return (int) (getSystemTimestampLong() / 1000);
    }

	/**
	 * 获取java时间戳(毫秒)
	 * 
	 * @return
	 */
	public static long getSystemTimestampLong() {
		return System.currentTimeMillis() + timeDiff;
    }

	// -------- formatString<=>timestamp(long) --------//
	/**
	 * 将当前时间转为特定格式
	 * 
	 * @param dateFormat
	 * @return
	 */
	public static String currentTime2DateString(String dateFormat) {
		return timestampLong2DateString(getSystemTimestampLong(), dateFormat);
	}

	/**
	 * 根据给定的时间格式格式化时间戳(秒)
	 * 
	 * @param timestamp
	 * @param dateFormat
	 * @return
	 */
    public static String timestamp2DateString(int timestamp, String dateFormat) {
		return timestampLong2DateString(timestamp * 1000L, dateFormat);
	}

	/**
	 * 根据给定的时间格式格式化时间戳(毫秒)
	 * 
	 * @param timestampLong
	 * @param dateFormat
	 * @return
	 */
	public static String timestampLong2DateString(long timestampLong, String dateFormat) {
		Date date = new Date(timestampLong);
		DateFormat format = getFormat(dateFormat);
		return format.format(date);
	}

	/**
	 * 格式化的时间转化为时间戳(秒), 失败返回0
	 * 
	 * @param dateString
	 * @param dateFormat
	 * @return
	 */
    public static int dateString2Timestamp(String dateString, String dateFormat) {
		return (int) (dateString2TimestampLong(dateString, dateFormat) / 1000);
    }

	/**
	 * 格式化的时间转化为时间戳(毫秒), 失败返回0
	 * 
	 * @param dateString
	 * @param dateFormat
	 * @return
	 */
    public static long dateString2TimestampLong(String dateString, String dateFormat) {
		DateFormat sdf = getFormat(dateFormat);
        long timestamp = 0;
		try {
			timestamp = sdf.parse(dateString).getTime();
		} catch (ParseException e) {
			JLogger.error("dateString2TimestampLong error", e);
		}
    	return timestamp;
    }

	// -------- timestamp(long)便捷操作 --------//
	/**
	 * 将时间戳转为当日0点的时间戳(秒), 获取失败返回0
	 * 
	 * @param timestamp
	 * @return
	 */
    public static int toZeroTimestamp(int timestamp) {
		return (int) (toZeroTimestampLong(timestamp * 1000L) / 1000);
    }

	/**
	 * 将时间戳转为当日0点的时间戳(毫秒), 获取失败返回0
	 * 
	 * @param timestampLong
	 * @return
	 */
	public static long toZeroTimestampLong(long timestampLong) {
		DateFormat sdf = getFormat("yyyy-MM-dd");
		String dateString = sdf.format(new Date(timestampLong));
		try {
			timestampLong = sdf.parse(dateString).getTime();
		} catch (ParseException e) {
			JLogger.error("toZeroTimestampLong error", e);
			return 0L;
		}
		return timestampLong;
	}

	/**
	 * 获取0点的时间戳(秒), 获取失败返回0
	 * 
	 * @return
	 */
	public static int getZeroTimestamp() {
		long timestamp = getSystemTimestampLong();
		return (int) (toZeroTimestampLong(timestamp) / 1000);
	}

	/**
	 * 获取0点的时间戳(毫秒), 获取失败返回0
	 * 
	 * @return
	 */
    public static long getZeroTimestampLong() {
		long timestamp = getSystemTimestampLong();
		return toZeroTimestampLong(timestamp);
    }

	// -------- calendar相关操作 --------//
	private static int getWeekOfYear(Calendar calendar) {
		if (calendar.get(Calendar.DAY_OF_WEEK) == 1) {
			if (calendar.get(Calendar.WEEK_OF_YEAR) - 1 == 0) {
				// 2012最后一个星期，防错
				return 52;
			}
			return calendar.get(Calendar.WEEK_OF_YEAR) - 1;
		} else {
			return calendar.get(Calendar.WEEK_OF_YEAR);
		}
	}

	/**
	 * 获取当前是一年中的第几个星期
	 * 
	 * @return
	 */
	public static int getWeekOfYear() {
		return getWeekOfYear(getFixedCalendar());
	}

	/**
	 * 获取当前是当前月中的第几个星期
	 * 
	 * @return
	 */
	public static int getWeekOfMonth() {
		return getFixedCalendar().get(Calendar.WEEK_OF_MONTH);
	}

	/**
	 * 获取当前是一个月的第几天(第一天为1)
	 * 
	 * @return
	 */
	public static int getDayOfMonth() {
		return getFixedCalendar().get(Calendar.DAY_OF_MONTH);
	}

	/**
	 * 获取今天是一周的第几天, 星期日是第1天, 星期一是第2天,... 星期六是第7天
	 * 
	 * @return
	 */
	public static int getDayOfWeekSundayFirst() {
		int dayOfWeek = getFixedCalendar().get(Calendar.DAY_OF_WEEK);
		return dayOfWeek;
	}


	/**
	 * 获取今天是一周的第几天, 星期日是第1天, 星期一是第2天,... 星期六是第7天
	 * 使用本地时间，不与core_data校验
	 * @return
	 */
	public static int getDayOfWeekSundayFirstUseServer() {
		int dayOfWeek = getFixedCalendarUseServer().get(Calendar.DAY_OF_WEEK);
		return dayOfWeek;
	}


	/**
	 * 获取今天是一周的第几天, 星期日是第0天, 星期一是第1天,... 星期六是第6天
	 * 
	 * @return
	 */
	public static int getDayOfWeekSundayZero() {
		return getDayOfWeekSundayFirst() - 1;
	}

	/**
	 * 获取今天是一周的第几天, 星期日是第0天, 星期一是第1天,... 星期六是第6天
	 * 使用本地服务器时间，不与core_data库进行校验
	 * @return
	 */
	public static int getDayOfWeekSundayZeroUseServer() {
		return getDayOfWeekSundayFirstUseServer() - 1;
	}

	/**
	 * 获取今天是一周的第几天, 星期一是第1天, 星期二是第2天,... 星期日是第7天
	 * 
	 * @return
	 */
	public static int getDayOfWeekMondayFirst() {
		int dayOfWeek = getFixedCalendar().get(Calendar.DAY_OF_WEEK);
		dayOfWeek = dayOfWeek - 1;
		if (dayOfWeek <= 0) {
			dayOfWeek = dayOfWeek + 7;
		}
		return dayOfWeek;
	}

	/**
	 * 获取今天是一周的第几天, 星期一是第0天, 星期二是第1天,... 星期日是第6天
	 * 
	 * @return
	 */
	public static int getDayOfWeekMondayZero() {
		return getDayOfWeekMondayFirst() - 1;
	}

	/**
	 * 获取给定时间戳(毫秒)是一周的第几天, 星期一是第1天, 星期二是第2天,... 星期日是第7天
	 * 
	 * @param timestampLong
	 * @return
	 */
	public static int getTimestampLongDayOfWeekMonDayFirst(long timestampLong) {
		Calendar cal = getVariableCalendar();
		cal.setTimeInMillis(timestampLong);
		int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
		dayOfWeek = dayOfWeek - 1;
		if (dayOfWeek <= 0) {
			dayOfWeek = dayOfWeek + 7;
		}
		return dayOfWeek;
	}

	/**
	 * 获取给定时间戳(毫秒)是一周的第几天, 星期一是第0天, 星期二是第1天,... 星期日是第6天
	 * 
	 * @param timestamp
	 * @return
	 */
	public static int getTimestampLongDayOfWeekMonDayZero(long timestampLong) {
		return getTimestampLongDayOfWeekMonDayFirst(timestampLong) - 1;
	}

	/**
	 * 获取给定时间戳(秒)是一周的第几天, 星期一是第1天, 星期二是第2天,... 星期日是第7天
	 * 
	 * @param timestamp
	 * @return
	 */
	public static int getTimestampDayOfWeekMonDayFirst(int timestamp) {
		return getTimestampLongDayOfWeekMonDayFirst(timestamp * 1000L);
	}

	/**
	 * 获取给定时间戳(秒)是一周的第几天, 星期一是第0天, 星期二是第1天,... 星期日是第6天
	 * 
	 * @param timestamp
	 * @return
	 */
	public static int getTimestampDayOfWeekMonDayZero(int timestamp) {
		return getTimestampDayOfWeekMonDayFirst(timestamp) - 1;
	}

	/**
	 * 获取现在是一天中的第几小时
	 * 
	 * @return
	 */
	public static int getHourOfDay() {
		return getFixedCalendar().get(Calendar.HOUR_OF_DAY);
    }

	/**
	 * 获取现在是一天中的第几秒
	 * 
	 * @return
	 */
	public static int getSecondOfDay() {
		Calendar cal = getFixedCalendar();
		return cal.get(Calendar.SECOND) + cal.get(Calendar.MINUTE) * 60 + cal.get(Calendar.HOUR_OF_DAY) * 3600;
	}
	
	//获取下周或者上周某天的周五凌晨时间戳
	public static int getZeroTimeDay5(int ts, int z) {
		Calendar cal = Calendar.getInstance();
		long t  = (long)ts * 1000;
		cal.setTimeInMillis(t);
		//z为推迟的周数，1本周，-1向前推迟一周，2下周，依次类推
		//想周几，这里就传几Calendar.MONDAY（TUESDAY...）
		cal.setFirstDayOfWeek(Calendar.MONDAY);
		cal.set(Calendar.DAY_OF_WEEK,Calendar.FRIDAY);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		cal.add(Calendar.DATE,z * 7);
		return (int) (cal.getTimeInMillis() / 1000) ;
	}

	
	public static int getZeroTimeDay1(int ts, int z) {
		Calendar cal = Calendar.getInstance();
		long t  = (long)ts * 1000;
		cal.setTimeInMillis(t);
		//z为推迟的周数，1本周，-1向前推迟一周，2下周，依次类推
		//想周几，这里就传几Calendar.MONDAY（TUESDAY...）
		cal.setFirstDayOfWeek(Calendar.MONDAY);
		cal.set(Calendar.DAY_OF_WEEK,Calendar.MONDAY);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		cal.add(Calendar.DATE,z * 7);
		return (int) (cal.getTimeInMillis() / 1000) ;
	}
	
	/**
	 * 获取某天周天零点的时间戳
	 * @param ts
	 * @param z
	 * @return
	 */
	public static int getZeroTimeDay7(int ts, int add) {
		Calendar cal = Calendar.getInstance();
		long t  = (long)ts * 1000;
		cal.setTimeInMillis(t);
		cal.setFirstDayOfWeek(Calendar.MONDAY);
		cal.set(Calendar.DAY_OF_WEEK,Calendar.SUNDAY);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		cal.add(Calendar.DATE,add * 7);
		return (int) (cal.getTimeInMillis() / 1000) ;
	}
	
	/**
	 * 获取某月零点的时间戳
	 * @param ts
	 * @param z
	 * @return
	 */
	public static int getZeroMonthTime(int ts, int add) {
		Calendar cal = Calendar.getInstance();
		long t  = (long)ts * 1000;
		cal.setTimeInMillis(t);
		cal.add(Calendar.MONTH, add);
		cal.set(Calendar.DAY_OF_MONTH,cal.getActualMaximum(Calendar.DAY_OF_MONTH));
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return (int) (cal.getTimeInMillis() / 1000) ;
	}
	
	
	
	
	//获取某天凌晨时间戳
	public static int getZeroTimeByTs(int ts) {
		Calendar cal = Calendar.getInstance();
		long t  = (long)ts * 1000;
		cal.setTimeInMillis(t);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return (int) (cal.getTimeInMillis() / 1000) ;
	}
	
	
	
	

	/**
	 * 获取现在是当前小时中的第几分钟
	 * 
	 * @return
	 */
	public static int getMinuteOfHour() {
		return getFixedCalendar().get(Calendar.MINUTE);
	}

	/**
	 * 获取现在是当前小时中的第几秒
	 * 
	 * @return
	 */
	public static int getSecondOfHour() {
		Calendar cal = getFixedCalendar();
		return cal.get(Calendar.SECOND) + cal.get(Calendar.MINUTE) * 60;
	}

	/**
	 * 获取年(2位)-月-日
	 * 
	 * @return
	 */
	public static String getYMDDateSimple() {
		return currentTime2DateString("yy-MM-dd");
	}

	/**
	 * 获取年-月-日
	 * 
	 * @return
	 */
    public static String getYMDDate() {
		return currentTime2DateString("yyyy-MM-dd");
    }

	/**
	 * 获取年-月-日 小时:分钟:秒
	 * 
	 * @return
	 */
	public static String getYMDHMSDate() {
		return currentTime2DateString("yyyy-MM-dd HH:mm:ss");
	}
	
	/**
	 * 转换mysql中的%Y-%m-%d %T 格式的时间
	 * */
	public static String FROM_UNIXTIME(int ts) {
		return TimeManager.timestamp2DateString(ts, "yyyy-MM-dd HH:mm:ss");
	}
}
