/*====================================================================
+ 功能描述：国籍控件，用于选择省									++
+ 开发人：张浪														++
+ 开发日期：2015-05-04												++
=====================================================================*/
SurveyBuild.extend("Province", "baseComponent", {
   	itemName: "省",
	title:"省",
	"StorageType":"S",

	_getHtml: function(data, previewmode) {
		var c = "";
		
		if (previewmode) {
			if(SurveyBuild.accessType == "M"){
				if(SurveyBuild._readonly){
					c += '<div class="item">';
    				c += '	<p>'+data.title+'<span>'+(data.isRequire == "Y" ? "*": "")+'</span></p>';
    				c += '	  <div class="text-box">';
    				c += '	 	<a><input ' + (data.isReadOnly == "Y" ? 'readonly="true"': '') + ' type="text" class="text1" value="' + data.value + '"></a>';
    				c += '    </div>';
    				c += '</div>';
				}else{
					SurveyBuild.appInsId == "0" && this._getDefaultVal(data);
					
					  	c += '<div class="item">';
	    				c += '	<p>'+data.title+'<span>'+(data.isRequire == "Y" ? "*": "")+'</span></p>';
	    				c += '	<div id="' + data.itemId + 'Tip" class="tips" style="display: none;"><i></i><span></span></div>';			
	    				c += '	  <div class="text-box">';
	    				c += '		<a>						<input type="text" id="' + data.itemId + '" name="' + data.itemId + '" placeholder="请选择省份" value="' + data.value + '"></a>';
	    				c += '    </div>';
	    				c += '</div>';
	   				}
			}else{
				if(SurveyBuild._readonly){
					//只读模式
					c += '<div class="input-list">';
					c += '	<div class="input-list-info left"><span class="red-star">' + (data.isRequire == "Y" ? "*": "") + '</span>' + data.title + '</div>';
					c += '    <div class="input-list-text left">' + data.value + '</div>';
					c += '    <div class="input-list-suffix left"></div>';
					c += '    <div class="clear"></div>';
					c += '</div>';
				}else{
					//填写模式
					SurveyBuild.appInsId == "0" && this._getDefaultVal(data);
						
					c += '<div class="input-list">';
					c += '	<div class="input-list-info left"><span class="red-star">' + (data.isRequire == "Y" ? "*": "") + '</span>' + data.title + '</div>';
					c += '    <div class="input-list-text left"><input type="text" class="inpu-list-text-enter" id="' + data.itemId + '" name="' + data.itemId + '" title="' + data.itemName + '" value="' + data.value + '"/><img id="' + data.itemId + '_Btn" src="' + TzUniversityContextPath + '/statics/images/appeditor/new/location.png" class="input-icon" /></div>';
					c += '    <div class="input-list-suffix left"><div id="' + data.itemId + 'Tip" class="onShow"><div class="onShow"></div></div></div>';
					c += '    <div class="clear"></div>';
					c += '</div>';
				}
				
			}
			
		} else {
			c += '<div class="question-answer">';
			c += '	<div class="format ">';
			c += '		<b class="read-input">选择省</b>';
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
		
		if(SurveyBuild.accessType == "M"){
			var $inputBox = $("#" + data.itemId);
			var $selectBtn = $("#" + data.itemId + "_Btn");
			var siteId=$("#siteId").val();
			/*文本框和图标选择*/
			$.each([$inputBox,$selectBtn],function(i,el){
				var prov;
				el.click(function(e) { 
					$.each([$inputBox, $selectBtn],function(i, el) {
		              document.activeElement.blur();
		           });
		          
					$("#ParamCon").val(el.attr("id"));
					var tzParams = '{"ComID":"TZ_COMMON_COM","PageID":"TZ_M_PROVINCE_STD2","OperateType":"HTML","comParams":{"TZ_PROV_ID":"'+data.itemId+'","siteId":"'+siteId+'"}}';
					$.ajax({
						type: "post",
						async :false,
						data:{
							tzParams:tzParams
						},
						url: TzUniversityContextPath + "/dispatcher",
						dataType: "html",
						success: function(result){
							$("#searchCountry").html("");
							$("#searchCountry").html(result);
							$("#searchCountry").focus(function(){
		                     document.activeElement.blur();
								});
						
						
						$("#body").css("position","fixed");
						$("#MainDiv").hide();
						$("#searchCountry").fadeIn("slow");

						}
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
			
			
		}else{
			
			var $inputBox = $("#" + data.itemId);
			var $selectBtn = $("#" + data.itemId + "_Btn");
			var siteId=$("#siteId").val();
			/*文本框和图标选择*/
			$.each([$inputBox,$selectBtn],function(i,el){
				var prov;
				el.click(function(e) {
					var provinceUrl = SurveyBuild.tzGeneralURL + '?tzParams=';
					var params = '{"ComID":"TZ_COMMON_COM","PageID":"TZ_PROVINCE_STD","OperateType":"HTML","comParams":{"TPLID":"' + templId + '","TZ_PROV_ID":"' + data.itemId + '","siteId":"' + siteId + '"}}';
					provinceUrl = provinceUrl + window.escape(params);
		
					prov = $.layer({
						type: 2,
						title: false,
						fix: false,
						closeBtn: false,
						shadeClose: false,
						shade : [0.3 , '#000' , true],
						border : [3 , 0.3 , '#000', true],
						offset: ['100px',''],
						area: ['588px','300px'],
						iframe: {src: provinceUrl}
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

	}
})

