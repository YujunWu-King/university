select 
	TZ_ART_CONENT 
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
	and PT1.TZ_COLU_ID = (
		select 
			TZ_MENU_COLUMN 
		from 
			PS_TZ_SITEI_MENU_T 
		where 
			TZ_SITEI_ID = ? 
			and TZ_MENU_ID = ? 
			and TZ_MENU_STATE = 'Y'
	) 
order by PT2.TZ_ART_NEWS_DT desc