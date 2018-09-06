package com.yingwu.digital.bean.dto;

import java.util.List;

/**
 * @author Created by: zhangbingbing
 * @date 2018/8/27
 * 交易详情实体类
 **/
public class TradeDetailDto {
    private String ch;
    private String ts;
    private Tick tick;

    public String getCh() {
        return ch;
    }

    public void setCh(String ch) {
        this.ch = ch;
    }

    public String getTs() {
        return ts;
    }

    public void setTs(String ts) {
        this.ts = ts;
    }

    public void setTick(Tick tick) {
        this.tick = tick;
    }

    public Tick getTick() {
        return tick;
    }

    @Override
    public String toString() {
        return "TradeDetailDto{" +
                "ch='" + ch + '\'' +
                ", ts='" + ts + '\'' +
                ", tick=" + tick +
                '}';
    }
}
