select 
	a.TZ_EDIT_FLG 
from 
	PS_TZ_AQ_COMSQ_TBL a, 
	PSROLEUSER b, 
	PSROLECLASS c 
where 
	b.ROLEUSER=? 
	and b.ROLENAME=c.ROLENAME 
	and c.CLASSID=a.CLASSID 
	and a.TZ_COM_ID=? 
	and a.TZ_PAGE_ID=? 
	and a.TZ_EDIT_FLG=1