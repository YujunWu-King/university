/**
 * Created by WRL on 2015/5/15.
 * 工作经历
 */
SurveyBuild.extend("workExperience", "baseComponent", {
    itemName: "工作经历",
    title: "工作经历",
    itemMs: "",
    isDoubleLine: "Y",
    fixedContainer:"Y",//固定容器标识
    linesNo:[1,2,3],
    children:{
        "work_bdate": {
            "instanceId": "work_bdate",
            "itemId": "w_beginDate",
            "itemName": MsgSet["STARTDATE"],
            "title": MsgSet["STARTDATE"],
            "orderby": 1,
            "value": "",
            "StorageType":"S",
            "classname":"DateInputBox"
        },
        "work_edate": {
            "instanceId": "work_edate",
            "itemId": "w_endDate",
            "itemName": MsgSet["ENDDATE"],
            "title": MsgSet["ENDDATE"],
            "orderby": 2,
            "value": "",
            "StorageType":"S",
            "classname":"DateInputBox"
        },
        "work_tonow": {
            "instanceId": "work_tonow",
            "itemId": "w_toNow",
            "itemName": MsgSet["TODATE"],
            "title": MsgSet["TODATE"],
            "orderby": 3,
            "value": "",
            "StorageType":"S",
            "classname":"CheckBox"
        },
        "work_comp": {
            "instanceId": "work_comp",
            "itemId": "w_company",
            "itemName": MsgSet["UNITS"],
            "title": MsgSet["UNITS"],
            "orderby": 4,
            "value": "",
            "StorageType":"S",
            "classname":"SingleTextBox"
        },
        "work_zhiwu": {
            "instanceId": "work_zhiwu",
            "itemId": "w_zhiwu",
            "itemName": MsgSet["ZHIWU"],
            "title": MsgSet["ZHIWU"],
            "orderby": 5,
            "value": "",
            "StorageType":"S",
            "classname":"SingleTextBox"
        }
    },
    minLines: 1,
    maxLines: 4,

    _getHtml: function (data, previewmode) {

        var c = "", children = data.children,len = children.length;
        if (previewmode) {
            //title
			var c = "";
            c += '<div class="main_inner_content_title">';
			c += '	<span class="reg_title_star">' + (data.isRequire == "Y" ?"*":"") + '</span>';
			c += '	<span class="reg_title_grey_17px">' + data.title + '</span>';
            c += '</div>';
            c += '<div class="main_inner_content_top"></div>';

            //content
            var works = this._getHtmlOne(data,0);
            c += '<div class="main_inner_content">';
            c += works;
            c += '	<div class="main_inner_content_info">';
            c += '		<div id="main_inner_content_info_save0">';
			c += '			<div id="saveWork" class="bt_blue" onclick="SurveyBuild.saveApp();">' + MsgSet["SAVE"] + '</div>';
			c += '			<a href="#" class="alpha"></a>';
			c += '		</div>';
            c += '		<div style="display: inherit;" class="main_inner_content_info_add addnextbtn" id="save_and_add0" onclick="SurveyBuild.showDiv(this,\'' + data.instanceId + '\');">';
			c += '			<div class="bt_blue">' + MsgSet["ADD_ONE"] + '</div>';
			c += '		</div>';
            c += '	</div>';
            c += '</div>';
            //footer
            c += '<div class="main_inner_content_foot"></div>';

        } else {
	    var c = "";
            c += '<div class="question-answer">' + (data.itemMs ? '<div class="edu_exper_itemMs" style="background-color:#d8d8d8;padding:2px 5px;margin-bottom:10px;">'+ data.itemMs +'</div>' : "");
            c += '<div class="DHContainer" style="border:1px solid #ddd;padding:10px 20px;">'

            c += '<div class="edu_item_li">';
            c += '<span class="edu_item_label">工作时间：</span>';
            c += '<b class="read-input" style="min-width:120px; margin-right:10px;">YYYY-MM-DD</b>';
            c += '<b class="read-input" style="min-width:120px; margin-right:10px;">YYYY-MM-DD</b>';
            c += '<b class="read-check" style="display:inline-block;margin-right:-5px;min-width:20px;">&nbsp;</b>至今';
            c += '</div>';

            c += '<div class="edu_item_li">';
            c += '<span class="edu_item_label">工作单位：</span>';
            c += '<b class="read-input" style="width:272px">&nbsp;</b>';
            c += '</div>';

            c += '<div class="edu_item_li">';
            c += '<span class="edu_item_label">担任职务：</span>';
            c += '<b class="read-input" style="width:272px">&nbsp;</b>';
            c += '</div>';

            c += '</div></div>'
        }
        return c;
    },
    _edit: function (data) {

		var e = '';

        e += '<div class="edit_jygz">';
        e += '	<span class="title"><i class="icon-info-sign"></i> 参数设置</span>';
        e += '  <div class="groupbox">';
        e += '  <div class="edit_item_warp">';
        e += '      <span class="edit_item_label">最小行数：</span>';
        e += '     <input type="text" maxlength="1" class="medium" onkeyup="SurveyBuild.saveAttr(this,\'minLines\')" value="' + data.minLines + '"/>';
        e += '  </div>';

        e += '  <div class="edit_item_warp mb10">';
        e += '      <span class="edit_item_label">最大行数：</span>';
        e += '     <input type="text" maxlength="1" class="medium" onkeyup="SurveyBuild.saveAttr(this,\'maxLines\')" value="' + data.maxLines + '"/>';
        e += '  </div>';
        e += '</div>';

        //规则设置
        e += '<div class="edit_jygz">';
        e += '	    <span class="title"><i class="icon-cog"></i> 校验规则</span>';
        e += '      <div class="groupbox">';
        e += '          <div class="edit_item_warp" style="margin-top:5px;">';
        e += '              <input class="mbIE" type="checkbox" onchange="SurveyBuild.saveAttr(this,\'isRequire\')"' + (data.isRequire == "Y" ? "checked='checked'": "") + ' id="is_require">';
        e += '                 <label for="is_require">是否必填';
        e += '                  <a href="#" data-for-id="help_isRequire" onclick="SurveyBuild.showMsg(this,event)" class="big-link" data-reveal-id="myModal" data-animation="fade">(?)</a>';
        e += '                 </label>';
        e += '          </div>';
        e += '      </div>';
        //高级设置
        e += '      <div class="edit_item_warp">';
        e += '          <a href="javascript:void(0);" onclick="SurveyBuild.RulesSet(this);"><i class="icon-cogs"></i> 高级设置</a>';
        e += '		    <a href="#" data-for-id="help_advancedSetup" onclick="SurveyBuild.showMsg(this,event)" class="big-link" data-reveal-id="myModal" data-animation="fade">(?)</a>';
        e += '      </div>';
        e += '</div>';

        return e;
    },
    _getHtmlOne: function(data,rownum){
        var len = data.children.length;
        var works = "",j = 0,child = [];
        if (parseInt(rownum) == parseInt(len)){
            //返回最后一条记录的HTML及最后一个工作经历（主要用于新增）
            j = rownum - 1;
            child.push(data.children[rownum-1]);
        }else{
            child = data.children;
        }

        for (var i in child) {
            works += '<div class="main_inner_content_para" style="display: inherit;" >';
            if(j != 0){
                works += '<div class="main_inner_content_top"></div><div class="padding_div"></div><div class="main_inner_content_foot"></div>';
            }
            //工作时间
            if(SurveyBuild._readonly){
                //只读模式
                works += '<div class="main_inner_content_info_autoheight cLH">';
                works += '<div class="main_inner_connent_info_left">' + MsgSet["WTIME"] + '</div>';
                works += '<div class="main_inner_content_info_right">';
                works += '<span>' + child[i].work_bdate["value"] + '</span>';
                works += '<span style="' + (child[i].work_tonow["value"] == "Y" ? "display:none;": "") + '">&nbsp;&nbsp;&nbsp;&nbsp;' + child[i].work_edate["value"] + '</span>';
                if(j == 0){
                    works += '&nbsp;&nbsp;<div class="tz_checkbox_div ' + (child[i].work_tonow["value"] == "Y" ? "on_check": "") + '" style="margin: 0px;position: relative;' + (child[i].work_tonow["value"] != "Y" ? "display:none;": "") + '">';
                    works += '<input class="tz_radio_checkbox" type="checkbox" name="' + data.itemId + child[i].work_tonow["itemId"] + '" id="' + data.itemId + child[i].work_tonow["itemId"] + '" ' + (child[i].work_tonow["value"] == "Y" ? "checked='checked'": "") + 'style="width:20px;height:20px;">';
                    works += '</div>';
                    works += '<span style="position: relative;' + (child[i].work_tonow["value"] != "Y" ? "display:none;": "") + '">'+  child[i].work_tonow["itemName"] +'</span>';
                }
                works += '</div>';
                works += '</div>';
            }else {
                //填写模式
                works += '<div class="main_inner_content_info_autoheight">';
                works += '<div class="main_inner_connent_info_left">' + MsgSet["WTIME"] + '</div>';
                works += '<div class="main_inner_content_info_right">';
                works += '<input type="text" title="' + child[i].work_bdate["itemName"] + '" onchange="SurveyBuild.reFocus(\'' + data.itemId + child[i].work_bdate["itemId"] + '\');" readonly="readonly" id="' + data.itemId + child[i].work_bdate["itemId"] + '" class="input_120px" name="' + data.itemId + child[i].work_bdate["itemId"] + '" value="' + child[i].work_bdate["value"] + '">&nbsp;';
                works += '<input type="text" title="' + child[i].work_edate["itemName"] + '" onchange="SurveyBuild.reFocus(\'' + data.itemId + child[i].work_edate["itemId"] + '\');" readonly="readonly" id="' + data.itemId + child[i].work_edate["itemId"] + '" class="input_120px" name="' + data.itemId + child[i].work_edate["itemId"] + '" value="' + child[i].work_edate["value"] + '" style="'+(child[i].work_tonow["value"] == "Y" ? "display:none;": "")+'">';
                works += '<img id="' + data.itemId + child[i].work_bdate["itemId"] + '_Btn" src="' + TzUniversityContextPath + '/statics/images/appeditor/calendar.png" style="position:relative;left:'+ (child[i].work_tonow["value"] == "Y" ? "-40px" : "-168px") +';cursor:pointer;">';
                works += '<img id="' + data.itemId + child[i].work_edate["itemId"] + '_Btn" src="' + TzUniversityContextPath + '/statics/images/appeditor/calendar.png" style="position:relative;left:-61px;cursor:pointer;'+(child[i].work_tonow["value"] == "Y" ? "display:none;": "")+'">';

                if(j == 0){
                    works += '<div class="tz_checkbox_div '+(child[i].work_tonow["value"] == "Y" ? "on_check": "")+'" style="margin: 0px;position: relative; left:'+(child[i].work_tonow["value"] == "Y" ? "-25px" : "-48px")+'">';
                    works += '<input class="tz_radio_checkbox" type="checkbox" name="' + data.itemId + child[i].work_tonow["itemId"] + '" id="' + data.itemId + child[i].work_tonow["itemId"] + '" ' + (child[i].work_tonow["value"] == "Y" ? "checked='checked'": "") + 'style="width:20px;height:20px;">';
                    works += '</div>';
                    works += '<span style="position: relative; left:'+(child[i].work_tonow["value"] == "Y"  ? "-23px" : "-46px")+'">'+  child[i].work_tonow["itemName"] +'</span>';
                }
                works += '<div style="margin-top:-40px; margin-left:' + (j == 0 ? "320" : "260") + 'px"><div id="' + data.itemId + child[i].work_edate["itemId"] + 'Tip" style="margin: 0px; padding: 0px; background: none repeat scroll 0% 0% transparent;" class="onCorrect"><div class="onCorrect">&nbsp;</div></div></div>';
                works += '</div>';
                if(j != 0){
					works += '<div class="main_inner_content_del_bmb" onclick="SurveyBuild.deleteFun(this);">';
					works += '  <img src="' + TzUniversityContextPath + '/statics/images/appeditor/del.png" width="15" height="15">&nbsp;' + MsgSet["DEL"];
					works += '</div>';
                }
                works += '</div>';
            }

	        //工作单位
            if(SurveyBuild._readonly){
                //只读模式
                works += '<div class="main_inner_content_info_autoheight cLH">';
                works += '<div class="main_inner_connent_info_left">' + child[i].work_comp["itemName"] + '</div>';
                works += '<div class="main_inner_content_info_right">' + child[i].work_comp["value"] + '</div>';
                works += '</div>';
            }else {
                //填写模式
                works += '<div class="main_inner_content_info_autoheight">';
                works += '<div class="main_inner_connent_info_left">' + child[i].work_comp["itemName"] + '</div>';
                works += '<div class="main_inner_content_info_right">';
                works += '<input type="text" id="' + data.itemId + child[i].work_comp["itemId"] + '" class="input_411px" name="' + data.itemId + child[i].work_comp["itemId"] + '" value="' + child[i].work_comp["value"] + '">';
                works += '<div style="margin-top: -40px; margin-left: 420px"><div id="' + data.itemId + child[i].work_comp["itemId"] + 'Tip" style="margin: 0px; padding: 0px; background: none repeat scroll 0% 0% transparent;" class="onShow"><div class="onShow"></div></div></div>';
                works += '</div>';
                works += '</div>';
            }

            //工作职务
            if(SurveyBuild._readonly){
                //只读模式
                works += '<div class="main_inner_content_info_autoheight cLH">';
                works += '<div class="main_inner_connent_info_left">' + child[i].work_zhiwu["itemName"] + '</div>';
                works += '<div class="main_inner_content_info_right">' + child[i].work_zhiwu["value"] + '</div>';
                works += '</div>';
            }else{
                //填写模式
                works += '<div class="main_inner_content_info_autoheight">';
                works += '<div class="main_inner_connent_info_left">' + child[i].work_zhiwu["itemName"] + '</div>';
                works += '<div class="main_inner_content_info_right">';
                works += '<input type="text" title="' + child[i].work_zhiwu["itemName"] + '" id="' + data.itemId + child[i].work_zhiwu["itemId"] + '" class="input_97px" name="' + data.itemId + child[i].work_zhiwu["itemId"] + '" value="' + child[i].work_zhiwu["value"] + '">';
                works += '<div style="margin-top: -40px; margin-left: 106px"><div id="' + data.itemId + child[i].work_zhiwu["itemId"] + 'Tip" style="margin: 0px; padding: 0px; background: none repeat scroll 0% 0% transparent;" class="onShow"><div class="onShow"></div></div></div>';
                works += '</div>';
                works += '</div>';
            }
            works += '</div>';
            j++;
        }
        return works;
    },
    _eventbind: function(data) {
        var children = data.children,
            len = children.length;
			
		var $startDt = $("#" + data["itemId"] + children[len - 1].work_bdate["itemId"]);
        var $endDt = $("#" + data["itemId"] + children[len - 1].work_edate["itemId"]);
        var $today = $("#" + data["itemId"] + children[len - 1].work_tonow["itemId"]);
		
		var $checkboxDiv = $today.closest(".tz_checkbox_div");
		var $toDateDesc = $checkboxDiv.next("span");
		
		var $startDtImg = $("#" + data["itemId"] + children[len - 1].work_bdate["itemId"] + "_Btn");
        var $endDtImg = $("#" + data["itemId"] + children[len - 1].work_edate["itemId"] + "_Btn");

        $.each([$startDt,$endDt],function(i,dateEl){
            dateEl.datetimepicker({
                changeMonth: true,
                changeYear: true,
                showTimepicker:false,
                dateFormat:'yy/mm/dd',
				onSelect: function(dateText, inst) {
					dateEl.datetimepicker( "hide" );
					dateEl.trigger("blur");
				}
            });
        });

        $today.click(function(){
            if($(this).prop("checked")){
                $endDt.hide();
				$endDtImg.hide();
				$startDtImg.css("left","-40px");
				$checkboxDiv.css("left","-25px");
				$toDateDesc.css("left","-23px");
				$(this).closest(".tz_checkbox_div").addClass("on_check");
            }else{
                $endDt.show();
				$endDtImg.show();
				$startDtImg.css("left","-168px");
				$checkboxDiv.css("left","-48px");
				$toDateDesc.css("left","-46px");
				$(this).closest(".tz_checkbox_div").removeClass("on_check");
            }
        });

        $startDtImg.click(function(e) {
            $startDt.focus();
        });
		$endDtImg.click(function(e) {
            $endDt.focus();
        });

        $startDt.formValidator({tipID:data["itemId"] + children[len - 1].work_edate["itemId"] + 'Tip',onShow:"",onFocus:"&nbsp;",onCorrect:"&nbsp;"});
        if(data.isRequire == "Y"){
            $startDt.inputValidator({min:'1',onError: children[len - 1].work_bdate["itemName"] + MsgSet["REQUIRE"]});
        };
        $startDt.functionValidator({
                fun:function(val,elem){
                    var startTime = $startDt.val();
                    var endTime = $endDt.val();
                    var todayDt = new Date();
                    if ($today.prop("checked")){
                        if (todayDt < new Date(startTime) && startTime != ""){
                            return MsgSet["START_LT_DAY"];
                        }
                    } else {
                        if (endTime < startTime && endTime !="" && startTime != ""){
                            return MsgSet["END_GT_START"];
                        }
                    }
                }
        });

        $endDt.formValidator({tipID:data["itemId"] + children[len - 1].work_edate["itemId"] + 'Tip',onShow:"",onFocus:"&nbsp;",onCorrect:"&nbsp;"});
        if(data.isRequire == "Y"){
            $endDt.inputValidator({min:'1',onError: children[len - 1].work_edate["itemName"]  + MsgSet["REQUIRE"]});
        }
        $endDt.functionValidator({
            fun:function(val,elem){
                var startTime = $startDt.val();
                var endTime = $endDt.val();
                if (endTime < startTime && endTime !="" && startTime != ""){
                    return MsgSet["END_GT_START"];
                }
            }
        });

        $today.formValidator({tipID:data["itemId"] + children[len - 1].work_edate["itemId"] + 'Tip',onShow:"",onFocus:"&nbsp;",onCorrect:"&nbsp;"})
            .functionValidator({
                fun:function(val,elem){
                    var startTime = $startDt.val();
                    var endTime = $endDt.val();
                    var todayDt = new Date();
                    if ($today.prop("checked")){
                        if (todayDt < new Date(startTime) && startTime != ""){
                            return MsgSet["START_LT_DAY"];
                        }
                    } else {
                        if (endTime < startTime && endTime !="" && startTime != ""){
                            return MsgSet["END_GT_START"];
                        }
                    }
                }
            }
        );

        var $company = $("#" + data["itemId"] + children[len - 1].work_comp["itemId"]);
        var $zhiwu = $("#" + data["itemId"] + children[len - 1].work_zhiwu["itemId"]);

        $company.formValidator({tipID:data["itemId"] + children[len - 1].work_comp["itemId"] + 'Tip',onShow:"",onFocus:"&nbsp;",onCorrect:"&nbsp;"});
        if(data.isRequire == "Y"){
            $company.inputValidator({min:'1',onError: children[len - 1].work_comp["itemName"]  + MsgSet["REQUIRE"]});
        }

        $zhiwu.formValidator({tipID:data["itemId"] + children[len - 1].work_zhiwu["itemId"] + 'Tip',onShow:"",onFocus:"&nbsp;",onCorrect:"&nbsp;"});
        if(data.isRequire == "Y"){
            $zhiwu.inputValidator({min:'1',onError: children[len - 1].work_zhiwu["itemName"]  + MsgSet["REQUIRE"]});
        }
    }
})