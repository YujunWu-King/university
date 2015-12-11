package com.tranzvision.gd.TZEmailSmsSendBundle.dao;

import com.tranzvision.gd.TZEmailSmsSendBundle.model.PsTzYjfsrizhTbl;

public interface PsTzYjfsrizhTblMapper {
    int deleteByPrimaryKey(String tzRzjlId);

    int insert(PsTzYjfsrizhTbl record);

    int insertSelective(PsTzYjfsrizhTbl record);

    PsTzYjfsrizhTbl selectByPrimaryKey(String tzRzjlId);

    int updateByPrimaryKeySelective(PsTzYjfsrizhTbl record);

    int updateByPrimaryKeyWithBLOBs(PsTzYjfsrizhTbl record);

    int updateByPrimaryKey(PsTzYjfsrizhTbl record);
}