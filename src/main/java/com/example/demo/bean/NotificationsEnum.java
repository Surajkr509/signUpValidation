package com.example.demo.bean;

public enum NotificationsEnum {
	STUDENT_SIGNUP("STUDENT_SIGNUP"), TEACHER_SIGNUP("TEACHER_SIGNUP");

	private String type;

	private NotificationsEnum() {
	}

	private NotificationsEnum(String type) {
		this.type = type;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	
	
}
