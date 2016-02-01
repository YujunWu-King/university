/**
 * Created by admin on 2015/8/26.
 */
SurveyBuild.extend("YearsAndMonth", "baseComponent", {
    itemName: "年月组合控件",
    title:"年月组合控件",
    isSingleLine: "Y",
    dateformate: "yy-mm-dd",
    minYear: "1960",
    maxYear: "2030",
    children: [
        {
            "itemId": "YM_years",
            "itemName": MsgSet["YEAR"],
            "title":MsgSet["YEAR"],
            "value": "",
            "StorageType": "S",
            "orderby":1,
            "classname":"Select",
            "option": {}
        },
        {
            "itemId": "YM_month",
            "itemName": MsgSet["MONTH"],
            "title": MsgSet["MONTH"],
            "value": "",
            "StorageType": "S",
            "orderby":2,
            "classname":"Select",
            "option": {"A14405540549721":{"code":"01","txt":"01","orderby":1,"defaultval":"N","other":"N","weight":0},"A14405540549722":{"code":"02","txt":"02","orderby":2,"defaultval":"N","other":"N","weight":0},"A14405540549723":{"code":"03","txt":"03","orderby":3,"defaultval":"N","other":"N","weight":0},"A14405540549724":{"code":"04","txt":"04","orderby":4,"defaultval":"N","other":"N","weight":0},"A14405540549725":{"code":"05","txt":"05","orderby":5,"defaultval":"N","other":"N","weight":0},"A14405540549726":{"code":"06","txt":"06","orderby":6,"defaultval":"N","other":"N","weight":0},"A14405540549727":{"code":"07","txt":"07","orderby":7,"defaultval":"N","other":"N","weight":0},"A14405540549728":{"code":"08","txt":"08","orderby":8,"defaultval":"N","other":"N","weight":0},"A14405540549729":{"code":"09","txt":"09","orderby":9,"defaultval":"N","other":"N","weight":0},"A14405540549730":{"code":"10","txt":"10","orderby":10,"defaultval":"N","other":"N","weight":0},"A14405540549731":{"code":"11","txt":"11","orderby":11,"defaultval":"N","other":"N","weight":0},"A14405540549732":{"code":"12","txt":"12","orderby":12,"defaultval":"N","other":"N","weight":0}}
        }
    ],
    _getHtml: function(data, previewmode) {
        var c = "",children = data.children;
        if (previewmode) {
            if(SurveyBuild._readonly){
                //只读模式

                c += '<div class="main_inner_content_info_autoheight cLH">';
                c += '  <div class="main_inner_connent_info_left">';
                c += '      <span class="reg_title_star">' + (data.isRequire == "Y" ? "*": "") + '</span>' + data.title;
                c += '  </div>';
                c += '  <div class="main_inner_content_info_readonly_right" >' + children[0]["value"] + (children[0]["value"]?"-":"") + children[1]["value"] + '</div>';
                c += '</div>'
            }else{
                //填写模式

                c += '<div class="main_inner_content_info_autoheight">';
                c += '	<div class="main_inner_connent_info_left">';
                c += '		<span class="reg_title_star">' + (data.isRequire == "Y" ? "*": "") + '</span>' + data.title;
                c += '	</div>';
                c += '	<div class="main_inner_content_info_right">';
                c += '		<div class="main_inner_content_info_right_l140px">';
                c += '			<select name="' + data["itemId"] + children[0]["itemId"] + '" value="' + children[0]["value"] + '" class="chosen-select width_125px" id="' + data["itemId"] + children[0]["itemId"] + '" title="' + children[0]["itemName"] + '">';
                c += '			<option value="">' + MsgSet["PLEASE_SELECT"] + '</option>';
                for (var i in children[0]["option"]) {
                    c += '<option ' + (children[0].value == children[0]["option"][i]["code"] ? "selected='selected'": "") + 'value="' + children[0]["option"][i]["code"] + '">' + children[0]["option"][i]["txt"] + '</option>';
                }
                c += '			</select>';
                c += '		</div>';
                c += '		<div class="main_inner_content_info_right_r140px">';
                c += '			<select title="' + children[1]["itemName"] + '" id="' + data["itemId"] + children[1]["itemId"] + '" class="chosen-select width_125px" value="' + children[1]["value"] + '" name="' + data["itemId"] + children[1]["itemId"] + '">';
                c += '			<option value="">' + MsgSet["PLEASE_SELECT"] + '</option>';
                for (var i in children[1]["option"]) {
                    c += '<option ' + (children[1].value == children[1]["option"][i]["code"] ? "selected='selected'": "") + 'value="' + children[1]["option"][i]["code"] + '">' + children[1]["option"][i]["code"] + '</option>';
                }
                c += '			</select>';
                c += '		</div>';
                c += '		<div style="margin-top:-40px;margin-left:256px;float:left;">';
                c += '			<div id="' + data["itemId"] + 'Tip" class="onShow" style="margin: 0px; padding: 0px; background: transparent none repeat scroll 0% 0%;">';
                c += '				<div class="onShow"></div>';
                c += '			</div>';
                c += '		</div>';
                c += '	</div>';
                c += '</div>';
            }
        } else {
            c += '<div class="question-answer">';
            c += '  <div class="format ">'
            c += '      <b class="read-select">- 请选择 -</b>';
            c += '      <b class="read-select">- 请选择 -</b>';
            c += '  </div>'
            c += '</div>'
        }
        return c;
    },
    _edit: function(data) {
        var e = '';
        e += '<div class="edit_item_warp">';
        e += '	<span class="edit_item_label">年份最小值：</span>';
        e += '	<input type="text" class="medium datemin" data_id="' + data.instanceId + '" onkeyup="SurveyBuild.saveAttr(this,\'minYear\')" value="' + data.minYear + '"/>';
        e += '</div>';

        e += '<div class="edit_item_warp">';
        e += '	<span class="edit_item_label">年份最大值：</span>';
        e += '	<input type="text" class="medium datemax" data_id="' + data.instanceId + '" onkeyup="SurveyBuild.saveAttr(this,\'maxYear\')" value="' + data.maxYear + '"/>';
        e += '</div>';
        e += '<div class="edit_jygz">';
		e += '	<span class="title"><i class="icon-cog"></i> 校验规则</span>';
		e += '  <div class="groupbox">';
        e += '    <div class="edit_item_warp" style="margin-top:5px;">';
        e += '        <input class="mbIE" type="checkbox" onchange="SurveyBuild.saveAttr(this,\'isRequire\')"' + (data.isRequire == "Y" ? "checked='checked'": "") + ' id="is_require"> <label for="is_require">是否必填</label>';
        e += '    </div>';
        e += '    </div>';
		
        e += '    <div class="edit_item_warp">';
        e += '        <a href="javascript:void(0);" style="color:#3F69B6;" onclick="SurveyBuild.RulesSet(this);"><i class="icon-cogs"></i> 高级设置</a>';
        e += '    </div>';
        e += '</div>';
        return e;
    },
    _eventbind: function(data) {
        var $year = $("#" + data["itemId"] + data.children[0]["itemId"]);
        var $month = $("#" + data["itemId"] + data.children[1]["itemId"]);

        $year.formValidator({tipID:data["itemId"] + 'Tip',onShow:"",onFocus:"&nbsp;",onCorrect:"&nbsp;"});
        $year.functionValidator({
            fun: function(val,elem) {
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
                                _result = _ruleClass._Validator(data["itemId"] + data.children[0]["itemId"], classObj["messages"], data);
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

        $month.formValidator({tipID:data["itemId"] +'Tip',onShow:"",onFocus:"&nbsp;",onCorrect:"&nbsp;"});
        $month.functionValidator({
            fun: function(val,elem) {
                //执行高级设置中的自定规则
                /*********************************************\
                 ** 注意：自定义规则中不要使用formValidator **
                 \*********************************************/
                var _result = true;
                if (ValidationRules) {
                    $.each(data["rules"],function(classname, classObj) {
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
            }
        })

    },
    _validatorAttr: function(data) {
        var msg;
        var $edit_box = $("#question-edit");
        var dateMax = data.maxYear;
        var dateMin = data.minYear;

        if (!dateMax) {
            msg = "年份最大值不能为空！";
            var $targetObj = $edit_box.find(".datemax");
            SurveyBuild.fail($targetObj, msg);
            return false;
        }
        if (!dateMin) {
            msg = "年份最小值不能为空！";
            var $targetObj = $edit_box.find(".datemin");
            SurveyBuild.fail($targetObj, msg);
            return false;
        }
        if (parseInt(dateMax) < parseInt(dateMin)) {
            msg = "年份最大值要大于年份最小值！";
            var $targetObj = $edit_box.find(".datemax");
            SurveyBuild.fail($targetObj, msg);
            return false;
        }

        var d = data["instanceId"];
        data.children[0]["option"] = {};
        for (var i = parseInt(data.minYear); i <= parseInt(data.maxYear); ++i) {
            data.children[0]["option"][d + i] = {
                code: i,
                txt: "" + i,
                orderby: i,
                defaultval: 'N',
                other: 'N',
                weight: 0
            }
        }
        return true;
    }
})