package com.tranzvision.gd.TZEmailSmsQFBundle.dao;

import com.tranzvision.gd.TZEmailSmsQFBundle.model.PsTzYjqftxrzT;
import com.tranzvision.gd.TZEmailSmsQFBundle.model.PsTzYjqftxrzTKey;

public interface PsTzYjqftxrzTMapper {
    int deleteByPrimaryKey(PsTzYjqftxrzTKey key);

    int insert(PsTzYjqftxrzT record);

    int insertSelective(PsTzYjqftxrzT record);

    PsTzYjqftxrzT selectByPrimaryKey(PsTzYjqftxrzTKey key);

    int updateByPrimaryKeySelective(PsTzYjqftxrzT record);

    int updateByPrimaryKey(PsTzYjqftxrzT record);
}