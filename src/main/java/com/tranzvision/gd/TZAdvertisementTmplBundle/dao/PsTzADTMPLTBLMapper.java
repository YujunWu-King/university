package com.tranzvision.gd.TZAdvertisementTmplBundle.dao;

import com.tranzvision.gd.TZAdvertisementTmplBundle.model.PsTzADTMPLTBL;
import com.tranzvision.gd.TZAdvertisementTmplBundle.model.PsTzADTMPLTBLKey;

public interface PsTzADTMPLTBLMapper {
    int deleteByPrimaryKey(PsTzADTMPLTBLKey key);

    int insert(PsTzADTMPLTBL record);

    int insertSelective(PsTzADTMPLTBL record);

    PsTzADTMPLTBL selectByPrimaryKey(PsTzADTMPLTBLKey key);

    int updateByPrimaryKeySelective(PsTzADTMPLTBL record);

    int updateByPrimaryKeyWithBLOBs(PsTzADTMPLTBL record);

    int updateByPrimaryKey(PsTzADTMPLTBL record);
}