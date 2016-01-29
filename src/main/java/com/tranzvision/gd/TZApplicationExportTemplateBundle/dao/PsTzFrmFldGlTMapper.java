package com.tranzvision.gd.TZApplicationExportTemplateBundle.dao;

import com.tranzvision.gd.TZApplicationExportTemplateBundle.model.PsTzFrmFldGlT;
import com.tranzvision.gd.TZApplicationExportTemplateBundle.model.PsTzFrmFldGlTKey;

public interface PsTzFrmFldGlTMapper {
    int deleteByPrimaryKey(PsTzFrmFldGlTKey key);

    int insert(PsTzFrmFldGlT record);

    int insertSelective(PsTzFrmFldGlT record);

    PsTzFrmFldGlT selectByPrimaryKey(PsTzFrmFldGlTKey key);

    int updateByPrimaryKeySelective(PsTzFrmFldGlT record);

    int updateByPrimaryKey(PsTzFrmFldGlT record);
}