package com.tranzvision.gd.TZMbaPwClpsBundle.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.tranzvision.gd.TZMbaPwClpsBundle.model.PsTzMsPskshTbl;
import com.tranzvision.gd.TZMbaPwClpsBundle.model.PsTzMsPskshTblKey;

public interface PsTzMsPskshTblMapper {
    int deleteByPrimaryKey(PsTzMsPskshTblKey key);

    int insert(PsTzMsPskshTbl record);

    int insertSelective(PsTzMsPskshTbl record);

    PsTzMsPskshTbl selectByPrimaryKey(PsTzMsPskshTblKey key);

    int updateByPrimaryKeySelective(PsTzMsPskshTbl record);

    int updateByPrimaryKey(PsTzMsPskshTbl record);
    
    PsTzMsPskshTbl selectByCidAndAid(@Param("tzClassId")String tzClassId, @Param("tzAppInsId")Long tzAppInsId );

	List<PsTzMsPskshTbl> findByGroupID(Integer tz_group_id);
}