package com.tranzvision.gd.TZCertTypeGlBundle.dao;

import com.tranzvision.gd.TZCertTypeGlBundle.model.PsTzCertTypeTbl;
import com.tranzvision.gd.TZCertTypeGlBundle.model.PsTzCertTypeTblKey;

public interface PsTzCertTypeTblMapper {
    int deleteByPrimaryKey(PsTzCertTypeTblKey key);

    int insert(PsTzCertTypeTbl record);

    int insertSelective(PsTzCertTypeTbl record);

    PsTzCertTypeTbl selectByPrimaryKey(PsTzCertTypeTblKey key);

    int updateByPrimaryKeySelective(PsTzCertTypeTbl record);

    int updateByPrimaryKey(PsTzCertTypeTbl record);
}