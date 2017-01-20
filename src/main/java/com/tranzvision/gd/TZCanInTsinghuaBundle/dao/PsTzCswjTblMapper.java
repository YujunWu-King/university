package com.tranzvision.gd.TZCanInTsinghuaBundle.dao;

import com.tranzvision.gd.TZCanInTsinghuaBundle.model.PsTzCswjTbl;

public interface PsTzCswjTblMapper {
    int deleteByPrimaryKey(String tzCsWjId);

    int insert(PsTzCswjTbl record);

    int insertSelective(PsTzCswjTbl record);

    PsTzCswjTbl selectByPrimaryKey(String tzCsWjId);

    int updateByPrimaryKeySelective(PsTzCswjTbl record);

    int updateByPrimaryKey(PsTzCswjTbl record);
}