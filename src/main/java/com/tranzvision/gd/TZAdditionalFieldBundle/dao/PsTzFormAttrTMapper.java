package com.tranzvision.gd.TZAdditionalFieldBundle.dao;

import com.tranzvision.gd.TZAdditionalFieldBundle.model.PsTzFormAttrT;
import com.tranzvision.gd.TZAdditionalFieldBundle.model.PsTzFormAttrTKey;

public interface PsTzFormAttrTMapper {
    int deleteByPrimaryKey(PsTzFormAttrTKey key);

    int insert(PsTzFormAttrT record);

    int insertSelective(PsTzFormAttrT record);

    PsTzFormAttrT selectByPrimaryKey(PsTzFormAttrTKey key);

    int updateByPrimaryKeySelective(PsTzFormAttrT record);

    int updateByPrimaryKey(PsTzFormAttrT record);
}