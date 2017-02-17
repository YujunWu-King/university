SurveyBuild.extend("StartBusinessExp", "baseComponent", {
	itemName: "创业经历",
	title: "创业经历",
	isDoubleLine: "Y",
	fixedContainer: "Y",//固定容器标识
	minLines: "1",
	maxLines: "4",
	children: {
		//创业类型
		"WorkExper1": {
			"instanceId": "WorkExper1",
			"itemId": "business_type",
			"itemName": MsgSet["BUSINESS_TYPE"],
			//"itemName":"创业类型",
			"title": MsgSet["BUSINESS_TYPE"],
			"title": "创业类型",
			"orderby": 1,
			"value": "",
			"StorageType": "S",
			"classname": "Select"
		},
		//融资情况
		"WorkExper2": {
			"instanceId": "WorkExper2",
			"itemId": "financing_type",
			"itemName": MsgSet["FINANCING"],
			//"itemName": "融资情况",
			"title": MsgSet["FINANCING"],
			//"title": "融资情况",
			"orderby": 2,
			"value": "",
			"format":"5",
			"StorageType": "S",
			"classname": "SingleTextBox"
		},
		"WorkExper2_1": {
			"instanceId": "WorkExper2_1",
			"itemId": "financing_binput",
			"itemName": MsgSet["BUSINESS_TYPE"],
			//"itemName":"创业类型",
			"title": MsgSet["BUSINESS_TYPE"],
			//"title": "创业类型",
			"orderby": 1,
			"value": "",
			"StorageType": "S",
			"classname": "SingleTextBox"
		},
		"WorkExper2_2": {
			"instanceId": "WorkExper2_2",
			"itemId": "financing_ainput",
			"orderby": 1,
			"value": "",
			"StorageType": "S",
			"classname": "SingleTextBox"
		},
		"WorkExper2_3": {
			"instanceId": "WorkExper2_3",
			"itemId": "financing_anginput",
			"orderby": 1,
			"value": "",
			"StorageType": "S",
			"classname": "SingleTextBox"
		},
		//SingleTextBox
		"WorkExper3": {
			"instanceId": "WorkExper3",
			"itemId": "income_y",
			"itemName": MsgSet["INCOME"],
			//"itemName":"近12个月收入",
			"title": MsgSet["INCOME"],
			//"title": "近12个月收入",
			"orderby": 1,
			"value": "",
			"StorageType": "S",
			"classname": "SingleTextBox"

		},
		"WorkExper4": {
			"instanceId": "WorkExper4",
			"itemId": "user_num",
			"itemName": MsgSet["USER_NUM"],
			//"itemName":"用户数",
			"title": MsgSet["USER_NUM"],
			//"title": "用户数",
			"orderby": 1,
			"value": "",
			"StorageType": "S",
			"classname": "SingleTextBox"

		},
		"WorkExper5": {
			"instanceId": "WorkExper5",
			"itemId": "own_money",
			"itemName": MsgSet["OWN_MONEY"],
			//"itemName":"自有资金",
			"title": MsgSet["OWN_MONEY"],
			//"title": "自有资金",
			"orderby": 1,
			"value": "",
			"StorageType": "S",
			"classname": "SingleTextBox"
		},
		"WorkExper6": {
			"instanceId": "WorkExper6",
			"itemId": "family_money",
			"itemName": MsgSet["FAMILY_MONEY"],
			//"itemName":"家族企业资产",
			"title": MsgSet["FAMILY_MONEY"],
			//"title": "家族企业资产",
			"orderby": 1,
			"value": "",
			"StorageType": "S",
			"classname": "SingleTextBox"
		},
		"WorkExper7": {
			"instanceId": "WorkExper7",
			"itemId": "income_o",
			"itemName": MsgSet["INCOME"],
			//"itemName":"近12个月收入",
			"title": MsgSet["INCOME"],
			//"title": "近12个月收入",
			"value":"",
			"orderby": 1,
			"StorageType": "S",
			"classname": "SingleTextBox"

		},
		"WorkExper8": {
			"instanceId": "WorkExper8",
			"itemId": "year_profit",
			"itemName": MsgSet["YEAR_PROFIT"],
			//"itemName":"年纯利润",
			"title": MsgSet["YEAR_PROFIT"],
			//"title": "年纯利润",
			"value":"",
			"orderby": 1,
			"StorageType": "S",
			"classname": "SingleTextBox"
		},
		"WorkExper9": {
			"instanceId": "WorkExper9",
			"itemId": "firm_scale",
			"itemName": MsgSet["FIRM_SCALE"],
			//"itemName":"企业规模",
			"title": MsgSet["FIRM_SCALE"],
			//"title": "企业规模",
			"value":"",
			"orderby": 1,
			"StorageType": "S",
			"classname": "SingleTextBox"
		}
	},
	//minLines: "1",
	//maxLines: "4",
	//linesNo: [1, 2, 3],
	_getHtml: function(data, previewmode) {
		var c = "";
		var opt = "",x = "";
		if (previewmode) {
			var htmlContent = this._getContentHtml(data);

			c += '<div class="main_inner_content_top"></div>';
			c += '<div class="main_inner_content">';
			c += htmlContent;
			c += '</div>';
			c += '<div class="main_inner_content_foot"></div>';

		} else {
			var htmlDisplay = '';
			//创业类型 MsgSet["BUSINESS_TYPE"]
			htmlDisplay += '<div class="type_item_li">';
			htmlDisplay += '	<span class="type_item_label">'+ MsgSet["BUSINESS_TYPE"]+':</span>';
			htmlDisplay += '		<b class="read-select" style="min-width:120px;">--'+MsgSet["PLEASE_SELECT"]+'--</b>';
			htmlDisplay += '	</div>';

			//融资情况 sgSet["FINANCING"]  "请选择"：MsgSet["PLEASE_SELECT"]
			htmlDisplay += '<div class="type_item_li">';
			htmlDisplay += '	<span class="type_item_label">'+"融资情况"+':</span>';
			htmlDisplay += '		<b class="read-select" style="min-width:120px;">--'+MsgSet["PLEASE_SELECT"]+'--</b>';
			htmlDisplay += '	</div>';
			
			
			c += '<div class="question-answer"  style="border:1px solid #ddd;padding:10px;">';
			c += '	<div class="DHContainer">' + htmlDisplay + '</div>';
			c += '</div>';
		}
		return c;
	},
	_edit: function(data) {
//		var e = '';//所有都为必填
		//规则设置
//		e += '<div class="edit_jygz">';
//		e += '	    <span class="title"><i class="icon-cog"></i> 校验规则</span>';
//		e += '      <div class="groupbox">';
//		e += '          <div class="edit_item_warp" style="margin-top:5px;">';
//		e += '              <input class="mbIE" type="checkbox" onchange="SurveyBuild.saveAttr(this,\'isRequire\')"' + (data.isRequire == "Y" ? "checked='checked'": "") + ' id="is_require">';
//		e += '                 <label for="is_require">是否必填';
//		e += '                  <a href="#" data-for-id="help_isRequire" onclick="SurveyBuild.showMsg(this,event)" class="big-link" data-reveal-id="myModal" data-animation="fade">(?)</a>';
//		e += '                 </label>';
//		e += '          </div>';
//		e += '      </div>';
//		//高级设置
//		e += '      <div class="edit_item_warp">';
//		e += '          <a href="javascript:void(0);" onclick="SurveyBuild.RulesSet(this);"><i class="icon-cogs"></i> 高级设置</a>';
//		e += '		    <a href="#" data-for-id="help_advancedSetup" onclick="SurveyBuild.showMsg(this,event)" class="big-link" data-reveal-id="myModal" data-animation="fade">(?)</a>';
//		e += '      </div>';
//		e += '</div>';
//		return e;
	},
	_getContentHtml: function(data) {
		//用在拼写给定的div排列
		console.dir(data);
		var child=data["children"][0];
		if (child == undefined) {
	   		 child=data["children"];
	   	 	}
		//var child=data["children"];
		var BUSINESS_TYPE_GP=[
			"互联网类",//01
			"家族企业",//02
			"其他"     //03
		];	
		var htmlContent="";
		//--创业类型初始值
		console.dir(child);
		var BUSINESS_TYPE_DEF=child.WorkExper1.value;
		//--融资情况初始值
		var FINANCING_DEL=child.WorkExper2.value;
		//--------------------------------------------放入创业类型下拉框html
			htmlContent += '<div class="main_inner_content_para" >';
			
			htmlContent += '<div class="main_inner_content_top"></div><div class="padding_div"></div><div class="main_inner_content_foot"></div>';

			//1.创业类型
			if (SurveyBuild._readonly) {
				//--只读模式   完全还原填写模式，将div中所有可变属性设置成只读模式
				//------只读模式 1下拉框
				var OPT_BTYPE='';
				var btypeDesc='';
				for(var k=0;k<BUSINESS_TYPE_GP.length;k++){
					//OPT_BTYPE+='<option value="0'+parseInt(k+1)+'"'+(BUSINESS_TYPE_DEF=="0"+parseInt(k+1)?'selected="selected"': '')+'>'+BUSINESS_TYPE_GP[k]+'</option>';
					if(BUSINESS_TYPE_DEF=="0"+parseInt(k+1)){
						btypeDesc=BUSINESS_TYPE_GP[k];
					}
				}
				//----------------------------放入创业类型OPT "请选择"：MsgSet["PLEASE_SELECT"]
				htmlContent += '<div class="input-list">';
				htmlContent += '	<div class="input-list-info left"><span class="red">*</span>' + child.WorkExper1.itemName + ':</div>';
				htmlContent += '	<div class="input-list-text left">';
				htmlContent +=btypeDesc;
				htmlContent += '	</div>';
				htmlContent += '</div>';
				
				//------只读模式 2可变DIV
				//----第一次只显示互联网相关div 其他关联div隐藏
				var NET_DIV='';	
					//初始值设定
				var finDesc='';
				var incomeYDesc='';
				var peopleDesc='';
				var ownMoneyDesc='';
				var familyMoney='';
				if(BUSINESS_TYPE_DEF=="01"||BUSINESS_TYPE_DEF==""){
					NET_DIV+='<div id="NET_TYPE_SHOW">'
					incomeYDesc=child.WorkExper3.value+MsgSet["ONE_MILLION"];
					peopleDesc=child.WorkExper4.value+MsgSet["PEOPLES"];	
					if(FINANCING_DEL=="B_FINANCING"){
						finDesc=MsgSet["B_FINANCING"]+child.WorkExper2_1.value+MsgSet["ONE_MILLION"];
					}else if(FINANCING_DEL=="A_FINANCING"){
						finDesc=MsgSet["A_FINANCING"]+child.WorkExper2_2.value+MsgSet["ONE_MILLION"];
						ownMoneyDesc=child.WorkExper3.value+MsgSet["ONE_MILLION"];
						familyMoney=child.WorkExper3.value+MsgSet["ONE_MILLION"];
					}else if(FINANCING_DEL=="ANGEL_INVEST"){
						finDesc=MsgSet["ANGEL_INVEST"]+child.WorkExper2_3.value+MsgSet["ONE_MILLION"];
					}else if(FINANCING_DEL="NO_FINANCING"){
						finDesc=MsgSet["ANGEL_INVEST"];
					}else if(FINANCING_DEL="NEW_CREATE"){
						finDesc=MsgSet["NEW_CREATE"];
					}
				}else{
					NET_DIV+='<div id="NET_TYPE_SHOW" style="display:none">'
				}
					//<!--融资标题-->
					NET_DIV+='<div class="input-list-info left"><span class="red">*</span>'+MsgSet["FINANCING"]+':</div>'
					//<!---融资radioGroup-->	
					NET_DIV+='<div  class="input-list-text left">'
					NET_DIV+=finDesc;	
					NET_DIV+='</div>'	
					
					//<!--收入140px-->
					NET_DIV+='<div id="income_y" style="margin-top:10px" class="input-list">'	
						NET_DIV+='<div  class="input-list-info left"><span class="red-star">*</span>'+MsgSet["INCOME"]+':</div>'
						NET_DIV+='<div class="input-list-text left">'
							NET_DIV+=incomeYDesc;	
						NET_DIV+='</div>'
					
					NET_DIV+='</div>'	
				
					//<!--用户数20px-->
					NET_DIV+='<div id="people" style="margin-top:10px" class="input-list">'
						NET_DIV+='<div  class="input-list-info left"><span class="red-star">*</span>'+MsgSet["USER_NUM"]+':</div>'
						NET_DIV+='<div class="input-list-text left"  >'
							NET_DIV+=peopleDesc;	
						NET_DIV+='</div>'
					NET_DIV+='</div>'	
				NET_DIV+='</div>'
				//-----家族企业关联的DIV
				var FAM_DIV='';
					//初始值设定
				var ownMoneyDesc='';
				var familyMoneyDesc='';
				if(BUSINESS_TYPE_DEF=="02"){
					FAM_DIV+='<div id="FAMILY_TYPE_SHOW">'
					ownMoneyDesc=child.WorkExper5.value+MsgSet["ONE_MILLION"];	
					familyMoneyDesc=child.WorkExper6.value+MsgSet["ONE_MILLION"];	
				}else{
					FAM_DIV+='<div id="FAMILY_TYPE_SHOW" style="display:none;">'
				}	
					//<!--自有资金-->
					FAM_DIV+='<div id="own_money" class="input-list">'
						FAM_DIV+='<div  class="input-list-info left"><span class="red-star">*</span>'+MsgSet["OWN_MONEY"]+':</div>'
						FAM_DIV+='<div class="input-list-text left"  >'
							FAM_DIV+=ownMoneyDesc;	
						FAM_DIV+='</div>'
					FAM_DIV+='</div>'		
					//<!--家族企业资产-->
					FAM_DIV+='<div id="family_money" class="input-list">'	
						FAM_DIV+='<div  class="input-list-info left"><span class="red-star">*</span>'+MsgSet["FAMILY_MONEY"]+':</div>'
						FAM_DIV+='<div class="input-list-text left"  >'
							FAM_DIV+=familyMoneyDesc;	
						FAM_DIV+='</div>'	
				FAM_DIV+='</div>'	
				//------其他创业类型关联的DIV--------
				var OTH_DIV='';
					//初始值设定
				var incomeODesc='';
				var yearIncomeDesc='';
				var firmScaleDesc='';
				if(FINANCING_DEL=="03"){
					OTH_DIV+='<div id="OTHER_TYPE_SHOW">'
					incomODesc=	child.WorkExper7.value+MsgSet["ONE_MILLION"];
					yearIncomeDesc=	child.WorkExper8.value+MsgSet["ONE_MILLION"];
					firmScaleDesc=	child.WorkExper9.value+MsgSet["PEOPLES"];
				}else{
					OTH_DIV+='<div id="OTHER_TYPE_SHOW" style="display:none;">'
				}
					//<!--'+MsgSet["INCOME"]+'-->
					OTH_DIV+='<div id="income_o" class="input-list">'
						OTH_DIV+='<div  class="input-list-info left">'+MsgSet["INCOME"]+':</div>'
						OTH_DIV+='<div class="input-list-text left"  >'
							OTH_DIV+=incomeODesc;
						OTH_DIV+='</div>'	
					OTH_DIV+='</div>'	
					//<!--'+MsgSet["YEAR_PROFIT"]+'-->
					OTH_DIV+='<div id="year_income" class="input-list">'
						OTH_DIV+='<div  class="input-list-info left">'+MsgSet["YEAR_PROFIT"]+':</div>'
						OTH_DIV+='<div class="input-list-text left"  >'
							OTH_DIV+=yearIncomeDesc;
						OTH_DIV+='</div>'	
					OTH_DIV+='</div>'	
					//<!--企业规模-->
					OTH_DIV+='<div id="firm_scale" class="input-list">'
						OTH_DIV+='<div  class="input-list-info left">'+MsgSet["FIRM_SCALE"]+':</div>'
						OTH_DIV+='<div class="input-list-text left"  >'
							OTH_DIV+=firmScaleDesc;
						OTH_DIV+='</div>'
					OTH_DIV+='</div>'	
				OTH_DIV+='</div>'		
				//-------------------------------
				htmlContent+=NET_DIV
				htmlContent+=FAM_DIV
				htmlContent+=OTH_DIV
				//---------------------------------------------------------

			} else {
				//填写模式
				htmlContent+='<div class="clear"></div>'
				var OPT_BTYPE='';
				for(var k=0;k<BUSINESS_TYPE_GP.length;k++){
					OPT_BTYPE+='<option value="0'+parseInt(k+1)+'"'+(BUSINESS_TYPE_DEF=="0"+parseInt(k+1)?'selected="selected"': '')+'>'+BUSINESS_TYPE_GP[k]+'</option>';
				}
				//----------------------------放入创业类型OPT "请选择"：MsgSet["PLEASE_SELECT"]
				htmlContent += '<div class="input-list">';
				htmlContent += '	<div class="input-list-info left"><span class="red">*</span>' + child.WorkExper1.itemName + ':</div>';
				htmlContent += '	<div class="input-list-text left input-edu-select">';
				htmlContent += '		<select id="' + data["itemId"] + child.WorkExper1.itemId + '" class="chosen-select" style="width:100%;" data-regular="" title="' + child.WorkExper1.itemName + '" value="' + child.WorkExper1["value"] + '" name="' + data["itemId"] + child.WorkExper1.itemId + '">';
				//htmlContent += '			<option value="-1">' + '--'+"请选择"+'--' + '</option>';
				htmlContent += OPT_BTYPE;
				htmlContent += '		</select>';
				//----------------------------
				htmlContent += '		<div style="margin-top:-40px;margin-left:365px"><div id="' + data["itemId"] + child.WorkExper1.itemId + 'Tip" class="onCorrect" style="margin: 0px; padding: 0px; background: transparent;">';
				htmlContent += '			<div class="onCorrect">&nbsp;</div></div>';
				htmlContent += '		</div>';
				htmlContent += '	</div>';
				htmlContent += '</div>';


			//2.融资情况:
				//----第一次只显示互联网相关div 其他关联div隐藏
				var NET_DIV=''
					//初始值设定
				if(BUSINESS_TYPE_DEF=="01"||BUSINESS_TYPE_DEF==""){
					NET_DIV+='<div id="NET_TYPE_SHOW">'
				}else{
					NET_DIV+='<div id="NET_TYPE_SHOW" style="display:none">'
				}
					//<!--融资标题-->
					NET_DIV+='<div class="input-list-info left" style="height:100px"><span class="red">*</span>'+MsgSet["FINANCING"]+':</div>'
					//<!---融资radioGroup-->	
					NET_DIV+='<div class="input-list-text left" style="height:240px" >'
						//<!--加入一个隐藏input缓存radio数据-->
						NET_DIV+='<input type="hidden" id="'+ data["itemId"] + child.WorkExper2.itemId + '" value="'+child.WorkExper2.value + '"/>'
						NET_DIV+='<input type="radio" name="financing_type"'+(FINANCING_DEL=="B_FINANCING"?'checked="checked"': '')+' value="B_FINANCING" />'+MsgSet["B_FINANCING"]+'<span id="'+ data["itemId"] + child.WorkExper2.itemId + '_bspan"'+(FINANCING_DEL=="B_FINANCING"?'':' style="display:none"')+'><input id="'+data["itemId"] + child.WorkExper2_1.itemId+'" value="'+child.WorkExper2_1.value+'" style="width:120px;margin-left:5px"/><span>'+MsgSet["ONE_MILLION"]+'</span></span><br/>'	
						NET_DIV+='<input type="radio" name="financing_type"'+(FINANCING_DEL=="A_FINANCING"?'checked="checked"': '')+' value="A_FINANCING" >'+MsgSet["A_FINANCING"]+'<span id="'+ data["itemId"] + child.WorkExper2.itemId + '_aspan"'+(FINANCING_DEL=="A_FINANCING"?'':' style="display:none"')+'><input id="'+data["itemId"] + child.WorkExper2_2.itemId+'" value="'+child.WorkExper2_2.value+'" style="width:120px;margin-left:5px"/><span>'+MsgSet["ONE_MILLION"]+'</span></span><br/>'	
						NET_DIV+='<input type="radio" name="financing_type"'+(FINANCING_DEL=="ANGEL_INVEST"?'checked="checked"': '')+' value="ANGEL_INVEST" />'+MsgSet["ANGEL_INVEST"]+'<span id="'+ data["itemId"] + child.WorkExper2.itemId + '_angspan"'+(FINANCING_DEL=="ANGEL_INVEST"?'':' style="display:none"')+'><input id="'+data["itemId"] + child.WorkExper2_3.itemId+'" value="'+child.WorkExper2_3.value+'" style="width:120px;margin-left:5px"/><span>'+MsgSet["ONE_MILLION"]+'</span></span><br/>'	
						NET_DIV+='<input type="radio" name="financing_type"'+(FINANCING_DEL=="NO_FINANCING"?'checked="checked"': '')+' value="NO_FINANCING" />'+MsgSet["NO_FINANCING"]+'<br/>'	
						NET_DIV+='<input type="radio" name="financing_type"'+(FINANCING_DEL=="NEW_CREATE"?'checked="checked"': '')+' value="NEW_CREATE" />'+MsgSet["NEW_CREATE"]+''	
					NET_DIV+='</div>'	
					
					//<!--收入140px-->
					NET_DIV+='<div id="income_y" style="margin-top:10px" class="input-list">'	
						NET_DIV+='<div  class="input-list-info left"><span class="red-star">*</span>'+MsgSet["INCOME"]+':</div>'
						NET_DIV+='<div class="input-list-text left"><input class="inpu-list-text-enter" style="height:35px; " id="'+ data["itemId"] + child.WorkExper3.itemId + '" value="'+child.WorkExper3.value+'"/><span>'+MsgSet["ONE_MILLION"]+'</span>'
						//---------NET_DIV input格式检验DIV:
						NET_DIV += '<div style="margin-top: -40px; margin-left: 365px">';
						NET_DIV += '	<div id="' + data.itemId+child.WorkExper3.itemId + 'Tip" class="onShow" style="margin: 0px; padding: 0px; background: transparent;">';
						NET_DIV += '		<div class="onShow">test</div>';
						NET_DIV += '	</div>';
						NET_DIV += '</div>';
						//---------
					NET_DIV+='</div></div>'					
					//<!--用户数20px-->
					NET_DIV+='<div id="people" style="margin-top:10px" class="input-list">'
						NET_DIV+='<div  class="input-list-info left"><span class="red-star">*</span>'+MsgSet["USER_NUM"]+':</div>'
						NET_DIV+='<div class="input-list-text left"  ><input class="inpu-list-text-enter" style="height:35px; " id="'+ data["itemId"] + child.WorkExper4.itemId + '" value="'+child.WorkExper4.value+'" /><span>'+MsgSet["PEOPLES"]+'</span>'
						//---------NET_DIV input格式检验DIV:
						NET_DIV += '<div style="margin-top: -40px; margin-left: 365px">';
						NET_DIV += '	<div id="' + data.itemId+child.WorkExper4.itemId + 'Tip" class="onShow" style="margin: 0px; padding: 0px; background: transparent;">';
						NET_DIV += '		<div class="onShow">test</div>';
						NET_DIV += '	</div>';
						NET_DIV += '</div>';
						//---------
					NET_DIV+='</div></div>'	
					NET_DIV+='<div class="clear"></div>'
				NET_DIV+='</div>'
				//-----家族企业关联的DIV
				var FAM_DIV='';
					//初始值设定
				if(BUSINESS_TYPE_DEF=="02"){
					FAM_DIV+='<div id="FAMILY_TYPE_SHOW">'
				}else{
					FAM_DIV+='<div id="FAMILY_TYPE_SHOW" style="display:none;">'
				}	
					//<!--自有资金-->
					FAM_DIV+='<div id="own_money" class="input-list">'
						FAM_DIV+='<div  class="input-list-info left"><span class="red-star">*</span>'+MsgSet["OWN_MONEY"]+':</div>'
						FAM_DIV+='<div class="input-list-text left"  ><input class="inpu-list-text-enter" style="height:35px; " id="'+ data["itemId"] + child.WorkExper5.itemId + '" value="'+child.WorkExper5.value+'"/><span>'+MsgSet["ONE_MILLION"]+'</span>'
						//---------FAM_DIV input格式检验DIV:
						FAM_DIV += '<div style="margin-top: -40px; margin-left: 365px">';
						FAM_DIV += '	<div id="' + data.itemId+child.WorkExper5.itemId + 'Tip" class="onShow" style="margin: 0px; padding: 0px; background: transparent;">';
						FAM_DIV += '		<div class="onShow">test</div>';
						FAM_DIV += '	</div>';
						FAM_DIV += '</div>';
						//---------
					FAM_DIV+='</div></div>'		
					//<!--家族企业资产-->
					FAM_DIV+='<div id="family_money" class="input-list">'	
						FAM_DIV+='<div  class="input-list-info left"><span class="red-star">*</span>'+MsgSet["FAMILY_MONEY"]+':</div>'
						FAM_DIV+='<div class="input-list-text left"  ><input class="inpu-list-text-enter" style="height:35px; " id="'+ data["itemId"] + child.WorkExper6.itemId + '" value="'+child.WorkExper6.value+'"/><span>'+MsgSet["ONE_MILLION"]+'</span>'
						//---------FAM_DIV input格式检验DIV:
						FAM_DIV += '<div style="margin-top: -40px; margin-left: 365px">';
						FAM_DIV += '	<div id="' + data.itemId+child.WorkExper6.itemId + 'Tip" class="onShow" style="margin: 0px; padding: 0px; background: transparent;">';
						FAM_DIV += '		<div class="onShow">test</div>';
						FAM_DIV += '	</div>';
						FAM_DIV += '</div>';
						//----------------------
					FAM_DIV+='</div></div>'
					FAM_DIV+='<div class="clear"></div>'		
				FAM_DIV+='</div>'	
				//------其他创业类型关联的DIV--------
				var OTH_DIV='';
					//初始值设定
				if(BUSINESS_TYPE_DEF=="03"){
					OTH_DIV+='<div id="OTHER_TYPE_SHOW">'
				}else{
					OTH_DIV+='<div id="OTHER_TYPE_SHOW" style="display:none;">'
				}
					//<!--近12个月收入-->
					OTH_DIV+='<div id="income_o" class="input-list">'
						OTH_DIV+='<div  class="input-list-info left" ><span class="red-star">*</span>'+MsgSet["INCOME"]+':</div>'
						OTH_DIV+='<div class="input-list-text left"  ><input class="inpu-list-text-enter" style="height:35px; " id="'+ data["itemId"] + child.WorkExper7.itemId + '" value="'+child.WorkExper7.value+'"/><span>'+MsgSet["ONE_MILLION"]+'</span>'	
						//---------FAM_DIV input格式检验DIV:
						OTH_DIV += '<div style="margin-top: -40px; margin-left: 365px">';
						OTH_DIV += '	<div id="' + data.itemId+child.WorkExper7.itemId + 'Tip" class="onShow" style="margin: 0px; padding: 0px; background: transparent;">';
						OTH_DIV += '		<div class="onShow">test</div>';
						OTH_DIV += '	</div>';
						OTH_DIV += '</div>';
						//----------------------
					OTH_DIV+='</div></div>'	
					//<!--年纯利润-->
					OTH_DIV+='<div id="year_income" class="input-list">'
						OTH_DIV+='<div  class="input-list-info left"><span class="red-star">*</span>'+MsgSet["YEAR_PROFIT"]+':</div>'
						OTH_DIV+='<div class="input-list-text left"  ><input class="inpu-list-text-enter" style="height:35px; " id="'+ data["itemId"] + child.WorkExper8.itemId + '" value="'+child.WorkExper8.value+'"/><span>'+MsgSet["ONE_MILLION"]+'</span>'
						//---------OTH_DIV input格式检验DIV:
						OTH_DIV += '<div style="margin-top: -40px; margin-left: 365px">';
						OTH_DIV += '	<div id="' + data.itemId+child.WorkExper8.itemId + 'Tip" class="onShow" style="margin: 0px; padding: 0px; background: transparent;">';
						OTH_DIV += '		<div class="onShow">test</div>';
						OTH_DIV += '	</div>';
						OTH_DIV += '</div>';
						//----------------------
					OTH_DIV+='</div></div>'	
					//<!--企业规模-->
					OTH_DIV+='<div id="firm_scale" class="input-list">'
						OTH_DIV+='<div  class="input-list-info left"><span class="red-star">*</span>'+MsgSet["FIRM_SCALE"]+':</div>'
						OTH_DIV+='<div class="input-list-text left"  ><input class="inpu-list-text-enter" style="height:35px; " id="'+ data["itemId"] + child.WorkExper9.itemId + '" value="'+child.WorkExper9.value+'"/><span>'+MsgSet["PEOPLES"]+'</span>'	
						//---------OTH_DIV input格式检验DIV:
						OTH_DIV += '<div style="margin-top: -40px; margin-left: 365px">';
						OTH_DIV += '	<div id="' + data.itemId+child.WorkExper9.itemId + 'Tip" class="onShow" style="margin: 0px; padding: 0px; background: transparent;">';
						OTH_DIV += '		<div class="onShow">test</div>';
						OTH_DIV += '	</div>';
						OTH_DIV += '</div>';
						//----------------------
					OTH_DIV+='</div></div>'
				OTH_DIV+='</div>'
				OTH_DIV+='<div class="clear"></div>'	
				//-------------------------------
				htmlContent+=NET_DIV
				htmlContent+=FAM_DIV
				htmlContent+=OTH_DIV
			}
		
			htmlContent+="</div>";
		return htmlContent;
	},
	_eventbind: function(data) {
		//-----根据"创业类型"下拉框 显示和隐藏"类型关联div"
		var child = data["children"][0];
		if (child == undefined) {
	   		 child=data["children"];
	   	 	}
		//var child = data["children"];
		var $btype_select = $("#" +data["itemId"] + child.WorkExper1.itemId);
		$btype_select.change(function(){
			var btypeVal=$btype_select.val();
			//console.log("blur");
			if(btypeVal=="01"){
				$("#NET_TYPE_SHOW").css("display","block");
				//清空除开"互联网"之外的数据
				$("#"+data["itemId"] + child.WorkExper5.itemId).val("");
				$("#"+data["itemId"] + child.WorkExper6.itemId).val("");
				
				$("#"+data["itemId"] + child.WorkExper7.itemId).val("");
				$("#"+data["itemId"] + child.WorkExper8.itemId).val("");
				$("#"+data["itemId"] + child.WorkExper9.itemId).val("");
				//---------------------
			}else{
				$("#NET_TYPE_SHOW").css("display","none");
			}
			if(btypeVal=="02"){
				$("#FAMILY_TYPE_SHOW").css("display","block");
				//清空除开"家族企业"之外的数据
				$("#"+data["itemId"] + child.WorkExper2.itemId).val("");
				$("#"+data["itemId"] + child.WorkExper2_1.itemId).val("");
				$("#"+data["itemId"] + child.WorkExper2_2.itemId).val("");
				$("#"+data["itemId"] + child.WorkExper2_3.itemId).val("");
				$("#"+data["itemId"] + child.WorkExper3.itemId).val("");
				$("#"+data["itemId"] + child.WorkExper4.itemId).val("");
				
				$("#"+data["itemId"] + child.WorkExper7.itemId).val("");
				$("#"+data["itemId"] + child.WorkExper8.itemId).val("");
				$("#"+data["itemId"] + child.WorkExper9.itemId).val("");
				//-----------------------
			}else{
				$("#FAMILY_TYPE_SHOW").css("display","none");
			}
			if(btypeVal=="03"){
				$("#OTHER_TYPE_SHOW").css("display","block");
				//清除除开"其他"之外所有数据
				$("#"+data["itemId"] + child.WorkExper2.itemId).val("");
				$("#"+data["itemId"] + child.WorkExper2_1.itemId).val("");
				$("#"+data["itemId"] + child.WorkExper2_2.itemId).val("");
				$("#"+data["itemId"] + child.WorkExper2_3.itemId).val("");
				$("#"+data["itemId"] + child.WorkExper3.itemId).val("");
				$("#"+data["itemId"] + child.WorkExper4.itemId).val("");
				
				$("#"+data["itemId"] + child.WorkExper5.itemId).val("");
				$("#"+data["itemId"] + child.WorkExper6.itemId).val("");
				
			}else{
				$("#OTHER_TYPE_SHOW").css("display","none");
				//
			}
		});
		//----"互联网"类型下，radio的切换处理
		$("input[name='financing_type']").click(function(){
			//alert($(this).val());
			var financingTypeVal=($(this).val());
			//获取5个radio的句柄
			var b_financing_s=$("#"+data["itemId"] + child.WorkExper2.itemId + "_bspan");
			var a_financing_s=$("#"+data["itemId"] + child.WorkExper2.itemId + "_aspan");
			var ang_financing_s=$("#"+data["itemId"] + child.WorkExper2.itemId + "_angspan");

			//---radio-1
			if(financingTypeVal=="B_FINANCING"){
				//$("#B_FINANCING_S").css("display","inline");
				b_financing_s.css("display","inline");
			}else{
				//$("#B_FINANCING_S").css("display","none");
				b_financing_s.css("display","none");
			}
			//---radio-2
			if(financingTypeVal=="A_FINANCING"){
				//$("#A_FINANCING_S").css("display","inline");
				a_financing_s.css("display","inline");
			}else{
				//$("#A_FINANCING_S").css("display","none");
				a_financing_s.css("display","none");
				
			}
			//---radio-3
			if(financingTypeVal=="ANGEL_INVEST"){
				//$("#ANGEL_INVEST_S").css("display","inline");
				ang_financing_s.css("display","inline");
			}else{
				//$("#ANGEL_INVEST_S").css("display","none");
				ang_financing_s.css("display","none");
				//清除天使融资之外的数据
				//--------------------
			}
			//-----------将raido中的数据放入radioGroup下的隐藏input中
			var radioValInput=$("#"+data["itemId"] + child.WorkExper2.itemId);
			radioValInput.val(financingTypeVal);
			//console.dir("radioValInput:");
			//console.dir(radioValInput);
		});
		
		//---所有的input,select非空验证:
		$("select").each(function(){
			//$(this).chosen({width:"100%"});
			$(this).formValidator({tipID:($(this).attr("id")+'Tip'), onShow:"", onFocus:"&nbsp;", onCorrect:"&nbsp;"});
			$(this).functionValidator({
				fun:function(val,el){
					if(val==""||val=="-1")
						return "此项必选";
				}	
			});
		});
		$("input").each(function(){
			$(this).formValidator({tipID:($(this).attr("id")+'Tip'), onShow:"", onFocus:"&nbsp;", onCorrect:"&nbsp;"});
			$(this).functionValidator({
				fun:function(val,el){
					if(val=="")
						return "此项必填";
				}	
			});
		});
		}
})