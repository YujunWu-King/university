package com.tranzvision.gd.TZApplicationVerifiedBundle.dao;

import java.util.List;

import com.tranzvision.gd.TZApplicationVerifiedBundle.model.PsTzInteGroup;
import com.tranzvision.gd.TZApplicationVerifiedBundle.model.PsTzInterviewee;
/**
 * 面试分组
 * @author Administrator
 *
 */
public interface PsTzInteGroupMapper {
    
	//根据评委组id查询面试组
   List<PsTzInteGroup> findByPwId(String pwId);

   PsTzInteGroup findByGid(int group_id);
}