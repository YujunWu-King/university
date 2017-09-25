package com.tranzvision.gd.TZWeChatMaterialBundle.dao;

import com.tranzvision.gd.TZWeChatMaterialBundle.model.PsTzWxTwlTbl;
import com.tranzvision.gd.TZWeChatMaterialBundle.model.PsTzWxTwlTblKey;

public interface PsTzWxTwlTblMapper {
    int deleteByPrimaryKey(PsTzWxTwlTblKey key);

    int insert(PsTzWxTwlTbl record);

    int insertSelective(PsTzWxTwlTbl record);

    PsTzWxTwlTbl selectByPrimaryKey(PsTzWxTwlTblKey key);

    int updateByPrimaryKeySelective(PsTzWxTwlTbl record);

    int updateByPrimaryKeyWithBLOBs(PsTzWxTwlTbl record);

    int updateByPrimaryKey(PsTzWxTwlTbl record);
}