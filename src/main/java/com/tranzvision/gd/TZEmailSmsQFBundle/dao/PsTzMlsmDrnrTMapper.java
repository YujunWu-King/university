package com.tranzvision.gd.TZEmailSmsQFBundle.dao;

import com.tranzvision.gd.TZEmailSmsQFBundle.model.PsTzMlsmDrnrT;
import com.tranzvision.gd.TZEmailSmsQFBundle.model.PsTzMlsmDrnrTKey;

public interface PsTzMlsmDrnrTMapper {
    int deleteByPrimaryKey(PsTzMlsmDrnrTKey key);

    int insert(PsTzMlsmDrnrT record);

    int insertSelective(PsTzMlsmDrnrT record);

    PsTzMlsmDrnrT selectByPrimaryKey(PsTzMlsmDrnrTKey key);

    int updateByPrimaryKeySelective(PsTzMlsmDrnrT record);

    int updateByPrimaryKeyWithBLOBs(PsTzMlsmDrnrT record);

    int updateByPrimaryKey(PsTzMlsmDrnrT record);
}