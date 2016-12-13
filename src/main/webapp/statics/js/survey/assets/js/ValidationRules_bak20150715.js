var ValidationRules = {
	"RequireValidator" : {
		 _eventList : {

		 },
		 _Validator : function(itemId,msg){
			 if($.trim($("#"+itemId).val())){
					return true;
				}else{
					
				}
		 }		
	},
	"ByzhAndXwzhValidator" : {		
		 is_configpage : true,	
		 _getConfigPage : function(params){
			var byzh = params["byzh"] || "";
			var xwzh = params["xwzh"] || "";
			return '<fieldset>*毕业证号信息项ID：<input id="rule_set_byzhid" type="text" width="300px" value="'+byzh+'"><div style="height:20px;"></div>*学位证号信息项ID：<input id="rule_set_xwzhid"  type="text" width="300px" value="'+xwzh+'" ></fieldset>';
		 },
		 _getConfigDataValidator:function(){
			 if(!$("#rule_set_byzhid").val()){
				 SurveyBuild.fail($("#rule_set_byzhid"),"毕业证号信息项ID必填！");
				 return false
			 }else  if( !$("#rule_set_xwzhid").val()){
				 SurveyBuild.fail($("#rule_set_xwzhid"),"学位证号信息项ID必填！");
				 return false
			 } 
			 return true;
		 },
		 _getConfigData : function(){
			return {"byzh":$("#rule_set_byzhid").val(),"xwzh":$("#rule_set_xwzhid").val()};
		 },
		 _eventList : {
			"keyup" : function(){
				$(this).val($(this).val().replace(/[^\d]/g,''));
			},
			"onbeforepaste" : function(){
				clipboardData.setData('text',clipboardData.getData('text').replace(/[^\d]/g,''));
			}
		 },
		 _Validator : function(itemId,msg,params){
			 if($.trim($("#"+itemId).val())){
					return true;
				}else{
					$('#'+itemId).next().html(msg).show();
				}
		 }
	},
	"AhphValidator" : {		
		 _eventList : {
			"keyup" : function(itemId,msg,params){
				$("#"+itemId).val($("#"+itemId).val().replace(/[^A-Za-z,\s]+$/g,''));
			},
			"keydown" :function(){
				
			}
		 }
	},
	"RegularValidator" : {
		 _eventList : {
		 },
		 _Validator : function(itemId,msg){
			 var _val = $.trim($("#"+itemId).val());
			 var _result =new RegExp($("#"+itemId).attr("data-regular")).test(_val);
			 if(!_result){
				return msg || "格式不正确！";
			}
		 }		
	},
	"RegularValidator" : {
		 _eventList : {
		 },
		 _Validator : function(itemId,msg){
			 var _val = $.trim($("#"+itemId).val());
			 var _result =new RegExp($("#"+itemId).attr("data-regular")).test(_val);
			 if(!_result){
				return msg || "格式不正确！";
			}
		 }		
	},
	"DateComReuqire" : {
		 _eventList : {
		 },
		 _Validator : function(itemId,children){
			 $.each([children[0]["itemId"],children[1]["itemId"]],function(i,id){
				 $("#"+itemId+id)
					.formValidator({tipID:itemId+children[0]["itemId"]+'Tip',onShow: "",onFocus: "&nbsp;",onCorrect: "&nbsp;"})			
					.inputValidator({min:1,onError:children[i]["name"]+"必须填写！" })
					.functionValidator({
						fun:function(){
							if($("#"+itemId+children[2]["itemId"]).prop("checked")){
								var dateArr=$("#"+itemId+id).val().split("-"); 
								if(new Date(parseInt(dateArr[0]),parseInt(dateArr[1]) - 1,parseInt(dateArr[2]),23,59) >= new Date() ){
									return "开始时期、结束日期都不能大于当前日期";
								}
							}else{
								if($("#"+itemId+children[0]["itemId"]).val() &&  $("#"+itemId+children[1]["itemId"]).val() && $("#"+itemId+children[0]["itemId"]).val()>$("#"+itemId+children[1]["itemId"]).val()){
									return "开始日期不能大于结束日期";
								}
							}
						}
					});
			 });	 
		 }		
	},
	"AssociatedValidator" : {
		 _eventList : {
			"click" : function(itemId,msg,params,item,$this){
				//console.log('click event');
				//console.log($this);
				eval(msg);
			},
			"blur" : function(itemId,msg,params,item,$this){
				//console.log('blur event');
				eval(msg);
			}
		 },
		 _Validator : function(itemId,msg,params,item){
			
		 }		
	}
};



/*
 *  编辑页面的引擎，报名表页面的引擎，编辑页面的保存，报名表页面的保存 
 *  控件案例：1、标准控件  - 日期控件， 2、标准控件 - 单行文本， 3、常用控件 - 英文字母
 *  校验
 **/