/*====================================================================
 + 功能描述：多行文本框控件，可控制文本框显示大小及输入字符范围	++
 + 开发人：张浪							++
 + 开发日期：2015-04-30						++
 =====================================================================*/
SurveyBuild.extend("MultilineTextBox", "baseComponent", {
	itemName: "多行文本框",
	title:"多行文本框",
	isCheckStrLen: "",
	rows:6,
	cols:45,
	minLen: "0",
	maxLen: "300",
	isShowLabel:"Y",
	"StorageType":"L",

	_getHtml: function(data, previewmode) {
		var c = "";

		if (previewmode) {
			SurveyBuild.appInsId == "0" && this._getDefaultVal(data);

			var regular = "";
			if(data.preg && SurveyBuild._preg.hasOwnProperty(data.preg)){
				regular = SurveyBuild._preg[data.preg]["regExp"];
			}

			c += '<div>';
			if(data.isShowLabel == "Y") {
				c += '	<div class="main_inner_content_info_autoheight_title">';
				c += '		<span class="reg_title_star">' + (data.isRequire == "Y" ? "*" : "") + '</span>' + data.title;
				c += '	</div>';
			}
			c += '	<div class="main_inner_content_info_autoheight_d height-lineHeight-36px" >';
			c += '          <div id="' + data.itemId + 'Tip" class="onShow" style="margin: 0px; padding: 0px; background: transparent;">';
			c += '              <div class="onShow"></div>';
			c += '          </div>';
			c += '	</div>';
			c += '	<div class="main_inner_content_info_autoheight_real">';
			c += '		<div class="main_inner_content_info_text_mid">';
			c += '			<textarea  class="main_text_area" data-regular="' + regular + '" title="' + data.itemName + '" style="height: 140px; white-space: pre-wrap;" rows="' + data.rows + '" cols="'+ data.cols +'" id="' + data.itemId + '" name="' + data.itemId + '">' + data.value + '</textarea>';
			c += '		</div>';
			c += '		<div class="main_inner_content_info_text_foot"></div>';
			c += '	</div>';
			c += '</div>';
		} else {
			c =  '<div class="question-answer">';
			c += '	<div class="format ' + (data["rows"] ? "rows" + data.rows: "4") + '">';
			c += '		<b class="read-input"></b>';
			c += '	</div>';
			c += '</div>';
		}

		return c;
	},
	_edit: function(data) {
		var _fix = "",e = "";

		$.each(SurveyBuild._preg, function(reg, regObj) {
			_fix += '<option value="' + reg + '"' + (data.preg == reg ? ' selected="selected"': "") + ">" + regObj["name"] + "</option>"
		});

		e = '<div class="edit_item_warp">';
        e += '  <span class="edit_item_label">行：</span>';
        e += '  <input type="text" class="medium" onchange="SurveyBuild.saveAttr(this,\'rows\')" id="rows" value="' + data.rows + '"/>';
        e += '</div>';
		e += '<div class="edit_item_warp">';
        e += '  <span class="edit_item_label">列：</span>';
        e += '  <input type="text" class="medium" onchange="SurveyBuild.saveAttr(this,\'cols\')" id="cols" value="' + data.cols + '"/>';
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
		e += '		<input class="mbIE" type="checkbox" onchange="SurveyBuild.saveAttr(this,\'isShowLabel\')"' + (data.isShowLabel == "Y" ? "checked='checked'": "") + ' id="is_showLabel"> <label for="is_showLabel">显示Label</label>';
		e += '	</div>';
		e += '	<div class="edit_item_warp">';
		e += '		<input class="mbIE" type="checkbox" onchange="SurveyBuild.saveAttr(this,\'isCheckStrLen\')"' + (data.isCheckStrLen == "Y" ? "checked='checked'": "") + 'id="is_checkstrlen" class="edit_checkStrLen"> <label for="is_checkstrlen">字数范围</label>';
		e += '	</div>';
		e += '	<div class="edit_item_warp">';
		e += '		<span class="edit_item_label">最少：</span>';
		e += '		<input type="text" class="medium minLen" data_id="' + data.instanceId + '" onkeyup="SurveyBuild.saveAttr(this,\'minLen\')" value="' + data.minLen + '" style="ime-mode: disabled;" />';
		e += '	</div>';
		e += '	<div class="edit_item_warp mb10">';
		e += '		<span class="edit_item_label">最多：<a href="#" data-for-id="help_maxLen" onclick="SurveyBuild.showMsg(this,event)" class="big-link" data-reveal-id="myModal" data-animation="fade">(?)</a></span>';
		e += '		<input type="text" class="medium maxLen" data_id="' + data.instanceId + '" onkeyup="SurveyBuild.saveAttr(this,\'maxLen\')" value="' + data.maxLen + '" style="ime-mode: disabled;" />';
		e += '	</div>';
		e += '	<div class="edit_item_warp mb10" style="display:none">';
		e += '		<span class="edit_item_label" >校验规则：</span>';
		e += '		<select id="is_preg" style="width:200px;" onchange="SurveyBuild.saveAttr(this,\'preg\')">';
		e += '			<option></option>' + _fix;
		e += '		</select>';
		e += '	</div>';
		
		e += '	</div>';
		
		e += '	<div class="edit_item_warp">';
		e += '		<a href="javascript:void(0);" onclick="SurveyBuild.RulesSet(this);"><i class="icon-cogs"></i> 高级设置</a>';
		e += '	</div>';
		e += '</div>';

		return e;
	},
	_validatorAttr: function(data){
		var $min = $("#question-edit .minLen");
		var $max = $("#question-edit .maxLen");
		var msg = "";
		if (isNaN(data.minLen)){
			SurveyBuild.fail($min, "请填写数字！");
			return false;
		}
		if (isNaN(data.maxLen)){
			SurveyBuild.fail($max, "请填写数字！");
			return false;
		}
		if (data.isCheckStrLen == "Y"){
			if (data.minLen == ""){
				SurveyBuild.fail($min, "请填写最少值！");
				return false;
			}
			if (data.maxLen == ""){
				SurveyBuild.fail($max, "请填写最多值！");
				return false;
			}
			if (data.minLen && data.maxLen){
				if (data.minLen <= data.maxLen){
					return true;
				}else{
					SurveyBuild.fail($max, "最大值不能小于最少值！");
					return false;
				}
			}
		} else {
			return true;
		}
	},

	_eventbind:function(data){
		var $inputBox = $("#" + data.itemId);

		//判断是否为必填
		var allowEmpty = true;
		var errorMsg = "&nbsp;";
		if (data.isRequire == "Y"){
			allowEmpty = false;
			errorMsg = MsgSet["REQUIRE"];
		}
		
		$inputBox.formValidator({tipID:(data["itemId"]+'Tip'), onShow:"&nbsp;", onFocus:"&nbsp;", onCorrect:"&nbsp;"});
		$inputBox.functionValidator({
			fun:function(val,elem){
				
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
				return _result;
			}
		});
		
	}
});