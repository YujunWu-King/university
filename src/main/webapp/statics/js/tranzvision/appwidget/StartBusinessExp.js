SurveyBuild.extend("StartBusinessExp", "baseComponent", {
	itemName: "创业经历",
	title: "创业经历",
	isDoubleLine: "Y",
	isSingleLine:"Y",
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
			var htmlContent;

			//手机
			if(SurveyBuild.accessType == "M"){

				htmlContent = this._getContentHtmlP(data);
				c += htmlContent;
			}else{

				htmlContent = this._getContentHtml(data);

				c += '<div class="main_inner_content_top"></div>';
				c += '<div class="main_inner_content">';
				c += htmlContent;
				c += '</div>';
				c += '<div class="main_inner_content_foot"><div class="clear"></div></div>';

			}

		} else {
			var htmlDisplay = '';
			//创业类型 MsgSet["BUSINESS_TYPE"]
			htmlDisplay += '<div class="type_item_li">';
			htmlDisplay += '	<span class="type_item_label">'+ MsgSet["BUSINESS_TYPE"]+':</span>';
			htmlDisplay += '		<b class="read-select" style="min-width:120px;">'+MsgSet["PLEASE_SELECT"]+'</b>';
			htmlDisplay += '	</div>';

			//融资情况 sgSet["FINANCING"]  "请选择"：MsgSet["PLEASE_SELECT"]
			htmlDisplay += '<div class="type_item_li">';
			htmlDisplay += '	<span class="type_item_label">'+MsgSet["FINANCING"]+':</span>';
			htmlDisplay += '		<b class="read-select" style="min-width:120px;">'+MsgSet["PLEASE_SELECT"]+'</b>';
			htmlDisplay += '	</div>';
			
			
			c += '<div class="question-answer"  style="border:1px solid #ddd;padding:10px;">';
			c += '	<div class="DHContainer">' + htmlDisplay + '</div>';
			c += '</div>';
		}
		return c;
	},
	_edit: function(data) {
		var e = '';//所有都为必填
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
		return e;
	},
	_getContentHtml: function(data) {
		//用在拼写给定的div排列
		//console.dir(data);
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
		//console.dir(child);
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
				htmlContent += '	<div class="input-list-info left"> ' + child.WorkExper1.itemName + '：</div>';
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
					}else if(FINANCING_DEL=="NO_FINANCING"){
						finDesc=MsgSet["NO_FINANCING"];
					}else if(FINANCING_DEL=="NEW_CREATE"){
						finDesc=MsgSet["NEW_CREATE"];
					}
				}else{
					NET_DIV+='<div id="NET_TYPE_SHOW" style="display:none">'
				}
					//<!--融资标题-->
					NET_DIV+='<div class="input-list-info left"> '+MsgSet["FINANCING"]+'：</div>'
					//<!---融资radioGroup-->	
					NET_DIV+='<div  class="input-list-text left">'
					NET_DIV+=finDesc;	
					NET_DIV+='</div>'	
					
					//<!--收入140px-->
					NET_DIV+='<div id="income_y" style="margin-top:10px" class="input-list">'	
						NET_DIV+='<div  class="input-list-info left">'+MsgSet["INCOME"]+'：</div>'
						NET_DIV+='<div class="input-list-text left">'
							NET_DIV+=incomeYDesc;	
						NET_DIV+='</div>'
					
					NET_DIV+='</div>'	
				
					//<!--用户数20px-->
					NET_DIV+='<div id="people" style="margin-top:10px" class="input-list">'
						NET_DIV+='<div  class="input-list-info left">'+MsgSet["USER_NUM"]+'：</div>'
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
						FAM_DIV+='<div  class="input-list-info left">'+MsgSet["OWN_MONEY"]+'：</div>'
						FAM_DIV+='<div class="input-list-text left"  >'
							FAM_DIV+=ownMoneyDesc;	
						FAM_DIV+='</div>'
					FAM_DIV+='</div>'		
					//<!--家族企业资产-->
					FAM_DIV+='<div id="family_money" class="input-list">'	
						FAM_DIV+='<div  class="input-list-info left">'+MsgSet["FAMILY_MONEY"]+'：</div>'
						FAM_DIV+='<div class="input-list-text left"  >'
							FAM_DIV+=familyMoneyDesc;	
						FAM_DIV+='</div>'
					FAM_DIV+='</div>'		
				FAM_DIV+='</div>'	
				//------其他创业类型关联的DIV--------
				var OTH_DIV='';
					//初始值设定
				var incomeODesc='';
				var yearIncomeDesc='';
				var firmScaleDesc='';
				if(BUSINESS_TYPE_DEF=="03"){
					OTH_DIV+='<div id="OTHER_TYPE_SHOW">'
					incomeODesc=child.WorkExper7.value+MsgSet["ONE_MILLION"];
					yearIncomeDesc=	child.WorkExper8.value+MsgSet["ONE_MILLION"];
					firmScaleDesc=	child.WorkExper9.value+MsgSet["PEOPLES"];
				}else{
					OTH_DIV+='<div id="OTHER_TYPE_SHOW" style="display:none;">'
				}
					//<!--'+MsgSet["INCOME"]+'-->
					OTH_DIV+='<div id="income_o" class="input-list">'
						OTH_DIV+='<div  class="input-list-info left">'+MsgSet["INCOME"]+'：</div>'
						OTH_DIV+='<div class="input-list-text left"  >'
							OTH_DIV+=incomeODesc;
						OTH_DIV+='</div>'	
					OTH_DIV+='</div>'	
					//<!--'+MsgSet["YEAR_PROFIT"]+'-->
					OTH_DIV+='<div id="year_income" class="input-list">'
						OTH_DIV+='<div  class="input-list-info left">'+MsgSet["YEAR_PROFIT"]+'：</div>'
						OTH_DIV+='<div class="input-list-text left"  >'
							OTH_DIV+=yearIncomeDesc;
						OTH_DIV+='</div>'	
					OTH_DIV+='</div>'	
					//<!--企业规模-->
					OTH_DIV+='<div id="firm_scale" class="input-list">'
						OTH_DIV+='<div  class="input-list-info left">'+MsgSet["FIRM_SCALE"]+'：</div>'
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
				/*添加请选择*/
				OPT_BTYPE+='<option value=""'+(BUSINESS_TYPE_DEF==""?'selected="selected"': '')+'>请选择</option>';
				for(var k=0;k<BUSINESS_TYPE_GP.length;k++){
					OPT_BTYPE+='<option value="0'+parseInt(k+1)+'"'+(BUSINESS_TYPE_DEF=="0"+parseInt(k+1)?'selected="selected"': '')+'>'+BUSINESS_TYPE_GP[k]+'</option>';
				}
				//----------------------------放入创业类型OPT "请选择"：MsgSet["PLEASE_SELECT"]
				htmlContent += '<div class="input-list">';
				htmlContent += '	<div class="input-list-info left"> ' + child.WorkExper1.itemName + '：</div>';
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
				if(BUSINESS_TYPE_DEF=="01"){
					NET_DIV+='<div id="NET_TYPE_SHOW">'
				}else{
					NET_DIV+='<div id="NET_TYPE_SHOW" style="display:none">'
				}
					//<!--融资标题-->
				//原始版：
//					NET_DIV+='<div class="input-list-info left"> '+MsgSet["FINANCING"]+'：</div>'
//					//<!---融资radioGroup-->	
//					NET_DIV+='<div class="margart8 input-list-textwrap left">'
//						//<!--加入一个隐藏input缓存radio数据-->checkedRadio
//						NET_DIV+='<ul>'
//						NET_DIV+='<li name="radioData"><input type="hidden" id="'+ data["itemId"] + child.WorkExper2.itemId + '" value="'+child.WorkExper2.value + '"/></li>'
//						NET_DIV+='<li name="financingB"><div class="radio-btn '+(FINANCING_DEL=="B_FINANCING"?'checkedRadio': '')+'"><i><input type="radio" name="financing_type"'+(FINANCING_DEL=="B_FINANCING"?'checked="checked"': '')+' value="B_FINANCING"/></i></div>'+MsgSet["B_FINANCING"]+'<span id="'+ data["itemId"] + child.WorkExper2.itemId + '_bspan"'+(FINANCING_DEL=="B_FINANCING"?'':' style="display:none"')+'><input class="inpu-list-text-enter" style="width:55%;height:35px;margin-left:5px" id="'+data["itemId"] + child.WorkExper2_1.itemId+'" value="'+child.WorkExper2_1.value+'" style="width:120px;margin-left:5px"/><span>'+MsgSet["ONE_MILLION"]+'</span></span></li>'	
//						NET_DIV+='<li name="financingA"><div class="radio-btn '+(FINANCING_DEL=="A_FINANCING"?'checkedRadio': '')+'"><i><input type="radio" name="financing_type"'+(FINANCING_DEL=="A_FINANCING"?'checked="checked"': '')+' value="A_FINANCING"/></i></div>'+MsgSet["A_FINANCING"]+'<span id="'+ data["itemId"] + child.WorkExper2.itemId + '_aspan"'+(FINANCING_DEL=="A_FINANCING"?'':' style="display:none"')+'><input class="inpu-list-text-enter" style="width:55%;height:35px;margin-left:5px" id="'+data["itemId"] + child.WorkExper2_2.itemId+'" value="'+child.WorkExper2_2.value+'" style="width:120px;margin-left:5px"/><span>'+MsgSet["ONE_MILLION"]+'</span></span></li>'	
//						NET_DIV+='<li name="financingAn"><div class="radio-btn '+(FINANCING_DEL=="ANGEL_INVEST"?'checkedRadio': '')+'"><i><input type="radio" name="financing_type"'+(FINANCING_DEL=="ANGEL_INVEST"?'checked="checked"': '')+' value="ANGEL_INVEST"/></i></div>'+MsgSet["ANGEL_INVEST"]+'<span id="'+ data["itemId"] + child.WorkExper2.itemId + '_angspan"'+(FINANCING_DEL=="ANGEL_INVEST"?'':' style="display:none"')+'><input class="inpu-list-text-enter" style="width:55%;height:35px;margin-left:5px" id="'+data["itemId"] + child.WorkExper2_3.itemId+'" value="'+child.WorkExper2_3.value+'" style="width:120px;margin-left:5px"/><span>'+MsgSet["ONE_MILLION"]+'</span></span></li>'	
//						NET_DIV+='<li name="financingNo"><div class="radio-btn '+(FINANCING_DEL=="NO_FINANCING"?'checkedRadio': '')+'"><i><input type="radio" name="financing_type"'+(FINANCING_DEL=="NO_FINANCING"?'checked="checked"': '')+' value="NO_FINANCING"/></i></div>'+MsgSet["NO_FINANCING"]+'</li>'	
//						NET_DIV+='<li name="financingNe"><div class="radio-btn '+(FINANCING_DEL=="NEW_CREATE"?'checkedRadio': '')+'"><i><input type="radio" name="financing_type"'+(FINANCING_DEL=="NEW_CREATE"?'checked="checked"': '')+' value="NEW_CREATE"/></i></div>'+MsgSet["NEW_CREATE"]+'</li>'	
//						NET_DIV+='</ul>'
//					NET_DIV+='</div>'	
					//-------------------------------------修正版:
		
						//<!--融资标题-->
						NET_DIV+='<div class="input-list-info left" style="height:240px"> '+MsgSet["FINANCING"]+'：</div>'
						//<!---融资radioGroup-->	
						NET_DIV+='<div class="right" name="financingGp" style="width:65%;height:240px">'
							//隐藏DIV中的input存值
							NET_DIV+='<div name="radioData" style="display:block"><input type="hidden" id="'+ data["itemId"] + child.WorkExper2.itemId + '" value="'+child.WorkExper2.value + '"/></div>'
							//<!--B轮融资->
							NET_DIV+='<div id="FB">'
								NET_DIV+='<div name="financingB" class="input-list-text left" style="width:53%">'
									NET_DIV+='<div class="left" style="width:32%"><div name="radioInput" class="radio-btn'+(FINANCING_DEL=="B_FINANCING"?' checkedRadio': '')+'"><i><input type="radio" name="financing_type" value="B_FINANCING" '+(FINANCING_DEL=="B_FINANCING"?'checked="checked"': '')+'></i></div>'+MsgSet["B_FINANCING"]+'</div>'
									NET_DIV+='<div class="right" style="width:68%;  '+(FINANCING_DEL=="B_FINANCING"?'':' display:none')+'"><input class="inpu-list-text-enter" style="width:100%;margin-left:2.5px;margin-top:2.5px;height:35px"  id="'+data["itemId"] + child.WorkExper2_1.itemId+'" value="'+child.WorkExper2_1.value+'"></div>'
								NET_DIV+='</div>'
								NET_DIV+='<div class="input-list-suffix left" '+(FINANCING_DEL=="B_FINANCING"?'':' style="display:none"')+'>'+MsgSet["ONE_MILLION"]+'<span class="input-list-suffix-span">&nbsp;</span><div id="'+data["itemId"] + child.WorkExper2_1.itemId+'Tip" class="onShow" style="margin: 0px; padding: 0px; background: transparent;"><div class="onShow" tips="&nbsp;">&nbsp;</div></div></div>'
							NET_DIV+='</div>'
							//<!--A轮融资->
							NET_DIV+='<div id="FA">'
								NET_DIV+='<div name="financingA" class="input-list-text left" style="width:53%">'
									NET_DIV+='<div class="left" style="width:32%"><div name="radioInput" class="radio-btn'+(FINANCING_DEL=="A_FINANCING"?' checkedRadio': '')+'"><i><input type="radio" name="financing_type" value="A_FINANCING" '+(FINANCING_DEL=="A_FINANCING"?'checked="checked"': '')+'></i></div>'+MsgSet["A_FINANCING"]+'</div>'
									NET_DIV+='<div class="right" style="width:68%; '+(FINANCING_DEL=="A_FINANCING"?'':' display:none')+'"><input class="inpu-list-text-enter" style="width:100%;margin-left:2.5px;margin-top:2.5px;height:35px"  id="'+data["itemId"] + child.WorkExper2_2.itemId+'" value="'+child.WorkExper2_2.value+'"></div>'
								NET_DIV+='</div>'
								NET_DIV+='<div class="input-list-suffix left" '+(FINANCING_DEL=="A_FINANCING"?'':' style="display:none"')+'>'+MsgSet["ONE_MILLION"]+'<span class="input-list-suffix-span">&nbsp;</span><div id="'+data["itemId"] + child.WorkExper2_2.itemId+'Tip" class="onShow" style="margin: 0px; padding: 0px; background: transparent;"><div class="onShow" tips="&nbsp;">&nbsp;</div></div></div>'
							NET_DIV+='</div>'
							//<!--天使轮融资->
							NET_DIV+='<div id="FAN">'
								NET_DIV+='<div name="financingAn" class="input-list-text left" style="width:53%">'
									NET_DIV+='<div class="left" style="width:32%"><div name="radioInput" class="radio-btn'+(FINANCING_DEL=="ANGEL_INVEST"?' checkedRadio': '')+'"><i><input type="radio" name="financing_type" value="ANGEL_INVEST" '+(FINANCING_DEL=="ANGEL_INVEST"?'checked="checked"': '')+'></i></div>'+MsgSet["ANGEL_INVEST"]+'</div>'
									NET_DIV+='<div class="right" style="width:68%; '+(FINANCING_DEL=="ANGEL_INVEST"?'':' display:none')+'"><input class="inpu-list-text-enter" style="width:100%;margin-left:2.5px;margin-top:2.5px;height:35px"  id="'+data["itemId"] + child.WorkExper2_3.itemId+'" value="'+child.WorkExper2_3.value+'"></div>'
								NET_DIV+='</div>'
								NET_DIV+='<div class="input-list-suffix left" '+(FINANCING_DEL=="ANGEL_INVEST"?'':' style="display:none"')+'>'+MsgSet["ONE_MILLION"]+'<span class="input-list-suffix-span">&nbsp;</span><div id="'+data["itemId"] + child.WorkExper2_3.itemId+'Tip" class="onShow" style="margin: 0px; padding: 0px; background: transparent;"><div class="onShow" tips="&nbsp;">&nbsp;</div></div></div>'
							NET_DIV+='</div>'
							//<!--未融资->
							NET_DIV+='<div id="FNO">'
								NET_DIV+='<div name="financingNo" class="input-list-text left" style="width:53%">'
									NET_DIV+='<div class="left"><div name="radioInput" class="radio-btn'+(FINANCING_DEL=="NO_FINANCING"?' checkedRadio': '')+'"><i><input type="radio" name="financing_type" value="NO_FINANCING" '+(FINANCING_DEL=="NO_FINANCING"?'checked="checked"': '')+'></i></div>'+MsgSet["NO_FINANCING"]+'</div>'
								NET_DIV+='</div>'
							NET_DIV+='</div>'
							//<!--初创->	
							NET_DIV+='<div id="FNE">'
								NET_DIV+='<div name="financingNe" class="input-list-text left" style="width:53%">'
									NET_DIV+='<div class="left"><div name="radioInput" class="radio-btn'+(FINANCING_DEL=="NEW_CREATE"?' checkedRadio': '')+'"><i><input type="radio" name="financing_type" value="NEW_CREATE" '+(FINANCING_DEL=="NEW_CREATE"?'checked="checked"': '')+'></i></div>'+MsgSet["NEW_CREATE"]+'</div>'
								NET_DIV+='</div>'
							NET_DIV+='</div>'									
							//<!--加入一个隐藏input缓存radio数据-->checkedRadio
//							NET_DIV+='<ul>'
//							NET_DIV+='<li name="radioData"><input type="hidden" id="'+ data["itemId"] + child.WorkExper2.itemId + '" value="'+child.WorkExper2.value + '"/></li>'
//							NET_DIV+='<li name="financingB"><div class="radio-btn '+(FINANCING_DEL=="B_FINANCING"?'checkedRadio': '')+'"><i><input type="radio" name="financing_type"'+(FINANCING_DEL=="B_FINANCING"?'checked="checked"': '')+' value="B_FINANCING"/></i></div>'+MsgSet["B_FINANCING"]+'<span id="'+ data["itemId"] + child.WorkExper2.itemId + '_bspan"'+(FINANCING_DEL=="B_FINANCING"?'':' style="display:none"')+'><input class="inpu-list-text-enter" style="width:55%;height:35px;margin-left:5px" id="'+data["itemId"] + child.WorkExper2_1.itemId+'" value="'+child.WorkExper2_1.value+'" style="width:120px;margin-left:5px"/><span>'+MsgSet["ONE_MILLION"]+'</span></span></li>'	
//							NET_DIV+='<li name="financingA"><div class="radio-btn '+(FINANCING_DEL=="A_FINANCING"?'checkedRadio': '')+'"><i><input type="radio" name="financing_type"'+(FINANCING_DEL=="A_FINANCING"?'checked="checked"': '')+' value="A_FINANCING"/></i></div>'+MsgSet["A_FINANCING"]+'<span id="'+ data["itemId"] + child.WorkExper2.itemId + '_aspan"'+(FINANCING_DEL=="A_FINANCING"?'':' style="display:none"')+'><input class="inpu-list-text-enter" style="width:55%;height:35px;margin-left:5px" id="'+data["itemId"] + child.WorkExper2_2.itemId+'" value="'+child.WorkExper2_2.value+'" style="width:120px;margin-left:5px"/><span>'+MsgSet["ONE_MILLION"]+'</span></span></li>'	
//							NET_DIV+='<li name="financingAn"><div class="radio-btn '+(FINANCING_DEL=="ANGEL_INVEST"?'checkedRadio': '')+'"><i><input type="radio" name="financing_type"'+(FINANCING_DEL=="ANGEL_INVEST"?'checked="checked"': '')+' value="ANGEL_INVEST"/></i></div>'+MsgSet["ANGEL_INVEST"]+'<span id="'+ data["itemId"] + child.WorkExper2.itemId + '_angspan"'+(FINANCING_DEL=="ANGEL_INVEST"?'':' style="display:none"')+'><input class="inpu-list-text-enter" style="width:55%;height:35px;margin-left:5px" id="'+data["itemId"] + child.WorkExper2_3.itemId+'" value="'+child.WorkExper2_3.value+'" style="width:120px;margin-left:5px"/><span>'+MsgSet["ONE_MILLION"]+'</span></span></li>'	
//							NET_DIV+='<li name="financingNo"><div class="radio-btn '+(FINANCING_DEL=="NO_FINANCING"?'checkedRadio': '')+'"><i><input type="radio" name="financing_type"'+(FINANCING_DEL=="NO_FINANCING"?'checked="checked"': '')+' value="NO_FINANCING"/></i></div>'+MsgSet["NO_FINANCING"]+'</li>'	
//							NET_DIV+='<li name="financingNe"><div class="radio-btn '+(FINANCING_DEL=="NEW_CREATE"?'checkedRadio': '')+'"><i><input type="radio" name="financing_type"'+(FINANCING_DEL=="NEW_CREATE"?'checked="checked"': '')+' value="NEW_CREATE"/></i></div>'+MsgSet["NEW_CREATE"]+'</li>'	
//							NET_DIV+='</ul>'
						NET_DIV+='</div>'		
					//-------------------------------------	
					//<!--收入140px-->
					NET_DIV+='<div id="income_y" style="margin-top:10px" class="input-list">'	
						NET_DIV+='<div  class="input-list-info left">'+MsgSet["INCOME"]+'：</div>'
						NET_DIV+='<div class="input-list-text left"><input class="inpu-list-text-enter" style="height:35px; " id="'+ data["itemId"] + child.WorkExper3.itemId + '" value="'+child.WorkExper3.value+'"/>'//&nbsp;&nbsp;&nbsp;&nbsp;<span style="color:#999">'+MsgSet["ONE_MILLION"]+'</span>'
							//---------NET_DIV input格式检验DIV:
//							NET_DIV += '<div style="margin-top: -40px; margin-left: 365px">';
//							NET_DIV += '	<div id="' + data.itemId+child.WorkExper3.itemId + 'Tip" class="onShow" style="margin: 0px; padding: 0px; background: transparent;">';
//							NET_DIV += '		<div class="onShow"></div>';
//							NET_DIV += '	</div>';
//							NET_DIV += '</div>';
							//---------
						NET_DIV+='</div>'
						NET_DIV+='<div class="input-list-suffix left">'+MsgSet["ONE_MILLION"]+'<span class="input-list-suffix-span">&nbsp;</span><div id="'+data["itemId"] + child.WorkExper3.itemId+'Tip" class="onShow" style="margin: 0px; padding: 0px; background: transparent;"><div class="onShow" tips="&nbsp;">&nbsp;</div></div></div>'

					NET_DIV+='</div>'					
					//<!--用户数20px-->
					NET_DIV+='<div id="people" style="margin-top:10px" class="input-list">'
						NET_DIV+='<div  class="input-list-info left">'+MsgSet["USER_NUM"]+'：</div>'
						NET_DIV+='<div class="input-list-text left"  ><input class="inpu-list-text-enter" style="height:35px; " id="'+ data["itemId"] + child.WorkExper4.itemId + '" value="'+child.WorkExper4.value+'" />'//&nbsp;&nbsp;&nbsp;&nbsp;<span style="color:#999">'+MsgSet["PEOPLES"]+'</span>'
							//---------NET_DIV input格式检验DIV:
//							NET_DIV += '<div style="margin-top: -40px; margin-left: 365px">';
//							NET_DIV += '	<div id="' + data.itemId+child.WorkExper4.itemId + 'Tip" class="onShow" style="margin: 0px; padding: 0px; background: transparent;">';
//							NET_DIV += '		<div class="onShow"></div>';
//							NET_DIV += '	</div>';
//							NET_DIV += '</div>';
							//---------
						NET_DIV+='</div>'
						NET_DIV+='<div class="input-list-suffix left">'+MsgSet["PEOPLES"]+'<span class="input-list-suffix-span">&nbsp;</span><div id="'+data["itemId"] + child.WorkExper4.itemId+'Tip" class="onShow" style="margin: 0px; padding: 0px; background: transparent;"><div class="onShow" tips="&nbsp;">&nbsp;</div></div></div>'

					NET_DIV+='</div>'	
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
						FAM_DIV+='<div  class="input-list-info left">'+MsgSet["OWN_MONEY"]+'：</div>'
						FAM_DIV+='<div class="input-list-text left"  ><input class="inpu-list-text-enter" style="height:35px; " id="'+ data["itemId"] + child.WorkExper5.itemId + '" value="'+child.WorkExper5.value+'"/>'//&nbsp;&nbsp;&nbsp;&nbsp;<span style="color:#999">'+MsgSet["ONE_MILLION"]+'</span>'
							//---------FAM_DIV input格式检验DIV:
//							FAM_DIV += '<div style="margin-top: -40px; margin-left: 365px">';
//							FAM_DIV += '	<div id="' + data.itemId+child.WorkExper5.itemId + 'Tip" class="onShow" style="margin: 0px; padding: 0px; background: transparent;">';
//							FAM_DIV += '		<div class="onShow"></div>';
//							FAM_DIV += '	</div>';
//							FAM_DIV += '</div>';
							//---------
						FAM_DIV+='</div>'
						FAM_DIV+='<div class="input-list-suffix left">'+MsgSet["ONE_MILLION"]+'<span class="input-list-suffix-span">&nbsp;</span><div id="'+data["itemId"] + child.WorkExper5.itemId+'Tip" class="onShow" style="margin: 0px; padding: 0px; background: transparent;"><div class="onShow" tips="&nbsp;">&nbsp;</div></div></div>'

					FAM_DIV+='</div>'		
					//<!--家族企业资产-->
					FAM_DIV+='<div id="family_money" class="input-list">'	
						FAM_DIV+='<div  class="input-list-info left">'+MsgSet["FAMILY_MONEY"]+'：</div>'
						FAM_DIV+='<div class="input-list-text left"  ><input class="inpu-list-text-enter" style="height:35px; " id="'+ data["itemId"] + child.WorkExper6.itemId + '" value="'+child.WorkExper6.value+'"/>'//&nbsp;&nbsp;&nbsp;&nbsp;<span style="color:#999">'+MsgSet["ONE_MILLION"]+'</span>'
							//---------FAM_DIV input格式检验DIV:
//							FAM_DIV += '<div style="margin-top: -40px; margin-left: 365px">';
//							FAM_DIV += '	<div id="' + data.itemId+child.WorkExper6.itemId + 'Tip" class="onShow" style="margin: 0px; padding: 0px; background: transparent;">';
//							FAM_DIV += '		<div class="onShow"></div>';
//							FAM_DIV += '	</div>';
//							FAM_DIV += '</div>';
							//----------------------
						FAM_DIV+='</div>'
						FAM_DIV+='<div class="input-list-suffix left">'+MsgSet["ONE_MILLION"]+'<span class="input-list-suffix-span">&nbsp;</span><div id="'+data["itemId"] + child.WorkExper6.itemId+'Tip" class="onShow" style="margin: 0px; padding: 0px; background: transparent;"><div class="onShow" tips="&nbsp;">&nbsp;</div></div></div>'
					FAM_DIV+='</div>'
						
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
						OTH_DIV+='<div  class="input-list-info left" >'+MsgSet["INCOME"]+'：</div>'
						OTH_DIV+='<div class="input-list-text left"  ><input class="inpu-list-text-enter" style="height:35px; " id="'+ data["itemId"] + child.WorkExper7.itemId + '" value="'+child.WorkExper7.value+'"/>'//&nbsp;&nbsp;&nbsp;&nbsp;<span style="color:#999">'+MsgSet["ONE_MILLION"]+'</span>'	
							//---------FAM_DIV input格式检验DIV:
//							OTH_DIV += '<div style="margin-top: -40px; margin-left: 365px">';
//							OTH_DIV += '	<div id="' + data.itemId+child.WorkExper7.itemId + 'Tip" class="onShow" style="margin: 0px; padding: 0px; background: transparent;">';
//							OTH_DIV += '		<div class="onShow"></div>';
//							OTH_DIV += '	</div>';
//							OTH_DIV += '</div>';
							//----------------------
						OTH_DIV+='</div>'
						OTH_DIV+='<div class="input-list-suffix left">'+MsgSet["ONE_MILLION"]+'<span class="input-list-suffix-span">&nbsp;</span><div id="'+data["itemId"] + child.WorkExper7.itemId+'Tip" class="onShow" style="margin: 0px; padding: 0px; background: transparent;"><div class="onShow" tips="&nbsp;">&nbsp;</div></div></div>'

					OTH_DIV+='</div>'	
					//<!--年纯利润-->
					OTH_DIV+='<div id="year_income" class="input-list">'
						OTH_DIV+='<div  class="input-list-info left">'+MsgSet["YEAR_PROFIT"]+'：</div>'
						OTH_DIV+='<div class="input-list-text left"  ><input class="inpu-list-text-enter" style="height:35px; " id="'+ data["itemId"] + child.WorkExper8.itemId + '" value="'+child.WorkExper8.value+'"/>'//&nbsp;&nbsp;&nbsp;&nbsp;<span style="color:#999">'+MsgSet["ONE_MILLION"]+'</span>'
							//---------OTH_DIV input格式检验DIV:
//							OTH_DIV += '<div style="margin-top: -40px; margin-left: 365px">';
//							OTH_DIV += '	<div id="' + data.itemId+child.WorkExper8.itemId + 'Tip" class="onShow" style="margin: 0px; padding: 0px; background: transparent;">';
//							OTH_DIV += '		<div class="onShow"></div>';
//							OTH_DIV += '	</div>';
//							OTH_DIV += '</div>';
							//----------------------
						OTH_DIV+='</div>'
						OTH_DIV+='<div class="input-list-suffix left">'+MsgSet["ONE_MILLION"]+'<span class="input-list-suffix-span">&nbsp;</span><div id="'+data["itemId"] + child.WorkExper8.itemId+'Tip" class="onShow" style="margin: 0px; padding: 0px; background: transparent;"><div class="onShow" tips="&nbsp;">&nbsp;</div></div></div>'
					OTH_DIV+='</div>'	
					//<!--企业规模-->
					OTH_DIV+='<div id="firm_scale" class="input-list">'
						OTH_DIV+='<div  class="input-list-info left">'+MsgSet["FIRM_SCALE"]+'：</div>'
						OTH_DIV+='<div class="input-list-text left"  ><input class="inpu-list-text-enter" style="height:35px; " id="'+ data["itemId"] + child.WorkExper9.itemId + '" value="'+child.WorkExper9.value+'"/>'//&nbsp;&nbsp;&nbsp;&nbsp;<span style="color:#999">'+MsgSet["PEOPLES"]+'</span>'	
							//---------OTH_DIV input格式检验DIV:
//							OTH_DIV += '<div style="margin-top: -40px; margin-left: 365px">';
//							OTH_DIV += '	<div id="' + data.itemId+child.WorkExper9.itemId + 'Tip" class="onShow" style="margin: 0px; padding: 0px; background: transparent;">';
//							OTH_DIV += '		<div class="onShow"></div>';
//							OTH_DIV += '	</div>';
//							OTH_DIV += '</div>';
							//----------------------
						OTH_DIV+='</div>'
						OTH_DIV+='<div class="input-list-suffix left">'+MsgSet["PEOPLES"]+'<span class="input-list-suffix-span">&nbsp;</span><div id="'+data["itemId"] + child.WorkExper9.itemId+'Tip" class="onShow" style="margin: 0px; padding: 0px; background: transparent;"><div class="onShow" tips="&nbsp;">&nbsp;</div></div></div>'
					OTH_DIV+='</div>'
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

	_getContentHtmlP: function(data) {
		var child=data["children"][0];
		if (child == undefined) {
			child=data["children"];
		}

		//公司类型json数据
		var BUSINESS_TYPE_GP=[
			"互联网类",//01
			"家族企业",//02
			"其他"     //03
		];
		var htmlContent="";

		//--创业类型初始值
		var BUSINESS_TYPE_DEF=child.WorkExper1.value;
		//--融资情况初始值
		var FINANCING_DEL=child.WorkExper2.value;

		//1.创业类型
		if (SurveyBuild._readonly) {
			//只读模式(放入已选择创业类型)
			var types = "";
			var btypeDesc='';
			for(var k=0;k<BUSINESS_TYPE_GP.length;k++){
				if(BUSINESS_TYPE_DEF=="0"+parseInt(k+1)){
					btypeDesc=BUSINESS_TYPE_GP[k];
				}
			}

			types += '<div class="item">';
			types += '<p>'+ child.WorkExper1.itemName + '</p>';
			types += '<div class="text-box"><input ' + ' type="text" class="text1" readonly="true" value="' + btypeDesc + '"/></div>';
			types += '</div>';

			//-----------------------------融资
			var NET_DIV='';
			var finDesc='';
			var incomeYDesc='';
			var peopleDesc='';
			var ownMoneyDesc='';
			var familyMoney='';
			if(BUSINESS_TYPE_DEF=="01"||BUSINESS_TYPE_DEF==""){
				NET_DIV+='<div id="NET_TYPE_SHOW" >'
				incomeYDesc=child.WorkExper3.value+MsgSet["ONE_MILLION"];
				peopleDesc=child.WorkExper4.value+ ["PEOPLES"];
				if(FINANCING_DEL=="B_FINANCING"){
					finDesc=MsgSet["B_FINANCING"]+child.WorkExper2_1.value+MsgSet["ONE_MILLION"];
				}else if(FINANCING_DEL=="A_FINANCING"){
					finDesc=MsgSet["A_FINANCING"]+child.WorkExper2_2.value+MsgSet["ONE_MILLION"];
					ownMoneyDesc=child.WorkExper3.value+MsgSet["ONE_MILLION"];
					familyMoney=child.WorkExper3.value+MsgSet["ONE_MILLION"];
				}else if(FINANCING_DEL=="ANGEL_INVEST"){
					finDesc=MsgSet["ANGEL_INVEST"]+child.WorkExper2_3.value+MsgSet["ONE_MILLION"];
				}else if(FINANCING_DEL=="NO_FINANCING"){
					finDesc=MsgSet["NO_FINANCING"];
				}else if(FINANCING_DEL=="NEW_CREATE"){
					finDesc=MsgSet["NEW_CREATE"];
				}
			}else{
				NET_DIV+='<div id="NET_TYPE_SHOW" style="display:none">'
			}

			//融资标题
			NET_DIV += '<div class="item">';
			NET_DIV += '<p>'+ MsgSet["FINANCING"] + '</p>';

			//融资radio(已选择变为input输入框)
			NET_DIV += '<div class="text-box"><input ' + ' type="text" class="text1" readonly="true" value="' + finDesc + '"/></div>';
			NET_DIV += '</div>';

			//最近一年企业收入
			NET_DIV += '<div class="item">';
			NET_DIV += '<p>'+ MsgSet["INCOME"] + '</p>';
			NET_DIV += '<div class="text-box"><input ' + ' type="text" class="text1" readonly="true" value="' + incomeYDesc + '"/></div>';
			NET_DIV += '</div>';

			//用户数
			NET_DIV += '<div class="item">';
			NET_DIV += '<p>'+ MsgSet["USER_NUM"] + '</p>';
			NET_DIV += '<div class="text-box"><input ' + ' type="text" class="text1" readonly="true" value="' + peopleDesc + '"/></div>';
			NET_DIV += '</div>';
			NET_DIV+='</div>';

			//-----------------------------家族企业
			var FAM_DIV='';
			var ownMoneyDesc='';
			var familyMoneyDesc='';
			if(BUSINESS_TYPE_DEF=="02"){
				FAM_DIV+='<div id="FAMILY_TYPE_SHOW">'
				ownMoneyDesc=child.WorkExper5.value+MsgSet["ONE_MILLION"];
				familyMoneyDesc=child.WorkExper6.value+MsgSet["ONE_MILLION"];
			}else{
				FAM_DIV+='<div id="FAMILY_TYPE_SHOW" style="display:none;">'
			}

			//自有资金
			FAM_DIV += '<div class="item">';
			FAM_DIV += '<p>'+ MsgSet["OWN_MONEY"] + '</p>';
			FAM_DIV += '<div class="text-box"><input ' + ' type="text" class="text1" readonly="true" value="' + ownMoneyDesc + '"/></div>';
			FAM_DIV += '</div>';

			//家族企业资产
			FAM_DIV += '<div class="item">';
			FAM_DIV += '<p>'+ MsgSet["FAMILY_MONEY"] + '</p>';
			FAM_DIV += '<div class="text-box"><input ' + ' type="text" class="text1" readonly="true" value="' + familyMoneyDesc + '"/></div>';
			FAM_DIV += '</div>';
			FAM_DIV += '</div>';

			//-----------------------------其他
			var OTH_DIV='';
			var incomeODesc='';
			var yearIncomeDesc='';
			var firmScaleDesc='';
			if(BUSINESS_TYPE_DEF=="03"){
				OTH_DIV+='<div id="OTHER_TYPE_SHOW">'
				incomeODesc=child.WorkExper7.value+MsgSet["ONE_MILLION"];
				yearIncomeDesc=	child.WorkExper8.value+MsgSet["ONE_MILLION"];
				firmScaleDesc=	child.WorkExper9.value+MsgSet["PEOPLES"];
			}else{
				OTH_DIV+='<div id="OTHER_TYPE_SHOW" style="display:none;">'
			}

			//最近一年企业收入
			OTH_DIV += '<div class="item">';
			OTH_DIV += '<p>'+ MsgSet["INCOME"] + '</p>';
			OTH_DIV += '<div class="text-box"><input ' + ' type="text" class="text1" readonly="true" value="' + incomeODesc + '"/></div>';
			OTH_DIV += '</div>';

			//年纯利润
			OTH_DIV += '<div class="item">';
			OTH_DIV += '<p>'+ MsgSet["YEAR_PROFIT"] + '</p>';
			OTH_DIV += '<div class="text-box"><input ' + ' type="text" class="text1" readonly="true" value="' + yearIncomeDesc + '"/></div>';
			OTH_DIV += '</div>';

			//企业规模
			OTH_DIV += '<div class="item">';
			OTH_DIV += '<p>'+ MsgSet["FIRM_SCALE"] + '</p>';
			OTH_DIV += '<div class="text-box"><input ' + ' type="text" class="text1" readonly="true" value="' + firmScaleDesc + '"/></div>';
			OTH_DIV += '</div>';
			OTH_DIV += '</div>';

			htmlContent += NET_DIV;
			htmlContent += FAM_DIV;
			htmlContent += OTH_DIV
		}else{
			//填写模式
			var OPT_BTYPE='';

			//添加请选择从下拉框
			OPT_BTYPE+='<option value=""'+(BUSINESS_TYPE_DEF==""?'selected="selected"': '')+'>请选择</option>';
			for(var k=0;k<BUSINESS_TYPE_GP.length;k++){
				OPT_BTYPE+='<option value="0'+parseInt(k+1)+'"'+(BUSINESS_TYPE_DEF=="0"+parseInt(k+1)?'selected="selected"': '')+'>'+BUSINESS_TYPE_GP[k]+'</option>';
			}

			//放入创业类型中
			htmlContent += '<div class="item">';
			htmlContent += '<p>'+ child.WorkExper1.itemName + '</p>';
			htmlContent += '<div class="text-box">';
			htmlContent += '<select id="' + data["itemId"] + child.WorkExper1.itemId + '" class="select1" title="' + child.WorkExper1.itemName + '" value="' + child.WorkExper1["value"] + '" name="' + data["itemId"] + child.WorkExper1.itemId + '">';
			htmlContent += OPT_BTYPE;
			htmlContent += '</select>';
			htmlContent += '<div id="' + data["itemId"] + child.WorkExper1.itemId + 'Tip">';
			htmlContent += '</div>';
			htmlContent += '</div>';
			htmlContent += '</div>';

			//-----------------------------融资情况(第一次只显示互联网相关div 其他关联div隐藏)
			var NET_DIV='';
			if(BUSINESS_TYPE_DEF=="01"){
				NET_DIV+='<div id="NET_TYPE_SHOW">'
			}else{
				NET_DIV+='<div id="NET_TYPE_SHOW" style="display:none">'
			}

			//融资标题
			NET_DIV += '<div class="item">';
			NET_DIV += '<p>'+ MsgSet["FINANCING"] + '</p>';

			//融资radio
			NET_DIV += '<div class="item">';
			NET_DIV += '<ul class="company">';

			NET_DIV += '<li id="B">';//B轮
			NET_DIV += '<input type="radio" name="financing" class="radio" id="FB">';
			NET_DIV += '<label id="fb" for="FB">'+ MsgSet["B_FINANCING"] + '</label>';
			NET_DIV += '<input type="text" class="invest" id="'+data["itemId"] + child.WorkExper2_1.itemId+'" value="'+child.WorkExper2_1.value+'" name="finance" hidden="true">';
			NET_DIV += '<label id="financing_b" style="display: none">'+ MsgSet["ONE_MILLION"]+ '</label>';
			NET_DIV += '</li>';

			NET_DIV += '<li id="A">';//A轮
			NET_DIV += '<input type="radio" name="financing" class="radio" id="FA">';
			NET_DIV += '<label id="fa" for="FA">'+ MsgSet["A_FINANCING"] + '</label>';
			NET_DIV += '<input type="text" class="invest" id="'+data["itemId"] + child.WorkExper2_2.itemId+'" value="'+child.WorkExper2_2.value+'" name="finance"  hidden="true">';
			NET_DIV += '<label id="financing_a"  style="display: none">'+ MsgSet["ONE_MILLION"]+ '</label>';
			NET_DIV += '</li>';

			NET_DIV += '<li id="V">';//天使投资
			NET_DIV += '<input type="radio" name="financing" class="radio" id="AV">';
			NET_DIV += '<label id="fv" for="AV">'+ MsgSet["ANGEL_INVEST"] + '</label>';
			NET_DIV += '<input type="text" class="invest" id="'+data["itemId"] + child.WorkExper2_3.itemId+'" value="'+child.WorkExper2_3.value+'" name="finance" hidden="true">';
			NET_DIV += '<label id="financing_v" style="display: none">'+ MsgSet["ONE_MILLION"]+ '</label>';
			NET_DIV += '</li>';

			NET_DIV += '<li id="O">';//尚未获得投资
			NET_DIV += '<input type="radio" name="financing" class="radio" id="other">';
			NET_DIV += '<label id="fo" for="other">'+ MsgSet["NO_FINANCING"] + '</label>';
			NET_DIV += '</li>';

			NET_DIV += '<li id="O">';//初创
			NET_DIV += '<input type="radio" name="financing" class="radio" id="other1">';
			NET_DIV += '<label id="fo" for="other1">'+ MsgSet["NEW_CREATE"] + '</label>';
			NET_DIV += '</li>';
			NET_DIV += '</ul>';
			NET_DIV += '</div>';

			//收入
			NET_DIV += '<div class="item">';
			NET_DIV += '<p>' + MsgSet["INCOME"] + '</p>';
			NET_DIV += '<div class="overhidden"><div class="text-box fl" style="width:70%;">';
			NET_DIV += '<input type="text" class="text1" id="'+data["itemId"] + child.WorkExper3.itemId+'" value="'+child.WorkExper3.value+'"></div>';
			NET_DIV += '<label style="line-height:1.5rem;margin-left:10px;font-size:0.48rem;color:#999;">'+ MsgSet["ONE_MILLION"] + '</label>';
			NET_DIV += '</div>';
			NET_DIV += '</div>';

			//用户数
			NET_DIV += '<div class="item">';
			NET_DIV += '<p>' + MsgSet["USER_NUM"] + '</p>';
			NET_DIV += '<div class="overhidden"><div class="text-box fl" style="width:70%;">';
			NET_DIV += '<input type="text" class="text1" id="'+data["itemId"] + child.WorkExper4.itemId+'" value="'+child.WorkExper4.value+'"></div>';
			NET_DIV += '<label style="line-height:1.5rem;margin-left:10px;font-size:0.48rem;color:#999;">'+ MsgSet["PEOPLES"] + '</label>';
			NET_DIV += '</div>';
			NET_DIV += '</div>';
			NET_DIV += '</div>';
			NET_DIV += '</div>';

			//-----------------------------家族企业
			var FAM_DIV='';
			if(BUSINESS_TYPE_DEF=="02"){
				FAM_DIV+='<div id="FAMILY_TYPE_SHOW">'
			}else{
				FAM_DIV+='<div id="FAMILY_TYPE_SHOW" style="display:none;">'
			}

			//自有资金
			FAM_DIV += '<div class="item">';
			FAM_DIV += '<p>' + MsgSet["OWN_MONEY"] + '</p>';
			FAM_DIV += '<div class="overhidden"><div class="text-box fl" style="width:70%;">';
			FAM_DIV += '<input type="text" class="text1" id="'+ data["itemId"] + child.WorkExper5.itemId + '" value="'+child.WorkExper5.value+'"></div>';
			FAM_DIV += '<label style="line-height:1.5rem;margin-left:10px;font-size:0.48rem;color:#999;">'+ MsgSet["ONE_MILLION"] + '</label>';
			FAM_DIV += '</div>';
			FAM_DIV += '</div>';


			//家族企业资产
			FAM_DIV += '<div class="item">';
			FAM_DIV += '<p>' + MsgSet["FAMILY_MONEY"] + '</p>';
			FAM_DIV += '<div class="overhidden"><div class="text-box fl" style="width:70%;">';
			FAM_DIV += '<input type="text" class="text1" id="'+ data["itemId"] + child.WorkExper6.itemId + '" value="'+child.WorkExper6.value+'"></div>';
			FAM_DIV += '<label style="line-height:1.5rem;margin-left:10px;font-size:0.48rem;color:#999;">'+ MsgSet["ONE_MILLION"] + '</label>';
			FAM_DIV += '</div>';
			FAM_DIV += '</div>';
			FAM_DIV += '</div>';

			//-----------------------------其他
			var OTH_DIV='';
			if(BUSINESS_TYPE_DEF=="03"){
				OTH_DIV+='<div id="OTHER_TYPE_SHOW">'
			}else{
				OTH_DIV+='<div id="OTHER_TYPE_SHOW" style="display:none;">'
			}

			//12个月收入
			OTH_DIV += '<div class="item">';
			OTH_DIV += '<p>' + MsgSet["INCOME"] + '</p>';
			OTH_DIV += '<div class="overhidden"><div class="text-box fl" style="width:70%;">';
			OTH_DIV += '<input type="text" class="text1" id="'+ data["itemId"] + child.WorkExper7.itemId + '" value="'+child.WorkExper7.value+'"></div>';
			OTH_DIV += '<label style="line-height:1.5rem;margin-left:10px;font-size:0.48rem;color:#999;">'+ MsgSet["ONE_MILLION"] + '</label>';
			OTH_DIV += '</div>';
			OTH_DIV += '</div>';

			//年纯利润
			OTH_DIV += '<div class="item">';
			OTH_DIV += '<p>' + MsgSet["YEAR_PROFIT"] + '</p>';
			OTH_DIV += '<div class="overhidden"><div class="text-box fl" style="width:70%;">';
			OTH_DIV += '<input type="text" class="text1" id="'+ data["itemId"] + child.WorkExper8.itemId + '" value="'+child.WorkExper8.value+'"></div>';
			OTH_DIV += '<label style="line-height:1.5rem;margin-left:10px;font-size:0.48rem;color:#999;">'+ MsgSet["ONE_MILLION"] + '</label>';
			OTH_DIV += '</div>';
			OTH_DIV += '</div>';

			//企业规模
			OTH_DIV += '<div class="item">';
			OTH_DIV += '<p>' + MsgSet["FIRM_SCALE"] + '</p>';
			OTH_DIV += '<div class="overhidden"><div class="text-box fl" style="width:70%;">';
			OTH_DIV += '<input type="text" class="text1" id="'+ data["itemId"] + child.WorkExper9.itemId + '" value="'+child.WorkExper9.value+'"></div>';
			OTH_DIV += '<label style="line-height:1.5rem;margin-left:10px;font-size:0.48rem;color:#999;">'+ MsgSet["PEOPLES"] + '</label>';
			OTH_DIV += '</div>';
			OTH_DIV += '</div>';

			htmlContent += NET_DIV;
			htmlContent += FAM_DIV;
			htmlContent += OTH_DIV
		}
		return htmlContent
	},

	_eventbind: function(data) {
		//-----根据"创业类型"下拉框 显示和隐藏"类型关联div"
		var child = data["children"][0];
		if (child == undefined) {
	   		 child=data["children"];
	   	 	}
		//var child = data["children"];
		var $btype_select = $("#" +data["itemId"] + child.WorkExper1.itemId);
		//--------------------------input-list sliblings("#NET_TYPE_SHOW")

		if(SurveyBuild.accessType == "M"){

			//手机版修改
			$btype_select.each(function(){
				$(this).change(function(){

					var btypeVal=$(this).val();
					//关联DIV元素：
					var netDiv=$(this).parents(".item").siblings("#NET_TYPE_SHOW");
					var familyDiv=$(this).parents(".item").siblings("#FAMILY_TYPE_SHOW");
					var otherDiv=$(this).parents(".item").siblings("#OTHER_TYPE_SHOW");
					if(btypeVal=="01"){

						netDiv.css("display","block");

						//清空已填写数据(家族企业和其他)
						familyDiv.find("#"+data["itemId"] + child.WorkExper5.itemId).val("");
						familyDiv.find("#"+data["itemId"] + child.WorkExper6.itemId).val("");

						otherDiv.find("#"+data["itemId"] + child.WorkExper7.itemId).val("");
						otherDiv.find("#"+data["itemId"] + child.WorkExper8.itemId).val("");
						otherDiv.find("#"+data["itemId"] + child.WorkExper9.itemId).val("");
					}else{
						netDiv.css("display","none");

					}
					if(btypeVal=="02"){
						familyDiv.css("display","block");

						//初始化互联网选项
						netDiv.find("#"+data["itemId"] + child.WorkExper2.itemId).val("");
						netDiv.find("#"+data["itemId"] + child.WorkExper2_1.itemId).val("");
						netDiv.find("#"+data["itemId"] + child.WorkExper2_2.itemId).val("");
						netDiv.find("#"+data["itemId"] + child.WorkExper2_3.itemId).val("");
						netDiv.find("#"+data["itemId"] + child.WorkExper3.itemId).val("");
						netDiv.find("#"+data["itemId"] + child.WorkExper4.itemId).val("");
						netDiv.find("#"+data["itemId"] + child.WorkExper2_1.itemId).hide();
						netDiv.find("#"+data["itemId"] + child.WorkExper2_2.itemId).hide();
						netDiv.find("#"+data["itemId"] + child.WorkExper2_3.itemId).hide();
						netDiv.find("#financing_a").hide();
						netDiv.find("#financing_b").hide();
						netDiv.find("#financing_v").hide();
						netDiv.find("#FB").removeAttr("checked");
						netDiv.find("#FA").removeAttr("checked");
						netDiv.find("#AV").removeAttr("checked");
						netDiv.find("#other").removeAttr("checked");
						netDiv.find("#other1").removeAttr("checked");

						otherDiv.find("#"+data["itemId"] + child.WorkExper7.itemId).val("");
						otherDiv.find("#"+data["itemId"] + child.WorkExper8.itemId).val("");
						otherDiv.find("#"+data["itemId"] + child.WorkExper9.itemId).val("");

					}else{
						familyDiv.css("display","none");
					}
					if(btypeVal=="03"){
						otherDiv.css("display","block");

						//初始化互联网选项
						netDiv.find("#"+data["itemId"] + child.WorkExper2.itemId).val("");
						netDiv.find("#"+data["itemId"] + child.WorkExper2_1.itemId).val("");
						netDiv.find("#"+data["itemId"] + child.WorkExper2_2.itemId).val("");
						netDiv.find("#"+data["itemId"] + child.WorkExper2_3.itemId).val("");
						netDiv.find("#"+data["itemId"] + child.WorkExper3.itemId).val("");
						netDiv.find("#"+data["itemId"] + child.WorkExper4.itemId).val("");
						netDiv.find("#"+data["itemId"] + child.WorkExper2_1.itemId).hide();
						netDiv.find("#"+data["itemId"] + child.WorkExper2_2.itemId).hide();
						netDiv.find("#"+data["itemId"] + child.WorkExper2_3.itemId).hide();
						netDiv.find("#financing_a").hide();
						netDiv.find("#financing_b").hide();
						netDiv.find("#financing_v").hide();
						netDiv.find("#FB").removeAttr("checked");
						netDiv.find("#FA").removeAttr("checked");
						netDiv.find("#AV").removeAttr("checked");
						netDiv.find("#other").removeAttr("checked");
						netDiv.find("#other1").removeAttr("checked");

						familyDiv.find("#"+data["itemId"] + child.WorkExper5.itemId).val("");
						familyDiv.find("#"+data["itemId"] + child.WorkExper6.itemId).val("");


					}else{
						otherDiv.css("display","none");
					}
				});
			});

			//----"互联网"类型下，radio的切换处理
			var radioEl;
			if($(".radio[name='financing']")!=undefined){
				radioEl=$(".radio[name='financing']");

				radioEl.click(function(){

					var commonId = $(this).attr("id");
					if(commonId == "FB"){//将A.V隐藏

						$(this).siblings("#financing_b").show();
						$(this).siblings(".invest").show();
						$(this).parent("#B").siblings("#A").find("input[name='finance']").val("");
						$(this).parent("#B").siblings("#V").find("input[name='finance']").val("");
						$(this).parent("#B").siblings("#A").find("input[name='finance']").hide();
						$(this).parent("#B").siblings("#V").find("input[name='finance']").hide();
						$(this).parent("#B").siblings("#A").children("#financing_a").hide();
						$(this).parent("#B").siblings("#V").children("#financing_v").hide()

					}else if(commonId == "FA"){//将B.V隐藏

						$(this).siblings("#financing_a").show();
						$(this).siblings(".invest").show();
						$(this).parent("#A").siblings("#B").find("input[name='finance']").val("");
						$(this).parent("#A").siblings("#V").find("input[name='finance']").val("");
						$(this).parent("#A").siblings("#B").find("input[name='finance']").hide();
						$(this).parent("#A").siblings("#V").find("input[name='finance']").hide();
						$(this).parent("#A").siblings("#B").children("#financing_b").hide();
						$(this).parent("#A").siblings("#V").children("#financing_v").hide()

					}else if(commonId == "AV"){//将A.B隐藏

						$(this).siblings("#financing_v").show();
						$(this).siblings(".invest").show();
						$(this).parent("#V").siblings("#A").find("input[name='finance']").val("");
						$(this).parent("#V").siblings("#B").find("input[name='finance']").val("");
						$(this).parent("#V").siblings("#B").find("input[name='finance']").hide();
						$(this).parent("#V").siblings("#A").find("input[name='finance']").hide();
						$(this).parent("#V").siblings("#B").children("#financing_b").hide();
						$(this).parent("#V").siblings("#A").children("#financing_a").hide()
					}else{//没有输入值，将A.B.V隐藏

						$(this).parent("#O").siblings("#A").find("input[name='finance']").val("");
						$(this).parent("#O").siblings("#B").find("input[name='finance']").val("");
						$(this).parent("#O").siblings("#V").find("input[name='finance']").val("");
						$(this).parent("#O").siblings("#A").find("input[name='finance']").hide();
						$(this).parent("#O").siblings("#B").find("input[name='finance']").hide();
						$(this).parent("#O").siblings("#V").find("input[name='finance']").hide();
						$(this).parent("#O").siblings("#B").children("#financing_b").hide();
						$(this).parent("#O").siblings("#V").children("#financing_v").hide();
						$(this).parent("#O").siblings("#A").children("#financing_a").hide()
					}

				});
			}
		}else{

			//PC相关修改
			$btype_select.each(function(){
				$(this).change(function(){
					var btypeVal=$(this).val();
					//关联DIV元素：
					var netDiv=$(this).parents(".input-list").siblings("#NET_TYPE_SHOW");
					var familyDiv=$(this).parents(".input-list").siblings("#FAMILY_TYPE_SHOW");
					var otherDiv=$(this).parents(".input-list").siblings("#OTHER_TYPE_SHOW");

					if(btypeVal=="01"){
						netDiv.css("display","block");
						//清空除开"互联网"之外的数据
						familyDiv.find("#"+data["itemId"] + child.WorkExper5.itemId).val("");
						familyDiv.find("#"+data["itemId"] + child.WorkExper6.itemId).val("");

						otherDiv.find("#"+data["itemId"] + child.WorkExper7.itemId).val("");
						otherDiv.find("#"+data["itemId"] + child.WorkExper8.itemId).val("");
						otherDiv.find("#"+data["itemId"] + child.WorkExper9.itemId).val("");
						//---------------------
					}else{
						netDiv.css("display","none");
					}
					if(btypeVal=="02"){
						familyDiv.css("display","block");
						//清空除开"家族企业"之外的数据
						netDiv.find("#"+data["itemId"] + child.WorkExper2.itemId).val("");
						netDiv.find("#"+data["itemId"] + child.WorkExper2_1.itemId).val("");
						netDiv.find("#"+data["itemId"] + child.WorkExper2_2.itemId).val("");
						netDiv.find("#"+data["itemId"] + child.WorkExper2_3.itemId).val("");
						netDiv.find("#"+data["itemId"] + child.WorkExper3.itemId).val("");
						netDiv.find("#"+data["itemId"] + child.WorkExper4.itemId).val("");

						otherDiv.find("#"+data["itemId"] + child.WorkExper7.itemId).val("");
						otherDiv.find("#"+data["itemId"] + child.WorkExper8.itemId).val("");
						otherDiv.find("#"+data["itemId"] + child.WorkExper9.itemId).val("");
						//清除"互联网"下的radio选择和隐藏后缀
						netDiv.find("#FB").find("div[name='financingB']").find(".left").find(".radio-btn").removeClass("checkedRadio");
						netDiv.find("#FB").find("div[name='financingB']").find(".right").css("display","none");
						netDiv.find("#FB").find(".input-list-suffix").css("display","none");

						netDiv.find("#FA").find("div[name='financingA']").find(".left").find(".radio-btn").removeClass("checkedRadio");
						netDiv.find("#FA").find("div[name='financingA']").find(".right").css("display","none");
						netDiv.find("#FA").find(".input-list-suffix").css("display","none");

						netDiv.find("#FAN").find("div[name='financingAn']").find(".left").find(".radio-btn").removeClass("checkedRadio");
						netDiv.find("#FAN").find("div[name='financingAn']").find(".right").css("display","none");
						netDiv.find("#FAN").find(".input-list-suffix").css("display","none");


					}else{
						familyDiv.css("display","none");
					}
					if(btypeVal=="03"){
						otherDiv.css("display","block");
						//清除除开"其他"之外所有数据
						netDiv.find("#"+data["itemId"] + child.WorkExper2.itemId).val("");
						netDiv.find("#"+data["itemId"] + child.WorkExper2_1.itemId).val("");
						netDiv.find("#"+data["itemId"] + child.WorkExper2_2.itemId).val("");
						netDiv.find("#"+data["itemId"] + child.WorkExper2_3.itemId).val("");
						netDiv.find("#"+data["itemId"] + child.WorkExper3.itemId).val("");
						netDiv.find("#"+data["itemId"] + child.WorkExper4.itemId).val("");

						familyDiv.find("#"+data["itemId"] + child.WorkExper5.itemId).val("");
						familyDiv.find("#"+data["itemId"] + child.WorkExper6.itemId).val("");

						//清除"互联网"下的radio选择和隐藏后缀
						netDiv.find("#FB").find("div[name='financingB']").find(".left").find(".radio-btn").removeClass("checkedRadio");
						netDiv.find("#FB").find("div[name='financingB']").find(".right").css("display","none");
						netDiv.find("#FB").find(".input-list-suffix").css("display","none");

						netDiv.find("#FA").find("div[name='financingA']").find(".left").find(".radio-btn").removeClass("checkedRadio");
						netDiv.find("#FA").find("div[name='financingA']").find(".right").css("display","none");
						netDiv.find("#FA").find(".input-list-suffix").css("display","none");

						netDiv.find("#FAN").find("div[name='financingAn']").find(".left").find(".radio-btn").removeClass("checkedRadio");
						netDiv.find("#FAN").find("div[name='financingAn']").find(".right").css("display","none");
						netDiv.find("#FAN").find(".input-list-suffix").css("display","none");

					}else{
						otherDiv.css("display","none");
						//
					}
				});
			});
		}

		//----"互联网"类型下，radio的切换处理
		var radioEl;
		if($(".radio-btn[name='radioInput']")!=undefined){
			radioEl=$(".radio-btn[name='radioInput']");
			radioEl.click(function(){
				var radioEl=$(this).find("input[name='financing_type']");
	
				$(this).addClass("checkedRadio");
				var financingTypeVal=radioEl.val();
				//alert(financingTypeVal);
				//获取5个radio的句柄
				var b_financing_i=$(this).parents("div[name='financingGp']").find("#FB").find("div[name='financingB']").find(".right");
				var b_financing_s=$(this).parents("div[name='financingGp']").find("#FB").find(".input-list-suffix");
				
				//console.log("b_financing_i:");
				//console.dir(b_financing_i);
				var a_financing_i=$(this).parents("div[name='financingGp']").find("#FA").find("div[name='financingA']").find(".right");
				var a_financing_s=$(this).parents("div[name='financingGp']").find("#FA").find(".input-list-suffix");
				
				var ang_financing_i=$(this).parents("div[name='financingGp']").find("#FAN").find("div[name='financingAn']").find(".right");
				var ang_financing_s=$(this).parents("div[name='financingGp']").find("#FAN").find(".input-list-suffix");
				
				var no_radio=$(this).parents("div[name='financingGp']").find("#FNO").find("div[name='financingNo']").find(".radio-btn");
				var new_radio=$(this).parents("div[name='financingGp']").find("#FNE").find("div[name='financingNe']").find(".radio-btn");
	
				//---radio-1
				if(financingTypeVal=="B_FINANCING"){
					b_financing_i.css("display","inline");
					b_financing_s.css("display","inline");
				}else{
					b_financing_i.siblings(".left").find(".radio-btn").removeClass("checkedRadio");
					b_financing_i.css("display","none");
					b_financing_s.css("display","none");
					//清空B轮融资的数据：
					b_financing_i.find("input").val("");
				}
				//---radio-2
				if(financingTypeVal=="A_FINANCING"){
					a_financing_i.css("display","inline");
					a_financing_s.css("display","inline");
				}else{
					a_financing_i.siblings(".left").find(".radio-btn").removeClass("checkedRadio");
					a_financing_i.css("display","none");
					a_financing_s.css("display","none");
					//清空A轮融资的数据:
					a_financing_i.find("input").val("");
				}
				//---radio-3
				if(financingTypeVal=="ANGEL_INVEST"){
					ang_financing_i.css("display","inline");
					ang_financing_s.css("display","inline");
				}else{
					ang_financing_i.siblings(".left").find(".radio-btn").removeClass("checkedRadio");
					ang_financing_i.css("display","none");
					ang_financing_s.css("display","none");
					//清空天使融资的数据：
					ang_financing_i.find("input").val("");
				}
				if(financingTypeVal!="NO_FINANCING"){
					no_radio.removeClass("checkedRadio");
				}
				if(financingTypeVal!="NEW_CREATE"){
					new_radio.removeClass("checkedRadio");
				}
				if(financingTypeVal=="NO_FINANCING"||financingTypeVal=="NEW_CREATE"){
					//清楚B轮融资 A轮融资 天使融资 数据:
					a_financing_i.val("");
					b_financing_i.val("");
					ang_financing_i.val("");
				}
				//-----------将raido中的数据放入radioGroup下的隐藏input中

				var radioValInput=$(this).parents(".right").find("div[name='radioData']").find("#"+data["itemId"] + child.WorkExper2.itemId);
				radioValInput.val(financingTypeVal);
			});
		}
		//--------------------------
	
		
		//---所有的input,select非空验证:
		/*
		//验证所有的select:WorkExper1
		 var selectEl = $("#" + data.itemId +child.WorkExper1.itemId);
		 	 selectEl.each(function(){
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
		//验证所有的input:WorkExper2 WorkExper2_1 WorkExper2_3 WorkExper3 WorkExper4 WorkExper5 WorkExper6 WorkExper7 WorkExper8 WorkExper9
		var input_id_gp=["2","2_1","2_2","2_3","3","4","5","6","7","8","9"];
		for(var j=0;j<input_id_gp.length;j++){
			 var workExperOpt="WorkExper"+input_id_gp[j];
			   var inputEl = $("#" + data.itemId +child[workExperOpt].itemId);
			   inputEl.each(function(){
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
		}*/
		}
		
})