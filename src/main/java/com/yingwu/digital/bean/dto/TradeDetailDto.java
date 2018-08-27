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
    private class Tick{
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

    public Tick getTick() {
        return tick;
    }

    public void setTick(Tick tick) {
        this.tick = tick;
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
