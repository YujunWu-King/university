/*====================================================================
 + 功能描述：院校选择							                     ++
 + 开发人：王瑞立													 ++
 + 开发日期：2016-07-21												 ++
 =====================================================================*/
SurveyBuild.extend("University", "baseComponent", {
    itemName: "院校选择",
    title:"院校选择",
    isSingleLine:"Y",
    fixedContainer:"Y",//固定容器标识
    children: [
			{
				"itemId":"country",
				"itemName":MsgSet["CY"],
				"value":"",
				"StorageType":"S",
				"ccode":"",
				"classname":"Nationality"
			},
			{
				"itemId":"sch",
				"itemName":MsgSet["sch"],
				"value":"",
				"StorageType": "S",
				"classname":"SingleTextBox"
			}
		],
    _getHtml: function(data, previewmode) {
        var children = data.children;
		var c = '<input id="CountryCode" type="hidden" name="CountryCode" value="CHN">';
		
		
		
        if (previewmode) {
        	
            if(SurveyBuild._readonly){
                //只读模式

                /*c += '<div class="main_inner_content_info_autoheight cLH">';
                c += '  <div class="main_inner_connent_info_left">';
                c += '      <span class="reg_title_star">' + (data.isRequire == "Y" ? "*": "") + '</span>' + data.title;
                c += '  </div>';
                c += '  <div class="main_inner_content_info_right" >';
                c += '      <span style="' + (children[0]["value"] ? "line-height:25px;" : "") + '">' + children[0]["value"] + (children[0]["value"] ? "<br>" : "") + children[1]["value"] + '</span>';
                c += '  </div>';
                c += '</div>' */
                	
                	
                c += '<div class="input-list">';
                c += '	<div class="input-list-info left"><span class="red">' + (data.isRequire == "Y" ? "*": "") + '</span>' + data.title + '</div>';
                c += '  <div class="input-list-text left"><span style="line-height:24px">' + children[0]["value"] + '<br>' + children[1]["value"] + '</span></div>';
                c += '	<div class="input-list-suffix left"></div>';
                c += '	<div class="clear"></div>';
                c += '</div>';	
                	
            }else{
                //填写模式
                SurveyBuild.appInsId == "0" && this._getDefaultVal(data,"P2");
                
                
              //modity by caoy 增加对于默认值的处理
        		var defaulCountry = data.defaultval;
        		//console.log("111"+defaulCountry);
        		
        		if (defaulCountry != undefined  && defaulCountry.length > 0) {
        			params = '{"ComID":"TZ_COMMON_COM","PageID":"TZ_COUNTRY_STD","OperateType":"EJSON","comParams":{"OType":"BYCOUNTRY","search-text":"' +defaulCountry + '"}}';
					$.ajax({
                        type: "POST",
                        dataType: "JSON",
                        data:{
                            tzParams:params
                        },
                        async:false,
                        url:SurveyBuild.tzGeneralURL,
                        success: function(f) {
                        	var data = [];
							if(f.state.errcode == "0"){
								data = f.comContent;
							}
							if(data.length) {
								//遍历data，添加到自动完成区
								$.each(data, function(index,term) {
									if (term.descr == defaulCountry) {
										children[0]["value"] = term.descr;
										children[0]["ccode"] = term.country;
									}
								});
							}
                        }
                    });
        		}

                /*c += '<div class="main_inner_content_info_autoheight">';
                c += '	<div class="main_inner_connent_info_left">';
                c += '		<span class="reg_title_star">' + (data.isRequire == "Y" ? "*": "") + '</span>' + data.title;
                c += '	</div>';
                c += '	<div class="main_inner_content_info_right">';
                c += '		<input type="text" ccode="'+ children[0]["ccode"] +'" title="' + MsgSet["CY"] + '" value="' + children[0]["value"] + '" id="' + data["itemId"] + children[0]["itemId"] + '" class="input_63px" name="' + data["itemId"] + children[0]["itemId"] + '">';
                c += '		<input type="text" title="' + MsgSet["sch"] + '" value="' + children[1]["value"] + '" id="' + data["itemId"] + children[1]["itemId"] + '" class="input_180px" name="' + data["itemId"] + children[1]["itemId"] + '">';
                c += '		<div style="margin-top:-40px;margin-left:256px">';
                c += '			<div id="' + data["itemId"] + data.children[0]["itemId"] + 'Tip" style="margin: 0px; padding: 0px; background: transparent none repeat scroll 0% 0%;" class="onCorrect">';
                c += '              <div class="onCorrect">&nbsp;</div>';
                c += '			</div>';
                c += '		</div>';
                c += '	</div>';
                c += '</div>'; */
                
                c += '<div class="input-list">';
                c += '	<div class="input-list-info left"><span class="red">' + (data.isRequire == "Y" ? "*": "") + '</span>' + data.title + '</div>';
                c += '	<div class="input-list-textdate left input-date-select" style="width:12.5%">';
                c += '    	<input type="text" class="inpu-list-text-enter" ccode="'+ children[0]["ccode"] +'" title="' + MsgSet["CY"] + '" value="' + children[0]["value"] + '" id="' + data["itemId"] + children[0]["itemId"] + '" name="' + data["itemId"] + children[0]["itemId"] + '" readOnly="true" ><img id="' + data["itemId"] + data.children[0]["itemId"] + '_Btn" src="' + TzUniversityContextPath + '/statics/images/appeditor/new/location.png" class="input-icon" />';
                c += '	</div>';
                c += '	<div class="input-list-textdate left input-date-select" style="width: 21%; margin: 0 15px 0 0;">';
                c += '    	<input type="text" class="inpu-list-text-enter" title="' + MsgSet["sch"] + '" value="' + children[1]["value"] + '" id="' + data["itemId"] + children[1]["itemId"] + '" name="' + data["itemId"] + children[1]["itemId"] + '" readOnly="true" >';
                c += '	</div>';
                c += '	<div class="input-list-suffix left"><div id="' + data["itemId"]  + children[0]["itemId"] + 'Tip" class="onShow"><div class="onShow">&nbsp;</div></div></div>';
                c += '	<div class="clear"></div>';
                c += '</div>';

            }
        } else {
            c += '<div class="question-answer">';
            c += '  <b style="min-width: 80px" class="read-select">国家</b>';
            c += '  <b style="min-width: 400px;" class="read-input">&nbsp;</b>';
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
        e += '  <div class="edit_item_warp" style="margin-top:5px;">';
        e += '      <input class="mbIE" type="checkbox" onchange="SurveyBuild.saveAttr(this,\'isRequire\')"' + (data.isRequire == "Y" ? "checked='checked'": "") + ' id="is_require"> <label for="is_require">是否必填</label>';
        e += '  </div>';
        e += '  </div>';
		
        e += '  <div class="edit_item_warp">';
        e += '      <a href="javascript:void(0);" onclick="SurveyBuild.RulesSet(this);"><i class="icon-cogs"></i> 高级设置</a>';
        e += '  </div>';
        e += '</div>';
        return e;
    },
    _eventbind: function(data) {
        var national = $("#" + data["itemId"] + data.children[0]["itemId"]);
        var sch = $("#" + data["itemId"] + data.children[1]["itemId"]);
        
        var $selectBtn = $("#" + data["itemId"] + data.children[0]["itemId"] + "_Btn");

        //national.click(function(e) {
        $.each([national,$selectBtn],function(i,el){
			el.click(function(e) {

				var nationalUrl = SurveyBuild.tzGeneralURL + '?tzParams=';
				var params = '{"ComID":"TZ_COMMON_COM","PageID":"TZ_COUNTRY_STD","OperateType":"HTML","comParams":{"TPLID":"' + templId + '"}}';
				nationalUrl = nationalUrl + window.escape(params);
			
				$("#ParamCon").val(data["itemId"] + data.children[0]["itemId"]);
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

        sch.click(function(e) {
			var schoollUrl = SurveyBuild.tzGeneralURL + '?tzParams=';
			var params = '{"ComID":"TZ_COMMON_COM","PageID":"TZ_SCHOOL_STD","OperateType":"HTML","comParams":{"TPLID":"' + templId + '","Type":"B"}}';
			schoollUrl = schoollUrl + window.escape(params);

			var ccode = national.attr("ccode");
			var natVal = national.val();

			if(!natVal){
				national.attr("ccode","CHN");
			}
			if(!ccode){
				national.attr("ccode","CHN");
			}
			$("#ParamValue").val(data["itemId"] + data.children[1]["itemId"]);
			$("#CountryCode").val(data["itemId"] + data.children[0]["itemId"]);
			//console.log("11:"+$("#CountryCode").val());
			//console.log("11:"+$("#CountryCode").attr("ccode"));
			s = $.layer({
				type: 2,
				title: false,
				fix: false,
				closeBtn: false,
				shadeClose: false,
				shade: [0.3, '#000', true],
				border: [3, 0.3, '#000', true],
				offset: ['50%', ''],
				area: ['830px', '720px'],
				iframe: {src: schoollUrl}
			});
		});
        national.formValidator({tipID:(data["itemId"] + data.children[0]["itemId"] + 'Tip'),onShow:"",onFocus:"&nbsp;",onCorrect:"&nbsp;"});
        sch.formValidator({tipID:(data["itemId"] + data.children[0]["itemId"] + 'Tip'),onShow:"",onFocus:"&nbsp;",onCorrect:"&nbsp;"});

        national.functionValidator({
            fun:function(val,elem){

                //执行高级设置中的自定义规则
                /*********************************************\
                 ** 注意：自定义规则中不要使用formValidator **
                 \*********************************************/
                var _result = true;
                if (ValidationRules) {
                    $.each(data["rules"],function(classname, classObj) {
                        //单选钮不需要在高级规则中的必选判断
                        if ($.inArray(classname, SurveyBuild._baseRules) == -1 && data["rules"][classname]["isEnable"] == "Y") {
                            var _ruleClass = ValidationRules[classname];
                            if (_ruleClass && _ruleClass._Validator) {
                                _result = _ruleClass._Validator(data["itemId"] + data.children[0]["itemId"], classObj["messages"]);
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
        sch.functionValidator({
            fun:function(val,elem){

                //执行高级设置中的自定义规则
                /*********************************************\
                 ** 注意：自定义规则中不要使用formValidator **
                 \*********************************************/
                var _result = true;
                if (ValidationRules) {
                    $.each(data["rules"],function(classname, classObj) {
                        //单选钮不需要在高级规则中的必选判断
                        if ($.inArray(classname, SurveyBuild._baseRules) == -1 && data["rules"][classname]["isEnable"] == "Y") {
                            var _ruleClass = ValidationRules[classname];
                            if (_ruleClass && _ruleClass._Validator) {
                                _result = _ruleClass._Validator(data["itemId"] + data.children[1]["itemId"], classObj["messages"]);
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
});