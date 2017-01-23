package com.tranzvision.gd.TZImportTplMgBundle.dao;

import com.tranzvision.gd.TZImportTplMgBundle.model.TzImpTplDfnT;

public interface TzImpTplDfnTMapper {
    int deleteByPrimaryKey(String tzTplId);

    int insert(TzImpTplDfnT record);

    int insertSelective(TzImpTplDfnT record);

    TzImpTplDfnT selectByPrimaryKey(String tzTplId);

    int updateByPrimaryKeySelective(TzImpTplDfnT record);

    int updateByPrimaryKey(TzImpTplDfnT record);
}