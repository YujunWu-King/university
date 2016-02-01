/**
 * Created by admin on 2015/6/19.
 * 班级专业方向
 */
SurveyBuild.extend("bmrMajor", "baseComponent", {
    itemName: "专业方向",
    title:"专业方向",
    "StorageType":"S",
    option: {},
    _getHtml: function(data, previewmode) {
        var c = "", e = "";

        if (previewmode) {
            if(SurveyBuild._readonly){
                //只读模式
                c += '<div class="main_inner_content_info_autoheight cLH">';
                c += '	<div class="main_inner_connent_info_left">';
                c += '		<span class="reg_title_star">' + (data.isRequire == "Y" ? "*": "") + '</span>' + data.title;
                c += '	</div>';
                c += '	<div class="main_inner_content_info_right">' + data.wzsm + '</div>';
                c += '</div>'
            }else {
                //编辑模式
                var classid = $("#ClassId").val();
                var params = '{"ComID":"TZ_ONLINE_REG_COM","PageID":"TZ_ONREG_OTHER_STD","OperateType":"EJSON","comParams":{"OType":"MAJOR","CLASSID":"' + classid + '"}}';
                $.ajax({
                    type: "get",
                    dataType: "JSON",
                    data:{
                        tzParams:params
                    },
                    async:false,
                    url:SurveyBuild.tzGeneralURL,
                    success: function(f) {
                        if(f.state.errcode == "0"){
                            data.option = f.comContent;
                        }
                    }
                });
                c += '<div class="main_inner_content_info_autoheight">';
                c += '	<div class="main_inner_connent_info_left">';
                c += '		<span class="reg_title_star">' + (data.isRequire == "Y" ? "*": "") + '</span>' + data.title;
                c += '	</div>';
                c += '	<div class="main_inner_content_info_right">';
                c += '  	<div class="main_inner_content_info_right_option_251px">';
                c += '      	<select name="' + data.itemId + '" class="chosen-select" id="' + data.itemId + '" style="width:251px;" title="' + data.itemName + '">';
                c += '          	<option value="">'+ MsgSet["PLEASE_SELECT"] +'</option>';
                for (var i in data.option) {
                    c += '<option ' + (data.value == data["option"][i]["code"] ? "selected='selected'": "") + 'value="' + data["option"][i]["code"] + '">' + data["option"][i]["txt"] + '</option>';
                }
                c += '			</select>';
                c += '		</div>';
                c += '			<a class="blue" target="' + (data.suffixUrl ? "_blank" : "") + '" href="' + (data.suffixUrl ? data.suffixUrl : "javascript:void(0);") + '">' + data.suffix + '</a>';
                c += '  	<div style="margin-top:-40px;margin-left:256px;float:left;">';
                c += '      	<div id="' + data.itemId + 'Tip" class="onShow" style="margin: 0px; padding: 0px; background: transparent;">';
                c += '          	<div class="onShow"></div>';
                c += '      	</div>';
                c += '  	</div>';
                c += '	</div>';
                c += '</div>'
            }
        } else {
            c += '<div class="question-answer">';
            c += '	<b class="read-select"> - 请选择 - </b>';
            c += '	<span class="suffix">' + (data["suffix"] ? data.suffix: "") + '</span>';
            c += '</div>'
        }
        return c;
    },
    _edit: function(data) {
        var e = "";
        //后缀
        e += '<fieldset>';
        e += '	<legend>';
        e += '      <span class="edit_item_label">后缀：</span>';
        e += '     <input type="text" class="medium" onkeyup="SurveyBuild.saveAttr(this,\'suffix\')" value="' + data.suffix + '"/>';
        e += '	</legend>';
        e += '</fieldset>';

        //后缀链接
        e += '<fieldset>';
        e += '	<legend>';
        e += '      <span class="edit_item_label">后缀链接：</span>';
        e += '     <input type="text" class="medium" onkeyup="SurveyBuild.saveAttr(this,\'suffixUrl\')" value="' + data.suffixUrl + '"/>';
        e += '	</legend>';
        e += '</fieldset>';

        //规则设置
        e += '<div class="edit_jygz">';
        e += '	    <span class="title"><i class="icon-cog"></i> 校验规则</span>';
        e += '      <div class="groupbox">';
        e += '          <div class="edit_item_warp" style="margin-top:5px;">';
        e += '              <input class="mbIE" type="checkbox" onchange="SurveyBuild.saveAttr(this,\'isRequire\')"' + (data.isRequire == "Y" ? "checked='checked'": "") + ' id="is_require"> <label for="is_require">是否必填</label>';
        e += '          </div>';
        e += '      </div>';
        //高级设置
        e += '      <div class="edit_item_warp">';
        e += '          <a href="javascript:void(0);" onclick="SurveyBuild.RulesSet(this);"><i class="icon-cogs"></i> 高级设置</a>';
        e += '      </div>';
        e += '</div>';
        return e;
    },
    _eventbind: function(data) {
        var obj = $("#" + data["itemId"]);
        obj.change(function() {
            var selectIndex = $("#" + data["itemId"])[0].selectedIndex;
            if($("#" + data["itemId"])[0].options[selectIndex].value){
                  data.wzsm = $("#" + data["itemId"])[0].options[selectIndex].text;
            }else{
                data.wzsm = "";
            }
        });
        obj.formValidator({tipID:(data["itemId"] + 'Tip'),onShow:"",onFocus:"&nbsp;",onCorrect:"&nbsp;"});
        obj.functionValidator({
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