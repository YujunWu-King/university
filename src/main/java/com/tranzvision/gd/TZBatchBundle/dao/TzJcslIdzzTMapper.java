package com.tranzvision.gd.TZBatchBundle.dao;

import com.tranzvision.gd.TZBatchBundle.model.TzJcslIdzzT;

public interface TzJcslIdzzTMapper {
    int deleteByPrimaryKey(String tzJgId);

    int insert(TzJcslIdzzT record);

    int insertSelective(TzJcslIdzzT record);

    TzJcslIdzzT selectByPrimaryKey(String tzJgId);

    int updateByPrimaryKeySelective(TzJcslIdzzT record);

    int updateByPrimaryKey(TzJcslIdzzT record);
}