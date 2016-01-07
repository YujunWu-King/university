$(document).ready(function(){
	
	$("#jgid").val(TZ_GD_LOGIN_SITEI_ORG_CODE);
	$("#siteid").val(TZ_GD_LOGIN_SITEI_ID);
	
	$("#letf_menu").height(600);

	QueryColu(1);

	LoadHeader($("#jgid").val(),$("#siteid").val(),"");
	LoadFooter($("#jgid").val(),$("#siteid").val(),"");
	LoadMenu($("#jgid").val(),$("#siteid").val(),"");
    LoadWelcome($("#jgid").val(),$("#siteid").val(),"");
});