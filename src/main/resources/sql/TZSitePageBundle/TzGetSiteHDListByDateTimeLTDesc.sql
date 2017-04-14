select 
	PT2.TZ_COLU_ID,
	PT3.TZ_ART_ID,
	PT4.TZ_NACT_NAME,  
	PT4.TZ_START_DT ,
	PT4.TZ_HD_CS,
	PT4.TZ_QY_ZXBM ,
	PT4.TZ_APPF_DT,
	concat(PT4.TZ_APPF_DT,' ', PT4.TZ_APPF_TM) as TZ_APPF_TM,
	PT4.TZ_APPE_DT,
	concat(PT4.TZ_APPE_DT,' ', PT4.TZ_APPE_TM) as TZ_APPE_TM
from 
	(SELECT A.* FROM  PS_TZ_LM_NR_GL_T A,( 
        select TZ_SITE_ID,TZ_ART_ID,max(TZ_COLU_ID) TZ_COLU_ID from PS_TZ_LM_NR_GL_T where TZ_ART_PUB_STATE='Y' group by TZ_SITE_ID,TZ_ART_ID) B
        WHERE A.TZ_SITE_ID=B.TZ_SITE_ID AND A.TZ_COLU_ID=B.TZ_COLU_ID AND A.TZ_ART_ID=B.TZ_ART_ID) PT2, 
	PS_TZ_ART_REC_TBL PT3, 
	PS_TZ_ART_HD_TBL PT4  
where     
	PT2.TZ_ART_ID = PT3.TZ_ART_ID 
	and PT3.TZ_ART_ID = PT4.TZ_ART_ID  
	and PT2.TZ_ART_PUB_STATE = 'Y' 
	and PT2.TZ_SITE_ID = ? 
	and concat(PT4.TZ_END_DT,' ', PT4.TZ_END_TM) < ?
	and (
    	PT3.TZ_PROJECT_LIMIT<>'B' OR 
		EXISTS (SELECT 1 FROM PS_TZ_ART_AUDIENCE_T AUD 
			INNER JOIN PS_TZ_AUD_LIST_T LST ON(AUD.TZ_AUD_ID = LST.TZ_AUD_ID AND LST.OPRID=?
        ) WHERE AUD.TZ_ART_ID=PT4.TZ_ART_ID))
order by 
	PT2.TZ_MAX_ZD_SEQ desc, 
	PT2.TZ_ART_SEQ desc,
	PT4.TZ_START_DT desc 
limit ?,?