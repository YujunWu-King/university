/*====================================================================
+ 功能描述：公司性质控件，可以供选择公司性质，单选					++
+ 开发人：叶少威													++
+ 开发日期：2015-05-15												++
=====================================================================*/
SurveyBuild.extend("CompanyNature", "baseComponent", {
    itemName: "公司性质",
    title:"公司性质",
    "StorageType":"S",
    option: {},
    _init: function(d, previewmode) {
        if ($.isEmptyObject(this.option)) {
            /*如果下拉框无选项值，将初始化this.option*/
        } else {
            /*如果下拉框有选项值，直接返回*/
            return;
        }

		var companyNature = ["国有或国有控股","外商合资","外商独资",'港澳台合资','港澳台独资','民营或私有控股','政府部门','事业单位','非营利机构','其它'];
        for (var i = 1; i <= 10; ++i) {
            this.option[d + i] = {
                code: i,
                txt: companyNature[i - 1],
                orderby: i,
                defaultval: 'N',
                other: 'N',
                weight: 0
            }
        }
    },
    _getHtml: function(data, previewmode) {
        var c = "",e = "";
        if (previewmode) {
            if(SurveyBuild._readonly){
                //只读模式
                var valDesc = "";
                for (var i in data.option) {
                    if(data.value == data["option"][i]["code"]){
                        valDesc = data["option"][i]["txt"];
                    }
                }
				c += '<div class="input-list">';
				c += '	<div class="input-list-info left"><span class="red-star">' + (data.isRequire == "Y" ? "*": "") + '</span>' + data.title + '</div>';
				c += '  <div class="input-list-text left">' + valDesc + '</div>';
				c += '  <div class="input-list-suffix left"></div>';
				c += '  <div class="clear"></div>';
				c += '</div>';
            }else{
                //填写模式
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
				c += '    <div class="input-list-suffix left"><div id="' + data.itemId + 'Tip" class="onShow"><div class="onShow"></div></div></div>';
				c += '    <div class="clear"></div>';
				c += '</div>';
            }
        } else {
            for (var i in data.option) {
                e += '<li id="s' + i + '">' + data["option"][i]["txt"] + '</li>';
            }
            c += '<div class="question-answer">';
            c += '  <b class="read-select"> - 请选择 - </b>';
            c += '  <ul class="select-box">' + e + '</ul>';
            c += '</div>'
        }
        return c;
    },
    _edit: function(data) {
        var e = '',list = "";
		
        for (var i in data.option) {
            list += '<tr class="read-radio" data-id="' + data.instanceId + '-' + i + '">';
            list += '   <td>';
            list += '       <input type="checkbox" onchange="$(\'.defaultval\').not(this).prop(\'checked\',false);SurveyBuild.saveLevel1Attr(this,\'defaultval\')" class="defaultval" ' + (data["option"][i]["defaultval"] == "Y" ? "checked='checked'": "") + ' value="1">';
            list += '   </td>';
            list += '   <td>';
            list += '       <input type="text" onkeyup="SurveyBuild.saveLevel1Attr(this,\'code\')" value="' + data["option"][i]["code"] + '" oncontextmenu="return false;" ondragenter="return false" onpaste="return false" class="ocode">';
            list += '   </td>';
            list += '   <td>';
            list += '       <input type="text" onkeyup="SurveyBuild.saveLevel1Attr(this,\'txt\')" value="' + data["option"][i]["txt"] + '" oncontextmenu="return false;" ondragenter="return false" class="option-txt">';
            list += '   </td>';
            list += '   <td>';
            list += '       <a onclick="SurveyBuild.plusOption(this);return false;" class="text-success" href="javascript:void(0);"><i class="icon-plus-sign"></i> </a><a onclick="SurveyBuild.minusOption(this);return false;" class="text-warning" href="javascript:void(0);"><i class="icon-minus-sign"></i> </a><a href="javascript:void(0);" class="text-info option-move"><i class="icon-move"></i> </a>';
            list += '   </td>';
            list += '</tr>';
        }

        e += '<fieldset id="option-box">';
        e += '	<span class="edit_item_label titlewidth"><i class="icon-list-alt"></i> 可选值设置</span>';
        e += '  <table class="table table-bordered data-table">';
        e += '      <thead>';
        e += '          <tr>';
        e += '              <th class="thw">默认</th>';
        e += '              <th>值</th>';
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

        e += '<div class="edit_jygz">';
        e += '	<span class="title"><i class="icon-cog"></i> 校验规则</span>';
		e += '  <div class="groupbox">';
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

		$inputBox.formValidator({tipID:(data["itemId"]+'Tip'), onShow:"", onFocus:"&nbsp;", onCorrect:"&nbsp;"});
		$inputBox.functionValidator({
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
								_result = _ruleClass._Validator(data["itemId"], classObj["messages"]);
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