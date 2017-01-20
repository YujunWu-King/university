package com.tranzvision.gd.TZEmailSmsQFBundle.dao;

import com.tranzvision.gd.TZEmailSmsQFBundle.model.PsTxLjAet;

public interface PsTxLjAetMapper {
    int deleteByPrimaryKey(Integer prcsinstance);

    int insert(PsTxLjAet record);

    int insertSelective(PsTxLjAet record);

    PsTxLjAet selectByPrimaryKey(Integer prcsinstance);

    int updateByPrimaryKeySelective(PsTxLjAet record);

    int updateByPrimaryKey(PsTxLjAet record);
}