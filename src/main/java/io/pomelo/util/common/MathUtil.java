package io.pomelo.util.common;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;

/**
 * <p>算术工具类<br>Math util</p>
 * @ClassName MathUtil.java
 * @author PomeloMan
 */
public class MathUtil {

	private final static DecimalFormat formatter = new DecimalFormat();
	/**
	 * <p>获取formatter对象<br>Get formatter object</p>
	 * @return DecimalFormat
	 */
	public static DecimalFormat getFormatter() {
		return formatter;
	}

	/**
	 * <p>根据格式化格式格式化数据<br>Format the data according to the formatting format</p>
	 * <pre>
	 * ("0.00", 99.9974) -> "100.00"
	 * ("0.00", 99.9945) -> "99.99"
	 * ("0.00%", 0.99456) -> "99.46%"
	 * ("0.00%", 0.999956) -> "100.00%"
	 * </pre>
	 * @return
	 */
	public static String format(String pattern, Object value) {
		formatter.applyPattern(pattern);
		return formatter.format(value);
	}

	/**
	 * <p>保留2位小数<br>Keep 2 decimal places</p>
	 * <pre>
	 * 99.9974 -> "100.00"
	 * 99.9945 -> "99.99"
	 * </pre>
	 * @return
	 */
	public static String keepTwoDecimalPlaces(Object value) {
		formatter.applyPattern("0.00");
		return formatter.format(value);
	}

	/**
	 * <p>百分比<br>percentage</p>
	 * <pre>
	 * 0.99456 -> "99.46%"
	 * 0.999956 -> "100.00%"
	 * </pre>
	 * @return
	 */
	public static String toPercent(Object value) {
		formatter.applyPattern("0.00%");
		return formatter.format(value);
	}

	/**
	 * <p>精确加法运算<br>Accurate addition</p>
	 * @return
	 */
	public static double plus(Object obj1, Object obj2) {
		BigDecimal v1 = new BigDecimal(obj1.toString());
		BigDecimal v2 = new BigDecimal(obj2.toString());
		return v1.add(v2).doubleValue();
	}

	/**
	 * <p>精确减法运算<br>Accurate subtraction</p>
	 * <pre>
	 * (1,2) -> -1.0
	 * (2,1) -> 1.0
	 * </pre>
	 * @return
	 */
	public static double subtract(Object obj1, Object obj2) {
		BigDecimal v1 = new BigDecimal(obj1.toString());
		BigDecimal v2 = new BigDecimal(obj2.toString());
		return v1.subtract(v2).doubleValue();
	}

	/**
	 * <p>精确乘法运算<br>Exact multiplication</p>
	 * @return
	 */
	public static double multiply(Object obj1, Object obj2) {
		BigDecimal v1 = new BigDecimal(obj1.toString());
		BigDecimal v2 = new BigDecimal(obj2.toString());
		return v1.multiply(v2).doubleValue();
	}

	/**
	 * <p>除法运算(默认保留2位小数)<br>Division (default 2 decimal places)</p>
	 * <pre>
	 * (5,3) -> 1.67
	 * (3,5) -> 0.6
	 * </pre>
	 * @return
	 */
	public static double divide(Object obj1, Object obj2) {
		return divide(obj1, obj2, 2);
	}

	/**
	 * <p>除法运算(默认保留2位小数)<br>Division (default 2 decimal places)</p>
	 * <pre>
	 * (5,3,2) -> 1.67
	 * (3,5,2) -> 0.6
	 * </pre>
	 * @return
	 */
	public static double divide(Object obj1, Object obj2, int scale) {
		BigDecimal v1 = new BigDecimal(obj1.toString());
		BigDecimal v2 = new BigDecimal(obj2.toString());
		return v1.divide(v2, scale, RoundingMode.HALF_UP).doubleValue();
	}

	/**
	 * <p>小数四舍五入方法<br>Decimal rounding method</p>
	 * <pre>
	 * (3.1415926,4) -> 3.1416
	 * (3.1415926,2) -> 3.14
	 * </pre>
	 * @return
	 */
	public static double round(Object obj, int scale) {
		BigDecimal value = new BigDecimal(obj.toString());
		BigDecimal one = new BigDecimal("1");
		return value.divide(one, scale, RoundingMode.HALF_UP).doubleValue();
	}

}
