$(document).ready(function(){
	$("#letf_menu").height(600);
		
	QueryColuHY(1,0);
		
	LoadHeader($("#jgid").val(),$("#siteid").val(),"");
	LoadFooter($("#jgid").val(),$("#siteid").val(),"");
	LoadMenu($("#jgid").val(),$("#siteid").val(),"");
	LoadWelcome($("#jgid").val(),$("#siteid").val(),"");
	
});