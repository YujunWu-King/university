package com.tranzvision.gd.TZStuCertificateBundle.dao;

import com.tranzvision.gd.TZStuCertificateBundle.model.PsTzCertInfoTbl;

public interface PsTzCertInfoTblMapper {
    int deleteByPrimaryKey(Long tzSeqnum);

    int insert(PsTzCertInfoTbl record);

    int insertSelective(PsTzCertInfoTbl record);

    PsTzCertInfoTbl selectByPrimaryKey(Long tzSeqnum);

    int updateByPrimaryKeySelective(PsTzCertInfoTbl record);

    int updateByPrimaryKey(PsTzCertInfoTbl record);
}