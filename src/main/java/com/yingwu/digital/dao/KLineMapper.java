package com.yingwu.digital.dao;


import com.yingwu.digital.bean.POJO.KLine;
import org.springframework.stereotype.Repository;

@Repository
public interface KLineMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(KLine record);

    int insertSelective(KLine record);

    KLine selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(KLine record);

    int updateByPrimaryKey(KLine record);
}