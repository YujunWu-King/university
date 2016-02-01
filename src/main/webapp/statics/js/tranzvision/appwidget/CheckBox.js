/**
 * Created by WRL on 2015/7/22.
 */
SurveyBuild.extend("CheckBox", "baseComponent", {
    itemName: "复选框",
    title:"复选框",
    "StorageType":"S",
    _getHtml: function(data, previewmode) {

        var c = "",e = "";
        if (previewmode) {
			SurveyBuild.appInsId == "0" && this._getDefaultVal(data);
            c += '<div class="main_inner_content_info_autoheight">';
            c += '	<div class="main_inner_connent_info_left">';
            c += '      &nbsp;<span class="reg_title_star">' + (data.isRequire == "Y" ? "*": "") + '</span>';
            c += '  </div>';
            c += '	<div class="main_inner_content_info_right">';
			c += '		<div class="tz_checkbox_div '+(data.value == "Y" ? "on_check": "")+'">';
            c += '		    <input class="tz_radio_checkbox" type="checkbox" ' + (data.value == "Y" ? "checked='checked'": "") + ' instanceId="' + data.instanceId + '" id="' + data.itemId + '" name="' + data.itemId + '" value="' + data.value + '" style="width:20px;height:20px;"/>';
			c += '      </div>';
			c += '      <span>' + data.title + '</span>';
            c += '		<div id="' + data.itemId + 'Tip" style="margin: 0px; padding: 0px; background: transparent none repeat scroll 0% 0%;" class="onShow">';
            c += '          <div class="onShow">&nbsp;</div>';
            c += '      </div>';
            c += '	</div>';
            c += '</div>';
        } else {
            c = '<div class="question-answer"><ul class="format">' + e + '</ul></div>'
            c += '<div class="question-answer">';
            c += '	<ul class="format">';
            c += '		<li class="read-check">' + data.title + '</li>';
            c += '	</ul>';
            c += '</div>';
        }
        return c;
    },
    _edit: function(data) {
        var e = '';

        e += '<div class="edit_item_warp">';
        e += '  <span class="edit_item_label">默认值：</span>';
        e += '  <input type="text" class="medium" id="defaultval" onkeyup="SurveyBuild.saveAttr(this,\'defaultval\')" value="' + data.defaultval + '"/>';
        e += '</div>';

        e += '<div class="edit_item_warp" style="text-align:right;">';
        e += '  <a href="javascript:void(0);" style="color:#3F69B6;" onclick="SurveyBuild.DynamicBindVal()" class="">动态绑定值</a>';
        e += '</div>';

        e += '<div class="edit_jygz">';
        e += '	<span class="title"><i class="icon-cog"></i> 校验规则</span>';
        e += '  <div class="groupbox">';
        e += '		<div class="edit_item_warp" style="margin-top:5px;">';
        e += '			<input type="checkbox" onchange="SurveyBuild.saveAttr(this,\'isRequire\')"' + (data.isRequire == "Y" ? "checked='checked'": "") + ' id="is_require"> <label for="is_require">是否必填</label>';
        e += '		</div>';
        e += '	</div>';
        e += '	<div class="edit_item_warp">';
        e += '		<a href="javascript:void(0);" onclick="SurveyBuild.RulesSet(this);"><i class="icon-cogs"></i> 高级设置</a>';
        e += '	</div>';
        e += '</div>';
        return e;
    },
    _eventbind: function(data) {
        var $inputBox = $("#" + data.itemId);
		
		$inputBox.click(function(e) {
            var isCheck = $(this).prop("checked");
			if(isCheck){
                data.value = "Y";
				$(this).closest(".tz_checkbox_div").addClass("on_check");
			}else{
                data.value = "N";
				$(this).closest(".tz_checkbox_div").removeClass("on_check");
			}
        });

        var ReqErrorMsg = "&nbsp;";
        var allowEmpty = true;
        if (ValidationRules) {
            $.each(data["rules"],function(classname, classObj) {
                if ($.inArray(classname, SurveyBuild._baseRules) == -1 && data["rules"][classname]["isEnable"] == "Y") {
                    //必填校验
                    if(classname == "RequireValidator" && data.isRequire == "Y"){
                        allowEmpty = false;
                        ReqErrorMsg = classObj["messages"];
                    }
                }
            });
        }
        //判断是否为必填
        $inputBox.formValidator({tipID:(data["itemId"]+'Tip'), onShow:"", onFocus:"&nbsp;", onCorrect:"&nbsp;", empty:allowEmpty, onEmpty:ReqErrorMsg});
        if(!allowEmpty){
            $inputBox.inputValidator({min:1, onError:ReqErrorMsg});
        }

        $inputBox.functionValidator({
            fun:function(val,elem){

                //执行高级设置中的自定义规则
                /*********************************************\
                 ** 注意：自定义规则中不要使用formValidator **
                 \*********************************************/
                var _result = true;
                if (ValidationRules) {

                    $.each(data["rules"],function(classname, classObj) {
                        if ($.inArray(classname, SurveyBuild._baseRules) == -1 && data["rules"][classname]["isEnable"] == "Y" && classname != "RequireValidator") {
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

