/**
 * Created by WRL on 2017/3/6.
 * 在线调查--自动补全
 */
SurveyBuild.extend("autoCompletion", "baseComponent", {
    itemName: "自动补全",
    title: "自动补全",
    StorageType:"S",
    qCode:"Q1",
    option: {},
    _init: function(d, previewmode) {
        if ($.isEmptyObject(this.option)) {
            /*如果下拉框无选项值，将初始化this.option*/
        } else {
            /*如果下拉框有选项值，直接返回*/
            return;
        }
        for (var i = 1; i <= 3; ++i) {
            this.option[d + i] = {
                code: i,
                txt: "选项" + i,
                orderby: i,
                defaultval: 'N',
                other: 'N',
                weight: 0
            }
        }
    },
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
        var e = '',list = "";
        //默认值
        e += '<div class="edit_item_warp">';
        e += '  <span class="edit_item_label">默认值：</span>';
        e += '  <input type="text" class="medium" id="defaultval" onkeyup="SurveyBuild.saveAttr(this,\'defaultval\')" value="' + data.defaultval + '"/>';
        e += '</div>';

        for (var i in data.option) {
            list += '<tr class="read-radio" data-id="' + data.instanceId + '-' + i + '">';
            list += '<td><input type="text" onkeyup="SurveyBuild.saveLevel1Attr(this,\'code\')" value="' + data["option"][i]["code"] + '" oncontextmenu="return false;" ondragenter="return false" onpaste="return false" class="ocode"></td>';
            list += '<td><input type="text" onkeyup="SurveyBuild.saveLevel1Attr(this,\'txt\')" value="' + data["option"][i]["txt"] + '" oncontextmenu="return false;" ondragenter="return false" class="option-txt"></td>';
            list += '<td><a onclick="SurveyBuild.plusOption(this,\'autocpl\');return false;" class="text-success" href="javascript:void(0);"><i class="icon-plus-sign"></i> </a><a onclick="SurveyBuild.minusOption(this);return false;" class="text-warning" href="javascript:void(0);"><i class="icon-minus-sign"></i> </a><a href="javascript:void(0);" class="text-info option-move"><i class="icon-move"></i> </a></td>';
            list += '</tr>';
        }
        e += '<fieldset id="option-box">';
		e += '	<span class="edit_item_label titlewidth"><i class="icon-list-alt"></i> 可选值设置</span>';
        e += '  <table class="table table-bordered data-table">';
        e += '      <thead>';
        e += '          <tr><th>名称</th><th class="alLeft">描述<button onclick="SurveyBuild.optionBatch(\'' + data.instanceId + '\')" class="btn btn-primary btn-mini pull-right">批量编辑</button></th><th width="45">操作</th></tr>';
        e += '      </thead>';
        e += '      <tbody class="ui-sortable">' + list + '</tbody>';
        e += '  </table>';
        e += '</fieldset>';

        e += '<div class="edit_jygz">';
		e += '	<span class="title"><i class="icon-cog"></i> 校验规则</span>';
		e += '  <div class="groupbox">';
        e += '  <div class="edit_item_warp" style="margin-top:5px;">';
        e += '      <input class="mbIE" type="checkbox" onchange="SurveyBuild.saveAttr(this,\'isRequire\')"' + (data.isRequire == "Y" ? "checked='checked'": "") + ' id="is_require"> <label for="is_require">是否必填</label>';
        e += '  </div>';
        e += '</div>';
		
        e += '  <div class="edit_item_warp">';
        e += '      <a href="javascript:void(0);" onclick="SurveyBuild.RulesSet(this);"><i class="icon-cogs"></i> 高级设置</a>';
        e += '  </div>';
        e += '</div>';

        return e;
    },
    _eventbind: function(data) {
        var $inputBox = $("#" + data.itemId);

        var source = [],j = 0;
        for (var i in data.option) {
            source[j] = data["option"][i]["txt"];
            j++;
        }

        $inputBox.autocomplete({
            source: source
        });

        $inputBox.formValidator({tipID:(data["itemId"] + 'Tip'), onShow:"&nbsp;", onFocus:"&nbsp;", onCorrect:"&nbsp;"});
        $inputBox.functionValidator({
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