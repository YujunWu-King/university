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
			if(SurveyBuild.accessType == "M"){
				
				c += '<div class="item" data-instancid="' + data.instanceId + '">';
				c += '  <div id="' + data.itemId + 'Tip" class="tips" style="display: none;"><i></i><span></span></div>';
				c += '<div class="check-box ">';
				c += '	<ul>';
				c += '		<li class="check-box ' + (data.value == "Y" ? "checkedBox": "") + '"><input type="checkbox" class="checkbox"  '+ (data.value == "Y" ? "checked='checked'": "") + ' instanceId="' + data.instanceId + '" id="' + data.itemId + '" name="' + data.itemId + '" value="' + data.value + '"><label for="' + data.itemId + '">'+data.title+' </label>';
				c += '<span>'+(data.isRequire == "Y" ? "*": "") +'</span></li>';
				c += '	</ul>';
				c += '</div>';
				c += '</div>';
			}else{
				c += '<div class=" input-list input-radiobox">';
				c += '	<div class="input-list-info left"><span class="red-star">' + (data.isRequire == "Y" ? "*": "") + '</span></div>';
				c += '    <div class="margart15 input-list-textwrap left">';
				c += '    	 <ul>';
				c += '    	 	<li><div class="check-box ' + (data.value == "Y" ? "checkedBox": "") + '"><i><input type="checkbox" name="check-box" ' + (data.value == "Y" ? "checked='checked'": "") + ' instanceId="' + data.instanceId + '" id="' + data.itemId + '" name="' + data.itemId + '" value="' + data.value + '"></i></div>' + data.title + '</li>';
				c += '            <div class="clear"></div>';
				c += '         </ul>';
				c += '    </div>';
				c += '    <div class="input-list-suffix left"><div id="' + data.itemId + 'Tip" class="onShow"><div class="onShow">&nbsp;</div></div></div>';
				c += '    <div class="clear"></div>';
				c += '</div>';
			}
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
		$.fn.toggleCheckbox = function () {
			this.prop('checked', !this.prop('checked'));
		}
		if(!SurveyBuild._readonly){
			$inputBox.parents(".check-box").on('click', function () {
			    $(this).find(':checkbox').toggleCheckbox();
			    $(this).toggleClass('checkedBox');

	            var isCheck = $(this).find(':checkbox').prop("checked");
				if(isCheck){
	                data.value = "Y";
				}else{
	                data.value = "N";
				}
			});
		}
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

