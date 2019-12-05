/**
 * Created by WRL on 2015/5/13.
 * 证件类型
 */
SurveyBuild.extend("CertificateNum", "baseComponent", {
    itemName: "证件类型",
    title:"证件类型",
    isSingleLine: "Y",
    children: [
        {
            "itemId": "com_CerType",
            "itemName": "证件类型",
            "title": "证件类型",
            "value": "1",
            "orderby":1,
            "StorageType": "S",
            "option": {},
            "classname":"Select"
        },
        {
            "itemId": "com_CerNum",
            "itemName": "证件号码",
            "title": "证件号码",
            "value": "",
            "orderby":2,
            "StorageType": "S",
            "classname":"SingleTextBox"
        }
    ],
    _init: function(d, previewmode) {
        if ($.isEmptyObject(this.children[0]["option"])) {
            /*如果下拉框无选项值，将初始化this.option*/
        } else {
            /*如果下拉框有选项值，直接返回*/
            return;
        }

		var degree = [MsgSet["IDNUM_SFZ"],MsgSet["IDNUM_HKB"],MsgSet["IDNUM_HZ"],MsgSet["IDNUM_JUNGZ"],MsgSet["IDNUM_SBZ"],MsgSet["IDNUM_GATXZ"],MsgSet["IDNUM_TWTXZ"],MsgSet["IDNUM_LSSFZ"],MsgSet["IDNUM_WGRJLZ"],MsgSet["IDNUM_JINGGZ"],MsgSet["IDNUM_QT"]];
        for (var i = 1; i <= 11; ++i) {
        	this.children[0]["option"][d + i] = {
                code: i,
                txt: degree[i-1],
                orderby: i,
                defaultval: 'N',
                other: 'N',
                weight: 0
            }
        }
    },
    _getHtml: function(data, previewmode) {
        var c = "",children = data.children;
        if (previewmode) {
           if(SurveyBuild._readonly){
                //只读模式
				var valDesc = "";
				for (var i in children[0].option) {
					if(children[0]["value"] == children[0]["option"][i]["code"]){
						valDesc = children[0]["option"][i]["txt"];
					}
				}
                c += '<div class="input-list">';
                c += '	<div class="input-list-info left"><span class="red">' + (data.isRequire == "Y" ? "*": "") + '</span>' + data.title + '</div>';
                c += '	<div class="input-list-text left"><span style="line-height:24px">' + valDesc + '<br>' + children[1]["value"] + '</span></div>';
                c += '	<div class="input-list-suffix left"></div>';
                c += '	<div class="clear"></div>';
                c += '</div>';
            }else{
                //填写模式
                SurveyBuild.appInsId == "0" && this._getDefaultVal(data,"P2");

                c += '<div class="input-list">';
                c += '	<div class="input-list-info left"><span class="red">' + (data.isRequire == "Y" ? "*": "") + '</span>' + data.title + '</div>';
                c += '    <div class="input-list-textdate left input-date-select" style="width:12.5%">';
                c += '			<select style="width:100%;" title="' + children[0]["itemName"] + '" id="' + data["itemId"] + children[0]["itemId"] + '" class="chosen-select" value="' + children[0]["value"] + '" name="' + data["itemId"] + children[0]["itemId"] + '">';
                c += '			<option value="">' + MsgSet["PLEASE_SELECT"] + '</option>';
                for (var i in children[0]["option"]) {
                    c += '<option ' + (children[0].value == children[0]["option"][i]["code"] ? "selected='selected'": "") + 'value="' + children[0]["option"][i]["code"] + '">' + children[0]["option"][i]["txt"] + '</option>';
                }
                c += '			</select>';
                c += '	</div>';
                c += '	<div class="input-list-textdate left input-date-select" style="width: 21%; margin: 0px 15px 0 0;">';
                c += '    	<input type="text" class="inpu-list-text-enter" name="' + data["itemId"] + children[1]["itemId"] + '" id="' + data["itemId"] + children[1]["itemId"] + '" title="' + children[1]["itemName"] + '" value="' + children[1]["value"] + '">';
                c += '    </div>';
                c += '    <div class="input-list-suffix left"><div id="' + data["itemId"] + 'Tip" class="onShow"><div class="onShow">&nbsp;</div></div></div>';
                c += '    <div class="clear"></div>';
                c += '</div>';
            }
        } else {
            c += '<div class="question-answer">';
            c += '  <b style="min-width: 80px" class="read-select">证件类型</b>';
            c += '  <b style="min-width: 200px;" class="read-input">&nbsp;</b>';
            c += '</div>';
        }
        return c;
    },
    _edit: function(data) {
        var e = '';

        e += '<div class="edit_item_warp">';
        e += '  <span class="edit_item_label">默认值：</span>';
        e += '  <input type="text" class="medium" id="defaultval" onkeyup="SurveyBuild.saveAttr(this,\'defaultval\')" value="' + data.defaultval + '"/>';
        e += '</div>';

        e += '<div class="edit_item_warp" style="text-align:right;">';
        e += '  <a href="javascript:void(0);" onclick="SurveyBuild.DynamicBindVal()" class="">动态绑定值</a>';
        e += '</div>';
		
		e += '<div class="edit_item_warp">';
		e += '  <span class="edit_item_label">关联项：</span>';
		e += '  <input type="text" class="medium" id="linkItems" onkeyup="SurveyBuild.saveAttr(this,\'linkItems\')" value="' + data.linkItems + '"/>';
		e += '</div>';

        e += '<div class="edit_jygz">';
		e += '	<span class="title"><i class="icon-cog"></i> 校验规则</span>';
		e += '  <div class="groupbox">';
        e += '  <div class="edit_item_warp" style="margin-top:5px;">';
        e += '      <input class="mbIE" type="checkbox" onchange="SurveyBuild.saveAttr(this,\'isRequire\')"' + (data.isRequire == "Y" ? "checked='checked'": "") + ' id="is_require"> <label for="is_require">是否必填</label>';
        e += '  </div>';
		e += '  </div>';
        e += '	<div class="edit_item_warp">';
        e += '		<a href="javascript:void(0);" onclick="SurveyBuild.RulesSet(this);"><i class="icon-cogs"></i> 高级设置</a>';
        e += '	</div>';
        e += '</div>';
        return e;
    },
    _eventbind: function(data) {
        var $cerType = $("#" + data["itemId"] + data.children[0]["itemId"]);
        var $cerCode = $("#" + data["itemId"] + data.children[1]["itemId"]);
        /*身份证*/
        if($cerType.val() == "1"){
            //return "身份证输入不合法！";
            $cerCode.attr("data-regular","/(^\\d{15}$)|(^\\d{18}$)|(^\\d{17}(\\d|X|x)$)/");
        }else{
        	$cerCode.attr("data-regular","");
        }


        $cerType.change(function() {
            var cerCodeType = $(this).children('option:selected').val();
            /*身份证*/
            if(cerCodeType == "1"){
                //return "身份证输入不合法！";
                $cerCode.attr("data-regular","/(^\\d{15}$)|(^\\d{18}$)|(^\\d{17}(\\d|X|x)$)/");
            }else{
            	$cerCode.attr("data-regular","");
            }
			
			/*身份证下拉框关联项*/
			if(data.linkItems){
				var $linkitemsCerType = $("#" + data.linkItems + data.children[0]["itemId"]);
				if($linkitemsCerType && $linkitemsCerType.length > 0){
					$linkitemsCerType.val(cerCodeType);
					$linkitemsCerType.chosen("destroy").chosen();
					$linkitemsCerType.trigger("blur");
				}
			}
			
        });
		
		/*身份证号码关联项*/
		if(data.linkItems){
			var $linkitemsCerCode = $("#" + data.linkItems + data.children[1]["itemId"]);
			if($linkitemsCerCode && $linkitemsCerCode.length > 0){
				$cerCode.change(function(e) {
					$linkitemsCerCode.val($cerCode.val());
					$linkitemsCerCode.trigger("change");
					$linkitemsCerCode.trigger("blur");
				});
			}
		}

        $cerCode.formValidator({tipID:(data["itemId"] + 'Tip'),onShow:"",onFocus:"&nbsp;",onCorrect:"&nbsp;"});
        $cerCode.functionValidator({
            fun:function(val,elem){

                var _result = true;
                if (ValidationRules) {
                    $.each(data["rules"],function(classname, classObj) {
                        //单选钮不需要在高级规则中的必选判断
                        if ($.inArray(classname, SurveyBuild._baseRules) == -1 && data["rules"][classname]["isEnable"] == "Y") {
                            var _ruleClass = ValidationRules[classname];
                            if (_ruleClass && _ruleClass._Validator) {
                                _result = _ruleClass._Validator(data["itemId"] + data.children[1]["itemId"], classObj["messages"], data);
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
})
