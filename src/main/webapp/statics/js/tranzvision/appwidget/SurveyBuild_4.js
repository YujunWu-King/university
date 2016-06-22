var SurveyBuild = {
    _tid: 0,
    _data: {},
    _items: {},
    _events: {},
    _count: 0,
    BMB_LANG:'',
    tzGeneralURL:"",
    refLetterId:"",
    _mainTplArr:"",
    _qid: [],
    _oid: [],
    _preg: {"email": {"name": "邮箱","regExp": "/^[\\w-]+(\\.[\\w-]+)*@[\\w-]+(\\.[\\w-]+)+$/"},"telphone": {"name": "手机","regExp": "/^1\\d{10}$/"},"idcard": {"name": "身份证号","regExp": "/(^\\d{15}$)|(^\\d{18}$)|(^\\d{17}(\\d|X|x)$)/"},"url": {"name": "网址URL","regExp": "/(http|ftp|https):\\/\\/[\\w\\-_]+(\\.[\\w\\-_]+)+([\\w\\-\\.,@?^=%&amp;:/~\\+#]*[\\w\\-\\@?^=%&amp;/~\\+#])?/"}},
    is_edit: false,
    is_edit_moda: true,
    _alph: ['A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'],
    _color: ["#FFFFFF", "#5D762A", "#FF0000", "#800080", "#008000", "#855B00", "#000000", "#FFFF00", "#990000", "#FFA500", "#E4E4E4", "#D2691E", "#1EDDFF", "#FFFFB1", "#98FB98", "#BDB76B", "#666666", "#4B0082", "#041690", "#FFB6C1", "#DDA0DD", "#0000FF", "url(" + TzUniversityContextPath + "/statics/images/appeditor/mixed.png);", "url(" + TzUniversityContextPath + "/statics/images/appeditor/trans.png);"],
    comClass: {},//控件实例类
    _components: {},
    _componentConfig: [],//控件后台配置信息
    _componentIndex: [],//
    _componentLoadedIndex: [],
    _helpDescSet: {},//控件属性说明信息
	_baseRules: [],
    _define: function(clsassname, source, target) {
        var me = this;
        this["comClass"][clsassname] = function(source) {
            source = $.extend(true, {}, me["comClass"][clsassname], source);
            for (var p in source) {
                if (source.hasOwnProperty(p)) {
                    this[p] = source[p];
                }
            }
        };
        //prototype继承，hasOwnProperty()
        if (target) {
            this["comClass"][clsassname].prototype = new this["comClass"][target]();
            this["comClass"][clsassname].prototype.constructor = this["comClass"][clsassname];
        }
        for (var p in source) {
            if (source.hasOwnProperty(p)) {
                if (typeof source[p] == "function") {
                    if (!this["comClass"][clsassname]) this["comClass"][clsassname] = function() {};
                    this["comClass"][clsassname].prototype[p] = source[p];
                } else {
                    me["comClass"][clsassname][p] = source[p];
                }
            }
        }
        me["comClass"][clsassname]["classname"] = clsassname;
        var c = me["comClass"][clsassname];
    },
    define: function(clsassname, source, fun) {
        if (source && typeof source == "object") {
            this._define(clsassname, source);
            if (fun && typeof fun == "function") {
                fun.apply(source);
            }
        }
        return this["comClass"][clsassname];
    },
    extend: function(classname, target, source) {
        if (classname && target && source) {
            if (typeof target == "string") {
                //被继承的JS对象没有被加载
                if (!this["comClass"][target]) {
                    this.loadScript(target);
                }
                target = cloneObj(new this["comClass"][target]());
            }
            var source = $.extend(true, {}, target, source);
            this._define(classname, source, target["classname"]);
        }
    },
    loadScript: function(className, callBack, params) {
        var isLoadad = true;
        var me = this;
        if (!className) return false;
        var i = $.inArray(className, this._componentLoadedIndex);
        if (i > -1) {
            if (callBack && typeof callBack == "function") {
                if (!params) params = [className];
                if (params && !$.isArray(params)) params = [className];
                callBack.call(me, params);
            }
            return isLoadad;
        }
        i = $.inArray(className, this._componentIndex);
        var fileUrl = this._componentConfig[i] && $.trim(this._componentConfig[i]["jsfileUrl"]);
        if (i > -1 && fileUrl) {
            $.ajax({
                type: "get",
                dataType: "script",
                data: {},
                async: false,
                cache: false,
                url: fileUrl,
                success: function() {
                    me._componentLoadedIndex.push(className);
                    if (callBack && typeof callBack == "function") {
                        if (!params) params = [className];
                        if (params && !$.isArray(params)) params = [className];
                        callBack.call(me, params);
                    }
                },
                complete: function(xhr, status) {
                    if (status == "success") {
		    } else {
                        alert(fileUrl + "加载失败！");
                    }
                }
            });

        }
    },
	_initTab: function() {
		var tabs = "", i = 0;
		$("#tabNav").empty();
		$.each(this._items, function(h, obj) {
			if (obj["classname"] == "Page") {
				tabs += '<div data_id="' + h + '" class="' + (i == 0 ? "tabNav_c": "tabNav") + '" onclick="SurveyBuild._editTabs();return false;" style="' + obj.tapStyle + '">' + obj.title + '</div>';
				++i;
			}
		});
		$("#tabNav").append(tabs);
	},
	/*张彬彬修改*/
    _editTabs: function() {
        $("#question-box>li").removeClass("active");
        $("#build-right").css("height", "auto");
        $("#href2").click();
        var e = "",tabs = "";
        $.each(this._items, function(h, obj) {
            if (obj["classname"] == "Page") {
                tabs += '<tr>';
                tabs += '   <td>';
                tabs += '       <input data_id="' + h + '" type="text" class="option-txt" value="' + obj.itemName + '" onkeyup="SurveyBuild.saveTabAttr(this,\'itemName\')" />';
                tabs += '   </td>';
                tabs += '   <td>';
                tabs += '       <input data_id="' + h + '" type="text" class="option-txt" value="' + obj.tapWidth + '" onkeyup="SurveyBuild.saveTabAttr(this,\'tapWidth\')" />';
                tabs += '   </td>';
				tabs += '   <td>';
                tabs += '   <a href="javascript:void(0);" style="color:#0088cc;" onmouseover="this.style.color=\'#faac3c\'"  onmouseout="this.style.color=\'#0088cc\'" onclick="SurveyBuild.editorTap(\'' + h + '\')">编辑</a>';
                tabs += '   </td>';
                tabs += '</tr>';
            }
        });

        e += '<fieldset id="option-box">';
		var data = this._data;
		if(!data.hasOwnProperty("pageHeight")){
			data.pageHeight = 55;
		}
		e += '<div class="edit_item_warp" style="padding-bottom:8px"><span class="edit_item_label"><span style="color:red">*</span>页签高度：</span><input type="text" style="width:170px;" onkeyup="SurveyBuild.saveTabAttr(this,\'pageHeight\')" value="' + data.pageHeight + '"> px</div>';
		
        e += '  <table class="table table-bordered data-table">';
        e += '      <thead>';
        e += '          <tr>';
        e += '              <th width="140" style="text-align:center">页签名称</th>';
        e += '              <th width="70" style="text-align:center">宽度(px)</th>';
		e += '              <th width="60" style="text-align:center">显示文字</th>';
        e += '         </tr>';
        e += '      </thead>';
        e += '      <tbody>' + tabs + '</tbody>';
        e += '  </table>';
        e += '</fieldset>';

        $(window).scrollTop();
        $("#question-edit").html(e)
    },
    saveTabAttr: function(el, attrName) {
        if (!el || !attrName) return;
        var instanceId = $(el).attr("data_id");
        var val = this._getAttrVal(el);
	
        if(attrName == "itemName"){
        	this._items[instanceId][attrName] = val;
            $("#question-box li[data_id='" + instanceId + "'] .pagename").html(val);
        }
        if(attrName == "tapWidth"){
            if (!el || !attrName) return;
            var instanceId = $(el).attr("data_id");
            var val = this._getAttrVal(el);
            
            this._items[instanceId][attrName] = val;
            
    		var data = this._data;
    		var pageHeight = data.hasOwnProperty("pageHeight") ? data.pageHeight : "55";
    		
    		$.each(this._items, function(h, obj) {
    			if (obj["classname"] == "Page") {
    				obj.tapStyle = "width:"+ obj.tapWidth+"px;height:"+ pageHeight + "px";
    				$("#tabNav div[data_id='" + h + "']").attr("style",obj.tapStyle);
    			}
    		});
        }
        if(attrName == "pageHeight"){
            if (!el || !attrName) return;
            data = this._data;
    		var val = this._getAttrVal(el);
            data[attrName] = val;
    		
    		$.each(this._items, function(h, obj) {
                if (obj["classname"] == "Page") {
    				obj.tapHeight = val;
    				obj.tapStyle = "width:"+ obj.tapWidth+"px;height:"+ val + "px";
                }
            });
    		
    		var items = this._items;
    		
    		$("#tabNav > div").each(function(){
    		  var instanceId = $(this).attr("data_id");
    		  $("#tabNav div[data_id='" + instanceId + "']").html(items[instanceId].title);
    		  $("#tabNav div[data_id='" + instanceId + "']").attr("style",items[instanceId].tapStyle);
    		});
        }

    },
    /* 暂时注释掉 By WRL @20160411
    saveTabAttr: function(el, attrName) {
		
        if (!el || !attrName) return;
        var instanceId = $(el).attr("data_id");
        var val = this._getAttrVal(el);
	
        if(attrName == "itemName"){
            //$("#tabNav div[data_id='" + instanceId + "']").html(val);
            $("#question-box li[data_id='" + instanceId + "'] .pagename").html(val);
        }
        this._items[instanceId][attrName] = val;
        this.is_edit = true;
    },
	saveTapNumAttr: function(el, attrName) {

		var data = this._data;
		
		var pageHeight = data.hasOwnProperty("pageHeight") ? data.pageHeight : "55";
		
        if (!el || !attrName) return;
        var instanceId = $(el).attr("data_id");
		
		var tapWidth = this._items[instanceId][attrName];
		
        var val = this._getAttrVal(el);
		
		this._items[instanceId][attrName] = val;
		this.is_edit = true;
		
		$.each(this._items, function(h, obj) {
			if (obj["classname"] == "Page") {
				obj.tapStyle = "width:"+ obj.tapWidth+"px;height:"+ pageHeight + "px";
			}
		});
    },

	saveTapHeight: function(el, attrName) {
        if (!el || !attrName) return;
        data = this._data;
		var tapHeight;
		tapHeight = this._getAttrVal(el);
        data[attrName] = tapHeight;

        this.is_edit = true;
		
		$.each(this._items, function(h, obj) {
            if (obj["classname"] == "Page") {
				obj.tapHeight = tapHeight;
				obj.tapStyle = "width:"+ obj.tapWidth+"px;height:"+ tapHeight + "px";
            }
        });
		
		var items = this._items;
		
		$("#tabNav > div").each(function(){
		  var instanceId = $(this).attr("data_id");
		  $("#tabNav div[data_id='" + instanceId + "']").html(items[instanceId].title);
		  //$("#tabNav div[data_id='" + instanceId + "']").attr("style",items[instanceId].tapStyle);
		  
		});
    },
    */
    saveTjx_fj: function() {
		var _max_tjx_sm=$("#max_tjx_ts").val();
		for (var i=1;i<=_max_tjx_sm;i++)
		{
			$("#saveRec_"+(i-1)).click();
		}
    },
    _getAttrVal: function(el) {
        var val = "";
        if (el.tagName == "INPUT") {
            val = $(el).val();
            if ($(el).attr("type") == "radio" || $(el).attr("type") == "checkbox") {
                val = $(el).prop("checked") ? "Y": "N";
            }
        } else if (el.tagName == "TEXTAREA") {
            val = $(el).val();
        } else if (el.tagName == "SELECT") {
            val = $(el).val();
        }
        val = $.trim(val);
        return val;
    },
    _clearAttrVal: function(el) {
        if (el.tagName == "INPUT") {
            if ($(el).attr("type") == "radio" || $(el).attr("type") == "checkbox") {
                $(el).attr("checked", false);
            } else {
                $(el).val("");
            }
        } else if (el.tagName == "TEXTAREA") {
            $(el).html("");
        } else if (el.tagName == "SELECT") {
            $(el).val("");
        }
    },
    /*level0级属性赋值*/
    saveAttr: function(el, attrName) {
        if (!el || !attrName) return;

        var instanceId = $("#question-edit").attr("data_id");
        var data = {};
        if (SurveyBuild.isDHContainer) {
            data = this._items[SurveyBuild.currentDHID]["children"][instanceId];
        } else {
            data = this._items[instanceId];
        }

        var val = this._getAttrVal(el);
        if (attrName == "fileType") {
            //上传附件中的文件类型，去掉所有空额
            val = val.replace(/[ ]/g, "")
        }
        data[attrName] = val;

        var rules = data["rules"];
        var _rules = this._componentConfig[$.inArray(data["classname"], this._componentIndex)]["rules"];

        var $activeLi = $("#question-box li.active");
        if (attrName == "title") {
            /*Bug修改TextExplain的Title属性，在设计区域会重复显示Title内容   暂时注释by WRL 2016/2/24
             if (data["classname"] == 'TextExplain') {
             $activeLi.find(".question-answer").html(val);
             } else {
             $activeLi.find(".question-question").first().html(val);
             }*/
            $activeLi.find(".question-question").first().html(val);
            data["classname"] == "Page" && this._initTab();
        } else if (attrName == "suffix") {
            $activeLi.find(".suffix").html(val);
        } else if (attrName == "format") {
            $activeLi.find(".format").removeClass().addClass("format format" + val)
        } else if (attrName == "dateformate") {
            var format = "";
            if (val == "mm-dd-yy") {
                format = "MM-DD-YYYY";
            } else if (val == "mm/dd/yy") {
                format = "MM/DD/YYYY";
            } else if (val == "yy-mm-dd") {
                format = "YYYY-MM-DD";
            } else if (val == "yy/mm/dd") {
                format = "YYYY/MM/DD";
            } else if (val == "dd-mm-yy") {
                format = "DD-MM-YYYY";
            } else if (val == "dd/mm/yy") {
                format = "DD/MM/YYYY";
            } else if (val == "yy/mm") {
                format = "YYYY/MM";
            } else if (val == "yy-mm") {
                format = "YYYY-MM";
            }
            $activeLi.find(".question-answer").find("#" + instanceId + "Format").html(format);
        } else if (attrName == "itemMs") {
            if (val) {
                $activeLi.find(".edu_exper_desc").css("display", "");
            } else {
                $activeLi.find(".edu_exper_desc").css("display", "none");
            }
            $activeLi.find(".edu_exper_desc").html(val);
        } else if (attrName == "isRequire") {
            var RequireValidatorObj = _rules["RequireValidator"];
            if (!rules["RequireValidator"] && RequireValidatorObj) {
                rules["RequireValidator"] = RequireValidatorObj;
            }
            if (rules["RequireValidator"]) {
                if (val == "Y") {
                    rules["RequireValidator"]["isEnable"] = "Y";
                } else {
                    rules["RequireValidator"]["isEnable"] = "N"
                }
                $("#is_require").prop("checked", $(el).prop("checked"));
            }
        } else {
            if (attrName == "isCheckStrLen") {
                var CharLenValidatorObj = _rules["CharLenValidator"]
                if (!rules["CharLenValidator"] && CharLenValidatorObj) {
                    rules["CharLenValidator"] = CharLenValidatorObj;
                }
                if (rules["CharLenValidator"]) {
                    if (val == "Y") {
                        rules["CharLenValidator"]["isEnable"] = "Y"
                    } else {
                        rules["CharLenValidator"]["isEnable"] = "N"
                    }
                    $("#is_checkstrlen").prop("checked", $(el).prop("checked"));
                }
            } else {
                if (attrName == "isNumSize") {
                    var NumSizeValidatorObj = _rules["NumSizeValidator"]
                    if (!rules["NumSizeValidator"] && NumSizeValidatorObj) {
                        rules["NumSizeValidator"] = NumSizeValidatorObj;
                    }
                    if (rules["NumSizeValidator"]) {
                        if (val == "Y") {
                            rules["NumSizeValidator"]["isEnable"] = "Y"
                        } else {
                            rules["NumSizeValidator"]["isEnable"] = "N"
                        }
                        $("#is_checkNumSize").prop("checked", $(el).prop("checked"));
                    }
                } else {
                    //正则校验
                    if (attrName == "preg") {
                        var RegularValidatorObj = _rules["RegularValidator"]
                        if (!rules["RegularValidator"] && RegularValidatorObj) {
                            rules["RegularValidator"] = RegularValidatorObj;
                        }
                        if (rules["RegularValidator"]) {
                            if (val == "N" || val == "") {
                                rules["RegularValidator"]["isEnable"] = "N"
                            } else {
                                rules["RegularValidator"]["isEnable"] = "Y"
                            }
                        }
                    } else {
                        if (attrName == "toCheckTjx") {
                            var RefSizeValidatorObj = _rules["RefLetterValidator"]
                            if (!rules["RefLetterValidator"] && RefSizeValidatorObj) {
                                rules["RefLetterValidator"] = RefSizeValidatorObj;
                            }
                            if (rules["RefLetterValidator"]) {
                                if (val == "Y") {
                                    rules["RefLetterValidator"]["isEnable"] = "Y"
                                } else {
                                    rules["RefLetterValidator"]["isEnable"] = "N"
                                }
                                //$("#is_toCheck").prop("checked", $(el).prop("checked"));
                            }
                        } else {
                            if (attrName == "maxLines") {
                                //设置最多行数时，初始化linesNo属性
                                var linesNo = [];
                                for (var i = 1; i < val; i++) {
                                    linesNo.push(i);
                                }
                                data["linesNo"] = linesNo;
                            } else {
                                if (attrName == "defaultval") {
                                    //修改默认值时，直接将默认值赋值于value
                                    if (val.indexOf("%BIND") == -1) {
                                        data["value"] = val;
                                    } else {
                                        data["value"] = "";
                                    }
                                } else {
                                    if (attrName == "isAttachedTemplate") {
                                        if (val == "Y") {
                                            $("#mainTemplate").show();
                                            $("#mainTemplateSpan").show();
                                        } else {
                                            $("#mainTemplate").hide();
                                            $("#mainTemplateSpan").hide();
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        this.is_edit = true;
    },
    saveLevel1Attr: function(el, attrNameLevel1) {
        if (!el || !attrNameLevel1) return;
        var val = this._getAttrVal(el),data = "";
        var $tr = $(el).closest("tr"),
        ids = $tr.attr("data-id").split("-"),
        instanceId = ids[0];
        optionId = ids[1];
        if (SurveyBuild.isDHContainer) {
            data = this._items[SurveyBuild.currentDHID]["children"][instanceId];
        } else {
            data = this._items[instanceId];
        }

        try {
            if (attrNameLevel1 == "defaultval") {
                for (var i in data["option"]) {
                    data["option"][i][attrNameLevel1] = 'N';
                }
            }
			
			if (attrNameLevel1 == "other") {
                for (var i in data["option"]) {
                    data["option"][i][attrNameLevel1] = 'N';
                    $("#o" + i).find("b").remove();
                }
                if(val == "Y"){
                    $("#o" + optionId).append('<b class="read-input"></b>')
                }
            }

            data["option"][ids[1]][attrNameLevel1] = val;
            if (attrNameLevel1 == "defaultval") {
                data.value = data["option"][ids[1]]["code"];
            }
            if (attrNameLevel1 == "txt") {
                if (false) val += '<b class="read-input"></b>';
                $("#o" + ids[1]).html(val)
            }
        } catch(e) {};
        this.is_edit = true;
    },
	saveLevel1Attr1: function(el, attrNameLevel1,recommend) {
        if (!el || !attrNameLevel1) return;
        var val = this._getAttrVal(el),data = "";
        var $tr = $(el).closest("tr"),
        ids = $tr.attr("data-id").split("-"),
        instanceId = ids[0];
        optionId = ids[1];
        if (SurveyBuild.isDHContainer) {
            data = this._items[SurveyBuild.currentDHID]["children"][instanceId];
        } else {
            data = this._items[instanceId];
        }
        try {
            data["children"][recommend][attrNameLevel1] = val;
			if (attrNameLevel1=="itemName")
			{
				$("#" + $tr.attr("data-id")).html(val);
			}
        } catch(e) {};
        this.is_edit = true;
    },
    saveLevel1Attr2: function(el, attrNameLevel1, recommend) {
        if (!el || !attrNameLevel1) return;
        var val = this._getAttrVal(el),data = "";
        var $tr = $(el).closest("tr"),
            ids = $tr.attr("data-id").split("-"),
            instanceId = ids[0];
        optionId = ids[1];
        if (SurveyBuild.isDHContainer) {
            data = this._items[SurveyBuild.currentDHID]["children"][instanceId];
        } else {
            data = this._items[instanceId];
        }
        try {
            data["children"][recommend][attrNameLevel1] = val;
            if (attrNameLevel1 == "itemName") {
                $("#" + $tr.attr("data-id")).html(val);
            }
            if (attrNameLevel1 == "useby") {
                if (val == "Y") {
                    $("#" + $tr.attr("data-id") + "_1").css("display", "block");
                } else {
                    $("#" + $tr.attr("data-id") + "_1").css("display", "none");
                }

            }
        } catch(e) {};
        this.is_edit = true;
    },
    checkAttrVal: function() {
        var $liActive = $("#question-box li.active[data_id]");
        var checkBz = true;

        if ($liActive.length == 0) {
            return checkBz
        }
        var tipObj; //提示信息目标元素
        var d = $liActive.attr("data_id");
        //if (!SurveyBuild.isDHContainer && this._items[d] && (this._items[d]["classname"] == "Page" || this._items[d]["classname"] == "Separator")) {
        //    //分页符、文字描述控件跳过检查
        //    return checkBz;
        //}

        var $itemId = $("#question-edit").find(".edit_itemId");
        var $itemName = $("#question-edit").find(".edit_itemName");
        var has = false,data;

        if (SurveyBuild.isDHContainer) {
            data = this._items[SurveyBuild.currentDHID]["children"];
        } else {
            data = this._items;
        }

        if (!$itemId.val()) {
            /*信息项编号必填*/
            checkBz = false;
            tipObj = $itemId;
            msg = "信息项编号不能为空";
        } else if (!/^\w+$/g.test($itemId.val())) {
            /*信息项编号只允许字母、数字、下划线*/
            checkBz = false;
            tipObj = $itemId;
            msg = "信息项编号只允许字母数字以及下划线";
        } else if ($itemName.length > 0 && !$itemName.val().length > 30) {
            /*信息项编号长度限制*/
            checkBz = false;
            tipObj = $itemId;
            msg = "信息项编号长度不能超过30";
        } else if ($itemName.length > 0 && !$itemName.val()) {
            /*信息项名称必填*/
            checkBz = false;
            tipObj = $itemName;
            msg = "信息项名称不能为空";
        } else {
            //信息项编号是否重复
            dance: for (var insId in this._items) {
                if (d != insId) {
                    if (this._items[insId]["itemId"] == $itemId.val()) {
                        has = true;
                        break;
//                    } else if (!SurveyBuild.isDHContainer && this._items[insId]["isDoubleLine"] == "Y") {
                    } else if (this._items[insId]["isDoubleLine"] == "Y") {
                        //普通信息项与容器中信息项编号也不能重复
                        for (var insDHId in this._items[insId]["children"]) {
                            if (d != insDHId && this._items[insId]["children"][insDHId]["itemId"] == $itemId.val()) {
                                has = true;
                                break;
                            }
                        }
                    }
//                    break dance;
                }
            }
            if (has) {
                /*信息项编号重复*/
                checkBz = false;
                tipObj = $itemId;
                msg = "信息项编号已存在";
            } else {
                //可选值重复性检查
                if (data.hasOwnProperty(d) && data[d].option) {
                    //Option可选值不能重复
                    var hash = {},trId = "";
                    for (var i in data[d].option) {
                        if (hash[data[d].option[i]["code"]]) {
                            has = true;
                            trId = data[d]["instanceId"] + "-" + i;
                        }
                        hash[data[d].option[i]["code"]] = true;
                    }
                    if (has) {
                        checkBz = false;
                        tipObj = $("#question-edit tr[data-id='" + trId + "']");
                        msg = "可选值名称重复";
                    }
                }
            }
        }

        $(".popover").hide();
        if (checkBz == false) {
            $("#href2").click();
            this.fail(tipObj, msg);
            return checkBz;
        };
        /*控件属性验证*/
        if (data.hasOwnProperty(d) && data[d]._validatorAttr) {
            return data[d]._validatorAttr(data[d]);
        } else {
            return checkBz;
        }
    },
    openMoadal: function(content, CallBack) {
        $.fancybox.open({
            content: content,
            helpers: {
                overlay: {
                    closeClick: false
                }
            },
            beforeShow: function() {
                $(".popo").popover({
                    trigger: "hover"
                })
            }
        });
	if (CallBack && typeof CallBack == "function") {
            CallBack()
        };
    },
    /*控件属性--高级设置--Begin*/
    RulesSet: function(el) {
        /*控件实例ID*/
        var instanceId = $("#question-edit").attr("data_id");
        var _d;
        if (SurveyBuild.isDHContainer) {
            _d = this._items[SurveyBuild.currentDHID]["children"][instanceId];
        } else {
            _d = this._items[instanceId];
        }
        /*控件最新规则列表*/
        var _rules = this._componentConfig[$.inArray(_d["classname"], this._componentIndex)]["rules"];

        /*当前信息项规则列表*/
        var rules = _d["rules"];

        var pageHtml = "";
        pageHtml += '<div class="modal-header">';
        pageHtml += '	<h4>规则高级设置</h4>';
        pageHtml += '</div>';
        pageHtml += '<div class="modal-line"></div>';

        pageHtml += '<div id="modal-question-advanced" class="modal-body">';
        pageHtml += '	<fieldset>信息项名称：<span style="font-weight:bold">' + _d.itemName + '</span></fieldset>';
        pageHtml += '	<fieldset>';
        pageHtml += '		<table id="table-advanced-relus" class="table table-hover table-bordered">';
        pageHtml += '			<tr><th style="text-align:center" width="50px">启用</th><th width="100px">规则名称</th><th>提示信息</th></tr>';

        //table方式展示规则列表
        $.each(_rules, function(ruleClsName, _ruleObj) {
            //校验规则是否启用，后台配置信息中是否启用，当前实例对象中是否启用了该校验规则
            var obj = rules[ruleClsName] || _ruleObj;
            var checked = obj["isEnable"] == "Y" ? "checked='checked'": "";
            var disabled = obj["isEnable"] != "Y" ? "disabled='disabled'" : "";

            //校验规则启用属性的设置
            var onchange = "";
            if (ruleClsName == "RequireValidator") {
                onchange = "SurveyBuild.saveCommonRulesBz(this,\'isRequire\')";
            } else if (ruleClsName == "CharLenValidator") {
                onchange = "SurveyBuild.saveCommonRulesBz(this,\'isCheckStrLen\')";
            } else if (ruleClsName == "NumSizeValidator") {
                onchange = "SurveyBuild.saveCommonRulesBz(this,\'isNumSize\')";
            } else if (ruleClsName == "RegularValidator") {
                onchange = "SurveyBuild.saveCommonRulesBz(this,\'preg\')";
            } else if (ruleClsName == "RefLetterValidator") {
                onchange = "SurveyBuild.saveCommonRulesBz(this,\'toCheckTjx')";
            } else {
                onchange = "SurveyBuild.saveRulesBz(this,\'" + ruleClsName + "\')";
            }

            var inputHTML = "";
            if (obj.ruleId == 'TZ_JYGZ_ASSOCIATED_LOGIC') {
                inputHTML = '<textarea style="width:98%;height:100px;" onkeyup="SurveyBuild.saveRuleMsg(this)" class="adv_first_msg" ' + disabled + '>' + obj["messages"] + '</textarea>';
            } else {
                inputHTML = '<input type="text" value="' + obj["messages"] + '" style="width:98%;" onkeyup="SurveyBuild.saveRuleMsg(this)" class="adv_first_msg" ' + disabled + '>';
            }

            pageHtml += '<tr rule-id="' + obj.ruleId + '" data-classname="' + ruleClsName + '">';
            pageHtml += '	<td style="text-align:center">';
            pageHtml += '			<input type="checkbox" ' + (checked) + ' onchange="' + onchange + '">';
            pageHtml += '	</td>';

            pageHtml += '	<td>' + obj.ruleName + '</td>';
            pageHtml += '	<td class="inputleft">' + inputHTML + '</td>';
            pageHtml += '</tr>'
        });

        pageHtml += '		</table>';
        pageHtml += '   </fieldset>';

        //信息项同步配置
        if ($.isEmptyObject(_d["syncRule"])) {
            var n = "S" + ( + new Date()),g = {};
            g[n] = {
                "isEff": "N",
                "syncType": "",
                "syncOrder": "",
                "syncSep": ""
            }
            _d["syncRule"] = g;
        }
        pageHtml += '<div class="edit_item_warp">';
        pageHtml += '	<fieldset><span style="font-weight: bold">信息项同步配置</span></fieldset>';
        pageHtml += '</div>';

        pageHtml += '<fieldset>';
        pageHtml += '	<table id="table-advanced-relus" class="table table-hover table-bordered">';
        pageHtml += '		<tbody>';
        pageHtml += '			<tr>';
        pageHtml += '				<th width="50px" style="text-align: center">启用</th>';
        pageHtml += '				<th>同步类型</th>';
        pageHtml += '				<th>合并顺序</th>';
        pageHtml += '				<th>合并分隔符</th>';
        pageHtml += '				<th>&nbsp;</th>';
        pageHtml += '			</tr>';


        $.each(_d["syncRule"],function(h, obj) {
            pageHtml += '			<tr data_id="' + h + '">';
            pageHtml += '				<td style="text-align: center">';
            pageHtml += '					<label class="checkbox inline"><input type="checkbox" id="eff_' + h + '" onchange="SurveyBuild.saveSyncAttr(this,\'isEff\')" ' + (obj.isEff == "Y" ? " checked=\'true\'": "") + '></label>';
            pageHtml += '				</td>';
            pageHtml += '				<td>';
            pageHtml += '					<select id="type_' + h + '" onchange="SurveyBuild.saveSyncAttr(this,\'syncType\')" class="ml8">';
            pageHtml += '                       <option>--请选择--</option>';
            pageHtml += '                       <option value="ZYSJ" ' + (obj.syncType == "ZYSJ" ? "selected='selected'": "") + '>主要手机</option>';
            pageHtml += '                       <option value="BYSJ" ' + (obj.syncType == "BYSJ" ? "selected='selected'": "") + '>备用手机</option>';
            pageHtml += '                       <option value="ZYDH" ' + (obj.syncType == "ZYDH" ? "selected='selected'": "") + '>主要电话</option>';
            pageHtml += '                       <option value="BYDH" ' + (obj.syncType == "BYDH" ? "selected='selected'": "") + '>备用电话</option>';
            pageHtml += '                       <option value="ZYYX" ' + (obj.syncType == "ZYYX" ? "selected='selected'": "") + '>主要邮箱</option>';
            pageHtml += '                       <option value="BYYX" ' + (obj.syncType == "BYYX" ? "selected='selected'": "") + '>备用邮箱</option>';
            pageHtml += '                       <option value="ZYDZ" ' + (obj.syncType == "ZYDZ" ? "selected='selected'": "") + '>主要通讯地址</option>';
            pageHtml += '                       <option value="ZYYB" ' + (obj.syncType == "ZYYB" ? "selected='selected'": "") + '>主要通讯地址邮编</option>';
            pageHtml += '                       <option value="BYDZ" ' + (obj.syncType == "BYDZ" ? "selected='selected'": "") + '>备用通讯地址</option>';
            pageHtml += '                       <option value="BYYB" ' + (obj.syncType == "BYYB" ? "selected='selected'": "") + '>备用通讯地址邮编</option>';
            pageHtml += '                       <option value="WX" ' + (obj.syncType == "WX" ? "selected='selected'": "") + '>微信号	</option>';
            pageHtml += '                       <option value="SKY" ' + (obj.syncType == "SKY" ? "selected='selected'": "") + '>Sky账号</option>';
            pageHtml += '                       <option value="IDCARD" ' + (obj.syncType == "IDCARD" ? "selected='selected'": "") + '>证件号码</option>';
            pageHtml += '					</select>';
            pageHtml += '				</td>';
            pageHtml += '				<td>';
            pageHtml += '					<input type="text" class="option-txt syncOrder" id="order_' + h + '" value="' + obj.syncOrder + '" onkeyup="SurveyBuild.saveSyncAttr(this,\'syncOrder\')">';
            pageHtml += '				</td>';
            pageHtml += '				<td>';
            pageHtml += '					<input type="text" class="option-txt" id="sep_' + h + '" value="' + obj.syncSep + '" onkeyup="SurveyBuild.saveSyncAttr(this,\'syncSep\')">';
            pageHtml += '				</td>';
            pageHtml += '				<td>';
            pageHtml += '					<a href="javascript:void(0);" class="text-success" onclick="SurveyBuild.plusSync(this);return false;"><i class="icon-plus-sign"></i> </a>&nbsp;';
            pageHtml += '					<a href="javascript:void(0);" class="text-warning" onclick="SurveyBuild.minusSync(this);return false;"><i class="icon-minus-sign"></i> </a>';
            pageHtml += '				</td>';
            pageHtml += '			</tr>';
        });
        pageHtml += '		</tbody>';
        pageHtml += '	</table>';
        pageHtml += '</fieldset>';
        pageHtml += '</div>';

        this.openMoadal(pageHtml);
        $("#modal-question-advanced").on("keypress", ".syncOrder",
            function(c) {
                var b = c.which;
                return b == 8 || b == 127 || b == 0 || b >= 48 && b <= 57
            });
    },
    //新增同步配置
    plusSync: function(f) {
        this.is_edit = true;
        var m = $(f).parents("tr").attr("data_id"),
            l = 0,
            n = "S" + ( + new Date()),
            g = {},
            h = 0,
            syncRow = "";

        var data = "{}";
        var insId = $("#question-edit").attr("data_id");
        if(this._items[insId]){
            data = this._items[insId];
        }else{
            $.each(this._items,function(ins, obj) {
                if(obj.hasOwnProperty("children")) {
                    if(obj["children"][insId]){
                        data = obj["children"][insId];
                        return false;
                    }
                }
            });
        }

        var a = ++l;
        for (var e in data.syncRule) {
            h++;
            g[e] = cloneObj(data.syncRule[e]);
            if (e == m) {++h;
                g[n] = {
                    "isEff": "N",
                    "syncType": "",
                    "syncOrder": "",
                    "syncSep": ""
                }
            }
        }

        data.syncRule = g;

        syncRow += '<tr data_id="' + n + '">';
        syncRow += '	<td style="text-align: center">';
        syncRow += '    	<label class="checkbox inline"><input type="checkbox" id="eff_' + n + '" onchange="SurveyBuild.saveSyncAttr(this,\'isEff\')" ' + (data.syncRule[n].isEff == "Y" ? " checked=\'true\'": "") + '></label>';
        syncRow += '	</td>';
        syncRow += '	<td>';
        syncRow += '		<select id="type_' + n + '" onchange="SurveyBuild.saveSyncAttr(this,\'syncType\')" class="ml8">';
        syncRow += '            <option>--请选择--</option>';
        syncRow += '          <option value="ZYSJ" ' + (data.syncRule[n].syncType == "ZYSJ" ? "selected='selected'": "") + '>主要手机</option>';
        syncRow += '          <option value="BYSJ" ' + (data.syncRule[n].syncType == "BYSJ" ? "selected='selected'": "") + '>备用手机</option>';
        syncRow += '          <option value="ZYDH" ' + (data.syncRule[n].syncType == "ZYDH" ? "selected='selected'": "") + '>主要电话</option>';
        syncRow += '          <option value="BYDH" ' + (data.syncRule[n].syncType == "BYDH" ? "selected='selected'": "") + '>备用电话</option>';
        syncRow += '          <option value="ZYYX" ' + (data.syncRule[n].syncType == "ZYYX" ? "selected='selected'": "") + '>主要邮箱</option>';
        syncRow += '          <option value="BYYX" ' + (data.syncRule[n].syncType == "BYYX" ? "selected='selected'": "") + '>备用邮箱</option>';
        syncRow += '          <option value="ZYDZ" ' + (data.syncRule[n].syncType == "ZYDZ" ? "selected='selected'": "") + '>主要通讯地址</option>';
        syncRow += '          <option value="ZYYB" ' + (data.syncRule[n].syncType == "ZYYB" ? "selected='selected'": "") + '>主要通讯地址邮编</option>';
        syncRow += '          <option value="BYDZ" ' + (data.syncRule[n].syncType == "BYDZ" ? "selected='selected'": "") + '>备用通讯地址</option>';
        syncRow += '          <option value="BYYB" ' + (data.syncRule[n].syncType == "BYYB" ? "selected='selected'": "") + '>备用通讯地址邮编</option>';
        syncRow += '          <option value="WX" ' + (data.syncRule[n].syncType == "WX" ? "selected='selected'": "") + '>微信号	</option>';
        syncRow += '          <option value="SKY" ' + (data.syncRule[n].syncType == "SKY" ? "selected='selected'": "") + '>Sky账号</option>';
        syncRow += '		</select>';
        syncRow += '    </td>';
        syncRow += '	 <td>';
        syncRow += '		<input type="text" class="option-txt syncOrder" id="order_' + n + '" value="' + data.syncRule[n].syncOrder + '" onkeyup="SurveyBuild.saveSyncAttr(this,\'syncOrder\')">';
        syncRow += '	</td>';
        syncRow += '	<td>';
        syncRow += '		<input type="text" class="option-txt" id="sep_' + n + '" value="' + data.syncRule[n].syncSep + '" onkeyup="SurveyBuild.saveSyncAttr(this,\'syncSep\')">';
        syncRow += '	</td>';
        syncRow += '	<td>';
        syncRow += '		<a href="javascript:void(0);" class="text-success" onclick="SurveyBuild.plusSync(this);return false;"><i class="icon-plus-sign"></i> </a>&nbsp;';
        syncRow += '		<a href="javascript:void(0);" class="text-warning" onclick="SurveyBuild.minusSync(this);return false;"><i class="icon-minus-sign"></i> </a>';
        syncRow += '	</td>';
        syncRow += '</tr>';
        var d = $("#build-right").height();
        $(f).parents("tr").after(syncRow);
    },
    /*删除同步配置*/
    minusSync: function(e) {
        this.is_edit = true;
        if ($(e).parents("tr").siblings().length == 0) {
            this._error("至少要有一个选项")
        } else {
            var data = "{}";
            var insId = $("#question-edit").attr("data_id");
            if(this._items[insId]){
                data = this._items[insId];
            }else{
                $.each(this._items,function(ins, obj) {
                    if(obj.hasOwnProperty("children")) {
                        if(obj["children"][insId]){
                            data = obj["children"][insId];
                            return false;
                        }
                    }
                });
            }
            var c = $(e).parents("tr").attr("data_id");
            if (data.syncRule && data.syncRule.hasOwnProperty(c)) {
                var b = $("#build-right").height();
                $(e).parents("tr").remove();
                delete data.syncRule[c]
            } else {
                this._error("非法操作")
            }
        }
    },
    //设置信息项同步规则属性
    saveSyncAttr: function(el, syncAttr) {
        if (!el || !syncAttr) return;

        var instanceId = $("#question-edit").attr("data_id");
        var syncRule = "{}";
        if(this._items[instanceId]){
            syncRule = this._items[instanceId]["syncRule"];
        }else{
            $.each(this._items,function(ins, obj) {
                if(obj.hasOwnProperty("children")) {
                    if(obj["children"][instanceId]){
                        syncRule = obj["children"][instanceId]["syncRule"];
                        return false;
                    }
                }
            });
        }
        var val = this._getAttrVal(el);
        var syncObj = $(el).closest("tr").attr("data_id");
        syncRule[syncObj][syncAttr] = val;
    },

    DynamicBindValCallBack: function(td) {
        var regId = $(td).closest('tr').attr('reg-id');
        var defVal = "{%BIND:" + regId + "}";

        $("#question-edit #defaultval").val(defVal);
        $("#question-edit #defaultval").trigger("keyup");

        $.fancybox.close();
    },
    DynamicBindVal: function(){
         var callBack = function(){

	};
	if(!SurveyBuild._DynamicBindHtml){
		 $.ajax({
			type: "post",
			url: SurveyBuild.tzGeneralURL+'?tzParams={"ComID":"TZ_USER_REG_COM","PageID":"TZ_REGGL_STD","OperateType":"QG","comParams":{}}',
			dataType: "json",
			async: false,
			success: function(result){
				var _items = result.comContent.root;
				 if(result.state.errcode != "0"){
				   alert(result.state.errdesc);
				}else{
				 var getRegFieldTypeDesc =function(regFieldType){
					if(regFieldType ="INP"){
						return "文本框";
					}else if(regFieldType ="DROP"){
						return "下拉框";
					}else{
						return  	regFieldType;
					}
				}
				 var pageHtml = "";
       				 pageHtml += '<div class="modal-header">';
       				 pageHtml += '	<h4>动态绑定值</h4>';
     				 pageHtml += '</div>';
				 pageHtml += '<div class="modal-line"></div>';

       				 pageHtml += '<div id="modal-question-advanced" class="modal-body">';
      				 pageHtml += '	<fieldset>';
       				 pageHtml += '		<table id="table-dynbindval" class="table table-hover table-bordered">';
       				 pageHtml += '			<tr><th width="50px">注册项ID</th><th width="100px">名称</th><th>是否启用</th><th>注册项类型</th><th>是否必填</th><th>绑定</th></tr>';
				 $.each(_items, function(index, item) {
					pageHtml += '<tr reg-id="' + item.regId+ '">';
					pageHtml += '<td>'+item.regId+'</td>' ;
					pageHtml += '<td>'+item.regName+'</td>' ;
					pageHtml += '<td><input type="checkbox" disabled="disabled" '+(item.isEnable?'checked="checked"':"")+'</td>' ;
					pageHtml += '<td>'+getRegFieldTypeDesc (item.regFieldType)+'</td>' ;
					pageHtml += '<td><input type="checkbox" disabled="disabled" '+(item.isRequired?'checked="checked"':"")+'</td>' ;
					pageHtml += '<td><button class="btn btn-small" onclick="SurveyBuild.DynamicBindValCallBack(this)">绑定</button></td>'; 												
					pageHtml  += '</tr> ';
				 });
				pageHtml += '		</table>';
    				pageHtml += '	<fieldset>';
      				pageHtml += '</div>';	
				SurveyBuild._DynamicBindHtml	=pageHtml ;
				}				
			}
		});	
	}
	this.openMoadal(SurveyBuild._DynamicBindHtml,callBack);

       },  
    //设置常用控件的启用标识
    saveCommonRulesBz: function(el, key) {
        this.saveAttr(el, key);
        if (key == "preg") {
            if ($(el).prop("checked")) {
                $("#is_preg").val($("#is_preg").find("option").eq(1).val());
            } else {
                $("#is_preg").val("");
            }
        }
        this._setRuleStatu(el);
    },
    //设置rules启用标识
    saveRulesBz: function(el, ruleClassName) {
        if (!el || !ruleClassName) return;

        var instanceId = $("#question-edit").attr("data_id");
        var isEnable = this._getAttrVal(el);

        var rules = "";
        if(this._items[instanceId]){
            rules = this._items[instanceId]["rules"];
        }else{
            $.each(this._items,function(ins, obj) {
                if(obj.hasOwnProperty("children")) {
                    if(obj["children"][instanceId]){
                        rules = obj["children"][instanceId]["rules"];
                        return false;
                    }
                }
            });
        }

        var currentRule = rules[ruleClassName];
        if (currentRule) {
            if (isEnable == "Y") {
                currentRule["isEnable"] = "Y"
            } else {
                currentRule["isEnable"] = "N"
            }
            this._setRuleStatu(el);
        }
    },
    //设置当前行的状态(提示信息是否可编辑)
    _setRuleStatu: function(el) {
        var $tr = $(el).closest("tr");
        var val = this._getAttrVal(el);

        if (val == "Y") {
            $tr.find(".adv_first_msg").attr("disabled", null);
        } else {
            $tr.find(".adv_first_msg").attr("disabled", "disabled");
        }
    },
    //设置rule的提示信息
    saveRuleMsg: function(el) {
        var instanceId = $("#question-edit").attr("data_id");
        var rules;
        if(this._items[instanceId]){
            rules = this._items[instanceId]["rules"];
        }else{
            $.each(this._items,function(ins, obj) {
                if(obj.hasOwnProperty("children")) {
                    if(obj["children"][instanceId]){
                        rules = obj["children"][instanceId]["rules"];
                        return false;
                    }
                }
            });
        }

        var val = this._getAttrVal(el);
        var className = $(el).closest("tr").attr("data-classname");
        rules[className]["messages"] = val;
    },
    /*控件属性--高级属性--End*/


	//推荐信控件选择推荐信模板和推荐信发送邮件模板(中文)
	RulesZHS: function(el) {
		/*var _url=document.location.href;
		var _url=_url.split("?");
		var _url1=_url[1].split("=");
		var tz_app_id=_url1[1];*/
		var tz_app_id=SurveyBuild._tid;
		var _url=SurveyBuild.tzGeneralURL;
		var _bmb_xz="";
		var _email_desc="";
		var _tjx_qy="";
		var ruleSetPage = "";
		$.ajax({
			type: "post",
			url: _url+'?tzParams={"ComID":"TZ_GD_TJX_COM","PageID":"TZ_GD_TJX_PZ_STD","OperateType":"QF","comParams":{"tz_app_id":"'+tz_app_id+'","tz_language":"C"}}',
			dataType: "json",
			async: false,
			success: function(result){
				_bmb_xz=result.comContent.formData.tjx_xz;
				_email_desc=result.comContent.formData.email_desc;
				_tjx_qy=result.comContent.formData.tjx_qy;
			}
		});
		ruleSetPage = '<div class="modal-header"><h4>中文推荐信设置</h4></div>';
		ruleSetPage += '<div class="modal-line" style="width:330px;"></div>';
		//是否启用中文推荐信
		ruleSetPage += '<div class="edit_item_warp" style="margin-top:15px;margin-left:20px;">';
        ruleSetPage += '<input type="checkbox" ' + (_tjx_qy == "Y" ? "checked='checked'" : "") + ' id="zhs_qy" onclick="SurveyBuild.tjx_zhschange()"/> <label for="zhs_qy" style="display:inline;">启用中文推荐信</label>';
        ruleSetPage += '</div>';
		//选择推荐信报名表
		ruleSetPage += "<div style='height:40px;margin-top:25px;'><div style='width: 120px; margin-left: 20px;'>推荐信模板</div>";
		ruleSetPage += "<div style='width: 250px; margin-left: 120px; margin-top: -25px;'><select style='width:220px' id='tjx_mb_id' class='selectCss' " + (_tjx_qy != 'Y' ? 'disabled="disabled"' : '') + ">";
		ruleSetPage += _bmb_xz;
		ruleSetPage += "</select></div></div>";
		//选择发送邮件模板
		ruleSetPage += "<div style='height:40px;margin-top:25px;'><div style='width: 120px; margin-left: 20px;'>推荐信邮件模板</div>";
		ruleSetPage += "<div style='width: 250px; margin-left: 120px; margin-top: -25px;'><select style='width:220px' id='tjx_eamil_id' class='selectCss' " + (_tjx_qy != 'Y' ? 'disabled="disabled"' : '') + ">";
		ruleSetPage += _email_desc;
		ruleSetPage += "</select></div></div>";
		ruleSetPage	+= '<div class="modal-footer clearfix">';
		ruleSetPage	+= '<button class="btn btn-warning pull-right" onclick="SurveyBuild.SaveTjx1('+tz_app_id+');"><i class="icon-ok"></i>确定</button></div>';
		this.openMoadal(ruleSetPage);        
    },
	tjx_zhschange:function(){
		if (document.getElementById("zhs_qy").checked){
			$("#tjx_mb_id").removeAttr("disabled");
			$("#tjx_eamil_id").removeAttr("disabled");
		}else{
			$("#tjx_mb_id").attr("disabled","disabled");
			$("#tjx_eamil_id").attr("disabled","disabled");
		}
	},
	SaveTjx1: function(id){
		var _bmb_id=$("#tjx_mb_id").val();
		var _email_desc=$("#tjx_eamil_id").val();
		var _zhs_qy="";
		if (document.getElementById("zhs_qy").checked){
			_zhs_qy="Y";
		}else{
			_zhs_qy="N";
		}
		var _url=SurveyBuild.tzGeneralURL;
		$.ajax({
			type: "post",
			url: _url+'?tzParams={"ComID":"TZ_GD_TJX_COM","PageID":"TZ_GD_TJX_PZ_STD","OperateType":"U","comParams":{"update":[{"tz_app_id":"'+id+'","tz_language":"C","bmb_id":"'+_bmb_id+'","email_desc":"'+_email_desc+'","tjx_qy":"'+_zhs_qy+'"}]}}',
			dataType: "json",
			async: false,
			success: function(result){
				$.fancybox.close();
			}
		});
	},
	//推荐信控件选择推荐信模板和推荐信发送邮件模板(英文)
	RulesENG: function(el) {
		/*var _url=document.location.href;
		var _url=_url.split("?");
		var _url1=_url[1].split("=");
		var tz_app_id=_url1[1];*/
		var tz_app_id=SurveyBuild._tid;
		var _url=SurveyBuild.tzGeneralURL;
		var _bmb_xz="";
		var _email_desc="";
		var _tjx_qy="";
		var ruleSetPage = "";
		$.ajax({
			type: "post",
			url: _url+'?tzParams={"ComID":"TZ_GD_TJX_COM","PageID":"TZ_GD_TJX_PZ_STD","OperateType":"QF","comParams":{"tz_app_id":"'+tz_app_id+'","tz_language":"E"}}',
			dataType: "json",
			async: false,
			success: function(result){
				_bmb_xz=result.comContent.formData.tjx_xz;
				_email_desc=result.comContent.formData.email_desc;
				_tjx_qy=result.comContent.formData.tjx_qy;
			}
		});
		ruleSetPage = '<div class="modal-header"><h4>英文推荐信设置</h4></div>';
		ruleSetPage += '<div class="modal-line" style="width:330px;"></div>';
		//是否启用英文推荐信
		ruleSetPage += '<div class="edit_item_warp" style="margin-top:15px;margin-left:20px;">';
        ruleSetPage += '<input type="checkbox" ' + (_tjx_qy == "Y" ? "checked='checked'" : "") + ' id="eng_qy" onclick="SurveyBuild.tjx_engchange()"/> <label for="eng_qy" style="display:inline;">启用英文推荐信</label>';
        ruleSetPage += '</div>';
		//选择推荐信报名表
		ruleSetPage += "<div style='height:40px;margin-top:25px;'><div style='width: 120px; margin-left: 20px;'>推荐信模板</div>";
		ruleSetPage += "<div style='width: 250px; margin-left: 120px; margin-top: -25px;'><select style='width:220px' id='tjx_mb_id' class='selectCss' " + (_tjx_qy == 'N' ? 'disabled="disabled"' : '') + ">";
		ruleSetPage += _bmb_xz;
		ruleSetPage += "</select></div></div>";
		//选择发送邮件模板
		ruleSetPage += "<div style='height:40px;margin-top:25px;'><div style='width: 120px; margin-left: 20px;'>推荐信邮件模板</div>";
		ruleSetPage += "<div style='width: 250px; margin-left: 120px; margin-top: -25px;'><select style='width:220px' id='tjx_eamil_id' class='selectCss' " + (_tjx_qy == 'N' ? 'disabled="disabled"' : '') + ">";
		ruleSetPage += _email_desc;
		ruleSetPage += "</select></div></div>";

		ruleSetPage	+= '<div class="modal-footer clearfix">';
		ruleSetPage	+= '<button class="btn btn-warning pull-right" onclick="SurveyBuild.SaveTjx2('+tz_app_id+');"><i class="icon-ok"></i>确定</button></div>';
		this.openMoadal(ruleSetPage);        
    },
	tjx_engchange:function(){
		if (document.getElementById("eng_qy").checked){
			$("#tjx_mb_id").removeAttr("disabled");
			$("#tjx_eamil_id").removeAttr("disabled");
		}else{
			$("#tjx_mb_id").attr("disabled","disabled");
			$("#tjx_eamil_id").attr("disabled","disabled");
		}
	},
	SaveTjx2: function(id){
		var _bmb_id=$("#tjx_mb_id").val();
		var _email_desc=$("#tjx_eamil_id").val();
		var _eng_qy="";
		if (document.getElementById("eng_qy").checked){
			_eng_qy="Y";
		}else{
			_eng_qy="N";
		}
		var _url=SurveyBuild.tzGeneralURL;
		$.ajax({
			type: "post",
			url: _url+'?tzParams={"ComID":"TZ_GD_TJX_COM","PageID":"TZ_GD_TJX_PZ_STD","OperateType":"U","comParams":{"update":[{"tz_app_id":"'+id+'","tz_language":"E","bmb_id":"'+_bmb_id+'","email_desc":"'+_email_desc+'","tjx_qy":"'+_eng_qy+'"}]}}',
			dataType: "json",
			async: false,
			success: function(result){
				$.fancybox.close();
			}
		});
	},
    add: function(f, a) {
        $("#y").show();
        this.is_edit = true;
        var me = this;
        if(!a){
            SurveyBuild.isDHContainer = false;
            SurveyBuild.currentDHID = "";
            /*
            $("#question-box li.active").removeClass("active");
            $("#question-box li.grey").removeClass("grey");
            $("#question-edit").empty();
            */
        }

        this._count == 0 && $("#question-new").hide(); //第一次添加信息项时，隐藏提示信息
        SurveyBuild.isDHContainer != true && ++this._count;
        var callback = function(f) {
            var d = "A" + ( + new Date());
            var _childrenLen = 0;
            if (SurveyBuild.isDHContainer) {
                $.each(me._items[SurveyBuild.currentDHID]["children"],function(i) {
                    _childrenLen += 1;
                });
                _childrenLen += 1;
            }
            var component = new me.comClass[f]();
            if (!component["instanceId"]) {
                component["instanceId"] = d
            }
            if (!component["orderby"]) {
                component["orderby"] = (SurveyBuild.isDHContainer ? _childrenLen: me._count) + ""
            }
            if (!component["itemId"]) {
                component["itemId"] = "TZ_" + (SurveyBuild.isDHContainer ? me._items[SurveyBuild.currentDHID]["itemId"] + "_" + _childrenLen: me._count);
            }
            //常用控件默认包含的校验规则
            var _getRules = function(instanceId) {
                var rules = [];
                var i = $.inArray(component["classname"], me._componentIndex);
                if (i > -1) {
                    rules = me._componentConfig[i]["rules"];
                }
                return rules;
            };
            var rules = _getRules(d) || {};
            $.each(rules,
                function(classname, rec) {
                    if (rec["isEnable"] == "Y") {
                        component["rules"][classname] = rec;
                        if (classname == "RequireValidator") {
                            component["isRequire"] = "Y";
                        } else if (classname == "CharLenValidator") {
                            component["isCheckStrLen"] = "Y";
                        } else if (classname == "NumSizeValidator") {
                            component["isNumSize"] = "Y";
                        } else if (classname == "RegularValidator") {
                            component["preg"] = "Y";
                        }
                    }
                });
            //把校验规则Copy到实例对象中
            component["rules"] = cloneObj(rules);
            if (SurveyBuild.isDHContainer) {
                me._items[SurveyBuild.currentDHID]["children"][d] = component;
            } else {
                me._items[d] = component;
            }
            component._init && component._init.call(component, d);

            var _itemHtml = "";
            if (a) {
                setTimeout(function() {
                        $("#y").animate({
                                top: '250px'
                            },
                            "slow",
                            function() {
                                $("#y").hide();
                                $("#y").css("top", "0");
                            })
                    },
                    650);
                var b = -1;
                if (SurveyBuild.isDHContainer) {
					b = $(a).children().index($(".draggable-holder"));
					$(".draggable-holder").remove();
                } else {
               		b = $("#question-box").children().not("#question-new").index($(".draggable-holder"));
					$(".draggable-holder").remove();
                }

                _itemHtml = me._html(d);
                if (SurveyBuild.isDHContainer == true) {
                    if (b > 0) {
                        $(_itemHtml).insertAfter($(a).children().eq(b - 1)).click();
                    } else {
                        $(_itemHtml).prependTo($(a)).click();
                    }
                } else {
                    if (b > 0) {
                        $(_itemHtml).insertAfter($("#question-box>li").eq(b - 1)).click();
                    } else {
                        $(_itemHtml).prependTo($("#question-box")).click();
                    }
                }
            } else {
                $(me._html(d)).appendTo($("#question-box")).click();
                $("html,body").scrollTop($(document).height());
                setTimeout(function() {
                        $("#y").animate({
                                top: '250px'
                            },
                            "slow",
                            function() {
                                $("#y").hide();
                                $("#y").css("top", "0");
                            })
                    },
                    650);
            }
        };
        this.loadScript(f, callback);
        if ($.inArray(f, ["DHContainer","LayoutControls"]) != -1) {
            $(".DHSort").sortable({
                cursor: "move",
                axis: "y",
                opacity: 0.6,
                revert: true,
                //connectWith: '.DHContainer',
                receive: function(event, ui) {
                    SurveyBuild.isDHForTwo = true;
                    SurveyBuild.isDHContainer = true;
                    SurveyBuild.currentDHID = $(event.target).closest("li").attr("data_id");
                    SurveyBuild.add(ui.item.attr("data-classname"), $(event.target));
                },
                update: function(event, ui) {
                    var dhObj = $(this).closest(".DHContainer");
                    SurveyBuild.dhSort(dhObj);
                }
            }).disableSelection();
        }
        f == "Page" && this._initTab();
        setTimeout(function() {
                $("#y").animate({
                        top: '250px'
                    },
                    "slow",
                    function() {
                        $("#y").hide();
                        $("#y").css("top", "0");
                    })
            },
            650);
    },
    edit: function(f, event) {
        if (!this.checkAttrVal()) {
            return;
        }
        $("#href2").click();
        var e = $(f);
        //点击多行容器子项的时候置为Y
        var dhbz = e.closest("ul").attr("data-dhbz") == "Y" ? true: false;
        $("#question-box .grey").removeClass("grey");
        if (dhbz) {
            //容器置灰
            e.parents("li").addClass("grey");
            //设置为编辑多行容器模式
            SurveyBuild.isDHContainer = true;
            //设置当前多行容器ID
            SurveyBuild.currentDHID = e.parents("li").attr("data_id");
        } else {
            SurveyBuild.isDHContainer = false;
            e.find(".DHContainer li").addClass("grey");
        }
        h = e.attr("data_id");
        $("#question-box li.active").removeClass("active");
        e.removeClass("grey").addClass("active");
        $("#build-right").css("height", "auto");

        var a = this._edit(h);
        var b = $(window).scrollTop(),
        d = $(f).offset().top - $("#question-wrap").offset().top - 150;
        $("#question-edit").html(a);
        $("#question-edit").attr("data_id", h);

        event && event.preventDefault && event.preventDefault();
        event && event.stopPropagation && event.stopPropagation();
        SurveyBuild._optionMove();
    	return false;
    },
    _optionMove: function() {
        $("#child-box tbody").sortable({
            cursor: "move",
            items: "tr",
            handle: ".icon-move",
            appendTo: "parent",
            axis: "y",
            revert: true,
            update: function(b, a) {
                var d = {},
                c = a.item.attr("data-id").split("-")[0];
                a.item.parent().find("tr").each(function(e) {
                    var f = $(this).attr("data-id").split("-")[1];
                    d[f] = SurveyBuild._items[c]["child"][f];
                    d[f].edit = 1;
                    d[f]["orderby"] = e + 1
                });
                SurveyBuild._items[c].child = d;
                $(SurveyBuild._html(c)).find(".question-answer").replaceAll("#q" + c + " .question-answer")
            }
        });
        /*拖拽元素，更新orderby的值*/
        $("#option-box tbody").sortable({
            cursor: "move",
            items: "tr",
            handle: ".icon-move",
            appendTo: "parent",
            axis: "y",
            revert: true,
            update: function(c, b) {
                var a = {},
                d = b.item.attr("data-id").split("-")[0];
                b.item.parent().find("tr").each(function(e) {
                    var f = $(this).attr("data-id").split("-")[1];
                    a[f] = SurveyBuild._items[d].option[f];
                    a[f].edit = 1;
                    a[f]["orderby"] = e + 1
                });
                SurveyBuild._items[d].option = a;
                $(SurveyBuild._html(d)).find(".question-answer").replaceAll("#q" + d + " .question-answer")
            }
        })
    },
    /*控件属性*/
    _edit: function(d) {
        var data = this._items[d];
        if (SurveyBuild.isDHContainer) {
            data = this._items[SurveyBuild.currentDHID]["children"][d];
        }
        var e = '';
        e += '<fieldset>';
        e += '  <legend>';
        e += '      <span class="edit_item_label ">编号：</span>';
        e += '      <input type="text" onkeyup="SurveyBuild.saveAttr(this,\'itemId\')" value="' + data.itemId + '" class="medium edit_itemId" onpaste="return false" ondragenter="return false" oncontextmenu="return false;" maxlength="30" />';
        e += '  </legend>';
        e += '</fieldset>';

        var nt = "";
        nt += '<fieldset>';
        nt += '  <legend>';
        nt += '		<span class="edit_item_label">名称：</span>';
        nt += '		<input type="text" class="medium edit_itemName" onkeyup="SurveyBuild.saveAttr(this,\'itemName\')" value="' + data.itemName + '" maxlength="60"/>';
        nt += '  </legend>';
        nt += '</fieldset>';

        nt += '<div id="editor-bar" style="width:300px;">';
        nt += '	<button class="btn btn-primary btn-mini" onclick="SurveyBuild.editor(\'' + data.instanceId + '\')">';
        nt += '		<i class="icon-font"></i> 编辑文字或插入图片';
        nt += '	</button>';
        nt += '</div>';
        nt += '<textarea id="'+ data.instanceId +'" class="question-text" onkeyup="SurveyBuild.saveAttr(this,\'title\')">' + data.title + '</textarea>';

        //e = (data["_CommonField"] == "Y" ? e: "") + data._edit(data);
        e = e + (data["classname"] == "Separator" ? "" : nt) +data._edit(data);
        e += '<div class="edit_item_warp" style="text-align: right;">';
        e += '  <button class="btn btn-small" onclick="SurveyBuild.remove(event,\'' + d + '\')"><i class="icon-trash"></i>删除</button>';
        e += '</div>';
        return e;
    },
    copy: function(e,g) {
        this.is_edit = true;
		//var $currObj = $(event.target);
		e = e||event; //火狐直接用event报错
        var $currObj = $(e.target);
        data = {};
        me = this;
        var isDH = ($currObj.closest(".DHContainer").length > 0 ? true: false);
	    ++this._count;
        var d = "A" + ( + new Date());
        if (isDH) {
            var $container = $currObj.parents("li").eq(1);
            var _dhContainerId = $container.attr("data_id");
            SurveyBuild.isDHContainer = true;
            SurveyBuild.currentDHID = _dhContainerId;
            data = cloneObj(me._items[_dhContainerId]["children"][g]);
            data["instanceId"] = d;
            var _childrenLen = 0;
            $.each(me._items[_dhContainerId]["children"],function(i) {
                _childrenLen += 1;
            });
            _childrenLen += 1;
            data["itemId"] = "TZ_" + me._items[_dhContainerId]["itemId"] + "_" + _childrenLen;
            var component = me._items[_dhContainerId]["children"][d] = new me.comClass[data["classname"]](data);
            component.orderby = _childrenLen;
            component._init && component._init.call(component, d);
            $(me._html(d)).insertAfter($currObj.closest("li")).effect("highlight", {},
            300);

        } else {
            data = cloneObj(me._items[g]);
            data["instanceId"] = d;
            data["itemId"] = "TZ_" + me._count;
            var component = me._items[d] = new me.comClass[data["classname"]](data);
            component._init && component._init.call(component, d);
            SurveyBuild.isDHContainer = false;
            if (data.isDoubleLine == "Y") {
                $.each(component.children,function(instanceId, obj) {
                    component.children[instanceId] = new me.comClass[obj.classname](obj);
                })
            }
            var $c = $(me._html(d));
            $c.insertAfter($currObj.closest("li")).effect("highlight", {},
            300,
            function() {
                $c.click();
                SurveyBuild.sort()
            });
            data["classname"] == "Page" && this._initTab();
        }

        return false
    },
    remove: function(event, target) {
        if (confirm("确定删除吗？")) {
            this.is_edit = true;
            var dhObj = $(event.target).closest("ul");
            var dhbz = dhObj.attr("data-dhbz") == "Y" ? true: false;

            if (SurveyBuild.isDHContainer || dhbz) {
                $("#question-box li[data_id='" + target + "']").remove();
                delete this._items[SurveyBuild.currentDHID]["children"][target];
                $("#question-edit").empty();
                this.dhSort(dhObj);
            }else if (this._items.hasOwnProperty(target)) {
                --this._count;
                $("#question-box li[data_id='" + target + "']").remove();
                this._items[target]["classname"] == "Page" && $("#tabNav div[data_id='" + target + "']").remove();
                delete this._items[target];

                target == $("#question-edit").attr("data_id") && $("#question-edit").empty();
                if ($("#question-box>li").length == 0) {
                    $("#question-new").show()
                } else {
                    this.sort()
                }
            } else {
                this._error("非法操作")
            }
        }
        if (event.stopPropagation) {
            event.stopPropagation()
        }
        event.cancelBubble = true;
        return false
    },
    sort: function() {
        this.is_edit = true;
        var b = 0,
        a = 0;
        $("#question-box>li").each(function(c) {
            var e = $(this).attr("data_id");
            if (e) {
				++a;
                if (SurveyBuild._items[e] && SurveyBuild._items[e].orderby) {
                    SurveyBuild._items[e].orderby = a
                }
                $(this).find(".question-code").eq(0).html(a);
            }
        })
    },
    dhSort: function(dhObj) {
        var dhInsId = $(dhObj).closest("li").attr("data_id");
        var dhdata = SurveyBuild._items[dhInsId]["children"];
        this.is_edit = true;
        var b = 0,
            a = 0;
        $(dhObj).find("li").each(function(c) {
            var e = $(this).attr("data_id");
            if (e) {
                ++a;
                if (dhdata[e] && dhdata[e].orderby) {
                    dhdata[e].orderby = a
                }
                $(this).find(".question-code").eq(0).html(a);
            }
        })
    },
    _html: function(d) {
        var _currentData, _style = "";
        if (SurveyBuild.isDHContainer) {
            _currentData = this._items[SurveyBuild.currentDHID]["children"][d];
        } else {
            _currentData = this._items[d]
            if (_currentData["isDoubleLine"] == "Y") {
                _style = "border:1px solid #eee;"
            }
        }
		//固定多行容器，工作经历、教育经历
		if (_currentData["fixedContainer"] && _currentData["fixedContainer"] == "Y"){
			_style = "";
		}
        //李丹丹修改，如果是分页符，则绑定和页签一样的事件
        //if (_currentData.classname == "Page") {
        //    c = '<li data_id="' + d + '" id="q' + d + '" onclick="SurveyBuild._editTabs();return false;" style="' + _style + '">';
        //} else{
        //    c = '<li data_id="' + d + '" id="q' + d + '" onclick="SurveyBuild.edit(this,event)" style="' + _style + '">';
        //}
        c = '<li data_id="' + d + '" id="q' + d + '" onclick="SurveyBuild.edit(this,event)" style="' + _style + '">';
        if (_currentData["_CommonField"] == "Y") {
            c += '<div class="question-title"><b class="question-code">' + _currentData.orderby + '.</b><div class="question-question">' + _currentData.title + '</div></div>';
        }
        //通过实例对象中的类名，找到类并调用_gethtml方法
        c += _currentData._getHtml(_currentData) || "";
        c += '<div class="question-action"><a class="build-icon-minus" title="删除" onclick="return SurveyBuild.remove(event,\'' + d + '\')"></a><a class="build-icon-copy" title="复制" onclick="SurveyBuild.copy(event,\'' + d + '\')"></a><i style="display:none;" class="build-icon-arrow"></i></div></li>'
        if (_style) {
            var e = "",_cc = c;
            SurveyBuild.isDHContainer = true;
            SurveyBuild.currentDHID = _currentData["instanceId"];
            $.each(_currentData.children,function(i, rec) {
                e += SurveyBuild._html(rec["instanceId"]);
            });
            SurveyBuild.isDHContainer = false;
            c = $(_cc).find(".DHContainer").append(e).parents("li").get(0).outerHTML;
        }
        return c;
    },
    editor: function(a) {
        this.is_edit = true;
        var b = "K_" + a;
        $.fancybox.open({
            //解决多行容器中的文字说明，在多行容器中的文字说明控件内容无法通过SurveyBuild._items[a].wzsm获取；
            //content: '<textarea id="' + b + '">' + SurveyBuild._items[a].wzsm + "</textarea>",
            content: '<textarea id="' + b + '">' + $("#" + a).val() + "</textarea>",
            minWidth: 1002,
            minHeight: 482,
            beforeShow: function() {
                window.editor = KindEditor.create("#" + b, {
                    items: ["source", "fontname", "fontsize", "|", "forecolor", "hilitecolor", "bold", "italic", "underline", "strikethrough", "removeformat", "|", "justifyleft", "justifycenter", "justifyright", "insertorderedlist", "insertunorderedlist", "|", "emoticons", "image", "link", "wordpaste"],
                    width: "1000px",
                    height: "480px",
                    allowFileManager: true,
                    filterMode: false
                })
            },
            beforeClose: function() {
                var c = editor.html();
                //SurveyBuild._items[a].edit = 1;
                //SurveyBuild._items[a].wzsm = c;
                $("#" + a).val(c);
                $("#" + a).trigger("onkeyup");
                //$("#q" + a).find(".question-answer").html(c);
                KindEditor.remove("#" + b)
            }
        })
    },
	editorTap: function(a) {
        this.is_edit = true;
        var b = "K_" + a;
        $.fancybox.open({
            //解决多行容器中的文字说明，在多行容器中的文字说明控件内容无法通过SurveyBuild._items[a].wzsm获取；
            content: '<textarea id="' + b + '">' + SurveyBuild._items[a].title + "</textarea>",
            //content: '<textarea id="' + b + '">' + $("#" + a).val() + "</textarea>",
            minWidth: 482,
            minHeight: 242,
            beforeShow: function() {
                window.editor = KindEditor.create("#" + b, {
                    items: ["source", "fontname", "fontsize", "|", "forecolor", "hilitecolor", "bold", "italic", "underline", "strikethrough", "removeformat", "|", "justifyleft", "justifycenter", "justifyright"],
                    width: "480px",
                    height: "240px",
                    allowFileManager: true,
                    filterMode: false
                })
            },
            beforeClose: function() {
                var c = editor.html();
                SurveyBuild._items[a].title = c;
				instanceId = SurveyBuild._items[a].instanceId;
			
				$("#tabNav div[data_id='" + instanceId + "']").html(c);
                KindEditor.remove("#" + b)
            }
        })
    },
    allRand: function(b, d) {
        var a = d ? "crand": "rand";
        $("." + a).prop("checked", $(b).prop("checked"))
    },
    allMandatory: function(a) {
        $(".mandatory").prop("checked", $(a).prop("checked"))
    },
    _advancedOption: function(g) {
        var b = this._items[g].qtype,
        d = this._items[g].option;
        if (b == 6) {
            var a = '<fieldset>';
            for (var c in d) {
                a += '<li><label style="background:' + d[c]["txt"] + '" for="rand' + c + '"></label><input type="checkbox" class="rand" id="rand' + c + '" data-id="' + c + '" value="1"' + (d[c]["is_rand"] == 1 ? " checked": "") + " /></li>"
            }
            return a + "</ul></fieldset>"
        }
        var e = '<fieldset><table id="table-advanced-option" class="table table-hover table-bordered"><tr><th>选项</th>';
        if (b == 1) {
            e += '<th style="width:55px;">默认选择</th></tr>';
            for (var c in d) {
                e += "<tr><td>" + d[c]["txt"] + '</td><td><input type="checkbox" class="rand" data-id="' + c + '" value="1"' + (d[c]["is_rand"] == 1 ? " checked": "") + ' /></td><td><input type="checkbox" onchange="$(\'.defaultval\').not(this).prop(\'checked\',false);" class="defaultval" data-id="' + c + '" value="1"' + (this._items[g].settings.hasOwnProperty("defaultval") && $.inArray(d[c].code, this._items[g].settings.defaultval) > -1 ? " checked": "") + " /></td></tr>"
            }
        } else {
            if (b == 2) {
                e += '<th style="width:50px;">其它&nbsp;&nbsp;<a href="#" class="popo" data-placement="left" data-content="勾选后该选项后出现文本框。常用于其它，请注明_____"><i class="icon-question-sign"></i></a></th><th style="width:55px;">默认选择</th></tr>';
                for (var c in d) {
                    e += "<tr><td>" + d[c]["txt"] + '</td><td><input type="checkbox" class="other" data-id="' + c + '" value="1"' + (d[c].settings.other == 1 ? " checked": "") + ' /></td><td><input type="checkbox" onchange="$(\'.defaultval\').not(this).prop(\'checked\',false);" class="defaultval" data-id="' + c + '" value="1"' + (this._items[g].settings.hasOwnProperty("defaultval") && $.inArray(d[c].code, this._items[g].settings.defaultval) > -1 ? " checked": "") + " /></td></tr>"
                }
            } else {
                if (b == 3) {
                    e += '<th style="width:50px;">其它&nbsp;&nbsp;<a href="#" class="popo" data-placement="left" data-content="勾选后该选项后出现文本框。常用于其它，请注明_____"><i class="icon-question-sign"></i></a></th><th style="width:30px;">排他</th><th style="width:55px;">默认选择</th></tr>';
                    for (var c in d) {
                        e += "<tr><td>" + d[c]["txt"] + '</td><td><input type="checkbox" class="rand" data-id="' + c + '" value="1"' + (d[c]["is_rand"] == 1 ? " checked": "") + ' /></td><td><input type="checkbox" class="other" data-id="' + c + '" value="1"' + (d[c].settings.other == 1 ? " checked": "") + ' /></td><td><input type="checkbox" class="exclusive" data-id="' + c + '" value="1"' + (d[c]["settings"]["exclusive"] == 1 ? " checked": "") + ' /></td><td><input type="checkbox" class="defaultval" data-id="' + c + '" value="1"' + (this._items[g].settings.hasOwnProperty("defaultval") && $.inArray(d[c].code, this._items[g].settings.defaultval) > -1 ? " checked": "") + " /></td></tr>"
                    }
                } else {
                    if (b == 5) {} else {
                        if (b == 9) {
                            e += '<th style="width:60px;"><input type="checkbox" onchange="SurveyBuild.allMandatory(this)"/> 必填</th><th style="width:50px;">其它&nbsp;&nbsp;<a href="#" class="popo" data-placement="left" data-content="勾选后该选项后出现文本框。常用于其它，请注明_____"><i class="icon-question-sign"></i></a></th><th style="width:60px;">正则</th><th style="width:40px;">后缀</th></tr>';
                            for (var c in d) {
                                e += "<tr><td>" + d[c]["txt"] + '</td><td><input type="checkbox" class="rand" data-id="' + c + '" value="1"' + (d[c]["is_rand"] == 1 ? " checked": "") + ' /></td><td><input type="checkbox" class="mandatory" data-id="' + c + '" value="1"' + (d[c].settings.mandatory == "1" ? " checked": "") + ' /></td><td><input type="checkbox" class="other" data-id="' + c + '" value="1"' + (d[c].settings.other == "1" ? " checked": "") + ' /></td><td><select data-id="' + c + '" name="preg"><option value="">不使用</option>';
                                for (var f in this._preg) {
                                    e += '<option value="' + this._preg[f][0] + '"' + (d[c].settings.preg == this._preg[f][0] ? ' selected="selected"': "") + ">" + this._preg[f][1] + "</option>"
                                }
                                e += '</select></td><td><input type="text" data-id="' + c + '" class="suffix" style="width:30px" value="' + d[c].settings.suffix + '" /></td></tr>'
                            }
                        } else {
                            if (b == 10) {
                                e += "</tr>";
                                for (var c in d) {
                                    e += '<tr><td><img src="' + d[c]["settings"]["pic"] + '" style="width:60px;height:50px;"/>' + d[c]["txt"] + '</td><td style="text-align:center;"><input type="checkbox" class="rand" data-id="' + c + '" value="1"' + (d[c]["is_rand"] == 1 ? " checked": "") + " /></td></tr>"
                                }
                            } else {
                                e += "</tr>";
                                for (var c in d) {
                                    e += "<tr><td>" + d[c]["txt"] + '</td></tr>'
                                }
                            }
                        }
                    }
                }
            }
        }
        return e + "</table></fieldset>"
    },
    minSelect: function() {
        var a = $("#min").val();
        $("#max>option").each(function(b) {
            $(this).prop("disabled", b < a)
        })
    },
    maxSelect: function() {
        var a = $("#max").val();
        $("#min>option").each(function(b) {
            $(this).prop("disabled", b > a)
        })
    },
    keypressNum: function(b) {
        var a = b.which;
        return a == 8 || a == 46 || a == 127 || a == 0 || a >= 48 && a <= 57
    },
    questionAdvanced: function(j) {
        var g = this._items[j].option;
        var a = this._items[j].child;
        var b = '<div class="modal-header"><h4>问题题目设置</h4></div><div id="modal-question-advanced" class="modal-body"><fieldset><label class="checkbox inline" for="is_mandatory"><input type="checkbox"' + (this._items[j]["is_mandatory"] == 1 ? " checked": "") + ' value="1" id="is_mandatory"/> 是否必填&nbsp;&nbsp;<a href="#" class="popo" data-content="用户需要填写这个信息项。"><i class="icon-question-sign"></i></a></label>';
        if (this._items[j].qtype == 5 && this._items[j].settings.type != "G") {
            b += '<label class="checkbox inline" for="fixed"><input type="checkbox"' + (this._items[j].settings.fixed == 1 ? " checked": "") + ' value="1" id="fixed"> 固定表头&nbsp;&nbsp;<a href="#" class="popo" data-content="表头会随着滚动条移动"><i class="icon-question-sign"></i></a></label><label class="checkbox inline" for="direction"><input type="checkbox" value="1" id="direction"' + (this._items[j].settings.direction == "Y" ? " checked": "") + " onchange=\"SurveyBuild.changeDirection('" + j + '\')"/> 矩阵翻转&nbsp;&nbsp;<a href="#" class="popo" data-content="转为列矩阵，每列单选、多选"><i class="icon-question-sign"></i></a></label>';
            if (this._items[j].settings.type == "S") {
                b += '<label class="checkbox inline" for="mean"><input type="checkbox" value="1" id="mean"' + (this._items[j].settings.hasOwnProperty("mean") && this._items[j].settings.mean == 1 ? " checked": "") + "/> 报表统计计算平均值</label>"
            }
        }
        b += "</fieldset>";
        if (this._items[j].qtype == 17) {
            b += '<fieldset><table id="table-advanced-child" class="table table-hover table-bordered"><tr><th>一级列表</th><th style="width:75px;"><input type="checkbox" onchange="SurveyBuild.allRand(this,1)" /> 随机&nbsp;&nbsp;<a href="#" class="popo" data-placement="left" data-content="选中的选项进行随机，未选中的不变"><i class="icon-question-sign"></i></a></th></tr>';
            for (var f in a) {
                b += "<tr><td>" + a[f]["question"] + '</td><td><input type="checkbox" class="crand" data-id="' + f + '" value="1"' + (a[f]["is_rand"] == 1 ? " checked": "") + " /></td></tr>"
            }
            b += "</table></fieldset>";
            b += '<fieldset><table id="table-advanced-option" class="table table-hover table-bordered"><tr><th>选项</th><th style="width:75px;"><input type="checkbox" onchange="SurveyBuild.allRand(this)"/> 随机&nbsp;&nbsp;<a href="#" class="popo" data-placement="left" data-content="选中的选项进行随机，未选中的不变"><i class="icon-question-sign"></i></a></th><th style="width:50px;">其它&nbsp;&nbsp;<a href="#" class="popo" data-placement="left" data-content="勾选后该选项后出现文本框。常用于其它，请注明_____"><i class="icon-question-sign"></i></a></th></tr>';
            for (var f in g) {
                b += "<tr><td>" + g[f]["txt"] + '</td><td><input type="checkbox" class="other" data-id="' + f + '" value="1"' + (g[f].settings.other == 1 ? " checked": "") + " /></td></tr>"
            }
            b += "</table></fieldset>"
        } else {
            if (this._items[j].qtype == 5) {
                b += '<fieldset><table id="table-advanced-child" class="table table-hover table-bordered"><tr><th>子问题</th><th style="width:75px;"><input type="checkbox" onchange="SurveyBuild.allRand(this,1)" /> 随机&nbsp;&nbsp;<a href="#" class="popo" data-placement="left" data-content="选中的选项进行随机，未选中的不变"><i class="icon-question-sign"></i></a></th><th style="width:50px;"><input type="checkbox" onchange="SurveyBuild.allMandatory(this)" /> 必答</th><th style="width:50px;">其它&nbsp;&nbsp;<a href="#" class="popo" data-placement="left" data-content="勾选后该选项后出现文本框。常用于其它，请注明_____"><i class="icon-question-sign"></i></a></th>';
                if (this._items[j].settings.direction == "Y") {
                    b += '<th style="width:75px">列宽（px）</th>'
                }
                b += "</tr>";
                var d = 1;
                for (var f in a) {
                    b += "<tr><td>" + a[f]["question"] + '</td><td><input type="checkbox" class="crand" data-id="' + f + '" value="1"' + (a[f]["is_rand"] == 1 ? " checked": "") + ' /></td><td><input type="checkbox" class="mandatory" data-id="' + f + '" value="1"' + (a[f].settings.mandatory == 1 ? " checked": "") + ' /></td><td><input type="checkbox" class="cother" data-id="' + f + '" value="1"' + (a[f].settings.other == 1 ? " checked": "") + " /></td>";
                    if (this._items[j].settings.direction == "Y") {
                        b += '<td><input type="text" data-id="' + f + '" value="' + (a[f].settings.hasOwnProperty("fixedwidth") ? a[f].settings.fixedwidth: "") + '" onfocus="this.select()" onkeypress="return SurveyBuild.keypressNum(event)" class="input-mini fixedwidth" maxlength="3" tabindex="' + d + '"/></td>'; ++d
                    }
                    b += "</tr>"
                }
                b += "</table></fieldset>";
                if (this._items[j].settings.type == "G") {
                    b += '<fieldset><p>最高多少分：<select id="max" onchange="SurveyBuild.changeGG(\'' + j + "')\">";
                    for (var f = 2; f <= 10; ++f) {
                        b += '<option value="' + f + '"' + (f == this._items[j].settings.max ? " selected": "") + ">" + f + "</option>"
                    }
                    b += '</select></p><table id="table-advanced-option" class="table table-hover table-bordered"><thead><tr><th style="width:60px;text-align:center;">分值</th><th style="text-align:left;">分值描述（可为空）</th></tr></thead><tbody>';
                    for (var f in g) {
                        b += '<tr><td style="text-align:center">' + g[f].code + '</td><td style="text-align:left"><input type="text" value="' + g[f].txt + '" tabindex="' + d + '"/></td></tr>'; ++d
                    }
                    b += "</tbody></table></fieldset>"
                } else {
                    d = 1;
                    var n = '<fieldset><table id="table-advanced-option" class="table table-hover table-bordered"><tr><th>选项</th>';
                    if (this._items[j].settings.type != "O") {
                        n += '<th style="width:50px;">其它&nbsp;&nbsp;<a href="#" class="popo" data-placement="left" data-content="勾选后该选项后出现文本框。常用于其它，请注明_____"><i class="icon-question-sign"></i></a></th>'
                    }
                    if (this._items[j].settings.type == "M") {
                        n += '<th style="width:30px;">排他</th>';
                        if (this._items[j].settings.direction == "X") {
                            n += '<th style="width:75px;">列宽（px）</th>'
                        }
                        n += "</tr>";
                        for (var f in g) {
                            n += "<tr><td>" + g[f]["txt"] + '</td><td><input type="checkbox" class="other" data-id="' + f + '" value="1"' + (g[f].settings.other == 1 ? " checked": "") + ' /></td><td><input type="checkbox" class="exclusive" data-id="' + f + '" value="1"' + (g[f]["settings"]["exclusive"] == 1 ? " checked": "") + " /></td>";
                            if (this._items[j].settings.direction == "X") {
                                n += '<td><input type="text" data-id="' + f + '" value="' + (g[f].settings.hasOwnProperty("fixedwidth") ? g[f].settings.fixedwidth: "") + '" onfocus="this.select()" onkeypress="return SurveyBuild.keypressNum(event)" class="input-mini fixedwidth" maxlength="3" tabindex="' + d + '"/></td>'; ++d
                            }
                            n += "</tr>"
                        }
                    } else {
                        if (this._items[j].settings.direction == "X") {
                            n += '<th style="width:75px;">列宽（px）</th>'
                        }
                        n += "</tr>";
                        for (var f in g) {
                            n += "<tr><td>" + g[f]["txt"] + '</td>';
                            if (this._items[j].settings.type != "O") {
                                n += '<td><input type="checkbox" class="other" data-id="' + f + '" value="1"' + (g[f].settings.other == 1 ? " checked": "") + " /></td>"
                            }
                            if (this._items[j].settings.direction == "X") {
                                n += '<td><input type="text" data-id="' + f + '" value="' + (g[f].settings.hasOwnProperty("fixedwidth") ? g[f].settings.fixedwidth: "") + '" onfocus="this.select()" onkeypress="return SurveyBuild.keypressNum(event)" class="input-mini fixedwidth" maxlength="3" tabindex="' + d + '"/></td>'; ++d
                            }
                            n += "</tr>"
                        }
                    }
                    n += "</table></fieldset>";
                    b += n
                }
            } else {
                if (this._items[j].hasOwnProperty("option")) {
                    b += this._advancedOption(j)
                }
            }
        }
        if (this._items[j].qtype == 3) {
            var h = 0,
            e = "",
            m = "";
            for (var f in g) {++h
            }
            var c = 0,
            l = 0;
            if (this._items[j].settings.min > 0) {
                c = this._items[j].settings.min
            }
            if (this._items[j].settings.max > 0) {
                l = this._items[j].settings.max
            }
            for (var f = 0; f <= h; ++f) {
                e += '<option value="' + f + '"' + (f == c ? " selected": "") + ">" + f + "</option>";
                m += '<option value="' + f + '"' + (f == l ? " selected": "") + ">" + f + "</option>"
            }
            b += '<fieldset>最少选择的答案数<select onchange="SurveyBuild.minSelect()" id="min">' + e + "</select></fieldset>";
            b += '<fieldset>最多选择的答案数<select onchange="SurveyBuild.maxSelect()" id="max">' + m + "</select></fieldset>"
        } else {
            if (this._items[j].qtype == 4 && !this._items[j].settings.hasOwnProperty("type")) {
                b += '<fieldset><p>字数范围</p><p>最少：<input type="text" class="input-mini" id="min" maxlength="5" value="' + this._items[j].settings.min + '">&nbsp;&nbsp;&nbsp;&nbsp;最多：<input type="text" class="input-mini" id="max" maxlength="5" value="' + this._items[j].settings.max + '"></p></fieldset>';
                b += '<fieldset><p>后缀：<input type="text" class="input-mini" id="suffix" value="' + (this._items[j].settings.hasOwnProperty("suffix") ? this._items[j].settings.suffix: "") + '">&nbsp;&nbsp;&nbsp;&nbsp;默认值：<input type="text" id="defaultval" value="' + (this._items[j].settings.hasOwnProperty("defaultval") ? this._items[j].settings.defaultval: "") + '"></p></fieldset>';
                b += '<fieldset><p>使用正则匹配<select id="preg" onchange="SurveyBuild.selectedPreg(this)"><option value="">不使用</option>';
                for (var f in this._preg) {
                    b += '<option value="' + this._preg[f][0] + '"' + (this._items[j].settings.preg == this._preg[f][0] ? ' selected="selected"': "") + ">" + this._preg[f][1] + "</option>"
                }
                b += '</select>&nbsp;&nbsp;<span id="minmaxnum" style="display:' + (this._items[j].settings.preg == "^\\d+$" || this._items[j].settings.preg == "^\\d+(\\.\\d+)?$" ? "inline": "none") + '">最小：<input type="text" class="input-mini" id="mininum" maxlength="9" value="' + (this._items[j].settings.hasOwnProperty("mininum") ? this._items[j].settings.mininum: "") + '">&nbsp;&nbsp;&nbsp;&nbsp;最大：<input type="text" class="input-mini" id="maxinum" maxlength="9" value="' + (this._items[j].settings.hasOwnProperty("maxinum") ? this._items[j].settings.maxinum: "") + '"></span></p></fieldset>'
            } else {
                if (this._items[j].qtype == 8) {
                    var h = 0;
                    for (var f in g) {++h
                    }
                    var k = this._items[j].settings.max != 0 ? this._items[j].settings.max: 0;
                    b += '<fieldset>排序的个数<select id="max"><option value="0">不限制</option>';
                    for (var h; h > 1; --h) {
                        b += '<option value="' + h + '"' + (h == k ? " selected": "") + ">" + h + "</option>"
                    }
                    b += "</select></fieldset>"
                } else {
                    if (this._items[j].qtype == 9) {
                        b += '<fieldset><p>所有答案总和等于：<input type="text" onkeypress="return SurveyBuild.keypressNum(event)" class="input-small" id="total" value="' + this._items[j].settings.total + '" maxlength="11">&nbsp;<span style="color:#f60">注：设置此项后，各项答案的值必须为数字</span></p></fieldset>'
                    } else {
                        if (this._items[j].qtype == 11) {
                            var h = 4,
                            k = "";
                            for (var f = 1; f <= h; ++f) {
                                k += '<option value="' + f + '"' + (f == this._items[j].settings.max ? " selected": "") + ">" + f + "</option>"
                            }
                            b += '<fieldset>要上传的文件数<select id="max">' + k + "</select></fieldset>"
                        }
                    }
                }
            }
        }
        b += '</div><div class="modal-footer clearfix"><button class="btn pull-left" onclick="$.fancybox.close()"><i class="icon-ban-circle"></i>取消</button><button class="btn btn-warning pull-right" onclick="SurveyBuild.saveSettings(\'' + j + '\')"><i class="icon-ok"></i>确定</button></div>';
        $.fancybox.open({
            content: b,
            helpers: {
                overlay: {
                    closeClick: false
                }
            },
            beforeShow: function() {
                $(".popo").popover({
                    trigger: "hover"
                })
            }
        })
    },
    selectedPreg: function(a) {
        var b = $(a).val();
        if (b == "^\\d+$" || b == "^\\d+(\\.\\d+)?$") {
            $("#minmaxnum").css("display", "inline")
        } else {
            $("#minmaxnum input").val("0");
            $("#minmaxnum").css("display", "none")
        }
    },
    changeGG: function(e) {
        var c = parseInt($("#max").val()),
        b = "",
        a = $("#table-advanced-option tr").length - 1;
        if (c > a) {
            for (var d = a + 1; d <= c; ++d) {
                b += '<tr><td style="text-align:center">' + d + '</td><td style="text-align:left"><input type="text" tabindex="' + d + '" value="" /></td></tr>'
            }
            $("#table-advanced-option").find("tbody").append(b)
        } else {
            $("#table-advanced-option tr:gt(" + c + ")").remove()
        }
    },
    changeDirection: function(d) {
        if ($("#direction").prop("checked")) {
            $("#table-advanced-option").find("tr th:last-child,tr td:last-child").remove();
            $("#table-advanced-child").find("tr:first").append('<th style="width:75px">列宽（px）</th>');
            var a = 1,
            e = this._items[d].child;
            for (var b in e) {
                $("#table-advanced-child").find("tr:eq(" + a + ")").append('<td><input type="text" data-id="' + b + '" value="' + (e[b].settings.hasOwnProperty("fixedwidth") ? e[b].settings.fixedwidth: "") + '" onfocus="this.select()" onkeypress="return SurveyBuild.keypressNum(event)" class="input-mini fixedwidth" maxlength="3" tabindex="' + a + '"/></td>'); ++a
            }
        } else {
            $("#table-advanced-child").find("tr th:last-child,tr td:last-child").remove();
            $("#table-advanced-option").find("tr:first").append('<th style="width:75px">列宽（px）</th>');
            var a = 1,
            c = this._items[d].option;
            for (var b in c) {
                $("#table-advanced-option").find("tr:eq(" + a + ")").append('<td><input type="text" data-id="' + b + '" value="' + (c[b].settings.hasOwnProperty("fixedwidth") ? c[b].settings.fixedwidth: "") + '" onfocus="this.select()" onkeypress="return SurveyBuild.keypressNum(event)" class="input-mini fixedwidth" maxlength="3" tabindex="' + a + '"/></td>'); ++a
            }
        }
    },
    optionBatch: function(e) {
        var a = '';

        var data = {};
        if (SurveyBuild.isDHContainer) {
            data = this._items[SurveyBuild.currentDHID]["children"][e];
        } else {
            data = this._items[e];
        }

        a += '<div class="modal-header">';
        a += '	<h4>批量编辑选项<span class="muted">(记录之间以回车分隔，名称与描述以”,“分隔)</span></h4>';
        a += '</div>';
		
		a += '<div class="modal-line"></div>';

        a += '<div id="modal-question-option" class="modal-body">';
        a += '	<textarea id="' + e + '-batch" class="option-text">';

        /*option列表*/
        var c = data.option;
        for (var b in c) {
            a += c[b]["code"] + "," + c[b]["txt"] + "\n"
        }

        a +='</textarea>';
        a +='</div>';
        a +='<div class="modal-footer clearfix">';
        a +='	<button class="btn pull-left" onclick="$.fancybox.close()">';
        a +='		<i class="icon-ban-circle"></i>取消';
        a +='	</button>';
        a +='	<button class="btn btn-warning pull-right" onclick="SurveyBuild.optionBatchSave(\'' + e + '\')">';
        a +='		<i class="icon-ok"></i>确认';
        a +='	</button>';
        a +='</div>';

        $.fancybox.open({
            content: a,
            helpers: {
                overlay: {
                    closeClick: false
                }
            },
            afterShow: function() {
                var g = $("#" + e + "-batch").val();
                $("#" + e + "-batch").val("").focus().val(g)
            }
        })
    },
    optionBatchSave: function(l) {
        this.is_edit = true;
        var m = $.trim($("#" + l + "-batch").val()),
        a = m.split("\n"),
        y = {},
        e = a.length,
        n = "A" + ( + new Date()),
        f = 0,
        h = {},
        code = "",
        txt = "",
        d = null;
        if (SurveyBuild.isDHContainer) {
            data = this._items[SurveyBuild.currentDHID]["children"][l];
        } else {
            data = this._items[l];
        }
        var b = data.option;

        for (var g in b) {
            if(e <= f){
                break;
            }
            y = a[f].split(",");
            if (a[f].indexOf(",") > 0) {
                code = y[0] || "";
                txt = y[1] || "";
            } else {
                txt = a[f];
            }
            if (e > f) {
                h[g] = b[g];
                h[g].txt = txt;
                if (a[f].indexOf(",") > 0) {
                    h[g].code = code;
                }
				++f;
            }
        }
        if (e > f) {
            var k = e - f;
            for (var g = 0; g < k; ++g) {
				++f;
                y = a[f - 1].split(",");
                if (a[f - 1].indexOf(",") > 0) {
                    code = y[0] || "";
                    txt = y[1] || "";
                } else {
                    code = f,
                    txt = a[f - 1];
                }
                h[n + g] = {
                    code: code,
                    txt: txt,
                    orderby: f
                };
            }
        }
        data.option = h;

        $(this._html(l)).find(".question-answer").replaceAll("#question-box li[data_id='" + l + "'] .question-answer");
        $("#question-box li[data_id='" + l + "']").click();
        $.fancybox.close()
    },
    plusOption: function(f, showType) {
        this.is_edit = true;
        var m = $(f).parents("tr").attr("data-id").split("-"),
        k = m[0],
        b = m[1],
        l = 0,
        n = "A" + ( + new Date()),
        g = {},
        h = 0,
        data = {};
        if (SurveyBuild.isDHContainer) {
            data = this._items[SurveyBuild.currentDHID]["children"][k];
        } else {
            data = this._items[k];
        }
        //生成唯一编号code
        for (var e in data.option) {
            if ($.isNumeric(data.option[e].code)) {
                var j = 1 * data.option[e].code;
                if (j > l) {
                    l = j
                }
            }
        }
        var a = ++l;
        data.sort = 1;
        for (var e in data.option) {
            h++;
            g[e] = cloneObj(data.option[e]);
            g[e].orderby = h;
            if (e == b) {++h;
                g[n] = {
                    code: a,
                    txt: "",
                    orderby: h,
                    defaultval: 'N',
                    other: 'N',
                    weight: 0
                }
            }
        }
        data.option = g;
        var d = $("#build-right").height();
        var tr = "";
		if(showType=='autocpl'){
			tr += '<tr class="read-radio" data-id="' + k + '-' + n + '">';
            tr += '<td><input type="text" onkeyup="SurveyBuild.saveLevel1Attr(this,\'code\')" value="' + a + '" oncontextmenu="return false;" ondragenter="return false" onpaste="return false" class="ocode"></td>';
            tr += '<td><input type="text" onkeyup="SurveyBuild.saveLevel1Attr(this,\'txt\')" value="" oncontextmenu="return false;" ondragenter="return false" class="option-txt"></td>';
            tr += '<td><a onclick="SurveyBuild.plusOption(this,\'autocpl\');return false;" class="text-success" href="javascript:void(0);"><i class="icon-plus-sign"></i> </a><a onclick="SurveyBuild.minusOption(this);return false;" class="text-warning" href="javascript:void(0);"><i class="icon-minus-sign"></i> </a><a href="javascript:void(0);" class="text-info option-move"><i class="icon-move"></i> </a></td>';
            tr += '</tr>';
		}else{			
			tr += '<tr class="read-radio" data-id="' + k + '-' + n + '">';
			tr += '<td><input type="checkbox" onchange="$(\'.defaultval\').not(this).prop(\'checked\',false);SurveyBuild.saveLevel1Attr(this,\'defaultval\')" class="defaultval" value="1"></td>';
			tr += '<td><input type="text" onkeyup="SurveyBuild.saveLevel1Attr(this,\'code\')" value="' + a + '" oncontextmenu="return false;" ondragenter="return false" onpaste="return false" class="ocode"></td>';
			tr += '<td><input type="text" onkeyup="SurveyBuild.saveLevel1Attr(this,\'txt\')" value="" oncontextmenu="return false;" ondragenter="return false" class="option-txt"></td>';
			tr += '<td><a onclick="SurveyBuild.plusOption(this);return false;" class="text-success" href="javascript:void(0);"><i class="icon-plus-sign"></i> </a><a onclick="SurveyBuild.minusOption(this);return false;" class="text-warning" href="javascript:void(0);"><i class="icon-minus-sign"></i> </a><a href="javascript:void(0);" class="text-info option-move"><i class="icon-move"></i> </a></td>';
			tr += '</tr>';
		}
        $(f).parents("tr").after(tr);
        $(this._html(k)).find(".question-answer").replaceAll("#question-box li[data_id='" + k + "'] .question-answer");
    },
		
	//add by kwl 20150420
	plusOption_radio: function(f) {
        this.is_edit = true;
        var m = $(f).parents("tr").attr("data-id").split("-"),
        k = m[0],
        b = m[1],
        l = 0,
        n = "A" + ( + new Date()),
        g = {},
        h = 0,
        data = {};
        if (SurveyBuild.isDHContainer) {
            data = this._items[SurveyBuild.currentDHID]["children"][k];
        } else {
            data = this._items[k];
        }

        //生成唯一编号code
        for (var e in data.option) {
            if ($.isNumeric(data.option[e].code)) {
                var j = 1 * data.option[e].code;
                if (j > l) {
                    l = j
                }
            }
        }
        var a = ++l;
        data.sort = 1;
        for (var e in data.option) {
            h++;
            g[e] = cloneObj(data.option[e]);
            g[e].orderby = h;
            if (e == b) {++h;
                g[n] = {
                    code: a,
                    txt: "",
                    orderby: h,
                    defaultval: 'N',
                    other: 'N',
                    weight: 0
                }
            }
        }
        data.option = g;
        var d = $("#build-right").height();
        var tr = "";
        tr += '<tr class="read-radio" data-id="' + k + '-' + n + '">';
        //默认
        tr += '<td><input type="checkbox" onchange="$(\'.defaultval\').not(this).prop(\'checked\',false);SurveyBuild.saveLevel1Attr(this,\'defaultval\')" class="defaultval" value="1"></td>';
        //其他
        tr += '<td><input type="checkbox" class="other" value="" oncontextmenu="return false;" ondragenter="return false" onpaste="return false" onchange="$(\'.other\').not(this).prop(\'checked\',false);SurveyBuild.saveLevel1Attr(this,\'other\')"></td>';
        //值
        tr += '<td><input type="text" onkeyup="SurveyBuild.saveLevel1Attr(this,\'code\')" value="' + a + '" oncontextmenu="return false;" ondragenter="return false" onpaste="return false" class="ocode"></td>';
        //描述
        tr += '<td><input type="text" onkeyup="SurveyBuild.saveLevel1Attr(this,\'txt\')" value="" oncontextmenu="return false;" ondragenter="return false" class="option-txt"></td>';
        //操作
        tr += '<td><a onclick="SurveyBuild.plusOption_radio(this);return false;" class="text-success" href="javascript:void(0);"><i class="icon-plus-sign"></i> </a><a onclick="SurveyBuild.minusOption(this);return false;" class="text-warning" href="javascript:void(0);"><i class="icon-minus-sign"></i> </a><a href="javascript:void(0);" class="text-info option-move"><i class="icon-move"></i> </a></td>';
        tr += '</tr>';
		
        $(f).parents("tr").after(tr);
        $(this._html(k)).find(".question-answer").replaceAll("#question-box li[data_id='" + k + "'] .question-answer");
    },
	
    minusOption: function(e) {
        this.is_edit = true;
        if ($(e).parents("tr").siblings().length == 0) {
            this._error("至少要有一个选项")
        } else {
            var c = $(e).parents("tr").attr("data-id").split("-"),
            f = c[0],
            d = c[1],
            data = {};
            if (SurveyBuild.isDHContainer) {
                data = this._items[SurveyBuild.currentDHID]["children"][f];
            } else {
                data = this._items[f];
            }
            if (data.option.hasOwnProperty(d)) {
                $(e).parents("tr").remove();
                $("#o" + d).remove();
                delete data.option[d]
            } else {
                this._error("非法操作")
            }
        }
    },
    /*修改问题后，自动保存*/
    saveQuestion: function(a) {
        this.is_edit = true;
        var c = $(a).attr("id"),
        b = $(a).val();
        this._items[c].edit = 1;
        this._items[c].question = b;
        b = b.replace(/<script/ig, "&lt;script").replace(/<\/script>/ig, "&lt;script&gt;");
        $("#q" + c).find(".question-question").html(b)
    },
    saveSliderMax: function(a, c) {
        this.is_edit = true;
        this._items[c].edit = 1;
        var b = $(a).val();
        this._items[c].settings.max = b;
        $("#q" + c + " .slider-max").text(Math.ceil(b * 0.2))
    },
    saveSliderStart: function(a, c) {
        this.is_edit = true;
        this._items[c].edit = 1;
        var b = $(a).val();
        this._items[c].settings.start = b;
        $("#q" + c + " .slider-start").text(b)
    },
    saveSliderEnd: function(a, c) {
        this.is_edit = true;
        this._items[c].edit = 1;
        var b = $(a).val();
        this._items[c].settings.end = b;
        $("#q" + c + " .slider-end").text(b)
    },

    saveQCode: function(a) {
        this.is_edit = true;
        var b = $(a).attr("id").substr(1);
        this._items[b].edit = 1;
        this._items[b].code = $(a).val();
        $("#q" + b + " b.question-code").text(this._items[b].code + ".")
    },
    saveQName: function(a) {
        this.is_edit = true;
        var b = $(a).attr("id").substr(1);
        this._items[b].edit = 1;
        this._items[b].code = $(a).val();
        $("#q" + b + " b.question-code").text(this._items[b].code + ".")
    },
    saveCCode: function(b) {
        this.is_edit = true;
        var a = $(b).parents("tr").attr("id").split("-"),
        c = a[0],
        d = a[1];
        this._items[c].child[d].edit = 1;
        this._items[c].child[d].code = $(b).val()
    },
    saveOCode: function(c) {
        this.is_edit = true;
        var a = $(c).parents("tr").attr("id").split("-"),
        d = a[0],
        b = a[1];
        this._items[d].option[b].edit = 1;
        this._items[d].option[b].code = $(c).val()
    },
    saveTxt: function(c) {
        this.is_edit = true;
        var a = $(c).parents("tr").attr("id").split("-"),
        e = a[0],
        b = a[1],
        d = $(c).val();
        this._items[e].option[b].edit = 1;
        this._items[e].option[b].txt = d;
        d = d.replace(/<script/ig, "&lt;script").replace(/<\/script>/ig, "&lt;script&gt;");
        if (this._items[e].option[b].hasOwnProperty("settings") && this._items[e].option[b].settings.other == 1) {
            d += '<b class="read-input"></b>'
        }
        if (this._items[e].qtype != 10) {
            $("#o" + b).html(d)
        }
    },
    saveChild: function(b) {
        this.is_edit = true;
        var a = $(b).parents("tr").attr("id").split("-"),
        d = a[0],
        e = a[1],
        c = $(b).val();
        this._items[d].child[e].edit = 1;
        this._items[d].child[e].question = c;
        if (this._items[d].qtype == 17) {
            $("#first" + e).text(c)
        }
        c = c.replace(/<script/ig, "&lt;script").replace(/<\/script>/ig, "&lt;script&gt;");
        if (this._items[d].child[e].hasOwnProperty("settings") && this._items[d].child[e].settings.other == 1) {
            c += '<b class="read-input"></b>'
        }
        $("#c" + e).html(c)
    },
    preiewBiuld: function(){
	    SurveyBuild.saveBuild(true);
    },
    saveBuild: function(isPreview) {
        if (!this.checkAttrVal()) {
            return
        }
		var pageSize = $("#tabNav").children().size();
		if(pageSize > 0){
			if(pageSize == 1){
				noteing("建议分页报名表至少分两页！", 2)
			}
			if(!this.checkPage()){
				return;
			}
		}
        var a = {},b = true,d = "Page",c = 0,pageno = 0;
        $("#question-box>li").each(function(f) {
            var g = $(this),h = g.attr("data_id"),e = SurveyBuild._items[h]["classname"];
            e == "Page" && ++c && ++pageno;
            SurveyBuild._items[h]["pageno"] = pageno;
            if (d == "Page" && e == "Page" && c != 1) {
                SurveyBuild.fail($(this), "第" + c + "页不能没有问题", "top");
                b = false;
                return false
            } else {
                d = e;
                /*如果是多行容器、分组框，那么重新获取其内容（用于多行容器信息项排序）*/
                if ($.inArray(SurveyBuild._items[h].classname, ["DHContainer","LayoutControls"]) != -1) {
                    var children = {};
                     $("#q" + SurveyBuild._items[h]["instanceId"] + " > ul > li").each(function(f){
                        var insId = $(this).attr("data_id");
                         children[insId] =  SurveyBuild._items[h]["children"][insId];
                     });
                    SurveyBuild._items[h]["children"] = children;
                }
                a[h] = SurveyBuild._items[h]
            }

        });

        SurveyBuild._data['items'] = a;
        var params = '{"ComID":"TZ_ONLINE_REG_COM","PageID":"TZ_ONREG_OTHER_STD","OperateType":"U","comParams":{"update":[{"tid":"' + SurveyBuild._tid + '","data":' + $.toJSON(SurveyBuild._data) + '}]}}';
        if (b) {
            loading($("#build-box"));
            $("#build-save button[id=save]").text("保存中...");
            $.ajax({
                type: "POST",
                url: SurveyBuild.tzGeneralURL,
                data: {
                    tzParams: params
                },
                dataType: "JSON",
                success: function(f) {
                    loaded($("#build-box"));
                    $("#build-save button[id=save]").html('保存<span style="font-size:12px;">(建议10分钟一次)</span>');
                    if (f.state.errcode == 0) {
                        if(f.comContent.alterMsg != ""){
                            alert(f.comContent.alterMsg);
                        }
                        noteing("保存成功");
                        SurveyBuild.is_edit = false;
                        var e = $("#question-box>li.active").index() - 1;
                        if (isPreview) {
                            var tzParams = '?tzParams={"ComID":"TZ_ONLINE_REG_COM","PageID":"TZ_ONLINE_FORM_STD","OperateType":"HTML","comParams":{"mode":"Y","TZ_APP_TPL_ID":"' + SurveyBuild._tid + '"}}'
                            var url = SurveyBuild.tzGeneralURL + tzParams;
                            window.open(url, '_blank');
                        }
                    } else {
                        noteing("保存失败", 2)
                    }
                }
            });
            return true
        }
    },
    saveEmptyText: function(a) {
        this.is_edit = true;
        var b = $(a).attr("data_id");
        this._items[b].edit = 1;
        this._items[b].emptyText = $(a).val();
    },
	init: function(a) {
		$("#question-edit").on("keydown", "input:text",
		function(d) {
			if (d.which == 9) {
				var b = $(":text").length,
				c = $(":text").index(this);
				c = ++c == b ? 0 : c;
				$(":text").eq(c).focus().select();
				return false
			}
		});
		$("#question-type-box li.move").draggable({
			connectToSortable: "#question-box,.DHContainer",
			addClasses: false,
			appendTo: "#build-box",
			snapTolerance: 300,
			revert: "invalid",
			helper: function() {
				return $('<div class="draggable-holder">' + $(this).html() + "</div>")
			},
			stop: function(event, ui) {
				$(".DHSort").sortable({
					cursor: "move",
					axis: "y",
					opacity: 0.6,
					revert: true,
					//connectWith: '.DHContainer',
					receive: function(event, ui) {
						SurveyBuild.isDHForTwo = true;
						SurveyBuild.isDHContainer = true;
						SurveyBuild.currentDHID = $(event.target).closest("li").attr("data_id");
						SurveyBuild.add(ui.item.attr("data-classname"), $(event.target));
					},
					update: function(event, ui) {
						var dhObj = $(this).closest(".DHContainer");
						SurveyBuild.dhSort(dhObj);
					}
				}).disableSelection();
			}
		});
		$("#question-box").sortable({
			cursor: "move",
			// placeholder: "place-holder",
			axis: "y",
			opacity: 0.6,
			revert: true,
			cancel: "#question-new",
			receive: function(event, ui) {
				if (SurveyBuild.isDHForTwo != true) {
					SurveyBuild.isDHContainer = false;
					SurveyBuild.add(ui.item.attr("data-classname"), 1);
				} else {
					SurveyBuild.isDHForTwo = false;
					$("#question-box>li").each(function(e) {
						$(".draggable-holder").remove();
						//if ($(this).attr("data-classname")) {
						//	$(this).remove();
						//	return
						//}
					});
				}
			},
			update: function(c, b) {
				SurveyBuild.sort();
			}
		}).disableSelection();

		window.onbeforeunload = function() {
			if (SurveyBuild.is_edit) {
				return "当前页面有未保存内容\n选择“离开页面” 会丢失当前编辑的内容\n选择“留在此面” 可以继续编辑问题然后保存离开"
			}
		};
		this._initHelpDesc();
		this._htmlSet();

		var events = '{"SA_A": "保存事件后","LO_A": "加载事件后","SU_A": "提交事件后"}';
		this._events = JSON.parse(events);

		this._items = this._data["items"];
		$("#question-edit").on("keypress", ".qcode,.ocode,.ccode",
		function(c) {
			var b = c.which;
			return b == 8 || b == 127 || b == 0 || b == 95 || (b >= 48 && b <= 57) || (b >= 65 && b <= 90) || (b >= 97 && b <= 122)
		});
		$("#question-edit").on("keypress", ".maxYear,.minYear,.minLen,.maxLen,.numSizeMax,.numSizeMin,.numDecws,.minSelect,.maxSelect,.minLines,.maxLines",
		function(c) {
			var b = c.which;
			return b == 8 || b == 127 || b == 0 || b >= 48 && b <= 57
		});
		$("#question-edit").on("keypress", ".edit_itemId", function(c) {
			var b = c.which;
			return b == 8 || b >= 65 && b <= 90 || b >= 97 && b <= 122 || b == 95 || b >= 48 && b <= 57;
		});
		$("#question-edit").on("keypress", ".edit_itemId", function(c) {
			var b = c.which;
			return b == 8 || b >= 65 && b <= 90 || b >= 97 && b <= 122 || b == 95 || b >= 48 && b <= 57;
		});
		SurveyBuild._tid = a;

		if (SurveyBuild._items) {
			SurveyBuild._load();
			loaded($("#question-wrap"))
		}
		$(".DHSort").sortable({
			cursor: "move",
			axis: "y",
			opacity: 0.6,
			revert: true,
			//connectWith: '.DHContainer',
			update: function(c, b) {
				var dhObj = $(this).closest(".DHContainer");
				SurveyBuild.dhSort(dhObj);
			}
		}).disableSelection();
	},
    _load: function (b) {
        var a = "", d = false;
        this._count = 0;
        var me = this;
        var callback = function (params) {
            var classname = params[0];
            var data = params[1];
            var d = data["instanceId"];
            //data转换成项对应的对象
            var component = me._items[d] = new me.comClass[classname](data);
            if (component.isDoubleLine == "Y" && component.fixedContainer != "Y") {
                if (me.is_edit_moda) {
                    $.each(component.children,function (instanceId, obj) {
                            component.children[instanceId] = new me.comClass[obj.classname](obj);
                        })
                } else {
                    $.each(component.children,function (i, rec) {
                            $.each(rec,function (instanceId, obj) {
                                    component.children[i][instanceId] = new me.comClass[obj.classname](obj);
                                })
                        })
                }
            }
            component._init && component._init.call(component, d);

            if (me.is_edit_moda) {
                var $itemHtml = me._html(d);
                $("#question-box").append($itemHtml);
                /* 暂时注释 By WRL @2015-08-19
                var _eventbindEditor = me._items[d]._eventbindEditor;
                if (_eventbindEditor && typeof _eventbindEditor == "function") {
                    _eventbindEditor(me._items[d]);
                }
                 */
            } else {
                var _c = me._items[d]._getHtml(me._items[d], true),
                    data = me._items[d],_co = "";

                if (me._items[d]["isDoubleLine"] == "Y" && data.children && data.children.length > 0 && me._items[d]["fixedContainer"] != "Y") {
                    for (var i = 0; i < data.children.length; i++) {
                        _co += me._addOneRec(data.children, i);
                    }
                    _c = $("<div class='dhcontainer page" + data.pageno + "' style='' data-instancid='" + data.instanceId + "' >" + _c + "</div>").find(".main_inner_content").prepend(_co).parents('.dhcontainer')

                } else if (me._items[d]["fixedContainer"] && me._items[d]["fixedContainer"] == "Y") {
                    _c = "<div class='dhcontainer page" + data.pageno + "' style='' data-instancid='" + data.instanceId + "' >" + (_c || "") + "</div>";
                } else {
                    _c = "<div class='page" + data.pageno + "' style='' data-instancid='" + data.instanceId + "' >" + (_c || "") + "</div>";
                }
                $("#main_list").append(_c);

                var _eventbind = me._items[d]._eventbind;
                if (_eventbind && typeof _eventbind == "function") {
                    _eventbind(me._items[d]);
                }
                if (me._items[d]["isDoubleLine"] == "Y" && data.children && data.children.length > 0) {
                    for (var i = 0; i < data.children.length; i++) {
                        $.each(data.children[i],function (d, obj) {
                                if (obj._eventbind && typeof obj._eventbind == "function") {
                                    obj._eventbind(obj);
                                }
                            });
                    }
                }
                //是否可以继续添加
                if (data.children && data.maxLines >= 1 && data.maxLines == data.children.length) {
                    $(_c).find(".addnextbtn").hide();
                }
            }
        };

        /*加载信息项对应的JavaScript文件以及事件绑定 Begin*/
        $.each(this._items, function (c) {
            ++me._count;
            d = true;
            var item = me._items[c];
            if (item.isDoubleLine == "Y") {
                if (me.is_edit_moda) {
                    $.each(item.children, function (instanceId, obj) {
                        me.loadScript(obj["classname"]);
                    })
                } else {
                    if (!$.isArray(item.children)) {
                        item.children = [item.children];
                    }
                    $.each(item.children, function (i, rec) {
                        $.each(rec, function (instanceId, obj) {
                            me.loadScript(obj["classname"]);
                        });
                    })
                }
            }
            me.loadScript(item["classname"], callback, [item["classname"], item]);

            //预览模式、有校验规则、非单行组合框
            if (!me.is_edit_moda && item.hasOwnProperty("rules") && item.isSingleLine != "Y") {
                //事件绑定
                $.each(item["rules"], function (classname, classObj) {
                    if ($.inArray(classname, me._baseRules) == -1 && item["rules"][classname]["isEnable"] == "Y") {
                        var _ruleClass = ValidationRules[classname];
                        if (_ruleClass && _ruleClass._eventList) {
                            if (item["classname"] == "Radio" || item["classname"] == "Check" || item["classname"] == "CheckBox") {
                                $inputObjects = $("input[name='" + item["instanceId"] + "']");
                                $.each(_ruleClass._eventList, function (eventname, fun) {
                                    $inputObjects.bind(eventname,
                                        function () {
                                            if (fun && typeof fun == "function") {
                                                fun(item["itemId"], classObj["messages"], classObj["params"] || {},item, $(this));
                                            }
                                        });
                                });
                            } else {
                                $inputObject = $("#" + item["itemId"]);
                                $.each(_ruleClass._eventList,function (eventname, fun) {
                                        $inputObject.bind(eventname,
                                            function () {
                                                if (fun && typeof fun == "function") {
                                                    fun(item["itemId"], classObj["messages"], classObj["params"] || {},item);
                                                }
                                            });
                                    });
                            }
                        }
                    }
                });
            } else if (!me.is_edit_moda && item.isSingleLine == "Y") {
                if (ValidationRules && ValidationRules[item["requireJygz"]] && ValidationRules[item["requireJygz"]]._Validator && item["isRequire"] == "Y") {
                    ValidationRules[item["requireJygz"]]._Validator(item["itemId"], item["children"])
                }
                //多行容器中的校验
            } else if (!me.is_edit_moda && item.isDoubleLine == "Y") {
                $.each(item.children, function (i, rec) {
                    SurveyBuild._setValidator(rec);
                })
           }

            if(!me.is_edit_moda && item.hasOwnProperty("children") && item.isDoubleLine == "Y" && item.fixedContainer != "Y"){
                var childrens = item["children"];
                for (var i in childrens) {
                    children1 = childrens[i];
                    $.each(children1,function(insid, obj) {

                        if(obj.hasOwnProperty("rules") && obj.isSingleLine != "Y"){

                            //事件绑定
                            $.each(obj["rules"],function(classname, classObj) {
                                if ($.inArray(classname, me._baseRules) == -1 && obj["rules"][classname]["isEnable"] == "Y") {
                                    var _ruleClass = ValidationRules[classname];
                                    if (_ruleClass && _ruleClass._eventList && $.trim(classObj["messages"])!="") {
                                        if (obj["classname"] == "CheckBox") {
                                            $inputObject = $("#" + obj["itemId"]);
                                            $.each(_ruleClass._eventList, function (eventname, fun) {
                                                    $inputObject.bind(eventname, function () {
                                                    if (fun && typeof fun == "function") {
                                                        fun(obj["itemId"], classObj["messages"], classObj["params"] || {},obj);
                                                    }
                                                });
                                            });
                                        }
                                    }
                                }
                            });
                        }
                    });
                }
            }
        });
        /*加载信息项对应的JavaScript文件以及事件绑定 End*/

        if (!d && me.is_edit_moda) {
            $("#question-new").show()
        } else {

        }
        if (me.is_edit_moda) this._initTab();
        /*加载完成后默认第一个问题 By  WRL 2015-08-21 BEGIN*/
        var firstChild = $("#question-box>li").eq(0);
        firstChild.click();
        /*加载完成后默认第一个问题 By  WRL 2015-08-21 END*/
    },
	_initAssociatedShowHide: function(b) {
        var a = "",
        d = false;
        this._count = 0;
        var me = this;

        $.each(this._items,function(c) {
            var d = true;
			++me._count;
            var item = me._items[c];

            if(item.hasOwnProperty("children") && item.isDoubleLine == "Y" && item.fixedContainer != "Y"){

                var childrens = item["children"];
                for (var i in childrens) {
                children1 = childrens[i];
                $.each(children1,function(insid, obj) {
                    if(obj.hasOwnProperty("rules") && obj.isSingleLine != "Y"){
						
                        //事件绑定
                        $.each(obj["rules"],function(classname, classObj) {
                            if(classname=="AssociatedValidator"){
								if ($.inArray(classname, me._baseRules) == -1 && obj["rules"][classname]["isEnable"] == "Y") {
									
									var _ruleClass = ValidationRules[classname];
                                    if (_ruleClass && _ruleClass._eventList && $.trim(classObj["messages"])!="") {
										
                                        if(obj["classname"]=="Radio"){
                                            $inputObjects = $("input[name='radio" + obj["itemId"] + "']");
                                            eval(classObj["messages"]);
                                        }else if(item["classname"]=="Check"){
                                            $inputObjects = $("input[name='" + obj["itemId"] + "']");
                                            eval(classObj["messages"]);
                                        }else{
                                            $inputObject = $("#" + obj["itemId"]);
                                            eval(classObj["messages"]);
                                        }
                                    }
                                }
                            }
                            
                        });
                    }
                });
                }
            }else{
                //预览模式、有校验规则、非单行组合框
                if (!me.is_edit_moda && item.hasOwnProperty("rules") && item.isSingleLine != "Y") {
                    //事件绑定
                    $.each(item["rules"],function(classname, classObj) {
                        if(classname=="AssociatedValidator"){
                            if ($.inArray(classname, me._baseRules) == -1 && item["rules"][classname]["isEnable"] == "Y") {
                                var _ruleClass = ValidationRules[classname];
                                if (_ruleClass && _ruleClass._eventList && $.trim(classObj["messages"])!="") {

                                    if(item["classname"]=="Radio"){
                                        $inputObjects = $("input[name='radio" + item["itemId"] + "']");
                                        eval(classObj["messages"]);
                                    }else if(item["classname"]=="Check"){
                                        $inputObjects = $("input[name='" + item["itemId"] + "']");
                                        eval(classObj["messages"]);
                                    }else{
                                        $inputObject = $("#" + item["itemId"]);
                                        eval(classObj["messages"]);
                                    }
                                }
                            }
                        }
                    });
                }
            }

        });

    },
    _setValidator: function(rec) {
        var _min = 0,
            _max = 99999999999,
            _onError = "",
            _onCorrect = "",
            me = this;
        $.each(rec,function(instanceId, obj) {
            if (obj.isSingleLine == "Y") {
                if (ValidationRules && ValidationRules[obj["requireJygz"]] && ValidationRules[obj["requireJygz"]]._Validator && obj["isRequire"] == "Y") {
                    ValidationRules[obj["requireJygz"]]._Validator(obj["itemId"], obj["children"])
                }
            } else {
                var is_hava_rule_c = false;
                for (var _r in obj["rules"]) {
                    if (obj["rules"][_r]["isEnable"] == "Y") {
                        is_hava_rule_c = true;
                        break;
                    }
                }
                if (obj["isRequire"] == "Y" || obj["isCheckStrLen"] == "Y" || obj["isNumSize"] == "Y") {
                    is_hava_rule_c = true
                };
                if (is_hava_rule_c) {
                    if (obj["isRequire"] == "Y") {
                        _onError = obj["rules"]["RequireValidator"]["messages"];
                        _min = 1;
                    }
                    if (obj["isCheckStrLen"] == "Y") {
                        _min = Math.max(obj["minLen"], _min);
                        if (obj["maxLen"] > 0) _max = obj["maxLen"];
                        if (_max > 1 || obj["minLen"] > 0) {
                            if (obj["isCheckStrLen"] == "Y") _onError = obj["rules"]["CharLenValidator"]["messages"];
                        }
                    }
                    if (obj["isNumSize"] == "Y") {
                        _min = Math.max(obj["min"], _min);
                        if (obj["max"] > 0) _max = obj["max"];
                        if (_max > 1 || obj["min"] > 0) {
                            if (obj["isNumSize"] == "Y") _onError = obj["rules"]["NumSizeValidator"]["messages"];
                        }
                    }

                    $("#"+obj["itemId"])
                        .formValidator({tipID:obj["itemId"]+'Tip',onShow: obj["onShowMessage"]||"",onFocus: obj["onFoucsMessage"]||"&nbsp;",onCorrect: "&nbsp;"})
                        .functionValidator({
                            fun: function() {
                                var _result = true;
                                if (ValidationRules) {
                                    $.each(obj["rules"],function(classname, classObj) {
                                        if ($.inArray(classname, me._baseRules) == -1 && obj["rules"][classname]["isEnable"] == "Y") {
                                            var _ruleClass = ValidationRules[classname];
                                            if (_ruleClass && _ruleClass._Validator) {
                                                _result = _ruleClass._Validator(obj["itemId"], classObj["messages"], obj);
                                                if (_result != true) {
                                                    return false;
                                                }
                                            }
                                        }
                                    });
                                    return _result
                                }
                            }
                        });
                }
            }
        });
    },
    fail: function(c, b, a) {
        if (a == undefined) {
            a = "bottom"
        }
        $("html,body").animate({
            scrollTop: c.offset().top - 200
        },
        200,
        function() {
            c.popover({
                content: b,
                placement: a
            }).popover("show");
            c.effect("highlight");
            c.focus();
            setTimeout(function() {
                c.popover("destroy")
            },
            115500)
        })
    },
    _error: function(a) {
        alert(a)
    },
    //多行容器添加行
    _addOneRec: function(children, i) {
        var _co = "",del = "",lsep = ""; //容器行信息、容器行删除按钮、行与行直接的间隔
        //容器中行删除按钮源码
        del += '<div class="main_inner_content_del_bmb" onclick="SurveyBuild.deleteFun(this);">';
        del += '  <img src="' + TzUniversityContextPath + '/statics/images/appeditor/del.png" width="15" height="15">&nbsp;' + MsgSet["DEL"];
        del += '</div>';

        //容器中行与行直接的间隔
        lsep += '<div class="main_inner_content_top"></div>';
        lsep += '<div class="padding_div"></div>';
        lsep += '<div class="main_inner_content_foot"></div>';

        /*容器行信息 begin*/
        _co += "<div class='main_inner_content_para'>";

        if (i > 0) {
            //在第N行与N+1行直接添加间隔信息（首行除外）
            _co += lsep;
        }
		var lineno = 0;
        $.each(children[i],function(d, obj) {
                _co += obj._getHtml(obj, true);
				if(lineno > 0){
					return true
				}else{
					if(obj["isSingleLine"]&& obj["isSingleLine"] == "Y"){
						lineno = obj[0]["itemId"].substr(-1);
					}else{
						lineno = obj["itemId"].substr(-1);		
					}
				}
        });
        _co += "</div>";
        /*容器行信息 end*/

        if (i > 0) {
            //为除第一行之外的行添加删除功能
            //将删除按钮添加在行信息中，首个信息项的后面（该信息项必须包含main_inner_content_info_autoheight）
            _co = $(_co).find(".main_inner_content_info_autoheight").eq(0).append(del).closest(".main_inner_content_para").get(0).outerHTML;
        }
        return _co;
    },

    saveApp: function() {
        if ($("#app_save").length > 0) {
            $("#app_save").click();
        }
    },
    showDiv: function(btnEl, instanceId) {
        var dhid = $(btnEl).closest(".dhcontainer").attr("data-instancid");

        var maxLines = this._items[instanceId]["maxLines"], me = this;
        var isFixedCon = this._items[instanceId].fixedContainer;    //是否为固定多行容器
        var _children = this._items[instanceId]["children"], _fc = cloneObj(_children[0]);

        var suffix = this._items[dhid]["linesNo"].shift();

        //初始化多行容器的行信息data
        $.each(_fc,function(ins, obj) {
            _fc[ins]["value"] = "";

            if (obj.isSingleLine == "Y") {
                $.each(obj.children,function(i, ch) {
                    ch["value"] = "";
                    //ch["itemId"] += "_" + _children.length;
                    ch["itemId"] += "_" + suffix;
                });
            }else{
                //_fc[ins]["itemId"] += "_" + _children.length;
                _fc[ins]["itemId"] += "_" + suffix;
            }
            if (!isFixedCon || isFixedCon != "Y"){
                _fc[ins] = new me.comClass[obj.classname](obj);
            }
            if(obj.hasOwnProperty("option")){
                $.each(obj.option,function(i, opt) {
                    _fc[ins]["option"][i]["defaultval"] = "N";
                    _fc[ins]["option"][i]["other"] = "N";
                    _fc[ins]["option"][i]["checked"] = "N";
                });
            }
        });
        _children.push(_fc);
        if (isFixedCon && isFixedCon == "Y"){
            //处理固定多行容器
            $(this._items[instanceId]._getHtmlOne(this._items[instanceId],_children.length)).insertBefore($(btnEl).parents(".main_inner_content_info"));

            /*行信息中的Select格式化*/
            var selectObj = $(this._items[instanceId]._getHtml(this._items[instanceId],true)).find("select");
            $.each(selectObj,function(i,sObj){
                $("#" + $(sObj).attr("id")).chosen();
            });
            if (this._items[instanceId]._eventbind && typeof this._items[instanceId]._eventbind == "function") {
                this._items[instanceId]._eventbind(this._items[instanceId]);
            }
        } else {
            $(this._addOneRec(_children, _children.length - 1)).insertBefore($(btnEl).parents(".main_inner_content_info"));

            /*行信息中的Select格式化*/
            var selectObj = $(this._addOneRec(_children, _children.length - 1)).find("select");
            $.each(selectObj,function(i,sObj){
                $("#" + $(sObj).attr("id")).chosen();
            });
        }
		/*新增一行动态效果*/
		var $newRow = $(btnEl).parents(".main_inner_content_info").prev(".main_inner_content_para");
		$("html,body").animate({scrollTop: $newRow.offset().top}, 1000);
		
		
        //行数等于最大行数时，隐藏“Add One +”按钮
        if (_children.length == maxLines) {
            $(btnEl).hide();
        }

        /*子信息项事件绑定*/
        $.each(_children[_children.length - 1],function(d, obj) {
            if (obj._eventbind && typeof obj._eventbind == "function") {
                obj._eventbind(obj);
            }
            /*
            if(obj.hasOwnProperty("rules") && obj.isSingleLine != "Y"){
                //事件绑定
                $.each(obj["rules"],function(classname, classObj) {
                    if ($.inArray(classname, me._baseRules) == -1 && obj["rules"][classname]["isEnable"] == "Y") {

                        var _ruleClass = ValidationRules[classname];
                        if (_ruleClass && _ruleClass._eventList && $.trim(classObj["messages"])!="") {
                            if (obj["classname"] == "CheckBox") {
                                $inputObject = $("#" + obj["itemId"]);
                                $.each(_ruleClass._eventList, function (eventname, fun) {
                                    $inputObject.bind(eventname, function () {
                                        if (fun && typeof fun == "function") {
                                            fun(obj["itemId"], classObj["messages"], classObj["params"] || {}, obj);
                                        }
                                    });
                                });
                            }
                        }
                    }
                });
            }
            */
        });
        this._setValidator(_fc);

        $.each(_fc,function(insid, obj) {
            if(obj.hasOwnProperty("rules") && obj.isSingleLine != "Y"){

                //事件绑定
                $.each(obj["rules"],function(classname, classObj) {
                    if ($.inArray(classname, me._baseRules) == -1 && obj["rules"][classname]["isEnable"] == "Y") {

                        var _ruleClass = ValidationRules[classname];
                        if (_ruleClass && _ruleClass._eventList && $.trim(classObj["messages"])!="") {
                            if (obj["classname"] == "CheckBox") {
                                $inputObject = $("#" + obj["itemId"]);
                                $.each(_ruleClass._eventList, function (eventname, fun) {
                                    $inputObject.bind(eventname, function () {
                                        if (fun && typeof fun == "function") {
                                            fun(obj["itemId"], classObj["messages"], classObj["params"] || {}, obj);
                                        }
                                    });
                                });
                            }
                        }
                    }
                });
            }
        });
    },
	showTjx: function(btnEl, instanceId) {
        var max = this._items[instanceId]["maxLines"],
        me = this;
		var isFixedCon = this._items[instanceId].fixedContainer; //是否为固定多行
        var _children = this._items[instanceId]["children"],
        _fc = cloneObj(_children[0]);
        $.each(_fc,function(ins, obj) {
            _fc[ins]["value"] = "";
            _fc[ins]["itemId"] += "_" + _children.length;
            if (obj.isSingleLine == "Y") {
                $.each(obj.children,function(i, ch) {
                    ch["value"] = "";
                });
            }
			if (!isFixedCon || isFixedCon != "Y"){
            	_fc[ins] = new me.comClass[obj.classname](obj);
			}
        });
        _children.push(_fc);
		/*

        $.each(_children[_children.length - 1],function(d, obj) {
            if (obj.classname == "Select") {
                $("#" + obj.itemId).chosen()
            }
            if (obj._eventbind && typeof obj._eventbind == "function") {
                obj._eventbind(obj);
            }
        });*/
        //this._setValidator(_fc);

    },
    deleteFun: function(el) {
        //if (confirm("是否删除该条信息？")) {
            var index = $(el).closest(".main_inner_content_para").index();
            var instanceId = $(el).closest(".dhcontainer").attr("data-instancid");
            if (index > 0) {
                $(el).closest(".main_inner_content_para").animate({
                    height: 'hide',
                    opacity: 'hide'
                },
                'slow',
                function() {
                    $(el).closest(".main_inner_content_para").remove();
                });
                $("html,body").animate({
                    scrollTop: $(el).closest(".dhcontainer").find(".main_inner_content_para").eq(index - 1).offset().top
                },
                1000);
                $(el).closest(".dhcontainer").find(".addnextbtn").show();
                SurveyBuild._items[instanceId]["children"].splice(index, 1);
            } else {
                var chs = SurveyBuild._items[instanceId];
                $.each(chs.children[0],function(instanceId, obj) {
                    var $el = $("#" + obj.itemId);
                    if ($el.length > 0) {
                        SurveyBuild._clearAttrVal($el.get(0));
                    }
                    if (obj.isSingleLine == "Y") {
                        $.each(obj.children,function(k, ch) {
                            $el = $("#" + obj.itemId + ch.itemId);
                            SurveyBuild._clearAttrVal($el.get(0));
                        })
                    }
                })
            }
            //delete SurveyBuild._items[instanceId]["children"][index];

        //}
    },
    showMsg: function(obj, e) {

        var help_id = $(obj).attr("data-for-id");
        var vid = $(obj).attr("data-reveal-id");

        var helpSet = this._getHelpDesc(help_id);
        $("#" + vid).find("h1").text(helpSet["title"]);
	    $("#" + vid).find("p").text(helpSet["desc"]);

        var mX = 0,
        mY = 0;
        mX = this._pointerX(e);
        mY = this._pointerY(e);

        if ((mX + 286) > document.documentElement.clientWidth) {
            mX = document.documentElement.clientWidth - 300;
        }
        e.preventDefault();
        var modalLocation = $(obj).attr('data-reveal-id');
        $(obj).attr("data-for-x", mX + "px");
	$(obj).attr("data-for-y", mY + "px");
	$('#' + modalLocation).reveal($(obj).data());
    },
    _pointerX: function(event) {
        var docElement = document.documentElement,
        body = document.body || {
            scrollLeft: 0
        };
        return event.pageX || (event.clientX + (docElement.scrollLeft || body.scrollLeft) - (docElement.clientLeft || 0));
    },
    _pointerY: function(event) {
        var docElement = document.documentElement,
        body = document.body || {
            scrollTop: 0
        };
        return event.pageY || (event.clientY + (docElement.scrollTop || body.scrollTop) - (docElement.clientTop || 0));
    },
    _initHelpDesc: function() {
        if (this._helpDescSet.hasOwnProperty("help_init")) {} else {
            this._setHelpDesc("help_init", "");
            this._setHelpDesc("help_suffix", "关于后缀_一般用户展示信息项的单位，比如金额后面的￥号！");
            this._setHelpDesc("help_maxLen", "关于最多_用于定义用户录入数据的最大长度！");
            this._setHelpDesc("help_xxx", "关于XXX_XX用于！");
			this._setHelpDesc("help_isRequire","关于必填_勾选后该选项为必填项！");
			this._setHelpDesc("help_advancedSetup","关于高级设置_用于设置后台配置校验规则的启用及相关提示信息！");
			this._setHelpDesc("help_tplMaking","模板制作_请根据下载的word帮助文档内容，将第一步下载的文件制作成模板！");
			this._setHelpDesc("help_export","上传模板_请上传您制作好的模板文件！");
			this._setHelpDesc("help_isNumSize","数值范围_设定输入数值的最大、最小值，以及小数位数！");
        }
    },
    _setHelpDesc: function(hid, desc) {
        this._helpDescSet[hid] = this._helpDescSet[hid] || desc;
    },
    _getHelpDesc: function(hid) {
        var ret = {title: "error",desc: "Undefined help information!"};
        if (this._helpDescSet != undefined) {
            var desc = this._helpDescSet[hid];
            if (desc != undefined) {
                var tmpArray = desc.split("_");
                if (tmpArray.length == 2) {
                    ret["title"] = tmpArray[0];
                    ret["desc"] = tmpArray[1];
                }
            }
        }
        return ret;
    },
    /*模板设置---高级设置 BEGIN*/
    EventSet: function(el) {

		var mainTplArr;
		if(this._mainTplArr==""){
		    var params = '{"ComID":"TZ_ONLINE_REG_COM","PageID":"TZ_ONREG_OTHER_STD","OperateType":"EJSON","comParams":{"OType":"MAINTPL"}}';
            $.ajax({
                type: "get",
                dataType: "JSON",
                data:{
                    tzParams:params
                },
                async:false,
				objthis: this,
                url:SurveyBuild.tzGeneralURL,
                success: function(f) {
                    if(f.state.errcode == "0"){
                        //data.option = f.comContent;
						//alert(f.comContent);
						mainTplArr = f.comContent;
						this.objthis._mainTplArr = f.comContent;
                    }
                }
            });
		}else{
			mainTplArr = this._mainTplArr;
		}
	    /*邮件模板Begin*/
        var mailTpl = [];
        var params = '{"ComID":"TZ_ONLINE_REG_COM","PageID":"TZ_ONREG_OTHER_STD","OperateType":"EJSON","comParams":{"OType":"MAILTPL"}}';
        $.ajax({
            type: "get",
            dataType: "JSON",
            data:{tzParams:params},
            async:false,
            url:SurveyBuild.tzGeneralURL,
            success: function(f) {
                if(f.state.errcode == "0"){
                    mailTpl = f.comContent;
                }
            }
        });
        /*邮件模板End*/
        var eventSetPage = "";

        eventSetPage += '<div class="modal-header"><h4>高级设置</h4></div><div class="modal-line"></div><div class="modal-body" id="modal-question-advanced">';
        eventSetPage += '<fieldset>';
        eventSetPage += '<table id="table-advanced-relus" class="table table-hover table-bordered"><tbody>';
        eventSetPage += '<tr><th style="display: none;" width="26px">启用</th><th width="160px">应用程序类路径</th><th width="160px">应用程序类名称</th><th width="160px">应用程序类方法</th><th width="92px">事件</th><th>&nbsp;</th></tr>';
        if ($.isEmptyObject(this._data["events"])) {
            var n = "E" + ( + new Date()),
            g = {};
            g[n] = {
                "isEff": "N",
                "className": "",
                "classPath": "",
                "classFun": "",
                "eventType": ""
            }
            this._data["events"] = g;
        }

        $.each(this._data["events"],function(h, obj) {
            eventSetPage += '<tr data-classname="RequireValidator" data-id="' + h + '">';
            eventSetPage += '<td style="display: none;"><label class="checkbox inline"><input type="checkbox" id="eff_' + h + '"' + (obj.isEff == "Y" ? " checked=\'true\'": "") + '></label></td>';
            eventSetPage += '<td><input type="text" class="option-txt" id="path_' + h + '" value="' + (obj.hasOwnProperty("classPath") ? obj.classPath: "") + '"></td>';
            eventSetPage += '<td><input type="text" class="option-txt" id="name_' + h + '" value="' + (obj.hasOwnProperty("className") ? obj.className: "") + '"></td>';
            eventSetPage += '<td><input type="text" class="option-txt" id="fun_' + h + '" value="' + (obj.hasOwnProperty("classFun") ? obj.classFun: "") + '"></td>';
            eventSetPage += '<td><select id="event_' + h + '" style="width: 100%; margin-left: 0px">';
            eventSetPage += '<option value="">--请选择--</option>';

            $.each(SurveyBuild._events,function(key, value) {
                eventSetPage += '<option value="' + key + '" ' + (obj.eventType == key ? " selected=\'selected\'": "") + '>' + value + '</option>';
            });

            eventSetPage += '</select></td>';
            eventSetPage += '<td><a href="javascript:void(0);" class="text-success" onclick="SurveyBuild.plusEvent(this);return false;"><i class="icon-plus-sign"></i> </a>&nbsp;<a href="javascript:void(0);" class="text-warning" onclick="SurveyBuild.minusEvent(this);return false;"><i class="icon-minus-sign"></i> </a></td></tr>';
        });
		
        eventSetPage += '</tbody></table>';
        eventSetPage += '<fieldset>';
        eventSetPage += '<div class="edit_item_warp"><span class="edit_item_label">跳转方式：</span><select id="targetType" class="edit_boxSize elewidth-select"><option value="IFRM" ' + (this._data.targetType == "IFRM" ? " selected=\'selected\'": "") + '>当前Iframe</option><option value="TOP" ' + (this._data.targetType == "TOP" ? " selected=\'selected\'": "") + '>顶层窗口</option></select></div>';
        eventSetPage += '<div class="edit_item_warp"><span class="edit_item_label">Redirect URL：</span><textarea id="redirectUrl" rows="3" class="elewidth">' + this._data.redirectUrl + '</textarea></div>';
		/*附属模版*/
		eventSetPage += '<div class="edit_item_warp" style = "padding-top:5px"><input style="margin:0 2px 2px 0;margin-bottom:4px\\0;" id="isAttachedTemplate" name="isAttachedTemplate" type="checkbox" onchange = "SurveyBuild.saveAttr(this,\'isAttachedTemplate\')" ' + (this._data.isAttachedTemplate == "Y" ? "checked='checked'" : "") + '/><span class="edit_item_label"><label for="isAttachedTemplate">附属模版</label></span>';

		var showMainTpl = 'style="display:none;"';
		if(this._data.isAttachedTemplate == "Y" )
		{
			showMainTpl = "";
		}
		eventSetPage += '<div class="edit_item_warp" id="mainTemplateSpan" '+ showMainTpl +'><span class="edit_item_label">主模版：</span><select id="mainTemplate" class="edit_boxSize elewidth-select">';		
		eventSetPage += '<option value="">请选择</option>';
		for (var i in mainTplArr) {
				eventSetPage += '<option ' + (this._data.mainTemplate == mainTplArr[i]["tplid"] ? "selected='selected'": "") + 'value="' + mainTplArr[i]["tplid"] + '">' + mainTplArr[i]["tplname"] + '</option>';
		}
		eventSetPage += '</select>';
		eventSetPage += '</div>';

        /*提交后发送邮件 Begin*/
        eventSetPage += '<div style="padding-top: 5px" class="edit_item_warp">';
        eventSetPage += '   <input type="checkbox" ' + (this._data.isSendMail == "Y" ? "checked='checked'" : "") + ' name="isSendMail" id="isSendMail" style="margin:0 2px 2px 0;margin-bottom:4px\\0;">';
        eventSetPage += '   <span class="edit_item_label"><label for="isSendMail">提交后发送邮件</label></span>';
        eventSetPage += '   <select class="edit_boxSize elewidth-select" id="mailTemplate">';
        eventSetPage += '       <option value="">请选择</option>';
        for (var i in mailTpl) {
            eventSetPage += '<option ' + (this._data.mailTemplate == mailTpl[i]["tplid"] ? "selected='selected'": "") + 'value="' + mailTpl[i]["tplid"] + '">' + mailTpl[i]["tplname"] + '</option>';
        }
        eventSetPage += '   </select>';
        eventSetPage += '</div>';
        /*提交后发送邮件 End*/

        eventSetPage += '</fieldset>';
        eventSetPage += '</fieldset>';
        eventSetPage += '</div>';
		eventSetPage += '<div class="modal-line-1"></div>';
        eventSetPage += '<div class="modal-footer clearfix"><button class="btn pull-left" onclick="$.fancybox.close()"><i class="icon-ban-circle"></i>取消</button><button class="btn btn-warning pull-right" onclick="SurveyBuild.saveEvents()"><i class="icon-ok"></i>确定</button></div>';
        this.openMoadal(eventSetPage);
    },
    saveEvents: function(g) {
        this.is_edit = true;
        var data = SurveyBuild._data;

        data.targetType = $("#targetType").val();
        data.redirectUrl = $("#redirectUrl").val();
		data.mainTemplate = $("#isAttachedTemplate").prop("checked") ? $("#mainTemplate").val() : "";
		data.isAttachedTemplate = $("#isAttachedTemplate").prop("checked") ? "Y": "N";
        /*提交后发送邮件、邮件模板*/
        data.mailTemplate = $("#mailTemplate").val();
        data.isSendMail = $("#isSendMail").prop("checked") ? "Y": "N";

        $.each(data["events"],function(key, obj) {
            obj.isEff = $("#eff_" + key).prop("checked") ? "Y": "N";
            obj.classPath = $("#path_" + key).val();
            obj.className = $("#name_" + key).val();
            obj.classFun = $("#fun_" + key).val();
            obj.eventType = $("#event_" + key).val();
        });

        $.fancybox.close()
    },
    /*添加事件*/
    plusEvent: function(f) {
        this.is_edit = true;
        var m = $(f).parents("tr").attr("data-id"),
        l = 0,
        n = "E" + ( + new Date()),
        g = {},
        h = 0,
        data = this._data;

        var a = ++l;

        for (var e in data.events) {
            h++;
            g[e] = cloneObj(data.events[e]);
            if (e == m) {++h;
                g[n] = {
                    "isEff": "N",
                    "className": "",
                    "classPath": "",
                    "classFun": "",
                    "eventType": ""
                }
            }
        }
        data.events = g;

        var eventRow = "";
        eventRow += '<tr data-classname="RequireValidator" data-id="' + n + '">';
        eventRow += '<td style="display: none;"><label class="checkbox inline"><input type="checkbox" id="eff_' + n + '"></label></td>';
        eventRow += '<td><input type="text" class="option-txt" id="path_' + n + '" value="' + (data.events[n].hasOwnProperty("classPath") ? data.events[n].classPath: "") + '"></td>';
        eventRow += '<td><input type="text" class="option-txt" id="name_' + n + '" value="' + (data.events[n].hasOwnProperty("className") ? data.events[n].className: "") + '"></td>';
        eventRow += '<td><input type="text" class="option-txt" id="fun_' + n + '" value="' + (data.events[n].hasOwnProperty("classFun") ? data.events[n].classFun: "") + '"></td>';
        eventRow += '<td><select id="event_' + n + '" style="width: 100%; margin-left: 0px">';
        eventRow += '<option value="">--请选择--</option>';

        $.each(this._events,function(key, value) {
            eventRow += '<option value="' + key + '">' + value + '</option>';
        });

        eventRow += '</select></td>';
        eventRow += '<td><a href="javascript:void(0);" class="text-success" onclick="SurveyBuild.plusEvent(this);return false;"><i class="icon-plus-sign"></i> </a>&nbsp;<a href="javascript:void(0);" class="text-warning" onclick="SurveyBuild.minusEvent(this);return false;"><i class="icon-minus-sign"></i> </a></td></tr>';

        var d = $("#build-right").height();
        $(f).parents("tr").after(eventRow);
    },
    /*删除事件*/
    minusEvent: function(e) {
        this.is_edit = true;
        if ($(e).parents("tr").siblings().length == 0) {
            this._error("至少要有一个选项")
        } else {
            var c = $(e).parents("tr").attr("data-id"),
            data = this._data;
            if (data.events.hasOwnProperty(c)) {
                var b = $("#build-right").height();
                $(e).parents("tr").remove();
                delete data.events[c]
            } else {
                this._error("非法操作")
            }
        }
    },
    /*模板设置---高级设置 END*/

    /*模板设置 BEGIN*/
    _htmlSet: function() {

        var htmlSet = "",data = this._data;
        htmlSet += '<fieldset><legend>';
        htmlSet += '	<span class="edit_item_label">所属机构：</span>';
        htmlSet += '	<input type="text" class="medium" value="' + (data.hasOwnProperty("deptName") ? data.deptName: "") + '" disabled="disabled">';
        htmlSet += '</legend></fieldset>';

        htmlSet += '<fieldset><legend>';
        htmlSet += '	<span class="edit_item_label">模板名称：</span>';
        htmlSet += '	<input type="text" class="medium" onkeyup="SurveyBuild.save0Attr(this,\'tplName\')" value="' + (data.hasOwnProperty("tplName") ? data.tplName: "") + '">';
        htmlSet += '</legend></fieldset>';

        htmlSet += '<fieldset><legend>';
        htmlSet += '	<span class="edit_item_label">有效状态：</span>';
        htmlSet += '	<select class="edit_boxSize select" onchange="SurveyBuild.save0Attr(this,\'state\')">';
        htmlSet += '		<option value="Y" ' + (data.hasOwnProperty("state") && data.state == "Y" ? " selected=\'selected\'": "") + '>生效</option>';
        htmlSet += '		<option value="N" ' + (data.hasOwnProperty("state") && data.state == "N" ? " selected=\'selected\'": "") + '>失效</option>';
        htmlSet += '	</select>';
        htmlSet += '</legend></fieldset>';
		
        htmlSet += '<fieldset><legend>';
        htmlSet += '	<span class="edit_item_label">语言选择：</span>';
        htmlSet += '	<select class="edit_boxSize select" onchange="SurveyBuild.save0Attr(this,\'lang\')">';
        htmlSet += '		<option value="ZHS" ' + (data.hasOwnProperty("lang") && data.lang == "ZHS" ? " selected=\'selected\'": "") + '>中文</option>';
        htmlSet += '		<option value="ENG" ' + (data.hasOwnProperty("lang") && data.lang == "ENG" ? " selected=\'selected\'": "") + '>ENGLISH</option>';
        htmlSet += '	</select>';
        htmlSet += '</legend></fieldset>';
        
        htmlSet += '<fieldset><legend>';
        htmlSet += '	<span class="edit_item_label">报名表打印类型：</span>';
        htmlSet += '	<select class="edit_boxSize select" onchange="SurveyBuild.save0Attr(this,\'tpPdfType\')">';
        htmlSet += '		<option value="HPDF" ' + (data.hasOwnProperty("tpPdfType") && data.lang == "HPDF" ? " selected=\'selected\'": "") + '>原html转pdf</option>';
        htmlSet += '		<option value="TPDF" ' + (data.hasOwnProperty("tpPdfType") && data.lang == "TPDF" ? " selected=\'selected\'": "") + '>使用pdf模板打印</option>';
        htmlSet += '	</select>';
        htmlSet += '</legend></fieldset>';

        htmlSet += '<div class="edit_item_warp">';
        htmlSet += '	<span class="edit_item_label">描述:</span>';
        htmlSet += '	<textarea class="question-text" onchange="SurveyBuild.save0Attr(this,\'tplDesc\')">' + (data.hasOwnProperty("tplDesc") ? data.tplDesc: "") + '</textarea>';
        htmlSet += '</div>';

        htmlSet += '<fieldset><legend>';
        htmlSet += '	<span class="edit_item_label">模板类型：</span>';
        htmlSet += '	<select class="edit_boxSize select" onchange="SurveyBuild.save0Attr(this,\'tplUseType\')">';
        htmlSet += '		<option ' + (data.hasOwnProperty("tplUseType") && data.tplUseType == "BMB" ? " selected=\'selected\'": "") + ' value="BMB">报名表</option>';
        htmlSet += '		<option ' + (data.hasOwnProperty("tplUseType") && data.tplUseType == "TJX" ? " selected=\'selected\'": "") + ' value="TJX">推荐信</option>';
        htmlSet += '	</select>';
        htmlSet += '</legend></fieldset>';
        
    	if(!data.hasOwnProperty("leftWidth")){
    		data.leftWidth = "310";
    	}
        htmlSet += '<fieldset><legend>';
        htmlSet += '	<span class="edit_item_label">Left Width：</span>';
        htmlSet += '	<input type="text" maxlength="11" class="medium" onkeyup="SurveyBuild.save0Attr(this,\'leftWidth\')" value="' + data.leftWidth + '">';
        htmlSet += '</legend></fieldset>';

    	if(!data.hasOwnProperty("rightWidth")){
    		data.rightWidth = "390";
    	}
        htmlSet += '<fieldset><legend>';
        htmlSet += '	<span class="edit_item_label">Right Width：</span>';
        htmlSet += '	<input type="text" maxlength="11" class="medium" onkeyup="SurveyBuild.save0Attr(this,\'rightWidth\')" value="' + data.rightWidth + '">';
        htmlSet += '</legend></fieldset>';
        /* 暂时注释 By WRL @2015-10-30 (BUG 1139)
        htmlSet += '<fieldset><legend>';
        htmlSet += '	<span class="edit_item_label">标签位置：</span>';
        htmlSet += '	<select class="edit_boxSize select" onchange="SurveyBuild.save0Attr(this,\'labelPostion\')">';
        htmlSet += '		<option ' + (data.hasOwnProperty("labelPostion") && data.labelPostion == "LEFT" ? " selected=\'selected\'": "") + ' value="LEFT">左</option>';
        htmlSet += '		<option ' + (data.hasOwnProperty("labelPostion") && data.labelPostion == "UP" ? " selected=\'selected\'": "") + ' value="UP">上</option>';
        htmlSet += '	</select>';
        htmlSet += '</legend></fieldset>';

        htmlSet += '<fieldset><legend>';
        htmlSet += '	<span class="edit_item_label">提示方式：</span>';
        htmlSet += '	<select class="edit_boxSize select" onchange="SurveyBuild.save0Attr(this,\'showType\')">';
        htmlSet += '		<option ' + (data.hasOwnProperty("showType") && data.showType == "POP" ? " selected=\'selected\'": "") + ' value="POP">弹出框</option>';
        htmlSet += '		<option value="VAL" ' + (data.hasOwnProperty("showType") && data.showType == "VAL" ? " selected=\'selected\'": "") + '>Validate</option>';
        htmlSet += '	</select>';
        htmlSet += '</legend></fieldset>';

        
        htmlSet += '<div class="header-title">';
        htmlSet += '	<span class="title"><i class="icon-cog"></i> 打印设置</span>';
        htmlSet += '</div>';

        htmlSet += '<div class="groupbox">';
		
        htmlSet += '<div class="edit_item_warp">';
        htmlSet += '	<span class="edit_item_label">第一步：</span>';
        htmlSet += '	<div class="edit_item_right">';
        htmlSet += '		<div style="display: inherit;" onclick="SurveyBuild.expSource();">';
        htmlSet += '			<div class="bt_blue">导出报名表元数据</div>';
        htmlSet += '		</div>';
        htmlSet += '	</div>';
        htmlSet += '	<a style="display:none" data-animation="fade" data-reveal-id="myModal" class="big-link" onclick="SurveyBuild.showMsg(this,event)" data-for-id="help_export" href="#">(?)</a>';
        htmlSet += '</div>';

        htmlSet += '<div class="edit_item_warp">';
        htmlSet += '	<span class="edit_item_label">第二步：</span>';
        htmlSet += '	<div class="edit_item_right">';
        htmlSet += '		<a href="' + TzUniversityContextPath + '/statics/js/tranzvision/appwidget/manual20150914.doc" target="_blank">模板制作</a>';
        htmlSet += '		<a data-animation="fade" data-reveal-id="myModal" class="big-link" onclick="SurveyBuild.showMsg(this,event)" data-for-id="help_tplMaking" href="#">(?)</a>';
        htmlSet += '	</div>';
        htmlSet += '</div>';

        htmlSet += '<div class="edit_item_warp">';
        htmlSet += '	<form id="form1" name="form1" method="post" action="' + TzUniversityContextPath + '/UpdWebServlet?filePath=appFormPrintTpl" enctype="multipart/form-data">';
        htmlSet += '		<span class="edit_item_label">第三步：</span>';
        htmlSet += '		<div class="edit_item_right">';
        htmlSet += '			<div style="display: inherit;">';
        htmlSet += '				<input class="right-uplfile" type="file" onchange="SurveyBuild.upload()" class="fileupload" name="websitefile" id="fileupload">';
        htmlSet += '			</div>';
        htmlSet += '			<input type="hidden" id="mbaSqXzsmj1" name="mbaSqXzsmj1" value="">';
        htmlSet += '			<input type="hidden" id="newfilename" name="newfilename">';
        htmlSet += '			<span style="display:none;" id="sysfilename"></span>';
        htmlSet += '		</div>';
        htmlSet += '		<a href="#" data-for-id="help_export" onclick="SurveyBuild.showMsg(this,event)" class="big-link" data-reveal-id="myModal" data-animation="fade">(?)</a>';
        htmlSet += '	</form>';
        htmlSet += '</div>';
		
        htmlSet += '<div id="PrintAttFile" style="margin-bottom:5px;" class="edit_item_warp">';
        htmlSet += '<a style="color: #0c7bce;" target="_blank" id="downAtt" href="' + (data.hasOwnProperty("sysFileName") && data.hasOwnProperty("accessPath") ? TzUniversityContextPath + data["accessPath"] + "/" + data["sysFileName"]: "javascript:void(0)") + '">' + (data.hasOwnProperty("filename") ? data["filename"]: "") + '</a>';
        htmlSet += '&nbsp;&nbsp;&nbsp;&nbsp;<a id="deleteAtt" style="' + (data.hasOwnProperty("filename") && data["filename"].length > 1 ? "": "display:none") + '" href="javascript:void(0)" onclick="SurveyBuild.deleteAtt(this)">删除</a>';
        htmlSet += '</div>';
		
        htmlSet += '</div>';
        */
        htmlSet += '<div class="edit_item_warp">';
        htmlSet += '	<a onclick="SurveyBuild.EventSet(this);" href="javascript:void(0);"><i class="icon-cogs"></i> 高级设置</a>';
        htmlSet += '</div>';
		
		htmlSet += '<div class="edit_item_warp">';
        htmlSet += '&nbsp;';
        htmlSet += '</div>';

        $("#template-setting-box").html(htmlSet);
    },
    //导出源数据
    expSource: function(){
        if(this.is_edit){
            //noteing("请首先保存当前模板！", 2)
        }else{
			var tzParams = '{"ComID":"TZ_ONLINE_REG_COM","PageID":"TZ_ONREG_OTHER_STD","OperateType":"EJSON","comParams":{"OType":"METADATA","tid":"' + SurveyBuild._tid + '"}}';
			$.ajax({
				type: "GET",
				dataType: "JSON",
				url:SurveyBuild.tzGeneralURL,
				data:{
					tzParams:tzParams
				},
				success: function(f) {
					if(f.state.errcode == "0"){
						if(f.comContent.code == "0"){
							srcUrl = TzUniversityContextPath + f.comContent.url;
							window.open(srcUrl);
						} else {
							noteing(f.comContent.msg, 2)
						}
					}
				}
			});
        }
    },
    //上传模板
    upload: function(){
        var myDate = new Date();
        var path = document.getElementById("fileupload").value;
        filename = path.substring(path.lastIndexOf("\\") + 1,path.length);

        var myDate = new Date();
        var tmp_tp_num = myDate.getYear().toString() + myDate.getMonth().toString() + myDate.getDate().toString() + myDate.getHours().toString() + myDate.getMinutes().toString() + myDate.getSeconds().toString() + myDate.getMilliseconds().toString();
        var rand = parseInt(Math.random() * 389999);

        // 文件随机码，做系统文件名用
        var sysfilename = tmp_tp_num + rand;

        //文件后缀
        var sysfileSuffix = (filename.substring(filename.lastIndexOf(".") + 1)).toLowerCase();
        try{
            allsysfilename = sysfilename + "." + sysfileSuffix;
            sysfilename = sysfilename.toLowerCase();
            $("#form1").ajaxSubmit({
                dataType:'json',
                url:TzUniversityContextPath + "/UpdWebServlet?filePath=appFormPrintTpl",
                success: function(obj) {
                    if(obj.success){
                        $("#downAtt").attr("href",TzUniversityContextPath + obj.msg.accessPath + "/" + obj.msg.sysFileName);
                        $("#downAtt").text(obj.msg.filename);

                        this.is_edit = true;
                        data = SurveyBuild._data;
                        data["filename"] = obj.msg.filename;
                        data["sysFileName"] = obj.msg.sysFileName;
                        data["path"] = obj.msg.path;
                        data["accessPath"] = obj.msg.accessPath;
						$("#fileupload").val("");
                        $("#deleteAtt").show();
						$("#PrintAttFile").show();
                    }else{
                        noteing("模板上传失败！", 2);
                    }
                }
            });
        }catch(e){
            alert(e);
        }
    },
    //删除附件
    deleteAtt: function(obj){
        this.is_edit = true;
        data = this._data;

        data["filename"] = "";
        data["sysFileName"] = "";
        data["path"] = "";
        data["accessPath"] = "";
        $("#downAtt").attr("href","").text("");
        $(obj).hide();
		$("#PrintAttFile").hide();

        noteing("已经删除，请保存模板！");
    },
    /*模板设置 END*/
    save0Attr: function(el, attrName) {
        if (!el || !attrName) return;
        data = this._data;
        data[attrName] = this._getAttrVal(el);

        this.is_edit = true;
    },
    reFocus:function(id){
        $("#"+id).trigger('blur');
    },
	specialCharReplace: function(str){ 
		var s = "";
		if(str == undefined) {
			return str;
		}
		if(str.length == 0) return "";
		s = $.trim(str.toString());
		s = s.replace(/\\/g,"\\\\")
		s = s.replace(/\"/g,"\\\"");
		s = s.replace(/\'/g,"\\'");
		s = s.replace(/\t/g,"\\t");
		s = s.replace(/\f/g,"\\f");
		s = s.replace(/\//g,"\\/");
		s = s.replace(/\n/g,"\\n");
		return s;
	},
	checkPage: function() {
		var first = $("#question-box > li").first().attr("data_id");
		if(first){
			classname = this._items[first]["classname"];
			if(classname != "Page"){
				SurveyBuild._error("报名表第一个信息项必须是分页符！");
				return false;
			}
			return true;
		}else{
			return true;
		}
	}
};
var MsgSet = {};
