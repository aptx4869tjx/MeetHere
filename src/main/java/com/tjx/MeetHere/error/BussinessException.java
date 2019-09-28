package com.tjx.MeetHere.error;

public class BussinessException extends Exception implements CommonError {

    private CommonError commonError;

    public BussinessException(CommonError commonError) {
        this.commonError = commonError;
    }
    public BussinessException(CommonError commonError,String message) {
        this.commonError = commonError;
        this.commonError.setErrorMessage(message);
    }

    @Override
    public int getErrorCode() {
        return this.commonError.getErrorCode();
    }

    @Override
    public String getErrorMessage() {
        return this.commonError.getErrorMessage();
    }

    @Override
    public CommonError setErrorMessage(String message) {
        this.commonError.setErrorMessage(message);
        return this.commonError;
    }
}
