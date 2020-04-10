package io.pomelo.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import org.apache.commons.lang3.time.DateUtils;
import org.junit.Test;

import io.pomelo.util.common.DateUtil;

public class DateUtilTest {

	@Test
	public void formatTimeZoneTest() {
		Date now = null;
		DateUtil.setDefault(TimeZone.getTimeZone("GMT+8"), Locale.ENGLISH);
		now = Calendar.getInstance().getTime();
		System.out.println(DateFormat.getDateInstance(DateFormat.MEDIUM).format(now));
		DateUtil.setDefault(TimeZone.getTimeZone("UTC"), Locale.ENGLISH);
		now = Calendar.getInstance().getTime();
		System.out.println(DateFormat.getDateInstance(DateFormat.MEDIUM).format(now));
	}

	@Test
	public void formatLocaleTest() {
		Date now = Calendar.getInstance().getTime();
		DateUtil.setDefault(null, Locale.ENGLISH);
		System.out.println(DateFormat.getDateInstance(DateFormat.MEDIUM).format(now));
		DateUtil.setDefault(null, Locale.CHINESE);
		System.out.println(DateFormat.getDateInstance(DateFormat.MEDIUM).format(now));
	}

	@Test
	public void dateTest() throws ParseException {
		Date date = null;
		String dateStr = "2018-01-01";
		DateUtil.setDefault(TimeZone.getTimeZone("GMT+8"), Locale.ENGLISH);
		date = DateUtils.parseDate(dateStr, "yyyy-MM-dd");
		System.out.println(date.getTime());
		DateUtil.setDefault(TimeZone.getTimeZone("UTC"), Locale.ENGLISH);
		date = DateUtils.parseDate(dateStr, "yyyy-MM-dd");
		System.out.println(date.getTime());
	}
}
