/*====================================================================
 + 功能描述：英文字母控件，只能输入英文字母和空格					++
 + 开发人：张浪														++
 + 开发日期：2015-05-04												++
 =====================================================================*/
SurveyBuild.extend("EnglishAlphabet", "baseComponent", {
	itemName: "英文字母",
	title: "英文字母",
	isCheckStrLen: "N",
	"StorageType": "S",
	minLen: "0",
	maxLen: "60",

	_getHtml: function(data, previewmode) {
		var c = "";
		if (previewmode) {
			if(SurveyBuild._readonly){
				//只读模式
				c += '<div class="input-list">';
				c += '	<div class="input-list-info left"><span class="red-star">' + (data.isRequire == "Y" ? "*": "") + '</span>' + data.title + '</div>';
				c += '  <div class="input-list-text left">' + data.value + '</div>';
				c += '  <div class="input-list-suffix left"></div>';
				c += '  <div class="clear"></div>';
				c += '</div>';
			}else{
				//填写模式
				var regular = "/^[A-Za-z,\\s]+$/";
				SurveyBuild.appInsId == "0" && this._getDefaultVal(data);
					
				c += '<div class="input-list">';
				c += '	<div class="input-list-info left"><span class="red-star">' + (data.isRequire == "Y" ? "*": "") + '</span>' + data.title + '</div>';
				c += '  <div class="input-list-text left"><input type="text" class="inpu-list-text-enter" id="' + data.itemId + '" name="' + data.itemId + '" value="' + data.value + '" title="' + data.itemName + '" data-regular="' + regular + '"/></div>';
				c += '  <div class="input-list-suffix left"><div id="' + data.itemId + 'Tip" class="onShow"><div class="onShow"></div></div></div>';
				c += '  <div class="clear"></div>';
				c += '</div>';
			}
		} else {
			c += '<div class="question-answer">';
			c += '	<div class="format">';
			c += '		<b class="read-input"></b>';
			c += '		<span class="suffix">' + (data["suffix"] ? data.suffix: "") + '</span>';
			c += '	</div>';
			c += '</div>'
		}
		return c;
	},

	_edit: function(data) {
		var e = "";
		e += '<div class="edit_item_warp">';
		e += '	<span class="edit_item_label">后缀：<a href="#" data-for-id="help_suffix" onclick="SurveyBuild.showMsg(this,event)" class="big-link" data-reveal-id="myModal" data-animation="fade">(?)</a></span>';
		e += '	<input type="text" class="medium" onkeyup="SurveyBuild.saveAttr(this,\'suffix\')" value="' + data.suffix + '"/>';
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

		e += '	<div class="edit_item_warp">';
		e += '		<input class="mbIE" type="checkbox" onchange="SurveyBuild.saveAttr(this,\'isCheckStrLen\')"' + (data.isCheckStrLen == "Y" ? "checked='checked'": "") + ' class="edit_checkStrLen" id="is_checkStrLen"> <label for="is_checkStrLen">字数范围</label>';
		e += '	</div>';

		e += '	<div class="edit_item_warp">';
		e += '		<span class="edit_item_label">最少：</span>';
		e += '		<input type="text" maxlength="11" class="medium minLen" data_id="' + data.instanceId + '" onkeyup="SurveyBuild.saveAttr(this,\'minLen\')" value="' + data.minLen + '" style="ime-mode: disabled;"/>';
		e += '	</div>';

		e += '	<div class="edit_item_warp mb10">';
		e += '		<span class="edit_item_label">最多：</span>';
		e += '		<input type="text" maxlength="11" class="medium maxLen" data_id="' + data.instanceId + '" onkeyup="SurveyBuild.saveAttr(this,\'maxLen\')" value="' + data.maxLen + '" style="ime-mode: disabled;"/>';
		e += '	</div>';
		e += '</div>';

		e += '	<div class="edit_item_warp">';
		e += '		<a href="javascript:void(0);" onclick="SurveyBuild.RulesSet(this);"><i class="icon-cogs"></i> 高级设置</a>';
		e += '	</div>';
		e += '</div>';

		return e;
	},
	//校验最少、最多在字数范围勾选时必填
	_validatorAttr: function(data) {
		var $minLen = $("#question-edit .minLen");
		var $maxLen = $("#question-edit .maxLen");
		var msg = "";
		if (isNaN(data.minLen)) {
			SurveyBuild.fail($minLen, "请填写数字！");
			return false;
		}
		if (isNaN(data.maxLen)) {
			SurveyBuild.fail($maxLen, "请填写数字！");
			return false;
		}
		if (data.isCheckStrLen == "Y") {
			if (data.minLen == "") {
				SurveyBuild.fail($minLen, "请填写最少值！");
				return false;
			}
			if (data.maxLen == "") {
				SurveyBuild.fail($maxLen, "请填写最多值！");
				return false;
			}
			if (data.minLen && data.maxLen) {
				if (data.minLen <= data.maxLen) {
					return true;
				} else {
					SurveyBuild.fail($maxLen, "最大值不能小于最少值！");
					return false;
				}
			}
		} else {
			return true;
		}
	},

	_eventbind: function(data) {
		var $inputBox = $("#" + data.itemId);
		$inputBox.formValidator({tipID: (data["itemId"] + 'Tip'),onShow: "",onFocus: "&nbsp;",onCorrect: "&nbsp;"});
		$inputBox.functionValidator({
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
								_result = _ruleClass._Validator(data["itemId"], classObj["messages"], data);
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
		})

	}
});