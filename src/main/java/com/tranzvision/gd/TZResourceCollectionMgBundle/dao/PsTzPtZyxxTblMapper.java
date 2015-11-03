package com.tranzvision.gd.TZResourceCollectionMgBundle.dao;

import com.tranzvision.gd.TZResourceCollectionMgBundle.model.PsTzPtZyxxTbl;
import com.tranzvision.gd.TZResourceCollectionMgBundle.model.PsTzPtZyxxTblKey;

public interface PsTzPtZyxxTblMapper {
    int deleteByPrimaryKey(PsTzPtZyxxTblKey key);

    int insert(PsTzPtZyxxTbl record);

    int insertSelective(PsTzPtZyxxTbl record);

    PsTzPtZyxxTbl selectByPrimaryKey(PsTzPtZyxxTblKey key);

    int updateByPrimaryKeySelective(PsTzPtZyxxTbl record);

    int updateByPrimaryKey(PsTzPtZyxxTbl record);
}