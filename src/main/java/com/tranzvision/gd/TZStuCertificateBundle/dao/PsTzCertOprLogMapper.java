package com.tranzvision.gd.TZStuCertificateBundle.dao;

import com.tranzvision.gd.TZStuCertificateBundle.model.PsTzCertOprLog;

public interface PsTzCertOprLogMapper {
    int deleteByPrimaryKey(Long tzCertLsh);

    int insert(PsTzCertOprLog record);

    int insertSelective(PsTzCertOprLog record);

    PsTzCertOprLog selectByPrimaryKey(Long tzCertLsh);

    int updateByPrimaryKeySelective(PsTzCertOprLog record);

    int updateByPrimaryKey(PsTzCertOprLog record);
}