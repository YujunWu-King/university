/*====================================================================
+ 功能描述：国籍控件，用于选择国籍									++
+ 开发人：张浪														++
+ 开发日期：2015-05-04												++
=====================================================================*/
SurveyBuild.extend("Nationality", "baseComponent", {
	itemName: "国籍",
	title: "国籍",
	"StorageType": "S",

	_getHtml: function(data, previewmode) {
		var c = "";

		if (previewmode) {
			if(SurveyBuild.accessType == "M"){
				if (SurveyBuild._readonly) {
					//只读模式
					/*
					c += '<div class="input-list">';
					c += '	<div class="input-list-info left"><span class="red-star">' + (data.isRequire == "Y" ? "*": "") + '</span>' + data.title + '</div>';
					c += '    <div class="input-list-text left">' + data.value + '</div>';
					c += '    <div class="input-list-suffix left"></div>';
					c += '    <div class="clear"></div>';
					c += '</div>';
					*/
					
					c += '<div class="item">';
    				c += '	<p>'+data.title+'<span>'+(data.isRequire == "Y" ? "*": "")+'</span></p>';
    				c += '	  <div class="text-box">';

    				c += '	 	<a><input ' + (data.isReadOnly == "Y" ? 'readonly="true"': '') + ' type="text" class="text1" value="' + data.value + '"></a>';
    				c += '    </div>';
    				c += '</div>';
					
				} else {
					//填写模式
					SurveyBuild.appInsId == "0" && this._getDefaultVal(data);
					/*
					c += '<div class="input-list">';
					c += '	<div class="input-list-info left"><span class="red-star">' + (data.isRequire == "Y" ? "*": "") + '</span>' + data.title + '</div>';
					c += '    <div class="input-list-text left"><input type="text" class="inpu-list-text-enter" id="' + data.itemId + '" name="' + data.itemId + '" title="' + data.itemName + '" value="' + data.value + '"/><img id="' + data.itemId + '_Btn" src="' + TzUniversityContextPath + '/statics/images/appeditor/new/location.png" class="input-icon" /></div>';
					c += '    <div class="input-list-suffix left"><div id="' + data.itemId + 'Tip" class="onShow"><div class="onShow"></div></div></div>';
					c += '    <div class="clear"></div>';
					c += '</div>';
					*/

				  	c += '<div class="item">';
    				c += '	<p>'+data.title+'<span>'+(data.isRequire == "Y" ? "*": "")+'</span></p>';
    				c += '	  <div class="text-box">';
//    				c += '	 	<a href="pop_city.html"><input type="text" class="text1"  id="' + data.itemId + '" name="' + data.itemId + '" title="' + data.itemName + '" value="' + data.value + '"></a>';
//    				c += '	 	<a href="pop_county.html"><input type="text" class="text1"  name="' + data.itemId + '" title="' + data.itemName + '" value="' + data.value + '"></a>';
    				c += '		<a>						  <input type="text" id="' + data.itemId + '" name="' + data.itemId + '"  value="' + data.value + '" placeholder="请选择国家" ccode=""></a>';
//    				c += '		<a>						  <input type="text" id="m' + data.itemId + '" name="' + data.itemId + '"  value="' + data.value + '" placeholder="请选择国家" ccode=""></a>';
    				
    				c += '    </div>';
    				c += '</div>';
//    				c += '<div class="viewport-adaptive" id="searchCountry" style="display:none;width: 100%;min-width: 320px;max-width:750px;margin: 0 auto;height: auto;" ></div>';
//    				c += '<div><a><input type="button" id="TEST" value="按钮"></a></div>';
				}
			}else{
				if (SurveyBuild._readonly) {
					//只读模式
					c += '<div class="input-list">';
					c += '	<div class="input-list-info left"><span class="red-star">' + (data.isRequire == "Y" ? "*": "") + '</span>' + data.title + '</div>';
					c += '    <div class="input-list-text left">' + data.value + '</div>';
					c += '    <div class="input-list-suffix left"></div>';
					c += '    <div class="clear"></div>';
					c += '</div>';
				} else {
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
		
		e += '<div class="edit_item_warp">';
		e += '  <span class="edit_item_label">关联项：</span>';
		e += '  <input type="text" class="medium" id="linkItems" onkeyup="SurveyBuild.saveAttr(this,\'linkItems\')" value="' + data.linkItems + '"/>';
		e += '</div>';
		
		e += '<div class="edit_jygz">';
		e += '	<span class="title"><i class="icon-cog"></i> 校验规则</span>';
		e += '  <div class="groupbox">';
		e += '	<div class="edit_item_warp" style="margin-top:5px;">';
		e += '		<input class="mbIE" type="checkbox" onchange="SurveyBuild.saveAttr(this,\'isRequire\')"' + (data.isRequire == "Y" ? "checked='checked'": "") + ' id="is_require"> <label for="is_require">是否必填</label>';
		e += '	</div>';
		e += '	</div>';

		e += '  <div class="edit_item_warp">';
		e += '      <a href="javascript:void(0);" onclick="SurveyBuild.RulesSet(this);"><i class="icon-cogs"></i> 高级设置</a>';
		e += '  </div>';
		e += '</div>';
		return e;
	},

	_eventbind: function(data) {
		
		if(SurveyBuild.accessType == "M"){
			//顾贤达    2017年6月12日 11:15:18 手机版TEST
			
//			var $inputBox = $("#m" + data.itemId);
//			var $selectBtn = $("#m" + data.itemId + "_Btn");
//			
			var $inputBox = $("#" + data.itemId);
			var $selectBtn = $("#" + data.itemId + "_Btn");
			var siteId=$("#siteId").val();
			

//			$.each([$("#TZ_COUNTRY"),$("#TZ_COUNTRY_click"),$("#TZ_SCH_CNAME_Country"),$("#TZ_SCH_CNAME"),$("#TZ_LEN_PROID")],function(i,el){
			$.each([$inputBox, $selectBtn],function(i, el) {
				el.focus(function(){
		        document.activeElement.blur();
		       })
		     });
			/*var $inputBoxTEST=$("#TEST");
			$.each([$inputBoxTEST],function(i, el) {
				el.click(function(e) { 
//					console.log("click");
					console.log($("#m" + data.itemId).val());
					
		       })
		     });*/
//			$.each([$("#TZ_COUNTRY"),$("#TZ_COUNTRY_click"),$("#TZ_SCH_CNAME_Country")],function(i,el){	
			$.each([$inputBox, $selectBtn],function(i, el) {
				el.click(function(e) { 
				
					$("#ParamCon").val(el.attr("id"));
//					var tzParams = '{"ComID":"TZ_COMMON_COM","PageID":"TZ_M_COUNTRY_STD","OperateType":"HTML","comParams":{"orgid":"'+strJgid+'","siteId":"'+strSiteId+'","lang":"'+$("#lang").val()+'","sen":"2"}}';
					var tzParams = '{"ComID":"TZ_COMMON_COM","PageID":"TZ_M_COUNTRY_STD2","OperateType":"HTML","comParams":{"orgid":"SEM","siteId":"72","lang":"'+$("#lang").val()+'","sen":"2"}}';
					
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
							//$("#body").css("position","fixed");
							//$(".shade").show();
//							console.log(result);
//							$("#before").hide();
							$("#MainDiv").hide();
						    $("#searchCountry").fadeIn("slow"); 
		                     loaded ();
						}
					});
					console.log($("#" + data.itemId).val());
				});
			});
			
		}else{
			
			var $inputBox = $("#" + data.itemId);
			var $selectBtn = $("#" + data.itemId + "_Btn");
			var siteId=$("#siteId").val();
			/*文本框和图标选择*/
			$.each([$inputBox, $selectBtn],function(i, el) {
				el.click(function(e) {
					var nationalUrl = SurveyBuild.tzGeneralURL + '?tzParams=';
					var params = '{"ComID":"TZ_COMMON_COM","PageID":"TZ_COUNTRY_STD","OperateType":"HTML","comParams":{"TPLID":"' + templId+ '","siteId":"' + siteId + '"}}';
					nationalUrl = nationalUrl + window.escape(params);

					$("#ParamCon").val(data.itemId);
					i2 = $.layer({
						type: 2,
						title: false,
						fix: false,
						closeBtn: false,
						shadeClose: false,
						shade: [0.3, '#000', true],
						border: [3, 0.3, '#000', true],
						offset: ['50%', ''],
						area: ['830px', '610px'],
						iframe: {
							src: nationalUrl
						}
					});
				});
			});
			$inputBox.formValidator({tipID: data["itemId"] + 'Tip',onShow: "",onFocus: "&nbsp;",onCorrect: "&nbsp;"});
			$inputBox.functionValidator({
				fun: function(val, elem) {
					//执行高级设置中的自定义规则
					/*********************************************\
					 ** 注意：自定义规则中不要使用formValidator **
					\*********************************************/
					var _result = true;
					if (ValidationRules) {
						$.each(data["rules"],
						function(classname, classObj) {
							if ($.inArray(classname, SurveyBuild._baseRules) == -1 && data["rules"][classname]["isEnable"] == "Y") {
								var _ruleClass = ValidationRules[classname];
								if (_ruleClass && _ruleClass._Validator) {
									_result = _ruleClass._Validator(data["itemId"], classObj["messages"]);
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
				}
			});
			/*关联项*/
			if(data.linkItems){
				var $linkitems = $("#" + data.linkItems);
				if($linkitems && $linkitems.length > 0){
					$inputBox.change(function(e) {
						$linkitems.val($inputBox.val());
						$linkitems.trigger("change");
						$linkitems.trigger("blur");
					});
				}
			}
		}

	}
})