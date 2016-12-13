var SurveyLogic = {
	_items:{},
	_glItems:{},
	_logic:[],
	SurveyID:"",
	tzGeneralURL:"",
	edit:false,
	
	init: function(){
		_logic = SurveyLogic._logic;
		var c = "";
		$.each(_logic,function(index,logic){
			c += SurveyLogic._load(logic);
		});
		$("#logicContent").html(c);
	},
	_load: function(logic){
		var c = '';
		var items = this._items;
		var _itemId = logic.itemId;
		var _subQuest = [],_subOpt = [];
		
		if(items[_itemId]) _subQuest = items[_itemId].subQuestion;
		if(items[_itemId]) _subOpt = items[_itemId].subOption;

		c += '<div class="logicInner" logic-id="'+ logic.logicalId +'">';
		c += '	<div class="logicView">';
		c += '		<div class="logicDesc clearfix">';
		c += '			<div class="logicBtns">';
		c += '				<a class="logicNum">逻辑'+logic.orderBy+'</a>';
		c += '				<a class="copy" onclick="SurveyLogic.copy(this)">复制</a>';
		c += '				<a class="remove" onclick="SurveyLogic.remove(this)">删除</a>';
		c += '			</div>';
		c += '		</div>';
		c += '		<div class="logicEdit clearfix">';
		c += '			<div class="expressionSet">';
		c += '				<div class="expressionContainer">';
		c += '					<div class="expression">';
		c += '						<div class="selectors">';
		c += '							<div class="main_question_selector">';
		c += '								<span class="expressionField">如果</span>';
		c += '								<a class="menubutton">';
		c += '									<select class="chosen-select mainQuestion" data-placeholder="请选择"  onChange="SurveyLogic.selMainQues(this);">';
		c += '										<option value=""></option>';
		for(var i in items){
			c += '<option value="'+ items[i].itemId +'" '+ (logic.itemId == items[i].itemId ? 'selected="selected"':'') +'>'+ items[i].itemName +'</option>';
		}
		c += '									</select>';
		c += '								</a>';
		c += '      					</div>';
		
		//逻辑规则定义子选项
		c += '							<div class="main_question_selector_right">'
		
		var _subItem = logic.subQuestion;
		if(logic.isSubQue == "Y"){
			for(var i=0; i<_subItem.length; i++){
				if(_itemId){
					c += '<div class="menubutton_right">';
					c += '	<a class="menubutton">';
					c += '		<select onchange="SurveyLogic.selSubAttribute(this,\'sItemId\');" class="chosen-select subQuestion">';
					c += '			<option value=""></option>';
						for(var j=0; j<_subQuest.length; j++){
							c += '<option value="'+ _subQuest[j].sItemId +'" '+ (_subItem[i].sItemId == _subQuest[j].sItemId ? 'selected="selected"':'') +'>'+ _subQuest[j].sItemName +'</option>';	
						}
									
					c += '		</select>';
					c += '	</a>';
					c += '	<a class="menubutton">';
					c += '		<select onchange="SurveyLogic.selSubAttribute(this,\'option\');" class="chosen-select qOption">';
					c += '			<option value=""></option>';
						for(var j=0; j<_subOpt.length; j++){
							c += '<option value="'+ _subOpt[j].sOptName +'" '+ (_subItem[i].option == _subOpt[j].sOptName ? 'selected="selected"':'') +'>'+ _subOpt[j].sOptDesc +'</option>';	
						}
					c += '		</select>';
					c += '	</a>';
					c += '	<span class="expressionField">为</span>';
					c += '	<a class="menubutton">';
					c += '		<select onchange="SurveyLogic.selSubAttribute(this,\'checked\');" class="chosen-select checkType">';
					c += '			<option value="1" '+ (_subItem[i].checked == "1" ? 'selected="selected"':'') +'>选中</option>';
					c += '			<option value="2" '+ (_subItem[i].checked == "2" ? 'selected="selected"':'') +'>未选中</option>';
					c += '		</select>';
					c += '	</a>';
					c += '	<div class="buttons">';
					c += '		<button onclick="SurveyLogic.delSubItem(this);" class="btn-jian" type="button"></button>&nbsp;';
					c += '		<button onclick="SurveyLogic.addSubItem(this);" class="btn-add" type="button"></button>';
					c += '	</div>';
					c += '</div>';	
				}
			}
		}else{
			for(var i=0; i<_subItem.length; i++){
				if(_itemId){
					c += '<div class="menubutton_right">';
					if (_subOpt.length > 0){
						c += '	<a class="menubutton">';
						c += '		<select onchange="SurveyLogic.selSubAttribute(this,\'option\');" class="chosen-select qOption">';
						c += '			<option value=""></option>';
									for(var j=0; j<_subOpt.length; j++){
										c += '<option value="'+ _subOpt[j].sOptName +'" '+ (_subItem[i].option == _subOpt[j].sOptName ? "selected":"") +'>'+ _subOpt[j].sOptDesc +'</option>';	
									}
						c += '		</select>';
						c += '	</a>';
					}
					c += '	<span class="expressionField">为</span>';
					c += '	<a class="menubutton">';
					c += '		<select onchange="SurveyLogic.selSubAttribute(this,\'checked\');" class="chosen-select checkType">';
					if (_subOpt.length > 0){
						c += '			<option value="1" '+ (_subItem[i].checked == "1" ? 'selected="selected"':'') +'>选中</option>';
						c += '			<option value="2" '+ (_subItem[i].checked == "2" ? 'selected="selected"':'') +'>未选中</option>';
					}else{
						c += '			<option value="3" '+ (_subItem[i].checked == "3" ? 'selected="selected"':'') +'>填写</option>';
						c += '			<option value="4" '+ (_subItem[i].checked == "4" ? 'selected="selected"':'') +'>未填写</option>';
					}
					c += '		</select>';
					c += '	</a>';
					
					if(items[_itemId].className == "MultipleChoice"){
						c += '	<div class="buttons">';
						c += '		<button type="button" class="btn-jian" onClick="SurveyLogic.delSubItem(this);"></button>&nbsp;';
						c += '		<button type="button" class="btn-add" onClick="SurveyLogic.addSubItem(this);"></button>';
						c += '	</div>';
					}
					
					c += '</div>';
				}
			}
		}
		c += '</div>';
		c += '</div></div></div></div></div></div>';

		//控制关联问题
		c += '<div class="logicElement">';
		c += '	<div class="indent ctrl">';
		c += '		<div class="logicInner" logic-id="'+ logic.logicalId +'">';
		c += '			<div class="logicResult clearfix">';
		c += '				<div class="types">';
		c += '					<span class="expressionField">则</span>';
		c += '					<a class="menubutton">';
		c += '						<select onchange="SurveyLogic.setCtlType(this);" class="chosen-select ctrType">';
		c += '							<option value="1" '+ (logic.type == "1" ? 'selected="selected"':'') +'>显示</option>';
		c += '							<option value="2" '+ (logic.type == "2" ? 'selected="selected"':'') +'>隐藏</option>';
		c += '						</select>';
		c += '					</a>';
		c += ' 				</div>';
		
		var _relQuest = logic.relatedQ;
		c += this._relatedHtml(_relQuest,_itemId);
		
		c +='</div><div class="guideBlock"><div>';
		c +='</div></div></div></div></div></div>';
		
		return c;
	},
	_optHtml: function(selVal , mainQ){
		var c = "";
		var isAfter = false;
		var itemsDate = this._glItems;
		for(var i in itemsDate){
			if(isAfter){
				c +='<option value="'+itemsDate[i].itemId+'" '+ (itemsDate[i].itemId == selVal ? 'selected="selected"':'') +'>'+itemsDate[i].itemName+'</option>';	
			}
			if(itemsDate[i].itemId == mainQ){
				isAfter	= true;
			}
		}
		return c;
	},
	//根据逻辑关联问题报文生成关联问题HTML
	_relatedHtml: function(_relQuest, mainQ){
		var c = "";
		for(var i=0; i<_relQuest.length; i++){
			c += '<div class="expressionContainer">';
			c += '	<div class="expression">';
			c += '		<div class="selectors">';
			c += '			<a class="menubutton">';
			c += '				<select class="chosen-select controlQuestion" onchange="SurveyLogic.selRelQuest(this);">';
			c += '					<option value=""></option>';
			c += this._optHtml(_relQuest[i].itemId, mainQ);	
			c += '				</select>';
			c += '			</a>';
			c += '			<div class="buttons"><button type="button" class="btn-jian" onClick="SurveyLogic.delQuestion(this);"></button>&nbsp;';
			c += '			<button type="button" class="btn-add" onClick="SurveyLogic.addQuestion(this);"></button></div>';
			c += '		</div>';
			c += '	</div>';
			c += '</div>';	
		}	
		return c;
	},
	addNewLogic: function(el){
		this.edit = true;
		var c = '', me = this;
		var logic;
		var logicIns={
			logicalId:"",
			itemId:"",
			isSubQue:"",
			type:"1",
			orderBy:"",
			subQuestion:[{
				sItemId:"",  
				option:"", 
				checked:"1"
			}],
			relatedQ:[{
				itemId:""
			}]
		};

		var index = $("#logicContent").children(".logicInner").length + 1;//逻辑规则序号
		//var d = ( + new Date()) + "" + parseInt(Math.random() * 99);//逻辑编号
		
		logic = logicIns;
		//logic.logicalId = d;
		logic.orderBy = index;
		this._logic.push(logic);

		c = this._load(logic);		
		$("#logicContent").append(c);
		//var $newLogic = $("#logicContent").children(".logicInner").last();
		//$newLogic.find("select").chosen();		
		
		/*新增一行动态效果*/
		$("html,body").animate({scrollTop: $(el).offset().top}, 1000);
	},
	copy: function(el){
		this.edit = true;
		var _logic = this._logic,
		_newLogic,c = "";
		var $currLogic = $(el).closest(".logicInner");
		var currIns = $currLogic.attr("logic-id");
		var index = $currLogic.index();
		//var d = ( + new Date()) + "" + parseInt(Math.random() * 99);//逻辑编号
		
		_newLogic = cloneObj(_logic[index]);
		_newLogic.logicalId = "";
		//_logic[d] = _newLogic;
		_logic.splice(index+1,0,_newLogic);
		
		c = this._load(_newLogic);
		$currLogic.after(c);
		
		var logicEl = $("#logicContent").children(".logicInner");
		//console.log(logicEl);
		for (var i=0; i<logicEl.length; i++){
			//var lIns = $(logicEl[i]).attr("logic-id");
			_logic[i].orderBy = ""+(i+1);
			$(logicEl[i]).find(".logicNum").html("逻辑"+(i+1));	
		}
		
		/*新增一行动态效果*/
		var $newRow = $(el).closest(".logicInner").next(".logicInner");
		$("html,body").animate({scrollTop: $newRow.offset().top-60}, 1000);
		
		
	},
	remove: function(el){
		if(confirm("确定删除该逻辑定义吗？")){
			this.edit = true;
			var _logic = this._logic;
			var $currLogic = $(el).closest(".logicInner");
			//var currIns = $currLogic.attr("logic-id");
			var index = $currLogic.index();
			var prevLogic = $currLogic.prev(".logicInner");
	
			_logic.splice(index,1);
			$currLogic.animate({height: 'hide',opacity: 'hide'},'slow',function() {
				$currLogic.remove();
				
				var logicEl = $("#logicContent").children(".logicInner");
				for (var i=0; i<logicEl.length; i++){
					_logic[i].orderBy = ""+(i+1);
					$(logicEl[i]).find(".logicNum").html("逻辑"+(i+1));	
				}
			});
			$("html,body").animate({scrollTop: prevLogic.offset().top},1000);
		}
	},
	
	addSubItem: function(el){
		this.edit = true;
		//var logicId = $(el).closest(".logicInner").attr("logic-id");
		var LogicIndex = $(el).closest(".logicInner").index();
		var _subItem = this._logic[LogicIndex].subQuestion;
		var index = $(el).closest(".menubutton_right").index();
		
		var mItemId = $(el).closest(".selectors").children(".main_question_selector").find(".mainQuestion").val();
	 	c = this.getSubItemHtml(mItemId);
		$(el).closest(".menubutton_right").after(c);
		//$(el).closest(".menubutton_right").next(".menubutton_right").find("select").chosen();
		
		_cItem = cloneObj(_subItem[0]);
		_cItem.sItemId = "";
		_cItem.option = "";
		_cItem.checked = "1";
		
		_subItem.splice(index+1,0,_cItem);
	},
	delSubItem: function(el){
		this.edit = true;
		//var logicId = $(el).closest(".logicInner").attr("logic-id");
		var LogicIndex = $(el).closest(".logicInner").index();
		var _subItem = this._logic[LogicIndex].subQuestion;
		var index = $(el).closest(".menubutton_right").index();
		
		var sqList = $(el).closest(".main_question_selector_right").children(".menubutton_right");
		if (sqList.length > 1){
			_subItem.splice(index,1);
			$(el).closest(".menubutton_right").remove();
		}else{
			_subItem[0].sItemId = "";
			_subItem[0].option = "";
			_subItem[0].checked = "1"
			
			sqList.eq(0).find("select.subQuestion").val("");	
			sqList.eq(0).find("select.qOption").val("");
			sqList.eq(0).find("select.checkType").val("1");
		}
	},
	
	//添加问题
	addQuestion: function(el){
		this.edit = true;
		var _relQuest,c = "";
		//var logicId = $(el).closest(".logicInner").attr("logic-id");
		var LogicIndex = $(el).closest(".logicElement").closest(".logicInner").index();
		_relQuest = this._logic[LogicIndex].relatedQ;
		var _itemId = this._logic[LogicIndex].itemId;
		var $q= $(el).closest(".expressionContainer");
		var index = $q.index();
		
		_cItem = cloneObj(_relQuest[0]);
		_cItem.itemId = "";
		
		_relQuest.splice(index,0,_cItem);
		c += '<div class="expressionContainer">';
		c += '	<div class="expression">';
		c += '		<div class="selectors">';
		c += '			<a class="menubutton">';
		c += '				<select class="chosen-select controlQuestion" onchange="SurveyLogic.selRelQuest(this);">';
		//c += '					<option value=""></option>'+_optHtml;
		c += '					<option value=""></option>';
		c += this._optHtml("", _itemId);	
		c += '				</select>';
		c += '			</a>';
		c += '			<div class="buttons">';
		c += '				<button type="button" class="btn-jian" onClick="SurveyLogic.delQuestion(this);"></button>&nbsp;';
		c += '				<button type="button" class="btn-add" onClick="SurveyLogic.addQuestion(this);"></button>';
		c += '			</div>';
		c += '		</div>';
		c += '	</div>';
		c += '</div>';

		$q.after(c);
		//$q.next().find("select").chosen();
	},
	//删除问题
	delQuestion: function(el){
		this.edit = true;
		//var logicId = $(el).closest(".logicInner").attr("logic-id");
		var LogicIndex = $(el).closest(".logicElement").closest(".logicInner").index();
		var _relQuest = this._logic[LogicIndex].relatedQ;
		var index= $(el).closest(".expressionContainer").index();
		
		var $rslt = $(el).closest(".logicResult").children(".expressionContainer");
		if($rslt.length == 1){
			var sel1 = $(el).closest(".selectors").children(".menubutton").eq(0).children("select").eq(0);
			sel1.val("");
			_relQuest[0].itemId = "";
		}else{
			$(el).closest(".expressionContainer").remove();
			_relQuest.splice(index-1,1);
		}
	},
	
	//选择主问题
	selMainQues: function(el){
		this.edit = true;
		var itemId = $(el).val();
		var Item = this._items[itemId]
		var LogicIndex = $(el).closest(".logicInner").index();
		var logicIns = this._logic[LogicIndex];
		var _subItem = logicIns.subQuestion;
		var _relQuest = logicIns.relatedQ;
		
		//删除子问题及可选项
		if(_subItem.length > 1){
			_subItem.splice(1,_subItem.length-1);	
		}
		_subItem[0].sItemId = "";
		_subItem[0].option = "";
		if(Item.subOption.length>0){
			_subItem[0].checked = "1";
		}else{
			_subItem[0].checked = "3";
		}
		if(itemId){
			var qtype = this._items[itemId].isSubQue;
			logicIns.isSubQue = qtype;
			logicIns.itemId = itemId;
		}
		var c = this.getSubItemHtml(itemId);
		var $rMin = $(el).closest(".main_question_selector").next(".main_question_selector_right");
		$rMin.html(c);
		
		//删除关联问题
		var $logicResult = $(el).closest(".logicInner").children(".logicElement").find(".logicResult");
		$logicResult.children(".expressionContainer").remove();
		if(_relQuest.length > 1){
			_relQuest.splice(1,_relQuest.length-1);	
		}
		_relQuest[0].itemId = "";
		
		var relatedHtml = this._relatedHtml(_relQuest,itemId);
		$logicResult.append(relatedHtml);
	},
	
	getSubItemHtml: function(itemId){
		if(itemId){
			var Item = this._items[itemId];
			var c = '<div class="menubutton_right">',sOpt;
			if (Item.isSubQue == "Y"){
				if(Item.subQuestion.length > 0){
					sOpt = '<option value=""></option>';
					c += '	<a class="menubutton"><select class="chosen-select subQuestion" onChange="SurveyLogic.selSubAttribute(this,\'sItemId\');">';
					for(var i=0; i<Item.subQuestion.length; i++){
						sOpt += '<option value="'+Item.subQuestion[i].sItemId+'">'+Item.subQuestion[i].sItemName+'</option>';	
					}	
					c += sOpt + '</select></a>';
				}
				if(Item.subOption.length > 0){
					sOpt = '<option value=""></option>';	
					c += '	<a class="menubutton"><select class="chosen-select qOption" onChange="SurveyLogic.selSubAttribute(this,\'option\');">';
					for(var i=0; i<Item.subOption.length; i++){
						sOpt += '<option value="'+Item.subOption[i].sOptName+'">'+Item.subOption[i].sOptDesc+'</option>';	
					}	
					c += sOpt + '</select></a>';
				}
				c += '	<span class="expressionField">为</span>';
				c += '	<a class="menubutton">';
				c += '		<select class="chosen-select checkType" onChange="SurveyLogic.selSubAttribute(this,\'checked\');">';
				//c += '			<option value=""></option>';
				if(Item.subOption.length > 0){
					c += '			<option value="1">选中</option>';
					c += '			<option value="2">未选中</option>';
				}else{
					c += '			<option value="3">填写</option>';
					c += '			<option value="4">未填写</option>';
				}
				c += '		</select>';
				c += '	</a>';
				c += '	<div class="buttons">';
				c += '		<button type="button" class="btn-jian" onClick="SurveyLogic.delSubItem(this);"></button>&nbsp;';
				c += '		<button type="button" class="btn-add" onClick="SurveyLogic.addSubItem(this);"></button>';
				c += '	</div>';
			}else{
				if(Item.subOption.length > 0){
					sOpt = '<option value=""></option>';	
					c += '<a class="menubutton"><select class="chosen-select qOption" onChange="SurveyLogic.selSubAttribute(this,\'option\');">';
					for(var i=0; i<Item.subOption.length; i++){
						sOpt += '<option value="'+Item.subOption[i].sOptName+'">'+Item.subOption[i].sOptDesc+'</option>';	
					}	
					c += sOpt + '</select></a>';
				}
				c += '	<span class="expressionField">为</span>';
				c += '	<a class="menubutton">';
				c += '	<select class="chosen-select checkType" onChange="SurveyLogic.selSubAttribute(this,\'checked\');">';
				//c += '		<option value=""></option>';
				if(Item.subOption.length > 0){
					c += '		<option value="1">选中</option>';
					c += '		<option value="2">未选中</option>';
				}else{
					c += '		<option value="3">填写</option>';
					c += '		<option value="4">未填写</option>';
				}
				c += '	</select>';
				c += '</a>';
				
				if(Item.className == "MultipleChoice"){
					c += '	<div class="buttons">';
					c += '		<button type="button" class="btn-jian" onClick="SurveyLogic.delSubItem(this);"></button>&nbsp;';
					c += '		<button type="button" class="btn-add" onClick="SurveyLogic.addSubItem(this);"></button>';
					c += '	</div>';
				}
			}
			c += '</div>';
		}else{
			c = "";	
		}
		return c;
	},
	
	//选择子问题、选项、选中类型
	selSubAttribute: function(el,name){
		this.edit = true;
		//var logicId = $(el).closest(".logicInner").attr("logic-id");
		var LogicIndex = $(el).closest(".logicInner").index();
		var logicIns = this._logic[LogicIndex];
		var index = $(el).closest(".menubutton_right").index();
		var val = $(el).val();
		
		var itemId = logicIns.itemId;
		var currSquest = logicIns.subQuestion[index];
		currSquest[name] = val;
	},
	//选择关联问题
	selRelQuest: function(el){		
		this.edit = true;
		//var logicId = $(el).closest(".logicInner").attr("logic-id");
		var LogicIndex = $(el).closest(".logicElement").closest(".logicInner").index();
		var _relQuest = this._logic[LogicIndex].relatedQ;
		var index= $(el).closest(".expressionContainer").index();
		
		_relQuest[index-1].itemId = $(el).val();
	},
	//设置控制类型，1-显示，2-隐藏
	setCtlType: function(el){
		this.edit = true;
		var LogicIndex = $(el).closest(".logicElement").closest(".logicInner").index();
		var logicIns = this._logic[LogicIndex];
		logicIns.type = $(el).val();
	},
	validLogic: function(logic){
		var result = [true,""];
		for(var i=0; i<logic.length; i++){
			if(logic[i].itemId == ""){
				result[0]=false;
				result[1]="逻辑"+(i+1)+"主问题不能为空！"	
				break;
			}
			if(!result[0]) break;
			 
			var subQuestion = logic[i].subQuestion;
			var itemID = logic[i].itemId;
			var subOption = SurveyLogic._items[itemID].subOption;
			for(var j=0; j<subQuestion.length; j++){
				if(logic[i].isSubQue == "Y" && subQuestion[j].sItemId == ""){
					result[0]=false;
					result[1]="逻辑"+(i+1)+"的子问题选项不能为空！"	
					break;	
				}
				if(subOption.length > 0 && subQuestion[j].option == ""){
					result[0]=false;
					result[1]="逻辑"+(i+1)+"的可选项不能为空！"	
					break;		
				}
			}
			if(!result[0]) break;
			
			var relatedQ = logic[i].relatedQ;	
			for(var j=0; j<relatedQ.length; j++){
				if(relatedQ[j].itemId == ""){
					result[0]=false;
					result[1]="逻辑"+(i+1)+"的关联问题不能为空！"	
					break;	
				}	
			}
			if(!result[0]) break;
		}
		return result;
	}
}