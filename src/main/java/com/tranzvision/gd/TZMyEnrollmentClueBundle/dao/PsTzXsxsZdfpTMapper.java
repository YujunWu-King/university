package com.tranzvision.gd.TZMyEnrollmentClueBundle.dao;

import com.tranzvision.gd.TZMyEnrollmentClueBundle.model.PsTzXsxsZdfpT;
import com.tranzvision.gd.TZMyEnrollmentClueBundle.model.PsTzXsxsZdfpTKey;

public interface PsTzXsxsZdfpTMapper {
    int deleteByPrimaryKey(PsTzXsxsZdfpTKey key);

    int insert(PsTzXsxsZdfpT record);

    int insertSelective(PsTzXsxsZdfpT record);

    PsTzXsxsZdfpT selectByPrimaryKey(PsTzXsxsZdfpTKey key);

    int updateByPrimaryKeySelective(PsTzXsxsZdfpT record);

    int updateByPrimaryKey(PsTzXsxsZdfpT record);
}