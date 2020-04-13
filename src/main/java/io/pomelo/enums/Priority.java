package io.pomelo.enums;

public enum Priority {

	/** 1 */ High(1),
	/** 2 */ Medium(2),
	/** 3 */ Low(3),
	/** 4 */ NiceToHave(4);

	private int code;

	private Priority(int code) {
		this.code = code;
	}

	public int getCode() {
		return code;
	}

	public static Priority valueOf(int value) {
		switch (value) {
		case 1:
			return High;
		case 2:
			return Medium;
		case 3:
			return Low;
		case 4:
			return NiceToHave;
		default:
			return NiceToHave;
		}
	}
}
