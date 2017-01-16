package com.tranzvision.gd.TZCanInTsinghuaBundle.dao;

import com.tranzvision.gd.TZCanInTsinghuaBundle.model.PsTzCswjPctTbl;
import com.tranzvision.gd.TZCanInTsinghuaBundle.model.PsTzCswjPctTblKey;

public interface PsTzCswjPctTblMapper {
    int deleteByPrimaryKey(PsTzCswjPctTblKey key);

    int insert(PsTzCswjPctTbl record);

    int insertSelective(PsTzCswjPctTbl record);

    PsTzCswjPctTbl selectByPrimaryKey(PsTzCswjPctTblKey key);

    int updateByPrimaryKeySelective(PsTzCswjPctTbl record);

    int updateByPrimaryKey(PsTzCswjPctTbl record);
}