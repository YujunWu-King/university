var SurveyBuild = {
    _tid: 0,
    _data: {},
    _items: {},
    _count: 0,
    _use:"",
    tzGeneralURL:"",
    _preg: {"email": {"name": "邮箱","regExp": "/^[\\w-]+(\\.[\\w-]+)*@[\\w-]+(\\.[\\w-]+)+$/"},"telphone": {"name": "手机","regExp": "/^1\\d{10}$/"},"idcard": {"name": "身份证号","regExp": "/(^\\d{15}$)|(^\\d{18}$)|(^\\d{17}(\\d|X|x)$)/"},"url": {"name": "网址URL","regExp": "/(http|ftp|https):\\/\\/[\\w\\-_]+(\\.[\\w\\-_]+)+([\\w\\-\\.,@?^=%&amp;:/~\\+#]*[\\w\\-\\@?^=%&amp;/~\\+#])?/"}},
    is_edit: false,
    is_edit_moda: true,
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
        data = this._items[instanceId];
        var val = this._getAttrVal(el);
        data[attrName] = val;
        var rules = data["rules"];
        var _rules = this._componentConfig[$.inArray(data["classname"], this._componentIndex)]["rules"];
        var $activeLi = $("#question-box li.active");

        if (attrName == "title") {
            if (data["classname"] == 'TextExplain') {
                $activeLi.find(".question-answer").html(val);
            } else {
                $activeLi.find(".question-question").first().html(val);
            }
        } else if (attrName == "suffix") {
            $activeLi.find(".suffix").html(val);
        } else if (attrName == "format") {
            $activeLi.find(".format").removeClass().addClass("format format" + val)
            /* 所有可以设置控件属性的（edit）的报文中，都不在使用“wzsm”（报名人控件除外），暂时注释 by WRL 2015/08/14
             } else if (attrName == "wzsm") {
             //文字说明
             $activeLi.find(".question-answer").html(val);
             */
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
            } else if(val == "yy/mm"){
                format = "YYYY/MM";
            } else if(val == "yy-mm"){
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
            /* 条件语句为空，暂时注释 by WRL 2015/08/14
             } else if (attrName == "itemId") {
             //$activeLi.find(".question-code").html(val);
             */
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
                        if (attrName == "maxLines") {
                            //设置最多行数时，初始化linesNo属性
                            var linesNo = [];
                            for (var i = 1; i < val; i++) {
                                linesNo.push(i);
                            }
                            data["linesNo"] = linesNo;
                        }else{
                            if(attrName == "defaultval"){
                                //修改默认值时，直接将默认值赋值于value
                                data["value"] = val;
                            }else{
                                if(attrName == "isAttachedTemplate"){
                                    if(val=="Y")
                                    {
                                        $("#mainTemplate").show();
                                        $("#mainTemplateSpan").show();
                                    }else
                                    {
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
        this.is_edit = true;
    },
    saveLevel1Attr: function(el, attrNameLevel1) {
        if (!el || !attrNameLevel1) return;
        var val = this._getAttrVal(el),data = "";
        var $tr = $(el).closest("tr"),
        ids = $tr.attr("data-id").split("-"),
        instanceId = ids[0];
        optionId = ids[1];
        data = this._items[instanceId];
        try {
            if (attrNameLevel1 == "defaultval") {
                for (var i in data["option"]) {
                    data["option"][i][attrNameLevel1] = 'N';
                    data["option"][i]["checked"] = 'N';
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
            if (attrNameLevel1 == "defaultval" && val == "Y") {
                data["option"][ids[1]]["checked"] = 'Y';
            }
            if (attrNameLevel1 == "txt") {
                if (false) val += '<b class="read-input"></b>';
                $("#o" + ids[1]).html(val)
            }
            if(attrNameLevel1=="weight"){
                data["option"][ids[1]]["weight"] = val;
            }
        } catch(e) {};
        this.is_edit = true;
    },
	//子问题属性保存
	saveChildAttr: function(el, attrNameLevel1) {
        if (!el || !attrNameLevel1) return;
        var val = this._getAttrVal(el),data = "";
        var $tr = $(el).closest("tr"),
        ids = $tr.attr("data-id").split("-"),
        instanceId = ids[0];
        optionId = ids[1];
        data = this._items[instanceId];
        try {
            data["child"][ids[1]][attrNameLevel1] = val;
            if (attrNameLevel1 == "question") {
                $("#sq" + ids[1]).html(val)
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

        data = this._items[instanceId];
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
        data = this._items[instanceId];
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
        var tipObj;     //提示信息目标元素
        var d = $liActive.attr("data_id");
        if (this._items[d] && (this._items[d]["classname"] == "PageNav" || this._items[d]["classname"] == "Separator")) {
            return checkBz;
        }
        var $edit_box = $("#question-edit");
        var $itemId = $edit_box.find(".edit_itemId");
        var $itemName = $edit_box.find(".edit_itemName");
        var has = false,data;

        data = this._items;
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
        } else if (!$itemName.val().length > 30) {
            /*信息项编号长度限制*/
            checkBz = false;
            tipObj = $itemId;
            msg = "信息项编号长度不能超过30";
        } else if (!$itemName.val()) {
            /*信息项名称必填*/
            checkBz = false;
            tipObj = $itemName;
            msg = "信息项名称不能为空";
        } else {
            //信息项编号是否重复
            dance:for (var insId in this._items) {
                if (d != insId) {
                    if (this._items[insId]["itemId"] == $itemId.val()) {
                        has = true;
                        break;
                    } else if (!SurveyBuild.isDHContainer && this._items[insId]["isDoubleLine"] == "Y") {
                        //普通信息项与容器中信息项编号也不能重复
                        for (var insDHId in this._items[insId]["children"]) {
                            if (d != insDHId && this._items[insId]["children"][insDHId]["itemId"] == $itemId.val()) {
                                has = true;
                                break dance;
                            }
                        }
                    }
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

        _d = this._items[instanceId];
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
        pageHtml += '</div>';

        this.openMoadal(pageHtml);
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
        var rules = {};
        if(this._items[instanceId]){
            rules = this._items[instanceId]["rules"];
        }
        var val = this._getAttrVal(el);
        var className = $(el).closest("tr").attr("data-classname");

        /*控件最新规则列表*/
        var _rules = this._componentConfig[$.inArray(this._items[instanceId]["classname"], this._componentIndex)]["rules"];
        var rulesObj = rules[className] || _rules[className];

        rulesObj["messages"] = val;
        rules[className] = rulesObj;
    },
    /*控件属性--高级属性--End*/


    add: function(f, a) {
        $("#y").show();
        this.is_edit = true;
        var me = this;

        this._count == 0 && $("#question-new").hide(); //第一次添加信息项时，隐藏提示信息
        ++this._count;
        var callback = function(f) {
            var d = "A" + ( + new Date());
            var _childrenLen = 0;
            var component = new me.comClass[f]();
            if (!component["instanceId"]) {
                component["instanceId"] = d
            }
            if (!component["orderby"]) {
                component["orderby"] = me._count;
            }
			//题号
			component["qCode"] = "Q" + me._count;

            if (!component["itemId"]) {
                component["itemId"] = "TZ_" + me._count;
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

            me._items[d] = component;
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

                    $("#question-box>li").each(function(e) {
                        if ($(this).attr("data-classname")) {
                            b = e;
                            $(this).remove();
                            return
                        }
                    });

                _itemHtml = me._html(d);
                if (b > 0) {
                    $(_itemHtml).insertAfter($("#question-box>li").eq(b - 1)).click();
                } else {
                    $(_itemHtml).prependTo($("#question-box")).click();
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
        var e = '';
        e += '<fieldset>';
        e += '  <legend>';
        e += '      <span class="edit_item_label ">编号：</span>';
        e += '      <input type="text" onkeyup="SurveyBuild.saveAttr(this,\'itemId\')" value="' + data.itemId + '" class="medium edit_itemId" onpaste="return false" ondragenter="return false" oncontextmenu="return false;" maxlength="30" />';
        e += '  </legend>';
        e += '</fieldset>';

        e += '<fieldset>';
        e += '  <legend>';
        e += '      <span class="edit_item_label ">题号：</span>';
        e += '      <input type="text" onkeyup="SurveyBuild.saveAttr(this,\'qCode\')" value="' + data.qCode + '" class="medium edit_itemNo" onpaste="return false" ondragenter="return false" oncontextmenu="return false;" maxlength="30" />';
        e += '  </legend>';
        e += '</fieldset>';

        e += '<fieldset>';
        e += '  <legend>';
        e += '		<span class="edit_item_label">名称：</span>';
        e += '		<input type="text" class="medium edit_itemName" onkeyup="SurveyBuild.saveAttr(this,\'itemName\')" value="' + data.itemName + '"/>';
        e += '  </legend>';
        e += '</fieldset>';

        e += '<div id="editor-bar" style="width:300px;">';
        e += '	<button class="btn btn-primary btn-mini" onclick="SurveyBuild.editor(\'' + data.instanceId + '\')">';
        e += '		<i class="icon-font"></i> 编辑文字或插入图片';
        e += '	</button>';
        e += '</div>';
        e += '<textarea id="'+ data.instanceId +'" class="question-text" onkeyup="SurveyBuild.saveAttr(this,\'title\')">' + data.title + '</textarea>';

        e = (data["_CommonField"] == "Y" ? e: "") + data._edit(data);

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
        }

        return false
    },
    remove: function(event, target) {
        if (confirm("确定删除吗？")) {
            this.is_edit = true;
            var dhObj = $(event.target).closest("ul");
            if (this._items.hasOwnProperty(target)) {
                --this._count;
                $("#question-box li[data_id='" + target + "']").remove();
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
                    SurveyBuild._items[e].orderby = a;
					SurveyBuild._items[e].qCode = "Q" + a;
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
        if (_currentData["fixedContainer"] && _currentData["fixedContainer"] == "Y") {
            _style = "";
        }
        /*if (_currentData.classname == "Page") {
            c = '<li data_id="' + d + '" id="q' + d + '" onclick="SurveyBuild._editTabs();return false;" style="' + _style + '">';
        } else{
            c = '<li data_id="' + d + '" id="q' + d + '" onclick="SurveyBuild.edit(this,event)" style="' + _style + '">';
        }*/
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
        var data = this._items[e];

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
	other = "N",/*默认为N*/
        code = "",
        txt = "",
        d = null;
        data = this._items[l];
        var b = data.option;
	if(data.classname == "MultipleTextBox") other = "Y";/*多行文本框时默认值为"Y"*/
	var reptArr = [];
	var boolFlag = true;
		
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
		if (reptArr.indexOf(code) == -1){
			reptArr.push(code);
		}else{
			boolFlag=false;
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
                    orderby: f,
					checked:'N',
					defaultval:'N',
					other: other,
					weight:0
                };
				if (reptArr.indexOf(code) == -1){
					reptArr.push(code);
				}else{
					boolFlag=false;
				}
            }
        }
		if(boolFlag){
			data.option = h;
	
			$(this._html(l)).find(".question-answer").replaceAll("#question-box li[data_id='" + l + "'] .question-answer");
			$("#question-box li[data_id='" + l + "']").click();
			$.fancybox.close()
		}else{
			var tipObj = $("#" + l + "-batch");
			var msg = "选项编号重复";
			this.fail(tipObj, msg);	
		}
    },
	//子问题批量编辑----zhanglang
	childQuestBatch: function(e) {
        var a = '';
        var data = this._items[e];

        a += '<div class="modal-header">';
        a += '	<h4>批量编辑选项<span class="muted">(记录之间以回车分隔，编号、子问题与简称以”,“分隔)</span></h4>';
        a += '</div>';
		
		a += '<div class="modal-line"></div>';

        a += '<div id="modal-question-option" class="modal-body">';
        a += '	<textarea id="' + e + '-batch" class="option-text">';

        /*child列表*/
        var c = data.child;
        for (var b in c) {
            a += c[b]["sqCode"] + "," + c[b]["question"] + "," + c[b]["shortDesc"] + "\n"
        }

        a +='</textarea>';
        a +='</div>';
        a +='<div class="modal-footer clearfix">';
        a +='	<button class="btn pull-left" onclick="$.fancybox.close()">';
        a +='		<i class="icon-ban-circle"></i>取消';
        a +='	</button>';
        a +='	<button class="btn btn-warning pull-right" onclick="SurveyBuild.childQuestBatchSave(\'' + e + '\')">';
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
	//保存子问题批量编辑---zhanglang
    childQuestBatchSave: function(l) {
        this.is_edit = true;
        var m = $.trim($("#" + l + "-batch").val()),
        a = m.split("\n"),
        y = {},
        e = a.length,
        n = "S" + ( + new Date()),
        f = 0,
        h = {},
        sqCode = "",
        question = "",
		shortDesc = "",
        d = null;
        data = this._items[l];
        var b = data.child;
		
		var reptArr = [];
		var boolFlag = true;
		
        for (var g in b) {
            if(e <= f){
                break;
            }
            y = a[f].split(",");
            if (a[f].indexOf(",") > 0) {
                sqCode = y[0] || "";
                question = y[1] || "";
				shortDesc = y[2] || "";
				if (shortDesc == "undefined"){
					shortDesc = "";	
				}
            } else {
				sqCode = a[f];
                question = a[f];
            }
            if (e > f) {
                h[g] = b[g];
                h[g].question = question;
				h[g].shortDesc = shortDesc;
                if (a[f].indexOf(",") > 0) {
                    h[g].sqCode = sqCode;
                }
				++f;
            }
			if (reptArr.indexOf(sqCode) == -1){
				reptArr.push(sqCode);
			}else{
				boolFlag=false;
			}
        }
        if (e > f) {
            var k = e - f;
            for (var g = 0; g < k; ++g) {
				++f;
                y = a[f - 1].split(",");
                if (a[f - 1].indexOf(",") > 0) {
                    sqCode = y[0] || "";
                    question = y[1] || "";
					shortDesc = y[2] || "";
					if (shortDesc == "undefined"){
						shortDesc = "";	
					}
                } else {
                    sqCode = f,
                    question = a[f - 1];
                }
                h[n + g] = {
                    sqCode: sqCode,
                    question: question,
                    orderby: f,
					shortDesc: shortDesc,
					value:[],
					weight:''
                };
				if (reptArr.indexOf(sqCode) == -1){
					reptArr.push(sqCode);
				}else{
					boolFlag=false;
				}
            }
        }
		
		if(boolFlag){
			data.child = h;
			
			$(this._html(l)).find(".question-answer").replaceAll("#question-box li[data_id='" + l + "'] .question-answer");
			$("#question-box li[data_id='" + l + "']").click();
			
			$.fancybox.close();
		}else{
			var tipObj = $("#" + l + "-batch");
			var msg = "子问题编号重复";
			this.fail(tipObj, msg);	
		}
    },

	plusOption: function(f) {
		this.is_edit = true;
		var m = $(f).parents("tr").attr("data-id").split("-"),
		k = m[0],
		b = m[1],
		l = 0,
		n = "A" + ( + new Date()),
		g = {},
		h = 0,
		data = this._items[k];
		
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
		tr += '<td><input type="checkbox" onchange="$(\'.defaultval\').not(this).prop(\'checked\',false);SurveyBuild.saveLevel1Attr(this,\'defaultval\')" class="defaultval" value="1"></td>';
		tr += '<td><input type="text" onkeyup="SurveyBuild.saveLevel1Attr(this,\'code\')" value="' + a + '" oncontextmenu="return false;" ondragenter="return false" onpaste="return false" class="ocode"></td>';
		tr += '<td><input type="text" onkeyup="SurveyBuild.saveLevel1Attr(this,\'txt\')" value="" oncontextmenu="return false;" ondragenter="return false" class="option-txt"></td>';
		if(data.classname == "QuantifyQu"){
			//量表题加权重
			tr += '<td><input type="text" onkeyup="SurveyBuild.saveLevel1Attr(this,\'weight\')" value="0" oncontextmenu="return false;" ondragenter="return false" onpaste="return false" class="ocode"></td>';	
		}
		tr += '<td><a onclick="SurveyBuild.plusOption(this);return false;" class="text-success" href="javascript:void(0);"><i class="icon-plus-sign"></i> </a><a onclick="SurveyBuild.minusOption(this);return false;" class="text-warning" href="javascript:void(0);"><i class="icon-minus-sign"></i> </a><a href="javascript:void(0);" class="text-info option-move"><i class="icon-move"></i> </a></td>';
		tr += '</tr>';

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
		other = "N",
        data = this._items[k];
        for (var e in data.option) {
            if($.isNumeric(data.option[e].code)){
                var j = 1 * data.option[e].code;
                if (j > l) {
                    l = j
                }
            }
        }
        var a = ++l;

        data.sort = 1;
		if(data.classname == "MultipleTextBox"){other="Y"};
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
					other: other,
					weight: 0,
					othervalue :'',
					checked:'N'
				}
			}
		}

        data.option = g;
        var d = $("#build-right").height();
        var tr = "";
        tr += '<tr class="read-radio" data-id="' + k + '-' + n + '">';

		//表格单选或表格多选 以及多行文本框没有默认和其他选项
		if(data.classname != "GridSingleChoice" && data.classname != "GridMultipleChoice" &&data.classname!="MultipleTextBox"){
			//默认
			tr += '<td><input type="checkbox" onchange="$(\'.defaultval\').not(this).prop(\'checked\',false);SurveyBuild.saveLevel1Attr(this,\'defaultval\')" class="defaultval" value="1"></td>';
			//其他
			tr += '<td><input type="checkbox" onchange="$(\'.other\').not(this).prop(\'checked\',false);SurveyBuild.saveLevel1Attr(this,\'other\');" class="other" value=""></td>';
		}

		//值
		tr += '<td><input type="text" onkeyup="SurveyBuild.saveLevel1Attr(this,\'code\')" value="' + a + '" oncontextmenu="return false;" ondragenter="return false" onpaste="return false" class="ocode"></td>';
        //描述
        if(data.classname=="RadioBoxQu"||data.classname=="MultipleChoiceQu"){
        tr += '<td><input type="text" onkeyup="SurveyBuild.saveLevel1Attr(this,\'txt\')" value="" oncontextmenu="return false;" ondragenter="return false" class="option-qutxt"></td>';
        }else{
        tr += '<td><input type="text" onkeyup="SurveyBuild.saveLevel1Attr(this,\'txt\')" value="" oncontextmenu="return false;" ondragenter="return false" class="option-txt"></td>';
        }
       //单选量表题和多选量表题  增加了分值一列
        if(data.classname=="RadioBoxQu"||data.classname=="MultipleChoiceQu"){
         tr += '<td><input type="text" onkeyup="SurveyBuild.saveLevel1Attr(this,\'weight\')" value="" oncontextmenu="return false;" ondragenter="return false" onpaste="return false" class="ocode"></td>';
        }
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
            data = this._items[f];

            if (data.option.hasOwnProperty(d)) {
                $(e).parents("tr").remove();
				if(data.classname != "GridSingleChoice" && data.classname != "GridMultipleChoice" && data.classname != "MultipleTextBox"){
					$("#o" + d).remove();
					delete data.option[d]
				}else{
					//表格单选或表格多选
					delete data.option[d];
					$(this._html(f)).find(".question-answer").replaceAll("#question-box li[data_id='" + f + "'] .question-answer");
				}
            } else {
                this._error("非法操作")
            }
        }
    },
	//添加子问题选项---zhanglang
	plusChildQuestion: function(e) {
		this.is_edit = true;
		var m = $(e).parents("tr").attr("data-id").split("-"),
		k = m[0],
		b = m[1],
		l = 0,
		n = "S" + ( + new Date()),
		g = {},
		h = 0,
		data = this._items[k];

		//生成唯一编号code
		for (var i in data.child) {
			if ($.isNumeric(data.child[i].sqCode)) {
				var j = 1 * data.child[i].sqCode;
				if (j > l) {
					l = j
				}
			}
		}
		var a = ++l;

		for (var i in data.child) {
			h++;
			g[i] = cloneObj(data.child[i]);
			g[i].orderby = h;
			if (i == b) {++h;
				g[n] = {
					sqCode: a,
					question: "",
					shortDesc: "",
					orderby: h,
					weight: 0,
					value: []
				}
			}
		}
		data.child = g;

		var tr = "";
		tr += '<tr class="read-radio" data-id="' + k + '-' + n + '">';
		//值
		tr += '<td><input type="text" onkeyup="SurveyBuild.saveChildAttr(this,\'sqCode\')" value="' + a + '" oncontextmenu="return false;" ondragenter="return false" onpaste="return false" class="ocode"></td>';
		//描述
		tr += '<td><input type="text" onkeyup="SurveyBuild.saveChildAttr(this,\'question\')" oncontextmenu="return false;" ondragenter="return false" class="option-txt"></td>';
		//简称
		tr += '<td><input type="text" onkeyup="SurveyBuild.saveChildAttr(this,\'shortDesc\')" oncontextmenu="return false;" ondragenter="return false" class="option-txt"></td>';
		//操作
		tr += '<td><a onclick="SurveyBuild.plusChildQuestion(this);return false;" class="text-success" href="javascript:void(0);"><i class="icon-plus-sign"></i> </a><a onclick="SurveyBuild.minusChildQuestion(this);return false;" class="text-warning" href="javascript:void(0);"><i class="icon-minus-sign"></i> </a><a href="javascript:void(0);" class="text-info option-move"><i class="icon-move"></i> </a></td>';
		tr += '</tr>';

		$(e).parents("tr").after(tr);
		$(this._html(k)).find(".question-answer").replaceAll("#question-box li[data_id='" + k + "'] .question-answer");
	},

	//删除子问题选项----zhanglang
    minusChildQuestion: function(e) {
        this.is_edit = true;
        if ($(e).parents("tr").siblings().length == 0) {
            this._error("至少要有一个选项")
        } else {
            var c = $(e).parents("tr").attr("data-id").split("-"),
            f = c[0],
            d = c[1], data = this._items[f];
            if (data.child.hasOwnProperty(d)) {
                $(e).parents("tr").remove();
                $("#sq" + d).closest("tr").remove();
				delete data.child[d]
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
        var a = {},b = true,d = "PageNav",c = 0,pageno = 0;
        $("#question-box>li").each(function(f) {
            var g = $(this),h = g.attr("data_id"),e = SurveyBuild._items[h]["classname"];
            e == "PageNav" && ++c && ++pageno;
            SurveyBuild._items[h]["pageno"] = pageno;
            if (d == "PageNav" && e == "PageNav" && c != 1) {
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
        if(SurveyBuild._use && SurveyBuild._use == "TPL"){
            var params = '{"ComID":"TZ_ZXDC_MBGL_COM","PageID":"TZ_ZXDC_EDIT_STD","OperateType":"U","comParams":{"update":[{"tid":"' + SurveyBuild._tid + '","data":' + $.toJSON(SurveyBuild._data) + '}]}}';
        }else{
            var params = '{"ComID":"TZ_ZXDC_WJGL_COM","PageID":"TZ_ZXDC_EDIT_STD","OperateType":"U","comParams":{"update":[{"tid":"' + SurveyBuild._tid + '","data":' + $.toJSON(SurveyBuild._data) + '}]}}';
        }

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
                    $("#build-save button[id=save]").html('保存<span style="font-size:12px;">(建议5分钟一次)</span>');
                    if (f.state.errcode == 0) {
                        if(f.comContent.alterMsg != ""){
                            alert(f.comContent.alterMsg);
                        }
                        noteing("保存成功");
                        SurveyBuild.is_edit = false;
                        var e = $("#question-box>li.active").index() - 1;
                        if (isPreview) {
                            if(SurveyBuild._use && SurveyBuild._use == "TPL"){
                                var tzParams = '?tzParams='+encodeURIComponent('{"ComID":"TZ_ZXDC_WJGL_COM","PageID":"TZ_ZXDC_VIEW_STD","OperateType":"HTML","comParams":{"TYPE":"TPL","SURVEY_ID":"' + SurveyBuild._tid + '"}}')
                            }else{
                                var tzParams = '?tzParams='+encodeURIComponent('{"ComID":"TZ_ZXDC_WJGL_COM","PageID":"TZ_ZXDC_VIEW_STD","OperateType":"HTML","comParams":{"TYPE":"SURVEY","SURVEY_ID":"' + SurveyBuild._tid + '"}}')
                            }
                            var url = SurveyBuild.tzGeneralURL + tzParams;
                            var newWin = window.open('about:blank');
                            newWin.location.href = url;
                        }
                    } else {
                        noteing("保存失败", 2)
                    }
                }
            });
            return true
        }
    },
    init: function(a) {
        $("#question-edit").on("keydown", "input:text",function(d) {
            if (d.which == 9) {
                var b = $(":text").length,
                c = $(":text").index(this);
                c = ++c == b ? 0 : c;
                $(":text").eq(c).focus().select();
                return false
            }
        });
        $("#question-type-box li.move").draggable({
            connectToSortable: "#question-box",
            addClasses: false,
            appendTo: "#build-box",
            snapTolerance: 300,
            revert: "invalid",
            helper: function() {
                return $('<div class="draggable-holder">' + $(this).html() + "</div>")
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
                SurveyBuild.add(ui.item.attr("data-classname"), 1);
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

        this._items = this._data["items"] ?this._data["items"]:{};
        $("#question-edit").on("keypress", ".qcode,.ocode,.ccode",function(c) {
            var b = c.which;
            return b == 8 || b == 127 || b == 0 || b == 95 || (b >= 48 && b <= 57) || (b >= 65 && b <= 90) || (b >= 97 && b <= 122)
        });
        $("#question-edit").on("keypress", ".datemax,.datemin,.timertime",function(c) {
            var b = c.which;
            //console.log(c.which + " : " + String.fromCharCode(c.which));
            return b == 8 || b == 127 || b == 0 || b >= 48 && b <= 57
        });
        SurveyBuild._tid = a;

        if (SurveyBuild._items) {
            SurveyBuild._load();
            loaded($("#question-wrap"))
        }
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

            component._init && component._init.call(component, d);

            if (me.is_edit_moda) {
                var $itemHtml = me._html(d);
                $("#question-box").append($itemHtml);

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
                conlose.log(_c);
                var _eventbind = me._items[d]._eventbind;
                if (_eventbind && typeof _eventbind == "function") {
                    _eventbind(me._items[d]);
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
        /*加载完成后默认第一个问题 By  WRL 2015-08-21 BEGIN*/
        var firstChild = $("#question-box>li").eq(0);
        firstChild.click();
        /*加载完成后默认第一个问题 By  WRL 2015-08-21 END*/
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
        c.popover("destroy");
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
    saveApp: function() {
        if ($("#app_save").length > 0) {
            $("#app_save").click();
        }
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
    /*模板设置---高级设置 END*/
    reFocus:function(id){
        $("#"+id).trigger('blur');
    }
};
var MsgSet = {};
