/**
 * Created by admin on 2015/6/19.
 * 班级批次
 */
SurveyBuild.extend("bmrBatch", "baseComponent", {
    itemName:"班级批次",
    title:"班级批次",
    "StorageType":"S",
    option: {},
    _getHtml: function(data, previewmode) {
        var c = "";

        if (previewmode) {
            if(SurveyBuild._readonly){
                //只读模式
				c += '<div class="input-list">';
				c += '	<div class="input-list-info left"><span class="red-star">' + (data.isRequire == "Y" ? "*": "") + '</span>' + data.title + '</div>';
				c += '  <div class="input-list-text left">' + data.wzsm + '</div>';
				c += '  <div class="input-list-suffix left"></div>';
				c += '  <div class="clear"></div>';
				c += '</div>';
            }else {
                //编辑模式
                var classid = $("#ClassId").val();
                var params = '{"ComID":"TZ_ONLINE_REG_COM","PageID":"TZ_ONREG_OTHER_STD","OperateType":"EJSON","comParams":{"OType":"BATCH","CLASSID":"' + classid + '"}}';
                $.ajax({
                    type: "get",
                    dataType: "JSON",
                    data: {
                        tzParams: params
                    },
                    async: false,
                    url: SurveyBuild.tzGeneralURL,
                    success: function (f) {
                        if (f.state.errcode == "0") {
                            data.option = f.comContent;
                        }
                    }
                });
                	
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
				c += '    <div class="input-list-suffix left">' + (data.suffix != "" ? data.suffix: "") + '<div id="' + data.itemId + 'Tip" class="onShow"><div class="onShow"></div></div></div>';
				c += '    <div class="clear"></div>';
				c += '</div>';
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
        //$(this).children('option:selected').val()
        // $("#" + data["itemId"])[0].options[$("#" + data["itemId"])[0].selectedIndex].text;
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