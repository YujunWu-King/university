package com.tranzvision.gd.TZEmailSmsSendBundle.dao;

import com.tranzvision.gd.TZEmailSmsSendBundle.model.PsTzYjfjlshiTbl;
import com.tranzvision.gd.TZEmailSmsSendBundle.model.PsTzYjfjlshiTblKey;

public interface PsTzYjfjlshiTblMapper {
    int deleteByPrimaryKey(PsTzYjfjlshiTblKey key);

    int insert(PsTzYjfjlshiTbl record);

    int insertSelective(PsTzYjfjlshiTbl record);

    PsTzYjfjlshiTbl selectByPrimaryKey(PsTzYjfjlshiTblKey key);

    int updateByPrimaryKeySelective(PsTzYjfjlshiTbl record);

    int updateByPrimaryKey(PsTzYjfjlshiTbl record);
}