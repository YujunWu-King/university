package com.tranzvision.gd.TZExportTplMgBundle.dao;

import com.tranzvision.gd.TZExportTplMgBundle.model.TzExpTplDfnT;

public interface TzExpTplDfnTMapper {
    int deleteByPrimaryKey(String tzTplId);

    int insert(TzExpTplDfnT record);

    int insertSelective(TzExpTplDfnT record);

    TzExpTplDfnT selectByPrimaryKey(String tzTplId);

    int updateByPrimaryKeySelective(TzExpTplDfnT record);

    int updateByPrimaryKey(TzExpTplDfnT record);
}