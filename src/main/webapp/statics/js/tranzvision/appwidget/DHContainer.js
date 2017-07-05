SurveyBuild.extend("DHContainer", "baseComponent", {
	isDoubleLine: "Y",
	itemName: "多行容器",
	title:"多行容器",
	"StorageType": "S",
	children: {},
	minLines: 1,
	maxLines: 4,
	defaultLines:1,
	_init: function(d, previewmode) {
		var linesNo = [];
		for (var i = 1; i < this.maxLines; i++) {
			linesNo.push(i);
		}
		this["linesNo"] = linesNo;
	},
	_getHtml: function(data, previewmode) {
		var c = "", children = data.children;
		if (previewmode) {
			if(SurveyBuild.accessType == "M"){
				c += '<input id="' + data.itemId + '" type="hidden" name="' + data.itemId + '">';
				
		    	c += '<div class="se_tit1">';
		    	c +=  data.title;
		    	c += '</div>';
		    	
		    	c += '<div class="workbg">' + data.wzsm + '</div>';
		    	c += '<div class="index_body">';

		        //c += '	<div class="addNext">';
		        //c += '	    <div class="input-list-suffix-blank right input-btn" style="width:17%">';
		    	if (children.length<data.maxLines) {
		        c += '			<div class="addNext clear"><div onclick="SurveyBuild.showDiv(this,\'' + data.instanceId + '\');" class="add_next">' + MsgSet["ADD_ONE"] + '</div>';
		    	} else {
		        	c += '	<div class="addNext clear" style="display:none;"><div onclick="SurveyBuild.showDiv(this,\'' + data.instanceId + '\');" class="add_next">' + MsgSet["ADD_ONE"] + '</div>';
		        }
		        c += '		</div>';
		        c += '		</div>';
		        //c += '	</div>';	    	
		    	//c += '</div>';
				
				
			} else {
			c += '<input id="' + data.itemId + '" type="hidden" name="' + data.itemId + '">';
			
	    	c += '<div class="mainright-title">';
	    	c += '	<span class="title-line"></span>' + data.title;
	    	c += '</div>';
	    	c += '<div class="workbg">' + data.wzsm + '</div>';
	    	c += '<div class="main_content_box">';

	        c += '	<div class="input-list-blank addNext">';
	        c += '	    <div class="input-list-suffix-blank right input-btn" style="width:17%">';
			/*
	        c += '	    	<div onclick="SurveyBuild.saveApp(this);" class="input-savebtn">' + MsgSet["SAVE"] + '&nbsp;&nbsp;<span class="input-btn-icon"><img src="' + TzUniversityContextPath + '/statics/images/appeditor/new/add-save.png" /></span></div>';
			*/
	        if (children.length<data.maxLines) {
	        c += '			<div onclick="SurveyBuild.showDiv(this,\'' + data.instanceId + '\');" class="input-addbtn">' + MsgSet["ADD_ONE"] + '&nbsp;&nbsp;<span class="input-btn-icon"><img src="' + TzUniversityContextPath + '/statics/images/appeditor/new/add-jia.png" /></span></div>';
	        } else {
	        	c += '			<div style="display:none;" onclick="SurveyBuild.showDiv(this,\'' + data.instanceId + '\');" class="input-addbtn">' + MsgSet["ADD_ONE"] + '&nbsp;&nbsp;<span class="input-btn-icon"><img src="' + TzUniversityContextPath + '/statics/images/appeditor/new/add-jia.png" /></span></div>';
	        }
	        c += '		</div>';
	        c += '		<div class="clear"></div>';
	        c += '	</div>';	    	
	    	c += '</div>';
			}
		} else {
			c = '<ul class="DHContainer DHSort" data-dhbz="Y" style="min-height:150px;"></ul>';
		}
		return c;
	},
	_edit: function(data) {
		var e = "";
		e = '<div class="edit_item_warp">';
        e += '  <span class="edit_item_label">描述：</span>';
        e += '  <input type="text" class="medium" onchange="SurveyBuild.saveAttr(this,\'wzsm\')" id="rows" value="' + data.wzsm + '"/>';
        e += '</div>';
		//参数设置
		e += '<div class="edit_jygz">';
		e += '	    <span class="title"><i class="icon-info-sign"></i> 参数设置</span>';
		e += '      <div class="groupbox">';

		e += '          <div class="edit_item_warp" style="margin-top:5px;">';
		e += '          	<span class="edit_item_label">最小行数：</span>';
		e += '				<input type="text" maxlength="1" class="medium edit_attaType minLines" onkeyup="SurveyBuild.saveAttr(this,\'minLines\')" value="' + data.minLines + '"/>';
		e += '          </div>';

		e += '          <div class="edit_item_warp mb10">';
		e += '          	<span class="edit_item_label">最大行数：</span>';
		e += '				<input type="text" maxlength="2" class="medium edit_attaType maxLines" onkeyup="SurveyBuild.saveAttr(this,\'maxLines\')" value="' + data.maxLines + '"/>';
		e += '          </div>';
		
		e += '          <div class="edit_item_warp mb10">';
		e += '          	<span class="edit_item_label">默认行数：</span>';
		e += '				<input type="text" maxlength="2" class="medium edit_attaType defaultLines" onkeyup="SurveyBuild.saveAttr(this,\'defaultLines\')" value="' + data.defaultLines + '"/>';
		e += '          </div>';

		e += '      </div>';
		//高级设置
		e += '      <div class="edit_item_warp">';
		e += '          <a href="javascript:void(0);" onclick="SurveyBuild.RulesSet(this);"><i class="icon-cogs"></i> 高级设置</a>';
		e += '      </div>';
		e += '</div>';
		return e;
	},
	_validatorAttr: function(data){
		var $max = $("#question-edit .maxLines");
		if (data.maxLines > 15){
			SurveyBuild.fail($max, "最大行数不能超过15行！");
			return false;	
		}
		 return true;
	},
	_eventbindEditor: function(data) {
		$("#question-box li[data_id='" + data.instanceId + "']").find(".DHContainer").droppable({
			drop: function(event, obj) {
				var _classname = $(obj.draggable).attr("data-classname");
				if ($.inArray(_classname, ["Page", "DHContainer"]) > -1) {
					SurveyBuild.isDHForTwo = true;
					return;
				}
				if (!SurveyBuild.isDHForTwo) {
					SurveyBuild.isDHForTwo = true;
					SurveyBuild.isDHContainer = true;
					SurveyBuild.currentDHID = data.instanceId;
					SurveyBuild.add(_classname, $(event.target));

				}
			}
		})
	},
	_eventbind: function(data) {
		var $inputBox = $("#" + data.itemId);
		$inputBox.formValidator({tipID:(data["itemId"]+'Tip'), onShow:"", onFocus:"&nbsp;", onCorrect:"&nbsp;"});

		$inputBox.functionValidator({
			fun:function(val,elem){
				var _result = true;
				if (ValidationRules) {
					$.each(data["rules"],function(classname, classObj) {
						//单选钮不需要在高级规则中的必选判断
						if ($.inArray(classname, SurveyBuild._baseRules) == -1 && data["rules"][classname]["isEnable"] == "Y") {
							var _ruleClass = ValidationRules[classname];
							if (_ruleClass && _ruleClass._Validator) {
								_result = _ruleClass._Validator(data["itemId"], classObj["messages"],data);
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
	}
})