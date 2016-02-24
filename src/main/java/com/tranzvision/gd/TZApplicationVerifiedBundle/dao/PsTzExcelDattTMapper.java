package com.tranzvision.gd.TZApplicationVerifiedBundle.dao;

import com.tranzvision.gd.TZApplicationVerifiedBundle.model.PsTzExcelDattT;

public interface PsTzExcelDattTMapper {
    int deleteByPrimaryKey(Integer processinstance);

    int insert(PsTzExcelDattT record);

    int insertSelective(PsTzExcelDattT record);

    PsTzExcelDattT selectByPrimaryKey(Integer processinstance);

    int updateByPrimaryKeySelective(PsTzExcelDattT record);

    int updateByPrimaryKey(PsTzExcelDattT record);
}