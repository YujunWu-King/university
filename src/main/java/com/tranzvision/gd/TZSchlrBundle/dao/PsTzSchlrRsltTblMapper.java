package com.tranzvision.gd.TZSchlrBundle.dao;

import com.tranzvision.gd.TZSchlrBundle.model.PsTzSchlrRsltTbl;
import com.tranzvision.gd.TZSchlrBundle.model.PsTzSchlrRsltTblKey;

public interface PsTzSchlrRsltTblMapper {
    int deleteByPrimaryKey(PsTzSchlrRsltTblKey key);

    int insert(PsTzSchlrRsltTbl record);

    int insertSelective(PsTzSchlrRsltTbl record);

    PsTzSchlrRsltTbl selectByPrimaryKey(PsTzSchlrRsltTblKey key);

    int updateByPrimaryKeySelective(PsTzSchlrRsltTbl record);

    int updateByPrimaryKey(PsTzSchlrRsltTbl record);
}