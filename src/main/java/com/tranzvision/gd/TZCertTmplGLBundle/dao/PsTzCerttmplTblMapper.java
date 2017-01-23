package com.tranzvision.gd.TZCertTmplGLBundle.dao;

import com.tranzvision.gd.TZCertTmplGLBundle.model.PsTzCerttmplTbl;
import com.tranzvision.gd.TZCertTmplGLBundle.model.PsTzCerttmplTblKey;
import com.tranzvision.gd.TZCertTmplGLBundle.model.PsTzCerttmplTblWithBLOBs;

public interface PsTzCerttmplTblMapper {
    int deleteByPrimaryKey(PsTzCerttmplTblKey key);

    int insert(PsTzCerttmplTblWithBLOBs record);

    int insertSelective(PsTzCerttmplTblWithBLOBs record);

    PsTzCerttmplTblWithBLOBs selectByPrimaryKey(PsTzCerttmplTblKey key);

    int updateByPrimaryKeySelective(PsTzCerttmplTblWithBLOBs record);

    int updateByPrimaryKeyWithBLOBs(PsTzCerttmplTblWithBLOBs record);

    int updateByPrimaryKey(PsTzCerttmplTbl record);
}