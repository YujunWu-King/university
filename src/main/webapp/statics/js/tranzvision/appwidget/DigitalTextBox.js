/*====================================================================
+ 功能描述：数字文本框控件，校验规则中规定当前信息项的最大值和最小值++
+ 开发人：张浪														++
+ 开发日期：2015-04-30												++
=====================================================================*/
SurveyBuild.extend("DigitalTextBox", "baseComponent", {
	itemName: "数字文本框",
	title: "数字文本框",
	isNumSize: "N",
	min: "0",
	//最小值
	max: "9999",
	//最大值
	digits: "0",
	//小数位数
	"StorageType": "S",

	_getHtml: function(data, previewmode) {
		var c = "";
		if (previewmode) {
			if (SurveyBuild.accessType == "M") {
				if (SurveyBuild._readonly) {
					//只读模式
					c += '<div class="item">';
					c += '	<p>'+data.title+'<span>'+(data.isRequire == "Y" ? "*": "")+'</span></p>';
					c += '	<div class="text-box"><input type="text" class="text1" ' + (data.isReadOnly == "Y" ? 'readonly="true"': '') + 'id="' + data.itemId + '" name="' + data.itemId + '" value="' + data.value + '" title="' + data.itemName + '" data-regular="' + regular + '" /></div>';
					c += '  <p style="color:#666;font-size:0.56rem;"></p>';
					c += '</div>';
				} else {
					//填写模式
					SurveyBuild.appInsId == "0" && this._getDefaultVal(data);
					var regular = "^(-?\\d+)(\\.\\d+)?$";
					c += '<div class="item">';
					c += '	<p>'+data.title+'<span>'+(data.isRequire == "Y" ? "*": "")+'</span></p>';
					c += '	<div id="' + data.itemId + 'Tip" class="tips" style="display: none;"><i></i><span></span></div>';
					c += '	<div class="text-box">';
					c += '		<input type="text" class="text1" id="' + data.itemId + '" name="' + data.itemId + '" value="' + data.value + '" title="' + data.itemName + '" data-regular="' + regular + '" />';
					c += '  </div>';
					if ($.trim(data.onShowMessage) != "") {
						c += '  <p style="color:#666;font-size:0.56rem;">'+data.onShowMessage+'</p>';
					}
					c += '</div>';
				}
			}else{
				if (SurveyBuild._readonly) {
					//只读模式
					c += '<div class="input-list">';
					c += '	<div class="input-list-info left"><span class="red-star">' + (data.isRequire == "Y" ? "*": "") + '</span>' + data.title + '</div>';
					c += '  <div class="input-list-text left">' + data.value + '</div>';
					c += '  <div class="input-list-suffix left"></div>';
					c += '  <div class="clear"></div>';
					c += '</div>';
				} else {
					//填写模式
					SurveyBuild.appInsId == "0" && this._getDefaultVal(data);
					var regular = "^(-?\\d+)(\\.\\d+)?$";

					c += '<div class="input-list">';
					c += '	<div class="input-list-info left"><span class="red-star">' + (data.isRequire == "Y" ? "*": "") + '</span>' + data.title + '</div>';
					c += '  <div class="input-list-text left"><input type="text" class="inpu-list-text-enter" id="' + data.itemId + '" name="' + data.itemId + '" value="' + data.value + '" title="' + data.itemName + '" data-regular="' + regular + '" style="width:' + data.boxWidth + 'px;"/></div>';
					c += '  <div class="input-list-suffix left">' + (data["suffix"] ? data.suffix + '<span class="input-list-suffix-span">&nbsp;</span>': "")  + '<div id="' + data.itemId + 'Tip" class="onShow"><div class="onShow"></div></div></div>';
					c += '  <div class="clear"></div>';
					c += '</div>';
					if ($.trim(data.onShowMessage) != "") {
						c += '<div class="input-list-blank" id="' + data.itemId + 'Tip">';
						c += '	<div class="input-list-info-blank left"><span class="red-star"></div>';
						c += '	<div class="input-list-wz left"><span class="blue">'+data.onShowMessage+'</span></div>';
						c += '	<div class="input-list-suffix-blank left"></div>';
						c += '	<div class="clear"></div>';
						c += '</div>';
					}
				}
			}
		} else {
			c += '<div class="question-answer">';
			c += '	<div class="format">';
			c += '		<b class="read-input"></b>';
			c += '		<span class="suffix">' + (data["suffix"] ? data.suffix: "") + '</span>';
			c += '	</div>';
			c += '</div>';
		}

		return c;
	},
	_edit: function(data) {
		var e = "";
		e += '<div class="edit_item_warp">';
		e += '  <span class="edit_item_label">提示信息：</span>';
		e += '  <input type="text" class="medium" onkeyup="SurveyBuild.saveAttr(this,\'onShowMessage\')" value="' + data.onShowMessage + '"/>';
		e += '</div>';

		e += '<div class="edit_item_warp">';
		e += '	<span class="edit_item_label">后缀：<a href="#" data-for-id="help_suffix" onclick="SurveyBuild.showMsg(this,event)" class="big-link" data-reveal-id="myModal" data-animation="fade">(?)</a></span>';
		e += '	<input type="text" class="medium" onkeyup="SurveyBuild.saveAttr(this,\'suffix\')" value="' + data.suffix + '"/>';
		e += '</div>';

		e += '<div class="edit_item_warp">';
		e += '	<span class="edit_item_label">默认值：</span>';
		e += '	<input type="text" class="medium" id="defaultval" onkeyup="SurveyBuild.saveAttr(this,\'defaultval\')" value="' + data.defaultval + '"/>';
		e += '</div>';

		e += '<div class="edit_item_warp" style="text-align:right;">';
		e += '  <a href="javascript:void(0);" onclick="SurveyBuild.DynamicBindVal()" class="">动态绑定值</a>';
		e += '</div>';
		
		e += '<div class="edit_item_warp">';
		e += '  <span class="edit_item_label">关联项：</span>';
		e += '  <input type="text" class="medium" id="linkItems" onkeyup="SurveyBuild.saveAttr(this,\'linkItems\')" value="' + data.linkItems + '"/>';
		e += '</div>';
		
		e += '<div class="edit_jygz">';
		e += '	<span class="title"><i class="icon-cog"></i> 校验规则：</span>';
		e += '  <div class="groupbox">';
		e += '	<div class="edit_item_warp" style="margin-top:5px;">';
		e += '		<input class="mbIE" type="checkbox" onchange="SurveyBuild.saveAttr(this,\'isRequire\')"' + (data.isRequire == "Y" ? "checked='checked'": "") + ' id="is_require"> <label for="is_require">是否必填：</label><a href="#" data-for-id="help_isRequire" onclick="SurveyBuild.showMsg(this,event)" class="big-link" data-reveal-id="myModal" data-animation="fade">(?)</a>';
		e += '</div>';

		e += '<div class="edit_item_warp">';
		e += '	<input class="mbIE" type="checkbox" onchange="SurveyBuild.saveAttr(this,\'isNumSize\')"' + (data.isNumSize == "Y" ? "checked='checked'": "") + ' id="is_checkNumSize" id="is_checkNumSize"> <label for="is_checkNumSize">数值范围：</label><a href="#" data-for-id="help_isNumSize" onclick="SurveyBuild.showMsg(this,event)" class="big-link" data-reveal-id="myModal" data-animation="fade">(?)</a>';
		e += '</div>';

		e += '<div class="edit_item_warp">';
		e += '	<span class="edit_item_label">最小值：</span>';
		e += '	<input type="text" maxlength="15" class="medium numSizeMin" data_id="' + data.instanceId + '" onkeyup="SurveyBuild.saveAttr(this,\'min\')" value="' + data.min + '"/>';
		e += '</div>';

		e += '<div class="edit_item_warp">';
		e += '	<span class="edit_item_label">最大值：</span>';
		e += '	<input type="text" maxlength="15"class="medium numSizeMax" data_id="' + data.instanceId + '" onkeyup="SurveyBuild.saveAttr(this,\'max\')" value="' + data.max + '"/>';
		e += '</div>';

		e += '<div class="edit_item_warp mb10">';
		e += '	<span class="edit_item_label">小数位数：</span>';
		e += '	<input type="text" maxlength="1" class="medium numDecws" data_id="' + data.instanceId + '" onkeyup="SurveyBuild.saveAttr(this,\'digits\')" value="' + data.digits + '"/>';
		e += '</div>';

		e += '</div>';

		e += '<div class="edit_item_warp">';
		e += '	<a href="javascript:void(0);" onclick="SurveyBuild.RulesSet(this);"><i class="icon-cogs"></i> 高级设置</a>';
		e += '</div>';

		return e;
	},

	_validatorAttr: function(data) {
		var $min = $("#question-edit .numSizeMin");
		var $max = $("#question-edit .numSizeMax");
		var $digits = $("#question-edit .numDecws");
		if (isNaN(data.min)) {
			SurveyBuild.fail($min, "请填写数字！");
			return false;
		}
		if (isNaN(data.max)) {
			SurveyBuild.fail($max, "请填写数字！");
			return false;
		}
		if (isNaN(data.digits)) {
			SurveyBuild.fail($digits, "请填写数字！");
			return false;
		}
		if (data.isNumSize == "Y") {
			if (data.min == "") {
				SurveyBuild.fail($min, "请填写最小值！");
				return false;
			}
			if (data.max == "") {
				SurveyBuild.fail($max, "请填写最大值！");
				return false;
			}
			if (data.digits == "") {
				SurveyBuild.fail($digits, "请填写小数位数！");
				return false;
			}
			if (parseInt(data.min, 10) <= parseInt(data.max, 10)) {
				return true;
			} else {
				SurveyBuild.fail($max, "最大值不能小于最小值！");
				return false;
			}
		} else {
			return true;
		}
	},

	_eventbind: function(data) {
		var $inputBox = $("#" + data.itemId);

		$inputBox.formValidator({tipID: (data["itemId"] + 'Tip'),onShow: "",onFocus: "&nbsp;",onCorrect: "&nbsp;"});
		$inputBox.functionValidator({
			fun: function(val, elem) {
				var digital = $.trim($inputBox.val());
				var _cs = /^(-?\d+)(\.\d+)?$/;

				if (digital && !_cs.test(digital)) {
					return "该字段只允许负数、数字、小数点！";
				}

				//执行高级设置中的自定义规则
				/*********************************************\
				 ** 注意：自定义规则中不要使用formValidator **
				\*********************************************/
				var _result = true;
				if (ValidationRules) {
					$.each(data["rules"],
					function(classname, classObj) {
						if ($.inArray(classname, SurveyBuild._baseRules) == -1 && data["rules"][classname]["isEnable"] == "Y") {
							var _ruleClass = ValidationRules[classname];
							if (_ruleClass && _ruleClass._Validator) {
								_result = _ruleClass._Validator(data["itemId"], classObj["messages"], data);
								if (_result !== true) {
									return false;
								}
							}
						}
					});
					if (_result !== true) {
						return _result;
					}
				}
			}
		});
		/*关联项*/
		if(data.linkItems){
			var $linkitems = $("#" + data.linkItems);
			if($linkitems && $linkitems.length > 0){
				$inputBox.change(function(e) {
					$linkitems.val($inputBox.val());
					$linkitems.trigger("change");
					$linkitems.trigger("blur");
				});
			}
		}
	}
});