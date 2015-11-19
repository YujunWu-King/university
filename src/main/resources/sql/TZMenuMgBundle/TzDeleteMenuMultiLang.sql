delete from 
	PS_TZ_AQ_CDJD_LNG 
where 
	TREE_NAME=? 
	and TZ_MENU_NUM in (
		select 
			TREE_NODE 
		from 
			PSTREENODE 
		where 
			TREE_NAME=? 
			and TREE_NODE_NUM>=? 
			and TREE_NODE_NUM_END<=?
	)