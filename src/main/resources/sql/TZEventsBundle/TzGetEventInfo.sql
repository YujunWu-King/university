select 
	a.TZ_NACT_NAME,
	a.TZ_START_DT,
	a.TZ_START_TM,
	a.TZ_END_DT,
	a.TZ_END_TM,
	a.TZ_NACT_ADDR,
	a.TZ_HD_CS,
	b.TZ_OUT_ART_URL,
	b.TZ_ART_CONENT,
	b.TZ_IMAGE_TITLE,
	b.TZ_IMAGE_DESC,
	b.TZ_ATTACHSYSFILENA,
	a.TZ_QY_ZXBM, 
	a.TZ_APPF_DT,
	a.TZ_APPF_TM,
	a.TZ_APPE_DT,
	a.TZ_APPE_TM, 
	a.TZ_XWS, 
	a.TZ_XSMS, 
	c.TZ_ART_PUB_STATE,
	b.TZ_ART_TYPE1,
	b.TZ_PROJECT_LIMIT
from 
	PS_TZ_ART_HD_TBL a,
	PS_TZ_ART_REC_TBL b,
	PS_TZ_LM_NR_GL_T c 
where 
	a.TZ_ART_ID=? 
	and a.TZ_ART_ID=b.TZ_ART_ID 
	and a.TZ_ART_ID=c.TZ_ART_ID 
	and c.TZ_SITE_ID=? 
	and c.TZ_COLU_ID=?