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
					  var Apply = response.comContent.Apply;
					  if(Apply == "true"){
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
					  }else{
						  if (languageCd == "ENG") {
								alert("不能连续申请不同的班级。返回报名表可以更换报考班级.");
						 } else {
								alert("不能连续申请不同的班级。返回报名表可以更换报考班级.");
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

//复试资料删除
function deleteAttachment(appins,sysfileName,layero){
	var tzParams = '{"ComID":"TZ_BMGL_BMBSH_COM","PageID":"TZ_UPLOADFSZL_STD","OperateType":"U","comParams":{ "delete":[{"appins":"'+appins+'","sysfileName":"'+sysfileName+'"}]}}';
	var layerIndex = layer.load(1,{
		  shade: [0.3,'#000'] 
	});
	$.ajax({
		type: "post",
		url: "/dispatcher", 									
		data:{
			tzParams:tzParams
		},
		dataType: "json",
		success: function(response){
			layer.close(layerIndex);
			queryFszl()
		}
	});
}

//查询复试资料
function queryFszl(){
	var classId = $("#classId").val();
	  var tzParams = '{"ComID":"TZ_BMGL_BMBSH_COM","PageID":"TZ_BMGL_AUDIT_STD","OperateType":"queryFile","comParams":{"classId":"'+classId+'"}}';
	  $.ajax({
			type: "POST",
			url: "/dispatcher",
			data: {
				tzParams: tzParams
			},
			dataType:"JSON",
	        xhrFields:{
	        	withCredentials:true
	        },
			success: function (response) {
	           var html = "";
	           var fileList = response.comContent.root;
	           if(fileList != null && fileList.length > 0){
	        	   for(var i = 0;i < fileList.length;i++){
	        		   var fileName = fileList[i].fileName;
	        		   var TZ_XXX_BH = fileList[i].TZ_XXX_BH;
	        		   var xuhao = fileList[i].xuhao;
	        		   var strfileDate = fileList[i].strfileDate;
	        		   var appInsId = fileList[i].appInsId;
	        		   var TZ_XXX_BH = fileList[i].TZ_XXX_BH;
	        		   
	        		   html += "<tr id='"+TZ_XXX_BH+"'>";
	        		   html += "<td>"+(i+1)+"</td>";
	        		   html += "<td>"+strfileDate+"</td>";
	        		   html += "<td>";
	        		   html += "<button type='button' class='layui-btn layui-btn-sm' onclick=deleteAttachment('"+appInsId+"','"+TZ_XXX_BH+"')><i class='layui-icon'>删除</i></button>";
	        		   html += "</td>";
	        		   html += "</tr>";
	        	   }
	           }
	           $("#myTbody").html(html);
			},
			failure: function (e) {
				console.log(e)
			}
		});
}

//打开复试资料上传窗口
function openFszl(){
	$('.layui-btn').click(function(){
		var strVar = "";
	    strVar += "<table class=\"layui-table\">\n";
	    strVar += "  <thead>\n";
	    strVar += "    <tr>\n";
	    strVar += "      <th style='min-width: 28px;'>序号<\/th>\n";
	    strVar += "      <th>附件名称<\/th>\n";
	    strVar += "      <th>操作<\/th>\n";
	    strVar += "    <\/tr> \n";
	    strVar += "  <\/thead>\n";
	    strVar += "  <tbody id='myTbody'>\n";
	    strVar += "  <\/tbody>\n";
	    strVar += "<\/table>\n";
		layer.open({
			  title: '补充资料上传',
			  area: ['500px', '300px'],
			  btn:[],
			  content: '<button type="button" class="layui-btn" id="test1">'
				  		+'<i class="layui-icon"></i>上传文件'
				  		+'</button>' + strVar,
				  		
			  yes: function(index, layero){
				    //do something
				    layer.close(index); //如果设定了yes回调，需进行手工关闭
				  },
			  success: function(layero, index){
				  queryFszl();
				  
				  var date = new Date();
				  var dateStr = date.getFullYear() +""+ (date.getMonth() + 1) +""+ date.getDate();
				  layui.use('upload', function(){
					  var index;
					  var upload = layui.upload;
					  //执行实例
					  var uploadInst = upload.render({
					    elem: '#test1', //绑定元素
					    url: '/UpdServlet', //上传接口
					    accept: 'file', //允许上传的文件类型
					    exts: 'docx|jpg|xlsx|doc|pdf|gif|bmp',
					    field:'orguploadfile',
					    multiple: true,//多附件上传
					    drag:true,
					    data: {
						    	//filePath:'/enrollment/' + dateStr,
						    	//orguploadfile: '213'
					    	},
					    before: function(){
	                            index = layer.load(0, {shade: false}); //0代表加载的风格，支持0-2
	                            //loading层
	                            index = top.layer.load(1, {
	                                shade: [0.5,'#ccc'],//0.1透明度的白色背景
	                            });
	                            //setTimeout(function () {
	                                //layer.close(index)
	                            //}, 2000)
	                    },
	                    allDone: function(obj){ //当文件全部被提交后，才触发
	                        console.log(obj.total); //得到总文件数
	                        console.log(obj.successful); //请求成功的文件数
	                        console.log(obj.aborted); //请求失败的文件数
	                    },
					    done: function(res){
					    	layer.close(index)
					    	//上传完毕回调
					    	var accessPath = res.msg.accessPath;
					    	var filename = res.msg.filename;
					    	var sysFileName = res.msg.sysFileName;
					    	var appinsId = $("#appinsId").val();
					    	var tzParams = {
		                    		"ComID":"TZ_BMGL_BMBSH_COM",
		                    		"PageID":"TZ_UPLOADFSZL_STD",
		                    		"OperateType":"U",
		                    		"comParams":
		                    			{ "add":
		                    				[{
		                    					"strAppId":appinsId,
		                    					"refLetterFile":sysFileName,
		                    					"FileName":filename,
		                    					"strSysFile":sysFileName,
		                    					"fileUrl":accessPath,
		                    					"fileType":1
		                    				}]
		                    			}
					    			};
					    	tzParams = JSON.stringify(tzParams);
					    	$.ajax({
					    		type: "post",
					    		url: "/dispatcher", 									
					    		data:{
					    			tzParams:tzParams
					    		},
					    		dataType: "json",
					    		success: function(response){
					    			//layer.close(layerIndex);
					    			queryFszl()
					    		}
					    	});
					    	
					    },
					    error: function(){
					      //请求异常回调
					    	alert("上传失败!");
					    }
					  });
					});
				  }
		});
	})
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
			
			var showLcNumVar = $("#showLcNum");
			if(showLcNumVar.length > 0){
				var showLcNum = showLcNumVar.val();
				var $content = $(".zxj_mba .step_Note").children();
	            $content.hide();
	            $content.eq(showLcNum).show();
	            
	            var $shade1 = $(".step_li .triangle");
	            var $shade2 = $(".step_li .triangle_span");
	            $shade1.hide();
	            $shade2.hide();
	            $shade1.eq(showLcNum/2).show();
	            $shade2.eq(showLcNum/2).show();
			}
			
			$(".sq_btn").click(function(){
			       $(".Shade").show();
			       $(".sq_pop").show();
			  
			});
			
			$(".steps .step_li").click(function(){
			      $(this).parents("").find(".step_li").children(".triangle_span").hide();
			      $(this).parents("").find(".step_li").children(".triangle").hide();
			      $(this).find(".triangle_span").show();
			      $(this).find(".triangle").show();
			    }); 
			
			var jgid = $("#jgid").val();
			if(jgid != "mpacc" || jgid != "mf" || jgid != "MPACC" || jgid != "MF"){
				$("#projectAdjust").hide();
			}
			$("#projectAdjust").click(function(){
				if (confirm("您确认申请项目调整？")==true){
					var classId = $("#classId").val();
					var appinsId = $("#appinsId").val();
					var applyId = $(".zxj_number>span").text();
					console.log(applyId);
					var tzParams = '{"ComID":"TZ_PROADJUST_COM","PageID":"TZ_PROADJUST_STD","OperateType":"projectAdjust","comParams":{"classId":"'+classId +'","appinsId":"'+appinsId+'","applyId":"'+applyId+'"}}';
					$.ajax({
						type:"POST",
						url: TzUniversityContextPath+"/dispatcher",
						data:{
							tzParams:tzParams
						},
						success:function(response){
							alert("申请项目调整成功，请等候管理员处理！");
						}
					})
				}
			})
			
			//打开复试资料上传窗口
			openFszl();
			
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
				$(".main_mid_zxj_rili.assdiv").each(function(){
					var _me = $(this);
					_me.find("a.zxj_more").click(function(){
						url = TzUniversityContextPath+"/dispatcher?classid=websiteCommList&siteId="+siteid+"&areaId="+areaId+"&columnId="+_me.find("div.zxj_tit1").attr("tab-col"),
						window.location = url;
					});
				});
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
						window.location = url;
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
