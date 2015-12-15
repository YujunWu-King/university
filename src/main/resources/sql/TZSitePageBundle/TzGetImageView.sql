select 
	C.TZ_SKIN_ID,
    C.TZ_IMG_VIEW 
from 
	PS_TZ_SITEM_DEFN_T A,
    PS_TZ_SITEM_SKIN_T B,
    PS_TZ_SITEM_IMG_T C 
where 
	A.TZ_SITEM_ID=B.TZ_SITEM_ID 
    and A.TZ_SITEM_ID=C.TZ_SITEM_ID 
    and B.TZ_SKIN_ID=C.TZ_SKIN_ID 
    and A.TZ_SITEM_ID=? 
    and B.TZ_SKIN_ID=? 
order by TZ_IMG_XH