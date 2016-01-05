function BindEnter(obj)
{
	if(obj.keyCode == 13)        
	{              
		Login();
	}
}

function setTab(m,n){
	var menu=document.getElementById("tab"+m).getElementsByTagName("a");  
	var showdiv=$("#content"+m+" .tabs1"); 
	for(i=0;i<menu.length;i++)
	{
	   menu[i].className=i==n?"now2":""; 
	   showdiv[i].style.display=i==n?"block":"none"; 
	}
}

$(document).ready(function(){
	LoadHeader($("#jgid").val(),$("#siteid").val(),$("#operator").val());
	LoadFooter($("#jgid").val(),$("#siteid").val(),$("#operator").val());
	SetImgCode();
});