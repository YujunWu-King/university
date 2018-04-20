package com.tranzvision.gd.TZMyEnrollmentClueBundle.dao;

import com.tranzvision.gd.TZMyEnrollmentClueBundle.model.PsTzWtxsTmpT;
import com.tranzvision.gd.TZMyEnrollmentClueBundle.model.PsTzWtxsTmpTKey;

public interface PsTzWtxsTmpTMapper {
    int deleteByPrimaryKey(PsTzWtxsTmpTKey key);

    int insert(PsTzWtxsTmpT record);

    int insertSelective(PsTzWtxsTmpT record);

    PsTzWtxsTmpT selectByPrimaryKey(PsTzWtxsTmpTKey key);

    int updateByPrimaryKeySelective(PsTzWtxsTmpT record);

    int updateByPrimaryKeyWithBLOBs(PsTzWtxsTmpT record);

    int updateByPrimaryKey(PsTzWtxsTmpT record);
}