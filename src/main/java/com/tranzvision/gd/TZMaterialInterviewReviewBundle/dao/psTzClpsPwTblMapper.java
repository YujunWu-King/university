package com.tranzvision.gd.TZMaterialInterviewReviewBundle.dao;

import com.tranzvision.gd.TZMaterialInterviewReviewBundle.model.psTzClpsPwTbl;
import com.tranzvision.gd.TZMaterialInterviewReviewBundle.model.psTzClpsPwTblKey;

public interface psTzClpsPwTblMapper {
    int deleteByPrimaryKey(psTzClpsPwTblKey key);

    int insert(psTzClpsPwTbl record);

    int insertSelective(psTzClpsPwTbl record);

    psTzClpsPwTbl selectByPrimaryKey(psTzClpsPwTblKey key);

    int updateByPrimaryKeySelective(psTzClpsPwTbl record);

    int updateByPrimaryKey(psTzClpsPwTbl record);
}