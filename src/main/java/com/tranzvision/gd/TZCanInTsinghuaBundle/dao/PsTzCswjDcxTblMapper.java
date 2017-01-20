package com.tranzvision.gd.TZCanInTsinghuaBundle.dao;

import com.tranzvision.gd.TZCanInTsinghuaBundle.model.PsTzCswjDcxTbl;
import com.tranzvision.gd.TZCanInTsinghuaBundle.model.PsTzCswjDcxTblKey;

public interface PsTzCswjDcxTblMapper {
    int deleteByPrimaryKey(PsTzCswjDcxTblKey key);

    int insert(PsTzCswjDcxTbl record);

    int insertSelective(PsTzCswjDcxTbl record);

    PsTzCswjDcxTbl selectByPrimaryKey(PsTzCswjDcxTblKey key);

    int updateByPrimaryKeySelective(PsTzCswjDcxTbl record);

    int updateByPrimaryKey(PsTzCswjDcxTbl record);
}