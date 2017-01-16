SurveyBuild.extend("FirmType", "baseComponent", {
	itemName: "职业背景",
	title: "职业背景",
	isDoubleLine: "Y",
	fixedContainer: "Y",//固定容器标识
	configData:{
		
	},
	children: {
		//公司类型
		"WorkExper1": {
			"instanceId": "WorkExper1",
			"itemId": "firm_type",
			//"itemName": MsgSet["FIRM_TYPE"],
			"itemName":"公司类型",
			//"title": MsgSet["FIRM_TYPE"],
			"title": "公司类型",
			"orderby": 1,
			"value": "",
			"StorageType": "S",
			"classname": "ComboBox"
		},
		//岗位类型
		"WorkExper2": {
			"instanceId": "WorkExper2",
			"itemId": "position_type",
			//"itemName": MsgSet["POSITION_TYPE"],
			"itemName": "岗位类型",
			//"title": MsgSet["POSITION_TYPE"],
			"title": "岗位类型",
			"orderby": 2,
			"value": "",
			"StorageType": "S",
			"classname": "ComboBox"
		},
	},
	minLines: "1",
	maxLines: "4",
	//linesNo: [1, 2, 3],
	_getHtml: function(data, previewmode) {
		var c = "",children = data.children,len = children.length;
		var opt = "",x = "";
		if (previewmode) {
			//title
			c += '<div class="main_inner_content_title">';
			c += '	<span class="reg_title_star">' + (data.isRequire == "Y" ? "*": "") + '</span>';
			c += '	<span class="reg_title_grey_17px">' + data.title + '</span>';
			c += '</div>';

			//content
			var types = this._getContentHtml(data);

			c += '<div class="main_inner_content_top"></div>';
			c += '<div class="main_inner_content">';
			c += types;
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
			var typeLi = '';
			//公司类型
			typeLi += '<div class="type_item_li">';
			typeLi += '	<span class="type_item_label">公司类型：</span>';
			typeLi += '		<b class="read-input" style="min-width:120px;"></b>';
			typeLi += '	</div>';

			//岗位类型
			typeLi += '<div class="type_item_li">';
			typeLi += '	<span class="type_item_label">岗位类型：</span>';
			typeLi += '		<b class="read-input" style="min-width:120px;"></b>';
			typeLi += '	</div>';
			
			
			c += '<div class="question-answer">';
			c += '	<div class="DHContainer" style="border:1px solid #ddd;padding:10px 20px;">' + typeLi + '</div>';
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
		var child=data["childern"];
		//-------------------------------------------------------设置下拉框选项值 01公司性质和岗位性质数据
		var FIRM_TYPE_GRP=[
				"外资/合资企业",//01
				"自主创业",//02
				"国有企业",//03
				"民营企业",//04
				"政府机构",//05
				"事业单位",//06
				"其他"//07
			];
			var POSITION_TYPE_GP1=[
				"高层管理（总经理/副总经理以上级）",//01
				"高级管理（总助/执行主任/执行总监级）",//02
				"中级管理（总监/部门经理级）",//03
				"初级管理（主管级/一般经理级）",//04
				"高级专业人士",//05
				"初级专业人士",//06
				"管理培训生",//07
				"其他"//08
			];
			var POSITION_TYPE_GP2=[
				"处级及以上",//01
				"副处级",//02
				"正科级",//03
				"副科级",//04
				"一般科员",//05
				"其他"//06
			]
		//------------------------------------------------------设置下拉框选项值 02填充select的option属性

			types += '<div class="main_inner_content_para" style="display: inherit;" >';
			
			types += '<div class="main_inner_content_top"></div><div class="padding_div"></div><div class="main_inner_content_foot"></div>';

			//1.公司性质
			if (SurveyBuild._readonly) {
				//只读模式
				var valDesc = "";
				for(var k=0;k<FIRM_TYPE_GRP.length;k++){
					if(child.WorkExper1["value"]==("0"+parseInt(k+1))){
						valDesc=FIRM_TYPE_GRP[k];
					}
					
				}
				
				types += '<div class="main_inner_content_info_autoheight cLH">';
				types += '	<div class="main_inner_connent_info_left">' + child.WorkExper1.itemName + '</div>';
				types += '	<div class="main_inner_content_info_right">' + valDesc + '</div>';
				types += '</div>';

			} else {
				//填写模式
				//-----------------------------公司性质下拉框
				var OPT_FIRM='';
				var $firm_select = $("#" +data["itemId"] + child.WorkExper1.itemId);
				for(var k=0;k<FIRM_TYPE_GRP.length;k++){
					//OPT_FIRM+="<option value='0"+parseInt(k+1)+"'>"+FIRM_TYPE_GRP[k]+"</option>"
					//OPT_FIRM+='<option value="0'+parseInt(i+1)+'"'+($('#select1').val()=="0"+parseInt(i+1)?'selected="selected"': '')+'>'+FIRM_TYPE_GRP[i]+'</option>'
					OPT_FIRM+='<option value="0'+parseInt(i+1)+'"'+($firm_select.val()=="0"+parseInt(i+1)?'selected="selected"': '')+'>'+FIRM_TYPE_GRP[i]+'</option>';

				}
				//----------------------------放入公司性质OPT
				types += '<div class="main_inner_content_info_autoheight">';
				types += '	<div class="main_inner_connent_info_left">' + child.WorkExper1.itemName + '</div>';
				types += '	<div class="main_inner_content_info_right">';
				types += '		<select id="' + data["itemId"] + child.WorkExper1.itemId + '" class="chosen-select" style="width: 255px;" data-regular="" title="' + child.WorkExper1.itemName + '" value="' + child.WorkExper1["value"] + '" name="' + data["itemId"] + child.WorkExper1.itemId + '">';
				types += '			<option value="-1">' + MsgSet["PLEASE_SELECT"] + '</option>';
				types += OPT_FIRM;
				types += '		</select>';
				//----------------------------
				types += '		<div style="margin-top:-40px;margin-left:256px"><div id="' + data["itemId"] + child.WorkExper1.itemId + 'Tip" class="onCorrect" style="margin: 0px; padding: 0px; background: transparent;">';
				types += '			<div class="onCorrect">&nbsp;</div></div>';
				types += '		</div>';
				types += '	</div>';
				types += '	<div class="main_inner_content_edit"><img width="15" height="15" src="/onlineReg/images/edit.png">' + MsgSet["EDIT"] + '</div>';
				types += '	<div onclick="SurveyBuild.deleteFun(this);" class="main_inner_content_del_bmb"><img width="15" height="15" src="/onlineReg/images/del.png">' + MsgSet["DEL"] + '</div>';
				types += '</div>';
			}

			//2.职位类型：
			if (SurveyBuild._readonly) {
				//只读模式
				var valDesc = "--请选择--";
				types += '<div class="main_inner_content_info_autoheight cLH">';
				types += '	<div class="main_inner_connent_info_left">' + child.WorkExper2.itemName + '</div>';
				types += '	<div class="main_inner_content_info_right">' + valDesc + '</div>';
				types += '</div>';

			} else {
				//填写模式
				//-----------------------------职位下拉框
				var OPT_POSITION='--请选择--';
				var $position_select = $("#" +data["itemId"] + child.WorkExper2.itemId);
				//----------------------------职位类型OPT
				types += '<div class="main_inner_content_info_autoheight">';
				types += '	<div class="main_inner_connent_info_left">' + child.WorkExper2.itemName + '</div>';
				types += '	<div class="main_inner_content_info_right">';
				types += '		<select id="' + data["itemId"] + child.WorkExper2.itemId + '" class="chosen-select" style="width: 255px;" data-regular="" title="' + child.WorkExper2.itemName + '" value="' + child.WorkExper2["value"] + '" name="' + data["itemId"] + child.WorkExper2.itemId + '">';
				types += '			<option value="-1">' + MsgSet["PLEASE_SELECT"] + '</option>';
				types += OPT_POSITION;
				types += '		</select>';
				//----------------------------
				types += '		<div style="margin-top:-40px;margin-left:256px"><div id="' + data["itemId"] + child.WorkExper2.itemId + 'Tip" class="onCorrect" style="margin: 0px; padding: 0px; background: transparent;">';
				types += '			<div class="onCorrect">&nbsp;</div></div>';
				types += '		</div>';
				types += '	</div>';
				types += '	<div class="main_inner_content_edit"><img width="15" height="15" src="/onlineReg/images/edit.png">' + MsgSet["EDIT"] + '</div>';
				if (j != 0) {
					types += '	<div onclick="SurveyBuild.deleteFun(this);" class="main_inner_content_del_bmb"><img width="15" height="15" src="/onlineReg/images/del.png">' + MsgSet["DEL"] + '</div>';
				}
				types += '</div>';
			}
		
		
		return types;
	},

	_eventbind: function(data) {
		//------------------------
		var FIRM_TYPE_GRP=[
				"外资/合资企业",//01
				"自主创业",//02
				"国有企业",//03
				"民营企业",//04
				"政府机构",//05
				"事业单位",//06
				"其他"//07
			];
			var POSITION_TYPE_GP1=[
				"高层管理（总经理/副总经理以上级）",//01
				"高级管理（总助/执行主任/执行总监级）",//02
				"中级管理（总监/部门经理级）",//03
				"初级管理（主管级/一般经理级）",//04
				"高级专业人士",//05
				"初级专业人士",//06
				"管理培训生",//07
				"其他"//08
			];
			var POSITION_TYPE_GP2=[
				"处级及以上",//01
				"副处级",//02
				"正科级",//03
				"副科级",//04
				"一般科员",//05
				"其他"//06
			]
		//-----------------------
		var child = data.children;

		//取得公司性质下拉框的”选择器“
		var $firm_select = $("#" +data["itemId"] + child.WorkExper1.itemId);
		
		//职位类型
		var $position_select = $("#" +data["itemId"] + child.WorkExper2.itemId);
		
		$firm_select.on("change",function(){
			var FIRM_TYPE=$(this).val();
			var OPT_POSITION='<option value="-1">--请选择--</option>';
				if(FIRM_TYPE=='01'||FIRM_TYPE=='03'||FIRM_TYPE=='04'||FIRM_TYPE=='07'){
					for(var i=0;i<POSITION_TYPE_GP1.length;i++){
						OPT_POSITION+='<option value="A'+parseInt(i+1)+'">'+POSITION_TYPE_GP1[i]+'</option>'
					}
				}else if(FIRM_TYPE=='05'||FIRM_TYPE=='06'){
					for(var i=0;i<POSITION_TYPE_GP2.length;i++){
						OPT_POSITION+='<option value="B'+parseInt(i+1)+'">'+POSITION_TYPE_GP2[i]+'</option>'
					}

				}else if(FIRM_TYPE=='02'){
					for(var i=0;i<POSITION_TYPE_GP1.length;i++){
						OPT_POSITION+='<option value="A'+parseInt(i+1)+'">'+POSITION_TYPE_GP1[i]+'</option>'
					}
					//设置创业经历必填
				}
			$position_select.html(OPT_POSITION);
			
		});
	}	
})