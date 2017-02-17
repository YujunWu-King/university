//报名人选择班级;
//报名人选择可以报名的班级;
function viewClass(siteId,language,viewType){
	 //var siteId = TZ_GD_LOGIN_SITEI_ID;
	 var tzParams = '{"ComID":"TZ_ZLSQ_JD_COM","PageID":"TZ_ZLSQ_JD_PAGE","OperateType":"HTML","comParams":{"viewType":"' + viewType+ '","siteId":"' + siteId+ '","language":"' + language+ '","oprate":"R"}}';

	 $.ajax({
 					type:"POST",
 					url: urlBegin,
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


//选择新闻活动范围弹出框显示;
function selectNewsProject(siteid){
	var tzParams = '{"ComID":"TZ_ZLSQ_JD_COM","PageID":"TZ_ZLSQ_JD_PAGE","OperateType":"HTML","comParams":{"viewType":"SELECTPROJECT","siteid":"' + siteid+ '","oprate":"R"}}';

	 $.ajax({
				type:"POST",
				url: urlBegin,
				data:{
					tzParams:tzParams
				},
				success:function(response){
					$("div#showWindPerDiv").html(response);
					$("div#showWindPerDiv").show();

					$("span.close").click(function(){
						$("div#showWindPerDiv").hide();
					});
				
				},
		    	failure: function () {
				  		
		    	}    
		});
}

//选择新闻活动范围(全选和取消全选);
function selectAllProject(){
	var code_Values = document.getElementsByName("projectCheckbox"); 
	var i = 0;
	if ($("#selectAllProject").get(0).checked){
		for (i = 0; i < code_Values.length; i++) {  
			code_Values[i].checked = true;    
		}  
	}else{
		for (i = 0; i < code_Values.length; i++) { 
			code_Values[i].checked = false;    
		} 
	}
}

//选择新闻活动范围(确定);
function addSelectPrj(){
	var selectPrjs = "";
	var code_Values = document.getElementsByName("projectCheckbox"); 
	var i = 0;
	for (i = 0; i < code_Values.length; i++) {  
		if(code_Values[i].checked){
			if(selectPrjs == ""){
				selectPrjs = code_Values[i].value;
			}else{
				selectPrjs = selectPrjs + ";" + code_Values[i].value;
			}
		}
	}
	
	var tzParams = '{"ComID":"TZ_ZLSQ_JD_COM","PageID":"TZ_ZLSQ_JD_PAGE","OperateType":"HTML","comParams":{"viewType":"ADDSELECTPROJECT","selectPrjs":"' + selectPrjs+ '","oprate":"R"}}';

	 $.ajax({
		type:"POST",
		url: urlBegin,
		data:{
			tzParams:tzParams
		},
		success:function(response){
			iniArea();
		},
		failure: function () {
				  		
		}    
	});
	 
	$("div#showWindPerDiv").hide();
}

//选择新闻活动范围(取消);
function cancleSelectPrj(){
	$("div#showWindPerDiv").hide();
}

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


function checkHisApply(classId,languageCd){
	  var confirmValue = false;
	  var siteid=$("#siteid").val();
	  var tzParams = '{"ComID":"TZ_APPLY_CENTER_COM","PageID":"TZ_APPLY_CENT_PAGE","OperateType":"QF","comParams":{"classId":"'+classId+'","siteid":"'+siteid+'"}}';

		$.ajax({
				type:"POST",
				url:urlBegin,
				data:{
					tzParams:tzParams
				},
				dataType:'json',
				success:function(response){
					  var HaveHisApplyForm = response.comContent.HaveHisApplyForm;
					  var HaveHCBJ=response.comContent.HaveHCBJ;
					  if(HaveHCBJ=="true"){
						if(languageCd == "ENG"){
							alert("Our system has detected existing registration information from an application you previously started, and you are not allowed to  the application for other programs related.");
						}else{
							alert("系统检测到您已经申请了一个项目，不允许再申请相关的其他项目。");
						}
					  }else{
						if(HaveHisApplyForm == "true"){
							if(languageCd == "ENG"){
						  	confirmValue = confirm("Our system has detected existing registration information from an application you previously started. Would you like to copy your previously entered application information into the new application form?");
						  }else{
						  	confirmValue = confirm("系统检测到您曾经报过名，是否从过往报名表中带入历史数据？");
						  }
						}
						 
						if(confirmValue==true){
							location.href =urlBegin+'?classid=appId&APPCOPY=Y&TZ_CLASS_ID='+classId+'&SITE_ID='+siteid;
						}else{
							location.href =urlBegin+'?classid=appId&TZ_CLASS_ID='+classId+'&SITE_ID='+siteid;
						} 
					}
				}   
			});

}


function addBmb(languageCd){
	var classidVale = $('input[name="classidradio"]:checked ').val();
	if(classidVale == null || classidVale == ""){
		if(languageCd == "ENG"){
			alert("请选择班级");
		}else{
			alert("请选择班级");
		}
		
	}else{
		checkHisApply(classidVale,languageCd);
	}
}

function classSelectCancle(){
	$(".Shade").hide();
    $(".sq_pop").hide();
}


$(document).ready(function(){
	/*if ($(".main_mid").height()>760){
		$("#letf_menu").height($(".main_mid").height());
	}else{
		$("#letf_menu").height(760);
	}*/
	
	$("#jgid").val(TZ_GD_LOGIN_SITEI_ORG_CODE);
	$("#siteid").val(TZ_GD_LOGIN_SITEI_ID);
	$("#operator").val(getOperatorType());

	LoadHeader($("#jgid").val(),$("#siteid").val(),$("#operator").val());
	LoadFooter($("#jgid").val(),$("#siteid").val(),$("#operator").val());
	LoadMenu($("#jgid").val(),$("#siteid").val(),"");
	//iniArea();
	getPerInfCard();
	
	//加载报名中心;
	var siteid = $("#siteid").val();
	var oprate = $("#operator").val();

	var tzParams = '{"ComID":"TZ_APPLY_CENTER_COM","PageID":"TZ_APPLY_CENT_PAG2","OperateType":"HTML","comParams":{"siteId":"'+siteid +'","oprate":"'+oprate+'"}}';
	$.ajax({
		type:"POST",
		url: TzUniversityContextPath+"/dispatcher",
		data:{
			tzParams:tzParams
		},
		success:function(response){
			$('.main_mid_zxj_interview').prop('innerHTML', response);
			tab(".zxj_mba .step_head",".zxj_mba .step_Note","","mousedown");
			$(".sq_btn").click(function(){
			       $(".Shade").show();
			       $(".sq_pop").show();
			  
			});
		},
		failure: function () {
		  	
		} 
		
	});
	//end 报名中心;
	
	//加载报考日历
	$(".main_mid_zxj_rili").each(function(){
		var $me = $(this);
		var areaId = $me.attr("area-id");
		var colId = $me.attr("area-col");
		
		$me.html("加载中...");
		
		var tzParams = '{"ComID":"TZ_RECRUIT_VIEW_COM","PageID":"TZ_BKRL_STD","OperateType":"HTML","comParams":{"siteId":"'+siteid +'","areaId":"'+areaId+'","oprate":"'+oprate+'"}}';
		$.ajax({
			type:"POST",
			url: TzUniversityContextPath+"/dispatcher",
			data:{
				tzParams:tzParams
			},
			success:function(response){
				$me.html(response);
				$(".date_body").each(function(){
					$clamp(this, {clamp:2});
				});
			},
			failure: function () {
			  	//ToDo
			} 
		});
	});
	//end 报考日历;
	
	//加载招生活动、报考通知、资料专区区域
	$(".main_mid_zxj_zs.assdiv").each(function(){
		var $me = $(this);
		//区域id;
		var areaId = $me.attr("area-id");
	
		$me.html("加载中...");
		var tzParams = '{"ComID":"TZ_RECRUIT_VIEW_COM","PageID":"TZ_RECRUIT_ACT_STD","OperateType":"HTML","comParams":{"siteId":"'+siteid +'","areaId":"'+areaId+'","oprate":"'+oprate+'"}}';
		$.ajax({
			type:"POST",
			url: TzUniversityContextPath+"/dispatcher",
			data:{
				tzParams:tzParams
			},
			success:function(response){
				$me.html(response);
				$(".main_mid_zxj_zs.assdiv").each(function(){
					var _me = $(this);
					_me.find("a.zxj_more").click(function(){
						url = TzUniversityContextPath+"/dispatcher?classid=websiteCommList&siteId="+siteid+"&areaId="+areaId+"&columnId="+_me.find("li.tab_on").attr("tab-col"),
						window.open(url);
					});
				});
				tab(".tabwrap .tabhead",".tabwrap .tabNote","tab_on","mousedown");
			},
			failure: function () {
			  	//ToDo
			} 
		});
	});
	   
	//end招生活动、报考通知、资料专区区域
	
	//获取栏目单篇文章：友情链接，二维码等区域
	$(".single_page").each(function(){
		var $me = $(this);
		//区域id;
		var areaId = $me.attr("area-id");
		//栏目id;
		var colId = $me.attr("area-col");
		$me.html("加载中...");
		var tzParams = '{"ComID":"TZ_WEBINFO_SHOW_COM","PageID":"TZ_SINGLE_PAGE_STD","OperateType":"HTML","comParams":{"siteId":"'+siteid +'","areaId":"'+areaId+'","oprate":"'+oprate+'"}}';
		$.ajax({
			type:"POST",
			url: TzUniversityContextPath+"/dispatcher",
			data:{
				tzParams:tzParams
			},
			success:function(response){
				$me.html(response);
			},
			failure: function () {
			  	//ToDo
			} 
		});
	});
	//end获取栏目单篇文章
});
