$(document).ready(function(){
	
	$("#jgid").val(TZ_GD_LOGIN_SITEI_ORG_CODE);
	$("#siteid").val(TZ_GD_LOGIN_SITEI_ID);
	
	$("#letf_menu").height(600);

	LoadHeader("",ctnSiteid,"");
	LoadMenu("",ctnSiteid,"");
	LoadFooter("",ctnSiteid,"");
	LoadWelcome("",ctnSiteid,"");
});