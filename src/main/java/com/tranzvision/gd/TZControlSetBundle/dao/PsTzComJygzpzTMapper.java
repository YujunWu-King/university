package com.tranzvision.gd.TZControlSetBundle.dao;

import com.tranzvision.gd.TZControlSetBundle.model.PsTzComJygzpzT;
import com.tranzvision.gd.TZControlSetBundle.model.PsTzComJygzpzTKey;

public interface PsTzComJygzpzTMapper {
    int deleteByPrimaryKey(PsTzComJygzpzTKey key);

    int insert(PsTzComJygzpzT record);

    int insertSelective(PsTzComJygzpzT record);

    PsTzComJygzpzT selectByPrimaryKey(PsTzComJygzpzTKey key);

    int updateByPrimaryKeySelective(PsTzComJygzpzT record);

    int updateByPrimaryKey(PsTzComJygzpzT record);
}