// 在线调查日期输入框
SurveyBuild.extend("DateBox", "Completion", {
    itemName: "日期",
    title: "日期",
    dateformate: "yy-mm-dd",
    "StorageType": "S",
    minYear: "1960",
    maxYear: "2030",

    _getHtml: function(data, previewmode) {
        var c = "";
        if (previewmode) {
            //PC端
            if(SurveyBuild.accessType == "P"){
                SurveyBuild.appInsId == "0" && this._getDefaultVal(data);
                c += '<div class="listcon">';
                c += '  <div class="question">';
                c += '      <span class="fontblue-blod">' + data.qCode + '.</span>' + data.title;
                c += '  </div>';
                c += '  <div class="answer">';
                c += '      <input id="' + data.itemId + '" name="' + data.itemId + '" type="text" value="' + data.value + '"class="input-date-text " readonly="readonly" onclick="this.focus()" title="' + data.itemName + '">';
                c += '      <img id="' + data.itemId + '_Btn" src="' + TzUniversityContextPath + '/statics/images/appeditor/calendar.png" style="position:relative;top:5px;left:-35px;cursor:pointer;">';
                c += '       <div style="margin-top:-31px;margin-left:225px">';
                c += '          <div id="' + data.itemId + 'Tip" class="onShow" style="margin: 0px; padding: 0px; background: transparent;">';
                c += '              <div class="onShow"></div>';
                c += '            </div>';
                c += '      </div>';
                c += '  </div>';
                c += '</div>';
            }else{
                SurveyBuild.appInsId == "0" && this._getDefaultVal(data);
                c += '<div class="listcon">';
                c += '  <div id="' + data.itemId + 'Tip" class="onShow">';
                c += '      <div class="onShow"></div>';
                c += '  </div>';
                c += '	<div class="question">';
                c += '		<span class="fontblue-blod">' + data.qCode + '.</span>' + data.title;
                c += '	</div>';
                c += '  <div class="answer" style="position: relative;">';
                c += '      <input id="' + data.itemId + '" name="' + data.itemId + '" type="text" value="' + data.value + '"class="input-date-text " style="line-height: 39px;font-size:1.285em;margin:0 0 0 4px" readonly="readonly" onclick="this.focus()" title="' + data.itemName + '">';
                c += '      <img id="' + data.itemId + '_Btn" src="' + TzUniversityContextPath + '/statics/images/appeditor/calendar.png" style="cursor: pointer; position: absolute; right: 10px; top: 8px;">';
                c += '  </div>';
                c += '</div>';
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

       $inputBox.datepicker({
            showButtonPanel:true,
            changeMonth: true,
            changeYear: true,
            yearRange: data.minYear + ":" + data.maxYear,
            dateFormat: data.dateformate,
			onClose:function(dateText, inst){
				$(this).trigger("blur");
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
        var $def = $("#defaultval");
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

        if(data.defaultval){
            var defVal = new Date(data.defaultval);
            if(defVal == "Invalid Date" || defVal == "NaN"){
                msg = "默认值格式非法，请重新输入！";
                SurveyBuild.fail($def, msg);
                return false;
            }

            var defYear = defVal.getFullYear();
            if(defYear < data.minYear || defYear > data.maxYear){
                msg = "默认值超出年份最大值和最小值范围！";
                SurveyBuild.fail($def, msg);
                return false;
            }
        }

        return true;
    }
});