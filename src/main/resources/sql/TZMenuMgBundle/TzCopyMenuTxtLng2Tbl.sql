insert into 
	PS_TZ_AQ_CDJD_LNG 
 	select 
 		TREE_NAME,
 		TZ_MENU_NUM,
 		? TZ_LANGUAGE_ID,
 		TZ_MENU_MC 
 	from 
 		PS_TZ_AQ_CDJD_LNG A 
 	where 
 		A.TREE_NAME=? 
   		and A.TZ_LANGUAGE_ID=? 
   		and exists( 
 			select 
 				'X' 
  			from 
  				PSTREENODE S1 
 			where 
 				S1.TREE_NAME=A.TREE_NAME 
   				and S1.TREE_NODE=A.TZ_MENU_NUM 
   				and S1.TREE_NODE_NUM>=? 
   				and S1.TREE_NODE_NUM<=? 
   				and not exists( 
 					select 
 						'X' 
  					from 
  						PS_TZ_AQ_CDJD_LNG S2 
 					where 
 						S2.TREE_NAME=S1.TREE_NAME 
   						and S2.TZ_MENU_NUM=S1.TREE_NODE 
   						and S2.TZ_LANGUAGE_ID=? 
   				) 
   		)