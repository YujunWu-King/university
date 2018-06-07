package com.tranzvision.gd.TZMyEnrollmentClueBundle.dao;

import com.tranzvision.gd.TZMyEnrollmentClueBundle.model.PsTzLxbgFileT;
import com.tranzvision.gd.TZMyEnrollmentClueBundle.model.PsTzLxbgFileTKey;

public interface PsTzLxbgFileTMapper {
    int deleteByPrimaryKey(PsTzLxbgFileTKey key);

    int insert(PsTzLxbgFileT record);

    int insertSelective(PsTzLxbgFileT record);

    PsTzLxbgFileT selectByPrimaryKey(PsTzLxbgFileTKey key);

    int updateByPrimaryKeySelective(PsTzLxbgFileT record);

    int updateByPrimaryKey(PsTzLxbgFileT record);
}