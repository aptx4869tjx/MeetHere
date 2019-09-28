package com.tjx.MeetHere.error;

public enum ErrorEm implements CommonError{
    PARAMETER_VALIDATION_ERROR(10001,"参数不合法"),
    UNKNOWN_ERROR(20001,"未知错误"),
    USER_NOT_EXIST(30001,"用户不存在"),
    USER_LOGIN_FAIL(30002,"用户手机号或者密码不正确"),
    USER_NOT_LOGIN(30003,"用户还未登录")
    ;

    private int errorCode;
    private String errorMessage;

    ErrorEm(int errorCode, String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    @Override
    public int getErrorCode() {
        return this.errorCode;
    }

    @Override
    public String getErrorMessage() {
        return this.errorMessage;
    }

    @Override
    public CommonError setErrorMessage(String message) {
        this.errorMessage = message;
        return this;
    }
}
