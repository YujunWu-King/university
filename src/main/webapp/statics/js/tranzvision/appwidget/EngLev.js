SurveyBuild.extend("EngLev", "baseComponent", {
	itemName: "英语水平",
	title: "英语水平",
	isDoubleLine: "Y",
	fixedContainer: "Y",//固定容器标识
	children: {
		//
		"EngLevelType": {
			"instanceId": "EngLevelType",
			"itemId": "exam_type",
			//"itemName": MsgSet["EXAM_TYPE"],
			"itemName":"考试名称",
			//"title": MsgSet["EXAM_TYPE"],
			"title": "考试名称",
			"orderby": 1,
			"value": "-1",
			"StorageType": "S",
			"classname": "Select"
		},
		//1.GRE值 存储结构
		"EngLevelOpt1_1": {
			"instanceId": "EngLevelOpt1_1",
			"itemId": "opt1_exam_date",
			//"itemName": MsgSet["EXAM_TDATE"],
			"itemName": "Test date",
			//"title": MsgSet["EXAM_TDATE"],
			"title": "Test date",
			"orderby": 2,
			"value": "",
			"StorageType": "S",
			"classname": "DateInputBox"
		},
		"EngLevelOpt1_2": {
			"instanceId": "EngLevelOpt1_2",
			"itemId": "opt1_exam_score",
			//"itemName": MsgSet["EXAM_TSCORE"],
			"itemName": "Total(Score)",
			//"title": MsgSet["EXAM_TSCORE"],
			"title": "Total(Score)",
			"orderby": 3,
			"value": "",
			"StorageType": "S",
			"classname": "SingleTextBox"
		},
		//2.GMAT值 存储结构
	 	"EngLevelOpt2_1": {
			"instanceId": "EngLevelOpt2_1",
			"itemId": "opt2_exam_date",
			//"itemName": MsgSet["EXAM_TDATE"],
			"itemName": "Test date",
			//"title": MsgSet["EXAM_TDATE"],
			"title": "Test date",
			"orderby": 4,
			"value": "",
			"StorageType": "S",
			"classname": "DateInputBox"
		},
		"EngLevelOpt2_2": {
			"instanceId": "EngLevelOpt2_2",
			"itemId": "opt2_exam_score",
			//"itemName": MsgSet["EXAM_TSCORE"],
			"itemName": "Total(Score)",
			//"title": MsgSet["EXAM_TSCORE"],
			"title": "Total(Score)",
			"orderby": 5,
			"value": "",
			"StorageType": "S",
			"classname": "SingleTextBox"
		},
		//3.TOFEL值 存储结构
		"EngLevelOpt3_1": {
			"instanceId": "EngLevelOpt3_1",
			"itemId": "opt3_exam_date",
			//"itemName": MsgSet["EXAM_TDATE"],
			"itemName": "Test date",
			//"title": MsgSet["EXAM_TDATE"],
			"title": "Test date",
			"orderby": 6,
			"value": "",
			"StorageType": "S",
			"classname": "DateInputBox"
		},
		"EngLevelOpt3_2": {
			"instanceId": "EngLevelOpt3_2",
			"itemId": "opt3_exam_score",
			//"itemName": MsgSet["EXAM_TOTAL"],
			"itemName": "Total",
			//"title": MsgSet["EXAM_TOTAL"],
			"title": "Total",
			"orderby": 7,
			"value": "",
			"StorageType": "S",
			"classname": "SingleTextBox"
		},
		//4.IELTS值 存储结构
		"EngLevelOpt4_1": {
			"instanceId": "EngLevelOpt4_1",
			"itemId": "opt4_exam_date",
			//"itemName": MsgSet["EXAM_TDATE"],
			"itemName": "Test date",
			//"title": MsgSet["EXAM_TDATE"],
			"title": "Test date",
			"orderby": 8,
			"value": "",
			"StorageType": "S",
			"classname": "DateInputBox"
		},
		"EngLevelOpt4_2": {
			"instanceId": "EngLevelOpt4_2",
			"itemId": "opt4_exam_score",
			//"itemName": MsgSet["EXAM_ISCORE"],
			"itemName": "Overall Band Score",
			//"title": MsgSet["EXAM_ISCORE"],
			"title": "Overall Band Score",
			"orderby": 9,
			"value": "",
			"StorageType": "S",
			"classname": "SingleTextBox"
		},
		//5.英语六级（710分制）存储结构
		"EngLevelOpt5_1": {
			"instanceId": "EngLevelOpt5_1",
			"itemId": "opt5_exam_score",
			//"itemName": MsgSet["EXAM_SCORE"],
			"itemName": "总分",
			//"title": MsgSet["EXAM_SCORE"],
			"title": "总分",
			"orderby": 10,
			"value": "",
			"StorageType": "S",
			"classname": "SingleTextBox"
		},
		//6.英语四级（710分制）存储结构		
		"EngLevelOpt6_1": {
			"instanceId": "EngLevelOpt6_1",
			"itemId": "opt6_exam_score",
			//"itemName": MsgSet["EXAM_SCORE"],
			"itemName": "总分",
			//"title": MsgSet["EXAM_SCORE"],
			"title": "总分",
			"orderby": 11,
			"value": "",
			"StorageType": "S",
			"classname": "SingleTextBox"
		},
		//7.英语六级（100分制）存储结构		
		"EngLevelOpt7_1": {
			"instanceId": "EngLevelOpt7_1",
			"itemId": "opt7_cet6_pass",
			//"itemName": MsgSet["EXAM_PASS"],
			"itemName": "是否通过",
			//"title": MsgSet["EXAM_PASS"],
			"title": "是否通过",
			"orderby": 12,
			"value": "",
			"StorageType": "S",
			"classname": "Select"
		},
		//8.英语四级（100分制）存储结构		
		"EngLevelOpt8_1": {
			"instanceId": "EngLevelOpt8_1",
			"itemId": "opt8_cet4_pass",
			//"itemName": MsgSet["EXAM_PASS"],
			"itemName": "是否通过",
			//"title": MsgSet["EXAM_PASS"],
			"title": "是否通过",
			"orderby": 13,
			"value": "",
			"StorageType": "S",
			"classname": "Select"
		},
		//9.专业英语 存储结构		
		"EngLevelOpt9_1": {
			"instanceId": "EngLevelOpt9_1",
			"itemId": "opt9_exam_result",
			//"itemName": MsgSet["EXAM_GSCORE"],
			"itemName": "总分(成绩)",
			//"title": MsgSet["EXAM_GSCORE"],
			"title": "总分(成绩)",
			"orderby": 14,
			"value": "",
			"StorageType": "S",
			"classname": "Select"
		},
		//10.高级口译 存储结构		
		"EngLevelOpt10_1": {
			"instanceId": "EngLevelOpt10_1",
			"itemId": "opt10_exam_result",
			//"itemName": MsgSet["EXAM_GSCORE"],
			"itemName": "总分(成绩)",
			//"title": MsgSet["EXAM_GSCORE"],
			"title": "总分(成绩)",
			"orderby": 15,
			"value": "",
			"StorageType": "S",
			"classname": "Select"
		},
		//11.中级口译 存储结构		
		"EngLevelOpt11_1": {
			"instanceId": "EngLevelOpt11_1",
			"itemId": "opt11_exam_result",
			//"itemName": MsgSet["EXAM_GSCORE"],
			"itemName": "总分(成绩)",
			//"title": MsgSet["EXAM_GSCORE"],
			"title": "总分(成绩)",
			"orderby": 16,
			"value": "",
			"StorageType": "S",
			"classname": "Select"
		},
		//12.BEC 存储结构		
		"EngLevelOpt12_1": {
			"instanceId": "EngLevelOpt12_1",
			"itemId": "opt12_exam_result",
			//"itemName": MsgSet["EXAM_GSCORE"],
			"itemName": "总分(成绩)",
			//"title": MsgSet["EXAM_GSCORE"],
			"title": "总分(成绩)",
			"orderby": 17,
			"value": "",
			"StorageType": "S",
			"classname": "Select"
		},
		//13.TOEIC（990） 存储结构
		"EngLevelOpt13_1": {
			"instanceId": "EngLevelOpt13_1",
			"itemId": "opt13_exam_date",
			//"itemName": MsgSet["EXAM_DATE"],
			"itemName": "考试时间",
			//"title": MsgSet["EXAM_DATE"],
			"title": "考试时间",
			"orderby": 18,
			"value": "",
			"StorageType": "S",
			"classname": "DateInputBox"
		},
		"EngLevelOpt13_2": {
			"instanceId": "EngLevelOpt13_2",
			"itemId": "opt13_exam_score",
			//"itemName": MsgSet["EXAM_SCORE"],
			"itemName": "总分",
			//"title": MsgSet["EXAM_SCORE"],
			"title": "总分",
			"orderby": 19,
			"value": "",
			"StorageType": "S",
			"classname": "SingleTextBox"
		},
		//上传文件 存储结构 EngLevelUp---
		"EngLevelUp": {
			"instanceId": "EngLevelUp",
			"itemId": "exam_upload",
			//"itemName": MsgSet["EXAM_UPLOAD"],
			"itemName": "扫描件上传",
			//"title": MsgSet["EXAM_UPLOAD"],
			"title": "扫描件上传",
			"orderby": 20,
			"value": "",
			"StorageType": "S",
			"classname": "imagesUpload"
		}
	},
	minLines: "1",
	maxLines: "4",
	//linesNo: [1, 2, 3],
	_getHtml: function(data, previewmode) {
		var c = ""

		if (previewmode) {
			var htmlContent = this._getContentHtml(data);

			c += '<div class="main_inner_content_top"></div>';
			c += '<div class="main_inner_content">';
			c += htmlContent;
			c += '</div>';
			c += '<div class="main_inner_content_foot"></div>';

		} else {
			var htmlRead = '';
			//考试类型类型
			htmlRead += '<div class="type_item_li">';
			htmlRead += '	<span class="type_item_label">'+"考试类型"+'：</span>';
			htmlRead += '		<b class="read-select" style="min-width:120px;">--'+MsgSet["PLEASE_SELECT"]+'--</b>';
			htmlRead += '	</div>';

			//成绩显示DIV
			htmlRead += '<div class="type_item_li">';
			htmlRead += '	<span class="type_item_label">'+"考试成绩"+'：</span>';
			htmlRead += '		<b class="read-input" style="min-width:120px;">显示相应考试成绩</b>';
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
	_getContentHtml: function(data) {
		//考试种类OPT
		var TzUniversityContextPath="/university";
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
		console.dir(data);
		var child=data["children"][0];
		if (child == undefined) {
	   		 child=data["children"];
	   	 	}
		//var child=data["children"];
		var htmlContent="";
		//--考试种类初始值
		//console.dir(child);
		var EXAM_TYPE_DEF=child.EngLevelType.value;
		
		//------------

			htmlContent += '<div class="main_inner_content_para" style="margin-top:50px;margin-bottom:30px" >';
			
			htmlContent += '<div class="main_inner_content_top"></div><div class="padding_div"></div><div class="main_inner_content_foot"></div>';

			//1.公司性质
			if (SurveyBuild._readonly) {
				//只读模式
				//----------------------------考试种类OPT
				var OPT_ENG='';
				for(var i in EXAM_TYPE_MAP){
					OPT_ENG+='<option value="'+i+'"'+(EXAM_TYPE_DEF==i?'selected="selected"': '')+'>'+EXAM_TYPE_MAP[i]+'</option>'
				}
				//----------------------------放入考试种类OPT
				htmlContent += '<div  class="input-list" style="display:block">';
				htmlContent += '	<div   class="input-list-info left">' + child.EngLevelType.itemName + ':</div>';
				htmlContent += '	<div class="input-list-text left input-edu-select">';
				htmlContent += '		<select disabled=true id="' + data["itemId"] + child.EngLevelType.itemId + '" class="chosen-select" style="width: 255px;" data-regular="" title="' + child.EngLevelType.itemName + '" value="' + child.EngLevelType["value"] + '" name="' + data["itemId"] + child.EngLevelType.itemId + '">';
				htmlContent += '			<option value="-1">' + '--'+MsgSet["PLEASE_SELECT"]+'--' + '</option>';
				htmlContent += OPT_ENG;
				htmlContent += '		</select>';
				//----------------------------不知道干嘛用的，这段可删除
				htmlContent += '		<div style="margin-top:-40px;margin-left:256px"><div id="' + data["itemId"] + child.EngLevelType.itemId + 'Tip" class="onCorrect" style="margin: 0px; padding: 0px; background: transparent;">';
				htmlContent += '			<div class="onCorrect">&nbsp;</div></div>';
				htmlContent += '		</div>';
				htmlContent += '	</div>';
				htmlContent += '<div class="clear"></div>';
				htmlContent += '</div>';
				//---------------------------根据考试种类，要显示的不同种类DIV
				//日期框HTML
				var getDateDiv=function(date_id,date_val,date_name,date_title){
					var DATE_DIV='';
					DATE_DIV += '  <div class="input-list" style="display:block"><span class="input-list-info left">Test date：</span>';
					DATE_DIV += '      <div class="input-list-text left"><input readOnly=true id="' + date_id+ '" name="' + date_name+ '" type="text" value="'  +date_val + '"class="inpu-list-text-enter" style="height:36px" readonly="readonly" onclick="this.focus()" title="' +date_name + '">';
					DATE_DIV += '      <img id="' + date_id+ '_Btn" src="' + TzUniversityContextPath + '/statics/images/appeditor/calendar.png" style="position:relative;top:5px;left:-31px;cursor:pointer;"></div>';
					DATE_DIV += ' <div class="clear"></div>';
					DATE_DIV += '</div>';
					return DATE_DIV;
				}
				//--GRE日期框HTML
				var GRE_DATE=getDateDiv(data.itemId +child.EngLevelOpt1_1.itemId,child.EngLevelOpt1_1.value,data.itemId+child.EngLevelOpt1_1.name,child.EngLevelOpt1_1.itemName);
				//---GMAT日期HTML
				var GMAT_DATE=getDateDiv(data.itemId +child.EngLevelOpt2_1.itemId,child.EngLevelOpt2_1.value,data.itemId+child.EngLevelOpt2_1.name,child.EngLevelOpt2_1.itemName);
				//---TOFEL日期HTML
				var TOFEL_DATE=getDateDiv(data.itemId +child.EngLevelOpt3_1.itemId,child.EngLevelOpt3_1.value,data.itemId+child.EngLevelOpt3_1.name,child.EngLevelOpt3_1.itemName);
				//---IELTS日期HTML
				var IELTS_DATE=getDateDiv(data.itemId +child.EngLevelOpt4_1.itemId,child.EngLevelOpt4_1.value,data.itemId+child.EngLevelOpt4_1.name,child.EngLevelOpt4_1.itemName);
				//---TOEIC日期HTML
				var TOEIC_DATE=getDateDiv(data.itemId +child.EngLevelOpt13_1.itemId,child.EngLevelOpt13_1.value,data.itemId+child.EngLevelOpt13_1.name,child.EngLevelOpt13_1.itemName);
				//----------------
				
				
				//<!--1.GRE关联DIV-->
				var GRE_DIV='';
				if(EXAM_TYPE_DEF=="ENG_LEV_T1")
					GRE_DIV+='<div id="'+data.itemId+'ENG_LEV_T1" class="input-list" style="display:block" >'
				else
				GRE_DIV+='<div id="'+data.itemId+'ENG_LEV_T1" class="input-list" style="display:none">'
					GRE_DIV+=GRE_DATE
					GRE_DIV+='<div class="input-list-info left"><span>Total(Score):</span></div> <div class="input-list-text left" ><input readOnly=true id="'+data.itemId+child.EngLevelOpt1_2.itemId+'" value="'+child.EngLevelOpt1_2.value+'"/></div>'
				GRE_DIV+='</div>';
				GRE_DIV+='<div class="clear"></div>';
				htmlContent+=GRE_DIV;
				
				//<!--2.GMAT关联DIV-->
				var GMAT_DIV='';
				if(EXAM_TYPE_DEF=="ENG_LEV_T2")
					GMAT_DIV+='<div id="'+data.itemId+'ENG_LEV_T2" class="input-list" style="display:block">'
				else
				GMAT_DIV+='<div id="'+data.itemId+'ENG_LEV_T2" class="input-list" style="display:none">'
					GMAT_DIV+=GMAT_DATE
					GMAT_DIV+='<div class="input-list-info left"><span>Total(Score):</span></div><div class="input-list-text left" ><input readOnly=true id="'+data.itemId+child.EngLevelOpt2_2.itemId+'" value="'+child.EngLevelOpt2_2.value+'"/></div>'
				GMAT_DIV+='</div>'
					GMAT_DIV+='<div class="clear"></div>'
				htmlContent+=GMAT_DIV;
				
				//<!--3.TOFEL关联DIV-->
				var TOFEL_DIV='';
				if(EXAM_TYPE_DEF=="ENG_LEV_T3")
					TOFEL_DIV+='<div id="'+data.itemId+'ENG_LEV_T3" class="input-list" style="display:block">'
				else
				TOFEL_DIV+='<div id="'+data.itemId+'ENG_LEV_T3" class="input-list" style="display:none">'
					TOFEL_DIV+=TOFEL_DATE
					TOFEL_DIV+='<div class="input-list-info left"><span>Total:</span></div><div class="input-list-text left" ><input readOnly=true id="'+data.itemId+child.EngLevelOpt3_2.itemId+'" value="'+child.EngLevelOpt3_2.value+'"/></div>'
				TOFEL_DIV+='</div>'	
					TOFEL_DIV+='<div class="clear"></div>'	
				htmlContent+=TOFEL_DIV;
				
				//<!--4.IELTS关联DIV-->
				var IELTS_DIV='';
				if(EXAM_TYPE_DEF=="ENG_LEV_T4")
					IELTS_DIV+='<div id="'+data.itemId+'ENG_LEV_T4" class="input-list" style="display:block">'
				else
				IELTS_DIV+='<div id="'+data.itemId+'ENG_LEV_T4" class="input-list" style="display:none">'
					IELTS_DIV+=IELTS_DATE
					IELTS_DIV+='<div class="input-list-info left"><span>Overall Band Score:</span></div><div class="input-list-text left" ><input readOnly=true id="'+data.itemId+child.EngLevelOpt4_2.itemId+'" value="'+child.EngLevelOpt4_2.value+'"/></div>'
				IELTS_DIV+='</div>'
					IELTS_DIV+='<div class="clear"></div>'
				htmlContent+=IELTS_DIV;
				
				//<!--5.英语六级（710分制）关联DIV-->
				var CET6_DIV='';
				if(EXAM_TYPE_DEF=="ENG_LEV_T5")
					CET6_DIV+='<div id="'+data.itemId+'ENG_LEV_T5" class="input-list" style="display:block">'
				else	
				CET6_DIV+='<div id="'+data.itemId+'ENG_LEV_T5" class="input-list" style="display:none">'
					CET6_DIV+='<span class="input-list-info left">总分:</span><div class="input-list-text left" ><input readOnly=true class="input-list-text left"   id="'+data["itemId"] + child.EngLevelOpt5_1.itemId+'" value="'+child.EngLevelOpt5_1.value+'"/></div>'
				CET6_DIV+='</div>'
					CET6_DIV+='<div class="clear"></div>'
				htmlContent+=CET6_DIV;
				
				//<!--6.英语四级（710分制）关联DIV-->
				var CET4_DIV='';
				if(EXAM_TYPE_DEF=="ENG_LEV_T6")
					CET4_DIV+='<div id="'+data.itemId+'ENG_LEV_T6" class="input-list" style="display:block">'
				else
				CET4_DIV+='<div id="'+data.itemId+'ENG_LEV_T6" class="input-list" style="display:none">'
					CET4_DIV+='<span class="input-list-info left">总分:</span><div class="input-list-text left" ><input readOnly=true id="'+data["itemId"] + child.EngLevelOpt6_1.itemId+'" value="'+child.EngLevelOpt6_1.value+'"/></div>'
				CET4_DIV+='</div>'
					CET4_DIV+='<div class="clear"></div>'
				htmlContent+=CET4_DIV;
				
				//<!--7.英语六级（100分制）关联DIV-->
				var CET6_DIV2='';
				if(EXAM_TYPE_DEF=="ENG_LEV_T7")
					CET6_DIV2+='<div id="'+data.itemId+'ENG_LEV_T7" class="input-list" style="display:block">'
				else
				CET6_DIV2+='<div id="'+data.itemId+'ENG_LEV_T7" class="input-list" style="display:none">'
					CET6_DIV2+='<div class="input-list-info left"><span>是否通过:</span></div>'
					CET6_DIV2+='<div class="input-list-text left" ><select style="width:100%;height:36px" disabled=true id="'+data["itemId"] + child.EngLevelOpt7_1.itemId+'" value="'+child.EngLevelOpt7_1.value+'">'
						CET6_DIV2+='<option value="Y">通过</option>'
						CET6_DIV2+='<option value="N">未通过</option>'
					CET6_DIV2+='</select></div>'
				CET6_DIV2+='</div>'
					CET6_DIV2+='<div class="clear"></div>'
				htmlContent+=CET6_DIV2;
				
				//<!--8.英语四级（100分制）关联DIV-->
				var CET4_DIV2='';
				if(EXAM_TYPE_DEF=="ENG_LEV_T8")
					CET4_DIV2+='<div id="'+data.itemId+'ENG_LEV_T8" class="input-list" style="display:block">'
				else
				CET4_DIV2+='<div id="'+data.itemId+'ENG_LEV_T8" class="input-list" style="display:none">'
					CET4_DIV2+='<div class="input-list-info left"><span>是否通过:</span></div>'
					CET4_DIV2+='<div class="input-list-text left" ><select style="width:100%;height:36px" disabled=true  id="'+data["itemId"] + child.EngLevelOpt8_1.itemId+'" value="'+child.EngLevelOpt8_1.value+'">'
						CET4_DIV2+='<option value="Y">通过</option>'
						CET4_DIV2+='<option value="N">未通过</option>'
					CET4_DIV2+='</select></div>'
				CET4_DIV2+='</div>'
					CET4_DIV2+='<div class="clear"></div>'
				htmlContent+=CET4_DIV2;
				
				//<!--9.专业英语关联DIV-->
				var TEM_DIV=''
				if(EXAM_TYPE_DEF=="ENG_LEV_T9")
					TEM_DIV+='<div id="'+data.itemId+'ENG_LEV_T9" class="input-list" style="display:block">'
				else
				TEM_DIV+='<div id="'+data.itemId+'ENG_LEV_T9" class="input-list" style="display:none">'
					TEM_DIV+='<div class="input-list-info left"><span>总分(成绩):</span></div>'
					TEM_DIV+='<div class="input-list-text left" ><select style="width:100%;height:36px" disabled=true  id="'+data["itemId"] + child.EngLevelOpt9_1.itemId+'" value="'+child.EngLevelOpt9_1.value+'">'
						TEM_DIV+='<option value="TEM4">专业四级</option>'
						TEM_DIV+='<option value="TEM8">专业八级</option>'
					TEM_DIV+='</select></div>'
				TEM_DIV+='</div>'
					TEM_DIV+='<div class="clear"></div>'
				htmlContent+=TEM_DIV;
				
				//<!--10.高级口译关联DIV-->
				var H_INTER_DIV='';
				if(EXAM_TYPE_DEF=="ENG_LEV_T10")
					H_INTER_DIV+='<div id="'+data.itemId+'ENG_LEV_T10" class="input-list" style="display:block">'
				else
				H_INTER_DIV+='<div id="'+data.itemId+'ENG_LEV_T10" class="input-list" style="display:none">'
					H_INTER_DIV+='<div class="input-list-info left"><span>总分(成绩):</span></div>'
					H_INTER_DIV+='<div class="input-list-text left" ><select style="width:100%;height:36px" disabled=true  id="'+data["itemId"] + child.EngLevelOpt10_1.itemId+'" value="'+child.EngLevelOpt10_1.value+'">'
						H_INTER_DIV+='<option>拿到资格证书</option>'
						H_INTER_DIV+='<option>笔试证书</option>'
					H_INTER_DIV+='</select></div>'
				H_INTER_DIV+='</div>'
					H_INTER_DIV+='<div class=""clear></div>'
				htmlContent+=H_INTER_DIV;
				
				//<!--11.中级口译关联DIV-->
				var M_INTER_DIV='';
				if(EXAM_TYPE_DEF=="ENG_LEV_T11")
					M_INTER_DIV+='<div id="'+data.itemId+'ENG_LEV_T11" class="input-list" style="display:block">'
				else
				M_INTER_DIV+='<div id="'+data.itemId+'ENG_LEV_T11" class="input-list" style="display:none">'
					M_INTER_DIV+='<div class="input-list-info left"><span>总分(成绩):</span></div>'
					M_INTER_DIV+='<div class="input-list-text left" ><select style="width:100%;height:36px" disabled=true  id="'+data["itemId"] + child.EngLevelOpt11_1.itemId+'" value="'+child.EngLevelOpt11_1.value+'">'
						M_INTER_DIV+='<option value="A">拿到资格证书</option>'
						M_INTER_DIV+='<option value="B">笔试证书</option>'
					M_INTER_DIV+='</select></div>'
				M_INTER_DIV+='</div>'
					M_INTER_DIV+='<div class="clear"></div>'
				htmlContent+=M_INTER_DIV;
				
				//<!--12.BEC关联DIV-->
				var BEC_DIV='';
				if(EXAM_TYPE_DEF=="ENG_LEV_T12")
					BEC_DIV+='<div id="'+data.itemId+'ENG_LEV_T12" class="input-list" style="display:block">'
				else
				BEC_DIV+='<div id="'+data.itemId+'ENG_LEV_T12" class="input-list" style="display:none">'
					BEC_DIV+='<div class="input-list-info left"><span>总分(成绩):</span></div>'
					BEC_DIV+='<div class="input-list-text left" ><select style="width:100%;height:36px" disabled=true  id="'+data["itemId"] + child.EngLevelOpt12_1.itemId+'" value="'+child.EngLevelOpt12_1.value+'">'
						BEC_DIV+='<option value="A">高级</option>'
						BEC_DIV+='<option value="B">中级</option>'
						BEC_DIV+='<option value="C">初级</option>'
					BEC_DIV+='</select></div>'
				BEC_DIV+='</div>'
					BEC_DIV+='<div class="clear"></div>'
				htmlContent+=BEC_DIV;
				
				//<!--13.TOEIC990-->
				var TOEIC_DIV='';
				if(EXAM_TYPE_DEF=="ENG_LEV_T13")
					TOEIC_DIV+='<div id="'+data.itemId+'ENG_LEV_T13" class="input-list" style="display:block">'
				else
				TOEIC_DIV+='<div id="'+data.itemId+'ENG_LEV_T13" class="input-list" style="display:none">'
					TOEIC_DIV+=TOEIC_DATE
					TOEIC_DIV+='<div class="input-list-info left"><span>总分:</span><input readOnly=true id="'+data["itemId"] + child.EngLevelOpt13_1.itemId+'" value="'+child.EngLevelOpt13_1.value+'"/>'
				TOEIC_DIV+='</div>'	
					TOEIC_DIV+='<div class="clear"></div>'	
				htmlContent+=TOEIC_DIV;
				
				//<!--通用上传控件部分-->
				if(EXAM_TYPE_DEF!=""&&EXAM_TYPE_DEF!="-1")
					htmlContent+='<div id="'+data.itemId+'UP" class="input-list" style="display:block">'
				else
				htmlContent+='<div id="'+data.itemId+'UP" class="input-list" style="display:none">'
					htmlContent+='<div class="input-list-info-readonly left"><span>上传证书/成绩扫描件</span></div><div class="input-list-wz-readonly left" ><input readOnly=true type="file" id="'+data["itemId"] + child.EngLevelUp.itemId+'"/>'
				htmlContent+='<div class="clear"></div>'
					htmlContent+='</div>'
				//---------------------------

			} else {//填写模式
				
				//----------------------------考试种类OPT
				var OPT_ENG='';
				for(var i in EXAM_TYPE_MAP){
					OPT_ENG+='<option value="'+i+'"'+(EXAM_TYPE_DEF==i?'selected="selected"': '')+'>'+EXAM_TYPE_MAP[i]+'</option>'
				}
				//----------------------------放入考试种类OPT
				htmlContent += '<div  class="input-list" style="display:block">';
				htmlContent += '	<div  class="input-list-info left">' + child.EngLevelType.itemName + ':</div>';
				htmlContent += '	<div class="input-list-text left input-edu-select">';
				htmlContent += '		<select id="' + data["itemId"] + child.EngLevelType.itemId + '" class="chosen-select" style="width:100%;" data-regular="" title="' + child.EngLevelType.itemName + '" value="' + child.EngLevelType["value"] + '" name="' + data["itemId"] + child.EngLevelType.itemId + '">';
				htmlContent += '			<option value="-1">' + '--'+MsgSet["PLEASE_SELECT"]+'--' + '</option>';
				htmlContent += OPT_ENG;
				htmlContent += '		</select>';
				//----------------------------不知道干嘛用的，这段可删除
				htmlContent += '		<div style="margin-top:-40px;margin-left:256px"><div id="' + data["itemId"] + child.EngLevelType.itemId + 'Tip" class="onCorrect" style="margin: 0px; padding: 0px; background: transparent;">';
				htmlContent += '			<div class="onCorrect">&nbsp;</div></div>';
				htmlContent += '		</div>';
				htmlContent += '	</div>';
				htmlContent += '<div class="clear"><div>';
				htmlContent += '</div>';
				//---------------------------根据考试种类，要显示的不同种类DIV
				//日期框HTML
				var getDateDiv=function(date_id,date_val,date_name,date_title){
					var DATE_DIV='';
					DATE_DIV += '  <div class="input-list" style="display:block"><span class="input-list-info left">Test date：</span>';
					
					DATE_DIV += '     <div class="input-list-text left"> <input id="' + date_id+ '" name="' + date_name+ '" type="text" value="'  +date_val + '"class="inpu-list-text-enter" style="height:36px" readonly="readonly" onclick="this.focus()" title="' +date_name + '">';
					DATE_DIV += '      <img id="' + date_id+ '_Btn" src="' + TzUniversityContextPath + '/statics/images/appeditor/calendar.png" style="position:relative;top:0px;left:-31px;cursor:pointer;"></div>';
					DATE_DIV += ' <div class="clear"> </div>';
					DATE_DIV += '  </div>';
					return DATE_DIV;
				}
				//--GRE日期框HTML
				var GRE_DATE=getDateDiv(data.itemId +child.EngLevelOpt1_1.itemId,child.EngLevelOpt1_1.value,data.itemId+child.EngLevelOpt1_1.name,child.EngLevelOpt1_1.itemName);
				//---GMAT日期HTML
				var GMAT_DATE=getDateDiv(data.itemId +child.EngLevelOpt2_1.itemId,child.EngLevelOpt2_1.value,data.itemId+child.EngLevelOpt2_1.name,child.EngLevelOpt2_1.itemName);
				//---TOFEL日期HTML
				var TOFEL_DATE=getDateDiv(data.itemId +child.EngLevelOpt3_1.itemId,child.EngLevelOpt3_1.value,data.itemId+child.EngLevelOpt3_1.name,child.EngLevelOpt3_1.itemName);
				//---IELTS日期HTML
				var IELTS_DATE=getDateDiv(data.itemId +child.EngLevelOpt4_1.itemId,child.EngLevelOpt4_1.value,data.itemId+child.EngLevelOpt4_1.name,child.EngLevelOpt4_1.itemName);
				//---TOEIC日期HTML
				var TOEIC_DATE=getDateDiv(data.itemId +child.EngLevelOpt13_1.itemId,child.EngLevelOpt13_1.value,data.itemId+child.EngLevelOpt13_1.name,child.EngLevelOpt13_1.itemName);
				//----------------
				
				
				//<!--1.GRE关联DIV-->
				var GRE_DIV='';
				if(EXAM_TYPE_DEF=="ENG_LEV_T1")
					GRE_DIV+='<div id="'+data.itemId+'ENG_LEV_T1" class="input-list" style="display:block">'
				else
				GRE_DIV+='<div id="'+data.itemId+'ENG_LEV_T1" class="input-list" style="display:none">'
					GRE_DIV+=GRE_DATE
					GRE_DIV+='<div class="input-list-info left"><span>Total(Score):</span></div> <div class="input-list-text left"><input id="TZ_4r_gname" class="inpu-list-text-enter" style="height:36px;" id="'+data.itemId+child.EngLevelOpt1_2.itemId+'" value="'+child.EngLevelOpt1_2.value+'"/></div>'
				GRE_DIV+='</div>';
				GRE_DIV+='<div class="clear"></div>'
				htmlContent+=GRE_DIV;
				
				//<!--2.GMAT关联DIV-->
				var GMAT_DIV='';
				if(EXAM_TYPE_DEF=="ENG_LEV_T2")
					GMAT_DIV+='<div id="'+data.itemId+'ENG_LEV_T2" class="input-list" style="display:block">'
				else
				GMAT_DIV+='<div id="'+data.itemId+'ENG_LEV_T2"  class="input-list" style="display:none">'
					GMAT_DIV+=GMAT_DATE
					GMAT_DIV+='<div class="input-list-info left"><span>Total(Score):</span></div><div class="input-list-text left"><input  id="TZ_4r_gname" class="inpu-list-text-enter" style="height:36px;" id="'+data.itemId+child.EngLevelOpt2_2.itemId+'" value="'+child.EngLevelOpt2_2.value+'"/></div>'
				GMAT_DIV+='</div>'
					GMAT_DIV+='<div class="clear"></div>'	
				htmlContent+=GMAT_DIV;
				
				//<!--3.TOFEL关联DIV-->
				var TOFEL_DIV='';
				if(EXAM_TYPE_DEF=="ENG_LEV_T3")
					TOFEL_DIV+='<div id="'+data.itemId+'ENG_LEV_T3" class="input-list" style="display:block">'
				else
				TOFEL_DIV+='<div id="'+data.itemId+'ENG_LEV_T3" class="input-list" style="display:none">'
					TOFEL_DIV+=TOFEL_DATE
					TOFEL_DIV+='<div class="input-list-info left"><span>Total:</span></div><div class="input-list-text left"><input id="TZ_4r_gname" class="inpu-list-text-enter" style="height:36px;" id="'+data.itemId+child.EngLevelOpt3_2.itemId+'" value="'+child.EngLevelOpt3_2.value+'"/></div>'
				TOFEL_DIV+='</div>'	
					TOFEL_DIV+='<div class="clear"></div>'		
				htmlContent+=TOFEL_DIV;
				
				//<!--4.IELTS关联DIV-->
				var IELTS_DIV='';
				if(EXAM_TYPE_DEF=="ENG_LEV_T4")
					IELTS_DIV+='<div id="'+data.itemId+'ENG_LEV_T4" class="input-list" style="display:block">'
				else
				IELTS_DIV+='<div id="'+data.itemId+'ENG_LEV_T4" class="input-list" style="display:none">'
					IELTS_DIV+=IELTS_DATE
					IELTS_DIV+='<div class="input-list-info left"><span>Overall Band Score:</span></div><div class="input-list-text left"><input  id="TZ_4r_gname" class="inpu-list-text-enter" style="height:36px;" id="'+data.itemId+child.EngLevelOpt4_2.itemId+'" value="'+child.EngLevelOpt4_2.value+'"/></div>'
				IELTS_DIV+='</div>'
					IELTS_DIV+='<div class="clear"></div>'
				htmlContent+=IELTS_DIV;
				
				//<!--5.英语六级（710分制）关联DIV-->
				var CET6_DIV='';
				if(EXAM_TYPE_DEF=="ENG_LEV_T5")
					CET6_DIV+='<div id="'+data.itemId+'ENG_LEV_T5" class="input-list" style="display:block">'
				else	
				CET6_DIV+='<div id="'+data.itemId+'ENG_LEV_T5" class="input-list" style="display:none">'
					CET6_DIV+='<span class="input-list-info left">总分:</span><div class="input-list-text left"><input id="TZ_4r_gname" class="inpu-list-text-enter" style="height:36px;" id="'+data["itemId"] + child.EngLevelOpt5_1.itemId+'" value="'+child.EngLevelOpt5_1.value+'"/></div>'
				CET6_DIV+='</div>'
					CET6_DIV+='<div class="clear"></div>'
				htmlContent+=CET6_DIV;
				
				//<!--6.英语四级（710分制）关联DIV-->
				var CET4_DIV='';
				if(EXAM_TYPE_DEF=="ENG_LEV_T6")
					CET4_DIV+='<div id="'+data.itemId+'ENG_LEV_T6" class="input-list" style="display:block">'
				else
				CET4_DIV+='<div id="'+data.itemId+'ENG_LEV_T6" class="input-list" style="display:none">'
					CET4_DIV+='<span class="input-list-info left">总分:</span><div class="input-list-text left"><input   id="TZ_4r_gname" class="inpu-list-text-enter" style="height:36px;" id="'+data["itemId"] + child.EngLevelOpt6_1.itemId+'" value="'+child.EngLevelOpt6_1.value+'"/></div>'
					CET4_DIV+='<div class="clear"></div>'
				CET4_DIV+='</div>'
					CET4_DIV+='<div class="clear"></div>'	
				htmlContent+=CET4_DIV;
				
				//<!--7.英语六级（100分制）关联DIV-->
				var CET6_DIV2='';
				if(EXAM_TYPE_DEF=="ENG_LEV_T7")
					CET6_DIV2+='<div id="'+data.itemId+'ENG_LEV_T7" class="input-list" style="display:block">'
				else
				CET6_DIV2+='<div id="'+data.itemId+'ENG_LEV_T7" class="input-list" style="display:none">'
					CET6_DIV2+='<div class="input-list-info left"><span >是否通过:</span></div>'
					CET6_DIV2+='<div class="input-list-text left" ><select style="width:100%;height:36px" id="'+data["itemId"] + child.EngLevelOpt7_1.itemId+'" value="'+child.EngLevelOpt7_1.value+'">'
						CET6_DIV2+='<option value="Y">通过</option>'
						CET6_DIV2+='<option value="N">未通过</option>'
					CET6_DIV2+='</select></div>'
				CET6_DIV2+='</div>'
					CET6_DIV2+='<div class="clear"></div>'
				htmlContent+=CET6_DIV2;
				
				//<!--8.英语四级（100分制）关联DIV-->
				var CET4_DIV2='';
				if(EXAM_TYPE_DEF=="ENG_LEV_T8")
					CET4_DIV2+='<div id="'+data.itemId+'ENG_LEV_T8" class="input-list" style="display:block">'
				else
				CET4_DIV2+='<div id="'+data.itemId+'ENG_LEV_T8" class="input-list" style="display:none">'
					CET4_DIV2+='<div class="input-list-info left"><span>是否通过:</span></div>'
					CET4_DIV2+='<div class="input-list-text left" ><select style="width:100%;height:36px" id="'+data["itemId"] + child.EngLevelOpt8_1.itemId+'" value="'+child.EngLevelOpt8_1.value+'">'
						CET4_DIV2+='<option value="Y">通过</option>'
						CET4_DIV2+='<option value="N">未通过</option>'
					CET4_DIV2+='</select></div>'
				CET4_DIV2+='</div>'
					CET4_DIV2+='<div class="clear"></div>'
				htmlContent+=CET4_DIV2;
				
				//<!--9.专业英语关联DIV-->
				var TEM_DIV=''
				if(EXAM_TYPE_DEF=="ENG_LEV_T9")
					TEM_DIV+='<div id="'+data.itemId+'ENG_LEV_T9" class="input-list" style="display:block">'
				else
				TEM_DIV+='<div id="'+data.itemId+'ENG_LEV_T9" class="input-list" style="display:none">'
					TEM_DIV+='<div class="input-list-info left"><span>总分(成绩):</span></div>'
					TEM_DIV+='<div class="input-list-text left" ><select style="width:100%;height:36px" id="'+data["itemId"] + child.EngLevelOpt9_1.itemId+'" value="'+child.EngLevelOpt9_1.value+'">'
						TEM_DIV+='<option value="TEM4">专业四级</option>'
						TEM_DIV+='<option value="TEM8">专业八级</option>'
					TEM_DIV+='</select></div>'
				TEM_DIV+='</div>'
					TEM_DIV+='<div class="clear"></div>'
				htmlContent+=TEM_DIV;
				
				//<!--10.高级口译关联DIV-->
				var H_INTER_DIV='';
				if(EXAM_TYPE_DEF=="ENG_LEV_T10")
					H_INTER_DIV+='<div id="'+data.itemId+'ENG_LEV_T10" class="input-list" style="display:block">'
				else
				H_INTER_DIV+='<div id="'+data.itemId+'ENG_LEV_T10" class="input-list" style="display:none">'
					H_INTER_DIV+='<div class="input-list-info left"><span>总分(成绩):</span></div>'
					H_INTER_DIV+='<div class="input-list-text left" ><select style="width:100%;height:36px" id="'+data["itemId"] + child.EngLevelOpt10_1.itemId+'" value="'+child.EngLevelOpt10_1.value+'">'
						H_INTER_DIV+='<option value="A">拿到资格证书</option>'
						H_INTER_DIV+='<option value="B">笔试证书</option>'
					H_INTER_DIV+='</select></div>'
				H_INTER_DIV+='</div>'
					H_INTER_DIV+='<div class="clear"></div>'
				htmlContent+=H_INTER_DIV;
				
				//<!--11.中级口译关联DIV-->
				var M_INTER_DIV='';
				if(EXAM_TYPE_DEF=="ENG_LEV_T11")
					M_INTER_DIV+='<div id="'+data.itemId+'ENG_LEV_T11" class="input-list" style="display:block">'
				else
				M_INTER_DIV+='<div id="'+data.itemId+'ENG_LEV_T11" class="input-list" style="display:none">'
					M_INTER_DIV+='<div class="input-list-info left"><span>总分(成绩):</span></div>'
					M_INTER_DIV+='<div class="input-list-text left" ><select style="width:100%;height:36px" id="'+data["itemId"] + child.EngLevelOpt11_1.itemId+'" value="'+child.EngLevelOpt11_1.value+'">'
						M_INTER_DIV+='<option value="A">拿到资格证书</option>'
						M_INTER_DIV+='<option value="B">笔试证书</option>'
					M_INTER_DIV+='</select></div>'
				M_INTER_DIV+='</div>'
					M_INTER_DIV+='<div class="clear"></div>'
				htmlContent+=M_INTER_DIV;
				
				//<!--12.BEC关联DIV-->
				var BEC_DIV='';
				if(EXAM_TYPE_DEF=="ENG_LEV_T12")
					BEC_DIV+='<div id="'+data.itemId+'ENG_LEV_T12" class="input-list" style="display:block">'
				else
				BEC_DIV+='<div id="'+data.itemId+'ENG_LEV_T12" class="input-list" style="display:none">'
					BEC_DIV+='<div class="input-list-info left"><span>总分(成绩):</span></div>'
					BEC_DIV+='<div class="input-list-text left" ><select style="width:100%;height:36px" id="'+data["itemId"] + child.EngLevelOpt12_1.itemId+'" value="'+child.EngLevelOpt12_1.value+'">'
						BEC_DIV+='<option value="A">高级</option>'
						BEC_DIV+='<option value="B">中级</option>'
						BEC_DIV+='<option value="C">初级</option>'
					BEC_DIV+='</select></div>'
				BEC_DIV+='</div>'
					BEC_DIV+='<div class="clear"></div>'
				htmlContent+=BEC_DIV;
				
				//<!--13.TOEIC990-->
				var TOEIC_DIV='';
				if(EXAM_TYPE_DEF=="ENG_LEV_T13")
					TOEIC_DIV+='<div id="'+data.itemId+'ENG_LEV_T13" class="input-list" style="display:block">'
				else
				TOEIC_DIV+='<div id="'+data.itemId+'ENG_LEV_T13" class="input-list" style="display:none">'
					TOEIC_DIV+=TOEIC_DATE
					TOEIC_DIV+='<div class="input-list-info left"><span>总分:</span></div><div  class="input-list-text left"><input id="TZ_4r_gname" class="inpu-list-text-enter" style="height:36px;" id="'+data["itemId"] + child.EngLevelOpt13_2.itemId+'" value="'+child.EngLevelOpt13_1.value+'"/></div>'
				TOEIC_DIV+='</div>'	
					TOEIC_DIV+='<div class="clear"></div>'	
				htmlContent+=TOEIC_DIV;
				
				//<!--通用上传控件部分-->
				if(EXAM_TYPE_DEF!=""&&EXAM_TYPE_DEF!="-1")
					htmlContent+='<div id="'+data.itemId+'UP" class="input-list" style="display:block">'
				else
				htmlContent+='<div id="'+data.itemId+'UP"  class="input-list" style="display:none">'
					htmlContent+='<div class="input-list-info-readonly left"><span >上传证书/成绩扫描件</span></div><div class="input-list-wz-readonly left" ><input type="file" id="'+data["itemId"] + child.EngLevelUp.itemId+'"/></div>'
					htmlContent+='<div class="clear"></div>'
				htmlContent+='</div>'
				//---------------------------
				
			}
		
		
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
				var child=data["children"][0];
				if (child == undefined) {
			   		 child=data["children"];
			   	 	}
				var type_select=$("#"+ data["itemId"] + child.EngLevelType.itemId);
				type_select.on("change",function(){
					for(var i in EXAM_TYPE_MAP){
						if($(this).val()==i){
							$("#"+data.itemId+i).css("display","block");
						}else{
							$("#"+data.itemId+i).css("display","none");
						}
					}
					if($(this).val()=="-1")
						$("#"+data.itemId+"UP").css("display","none");
					else
						$("#"+data.itemId+"UP").css("display","block");
					//---清理数据
				});
				
				//日期控件处理1.2.3.4.13
				var id_gp=[1,2,3,4,13];
				for(var i=0;i<id_gp.length;i++){
					   var EngLevelOpt="EngLevelOpt"+id_gp[i]+"_1";
					   var $inputBox = $("#" + data.itemId +child[EngLevelOpt].itemId);
					   var $selectBtn = $("#" + data.itemId +child[EngLevelOpt].itemId + "_Btn");
					   $inputBox.datepicker({
							showButtonPanel:true,
							changeMonth: true,
							changeYear: true,
							yearRange: "1960:2030",
							dateFormat:"yy-mm-dd",
							onClose:function(dateText, inst){
								$(this).trigger("blur");
							}
						});

						$selectBtn.click(function() {
							$inputBox.click();
						});
				}
				
				
		       
	}
})