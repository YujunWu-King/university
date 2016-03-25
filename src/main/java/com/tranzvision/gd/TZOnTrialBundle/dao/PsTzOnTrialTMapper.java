package com.tranzvision.gd.TZOnTrialBundle.dao;

import com.tranzvision.gd.TZOnTrialBundle.model.PsTzOnTrialT;
import com.tranzvision.gd.TZOnTrialBundle.model.PsTzOnTrialTWithBLOBs;

public interface PsTzOnTrialTMapper {
    int deleteByPrimaryKey(Integer tzSeqNum);

    int insert(PsTzOnTrialTWithBLOBs record);

    int insertSelective(PsTzOnTrialTWithBLOBs record);

    PsTzOnTrialTWithBLOBs selectByPrimaryKey(Integer tzSeqNum);

    int updateByPrimaryKeySelective(PsTzOnTrialTWithBLOBs record);

    int updateByPrimaryKeyWithBLOBs(PsTzOnTrialTWithBLOBs record);

    int updateByPrimaryKey(PsTzOnTrialT record);
}