/**功能描述：在线调查多行文本框
 * 开发人：LDD
 * 开发时间:2015/11/20
 */
SurveyBuild.extend("MultipleTextBox", "baseComponent", {
    itemName: "多行文本框",
    title:"多行文本框",
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
                txt: "label" + c,
                orderby: c,
                defaultval: 'N',
                other: 'Y',
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
                //电脑版
                for (var i in data.option) { 
                    e += '<li class="overhidden ">';
                    e += '<p class="fl labelp">' + data["option"][i]["txt"] + '</p>';
                    e += '  <input type="text" class="fr" id="o' + data.itemId + data["option"][i]["code"] + '"name="o'+data.itemId + data["option"][i]["code"]+'"value="' + data["option"][i]["othervalue"] + '">';
                    e += '</li>';	 
                } 
				c += '<div class="listcon">';
				c += '	<div class="list_q">';
                c += '		<b>'+ data.qCode + '.</b>'+ data.title+'<span>'+(data.isRequire == "Y" ? "*": "")+'</span>';
                c += '	</div>';
                c += '	<div id="' + data.itemId + 'Tip" class="tips">';
                c += '		<img src="' + TzUniversityContextPath + '/statics/js/onlineSurvey/formvalidator/m/images/onError.gif">';
                c += '		<span></span>';
                c += '	</div>';
                c += '	<div class="text_box2"  name="'+ data.itemId +'" id="'+ data.itemId +'">';
                c +=  e ; 
                c += '	</div>';
                c += '</div>';
            }else{
                //手机版
                for (var i in data.option) { 
                    e += '<li class="overhidden ">';
                    e += '<p class="fl labelp">' + data["option"][i]["txt"] + '</p>';
                    e += '  <input type="text" class="fl w_70" id="o' + data.itemId + data["option"][i]["code"] + '"name="o'+data.itemId + data["option"][i]["code"]+'"value="' + data["option"][i]["othervalue"] + '">';
                    e += '</li>';	 
                } 
				c += '<div class="listcon">';
				c += '	<div class="list_q">';
                c += '		<b>'+ data.qCode + '.</b>'+ data.title+'<span>'+(data.isRequire == "Y" ? "*": "")+'</span>';
                c += '	</div>';
                c += '	<div id="' + data.itemId + 'Tip" class="tips">';
                c += '		<img src="' + TzUniversityContextPath + '/statics/js/onlineSurvey/formvalidator/m/images/onError.gif">';
                c += '		<span></span>';
                c += '	</div>';
                c += '	<div class="text_box2"  name="'+ data.itemId +'" id="'+ data.itemId +'">';
                c +=  e  ; 
                c += '	</div>';
                c += '</div>';
            }
        } else {
            //从设置页面点击编辑按钮 进入
			for (var i in data.option) {
				e += '<tr style="height:40px;" id="tr'+ i +'">';
				e += '	<td style="max-width:200px;" id="o' + i + '">'+data["option"][i]["txt"]+'</td>'
				e += '	<td style="padding-left:10px;"><b class="read-input" style="height:32px;"></b></td>'
				e += '</tr>';
            }
            c = '<div class="question-answer"><table>' + e + '</table>'
        }
        return c;
    },
    _edit: function(data) {
        var e = '',list = "";

        for (var i in data.option) {
            list += '<tr class="read-radio" data-id="' + data.instanceId + '-' + i + '">';
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

        //高级设置
        e += '		<div class="edit_item_warp">';
        e += '			<a href="javascript:void(0);" onclick="SurveyBuild.RulesSet(this);"><i class="icon-cogs"></i> 高级设置</a>';
        e += '		</div>';
        e += '</div>';
        return e;
    },
    _eventbind: function(data) {
        //事件绑定
        $.each(data.option,function(i,opt){
            $("#o" +  data.itemId + opt["code"]).keyup(function(){
                data["option"][i]["othervalue"] = $(this).val();
                if(data["option"][i]["othervalue"]){
                    data["option"][i]["checked"] = "Y";
                }else{
                    data["option"][i]["checked"] = "N";
                }
            });
            $("#o" +  data.itemId + opt["code"]).change(function(){
                data["option"][i]["othervalue"] = $(this).val();
                if(data["option"][i]["othervalue"]){
                    data["option"][i]["checked"] = "Y";
                }else{
                    data["option"][i]["checked"] = "N";
                }
            });
        });
        var $inputBox = $("[name='" + data.itemId + "']");
        $inputBox.formValidator({tipID: (data["itemId"] + 'Tip'),onShow: "",onFocus: "&nbsp;",onCorrect: "&nbsp;"});

        if (ValidationRules) {
            $.each(data["rules"],function(classname, classObj) {
                if ($.inArray(classname, SurveyBuild._baseRules) == -1 && data["rules"][classname]["isEnable"] == "Y") {
                    //必填校验
                    if(classname == "RequireValidator" && data.isRequire == "Y"){
                        allowEmpty = false;
                        ReqErrorMsg = classObj["messages"];
                    }
                }
            });
        }
        $inputBox.formValidator({tipID: (data["itemId"] + 'Tip'),onShow: "",onFocus: "&nbsp;",onCorrect: "&nbsp;"});
        $inputBox.functionValidator({
            fun:function(val,el){
                if (data.isRequire == "Y"){
                    var hasVal;
                    $.each(data.option,function(i,opt){
                        var obj = $("#o" + data.itemId + opt["code"]);
                        if(obj && obj.val()){
                            hasVal = obj.val();
                            return false;
                        }else{
                            return true;
                        }
                    });

                    if(hasVal){
                        return true;
                    }else{
                        return ReqErrorMsg;
                    }
                }
            }
        });
    }
});