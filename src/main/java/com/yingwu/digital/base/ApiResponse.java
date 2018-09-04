package com.yingwu.digital.base;

import java.util.Date;

public class ApiResponse {
    private String code;
    private String msg;
    private Object data;
    private String timestamp = Long.toString(new Date().getTime());

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public ApiResponse setSuccess(){
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setCode("0");
        apiResponse.setMsg("success");
        return apiResponse;
    }

    public ApiResponse setError(){
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setCode("1");
        apiResponse.setMsg("fail");
        return apiResponse;
    }
    @Override
    public String toString() {
        return "ApiResponse{" +
                "code='" + code + '\'' +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                ", timestamp='" + timestamp + '\'' +
                '}';
    }

}
