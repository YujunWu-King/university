package com.tranzvision.gd.TZMaterialInterviewReviewBundle.dao;

import com.tranzvision.gd.TZMaterialInterviewReviewBundle.model.PsTzQttjBzhTbl;
import com.tranzvision.gd.TZMaterialInterviewReviewBundle.model.PsTzQttjBzhTblKey;

public interface PsTzQttjBzhTblMapper {
    int deleteByPrimaryKey(PsTzQttjBzhTblKey key);

    int insert(PsTzQttjBzhTbl record);

    int insertSelective(PsTzQttjBzhTbl record);

    PsTzQttjBzhTbl selectByPrimaryKey(PsTzQttjBzhTblKey key);

    int updateByPrimaryKeySelective(PsTzQttjBzhTbl record);

    int updateByPrimaryKey(PsTzQttjBzhTbl record);
}