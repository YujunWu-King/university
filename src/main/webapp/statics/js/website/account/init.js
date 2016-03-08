$(document).ready(function(){
	
	$("#jgid").val(TZ_GD_LOGIN_SITEI_ORG_CODE);
	$("#siteid").val(TZ_GD_LOGIN_SITEI_ID);
	
	$("#letf_menu").height(922);
	
	LoadHeader("",$("#siteid").val(),"");
	LoadFooter("",$("#siteid").val(),"");
	LoadMenu("",$("#siteid").val(),"");
    LoadWelcome("",$("#siteid").val(),"");
    
});