package com.tranzvision.gd.TZMbaPwMspsBundle.dao;

import com.tranzvision.gd.TZMbaPwMspsBundle.model.psTzMspwpsjlTbl;
import com.tranzvision.gd.TZMbaPwMspsBundle.model.psTzMspwpsjlTblKey;

public interface psTzMspwpsjlTblMapper {
    int deleteByPrimaryKey(psTzMspwpsjlTblKey key);

    int insert(psTzMspwpsjlTbl record);

    int insertSelective(psTzMspwpsjlTbl record);

    psTzMspwpsjlTbl selectByPrimaryKey(psTzMspwpsjlTblKey key);

    int updateByPrimaryKeySelective(psTzMspwpsjlTbl record);

    int updateByPrimaryKey(psTzMspwpsjlTbl record);
}