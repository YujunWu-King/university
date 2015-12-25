package com.tranzvision.gd.TZWebSiteRegisteBundle.dao;

import com.tranzvision.gd.TZWebSiteRegisteBundle.model.PsTzDzyxYzmTbl;

public interface PsTzDzyxYzmTblMapper {
    int deleteByPrimaryKey(String tzTokenCode);

    int insert(PsTzDzyxYzmTbl record);

    int insertSelective(PsTzDzyxYzmTbl record);

    PsTzDzyxYzmTbl selectByPrimaryKey(String tzTokenCode);

    int updateByPrimaryKeySelective(PsTzDzyxYzmTbl record);

    int updateByPrimaryKey(PsTzDzyxYzmTbl record);
}