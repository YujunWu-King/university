package com.tranzvision.gd.TZMaterialInterviewReviewBundle.dao;

import com.tranzvision.gd.TZMaterialInterviewReviewBundle.model.PsTzQttjTbl;
import com.tranzvision.gd.TZMaterialInterviewReviewBundle.model.PsTzQttjTblKey;

public interface PsTzQttjTblMapper {
    int deleteByPrimaryKey(PsTzQttjTblKey key);

    int insert(PsTzQttjTbl record);

    int insertSelective(PsTzQttjTbl record);

    PsTzQttjTbl selectByPrimaryKey(PsTzQttjTblKey key);

    int updateByPrimaryKeySelective(PsTzQttjTbl record);

    int updateByPrimaryKey(PsTzQttjTbl record);
}