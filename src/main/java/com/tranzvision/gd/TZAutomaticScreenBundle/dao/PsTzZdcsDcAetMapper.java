package com.tranzvision.gd.TZAutomaticScreenBundle.dao;

import com.tranzvision.gd.TZAutomaticScreenBundle.model.PsTzZdcsDcAet;

public interface PsTzZdcsDcAetMapper {
    int deleteByPrimaryKey(String runCntlId);

    int insert(PsTzZdcsDcAet record);

    int insertSelective(PsTzZdcsDcAet record);

    PsTzZdcsDcAet selectByPrimaryKey(String runCntlId);

    int updateByPrimaryKeySelective(PsTzZdcsDcAet record);

    int updateByPrimaryKeyWithBLOBs(PsTzZdcsDcAet record);

    int updateByPrimaryKey(PsTzZdcsDcAet record);
}