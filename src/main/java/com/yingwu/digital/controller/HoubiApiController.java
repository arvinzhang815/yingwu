package com.yingwu.digital.controller;

import com.yingwu.digital.base.ApiResponse;
import com.yingwu.digital.service.HuobiApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/huobiApi")
public class HoubiApiController {

    @Autowired
    private HuobiApiService houbiApiService;

    @RequestMapping("/start")
    public ApiResponse connectWebSocket(String request){
        ApiResponse  apiResponse = new ApiResponse();
        try {
            houbiApiService.connectWebSocket(request);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return apiResponse;
    }

    @RequestMapping("/requestmsg")
    public ApiResponse requestMsg(String request){
        ApiResponse  apiResponse = new ApiResponse();
        try {
            houbiApiService.requestMsg(request);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return apiResponse;
    }
}
