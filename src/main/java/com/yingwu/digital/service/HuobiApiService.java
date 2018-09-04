package com.yingwu.digital.service;

import com.yingwu.digital.base.ApiResponse;
import com.yingwu.digital.base.DigitalException;

public interface HuobiApiService {

    public ApiResponse connectWebSocket(String json)throws DigitalException;
    public ApiResponse requestMsg(String json)throws DigitalException;
}
