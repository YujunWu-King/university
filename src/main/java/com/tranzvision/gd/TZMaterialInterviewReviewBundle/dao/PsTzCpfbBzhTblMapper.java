package com.tranzvision.gd.TZMaterialInterviewReviewBundle.dao;

import com.tranzvision.gd.TZMaterialInterviewReviewBundle.model.PsTzCpfbBzhTbl;
import com.tranzvision.gd.TZMaterialInterviewReviewBundle.model.PsTzCpfbBzhTblKey;

public interface PsTzCpfbBzhTblMapper {
    int deleteByPrimaryKey(PsTzCpfbBzhTblKey key);

    int insert(PsTzCpfbBzhTbl record);

    int insertSelective(PsTzCpfbBzhTbl record);

    PsTzCpfbBzhTbl selectByPrimaryKey(PsTzCpfbBzhTblKey key);

    int updateByPrimaryKeySelective(PsTzCpfbBzhTbl record);

    int updateByPrimaryKey(PsTzCpfbBzhTbl record);
}