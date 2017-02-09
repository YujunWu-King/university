package com.tranzvision.gd.TZImportTplMgBundle.dao;

import com.tranzvision.gd.TZImportTplMgBundle.model.TzImpTplFldT;
import com.tranzvision.gd.TZImportTplMgBundle.model.TzImpTplFldTKey;

public interface TzImpTplFldTMapper {
    int deleteByPrimaryKey(TzImpTplFldTKey key);

    int insert(TzImpTplFldT record);

    int insertSelective(TzImpTplFldT record);

    TzImpTplFldT selectByPrimaryKey(TzImpTplFldTKey key);

    int updateByPrimaryKeySelective(TzImpTplFldT record);

    int updateByPrimaryKey(TzImpTplFldT record);
}