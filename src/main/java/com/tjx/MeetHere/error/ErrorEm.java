package com.tjx.MeetHere.error;

public enum ErrorEm implements CommonError{
    PARAMETER_VALIDATION_ERROR(10001,"参数不合法"),
    UNKNOWN_ERROR(20001,"未知错误"),
    USER_NOT_EXIST(30001,"用户不存在"),
    USER_LOGIN_FAIL(30002,"用户手机号或者密码不正确"),
    USER_NOT_LOGIN(30003,"用户还未登录"),
    EMAIL_HAS_REGISTED(40001,"邮箱已经注册，请直接登录"),
    VENUE_NOT_EXIST(50001,"场地不存在，不可预约"),
    VENUE_TIME_IS_OCCUPIED(50002,"预约时间冲突，请更换其他时段")
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
