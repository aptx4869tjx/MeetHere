package com.tjx.MeetHere.controller;

import com.tjx.MeetHere.error.BusinessException;
import com.tjx.MeetHere.error.ErrorEm;
import com.tjx.MeetHere.response.CommonReturnType;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

public class BaseController {

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Object handleException(HttpServletRequest request, Exception ex) {
        Map<String, Object> map = new HashMap<>();
        if (ex instanceof BusinessException) {
            BusinessException exception = (BusinessException) ex;
            map.put("errorCode", exception.getErrorCode());
            map.put("errorMsg", exception.getErrorMessage());
        } else {
            System.out.println(ex.getMessage());
            map.put("errorCode", ErrorEm.UNKNOWN_ERROR.getErrorCode());
            map.put("errorMsg", ErrorEm.UNKNOWN_ERROR.getErrorMessage());
        }
        return new CommonReturnType("fail", map);
    }
}
