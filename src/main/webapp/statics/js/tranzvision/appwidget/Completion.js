/**
 * Created by admin on 2015/9/9.
 */
SurveyBuild.extend("Completion", "baseComponent", {
    itemName: "填空题",
    title: "填空题",
    isCheckStrLen: "N",
    StorageType: "S",
    minLen: "0",
    maxLen: "200",
    qCode:"Q1",

    _getHtml: function(data, previewmode) {
        var c = "";

        if (previewmode) {
            SurveyBuild.appInsId == "0" && this._getDefaultVal(data);
            var regular = "";
            if (data.preg && SurveyBuild._preg.hasOwnProperty(data.preg)) {
                regular = SurveyBuild._preg[data.preg]["regExp"];
            }
            if(SurveyBuild.accessType == "P"){
                c += '<div class="listcon">';
                c += '	<div class="question">';
                c += '      <span class="fontblue-blod">' + data.qCode + '.</span>' + data.title;
                c += '      <div id="' + data.itemId + 'Tip" class="onShow">';
                c += '          <div class="onShow"></div>';
                c += '       </div>';
                c += '  </div>';
                c += '	<div class="answer">';
                c += '		<input id="' + data.itemId + '" onchange="SurveyBuild.handleInput(this);" onkeyup="SurveyBuild.handleInput(this); " name="' + data.itemId + '" class="underline input-text-comp" data-regular="' + regular + '" value="' + data.value + '" >';
                c += '	</div>';
                c += '</div>';
            }else{
                c += '<div class="listcon">';
                c += '  <div id="' + data.itemId + 'Tip" class="onShow">';
                c += '      <div class="onShow"></div>';
                c += '  </div>';
                c += '	<div class="question">';
                c += '		<span class="fontblue-blod">' + data.qCode + '.</span>' + data.title;
                c += '	</div>';
                c += '	<div class="answer">';
                c += '		<input id="' + data.itemId + '" onchange="SurveyBuild.handleInput(this);" onkeyup="SurveyBuild.handleInput(this); " name="' + data.itemId + '" class="inputtext" data-regular="' + regular + '" value="' + data.value + '">';
                c += '	</div>';
                c += '</div>';
            }
        } else {
            c += '<div class="question-answer">';
            c += '  <div class="format">';
            c += '      <b class="read-input"></b>';
            c += '  </div>';
            c += '</div>'
        }

        return c;
    },
    _edit: function(data) {
        var _fix = "",e="";

        $.each(SurveyBuild._preg, function(reg, regObj) {
            _fix += '<option value="' + reg + '"' + (data.preg == reg ? ' selected="selected"': "") + ">" + regObj["name"] + "</option>"
        });
        //默认值
        e += '<div class="edit_item_warp">';
        e += '  <span class="edit_item_label">默认值：</span>';
        e += '  <input type="text" class="medium" id="defaultval" onkeyup="SurveyBuild.saveAttr(this,\'defaultval\')" value="' + data.defaultval + '"/>';
        e += '</div>';

        //校验规则
        e += '<div class="edit_jygz">';
        e += '  <span class="title"><i class="icon-cog"></i> 校验规则</span>';
        e += '  <div class="groupbox">';

        //是否必填
        e += '      <div class="edit_item_warp" style="margin-top:5px;">';
        e += '          <input class="mbIE" type="checkbox" onchange="SurveyBuild.saveAttr(this,\'isRequire\')"' + (data.isRequire == "Y" ? "checked='checked'": "") + ' id="is_require"> <label for="is_require">是否必填</label>';
        e += '      </div>';
        //字数范围
        e += '      <div class="edit_item_warp">';
        e += '          <input class="mbIE" type="checkbox" onchange="SurveyBuild.saveAttr(this,\'isCheckStrLen\')"' + (data.isCheckStrLen == "Y" ? "checked='checked'": "") + ' id="is_checkstrlen"> <label for="is_checkstrlen">字数范围</label>';
        e += '      </div>';
        //最少
        e += '      <div class="edit_item_warp">';
        e += '            <span class="edit_item_label">最少：</span>';
        e += '          <input type="text" maxlength="11" class="medium minLen" data_id="' + data.instanceId + '" onkeyup="SurveyBuild.saveAttr(this,\'minLen\')" value="' + data.minLen + '"/>';
        e += '      </div>';
        //最多
        e += '      <div class="edit_item_warp">';
        e += '            <span class="edit_item_label">最多：<a href="#" data-for-id="help_maxLen" onclick="SurveyBuild.showMsg(this,event)" class="big-link" data-reveal-id="myModal" data-animation="fade">(?)</a></span>';
        e += '          <input type="text" maxlength="11" class="medium maxLen" data_id="' + data.instanceId + '" onkeyup="SurveyBuild.saveAttr(this,\'maxLen\')" value="' + data.maxLen + '"/>';
        e += '      </div>';

        //校验规则
        e += '      <div class="edit_item_warp mb10">';
        e += '          <span class="edit_item_label" >校验规则：</span>';
        e += '          <select id="is_preg" onchange="SurveyBuild.saveAttr(this,\'preg\')">';
        e += '              <option></option>' + _fix;
        e += '          </select>';
        e += '      </div>';
        e += '</div>';

        e += '  <div class="edit_item_warp">';
        e += '      <a href="javascript:void(0);" onclick="SurveyBuild.RulesSet(this);"><i class="icon-cogs"></i> 高级设置</a>';
        e += '  </div>';
        e += '</div>';

        return e;
    },
    _validatorAttr: function(data) {
        var $min = $("#question-edit .minLen");
        var $max = $("#question-edit .maxLen");
        var $def = $("#defaultval");
        var msg = "";
        if (isNaN(data.minLen)){
            SurveyBuild.fail($min, "请填写数字！");
            return false;
        }
        if (isNaN(data.maxLen)){
            SurveyBuild.fail($max, "请填写数字！");
            return false;
        }
        var maxLen = parseInt(data.maxLen);
        if (maxLen > 254) {
            msg = "字数最多不能超过254个字符！";
            var $targetObj = $(".maxLen");
            SurveyBuild.fail($targetObj, msg);
            return false;
        }

        if (data.defaultval){
            var regular = "";
            if (data.preg && SurveyBuild._preg.hasOwnProperty(data.preg)) {
                regular = eval(SurveyBuild._preg[data.preg]["regExp"]);
                var _result = new RegExp(regular).test(data.defaultval);
                if(!_result){
                    SurveyBuild.fail($def, "默认值不符合固定格式校验！");
                    return false;
                }
            }
        }
        if (data.isCheckStrLen == "Y"){
            if (data.minLen == ""){
                SurveyBuild.fail($min, "请填写最少值！");
                return false;
            }
            if (data.maxLen == ""){
                SurveyBuild.fail($max, "请填写最多值！");
                return false;
            }
            if (parseInt(data.minLen,10) > parseInt(data.maxLen,10)){
                SurveyBuild.fail($max, "最大值不能小于最少值！");
                return false;
            }

            if(data.defaultval){
                var length = $.trim($def.val()).length;
                if((length > parseInt(data.maxLen,10) || length < parseInt(data.minLen,10))){
                    SurveyBuild.fail($def, "默认值长度不符合字符长度验证！");
                    return false;
                }
                return true;
            }
            return true;
        } else {
            return true;
        }
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
                    $.each(data["rules"],function(classname, classObj) {
                        if ($.inArray(classname, SurveyBuild._baseRules) == -1 && data["rules"][classname]["isEnable"] == "Y") {
                            var _ruleClass = ValidationRules[classname];
                            if (_ruleClass && _ruleClass._Validator) {
                                _result = _ruleClass._Validator(data["itemId"], classObj["messages"], data);
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
    }
});