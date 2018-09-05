package com.yingwu.digital.bean.dto;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author Created by: zhangbingbing
 * @date 2018/8/27
 * Kline实体类
 **/
public class KlineDto {
    private String ch;
    private String ts;//时间
    private Tick tick;

    private class Tick{
        private String id;//K线id,
        private BigDecimal amount;//成交量,
        private BigDecimal count;//成交笔数,
        private BigDecimal open;//开盘价,
        private BigDecimal close;// 收盘价,当K线为最晚的一根时，是最新成交价
        private BigDecimal low;//最低价
        private BigDecimal high;//最高价,
        private BigDecimal vol;//成交额, 即 sum(每一笔成交价 * 该笔的成交量)
        private String ext1;//备用字段1
        private String ext2;//备用字段2
        private String ext3;//备用字段3

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public BigDecimal getAmount() {
            return amount;
        }

        public void setAmount(BigDecimal amount) {
            this.amount = amount;
        }

        public BigDecimal getCount() {
            return count;
        }

        public void setCount(BigDecimal count) {
            this.count = count;
        }

        public BigDecimal getOpen() {
            return open;
        }

        public void setOpen(BigDecimal open) {
            this.open = open;
        }

        public BigDecimal getClose() {
            return close;
        }

        public void setClose(BigDecimal close) {
            this.close = close;
        }

        public BigDecimal getLow() {
            return low;
        }

        public void setLow(BigDecimal low) {
            this.low = low;
        }

        public BigDecimal getHigh() {
            return high;
        }

        public void setHigh(BigDecimal high) {
            this.high = high;
        }

        public BigDecimal getVol() {
            return vol;
        }

        public void setVol(BigDecimal vol) {
            this.vol = vol;
        }

        public String getExt1() {
            return ext1;
        }

        public void setExt1(String ext1) {
            this.ext1 = ext1;
        }

        public String getExt2() {
            return ext2;
        }

        public void setExt2(String ext2) {
            this.ext2 = ext2;
        }

        public String getExt3() {
            return ext3;
        }

        public void setExt3(String ext3) {
            this.ext3 = ext3;
        }

        @Override
        public String toString() {
            return "TickDto{" +
                    "id='" + id + '\'' +
                    ", amount=" + amount +
                    ", count=" + count +
                    ", open=" + open +
                    ", close=" + close +
                    ", low=" + low +
                    ", high=" + high +
                    ", vol=" + vol +
                    ", ext1='" + ext1 + '\'' +
                    ", ext2='" + ext2 + '\'' +
                    ", ext3='" + ext3 + '\'' +
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
        return "KlineDto{" +
                "ch='" + ch + '\'' +
                ", ts='" + ts + '\'' +
                ", tick=" + tick +
                '}';
    }
}
