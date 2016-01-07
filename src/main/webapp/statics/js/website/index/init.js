$(document).ready(function(){

	$("#jgid").val(TZ_GD_LOGIN_SITEI_ORG_CODE);
	$("#siteid").val(TZ_GD_LOGIN_SITEI_ID);
	$("#operator").val(getOperatorType());

	$("#letf_menu").height($("#rigth_menu").height());

	LoadHeader($("#jgid").val(),$("#siteid").val(),$("#operator").val());
	LoadFooter($("#jgid").val(),$("#siteid").val(),$("#operator").val());
	LoadMenu($("#jgid").val(),$("#siteid").val(),"");
	LoadWelcome($("#jgid").val(),$("#siteid").val(),$("#operator").val());
	iniArea();
	getPerInfCard();
});