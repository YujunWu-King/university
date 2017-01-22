package com.tranzvision.gd.TZCertTmplGLBundle.dao;

import com.tranzvision.gd.TZCertTmplGLBundle.model.PsTzCertTmplTbl;

public interface PsTzCertTblMapper {
    int deleteByPrimaryKey(String tzCertTmpl);

    int insert(PsTzCertTmplTbl record);

    int insertSelective(PsTzCertTmplTbl record);

    PsTzCertTmplTbl selectByPrimaryKey(String tzCertTmpl);

    int updateByPrimaryKeySelective(PsTzCertTmplTbl record);

    int updateByPrimaryKey(PsTzCertTmplTbl record);
}