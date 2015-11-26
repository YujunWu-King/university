package com.tranzvision.gd.TZWebSiteInfoMgBundle.dao;

import com.tranzvision.gd.TZWebSiteInfoMgBundle.model.PsTzArtRecTbl;
import com.tranzvision.gd.TZWebSiteInfoMgBundle.model.PsTzArtRecTblWithBLOBs;

public interface PsTzArtRecTblMapper {
    int deleteByPrimaryKey(String tzArtId);

    int insert(PsTzArtRecTblWithBLOBs record);

    int insertSelective(PsTzArtRecTblWithBLOBs record);

    PsTzArtRecTblWithBLOBs selectByPrimaryKey(String tzArtId);

    int updateByPrimaryKeySelective(PsTzArtRecTblWithBLOBs record);

    int updateByPrimaryKeyWithBLOBs(PsTzArtRecTblWithBLOBs record);

    int updateByPrimaryKey(PsTzArtRecTbl record);
}