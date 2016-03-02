package com.tranzvision.gd.TZApplicationVerifiedBundle.dao;

import com.tranzvision.gd.TZApplicationVerifiedBundle.model.Psprcsrqst;

public interface PsprcsrqstMapper {
    int deleteByPrimaryKey(Integer prcsinstance);

    int insert(Psprcsrqst record);

    int insertSelective(Psprcsrqst record);

    Psprcsrqst selectByPrimaryKey(Integer prcsinstance);

    int updateByPrimaryKeySelective(Psprcsrqst record);

    int updateByPrimaryKey(Psprcsrqst record);
}