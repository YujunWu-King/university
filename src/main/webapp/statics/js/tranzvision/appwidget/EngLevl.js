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
			"fileType": "jpg,png,jpeg",//允许上传类型
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
	getNumGradeDiv:function(top_id,grade_id,grade_val,grade_label,dateHtml){
		var gradeDiv="";
		gradeDiv+=dateHtml
		gradeDiv+='<div name="'+top_id+'_GRADE_DIV" id="'+top_id+'_GRADE_DIV" class="input-list">';
		gradeDiv+='<div class="input-list-info left"><span class="red-star">*</span><span id="'+grade_id+'Label">'+grade_label+'：</span></div> <div class="input-list-text left"><input class="inpu-list-text-enter" style="height:36px;" id="'+grade_id+'" value="'+grade_val+'"/>'
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
	getChoseGradeDiv:function(top_id,grade_id,grade_val,grade_label,optList){
		var gradeDiv="";
		gradeDiv+='<div name="'+top_id+'_GRADE_DIV" id="'+top_id+'_GRADE_DIV" class="input-list">'
			gradeDiv+='<div class="input-list-info left"><span class="red-star">*</span><span >'+grade_label+'：</span></div>'
			gradeDiv+='<div class="input-list-text left" ><select  style="width:255px;height:36px" id="'+grade_id+'" value="'+grade_val+'">'
			gradeDiv+=optList
			gradeDiv+='</select></div>'
		gradeDiv+='</div>'
		return gradeDiv;
	},
	//日期:input
	getDateDiv:function(top_id,date_id,date_val,date_name,date_title){
		var DATE_DIV='';
		DATE_DIV += '  <div id="'+top_id+'_DATE_DIV" class="input-list" style="display:block"><span class="input-list-info left"><span class="red-star">*</span>'+MsgSet["EXAM_TDATE"]+'：</span>';
		
		DATE_DIV += '     <div class="input-list-text left"> <input id="' + date_id+ '" name="' + date_name+ '" type="text" value="'  +date_val + '"class="inpu-list-text-enter" style="height:36px" readonly="readonly" onclick="this.focus()" title="' +date_name + '">';
		DATE_DIV += '      <img id="' + date_id+ '_Btn" src="' + TzUniversityContextPath + '/statics/images/appeditor/new/calendar.png" style="position:relative;top:5px;left:-31px;cursor:pointer;">';
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
	getDateRead:function(date_id,date_val,date_name,date_title){
		var DATE_DIV='';
		DATE_DIV += '  <div class="input-list" style="display:block"><div class="input-list-info left"><span class="red-star">*</span><span>'+MsgSet["EXAM_TDATE"]+'：</span></div>';
		
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

		if(EXAM_TYPE_DEF==EXAM_TYPE_MAP.ENG_LEV_T1||EXAM_TYPE_DEF==EXAM_TYPE_MAP.ENG_LEV_T2||EXAM_TYPE_DEF==""){
			dateHtml=this.getDateRead(data.itemId +child.EngLevelDate.itemId,child.EngLevelDate.value,data.itemId+child.EngLevelDate.name,child.EngLevelDate.itemName);
			label=MsgSet["EXAM_TSCORE"];
		}else if(EXAM_TYPE_DEF==EXAM_TYPE_MAP.ENG_LEV_T3){
			dateHtml=this.getDateRead(data.itemId +child.EngLevelDate.itemId,child.EngLevelDate.value,data.itemId+child.EngLevelDate.name,child.EngLevelDate.itemName);
			label=MsgSet["EXAM_TOTAL"];

		}else if(EXAM_TYPE_DEF==EXAM_TYPE_MAP.ENG_LEV_T4){
			dateHtml=this.getDateRead(data.itemId +child.EngLevelDate.itemId,child.EngLevelDate.value,data.itemId+child.EngLevelDate.name,child.EngLevelDate.itemName);
			label=MsgSet["EXAM_ISCORE"];

		}else if(EXAM_TYPE_DEF==EXAM_TYPE_MAP.ENG_LEV_T5||EXAM_TYPE_DEF==EXAM_TYPE_MAP.ENG_LEV_T6||EXAM_TYPE_DEF==EXAM_TYPE_MAP.ENG_LEV_T7||EXAM_TYPE_DEF==EXAM_TYPE_MAP.ENG_LEV_T8){
			label=MsgSet["EXAM_SCORE"];
			
		}
		//else if(EXAM_TYPE_DEF==EXAM_TYPE_MAP.ENG_LEV_T7||EXAM_TYPE_DEF==EXAM_TYPE_MAP.ENG_LEV_T8){
		//	label=MsgSet["EXAM_PASS"];
			
		//}
		else if(EXAM_TYPE_DEF==EXAM_TYPE_MAP.ENG_LEV_T9){
			label=MsgSet["EXAM_GSCORE"];
			
		}else if(EXAM_TYPE_DEF==EXAM_TYPE_MAP.ENG_LEV_T10||EXAM_TYPE_DEF==EXAM_TYPE_MAP.ENG_LEV_T11){
			label=MsgSet["EXAM_GSCORE"];
			
		}else if(EXAM_TYPE_DEF==EXAM_TYPE_MAP.ENG_LEV_T12){
			label=MsgSet["EXAM_GSCORE"];
			
		}else if(EXAM_TYPE_DEF==EXAM_TYPE_MAP.ENG_LEV_T13){
			dateHtml=this.getDateRead(data.itemId +child.EngLevelDate.itemId,child.EngLevelDate.value,data.itemId+child.EngLevelDate.name,child.EngLevelDate.itemName);
			label=MsgSet["EXAM_SCORE"];
		}
		//------------------
		var ReadDiv="";
		ReadDiv+='<div name="'+data.itemId+'_READ_DIV" id="'+data.itemId+'_READ_DIV" class="input-list">'
			//只读考试名称:
			ReadDiv+='<div class="input-list-info left"><span class="red-star">*</span><span>'+child.EngLevelType.itemName+'：</span></div> '
			ReadDiv+='<div class="input-list-text left">'
			ReadDiv+=EXAM_TYPE_DEF;
			ReadDiv+='</div>'	
			ReadDiv+='<div class="clear"></div>'
			//日期:	
			ReadDiv+=dateHtml
			//值:
			ReadDiv+='<div class="clear"></div>'
			ReadDiv+='<div class="input-list-info left"><span class="red-star">*</span><span>'+label+'：</span></div> '
			ReadDiv+='<div class="input-list-text left">'
			ReadDiv+=val;
			ReadDiv+='</div>'
		ReadDiv+='</div>';
		//------------------	
		return ReadDiv;
	},
	//考试类型“关联div”切换:
	getRelatedDiv:function(data,child,EXAM_TYPE_DEF,EXAM_TYPE_MAP){
		//alert("data:"+EXAM_TYPE_DEF+"  val:"+EXAM_TYPE_MAP.ENG_LEV_T6)
		var RELATED_DIV="";
		var DATE_HTML=this.getDateDiv(data.itemId,data.itemId +child.EngLevelDate.itemId,child.EngLevelDate.value,data.itemId+child.EngLevelDate.name,child.EngLevelDate.itemName);
		if(EXAM_TYPE_DEF==EXAM_TYPE_MAP.ENG_LEV_T1||EXAM_TYPE_DEF==EXAM_TYPE_MAP.ENG_LEV_T2||EXAM_TYPE_DEF==""){
			RELATED_DIV=this.getNumGradeDiv(data.itemId,data.itemId+child.EngLevelGrade.itemId,child.EngLevelGrade.value,MsgSet["EXAM_TSCORE"],DATE_HTML);
		}else if(EXAM_TYPE_DEF==EXAM_TYPE_MAP.ENG_LEV_T3){
			RELATED_DIV=this.getNumGradeDiv(data.itemId,data.itemId+child.EngLevelGrade.itemId,child.EngLevelGrade.value,MsgSet["EXAM_TOTAL"],DATE_HTML);
		}else if(EXAM_TYPE_DEF==EXAM_TYPE_MAP.ENG_LEV_T4){
			RELATED_DIV=this.getNumGradeDiv(data.itemId,data.itemId+child.EngLevelGrade.itemId,child.EngLevelGrade.value,MsgSet["EXAM_ISCORE"],DATE_HTML);
		}else if(EXAM_TYPE_DEF==EXAM_TYPE_MAP.ENG_LEV_T5||EXAM_TYPE_DEF==EXAM_TYPE_MAP.ENG_LEV_T6||EXAM_TYPE_DEF==EXAM_TYPE_MAP.ENG_LEV_T7||EXAM_TYPE_DEF==EXAM_TYPE_MAP.ENG_LEV_T8){
			RELATED_DIV=this.getNumGradeDiv(data.itemId,data.itemId+child.EngLevelGrade.itemId,child.EngLevelGrade.value,MsgSet["EXAM_SCORE"],"");
		}
//		else if(EXAM_TYPE_DEF==EXAM_TYPE_MAP.ENG_LEV_T7||EXAM_TYPE_DEF==EXAM_TYPE_MAP.ENG_LEV_T8){
//			var optList='<option value="'+MsgSet["PASS_Y"]+'">'+MsgSet["PASS_Y"]+'</option>';
//				optList+=('<option value="'+MsgSet["PASS_N"]+'">'+MsgSet["PASS_N"]+'</option>');
//			RELATED_DIV=this.getChoseGradeDiv(data.itemId,data.itemId+child.EngLevelGrade.itemId,child.EngLevelGrade.value,MsgSet["EXAM_PASS"],optList);//,child.EngLevelGrade.value
//		}
		else if(EXAM_TYPE_DEF==EXAM_TYPE_MAP.ENG_LEV_T9){
			var optList='<option value="'+MsgSet["EXAM_TEM4"]+'">'+MsgSet["EXAM_TEM4"]+'</option>';
			optList+=('<option value="'+MsgSet["EXAM_TEM8"]+'">'+MsgSet["EXAM_TEM8"]+'</option>');
			RELATED_DIV=this.getChoseGradeDiv(data.itemId,data.itemId+child.EngLevelGrade.itemId,child.EngLevelGrade.value,MsgSet["EXAM_GSCORE"],optList);//,child.EngLevelGrade.value EXAM_GSCORE
		}else if(EXAM_TYPE_DEF==EXAM_TYPE_MAP.ENG_LEV_T10||EXAM_TYPE_DEF==EXAM_TYPE_MAP.ENG_LEV_T11){
			var optList='<option value="'+MsgSet["INTER_A"]+'">'+MsgSet["INTER_A"]+'</option>';
			optList+=('<option value="'+MsgSet["INTER_B"]+'">'+MsgSet["INTER_B"]+'</option>');
			RELATED_DIV=this.getChoseGradeDiv(data.itemId,data.itemId+child.EngLevelGrade.itemId,child.EngLevelGrade.value,MsgSet["EXAM_GSCORE"],optList);//,child.EngLevelGrade.value EXAM_GSCORE
		}else if(EXAM_TYPE_DEF==EXAM_TYPE_MAP.ENG_LEV_T12){//LEV_C
			var optList='<option value="'+MsgSet["LEV_A"]+'">'+MsgSet["LEV_A"]+'</option>';
			optList+=('<option value="'+MsgSet["LEV_B"]+'">'+MsgSet["LEV_B"]+'</option>');
			optList+=('<option value="'+MsgSet["LEV_C"]+'">'+MsgSet["LEV_C"]+'</option>');
			RELATED_DIV=this.getChoseGradeDiv(data.itemId,data.itemId+child.EngLevelGrade.itemId,child.EngLevelGrade.value,MsgSet["EXAM_GSCORE"],optList);//EXAM_SCORE
		}else if(EXAM_TYPE_DEF==EXAM_TYPE_MAP.ENG_LEV_T13){
			RELATED_DIV=this.getNumGradeDiv(data.itemId,data.itemId+child.EngLevelGrade.itemId,child.EngLevelGrade.value,MsgSet["EXAM_SCORE"],DATE_HTML);
		}
		return RELATED_DIV;
	},
	bindTimePicker:function($inputBox,$selectBtn){
		$inputBox.each(function(){
			  $(this).datetimepicker({
				showButtonPanel:true,
				showTimepicker: false,
				changeMonth: true,
				changeYear: true,
				yearRange: "1960:2030",
				dateFormat:"yy-mm-dd",
				onClose:function(dateText, inst){
					$(this).trigger("blur");
				}
			  });
		 });
		   //日期小图标事件绑定:
		 $selectBtn.each(function(){
			   $(this).click(function(){
				   $(this).prev().click();
			   })
		  })
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
			"ENG_LEV_T1":"GRE",
			"ENG_LEV_T2":"GMAT",
			"ENG_LEV_T3":"TOFEL",
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
			htmlContent += '<div class="mainright-box pos-rela">';
			//-----
			if(index > 1&&!SurveyBuild._readonly){
				htmlContent += '		<div onclick="SurveyBuild.deleteEngLev(this);" class="input-delbtn">' + MsgSet["DEL"] + '&nbsp;&nbsp;<span class="input-btn-icon"><img src="' + TzUniversityContextPath + '/statics/images/appeditor/new/add-delete.png"></span></div>';
			}
			var child=childList[x];
			var EXAM_TYPE_DEF=child.EngLevelType.value;
			//-----
			//1.公司性质
			if (SurveyBuild._readonly) {
				htmlContent += '<div  class="main_inner_content_info_autoheight">';
				//<!--通用只读模式 成绩+日期显示-->
				htmlContent+=this.getReadDiv(data,child,EXAM_TYPE_DEF,EXAM_TYPE_MAP);
				//<!--通用上传控件部分-->
				//<!--通用上传控件部分-->
				htmlContent+='<div name="'+data.itemId+'UP" id="'+data.itemId+'UP" class="input-list" style="display:block">'
				htmlContent+='<div class="clear"></div>';
				htmlContent+='<div class="input-list-info left"><span >证书/成绩扫描件:</span></div>'
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
				for(var i in EXAM_TYPE_MAP){
					OPT_ENG+='<option value="'+EXAM_TYPE_MAP[i]+'"'+(EXAM_TYPE_DEF==EXAM_TYPE_MAP[i]?'selected="selected"': '')+'>'+EXAM_TYPE_MAP[i]+'</option>'
				}
				//----------------------------放入考试种类OPT
				htmlContent += '<div  class="input-list" style="display:block">';
				htmlContent += '	<div  class="input-list-info left"><span class="red-star">*</span>' + child.EngLevelType.itemName + '：</div>';
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
				htmlContent+='<div name="'+data.itemId+'UP" id="'+data.itemId+'UP"  class="input-list" style="display:block">'
					htmlContent+='<div class="input-list-info left"><span class="red-star">*</span><span >'+MsgSet["SCORE_UP"]+'</span></div>'
						htmlContent+='<div class="input-list-texttemplate left" >'
							//'<input type="file" id="'+data["itemId"] + child.EngLevelUp.itemId+'File"  name="' + data["itemId"] + child.EngLevelUp.itemId + 'File" onchange=SurveyBuild.eduImgUpload(this,"EngLevelUp") accept="image/*"/>'
								htmlContent+= '<div class="filebtn left">';
								htmlContent+= '	<div class="filebtn-org"><img src="' + TzUniversityContextPath + '/statics/images/appeditor/new/upload.png" />&nbsp;&nbsp;' + MsgSet["UPLOAD_BTN_MSG"] + '</div>';
								htmlContent+= '	<input data-instancid="' + data.instanceId + '" id="'+child.EngLevelUp.itemId+ '" name="'+ data.itemId + '" title="' + data.itemName + '" onchange="SurveyBuild.engUploadAttachment(this,\''+ data.instanceId +'\',\''+ child.EngLevelUp.instanceId +'\')" type="file" class="filebtn-orgtext"/>';
								htmlContent+= '</div>';
								htmlContent+='<div class="clear"></div>'
									
										htmlContent+= '<div>' + msg + '<div id="' + child.EngLevelUp.itemId + 'Tip" class="onShow" style="line-height:32px;height:18px;"><div class="onShow"></div></div></div>';
					        	
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
	_eventbind: function(data) {
				//选中隐藏和显示相应值
				var EXAM_TYPE_MAP={
					"ENG_LEV_T1":"GRE",
					"ENG_LEV_T2":"GMAT",
					"ENG_LEV_T3":"TOFEL",
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
				 if(len==undefined){
					 len=1;
				 }
				 var type_select="";
//				 //为所有的select注册事件
				 for(var i=0;i<len;i++){
					 var child=children[i];
						type_select=$("#"+ data["itemId"] + child.EngLevelType.itemId);
						type_select.each(function(index){
							type_select.chosen({width: "100%"});
							$(this).on("change",function(){
								//-------------------------清空数据:
								child.EngLevelDate.value="";
								child.EngLevelGrade.value="";
								//-------------------------
								for(var i in EXAM_TYPE_MAP){
									if($(this).val()==EXAM_TYPE_MAP[i]){
										var relatedDiv=data.getRelatedDiv(data,child,EXAM_TYPE_MAP[i],EXAM_TYPE_MAP);
										
										var topDiv=$(this).closest(".main_inner_content_info_autoheight");
										var timeDiv=topDiv.find("#"+data["itemId"]+"_DATE_DIV");
										var gradeDiv=topDiv.find("#"+data["itemId"]+"_GRADE_DIV");
										
										gradeDiv.after(relatedDiv);
										gradeDiv.remove();
										timeDiv.remove();
										//SELECT美化：
										if(i=="ENG_LEV_T7"||i=="ENG_LEV_T8"||i=="ENG_LEV_T9"||i=="ENG_LEV_T10"||i=="ENG_LEV_T11"||i=="ENG_LEV_T12"){
											gradeDiv=topDiv.find("#"+data["itemId"]+"_GRADE_DIV");
											gradeDiv.find("select").chosen({width: "100%"});
											//文字说明 解析:
										}
										//timePicker:
									    var $inputBox = $("#" + data.itemId +child.EngLevelDate.itemId);
									    var $selectBtn = $("#" + data.itemId +child.EngLevelDate.itemId + "_Btn");
										data.bindTimePicker($inputBox,$selectBtn);
										//非空验证:
										data.checkInputNotNull($inputBox);
									    var $inputBox2 = $("#" + data.itemId +child.EngLevelGrade.itemId);
									    data.checkInputNotNull($inputBox2);
									}
								}
								//-------------------------
							})
						});
						//为所有的timePicker注册事件:
						//日期控件处理1.2.3.4.13

					    var $inputBox = $("#" + data.itemId +child.EngLevelDate.itemId);
					    var $selectBtn = $("#" + data.itemId +child.EngLevelDate.itemId + "_Btn");
						data.bindTimePicker($inputBox,$selectBtn);
		
						//------上传控件验证:
						var $fileInput=$("#"+child["EngLevelUp"].itemId);
						
						$fileInput.each(function(k){
							$(this).mouseover(function(e){
								$(this).prev().css("opacity","0.8");	
							});
							$(this).attr("len",i);
							//console.log("len:"+$(this).attr("len"));
							$(this).mouseout(function(e) {
								$(this).prev().css("opacity","1");
					        });
							$(this).formValidator({tipID:($(this).attr("id")+'Tip'), onShow:"", onFocus:"&nbsp;", onCorrect:"&nbsp;"});
							var me=$(this);
							$(this).functionValidator({
								fun:function(val,el){
									//必须上传
									//if (child["EngLevelUp"].isRequire == "Y"){
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
									//}
								}	
							});
						});
						//日期.成绩非空验证:
						data.checkInputNotNull($inputBox);
					    var $inputBox2 = $("#" + data.itemId +child.EngLevelGrade.itemId);
					    data.checkInputNotNull($inputBox2);
				 }
				 //所有看的到的select美化:
				$("select").each(function(){
					$(this).chosen({width:"100%"});
				});
		       
	}
})