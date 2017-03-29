package com.tranzvision.gd.TZMaterialInterviewReviewBundle.dao;

import com.tranzvision.gd.TZMaterialInterviewReviewBundle.model.psTzClpwpslsTbl;
import com.tranzvision.gd.TZMaterialInterviewReviewBundle.model.psTzClpwpslsTblKey;

public interface psTzClpwpslsTblMapper {
    int deleteByPrimaryKey(psTzClpwpslsTblKey key);

    int insert(psTzClpwpslsTbl record);

    int insertSelective(psTzClpwpslsTbl record);

    psTzClpwpslsTbl selectByPrimaryKey(psTzClpwpslsTblKey key);

    int updateByPrimaryKeySelective(psTzClpwpslsTbl record);

    int updateByPrimaryKey(psTzClpwpslsTbl record);
}