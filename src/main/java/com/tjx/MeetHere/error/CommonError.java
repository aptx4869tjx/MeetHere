package com.tjx.MeetHere.error;

public interface CommonError {
    int getErrorCode();

    String getErrorMessage();

    CommonError setErrorMessage(String message);
}
