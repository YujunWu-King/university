/**
 * Created by admin on 2015/6/19.
 * 下拉框
 */
SurveyBuild.extend("Select", "baseComponent", {
	itemName: "下拉框",
	title: "下拉框",
	"StorageType": "S",
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
				weight: 0
			}
		}
	},
	_getHtml: function(data, previewmode) {
		var c = "",
		e = "";

		if (previewmode) {
			if(SurveyBuild.accessType == "M"){
				if (SurveyBuild._readonly) {
					//只读模式
					
	                e = '<option value="">' + MsgSet["PLEASE_SELECT"] + '</option>';
	                for (var i in data.option) {
	                    e += '<option ' + (data.value == data["option"][i]["code"] ? "selected='selected'": "") + 'value="' + data["option"][i]["code"] + '">' + data["option"][i]["txt"] + '</option>';
	                }
		            c += '<div class="item">';
		            c += '	<p>'+data.title+'<span>'+ (data.isRequire == "Y" ? "*": "") +'</span></p>';
		            c += '	<div class="text-box">';
		            c +='		<select name="' + data.itemId + '" class="select1" id="' + data.itemId + '"  title="' + data.itemName + '" disabled>';
		            c +=			e;
		            c +='		</select>';
		            c += '	</div>';
		            if ($.trim(data.onShowMessage) != "") {
					c += '	<p style="color:#666;font-size:0.56rem;">' + data.onShowMessage + '</p>';
					}
		            c += '</div>';
				}else{
					SurveyBuild.appInsId == "0" && this._getDefaultVal(data);

	                e = '<option value="">' + MsgSet["PLEASE_SELECT"] + '</option>';
	                for (var i in data.option) {
	                    e += '<option ' + (data.value == data["option"][i]["code"] ? "selected='selected'": "") + 'value="' + data["option"][i]["code"] + '">' + data["option"][i]["txt"] + '</option>';
	                }
	                c += '<div class="item">';
		            c += '	<p>'+data.title+'<span>'+ (data.isRequire == "Y" ? "*": "") +'</span></p>';
		            c += '  <div id="' + data.itemId + 'Tip" class="tips" style="display: none;"><i></i><span></span></div>';
		            c += '	<div class="text-box">' ;
		            c +='		<select name="' + data.itemId + '" class="select1" id="' + data.itemId + '"  title="' + data.itemName + '">';
		            c +=			e;
		            c +='		</select>';
		            c += '	</div>';
		            if ($.trim(data.onShowMessage) != "") {
					c += '	<p style="color:#666;font-size:0.56rem;">' + data.onShowMessage + '</p>';
					}
		            c += '</div>';
				}
			}else{
				if (SurveyBuild._readonly) {
					//只读模式
					 var valDesc = "";
	                e = '<option value="">' + MsgSet["PLEASE_SELECT"] + '</option>';
	                for (var i in data.option) {
	                    e += '<option ' + (data.value == data["option"][i]["code"] ? "selected='selected'": "") + 'value="' + data["option"][i]["code"] + '">' + data["option"][i]["txt"] + '</option>';
	                }
					for (var i in data.option) {
						if (data.value == data["option"][i]["code"]) {
							valDesc = data["option"][i]["txt"];
						}
					}
	                /*c += '<select name="' + data.itemId + '" id="' + data.itemId + '" style="width:100%;display:none;" title="' + data.itemName + '">';
	                c +=  e;
	                c += '</select>';  */
					c += '<div class="input-list">';
					c += '	<div class="input-list-info left"><span class="red-star">' + (data.isRequire == "Y" ? "*": "") + '</span>' + data.title + '</div>';
					c += '  <div class="input-list-text left">' + valDesc ;
					c += '  <input id="' + data.itemId + '" type="hidden" name="" value="" readonly="" disabled="">';
					c += '  </div>';
					c += '  <div class="input-list-suffix left"></div>';
					c += '  <div class="clear"></div>';
					c += '</div>';
				} else {
					SurveyBuild.appInsId == "0" && this._getDefaultVal(data);

	                e = '<option value="">' + MsgSet["PLEASE_SELECT"] + '</option>';
	                for (var i in data.option) {
	                    e += '<option ' + (data.value == data["option"][i]["code"] ? "selected='selected'": "") + 'value="' + data["option"][i]["code"] + '">' + data["option"][i]["txt"] + '</option>';
	                }
					c += '<div class="input-list">';
					c += '	<div class="input-list-info left"><span class="red-star">' + (data.isRequire == "Y" ? "*": "") + '</span>' + data.title + '</div>';
					c += '    <div class="input-list-text left input-edu-select">';
	                c += '          <select name="' + data.itemId + '" class="chosen-select" id="' + data.itemId + '" style="width:100%;" title="' + data.itemName + '">';
	                c +=                    e;
	                c += '          </select>';
					c += '    </div>';
					c += '    <div class="input-list-suffix left">' + (data["suffix"] ? data.suffix + '<span class="input-list-suffix-span">&nbsp;</span>': "")  + '<div id="' + data.itemId + 'Tip" class="onShow"><div class="onShow"></div></div></div>';
					c += '    <div class="clear"></div>';
					c += '</div>';

					if ($.trim(data.onShowMessage) != "") {
						c += '<div class="input-list-blank" id="' + data.itemId + 'msg">';
						c += '	<div class="input-list-info-blank left"><span class="red-star"></div>';
						c += '	<div class="input-list-wz left"><span class="blue">' + data.onShowMessage + '</span></div>';
						c += '	<div class="input-list-suffix-blank left"></div>';
						c += '	<div class="clear"></div>';
						c += '</div>';
					}
				}
			}
			
		} else {
			for (var i in data.option) {
				e += '<li id="o' + i + '">' + data["option"][i]["txt"] + '</li>';
			}
			c += '<div class="question-answer">';
			c += '      <b class="read-select"> - 请选择 - </b>';
			c += '      <ul class="select-box">' + e + '</ul>';
			c += '</div>'
		}
		return c;
	},
	_edit: function(data) {
		var e = '',
		list = "";
		for (var i in data.option) {
			list += '<tr class="read-radio" data-id="' + data.instanceId + '-' + i + '">';
			list += '<td><input type="checkbox" onchange="$(\'.defaultval\').not(this).prop(\'checked\',false);SurveyBuild.saveLevel1Attr(this,\'defaultval\')" class="defaultval" ' + (data["option"][i]["defaultval"] == "Y" ? "checked='checked'": "") + ' value="1"></td>';
			list += '<td><input type="text" onkeyup="SurveyBuild.saveLevel1Attr(this,\'code\')" value="' + data["option"][i]["code"] + '" oncontextmenu="return false;" ondragenter="return false" onpaste="return false" class="ocode"></td>';
			list += '<td><input type="text" onkeyup="SurveyBuild.saveLevel1Attr(this,\'txt\')" value="' + data["option"][i]["txt"] + '" oncontextmenu="return false;" ondragenter="return false" class="option-txt"></td>';
			list += '<td><a onclick="SurveyBuild.plusOption(this);return false;" class="text-success" href="javascript:void(0);"><i class="icon-plus-sign"></i> </a><a onclick="SurveyBuild.minusOption(this);return false;" class="text-warning" href="javascript:void(0);"><i class="icon-minus-sign"></i> </a><a href="javascript:void(0);" class="text-info option-move"><i class="icon-move"></i> </a></td>';
			list += '</tr>';
		}
		e = '<div class="edit_item_warp"><span class="edit_item_label">提示信息：</span>  <input type="text" class="medium" onkeyup="SurveyBuild.saveAttr(this,\'onShowMessage\')" value="' + data.onShowMessage + '"/></div>';
		
		e += '<div class="edit_item_warp">';
		e += '  <span class="edit_item_label">后缀：<a href="#" data-for-id="help_suffix" onclick="SurveyBuild.showMsg(this,event)" class="big-link" data-reveal-id="myModal" data-animation="fade">(?)</a></span>';
		e += '  <input type="text" class="medium" onkeyup="SurveyBuild.saveAttr(this,\'suffix\')" value="' + data.suffix + '"/>';
		e += '</div>';
		
		e += '<fieldset id="option-box">';
		e += '  <span class="edit_item_label titlewidth"><i class="icon-list-alt"></i> 选项设置</span>';
		e += '  <table class="table table-bordered data-table">';
		e += '      <thead>';
		e += '          <tr>';
		e += '              <th class="thw">默认</th>';
		e += '              <th>名称</th>';
		e += '              <th class="alLeft">描述<button onclick="SurveyBuild.optionBatch(\'' + data.instanceId + '\')" class="btn btn-primary btn-mini pull-right">批量编辑</button></th>';
		e += '              <th width="45">操作</th>';
		e += '          </tr>';
		e += '      </thead>';
		e += '      <tbody class="ui-sortable">' + list + '</tbody>';
		e += '  </table>';
		e += '</fieldset>';
		
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
		e += '    <span class="title"><i class="icon-cog"></i> 校验规则</span>';
		e += '    <div class="groupbox">';
		e += '    <div class="edit_item_warp" style="margin-top:5px;">';
		e += '        <input class="mbIE" type="checkbox" onchange="SurveyBuild.saveAttr(this,\'isRequire\')"' + (data.isRequire == "Y" ? "checked='checked'": "") + ' id="is_require"> <label for="is_require">是否必填</label>';
		e += '    </div>';
		e += '    </div>';

		e += '    <div class="edit_item_warp">';
		e += '        <a href="javascript:void(0);" onclick="SurveyBuild.RulesSet(this);"><i class="icon-cogs"></i> 高级设置</a>';
		e += '    </div>';
		e += '</div>';

		return e;
	},
	_eventbind: function(data) {
		var $inputBox = $("#" + data.itemId);

		$inputBox.formValidator({tipID: (data["itemId"] + 'Tip'),onShow: "",onFocus: "&nbsp;",onCorrect: "&nbsp;"});
		$inputBox.functionValidator({
			fun: function(val, elem) {

				//执行高级设置中的自定义规则
				/*********************************************\
				 ** 注意：自定义规则中不要使用formValidator **
				\*********************************************/
				var _result = true;
				if (ValidationRules) {
					$.each(data["rules"],
					function(classname, classObj) {
						//单选钮不需要在高级规则中的必选判断
						if ($.inArray(classname, SurveyBuild._baseRules) == -1 && data["rules"][classname]["isEnable"] == "Y") {
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
		/*关联项*/
		if(data.linkItems){
			var $linkitems = $("#" + data.linkItems);
			if($linkitems && $linkitems.length > 0){
				$inputBox.change(function(e) {
					$linkitems.val($inputBox.val());
					$linkitems.chosen("destroy").chosen();
					$linkitems.trigger("blur");
				});
			}
		}
	}
});