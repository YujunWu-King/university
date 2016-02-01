/*====================================================================
 + 功能描述：出生日期和年龄控件，用于选择出生日期，联动显示年龄	++
 + 开发人：张浪													++
 + 开发日期：2015-05-04											++
 =====================================================================*/
SurveyBuild.extend("BirthdayAndAge", "baseComponent", {
	itemName: "出生日期&年龄",
	title:"出生日期&年龄",
	dateformate: "yy-mm-dd",
	minYear: "1960",
	maxYear: "2010",
	isSingleLine: "Y",
	children: [
		{
			"itemId": "com_birthday",
			"itemName": MsgSet["BIRTHDAY"],
			"title": MsgSet["BIRTHDAY"],
			"value": "",
			"StorageType": "S",
			"orderby":1,
			"classname":"DateInputBox"
		},
		{
			"itemId": "com_age",
			"itemName": MsgSet["AGE"],
			"title": MsgSet["AGE"],
			"value": "",
			"StorageType": "S",
			"orderby":2,
			"classname":"DigitalTextBox"
		}
	],
	_getHtml: function(data, previewmode) {
		var c = "";

		if (previewmode) {
			if(SurveyBuild._readonly){
				//只读模式

				c += '<div class="main_inner_content_info_autoheight cLH">';
				c += '  <div class="main_inner_connent_info_left">';
				c += '      <span class="reg_title_star">' + (data.isRequire == "Y" ? "*": "") + '</span>' + data.title;
				c += '  </div>';
				c += '  <div class="main_inner_content_info_right" >' + data.children[0]["value"] + "&nbsp;&nbsp;&nbsp;&nbsp;" + data.children[1]["value"] + '	</div>';
				c += '</div>'
			}else{
				//填写模式
				SurveyBuild.appInsId == "0" && this._getDefaultVal(data,"P1");
				c += '<div class="main_inner_content_info_autoheight">';
				c += '	<div class="main_inner_connent_info_left">';
				c += '		<span class="reg_title_star">' + (data.isRequire == "Y" ? "*": "") + '</span>' + data.title;
				c += '	</div>';
				c += '	<div class="main_inner_content_info_right">';
				c += '		<input id="' + data["itemId"] + data.children[0]["itemId"] + '" name="' + data["itemId"] + data.children[0]["itemId"] + '" type="text" value="' + data.children[0]["value"] + '"class="input_120px margin-right-0px" readonly="readonly" onclick="this.focus()" title="' + data.children[0]["itemName"] + '">&nbsp;';
				c += '		<input type="text" id="' + data["itemId"] + data.children[1]["itemId"] + '" value="' + data.children[1]["value"] + '" title="' + data.children[1]["itemName"] + '" class="input_120px"  readonly="readonly">';
				c += '		<img id="' + data["itemId"] + data.children[0]["itemId"] + '_Btn" src="/tranzvision/images/calendar.png" style="position:relative;left:-171px;cursor:pointer;">';
				c += '		<div style="margin-top:-40px;margin-left:256px">';
				c += '			<div id="' + data.itemId + 'Tip" class="onShow" style="margin: 0px; padding: 0px; background: transparent;">';
				c += '				<div class="onShow"></div>';
				c += '			</div>';
				c += '		</div>';
				c += '	</div>';
				c += '</div>'
			}
		} else {
			var format = "";
			if (data.dateformate == "mm-dd-yy") {
				format = "MM-DD-YYYY";
			} else if (data.dateformate == "mm/dd/yy") {
				format = "MM/DD/YYYY";
			} else if (data.dateformate == "yy-mm-dd") {
				format = "YYYY-MM-DD";
			} else if (data.dateformate == "yy/mm/dd") {
				format = "YYYY/MM/DD";
			} else if (data.dateformate == "dd-mm-yy") {
				format = "DD-MM-YYYY";
			} else if (data.dateformate == "dd/mm/yy") {
				format = "DD/MM/YYYY";
			}

			c += '<div class="question-answer">';
			c += '	<b class="read-input" style="min-width:120px" id="' + data.instanceId + 'Format">' + format + '</b>&nbsp;&nbsp;';
			c += '	<b class="read-add-minus-btn">-</b>';
			c += '	<b class="read-input" style="min-width:20px;">18</b>';
			c += '	<b class="read-add-minus-btn">+</b>'
			c += '</div>'
		}

		return c;
	},
	_edit: function(data) {
		var e = '';
		
		e += '<div class="edit_item_warp">';
		e += '	<span class="edit_item_label">日期格式：</span>';
		e += '	<select class="edit_DateFormat" onchange="SurveyBuild.saveAttr(this,\'dateformate\')">';
        e += '		<option value="dd-mm-yy" ' + (data.dateformate == "dd-mm-yy" ? "selected='selected'": "") + '>dd-MM-yyyy</option>';
        e += '		<option value="dd/mm/yy" ' + (data.dateformate == "dd/mm/yy" ? "selected='selected'": "") + '>dd/MM/yyyy</option>';
		e += '		<option value="mm-dd-yy" ' + (data.dateformate == "mm-dd-yy" ? "selected='selected'": "") + '>MM-dd-yyyy</option>';
		e += '		<option value="mm/dd/yy" ' + (data.dateformate == "mm/dd/yy" ? "selected='selected'": "") + '>MM/dd/yyyy</option>';
		e += '		<option value="yy-mm-dd" ' + (data.dateformate == "yy-mm-dd" ? "selected='selected'": "") + '>yyyy-MM-dd</option>';
		e += '		<option value="yy/mm/dd" ' + (data.dateformate == "yy/mm/dd" ? "selected='selected'": "") + '>yyyy/MM/dd</option>';
		e += '	</select>';
		e += '</div>';

		e += '<div class="edit_item_warp">';
		e += '	<span class="edit_item_label">年份最小值：</span>';
		e += '	<input type="text" class="medium datemin" data_id="' + data.instanceId + '" onkeyup="SurveyBuild.saveAttr(this,\'minYear\')" value="' + data.minYear + '"/>';
		e += '</div>';

		e += '<div class="edit_item_warp">';
		e += '	<span class="edit_item_label">年份最大值：</span>';
		e += '	<input type="text" class="medium datemax" data_id="' + data.instanceId + '" onkeyup="SurveyBuild.saveAttr(this,\'maxYear\')" value="' + data.maxYear + '"/>';
		e += '</div>';

        e += '<div class="edit_item_warp">';
        e += '  <span class="edit_item_label">默认值：</span>';
        e += '  <input type="text" class="medium" id="defaultval" onkeyup="SurveyBuild.saveAttr(this,\'defaultval\')" value="' + data.defaultval + '"/>';
        e += '</div>';

        e += '<div class="edit_item_warp" style="text-align:right;">';
        e += '  <a href="javascript:void(0);" onclick="SurveyBuild.DynamicBindVal()" class="">动态绑定值</a>';
        e += '</div>';

		e += '<div class="edit_jygz">';
		e += '	<span class="title"><i class="icon-cog"></i> 校验规则</span>';
		e += '  <div class="groupbox">';
		e += '	<div class="edit_item_warp" style="margin-top:5px;">';
		e += '		<input class="mbIE" type="checkbox" onchange="SurveyBuild.saveAttr(this,\'isRequire\')"' + (data.isRequire == "Y" ? "checked='checked'": "") + ' id="is_require"> <label for="is_require">是否必填</label>';
		e += '	</div>';
		e += '	</div>';
		
		e += '	<div class="edit_item_warp">';
		e += '		<a href="javascript:void(0);" onclick="SurveyBuild.RulesSet(this);"><i class="icon-cogs"></i> 高级设置</a>';
		e += '	</div>';
		e += '</div>';

		return e;
	},

	_eventbind: function(data) {
		/*出生日期字段*/
		var $birthday = $("#" + data["itemId"] + data.children[0]["itemId"]);
		var $searchBtn = $("#" + data["itemId"] + data.children[0]["itemId"] + "_Btn");

		/*年龄字段*/
		var $age = $("#" + data["itemId"] + data.children[1]["itemId"]);

		/*出生日期字段绑定日历选择框插件*/
		$birthday.datetimepicker({
			changeMonth: true,
			changeYear: true,
			showTimepicker: false,
			autoClose: true,
			yearRange: data.minYear + ":" + data.maxYear,
            maxDate: new Date(data.maxYear + "-12-31"),
			dateFormat:data.dateformate,
            onSelect: function(dateText, inst) {
                $birthday.datetimepicker( "hide" );
                $birthday.trigger("blur");
            }
		});
		
		$searchBtn.click(function(e){
            $birthday.click();
        });
		
		/*出生日期字段绑定onchange事件*/
		$birthday.change(function(e) {
			var thisYear, nowDate, bYear;
			nowDate = new Date();
			thisYear = nowDate.getFullYear();

			var birthday = $(this).val();

			if (data.dateformate == "mm-dd-yy") {
				bYear = parseInt(birthday.split("-")[2]);
			} else if (data.dateformate == "mm/dd/yy") {
				bYear = parseInt(birthday.split("/")[2]);
			} else if (data.dateformate == "yy-mm-dd") {
				bYear = parseInt(birthday.split("-")[0]);
			} else if (data.dateformate == "yy/mm/dd") {
				bYear = parseInt(birthday.split("/")[0]);
			} else if (data.dateformate == "dd-mm-yy") {
				bYear = parseInt(birthday.split("-")[2]);
			} else if (data.dateformate == "dd/mm/yy") {
				bYear = parseInt(birthday.split("/")[2]);
			}
			var age = thisYear - bYear;

			if (age > 0) {
				$age.val(age);
			} else {
				$age.val(0);
			}
		});
		$birthday.trigger("change");
		//表单控件验证

		$birthday.formValidator({tipID: data["itemId"] + 'Tip',onShow: "",onFocus: "&nbsp;",onCorrect: "&nbsp;"});
		$birthday.functionValidator({
			fun:function(val,elem){

				//执行高级设置中的自定义规则
				/*********************************************\
				 ** 注意：自定义规则中不要使用formValidator **
				 \*********************************************/
				var _result = true;
				if (ValidationRules) {
					$.each(data["rules"],function(classname, classObj) {
						//单选钮不需要在高级规则中的必选判断
						if ($.inArray(classname, SurveyBuild._baseRules) == -1 && data["rules"][classname]["isEnable"] == "Y") {
							var _ruleClass = ValidationRules[classname];
							if (_ruleClass && _ruleClass._Validator) {
								_result = _ruleClass._Validator(data["itemId"] + data.children[0]["itemId"], classObj["messages"]);
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
				return _result;
			}
		});
	},

	_validatorAttr: function(data) {
		var msg;
		var $edit_box = $("#question-edit");
		var dateMax = data.maxYear;
		var dateMin = data.minYear;
		var curYear = new Date().getFullYear();
		if (!dateMin) {
			msg = "年份最小值不能为空！";
			var $targetObj = $edit_box.find(".datemin");
			SurveyBuild.fail($targetObj, msg);
			return false;
		}
		if (!dateMax) {
			msg = "年份最大值不能为空！";
			var $targetObj = $edit_box.find(".datemax");
			SurveyBuild.fail($targetObj, msg);
			return false;
		}
		if (parseInt(dateMax) < parseInt(dateMin)) {
			msg = "年份最大值要大于年份最小值！";
			var $targetObj = $edit_box.find(".datemax");
			SurveyBuild.fail($targetObj, msg);
			return false;
		}
		if (parseInt(dateMax) > parseInt(curYear)) {
			msg = "年份最大值要小于当前年份！";
			var $targetObj = $edit_box.find(".datemax");
			SurveyBuild.fail($targetObj, msg);
			return false;
		}
		return true;
	}
});