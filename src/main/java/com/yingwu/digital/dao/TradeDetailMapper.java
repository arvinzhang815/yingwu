package com.yingwu.digital.dao;


import com.yingwu.digital.bean.POJO.TradeDetail;

public interface TradeDetailMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(TradeDetail record);

    int insertSelective(TradeDetail record);

    TradeDetail selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(TradeDetail record);

    int updateByPrimaryKey(TradeDetail record);
}