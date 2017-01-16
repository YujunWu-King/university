select 
	TZ_DLZH_ID 
from 
	PS_TZ_AQ_YHXX_TBL 
where 
	((TZ_MOBILE=? and TZ_SJBD_BZ='Y') or (LCASE(TZ_EMAIL)=? and TZ_YXBD_BZ='Y')) 
	and TZ_RYLX='ZCYH' 
	and TZ_JG_ID=?