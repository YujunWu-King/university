SurveyBuild.extend("DateComboBox", "baseComponent", {
	itemName: "日期组合框",
	title:"日期组合框",
	todatebz: "Y",
	isSingleLine: "Y",
	dateformate: "yy-mm-dd",
	"minYear": "1960",
	"maxYear": "2030",
	children: [
		{
			"itemId": "com_startdate",
			"itemName": MsgSet["STARTDATE"],
			"title":MsgSet["STARTDATE"],
			"value": "",
			"StorageType": "S",
			"orderby":1,
			"classname":"DateInputBox"
		},
		{
			"itemId": "com_enddate",
			"itemName": MsgSet["ENDDATE"],
			"title": MsgSet["ENDDATE"],
			"value": "",
			"StorageType": "S",
			"orderby":2,
			"classname":"DateInputBox"
		},
		{
			"itemId": "com_todate",
			"itemName": MsgSet["TODATE"],
			"title": MsgSet["TODATE"],
			"value": "",
			"orderby":3,
			"StorageType": "S",
			"classname":"CheckBox"
		}
	],
	_getHtml: function(data, previewmode) {

		var c = "",children = data.children;
		if (previewmode) {
			SurveyBuild.appInsId == "0" && this._getDefaultVal(data,"P1");
			SurveyBuild.appInsId == "0" && this._getDefaultVal(data,"P2");
			if(SurveyBuild.accessType == "M"){
				if(SurveyBuild._readonly){
					c += '<div class="item">';
					c += '	<p>' + data.title + '<span>' + (data.isRequire == "Y" ? "*": "") + '</span></p >';
					c += '	<div class="overhidden">';
					c += '		<div class="text-box fl" style="width:30%;">';
					c += '      	<input type="text" class="text1"  value="' + children[0]["value"] + '">';
					c += '      </div>';
					c += '       <span class="fl" style="line-height:1.5rem;color:#999;">---</span>';
					if(data.hasOwnProperty("todatebz") && data.todatebz == "Y" && children[2]["value"] == "Y"){
						c += '       <div class="text-box fl" style="width:30%; display:none">';
						c += '           <input type="text" class="text1">';
						c += '       </div>';
						c += '       <label >' + MsgSet["TODATE"] + '  </label>';
					}else{
						c += '       <div class="text-box fl" style="width:30%;">';
						c += '           <input type="text" class="text1" value=" '+children[1]["value"]+'">';
						c += '       </div>';
					}
					c += '   </div>';
					c += '</div>';
				}else{
					c += '<div class="item">';
					c += '	<p>' + data.title + '<span>' + (data.isRequire == "Y" ? "*": "") + '</span></p >';
					c += '	<div class="overhidden">';
					c += '		<div class="text-box fl" style="width:30%;">';
					c += '      	<input type="text" class="text1"  readonly="readonly" name="' + data["itemId"] + children[0]["itemId"] + '" id="' + data["itemId"] + children[0]["itemId"] + '" onchange="SurveyBuild.reFocus(\'' + data["itemId"] + children[0]["itemId"] + '\');" value ="' + children[0]["value"] + '">';
					c += '      </div>';
					c += '       <span class="fl" id="span'+data["itemId"] + children[0]["itemId"]+'"style="line-height:1.5rem;color:#999;">---</span>';
					c += '       <div class="text-box fl" style="width:30%;">';
					c += '           <input type="text" class="text1" readonly="readonly"' + (data.todatebz == "Y" && children[2]["value"] == "Y" ? "disabled=\"disabled\"": "") + ' value=" '+children[1]["value"]+'" style="' + (data.todatebz == "Y" && children[2]["value"] == "Y" ? "display:none;": "") + '" name="' + data["itemId"] + children[1]["itemId"] + '" id="' + data["itemId"] + children[1]["itemId"] + '" onchange="SurveyBuild.reFocus(\'' + data["itemId"] + children[1]["itemId"] + '\');" >';
					c += '       </div>';
					if(data.hasOwnProperty("todatebz") && data.todatebz == "Y"){
						c += '       <input type="checkbox" class="checkbox" ' + (data.todatebz == "Y" && children[2]["value"] == "Y" ? "checked='checked'": "") + ' id="' + data["itemId"] + children[2]["itemId"] + '" value=" '+children[1]["value"]+'"name="' + data["itemId"] + children[2]["itemId"] + '" style="margin-left:20px;"><label for="' + data["itemId"] + children[2]["itemId"] + '">' + MsgSet["TODATE"] + '  </label>';
					}
					c += '   </div>';
					c += '</div>';
				}
			}else{
				if(SurveyBuild._readonly){
					//只读模式
					c += '<div class="input-list">';
					c += '	<div class="input-list-info left"><span class="red">' + (data.isRequire == "Y" ? "*": "") + '</span>' + data.title + '</div>';
					c += '    <div class="input-list-text left">';

					c += '<span style="width:49%">' + children[0]["value"] + '</span>';
					if(data.hasOwnProperty("todatebz") && data.todatebz == "Y" && children[2]["value"] == "Y"){
						c += '<span style="">&nbsp;~&nbsp;' + MsgSet["TODATE"] + '</span>';
					}else{
						c += '&nbsp;~&nbsp;<span style="width: 49%;">' + children[1]["value"] + '</span>';
					}

					c += '    </div>';
					c += '    <div class="input-list-suffix left"></div>';
					c += '    <div class="clear"></div>';
					c += '</div>';
				}else{
					//填写模式
					
						
					c += '<div class="input-list">';
					c += '	<div class="input-list-info left"><span class="red">' + (data.isRequire == "Y" ? "*": "") + '</span>' + data.title + '</div>';
					c += '    <div class="input-list-text left">';
					c += '    	<input type="text" class="inpu-list-text-enter" readonly="readonly" style="width:46%" name="' + data["itemId"] + children[0]["itemId"] + '" id="' + data["itemId"] + children[0]["itemId"] + '" onchange="SurveyBuild.reFocus(\'' + data["itemId"] + children[0]["itemId"] + '\');" value ="' + children[0]["value"] + '"><img src="' + TzUniversityContextPath + '/statics/images/appeditor/new/close.png" class="input-icon" id="' + data["itemId"] + children[0]["itemId"] + '_Clear" style="right:44px' + (children[0]["value"]  == "" ? ";visibility:hidden;": "") + '"/><img id="' + data["itemId"] + children[0]["itemId"] + '_Btn" src="' + TzUniversityContextPath + '/statics/images/appeditor/new/calendar.png" class="input-icon" style="right:43px">';
					c += '    	<input type="text" class="inpu-list-text-enter" readonly="readonly" ' + (data.todatebz == "Y" && children[2]["value"] == "Y" ? "disabled=\"disabled\"": "") + ' style="width:46%;' + (data.todatebz == "Y" && children[2]["value"] == "Y" ? "display:none;": "") + '" name="' + data["itemId"] + children[1]["itemId"] + '" id="' + data["itemId"] + children[1]["itemId"] + '" onchange="SurveyBuild.reFocus(\'' + data["itemId"] + children[1]["itemId"] + '\');" value="' + children[1]["value"] + '"><img src="' + TzUniversityContextPath + '/statics/images/appeditor/new/close.png" class="input-icon" id="' + data["itemId"] + children[1]["itemId"] + '_Clear" style="right:44px' + ((data.todatebz == "Y" && children[2]["value"] == "Y") || children[1]["value"]=="" ? ";visibility:hidden;": "") + '"/><img id="' + data["itemId"] + children[1]["itemId"] + '_Btn" style="right:43px;display:' + (data.todatebz == "Y" && children[2]["value"] == "Y" ? "none;": "") + '" src="' + TzUniversityContextPath + '/statics/images/appeditor/new/calendar.png" class="input-icon">';
					if(data.hasOwnProperty("todatebz") && data.todatebz == "Y"){
						c += '<div class="check-box' + (data.todatebz == "Y" && children[2]["value"] == "Y" ? " checkedBox": "") + '"><i>';
						c += '	<input type="checkbox" ' + (data.todatebz == "Y" && children[2]["value"] == "Y" ? "checked='checked'": "") + 'id="' + data["itemId"] + children[2]["itemId"] + '" name="' + data["itemId"] + children[2]["itemId"] + '"/>';
						c += '</i></div>';
						c += (data.todatebz == "Y" ? MsgSet["TODATE"]: "");
						c += '<div id="' + data["itemId"] + children[0]["itemId"] + 'Tip" class="onShow"><div class="onShow"></div></div>';
					}
					c += '    </div>';
					c += '    <div class="input-list-suffix left">';
					if(data.hasOwnProperty("todatebz") && data.todatebz == "Y"){
					}else{
						c += '<div id="' + data["itemId"] + children[0]["itemId"] + 'Tip" class="onShow"><div class="onShow"></div></div>';
					}
					c += '	  </div>';
					c += '    <div class="clear"></div>';
					c += '</div>';
				}
				
			}
		} else {
			c += '<div class="question-answer"><div class="format ">';

			c += '	<b class="read-input" style="min-width: 120px;"></b>&nbsp;';
			c += '	<b class="read-input" style="min-width: 120px;"></b>';
			var uptoNow = '<b style="padding: 15px 0px 0px 15px; color: #666666; background: url(\"/statics/images/appeditor/read-check.gif\") no-repeat scroll -4px 4px; margin-left: 10px;"></b><span class="suffix" style="font-weight: normal;">至今</span>';

			c += (data.todatebz == "Y" ? uptoNow : '');
			c += '</div></div>'
		}
		return c;
	},
	_edit: function(data) {
		
		var e = '<div class="edit_item_warp"><input class="mbIE" type="checkbox" onchange="SurveyBuild.saveAttr(this,\'todatebz\')"' + (data.todatebz == "Y" ? "checked='checked'": "") + ' id="is_showRightNow"> <label for="is_showRightNow">显示至今</label>&nbsp;&nbsp</div>';
		
		e += '<div class="edit_item_warp"><span class="edit_item_label" >日期格式：</span>  <select class="edit_format" onchange="SurveyBuild.saveAttr(this,\'dateformate\')">';
        e += '<option value="dd-mm-yy" ' + (data.dateformate == "dd-mm-yy" ? "selected='selected'": "") + '>dd-MM-yyyy</option>';
        e += '<option value="dd/mm/yy" ' + (data.dateformate == "dd/mm/yy" ? "selected='selected'": "") + '>dd/MM/yyyy</option>';
		e += '<option value="mm-dd-yy" ' + (data.dateformate == "mm-dd-yy" ? "selected='selected'": "") + '>MM-dd-yyyy</option>';
		e += '<option value="mm/dd/yy" ' + (data.dateformate == "mm/dd/yy" ? "selected='selected'": "") + '>MM/dd/yyyy</option>';
		e += '<option value="yy-mm-dd" ' + (data.dateformate == "yy-mm-dd" ? "selected='selected'": "") + '>yyyy-MM-dd</option>';
		e += '<option value="yy/mm/dd" ' + (data.dateformate == "yy/mm/dd" ? "selected='selected'": "") + '>yyyy/MM/dd</option>';
		e += '</select></div>';
				
		e += '<div class="edit_item_warp"><span class="edit_item_label">年份最小值：</span>  <input type="text" maxlength="4" class="medium minYear" data_id="' + data.instanceId + '" onkeyup="SurveyBuild.saveAttr(this,\'minYear\')" value="' + data.minYear + '"/></div>';
		e += '<div class="edit_item_warp"><span class="edit_item_label">年份最大值：</span>  <input type="text" maxlength="4" class="medium maxYear" data_id="' + data.instanceId + '" onkeyup="SurveyBuild.saveAttr(this,\'maxYear\')" value="' + data.maxYear + '"/></div>';

        e += '<div class="edit_item_warp">';
        e += '  <span class="edit_item_label">默认值：</span>';
        e += '  <input type="text" class="medium" id="defaultval" onkeyup="SurveyBuild.saveAttr(this,\'defaultval\')" value="' + data.defaultval + '"/>';
        e += '</div>';

        e += '<div class="edit_item_warp" style="text-align:right;">';
        e += '  <a href="javascript:void(0);" onclick="SurveyBuild.DynamicBindVal()" class="">动态绑定值</a>';
        e += '</div>';

		e += '<div class="edit_jygz">';
		e += '	<span class="title"><i class="icon-cog"></i> 校验规则：</span>';
		e += '  <div class="groupbox">';
		e += '	<div class="edit_item_warp" style="margin-top:5px;">';
		e += '		<input class="mbIE" type="checkbox" onchange="SurveyBuild.saveAttr(this,\'isRequire\')"' + (data.isRequire == "Y" ? "checked='checked'": "") + ' id="is_require"> <label for="is_require">是否必填</label>&nbsp;&nbsp';
		e += '	</div>';
		e += '	</div>';
		
		e += '  <div class="edit_item_warp">';
		e += '      <a href="javascript:void(0);" onclick="SurveyBuild.RulesSet(this);"><i class="icon-cogs"></i> 高级设置</a>';
		e += '  </div>';
		e +='</div>';
		return e;
	},
	_eventbind: function(data) {
		$.fn.toggleCheckbox = function () {
			this.prop('checked', !this.prop('checked'));
		}
		
		var $dateInputStart = $("#" + data["itemId"] + data.children[0]["itemId"]);
		var $dateInputEnd = $("#" + data["itemId"] + data.children[1]["itemId"]);
		var $todayCheckbox = $("#" + data["itemId"] + data.children[2]["itemId"]);
		var $todaySpan = $("#span" + data["itemId"] + data.children[0]["itemId"]);
		var $checkboxDev = $todayCheckbox.closest(".check-box");

		var $dateImgStart = $("#" + data["itemId"] + data.children[0]["itemId"] + "_Btn");
		var $dateImgEnd = $("#" + data["itemId"] + data.children[1]["itemId"] + "_Btn");
		
		var $clearImgEnd = $("#" + data["itemId"] + data.children[1]["itemId"] + "_Clear");
		var $clearImgStart = $("#" + data["itemId"] + data.children[0]["itemId"] + "_Clear");
		
		if(SurveyBuild.accessType == "P"){
		$dateInputStart.datetimepicker({
			changeMonth: true,
			changeYear: true,
			showTimepicker: false,
			yearRange: data.minYear + ":" + data.maxYear,
            maxDate: new Date(data.maxYear + "-12-31"),
			dateFormat: data.dateformate,
            onSelect: function(dateText, inst) {
                $dateInputStart.datetimepicker( "hide" );
                $dateInputStart.trigger("blur");
                $clearImgStart.css("visibility","visible");
            }
		});
		
			$dateInputEnd.datetimepicker({
				changeMonth: true,
				changeYear: true,
				showTimepicker: false,
				yearRange: data.minYear + ":" + data.maxYear,
	            maxDate: new Date(data.maxYear + "-12-31"),
				dateFormat: data.dateformate,
	            onSelect: function(dateText, inst) {
	                $dateInputEnd.datetimepicker( "hide" );
	                $dateInputEnd.trigger("blur");
	                $clearImgEnd.css("visibility","visible");
	            }
			});
		}else if(SurveyBuild.accessType == "M"){

			//M手机
			var $type;
			switch(data.dateformate)
			{
			case 'yy/mm':
				  $type= "ym";
				  break;
			case 'yy-mm':
				  $type= "ym";
				  break;
			default:
				 $type= "date";
			}
			 var calendarStart = new LCalendar();
			 calendarStart.init({
			        'trigger': "#" + data["itemId"] + data.children[0]["itemId"], //标签id
			        'type': $type, //date 调出日期选择 datetime 调出日期时间选择 time 调出时间选择 ym 调出年月选择,
			        'minDate': data.minYear + "-01-01", //最小日期
			        'maxDate':data.maxYear + "-12-31"
			    });
		    var calendarEnd = new LCalendar();
		    calendarEnd.init({
		        'trigger': "#" + data["itemId"] + data.children[1]["itemId"], //标签id
		        'type': $type, //date 调出日期选择 datetime 调出日期时间选择 time 调出时间选择 ym 调出年月选择,
		        'minDate': data.minYear + "-01-01", //最小日期
		        'maxDate':data.maxYear + "-12-31"
		    });
		    if(!SurveyBuild._readonly){
			    $todayCheckbox.click(function(){
			    	$(this).find(':checkbox').toggleCheckbox();
			    	if ($todayCheckbox.prop("checked")) {
			    		$dateInputEnd.parent(".text-box").hide();
			    		$todaySpan.hide();
			    		$dateInputEnd.attr("disabled","disabled").unFormValidator(true);
			    	}else{
			    		$dateInputEnd.parent(".text-box").show();
			    		$todaySpan.show();
			    		$dateInputEnd.removeAttr("disabled").unFormValidator(false);
			    	}
			    });
		    }
		}
		
		if(!SurveyBuild._readonly){
			$checkboxDev.click(function(){
			    $(this).find(':checkbox').toggleCheckbox();
			    $(this).toggleClass('checkedBox');
			    
				if ($(this).find(':checkbox').prop("checked")) {
					$dateInputEnd.hide();
					$dateImgEnd.hide();
					$clearImgEnd.css("visibility","hidden");
					$dateInputEnd.attr("disabled","disabled").unFormValidator(true);
				} else {
					$dateInputEnd.show();
					$dateImgEnd.show();
					if ($dateInputEnd.val() !="") {
						 $clearImgEnd.css("visibility","visible");
					}
					$dateInputEnd.removeAttr("disabled").unFormValidator(false);
				}
			});
		}

		
		$dateImgStart.click(function(e) {
            $dateInputStart.focus();
        });
		
		$dateImgEnd.click(function(e) {
            $dateInputEnd.focus();
        });

		
		$clearImgStart.click(function(e) {
			$dateInputStart.val("");
			$clearImgStart.css("visibility","hidden");
        });
		
		$clearImgEnd.click(function(e) {
			$dateInputEnd.val("");
			$clearImgEnd.css("visibility","hidden");
        });
		
		
		$dateInputStart.formValidator({tipID:data["itemId"] + data.children[0]["itemId"] +'Tip',onShow:"",onFocus:"&nbsp;",onCorrect:"&nbsp;"});
		$dateInputStart.functionValidator({
			fun: function(val,elem) {
				//执行高级设置中的自定义规则
				/*********************************************\
				 ** 注意：自定义规则中不要使用formValidator **
				 \*********************************************/
				var _result = true;
				if (ValidationRules) {
					$.each(data["rules"],function(classname, classObj) {
						if ($.inArray(classname, SurveyBuild._baseRules) == -1 && data["rules"][classname]["isEnable"] == "Y") {
							var _ruleClass = ValidationRules[classname];
							if (_ruleClass && _ruleClass._Validator) {
								_result = _ruleClass._Validator(data["itemId"] + data.children[0]["itemId"], classObj["messages"], data);
								if(_result !== true){
									return false;
								}
							}
						}
					});
					if(_result !== true){
						return _result;
					}
				}
			}
		});

		$dateInputEnd.formValidator({tipID:data["itemId"] + data.children[0]["itemId"] +'Tip',onShow:"",onFocus:"&nbsp;",onCorrect:"&nbsp;"});
		$dateInputEnd.functionValidator({
			fun: function(val,elem) {
				//执行高级设置中的自定义规则
				/*********************************************\
				 ** 注意：自定义规则中不要使用formValidator **
				 \*********************************************/
				var _result = true;
				if (ValidationRules) {
					$.each(data["rules"],function(classname, classObj) {
						if ($.inArray(classname, SurveyBuild._baseRules) == -1 && data["rules"][classname]["isEnable"] == "Y") {
							var _ruleClass = ValidationRules[classname];
							if (_ruleClass && _ruleClass._Validator) {
								_result = _ruleClass._Validator(data["itemId"] + data.children[1]["itemId"], classObj["messages"], data);
								if(_result !== true){
									return false;
								}
							}
						}
					});
					if(_result !== true){
						return _result;
					}
				}
			}
		});
		if($dateInputEnd.attr("disabled")){
			$dateInputEnd.attr("disabled","disabled").unFormValidator(true);
		}
	}
})