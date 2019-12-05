/**
 * Created by LuYan on 2018/1/17.
 * 老师推荐
 */
SurveyBuild.extend("RecommendTeacher","baseComponent",{
    itemName:"推荐老师",
    title:"推荐老师",
    "StorageType":"S",
    option:{},
    _getHtml:function(data,previewmode) {
        var c = "";
        if (previewmode) {

            var recmdTeaMobile = $("#RecmdTeaMobile").val();
            var recommendFlag = $("#RecommendFlag").val();
            var isCopyFormHisAppForm = $("#isCopyFormHisAppForm").val();

            var params = '{"ComID":"TZ_ONLINE_REG_COM","PageID":"TZ_ONREG_OTHER_STD","OperateType":"EJSON","comParams":{"OType":"TJTEA","MOBILE":"'+ recmdTeaMobile +'"}}';
            $.ajax({
                type: "get",
                dataType: "JSON",
                data: {
                    tzParams: params
                },
                async: false,
                url: SurveyBuild.tzGeneralURL,
                success: function (f) {
                    if (f.state.errcode == "0") {
                        data.option = f.comContent;
                    }
                }
            });

            //新建报名表或从历史复制报名表
            if(SurveyBuild.appInsId == "0" || isCopyFormHisAppForm == "Y") {
                if(recmdTeaMobile!=undefined && recmdTeaMobile!="" && data.option!=undefined && data.option!="") {
                    //带推荐老师信息
                	var count = 0;
                    for (var i in data.option) {
                    	count++;
                        data.value = data["option"][i]["txt"];
                    }
                    if(count==1) {
                    	//只有一个推荐老师，只读显示	
                    	data.isReadOnly = "Y";
                    } else {
                    	data.value = "";	
                    }
                    
                }
            } else {
                //保存过报名表
                if(recommendFlag=="Y") {
                    //通过推荐老师发送的链接保存过报名表，只读显示
                    data.isReadOnly = "Y";
                }
            }


            if(SurveyBuild.accessType == "M") {
                //手机模式
                c += '<div class="item">';
                c += '<p>'+data.title+'<span>'+(data.isRequire == "Y" ? "*": "")+'</span></p>';
                c += '<div class="text-box"><input ' + (data.isReadOnly == "Y" ? 'readonly="true"': '') + ' type="text" class="text1" id="' + data.itemId + '" name="' + data.itemId + '" value="' + data.value + '" title="' + data.itemName + '" /></div>';
                c += '<p style="color:#666;font-size:0.56rem;"></p>';
                c += '</div>';
            } else {
                //PC模式
                if(SurveyBuild._readonly) {
                    //只读模式
                    c += '<div class="input-list">';
                    c += '	<div class="input-list-info left"><span class="red-star">' + (data.isRequire == "Y" ? "*": "") + '</span>' + data.title + '</div>';
                    c += '  <div class="input-list-text left" title="' + data.itemName + '">' + data.value + '</div>';
                    c += '  <div class="input-list-suffix left"></div>';
                    c += '  <div class="clear"></div>';
                    c += '</div>';
                } else {
                    //填写模式
                    if(data.isReadOnly == "Y") {
                        //只读显示
                        c += '<div class="input-list">';
                        c += '	<div class="input-list-info left"><span class="red-star">' + (data.isRequire == "Y" ? "*": "") + '</span>' + data.title + '</div>';
                        c += '  <div class="input-list-text left" title="' + data.itemName + '">' + data.value + '</div>';
                        c += '  <div class="input-list-suffix left"></div>';
                        c += '  <div class="clear"></div>';
                        c += '</div>';
                    } else {
                        c += '<div class="input-list">';
                        c += '	<div class="input-list-info left"><span class="red-star">' + (data.isRequire == "Y" ? "*": "") + '</span>' + data.title + '</div>';
                        c += '  <div class="input-list-text left"><input  type="text" class="inpu-list-text-enter" id="' + data.itemId + '" name="' + data.itemId + '" value="' + data.value + '" title="' + data.itemName + '"/></div>';
                        c += '  <div class="input-list-suffix left"><div id="' + data.itemId + 'Tip" class="onShow"><div class="onShow"></div></div></div>';
                        c += '  <div class="clear"></div>';
                        c += '</div>';
                    }
                }
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
    _edit:function(data) {
        var e='',list='';

        //校验规则
        e += '<div class="edit_jygz">';
        e += '	<span class="title"><i class="icon-cog"></i> 校验规则</span>';
        e += '  <div class="groupbox">';
        e += '  <div class="edit_item_warp" style="margin-top:5px;">';
        e += '      <input class="mbIE" type="checkbox" onchange="SurveyBuild.saveAttr(this,\'isRequire\')"' + (data.isRequire == "Y" ? "checked='checked'": "") + ' id="is_require"> <label for="is_require">是否必填</label>';
        e += '  </div>';
        e += '</div>';

        //高级设置
        e += '  <div class="edit_item_warp">';
        e += '      <a href="javascript:void(0);" onclick="SurveyBuild.RulesSet(this);"><i class="icon-cogs"></i> 高级设置</a>';
        e += '  </div>';
        e += '</div>';

        return e;
    },
    _eventbind:function(data) {
        var $inputBox = $("#" + data.itemId);

        var source = [],j = 0;
        for (var i in data.option) {
            source[j] = data["option"][i]["txt"];
            j++;
        }

        if(SurveyBuild.accessType == "M") {
        	//手机模式,解决iphone点击两次才赋值问题
        	$inputBox.autocomplete({
                source: source,
                focus: function (event, ui) {
                  $inputBox.val(ui.item.value);
                  event.close();
                },
                change: function(e, ui) {
                    if (!ui.item) {
                        $(this).val("");
                    }
                },
                response: function(e, ui) {
                    if (ui.content.length == 0) {
                        $(this).val("");
                    }
                }
            });
        } else {
        	$inputBox.autocomplete({
                source: source,
                change: function(e, ui) {
                    if (!ui.item) {
                        $(this).val("");
                    }
                },
                response: function(e, ui) {
                    if (ui.content.length == 0) {
                        $(this).val("");
                    }
                }
            });
        }
        

        $inputBox.formValidator({tipID:(data["itemId"]+'Tip'), onShow:"", onFocus:"&nbsp;", onCorrect:"&nbsp;"});
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