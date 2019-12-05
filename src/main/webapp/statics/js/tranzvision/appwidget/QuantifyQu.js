/**
 * Created by admin on 2015/12/3.
 */
SurveyBuild.extend("QuantifyQu", "baseComponent", {
    itemName: "量表题",
    title: "量表题",
    StorageType:"S",
    qCode:"Q1",
	isAvg:"N",
    option: {},
    _init: function(d ,previewmode) {
        if ($.isEmptyObject(this.option)) {
            /*如果下拉框无选项值，将初始化this.option*/
        } else {
            /*如果下拉框有选项值，直接返回*/
            return;
        }
        var QuantifyQuValue = ["不满意","一般","满意","非常满意"];
        for (var i = 1; i <= 4; ++i) {
            this.option[d + i] = {
                code: i,
                txt: QuantifyQuValue[i-1],
                orderby: i,
                defaultval: 'N',
                other: 'N',
                weight: 0
            }
        }
    },
    _getHtml: function(data, previewmode) {
        var c = "",e = "";

        if (previewmode) {
            if(SurveyBuild.accessType == "P"){
                e += '<option style="height:30px;" value="">'+MsgSet["PLEASE_CHOOSE"]+'</option>';
                for (var i in data.option) {
                    e += '<option ' + (data.value == data["option"][i]["code"] || data["option"][i]["defaultval"] == "Y" ? "selected='selected'": "") + ' value="' + data["option"][i]["code"] + '" style="height:30px;">' + data["option"][i]["txt"] + '</option>';
                }
                c += '<div class="listcon">';
                c += '	<div class="question">';
                c += '      <span class="fontblue-blod">' + data.qCode + '.</span>' + data.title;
                c += '      <div id="' + data.itemId + 'Tip" class="onShow">';
                c += '          <div class="onShow"></div>';
                c += '       </div>';
                c += '  </div>';
                c += '	<div class="answer">';
                c += '		<div class="dropbox">';
                c += '			<select class="dropbox-con" name="' + data.itemId + '" id="' + data.itemId + '">' + e + '</select>';
                c += '		</div>';
                c += '	</div>';
                c += '</div>';
            }else{
                e += '<option style="height:30px;color:rgb(102,102,102);"  value="">'+MsgSet["PLEASE_CHOOSE"]+'</option>';
                for (var i in data.option) {
                    e += '<option ' + (data.value == data["option"][i]["code"]  || data["option"][i]["defaultval"] == "Y"? "selected='selected'": "") + ' value="' + data["option"][i]["code"] + '" style="height:30px;color:rgb(102,102,102);">' + data["option"][i]["txt"] + '</option>';
                }

                c += '<div class="listcon">';
                c += '  <div id="' + data.itemId + 'Tip" class="onShow">';
                c += '      <div class="onShow"></div>';
                c += '  </div>';
                c += '	<div class="question">';
                c += '		<span class="fontblue-blod">' + data.qCode + '.</span>' + data.title;
                c += '	</div>';
                c += '	<div class="dropbox-answer">';
                c += '		<div class="dropbox">';
                c += '			<select class="dropbox-con" name="' + data.itemId + '" id="' + data.itemId + '">' + e + '</select>';
                c += '		</div>';
                c += '	</div>';
                c += '</div>';
            }
        } else {
            for (var i in data.option) {
                e += '<li id="o' + i + '">' + data["option"][i]["txt"] + '</li>';
            }
            c += '<div class="question-answer">';
            c += '      <b class="read-select"> - '+MsgSet["PLEASE_CHOOSE"]+' - </b>';
            c += '      <ul class="select-box">' + e + '</ul>';
            c += '</div>'
        }
        return c;
    },
    _edit: function(data) {
        var e = '',list = "";
        for (var i in data.option) {
            list += '<tr class="read-radio" data-id="' + data.instanceId + '-' + i + '">';
            //默认
            list += '<td><input type="checkbox" onchange="$(\'.defaultval\').not(this).prop(\'checked\',false);SurveyBuild.saveLevel1Attr(this,\'defaultval\')" class="defaultval" ' + (data["option"][i]["defaultval"] == "Y" ? "checked='checked'": "") + ' value="1"></td>';
            //选项编码
            list += '<td><input type="text" onkeyup="SurveyBuild.saveLevel1Attr(this,\'code\')" value="' + data["option"][i]["code"] + '" oncontextmenu="return false;" ondragenter="return false" onpaste="return false" class="ocode"></td>';
            //描述
            list += '<td><input type="text" onkeyup="SurveyBuild.saveLevel1Attr(this,\'txt\')" value="' + data["option"][i]["txt"] + '" oncontextmenu="return false;" ondragenter="return false" class="option-txt"></td>';
            //分值
            list += '<td><input type="text" onkeyup="SurveyBuild.saveLevel1Attr(this,\'weight\')" value="' + data["option"][i]["weight"] + '" oncontextmenu="return false;" ondragenter="return false" onpaste="return false" class="ocode"></td>';
            //操作
            list += '<td><a onclick="SurveyBuild.plusOption(this);return false;" class="text-success" href="javascript:void(0);"><i class="icon-plus-sign"></i> </a><a onclick="SurveyBuild.minusOption(this);return false;" class="text-warning" href="javascript:void(0);"><i class="icon-minus-sign"></i> </a><a href="javascript:void(0);" class="text-info option-move"><i class="icon-move"></i> </a></td>';
            list += '</tr>';
        }

        e += '<fieldset id="option-box">';
        e += '  <span class="edit_item_label titlewidth"><i class="icon-list-alt"></i> 选项设置</span>';
        e += '  <table class="table table-bordered data-table">';
        e += '      <thead>';
        e += '          <tr>';
        e += '                  <th class="thw">默认</th>';
        e += '                  <th class="thw">编号</th>';
        e += '                  <th class="alLeft">描述<button onclick="SurveyBuild.optionBatch(\'' + data.instanceId + '\')" class="btn btn-primary btn-mini pull-right">批量编辑</button></th>';
        e += '                  <th>分值</th>';
        e += '                  <th width="45">操作</th>';
        e += '          </tr>';
        e += '      </thead>';
        e += '      <tbody class="ui-sortable">' + list + '</tbody>';
        e += '  </table>';
        e += '</fieldset>';

		//设置
        e += '<div class="edit_jygz">';
        e += '	<span class="title"><i class="icon-cog"></i> 设置</span>';
        e += '  <div class="groupbox">';
		e += '		<div class="edit_item_warp" style="margin-top:5px;">';
		e += '			<input class="mbIE" onchange="SurveyBuild.saveAttr(this,\'isAvg\')" ' + (data.isAvg == "Y" ? "checked='checked'": "") + ' id="isAvg" type="checkbox">';
		e += '			<label for="isAvg">是否计算平均分</label>';
		e += '		</div>';
		e += '	</div>';
		e += '</div>';

        //校验规则
        e += '<div class="edit_jygz">';
        e += '    <span class="title"><i class="icon-cog"></i> 校验规则</span>';
        e += '    <div class="groupbox">';
        e += '      <div class="edit_item_warp" style="margin-top:5px;">';
        e += '        <input class="mbIE" type="checkbox" onchange="SurveyBuild.saveAttr(this,\'isRequire\')"' + (data.isRequire == "Y" ? "checked='checked'": "") + ' id="is_require"> <label for="is_require">是否必填</label>';
        e += '      </div>';
        e += '    </div>';

        //高级设置
        e += '    <div class="edit_item_warp">';
        e += '        <a href="javascript:void(0);" onclick="SurveyBuild.RulesSet(this);"><i class="icon-cogs"></i> 高级设置</a>';
        e += '    </div>';
        e += '</div>';

        return e;
    },
    _eventbind: function(data) {
        var $inputBox = $("#" + data.itemId);
        $inputBox.on("change", function(){
            SurveyBuild.handleInput(this);
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