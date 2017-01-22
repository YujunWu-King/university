package com.tranzvision.gd.TZCertTmplGLBundle.dao;

import com.tranzvision.gd.TZCertTmplGLBundle.model.PsTzCertimageTbl;

public interface PsTzCertimageTblMapper {
    int deleteByPrimaryKey(String tzAttachsysfilena);

    int insert(PsTzCertimageTbl record);

    int insertSelective(PsTzCertimageTbl record);

    PsTzCertimageTbl selectByPrimaryKey(String tzAttachsysfilena);

    int updateByPrimaryKeySelective(PsTzCertimageTbl record);

    int updateByPrimaryKey(PsTzCertimageTbl record);
}