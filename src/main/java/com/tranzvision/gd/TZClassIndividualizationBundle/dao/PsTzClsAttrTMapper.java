package com.tranzvision.gd.TZClassIndividualizationBundle.dao;

import com.tranzvision.gd.TZClassIndividualizationBundle.model.PsTzClsAttrT;
import com.tranzvision.gd.TZClassIndividualizationBundle.model.PsTzClsAttrTKey;

public interface PsTzClsAttrTMapper {
    int deleteByPrimaryKey(PsTzClsAttrTKey key);

    int insert(PsTzClsAttrT record);

    int insertSelective(PsTzClsAttrT record);

    PsTzClsAttrT selectByPrimaryKey(PsTzClsAttrTKey key);

    int updateByPrimaryKeySelective(PsTzClsAttrT record);

    int updateByPrimaryKey(PsTzClsAttrT record);
}