package io.pomelo.util.common;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * <p>
 * 日期工具类<br>
 * Date util
 * </p>
 * 
 * @ClassName DateUtil.java
 * @author PomeloMan
 */
public class DateUtil {

	/**
	 * yyyy-MM-dd
	 */
	public final static String YYYY_MM_DD = "yyyy-MM-dd";
	/**
	 * MMM-yyyy
	 */
	public final static String MMM_YYYY = "MMM-yyyy";

	/**
	 * <p>
	 * 默认时区（GMT+8）<br>
	 * Default timezone(GMT+8)
	 * </p>
	 */
	public final static TimeZone DEFAULT_TIMEZONE = TimeZone.getTimeZone("GMT+8");
	/**
	 * <p>
	 * 默认地域（en）<br>
	 * Default locale(en)
	 * </p>
	 */
	public final static Locale DEFAULT_LOCALE = Locale.ENGLISH;

	/**
	 * <p>
	 * 设置默认时区/地域<br>
	 * Set default timezone/locale
	 * </p>
	 */
	public static void setDefault() {
		setDefault(DEFAULT_TIMEZONE, DEFAULT_LOCALE);
	}

	/**
	 * <p>
	 * 设置时区/地域<br>
	 * Set timezone/locale
	 * </p>
	 * 
	 * @param timezone
	 * @param locale
	 */
	public static void setDefault(TimeZone timezone, Locale locale) {
		TimeZone.setDefault(timezone);
		Locale.setDefault(locale);
	}

	/**
	 * <p>
	 * 时间转换枚举类<br>
	 * Time conversion enumeration class
	 * </p>
	 * 
	 * @ClassName TimeMillis.java
	 * @author PomeloMan
	 */
	public enum TimeMillis {
		Day(1000 * 60 * 60 * 24), Hour(1000 * 60 * 60), Minute(1000 * 60), Second(1000);

		private double milliseconds;

		private TimeMillis(double milliseconds) {
			this.milliseconds = milliseconds;
		}

		public double getMilliseconds() {
			return milliseconds;
		}
	}

	/**
	 * yyyy-MM-dd HH:mm:ss
	 */
	public final static String DEFAULT_PATTERN = "yyyy-MM-dd HH:mm:ss";
	/**
	 * yyyy-MM-dd'T'HH:mm:ss.SSSZ
	 */
	public final static String DEFAULT_PATTERN_TZ = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";

	/**
	 * <p>
	 * 获取当前时间（使用getInstance()方法中的时区/地域设置） <br>
	 * Get the current time (using the time zone / locale in the getInstance ()
	 * method)
	 * </p>
	 * 
	 * @return
	 */
	public static Calendar now() {
		return Calendar.getInstance();
	}

	/**
	 * <p>
	 * 获取两日期之差（to - from） <br>
	 * Get the difference between two dates (to - from)
	 * </p>
	 * 
	 * <pre>
	 * ('2017-12-12 00:00:00', '2017-12-14 00:00:00', TimeMillis.Day) -> 2
	 * ('2017-12-12 00:00:00', '2017-12-14 00:00:00', TimeMillis.Second) -> 172800
	 * </pre>
	 * 
	 * @param from
	 * @param to
	 * @param field
	 * @return
	 */
	public static double differ(Calendar from, Calendar to, TimeMillis field) {
		return MathUtil.divide(MathUtil.subtract(to.getTimeInMillis(), from.getTimeInMillis()),
				field.getMilliseconds());
	}

	/**
	 * <p>
	 * 获取两日期之差（to - from） <br>
	 * Get the difference between two dates (to - from)
	 * </p>
	 * 
	 * <pre>
	 * ('2017-12-12 00:00:00', '2017-12-14 00:00:00', TimeMillis.Day) -> 2
	 * ('2017-12-12 00:00:00', '2017-12-14 00:00:00', TimeMillis.Second) -> 172800
	 * </pre>
	 * 
	 * @param from
	 * @param to
	 * @param field
	 * @return
	 */
	public static double differ(Date from, Date to, TimeMillis field) {
		Calendar _from = Calendar.getInstance();
		_from.setTime(from);
		Calendar _to = Calendar.getInstance();
		_to.setTime(to);
		return differ(_from, _to, field);
	}
}
