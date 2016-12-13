package com.tranzvision.gd.TZApplicationSurveyBundle.dao;

import com.tranzvision.gd.TZApplicationSurveyBundle.model.PsTzDcWjattchT;

public interface PsTzDcWjattchTMapper {
    int deleteByPrimaryKey(String tzAttachsysfilena);

    int insert(PsTzDcWjattchT record);

    int insertSelective(PsTzDcWjattchT record);

    PsTzDcWjattchT selectByPrimaryKey(String tzAttachsysfilena);

    int updateByPrimaryKeySelective(PsTzDcWjattchT record);

    int updateByPrimaryKey(PsTzDcWjattchT record);
}