package com.tranzvision.gd.TZApplicationVerifiedBundle.dao;

import com.tranzvision.gd.TZApplicationVerifiedBundle.model.PsTzFormLabelTKey;

public interface PsTzFormLabelTMapper {
    int deleteByPrimaryKey(PsTzFormLabelTKey key);

    int insert(PsTzFormLabelTKey record);

    int insertSelective(PsTzFormLabelTKey record);
}