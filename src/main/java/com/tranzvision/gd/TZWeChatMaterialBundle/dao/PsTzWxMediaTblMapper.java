package com.tranzvision.gd.TZWeChatMaterialBundle.dao;

import com.tranzvision.gd.TZWeChatMaterialBundle.model.PsTzWxMediaTbl;
import com.tranzvision.gd.TZWeChatMaterialBundle.model.PsTzWxMediaTblKey;

public interface PsTzWxMediaTblMapper {
    int deleteByPrimaryKey(PsTzWxMediaTblKey key);

    int insert(PsTzWxMediaTbl record);

    int insertSelective(PsTzWxMediaTbl record);

    PsTzWxMediaTbl selectByPrimaryKey(PsTzWxMediaTblKey key);

    int updateByPrimaryKeySelective(PsTzWxMediaTbl record);

    int updateByPrimaryKey(PsTzWxMediaTbl record);
}