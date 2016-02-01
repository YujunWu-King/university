select 
	PT2.TZ_COLU_ID,
    PT3.TZ_ART_ID,
    PT3.TZ_ART_TITLE, 
    PT2.TZ_ART_NEWS_DT
from 
	PS_TZ_SITEI_COLU_T PT1, 
    PS_TZ_LM_NR_GL_T PT2, 
    PS_TZ_ART_REC_TBL PT3 
where 
	PT1.TZ_SITEI_ID = PT2.TZ_SITE_ID 
    and PT1.TZ_COLU_ID = PT2.TZ_COLU_ID 
    and PT2.TZ_ART_ID = PT3.TZ_ART_ID 
    and PT2.TZ_ART_PUB_STATE = 'Y' 
    and PT1.TZ_SITEI_ID = ? 
    and PT1.TZ_COLU_ID = ? 
order by 
	PT2.TZ_MAX_ZD_SEQ desc, 
	PT2.TZ_ART_NEWS_DT desc
limit ?,?