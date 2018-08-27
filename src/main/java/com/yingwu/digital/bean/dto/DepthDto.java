package com.yingwu.digital.bean.dto;

import java.util.List;

/**
 * @author Created by: zhangbingbing
 * @date 2018/8/27
 * depth实体类
 **/
public class DepthDto {
    private String ch;
    private String ts;//时间戳
    private List<?> asks;//卖盘

    private List<?> bids;//买盘

    public List<?> getAsks() {
        return asks;
    }

    public void setAsks(List<?> asks) {
        this.asks = asks;
    }

    public List<?> getBids() {
        return bids;
    }

    public void setBids(List<?> bids) {
        this.bids = bids;
    }

    @Override
    public String toString() {
        return "DepthDto{" +
                "ch='" + ch + '\'' +
                ", ts='" + ts + '\'' +
                ", asks=" + asks +
                ", bids=" + bids +
                '}';
    }
}
