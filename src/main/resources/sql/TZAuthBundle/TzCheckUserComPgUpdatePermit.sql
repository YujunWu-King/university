select 
	C.TZ_EDIT_FLG 
from 
	PSROLEUSER A,
	PSROLECLASS B,
	PS_TZ_AQ_COMSQ_TBL C 
where 
	A.ROLEUSER=? 
	and A.DYNAMIC_SW='N' 
	and A.ROLENAME = B.ROLENAME 
	and B.CLASSID=C.CLASSID 
	and (C.TZ_COM_ID=? or C.TZ_COM_ID like ?) 
	and C.TZ_PAGE_ID=? 
order by C.TZ_EDIT_FLG desc 
limit 0,1