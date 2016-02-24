package com.tranzvision.gd.TZApplicationVerifiedBundle.dao;

import com.tranzvision.gd.TZApplicationVerifiedBundle.model.PsTzFormAtt2T;
import com.tranzvision.gd.TZApplicationVerifiedBundle.model.PsTzFormAtt2TKey;

public interface PsTzFormAtt2TMapper {
    int deleteByPrimaryKey(PsTzFormAtt2TKey key);

    int insert(PsTzFormAtt2T record);

    int insertSelective(PsTzFormAtt2T record);

    PsTzFormAtt2T selectByPrimaryKey(PsTzFormAtt2TKey key);

    int updateByPrimaryKeySelective(PsTzFormAtt2T record);

    int updateByPrimaryKey(PsTzFormAtt2T record);
}