select 
	'Y' as VALID_TD,
	TZ_QY_ZXBM,
	TZ_XWS,
	TZ_XSMS 
from 
	PS_TZ_ART_HD_TBL 
where 
	TZ_ART_ID=? 
	and concat(TZ_APPF_DT,' ',TZ_APPF_TM) <= ? 
	and concat(TZ_APPE_DT,' ',TZ_APPE_TM) > ?