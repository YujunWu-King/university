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
		//console.dir(data);
		var htmlContent="";
		//--考试种类初始值
		//----容器:
		//var xx=1;
		//for (var x in childList) {
			//htmlContent+='<div class="clear"></div>'
			htmlContent += '<div id="main_inner_content_para'+index+'" class="main_inner_content_para">';
			htmlContent+='<div class="clear"></div>'
			htmlContent += '<div class="mainright-title">';
				htmlContent += '<span class="title-line"></span>' + "英语水平"  + ' ' +index+ ' :'+ data.title + ' :</div>';
			htmlContent += '<div class="mainright-box pos-rela">';
			//-----
			if(index > 1&&!SurveyBuild._readonly){
				htmlContent += '		<div onclick="SurveyBuild.deleteTjx(this);" class="input-delbtn">' + MsgSet["DEL"] + '&nbsp;&nbsp;<span class="input-btn-icon"><img src="' + TzUniversityContextPath + '/statics/images/appeditor/new/add-delete.png"></span></div>';
			}
			//--------------
			//works += '<span class="title-line"></span>' + MsgSet["REFFER"] + ' ' +rownum+ ' :' + data.title + '</div>';
			//-------------
			var child=childList[x];
			var EXAM_TYPE_DEF=child.EngLevelType.value;
			//-----
			//1.公司性质
			if (SurveyBuild._readonly) {
				//只读模式
				//----------------------------考试种类OPT
				//------只读数据：
				var varDesc="";	
				var OPT_ENG='';
				for(var i in EXAM_TYPE_MAP){
					OPT_ENG+='<option value="'+i+'"'+(EXAM_TYPE_DEF==i?'selected="selected"': '')+'>'+EXAM_TYPE_MAP[i]+'</option>'
					if(EXAM_TYPE_DEF==i){
						varDesc=EXAM_TYPE_MAP[i];
					}
				}
				//----------------------------放入考试种类OPT
				htmlContent += '<div  class="input-list" style="display:block">';
				htmlContent += '	<div  class="input-list-info left"><span class="red-star">*</span>' + child.EngLevelType.itemName + '：</div>';
				htmlContent += '	<div class="input-list-text left input-edu-select">';
				htmlContent += varDesc;
				htmlContent += '	</div>';
				htmlContent += '</div>';
				//---------------------------根据考试种类，要显示的不同种类DIV
				//日期框HTML
				var getDateDiv=function(date_id,date_val,date_name,date_title){
					var DATE_DIV='';
					DATE_DIV += '  <div class="input-list" style="display:block"><div class="input-list-info left"><span class="red-star">*</span><span>Test date：</span></div>';
					
					DATE_DIV += '     <div class="input-list-text left">';
					DATE_DIV +=date_val;
					DATE_DIV += '	  </div>'
					//DATE_DIV += ' <div class="clear"> </div>';
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
				var greDesc='';
				htmlContent+='<div class="clear"></div>'
				htmlContent+='<div name="relatedDiv">'	
				if(EXAM_TYPE_DEF=="ENG_LEV_T1"){
					GRE_DIV+='<div name="'+data.itemId+'ENG_LEV_T1" id="'+data.itemId+'ENG_LEV_T1" class="input-list" style="display:block">'
					greDesc=child.EngLevelOpt1_2.value;
				}
				else
				GRE_DIV+='<div name="'+data.itemId+'ENG_LEV_T1" id="'+data.itemId+'ENG_LEV_T1" class="input-list" style="display:none">'
					GRE_DIV+=GRE_DATE
					GRE_DIV+='<div class="clear"></div>'
					GRE_DIV+='<div class="input-list-info left"><span class="red-star">*</span><span>Total(Score)：</span></div> '
					GRE_DIV+='<div class="input-list-text left">'
						GRE_DIV+=greDesc;
					GRE_DIV+='</div>'
				GRE_DIV+='</div>';
				htmlContent+=GRE_DIV;
				
				//<!--2.GMAT关联DIV-->
				var GMAT_DIV='';
				var gmatDesc='';
				if(EXAM_TYPE_DEF=="ENG_LEV_T2"){
					GMAT_DIV+='<div name="'+data.itemId+'ENG_LEV_T2" id="'+data.itemId+'ENG_LEV_T2" class="input-list" style="display:block">'
					gmatDesc=child.EngLevelOpt2_2.value;
				}
				else
				GMAT_DIV+='<div name="'+data.itemId+'ENG_LEV_T2" id="'+data.itemId+'ENG_LEV_T2"  class="input-list" style="display:none">'
					GMAT_DIV+=GMAT_DATE
					GMAT_DIV+='<div class="clear"></div>'
					GMAT_DIV+='<div class="input-list-info left"><span class="red-star">*</span><span>Total(Score)：</span></div>'
					GMAT_DIV+='<div class="input-list-text left">'
						GMAT_DIV+=gmatDesc;
					GMAT_DIV+='</div>'				
				GMAT_DIV+='</div>'
					//GMAT_DIV+='<div class="clear"></div>'	
				htmlContent+=GMAT_DIV;
				
				//<!--3.TOFEL关联DIV-->
				var TOFEL_DIV='';
				var tofelDesc='';
				if(EXAM_TYPE_DEF=="ENG_LEV_T3"){
					TOFEL_DIV+='<div name="'+data.itemId+'ENG_LEV_T3" id="'+data.itemId+'ENG_LEV_T3" class="input-list" style="display:block">'
					tofelDesc=child.EngLevelOpt3_2.value;
				}
				else
				TOFEL_DIV+='<div name="'+data.itemId+'ENG_LEV_T3" id="'+data.itemId+'ENG_LEV_T3" class="input-list" style="display:none">'
					TOFEL_DIV+=TOFEL_DATE
					TOFEL_DIV+='<div class="clear"></div>'
					TOFEL_DIV+='<div class="input-list-info left"><span class="red-star">*</span><span>Total：</span></div>'
					TOFEL_DIV+='<div class="input-list-text left">'
						TOFEL_DIV+=tofelDesc;
					TOFEL_DIV+='</div>'	
				TOFEL_DIV+='</div>'	
				htmlContent+=TOFEL_DIV;
				
				//<!--4.IELTS关联DIV-->
				var IELTS_DIV='';
				var ieltsDesc='';
				if(EXAM_TYPE_DEF=="ENG_LEV_T4"){
					IELTS_DIV+='<div name="'+data.itemId+'ENG_LEV_T4" id="'+data.itemId+'ENG_LEV_T4" class="input-list" style="display:block">'
					ieltsDesc=child.EngLevelOpt4_2.value;
				}
				else
				IELTS_DIV+='<div name="'+data.itemId+'ENG_LEV_T4" id="'+data.itemId+'ENG_LEV_T4" class="input-list" style="display:none">'
					IELTS_DIV+=IELTS_DATE
					IELTS_DIV+='<div class="clear"></div>'
					IELTS_DIV+='<div class="input-list-info left"><span class="red-star">*</span><span>Overall Band Score:</span></div>'
					IELTS_DIV+='<div class="input-list-text left">'
						IELTS_DIV+=ieltsDesc;
					IELTS_DIV+='</div>'
				IELTS_DIV+='</div>'
					//IELTS_DIV+='<div class="clear"></div>'
				htmlContent+=IELTS_DIV;
				
				//<!--5.英语六级（710分制）关联DIV-->
				var CET6_DIV='';
				var cet6Desc='';
				if(EXAM_TYPE_DEF=="ENG_LEV_T5"){
					CET6_DIV+='<div name="'+data.itemId+'ENG_LEV_T5" id="'+data.itemId+'ENG_LEV_T5" class="input-list" style="display:block">'
					cet6Desc= child.EngLevelOpt5_1.value;
				}
				else	
				CET6_DIV+='<div name="'+data.itemId+'ENG_LEV_T5" id="'+data.itemId+'ENG_LEV_T5" class="input-list" style="display:none">'
					CET6_DIV+='<div class="input-list-info left"><span class="red-star">*</span><span>总分:</span></div>'
					CET6_DIV+='<div class="input-list-text left">'
						CET6_DIV+=cet6Desc;
					CET6_DIV+='</div>'
				CET6_DIV+='</div>'
				htmlContent+=CET6_DIV;
				
				//<!--6.英语四级（710分制）关联DIV-->
				var CET4_DIV='';
				var cet4Desc='';
				if(EXAM_TYPE_DEF=="ENG_LEV_T6"){
					CET4_DIV+='<div name="'+data.itemId+'ENG_LEV_T6" id="'+data.itemId+'ENG_LEV_T6" class="input-list" style="display:block">'
					cet4Desc=child.EngLevelOpt6_1.value;
				}
				else
				CET4_DIV+='<div name="'+data.itemId+'ENG_LEV_T6" id="'+data.itemId+'ENG_LEV_T6" class="input-list" style="display:none">'
					CET4_DIV+='<div class="input-list-info left"><span class="red-star">*</span><span>总分:</span></div>'
					CET4_DIV+='<div class="input-list-text left">'
						CET4_DIV+=cet4Desc;
					CET4_DIV+='</div>'	
				CET4_DIV+='</div>'
				htmlContent+=CET4_DIV;
				
				//<!--7.英语六级（100分制）关联DIV-->
				var CET6_DIV2='';
				var cet6Desc2='';
				if(EXAM_TYPE_DEF=="ENG_LEV_T7"){
					CET6_DIV2+='<div name="'+data.itemId+'ENG_LEV_T7" id="'+data.itemId+'ENG_LEV_T7" class="input-list" style="display:block">'
					if(child.EngLevelOpt7_1.value=="Y")
						cet6Desc2="通过";
					else 
						cet6Desc2="未通过";
				}
				else
				CET6_DIV2+='<div name="'+data.itemId+'ENG_LEV_T7" id="'+data.itemId+'ENG_LEV_T7" class="input-list" style="display:none">'
					CET6_DIV2+='<div class="input-list-info left"><span class="red-star">*</span><span >是否通过:</span></div>'
					CET6_DIV2+='<div class="input-list-text left" >'
						CET6_DIV2+=cet6Desc2;
					CET6_DIV2+='</div>'
				CET6_DIV2+='</div>'
					//CET6_DIV2+='<div class="clear"></div>'
				htmlContent+=CET6_DIV2;
				
				//<!--8.英语四级（100分制）关联DIV-->
				var CET4_DIV2='';
				var cetDesc2='';
				if(EXAM_TYPE_DEF=="ENG_LEV_T8"){
					CET4_DIV2+='<div name="'+data.itemId+'ENG_LEV_T8" id="'+data.itemId+'ENG_LEV_T8" class="input-list" style="display:block">'
					if(child.EngLevelOpt8_1.value=="Y")
						cetDesc2="通过";
					else
						cetDesc2="未通过";
				}
				else
				CET4_DIV2+='<div name="'+data.itemId+'ENG_LEV_T8" id="'+data.itemId+'ENG_LEV_T8" class="input-list" style="display:none">'
					CET4_DIV2+='<div class="input-list-info left"><span class="red-star">*</span><span>是否通过:</span></div>'
					CET4_DIV2+='<div class="input-list-text left" >'
						CET4_DIV2+=cetDesc2;
					CET4_DIV2+='</div>'
				CET4_DIV2+='</div>'
				htmlContent+=CET4_DIV2;
				
				//<!--9.专业英语关联DIV-->
				var TEM_DIV=''
				var temDesc='';
				if(EXAM_TYPE_DEF=="ENG_LEV_T9"){
					TEM_DIV+='<div name="'+data.itemId+'ENG_LEV_T9" id="'+data.itemId+'ENG_LEV_T9" class="input-list" style="display:block">'
					if(child.EngLevelOpt9_1.value=="TEM4")
						temDesc="专业四级";
					else
						temDesc="专业八级";
				}
				else
				TEM_DIV+='<div name="'+data.itemId+'ENG_LEV_T9" id="'+data.itemId+'ENG_LEV_T9" class="input-list" style="display:none">'
					TEM_DIV+='<div class="input-list-info left"><span class="red-star">*</span><span>总分(成绩):</span></div>'
					TEM_DIV+='<div class="input-list-text left" >'
						TEM_DIV+=temDesc;
					TEM_DIV+='</div>'
				TEM_DIV+='</div>'
				htmlContent+=TEM_DIV;
				
				//<!--10.高级口译关联DIV-->
				var H_INTER_DIV='';
				var hInterDesc='';
				if(EXAM_TYPE_DEF=="ENG_LEV_T10"){
					H_INTER_DIV+='<div name="'+data.itemId+'ENG_LEV_T10" id="'+data.itemId+'ENG_LEV_T10" class="input-list" style="display:block">'
					if(child.EngLevelOpt10_1.value=="A")
						hInterDesc="拿到资格证书";
					else
						hInterDesc="笔试证书";
				}
				else
				H_INTER_DIV+='<div name="'+data.itemId+'ENG_LEV_T10" id="'+data.itemId+'ENG_LEV_T10" class="input-list" style="display:none">'
					H_INTER_DIV+='<div class="input-list-info left"><span class="red-star">*</span><span>总分(成绩):</span></div>'
					H_INTER_DIV+='<div class="input-list-text left" >'
						H_INTER_DIV+=hInterDesc;
					H_INTER_DIV+='</div>'
				H_INTER_DIV+='</div>'
				htmlContent+=H_INTER_DIV;
				
				//<!--11.中级口译关联DIV-->
				var M_INTER_DIV='';
				var mInterDesc='';
				if(EXAM_TYPE_DEF=="ENG_LEV_T11"){
					M_INTER_DIV+='<div name="'+data.itemId+'ENG_LEV_T11" id="'+data.itemId+'ENG_LEV_T11" class="input-list" style="display:block">'
					if(child.EngLevelOpt11_1.value=="A")
						mInterDesc="拿到资格证书";
					else 
						mInterDesc="笔试证书";
				}
				else
				M_INTER_DIV+='<div name="'+data.itemId+'ENG_LEV_T11" id="'+data.itemId+'ENG_LEV_T11" class="input-list" style="display:none">'
					M_INTER_DIV+='<div class="input-list-info left"><span class="red-star">*</span><span>总分(成绩):</span></div>'
					M_INTER_DIV+='<div class="input-list-text left" >'
						M_INTER_DIV+=mInterDesc;
					M_INTER_DIV+='</div>'
				M_INTER_DIV+='</div>'
				htmlContent+=M_INTER_DIV;
				
				//<!--12.BEC关联DIV-->
				var BEC_DIV='';
				var becDesc='';
				if(EXAM_TYPE_DEF=="ENG_LEV_T12"){
					BEC_DIV+='<div name="'+data.itemId+'ENG_LEV_T12" id="'+data.itemId+'ENG_LEV_T12" class="input-list" style="display:block">'
					if(child.EngLevelOpt12_1.value=="A")
						becDesc="高级";
					else if(child.EngLevelOpt12_1.value=="B")
						becDesc="中级";
					else
						becDesc="初级";
				}
				else
				BEC_DIV+='<div name="'+data.itemId+'ENG_LEV_T12" id="'+data.itemId+'ENG_LEV_T12" class="input-list" style="display:none">'
					BEC_DIV+='<div class="input-list-info left"><span class="red-star">*</span><span>总分(成绩):</span></div>'
					BEC_DIV+='<div class="input-list-text left" >'
						BEC_DIV+=becDesc;
					BEC_DIV+='</div>'
				BEC_DIV+='</div>'
				htmlContent+=BEC_DIV;
				
				//<!--13.TOEIC990-->
				var TOEIC_DIV='';
				var toeicDesc='';
				if(EXAM_TYPE_DEF=="ENG_LEV_T13"){
					TOEIC_DIV+='<div name="'+data.itemId+'ENG_LEV_T13" id="'+data.itemId+'ENG_LEV_T13" class="input-list" style="display:block">'
					toeicDesc=child.EngLevelOpt13_2.value;
				}
				else
				TOEIC_DIV+='<div name="'+data.itemId+'ENG_LEV_T13" id="'+data.itemId+'ENG_LEV_T13" class="input-list" style="display:none">'
					TOEIC_DIV+=TOEIC_DATE
					TOEIC_DIV+='<div class="clear"></div>'
					TOEIC_DIV+='<div class="input-list-info left"><span class="red-star">*</span><span>总分:</span></div>'
					TOEIC_DIV+='<div  class="input-list-text left">'
						TOEIC_DIV+=toeicDesc;
					TOEIC_DIV+="</div>";
				TOEIC_DIV+='</div>'	
				htmlContent+=TOEIC_DIV;
				
				//<!--通用上传控件部分-->
					htmlContent+='<div name="'+data.itemId+'UP" id="'+data.itemId+'UP" class="input-list" style="display:block">'
					htmlContent+='<div class="clear"></div>';
					htmlContent+='<div class="input-list-info left"><span >证书/成绩扫描件:</span></div>'
					//var filename = child.EngLevelUp.filename;
					//htmlContent += '<div class="main_inner_content_info_text"><a id="'+data["itemId"]+child.EngLevelUp["itemId"]+'Attch" class="fancybox" href="' +child.EngLevelUp.accessPath + child.EngLevelUp.sysFileName + '" target="_blank">' + (filename ? filename.substring(0,20) + "..." : "") + '</a></div>';
					//htmlContent += '<input id="'+data["itemId"]+child.EngLevelUp.itemId+'Attch" type="hidden" name="'+data["itemId"]+child.EngLevelUp.itemId+'Attch" value="'+child.EngLevelUp.itemId+'"></div>';
				//----------------------------
					var childrenAttr=child.EngLevelUp.children;
					htmlContent+= '	<div class="input-list-upload left">';
				        htmlContent+= '		<div class="input-list-upload-con" id="' + child.EngLevelUp.itemId+ '_AttList" style="display:' + (childrenAttr.length < 1 ? 'none':'black') + '">';
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
			        	htmlContent+= '		</div>';
			        	htmlContent+= '	</div>';
			        	//将上传图片显示放到上传部分的div类
			        	htmlContent+='</div>'
				//---------------------------
				//加入clear之后结构被破坏，所以在clear下加入一层IDV	
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
					OPT_ENG+='<option value="'+i+'"'+(EXAM_TYPE_DEF==i?'selected="selected"': '')+'>'+EXAM_TYPE_MAP[i]+'</option>'
				}
				//----------------------------放入考试种类OPT
				htmlContent += '<div  class="input-list" style="display:block">';
				htmlContent += '	<div  class="input-list-info left"><span class="red-star">*</span>' + child.EngLevelType.itemName + '：</div>';
				htmlContent += '	<div class="input-list-text left input-edu-select">';
				htmlContent += '		<select id="' + data["itemId"] + child.EngLevelType.itemId + '" class="chosen-select" style="width:100%;" data-regular="" title="' + child.EngLevelType.itemName + '" value="' + child.EngLevelType["value"] + '" name="' + data["itemId"] + child.EngLevelType.itemId + '">';
				htmlContent += '			<option value="-1">' + '--'+MsgSet["PLEASE_SELECT"]+'--' + '</option>';
				htmlContent += OPT_ENG;
				htmlContent += '		</select>';
				//----------------------------非空验证DIV
				htmlContent += '		<div style="margin-top:-40px;margin-left:330px"><div id="' + data["itemId"] + child.EngLevelType.itemId + 'Tip" class="onCorrect" style="margin: 0px; padding: 0px; background: transparent;">';
				htmlContent += '			<div class="onCorrect">&nbsp;</div></div>';
				htmlContent += '		</div>';
				htmlContent += '	</div>';
				htmlContent += '<div class="changedDiv"></div>';
				htmlContent += '</div>';
				//---------------------------根据考试种类，要显示的不同种类DIV
				//日期框HTML
				var getDateDiv=function(date_id,date_val,date_name,date_title){
					var DATE_DIV='';
					DATE_DIV += '  <div class="input-list" style="display:block"><span class="input-list-info left"><span class="red-star">*</span>Test date：</span>';
					
					DATE_DIV += '     <div class="input-list-text left"> <input id="' + date_id+ '" name="' + date_name+ '" type="text" value="'  +date_val + '"class="inpu-list-text-enter" style="height:36px" readonly="readonly" onclick="this.focus()" title="' +date_name + '">';
					DATE_DIV += '      <img id="' + date_id+ '_Btn" src="' + TzUniversityContextPath + '/statics/images/appeditor/new/calendar.png" style="position:relative;top:5px;left:-31px;cursor:pointer;">';
					//DATE_DIV += ' <div class="clear"> </div>';
					//---------日期 input格式检验:
					DATE_DIV += '<div style="margin-top: -40px; margin-left: 330px">';
					DATE_DIV += '	<div id="' + date_id + 'Tip" class="onShow" style="margin: 0px; padding: 0px; background: transparent;">';
					DATE_DIV += '		<div class="onShow">test</div>';
					DATE_DIV += '	</div>';
					DATE_DIV += '</div>';
					//---------
					DATE_DIV += ' </div></div>';
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
				htmlContent+='<div class="clear"></div>'
				htmlContent+='<div name="relatedDiv">'	
				if(EXAM_TYPE_DEF=="ENG_LEV_T1")
					GRE_DIV+='<div name="'+data.itemId+'ENG_LEV_T1" id="'+data.itemId+'ENG_LEV_T1" class="input-list" style="display:block">'
				else
				GRE_DIV+='<div name="'+data.itemId+'ENG_LEV_T1" id="'+data.itemId+'ENG_LEV_T1" class="input-list" style="display:none">'
					GRE_DIV+=GRE_DATE
					GRE_DIV+='<div class="input-list-info left"><span class="red-star">*</span><span>Total(Score)：</span></div> <div class="input-list-text left"><input class="inpu-list-text-enter" style="height:36px;" id="'+data.itemId+child.EngLevelOpt1_2.itemId+'" value="'+child.EngLevelOpt1_2.value+'"/>'
					//---------GRE input格式检验:
					GRE_DIV += '<div style="margin-top: -40px; margin-left: 330px">';
					GRE_DIV += '	<div id="' + data.itemId+child.EngLevelOpt1_2.itemId + 'Tip" class="onShow" style="margin: 0px; padding: 0px; background: transparent;">';
					GRE_DIV += '		<div class="onShow">test</div>';
					GRE_DIV += '	</div>';
					GRE_DIV += '</div>';
					//---------
				GRE_DIV+='</div></div>';
				htmlContent+=GRE_DIV;

				//<!--2.GMAT关联DIV-->
				var GMAT_DIV='';
				if(EXAM_TYPE_DEF=="ENG_LEV_T2")
					GMAT_DIV+='<div name="'+data.itemId+'ENG_LEV_T2" id="'+data.itemId+'ENG_LEV_T2" class="input-list" style="display:block">'
				else
				GMAT_DIV+='<div name="'+data.itemId+'ENG_LEV_T2" id="'+data.itemId+'ENG_LEV_T2"  class="input-list" style="display:none">'
					GMAT_DIV+=GMAT_DATE
					GMAT_DIV+='<div class="input-list-info left"><span class="red-star">*</span><span>Total(Score)：</span></div><div class="input-list-text left"><input  class="inpu-list-text-enter" style="height:36px;" id="'+data.itemId+child.EngLevelOpt2_2.itemId+'" value="'+child.EngLevelOpt2_2.value+'"/>'
					//---------GMAT  input格式检验:
					GMAT_DIV += '<div style="margin-top: -40px; margin-left: 330px">';
					GMAT_DIV += '	<div id="' + data.itemId+child.EngLevelOpt2_2.itemId + 'Tip" class="onShow" style="margin: 0px; padding: 0px; background: transparent;">';
					GMAT_DIV += '		<div class="onShow">test</div>';
					GMAT_DIV += '	</div>';
					GMAT_DIV += '</div>';
					//---------
				GMAT_DIV+='</div></div>'
					//GMAT_DIV+='<div class="clear"></div>'	
				htmlContent+=GMAT_DIV;
				
				//<!--3.TOFEL关联DIV-->
				var TOFEL_DIV='';
				if(EXAM_TYPE_DEF=="ENG_LEV_T3")
					TOFEL_DIV+='<div name="'+data.itemId+'ENG_LEV_T3" id="'+data.itemId+'ENG_LEV_T3" class="input-list" style="display:block">'
				else
				TOFEL_DIV+='<div name="'+data.itemId+'ENG_LEV_T3" id="'+data.itemId+'ENG_LEV_T3" class="input-list" style="display:none">'
					TOFEL_DIV+=TOFEL_DATE
					TOFEL_DIV+='<div class="input-list-info left"><span class="red-star">*</span><span>Total：</span></div><div class="input-list-text left"><input  class="inpu-list-text-enter" style="height:36px;" id="'+data.itemId+child.EngLevelOpt3_2.itemId+'" value="'+child.EngLevelOpt3_2.value+'"/>'
					//---------TOFEL_DIV  input格式检验:
					TOFEL_DIV += '<div style="margin-top: -40px; margin-left: 330px">';
					TOFEL_DIV += '	<div id="' + data.itemId+child.EngLevelOpt3_2.itemId + 'Tip" class="onShow" style="margin: 0px; padding: 0px; background: transparent;">';
					TOFEL_DIV += '		<div class="onShow">test</div>';
					TOFEL_DIV += '	</div>';
					TOFEL_DIV += '</div>';
					//---------
				TOFEL_DIV+='</div></div>'	
					//TOFEL_DIV+='<div class="clear"></div>'		
				htmlContent+=TOFEL_DIV;
				
				//<!--4.IELTS关联DIV-->
				var IELTS_DIV='';
				if(EXAM_TYPE_DEF=="ENG_LEV_T4")
					IELTS_DIV+='<div name="'+data.itemId+'ENG_LEV_T4" id="'+data.itemId+'ENG_LEV_T4" class="input-list" style="display:block">'
				else
				IELTS_DIV+='<div name="'+data.itemId+'ENG_LEV_T4" id="'+data.itemId+'ENG_LEV_T4" class="input-list" style="display:none">'
					IELTS_DIV+=IELTS_DATE
					IELTS_DIV+='<div class="input-list-info left"><span class="red-star">*</span><span>Overall Band Score：</span></div><div class="input-list-text left"><input   class="inpu-list-text-enter" style="height:36px;" id="'+data.itemId+child.EngLevelOpt4_2.itemId+'" value="'+child.EngLevelOpt4_2.value+'"/>'
					//---------IELTS_DIV  input格式检验:
					IELTS_DIV += '<div style="margin-top: -40px; margin-left: 330px">';
					IELTS_DIV += '	<div id="' + data.itemId+child.EngLevelOpt4_2.itemId + 'Tip" class="onShow" style="margin: 0px; padding: 0px; background: transparent;">';
					IELTS_DIV += '		<div class="onShow">test</div>';
					IELTS_DIV += '	</div>';
					IELTS_DIV += '</div>';
					//---------
				IELTS_DIV+='</div></div>'
					//IELTS_DIV+='<div class="clear"></div>'
				htmlContent+=IELTS_DIV;
				
				//<!--5.英语六级（710分制）关联DIV-->
				var CET6_DIV='';
				if(EXAM_TYPE_DEF=="ENG_LEV_T5")
					CET6_DIV+='<div name="'+data.itemId+'ENG_LEV_T5" id="'+data.itemId+'ENG_LEV_T5" class="input-list" style="display:block">'
				else	
				CET6_DIV+='<div name="'+data.itemId+'ENG_LEV_T5" id="'+data.itemId+'ENG_LEV_T5" class="input-list" style="display:none">'
					CET6_DIV+='<div class="input-list-info left"><span class="red-star">*</span><span>总分：</span></div><div class="input-list-text left"><input  class="inpu-list-text-enter" style="height:36px;" id="'+data["itemId"] + child.EngLevelOpt5_1.itemId+'" value="'+child.EngLevelOpt5_1.value+'"/>'
					//---------CET6_DIV  input格式检验:
					CET6_DIV += '<div style="margin-top: -40px; margin-left: 330px">';
					CET6_DIV += '	<div id="' + data.itemId+child.EngLevelOpt5_1.itemId + 'Tip" class="onShow" style="margin: 0px; padding: 0px; background: transparent;">';
					CET6_DIV += '		<div class="onShow">test</div>';
					CET6_DIV += '	</div>';
					CET6_DIV += '</div>';
					//---------
				CET6_DIV+='</div></div>'
					//CET6_DIV+='<div class="clear"></div>'
				htmlContent+=CET6_DIV;
				
				//<!--6.英语四级（710分制）关联DIV-->
				var CET4_DIV='';
				if(EXAM_TYPE_DEF=="ENG_LEV_T6")
					CET4_DIV+='<div name="'+data.itemId+'ENG_LEV_T6" id="'+data.itemId+'ENG_LEV_T6" class="input-list" style="display:block">'
				else
				CET4_DIV+='<div name="'+data.itemId+'ENG_LEV_T6" id="'+data.itemId+'ENG_LEV_T6" class="input-list" style="display:none">'
					CET4_DIV+='<div class="input-list-info left"><span class="red-star">*</span><span>总分：</span></div><div class="input-list-text left"><input    class="inpu-list-text-enter" style="height:36px;" id="'+data["itemId"] + child.EngLevelOpt6_1.itemId+'" value="'+child.EngLevelOpt6_1.value+'"/>'
					//---------CET4_DIV  input格式检验:
					CET4_DIV += '<div style="margin-top: -40px; margin-left: 330px">';
					CET4_DIV += '	<div id="' + data.itemId+child.EngLevelOpt6_1.itemId + 'Tip" class="onShow" style="margin: 0px; padding: 0px; background: transparent;">';
					CET4_DIV += '		<div class="onShow">test</div>';
					CET4_DIV += '	</div>';
					CET4_DIV += '</div>';
					//---------
				CET4_DIV+='</div></div>'
					//CET4_DIV+='<div class="clear"></div>'	
				htmlContent+=CET4_DIV;
				
				//<!--7.英语六级（100分制）关联DIV-->
				var CET6_DIV2='';
				if(EXAM_TYPE_DEF=="ENG_LEV_T7")
					CET6_DIV2+='<div name="'+data.itemId+'ENG_LEV_T7" id="'+data.itemId+'ENG_LEV_T7" class="input-list" style="display:block">'
				else
				CET6_DIV2+='<div name="'+data.itemId+'ENG_LEV_T7" id="'+data.itemId+'ENG_LEV_T7" class="input-list" style="display:none">'
					CET6_DIV2+='<div class="input-list-info left"><span class="red-star">*</span><span >是否通过：</span></div>'
					CET6_DIV2+='<div class="input-list-text left" ><select  style="width:255px;height:36px" id="'+data["itemId"] + child.EngLevelOpt7_1.itemId+'" value="'+child.EngLevelOpt7_1.value+'">'
						CET6_DIV2+='<option value="Y">通过</option>'
						CET6_DIV2+='<option value="N">未通过</option>'
					CET6_DIV2+='</select></div>'
				CET6_DIV2+='</div>'
					//CET6_DIV2+='<div class="clear"></div>'
				htmlContent+=CET6_DIV2;
				
				//<!--8.英语四级（100分制）关联DIV-->
				var CET4_DIV2='';
				if(EXAM_TYPE_DEF=="ENG_LEV_T8")
					CET4_DIV2+='<div name="'+data.itemId+'ENG_LEV_T8" id="'+data.itemId+'ENG_LEV_T8" class="input-list" style="display:block">'
				else
				CET4_DIV2+='<div name="'+data.itemId+'ENG_LEV_T8" id="'+data.itemId+'ENG_LEV_T8" class="input-list" style="display:none">'
					CET4_DIV2+='<div class="input-list-info left"><span class="red-star">*</span><span>是否通过：</span></div>'
					CET4_DIV2+='<div class="input-list-text left" ><select style="width:100%;height:36px" id="'+data["itemId"] + child.EngLevelOpt8_1.itemId+'" value="'+child.EngLevelOpt8_1.value+'">'
						CET4_DIV2+='<option value="Y">通过</option>'
						CET4_DIV2+='<option value="N">未通过</option>'
					CET4_DIV2+='</select></div>'
				CET4_DIV2+='</div>'
					//CET4_DIV2+='<div class="clear"></div>'
				htmlContent+=CET4_DIV2;
				
				//<!--9.专业英语关联DIV-->
				var TEM_DIV=''
				if(EXAM_TYPE_DEF=="ENG_LEV_T9")
					TEM_DIV+='<div name="'+data.itemId+'ENG_LEV_T9" id="'+data.itemId+'ENG_LEV_T9" class="input-list" style="display:block">'
				else
				TEM_DIV+='<div name="'+data.itemId+'ENG_LEV_T9" id="'+data.itemId+'ENG_LEV_T9" class="input-list" style="display:none">'
					TEM_DIV+='<div class="input-list-info left"><span class="red-star">*</span><span>总分(成绩)：</span></div>'
					TEM_DIV+='<div class="input-list-text left" ><select style="width:100%;height:36px" id="'+data["itemId"] + child.EngLevelOpt9_1.itemId+'" value="'+child.EngLevelOpt9_1.value+'">'
						TEM_DIV+='<option value="TEM4">专业四级</option>'
						TEM_DIV+='<option value="TEM8">专业八级</option>'
					TEM_DIV+='</select></div>'
				TEM_DIV+='</div>'
					//TEM_DIV+='<div class="clear"></div>'
				htmlContent+=TEM_DIV;
				
				//<!--10.高级口译关联DIV-->
				var H_INTER_DIV='';
				if(EXAM_TYPE_DEF=="ENG_LEV_T10")
					H_INTER_DIV+='<div name="'+data.itemId+'ENG_LEV_T10" id="'+data.itemId+'ENG_LEV_T10" class="input-list" style="display:block">'
				else
				H_INTER_DIV+='<div name="'+data.itemId+'ENG_LEV_T10" id="'+data.itemId+'ENG_LEV_T10" class="input-list" style="display:none">'
					H_INTER_DIV+='<div class="input-list-info left"><span class="red-star">*</span><span>总分(成绩)：</span></div>'
					H_INTER_DIV+='<div class="input-list-text left" ><select style="width:100%;height:36px" id="'+data["itemId"] + child.EngLevelOpt10_1.itemId+'" value="'+child.EngLevelOpt10_1.value+'">'
						H_INTER_DIV+='<option value="A">拿到资格证书</option>'
						H_INTER_DIV+='<option value="B">笔试证书</option>'
					H_INTER_DIV+='</select></div>'
				H_INTER_DIV+='</div>'
					//H_INTER_DIV+='<div class="clear"></div>'
				htmlContent+=H_INTER_DIV;
				
				//<!--11.中级口译关联DIV-->
				var M_INTER_DIV='';
				if(EXAM_TYPE_DEF=="ENG_LEV_T11")
					M_INTER_DIV+='<div name="'+data.itemId+'ENG_LEV_T11" id="'+data.itemId+'ENG_LEV_T11" class="input-list" style="display:block">'
				else
				M_INTER_DIV+='<div name="'+data.itemId+'ENG_LEV_T11" id="'+data.itemId+'ENG_LEV_T11" class="input-list" style="display:none">'
					M_INTER_DIV+='<div class="input-list-info left"><span class="red-star">*</span><span>总分(成绩)：</span></div>'
					M_INTER_DIV+='<div class="input-list-text left" ><select style="width:100%;height:36px" id="'+data["itemId"] + child.EngLevelOpt11_1.itemId+'" value="'+child.EngLevelOpt11_1.value+'">'
						M_INTER_DIV+='<option value="A">拿到资格证书</option>'
						M_INTER_DIV+='<option value="B">笔试证书</option>'
					M_INTER_DIV+='</select></div>'
				M_INTER_DIV+='</div>'
					//M_INTER_DIV+='<div class="clear"></div>'
				htmlContent+=M_INTER_DIV;
				
				//<!--12.BEC关联DIV-->
				var BEC_DIV='';
				if(EXAM_TYPE_DEF=="ENG_LEV_T12")
					BEC_DIV+='<div name="'+data.itemId+'ENG_LEV_T12" id="'+data.itemId+'ENG_LEV_T12" class="input-list" style="display:block">'
				else
				BEC_DIV+='<div name="'+data.itemId+'ENG_LEV_T12" id="'+data.itemId+'ENG_LEV_T12" class="input-list" style="display:none">'
					BEC_DIV+='<div class="input-list-info left"><span class="red-star">*</span><span>总分(成绩)：</span></div>'
					BEC_DIV+='<div class="input-list-text left" ><select style="width:100%;height:36px" id="'+data["itemId"] + child.EngLevelOpt12_1.itemId+'" value="'+child.EngLevelOpt12_1.value+'">'
						BEC_DIV+='<option value="A">高级</option>'
						BEC_DIV+='<option value="B">中级</option>'
						BEC_DIV+='<option value="C">初级</option>'
					BEC_DIV+='</select></div>'
				BEC_DIV+='</div>'
					//BEC_DIV+='<div class="clear"></div>'
				htmlContent+=BEC_DIV;
				
				//<!--13.TOEIC990-->
				var TOEIC_DIV='';
				if(EXAM_TYPE_DEF=="ENG_LEV_T13")
					TOEIC_DIV+='<div name="'+data.itemId+'ENG_LEV_T13" id="'+data.itemId+'ENG_LEV_T13" class="input-list" style="display:block">'
				else
				TOEIC_DIV+='<div name="'+data.itemId+'ENG_LEV_T13" id="'+data.itemId+'ENG_LEV_T13" class="input-list" style="display:none">'
					TOEIC_DIV+=TOEIC_DATE
					TOEIC_DIV+='<div class="input-list-info left"><span class="red-star">*</span><span>总分：</span></div><div  class="input-list-text left"><input  class="inpu-list-text-enter" style="height:36px;" id="'+data["itemId"] + child.EngLevelOpt13_2.itemId+'" value="'+child.EngLevelOpt13_2.value+'"/>'
					//---------TOEIC_DIV  input格式检验:
					TOEIC_DIV += '<div style="margin-top: -40px; margin-left: 330px">';
					TOEIC_DIV += '	<div id="' +data.itemId+ child.EngLevelOpt13_2.itemId + 'Tip" class="onShow" style="margin: 0px; padding: 0px; background: transparent;">';
					TOEIC_DIV += '		<div class="onShow">test</div>';
					TOEIC_DIV += '	</div>';
					TOEIC_DIV += '</div>';
					//---------
				TOEIC_DIV+='</div></div>'	
				//	TOEIC_DIV+='<div class="clear"></div>'	
				htmlContent+=TOEIC_DIV;
				
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
				if(EXAM_TYPE_DEF!=""&&EXAM_TYPE_DEF!="-1")
					htmlContent+='<div name="'+data.itemId+'UP" id="'+data.itemId+'UP" class="input-list" style="display:block">'
				else
				htmlContent+='<div name="'+data.itemId+'UP" id="'+data.itemId+'UP"  class="input-list" style="display:none">'
					htmlContent+='<div class="input-list-info left"><span class="red-star">*</span><span >上传证书/成绩扫描件</span></div>'
						htmlContent+='<div class="input-list-texttemplate left" >'
							//'<input type="file" id="'+data["itemId"] + child.EngLevelUp.itemId+'File"  name="' + data["itemId"] + child.EngLevelUp.itemId + 'File" onchange=SurveyBuild.eduImgUpload(this,"EngLevelUp") accept="image/*"/>'
								htmlContent+= '<div class="filebtn left">';
								htmlContent+= '	<div class="filebtn-org"><img src="' + TzUniversityContextPath + '/statics/images/appeditor/new/upload.png" />&nbsp;&nbsp;' + MsgSet["UPLOAD_BTN_MSG"] + '</div>';
								htmlContent+= '	<input data-instancid="' + data.instanceId + '" id="'+child.EngLevelUp.itemId+ '" name="'+ data.itemId + '" title="' + data.itemName + '" onchange="SurveyBuild.engUploadAttachment(this,\''+ data.instanceId +'\',\''+ child.EngLevelUp.instanceId +'\')" type="file" class="filebtn-orgtext" accept="image/*"/>';
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
				htmlContent+='</div>'
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
				 //为所有的select注册事件
				 for(var i=0;i<len;i++){
					 var child=children[i];
						type_select=$("#"+ data["itemId"] + child.EngLevelType.itemId);
						type_select.each(function(index){
							$(this).on("change",function(){
								var related_div_name="div[name='relatedDiv']";
								for(var i in EXAM_TYPE_MAP){
									var div_name="div[name='"+data.itemId+i+"']";
									if($(this).val()==i){
										$(this).parents(".input-list").siblings(related_div_name).find(div_name).css("display","block");
										//如果子模块中有"select":
										$(this).parents(".input-list").siblings(related_div_name).find("select").chosen({width: "100%"});
										$(this).parents(".input-list").siblings(related_div_name).find("select").trigger("chosen:updated");
										//----
									}else{
										$(this).parents(".input-list").siblings(related_div_name).find(div_name).css("display","none");
									}
								}
								var up_name="div[name='"+data.itemId+"UP"+"']";
								var up_btn=$(this).parents(".input-list").siblings(related_div_name).find(up_name);
								if($(this).val()=="-1")
									up_btn.css("display","none");
								else
									up_btn.css("display","block");
								//---清理数据
							})
						});
					//为所有的timePicker注册事件:
						//日期控件处理1.2.3.4.13
						var id_gp=[1,2,3,4,13];
						for(var j=0;j<id_gp.length;j++){
							   var EngLevelOpt="EngLevelOpt"+id_gp[j]+"_1";
							   var $inputBox = $("#" + data.itemId +child[EngLevelOpt].itemId);
							   var $selectBtn = $("#" + data.itemId +child[EngLevelOpt].itemId + "_Btn");
							   //日期选择事件绑定:
							   $inputBox.each(function(){
								   $(this).datepicker({
										showButtonPanel:true,
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
									   //console.log("prev:");
									   //console.dir($(this).prev());
									   $(this).prev().click();
								   })
							   })
						}
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
											//return MsgSet["FILE_UPL_REQUIRE"];
											return "length:"+child["EngLevelUp"].children.length+"name:"+child["EngLevelUp"].children[0].fileName;
										}
									//}
								}	
							});
						});
						//-----验证input数据:EngLevelOpt1_1,EngLevelOpt1_2,EngLevelOpt2_1,EngLevelOpt2_2,
						var input_id_gp=["1_1","1_2","2_1","2_2","3_1","3_2","4_1","4_2","5_1","6_1","13_1","13_2"];
						for(var j=0;j<input_id_gp.length;j++){
							   var EngLevelOpt="EngLevelOpt"+input_id_gp[j];
							   var $inputBox = $("#" + data.itemId +child[EngLevelOpt].itemId);
							   $inputBox.each(function(){
								   $(this).formValidator({tipID:($(this).attr("id")+'Tip'), onShow:"", onFocus:"&nbsp;", onCorrect:"&nbsp;"});
									$(this).functionValidator({
										fun:function(val,el){
											if(val==""){
												return "此项必填";
											}else{
												return true;
											}
										}	
									}); 
							   });
						}
						//-----验证select数据:EngLevelOpt7_1 EngLevelOpt8_1 EngLevelOpt9_1 EngLevelOpt10_1 EngLevelOpt11_1 EngLevelOpt12_1
						var select_id_gp=["EngLevelType","EngLevelOpt7_1","EngLevelOpt8_1","EngLevelOpt9_1","EngLevelOpt10_1","EngLevelOpt11_1","EngLevelOpt12_1"];
						for(var j=0;j<select_id_gp.length;j++){
							 var EngLevelOpt=""+select_id_gp[j];
							   var $selectEl = $("#" + data.itemId +child[EngLevelOpt].itemId);
							   $selectEl.each(function(){
								   $(this).formValidator({tipID:($(this).attr("id")+'Tip'), onShow:"", onFocus:"&nbsp;", onCorrect:"&nbsp;"});
									$(this).functionValidator({
										fun:function(val,el){
											if(val==""||val=="-1"){
												return "此项必选";
											}else{
												return true;
											}
										}	
									}); 
							   });
						}
						
						
				 }
				 //所有看的到的select美化:
				$("select").each(function(){
					$(this).chosen({width:"100%"});
				});
		       
	}
})