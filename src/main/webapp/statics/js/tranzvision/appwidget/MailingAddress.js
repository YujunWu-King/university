/*====================================================================
 + 功能描述：通讯地址							                     ++
 + 开发人：叶少威													 ++
 + 开发日期：2015-05-15												 ++
 =====================================================================*/
SurveyBuild.extend("MailingAddress", "baseComponent", {
    itemName: "通讯地址",
    title:"通讯地址",
    isSingleLine:"Y",
    fixedContainer:"Y",//固定容器标识
    children: [{"itemId":"province","itemName":MsgSet["CITY"],"value":"","StorageType":"S","classname":"City"},{"itemId":"address","itemName":MsgSet["ADDRESS"],"value":"","StorageType": "S","classname":"SingleTextBox"}],

    _getHtml: function(data, previewmode) {
        var c = "", children = data.children;
        if (previewmode) {
            if(SurveyBuild._readonly){
                //只读模式

                c += '<div class="main_inner_content_info_autoheight cLH">';
                c += '  <div class="main_inner_connent_info_left">';
                c += '      <span class="reg_title_star">' + (data.isRequire == "Y" ? "*": "") + '</span>' + data.title;
                c += '  </div>';
                c += '  <div class="main_inner_content_info_right" >';
                c += '      <span style="' + (children[0]["value"] ? "line-height:25px;" : "") + '">' + children[0]["value"] + (children[0]["value"] ? "<br>" : "") + children[1]["value"] + '</span>';
                c += '  </div>';
                c += '</div>'
            }else{
                //填写模式
                SurveyBuild.appInsId == "0" && this._getDefaultVal(data,"P2");

                c += '<div class="main_inner_content_info_autoheight">';
                c += '	<div class="main_inner_connent_info_left">';
                c += '		<span class="reg_title_star">' + (data.isRequire == "Y" ? "*": "") + '</span>' + data.title;
                c += '	</div>';
                c += '	<div class="main_inner_content_info_right">';
                c += '		<input type="text" title="' + MsgSet["CITY"] + '" value="' + children[0]["value"] + '" id="' + data["itemId"] + children[0]["itemId"] + '" class="input_63px" name="' + data["itemId"] + children[0]["itemId"] + '">';
                c += '		<input type="text" title="' + MsgSet["ADDRESS"] + '" value="' + children[1]["value"] + '" id="' + data["itemId"] + children[1]["itemId"] + '" class="input_180px" name="' + data["itemId"] + children[1]["itemId"] + '">';
                c += '		<div style="margin-top:-40px;margin-left:256px">';
                c += '			<div id="' + data["itemId"] + 'Tip" style="margin: 0px; padding: 0px; background: transparent none repeat scroll 0% 0%;" class="onCorrect">';
                c += '              <div class="onCorrect">&nbsp;</div>';
                c += '			</div>';
                c += '		</div>';
                c += '	</div>';
                c += '</div>';
            }
        } else {
            c += '<div class="question-answer">';
            c += '  <b style="min-width: 80px" class="read-select">省/市</b>';
            c += '  <b style="min-width: 400px;" class="read-input">&nbsp;</b>';
            c += '</div>';
        }
        return c;
    },
    _edit: function(data) {
        var e = "";
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
		
        e += '  <div class="edit_item_warp">';
        e += '      <a href="javascript:void(0);" onclick="SurveyBuild.RulesSet(this);"><i class="icon-cogs"></i> 高级设置</a>';
        e += '  </div>';
        e += '</div>';
        return e;
    },
    _eventbind: function(data) {
        var province = $("#" + data["itemId"] + data.children[0]["itemId"]);
        var address = $("#" + data["itemId"] + data.children[1]["itemId"]);

        var prov;
        province.click(function(e) {
            var _prov_id=data["itemId"] + data.children[0]["itemId"];

            var provinceUrl = SurveyBuild.tzGeneralURL + '?tzParams=';
            var params = '{"ComID":"TZ_COMMON_COM","PageID":"TZ_CITY_STD","OperateType":"HTML","comParams":{"OType":"CITY","TZ_CITY_ID":"' + _prov_id + '"}}';
            provinceUrl = provinceUrl + window.escape(params);

            prov = $.layer({
                type: 2,
                title: false,
                fix: false,
                closeBtn: false,
                shadeClose: false,
                shade : [0.3 , '#000' , true],
                border : [3 , 0.3 , '#000', true],
                offset: ['100px',''],
                area: ['588px','400px'],
                iframe: {src: provinceUrl}
            });
        });
        province.formValidator({tipID:(data["itemId"] + 'Tip'),onShow:"",onFocus:"&nbsp;",onCorrect:"&nbsp;"});
        address.formValidator({tipID:(data["itemId"] + 'Tip'),onShow:"",onFocus:"&nbsp;",onCorrect:"&nbsp;"});

        province.functionValidator({
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
                                _result = _ruleClass._Validator(data["itemId"] + data.children[0]["itemId"], classObj["messages"]);
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
        address.functionValidator({
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
                                _result = _ruleClass._Validator(data["itemId"] + data.children[1]["itemId"], classObj["messages"]);
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
