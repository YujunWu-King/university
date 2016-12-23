/**
 * Created by WRL on 2015/9/9.
 */
SurveyBuild.extend("RadioBox", "baseComponent", {
    itemName: "单选题",
    title: "单选题",
    StorageType:"D",
    qCode:"Q1",
	format: "",
	isAvg:"N",
    othervalue:"",
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
                weight: 0,
                othervalue :'',
                checked:"N"
            }
        }
    },
    _getHtml: function(data, previewmode) {
        var c = "",e = "";
		if(!data.hasOwnProperty("format")){
			data["format"] = "";
		}
        if (previewmode) {
            if(SurveyBuild.accessType == "P"){

                for (var i in data.option) {
                    e += '<li>';
					e += '	<div class="input-radio-btn '+ (data["option"][i]["checked"] == "Y" ? "checked" : "") +'">';
                    e += '  <input type="radio" onchange="SurveyBuild.handleInput(this);" ' + (data["option"][i]["checked"] == "Y" ? checked="checked" : "") + ' id="o' + data.itemId + data["option"][i]["code"] + '" instanceId="' + i + '" name="' + data.itemId + '" value="' + data["option"][i]["code"] + '" >&nbsp;&nbsp;'
                    e += '  <label for="o' + data.itemId + data["option"][i]["code"] + '">' + data["option"][i]["txt"] + '</label>'
					e += ' </div>';
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
                c += '	<div class="answer' + data["format"] + '" id="' + data.itemId + '">';
                c += '		<ul>' + e +  '</ul>';
                c += '	</div>';
                c += '</div>';
            }else{
                for (var i in data.option) {
                    e += '<li>';
                    e += '  <div class="input-radio-btn '+ (data["option"][i]["checked"] == "Y" ? "checked" : "") +'"><input type="radio" onchange="SurveyBuild.handleInput(this);" ' + (data["option"][i]["checked"] == "Y" ? checked="checked" : "") + ' id="o' + data.itemId + data["option"][i]["code"] + '" instanceId="' + i + '" name="' + data.itemId + '" value="' + data["option"][i]["code"] + '">&nbsp;&nbsp;';
                    e += '  <label for="o' + data.itemId + data["option"][i]["code"] + '">' + data["option"][i]["txt"] + '</label></div>';
                    if (data["option"][i]["other"] == "Y"){
                        e += '<input type="text" maxlength="40" class="input-text-comp" id="other' + data.itemId + '" style="display:' + (data["option"][i]["checked"]=="Y"?"inline-block":"none") + '" class="othertext" value="' + data["option"][i]["othervalue"] + '">';
                    }
                    e += '</li>';
                }

                c += '<div class="listcon">';
                c += '  <div id="' + data.itemId + 'Tip" class="onShow">';
                c += '      <div class="onShow"></div>';
                c += '  </div>';
                c += '	<div class="question">';
                c += '		<span class="fontblue-blod">' + data.qCode + '.</span>' +  data.title;
                c += '	</div>';
                c += '	<div class="answer" id="' + data.itemId + '">';
                c += '		<ul>' + e + '</ul>';
                c += '	</div>';
                c += '</div>';
            }
        } else {
            for (var i in data.option) {
                e += '<li class="read-radio" id="o' + i + '">';
                e += data["option"][i]["txt"];
                if(data["option"][i]["other"] == "Y"){
                    e += '<b class="read-input"></b>';
                }
                e += '</li>';
            }
            c = '<div class="question-answer"><ul class="format format' + (data["format"]?data["format"]:'1') + '">' + e + '</ul></div>'
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
            //选项编码
            list += '<td><input type="text" onkeyup="SurveyBuild.saveLevel1Attr(this,\'code\')" value="' + data["option"][i]["code"] + '" oncontextmenu="return false;" ondragenter="return false" onpaste="return false" class="ocode"></td>';
            //描述
            list += '<td><input type="text" onkeyup="SurveyBuild.saveLevel1Attr(this,\'txt\')" value="' + data["option"][i]["txt"] + '" oncontextmenu="return false;" ondragenter="return false" class="option-txt"></td>';
            //分值
            list += '<td><input type="text" onkeyup="SurveyBuild.saveLevel1Attr(this,\'weight\')" value="' + data["option"][i]["weight"] + '" oncontextmenu="return false;" ondragenter="return false" onpaste="return false" class="ocode"></td>';
            //操作
            list += '<td><a onclick="SurveyBuild.plusOption_radio(this);return false;" class="text-success" href="javascript:void(0);"><i class="icon-plus-sign"></i> </a><a onclick="SurveyBuild.minusOption(this);return false;" class="text-warning" href="javascript:void(0);"><i class="icon-minus-sign"></i> </a><a href="javascript:void(0);" class="text-info option-move"><i class="icon-move"></i> </a></td>';
            list += '</tr>';
        }

        e += '<fieldset id="option-box">';
        e += '		<span class="edit_item_label titlewidth"><i class="icon-list-alt"></i> 选项设置</span>';
        e += '		<table class="table table-bordered data-table">';
        e += '			<thead>';
        e += '				<tr>';
        e += '					<th class="thw">默认</th>';
        e += '					<th class="thw">其他</th>';
        e += '					<th class="thw">编号</th>';
        e += '					<th class="alLeft">描述<button onclick="SurveyBuild.optionBatch(\'' + data.instanceId + '\')" class="btn btn-primary btn-mini pull-right">批量编辑</button></th>';
        e += '                  <th>分值</th>';
        e += '					<th width="45">操作</th>';
        e += '				</tr>';
        e += '			</thead>';
        e += '			<tbody class="ui-sortable">' + list + '</tbody>';
        e += '		</table>';
        e += '</fieldset>';

        e += '<div class="question-type clearfix">';
        e += '	<fieldset>';
        e += '		<legend>选项排列</legend>';
        e += '		<select onchange="SurveyBuild.saveAttr(this,\'format\')" data-id="' + data.instanceId + '">';
        e += '			<option value="1" ' + (data.format == "" ? "selected='selected'": "") + '>竖排</option>';
        e += '			<option value="2" ' + (data.format == "2" ? "selected='selected'": "") + '>垂直2列</option>';
        e += '			<option value="3" ' + (data.format == "3" ? "selected='selected'": "") + '>垂直3列</option>';
        e += '			<option value="4" ' + (data.format == "4" ? "selected='selected'": "") + '>垂直4列</option>';
        e += '			<option value="5" ' + (data.format == "5" ? "selected='selected'": "") + '>垂直5列</option>';
        e += '			<option value="6" ' + (data.format == "6" ? "selected='selected'": "") + '>垂直6列</option>';
        e += '		</select>';
        e += '	</fieldset>';
        e += '</div>';
        /* 暂时注释 By WRL @2015-10-21
        //默认值
        e += '<div class="edit_item_warp">';
        e += '    <span class="edit_item_label">默认值：</span>';
        e += '   <input type="text" class="medium" id="defaultval" onkeyup="SurveyBuild.saveAttr(this,\'defaultval\')" value="' + data.defaultval + '"/>';
        e += '</div>';
        */
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
        e += '	<span class="title"><i class="icon-cog"></i> 校验规则</span>';
        e += '  <div class="groupbox">';
        e += '	    <div class="edit_item_warp" style="margin-top:5px;">';
        e += '		    <input class="mbIE" type="checkbox" onchange="SurveyBuild.saveAttr(this,\'isRequire\')"' + (data.isRequire == "Y" ? "checked='checked'": "") + ' id="is_require"> <label for="is_require">是否必填</label>';
        e += '	    </div>';
        e += '	</div>';
        //高级设置
        e += '	<div class="edit_item_warp">';
        e += '		<a href="javascript:void(0);" onclick="SurveyBuild.RulesSet(this);"><i class="icon-cogs"></i> 高级设置</a>';
        e += '	</div>';
        e += '</div>';
        return e;
    },
    _eventbind: function(data) {
        $.each(data.option,function(i,opt){
            $("#o" + data.itemId + opt["code"]).click(function(){
                var meid = $(this).attr("instanceId");
                for (var j in data.option) {
                    data.option[j]["checked"] = "N";
                }
                data.option[meid]["checked"] = "Y";
                if(opt["other"] == "Y"){
                    $("#other" +  data.itemId).css("display","inline-block");
                } else {
                    $("#other" +  data.itemId).css("display","none");
                    $("#other" +  data.itemId).val("");
                    data["option"][i]["othervalue"] = "";
                }
				
				$(this).closest("ul").children("li").children(".input-radio-btn").removeClass("checked");
				$(this).closest(".input-radio-btn").addClass("checked");
            });
            if(opt["other"] == "Y"){
                $("#other" +  data.itemId).keyup(function(){
                    data["option"][i]["othervalue"] = $(this).val();
                });
            }
        });

        var $inputBox = $(":radio[name='"+ data.itemId +"']");
        //判断是否为必填
        var allowEmpty = true;
        var errorMsg = "&nbsp;";
        if (ValidationRules) {
            $.each(data["rules"],function(classname, classObj) {
                if ($.inArray(classname, SurveyBuild._baseRules) == -1 && data["rules"][classname]["isEnable"] == "Y") {
                    //必填校验
                    if(classname == "RequireValidator" && data.isRequire == "Y"){
                        allowEmpty = false;
                        errorMsg = classObj["messages"];
                    }
                }
            });
        }

        $inputBox.formValidator({tipID:(data["itemId"]+'Tip'), onShow:"&nbsp;", onFocus:"&nbsp;", onCorrect:"&nbsp;"});
        if(!allowEmpty){
            $inputBox.inputValidator({min:1, onError:errorMsg});
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
                        //单选钮不需要在高级规则中的必选判断
                        if ($.inArray(classname, SurveyBuild._baseRules) == -1 && data["rules"][classname]["isEnable"] == "Y" && classname!="RequireValidator") {
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