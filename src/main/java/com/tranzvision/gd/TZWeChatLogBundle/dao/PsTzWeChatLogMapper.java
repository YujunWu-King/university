package com.tranzvision.gd.TZWeChatLogBundle.dao;

import com.tranzvision.gd.TZWeChatLogBundle.model.PsTzWeChatLogTbl;
import com.tranzvision.gd.TZWeChatLogBundle.model.PsTzWeChatLogTblKey;

public interface PsTzWeChatLogMapper {
   
	PsTzWeChatLogTbl selectByPrimaryKey(PsTzWeChatLogTblKey key);

}