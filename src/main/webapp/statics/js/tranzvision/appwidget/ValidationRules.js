var ValidationRules = {
	/*信息项必填校验规则*/
	"RequireValidator" : {
		 _eventList : {

		 },
		 _Validator : function(itemId,msg,item){
			 if($.trim($("#" + itemId).val())){
				 return true;
			 }else{
				 return msg;
			 }
		 }
	},
	/*字符长度校验验证规则*/
	"CharLenValidator":{
		_eventList : {
		},
		_Validator : function(id, msg, item){
			var minLen = parseInt(item.minLen);
			var maxLen = parseInt(item.maxLen);
			var length = $.trim($("#" + id).val()).length;
			if(length > maxLen || length < minLen){
				return msg;
			}
			return true;
		}
	},
	/* 数值校验
	 * 数字文本框 - 数字大小以及小数位数验证规则
	 * 日期组合框 - 开始日期、结束日期、至今的顺序验证
	 * */
	"NumSizeValidator":{
		_eventList : {
		},
		_Validator : function(id, msg, item){
			var classname = item.classname;

			/*日期组合框*/
			if(classname == "DateComboBox"){
				var $dateInputStart = $("#" + item["itemId"] + item.children[0]["itemId"]);
				var $dateInputEnd = $("#" + item["itemId"] + item.children[1]["itemId"]);
				var $todayCheckbox = $("#" + item["itemId"] + item.children[2]["itemId"]);
				var startDt = $dateInputStart.val();
				var endDt = $dateInputEnd.val();
				var todayDt = new Date();

				if ($todayCheckbox.prop("checked")){
					if (todayDt < new Date(startDt) && startDt != ""){
						return msg;
					}
				} else {
					if (new Date(endDt) < new Date(startDt) !="" && startDt != ""){
						return msg;
					}
				}
				return true;
			}

			/*数字文本框*/
			if(classname == "DigitalTextBox" || classname == "DigitalCompletion"){
				var min = parseInt(item.min);
				var max = parseInt(item.max);
				var digits = parseInt(item.digits);

				var objVal = $.trim($("#" + id).val());
				var numAry = objVal.split(".");
				var digitsLen = 0;
				if(numAry.length > 1){
					digitsLen = numAry[1].length;
				}
				objVal = parseInt(objVal);

				if(objVal > max || objVal < min || (digitsLen > 0 && digitsLen > digits)){
					return msg;
				}
			}
			return true;
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
				 return false;
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
					return msg;
				}
			return true;
		 }
	},
	"AhphValidator" : {		
		 is_configpage : false,	
		 _eventList : {
			"keyup" : function(itemId,msg,params){
				$("#"+itemId).val($("#"+itemId).val().replace(/[^A-Za-z,\s]+$/g,''));
			},
			"keydown" :function(){
				
			}
		 },
		 _Validator : function(itemId,msg,params){
				 var py_name=$.trim($("#"+itemId).val());
				 var _cs=/^[A-Za-z,\s]+$/;
				 if(!_cs.test(py_name)){
					return "姓名拼音只能有字母和空格！";
				 }
				 return true;
		 }
	},
	/*固定格式校验*/
	"RegularValidator" : {
		 _eventList : {
		 },
		 _Validator : function(itemId, msg, item){

			 var _val = $.trim($("#"+itemId).val());
			 if(_val){
				var reg = eval($("#"+itemId).attr("data-regular"));
				var _result = new RegExp(reg).test(_val);
				if(!_result){
					return msg;
				}
			 }
			return true;
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
			return true;
		 }		
	},
	"AssociatedValidator" : {
		_eventList : {
			"click" : function(itemId,msg,params,item,$this){
				$inputObject = $("#" + itemId);
				eval(msg);
			},
			"blur" : function(itemId,msg,params,item,$this){
				eval(msg);
			}
		},
		_Validator : function(itemId,msg,params,item){
			return true;
		}
	},
	"DHLineValidator" : {
		_eventList : {
		},
		_Validator : function(itemId,msg,item){
			if(item.hasOwnProperty("children")){
				lines = parseInt(item.children.length);
				maxlines = parseInt(item.maxLines);
				minlines = parseInt(item.minLines);

				if(lines < minlines || lines > maxlines){
					return msg;
				}
			}
			return true;
		}
	}
};



/*
 *  编辑页面的引擎，报名表页面的引擎，编辑页面的保存，报名表页面的保存 
 *  控件案例：1、标准控件  - 日期控件， 2、标准控件 - 单行文本， 3、常用控件 - 英文字母
 *  校验
 **/