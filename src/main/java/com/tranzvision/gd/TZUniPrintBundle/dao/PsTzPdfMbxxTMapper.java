package com.tranzvision.gd.TZUniPrintBundle.dao;

import com.tranzvision.gd.TZUniPrintBundle.model.PsTzPdfMbxxTKey;

public interface PsTzPdfMbxxTMapper {
    int deleteByPrimaryKey(PsTzPdfMbxxTKey key);

    int insert(PsTzPdfMbxxTKey record);

    int insertSelective(PsTzPdfMbxxTKey record);
}