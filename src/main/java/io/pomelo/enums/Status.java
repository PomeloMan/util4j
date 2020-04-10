package io.pomelo.enums;

public enum Status {

	/** 0 */
	Init(0, "初始化"),
	/** 1 */
	Valid(1, "有效"),
	/** 2 */
	Invalid(2, "无效"),
	/** 3 */
	Deleted(3, "删除"),
	/** -1 */
	Expired(-1, "过期");

	private int code;
	private String description;

	private Status(int code, String description) {
		this.code = code;
		this.description = description;
	}

	public int getCode() {
		return code;
	}

	public String getDescription() {
		return description;
	}

	public static Status valueOf(int value) {
		switch (value) {
		case 0:
			return Init;
		case 1:
			return Valid;
		case 2:
			return Invalid;
		case -1:
			return Expired;
		default:
			return Init;
		}
	}
}
