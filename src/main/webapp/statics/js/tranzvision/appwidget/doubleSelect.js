/**
 * Created by caoy on 2017/9/12.
 * 双层联动下拉框
 */
SurveyBuild.extend("doubleSelect", "baseComponent", {
	itemName: "双层联动下拉框",
	title: "双层联动下拉框",
	isSingleLine: "Y",
	fixedContainer: "Y",//固定容器标识
	optionF: {},
	optionS: {},
	firstName:"",
	secondName:"",
	otherName:"",
	wzsm:"",
	other:"",
	children: [
	   		{
	   			"itemId": "first_select",
	   			"itemName": "firstName",
	   			"title":"firstName",
	   			"value": "",
	   			"StorageType": "S",
	   			"orderby":1,
	   			"classname":"Select",
	   			"wzsm":""
	   			//"option":"{}"
	   		},
	   		{
	   			"itemId": "second_select",
	   			"itemName": "secondName",
	   			"title": "secondName",
	   			"value": "",
	   			"StorageType": "S",
	   			"orderby":2,
	   			"classname":"Select",
	   			"wzsm":""
	   			//"option":"{}"
	   		},
	   		{
	   			"itemId": "other_input",
	   			"itemName": "otherName",
	   			"title": "otherName",
	   			"value": "",
	   			"StorageType": "S",
	   			"orderby":3,
	   			"classname":"SingleTextBox"
	   		}
	 ],
	
	
	_init: function(d, previewmode) {
		console.log("_init");
		if ($.isEmptyObject(this.optionF)) {
			/*如果下拉框无选项值，将初始化this.optionF*/
		} else {
			/*如果下拉框有选项值，直接返回*/
			return;
		}
		if ($.isEmptyObject(this.optionS)) {
			/*如果下拉框无选项值，将初始化this.optionS*/
		} else {
			/*如果下拉框有选项值，直接返回*/
			return;
		}
		for (var i = 1; i <= 3; ++i) {
			this.optionF[d + i] = {
				code: i,
				txt: "选项" + i,
				orderby: i,
				defaultval: 'N',
				other: 'N',
				weight: 0
			}
		}
		
		for (var i = 1; i <= 3; ++i) {
			this.optionS[d + i] = {
				code: i,
				txt: "选项" + i,
				orderby: i,
				defaultval: 'N',
				other: 'N',
				//默认父值是1
				weight: 0
			}
		}
	},
	_getHtml: function(data, previewmode) {
		var c = "",children = data.children,
		e = "";
		var isshow="N";
		if (previewmode) {
			if(SurveyBuild.accessType == "M"){
				//手机模式
				if (SurveyBuild._readonly) {
					//只读模式
		            e = '<option value="">' + MsgSet["PLEASE_SELECT"] + '</option>';
	                for (var i in data.optionF) {
	                    e += '<option ' + (children[0].value == data["optionF"][i]["code"] ? "selected='selected'": "") + 'value="' + data["optionF"][i]["code"] + '">' + data["optionF"][i]["txt"] + '</option>';
	                }
		            c += '<div class="item">';
		            c += '	<p>'+children[0].title+'<span>'+ (data.isRequire == "Y" ? "*": "") +'</span></p>';
		            c += '	<div class="text-box">';
		            c +='		<select name="' +data["itemId"] + children[0].itemId  + '" class="select1" id="' +data["itemId"] + children[0].itemId  + '"  title="' +data["itemId"] + children[0].itemName + '" disabled>';
		            c +=			e;
		            c +='		</select>';
		            c += '	</div>';
		            c += '</div>';
		            
					for (var i in data.optionF) {
						if (children[0].value == data["optionF"][i]["code"]) {
							isshow=data["optionF"][i]["other"];
						}
					}
					
					console.log(isshow);
					if (isshow=="Y") {
						c += '<div class="item">';
						c += '<p>'+children[2].title+'<span>'+(data.isRequire == "Y" ? "*": "")+'</span></p>';
						c += '<div class="text-box"><input ' + (data.isReadOnly == "Y" ? 'readonly="true"': '') + ' type="text" class="text1" id="' + data["itemId"] + children[2].itemId + '" name="' + data["itemId"] + children[2].itemId + '" value="' + children[2].value + '"/></div>';
						c += '<p style="color:#666;font-size:0.56rem;"></p>';
						c += '</div>';
					} else {
						e = '<option value="">' + MsgSet["PLEASE_SELECT"] + '</option>';
						for (var i in data.optionS) {
							e += '<option ' + (children[1].value == data["optionS"][i]["code"] ? "selected='selected'": "") + 'value="' + data["optionS"][i]["code"] + '">' + data["optionS"][i]["txt"] + '</option>';
						}
						c += '<div class="item">';
						c += '	<p>'+children[1].title+'<span>'+ (data.isRequire == "Y" ? "*": "") +'</span></p>';
						c += '	<div class="text-box">';
						c +='		<select name="' +data["itemId"] + children[1].itemId  + '" class="select1" id="' +data["itemId"] + children[1].itemId  + '"  title="' +data["itemId"] + children[1].itemName + '" disabled>';
						c +=			e;
						c +='		</select>';
						c += '	</div>';
						c += '</div>';
					}
		            
				} else {
					//编辑模式
					e = '<option value="">' + MsgSet["PLEASE_SELECT"] + '</option>';
	                for (var i in data.optionF) {
	                    e += '<option ' + (children[0]["value"] == data["optionF"][i]["code"] ? "selected='selected'": "") + 'value="' + data["optionF"][i]["code"] + '">' + data["optionF"][i]["txt"] + '</option>';
	                    if (children[0].value == data["optionF"][i]["code"]) {
							isshow=data["optionF"][i]["other"];
						}
	                }
	                data.other=isshow;
	                console.log(isshow);
	                c += '<div class="item">';
		            c += '	<p>'+children[0].title+'<span>'+ (data.isRequire == "Y" ? "*": "") +'</span></p>';
		            c += '  <div id="' + data["itemId"]+ children[0].itemId + 'Tip" class="tips" style="display: none;"><i></i><span></span></div>';
		            c += '	<div class="text-box">' ;
		            c +='		<select name="' + data["itemId"]+ children[0].itemId + '" class="select1" id="' + data["itemId"]+ children[0].itemId + '"  title="' + data["itemId"]+ children[0].itemName + '">';
		            c +=			e;
		            c +='		</select>';
		            c += '	</div>';
		            c += '</div>';


		            
		            //2。学历
		            e = '<option value="">' + MsgSet["PLEASE_SELECT"] + '</option>';
		            if (children[0].value !="") {
		            	for (var i in data.optionS) {
		            		if (data["optionS"][i]["weight"]==children[0].value ) {
		            			e += '<option ' + (children[1]["value"] == data["optionS"][i]["code"] ? "selected='selected'": "") + 'value="' + data["optionS"][i]["code"] + '">' + data["optionS"][i]["txt"] + '</option>';
		            		}
		            	}
		            }
		            
		            if (isshow=="Y") {
		            	c += '<div class="item" id="'+data.itemId +children[1].itemId+'Show" style="display:none">';
		            } else {
		            	c += '<div class="item" id="'+data.itemId +children[1].itemId+'Show">';
		            }
		            c += '	<p>'+children[1].title+'<span>'+ (data.isRequire == "Y" ? "*": "") +'</span></p>';
		            c += '  <div id="' + data["itemId"]+ children[1].itemId + 'Tip" class="tips" style="display: none;"><i></i><span></span></div>';
		            c += '	<div class="text-box">' ;
		            c +='		<select name="' + data["itemId"]+ children[1].itemId + '" class="select1" id="' + data["itemId"]+ children[1].itemId + '"  title="' + data["itemId"]+ children[1].itemName + '">';
		            c +=			e;
		            c +='		</select>';
		            c += '	</div>';
		            c += '</div>';
		            
		            if (isshow=="Y") {
		            	c += '<div class="item" id="'+data.itemId +children[2].itemId+'Show">';
		            } else {
		            	c += '<div class="item" style="display:none" id="'+data.itemId +children[2].itemId+'Show">';	
		            }
					c += '<p>'+children[2].title+'<span>'+(data.isRequire == "Y" ? "*": "")+'</span></p>';
					c += '<div id="' + data["itemId"]+ children[2].itemId + 'Tip" class="tips" style="display: none;"><i></i><span></span></div>';					
					c += '<div class="text-box"><input ' + (data.isReadOnly == "Y" ? 'readonly="true"': '') + ' type="text" class="text1" id="' + data["itemId"]+ children[2].itemId + '" name="' + data["itemId"]+ children[2].itemId + '" value="' + children[2]["value"] + '"/></div>';
					c += '</div>';

					
				}
			}else{
				//PC模式
				if (SurveyBuild._readonly) {
					var wzsmF = '';
					for (var i in data.optionF) {
						if (children[0].value == data["optionF"][i]["code"]) {
							wzsmF = data["optionF"][i]["txt"] ;
						}
	                }
					
					//只读模式
					c += '<div class="input-list">';
					c += '	<div class="input-list-info left"><span class="red-star">' + (data.isRequire == "Y" ? "*": "") + '</span>' + children[0].title + '</div>';
					c += '  <div class="input-list-text left">' +  wzsmF ;
					c += '  <input id="' + data.itemId +children[0].itemId+ '" type="hidden" name="" value="" readonly="" disabled="">';
					c += '  </div>';
					c += '  <div class="input-list-suffix left"></div>';
					c += '  <div class="clear"></div>';
					c += '</div>';
					
					for (var i in data.optionF) {
						if (children[0].value == data["optionF"][i]["code"]) {
							isshow=data["optionF"][i]["other"];
						}
					}
					
					if (isshow!="Y") {
						var wzsmS = '';
						for (var i in data.optionS) {
							if (children[1].value == data["optionS"][i]["code"]) {
								wzsmS = data["optionS"][i]["txt"] ;
							}
		                }	
						
						c += '<div class="input-list">';
						c += '	<div class="input-list-info left"><span class="red-star">' + (data.isRequire == "Y" ? "*": "") + '</span>' + children[1].title + '</div>';
						c += '  <div class="input-list-text left">' +  wzsmS ;
						c += '  <input id="' + data.itemId +children[1].itemId+ '" type="hidden" name="" value="" readonly="" disabled="">';
						c += '  </div>';
						c += '  <div class="input-list-suffix left"></div>';
						c += '  <div class="clear"></div>';
						c += '</div>';
					}
					
					
					
					
					console.log(isshow);
					if (isshow=="Y") {
						c += '<div class="input-list" >';
						c += '	<div class="input-list-info left"><span class="red-star">' + (data.isRequire == "Y" ? "*": "") + '</span>' + children[2].title + '</div>';
						c += '  <div class="input-list-text left">' + children[2].value + '</div>';
						c += '  <div class="input-list-suffix left"></div>';
						c += '  <div class="clear"></div>';
						c += '</div>';
					} 
					
				} else {
					
					console.log("first value:"+children[0].value);
	                e = '<option value="">' + MsgSet["PLEASE_SELECT"] + '</option>';
	                
	                //console.log("optionF :"+data.optionF);
	                for (var i in data.optionF) {
	                    e += '<option ' + (children[0].value == data["optionF"][i]["code"] ? "selected='selected'": "") + 'value="' + data["optionF"][i]["code"] + '">' + data["optionF"][i]["txt"] + '</option>';
	                    if (children[0].value == data["optionF"][i]["code"]) {
							isshow=data["optionF"][i]["other"];
						}
	                }
	                
	                //console.log(isshow);
	                data.other=isshow;
					c += '<div class="input-list">';
					c += '	<div class="input-list-info left"><span class="red-star">' + (data.isRequire == "Y" ? "*": "") + '</span>' + children[0].title + '</div>';
					c += '    <div class="input-list-text left input-edu-select">';
	                c += '          <select name="' + data.itemId +children[0].itemId+ '" class="chosen-select" id="' + data.itemId +children[0].itemId+ '" style="width:100%;">';
	                c +=                    e;
	                c += '          </select>';
					c += '    </div>';
					c += '    <div id="' + data.itemId +children[0].itemId + 'Tip" class="onShow"><div class="onShow"></div></div>';
					c += '    <div class="clear"></div>';
					c += '</div>';

					
					e = '<option value="">' + MsgSet["PLEASE_SELECT"] + '</option>';
					if (children[0].value !="") {
						for (var i in data.optionS) {
							if (data["optionS"][i]["weight"]==children[0].value ) {
								e += '<option ' + (children[1].value == data["optionS"][i]["code"] ? "selected='selected'": "") + 'value="' + data["optionS"][i]["code"] + '">' + data["optionS"][i]["txt"] + '</option>';
							}
						}
					}
					
					if (isshow=="Y") {
						c += '<div class="input-list" id="'+data.itemId +children[1].itemId+'Show" style="display:none">';
					} else {
						c += '<div class="input-list" id="'+data.itemId +children[1].itemId+'Show">';
					}
					c += '	<div class="input-list-info left"><span class="red-star">' + (data.isRequire == "Y" ? "*": "") + '</span>' + children[1].title + '</div>';
					c += '    <div class="input-list-text left input-edu-select">';
	                c += '          <select name="' + data.itemId +children[1].itemId+ '" class="chosen-select" id="' + data.itemId +children[1].itemId+ '" style="width:100%;">';
	                c +=                    e;
	                c += '          </select>';
					c += '    </div>';
					c += '    <div id="' + data.itemId +children[1].itemId + 'Tip" class="onShow"><div class="onShow"></div></div>';
					c += '    <div class="clear"></div>';
					c += '</div>';
					
					if (isshow=="Y") {
						c += '<div class="input-list" id="'+data.itemId +children[2].itemId+'Show">';
					} else {
						c += '<div class="input-list" style="display:none" id="'+data.itemId +children[2].itemId+'Show">';
					}
					c += '	<div class="input-list-info left"><span class="red-star">' + (data.isRequire == "Y" ? "*": "") + '</span>' + children[2].title + '</div>';
					c += '  <div class="input-list-text left"><input ' + (data.isReadOnly == "Y" ? 'readonly="true"': '') + ' type="text" class="inpu-list-text-enter" id="' + data.itemId +children[2].itemId + '" name="' + data.itemId +children[2].itemId + '" value="' + children[2].value + '"/></div>';
					c += '  <div id="' + data.itemId +children[2].itemId + 'Tip" class="onShow"><div class="onShow"></div></div>';
					c += '  <div class="clear"></div>';
					c += '</div>';
					 
				}
			}
			
		} else {
			//报名表配置页面
			var typeLi = '';
			//学位
			typeLi += '<div class="type_item_li">';
			typeLi += '	<span class="type_item_label"><span class="red-star">第一名称：</span>';
			typeLi += '		<b class="read-select" style="min-width:120px;">'+MsgSet["PLEASE_SELECT"]+'</b>';
			typeLi += '	</div>';

			//学历
			typeLi += '<div class="type_item_li">';
			typeLi += '	<span class="type_item_label"><span class="red-star">第二名称：</span>';
			typeLi += '		<b class="read-select" style="min-width:120px;">'+MsgSet["PLEASE_SELECT"]+'</b>';
			typeLi += '	</div>';
			
			c += '<div class="question-answer"  style="border:1px solid #ddd;padding:10px;">';
			c += '	<div class="DHContainer">' + typeLi + '</div>';
			c += '</div>';
			
		}
		return c;
	},
	_edit: function(data) {
		var children = data.children;
		var e = '',
		list = "",list2="";
		for (var i in data.optionF) {
			list += '<tr class="read-radio" data-id="' + data.instanceId+'-' + i + '">';
			list += '<td><input type="checkbox" onchange="$(\'.defaultval\').not(this).prop(\'checked\',false);SurveyBuild.saveLevel1AttrForDoubleSelect(this,\'defaultval\',\'optionF\')" class="defaultval" ' + (data["optionF"][i]["defaultval"] == "Y" ? "checked='checked'": "") + ' value="'+data["optionF"][i]["defaultval"]+'"></td>';
			list += '<td><input type="checkbox" onchange="$(\'.other\').not(this).prop(\'checked\',false);SurveyBuild.saveLevel1AttrForDoubleSelect(this,\'other\',\'optionF\');" class="other" ' + (data["optionF"][i]["other"] == "Y" ? "checked='checked'": "") + ' value="' + data["optionF"][i]["other"] + '"></td>';
			list += '<td><input type="text" onkeyup="SurveyBuild.saveLevel1AttrForDoubleSelect(this,\'code\',\'optionF\')" value="' + data["optionF"][i]["code"] + '" oncontextmenu="return false;" ondragenter="return false" onpaste="return false" class="ocode"></td>';
			list += '<td><input type="text" onkeyup="SurveyBuild.saveLevel1AttrForDoubleSelect(this,\'txt\',\'optionF\')" value="' + data["optionF"][i]["txt"] + '" oncontextmenu="return false;" ondragenter="return false" class="option-txt"></td>';
			list += '<td><a onclick="SurveyBuild.plusOptionForDoubleSelect(this,\'firstname\',\'optionF\');return false;" class="text-success" href="javascript:void(0);"><i class="icon-plus-sign"></i> </a><a onclick="SurveyBuild.minusOptionForDoubleSelect(this,\'optionF\');return false;" class="text-warning" href="javascript:void(0);"><i class="icon-minus-sign"></i> </a><a href="javascript:void(0);" class="text-info option-move"><i class="icon-move"></i> </a></td>';
			list += '</tr>';
		}
		
		for (var j in data.optionS) {
			list2 += '<tr class="read-radio" data-id="' + data.instanceId + '-' + j + '">';
			list2 += '<td><input type="text" onkeyup="SurveyBuild.saveLevel1AttrForDoubleSelect(this,\'code\')" value="' + data["optionS"][j]["code"] + '" oncontextmenu="return false;" ondragenter="return false" onpaste="return false" class="ocode"></td>';
			list2 += '<td><input type="text" onkeyup="SurveyBuild.saveLevel1AttrForDoubleSelect(this,\'txt\')" value="' + data["optionS"][j]["txt"] + '" oncontextmenu="return false;" ondragenter="return false" class="option-txt"></td>';
			list2 += '<td><input type="text" onkeyup="SurveyBuild.saveLevel1AttrForDoubleSelect(this,\'weight\')" value="' + data["optionS"][j]["weight"] + '" oncontextmenu="return false;" ondragenter="return false" onpaste="return false" class="ocode"></td>';
			list2 += '<td><a onclick="SurveyBuild.plusOptionForDoubleSelect(this,\'secondtname\',\'optionS\');return false;" class="text-success" href="javascript:void(0);"><i class="icon-plus-sign"></i> </a><a onclick="SurveyBuild.minusOptionForDoubleSelect(this,\'optionS\');return false;" class="text-warning" href="javascript:void(0);"><i class="icon-minus-sign"></i> </a><a href="javascript:void(0);" class="text-info option-move"><i class="icon-move"></i> </a></td>';
			list2 += '</tr>';
		}
		e = '<div class="edit_item_warp"><span class="edit_item_label">提示信息：</span>  <input type="text" class="medium" onkeyup="SurveyBuild.saveAttr(this,\'onShowMessage\')" value="' + data.onShowMessage + '"/></div>';
		
		
		e += '<div class="edit_item_warp">';
		e += '  <span class="edit_item_label">第一名称：</span>';
		e += '  <input type="text" class="medium" id="firstName" onkeyup="SurveyBuild.saveAttr(this,\'firstName\')" value="' + data.firstName + '"/>';
		e += '</div>';
		
		e += '<div class="edit_item_warp">';
		e += '  <span class="edit_item_label">第二名称：</span>';
		e += '  <input type="text" class="medium" id="secondName" onkeyup="SurveyBuild.saveAttr(this,\'secondName\')" value="' + data.secondName + '"/>';
		e += '</div>';
		
		e += '<div class="edit_item_warp">';
		e += '  <span class="edit_item_label">其他名称：</span>';
		e += '  <input type="text" class="medium" id="otherName" onkeyup="SurveyBuild.saveAttr(this,\'otherName\')" value="' + data.otherName + '"/>';
		e += '</div>';
		
		e += '<fieldset id="option-box">';
		e += '  <span class="edit_item_label titlewidth"><i class="icon-list-alt"></i> 选项设置</span>';
		e += '  <span class="edit_item_label titlewidth"><i class="icon-list-alt"></i> 第一选项</span>';
		e += '  <table class="table table-bordered data-table">';
		e += '      <thead>';
		e += '          <tr>';
		e += '              <th class="thw">默认</th>';
		e += '			    <th class="thw">其他</th>';
		e += '              <th>名称</th>';
		e += '              <th class="alLeft">描述<button onclick="SurveyBuild.optionBatchForDoubleSelect(\'' + data.instanceId + '\',\'' + data.itemId+ children[1].instanceId + '\',\'optionF\')" class="btn btn-primary btn-mini pull-right">批量编辑</button></th>';
		e += '              <th width="45">操作</th>';
		e += '          </tr>';
		e += '      </thead>';
		e += '      <tbody class="ui-sortable">' + list + '</tbody>';
		e += '  </table>';
		e += '  <span class="edit_item_label titlewidth"><i class="icon-list-alt"></i> 第二选项</span>';
		e += '  <table class="table table-bordered data-table">';
		e += '      <thead>';
		e += '          <tr>';
		e += '              <th>名称</th>';
		e += '              <th class="alLeft">描述<button onclick="SurveyBuild.optionBatchForDoubleSelect(\'' + data.instanceId + '\',\'' + data.itemId+ children[1].instanceId + '\',\'optionS\')" class="btn btn-primary btn-mini pull-right">批量编辑</button></th>';
		e += '              <th>关联值</th>';
		e += '              <th width="45">操作</th>';
		e += '          </tr>';
		e += '      </thead>';
		e += '      <tbody class="ui-sortable">' + list2 + '</tbody>';
		e += '  </table>';
		e += '</fieldset>';
		
		
		e += '<div class="edit_jygz">';
		e += '    <span class="title"><i class="icon-cog"></i> 校验规则</span>';
		e += '    <div class="groupbox">';
		e += '    <div class="edit_item_warp" style="margin-top:5px;">';
		e += '        <input class="mbIE" type="checkbox" onchange="SurveyBuild.saveAttr(this,\'isRequire\')"' + (data.isRequire == "Y" ? "checked='checked'": "") + ' id="is_require"> <label for="is_require">是否必填</label>';
		e += '    </div>';
		e += '    </div>';

		e += '    <div class="edit_item_warp">';
		e += '        <a href="javascript:void(0);" onclick="SurveyBuild.RulesSet(this);"><i class="icon-cogs"></i> 高级设置</a>';
		e += '    </div>';
		e += '</div>';

		return e;
	},
	_eventbind: function(data) {
		var children = data.children;
		
		var $inputBox = $("#" + data.itemId+children[0].itemId);
		var $inputBox2 = $("#" + data.itemId+children[1].itemId);
		
		var $inputBox3 = $("#" + data.itemId+children[2].itemId);
		
		
		$inputBox.on("change",function(){
            var selectIndex = $inputBox[0].selectedIndex;
            if($inputBox[0].options[selectIndex].value){
            	children[0].wzsm = $inputBox[0].options[selectIndex].text;
            }else{
            	children[0].wzsm = "";
            }
            
            var hasShow="N";
            for (var i in data.optionF) {
            	if($inputBox[0].options[selectIndex].value){
            		if ($inputBox[0].options[selectIndex].value == data["optionF"][i]["code"]) {
            			hasShow=data["optionF"][i]["other"];
            		}
            	}
			}
            
            data.other=hasShow;
            
            console.log("1111"+data.other);
            
            if (hasShow == "Y") {
            	$("#" + data.itemId+children[2].itemId+"Show").show();
            	$("#" + data.itemId+children[1].itemId+"Show").hide();
            	
            } else {
            	$("#" + data.itemId+children[2].itemId+"Show").hide();
            	$("#" + data.itemId+children[1].itemId+"Show").show();
            	//切换第二选择的option
            	var fvalue = $inputBox.val();
            	var svalue = $inputBox2.val();
            	var e='<option value="">'+MsgSet["PLEASE_SELECT"]+'</option>';
            	if (fvalue !="") {
            		for (var i in data.optionS) {
            			if (data["optionS"][i]["weight"]==fvalue ) {
            				e += '<option ' + (svalue == data["optionS"][i]["code"] ? "selected='selected'": "") + 'value="' + data["optionS"][i]["code"] + '">' + data["optionS"][i]["txt"] + '</option>';
            			}
            		}
            	}
            	$inputBox2.html(e);
            	$inputBox2.trigger("chosen:updated");
            	//TZ_TZ_7_1second_select_chosen
            	$("#" + data.itemId+children[1].itemId+"_chosen").css("width", "100%");
            }
            
        });
		
		$inputBox2.on("change",function(){
            var selectIndex = $inputBox2[0].selectedIndex;
            if($inputBox2[0].options[selectIndex].value){
            	children[1].wzsm = $inputBox2[0].options[selectIndex].text;          	
            }else{
            	children[1].wzsm = "";
            }
        });

		$inputBox.formValidator({tipID: (data["itemId"] + children[0]["itemId"] + 'Tip'),onShow: "",onFocus: "&nbsp;",onCorrect: "&nbsp;"});
		$inputBox.functionValidator({
			fun: function(val, elem) {
				//console.log("GOOOO");
				//执行高级设置中的自定义规则
				/*********************************************\
				 ** 注意：自定义规则中不要使用formValidator **
				\*********************************************/
				var _result = true;
				//console.log(ValidationRules);
				
				//console.log(data["rules"]);
				
				if (ValidationRules) {
					$.each(data["rules"],
					function(classname, classObj) {
						//console.log("classname:"+classname);
						//console.log("classObj:"+classObj);
						//单选钮不需要在高级规则中的必选判断
						if ($.inArray(classname, SurveyBuild._baseRules) == -1 && data["rules"][classname]["isEnable"] == "Y") {
							var _ruleClass = ValidationRules[classname];
							if (_ruleClass && _ruleClass._Validator) {
								_result = _ruleClass._Validator(data["itemId"] + children[0]["itemId"], classObj["messages"]);
								if (_result !== true) {
									return false;
								}
							}
						}
					});
					if (_result !== true) {
						return _result;
					}
				}
				return _result;
			}
		});
		
		
		/*var hhhshow="N";
		for (var i in data.optionF) {
        	if($inputBox.val()){
        		if ($inputBox.val() == data["optionF"][i]["code"]) {
        			hhhshow=data["optionF"][i]["other"];
        		}
        	}
		}*/
		
		
		//console.log(data.other);
			$inputBox3.formValidator({tipID: (data["itemId"] + children[2]["itemId"] + 'Tip'),onShow: "",onFocus: "&nbsp;",onCorrect: "&nbsp;"});
			$inputBox3.functionValidator({
				fun: function(val, elem) {
					console.log(data.other);					
					//执行高级设置中的自定义规则
					/*********************************************\
					 ** 注意：自定义规则中不要使用formValidator **
					\*********************************************/
					var _result = true; 
					if (ValidationRules && data.other=="Y") {
						$.each(data["rules"],
								function(classname, classObj) {
							//单	选钮不需要在高级规则中的必选判断
							if ($.inArray(classname, SurveyBuild._baseRules) == -1 && data["rules"][classname]["isEnable"] == "Y") {
								var _ruleClass = ValidationRules[classname];
								if (_ruleClass && _ruleClass._Validator) {
									_result = _ruleClass._Validator(data["itemId"] + children[2]["itemId"], classObj["messages"]);
									if (_result !== true) {
										return false;
									}
								}
							}
						});
						if (_result !== true) {
							return _result;
						}
					}
					return _result;
				}
			});

		
			$inputBox2.formValidator({tipID: (data["itemId"] + children[1]["itemId"] + 'Tip'),onShow: "",onFocus: "&nbsp;",onCorrect: "&nbsp;"});
			$inputBox2.functionValidator({
				fun: function(val, elem) {
					console.log(data.other);
					//执行高级设置中的自定义规则
					/*********************************************\
					 ** 注意：自定义规则中不要使用formValidator **
					\*********************************************/
					var _result = true;
					if (ValidationRules && data.other=="N") {
						$.each(data["rules"],
								function(classname, classObj) {
							//单	选钮不需要在高级规则中的必选判断
							if ($.inArray(classname, SurveyBuild._baseRules) == -1 && data["rules"][classname]["isEnable"] == "Y") {
								var _ruleClass = ValidationRules[classname];
								if (_ruleClass && _ruleClass._Validator) {
									_result = _ruleClass._Validator(data["itemId"] + children[1]["itemId"], classObj["messages"]);
									if (_result !== true) {
										return false;
									}
								}
							}
						});
						if (_result !== true) {
							return _result;
						}
					}
					return _result;
				}
			});
		

	}
});