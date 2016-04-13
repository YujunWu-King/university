SurveyBuild.extend("EduExperience","baseComponent",{
	itemName:"教育经历",
	title:"教育经历",
	isDoubleLine:"Y",
	fixedContainer:"Y",//固定容器标识
	children:{
		"EduExper1": {
			"instanceId": "EduExper1",
			"itemId": "Edu_studyStage",
			"itemName": MsgSet["LEARN_PHASE"],
			"title": MsgSet["LEARN_PHASE"],
			"orderby": 1,
			"value": "",
			"StorageType": "S",
			"classname":"Select"
		},
		"EduExper2": {
			"instanceId": "EduExper2",
			"itemId": "edu_startDt",
			"itemName": MsgSet["STARTDATE"],
			"title": MsgSet["STARTDATE"],
			"orderby": 2,
			"value": "",
			"StorageType": "S",
			"classname":"DateInputBox"
		},
		"EduExper3": {
			"instanceId": "EduExper3",
			"itemId": "edu_endDt",
			"itemName": MsgSet["ENDDATE"],
			"title": MsgSet["ENDDATE"],
			"orderby": 3,
			"value": "",
			"StorageType": "S",
			"classname":"DateInputBox"
		},
		"EduExper4": {
			"instanceId": "EduExper4",
			"itemId": "edu_today",
			"itemName": MsgSet["TODATE"],
			"title": MsgSet["TODATE"],
			"orderby": 4,
			"value": "",
			"StorageType": "D",
			"classname":"Check"
		},
		"EduExper5": {
			"instanceId": "EduExper5",
			"itemId": "edu_school",
			"itemName": MsgSet["SCHOOL"],
			"title": MsgSet["SCHOOL"],
			"orderby": 5,
			"value": "",
			"StorageType": "S",
			"classname":"SingleTextBox"
		},
		"EduExper6": {
			"instanceId": "EduExper6",
			"itemId": "edu_major",
			"itemName": MsgSet["MAJOR"],
			"title": MsgSet["MAJOR"],
			"orderby": 6,
			"value": "",
			"StorageType": "S",
			"classname":"SingleTextBox"
		},
		"EduExper7": {
			"instanceId": "EduExper7",
			"itemId": "edu_byzNo",
			"itemName": MsgSet["BYNO"],
			"title": MsgSet["BYNO"],
			"orderby": 7,
			"value": "",
			"StorageType": "S",
			"classname":"SingleTextBox"
		},
		"EduExper8": {
			"instanceId": "EduExper8",
			"itemId": "edu_xwzNo",
			"itemName": MsgSet["DEGREE"],
			"title": MsgSet["DEGREE"],
			"orderby": 8,
			"value": "999",
			"StorageType": "S",
			"classname":"SingleTextBox"
		},
		"EduExper9": {
			"instanceId": "EduExper9",
			"itemId": "edu_byzImg",
			"itemName": MsgSet["BY_ATTR"],
			"title": MsgSet["BY_ATTR"],
			"orderby": 9,
			"value": "",
			"StorageType": "F",
			"classname":"imagesUpload",
			"filename":"",
			"sysFileName":"",
			"path":"",
			"accessPath":""
		},
		"EduExper10": {
			"instanceId": "EduExper10",
			"itemId": "edu_xyzImg",
			"itemName": MsgSet["DEGREE_ATTR"],
			"title": MsgSet["DEGREE_ATTR"],
			"orderby": 10,
			"value": "",
			"StorageType": "F",
			"classname":"imagesUpload",
			"filename":"",
			"sysFileName":"",
			"path":"",
			"accessPath":""
		},
		"EduExper11": {
			"instanceId": "EduExper11",
			"itemId": "edu_rptCardImg",
			"itemName": MsgSet["CJD_ATTR"],
			"title": MsgSet["CJD_ATTR"],
			"orderby": 11,
			"value": "",
			"StorageType": "F",
			"classname":"imagesUpload",
			"filename":"",
			"sysFileName":"",
			"path":"",
			"accessPath":""
		}
	},
	minLines:"1",
	maxLines:"4",
	linesNo:[1,2,3],
  	_getHtml : function(data,previewmode){
	    var c = "",children = data.children,len = children.length;	
		var opt = "",x = "";    
	    if(previewmode){
			//title
            c += '<div class="main_inner_content_title">';
            c += '	<span class="reg_title_star">' + (data.isRequire == "Y" ?"*":"") + '</span>';
			c += '	<span class="reg_title_grey_17px">' + data.title + '</span>';
            c += '</div>';
			
			
			//content
			var edus = this._getHtmlOne(data,0);

            c += '<div class="main_inner_content_top"></div>';
			c += '<div class="main_inner_content">';
			c +=  	edus ;
			c += '	<div class="main_inner_content_info">';
			c += '		<div id="main_inner_content_info_save0">';
			c += '			<div id="saveEdu" class="bt_blue" onclick="SurveyBuild.saveApp();">' + MsgSet["SAVE"] + '</div>';
			c += '			<a href="#" class="alpha"></a>';
			c += '		</div>';

            c += '		<div style="display: inherit;" class="main_inner_content_info_add addnextbtn" id="save_and_add0" onclick="SurveyBuild.showDiv(this,\'' + data.instanceId + '\');">';
			c += '			<div class="bt_blue">' + MsgSet["ADD_ONE"] + '</div>';
			c += '		</div>';
            c += '	</div>';
            c += '</div>';
			//footer
			c += '<div class="main_inner_content_foot"></div>';
			
	    }else{
			var eduLi ='';
			eduLi += '<div class="edu_item_li">';
			eduLi += '	<span class="edu_item_label">学习阶段：</span>';
			eduLi += '		<b class="read-select" style="min-width:120px;"> - 请选择 - </b>';
			eduLi += '	</div>';

			eduLi += '	<div class="edu_item_li">';
			eduLi += '		<span class="edu_item_label">就读时间：</span>';
			eduLi += '		<b class="read-input" style="min-width:120px; margin-right:10px;">YYYY-MM-DD</b>';
			eduLi += '		<b class="read-input" style="min-width:120px; margin-right:10px;">YYYY-MM-DD</b>';
			eduLi += '		<b class="read-check" style="display:inline-block;margin-right:-5px;min-width:20px;">&nbsp;</b>至今';
			eduLi += '	</div>';

			eduLi += '	<div class="edu_item_li">';
			eduLi += '		<span class="edu_item_label">就读院校：</span>';
			eduLi += '		<b class="read-input" style="width:272px">&nbsp;</b>';
			eduLi += '	</div>';

			eduLi += '	<div class="edu_item_li">';
			eduLi += '		<span class="edu_item_label">专业：</span>';
			eduLi += '		<b class="read-input" style="width:272px">&nbsp;</b>';
			eduLi += '	</div>';

			eduLi += '	<div class="edu_item_li">';
			eduLi += '		<span class="edu_item_label">毕业证号：</span>';
			eduLi += '		<b class="read-input" style="width:272px">&nbsp;</b>';
			eduLi += '	</div>';

			eduLi += '	<div class="edu_item_li">';
			eduLi += '		<span class="edu_item_label">学位证号：</span>';
			eduLi += '		<b class="read-input" style="width:272px">&nbsp;</b>';
			eduLi += '	</div>';

			eduLi += '	<div class="edu_item_li">';
			eduLi += '		<span class="edu_item_label2">上传毕业证书扫描件：</span>';
			eduLi += '		<button class="btn btn-small"><i class="icon-upload-alt"></i>上传</button>';
			eduLi += '	</div>';

			eduLi += '	<div class="edu_item_li">';
			eduLi += '		<span class="edu_item_label2">上传学位证书扫描件：</span>';
			eduLi += '		<button class="btn btn-small"><i class="icon-upload-alt"></i>上传</button>';
			eduLi += '	</div>';

			eduLi += '	<div class="edu_item_li">';
			eduLi += '		<span class="edu_item_label2">上传成绩单原件扫描件：</span>';
			eduLi += '		<button class="btn btn-small"><i class="icon-upload-alt"></i>上传</button>';
			eduLi += '	</div>';

	    	c += '<div class="question-answer">';
			c += '	<div class="DHContainer" style="border:1px solid #ddd;padding:10px 20px;">'+ eduLi +'</div>';
			c += '</div>';
	    }	    
	    return c;
	},
	_edit : function(data){
		var e ='';

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
		var edus = "",j = 0,child = [];
		if (parseInt(rownum) == parseInt(len)){
			//返回最后一条记录的HTML及最后一个教育经历（主要用于新增）
			j = rownum - 1;
			child.push(data.children[rownum-1]);
		}else{
			child = data.children;
		}
		
		var opt = '<option value="">' + MsgSet["PLEASE_SELECT"] + '</option>';
		opt += '<option value="0">' + MsgSet["BS"] + '</option>';
		opt += '<option value="1">' + MsgSet["SS"] + '</option>';
		opt += '<option value="2">' + MsgSet["TKBKXW"] + '</option>';
		opt += '<option value="3">' + MsgSet["ZKBKXW"] + '</option>';
		opt += '<option value="4">' + MsgSet["CJBKXW"] + '</option>';
		opt += '<option value="5">' + MsgSet["TKBK"] + '</option>';
		opt += '<option value="6">' + MsgSet["ZKBK"] + '</option>';
		opt += '<option value="7">' + MsgSet["CJBK"] + '</option>';
		opt += '<option value="8">' + MsgSet["ZK"] + '</option>';
		
		for (var i in child) {
			edus += '<div id="main_inner_content_para' + j + '" class="main_inner_content_para" style="display: inherit;" >';
			if(j != 0){
				edus += '<div class="main_inner_content_top"></div><div class="padding_div"></div><div class="main_inner_content_foot"></div>';
			}
			
			//学习阶段
			if(SurveyBuild._readonly){
				//只读模式
				var valDesc = "";
				if(child[i].EduExper1["value"] == "0"){
					valDesc = MsgSet["BS"];
				}else if(child[i].EduExper1["value"] == "1"){
					valDesc = MsgSet["SS"];
				}else if(child[i].EduExper1["value"] == "2"){
					valDesc = MsgSet["TKBKXW"];
				}else if(child[i].EduExper1["value"] == "3"){
					valDesc = MsgSet["ZKBKXW"];
				}else if(child[i].EduExper1["value"] == "4"){
					valDesc = MsgSet["CJBKXW"];
				}else if(child[i].EduExper1["value"] == "5"){
					valDesc = MsgSet["TKBK"];
				}else if(child[i].EduExper1["value"] == "6"){
					valDesc = MsgSet["ZKBK"];
				}else if(child[i].EduExper1["value"] == "7"){
					valDesc = MsgSet["CJBK"];
				}else if(child[i].EduExper1["value"] == "8"){
					valDesc = MsgSet["ZK"];
				}
				edus += '<div class="main_inner_content_info_autoheight cLH">';
				edus += '	<div class="main_inner_connent_info_left">' + child[i].EduExper1["itemName"] + '</div>';
				edus += '	<div class="main_inner_content_info_right">' + valDesc + '</div>';
				edus += '</div>';

			}else {
				//填写模式
				edus += '<div class="main_inner_content_info_autoheight">';
				edus += '	<div class="main_inner_connent_info_left">' + child[i].EduExper1["itemName"] + '</div>';
				edus += '	<div class="main_inner_content_info_right">';
				edus += '		<select id="'+data["itemId"]+child[i].EduExper1["itemId"]+'" class="chosen-select" style="width: 255px;" data-regular="" title="' + child[i].EduExper1["itemName"] + '" value="' + child[i].EduExper1["value"] + '" name="'+data["itemId"]+child[i].EduExper1["itemId"]+'">';
				edus += '			<option value="">' + MsgSet["PLEASE_SELECT"] + '</option>';
				edus += '			<option value="0"' + (child[i].EduExper1["value"] == "0" ? "selected='selected'": "") + '>' + MsgSet["BS"] + '</option>';
				edus += '			<option value="1"' + (child[i].EduExper1["value"] == "1" ? "selected='selected'": "") + '>' + MsgSet["SS"] + '</option>';
				edus += '			<option value="2"' + (child[i].EduExper1["value"] == "2" ? "selected='selected'": "") + '>' + MsgSet["TKBKXW"] + '</option>';
				edus += '			<option value="3"' + (child[i].EduExper1["value"] == "3" ? "selected='selected'": "") + '>' + MsgSet["ZKBKXW"] + '</option>';
				edus += '			<option value="4"' + (child[i].EduExper1["value"] == "4" ? "selected='selected'": "") + '>' + MsgSet["CJBKXW"] + '</option>';
				edus += '			<option value="5"' + (child[i].EduExper1["value"] == "5" ? "selected='selected'": "") + '>' + MsgSet["TKBK"] + '</option>';
				edus += '			<option value="6"' + (child[i].EduExper1["value"] == "6" ? "selected='selected'": "") + '>' + MsgSet["ZKBK"] + '</option>';
				edus += '			<option value="7"' + (child[i].EduExper1["value"] == "7" ? "selected='selected'": "") + '>' + MsgSet["CJBK"] + '</option>';
				edus += '			<option value="8"' + (child[i].EduExper1["value"] == "8" ? "selected='selected'": "") + '>' + MsgSet["ZK"] + '</option>';
				edus += '		</select>';
				edus += '		<div style="margin-top:-40px;margin-left:256px"><div id="'+data["itemId"]+child[i].EduExper1["itemId"]+'Tip" class="onCorrect" style="margin: 0px; padding: 0px; background: transparent;">';
				edus += '			<div class="onCorrect">&nbsp;</div></div>';
				edus += '		</div>';
				edus += '	</div>';
				edus += '	<div class="main_inner_content_edit"><img width="15" height="15" src="' + TzUniversityContextPath + '/statics/images/appeditor/edit.png">' + MsgSet["EDIT"] + '</div>';
				if(j != 0){
					edus += '	<div onclick="SurveyBuild.deleteFun(this);" class="main_inner_content_del_bmb"><img width="15" height="15" src="' + TzUniversityContextPath + '/statics/images/appeditor/del.png">' + MsgSet["DEL"] + '</div>';
				}
				edus += '</div>';
			}

			//就读时间
			if(SurveyBuild._readonly){
				//只读模式
				edus += '<div class="main_inner_content_info_autoheight cLH">';
				edus += '<div class="main_inner_connent_info_left">' + MsgSet["LEARN_TIME"] + '</div>';
				edus += '<div class="main_inner_content_info_right">';
				edus += '<span>' + child[i].EduExper2["value"] + '</span>';
				edus += '<span style="' + (child[i].EduExper4["value"] == "Y" ? "display:none;": "") + '">&nbsp;&nbsp;&nbsp;&nbsp;' + child[i].EduExper3["value"] + '</span>';
				if(j == 0){
					edus += '&nbsp;&nbsp;<div class="tz_checkbox_div '+(child[i].EduExper4["value"] == "Y" ? "on_check": "")+'" style="margin: 0px;position: relative;' + (child[i].EduExper4["value"] != "Y" ? "display:none;": "") + '">';
					edus += '<input class="tz_radio_checkbox" type="checkbox" name="' + data.itemId + child[i].EduExper4["itemId"] + '" id="' + data.itemId + child[i].EduExper4["itemId"] + '" ' + (child[i].EduExper4["value"] == "Y" ? "checked='checked'": "") + ' style="width:20px;height:20px;"/>';
					edus += '</div>';
					edus += '<span style="position: relative;' + (child[i].EduExper4["value"] != "Y" ? "display:none;": "") + '">'+  child[i].EduExper4["itemName"] +'</span>';
				}
				edus += '</div>';
				edus += '</div>';
			}else {
				//填写模式
				edus += '<div class="main_inner_content_info_autoheight">';
				edus += '<div class="main_inner_connent_info_left">' + MsgSet["LEARN_TIME"] + '</div>';
				edus += '<div class="main_inner_content_info_right">';
				edus += '<input type="text" title="' + child[i].EduExper2["itemName"] + '" onchange="SurveyBuild.reFocus(\'' + data.itemId + child[i].EduExper2["itemId"] + '\');" readonly="readonly" id="' + data.itemId + child[i].EduExper2["itemId"] + '" class="input_120px" name="' + data.itemId + child[i].EduExper2["itemId"] + '" value="' + child[i].EduExper2["value"] + '">&nbsp;';
				edus += '<input type="text" title="' + child[i].EduExper3["itemName"] + '" onchange="SurveyBuild.reFocus(\'' + data.itemId + child[i].EduExper3["itemId"] + '\');" readonly="readonly" id="' + data.itemId + child[i].EduExper3["itemId"] + '" class="input_120px" name="' + data.itemId + child[i].EduExper3["itemId"] + '" value="' + child[i].EduExper3["value"] + '" style="'+(child[i].EduExper4["value"] == "Y" ? "display:none;": "")+'">';

				edus += '<img id="' + data.itemId + child[i].EduExper2["itemId"] + '_Btn" src="' + TzUniversityContextPath + '/statics/images/appeditor/calendar.png" style="position:relative;left:'+(child[i].EduExper4["value"] == "Y" ? "-40px" : "-168px")+';cursor:pointer;">';
				edus += '<img id="' + data.itemId + child[i].EduExper3["itemId"] + '_Btn" src="' + TzUniversityContextPath + '/statics/images/appeditor/calendar.png" style="position:relative;left:-61px;cursor:pointer;'+(child[i].EduExper4["value"] == "Y" ? "display:none;": "")+'">';

				if(j == 0){
					edus += '<div class="tz_checkbox_div '+(child[i].EduExper4["value"] == "Y" ? "on_check": "")+'" style="margin: 0px;position: relative; left:'+(child[i].EduExper4["value"] == "Y" ? "-25px" : "-48px")+'">';
					edus += '<input class="tz_radio_checkbox" type="checkbox" name="' + data.itemId + child[i].EduExper4["itemId"] + '" id="' + data.itemId + child[i].EduExper4["itemId"] + '" ' + (child[i].EduExper4["value"] == "Y" ? "checked='checked'": "") + ' style="width:20px;height:20px;"/>';
					//edus += '<label for="' + data.itemId + child[i].EduExper4["itemId"] + '" style="margin: 0px;position: relative; left:'+(child[i].EduExper4["value"] == "Y" ? "-25px" : "-48px")+'"></label>';
					edus += '</div>';
					edus += '<span style="position: relative; left:'+(child[i].EduExper4["value"] == "Y"  ? "-23px" : "-46px")+'">'+  child[i].EduExper4["itemName"] +'</span>';
				}
				edus += '<div style="margin-top: -40px; margin-left: 320px"><div id="' + data.itemId + child[i].EduExper2["itemId"] + 'Tip" style="margin: 0px; padding: 0px; background: none repeat scroll 0% 0% transparent;" class="onCorrect"><div class="onCorrect">&nbsp;</div></div></div>';
				edus += '</div>';
				edus += '</div>';
			}

			//就读院校
			if(SurveyBuild._readonly){
				//只读模式
				edus += '<div class="main_inner_content_info_autoheight cLH">';
				edus += '<div class="main_inner_connent_info_left">' + child[i].EduExper5["itemName"] + '</div>';
				edus += '<div class="main_inner_content_info_right">' + child[i].EduExper5["value"] + '</div>';
				edus += '</div>';
			}else {
				//填写模式
				edus += '<div class="main_inner_content_info_autoheight">';
				edus += '<div class="main_inner_connent_info_left">' + child[i].EduExper5["itemName"] + '</div>';
				edus += '<div class="main_inner_content_info_right">';
				edus += '<input id="'+data["itemId"]+child[i].EduExper5["itemId"]+'" class="input_251px" type="text" data-regular="" title="' + child[i].EduExper5["itemName"] + '" value="'+child[i].EduExper5["value"]+'" name="'+data["itemId"]+child[i].EduExper5["itemId"]+'" />';
				edus += '<img id="' + data["itemId"]+child[i].EduExper5["itemId"] + '_Btn" src="' + TzUniversityContextPath + '/statics/images/appeditor/search.png" style="position:relative;left:-40px;cursor:pointer;">';
				edus += '<div style="margin-top:-40px;margin-left:256px"><div id="'+data["itemId"]+child[i].EduExper5["itemId"]+'Tip" class="onCorrect" style="margin: 0px; padding: 0px; background: transparent;">';
				edus += '<div class="onCorrect">&nbsp;</div></div></div>';
				edus += '</div>';
				edus += '</div>';
			}

			//专业
			if(SurveyBuild._readonly){
				//只读模式
				edus += '<div class="main_inner_content_info_autoheight cLH">';
				edus += '<div class="main_inner_connent_info_left">' + child[i].EduExper6["itemName"] +  '</div>';
				edus += '<div class="main_inner_content_info_right">' + child[i].EduExper6["value"]  + '</div>';
				edus += '</div>';
			}else {
				//填写模式
				edus += '<div class="main_inner_content_info_autoheight">';
				edus += '<div class="main_inner_connent_info_left">' + child[i].EduExper6["itemName"] +  '</div>';
				edus += '<div class="main_inner_content_info_right">';
				edus += '<input id="'+data["itemId"]+child[i].EduExper6["itemId"]+'" class="input_251px" type="text" data-regular="" title="' + child[i].EduExper6["itemName"] + '" value="'+child[i].EduExper6["value"]+'" name="'+data["itemId"]+child[i].EduExper6["itemId"]+'" />';
				edus += '<div style="margin-top:-40px;margin-left:256px"><div id="'+data["itemId"]+child[i].EduExper6["itemId"]+'Tip" class="onCorrect" style="margin: 0px; padding: 0px; background: transparent;">';
				edus += '<div class="onCorrect">&nbsp;</div></div></div>';
				edus += '</div>';
				edus += '</div>';
			}

			//毕业证号
			if(SurveyBuild._readonly){
				//只读模式
				edus += '<div class="main_inner_content_info_autoheight cLH">';
				edus += '<div class="main_inner_connent_info_left">' + child[i].EduExper7["itemName"] +  '</div>';
				edus += '<div class="main_inner_content_info_right">' + child[i].EduExper7["value"] + '</div>';
				edus += '</div>';
			}else {
				//填写模式
				edus += '<div class="main_inner_content_info_autoheight">';
				edus += '<div class="main_inner_connent_info_left">' + child[i].EduExper7["itemName"] +  '</div>';
				edus += '<div class="main_inner_content_info_right">';
				edus += '<input id="'+data["itemId"]+child[i].EduExper7["itemId"]+'" class="input_251px" type="text" data-regular="" title="' + child[i].EduExper7["itemName"] +  '" value="'+child[i].EduExper7["value"]+'" name="'+data["itemId"]+child[i].EduExper7["itemId"]+'" />';
				edus += '<div style="margin-top:-40px;margin-left:256px"><div id="'+data["itemId"]+child[i].EduExper7["itemId"]+'Tip" class="onCorrect" style="margin: 0px; padding: 0px; background: transparent;">';
				edus += '<div class="onCorrect">&nbsp;</div></div></div>';
				edus += '</div>';
				edus += '</div>';
			}

			//学位证号
			if(SurveyBuild._readonly){
				//只读模式
				edus += '<div class="main_inner_content_info_autoheight cLH">';
				edus += '<div class="main_inner_connent_info_left">' + child[i].EduExper8["itemName"] + '</div>';
				edus += '<div class="main_inner_content_info_right">' + child[i].EduExper8["value"] + '</div>';
				edus += '</div>';
			}else {
				//填写模式
				edus += '<div class="main_inner_content_info_autoheight">';
				edus += '<div class="main_inner_connent_info_left">' + child[i].EduExper8["itemName"] + '</div>';
				edus += '<div class="main_inner_content_info_right">';
				edus += '<input id="'+data["itemId"]+child[i].EduExper8["itemId"]+'" class="input_251px" type="text" data-regular="" title="' + child[i].EduExper8["itemName"] + '" value="'+child[i].EduExper8["value"]+'" name="'+data["itemId"]+child[i].EduExper8["itemId"]+'" />';
				edus += '<div style="margin-top:-40px;margin-left:256px"><div id="'+data["itemId"]+child[i].EduExper8["itemId"]+'Tip" class="onCorrect" style="margin: 0px; padding: 0px; background: transparent;">';
				edus += '<div class="onCorrect">&nbsp;</div></div></div>';
				edus += '</div>';
				edus += '</div>';
			}

			//上传毕业证书扫描件
			edus += '<div class="main_inner_content_info_autoheight">';
			edus += '<div class="main_inner_connent_info_left">' + child[i].EduExper9["itemName"] + '</div>';
			edus += '<div class="main_inner_content_info_right" style="width: 100px;">';
			edus += '	<div class="file_upload_button">';
			edus += '		<div class="bt_blue">' + MsgSet["UPLOAD_BTN_MSG"] + '</div>';
			edus += '		<input type="file" id="' + data["itemId"] + child[i].EduExper9["itemId"] + 'File" name="' + data["itemId"] + child[i].EduExper9["itemId"] + 'File" title="' + child[i].EduExper9["itemName"] + '" class="fileupload_input" onchange=SurveyBuild.eduImgUpload(this,"EduExper9")>';
//			edus += '		<input type="file" id="' + data["itemId"] + child[i].EduExper9["itemId"] + 'File" name="websitefile" title="' + child[i].EduExper9["itemName"] + '" class="fileupload_input" onchange=SurveyBuild.eduImgUpload(this,"EduExper9")>';
			edus += '		<div style="margin-top: -40px; margin-left: 265px">';
			edus += '			<div id="' + data["itemId"] + child[i].EduExper9["itemId"] + 'Tip" class="onShow" style="margin: 0px; padding: 0px; background: transparent;">';
			edus += '				<div class="onShow"></div>';
			edus += '			</div>';
			edus += '		</div>';
			edus += '	</div>';
			edus += '</div>';

			var filename = child[i].EduExper9["filename"];
			edus += '<div class="main_inner_content_info_text"><a id="'+data["itemId"]+child[i].EduExper9["itemId"]+'Attch" class="fancybox" href="' + TzUniversityContextPath + child[i].EduExper9["accessPath"] + child[i].EduExper9["sysFileName"] + '" target="_blank">' + (filename ? filename.substring(0,20) + "..." : "") + '</a></div>';
			edus += '<input id="'+data["itemId"]+child[i].EduExper9["itemId"]+'" type="hidden" name="'+data["itemId"]+child[i].EduExper9["itemId"]+'" value="'+child[i].EduExper9["value"]+'"></div>';

			//上传学位证书扫描件
			edus += '<div class="main_inner_content_info_autoheight">';
			edus += '<div class="main_inner_connent_info_left">' + child[i].EduExper10["itemName"] + '</div>';
			edus += '<div class="main_inner_content_info_right" style="width: 100px;">';
			edus += '	<div class="file_upload_button">';
			edus += '		<div class="bt_blue">' + MsgSet["UPLOAD_BTN_MSG"] + '</div>';
			edus += '		<input type="file" id="' + data["itemId"] + child[i].EduExper10["itemId"] + 'File" name="' + data["itemId"] + child[i].EduExper10["itemId"] + 'File" title="' + child[i].EduExper10["itemName"] + '" class="fileupload_input" onchange=SurveyBuild.eduImgUpload(this,"EduExper10")>';
//			edus += '		<input type="file" id="' + data["itemId"] + child[i].EduExper10["itemId"] + 'File" name="websitefile" title="' + child[i].EduExper10["itemName"] + '" class="fileupload_input" onchange=SurveyBuild.eduImgUpload(this,"EduExper10")>';
			edus += '		<div style="margin-top: -40px; margin-left: 265px">';
			edus += '			<div id="' + data["itemId"] + child[i].EduExper10["itemId"] + 'Tip" class="onShow" style="margin: 0px; padding: 0px; background: transparent;">';
			edus += '				<div class="onShow"></div>';
			edus += '			</div>';
			edus += '		</div>';
			edus += '	</div>';
			edus += '</div>';

			var filename2 = child[i].EduExper10["filename"];
			edus += '<div class="main_inner_content_info_text"><a id="'+data["itemId"]+child[i].EduExper10["itemId"]+'Attch"  class="fancybox" href="' + TzUniversityContextPath + child[i].EduExper10["accessPath"] + child[i].EduExper10["sysFileName"] + '" target="_blank">' + (filename2 ? filename2.substring(0,20) + "..." : "") + '</a></div>';
			edus += '<input id="'+data["itemId"]+child[i].EduExper10["itemId"]+'" type="hidden" name="'+data["itemId"]+child[i].EduExper10["itemId"]+'" value="'+child[i].EduExper10["value"]+'"></div>';

			//上传成绩单原件扫描件
			edus += '<div class="main_inner_content_info_autoheight">';
			edus += '<div class="main_inner_connent_info_left">' + child[i].EduExper11["itemName"] + '</div>';
			edus += '<div class="main_inner_content_info_right" style="width: 100px;">';
			edus += '	<div class="file_upload_button">';
			edus += '		<div class="bt_blue">' + MsgSet["UPLOAD_BTN_MSG"] + '</div>';
			edus += '		<input type="file" id="' + data["itemId"] + child[i].EduExper11["itemId"] + 'File" name="' + data["itemId"] + child[i].EduExper11["itemId"] + 'File" title="' + child[i].EduExper11["itemName"] + '" class="fileupload_input" onchange=SurveyBuild.eduImgUpload(this,"EduExper11")>';
//			edus += '		<input type="file" id="' + data["itemId"] + child[i].EduExper11["itemId"] + 'File" name="websitefile" title="' + child[i].EduExper11["itemName"] + '" class="fileupload_input" onchange=SurveyBuild.eduImgUpload(this,"EduExper11")>';
			edus += '		<div style="margin-top: -40px; margin-left: 265px">';
			edus += '			<div id="' + data["itemId"] + child[i].EduExper11["itemId"] + 'Tip" class="onShow" style="margin: 0px; padding: 0px; background: transparent;">';
			edus += '				<div class="onShow"></div>';
			edus += '			</div>';
			edus += '		</div>';
			edus += '	</div>';
			edus += '</div>';

			var filename3 = child[i].EduExper10["filename"];
			edus += '<div class="main_inner_content_info_text"><a id="'+data["itemId"]+child[i].EduExper11["itemId"]+'Attch" class="fancybox" href="' + TzUniversityContextPath + child[i].EduExper11["accessPath"] + child[i].EduExper11["sysFileName"] + '" target="_blank">' + (filename3 ? filename3.substring(0,20) + "..." : "") + '</a></div>';
			edus += '<input id="'+data["itemId"]+child[i].EduExper11["itemId"]+'" type="hidden" name="'+data["itemId"]+child[i].EduExper11["itemId"]+'" value="'+child[i].EduExper11["value"]+'"></div>';

			edus += '</div>';
			j++;
		}
		return edus;
	},
	
	 _eventbind: function(data) {
		 var children = data.children,
		 len = children.length;
		 
		 var $studyStage = $("#"+data["itemId"]+children[len-1].EduExper1["itemId"]);
		//就读时间验证
		var $startDt = $("#"+data["itemId"]+children[len-1].EduExper2["itemId"]);
		var $endDt = $("#"+data["itemId"]+children[len-1].EduExper3["itemId"]);
		var $today = $("#"+data["itemId"]+children[len-1].EduExper4["itemId"]);
		
		var $checkboxDiv = $today.closest(".tz_checkbox_div");
		var $toDateDesc = $checkboxDiv.next("span");
		
		var $startDtImg = $("#"+data["itemId"]+children[len-1].EduExper2["itemId"] + "_Btn");
		var $endDtImg = $("#"+data["itemId"]+children[len-1].EduExper3["itemId"] + "_Btn");

		//就读院校
		var $school = $("#"+data["itemId"]+children[len-1].EduExper5["itemId"]);
		var $selectBtn = $("#"+data["itemId"]+children[len-1].EduExper5["itemId"] + "_Btn");
		//专业
		var $major = $("#"+data["itemId"]+children[len-1].EduExper6["itemId"]);
		//毕业证号
		var $byzNo = $("#"+data["itemId"]+children[len-1].EduExper7["itemId"]);
		//学位证号
		var $xwzNo = $("#"+data["itemId"]+children[len-1].EduExper8["itemId"]);
		
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
		
		if (data.isRequire == "Y"){
			$studyStage.formValidator({tipID:data["itemId"]+children[len-1].EduExper1["itemId"]+'Tip',onShow:"",onFocus:"&nbsp;",onCorrect:"&nbsp;"})			
					   .inputValidator({min:'1',onError: children[len-1].EduExper1["itemName"] + MsgSet["REQUIRE"]});
			
			$startDt.formValidator({tipID:data["itemId"]+children[len-1].EduExper2["itemId"]+'Tip',onShow:"",onFocus:"&nbsp;",onCorrect:"&nbsp;"})			
					.inputValidator({min:'1',onError: children[len-1].EduExper2["itemName"] + MsgSet["REQUIRE"]})
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
					});
			$endDt.formValidator({tipID:data["itemId"]+children[len-1].EduExper2["itemId"]+'Tip',onShow:"",onFocus:"&nbsp;",onCorrect:"&nbsp;"})			
				  .inputValidator({min:'1',onError: children[len-1].EduExper2["itemName"] + MsgSet["REQUIRE"]})
				  .functionValidator({	
						fun:function(val,elem){	
							var startTime = $startDt.val();
							var endTime = $endDt.val();
							if (endTime < startTime && endTime !="" && startTime != ""){
								return MsgSet["END_GT_START"];
							}
						}
					});
			$today.formValidator({tipID:data["itemId"]+children[len-1].EduExper2["itemId"]+'Tip',onShow:"",onFocus:"&nbsp;",onCorrect:"&nbsp;"})
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
					});
					
			$school.formValidator({tipID:data["itemId"]+children[len-1].EduExper5["itemId"]+'Tip',onShow:"",onFocus:"&nbsp;",onCorrect:"&nbsp;"})			
				   .inputValidator({min:'1',onError: children[len-1].EduExper5["itemName"] + MsgSet["REQUIRE"]});
				   
			$major.formValidator({tipID:data["itemId"]+children[len-1].EduExper6["itemId"]+'Tip',onShow:"",onFocus:"&nbsp;",onCorrect:"&nbsp;"})			
				   .inputValidator({min:'1',onError: children[len-1].EduExper6["itemName"] + MsgSet["REQUIRE"]});
				   
			$byzNo.formValidator({tipID:data["itemId"]+children[len-1].EduExper7["itemId"]+'Tip',onShow:"",onFocus:"&nbsp;",onCorrect:"&nbsp;"})			
				   .inputValidator({min:'1',onError: children[len-1].EduExper7["itemName"] + MsgSet["REQUIRE"]});
				   
			$xwzNo.formValidator({tipID:data["itemId"]+children[len-1].EduExper8["itemId"]+'Tip',onShow:"",onFocus:"&nbsp;",onCorrect:"&nbsp;"})			
				   .inputValidator({min:'1',onError: children[len-1].EduExper8["itemName"] + MsgSet["REQUIRE"]});
		}
		
		$startDtImg.click(function(e) {
            $startDt.focus();
        });
		$endDtImg.click(function(e) {
            $endDt.focus();
        });
		
		
		//绑定学校选择器
		$.each([$school,$selectBtn],function(i,el){
			el.click(function(e) {
			   // $("#ParamType").val(ID);
				$("#ParamValue").val(data["itemId"]+children[len-1].EduExper5["itemId"]);
                var schoollUrl = SurveyBuild.tzGeneralURL + '?tzParams=';
                var params = '{"ComID":"TZ_COMMON_COM","PageID":"TZ_SCHOOL_STD","OperateType":"HTML","comParams":{"TPLID":"' + templId + '"}}';
                schoollUrl = schoollUrl + window.escape(params);
                s = $.layer({
					type: 2,
					title: false,
					fix: false,
					closeBtn: false,
					shadeClose: false,
					shade : [0.3 , '#000' , true],
					border : [3 , 0.3 , '#000', true],
					offset: ['50%',''],
					area: ['830px','720px'],
					//iframe: {src: '/onlineReg/colselector_liu.html'}
                    iframe:{src:schoollUrl}
				});
			});
		});
		
	}
})