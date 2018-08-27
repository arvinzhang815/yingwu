package com.yingwu.digital.bean.dto;

/**
 * @author Created by: zhangbingbing
 * @date 2018/8/27
 * 交易数据实体类
 **/
public class TradeDataDto {

    private String id;//消息ID
    private String price;//成交价
    private String time;//成交时间
    private String amount;//成交量
    private String direction;//成交方向
    private String tradeId;//成交ID
    private String ts;//时间戳

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public String getTradeId() {
        return tradeId;
    }

    public void setTradeId(String tradeId) {
        this.tradeId = tradeId;
    }

    public String getTs() {
        return ts;
    }

    public void setTs(String ts) {
        this.ts = ts;
    }

    @Override
    public String toString() {
        return "TradeDataDto{" +
                "id='" + id + '\'' +
                ", price='" + price + '\'' +
                ", time='" + time + '\'' +
                ", amount='" + amount + '\'' +
                ", direction='" + direction + '\'' +
                ", tradeId='" + tradeId + '\'' +
                ", ts='" + ts + '\'' +
                '}';
    }
}
