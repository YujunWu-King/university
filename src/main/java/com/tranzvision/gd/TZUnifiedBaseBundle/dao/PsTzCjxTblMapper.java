package com.tranzvision.gd.TZUnifiedBaseBundle.dao;

import org.springframework.stereotype.Controller;

import com.tranzvision.gd.TZUnifiedBaseBundle.model.PsTzCjxTbl;
import com.tranzvision.gd.TZUnifiedBaseBundle.model.PsTzCjxTblKey;
import com.tranzvision.gd.TZUnifiedBaseBundle.model.PsTzCjxTblWithBLOBs;

@Controller("TZUnifiedBaseBundle")
public interface PsTzCjxTblMapper {
    int deleteByPrimaryKey(PsTzCjxTblKey key);

    int insert(PsTzCjxTblWithBLOBs record);

    int insertSelective(PsTzCjxTblWithBLOBs record);

    PsTzCjxTblWithBLOBs selectByPrimaryKey(PsTzCjxTblKey key);

    int updateByPrimaryKeySelective(PsTzCjxTblWithBLOBs record);

    int updateByPrimaryKeyWithBLOBs(PsTzCjxTblWithBLOBs record);

    int updateByPrimaryKey(PsTzCjxTbl record);
}