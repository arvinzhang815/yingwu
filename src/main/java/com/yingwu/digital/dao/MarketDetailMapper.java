package com.yingwu.digital.dao;


import com.yingwu.digital.bean.POJO.MarketDetail;

public interface MarketDetailMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(MarketDetail record);

    int insertSelective(MarketDetail record);

    MarketDetail selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(MarketDetail record);

    int updateByPrimaryKey(MarketDetail record);
}