package com.tjx.MeetHere.error;

public enum ErrorEm implements CommonError{
    PARAMETER_VALIDATION_ERROR(10001,"参数不合法"),
    UNKNOWN_ERROR(20001,"未知错误"),
    USER_NOT_EXIST(30001,"用户不存在"),
    USER_LOGIN_FAIL(30002,"用户邮箱或者密码不正确"),
    USER_NOT_LOGIN(30003,"用户还未登录"),
    EMAIL_HAS_REGISTED(30004,"邮箱已经注册，请直接登录"),
    VENUE_NOT_EXIST(50001,"场地不存在，不可预约"),
    VENUE_TIME_IS_OCCUPIED(50002,"预约时间冲突或者已经过期，请更换其他时段"),
    VENUE_UPDATE_FAIL(50003,"场地信息更新失败"),
    PICTURE_UPLOAD_FAIL(60001,"图片上传失败"),
    ORDER_CANCEL_FAIL(70001,"订单取消失败，预约的日期已经过期")
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
