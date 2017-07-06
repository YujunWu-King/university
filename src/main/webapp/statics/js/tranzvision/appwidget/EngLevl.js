SurveyBuild.extend("EngLevl", "baseComponent", {
	itemName: "英语水平",
	title: "英语水平",
	isDoubleLine: "Y",
	fixedContainer: "Y",//固定容器标识
	children: {
		//考试种类:
		"EngLevelType": {
			"instanceId": "EngLevelType",
			"itemId": "exam_type",
			"itemName": MsgSet["EXAM_TYPE"],
			//"itemName":"考试名称",
			"title": MsgSet["EXAM_TYPE"],
			//"title": "考试名称",
			"orderby": 1,
			"value": "",
			"StorageType": "S",
			"classname": "SingleTextBox"
		},
		//通用成绩存储结构
		"EngLevelGrade":{
			"instanceId": "EngLevelGrade",
			"itemId": "exam_score",
			"itemName": MsgSet["EXAM_SCORE"],
			//"itemName":"考试名称",
			"title": MsgSet["EXAM_SCORE"],
			//"title": "考试名称",
			"orderby": 1,
			"value": "",
			"StorageType": "S",
			"classname": "SingleTextBox"
		},
		//通用日期存储结构
		"EngLevelDate": {
			"instanceId": "EngLevelDate",
			"itemId": "exam_date",
			"itemName": MsgSet["EXAM_DATE"],
			//"itemName": "Test date",
			"title": MsgSet["EXAM_DATE"],
			//"title": "Test date",
			"orderby": 2,
			"value": "",
			"StorageType": "S",
			"classname": "DateInputBox"
		},
		//上传文件 存储结构 EngLevelUp---
		"EngLevelUp": {
			"instanceId": "EngLevelUp",
			"prompt": "请上传格式为.docx、.txt的文件，大小为2M以内。",
			"itemId": "exam_upload",
			"itemName": MsgSet["EXAM_UPLOAD"],
			//"itemName": "扫描件上传",
			"title": MsgSet["EXAM_UPLOAD"],
			"isRequire":"Y",
			"title": "扫描件上传",
			"orderby": 20,
			"value": "",
			"StorageType": "F",
			"classname": "imagesUpload",
			"filename": "",
			"sysFileName": "",
			"path": "",
			"accessPath": "",
			"fileType": "jpg,png,jpeg,pdf",//允许上传类型
		    "fileSize": "1",//允许上传大小
		    "isAllowTailoring":"N",   //是否允许裁剪
		    "tailoringStandard":"",   //裁剪类型
			"allowMultiAtta": "Y",//允许多附件上传
			"isDownLoad":"Y",//允许打包下载
			"StorageType":"F",//存储类型-附件
			"children": [{"itemId":"attachment_Upload","itemName":"图片上传","title":"图片上传","orderby":"","fileName":"","sysFileName":"","accessPath":"","viewFileName":""}]

		}
	},
	minLines: "1",
	maxLines: "4",
	
	defaultLines:1,
	_init: function(d, previewmode) {
		var linesNo = [];
		for (var i = 1; i < this.maxLines; i++) {
			linesNo.push(i);
		}
		
		this["linesNo"] = linesNo;
	},
	_getHtml: function(data, previewmode) {
		var c = ""

		if (previewmode) {
			var showLines;
			var len = data.children.length;
			if(len>=data.defaultLines)
			{
				showLines = len;
			}else{
				showLines = data.defaultLines;
			}
			var htmlContent="";
			if(SurveyBuild.accessType == "M"){
				
				for(var i=1;i<=showLines;i++){
					var tempHtmlP = this._getHtmlTwo(data,i);
					htmlContent += tempHtmlP;
				}
				if(!SurveyBuild._readonly){
					
					if(len<data.maxLines){
						htmlContent += '<div class="clear"><div class="add_next" onclick="SurveyBuild.addTjx(this,\'' + data.instanceId + '\')">' + MsgSet["ADD_ONE"] + '</div>';
						htmlContent += '</div>';
					}else{
						htmlContent += '<div class="clear" style="display: none"><div class="add_next" onclick="SurveyBuild.addTjx(this,\'' + data.instanceId + '\')>' + MsgSet["ADD_ONE"] + '</div>';
						htmlContent += '</div>';
					}
				}
				c += htmlContent;
			}else{

				for(var i=1;i<=showLines;i++){
					var tempHtml = this._getHtmlOne(data,i);
					htmlContent += tempHtml;
					if(i>1){
						data["linesNo"].shift();
					}
				}
				c += '<div class="main_inner_content">';
				c += htmlContent;
				//--------
				/*添加 (添加下一条) 按钮*/
				c += '	<div class="addNext">';
				if(len<data.maxLines){
					c += '		<div style="float:right" class="main_inner_content_info_add addnextbtn" id="save_and_add0" onclick="SurveyBuild.addTjx(this,\'' + data.instanceId + '\');">';
					c += '			<div class="input-addbtn">' + MsgSet["ADD_ONE"] + '</div>';
					c += '		</div>';
					c += '	</div>';
				}
				else{
					c += '		<div style="float:right;display:none" class="main_inner_content_info_add addnextbtn" id="save_and_add0" onclick="SurveyBuild.addTjx(this,\'' + data.instanceId + '\');">';
					c += '			<div class="input-addbtn">' + MsgSet["ADD_ONE"] + '</div>';
					c += '		</div>';
					c += '	</div>';
				}
				//--------
				c += '</div>';
				c += '<div class="main_inner_content_foot"><div class="clear"></div></div>';
			}
			
		} else {
			var htmlRead = '';
			//考试类型类型
			htmlRead += '<div class="type_item_li">';
			htmlRead += '	<span class="type_item_label">'+MsgSet["EXAM_TYPE_T"]+'：</span>';
			htmlRead += '		<b class="read-select" style="min-width:120px;">'+MsgSet["PLEASE_SELECT"]+'</b>';
			htmlRead += '	</div>';

			//成绩显示DIV
			htmlRead += '<div class="type_item_li">';
			htmlRead += '	<span class="type_item_label">'+MsgSet["EXAM_SCORE_T"]+'：</span>';
			htmlRead += '		<b class="read-input" style="min-width:120px;">'+MsgSet["SHOW_SCORE"]+'</b>';
			htmlRead += '	</div>';
			
			
			c += '<div class="question-answer"  style="border:1px solid #ddd;padding:10px;">';
			c += '	<div class="DHContainer">' + htmlRead + '</div>';
			c += '</div>';
		}
		return c;
	},
	_edit: function(data) {
		var e = '';

		e += '<div class="edit_jygz">';
		e += '	<span class="title"><i class="icon-info-sign"></i> 参数设置</span>';
		e += '  <div class="groupbox">';
		e += '  <div class="edit_item_warp">';
		e += '      <span class="edit_item_label">最小行数：</span>';
		e += '     <input type="text" class="medium" onkeyup="SurveyBuild.saveAttr(this,\'minLines\')" value="' + data.minLines + '"/>';
		e += '  </div>';

		e += '  <div class="edit_item_warp mb10">';
		e += '      <span class="edit_item_label">最大行数：</span>';
		e += '     <input type="text" class="medium" onkeyup="SurveyBuild.saveAttr(this,\'maxLines\')" value="' + data.maxLines + '"/>';
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
	//成绩input:
	getNumGradeDiv:function(data,top_id,grade_id,grade_val,grade_label,dateHtml){
		var gradeDiv="";
		gradeDiv+=dateHtml
		gradeDiv+='<div name="'+top_id+'_GRADE_DIV" id="'+top_id+'_GRADE_DIV" class="input-list">';
		gradeDiv+='<div class="input-list-info left"><span class="red-star">' + (data.isRequire == "Y" ? "*": "") + '</span><span id="'+grade_id+'Label">'+grade_label+'：</span></div> <div class="input-list-text left"><input onkeyup="this.value=this.value.replace(/[^\\d.]/g,\'\')" onafterpaste="this.value=this.value.replace(/[^\\d.]/g,\'\')" class="inpu-list-text-enter" style="height:36px;" id="'+grade_id+'" value="'+grade_val+'"/>'
		//---------input格式检验:
		gradeDiv += '<div style="margin-top: -40px; margin-left: 330px">';
		gradeDiv += '	<div id="' +grade_id + 'Tip" class="onShow" style="margin: 0px; padding: 0px; background: transparent;">';
		gradeDiv += '		<div class="onShow"></div>';
		gradeDiv += '	</div>';
		gradeDiv += '</div>';
		//------------------------
		gradeDiv+='</div><div class="clear"></div></div>';
		return gradeDiv;
	},
	//成绩:select
	getChoseGradeDiv:function(data,top_id,grade_id,grade_val,grade_label,optList,dateHtml){
		var gradeDiv="";
		gradeDiv+=dateHtml;
		gradeDiv+='<div name="'+top_id+'_GRADE_DIV" id="'+top_id+'_GRADE_DIV" class="input-list">'
			gradeDiv+='<div class="input-list-info left"><span class="red-star">' + (data.isRequire == "Y" ? "*": "") + '</span><span >'+grade_label+'：</span></div>'
			gradeDiv+='<div class="input-list-text left" ><select  style="width:255px;height:36px" id="'+grade_id+'" value="'+grade_val+'">'
			gradeDiv+=optList
			gradeDiv+='</select></div><div class="clear"></div>'
		gradeDiv+='</div>'
		return gradeDiv;
	},
	//日期:input
	getDateDiv:function(data,top_id,date_id,date_val,date_name,date_title,flg){
		var DATE_DIV='';
		if(flg!=undefined&&flg=="N"){
			DATE_DIV += '  <div id="'+top_id+'_DATE_DIV" class="input-list" style="display:none"><span class="input-list-info left"><span class="red-star">' + (data.isRequire == "Y" ? "*": "") + '</span>'+date_title+'：</span>';
		}else{
			DATE_DIV += '  <div id="'+top_id+'_DATE_DIV" class="input-list" style="display:block"><span class="input-list-info left"><span class="red-star">' + (data.isRequire == "Y" ? "*": "") + '</span>'+date_title+'：</span>';
		}
		
		//DATE_DIV += '     <div class="input-list-text left"> <input id="' + date_id+ '" name="' + date_name+ '" type="text" value="'  +date_val + '"class="inpu-list-text-enter" style="height:36px" readonly="readonly" onchange="SurveyBuild.reFocus(\'' + date_id + '\'); title="' +date_name + '">';
		
		DATE_DIV += '     <div class="input-list-text left"> <input id="' + date_id+ '" name="' + date_name+ '" type="text" value="'  +date_val + '"class="inpu-list-text-enter" style="height:36px" readonly="readonly" onchange="this.blur()"; title="' +date_name + '" />';
		DATE_DIV += '      <img id="' + date_id+ '_Clear" src="' + TzUniversityContextPath + '/statics/images/appeditor/new/close.png" style="position:relative;top:5px;left:-61px;cursor:pointer;' + (date_val == "" ? ";visibility:hidden;": "") + '" />';
		DATE_DIV += '      <img id="' + date_id+ '_Btn" src="' + TzUniversityContextPath + '/statics/images/appeditor/new/calendar.png" style="position:relative;top:5px;left:-58px;cursor:pointer;" />';
		//DATE_DIV += ' <div class="clear"> </div>';
		//---------日期 input格式检验:
		DATE_DIV += '<div style="margin-top: -40px; margin-left: 330px">';
		DATE_DIV += '	<div id="' + date_id + 'Tip" class="onShow" style="margin: 0px; padding: 0px; background: transparent;">';
		DATE_DIV += '		<div class="onShow"></div>';
		DATE_DIV += '	</div>';
		DATE_DIV += '</div>';
		//---------
		DATE_DIV += ' </div><div class="clear"></div></div>';
		return DATE_DIV;
	},
	//只读日期:
	getDateRead:function(data,date_id,date_val,date_name,date_title){
		var DATE_DIV='';
		DATE_DIV += '  <div class="input-list" style="display:block"><div class="input-list-info left"><span class="red-star">' + (data.isRequire == "Y" ? "*": "") + '</span><span>'+date_title+'：</span></div>';
		
		DATE_DIV += '     <div class="input-list-text left">';
		DATE_DIV +=date_val;
		DATE_DIV += '	  </div>'
		//DATE_DIV += ' <div class="clear"> </div>';
		DATE_DIV += '  </div>';
		return DATE_DIV;
	},
	//只读模式DIV->除开上传部位
	getReadDiv:function(data,child,EXAM_TYPE_DEF,EXAM_TYPE_MAP){
		//
		var label="";
		//---所有的成绩：
		var val=child.EngLevelGrade.value;
		//------------------1.2.3.4.13有日期
		var dateHtml="";
		if(EXAM_TYPE_DEF==EXAM_TYPE_MAP.ENG_LEV_T1||EXAM_TYPE_DEF==EXAM_TYPE_MAP.ENG_LEV_T2){
			dateHtml=this.getDateRead(data,data.itemId +child.EngLevelDate.itemId,child.EngLevelDate.value,data.itemId+child.EngLevelDate.name,MsgSet["EXAM_TDATE"]);
			label=MsgSet["EXAM_TSCORE"];
		}else if(EXAM_TYPE_DEF==EXAM_TYPE_MAP.ENG_LEV_T3){
			dateHtml=this.getDateRead(data,data.itemId +child.EngLevelDate.itemId,child.EngLevelDate.value,data.itemId+child.EngLevelDate.name,MsgSet["EXAM_TDATE"]);
			label=MsgSet["EXAM_TOTAL"];

		}else if(EXAM_TYPE_DEF==EXAM_TYPE_MAP.ENG_LEV_T4){
			dateHtml=this.getDateRead(data,data.itemId +child.EngLevelDate.itemId,child.EngLevelDate.value,data.itemId+child.EngLevelDate.name,MsgSet["EXAM_TDATE"]);
			label=MsgSet["EXAM_ISCORE"];

		}else if(EXAM_TYPE_DEF==EXAM_TYPE_MAP.ENG_LEV_T5||EXAM_TYPE_DEF==EXAM_TYPE_MAP.ENG_LEV_T6||EXAM_TYPE_DEF==EXAM_TYPE_MAP.ENG_LEV_T7||EXAM_TYPE_DEF==EXAM_TYPE_MAP.ENG_LEV_T8){
			dateHtml=this.getDateRead(data,data.itemId +child.EngLevelDate.itemId,child.EngLevelDate.value,data.itemId+child.EngLevelDate.name,"考试日期");
			label=MsgSet["EXAM_GSCORE"];
			
		}
		//else if(EXAM_TYPE_DEF==EXAM_TYPE_MAP.ENG_LEV_T7||EXAM_TYPE_DEF==EXAM_TYPE_MAP.ENG_LEV_T8){
		//	label=MsgSet["EXAM_PASS"];

		//}
		else if(EXAM_TYPE_DEF==EXAM_TYPE_MAP.ENG_LEV_T9){
			dateHtml=this.getDateRead(data,data.itemId +child.EngLevelDate.itemId,child.EngLevelDate.value,data.itemId+child.EngLevelDate.name,"考试日期");
			label=MsgSet["EXAM_GSCORE"];
			
		}else if(EXAM_TYPE_DEF==EXAM_TYPE_MAP.ENG_LEV_T10||EXAM_TYPE_DEF==EXAM_TYPE_MAP.ENG_LEV_T11){
			label=MsgSet["EXAM_GSCORE"];
			
		}else if(EXAM_TYPE_DEF==EXAM_TYPE_MAP.ENG_LEV_T12){
			label=MsgSet["EXAM_GSCORE"];
			
		}else if(EXAM_TYPE_DEF==EXAM_TYPE_MAP.ENG_LEV_T13){
			dateHtml=this.getDateRead(data,data.itemId +child.EngLevelDate.itemId,child.EngLevelDate.value,data.itemId+child.EngLevelDate.name,MsgSet["EXAM_TDATE"]);
			label=MsgSet["EXAM_TSCORE"];
		}
		//------------------
		var ReadDiv="";
		ReadDiv+='<div name="'+data.itemId+'_READ_DIV" id="'+data.itemId+'_READ_DIV" class="input-list">'
			//只读考试名称:
			ReadDiv+='<div class="input-list-info left"><span class="red-star">' + (data.isRequire == "Y" ? "*": "") + '</span><span>'+child.EngLevelType.itemName+'：</span></div> '
			ReadDiv+='<div class="input-list-text left">'
			ReadDiv+=EXAM_TYPE_DEF;
			ReadDiv+='</div>'	
			//ReadDiv+='<div class="clear"></div>'
			//日期:	
			ReadDiv+=dateHtml
			//值:
			//ReadDiv+='<div class="clear"></div>'
			ReadDiv+='<div class="input-list-info left"><span class="red-star">' + (data.isRequire == "Y" ? "*": "") + '</span><span>'+label+'：</span></div> '
			ReadDiv+='<div class="input-list-text left">'
			ReadDiv+=val;
			ReadDiv+='</div>'
		ReadDiv+='</div>';
		//------------------"无"选项:
			if(EXAM_TYPE_DEF==EXAM_TYPE_MAP.ENG_LEV_T0||EXAM_TYPE_DEF==""){
				ReadDiv="";
				ReadDiv+='<div name="'+data.itemId+'_READ_DIV" id="'+data.itemId+'_READ_DIV" class="input-list">'
					//只读考试名称:
					ReadDiv+='<div class="input-list-info left"><span class="red-star">' + (data.isRequire == "Y" ? "*": "") + '</span><span>'+child.EngLevelType.itemName+'：</span></div> '
					ReadDiv+='<div class="input-list-text left">'
					ReadDiv+=EXAM_TYPE_DEF;
					ReadDiv+='</div>'	
					ReadDiv+='<div class="clear"></div>'
				ReadDiv+='</div>';
			}
		//------------------
		return ReadDiv;
	},
	//考试类型“关联div”切换:
	getRelatedDiv:function(data,child,EXAM_TYPE_DEF,EXAM_TYPE_MAP){
		//alert("data:"+EXAM_TYPE_DEF+"  val:"+EXAM_TYPE_MAP.ENG_LEV_T6)
		var upTips=$("#"+child.EngLevelUp.itemId+"_greGmatTips");
		if(upTips!=undefined){

			if(EXAM_TYPE_DEF==EXAM_TYPE_MAP.ENG_LEV_T1||EXAM_TYPE_DEF==EXAM_TYPE_MAP.ENG_LEV_T2){
				upTips.show();
			}else{
				
				upTips.hide();
			}
		}
		var RELATED_DIV="";
		var DATE_HTML=this.getDateDiv(data,data.itemId,data.itemId +child.EngLevelDate.itemId,child.EngLevelDate.value,data.itemId+child.EngLevelDate.name,MsgSet["EXAM_TDATE"],"N");
		if(EXAM_TYPE_DEF==EXAM_TYPE_MAP.ENG_LEV_T0||EXAM_TYPE_DEF==""||EXAM_TYPE_DEF==EXAM_TYPE_MAP.ENG_LEV_T1||EXAM_TYPE_DEF==EXAM_TYPE_MAP.ENG_LEV_T2||EXAM_TYPE_DEF==EXAM_TYPE_MAP.ENG_LEV_T3||EXAM_TYPE_DEF==EXAM_TYPE_MAP.ENG_LEV_T4||EXAM_TYPE_DEF==EXAM_TYPE_MAP.ENG_LEV_T13){
			DATE_HTML=this.getDateDiv(data,data.itemId,data.itemId +child.EngLevelDate.itemId,child.EngLevelDate.value,data.itemId+child.EngLevelDate.name,MsgSet["EXAM_TDATE"]);
		}
		if(EXAM_TYPE_DEF==EXAM_TYPE_MAP.ENG_LEV_T9||EXAM_TYPE_DEF==EXAM_TYPE_MAP.ENG_LEV_T5||EXAM_TYPE_DEF==EXAM_TYPE_MAP.ENG_LEV_T6||EXAM_TYPE_DEF==EXAM_TYPE_MAP.ENG_LEV_T7||EXAM_TYPE_DEF==EXAM_TYPE_MAP.ENG_LEV_T8){
			DATE_HTML=this.getDateDiv(data,data.itemId,data.itemId +child.EngLevelDate.itemId,child.EngLevelDate.value,data.itemId+child.EngLevelDate.name,"考试日期");
		}
		if(EXAM_TYPE_DEF==EXAM_TYPE_MAP.ENG_LEV_T0||EXAM_TYPE_DEF==""){
			RELATED_DIV=this.getDateDiv(data,data.itemId,data.itemId +child.EngLevelDate.itemId,child.EngLevelDate.value,data.itemId+child.EngLevelDate.name,MsgSet["EXAM_TDATE"],"N");
		}
		if(EXAM_TYPE_DEF==EXAM_TYPE_MAP.ENG_LEV_T1||EXAM_TYPE_DEF==EXAM_TYPE_MAP.ENG_LEV_T2){
			RELATED_DIV=this.getNumGradeDiv(data,data.itemId,data.itemId+child.EngLevelGrade.itemId,child.EngLevelGrade.value,MsgSet["EXAM_TSCORE"],DATE_HTML);
		}else if(EXAM_TYPE_DEF==EXAM_TYPE_MAP.ENG_LEV_T3){
			RELATED_DIV=this.getNumGradeDiv(data,data.itemId,data.itemId+child.EngLevelGrade.itemId,child.EngLevelGrade.value,MsgSet["EXAM_TOTAL"],DATE_HTML);
		}else if(EXAM_TYPE_DEF==EXAM_TYPE_MAP.ENG_LEV_T4){
			RELATED_DIV=this.getNumGradeDiv(data,data.itemId,data.itemId+child.EngLevelGrade.itemId,child.EngLevelGrade.value,MsgSet["EXAM_ISCORE"],DATE_HTML);
		}else if(EXAM_TYPE_DEF==EXAM_TYPE_MAP.ENG_LEV_T5||EXAM_TYPE_DEF==EXAM_TYPE_MAP.ENG_LEV_T6||EXAM_TYPE_DEF==EXAM_TYPE_MAP.ENG_LEV_T7||EXAM_TYPE_DEF==EXAM_TYPE_MAP.ENG_LEV_T8){
			RELATED_DIV=this.getNumGradeDiv(data,data.itemId,data.itemId+child.EngLevelGrade.itemId,child.EngLevelGrade.value,MsgSet["EXAM_GSCORE"],DATE_HTML);
		}
//		else if(EXAM_TYPE_DEF==EXAM_TYPE_MAP.ENG_LEV_T7||EXAM_TYPE_DEF==EXAM_TYPE_MAP.ENG_LEV_T8){
//			var optList='<option value="'+MsgSet["PASS_Y"]+'">'+MsgSet["PASS_Y"]+'</option>';
//				optList+=('<option value="'+MsgSet["PASS_N"]+'">'+MsgSet["PASS_N"]+'</option>');
//			RELATED_DIV=this.getChoseGradeDiv(data.itemId,data.itemId+child.EngLevelGrade.itemId,child.EngLevelGrade.value,MsgSet["EXAM_PASS"],optList);//,child.EngLevelGrade.value
//		}
		else if(EXAM_TYPE_DEF==EXAM_TYPE_MAP.ENG_LEV_T9){
			var optList="";
			if(child.EngLevelGrade.value==MsgSet["EXAM_TEM4"]){
				optList='<option selected="selected" value="'+MsgSet["EXAM_TEM4"]+'">'+MsgSet["EXAM_TEM4"]+'</option>';
				optList+=('<option  value="'+MsgSet["EXAM_TEM8"]+'">'+MsgSet["EXAM_TEM8"]+'</option>');
			}
			else if(child.EngLevelGrade.value==MsgSet["EXAM_TEM8"]){
				optList='<option  value="'+MsgSet["EXAM_TEM4"]+'">'+MsgSet["EXAM_TEM4"]+'</option>';
				optList+=('<option selected="selected" value="'+MsgSet["EXAM_TEM8"]+'">'+MsgSet["EXAM_TEM8"]+'</option>');
			}
			RELATED_DIV=this.getChoseGradeDiv(data,data.itemId,data.itemId+child.EngLevelGrade.itemId,child.EngLevelGrade.value,MsgSet["EXAM_GSCORE"],optList,DATE_HTML);//,child.EngLevelGrade.value EXAM_GSCORE
		}else if(EXAM_TYPE_DEF==EXAM_TYPE_MAP.ENG_LEV_T10||EXAM_TYPE_DEF==EXAM_TYPE_MAP.ENG_LEV_T11){
			var optList="";
			if(child.EngLevelGrade.value==MsgSet["INTER_A"]){
				optList='<option selected="selected" value="'+MsgSet["INTER_A"]+'">'+MsgSet["INTER_A"]+'</option>';
				optList+=('<option  value="'+MsgSet["INTER_B"]+'">'+MsgSet["INTER_B"]+'</option>');
			}
			else if(child.EngLevelGrade.value==MsgSet["INTER_B"]){
				optList='<option value="'+MsgSet["INTER_A"]+'">'+MsgSet["INTER_A"]+'</option>';
				optList+=('<option selected="selected"  value="'+MsgSet["INTER_B"]+'">'+MsgSet["INTER_B"]+'</option>');
			}
			RELATED_DIV=this.getChoseGradeDiv(data,data.itemId,data.itemId+child.EngLevelGrade.itemId,child.EngLevelGrade.value,MsgSet["EXAM_GSCORE"],optList,DATE_HTML);//,child.EngLevelGrade.value EXAM_GSCORE
		}else if(EXAM_TYPE_DEF==EXAM_TYPE_MAP.ENG_LEV_T12){//LEV_C
			var optList="";
			if(child.EngLevelGrade.value==MsgSet["LEV_A"]){
				optList='<option selected="selected" value="'+MsgSet["LEV_A"]+'">'+MsgSet["LEV_A"]+'</option>';
				optList+=('<option  value="'+MsgSet["LEV_B"]+'">'+MsgSet["LEV_B"]+'</option>');
				optList+=('<option  value="'+MsgSet["LEV_C"]+'">'+MsgSet["LEV_C"]+'</option>');
			}
			if(child.EngLevelGrade.value==MsgSet["LEV_B"]){
				optList='<option  value="'+MsgSet["LEV_A"]+'">'+MsgSet["LEV_A"]+'</option>';
				optList+=('<option  selected="selected" value="'+MsgSet["LEV_B"]+'">'+MsgSet["LEV_B"]+'</option>');
				optList+=('<option  value="'+MsgSet["LEV_C"]+'">'+MsgSet["LEV_C"]+'</option>');
			}
			if(child.EngLevelGrade.value==MsgSet["LEV_C"]){
				optList='<option  value="'+MsgSet["LEV_A"]+'">'+MsgSet["LEV_A"]+'</option>';
				optList+=('<option  value="'+MsgSet["LEV_B"]+'">'+MsgSet["LEV_B"]+'</option>');
				optList+=('<option selected="selected"  value="'+MsgSet["LEV_C"]+'">'+MsgSet["LEV_C"]+'</option>');
			}
			RELATED_DIV=this.getChoseGradeDiv(data,data.itemId,data.itemId+child.EngLevelGrade.itemId,child.EngLevelGrade.value,MsgSet["EXAM_GSCORE"],optList,DATE_HTML);//EXAM_SCORE
		}else if(EXAM_TYPE_DEF==EXAM_TYPE_MAP.ENG_LEV_T13){
			RELATED_DIV=this.getNumGradeDiv(data,data.itemId,data.itemId+child.EngLevelGrade.itemId,child.EngLevelGrade.value,MsgSet["EXAM_TSCORE"],DATE_HTML);
		}
		return RELATED_DIV;
	},
	getChangedGradeDiv:function(data,child,EXAM_TYPE_DEF,EXAM_TYPE_MAP){
		var RELATED_DIV="";
		var DATE_HTML="";
		if(EXAM_TYPE_DEF==EXAM_TYPE_MAP.ENG_LEV_T1||EXAM_TYPE_DEF==EXAM_TYPE_MAP.ENG_LEV_T2||EXAM_TYPE_DEF==""){
			RELATED_DIV=this.getNumGradeDiv(data,data.itemId,data.itemId+child.EngLevelGrade.itemId,child.EngLevelGrade.value,MsgSet["EXAM_TSCORE"],DATE_HTML);
		}else if(EXAM_TYPE_DEF==EXAM_TYPE_MAP.ENG_LEV_T3){
			RELATED_DIV=this.getNumGradeDiv(data,data.itemId,data.itemId+child.EngLevelGrade.itemId,child.EngLevelGrade.value,MsgSet["EXAM_TOTAL"],DATE_HTML);
		}else if(EXAM_TYPE_DEF==EXAM_TYPE_MAP.ENG_LEV_T4){
			RELATED_DIV=this.getNumGradeDiv(data,data.itemId,data.itemId+child.EngLevelGrade.itemId,child.EngLevelGrade.value,MsgSet["EXAM_ISCORE"],DATE_HTML);
		}else if(EXAM_TYPE_DEF==EXAM_TYPE_MAP.ENG_LEV_T5||EXAM_TYPE_DEF==EXAM_TYPE_MAP.ENG_LEV_T6||EXAM_TYPE_DEF==EXAM_TYPE_MAP.ENG_LEV_T7||EXAM_TYPE_DEF==EXAM_TYPE_MAP.ENG_LEV_T8){
			RELATED_DIV=this.getNumGradeDiv(data,data.itemId,data.itemId+child.EngLevelGrade.itemId,child.EngLevelGrade.value,MsgSet["EXAM_GSCORE"],"");
		}
//		else if(EXAM_TYPE_DEF==EXAM_TYPE_MAP.ENG_LEV_T7||EXAM_TYPE_DEF==EXAM_TYPE_MAP.ENG_LEV_T8){
//			var optList='<option value="'+MsgSet["PASS_Y"]+'">'+MsgSet["PASS_Y"]+'</option>';
//				optList+=('<option value="'+MsgSet["PASS_N"]+'">'+MsgSet["PASS_N"]+'</option>');
//			RELATED_DIV=this.getChoseGradeDiv(data.itemId,data.itemId+child.EngLevelGrade.itemId,child.EngLevelGrade.value,MsgSet["EXAM_PASS"],optList);//,child.EngLevelGrade.value
//		}
		else if(EXAM_TYPE_DEF==EXAM_TYPE_MAP.ENG_LEV_T9){
			var optList='<option value="'+MsgSet["EXAM_TEM4"]+'">'+MsgSet["EXAM_TEM4"]+'</option>';
			optList+=('<option value="'+MsgSet["EXAM_TEM8"]+'">'+MsgSet["EXAM_TEM8"]+'</option>');
			RELATED_DIV=this.getChoseGradeDiv(data,data.itemId,data.itemId+child.EngLevelGrade.itemId,child.EngLevelGrade.value,MsgSet["EXAM_GSCORE"],optList,DATE_HTML);//,child.EngLevelGrade.value EXAM_GSCORE
		}else if(EXAM_TYPE_DEF==EXAM_TYPE_MAP.ENG_LEV_T10||EXAM_TYPE_DEF==EXAM_TYPE_MAP.ENG_LEV_T11){
			var optList='<option value="'+MsgSet["INTER_A"]+'">'+MsgSet["INTER_A"]+'</option>';
			optList+=('<option value="'+MsgSet["INTER_B"]+'">'+MsgSet["INTER_B"]+'</option>');
			RELATED_DIV=this.getChoseGradeDiv(data,data.itemId,data.itemId+child.EngLevelGrade.itemId,child.EngLevelGrade.value,MsgSet["EXAM_GSCORE"],optList,DATE_HTML);//,child.EngLevelGrade.value EXAM_GSCORE
		}else if(EXAM_TYPE_DEF==EXAM_TYPE_MAP.ENG_LEV_T12){//LEV_C
			var optList='<option value="'+MsgSet["LEV_A"]+'">'+MsgSet["LEV_A"]+'</option>';
			optList+=('<option value="'+MsgSet["LEV_B"]+'">'+MsgSet["LEV_B"]+'</option>');
			optList+=('<option value="'+MsgSet["LEV_C"]+'">'+MsgSet["LEV_C"]+'</option>');
			RELATED_DIV=this.getChoseGradeDiv(data,data.itemId,data.itemId+child.EngLevelGrade.itemId,child.EngLevelGrade.value,MsgSet["EXAM_GSCORE"],optList,DATE_HTML);//EXAM_SCORE
		}else if(EXAM_TYPE_DEF==EXAM_TYPE_MAP.ENG_LEV_T13){
			RELATED_DIV=this.getNumGradeDiv(data,data.itemId,data.itemId+child.EngLevelGrade.itemId,child.EngLevelGrade.value,MsgSet["EXAM_TSCORE"],DATE_HTML);
		}
		return RELATED_DIV;
	},

	bindTimePickerM:function (dateIdGroup) {

		dateIdGroup.each(function(){

			var calendar = new LCalendar();
			calendar.init({
				'trigger': '#' + $(this).attr('id'), //标签id
				'type': "date", //date 调出日期选择 datetime 调出日期时间选择 time 调出时间选择 ym 调出年月选择,
				'minDate': "1960-01-01", //最小日期
				'maxDate':"2030-12-31"
			});
		})
	},

	bindTimePicker:function($inputBox,$selectBtn,$clearBtn){

		$inputBox.each(function(){
			   var me=$(this);
			  $(this).datetimepicker({
				showButtonPanel:true,
				showTimepicker: false,
				changeMonth: true,
				changeYear: true,
				maxDate: new Date("2030-12-31"),
				yearRange: "1900:2030",
				dateFormat:"yy-mm-dd",
				onSelect:function(dateText, inst){
					me.datetimepicker("hide");
					me.trigger("blur");
					me.next().css("visibility","visible");
				}
			  });
		 });
		   //日期小图标事件绑定:
		 $selectBtn.each(function(){
			   $(this).click(function(){
				   $(this).prev().prev().focus();
			   })
		  });
		 
		 $clearBtn.each(function(){
			 $(this).click(function(){
				 $(this).prev().val("");
				 $(this).css("visibility","hidden");
			 })
		 });
	},
	checkInputNotNull:function(inputEl){
		inputEl.formValidator({tipID:(inputEl.attr("id")+'Tip'), onShow:"", onFocus:"&nbsp;", onCorrect:"&nbsp;"});
		inputEl.functionValidator({
				fun:function(val,el){
					if(val==""){
						return "此项必填";
					}else{
						return true;
					}
				}	
			}); 
	},
	_getHtmlOne: function(data,index) {
		/**/
		if (SurveyBuild.appInsId=="0"&&data.children.length<data.defaultLines){
			SurveyBuild.showTjx(this,data.instanceId);
			
		}else if (data.children.length<data.defaultLines){
			SurveyBuild.showTjx(this,data.instanceId);
		}
		
		/*已填写的数据的行数*/
		//多行容器处理：
		var len = data.children.length;
		var edus = "",j = 0,childList = [];
		if(len==undefined){
			len=1;
		}
		var x = index - 1;
		childList = data.children;

		//考试种类OPT
		//var TzUniversityContextPath="/university";
		var EXAM_TYPE_MAP={
			"ENG_LEV_T0":"无",
			"ENG_LEV_T1":"GRE",
			"ENG_LEV_T2":"GMAT",
			"ENG_LEV_T3":"TOEFL",
			"ENG_LEV_T4":"IELTS",
			"ENG_LEV_T5":"英语六级（710分制）",
			"ENG_LEV_T6":"英语四级（710分制）",
			"ENG_LEV_T7":"英语六级（100分制）",
			"ENG_LEV_T8":"英语四级（100分制）",
			"ENG_LEV_T9":"专业英语",
			"ENG_LEV_T10":"高级口译",
			"ENG_LEV_T11":"中级口译",
			"ENG_LEV_T12":"BEC",
			"ENG_LEV_T13":"TOEIC（990）"				
		};
		var htmlContent="";
		//--考试种类初始值
			htmlContent += '<div id="main_inner_content_para'+index+'" class="main_inner_content_para">';
			htmlContent+='<div class="clear"></div>'
			htmlContent += '<div class="mainright-title">';
				htmlContent += '<span class="title-line"></span>' + MsgSet["ENG_LEV"]  +index+ ' :</div>';
				//加入一段提示文字:
				//htmlContent += '<span class="title-line"></span>' + MsgSet["ENG_LEV"]  +index+ MsgSet["ENG_LEV_TIPS"]+'</div>';
			htmlContent += '<div class="mainright-box pos-rela">';
			//-----
			if(index > 1&&!SurveyBuild._readonly){
				htmlContent += '		<div onclick="SurveyBuild.deleteEngLev(this);" class="input-delbtn">' + MsgSet["DEL"] + '&nbsp;&nbsp;<span class="input-btn-icon"><img src="' + TzUniversityContextPath + '/statics/images/appeditor/new/add-delete.png"></span></div>';
			}
			var child=childList[x];
			var EXAM_TYPE_DEF=child.EngLevelType.value;

			//1.公司性质
			if (SurveyBuild._readonly) {
				htmlContent += '<div  class="main_inner_content_info_autoheight">';
				//<!--通用只读模式 成绩+日期显示-->
				htmlContent+=this.getReadDiv(data,child,EXAM_TYPE_DEF,EXAM_TYPE_MAP);
				//<!--通用上传控件部分-->
				if(EXAM_TYPE_DEF==EXAM_TYPE_MAP.ENG_LEV_T0||EXAM_TYPE_DEF==""){
					htmlContent+='<div name="'+data.itemId+'UP" id="'+data.itemId+'UP" class="input-list" style="display:none">'
				}else{
					htmlContent+='<div name="'+data.itemId+'UP" id="'+data.itemId+'UP" class="input-list" style="display:block">'
				}
				htmlContent+='<div class="clear"></div>';//证书/成绩扫描件
				htmlContent+='<div class="input-list-info left"><span >'+MsgSet["SCORE_UP"]+':</span></div>'
				//var filename = child.EngLevelUp.filename;
				//htmlContent += '<div class="main_inner_content_info_text"><a id="'+data["itemId"]+child.EngLevelUp["itemId"]+'Attch" class="fancybox" href="' +child.EngLevelUp.accessPath + child.EngLevelUp.sysFileName + '" target="_blank">' + (filename ? filename.substring(0,20) + "..." : "") + '</a></div>';
				//htmlContent += '<input id="'+data["itemId"]+child.EngLevelUp.itemId+'Attch" type="hidden" name="'+data["itemId"]+child.EngLevelUp.itemId+'Attch" value="'+child.EngLevelUp.itemId+'"></div>';
			//----------------------------
				var childrenAttr=child.EngLevelUp.children;
				htmlContent+= '	<div class="input-list-texttemplate left">';
			        htmlContent+= '		<div class="input-list-upload-con" id="' + child.EngLevelUp.itemId+ '_AttList" style="display:' + (childrenAttr.length < 1 ? 'none':'black') + '">';
			        htmlContent+='<div class="filebtn left">'
			        if(child.EngLevelUp.allowMultiAtta == "Y"){
			        	//alert(childrenAttr.length);
		        		for(var index=0; index<childrenAttr.length; index++){
		        			if (childrenAttr[index].viewFileName != "" && childrenAttr[index].sysFileName != ""){
		        				htmlContent+= '<div class="input-list-uploadcon-list">';
		        				htmlContent+= '	<div class="input-list-uploadcon-listl left"><a class="input-list-uploadcon-list-a" onclick=SurveyBuild.engViewImageSet(this,"' + data.instanceId + '","'+ child.EngLevelUp.instanceId +'") file-index="' + childrenAttr[index].orderby + '">' + childrenAttr[index].viewFileName + '</a></div>';
		        				htmlContent+= '<div class="input-list-uploadcon-listr left" style="display: ' + (SurveyBuild._readonly?'none':'block') + ';line-height:46px;" onclick="SurveyBuild.oldDeleteFile(this,\'' + data.instanceId + '\',\''+ child.EngLevelUp.instanceId +'\')"><img src="' + TzUniversityContextPath + '/statics/images/appeditor/del.png" title="' + MsgSet["DEL"] + '"/>&nbsp;</div>';
		        				htmlContent+= '	<div class="clear"></div>';
		        				htmlContent+= '</div>';
		        			}
		        		}
		        	}else{
		        		//alert(childrenAttr.length);
		        		for(var index=0; index<childrenAttr.length; index++){
		        			if (childrenAttr[index].viewFileName != "" && childrenAttr[index].sysFileName != ""){
		        				htmlContent+= '<div class="input-list-uploadcon-list">';
		        				htmlContent+= '	<div class="input-list-uploadcon-listl left"><a class="input-list-uploadcon-list-a" onclick=SurveyBuild.engViewImageSet(this,"' + data.instanceId + '","'+ child.EngLevelUp.instanceId +'") file-index="' + childrenAttr[index].orderby + '">' + childrenAttr[index].viewFileName + '</a></div>';
		        				htmlContent+= '<div class="input-list-uploadcon-listr left" style="display: ' + (SurveyBuild._readonly?'none':'block') + ';line-height:46px;" onclick="SurveyBuild.oldDeleteFile(this,\'' + data.instanceId + '\',\''+ child.EngLevelUp.instanceId+'\')"><img src="' + TzUniversityContextPath + '/statics/images/appeditor/del.png" title="' + MsgSet["DEL"] + '"/>&nbsp;</div>';
		        				htmlContent+= '</div>';
		        			}
		        		}
		        	}
			        htmlContent+='</div>'
		        	htmlContent+= '		</div>';
		        	htmlContent+= '	</div>';
		        	//将上传图片显示放到上传部分的div类
		        	htmlContent+='</div>'
			//---------------------------
			//加入clear之后结构被破坏，所以在clear下加入一层IDV
		        		 htmlContent+='<div class="clear"></div>'    		
			htmlContent+='</div>'
			//--------------	
			htmlContent+='</div>';
				//---------------------------		
			} else {//填写模式
				//-----main_inner_content_info_autoheight
				htmlContent += '<div  class="main_inner_content_info_autoheight">';
				//----------------------------考试种类OPT
				var OPT_ENG='';
				OPT_ENG+='<option value=""'+(EXAM_TYPE_DEF==""?'selected="selected"': '')+'>请选择</option>'
				for(var i in EXAM_TYPE_MAP){
					OPT_ENG+='<option value="'+EXAM_TYPE_MAP[i]+'"'+(EXAM_TYPE_DEF==EXAM_TYPE_MAP[i]?'selected="selected"': '')+'>'+EXAM_TYPE_MAP[i]+'</option>'
				}
				//----------------------------放入考试种类OPT
				htmlContent += '<div  class="input-list" style="display:block">';
				htmlContent += '	<div  class="input-list-info left"><span class="red-star">' + (data.isRequire == "Y" ? "*": "") + '</span>' + child.EngLevelType.itemName + '：</div>';
				htmlContent += '	<div class="input-list-text left input-edu-select">';
				htmlContent += '		<select id="' + data["itemId"] + child.EngLevelType.itemId + '" class="chosen-select" style="width:100%;" data-regular="" title="' + child.EngLevelType.itemName + '" value="' + child.EngLevelType["value"] + '" name="' + data["itemId"] + child.EngLevelType.itemId + '">';
				//htmlContent += '			<option value="">' +MsgSet["PLEASE_SELECT"] + '</option>';
				htmlContent += OPT_ENG;
				htmlContent += '		</select>';
				//----------------------------非空验证DIV
				htmlContent += '		<div style="margin-top:-40px;margin-left:330px"><div id="' + data["itemId"] + child.EngLevelType.itemId + 'Tip" class="onCorrect" style="margin: 0px; padding: 0px; background: transparent;">';
				htmlContent += '			<div class="onCorrect">&nbsp;</div></div>';
				htmlContent += '		</div>';
				htmlContent += '	</div>';
				htmlContent += '<div class="changedDiv"></div>';
				htmlContent += '</div>';
				htmlContent+='<div class="clear"></div>'
				//---------------------------根据考试种类，要显示的不同种类DIV
				//<!--通用成绩显示部分-->:
				htmlContent+=this.getRelatedDiv(data,child,EXAM_TYPE_DEF,EXAM_TYPE_MAP);
				
				//<!--通用上传控件部分-->
				//----------------------------图片上传处理:1.上传图片设置信息显示
			    var upData=child.EngLevelUp;
				var msg="";//上传图片的提示信息var msg="请上传.jpg .jpeg .png的文件 大小在1M以内"
				if(upData.fileType != "" || upData.fileSize != ""){
					var typeArr = upData.fileType.split(",");
					var fileType = "";
					for(var k = 0; k < typeArr.length; k++){
						if (SurveyBuild.BMB_LANG == "ENG"){
							fileType = fileType + typeArr[k] + ",";
						} else {
							fileType = fileType + "." + typeArr[k] + "、";
						}
					}
					if (fileType != ""){
						fileType = fileType.substring(0,fileType.length-1);
					}

					typeMsg = fileType != "" ? MsgSet["FILETYPE"].replace("【TZ_FILE_TYPE】",fileType) : "";
					if (SurveyBuild.BMB_LANG == "ENG"){
						msg = upData.fileSize != "" ? (typeMsg != "" ? typeMsg : "") + " " + MsgSet["FILESIZE"].replace("【TZ_FILE_SIZE】",upData.fileSize) : typeMsg;
					}else{
						msg = upData.fileSize != "" ? (typeMsg != "" ? typeMsg : "") + "，" + MsgSet["FILESIZE"].replace("【TZ_FILE_SIZE】",upData.fileSize) : typeMsg;
					}
				}
			     //------------------图片上传处理:2.上传图片div显示区域  
				 if(EXAM_TYPE_DEF==EXAM_TYPE_MAP.ENG_LEV_T0||EXAM_TYPE_DEF==""){
					 htmlContent+='<div name="'+data.itemId+'UP" id="'+data.itemId+'UP"  class="input-list" style="display:none">'
				 }else{
					 htmlContent+='<div name="'+data.itemId+'UP" id="'+data.itemId+'UP"  class="input-list" style="display:block">'
				 }
					htmlContent+='<div class="input-list-info left"><span class="red-star">' + (data.isRequire == "Y" ? "*": "") + '</span><span >'+MsgSet["SCORE_UP"]+'</span></div>'
						htmlContent+='<div class="input-list-texttemplate left" >'
							//'<input type="file" id="'+data["itemId"] + child.EngLevelUp.itemId+'File"  name="' + data["itemId"] + child.EngLevelUp.itemId + 'File" onchange=SurveyBuild.eduImgUpload(this,"EngLevelUp") accept="image/*"/>'
								htmlContent+= '<div class="filebtn left">';
								htmlContent+= '	<div class="filebtn-org"><img src="' + TzUniversityContextPath + '/statics/images/appeditor/new/upload.png" />&nbsp;&nbsp;' + MsgSet["UPLOAD_BTN_MSG"] + '</div>';
								htmlContent+= '	<input data-instancid="' + data.instanceId + '" id="'+child.EngLevelUp.itemId+ '" name="'+ data.itemId + '" title="' + data.itemName + '" onchange="SurveyBuild.engUploadAttachment(this,\''+ data.instanceId +'\',\''+ child.EngLevelUp.instanceId +'\')" type="file" class="filebtn-orgtext"/>';
								htmlContent+= '</div>';
								//GRE和GMAT特有:"请提供正式成绩单"ENG_UP_TIPS
								//if(EXAM_TYPE_DEF==EXAM_TYPE_MAP.ENG_LEV_T1||EXAM_TYPE_DEF==EXAM_TYPE_MAP.ENG_LEV_T2){
								//	htmlContent+='<div id="'+child.EngLevelUp.itemId+'_greGmatTips" style="line-height:46px;margin-left:8px;display:inline-block;color:red;font-size:16px">'+MsgSet["ENG_UP_TIPS"]+'</div>';
								//}else if(EXAM_TYPE_DEF==EXAM_TYPE_MAP.ENG_LEV_T0||EXAM_TYPE_DEF==""){
								//	htmlContent+='<div id="'+child.EngLevelUp.itemId+'_greGmatTips" style="line-height:46px;margin-left:8px;display:inline-block none;color:red;font-size:16px">'+MsgSet["ENG_UP_TIPS"]+'</div>';
								//}
								htmlContent+='<div class="clear"></div>'
									if(data.isRequire == "Y"){
										htmlContent+= '<div>' + msg + '<div id="' + child.EngLevelUp.itemId + 'Tip" class="onShow" style="line-height:32px;height:18px;"><div class="onShow"></div></div></div>';
									}
						htmlContent+='</div>'

						    //--------------------------图片上传处理:3.上传图片"预览"和"删除"   		
							var childrenAttr=child.EngLevelUp.children;
						    htmlContent+=  '<div class="input-list-info-blank left" style="display:block"><span class="red"></span></div>'
							htmlContent+= '	<div class="input-list-upload left">';
						        htmlContent+= '		<div class="input-list-upload-con" id="' + child.EngLevelUp.itemId+ '_AttList" style="display:' + (childrenAttr.length < 1 ? 'none':'black') + '">';
						        if(child.EngLevelUp.allowMultiAtta == "Y"){
						        	//alert(childrenAttr.length);
					        		for(var index=0; index<childrenAttr.length; index++){
					        			if (childrenAttr[index].viewFileName != "" && childrenAttr[index].sysFileName != ""){
					        				htmlContent+= '<div class="input-list-uploadcon-list">';
					        				htmlContent+= '	<div class="input-list-uploadcon-listl left"><a class="input-list-uploadcon-list-a" onclick=SurveyBuild.engViewImageSet(this,"' + data.instanceId + '","'+ child.EngLevelUp.instanceId +'") file-index="' + childrenAttr[index].orderby + '">' + childrenAttr[index].viewFileName + '</a></div>';
					        				htmlContent+= '<div class="input-list-uploadcon-listr left" style="display: ' + (SurveyBuild._readonly?'none':'block') + ';line-height:46px;" onclick="SurveyBuild.oldDeleteFile(this,\'' + data.instanceId + '\',\''+ child.EngLevelUp.instanceId +'\',\''+ j +'\')"><img src="' + TzUniversityContextPath + '/statics/images/appeditor/del.png" title="' + MsgSet["DEL"] + '"/>&nbsp;</div>';
					        				htmlContent+= '	<div class="clear"></div>';
					        				htmlContent+= '</div>';
					        			}
					        		}
					        	}else{
					        		//alert(childrenAttr.length);
					        		for(var index=0; index<childrenAttr.length; index++){
					        			if (childrenAttr[index].viewFileName != "" && childrenAttr[index].sysFileName != ""){
					        				htmlContent+= '<div class="input-list-uploadcon-list">';
					        				htmlContent+= '	<div class="input-list-uploadcon-listl left"><a class="input-list-uploadcon-list-a" onclick=SurveyBuild.engViewImageSet(this,"' + data.instanceId + '") file-index="' + childrenAttr[index].orderby + '">' + childrenAttr[index].viewFileName + '</a></div>';
					        				htmlContent+= '<div class="input-list-uploadcon-listr left" style="display: ' + (SurveyBuild._readonly?'none':'block') + ';line-height:46px;" onclick="SurveyBuild.oldDeleteFile(this,\'' + data.instanceId + '\',\''+ child.EngLevelUp.instanceId+'\',\''+ j +'\')"><img src="' + TzUniversityContextPath + '/statics/images/appeditor/del.png" title="' + MsgSet["DEL"] + '"/>&nbsp;</div>';
					        				htmlContent+= '	<div class="clear"></div>';
					        				htmlContent+= '</div>';
					        			}
					        		}
					        	}
					        	htmlContent+= '		</div>';
					        	htmlContent+= '	</div>';
					        	//----------------------------------	
			        	//将上传图片显示放到上传部分的div类
			        	htmlContent+='</div>'
				//加入clear之后结构被破坏，所以在clear下加入一层IDV
			        htmlContent+='<div class="clear"></div>'    		
				//htmlContent+='</div>'
				//---------------	
				htmlContent+='</div></div>';
			}
			//j++;
			htmlContent+="</div>";	
		//}
		return htmlContent;
	},

	_getHtmlTwo: function(data,index) {

		//不存在时显示信息
		if (SurveyBuild.appInsId=="0"&&data.children.length<data.defaultLines){
			SurveyBuild.showTjx(this,data.instanceId);

		}else if (data.children.length<data.defaultLines){
			SurveyBuild.showTjx(this,data.instanceId);
		}
		/*
		填写的外语水平个数
		*/
		var len = data.children.length;
		var edus = "",j = 0,childList = [];
		if(len==undefined){
			len=1;
		}
		var x = index - 1;
		childList = data.children;
		var child=childList[x];
		var EXAM_TYPE_DEF=child.EngLevelType.value;

		var EXAM_TYPE_MAP={
			"ENG_LEV_T0":"无",
			"ENG_LEV_T1":"GRE",
			"ENG_LEV_T2":"GMAT",
			"ENG_LEV_T3":"TOEFL",
			"ENG_LEV_T4":"IELTS",
			"ENG_LEV_T5":"英语六级（710分制）",
			"ENG_LEV_T6":"英语四级（710分制）",
			"ENG_LEV_T7":"英语六级（100分制）",
			"ENG_LEV_T8":"英语四级（100分制）",
			"ENG_LEV_T9":"专业英语",
			"ENG_LEV_T10":"高级口译",
			"ENG_LEV_T11":"中级口译",
			"ENG_LEV_T12":"BEC",
			"ENG_LEV_T13":"TOEIC（990）"
		};

		var htmlContent="";

		htmlContent += '<div id="main_inner_content_para' + index + '" class="next_record">';
		
		if(index > 1 && !SurveyBuild._readonly){
			htmlContent += '<div onclick="SurveyBuild.deleteEngLev(this);" class="btn_delete">' + MsgSet["DEL"] + '<img src="' + TzUniversityContextPath + '/statics/images/appeditor/m/de.png"></div>';
		}
		
		//外语水平标题
		htmlContent += '<div class="se_tit1">' + MsgSet["ENG_LEV"]  +index+ ' :</div>';
		//htmlContent += '<div class="w_96">';
		htmlContent += '<div class="index_body" style="margin-top: 25px;">';
		htmlContent += '<div class="mainright-box pos-rela">';
		htmlContent += '<div class="item">';
		htmlContent += '<p>' + child.EngLevelType.itemName + '<span>*</span></p>';

		//只读模式
		if(SurveyBuild._readonly){

			//考试名称
			htmlContent += '<div class="text-box"><input  type="text" value="' + child.EngLevelType.value + '" class="text1" readonly="true" id="' + data["itemId"] + child.EngLevelType.itemId + '"/></div>';
			htmlContent += '</div>';

			//-------------不同英语水平对应的readonly值
			if(EXAM_TYPE_DEF == "GRE" || EXAM_TYPE_DEF == "GMAT" || EXAM_TYPE_DEF == "TOEFL" || EXAM_TYPE_DEF == "TOEIC（990）"){//GRE、GMAT、TOEFL、TOEIC（990）
				
				htmlContent += '<div class="item">';
				htmlContent += '<p>'+ MsgSet["EXAM_TDATE"] +'<span>'+(data.isRequire == "Y" ? "*": "")+'</span>';
				htmlContent += '<div class="text-box" id="'+data.itemId+'_DATE_DIV"><input  type="text" value="'  + child.EngLevelDate.value + '" class="text1" readonly="true" id="' + data.itemId +child.EngLevelDate.itemId + '"/></div>';
				htmlContent += '</div>';
				htmlContent += '<div class="item">';
				htmlContent += '<p>'+ MsgSet["EXAM_TSCORE"] +'<span>'+(data.isRequire == "Y" ? "*": "")+'</span>';
				htmlContent += '<div class="text-box" id="'+data.itemId+'_GRADE_DIV"><input type="text" class="text1" readonly="true" value="'  + child.EngLevelGrade.value + '" id="' + data.itemId+child.EngLevelGrade.itemId + '"/></div>';
				htmlContent += '</div>';
				
			}else if(EXAM_TYPE_DEF=="IELTS"){//雅思
				
				htmlContent += '<div class="item">';
				htmlContent += '<p>'+ MsgSet["EXAM_TDATE"] +'<span>'+(data.isRequire == "Y" ? "*": "")+'</span>';
				htmlContent += '<div class="text-box" id="'+data.itemId+'_DATE_DIV"><input  type="text" value="'  + child.EngLevelDate.value + '" class="text1" readonly="true" id="' + data.itemId +child.EngLevelDate.itemId + '"/></div>';
				htmlContent += '</div>';
				htmlContent += '<div class="item">';
				htmlContent += '<p>'+ MsgSet["EXAM_ISCORE"] +'<span>'+(data.isRequire == "Y" ? "*": "")+'</span>';
				htmlContent += '<div class="text-box" id="'+data.itemId+'_GRADE_DIV"><input type="text" class="text1" value="'  + child.EngLevelGrade.value + '" id="' + data.itemId+child.EngLevelGrade.itemId + '"/></div>';
				htmlContent += '</div>';
				
			}else if(EXAM_TYPE_DEF == "英语六级（710分制）" || EXAM_TYPE_DEF == "英语四级（710分制）" || EXAM_TYPE_DEF == "英语六级（100分制）" || EXAM_TYPE_DEF == "英语四级（100分制）" || EXAM_TYPE_DEF == "专业英语"){//专业四六级、四六级
				
				htmlContent += '<div class="item">';
				htmlContent += '<p>'+ "考试日期" +'<span>'+(data.isRequire == "Y" ? "*": "")+'</span>';
				htmlContent += '<div class="text-box" id="'+data.itemId+'_DATE_DIV"><input type="text" value="'  + child.EngLevelDate.value + '" class="text1" readonly="readonly" id="' + data.itemId +child.EngLevelDate.itemId + '"/></div>';
				htmlContent += '</div>';
				htmlContent += '<div class="item">';
				htmlContent += '<p>'+ MsgSet["EXAM_GSCORE"] +'<span>'+(data.isRequire == "Y" ? "*": "")+'</span>';
				htmlContent += '<div class="text-box" id="'+data.itemId+'_GRADE_DIV"><input type="text" value="'  + child.EngLevelGrade.value + '" class="text1" id="' + data.itemId+child.EngLevelGrade.itemId + '"/></div>';
				htmlContent += '</div>';
				
			}else if(EXAM_TYPE_DEF == "高级口译" || EXAM_TYPE_DEF == "中级口译" || EXAM_TYPE_DEF == "BEC"){//中高级口译、BEC
				
				htmlContent += '<div class="item">';
				htmlContent += '<p>'+ MsgSet["EXAM_GSCORE"] +'<span>'+(data.isRequire == "Y" ? "*": "")+'</span>';
				htmlContent += '<div class="text-box" id="'+data.itemId+'_GRADE_DIV"><input type="text" value="'  + child.EngLevelGrade.value + '" class="text1" id="' + data.itemId+child.EngLevelGrade.itemId + '"/></div>';
				htmlContent += '</div>';
			}

			//附件

			htmlContent += '</div>';
			
		}else{

			//填写模式
			//option定义
			var OPT_ENG = '';
			OPT_ENG += '<option value=""'+(EXAM_TYPE_DEF == ""?'selected="selected"': '')+'>请选择</option>';
			for(var i in EXAM_TYPE_MAP){
				OPT_ENG+='<option value="'+EXAM_TYPE_MAP[i]+'"'+(EXAM_TYPE_DEF==EXAM_TYPE_MAP[i]?'selected="selected"': '')+'>'+EXAM_TYPE_MAP[i]+'</option>'
			}
			htmlContent += '<div class="text-box">';
			htmlContent += '<select id="' + data["itemId"] + child.EngLevelType.itemId + '" class="select1" title="' + child.EngLevelType.itemName + '" value="' + child.EngLevelType["value"] + '" name="' + data["itemId"] + child.EngLevelType.itemId + '">';
			htmlContent += OPT_ENG;
			htmlContent += '</select>';
			htmlContent += '</div>';
			htmlContent += '</div>';
			
			//---------------------不同英语水平切换div
			if(EXAM_TYPE_DEF == "GRE" || EXAM_TYPE_DEF == "GMAT" || EXAM_TYPE_DEF == "TOEFL" || EXAM_TYPE_DEF == "TOEIC（990）"){//GRE、GMAT、TOEFL、TOEIC（990）

				htmlContent += '<div class="item">';
				htmlContent += '<p>'+ MsgSet["EXAM_TDATE"] +'<span>'+(data.isRequire == "Y" ? "*": "")+'</span>';
				htmlContent += '<div id="' + data.itemId+child.EngLevelDate.itemId + 'Tip" class="tips" style="display: none;"><i></i><span></span></div>';
				htmlContent += '<div class="text-box" id="'+data.itemId+'_DATE_DIV"><input  type="text" value="'  + child.EngLevelDate.value + '" class="text1" readonly="true" id="' + data.itemId +child.EngLevelDate.itemId + '"/></div>';
				htmlContent += '</div>';
				htmlContent += '<div class="item">';
				htmlContent += '<p>'+ MsgSet["EXAM_TSCORE"] +'<span>'+(data.isRequire == "Y" ? "*": "")+'</span>';
				htmlContent += '<div id="' + data.itemId+child.EngLevelGrade.itemId + 'Tip" class="tips" style="display: none;"><i></i><span></span></div>';
				htmlContent += '<div class="text-box" id="'+data.itemId+'_GRADE_DIV"><input type="text" class="text1" value="'  + child.EngLevelGrade.value + '" id="' + data.itemId+child.EngLevelGrade.itemId + '"/></div>';
				htmlContent += '</div>';

			}else if(EXAM_TYPE_DEF=="IELTS"){//雅思

				htmlContent += '<div class="item">';
				htmlContent += '<p>'+ MsgSet["EXAM_TDATE"] +'<span>'+(data.isRequire == "Y" ? "*": "")+'</span>';
				htmlContent += '<div id="' + data.itemId+child.EngLevelDate.itemId + 'Tip" class="tips" style="display: none;"><i></i><span></span></div>';
				htmlContent += '<div class="text-box" id="'+data.itemId+'_DATE_DIV"><input type="text" value="'  + child.EngLevelDate.value + '" class="text1" readonly="readonly" id="' + data.itemId +child.EngLevelDate.itemId + '"/></div>';
				htmlContent += '</div>';
				htmlContent += '<div class="item">';
				htmlContent += '<p>'+ MsgSet["EXAM_ISCORE"] +'<span>'+(data.isRequire == "Y" ? "*": "")+'</span>';
				htmlContent += '<div id="' + data.itemId+child.EngLevelGrade.itemId + 'Tip" class="tips" style="display: none;"><i></i><span></span></div>';
				htmlContent += '<div class="text-box" id="'+data.itemId+'_GRADE_DIV"><input type="text" value="'  + child.EngLevelGrade.value + '" class="text1" id="' + data.itemId+child.EngLevelGrade.itemId + '"/></div>';
				htmlContent += '</div>';

			}else if(EXAM_TYPE_DEF == "英语六级（710分制）" || EXAM_TYPE_DEF == "英语四级（710分制）" || EXAM_TYPE_DEF == "英语六级（100分制）" || EXAM_TYPE_DEF == "英语四级（100分制）"){

				htmlContent += '<div class="item">';
				htmlContent += '<p>'+ "考试日期" +'<span>'+(data.isRequire == "Y" ? "*": "")+'</span>';
				htmlContent += '<div id="' + data.itemId+child.EngLevelDate.itemId + 'Tip" class="tips" style="display: none;"><i></i><span></span></div>';
				htmlContent += '<div class="text-box" id="'+data.itemId+'_DATE_DIV"><input type="text" value="'  + child.EngLevelDate.value + '" class="text1" readonly="readonly" id="' + data.itemId +child.EngLevelDate.itemId + '"/></div>';
				htmlContent += '</div>';
				htmlContent += '<div class="item">';
				htmlContent += '<p>'+ MsgSet["EXAM_GSCORE"] +'<span>'+(data.isRequire == "Y" ? "*": "")+'</span>';
				htmlContent += '<div id="' + data.itemId+child.EngLevelGrade.itemId + 'Tip" class="tips" style="display: none;"><i></i><span></span></div>';
				htmlContent += '<div class="text-box" id="'+data.itemId+'_GRADE_DIV"><input type="text" value="'  + child.EngLevelGrade.value + '" class="text1" id="' + data.itemId+child.EngLevelGrade.itemId + '"/></div>';
				htmlContent += '</div>';

			}else if(EXAM_TYPE_DEF=="专业英语"){//专业四六级

				//定义select内容
				var optList = "";
				if(child.EngLevelGrade.value == MsgSet["EXAM_TEM8"]){
					optList = '<option  value="'+MsgSet["EXAM_TEM4"]+'">'+MsgSet["EXAM_TEM4"]+'</option>';
					optList += ('<option selected="selected" value="'+MsgSet["EXAM_TEM8"]+'">'+MsgSet["EXAM_TEM8"]+'</option>');
				}else{
					optList = '<option selected="selected" value="'+MsgSet["EXAM_TEM4"]+'">'+MsgSet["EXAM_TEM4"]+'</option>';
					optList += ('<option  value="'+MsgSet["EXAM_TEM8"]+'">'+MsgSet["EXAM_TEM8"]+'</option>');
				}

				htmlContent += '<div class="item">';
				htmlContent += '<p>'+ "考试日期" +'<span>'+(data.isRequire == "Y" ? "*": "")+'</span>';
				htmlContent += '<div id="' + data.itemId+child.EngLevelDate.itemId + 'Tip" class="tips" style="display: none;"><i></i><span></span></div>';
				htmlContent += '<div class="text-box" id="'+data.itemId+'_DATE_DIV"><input type="text" value="'  + child.EngLevelDate.value + '" class="text1" readonly="readonly" id="' + data.itemId +child.EngLevelDate.itemId + '" /></div>';
				htmlContent += '</div>';

				htmlContent += '<div class="item">';
				htmlContent += '<p>'+ MsgSet["EXAM_GSCORE"] +'<span>'+(data.isRequire == "Y" ? "*": "")+'</span>';
				htmlContent += '<div id="' + data.itemId+child.EngLevelGrade.itemId + 'Tip" class="tips" style="display: none;"><i></i><span></span></div>';
				htmlContent += '<div class="text-box" id="'+data.itemId+'SELECT">';
				htmlContent += '<select class="select1" id="' + data.itemId+child.EngLevelGrade.itemId + '">';
				htmlContent += optList;
				htmlContent += '</div>';
				htmlContent += '</select></div>';

			}else if(EXAM_TYPE_DEF == "高级口译" || EXAM_TYPE_DEF == "中级口译"){

				//定义select内容
				var optList = "";
				if(child.EngLevelGrade.value==MsgSet["INTER_A"]){
					optList = '<option selected="selected" value="'+MsgSet["INTER_A"]+'">'+MsgSet["INTER_A"]+'</option>';
					optList += ('<option  value="'+MsgSet["INTER_B"]+'">'+MsgSet["INTER_B"]+'</option>');
				}
				else if(child.EngLevelGrade.value==MsgSet["INTER_B"]){
					optList = '<option value="'+MsgSet["INTER_A"]+'">'+MsgSet["INTER_A"]+'</option>';
					optList += ('<option selected="selected"  value="'+MsgSet["INTER_B"]+'">'+MsgSet["INTER_B"]+'</option>');
				}

				htmlContent += '<div class="item" style="display: none">';
				htmlContent += '<p>'+ MsgSet["EXAM_TDATE"] +'<span>'+(data.isRequire == "Y" ? "*": "")+'</span>';
				htmlContent += '<div id="' + data.itemId+child.EngLevelDate.itemId + 'Tip" class="tips" style="display: none;"><i></i><span></span></div>';
				htmlContent += '<div class="text-box" id="'+data.itemId+'_DATE_DIV"><input type="text" value="'  + child.EngLevelDate.value + '" class="text1" readonly="readonly" id="' + data.itemId +child.EngLevelDate.itemId + '"/></div>';
				htmlContent += '</div>';
				htmlContent += '<div class="item">';
				htmlContent += '<p>'+ MsgSet["EXAM_GSCORE"] +'<span>'+(data.isRequire == "Y" ? "*": "")+'</span>';
				htmlContent += '<div id="' + data.itemId+child.EngLevelGrade.itemId + 'Tip" class="tips" style="display: none;"><i></i><span></span></div>';
				htmlContent += '<div class="text-box" id="'+data.itemId+'_GRADE_DIV">';
				htmlContent += '<select class="select1" id="' + data.itemId+child.EngLevelGrade.itemId + '">';
				htmlContent += optList;
				htmlContent += '</div>';
				htmlContent += '</select></div>';


			} else if(EXAM_TYPE_DEF == "BEC"){

				//定义select内容
				var optList = "";
				if(child.EngLevelGrade.value==MsgSet["LEV_A"]){
					optList = '<option selected="selected" value="'+MsgSet["LEV_A"]+'">'+MsgSet["LEV_A"]+'</option>';
					optList += ('<option  value="'+MsgSet["LEV_B"]+'">'+MsgSet["LEV_B"]+'</option>');
					optList += ('<option  value="'+MsgSet["LEV_C"]+'">'+MsgSet["LEV_C"]+'</option>');
				}
				if(child.EngLevelGrade.value==MsgSet["LEV_B"]){
					optList = '<option  value="'+MsgSet["LEV_A"]+'">'+MsgSet["LEV_A"]+'</option>';
					optList += ('<option  selected="selected" value="'+MsgSet["LEV_B"]+'">'+MsgSet["LEV_B"]+'</option>');
					optList += ('<option  value="'+MsgSet["LEV_C"]+'">'+MsgSet["LEV_C"]+'</option>');
				}
				if(child.EngLevelGrade.value==MsgSet["LEV_C"]){
					optList = '<option  value="'+MsgSet["LEV_A"]+'">'+MsgSet["LEV_A"]+'</option>';
					optList += ('<option  value="'+MsgSet["LEV_B"]+'">'+MsgSet["LEV_B"]+'</option>');
					optList += ('<option selected="selected"  value="'+MsgSet["LEV_C"]+'">'+MsgSet["LEV_C"]+'</option>');
				}

				htmlContent += '<div class="item" style="display: none">';
				htmlContent += '<p>'+ MsgSet["EXAM_TDATE"] +'<span>'+(data.isRequire == "Y" ? "*": "")+'</span>';
				htmlContent += '<div id="' + data.itemId+child.EngLevelDate.itemId + 'Tip" class="tips" style="display: none;"><i></i><span></span></div>';
				htmlContent += '<div class="text-box" id="'+data.itemId+'_DATE_DIV"><input type="text" value="'  + child.EngLevelDate.value + '" class="text1" readonly="readonly" id="' + data.itemId +child.EngLevelDate.itemId + '"/></div>';
				htmlContent += '</div>';
				htmlContent += '<div class="item">';
				htmlContent += '<p>'+ MsgSet["EXAM_GSCORE"] +'<span>'+(data.isRequire == "Y" ? "*": "")+'</span>';
				htmlContent += '<div id="' + data.itemId+child.EngLevelGrade.itemId + 'Tip" class="tips" style="display: none;"><i></i><span></span></div>';
				htmlContent += '<div class="text-box" id="'+data.itemId+'_GRADE_DIV">';
				htmlContent += '<select class="select1" id="' + data.itemId+child.EngLevelGrade.itemId + '">';
				htmlContent += optList;
				htmlContent += '</div>';
				htmlContent += '</select></div>';
			}else{

				htmlContent += '<div class="item" style="display: none">';
				htmlContent += '<p>'+ MsgSet["EXAM_TDATE"] +'<span>'+(data.isRequire == "Y" ? "*": "")+'</span>';
				htmlContent += '<div id="' + data.itemId+child.EngLevelDate.itemId + 'Tip" class="tips" style="display: none;"><i></i><span></span></div>';
				htmlContent += '<div class="text-box" id="'+data.itemId+'_DATE_DIV"><input  type="text" value="'  + child.EngLevelDate.value + '" class="text1" readonly="readonly" id="' + data.itemId +child.EngLevelDate.itemId + '"/></div>';
				htmlContent += '</div>';
				htmlContent += '<div class="item" style="display: none">';
				htmlContent += '<p>'+ MsgSet["EXAM_TSCORE"] +'<span>'+(data.isRequire == "Y" ? "*": "")+'</span>';
				htmlContent += '<div id="' + data.itemId+child.EngLevelGrade.itemId + 'Tip" class="tips" style="display: none;"><i></i><span></span></div>';
				htmlContent += '<div class="text-box" id="'+data.itemId+'_GRADE_DIV"><input type="text" value="'  + child.EngLevelGrade.value + '" class="text1" id="' + data.itemId+child.EngLevelGrade.itemId + '"/></div>';
				htmlContent += '</div>';
			}

			//----------------------------图片上传处理:1.上传图片设置信息显示
			var upData=child.EngLevelUp;
			var msg="";//上传图片的提示信息var msg="请上传.jpg .jpeg .png的文件 大小在1M以内"
			if(upData.fileType != "" || upData.fileSize != ""){
				var typeArr = upData.fileType.split(",");
				var fileType = "";
				for(var k = 0; k < typeArr.length; k++){
					if (SurveyBuild.BMB_LANG == "ENG"){
						fileType = fileType + typeArr[k] + ",";
					} else {
						fileType = fileType + "." + typeArr[k] + "、";
					}
				}
				if (fileType != ""){
					fileType = fileType.substring(0,fileType.length-1);
				}

				typeMsg = fileType != "" ? MsgSet["FILETYPE"].replace("【TZ_FILE_TYPE】",fileType) : "";
				if (SurveyBuild.BMB_LANG == "ENG"){
					msg = upData.fileSize != "" ? (typeMsg != "" ? typeMsg : "") + " " + MsgSet["FILESIZE"].replace("【TZ_FILE_SIZE】",upData.fileSize) : typeMsg;
				}else{
					msg = upData.fileSize != "" ? (typeMsg != "" ? typeMsg : "") + "，" + MsgSet["FILESIZE"].replace("【TZ_FILE_SIZE】",upData.fileSize) : typeMsg;
				}
			}
			//上传div
			if(EXAM_TYPE_DEF == "无" || EXAM_TYPE_DEF == ""){

				htmlContent += '<div class="item" id="'+data.itemId+'UP" style="display: none">';
				htmlContent += '<p>'+ MsgSet["SCORE_UP"] +'<span>'+(data.isRequire == "Y" ? "*": "")+'</span></p>';
				htmlContent += '<div id="' +child.EngLevelUp.itemId + 'Tip" class="tips" style="display: none;"><i></i><span></span></div>';
				htmlContent += '<div class="text-box" style="border:none;display:' + (SurveyBuild._readonly?'none':'block') +' " >';
				htmlContent += '<div class="handle">';
				htmlContent += '<div class="ncsc-upload-btn">';
				htmlContent += '<a href="#" class="ncsc-upload-btn-a">';
				htmlContent += '<span class="ncsc-upload-btn-span">';
				htmlContent += '<input type="file" id="'+child.EngLevelUp.itemId+ '" hidefocus="true" size="1" class="input-file" name="goods_image" onchange="SurveyBuild.engUploadAttachment(this,\''+ data.instanceId +'\',\''+ child.EngLevelUp.instanceId +'\')"  ></span>';
				htmlContent += '<div class="ncsc-upload-btn-p">'+ MsgSet["UPLOAD_BTN_MSG"] +'<img src="' + TzUniversityContextPath + '/statics/images/appeditor/new/upload.png'+'"></div>';
				htmlContent += '</a>';
				htmlContent += '</div>';
				htmlContent += '</div>';
				htmlContent += '</div>';
				htmlContent += '<p style="color:#666;font-size:0.56rem;margin-top:5px;display:' + (SurveyBuild._readonly?'none':'block') +' ">'+ msg + '</p>';
				htmlContent += '</div>';

				var childrenAttr=child.EngLevelUp.children;
				if(child.EngLevelUp.allowMultiAtta == "Y"){

					for(var index=0; index<childrenAttr.length; index++){

						if (childrenAttr[index].viewFileName != "" && childrenAttr[index].sysFileName != ""){

							htmlContent += ' <div class="upload_list" id="' + child.EngLevelUp.itemId + '_AttList" style="display:' + (childrenAttr.length < 1 ? 'none':'block') + '">';
							htmlContent += '<p>'+ MsgSet["UP_FILE_LIST"] +'</p>';
							htmlContent += '<li class="fileLi">';
							htmlContent += '<span><a id="img_'+ child.EngLevelUp.itemId +'_'+index+'" onclick="SurveyBuild.engViewImageSet(this,\'' + data.instanceId + '\',\''+ child.EngLevelUp.instanceId +'\')" index="'+ index +'" file-index="' + childrenAttr[index].orderby + '">';
							htmlContent += childrenAttr[index].viewFileName +'</a></span>';
							htmlContent += '<i onclick="SurveyBuild.oldDeleteFile(this,\'' + data.instanceId + '\',\''+ child.EngLevelUp.instanceId+'\',\''+ j +'\')" style="background:url(' + TzUniversityContextPath + '/statics/images/appeditor/m/de.png'+') no-repeat center center">';
							htmlContent += '</i></li>';
							htmlContent += '</div>';
						}else{

							htmlContent += ' <div class="upload_list" id="' + child.EngLevelUp.itemId + '_AttList" style="display:none">';
							htmlContent += '<p>'+ MsgSet["UP_FILE_LIST"] +'</p>';
							htmlContent += '</div>';
						}
					}

				}else{

					htmlContent += ' <div class="upload_list" id="' + child.EngLevelUp.itemId + '_AttList" style="display:' + (childrenAttr.length < 1 ? 'none':'block') + '">';
					htmlContent += '<p>'+ MsgSet["UP_FILE_LIST"] +'</p>';
					htmlContent += '<li class="fileLi">';
					htmlContent += '<span><a id="img_'+ child.EngLevelUp.itemId +'_'+index+'" onclick="SurveyBuild.engViewImageSet(this,\'' + data.instanceId + '\',\''+ child.EngLevelUp.instanceId +'\')" index="'+ index +'" file-index="' + childrenAttr[index].orderby + '">';
					htmlContent += childrenAttr[index].viewFileName +'</a></span>';
					htmlContent += '<i onclick="SurveyBuild.oldDeleteFile(this,\'' + data.instanceId + '\',\''+ child.EngLevelUp.instanceId+'\',\''+ j +'\')" style="background:url(' + TzUniversityContextPath + '/statics/images/appeditor/m/de.png'+') no-repeat center center">';
					htmlContent += '</i></li>';
					htmlContent += '</div>';

				}

				htmlContent += '<div class="img_shade" id ="shade_'+ child.EngLevelUp.itemId +'"></div>';
				htmlContent += '<img class="img_pop_close" id ="close_'+ child.EngLevelUp.itemId +'" src="'+ TzUniversityContextPath + '/statics/images/appeditor/m/rl_btn.png'+'">';
				htmlContent += '<div class="img_pop_body" id ="body_'+ child.EngLevelUp.itemId +'">'  ;
				htmlContent += ' <img src="" id ="img_'+ child.EngLevelUp.itemId +'">';
				htmlContent += '</div>';

			}else{

				htmlContent += '<div class="item" id="'+data.itemId+'UP">';
				htmlContent += '<p>'+ MsgSet["SCORE_UP"] +'<span>'+(data.isRequire == "Y" ? "*": "")+'</span></p>';
				htmlContent += '<div id="'+child.EngLevelUp.itemId + 'Tip" class="tips" style="display: none;"><i></i><span></span></div>';
				htmlContent += '<div class="text-box" style="border:none;display:' + (SurveyBuild._readonly?'none':'block') +' " >';
				htmlContent += '<div class="handle">';
				htmlContent += '<div class="ncsc-upload-btn">';
				htmlContent += '<a href="#" class="ncsc-upload-btn-a">';
				htmlContent += '<span class="ncsc-upload-btn-span">';
				htmlContent += '<input type="file" id="'+child.EngLevelUp.itemId+ '" hidefocus="true" size="1" class="input-file" name="goods_image" onchange="SurveyBuild.engUploadAttachment(this,\''+ data.instanceId +'\',\''+ child.EngLevelUp.instanceId +'\')"  ></span>';
				htmlContent += '<div class="ncsc-upload-btn-p">'+ MsgSet["UPLOAD_BTN_MSG"] +'<img src="' + TzUniversityContextPath + '/statics/images/appeditor/new/upload.png'+'"></div>';
				htmlContent += '</a>';
				htmlContent += '</div>';
				htmlContent += '</div>';
				htmlContent += '</div>';
				htmlContent += '<p style="color:#666;font-size:0.56rem;margin-top:5px;display:' + (SurveyBuild._readonly?'none':'block') +' ">'+ msg + '</p>';
				htmlContent += '</div>';

				var childrenAttr=child.EngLevelUp.children;
				if(child.EngLevelUp.allowMultiAtta == "Y"){

					for(var index=0; index<childrenAttr.length; index++){

						if (childrenAttr[index].viewFileName != "" && childrenAttr[index].sysFileName != ""){

							htmlContent += ' <div class="upload_list" id="' + child.EngLevelUp.itemId + '_AttList" style="display:' + (childrenAttr.length < 1 ? 'none':'block') + '">';
							htmlContent += '<p>'+ MsgSet["UP_FILE_LIST"] +'</p>';
							htmlContent += '<li class="fileLi">';
							htmlContent += '<span><a id="img_'+ child.EngLevelUp.itemId +'_'+index+'" onclick="SurveyBuild.engViewImageSet(this,\'' + data.instanceId + '\',\''+ child.EngLevelUp.instanceId +'\')" index="'+ index +'" file-index="' + childrenAttr[index].orderby + '">';
							htmlContent += childrenAttr[index].viewFileName +'</a></span>';
							htmlContent += '<i onclick="SurveyBuild.oldDeleteFile(this,\'' + data.instanceId + '\',\''+ child.EngLevelUp.instanceId+'\',\''+ j +'\')" style="background:url(' + TzUniversityContextPath + '/statics/images/appeditor/m/de.png'+') no-repeat center center">';
							htmlContent += '</i></li>';
							htmlContent += '</div>';
						}else{

							htmlContent += ' <div class="upload_list" id="' + child.EngLevelUp.itemId + '_AttList" style="display:none">';
							htmlContent += '<p>'+ MsgSet["UP_FILE_LIST"] +'</p>';
							htmlContent += '</div>';
						}
					}

				}else{

					htmlContent += ' <div class="upload_list" id="' + child.EngLevelUp.itemId + '_AttList" style="display:' + (childrenAttr.length < 1 ? 'none':'block') + '">';
					htmlContent += '<p>'+ MsgSet["UP_FILE_LIST"] +'</p>';
					htmlContent += '<li class="fileLi">';
					htmlContent += '<span><a id="img_'+ child.EngLevelUp.itemId +'_'+index+'" onclick="SurveyBuild.engViewImageSet(this,\'' + data.instanceId + '\',\''+ child.EngLevelUp.instanceId +'\')" index="'+ index +'" file-index="' + childrenAttr[index].orderby + '">';
					htmlContent += childrenAttr[index].viewFileName +'</a></span>';
					htmlContent += '<i onclick="SurveyBuild.oldDeleteFile(this,\'' + data.instanceId + '\',\''+ child.EngLevelUp.instanceId+'\',\''+ j +'\')" style="background:url(' + TzUniversityContextPath + '/statics/images/appeditor/m/de.png'+') no-repeat center center">';
					htmlContent += '</i></li>';
					htmlContent += '</div>';

				}

				htmlContent += '<div class="img_shade" id ="shade_'+ child.EngLevelUp.itemId +'"></div>';
				htmlContent += '<img class="img_pop_close" id ="close_'+ child.EngLevelUp.itemId +'" src="'+ TzUniversityContextPath + '/statics/images/appeditor/m/rl_btn.png'+'">';
				htmlContent += '<div class="img_pop_body" id ="body_'+ child.EngLevelUp.itemId +'">'  ;
				htmlContent += ' <img src="" id ="img_'+ child.EngLevelUp.itemId +'">';
				htmlContent += '</div>';

			}

			htmlContent += '</div>';
			htmlContent += '</div>';
			htmlContent += '</div>';

		}

		return htmlContent;
	},

	_eventbind: function(data) {
		//选中隐藏和显示相应值
		var EXAM_TYPE_MAP={
			"ENG_LEV_T0":"无",
			"ENG_LEV_T1":"GRE",
			"ENG_LEV_T2":"GMAT",
			"ENG_LEV_T3":"TOEFL",
			"ENG_LEV_T4":"IELTS",
			"ENG_LEV_T5":"英语六级（710分制）",
			"ENG_LEV_T6":"英语四级（710分制）",
			"ENG_LEV_T7":"英语六级（100分制）",
			"ENG_LEV_T8":"英语四级（100分制）",
			"ENG_LEV_T9":"专业英语",
			"ENG_LEV_T10":"高级口译",
			"ENG_LEV_T11":"中级口译",
			"ENG_LEV_T12":"BEC",
			"ENG_LEV_T13":"TOEIC（990）"
		};
		var children = data.children;
		len = children.length;
		var type_select="";
		var child = "";
		if(len==undefined){
			len=1;
		}

		//手机版切换
		if(SurveyBuild.accessType == "M"){

            var calendar = new LCalendar();

			//存在多个英语水平时进行循环
			for(var j=0;j<len;j++){


			    //已经在日期插件中进行修改
                    calendar.init({
                        'trigger': '#' + data.itemId + children[j].EngLevelDate.itemId, //标签id
                        'type': "date", //date 调出日期选择 datetime 调出日期时间选择 time 调出时间选择 ym 调出年月选择,
                        'minDate': "1960-01-01", //最小日期
                        'maxDate':"2030-12-31"
                    });

                var child=children[j];
				//隐藏图片显示
				$("#shade_"+ child.EngLevelUp.itemId).hide();
				$("#body_"+ child.EngLevelUp.itemId).hide();
				$("#close_"+ child.EngLevelUp.itemId).hide();

				type_select=$("#"+ data["itemId"] + child.EngLevelType.itemId);
				type_select.each(function(index){

					//点击切换按钮清空之前数据
					$(this).change(function(){

						$(this).blur();
						if($(this).val()!=""){

							for(var i in EXAM_TYPE_MAP){

								//切换英语水平
								if($(this).val()==EXAM_TYPE_MAP[i]){

									var indexp = $(this).closest(".next_record").index()
									child = data.children[indexp];
									child.EngLevelDate.value="";
									child.EngLevelGrade.value="";


									//大div
									var topDiv=$(this).closest(".w_96");

									//日期、成绩、上传
									var timeDiv = topDiv.find("#"+data["itemId"]+"_DATE_DIV");
									var gradeDiv = topDiv.find("#"+data["itemId"]+"_GRADE_DIV");
									var upDiv = topDiv.find("#"+data["itemId"]+"UP");

									//清空日期和成绩
									timeDiv.find(".text1").val("");
									gradeDiv.find(".text1").val("");

									if(i == "ENG_LEV_T0"){

										upDiv.hide();
									}else{
										upDiv.show();
									}

									//改变date,成绩标签
									if(i=="ENG_LEV_T1"||i=="ENG_LEV_T2"||i=="ENG_LEV_T3"||i=="ENG_LEV_T4"||i=="ENG_LEV_T13"||i=="ENG_LEV_T5"||i=="ENG_LEV_T6"||i=="ENG_LEV_T7"||i=="ENG_LEV_T8"){

										//将成绩改为文本框
										var textDiv = '<input type="text" value="'  + child.EngLevelGrade.value + '"  class="text1" id="' + data.itemId +child.EngLevelGrade.itemId + '">';
										timeDiv.parent(".item").show();
										gradeDiv.parent(".item").show();
										gradeDiv.html(textDiv);

										if(i=="ENG_LEV_T5"||i=="ENG_LEV_T6"||i=="ENG_LEV_T7"||i=="ENG_LEV_T8"){

											timeDiv.siblings("p").html("考试日期<span>*</span>");
											gradeDiv.siblings("p").html("总分(成绩)<span>*</span>");

										}else{
											timeDiv.siblings("p").html("Test date<span>*</span>");

											if(i=="ENG_LEV_T4"){//雅思
												gradeDiv.siblings("p").html("Overall Band Score<span>*</span>");
											}else{

												//GRE、GMAT、TOEFL、TOEIC（990）
												gradeDiv.siblings("p").html("Total(Score)<span>*</span>");
											}
										}

									}else if(i=="ENG_LEV_T9"){//专业四级

										var optList1 = '<select class="select1" id="' + data.itemId +child.EngLevelGrade.itemId + '">';
										optList1 += '<option  value="'+MsgSet["EXAM_TEM4"]+'">'+MsgSet["EXAM_TEM4"]+'</option>';
										optList1 += ('<option value="'+MsgSet["EXAM_TEM8"]+'">'+MsgSet["EXAM_TEM8"]+'</option>');
										optList1 += '</select>';

										gradeDiv.prev("p").html("总分(成绩)<span>*</span>");
										timeDiv.parent(".item").show();
										gradeDiv.parent(".item").show();
										gradeDiv.html(optList1);

									}else if(i=="ENG_LEV_T10" || i=="ENG_LEV_T11"){//中高级

										var optList = '<select class="select1" id="' + data.itemId +child.EngLevelGrade.itemId + '">';
										optList += '<option value="'+MsgSet["INTER_A"]+'">'+MsgSet["INTER_A"]+'</option>';
										optList += ('<option  value="'+MsgSet["INTER_B"]+'">'+MsgSet["INTER_B"]+'</option>');
										optList += '</select>';

										gradeDiv.siblings("p").html("总分(成绩)<span>*</span>");
										timeDiv.parent(".item").hide();
										gradeDiv.parent(".item").show();
										gradeDiv.html(optList);

									}else if(i=="ENG_LEV_T12"){//BEC

										var optList2 = '<select class="select1" id="' + data.itemId +child.EngLevelGrade.itemId + '">';
										optList2 += '<option value="' + MsgSet["LEV_A"] + '">'+MsgSet["LEV_A"] + '</option>';
										optList2 += ('<option  value="' + MsgSet["LEV_B"] + '">'+MsgSet["LEV_B"] + '</option>');
										optList2 += ('<option  value="' + MsgSet["LEV_C"] + '">'+MsgSet["LEV_C"] + '</option>');
										optList2 += '</select>';

										gradeDiv.siblings("p").html("总分(成绩)<span>*</span>");
										timeDiv.parent(".item").hide();
										gradeDiv.parent(".item").show();
										gradeDiv.html(optList2);

									}else{

										//隐藏date
										timeDiv.parent(".item").hide();
										gradeDiv.parent(".item").hide();
									}

								}
							}
						}else{

							//大div
							var topDiv=$(this).closest(".w_96");

							//日期、成绩、上传
							var timeDiv = topDiv.find("#"+data["itemId"]+"_DATE_DIV");
							var gradeDiv = topDiv.find("#"+data["itemId"]+"_GRADE_DIV");
							var upDiv = topDiv.find("#"+data["itemId"]+"UP");

							//清空日期和成绩
							timeDiv.find(".text1").val("");
							gradeDiv.find(".text1").val("");

							//隐藏日期、成绩、上传
							timeDiv.parent(".item").hide();
							gradeDiv.parent(".item").hide();
							upDiv.hide();
						}

					})
				})

				var $inputBox3 = $("#"+child["EngLevelUp"].itemId);
				$inputBox3.each(function(k){
					$(this).mouseover(function(e){
						$(this).prev().css("opacity","0.8");
					});
					$(this).attr("len",j);
					$(this).mouseout(function(e) {
						$(this).prev().css("opacity","1");
					});

					$(this).formValidator({tipID:($(this).attr("id")+'Tip'), onShow:"", onFocus:"&nbsp;", onCorrect:"&nbsp;"});
					var me=$(this);
					$(this).functionValidator({
						fun:function(val,el){
							//必须上传

							if (data.isRequire == "Y"){
								//如果该div中上传附件数量>1,则不显示提示,如果=1可能是"只有一个节点数据为空"要提示
								var child=children[me.attr("len")];
								if (child["EngLevelUp"].children.length > 1){
									return 	true;
								} else if (child["EngLevelUp"].children.length == 1 && child["EngLevelUp"].children[0].fileName != ""){
									return true;
								} else {
									return MsgSet["FILE_UPL_REQUIRE"];
									//return "length:"+child["EngLevelUp"].children.length+"name:"+child["EngLevelUp"].children[0].fileName;
								}
							}
						}
					});
				});
                //日期.成绩非空验证:
                if(data.isRequire == "Y"){
                    var $inputBox1 = $("#" + data.itemId +child.EngLevelDate.itemId);
                    var $inputBox2 = $("#" + data.itemId +child.EngLevelGrade.itemId);

                    data.checkInputNotNull($inputBox1);
                    data.checkInputNotNull($inputBox2);
                }
			}

		}else{

			//为所有的select注册事件
			for(var ii=0;ii<len;ii++){
				child=children[ii];
				type_select=$("#"+ data["itemId"] + child.EngLevelType.itemId);
				type_select.chosen({width: "100%"});
				type_select.each(function(index){
					$(this).on("change",function(){
						//-------------------------清空数据:
						$(this).blur();
						//-------------------------
						if($(this).val()!=""){

							for(var i in EXAM_TYPE_MAP){
								if($(this).val()==EXAM_TYPE_MAP[i]){
									//切换select时，已经渲染完毕:
									//如何获取当前项在多行容器中的第几行？
									var indexx = $(this).closest(".main_inner_content_para").index()
									child=data.children[indexx];
									child.EngLevelDate.value="";
									child.EngLevelGrade.value="";
									var relatedDiv=data.getRelatedDiv(data,child,EXAM_TYPE_MAP[i],EXAM_TYPE_MAP);

									var topDiv=$(this).closest(".main_inner_content_info_autoheight");
									//时间DIV
									var timeDiv=topDiv.find("#"+data["itemId"]+"_DATE_DIV");
									//成绩DIV
									var gradeDiv=topDiv.find("#"+data["itemId"]+"_GRADE_DIV");
									//上传DIV
									//上传部位:
									var upDiv=topDiv.find("#"+data["itemId"]+"UP");
									if(i=="ENG_LEV_T0"){
										upDiv.hide();
									}else{
										upDiv.show();
									}

									//gradeDiv.after(relatedDiv);
									//changeSpanLabel(timeDiv.find())
									gradeDiv.remove();
									//timeDiv.remove();
									//SELECT美化：
									var dateSpan=timeDiv.find(".input-list-info");
									//清空dateInput中的value：
									var dateInput=timeDiv.find("#"+data.itemId +child.EngLevelDate.itemId);
									//console.log("dateInput:");
									//console.dir(dateInput);
									dateInput.val("");
									var dateClear=timeDiv.find("#"+data.itemId +child.EngLevelDate.itemId+"_Clear");
									dateClear.css("visibility","hidden");

									if(i=="ENG_LEV_T1"||i=="ENG_LEV_T2"||i=="ENG_LEV_T3"||i=="ENG_LEV_T4"||i=="ENG_LEV_T13"||i=="ENG_LEV_T9"||i=="ENG_LEV_T5"||i=="ENG_LEV_T6"||i=="ENG_LEV_T7"||i=="ENG_LEV_T8"){
										//改变date标签:
										//1.date标签change
										var label=timeDiv.find(".input-list-info").html();
										if(i=="ENG_LEV_T9"||i=="ENG_LEV_T5"||i=="ENG_LEV_T6"||i=="ENG_LEV_T7"||i=="ENG_LEV_T8"){
											label=label.replace("Test date","考试日期");
										}else{
											label=label.replace("考试日期","Test date");
										}
										timeDiv.find(".input-list-info").html(label);
										timeDiv.show();
									}else{
										//隐藏date
										timeDiv.hide();
									}
									var newGradeDiv=data.getChangedGradeDiv(data,child,EXAM_TYPE_MAP[i],EXAM_TYPE_MAP);
									//替换成绩显示框:
									gradeDiv.remove();
									timeDiv.after(newGradeDiv);
									if(i=="ENG_LEV_T7"||i=="ENG_LEV_T8"||i=="ENG_LEV_T9"||i=="ENG_LEV_T10"||i=="ENG_LEV_T11"||i=="ENG_LEV_T12"){
										gradeDiv=topDiv.find("#"+data["itemId"]+"_GRADE_DIV");
										gradeDiv.find("select").chosen({width: "100%"});
										//文字说明 解析:
									}
									//非空验证:
									if(data.isRequire == "Y"){
										data.checkInputNotNull($inputBox);
										var $inputBox2 = $("#" + data.itemId +child.EngLevelGrade.itemId);
										data.checkInputNotNull($inputBox2);
									}
									return false;
								}
								//-------------------------
							}
						}else{
							var indexx = $(this).closest(".main_inner_content_para").index()
							child=data.children[indexx];
							child.EngLevelDate.value="";
							child.EngLevelGrade.value="";
							var relatedDiv=data.getRelatedDiv(data,child,"",EXAM_TYPE_MAP);

							var topDiv=$(this).closest(".main_inner_content_info_autoheight");
							//时间DIV
							var timeDiv=topDiv.find("#"+data["itemId"]+"_DATE_DIV");
							//成绩DIV
							var gradeDiv=topDiv.find("#"+data["itemId"]+"_GRADE_DIV");
							//上传DIV
							//上传部位:
							var upDiv=topDiv.find("#"+data["itemId"]+"UP");
							upDiv.hide();
							//gradeDiv.after(relatedDiv);
							//changeSpanLabel(timeDiv.find())
							gradeDiv.remove();
							//timeDiv.remove();
							//SELECT美化：
							var dateSpan=timeDiv.find(".input-list-info");
							//清空dateInput中的value：
							var dateInput=timeDiv.find("#"+data.itemId +child.EngLevelDate.itemId);


							var dateClear=timeDiv.find("#"+data.itemId +child.EngLevelDate.itemId+"_Clear");
							dateClear.css("visibility","hidden");
							//console.log("dateInput:");
							//console.dir(dateInput);
							dateInput.val("");

							timeDiv.hide();
							/*
							 var newGradeDiv=data.getChangedGradeDiv(data,child,"",EXAM_TYPE_MAP);
							 //替换成绩显示框:
							 timeDiv.after(newGradeDiv);
							 */
							return false;
						}
					})
				});
				//为所有的timePicker注册事件:

				//日期控件处理1.2.3.4.13
				var $inputBox = $("#" + data.itemId +child.EngLevelDate.itemId);
				var $selectBtn = $("#" + data.itemId +child.EngLevelDate.itemId + "_Btn");

				var $clearBtn = $("#" + data.itemId +child.EngLevelDate.itemId + "_Clear");
				data.bindTimePicker($inputBox,$selectBtn,$clearBtn);
				//绑定TimePicker bindTime
				//data.bindTime(data.itemId +child.EngLevelDate.itemId);
				//------上传控件验证:
				var $fileInput=$("#"+child["EngLevelUp"].itemId);

				$fileInput.each(function(k){
					$(this).mouseover(function(e){
						$(this).prev().css("opacity","0.8");
					});
					$(this).attr("len",ii);
					$(this).mouseout(function(e) {
						$(this).prev().css("opacity","1");
					});
					$(this).formValidator({tipID:($(this).attr("id")+'Tip'), onShow:"", onFocus:"&nbsp;", onCorrect:"&nbsp;"});
					var me=$(this);
					$(this).functionValidator({
						fun:function(val,el){
							//必须上传
							if (data.isRequire == "Y"){
								//如果该div中上传附件数量>1,则不显示提示,如果=1可能是"只有一个节点数据为空"要提示
								var child=children[me.attr("len")];
								if (child["EngLevelUp"].children.length > 1){
									return 	true;
								} else if (child["EngLevelUp"].children.length == 1 && child["EngLevelUp"].children[0].fileName != ""){
									return true;
								} else {
									return MsgSet["FILE_UPL_REQUIRE"];
									//return "length:"+child["EngLevelUp"].children.length+"name:"+child["EngLevelUp"].children[0].fileName;
								}
							}
						}
					});
				});
				//日期.成绩非空验证:
				if(data.isRequire == "Y"){
					data.checkInputNotNull($inputBox);
					var $inputBox2 = $("#" + data.itemId +child.EngLevelGrade.itemId);
					data.checkInputNotNull($inputBox2);
				}

			}
			//所有看的到的select美化:
			$("select").each(function(){
				$(this).chosen({width:"100%"});
			});
		}

		       
	}
})