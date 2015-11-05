package com.tranzvision.gd.TZSystemVariableMgBundle.dao;

import com.tranzvision.gd.TZSystemVariableMgBundle.model.PsTzSvChainKey;

public interface PsTzSvChainMapper {
    int deleteByPrimaryKey(PsTzSvChainKey key);

    int insert(PsTzSvChainKey record);

    int insertSelective(PsTzSvChainKey record);
}