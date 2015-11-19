insert into 
	ps_TZ_TREENODE_TMP (
		TZ_SEQNUM, 
		TREE_NAME,
		TREE_NODE_NUM,
		TREE_NODE,
		TREE_NODE_NUM_END,
		TREE_LEVEL_NUM,
		PARENT_NODE_NAME
	) (
		select 
			?,
			TREE_NAME,
			TREE_NODE_NUM,
			TREE_NODE,
			TREE_NODE_NUM_END,
			TREE_LEVEL_NUM,
			PARENT_NODE_NAME 
		from 
			PSTREENODE 
		where 
			TREE_NAME=? 
			and EFFDT=? 
			and SETID=' ' 
			and SETCNTRLVALUE=' ' 
			and TREE_NODE_NUM >=? 
			and TREE_NODE_NUM_END <= ?
	)