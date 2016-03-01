//查看报名进度;
function viewJd(classId, instanceId,language,viewType){
	var tzParams = '{"ComID":"TZ_ZLSQ_JD_COM","PageID":"TZ_ZLSQ_JD_PAGE","OperateType":"HTML","comParams":{"bmClassId":"' + classId + '","instanceId":"' + instanceId+ '","viewType":"' + viewType+ '","language":"' + language+ '","oprate":"R"}}';
    $.ajax({
		type:"POST",
		url:  TzUniversityContextPath+"/dispatcher",
		data:{
				tzParams:tzParams
		},
		success:function(response){
			$("div#showWindDiv").html(response);
			$("div#showWindDiv").show();
	
			$("span.close").click(function(){
				$("div#showWindDiv").hide();
			});
					
		},
		failure: function () {
				  		
		}    
	});
}

$(document).ready(function(){
	if ($(".main_mid").height()>760){
		$("#letf_menu").height($(".main_mid").height());
	}else{
		$("#letf_menu").height(760);
	}
	
	$("#jgid").val(TZ_GD_LOGIN_SITEI_ORG_CODE);
	$("#siteid").val(TZ_GD_LOGIN_SITEI_ID);
	$("#operator").val(getOperatorType());

	LoadHeader($("#jgid").val(),$("#siteid").val(),$("#operator").val());
	LoadFooter($("#jgid").val(),$("#siteid").val(),$("#operator").val());
	LoadMenu($("#jgid").val(),$("#siteid").val(),"");
	LoadWelcome($("#jgid").val(),$("#siteid").val(),$("#operator").val());
	iniArea();
	getPerInfCard();
	
	//加载报名中心;
	var siteid = $("#siteid").val();
	var oprate = $("#operator").val();

	var tzParams = '{"ComID":"TZ_APPLY_CENTER_COM","PageID":"TZ_APPLY_CENT_PAGE","OperateType":"HTML","comParams":{"siteId":"'+siteid +'","oprate":"'+oprate+'"}}';
	$.ajax({
		type:"POST",
		url: TzUniversityContextPath+"/dispatcher",
		data:{
			tzParams:tzParams
		},
		success:function(response){
			$('.applicationCenter').prop('outerHTML', response);
					 
			$(".active").parent().parent().parent().next("table").find(".index_bd").hide();
			$(".index-bm table tr.index_hd td:first-child").click(function(){
	        
				$(this).toggleClass("active").parent().parent().parent().next("table").find(".index_bd").slideToggle();
				if($(this).hasClass("active")){
	         		 $(this).find("i").removeClass("application_shrink");
	         		 $(this).find("i").addClass("application_expand");
	             
				}else{
					$(this).find("i").removeClass("application_expand");
					$(this).find("i").addClass("application_shrink");
				}
			})
		},
		failure: function () {
		  	
		} 
		
	});
	//end 报名中心;
});