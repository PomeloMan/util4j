package io.pomelo.util.common;

import java.util.StringTokenizer;

/**
 * <p>字串处理工具类<br>String util</p>
 * @ClassName StringUtil.java
 * @author PomeloMan
 */
public class StringUtil {

	/**
	 * <p>
	 * 分割字符串。<br>
	 * split("hello|||world","\\|\\|\\|") == stringTokenizer("hello|||world","|||");
	 * <br>
	 * Split the string.
	 * </p>
	 * <pre>
	 * ("hello|||world","|||") -> ["hello","world"]
	 * </pre>
	 * @return
	 */
	public static String[] stringTokenizer(String str, String delim) {
		StringTokenizer st = new StringTokenizer(str, delim);
		String[] strArr = new String[st.countTokens()];
		int i = 0;
		while (st.hasMoreTokens()) {
			strArr[i++] = st.nextToken();
		}
		return strArr;
	}
}
