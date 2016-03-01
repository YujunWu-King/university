package com.tranzvision.gd.TZApplicationVerifiedBundle.dao;

import com.tranzvision.gd.TZApplicationVerifiedBundle.model.PsTzBmbDceT;

public interface PsTzBmbDceTMapper {
    int deleteByPrimaryKey(String runCntlId);

    int insert(PsTzBmbDceT record);

    int insertSelective(PsTzBmbDceT record);

    PsTzBmbDceT selectByPrimaryKey(String runCntlId);

    int updateByPrimaryKeySelective(PsTzBmbDceT record);

    int updateByPrimaryKeyWithBLOBs(PsTzBmbDceT record);

    int updateByPrimaryKey(PsTzBmbDceT record);
}