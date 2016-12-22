/**
 * Created by admin on 2015/6/23.
 * 叶少威（王瑞立）
 */
SurveyBuild.extend("mobilePhone","baseComponent",{
    itemName: "移动电话",
    title:"移动电话",
    isSingleLine: "Y",
    children: [
        {
            "itemId": "mobile_area",
            "itemName": MsgSet["AREA"],
            "title": MsgSet["AREA"],
            "value": "",
            "StorageType": "S",
            "orderby":1,
            "classname":"SingleTextBox"
        },
        {
            "itemId": "mobile_no",
            "itemName": MsgSet["MOBILE"],
            "title": MsgSet["MOBILE"],
            "value": "",
            "StorageType": "S",
            "orderby":2,
            "classname":"SingleTextBox"
        }
    ],
    _getHtml: function(data, previewmode) {
        var c = "", children = data.children;;
        if (previewmode) {
            if(SurveyBuild._readonly){
                //只读模式
                c += '<div class="input-list">';
                c += '	<div class="input-list-info left"><span class="red">' + (data.isRequire == "Y" ? "*": "") + '</span>' + data.title + '</div>';
                c += '  <div class="input-list-text left">' + (children[0]["value"] ? "(" : "") + children[0]["value"] +  (children[0]["value"] ? ")&nbsp;" : "") + children[1]["value"] + '</div>';
                c += '	<div class="input-list-suffix left"></div>';
                c += '	<div class="clear"></div>';
                c += '</div>';
            }else{
                //填写模式
                var regular = "/^[\\d,-]+$/";
                var regArea = "/^[\\+]?[\\d]+$/";
                SurveyBuild.appInsId == "0" && this._getDefaultVal(data,"P2");

                c += '<div class="input-list">';
                c += '	<div class="input-list-info left"><span class="red">' + (data.isRequire == "Y" ? "*": "") + '</span>' + data.title + '</div>';
                c += '	<div class="input-list-textdate left input-date-select" style="width:12.5%">';
                c += '    	<input type="text" class="inpu-list-text-enter" placeholder="' + children[0]["itemName"] + '" title="' + children[0]["itemName"] + '" id="' + data["itemId"] + children[0]["itemId"] + '" name="' + data["itemId"] + children[0]["itemId"] + '" value="' + children[0]["value"] + '" data-regular = "' + regArea + '">';
                c += '	</div>';
                c += '	<div class="input-list-textdate left input-date-select" style="width: 21%; margin: 0 15px 0 0;">';
                c += '    	<input type="text" class="inpu-list-text-enter" title="' + children[1]["itemName"] + '" id="' + data["itemId"] + children[1]["itemId"] + '" name="' + data["itemId"] + children[1]["itemId"] + '" value="' + children[1]["value"] + '" data-regular = "' + regular + '">';
                c += '	</div>';
                c += '	<div class="input-list-suffix left">' + (data["suffix"] ? data.suffix + '<span class="input-list-suffix-span">&nbsp;</span>': "")  + '<div id="' + data["itemId"] + 'Tip" class="onShow"><div class="onShow">&nbsp;</div></div></div>';
                c += '	<div class="clear"></div>';
                c += '</div>';
            }
        } else {
            c += '<div class="question-answer">';
            c += '  <div class="format">';
            c += '      <b class="read-input" style="min-width: 80px;"> +86 </b>&nbsp;'
            c += '      <b class="read-input" style="min-width: 120px;">&nbsp;</b>';
            c += '      <span class="suffix">' + (data["suffix"] ? data.suffix: "") + '</span>';
            c += '  </div>';
            c += '</div>'
        }

        return c;
    },
    _edit: function(data) {
        var _fix = "",e = "";

        $.each(SurveyBuild._preg, function(reg, regObj) {
            _fix += '<option value="' + reg + '"' + (data.preg == reg ? ' selected="selected"': "") + ">" + regObj["name"] + "</option>"
        });

        e += '<div class="edit_item_warp">';
        e += '  <span class="edit_item_label">后缀：<a href="#" data-for-id="help_suffix" onclick="SurveyBuild.showMsg(this,event)" class="big-link" data-reveal-id="myModal" data-animation="fade">(?)</a></span>';
        e += '  <input type="text" class="medium" onkeyup="SurveyBuild.saveAttr(this,\'suffix\')" value="' + data.suffix + '"/>';
        e += '</div>';

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
        e += '  <div class="edit_item_warp" style="margin-top:5px;">';
        e += '      <input class="mbIE" type="checkbox" onchange="SurveyBuild.saveAttr(this,\'isRequire\')"' + (data.isRequire == "Y" ? "checked='checked'": "") + ' id="is_require"> <label for="is_require">是否必填</label>';
		e += '  </div>';
		e += '  <div class="edit_item_warp" style="margin-top:5px;display:none;">';
        e += '      <input class="mbIE" type="checkbox" onchange="SurveyBuild.saveAttr(this,\'isShow\')"' + (data.isShow == "Y" ? "checked='checked'": "") + ' id="is_showCode"> <label for="is_showCode">显示区号</label>';
        e += '  </div>';
        //e += '  <div class="edit_item_warp">';
        //e += '      <span class="edit_item_label" >校验规则：</span>';
        //e += '      <select id="is_preg" style="width:200px" onchange="SurveyBuild.saveAttr(this,\'preg\')"><option></option>' + _fix + '</select>';
        //e += '  </div>';
		e += '  </div>';
		
        e += '  <div class="edit_item_warp">';
        e += '      <a href="javascript:void(0);" onclick="SurveyBuild.RulesSet(this);"><i class="icon-cogs"></i> 高级设置</a>';
        e += '  </div>';
        e += '</div>';

        return e;
    },
    _eventbind: function(data) {

        var area = $("#" + data["itemId"] + data.children[0]["itemId"]);
        area.formValidator({tipID:(data["itemId"] + 'Tip'),onShow:"",onFocus:"&nbsp;",onCorrect:"&nbsp;"});
        area.functionValidator({
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
                                _result = _ruleClass._Validator(data["itemId"] + data.children[0]["itemId"], classObj["messages"],data);
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
        /* BUG 979:手机号码控件BUG修改
        if(data.isShow != "Y"){
            area.attr("disabled","disabled").unFormValidator(true);
        }else{
            area.removeAttr("disabled").unFormValidator(false);
        }
        */
        var mobileNo = $("#" + data["itemId"] + data.children[1]["itemId"]);
        mobileNo.formValidator({tipID:(data["itemId"] + 'Tip'),onShow:"",onFocus:"&nbsp;",onCorrect:"&nbsp;"});
        mobileNo.functionValidator({
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
                                _result = _ruleClass._Validator(data["itemId"] + data.children[1]["itemId"], classObj["messages"],data);
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