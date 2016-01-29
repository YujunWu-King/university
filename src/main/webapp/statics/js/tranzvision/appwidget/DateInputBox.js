SurveyBuild.extend("DateInputBox", "SingleTextBox", {
    itemName: "日期",
    title: "日期",
    dateformate: "yy-mm-dd",
    "StorageType": "S",
    minYear: "1960",
    maxYear: "2030",

    _getHtml: function(data, previewmode) {
        var c = "";
        if (previewmode) {
            if(SurveyBuild._readonly){
                //只读模式
                c += '<div class="main_inner_content_info_autoheight cLH">';
                c += '  <div class="main_inner_connent_info_left">';
                c += '      <span class="reg_title_star">' + (data.isRequire == "Y" ? "*": "") + '</span>' + data.title;
                c += '  </div>';
                c += '  <div class="main_inner_content_info_readonly_right" >' + data.value + '</div>';
                c += '</div>'
            }else{
                //填写模式
                SurveyBuild.appInsId == "0" && this._getDefaultVal(data);
                c += '<div class="main_inner_content_info_autoheight">';
                c += '  <div class="main_inner_connent_info_left">';
                c += '      <span class="reg_title_star">' + (data.isRequire == "Y" ? "*": "") + '</span>' + data.title;
                c += '  </div>';
                c += '  <div class="main_inner_content_info_right">';
                c += '      <input id="' + data.itemId + '" name="' + data.itemId + '" type="text" value="' + data.value + '"class="input_251px " readonly="readonly" onclick="this.focus()" title="' + data.itemName + '">';
                c += '      <img id="' + data.itemId + '_Btn" src="/tranzvision/images/calendar.png" style="position:relative;left:-41px;cursor:pointer;">';
                c += '       <div style="margin-top:-42px;margin-left:256px">';
                c += '          <div id="' + data.itemId + 'Tip" class="onShow" style="margin: 0px; padding: 0px; background: transparent;">';
                c += '              <div class="onShow"></div>';
                c += '            </div>';
                c += '      </div>';
                c += '  </div>';
                c += '</div>'
            }
        } else {
            c += '<div class="question-answer">';
            c += '  <div class="format ">';
            c += '      <b class="read-input"></b>';
            c += '  </div>';
            c += '</div>';
        }
        return c;
    },
    _edit: function(data) {
        var e = '';
		
		e += '<div class="edit_item_warp">';
        e += '	<span class="edit_item_label" >日期格式：</span>';
        e += '	<select class="edit_format" onchange="SurveyBuild.saveAttr(this,\'dateformate\')">';
        e += '		<option value="dd-mm-yy" ' + (data.dateformate == "dd-mm-yy" ? "selected='selected'": "") + '>dd-MM-yyyy</option>';
        e += '		<option value="dd/mm/yy" ' + (data.dateformate == "dd/mm/yy" ? "selected='selected'": "") + '>dd/MM/yyyy</option>';
        e += '		<option value="mm-dd-yy" ' + (data.dateformate == "mm-dd-yy" ? "selected='selected'": "") + '>MM-dd-yyyy</option>';
        e += '		<option value="mm/dd/yy" ' + (data.dateformate == "mm/dd/yy" ? "selected='selected'": "") + '>MM/dd/yyyy</option>';
        e += '		<option value="yy-mm-dd" ' + (data.dateformate == "yy-mm-dd" ? "selected='selected'": "") + '>yyyy-MM-dd</option>';
        e += '		<option value="yy/mm/dd" ' + (data.dateformate == "yy/mm/dd" ? "selected='selected'": "") + '>yyyy/MM/dd</option>';
        e += '		<option value="yy/mm" ' + (data.dateformate == "yy/mm" ? "selected='selected'": "") + '>yyyy/MM</option>';
        e += '		<option value="yy-mm" ' + (data.dateformate == "yy-mm" ? "selected='selected'": "") + '>yyyy-MM</option>';
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
		e += '	<span class="title"><i class="icon-cog"></i> 校验规则：</span>';
		e += '  <div class="groupbox">';
        e += '	<div class="edit_item_warp" style="margin-top:5px;">';
        e += '		<input class="mbIE" type="checkbox" onchange="SurveyBuild.saveAttr(this,\'isRequire\')"' + (data.isRequire == "Y" ? "checked='checked'": "") + ' id="is_require"> <label for="is_require">是否必填</label>&nbsp;&nbsp</input>';
        e += '	</div>';
		e += '	</div>';
		
        e += '	<div class="edit_item_warp">';
        e += '		<a href="javascript:void(0);" onclick="SurveyBuild.RulesSet(this);"><i class="icon-cogs"></i> 高级设置</a>';
        e += '	</div>';
        e += '</div>';
        return e;
    },
    _eventbind: function(data) {
        var $inputBox = $("#" + data.itemId);
        var $selectBtn = $("#" + data.itemId + "_Btn");

        $inputBox.datetimepicker({
            changeMonth: true,
            changeYear: true,
            showTimepicker: false,
            yearRange: data.minYear + ":" + data.maxYear,
            maxDate: new Date(data.maxYear + "-12-31"),
            dateFormat: data.dateformate,
           onSelect: function(dateText, inst) {
                $inputBox.datetimepicker( "hide" );
                $inputBox.trigger("blur");
            }
        });

        $selectBtn.click(function(e) {
            $inputBox.click();
        });
        $inputBox.formValidator({tipID: data["itemId"] + 'Tip',onShow: "",onFocus: "&nbsp;",onCorrect: "&nbsp;"});
        $inputBox.functionValidator({
            fun: function(val, elem) {
                //执行高级设置中的自定义规则
                /*********************************************\
                 ** 注意：自定义规则中不要使用formValidator **
                 \*********************************************/
                var _result = true;
                if (ValidationRules) {
                    $.each(data["rules"], function(classname, classObj) {
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
            }
        })
    },
    _validatorAttr: function(data) {
        var msg;
        var $edit_box = $("#question-edit");
        var dateMax = data.maxYear;
        var dateMin = data.minYear;

        if (!dateMax) {
            msg = "年份最大值不能为空！";
            var $targetObj = $edit_box.find(".datemax");
            SurveyBuild.fail($targetObj, msg);
            return false;
        }
        if (!dateMin) {
            msg = "年份最小值不能为空！";
            var $targetObj = $edit_box.find(".datemin");
            SurveyBuild.fail($targetObj, msg);
            return false;
        }
        if (parseInt(dateMax) < parseInt(dateMin)) {
            msg = "年份最大值要大于年份最小值！";
            var $targetObj = $edit_box.find(".datemax");
            SurveyBuild.fail($targetObj, msg);
            return false;
        }
        return true;
    }
});