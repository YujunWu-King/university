package com.tranzvision.gd.TZEventsBundle.dao;

import com.tranzvision.gd.TZEventsBundle.model.PsTzArtAudienceTKey;

public interface PsTzArtAudienceTMapper {
    int deleteByPrimaryKey(PsTzArtAudienceTKey key);

    int insert(PsTzArtAudienceTKey record);

    int insertSelective(PsTzArtAudienceTKey record);
}