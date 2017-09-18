SurveyBuild.extend("FirmType", "baseComponent", {
	itemName: "职业背景",
	title: "职业背景",
	isDoubleLine: "Y",
	isSingleLine:"Y",
	fixedContainer: "Y",//固定容器标识
	children: {
		//公司类型
		"WorkExper1": {
			"instanceId": "WorkExper1",
			"itemId": "firm_type",
			"itemName": MsgSet["FIRM_TYPE"],
			//"itemName":"公司类型",
			"title": MsgSet["FIRM_TYPE"],
			//"title": "公司类型",
			"orderby": 1,
			"value": "",
			"StorageType": "S",
			"classname": "Select"
		},
		//岗位类型
		"WorkExper2": {
			"instanceId": "WorkExper2",
			"itemId": "position_type",
			"itemName": MsgSet["POSITION_TYPE"],
			//"itemName": "岗位类型",
			"title": MsgSet["POSITION_TYPE"],
			//"title": "岗位类型",
			"orderby": 2,
			"value": "",
			"StorageType": "S",
			"classname": "Select"
		}
	},
	minLines: "1",
	maxLines: "4",
	//linesNo: [1, 2, 3],
	_getHtml: function(data, previewmode) {
		var c = "",children = data.children,len = children.length;
		var opt = "",x = "";
		if (previewmode) {
			var types;
			if(SurveyBuild.accessType == "M"){

				types = this._getContentHtmlP(data);
				c += types;
			}else{

				types = this._getContentHtml(data);

				c += '<div class="main_inner_content_top"></div>';
				c += '<div class="main_inner_content">';
				c += types;
				c += '</div>';
				c += '<div class="main_inner_content_foot"><div class="clear"></div></div>';
			}

		} else {
			var typeLi = '';
			//公司类型
			typeLi += '<div class="type_item_li">';
			typeLi += '	<span class="type_item_label"><span class="red-star">' + (data.isRequire == "Y" ? "*": "") + '</span>'+MsgSet["FIRM_TYPE"]+'：</span>';
			typeLi += '		<b class="read-select" style="min-width:120px;">'+MsgSet["PLEASE_SELECT"]+'</b>';
			typeLi += '	</div>';

			//岗位类型
			typeLi += '<div class="type_item_li">';
			typeLi += '	<span class="type_item_label"><span class="red-star">' + (data.isRequire == "Y" ? "*": "") + '</span>'+MsgSet["POSITION_TYPE"]+'：</span>';
			typeLi += '		<b class="read-select" style="min-width:120px;">'+MsgSet["PLEASE_SELECT"]+'</b>';
			typeLi += '	</div>';
			
			
			c += '<div class="question-answer"  style="border:1px solid #ddd;padding:10px;">';
			c += '	<div class="DHContainer">' + typeLi + '</div>';
			c += '</div>';
		}
		return c;
	},
	_edit: function(data) {
		var e = '';

//		e += '<div class="edit_jygz">';
//		e += '	<span class="title"><i class="icon-info-sign"></i> 参数设置</span>';
//		e += '  <div class="groupbox">';
//		e += '  <div class="edit_item_warp">';
//		e += '      <span class="edit_item_label">最小行数：</span>';
//		e += '     <input type="text" class="medium" onkeyup="SurveyBuild.saveAttr(this,\'minLines\')" value="' + data.minLines + '"/>';
//		e += '  </div>';
//
//		e += '  <div class="edit_item_warp mb10">';
//		e += '      <span class="edit_item_label">最大行数：</span>';
//		e += '     <input type="text" class="medium" onkeyup="SurveyBuild.saveAttr(this,\'maxLines\')" value="' + data.maxLines + '"/>';
//		e += '  </div>';
//		e += '</div>';
//		//规则设置
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
//		//高级设置
		e += '      <div class="edit_item_warp">';
		e += '          <a href="javascript:void(0);" onclick="SurveyBuild.RulesSet(this);"><i class="icon-cogs"></i> 高级设置</a>';
		e += '		    <a href="#" data-for-id="help_advancedSetup" onclick="SurveyBuild.showMsg(this,event)" class="big-link" data-reveal-id="myModal" data-animation="fade">(?)</a>';
		e += '      </div>';
		e += '</div>';
		return e;
	},
	_getContentHtml: function(data) {
//		console.log("firmType:")
//		console.dir(data);
//		console.dir(data.children);
		var child=data["children"][0];
		if (child == undefined) {
	   		 child=data["children"];
	   	 }
		//var child=data["children"];
		var types="";
		//--公司类型初始值
		//console.dir(child);
		var FIRM_TYPE_DEF=child.WorkExper1.value;
		//--岗位类型初始值
		var POSITION_TYPE_DEL=child.WorkExper2.value;
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

			types += '<div class="main_inner_content_para" >';
			
			types += '<div class="main_inner_content_top"></div><div class="padding_div"></div><div class="main_inner_content_foot"></div>';

			//1.公司性质
			if (SurveyBuild._readonly) {
				//只读模式
				var OPT_FIRM='';
				var valDesc='';
				for(var k=0;k<FIRM_TYPE_GRP.length;k++){
					//OPT_FIRM+='<option value="0'+parseInt(k+1)+'"'+(FIRM_TYPE_DEF=="0"+parseInt(k+1)?'selected="selected"': '')+'>'+FIRM_TYPE_GRP[k]+'</option>';
					if(FIRM_TYPE_DEF=="0"+parseInt(k+1)){
						valDesc=FIRM_TYPE_GRP[k];
						break;
					}
				}
				//----------------------------放入公司性质OPT
				types += '<div class="input-list" style="display:block">';
				types += '	<div class="input-list-info left"> <span class="red-star">' + (data.isRequire == "Y" ? "*": "") + '</span>' + child.WorkExper1.itemName + ':</div>';
				types += '	<div class="input-list-text left">';
				types +=valDesc;
				types += '	</div>';
				types += '</div>';

			} else {
				//填写模式

				var OPT_FIRM='';
				for(var k=0;k<FIRM_TYPE_GRP.length;k++){
					OPT_FIRM+='<option value="0'+parseInt(k+1)+'"'+(FIRM_TYPE_DEF=="0"+parseInt(k+1)?'selected="selected"': '')+'>'+FIRM_TYPE_GRP[k]+'</option>';
				}
				//----------------------------放入公司性质OPT
				types +='<div class="clear"></div>'
				types += '<div class="input-list" style="display:block">';
				types += '	<div class="input-list-info left"> <span class="red-star">' + (data.isRequire == "Y" ? "*": "") + '</span>' + child.WorkExper1.itemName + ':</div>';
				types += '	<div class="input-list-text left input-edu-select">';
				types += '		<select id="' + data["itemId"] + child.WorkExper1.itemId + '" class="chosen-select" style="width: 100%;" data-regular="" title="' + child.WorkExper1.itemName + '" value="' + child.WorkExper1["value"] + '" name="' + data["itemId"] + child.WorkExper1.itemId + '">';
				types += '			<option value="">' +MsgSet["PLEASE_SELECT"]+'</option>';
				types += OPT_FIRM;
				types += '		</select>';
				//----------------------------
				types += '		<div style="margin-top:-40px;margin-left:330px"><div id="' + data["itemId"] + child.WorkExper1.itemId + 'Tip" class="onCorrect" style="margin: 0px; padding: 0px; background: transparent;">';
				types += '			<div class="onCorrect">&nbsp;</div></div>';
				types += '		</div>';
				types += '	</div>';
				types +='<div class="clear"></div>';
				types += '</div>';
			}

			//2.职位类型：
			if (SurveyBuild._readonly) {
				//只读模式
				var OPT_POSITION='';
				var valDesc='';
				if(FIRM_TYPE_DEF!=undefined&&FIRM_TYPE_DEF!=""&&FIRM_TYPE_DEF!=null){
					if(FIRM_TYPE_DEF=='01'||FIRM_TYPE_DEF=='02'||FIRM_TYPE_DEF=='03'||FIRM_TYPE_DEF=='04'||FIRM_TYPE_DEF=='07'){
						for(var k=0;k<POSITION_TYPE_GP1.length;k++){
							if(POSITION_TYPE_DEL==("A"+parseInt(k+1))){
								valDesc=POSITION_TYPE_GP1[k];
								break;
							}
						}
					}else if(FIRM_TYPE_DEF=='05'||FIRM_TYPE_DEF=='06'){
						for(var k=0;k<POSITION_TYPE_GP2.length;k++){
							if(POSITION_TYPE_DEL==("B"+parseInt(k+1))){
								valDesc=POSITION_TYPE_GP2[k];
								break;
							}
						}

					}
				}
				//var OPT_POSITION='--请选择--';
				//----------------------------职位类型OPT 请把"请选择"跟换成“MsgSet["PLEASE_SELECT"]”
				types += '<div class="input-list" style="display:block">';
				types += '	<div class="input-list-info left"> <span class="red-star">' + (data.isRequire == "Y" ? "*": "") + '</span>' + child.WorkExper2.itemName + ':</div>';
				types += '	<div class="input-list-text left input-edu-select">';
				types +=valDesc;
				types += '	</div>';
				types += '</div>';

			} else {
				//填写模式
				//------------------根据“公司类型”设置“职位类型”选项
				var OPT_POSITION='<option value="">' +MsgSet["PLEASE_SELECT"]+'</option>';
				//console.log(FIRM_TYPE_DEF);
				if(FIRM_TYPE_DEF!=undefined&&FIRM_TYPE_DEF!=""&&FIRM_TYPE_DEF!=null){
					if(FIRM_TYPE_DEF=='01'||FIRM_TYPE_DEF=='02'||FIRM_TYPE_DEF=='03'||FIRM_TYPE_DEF=='04'||FIRM_TYPE_DEF=='07'){
						for(var k=0;k<POSITION_TYPE_GP1.length;k++){
							OPT_POSITION+='<option value="A'+parseInt(k+1)+'"'+(POSITION_TYPE_DEL==("A"+parseInt(k+1)) ? 'selected="selected"': '')+'>'+POSITION_TYPE_GP1[k]+'</option>'
						}
					}else if(FIRM_TYPE_DEF=='05'||FIRM_TYPE_DEF=='06'){
						for(var k=0;k<POSITION_TYPE_GP2.length;k++){
							OPT_POSITION+='<option value="B'+parseInt(k+1)+'"'+(POSITION_TYPE_DEL==("B"+parseInt(k+1)) ? 'selected="selected"': '')+'>'+POSITION_TYPE_GP2[k]+'</option>'
						}

					}
				}
				//var OPT_POSITION='--请选择--';
				//----------------------------职位类型OPT 请把"请选择"跟换成“MsgSet["PLEASE_SELECT"]”
				types += '<div class="input-list" style="display:block">';
				types += '	<div class="input-list-info left"> <span class="red-star">' + (data.isRequire == "Y" ? "*": "") + '</span>' + child.WorkExper2.itemName + ':</div>';
				types += '	<div class="input-list-text left input-edu-select">';
				types += '		<select id="' + data["itemId"] + child.WorkExper2.itemId + '" class="chosen-select" style="width: 100%;" data-regular="" title="' + child.WorkExper2.itemName + '" value="' + child.WorkExper2["value"] + '" name="' + data["itemId"] + child.WorkExper2.itemId + '">';
				//types += '			<option value="-1">' + '--'+MsgSet["PLEASE_SELECT"]+'--' + '</option>';
				types += OPT_POSITION;
				types += '		</select>';
				//----------------------------
				types += '		<div style="margin-top:-40px;margin-left:330px"><div id="' + data["itemId"] + child.WorkExper2.itemId + 'Tip" class="onCorrect" style="margin: 0px; padding: 0px; background: transparent;">';
				types += '			<div class="onCorrect">&nbsp;</div></div>';
				types += '		</div>';
				types += '	</div>';
				types += '<div class="clear"></div>';
				types += '</div>';
			}
		
			types+="</div>"
		return types;
	},

	//手机版
	_getContentHtmlP: function(data) {

		var child=data["children"][0];
		if (child == undefined) {
			child=data["children"];
		}
		var types="";

		//--公司类型初始值
		var FIRM_TYPE_DEF=child.WorkExper1.value;

		//--岗位类型初始值
		var POSITION_TYPE_DEL=child.WorkExper2.value;

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
		//1.公司类型
		if (SurveyBuild._readonly) {

			//只读模式
			var OPT_FIRM='';
			var valDesc='';
			for(var k=0;k<FIRM_TYPE_GRP.length;k++){
				if(FIRM_TYPE_DEF=="0"+parseInt(k+1)){
					valDesc=FIRM_TYPE_GRP[k];
					break;
				}
			}

			//放入已选择的公司类型
			types += '<div class="item">';
			types += '<p>'+ child.WorkExper1.itemName +'<span>'+(data.isRequire == "Y" ? "*": "")+'</span></p>';
			types += '<div class="text-box"><input ' + ' type="text" class="text1" readonly="true" value="' + valDesc + '"/></div>';
			types += '</div>';

		}else{

			//填写模式
			var OPT_FIRM='';
			for(var k=0;k<FIRM_TYPE_GRP.length;k++){
				OPT_FIRM+='<option value="0'+parseInt(k+1)+'"'+(FIRM_TYPE_DEF=="0"+parseInt(k+1)?'selected="selected"': '')+'>'+FIRM_TYPE_GRP[k]+'</option>';
			}

			//--------------------------------将公司类型值放入下拉框
			types += '<div class="item">';
			types += '<p>'+ child.WorkExper1.itemName +'<span>'+(data.isRequire == "Y" ? "*": "")+'</span></p>';
			types += '<div id="' + data["itemId"] + child.WorkExper1.itemId + 'Tip" class="tips" style="display: none;"><i></i><span></span></div>';
			types += '<div class="text-box">';
			types += '<select id="' + data["itemId"] + child.WorkExper1.itemId + '" class="select1" data-regular="" title="' + child.WorkExper1.itemName + '" value="' + child.WorkExper1["value"] + '" name="' + data["itemId"] + child.WorkExper1.itemId + '">';
			types += '<option value="">' +MsgSet["PLEASE_SELECT"]+'</option>';
			types += OPT_FIRM;
			types += '</select>';
			types += '</div>';
			types += '</div>';
		}

		//职位类型
		if (SurveyBuild._readonly) {

			//只读模式
			var OPT_POSITION='';
			var valDesc='';
			if(FIRM_TYPE_DEF!=undefined&&FIRM_TYPE_DEF!=""&&FIRM_TYPE_DEF!=null){
				if(FIRM_TYPE_DEF=='01'||FIRM_TYPE_DEF=='02'||FIRM_TYPE_DEF=='03'||FIRM_TYPE_DEF=='04'||FIRM_TYPE_DEF=='07'){
					for(var k=0;k<POSITION_TYPE_GP1.length;k++){
						if(POSITION_TYPE_DEL==("A"+parseInt(k+1))){
							valDesc=POSITION_TYPE_GP1[k];
							break;
						}
					}
				}else if(FIRM_TYPE_DEF=='05'||FIRM_TYPE_DEF=='06'){
					for(var k=0;k<POSITION_TYPE_GP2.length;k++){
						if(POSITION_TYPE_DEL==("B"+parseInt(k+1))){
							valDesc=POSITION_TYPE_GP2[k];
							break;
						}
					}

				}
			}

			types += '<div class="item">';
			types += '<p>'+ child.WorkExper2.itemName +'<span>'+(data.isRequire == "Y" ? "*": "")+'</span></p>';
			types += '<div class="text-box"><input ' + ' type="text" class="text1" readonly="true" value="' + valDesc + '"/></div>';
			types += '</div>';
		}else{

			//填写模式(根据公司类型选择职位类型下拉框)
			var OPT_POSITION='<option value="">' +MsgSet["PLEASE_SELECT"]+'</option>';
			if(FIRM_TYPE_DEF!=undefined&&FIRM_TYPE_DEF!=""&&FIRM_TYPE_DEF!=null){
				if(FIRM_TYPE_DEF=='01'||FIRM_TYPE_DEF=='02'||FIRM_TYPE_DEF=='03'||FIRM_TYPE_DEF=='04'||FIRM_TYPE_DEF=='07'){
					for(var k=0;k<POSITION_TYPE_GP1.length;k++){
						OPT_POSITION+='<option value="A'+parseInt(k+1)+'"'+(POSITION_TYPE_DEL==("A"+parseInt(k+1)) ? 'selected="selected"': '')+'>'+POSITION_TYPE_GP1[k]+'</option>'
					}
				}else if(FIRM_TYPE_DEF=='05'||FIRM_TYPE_DEF=='06'){
					for(var k=0;k<POSITION_TYPE_GP2.length;k++){
						OPT_POSITION+='<option value="B'+parseInt(k+1)+'"'+(POSITION_TYPE_DEL==("B"+parseInt(k+1)) ? 'selected="selected"': '')+'>'+POSITION_TYPE_GP2[k]+'</option>'
					}

				}
			}

			//将值放入职位类型的下拉框
			types += '<div class="item">';
			types += '<p>'+ child.WorkExper2.itemName +'<span>'+(data.isRequire == "Y" ? "*": "")+'</span></p>';
			types += '<div id="' + data["itemId"] + child.WorkExper2.itemId + 'Tip" class="tips" style="display: none;"><i></i><span></span></div>';
			types += '<div class="text-box">';
			types += '<select id="' + data["itemId"] + child.WorkExper2.itemId + '" class="select1" data-regular="" title="' + child.WorkExper2.itemName + '" value="' + child.WorkExper2["value"] + '" name="' + data["itemId"] + child.WorkExper2.itemId + '">';
			types += OPT_POSITION;
			types += '</select>';
			types += '</div>';
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
			];
		//-----------------------
		var child = data["children"][0];
		if (child == undefined) {
	   		 child=data["children"];
	   	 	}
		//	var child = data["children"];
		//取得公司性质下拉框的”选择器“
		var $firm_select = $("#" +data["itemId"] + child.WorkExper1.itemId);

		var $test=$("." +data["itemId"] + child.WorkExper2.itemId+"_chosen");
		
		//职位类型
		var $position_select = $("#" +data["itemId"] + child.WorkExper2.itemId);

		$firm_select.on("change",function(){
			//console.log("valeList:");
			//console.log(child.WorkExper1.value);
			var POSITION_TYPE_DEL=$position_select.val();
			var FIRM_TYPE=$firm_select.val();
			var OPT_POSITION='<option value="">'+MsgSet["PLEASE_SELECT"]+'</option>';
			if(FIRM_TYPE=='01'||FIRM_TYPE=='03'||FIRM_TYPE=='04'||FIRM_TYPE=='07'){
				for(var k=0;k<POSITION_TYPE_GP1.length;k++){
					OPT_POSITION+='<option value="A'+parseInt(k+1)+'"'+(POSITION_TYPE_DEL==("A"+parseInt(k+1)) ? 'selected="selected"': '')+'>'+POSITION_TYPE_GP1[k]+'</option>';
				}
			}else if(FIRM_TYPE=='05'||FIRM_TYPE=='06'){
				for(var k=0;k<POSITION_TYPE_GP2.length;k++){
					OPT_POSITION+='<option value="B'+parseInt(k+1)+'"'+(POSITION_TYPE_DEL==("B"+parseInt(k+1)) ? 'selected="selected"': '')+'>'+POSITION_TYPE_GP2[k]+'</option>';
				}
			}else if(FIRM_TYPE=='02'){
				for(var k=0;k<POSITION_TYPE_GP1.length;k++){
					OPT_POSITION+='<option value="A'+parseInt(k+1)+'"'+(POSITION_TYPE_DEL==("A"+parseInt(k+1)) ? 'selected="selected"': '')+'>'+POSITION_TYPE_GP1[k]+'</option>';
				}
				//设置创业经历必填
			}
			$position_select.html(OPT_POSITION);
			$position_select.trigger("chosen:updated");
			child.WorkExper1.value=FIRM_TYPE;
			//console.log("value1:");
			//console.log(child.WorkExper1.value);
		})
		$position_select.on("change",function(){
			child.WorkExper2.value=$position_select.val();
			//console.log(child.WorkExper2.value);
		});
		 //所有select非空验证:WorkExper1 WorkExper2
		if(data.isRequire == "Y"){

			var select_id_gp=["WorkExper1","WorkExper2"];
			for(var j=0;j<select_id_gp.length;j++){
				var EngLevelOpt=""+select_id_gp[j];
				var $selectEl = $("#" + data.itemId +child[EngLevelOpt].itemId);
				$selectEl.each(function(){

					$(this).formValidator({tipID:($(this).attr("id")+'Tip'), onShow:"", onFocus:"&nbsp;", onCorrect:"&nbsp;"});
					$(this).functionValidator({
						fun:function(val,el){

							if(val==""){
								return "此项必选";
							}else{
								return true;
							}
						}
					});
				});
			}
		}
	
	}
})