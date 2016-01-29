package com.tranzvision.gd.TZApplicationExportTemplateBundle.dao;

import com.tranzvision.gd.TZApplicationExportTemplateBundle.model.PsTzExportTmpT;

public interface PsTzExportTmpTMapper {
    int deleteByPrimaryKey(String tzExportTmpId);

    int insert(PsTzExportTmpT record);

    int insertSelective(PsTzExportTmpT record);

    PsTzExportTmpT selectByPrimaryKey(String tzExportTmpId);

    int updateByPrimaryKeySelective(PsTzExportTmpT record);

    int updateByPrimaryKey(PsTzExportTmpT record);
}