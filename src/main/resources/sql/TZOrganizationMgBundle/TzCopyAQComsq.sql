select 
	TZ_COM_ID,
	TZ_PAGE_ID,
	DISPLAYONLY,
	TZ_EDIT_FLG,
	AUTHORIZEDACTIONS 
from 
	PS_TZ_AQ_COMSQ_TBL 
where 
	CLASSID=?