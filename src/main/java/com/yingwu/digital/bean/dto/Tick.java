package com.yingwu.digital.bean.dto;

import java.util.List;

/**
 * @author Created by: zhangbingbing
 * @date 2018/9/6
 **/
public class Tick {
    private String id;
    private String ts;
    private List<TradeDataDto> data;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTs() {
        return ts;
    }

    public void setTs(String ts) {
        this.ts = ts;
    }

    public List<TradeDataDto> getData() {
        return data;
    }

    public void setData(List<TradeDataDto> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "Tick{" +
                "id='" + id + '\'' +
                ", ts='" + ts + '\'' +
                ", data=" + data +
                '}';
    }
}
