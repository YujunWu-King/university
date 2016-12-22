/**
 * Created by ZhangLang on 2015/9/29.
 */
SurveyBuild.extend("GridSingleChoice", "baseComponent", {
    itemName: "表格单选题",
    title: "表格单选题",
    StorageType:"T",
    qCode:"",//题号
	isAvg:"N",
    option: {},
	child: {},
	//oneChoice: 'N',//每列/行只能选择一个
	subHorizontal: 'N',//子问题横向排列
    _init: function(d, previewmode) {
        if ($.isEmptyObject(this.option)) {
            /*如果表格单选题无选项值，将初始化this.option*/
			for (var i = 1; i <= 3; ++i) {
				this.option[d + i] = {
					code: i,
					txt: "选项" + i,
					orderby: i,
					defaultval: 'N',
					other: 'N',
					weight: 0,
					othervalue :'',
					checked:"N"
				}
        	}
        } 
		if ($.isEmptyObject(this.child)) {
            /*如果表格单选题无子问题，将初始化this.children*/
			var n = "S" + ( + new Date());
			for (var i = 1; i <= 3; ++i) {
				this.child[n + i] = {
					sqCode: i,
					question: "子问题" + i,
					shortDesc: '简称'+ i,
					orderby: i,
					weight: 0,
					value: []
				}
        	}
        } 
    },
    _getHtml: function(data, previewmode) {
        var c = "",e = "",s = "";

        if (previewmode) {
            if(SurveyBuild.accessType == "P"){
				//PC版
                c += '<div class="listcon">';
				c += '	<div class="question">';
				c += '		<span class="fontblue-blod">'+ data.qCode +'.</span>' + data.title;
				c += '      <div id="' + data.itemId + 'Tip" class="onShow">';
                c += '          <div class="onShow"></div>';
                c += '      </div>';
				c += '	</div>';
				c += '	<div class="answer">';
				c += '		<table width="100%" border="0" cellspacing="0" cellpadding="0" class="onechoose" id="' + data.itemId + '">';
				c += '			<tr class="gray-bg">';
				c += '				<td>&nbsp;</td>';
				for (var opt in data.option){
					c += '			<td>'+ data.option[opt].txt +'</td>';	
				}
				c += '			</tr>';
				
				var row = 0;
				for (var ch in data.child) {
					row ++;
					c += '		<tr '+ (row%2 == 0 ? 'class="gray-bg"':'') +'>';
					//c += '			<td width="12%">'+ data.child[ch].question +'</td>';
					c += '			<td style="text-align: left;">'+ data.child[ch].question +'</td>';
					for (var opt in data.option) {
						var optCode = data.option[opt].code;

						//c += '		<td width="8.8%">';
						c += '		<td>';
						c += '			<div class="input-radio-btn '+ (data.child[ch].value.indexOf(optCode) != -1 ? "checked": "") +'" style="width:20px;">';
						c += '				<input type="radio" onchange="SurveyBuild.handleInput(this);" id="R'+ data.itemId + '_' + data.child[ch].sqCode + '_' + data.option[opt].code +'" name="'+ data.itemId + '_' + data.child[ch].sqCode +'" value="'+ data.option[opt].code +'" child-instance="'+ ch +'" opt-instance="'+ opt +'" '+ (data.child[ch].value.indexOf(optCode) != -1 ? "checked='checked'": "") +'>';
						c += '			</div>';
						c += '		</td>';	
					}
					c += '		</tr>';	
				}
				c += '		</table>';
				c += '	</div>';
				c += '</div>';
            }else{
				//手机版
                c += '<div class="listcon">';
				c += '  <div id="' + data.itemId + 'Tip" class="onShow">';
				c += '      <div class="onShow"></div>';
				c += '  </div>';
				c += '	<div class="question">';
				c += '		<span class="fontblue-blod">'+ data.qCode +'.</span>' + data.title;
				c += '	</div>';
				c += '	<div class="answer-onechoose" id="' + data.itemId + '">';
				for (var ch in data.child) {
					c += '	<div class="answer-manychoose">';
					c += '		<h1><span class="orange-circle"></span>'+ data.child[ch].question +'</h1>';
					c += '		<ul>';
					for (var opt in data.option) {
						var optCode = data.option[opt].code;
						c += '		<li><div class="input-radio-btn '+ (data.child[ch].value.indexOf(optCode) != -1 ? "checked": "") +'"><input type="radio" onchange="SurveyBuild.handleInput(this);" id="R'+ data.itemId + '_' + data.child[ch].sqCode + '_' + data.option[opt].code +'"  name="'+ data.itemId + '_' + data.child[ch].sqCode +'" value="'+ data.option[opt].code +'" child-instance="'+ ch +'" opt-instance="'+ opt +'" '+ (data.child[ch].value.indexOf(optCode) != -1 ? "checked='checked'": "") +'>&nbsp;&nbsp;<label for="R'+ data.itemId + '_' + data.child[ch].sqCode + '_' + data.option[opt].code +'">'+ data.option[opt].txt +'</label></div></li>';
					}
					c += '		</ul>';
					c += '	</div>';
				}
				c += '	</div>';
				c += '</div>';
            }
        } else {
			s += '<tr><td></td>';
            for (var i in data.option) {
                s += '<td id="o'+ i +'">'+ data["option"][i]["txt"] +'</td>'
            }
			s += '</tr>';
			
			for (var i in data.child) {
				s += '<tr>';
				s += '<th id="sq'+ i +'">'+ data["child"][i]["question"] +'</th>';
				for (var j in data.option) {
					s += '<td><img src="' + TzUniversityContextPath + '/statics/js/survey/assets/img/read-radio.gif"></td>';	
				}
				s += '</tr>';	
			}
            c = '<div class="question-answer"><table class="table table-bordered"><tbody>' + s + '</tbody></table></div>'
        }
        return c;
    },
    _edit: function(data) {
        var e = '',list = "",childList = "";
		//子问题
		for (var i in data.child) {
			childList += '<tr class="read-radio" data-id="' + data.instanceId + '-' + i + '">';	
			 //值
            childList += '<td><input type="text" onkeyup="SurveyBuild.saveChildAttr(this,\'sqCode\')" value="' + data["child"][i]["sqCode"] + '" oncontextmenu="return false;" ondragenter="return false" onpaste="return false" class="ocode"></td>';
            //描述
            childList += '<td><input type="text" onkeyup="SurveyBuild.saveChildAttr(this,\'question\')" value="' + data["child"][i]["question"] + '" oncontextmenu="return false;" ondragenter="return false" class="option-txt"></td>';
			//简称
			childList += '<td><input type="text" onkeyup="SurveyBuild.saveChildAttr(this,\'shortDesc\')" value="' + data["child"][i]["shortDesc"] + '" oncontextmenu="return false;" ondragenter="return false" class="option-txt"></td>';
            //操作
            childList += '<td><a onclick="SurveyBuild.plusChildQuestion(this);return false;" class="text-success" href="javascript:void(0);"><i class="icon-plus-sign"></i> </a><a onclick="SurveyBuild.minusChildQuestion(this);return false;" class="text-warning" href="javascript:void(0);"><i class="icon-minus-sign"></i> </a><a href="javascript:void(0);" class="text-info option-move"><i class="icon-move"></i> </a></td>';
            childList += '</tr>';
		}
		//选项
        for (var i in data.option) {
            list += '<tr class="read-radio" data-id="' + data.instanceId + '-' + i + '">';
			/*
			 //默认
            list += '<td><input type="checkbox" onchange="$(\'.defaultval\').not(this).prop(\'checked\',false);SurveyBuild.saveLevel1Attr(this,\'defaultval\')" class="defaultval" ' + (data["option"][i]["defaultval"] == "Y" ? "checked='checked'": "") + ' value="1"></td>';
            //其他
            list += '<td><input type="checkbox" onchange="$(\'.other\').not(this).prop(\'checked\',false);SurveyBuild.saveLevel1Attr(this,\'other\');" class="other" ' + (data["option"][i]["other"] == "Y" ? "checked='checked'": "") + '  value="' + data["option"][i]["other"] + ' "></td>';
			*/
            //选项编码
            list += '<td><input type="text" onkeyup="SurveyBuild.saveLevel1Attr(this,\'code\')" value="' + data["option"][i]["code"] + '" oncontextmenu="return false;" ondragenter="return false" onpaste="return false" class="ocode"></td>';
            //描述
            list += '<td><input type="text" onkeyup="SurveyBuild.saveLevel1Attr(this,\'txt\')" value="' + data["option"][i]["txt"] + '" oncontextmenu="return false;" ondragenter="return false" class="option-txt"></td>';
            //分值
            list += '<td><input type="text" onkeyup="SurveyBuild.saveLevel1Attr(this,\'weight\')" value="' + data["option"][i]["weight"] + '" oncontextmenu="return false;" ondragenter="return false" onpaste="return false" class="ocode"></td>';
            //操作
            list += '<td><a onclick="SurveyBuild.plusOption_radio(this);return false;" class="text-success" href="javascript:void(0);"><i class="icon-plus-sign"></i> </a><a onclick="SurveyBuild.minusOption(this);return false;" class="text-warning" href="javascript:void(0);"><i class="icon-minus-sign"></i> </a><a href="javascript:void(0);" class="text-info option-move"><i class="icon-move"></i> </a></td>';
            list += '</tr>';
        }
		
		//子问题设置
		e += '<fieldset id="child-box">';
        e += '		<span class="edit_item_label titlewidth"><i class="icon-list-alt"></i> 子问题设置</span>';
        e += '		<table class="table table-bordered data-table">';
        e += '			<thead>';
        e += '				<tr>';
        //e += '					<th class="thw">默认</th>';
        //e += '					<th class="thw">其他</th>';
        e += '					<th>编号</th>';
        e += '					<th class="alLeft">子问题<button onclick="SurveyBuild.childQuestBatch(\'' + data.instanceId + '\')" class="btn btn-primary btn-mini pull-right">批量编辑</button></th>';
		e += '					<th width="55">简称</th>';
        e += '					<th width="45">操作</th>';
        e += '				</tr>';
        e += '			</thead>';
        e += '			<tbody class="ui-sortable">' + childList + '</tbody>';
        e += '		</table>';
        e += '</fieldset>';

		//选项设置
        e += '<fieldset id="option-box">';
        e += '		<span class="edit_item_label titlewidth"><i class="icon-list-alt"></i> 选项设置</span>';
        e += '		<table class="table table-bordered data-table">';
        e += '			<thead>';
        e += '				<tr>';
        //e += '					<th class="thw">默认</th>';
        //e += '					<th class="thw">其他</th>';
        e += '					<th class="thw">编号</th>';
        e += '					<th class="alLeft">答案<button onclick="SurveyBuild.optionBatch(\'' + data.instanceId + '\')" class="btn btn-primary btn-mini pull-right">批量编辑</button></th>';
		e += '                	<th>分值</th>';
        e += '					<th width="45">操作</th>';
        e += '				</tr>';
        e += '			</thead>';
        e += '			<tbody class="ui-sortable">' + list + '</tbody>';
        e += '		</table>';
        e += '</fieldset>';
		/*
        //默认值
        e += '<div class="edit_item_warp">';
        e += '    <span class="edit_item_label">默认值：</span>';
        e += '   <input type="text" class="medium" id="defaultval" onkeyup="SurveyBuild.saveAttr(this,\'defaultval\')" value="' + data.defaultval + '"/>';
        e += '</div>';
		*/
		//设置
        e += '<div class="edit_jygz">';
        e += '	<span class="title"><i class="icon-cog"></i> 设置</span>';
        e += '  <div class="groupbox">';
		e += '		<div class="edit_item_warp" style="margin-top:5px;">';
		e += '			<input class="mbIE" onchange="SurveyBuild.saveAttr(this,\'isAvg\')" ' + (data.isAvg == "Y" ? "checked='checked'": "") + ' id="isAvg" type="checkbox">';
		e += '			<label for="isAvg">是否计算平均分</label>';
		e += '		</div>';
		e += '	</div>';
		e += '</div>';

        //校验规则
        e += '<div class="edit_jygz">';
        e += '	<span class="title"><i class="icon-cog"></i> 校验规则</span>';
        e += '  <div class="groupbox">';
        e += '	    <div class="edit_item_warp" style="margin-top:5px;">';
        e += '		    <input class="mbIE" type="checkbox" onchange="SurveyBuild.saveAttr(this,\'isRequire\')"' + (data.isRequire == "Y" ? "checked='checked'": "") + ' id="is_require"> <label for="is_require">是否必填</label>';
		e += '	    </div>';
		/*
		e += '	    <div class="edit_item_warp" style="margin-top:5px;">';
		e += '		    <input class="mbIE" type="checkbox" onchange="SurveyBuild.saveAttr(this,\'subHorizontal\')"' + (data.subHorizontal == "Y" ? "checked='checked'": "") + ' id="subHorizontal"> <label for="subHorizontal">子问题横向排列</label>';
        e += '	    </div>';
		e += '	    <div class="edit_item_warp" style="margin-top:5px;">';
		e += '		    <input class="mbIE" type="checkbox" onchange="SurveyBuild.saveAttr(this,\'oneChoice\')"' + (data.oneChoice == "Y" ? "checked='checked'": "") + ' id="oneChoice"> <label for="oneChoice">每列/行只能选择一个</label>';
        e += '	    </div>';
		*/
        e += '	</div>';
        //高级设置
        e += '	<div class="edit_item_warp">';
        e += '		<a href="javascript:void(0);" onclick="SurveyBuild.RulesSet(this);"><i class="icon-cogs"></i> 高级设置</a>';
        e += '	</div>';
        e += '</div>';
        return e;
    },
    _eventbind: function(data) {
		$.each(data.child,function(i,ch){
			$.each(data.option,function(i,opt){
				var $gridRadio = $("#R" + data.itemId + '_' + ch.sqCode + '_' + opt.code);
				
				$gridRadio.click(function(e) {
					var chIns = $(this).attr("child-instance");
					var optIns = $(this).attr("opt-instance");
					var _oldVal = data.child[chIns].value[0];
					//var oneChecked = false;
					
					var cOpt = data.option[optIns].code;
					//data.child[chIns].value[0] = cOpt;
					
					//每列/行只能选择一个,控制答案只能被一个子问题选中
					/*张浪注释，不要“每列/行只能选择一个”
					if(data.oneChoice == "Y"){
						for(var cins in data.child){
							var index = data.child[cins].value.indexOf(cOpt);
							if(chIns != cins && index != -1){
								oneChecked = true;
							}	
						}
					}
					if(!oneChecked){
						if(SurveyBuild.accessType == "P"){
							$(this).closest("tr").find(".input-radio-btn").removeClass("checked");
						}else{
							$(this).closest("ul").find(".input-radio-btn").removeClass("checked");
						}
						$(this).closest(".input-radio-btn").addClass("checked");
						data.child[chIns].value[0] = cOpt;
					} else {
						if(_oldVal) $("#R" + data.itemId + '_' + ch.sqCode + '_' + _oldVal)[0].checked = true;
						//alert("每列/行只能选择一个");
						noteing("每列/行只能选择一个",2);
					}
					*/
					if(SurveyBuild.accessType == "P"){
						$(this).closest("tr").find(".input-radio-btn").removeClass("checked");
					}else{
						$(this).closest("ul").find(".input-radio-btn").removeClass("checked");
					}
					$(this).closest(".input-radio-btn").addClass("checked");
					data.child[chIns].value[0] = cOpt;
                });
			});
			
			
			var $inputBox = $(":radio[name='"+ data.itemId + "_" + ch.sqCode +"']");
			//判断是否为必填
			var allowEmpty = true;
			var errorMsg = "&nbsp;";
			if (ValidationRules) {
				$.each(data["rules"],function(classname, classObj) {
					if ($.inArray(classname, SurveyBuild._baseRules) == -1 && data["rules"][classname]["isEnable"] == "Y") {
						//必填校验
						if(classname == "RequireValidator" && data.isRequire == "Y"){
							allowEmpty = false;
							//errorMsg = classObj["messages"]+ch.sqCode;
							errorMsg = "子问题没有全部答完！";
						}
					}
				});
			}
	
			$inputBox.formValidator({tipID:(data["itemId"]+'Tip'), onShow:"&nbsp;", onFocus:"&nbsp;", onCorrect:"&nbsp;"});
			if(!allowEmpty){
				$inputBox.inputValidator({min:1, onError:errorMsg});
			}
			
			$inputBox.functionValidator({
				fun:function(val,elem){
	
					//执行高级设置中的自定义规则
					/*********************************************\
					 ** 注意：自定义规则中不要使用formValidator **
					 \*********************************************/
					var _result = true;
					if (ValidationRules) {
						$.each(data["rules"],function(classname, classObj) {
							//单选钮不需要在高级规则中的必选判断
							if ($.inArray(classname, SurveyBuild._baseRules) == -1 && data["rules"][classname]["isEnable"] == "Y" && classname!="RequireValidator") {
								var _ruleClass = ValidationRules[classname];
								if (_ruleClass && _ruleClass._Validator) {
									_result = _ruleClass._Validator(data["itemId"], classObj["messages"]);
									if(_result !== true){
										return false;
									}
								}
							}
						});
						if(_result !== true){
							return _result;
						}
					}
					return _result;
				}
			});
			
		});
    }
});
