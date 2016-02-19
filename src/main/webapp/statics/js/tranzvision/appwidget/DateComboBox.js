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
			if(SurveyBuild._readonly){
				//只读模式

				c += '<div class="main_inner_content_info_autoheight cLH">';
				c += '  <div class="main_inner_connent_info_left">';
				c += '      <span class="reg_title_star">' + (data.isRequire == "Y" ? "*": "") + '</span>' + data.title;
				c += '  </div>';
				c += '  <div class="main_inner_content_info_right" >';
				c += '		<span>' + children[0]["value"] + '</span>';
				c += '		&nbsp;&nbsp;&nbsp;&nbsp;<span style="' + (data.todatebz == "Y" && children[2]["value"] == "Y" ? "display:none;": "") + '">' + children[1]["value"] + '</span>';
				if(data.hasOwnProperty("todatebz") && data.todatebz == "Y"){
					c += '		<div class="tz_checkbox_div '+(data.todatebz == "Y" && children[2]["value"] == "Y" ? "on_check": "")+'" style="margin: 0px;position: relative;">';
					c += '			<input class="tz_radio_checkbox" type="checkbox"  ' + (data.todatebz == "Y" && children[2]["value"] == "Y" ? "checked='checked'": "") + ' style="width:20px;height:20px;' + (data.todatebz != "Y" ? "display:none;": "") + '"/>' ;
					c += '      </div>';
					c += '     <span style="position: relative;">'+ (data.todatebz == "Y" ? MsgSet["TODATE"]: "") +'</span>';
				}
				c += '	</div>';
				c += '</div>'
			}else{
				//填写模式
				SurveyBuild.appInsId == "0" && this._getDefaultVal(data,"P1");
				SurveyBuild.appInsId == "0" && this._getDefaultVal(data,"P2");

				c += '<div class="main_inner_content_info_autoheight">';
				c += '	<div class="main_inner_connent_info_left">';
				c += '		<span class="reg_title_star">' + (data.isRequire == "Y" ? "*": "") + '</span>' + data.title;
				c += '	</div>';
				c += '	<div class="main_inner_content_info_right">';
				c += '		<input name="' + data["itemId"] + children[0]["itemId"] + '" type="text" class="input_120px" id="' + data["itemId"] + children[0]["itemId"] + '" readonly="readonly" onchange="SurveyBuild.reFocus(\'' + data["itemId"] + children[0]["itemId"] + '\');" value ="' + children[0]["value"] + '"/>';
				c += '		<input ' + (data.todatebz == "Y" && children[2]["value"] == "Y" ? "disabled=\"disabled\"": "") + ' style="' + (data.todatebz == "Y" && children[2]["value"] == "Y" ? "display:none;": "") + '" name="' + data["itemId"] + children[1]["itemId"] + '" type="text" class="input_120px" id="' + data["itemId"] + children[1]["itemId"] + '" readonly="readonly" onchange="SurveyBuild.reFocus(\'' + data["itemId"] + children[1]["itemId"] + '\');" value="' + children[1]["value"] + '" />';

				c += '		<img id="' + data["itemId"] + children[0]["itemId"] + '_Btn" src="' + TzUniversityContextPath + '/statics/images/appeditor/calendar.png" style="position:relative;left:'+(data.todatebz == "Y" && children[2]["value"] == "Y" ? "-39px" : "-171px")+';cursor:pointer;">';
				c += '		<img id="' + data["itemId"] + children[1]["itemId"] + '_Btn" src="' + TzUniversityContextPath + '/statics/images/appeditor/calendar.png" style="position:relative;left:-69px;cursor:pointer;'+(data.todatebz == "Y" && children[2]["value"] == "Y" ? "display:none;": "")+'">';
				if(data.hasOwnProperty("todatebz") && data.todatebz == "Y"){
					c += '		<div class="tz_checkbox_div '+(data.todatebz == "Y" && children[2]["value"] == "Y" ? "on_check": "")+'" style="margin: 0px;position: relative; left:'+(data.todatebz == "Y" && children[2]["value"] == "Y" ? "-30px" : "-60px")+'">';
					c += '			<input class="tz_radio_checkbox" type="checkbox"  ' + (data.todatebz == "Y" && children[2]["value"] == "Y" ? "checked='checked'": "") + 'id="' + data["itemId"] + children[2]["itemId"] + '" name="' + data["itemId"] + children[2]["itemId"] + '"   style="width:20px;height:20px;' + (data.todatebz != "Y" ? "display:none;": "") + '"/>' ;
					c += '      </div>';
					//c += '	<label for="' + data["itemId"] + children[2]["itemId"] + '" style="margin: 0px;position: relative; left:'+(data.todatebz == "Y" && children[2]["value"] == "Y" ? "-30px" : "-60px")+'"></label>';
					c += '     <span style="position: relative; left:'+(data.todatebz == "Y" && children[2]["value"] == "Y" ? "-28px" : "-58px")+'">'+ (data.todatebz == "Y" ? MsgSet["TODATE"]: "") +'</span>';
				}
				c += '		<div style="margin-top:-40px;margin-left:320px">';
				c += '			<div id="' + data["itemId"] + children[0]["itemId"] + 'Tip" class="onShow" ><div class="onShow"></div></div>';
				c += '		</div>';
				c += '	</div>';
				c += '</div>'
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
				
		e += '<div class="edit_item_warp"><span class="edit_item_label">年份最小值：</span>  <input type="text" class="medium minYear" data_id="' + data.instanceId + '" onkeyup="SurveyBuild.saveAttr(this,\'minYear\')" value="' + data.minYear + '"/></div>';
		e += '<div class="edit_item_warp"><span class="edit_item_label">年份最大值：</span>  <input type="text" class="medium maxYear" data_id="' + data.instanceId + '" onkeyup="SurveyBuild.saveAttr(this,\'maxYear\')" value="' + data.maxYear + '"/></div>';

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
		var $dateInputStart = $("#" + data["itemId"] + data.children[0]["itemId"]);
		var $dateInputEnd = $("#" + data["itemId"] + data.children[1]["itemId"]);
		var $todayCheckbox = $("#" + data["itemId"] + data.children[2]["itemId"]);
		
		var $checkboxDev = $todayCheckbox.closest(".tz_checkbox_div");
		var $toDateDesc = $checkboxDev.next("span");
		
		var $dateImgStart = $("#" + data["itemId"] + data.children[0]["itemId"] + "_Btn");
		var $dateImgEnd = $("#" + data["itemId"] + data.children[1]["itemId"] + "_Btn");
		
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
            }
		});
		$todayCheckbox.click(function() {
			if ($(this).prop("checked")) {
				$dateInputEnd.hide();
				$dateImgEnd.hide();
				$dateInputEnd.attr("disabled","disabled").unFormValidator(true);
				$dateImgStart.css("left","-39px");
				$checkboxDev.css("left","-30px");
				$toDateDesc.css("left","-28px");
				$(this).closest(".tz_checkbox_div").addClass("on_check");
			} else {
				$dateInputEnd.show();
				$dateImgEnd.show();
				$dateInputEnd.removeAttr("disabled").unFormValidator(false);
				$dateImgStart.css("left","-171px");
				$checkboxDev.css("left","-60px");
				$toDateDesc.css("left","-58px");
				$(this).closest(".tz_checkbox_div").removeClass("on_check");
			}
		});
		
		$dateImgStart.click(function(e) {
            $dateInputStart.focus();
        });
		
		$dateImgEnd.click(function(e) {
            $dateInputEnd.focus();
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