function pageReadOnly()
{
	$("form input").prop("readonly", "readonly");
	$("form select").prop("readonly", "readonly");
	$("form textarea").prop("readonly", "readonly");
	$("form input").prop("disabled", true);
	$("form select").prop("disabled", true); 
	$("form textarea").prop("disabled", true);
	$("form input").nextAll('img').unbind("click");
	
	$(".main_inner_content_info").hide();
	$(".main_inner_content_del").hide();
	$(".main_inner_file_del").hide();
	$(".main_inner_content_del_bmb").hide();
	$(".age-action-button").hide();
	$(".fileupload_input").hide();
	$(".file_upload_button").hide();
	$(".main_inner_content_info_right_option_168px").hide();
	$('.main_inner_content_info_right_photo a').unbind("click"); 
	$('#app_save').unbind("click");
	$('#app_submit').unbind("click"); 
	$(".chosen-container").unbind();
	$('#app_save').hide();
	$('#app_submit').hide();
}

/*加载formvalidator信息*/
try{
	$.formValidator.initConfig({	
		formID:"main_list",		
		debug:false,	
		submitOnce:true,
		onError:function(msg,obj,errorlist){
			$("#errorlist").empty();
			$.map(errorlist,function(msg){	
				$("#errorlist").append("<li>" + msg + "</li>")	
			});
		},
		submitAfterAjaxPrompt : '有数据正在异步验证，请稍等...'	
	});
}catch(e)
{
}

//处理formvalidator的tips
var $flowtips = $("#flowtips");

/*处理报名表数据*/
if(dataApp){	

	/*处理推荐信重新拖之后实例发生变化的问题*/
	var refInstanceId = "";
	$.each(dataApp,function(instanceIdApp,objApp){
		if(objApp.classname=="recommendletter")
		{
			refInstanceId = instanceIdApp;
			return false;
		}
	})
	$.each(data.items,function(instanceId,obj){
		var recApp;
		if(obj.classname=="recommendletter" && refInstanceId!="")
		{
			recApp = dataApp[refInstanceId];
		}else
		{
			recApp = dataApp[instanceId];
		}
		
		if(recApp){
			if(recApp.isDoubleLine  == "Y"){
				//模板中的children为对象，报名表中的多行为数组，对象转换成数组并value赋值
				obj["children"] = [	obj["children"]] ;
				for(var j = 1; j< recApp["children"] .length;j++){
					obj["children"].push(cloneObj(obj["children"][0]));
				}
				/*是通用容器还是固定多行容器*/
				$.each(obj["children"],function(i,perChildren){
					$.each(perChildren,function(j,perXxx){
						var itemId0 = perXxx["itemId"];		
						
						if(i> 0)
						{	//设置itemId;
								if(perXxx.hasOwnProperty("isSingleLine"))
								{
									if(perXxx.isSingleLine  == "Y"){
									}else
									{
										perXxx["itemId"] =  itemId0 + "_" + i;
									}
								}else
								{
									perXxx["itemId"] =  itemId0 + "_" + i;
								}
								
						}
			
						if(recApp["children"][i][j]){	
							if(perXxx.isSingleLine  == "Y"){
								$.each(perXxx["children"],function(z,signlePerXxx){	
									var itemId1 = signlePerXxx["itemId"];	
									if(i>0)
									{
										signlePerXxx["itemId"] = itemId1 + "_" + i;
									}			
									signlePerXxx["value"] =  SurveyBuild.htmlCharReplace(recApp["children"][i][j]["children"][z]["value"]);
									if(signlePerXxx.hasOwnProperty("wzsm"))
									{
										signlePerXxx["wzsm"] =  recApp["children"][i][j]["children"][z]["wzsm"];
									}
								})
							}else if(perXxx.classname  == "imagesUpload" || perXxx.classname  == "AttachmentUpload"){
								/*多行容器的控件*/
								if(recApp["children"][i][j].hasOwnProperty("children"))
								{
									for(var k = 1; k< recApp["children"][i][j]["children"].length;k++){
										perXxx["children"].push(cloneObj(perXxx["children"][0]));
									}
									if( perXxx["children"] && $.isArray( perXxx["children"])){
										/*如果有多个附件，是需要克隆的*/
										$.each(perXxx["children"],function(x,filePerXxx){										
											filePerXxx["fileName"] = recApp["children"][i][j]["children"][x]["fileName"];
											filePerXxx["sysFileName"] = recApp["children"][i][j]["children"][x]["sysFileName"];
											filePerXxx["orderby"] = recApp["children"][i][j]["children"][x]["orderby"];
											filePerXxx["path"] = recApp["children"][i][j]["children"][x]["path"];
											filePerXxx["viewFileName"] = recApp["children"][i][j]["children"][x]["viewFileName"];
										})
									}
								}
							}else if(perXxx.classname  == "Radio" || perXxx.classname  == "Check"){
								/*单选框和复选框处理方式*/
								//console.log("Radio");
								//console.log(perXxx["option"]);
								if( perXxx["option"] ){
									$.each(perXxx["option"],function(ii,optionInfo){	
										//console.log(optionInfo);
										if(recApp["children"][i][j]["option"][ii]){
											optionInfo["checked"] = recApp["children"][i][j]["option"][ii]["checked"];
										}
									})
								}
								if(perXxx.hasOwnProperty("value")&&recApp["children"][i][j].hasOwnProperty("value"))
								{
									perXxx["value"] = recApp["children"][i][j]["value"];
								}
								if(recApp["othervalue"]){
									perXxx["othervalue"] = SurveyBuild.htmlCharReplace(recApp[i][j]["othervalue"]);
								}
							}else if(perXxx.classname  == "refLetterFile"){
								/*推荐信附件*/
								//console.log(recApp["children"][i][j]["filename"]);	
								perXxx["filename"] = recApp["children"][i][j]["filename"];
								perXxx["sysFileName"] = recApp["children"][i][j]["sysFileName"];
								perXxx["path"] = recApp["children"][i][j]["path"];
								perXxx["accessPath"] = recApp["children"][i][j]["accessPath"];
								perXxx["viewFileName"] = recApp["children"][i][j]["viewFileName"];
							}else{	
								if( recApp["children"][i][j]["value"]){
									perXxx["value"] = SurveyBuild.htmlCharReplace(recApp["children"][i][j]["value"]);
								}
								if(recApp["children"][i][j]){
									perXxx["othervalue"] = SurveyBuild.htmlCharReplace(recApp["children"][i][j]["othervalue"]);
								}
								if(perXxx.hasOwnProperty("wzsm"))
								{
									perXxx["wzsm"] =  recApp["children"][i][j]["wzsm"];
								}
							}
						}		
					})
				})						
			}else if(recApp.isSingleLine  == "Y"){
				//单行容器
				if( obj["children"] && $.isArray( obj["children"])){
					$.each(obj["children"],function(i,perXxx){
						//console.log(perXxx["itemId"] +"-->"+recApp["children"][i]["value"]+"-->"+i);										
						perXxx["value"] = 	SurveyBuild.htmlCharReplace(recApp["children"][i]["value"]);
						if(perXxx.hasOwnProperty("wzsm"))
						{
							perXxx["wzsm"] =  recApp["children"][i]["wzsm"];
						}
					})
				}
			}else if(recApp.classname  == "imagesUpload" || recApp.classname  == "AttachmentUpload"){
				//图片或者附件
				for(var j = 1; j< recApp["children"] .length;j++){
					obj["children"].push(cloneObj(obj["children"][0]));
				}
				if( obj["children"] && $.isArray( obj["children"])){
					//console.log(recApp["children"].length);
					$.each(obj["children"],function(i,perXxx){	
						//console.log( recApp["children"][i]["fileName"]);
						perXxx["fileName"] = recApp["children"][i]["fileName"];
						perXxx["sysFileName"] = recApp["children"][i]["sysFileName"];
						perXxx["orderby"] = recApp["children"][i]["orderby"];
						perXxx["path"] = recApp["children"][i]["path"];
						perXxx["viewFileName"] = recApp["children"][i]["viewFileName"];
					})
				}
			}else if(recApp.classname  == "Radio" || recApp.classname  == "Check"){
				/*单选框和复选框处理方式*/
				if( obj["option"] ){
					$.each(obj["option"],function(i,optionInfo){	
						//console.log(optionInfo);
						if(recApp["option"][i]){
							optionInfo["checked"] = recApp["option"][i]["checked"];
						}
					})
				}
				if(recApp.hasOwnProperty("value")&&obj.hasOwnProperty("value"))
				{
					obj["value"] = recApp["value"];
				}
				if(recApp["othervalue"]){
					obj["othervalue"] = SurveyBuild.htmlCharReplace(recApp["othervalue"]);
				}	
			}else{
				/*leve0级别普通字段*/
				if( recApp["value"]){
					obj["value"] = SurveyBuild.htmlCharReplace(recApp["value"]);
				}
				if(recApp["othervalue"]){
					obj["othervalue"] = SurveyBuild.htmlCharReplace(recApp["othervalue"]);
				}
				if(recApp["wzsm"]){
					obj["wzsm"] = recApp["wzsm"];
				}
			}
		}
	})
}

//console.log(JSON.stringify(data.items));
/*加载页面信息*/

SurveyBuild["_items"] = data.items;
SurveyBuild.is_edit_moda = false;
SurveyBuild._load();
SurveyBuild._initAssociatedShowHide();

/*处理select的样式*/
var config = {
				  '.chosen-select'           : {},
				  '.chosen-select-deselect'  : {allow_single_deselect:true},
				  '.chosen-select-no-single' : {disable_search_threshold:10},
				  '.chosen-select-no-results': {no_results_text:'Oops, nothing found!'},
				  '.chosen-select-width'     : {width:"95%"}
			};
for (var selector in config) {
  $(selector).chosen(config[selector]);
}

//console.log($(".bmbtjxtitle"));
$(".bmbtjxtitle").find(".chosen-container").unbind();


$("#tabNav .tabNav_c").click();
//console.log(appReadOnly);

if(appReadOnly == "Y"){
	pageReadOnly();
}else{
	if(!currentPageId){
		$('#app_save').show(); 
		$('#app_submit').show();
	}
}

layer.closeAll();
//单选框、复选框、多选框，其他
$(".sur_other_box").change(function(){
	if($(this).attr("type") == "radio"){
		if($(this).val() == $("input[name='"+$(this).attr("name")+"']:checked").val()){
			$(this).next("input").show()
		}else{
			$(this).next("input").hide()
		}
	}else{					
		$(this).prop("checked")?$(this).next("input").show():$(this).next("input").hide();
	}
});
$(".sur_radio_box").change(function(){
	$(this).closest("ul").find(".sur_other_box").next("input").hide();
});