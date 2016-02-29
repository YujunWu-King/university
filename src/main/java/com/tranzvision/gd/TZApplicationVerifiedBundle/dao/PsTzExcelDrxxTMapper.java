package com.tranzvision.gd.TZApplicationVerifiedBundle.dao;

import com.tranzvision.gd.TZApplicationVerifiedBundle.model.PsTzExcelDrxxT;

public interface PsTzExcelDrxxTMapper {
    int deleteByPrimaryKey(Integer processinstance);

    int insert(PsTzExcelDrxxT record);

    int insertSelective(PsTzExcelDrxxT record);

    PsTzExcelDrxxT selectByPrimaryKey(Integer processinstance);

    int updateByPrimaryKeySelective(PsTzExcelDrxxT record);

    int updateByPrimaryKey(PsTzExcelDrxxT record);
}