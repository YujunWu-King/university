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
    _getHtml: function(data, previewmode) {
        var c = "",children = data.children;
        if (previewmode) {
           if(SurveyBuild._readonly){
                //只读模式
               var valDesc = "";
               switch(children[0]["value"]){
                   case "1":
                       valDesc = MsgSet["IDNUM_SFZ"];
                       break;
                   case "2":
                       valDesc = MsgSet["IDNUM_HKB"];
                       break;
                   case "3":
                       valDesc = MsgSet["IDNUM_HZ"];
                       break;
                   case "4":
                       valDesc = MsgSet["IDNUM_JUNGZ"];
                       break;
                   case "5":
                       valDesc = MsgSet["IDNUM_SBZ"];
                       break;
                   case "6":
                       valDesc = MsgSet["IDNUM_GATXZ"];
                       break;
                   case "7":
                       valDesc = MsgSet["IDNUM_TWTXZ"];
                       break;
                   case "8":
                       valDesc = MsgSet["IDNUM_LSSFZ"];
                       break;
                   case "9":
                       valDesc = MsgSet["IDNUM_WGRJLZ"];
                       break;
                   case "10":
                       valDesc = MsgSet["IDNUM_JINGGZ"];
                       break;
                   case "11":
                       valDesc = MsgSet["IDNUM_QT"];
                       break;
               }
                c += '<div class="main_inner_content_info_autoheight cLH">';
                c += '  <div class="main_inner_connent_info_left">';
                c += '      <span class="reg_title_star">' + (data.isRequire == "Y" ? "*": "") + '</span>' + data.title;
                c += '  </div>';
                c += '  <div class="main_inner_content_info_right" >';
                c += '      <span style="line-height:25px;">' + valDesc + '<br>' + children[1]["value"] + '</span>';
                c += '  </div>';
                c += '</div>'
            }else{
                //填写模式
                SurveyBuild.appInsId == "0" && this._getDefaultVal(data,"P2");
                c += '<div class="main_inner_content_info_autoheight">';
                c += '  <div class="main_inner_connent_info_left">';
                c += '      <span class="reg_title_star">' + (data.isRequire == "Y" ? "*": "") + '</span>' + data.title;
                c += '  </div>';
                c += '  <div class="main_inner_content_info_right">';
                c += '      <div class="main_inner_content_info_right_l100px">';
                c += '          <select title="' + children[0]["itemName"] + '" id="' + data["itemId"] + children[0]["itemId"] + '" class="chosen-select input_100px" value="' + children[0]["value"] + '" name="' + data["itemId"] + children[0]["itemId"] + '">';
                c += '              <option value="1" ' + (children[0]["value"] == "1" ? "selected='selected'": "") + '>'+MsgSet["IDNUM_SFZ"]+'</option>';
                c += '              <option value="2" ' + (children[0]["value"] == "2" ? "selected='selected'": "") + '>'+MsgSet["IDNUM_HKB"]+'</option>';
                c += '              <option value="3" ' + (children[0]["value"] == "3" ? "selected='selected'": "") + '>'+MsgSet["IDNUM_HZ"]+'</option>';
                c += '              <option value="4" ' + (children[0]["value"] == "4" ? "selected='selected'": "") + '>'+MsgSet["IDNUM_JUNGZ"]+'</option>';
                c += '              <option value="5" ' + (children[0]["value"] == "5" ? "selected='selected'": "") + '>'+MsgSet["IDNUM_SBZ"]+'</option>';
                c += '              <option value="6" ' + (children[0]["value"] == "6" ? "selected='selected'": "") + '>'+MsgSet["IDNUM_GATXZ"]+'</option>';
                c += '              <option value="7" ' + (children[0]["value"] == "7" ? "selected='selected'": "") + '>'+MsgSet["IDNUM_TWTXZ"]+'</option>';
                c += '              <option value="8" ' + (children[0]["value"] == "8" ? "selected='selected'": "") + '>'+MsgSet["IDNUM_LSSFZ"]+'</option>';
                c += '              <option value="9" ' + (children[0]["value"] == "9" ? "selected='selected'": "") + '>'+MsgSet["IDNUM_WGRJLZ"]+'</option>';
                c += '              <option value="10" ' + (children[0]["value"] == "10" ? "selected='selected'": "") + '>'+MsgSet["IDNUM_JINGGZ"]+'</option>';
                c += '              <option value="11" ' + (children[0]["value"] == "11" ? "selected='selected'": "") + '>'+MsgSet["IDNUM_QT"]+'</option>';
                c += '          </select>';
                c += '      </div>';

                c += '      <div class="main_inner_content_info_right_r145px">';
                c += '          <input type="text" name="' + data["itemId"] + children[1]["itemId"] + '" class="input_145px" id="' + data["itemId"] + children[1]["itemId"] + '" title="' + children[1]["itemName"] + '" value="' + children[1]["value"] + '">';
                c += '      </div>';

                c += '      <div style="margin-top:-40px;margin-left:256px;float:left;">';
                c += '          <div id="' + data["itemId"] + 'Tip" style="margin: 0px; padding: 0px; background: none repeat scroll 0% 0% transparent;" class="onShow">';
                c += '              <div class="onShow">&nbsp;</div>';
                c += '          </div>';
                c += '      </div>';
                c += '  </div>';
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
        }
        /*户口簿*/
        if($cerType.val() == "2"){
            //return "户口簿XXXXXXXXXXXXXX！";
            $cerCode.attr("data-regular","/^[A-Za-z,\\s]+$/");
        }
        /*护照*/
        if($cerType.val() == "3"){
            //return "护照XXXXXXXXXXXXXX！";
            $cerCode.attr("data-regular","/^[A-Za-z,\\s]+$/");
        }
        /*军官证*/
        if($cerType.val() == "4"){
            //return "军官证XXXXXXXXXXXXXX！";
            $cerCode.attr("data-regular","/^[A-Za-z,\\s]+$/");
        }
        /*士兵证*/
        if($cerType.val() == "5"){
            //return "士兵证XXXXXXXXXXXXXX！";
            $cerCode.attr("data-regular","/^[A-Za-z,\\s]+$/");
        }
        /*港澳居民来往内地通行证*/
        if($cerType.val() == "6"){
            //return "港澳居民来往内地通行证XXXXXXXXXXXXXX！";
            $cerCode.attr("data-regular","/^[A-Za-z,\\s]+$/");
        }
        /*台湾同胞来往内地通行证*/
        if($cerType.val() == "7"){
            //return "台湾同胞来往内地通行证XXXXXXXXXXXXXX！";
            $cerCode.attr("data-regular","/^[A-Za-z,\\s]+$/");
        }
        /*临时身份证*/
        if($cerType.val() == "8"){
            //return "临时身份证XXXXXXXXXXXXXX！";
            $cerCode.attr("data-regular","/^[A-Za-z,\\s]+$/");
        }
        /*外国人居留证*/
        if($cerType.val() == "9"){
            // return "外国人居留证XXXXXXXXXXXXXX！";
            $cerCode.attr("data-regular","/^[A-Za-z,\\s]+$/");
        }
        /*警官证*/
        if($cerType.val() == "10"){
            //return "警官证XXXXXXXXXXXXXX！";
            $cerCode.attr("data-regular","/^[A-Za-z,\\s]+$/");
        }
        /*其他证件*/
        if($cerType.val() == "10"){
            //return "其他证件XXXXXXXXXXXXXX！";
            $cerCode.attr("data-regular","/^[A-Za-z,\\s]+$/");
        }


        $cerType.change(function() {
            var cerCodeType = $(this).children('option:selected').val();

            /*身份证*/
            if(cerCodeType == "1"){
                //return "身份证输入不合法！";
                $cerCode.attr("data-regular","/(^\\d{15}$)|(^\\d{18}$)|(^\\d{17}(\\d|X|x)$)/");
            }
            /*户口簿*/
            if(cerCodeType == "2"){
                //return "户口簿XXXXXXXXXXXXXX！";
                $cerCode.attr("data-regular","/^[A-Za-z,\\s]+$/");
            }
            /*护照*/
            if(cerCodeType == "3"){
                //return "护照XXXXXXXXXXXXXX！";
                $cerCode.attr("data-regular","/^[A-Za-z,\\s]+$/");
            }
            /*军官证*/
            if(cerCodeType == "4"){
                //return "军官证XXXXXXXXXXXXXX！";
                $cerCode.attr("data-regular","/^[A-Za-z,\\s]+$/");
            }
            /*士兵证*/
            if(cerCodeType == "5"){
                //return "士兵证XXXXXXXXXXXXXX！";
                $cerCode.attr("data-regular","/^[A-Za-z,\\s]+$/");
            }
            /*港澳居民来往内地通行证*/
            if(cerCodeType == "6"){
                //return "港澳居民来往内地通行证XXXXXXXXXXXXXX！";
                $cerCode.attr("data-regular","/^[A-Za-z,\\s]+$/");
            }
            /*台湾同胞来往内地通行证*/
            if(cerCodeType == "7"){
                //return "台湾同胞来往内地通行证XXXXXXXXXXXXXX！";
                $cerCode.attr("data-regular","/^[A-Za-z,\\s]+$/");
            }
            /*临时身份证*/
            if(cerCodeType == "8"){
                //return "临时身份证XXXXXXXXXXXXXX！";
                $cerCode.attr("data-regular","/^[A-Za-z,\\s]+$/");
            }
            /*外国人居留证*/
            if(cerCodeType == "9"){
                // return "外国人居留证XXXXXXXXXXXXXX！";
                $cerCode.attr("data-regular","/^[A-Za-z,\\s]+$/");
            }
            /*警官证*/
            if(cerCodeType == "10"){
                //return "警官证XXXXXXXXXXXXXX！";
                $cerCode.attr("data-regular","/^[A-Za-z,\\s]+$/");
            }
            /*其他证件*/
            if(cerCodeType == "11"){
                //return "其他证件XXXXXXXXXXXXXX！";
                $cerCode.attr("data-regular","/^[A-Za-z,\\s]+$/");
            }
        });

        $cerCode.formValidator({tipID:(data["itemId"] + 'Tip'),onShow:"",onFocus:"&nbsp;",onCorrect:"&nbsp;"});
        $cerCode.functionValidator({
            fun:function(val,elem){

                var _result = true;
                if (ValidationRules) {
                    $.each(data["rules"],function(classname, classObj) {
                        //单选钮不需要在高级规则中的必选判断
                        //if ($.inArray(classname, SurveyBuild._baseRules) == -1 && data["rules"][classname]["isEnable"] == "Y" && classname!="RequireValidator") {
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
