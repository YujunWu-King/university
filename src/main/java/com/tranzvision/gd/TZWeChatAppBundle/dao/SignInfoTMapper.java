package com.tranzvision.gd.TZWeChatAppBundle.dao;

import com.tranzvision.gd.TZWeChatAppBundle.model.SignInfoT;

public interface SignInfoTMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SignInfoT record);

    int insertSelective(SignInfoT record);

    SignInfoT selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SignInfoT record);

    int updateByPrimaryKey(SignInfoT record);
}