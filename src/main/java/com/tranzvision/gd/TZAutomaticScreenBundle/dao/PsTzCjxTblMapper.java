package com.tranzvision.gd.TZAutomaticScreenBundle.dao;

import com.tranzvision.gd.TZAutomaticScreenBundle.model.PsTzCjxTbl;
import com.tranzvision.gd.TZAutomaticScreenBundle.model.PsTzCjxTblKey;
import com.tranzvision.gd.TZAutomaticScreenBundle.model.PsTzCjxTblWithBLOBs;

public interface PsTzCjxTblMapper {
    int deleteByPrimaryKey(PsTzCjxTblKey key);
    
    int deleteByPrimaryKey(com.tranzvision.gd.TZUnifiedBaseBundle.model.PsTzCjxTblKey psTzCjxTblKey);

    int insert(PsTzCjxTblWithBLOBs record);
    
    int insert(com.tranzvision.gd.TZUnifiedBaseBundle.model.PsTzCjxTblWithBLOBs psTzCjxTblWithBLOBs);

    int insertSelective(PsTzCjxTblWithBLOBs record);

    PsTzCjxTblWithBLOBs selectByPrimaryKey(PsTzCjxTblKey key);

    int updateByPrimaryKeySelective(PsTzCjxTblWithBLOBs record);

    int updateByPrimaryKeyWithBLOBs(PsTzCjxTblWithBLOBs record);

    int updateByPrimaryKey(PsTzCjxTbl record);
}