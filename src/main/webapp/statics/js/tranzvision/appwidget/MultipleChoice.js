/**
 * Created by admin on 2015/9/9.
 */
SurveyBuild.extend("MultipleChoice", "baseComponent", {
    itemName: "多选题",
    title:"多选题",
    maxSelect:"1",
    minSelect:"1",
    qCode:"Q1",
    StorageType:"D",
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
                othervalue :'',
                checked:"N"
            }
        }
    },
    _getHtml: function(data, previewmode) {
        var c = "",e = "";
        if (previewmode) {
            if(SurveyBuild.accessType == "P"){
                for (var i in data.option) {
                    e += '<li>';
                    e += '  <div class="input-checkbox-btn '+ (data["option"][i]["checked"] == "Y" ? "checked" : "") +'">';
                    e += '	    <input type="checkbox" onchange="SurveyBuild.handleInput(this);" ' + (data["option"][i]["checked"] == "Y" ? checked="checked" : "") + ' id="o' + data.itemId + data["option"][i]["code"] + '" instanceId="' + i + '"  name="' + data.itemId + '" value="' + data["option"][i]["code"] + '" />&nbsp;&nbsp;<label for="o' + data.itemId + data["option"][i]["code"] + '">' + data["option"][i]["txt"] + '</label>';
                    e += '  </div>';
                    if (data["option"][i]["other"]=="Y"){
                        e += '<input type="text" maxlength="40" class="input-text-comp" id="other' + data.itemId + '" style="display:' + (data["option"][i]["checked"]=="Y"?"inline-block":"none") + ';color: rgb(102, 102, 102); margin-left: 10px;" class="underline" value="' + data["option"][i]["othervalue"] + '">';
                    }
                    e += '</li>';
                }
                c += '<div class="listcon">';
                c += '	<div class="question">';
                c += '		<span class="fontblue-blod">' + data.qCode + '.</span>' + data.title;
                c += '      <div id="' + data.itemId + 'Tip" class="onShow">';
                c += '          <div class="onShow"></div>';
                c += '      </div>';
                c += '	</div>';
                c += '	<div class="answer" id="' + data.itemId + '">';
                c += '		<ul>' + e +  '</ul>';
                c += '	</div>';
                c += '</div>';
            }else{
                for (var i in data.option) {
                    e += '<li>';
                    e += '  <div class="input-checkbox-btn '+ (data["option"][i]["checked"] == "Y" ? "checked" : "") +'">';
                    e += '      <input type="checkbox" onchange="SurveyBuild.handleInput(this);" ' + (data["option"][i]["checked"] == "Y" ? checked="checked" : "") + ' id="o' + data.itemId + data["option"][i]["code"] + '" instanceId="' + i + '"  name="' + data.itemId + '" value="' + data["option"][i]["code"] + '" />&nbsp;&nbsp;';
                    e += '      <label for="o' + data.itemId + data["option"][i]["code"] + '">' + data["option"][i]["txt"] + '</label>';
                    e += '  </div>';
                    if (data["option"][i]["other"] == "Y"){
                        e += '<input type="text" maxlength="40" class="input-text-comp" id="other' + data.itemId + '" style="display:' + (data["option"][i]["checked"]=="Y"?"inline-block":"none") + '" class="othertext" value="' + data["option"][i]["othervalue"] + '">';
                    }
                    e += '</li>';
                }

                c += '<div class="listcon">';
                c += '  <div id="' + data.itemId + 'Tip" class="onShow">';
                c += '      <div class="onShow"></div>';
                c += '  </div>';
                c += '  <div class="question">';
                c += '		<span class="fontblue-blod">' + data.qCode + '.</span>' + data.title;
                c += '	</div>';
                c += '  <div class="answer" id="' + data.itemId + '">';
                c += '		<ul>' + e +  '</ul>';
                c += '	</div>';
                c += '</div>';
            }
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
        e += '	<span class="edit_item_label titlewidth"><i class="icon-list-alt"></i> 选项设置</span>';
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

        //校验规则
        e += '	<div class="edit_jygz">';
        e += '	    <span class="title"><i class="icon-cog"></i> 校验规则</span>';
        e += '      <div class="groupbox">';
        e += '		    <div class="edit_item_warp" style="margin-top:5px;">';
        e += '			    <input type="checkbox" onchange="SurveyBuild.saveAttr(this,\'isRequire\')"' + (data.isRequire == "Y" ? "checked='checked'": "") + ' id="is_require"> <label for="is_require">是否必填</label>';
        e += '		    </div>';
        //个数范围
        e += '          <div class="edit_item_warp">';
        e += '              <input type="checkbox" onchange="SurveyBuild.saveAttr(this,\'isNumSize\')"' + (data.isNumSize == "Y" ? "checked='checked'": "") + ' id="is_checkNumSize">';
        e += '                <label for="is_checkNumSize">个数范围：</label>';
        e += '          </div>';

        //最少选择
        e += '	        <div class="edit_item_warp">';
        e += '		        <span class="edit_item_label">最少选择：</span>';
        e += '		        <input type="text" maxlength="11" class="medium minSelect" data_id="' + data.instanceId + '" onkeyup="SurveyBuild.saveAttr(this,\'minSelect\')" value="' + data.minSelect + '"/>';
        e += '	        </div>';

        //最多选择
        e += '	        <div class="edit_item_warp mb10">';
        e += '		        <span class="edit_item_label">最多选择：</span>';
        e += '		        <input type="text" maxlength="11" class="medium maxSelect" data_id="' + data.instanceId + '" onkeyup="SurveyBuild.saveAttr(this,\'maxSelect\')" value="' + data.maxSelect + '"/>';
        e += '	        </div>';
        e += '      </div>';

        //高级设置
        e += '		<div class="edit_item_warp">';
        e += '			<a href="javascript:void(0);" onclick="SurveyBuild.RulesSet(this);"><i class="icon-cogs"></i> 高级设置</a>';
        e += '		</div>';
        e += '</div>';
        return e;
    },
    _validatorAttr: function(data){
        var $min = $("#question-edit .minSelect");
        var $max = $("#question-edit .maxSelect");
        if (isNaN(data.minSelect)){
            SurveyBuild.fail($min, "请填写数字！");
            return false;
        }
        if (isNaN(data.maxSelect)){
            SurveyBuild.fail($max, "请填写数字！");
            return false;
        }
        if (data.isNumSize == "Y"){
            if (data.minSelect == ""){
                SurveyBuild.fail($min, "请填写最少选择个数！");
                return false;
            }
            if (data.minSelect <  1){
                SurveyBuild.fail($min, "最少选择一个选项！");
                return false;
            }
            if (data.maxSelect == ""){
                SurveyBuild.fail($max, "请填写最多选择个数！");
                return false;
            }
            if (parseInt(data.minSelect,10) <= parseInt(data.maxSelect,10)){
                return true;
            }else{
                SurveyBuild.fail($max, "最多选择个数不能小于最少选择个数！");
                return false;
            }
        } else {
            return true;
        }
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
                    $(this).closest(".input-checkbox-btn").addClass("checked");
                }else{
                    data["option"][meid]["checked"] = "N";
                    if (opt["other"] == "Y"){
                        $("#other" + data.itemId).css("display","none");
                    }
                    $(this).closest(".input-checkbox-btn").removeClass("checked");
                }
            });
            if(opt["other"] == "Y"){
                $("#other" + data.itemId).keyup(function(){
                    data["option"][i]["othervalue"] = $(this).val();
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
                    //选择个数
                    if(classname == "NumSizeValidator" && data.isNumSize == "Y"){
                        allowEmpty = false;
                        ReqErrorMsg = data["rules"]["RequireValidator"]["messages"];
                        NumErrorMsg = classObj["messages"];
                    }
                }
            });
        }
        $inputBox.formValidator({tipID:(data["itemId"] + 'Tip'), onShow:"&nbsp;", onFocus:"&nbsp;", onCorrect:"&nbsp;", empty:allowEmpty, onEmpty:ReqErrorMsg});
        if(!allowEmpty){
            $inputBox.inputValidator({min:1, onError:ReqErrorMsg});
        }
        if (data.isNumSize == "Y"){
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
                        if ($.inArray(classname, SurveyBuild._baseRules) == -1 && data["rules"][classname]["isEnable"] == "Y" && classname != "RequireValidator" && classname != "NumSizeValidator") {
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