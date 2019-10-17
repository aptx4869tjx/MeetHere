package com.tjx.MeetHere.response;

public class CommonReturnType {
    private String status;
    private Object data;

    //只有数据时状态为success
    public CommonReturnType(Object data) {
        this.status = "success";
        this.data = data;
    }

    //若发生异常，则通过此方法设置状态为失败
    public CommonReturnType(String status, Object data) {
        this.status = status;
        this.data = data;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
