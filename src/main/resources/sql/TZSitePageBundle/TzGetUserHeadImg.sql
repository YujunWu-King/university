select 
	TZ_ATT_A_URL,
	A.TZ_ATTACHSYSFILENA 
from 
	PS_TZ_OPR_PHT_GL_T A,
	PS_TZ_OPR_PHOTO_T B 
where 
	A.TZ_ATTACHSYSFILENA=B.TZ_ATTACHSYSFILENA 
	and A.OPRID=?