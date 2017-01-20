package com.tranzvision.gd.TZEmailSmsQFBundle.dao;

import com.tranzvision.gd.TZEmailSmsQFBundle.model.PsTzYjqftxjlT;
import com.tranzvision.gd.TZEmailSmsQFBundle.model.PsTzYjqftxjlTKey;

public interface PsTzYjqftxjlTMapper {
    int deleteByPrimaryKey(PsTzYjqftxjlTKey key);

    int insert(PsTzYjqftxjlT record);

    int insertSelective(PsTzYjqftxjlT record);

    PsTzYjqftxjlT selectByPrimaryKey(PsTzYjqftxjlTKey key);

    int updateByPrimaryKeySelective(PsTzYjqftxjlT record);

    int updateByPrimaryKeyWithBLOBs(PsTzYjqftxjlT record);

    int updateByPrimaryKey(PsTzYjqftxjlT record);
}