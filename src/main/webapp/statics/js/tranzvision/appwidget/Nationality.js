/*====================================================================
+ 功能描述：国籍控件，用于选择国籍									++
+ 开发人：张浪														++
+ 开发日期：2015-05-04												++
=====================================================================*/
SurveyBuild.extend("Nationality", "baseComponent", {
    itemName: "国籍",
	title:"国籍",
	"StorageType":"S",

	_getHtml: function(data, previewmode) {
		var c = "";
		
		if (previewmode) {

			if(SurveyBuild._readonly){
				//只读模式

				c += '<div class="main_inner_content_info_autoheight cLH">';
				c += '  <div class="main_inner_connent_info_left">';
				c += '      <span class="reg_title_star">' + (data.isRequire == "Y" ? "*": "") + '</span>' + data.title;
				c += '  </div>';
				c += '  <div class="main_inner_content_info_right" >' + data.value + '</div>';
				c += '</div>'
			}else{
				//填写模式
				SurveyBuild.appInsId == "0" && this._getDefaultVal(data);
				c += '<div class="main_inner_content_info_autoheight">';
				c += '	<div class="main_inner_connent_info_left">';
				c += '		<span class="reg_title_star">' + (data.isRequire == "Y" ? "*": "") + '</span>' + data.title;
				c += '	</div>';
				c += '	<div class="main_inner_content_info_right">';
				c += '		<input id="' + data.itemId + '" class="input_251px" name="' + data.itemId + '" type="text"  title="' + data.itemName + '" value="' + data.value + '" />';
				c += '		<img id="' + data.itemId + '_Btn" src="/tranzvision/images/search.png" style="position:relative;left:-40px;cursor:pointer;">';
				c += '		<div style="margin-top:-40px;margin-left:256px">';
				c += '			<div id="' + data.itemId + 'Tip" class="onShow" style="margin: 0px; padding: 0px; background: transparent;">';
				c += '				<div class="onShow"></div>';
				c += '			</div>';
				c += '		</div>';
				c += '	</div>';
				c += '</div>'
			}
		} else {
			c += '<div class="question-answer">';
			c += '	<div class="format">';
			c += '		<b class="read-input">选择国家</b>';
			c += '	</div>';
			c += '</div>';
		}
		return c;
	},
	
	_edit: function(data) {
		var e = "";
        e += '<div class="edit_item_warp">';
        e += '  <span class="edit_item_label">默认值：</span>';
        e += '  <input type="text" class="medium" id="defaultval" onkeyup="SurveyBuild.saveAttr(this,\'defaultval\')" value="' + data.defaultval + '"/>';
        e += '</div>';

        e += '<div class="edit_item_warp" style="text-align:right;">';
        e += '  <a href="javascript:void(0);" onclick="SurveyBuild.DynamicBindVal()" class="">动态绑定值</a>';
        e += '</div>';

		e += '<div class="edit_jygz">';
		e += '	<span class="title"><i class="icon-cog"></i> 校验规则</span>';
		e += '  <div class="groupbox">';
		e += '	<div class="edit_item_warp" style="margin-top:5px;">';
		e += '		<input class="mbIE" type="checkbox" onchange="SurveyBuild.saveAttr(this,\'isRequire\')"'+(data.isRequire == "Y" ? "checked='checked'" :"")+' id="is_require"> <label for="is_require">是否必填</label>';
		e += '	</div>';
		e += '	</div>';
		
		e += '  <div class="edit_item_warp">';
		e += '      <a href="javascript:void(0);" onclick="SurveyBuild.RulesSet(this);"><i class="icon-cogs"></i> 高级设置</a>';
		e += '  </div>';
		e += '</div>';
		return e;
	},
	
	_eventbind:function(data){
		var $inputBox = $("#" + data.itemId);
		var $selectBtn = $("#" + data.itemId + "_Btn");

		/*文本框和图标选择*/
		$.each([$inputBox,$selectBtn],function(i,el){
			el.click(function(e) {
				var nationalUrl = SurveyBuild.tzGeneralURL + '?tzParams=';
				var params = '{"ComID":"TZ_COMMON_COM","PageID":"TZ_COUNTRY_STD","OperateType":"HTML","comParams":{"TPLID":"' + templId + '"}}';
				nationalUrl = nationalUrl + window.escape(params);
	
				$("#ParamCon").val(data.itemId);
				i2 = $.layer({
					type: 2,
					title: false,
					fix: false,
					closeBtn: false,
					shadeClose: false,
					shade : [0.3 , '#000' , true],
					border : [3 , 0.3 , '#000', true],
					offset: ['50%',''],
					area: ['830px','610px'],
					iframe: {src: nationalUrl}
				});
			});
		});
		$inputBox.formValidator({tipID:data["itemId"]+'Tip',onShow:"",onFocus:"&nbsp;",onCorrect:"&nbsp;"});
		$inputBox.functionValidator({
			fun: function(val,elem) {
				//执行高级设置中的自定义规则
				/*********************************************\
				 ** 注意：自定义规则中不要使用formValidator **
				\*********************************************/
				var _result = true;
				if (ValidationRules) {
					$.each(data["rules"],function(classname, classObj) {
						if ($.inArray(classname, SurveyBuild._baseRules) == -1 && data["rules"][classname]["isEnable"] == "Y") {
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
			}
		})
	}
})