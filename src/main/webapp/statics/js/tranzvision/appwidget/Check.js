SurveyBuild.extend("Check", "baseComponent", {
    itemName: "多选框",
    title:"多选框",
    maxSelect:"1",
    minSelect:"1",
    format:"1",
    othervalue:"",
    "StorageType":"D",
    option: {},
    _init: function(d, previewmode) {
        if ($.isEmptyObject(this.option)) {
            /*如果下拉框无选项值，将初始化this.option*/
        } else {
            /*如果下拉框有选项值，直接返回*/
            return;
        }
        for (var c = 1; c <= 3; ++c) {
            this.option[d + c] = {
                code: c,
                txt: "选项" + c,
                orderby: c,
                defaultval: 'N',
                other: 'N',
                weight: 0,
                checked:"N",
	            othervalue :''
            }
        }
    },
    _getHtml: function(data, previewmode) {

        var c = "",e = "";
        if (previewmode) {
			SurveyBuild.appInsId == "0" && this._getDefaultVal(data);
            for (var i in data.option) {
                e += '<li>';
				e += '<div class="tz_checkbox_div '+(data["option"][i]["checked"] == "Y" ? "on_check" : "")+'">';
                e += '<label style="margin-left:10px;" for="o' + data.itemId + data["option"][i]["code"] + '"><input class="tz_radio_checkbox '+(data["option"][i]["other"]=="Y"?"sur_other_box":"")+'"instanceId=' + i + ' id="o' + data.itemId + data["option"][i]["code"] + '" name="' + data.itemId + '" type="checkbox" ' + (data["option"][i]["checked"] == "Y" ? "checked='checked'": "") + ' value="' + data["option"][i]["code"]+'" style="width:20px;height:20px;"/>' + data["option"][i]["txt"] + '</label>';
				e += '</div>'
                if (data["option"][i]["other"] == "Y"){
                    if(SurveyBuild._readonly){
                        //只读模式
                        e +='&nbsp;&nbsp;<span>' + data.othervalue + '</span>';
                    }else{
                        //编辑模式
                        e += '<input type="text" id="other' + data.itemId + '" style=" display:'+(data["option"][i]["checked"] == "Y" ? "inline-block": "none")+';width: 150px; height: 20px; line-height:20px; margin-left: 10px;" value="' + data.othervalue + '"/>';
                    }
                }
                e += '</li>';
            }

            c += '<div class="main_inner_content_info_autoheight">';
            c += '	<div class="main_inner_connent_info_left">';
            c += '		<span class="reg_title_star">' + (data.isRequire == "Y" ? "*": "") + '</span>' + data.title;
            c += '	</div>';
            c += '	<div class="main_inner_content_options_right question-answer" style="margin-bottom:12px;">';
            c += '		<ul class="format '+ data.format +'">' + e + '</ul>';
            c += '	</div>';
            c += '<input id="' + data.itemId + '" type="hidden">';
            c += '<div><div id="' + data.itemId + 'Tip" class="onShow" style="margin: 0px; padding: 0px; background: transparent;"><div class="onShow"></div></div></div>';
            c += '</div>';
        } else {
            for (var i in data.option) {
                e += '<li class="read-check" id="o' + i + '">';
                e += data["option"][i]["txt"];
                if(data["option"][i]["other"] == "Y"){
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
            list += '<td><input type="checkbox" onchange="$(\'.other\').not(this).prop(\'checked\',false);SurveyBuild.saveLevel1Attr(this,\'other\');" class="other" ' + (data["option"][i]["other"] == "Y" ? "checked='checked'": "") + ' value="' + data["option"][i]["other"] + '"></td>';
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
        e += '	<table class="table table-bordered data-table">';
        e += '		<thead>';
        e += '		<tr>';
        e += '			<th class="thw">默认</th>';
        e += '			<th class="thw">其他</th>';
        e += '			<th>值</th>';
        e += '			<th class="alLeft">描述<button onclick="SurveyBuild.optionBatch(\'' + data.instanceId + '\')" class="btn btn-primary btn-mini pull-right">批量编辑</button></th>';
        e += '			<th width="45">操作</th>';
        e += '		</tr>';
        e += '		</thead>';

        e += '		<tbody class="ui-sortable">' + list + '</tbody>';
        e += '	</table>';
        e += '</fieldset>';


        e += '<div class="edit_jygz">';

        e += '	<div class="question-type clearfix" style="display:none">';
        e += '		<fieldset>';
        e += '			<legend>选项排列</legend>';
        e += '			<select onchange="SurveyBuild.saveAttr(this,\'format\')" data-id="' + data.instanceId + '">';
        e += '				<option value="1" ' + (data.format == "1" ? "selected='selected'": "") + '>垂直1列</option>';
        e += '				<option value="2" ' + (data.format == "2" ? "selected='selected'": "") + '>垂直2列</option>';
        e += '				<option value="3" ' + (data.format == "3" ? "selected='selected'": "") + '>垂直3列</option>';
        e += '				<option value="4" ' + (data.format == "4" ? "selected='selected'": "") + '>垂直4列</option>';
        e += '				<option value="5" ' + (data.format == "5" ? "selected='selected'": "") + '>垂直5列</option>';
        e += '				<option value="6" ' + (data.format == "6" ? "selected='selected'": "") + '>垂直6列</option>';
        e += '				<option value="9" ' + (data.format == "9" ? "selected='selected'": "") + '>水平</option>';
        e += '			</select>';
        e += '		</fieldset>';
        e += '	</div>';

        e += '	<div class="edit_item_warp">';
        e += '		<span class="edit_item_label">最少选择：</span>';
        e += '		<input type="text" class="medium" data_id="' + data.instanceId + '" onkeyup="SurveyBuild.saveAttr(this,\'minSelect\')" value="' + data.minSelect + '"/>';
        e += '	</div>';
        e += '	<div class="edit_item_warp">';
        e += '		<span class="edit_item_label">最多选择：</span>';
        e += '		<input type="text" class="medium" data_id="' + data.instanceId + '" onkeyup="SurveyBuild.saveAttr(this,\'maxSelect\')" value="' + data.maxSelect + '"/>';
        e += '	</div>';

        e += '<div class="edit_item_warp">';
        e += '  <span class="edit_item_label">默认值：</span>';
        e += '  <input type="text" class="medium" id="defaultval" onkeyup="SurveyBuild.saveAttr(this,\'defaultval\')" value="' + data.defaultval + '"/>';
        e += '</div>';

        e += '<div class="edit_item_warp" style="text-align:right;">';
        e += '  <a href="javascript:void(0);" onclick="SurveyBuild.DynamicBindVal()" class="">动态绑定值</a>';
        e += '</div>';

        e += '	<div class="edit_jygz">';
		e += '	<span class="title"><i class="icon-cog"></i> 校验规则</span>';
		e += '  <div class="groupbox">';
        e += '		<div class="edit_item_warp" style="margin-top:5px;">';
        e += '			<input type="checkbox" onchange="SurveyBuild.saveAttr(this,\'isRequire\')"' + (data.isRequire == "Y" ? "checked='checked'": "") + ' id="is_require"> <label for="is_require">是否必填</label>';
        e += '		</div>';
        e += '	</div>';
        e += '	</div>';

        e += '		<div class="edit_item_warp">';
        e += '			<a href="javascript:void(0);" onclick="SurveyBuild.RulesSet(this);"><i class="icon-cogs"></i> 高级设置</a>';
        e += '		</div>';

        e += '</div>';

        return e;
    },
    _eventbind: function(data) {
        $.each(data.option,function(i,opt){

            $("#o" + data.itemId + opt["code"]).click(function(){
                var meid = $(this).attr("instanceId");
                var scheck = $(this).prop("checked");
                if(scheck){
                    data["option"][meid]["checked"] = "Y";
					if (opt["other"] == "Y"){
						$("#other" + data.itemId).css("display","inline-block");	
					}
					$(this).closest(".tz_checkbox_div").addClass("on_check");
                }else{
                    data["option"][meid]["checked"] = "N";
					if (opt["other"] == "Y"){
						$("#other" + data.itemId).css("display","none");
					}
					$(this).closest(".tz_checkbox_div").removeClass("on_check");
                }
            });

            if(opt["other"] == "Y"){
                $("#other" + data.itemId).keyup(function(){
                    data.othervalue = $(this).val();
                });
            }
        });

		var $inputBox = $(":checkbox[name='"+ data.itemId +"']");
        var ReqErrorMsg = "&nbsp;";
        var NumErrorMsg = "&nbsp;";
        var allowEmpty = true;

        if (ValidationRules) {
            $.each(data["rules"],function(classname, classObj) {
                if ($.inArray(classname, SurveyBuild._baseRules) == -1 && data["rules"][classname]["isEnable"] == "Y") {
                    //必填校验
                    if(classname == "RequireValidator" && data.isRequire == "Y"){
                        allowEmpty = false;
                        ReqErrorMsg = classObj["messages"];
                    }
                    //固定格式校验（选择个数）
                    if(classname == "NumSizeValidator"){
                        NumErrorMsg = classObj["messages"];
                    }
                }
            });
        }
		$inputBox.formValidator({tipID:(data["itemId"] + 'Tip'), onShow:"", onFocus:"&nbsp;", onCorrect:"&nbsp;", empty:allowEmpty, onEmpty:ReqErrorMsg});
        if(!allowEmpty){
            $inputBox.inputValidator({min:1, onError:ReqErrorMsg});
        }
        if(data.isRequire == "Y"){
			$inputBox.inputValidator({min:data.minSelect, max:data.maxSelect, onError:NumErrorMsg });
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
                        if ($.inArray(classname, SurveyBuild._baseRules) == -1 && data["rules"][classname]["isEnable"] == "Y" && classname != "NumSizeValidator" && classname != "RequireValidator") {
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