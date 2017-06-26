/*====================================================================
 + 功能描述：单选框         										++
 + 开发人：王瑞立													++
 + 开发日期：2015-06-03												++
 =====================================================================*/
SurveyBuild.extend("Radio", "baseComponent", {
	itemName: "单选框",
	title: "单选框",
	format: "1",
	othervalue: "",
	"StorageType": "D",
	format:"S",
	option: {},
	_init: function(d, previewmode) {
		if ($.isEmptyObject(this.option)) {
			/*如果下拉框无选项值，将初始化this.option*/
		} else {
			/*如果下拉框有选项值，直接返回*/
			return;
		}

		for (var i = 1; i <= 3; ++i) {
			this.option[d + i] = {
				code: i,
				txt: "选项" + i,
				orderby: i,
				defaultval: 'N',
				other: 'N',
				weight: 0,
				checked: "N",
				othervalue: ''
			}
		}
	},
	_getHtml: function(data, previewmode) {
		var c = "",e = "";

		if (previewmode) {
			SurveyBuild.appInsId == "0" && this._getDefaultVal(data);
			if(SurveyBuild.accessType == "M"){
				for (var i in data.option) {
					if(SurveyBuild.appInsId == "0"){
						if(data["option"][i]["code"] == data["value"]){
							data["option"][i]["checked"] = "Y";
						}
					}
					  e += '<li >';
					  e += '	<input type="radio" instanceId="' + i + '" name="' + data.itemId + '" class="radio ' + (data["option"][i]["other"] == "Y" ? "sur_other_box": "") + '" id="o' + data.itemId + data["option"][i]["code"] + '"  value="' + data["option"][i]["code"] + '" ' + (data["option"][i]["checked"] == "Y" ? "checked='checked'": "") + '>';
				      e += '	<label for="o' + data.itemId + data["option"][i]["code"] + '">'+ data["option"][i]["txt"]+'</label>';
				      if (data["option"][i]["other"] == "Y" &&data.format != "H"){
					        if(SurveyBuild._readonly){
					        	e += '<input type="text" class="others" disabled=true value="' + data.othervalue + '" >';
					        }else{
					        	e += '<input type="text" class="others" style="display:'+(data["option"][i]["checked"] == "Y" ? "inline": "none")+'" value="' + data.othervalue + '" id="other' + data.itemId + '"  name="other' + data.itemId + '">';
					        }
						}
				      e += '</li>';
				}
				data.format = (data.format?data.format:"S");
				c += '<div class="item" data-instancid="' + data.instanceId + '">';
				c += '	<p>'+data.title+'<span>'+(data.isRequire == "Y" ? "*": "")+'</span></p>';
				c += '  <div id="' + data.itemId + 'Tip" class="tips" style="display: none;"><i></i><span></span></div>';
				if(data.format == "H"){
				c += '	<ul class="sex" id="' + data.itemId + '">'+e+'</ul>';
				}else{
				c += '	<ul class="company" id="' + data.itemId + '">'+e+'</ul>';
				}
				c += '</div>';
				}else{
				for (var i in data.option) {
					if(SurveyBuild.appInsId == "0"){
						if(data["option"][i]["code"] == data["value"]){
							data["option"][i]["checked"] = "Y";
						}
					}
					e += '<li>';
					e += '	<div class="radio-btn ' + (data["option"][i]["checked"] == "Y" ? "checkedRadio": "") + '"><i><input type="radio" name="' + data.itemId + '" instanceId="' + i + '" id="o' + data.itemId + data["option"][i]["code"] + '" value="' + data["option"][i]["code"] + '" ' + (data["option"][i]["checked"] == "Y" ? "checked='checked'": "") + ' class="' + (data["option"][i]["other"] == "Y" ? "sur_other_box": "") + '"/></i></div>';
					e += data["option"][i]["txt"];
					if (data["option"][i]["other"] == "Y" && data["option"][i]["checked"] == "Y"){
					    if(SurveyBuild._readonly){
					        //只读模式
					        e += '<input type="text" disabled=true class="inputother" value="' + data["option"][i]["othervalue"] + '">';
					    }else{
					        //编辑模式
					        e += '<input type="text" id="other' + data.itemId + '" name="other' + data.itemId + '" class="inputother" value="' + data["option"][i]["othervalue"] + '">';
					    }
					}
					e += '</li>';
				}
				data.format = (data.format?data.format:"S");
				if(data.format == "H"){
					c += '<div class="input-list input-radiobox" data-instancid="' + data.instanceId + '">';
					c += '	<div class="input-list-info left"><span class="red-star">' + (data.isRequire == "Y" ? "*": "") + '</span>' + data.title + '</div>';
					c += '		<div class="margart8 input-list-text left Custom-radio">';
					c += '			<ul id="' + data.itemId + '">' + e + '<div class="clear"></div></ul>';
					c += '		</div>';
					c += '		<div class="input-list-suffix left">';
					if(!SurveyBuild._readonly){
						c += '<div id="' + data.itemId + 'Tip" class="onShow"><div class="onShow"></div></div>';
					}
					c += '		</div>';
					c += '		<div class="clear"></div>';
					c += '</div>';
				}else{
					c += '<div class="input-list input-radiobox" data-instancid="' + data.instanceId + '">';
					c += '	<div class="input-list-info left"><span class="red-star">' + (data.isRequire == "Y" ? "*": "") + '</span>' + data.title + '</div>';
					c += '	<div class="margart8 input-list-textwrap left">';
					c += '		<ul id="' + data.itemId + '">' + e + '<div class="clear"></div></ul>';
					c += '	</div>';
					c += '	<div class="input-list-suffix left">';
					if(!SurveyBuild._readonly){
						c += '<div id="' + data.itemId + 'Tip" class="onShow"><div class="onShow"></div></div>';
					}
					c += '</div>';
					c += '	<div class="clear"></div>';
					c += '</div>';
				}

			}
			
		} else {
			for (var i in data.option) {
				e += '<li class="read-radio" id="o' + i + '">';
				e += data["option"][i]["txt"];
				if (data["option"][i]["other"] == "Y") {
					e += '<b class="read-input"></b>';
				}
				e += '</li>';
			}
			c = '<div class="question-answer"><ul class="format">' + e + '</ul></div>'
		}
		return c;
	},
	_edit: function(data) {
		var e = '',list = "";
		for (var i in data.option) {
			list += '<tr class="read-radio" data-id="' + data.instanceId + '-' + i + '">';
			//默认
			list += '<td><input type="checkbox" onchange="$(\'.defaultval\').not(this).prop(\'checked\',false);SurveyBuild.saveLevel1Attr(this,\'defaultval\')" class="defaultval" ' + (data["option"][i]["defaultval"] == "Y" ? "checked='checked'": "") + ' value="1"></td>';
			//其他
//			list += '<td><input type="checkbox" onchange="$(\'.other\').not(this).prop(\'checked\',false);SurveyBuild.saveLevel1Attr(this,\'other\');" class="other" ' + (data["option"][i]["other"] == "Y" ? "checked='checked'": "") + '  value="' + data["option"][i]["other"] + ' "></td>';
			list += '<td><input type="checkbox" onchange="$(\'.other\').not(this);SurveyBuild.saveLevel1Attr(this,\'other\');" class="other" ' + (data["option"][i]["other"] == "Y" ? "checked='checked'": "") + '  value="' + data["option"][i]["other"] + ' "></td>';
			//值
			list += '<td><input type="text" onkeyup="SurveyBuild.saveLevel1Attr(this,\'code\')" value="' + data["option"][i]["code"] + '" oncontextmenu="return false;" ondragenter="return false" onpaste="return false" class="ocode"></td>';
			//描述
			list += '<td><input type="text" onkeyup="SurveyBuild.saveLevel1Attr(this,\'txt\')" value="' + data["option"][i]["txt"] + '" oncontextmenu="return false;" ondragenter="return false" class="option-txt"></td>';
			//操作
			list += '<td><a onclick="SurveyBuild.plusOption_radio(this);return false;" class="text-success" href="javascript:void(0);"><i class="icon-plus-sign"></i> </a><a onclick="SurveyBuild.minusOption(this);return false;" class="text-warning" href="javascript:void(0);"><i class="icon-minus-sign"></i> </a><a href="javascript:void(0);" class="text-info option-move"><i class="icon-move"></i> </a></td>';
			list += '</tr>';
		}

		e += '<fieldset id="option-box">';
		e += '		<span class="edit_item_label titlewidth"><i class="icon-list-alt"></i> 选项设置</span>';
		e += '		<table class="table table-bordered data-table">';
		e += '			<thead>';
		e += '				<tr>';
		e += '					<th class="thw">默认</th>';
		e += '					<th class="thw">其他</th>';
		e += '					<th>值</th>';
		e += '					<th class="alLeft">描述<button onclick="SurveyBuild.optionBatch(\'' + data.instanceId + '\')" class="btn btn-primary btn-mini pull-right">批量编辑</button></th>';
		e += '					<th width="45">操作</th>';
		e += '				</tr>';
		e += '			</thead>';
		e += '			<tbody class="ui-sortable">' + list + '</tbody>';
		e += '		</table>';
		e += '</fieldset>';

		e += '<div class="edit_jygz">';

		e += '<div class="edit_item_warp">';
		e += '	<span class="edit_item_label">选项排列</span>';
		e += '	<select onchange="SurveyBuild.saveAttr(this,\'format\')" data-id="' + data.instanceId + '">';
		e += '		<option></option>';
		e += '		<option value="H" ' + (data.format == "H" ? "selected='selected'": "") + '>横向排列</option>';
		e += '		<option value="S" ' + (data.format == "S" ? "selected='selected'": "") + '>纵向排列</option>';
		e += '	</select>';
		e += '</div>';

		e += '<div class="edit_item_warp">';
		e += '  <span class="edit_item_label">默认值：</span>';
		e += '  <input type="text" class="medium" id="defaultval" onkeyup="SurveyBuild.saveAttr(this,\'defaultval\')" value="' + data.defaultval + '"/>';
		e += '</div>';

		e += '<div class="edit_item_warp" style="text-align:right;">';
		e += '  <a href="javascript:void(0);" onclick="SurveyBuild.DynamicBindVal()" class="">动态绑定值</a>';
		e += '</div>';
		
		e += '<div class="edit_item_warp">';
		e += '  <span class="edit_item_label">关联项：</span>';
		e += '  <input type="text" class="medium" id="linkItems" onkeyup="SurveyBuild.saveAttr(this,\'linkItems\')" value="' + data.linkItems + '"/>';
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
		e += '</div>';
		return e;
	},
	_eventbind: function(data) {
		var $inputBox = $(":radio[name='" + data.itemId + "']");
		if(SurveyBuild.accessType == "M"){
			$inputBox.on('click', function () {
				
				$inputBox.formValidator({tipID: (data["itemId"] + 'Tip'),onShow: "",onFocus: "&nbsp;",onCorrect: "&nbsp;"});
			    var _this = $(this),block = _this.parent().parent();
			    var a=block.find('input:radio');
			    block.find('input:radio').prop('checked', false);
			    block.find(".radio-btn").removeClass('checkedRadio');
			    _this.addClass('checkedRadio');
			    var b=_this.find('input:radio');
			    _this.prop('checked', true);

				var meid = _this.attr("instanceId");
				for (var j in data.option) {
					data.option[j]["checked"] = "N";
				}
				data.option[meid]["checked"] = "Y";
				if (data.option[meid]["other"] == "Y"&&data.format != "H") {
						$("#other" + data.itemId).remove();
						var o = '<input type="text" id="other' + data.itemId + '" name="other' + data.itemId + '" class="others" value="' + data.othervalue + '">';
						$(this).closest('li').append(o);
					
					$("#other" + data.itemId).keyup(function() {
						data.othervalue = $(this).val();
						data.option[meid]["othervalue"] = $(this).val();
						
						/*关联项*/
						if(data.linkItems){
							var $targetObj = $("#other" + data.linkItems);
							if($targetObj && $targetObj.length > 0){
								$targetObj.val(data.othervalue);
							}
						}
					});
				} else {
					data.othervalue = "";
					data.option[meid]["othervalue"] = "";
					$("#other" + data.itemId).remove();
				}
				
				/*关联项*/
				if(data.linkItems){
					var $targetObj = $("#o" + data.linkItems + data["option"][meid]["code"]);
					if($targetObj && $targetObj.length > 0){
						$targetObj.parents(".radio-btn").trigger("click");
						$targetObj.parents(".radio-btn").trigger("blur");
						$("#other" + data.linkItems).val($("#other" + data.itemId).val());
						$("#other" + data.linkItems).trigger("keyup");
					}
				}
				
			});
		}else{
			$inputBox.parents(".radio-btn").on('click', function () {
				
				$inputBox.formValidator({tipID: (data["itemId"] + 'Tip'),onShow: "",onFocus: "&nbsp;",onCorrect: "&nbsp;"});
			    var _this = $(this),block = _this.parent().parent();
			    block.find('input:radio').prop('checked', false);
			    block.find(".radio-btn").removeClass('checkedRadio');
			    _this.addClass('checkedRadio');
			    _this.find('input:radio').prop('checked', true);

				var meid = _this.find(':radio').attr("instanceId");
				for (var j in data.option) {
					data.option[j]["checked"] = "N";
				}
				data.option[meid]["checked"] = "Y";
				if (data.option[meid]["other"] == "Y") {
					$("#other" + data.itemId).remove();
					var o = '<input type="text" id="other' + data.itemId + '" name="other' + data.itemId + '" class="inputother" value="' + data.othervalue + '">';
					$(this).closest('li').append(o);
					$("#other" + data.itemId).keyup(function() {
						data.othervalue = $(this).val();
						data.option[meid]["othervalue"] = $(this).val();
						
						/*关联项*/
						if(data.linkItems){
							var $targetObj = $("#other" + data.linkItems);
							if($targetObj && $targetObj.length > 0){
								$targetObj.val(data.othervalue);
							}
						}
					});
				} else {
					data.othervalue = "";
					data.option[meid]["othervalue"] = "";
					$("#other" + data.itemId).remove();
				}
				
				/*关联项*/
				if(data.linkItems){
					var $targetObj = $("#o" + data.linkItems + data["option"][meid]["code"]);
					if($targetObj && $targetObj.length > 0){
						$targetObj.parents(".radio-btn").trigger("click");
						$targetObj.parents(".radio-btn").trigger("blur");
						$("#other" + data.linkItems).val($("#other" + data.itemId).val());
						$("#other" + data.linkItems).trigger("keyup");
					}
				}
				
			});
		}
		
		if(SurveyBuild._readonly){
			$inputBox.parents(".radio-btn").unbind("click");
		}

		//判断是否为必填
		var allowEmpty = true;
		var errorMsg = "&nbsp;";
		if (ValidationRules) {
			$.each(data["rules"],function(classname, classObj) {
				if ($.inArray(classname, SurveyBuild._baseRules) == -1 && data["rules"][classname]["isEnable"] == "Y") {
					//必填校验
					if (classname == "RequireValidator" && data.isRequire == "Y") {
						allowEmpty = false;
						errorMsg = classObj["messages"];
					}
				}
			});
		}
		console.log(allowEmpty);
		$inputBox.formValidator({tipID: (data["itemId"] + 'Tip'),onShow: "",onFocus: "&nbsp;",onCorrect: "&nbsp;"});
		if (!allowEmpty) {
			$inputBox.inputValidator({min: 1,onError: errorMsg});
		}
		$inputBox.functionValidator({
			fun: function(val, elem) {

				//执行高级设置中的自定义规则
				/*********************************************\
				 ** 注意：自定义规则中不要使用formValidator **
				\*********************************************/
				var _result = true;
				if (ValidationRules) {
					$.each(data["rules"],function(classname, classObj) {
						//单选钮不需要在高级规则中的必选判断
						if ($.inArray(classname, SurveyBuild._baseRules) == -1 && data["rules"][classname]["isEnable"] == "Y" && classname != "RequireValidator") {
							var _ruleClass = ValidationRules[classname];
							if (_ruleClass && _ruleClass._Validator) {
								_result = _ruleClass._Validator(data["itemId"], classObj["messages"]);
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
				return _result;
			}
		});
	}
});