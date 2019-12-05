/**
 * Created by admin on 2015/9/9.
 */
SurveyBuild.extend("DigitalCompletion", "baseComponent", {
    itemName: "数字填空题",
    title:"数字填空题",
    isNumSize: "N",
    min: "0",//最小值
    max: "9999",//最大值
    digits: "0",//小数位数
    StorageType:"S",
    qCode:"Q1",
    _getHtml: function(data, previewmode) {
        var c = "";
        if (previewmode) {
            var regular = "^(-?\\d+)(\\.\\d+)?$";

            if(SurveyBuild.accessType == "P"){
                c += '<div class="listcon">';
                c += '	<div class="question">';
                c += '      <span class="fontblue-blod">' + data.qCode + '.</span>' + data.title;
                c += '      <div id="' + data.itemId + 'Tip" class="onShow">';
                c += '          <div class="onShow"></div>';
                c += '      </div>';
                c += '  </div>';
                c += '	<div class="answer">';
                c += '		<input id="' + data.itemId + '" onchange="SurveyBuild.handleInput(this);" onkeyup="SurveyBuild.handleInput(this); " name="' + data.itemId + '" data-regular="'+ regular +'" class="underline"  value="' + data.value + '">';
                c += '	</div>';
                c += '</div>';
            }else{
                c += '<div class="listcon">';
                c += '  <div id="' + data.itemId + 'Tip" class="onShow">';
                c += '      <div class="onShow"></div>';
                c += '  </div>';
                c += '	<div class="question">';
                c += '      <span class="fontblue-blod">' + data.qCode + '.</span>' + data.title;
                c += '  </div>';
                c += '	<div class="answer">';
                c += '		<input id="' + data.itemId + '" onchange="SurveyBuild.handleInput(this);" onkeyup="SurveyBuild.handleInput(this); " name="' + data.itemId + '" data-regular="'+ regular +'" class="inputtext" value="' + data.value + '">';
                c += '	</div>';
                c += '</div>';
            }

        } else {
            c += '<div class="question-answer">';
            c += '	<div class="format">';
            c += '		<b class="read-input"></b>';
            c += '	</div>';
            c += '</div>';
        }

        return c;
    },
    _edit: function(data) {
        var e = "";
        //默认值
        e += '<div class="edit_item_warp">';
        e += '	<span class="edit_item_label">默认值：</span>';
        e += '	<input type="text" class="medium" id="defaultval" onkeyup="SurveyBuild.saveAttr(this,\'defaultval\')" value="' + data.defaultval + '"/>';
        e += '</div>';

        //校验规则
        e += '<div class="edit_jygz">';
        e += '  <span class="title"><i class="icon-cog"></i> 校验规则：</span>';
        e += '  <div class="groupbox">';
        //是否必填
        e += '	    <div class="edit_item_warp" style="margin-top:5px;">';
        e += '		    <input type="checkbox" onchange="SurveyBuild.saveAttr(this,\'isRequire\')"' + (data.isRequire == "Y" ? "checked='checked'": "") + ' id="is_require"> <label for="is_require">是否必填：</label><a href="#" data-for-id="help_isRequire" onclick="SurveyBuild.showMsg(this,event)" class="big-link" data-reveal-id="myModal" data-animation="fade">(?)</a>';
        e += '      </div>';
        //数值范围
        e += '      <div class="edit_item_warp">';
        e += '	        <input type="checkbox" onchange="SurveyBuild.saveAttr(this,\'isNumSize\')"' + (data.isNumSize == "Y" ? "checked='checked'": "") + ' id="is_checkNumSize"> <label for="is_checkNumSize">数值范围：</label><a href="#" data-for-id="help_isNumSize" onclick="SurveyBuild.showMsg(this,event)" class="big-link" data-reveal-id="myModal" data-animation="fade">(?)</a>';
        e += '      </div>';
        //最小值
        e += '      <div class="edit_item_warp">';
        e += '            <span class="edit_item_label">最小值：</span>';
        e += '          <input type="text" maxlength="30" class="medium numSizeMin" data_id="' + data.instanceId + '" onkeyup="SurveyBuild.saveAttr(this,\'min\')" value="' + data.min + '"/>';
        e += '      </div>';
        //最大值
        e += '      <div class="edit_item_warp">';
        e += '	          <span class="edit_item_label">最大值：</span>';
        e += '	        <input type="text" maxlength="30" class="medium numSizeMax" data_id="' + data.instanceId + '" onkeyup="SurveyBuild.saveAttr(this,\'max\')" value="' + data.max + '"/>';
        e += '      </div>';
        //小数位数
        e += '      <div class="edit_item_warp mb10">';
        e += '	          <span class="edit_item_label">小数位数：</span>';
        e += '	        <input type="text" maxlength="3" class="medium numDecws" data_id="' + data.instanceId + '" onkeyup="SurveyBuild.saveAttr(this,\'digits\')" value="' + data.digits + '"/>';
        e += '      </div>';
        e += '  </div>';
        //高级设置
        e += '  <div class="edit_item_warp">';
        e += '	    <a href="javascript:void(0);" onclick="SurveyBuild.RulesSet(this);"><i class="icon-cogs"></i> 高级设置</a>';
        e += '  </div>';
        e += '</div>';

        return e;
    },

    _validatorAttr: function(data){
        var $min = $("#question-edit .numSizeMin");
        var $max = $("#question-edit .numSizeMax");
        var $digits = $("#question-edit .numDecws");
        var $def = $("#defaultval");
        if (isNaN(data.min)){
            SurveyBuild.fail($min, "请填写数字！");
            return false;
        }
        if (isNaN(data.max)){
            SurveyBuild.fail($max, "请填写数字！");
            return false;
        }
        if (isNaN(data.digits)){
            SurveyBuild.fail($digits, "请填写数字！");
            return false;
        }
        if (isNaN(data.defaultval)){
            SurveyBuild.fail($def, "请填写数字！");
            return false;
        }
        if (data.isNumSize == "Y"){
            if (data.min == ""){
                SurveyBuild.fail($min, "请填写最小值！");
                return false;
            }
            if (data.max == ""){
                SurveyBuild.fail($max, "请填写最大值！");
                return false;
            }
            if (data.digits == ""){
                SurveyBuild.fail($digits, "请填写小数位数！");
                return false;
            }
            if (parseInt(data.min,10) > parseInt(data.max,10)){
                SurveyBuild.fail($max, "最大值不能小于最小值！");
                return false;
            }
            if(data.defaultval){
                if(parseInt(data.defaultval,10) > parseInt(data.max,10) || parseInt(data.defaultval,10) < parseInt(data.min,10)){
                    SurveyBuild.fail($def, "默认值不符合数值范围！");
                    return false;
                }
                return true;
            }

            return true;
        } else {
            return true;
        }
    },

    _eventbind:function(data){
        var $inputBox = $("#" + data.itemId);

        $inputBox.formValidator({tipID:(data["itemId"]+'Tip'), onShow:"&nbsp;", onFocus:"&nbsp;", onCorrect:"&nbsp;"});
        $inputBox.functionValidator({
            fun:function(val,elem){
                var digital = $.trim($inputBox.val());
                var _cs = /^(-?\d+)(\.\d+)?$/;

                if (digital && !_cs.test(digital)) {
                    return "该字段只允许负数、数字、小数点！";
                }

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
                                _result = _ruleClass._Validator(data["itemId"], classObj["messages"],data);
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
            }
        });

    }
});