package com.tranzvision.gd.TZApplicationExportTemplateBundle.dao;

import com.tranzvision.gd.TZApplicationExportTemplateBundle.model.PsTzExpFrmFldT;
import com.tranzvision.gd.TZApplicationExportTemplateBundle.model.PsTzExpFrmFldTKey;

public interface PsTzExpFrmFldTMapper {
    int deleteByPrimaryKey(PsTzExpFrmFldTKey key);

    int insert(PsTzExpFrmFldT record);

    int insertSelective(PsTzExpFrmFldT record);

    PsTzExpFrmFldT selectByPrimaryKey(PsTzExpFrmFldTKey key);

    int updateByPrimaryKeySelective(PsTzExpFrmFldT record);

    int updateByPrimaryKey(PsTzExpFrmFldT record);
}