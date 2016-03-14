SurveyBuild.extend("DHContainer", "baseComponent", {
	isDoubleLine: "Y",
	itemName: "多行容器",
	title:"多行容器",
	"StorageType": "S",
	children: {},
	minLines: 1,
	maxLines: 4,
	linesNo:[1,2,3],

	_getHtml: function(data, previewmode) {
		var c = "", children = data.children;
		if (previewmode) {
			c += '<input id="' + data.itemId + '" type="hidden" name="' + data.itemId + '">';
			c += '<div class="main_inner_content_title">';
			c += '	<span class="reg_title_star">' + (data.isRequire == "Y" ? "*": "") + '</span>';
			c += '	<span class="reg_title_grey_17px">' + data.title + '</span>';
			c += '  <div style="float: right;width: 40px;">';
			c += '		<div id="' + data.itemId + 'Tip" class="onShow" style="margin: 0px; padding: 0px; background: transparent;">';
			c += '			<div class="onShow"></div>';
			c += '		</div>';
			c += '	</div>';
			c += '</div>';

			c += '<div class="main_inner_content_top"></div>';
			c += '<div class="main_inner_content">';
			c += '	<div class="main_inner_content_info" style="' + (data.maxLines == "1" ? "display:none": "") + '">';
			c += '		<div class="bmb_save">';
			c += '			<div class="bt_blue" onclick="SurveyBuild.saveApp(this);">' + MsgSet["SAVE"] + '</div>';
			c += '			<a href="javascript:;" class="alpha"></a>';
			c += '		</div>';
			c += '		<div class="main_inner_content_info_add addnextbtn" onclick="SurveyBuild.showDiv(this,\'' + data.instanceId + '\');">';
			c += '			<div class="bt_blue">' + MsgSet["ADD_ONE"] + '</div>';
			c += '		</div>';
			c += '	</div>';
			c += '</div>';
			c += '<div class="main_inner_content_foot"></div>';
		} else {
			c = '<ul class="DHContainer DHSort" data-dhbz="Y" style="min-height:150px;"></ul>';
		}
		return c;
	},
	_edit: function(data) {
		var e = "";

		//参数设置
		e += '<div class="edit_jygz">';
		e += '	    <span class="title"><i class="icon-info-sign"></i> 参数设置</span>';
		e += '      <div class="groupbox">';

		e += '          <div class="edit_item_warp" style="margin-top:5px;">';
		e += '          	<span class="edit_item_label">最小行数：</span>';
		e += '				<input type="text" maxlength="1" class="medium edit_attaType" onkeyup="SurveyBuild.saveAttr(this,\'minLines\')" value="' + data.minLines + '"/>';
		e += '          </div>';

		e += '          <div class="edit_item_warp mb10">';
		e += '          	<span class="edit_item_label">最大行数：</span>';
		e += '				<input type="text" maxlength="1" class="medium edit_attaType" onkeyup="SurveyBuild.saveAttr(this,\'maxLines\')" value="' + data.maxLines + '"/>';
		e += '          </div>';

		e += '      </div>';
		//高级设置
		e += '      <div class="edit_item_warp">';
		e += '          <a href="javascript:void(0);" onclick="SurveyBuild.RulesSet(this);"><i class="icon-cogs"></i> 高级设置</a>';
		e += '      </div>';
		e += '</div>';
		return e;
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