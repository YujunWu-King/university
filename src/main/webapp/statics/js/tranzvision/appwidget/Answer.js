var SurveyBuild = {
    _tid: 0,
    _data: {},
    _items: {},
    _events: {},
    _count: 0,
    BMB_LANG:'',
    tzGeneralURL:"",
    refLetterId:"",
    appManager:"",
    _qid: [],
    _oid: [],
    _preg: {"email": {"name": "邮箱","regExp": "/^[\\w-]+(\\.[\\w-]+)*@[\\w-]+(\\.[\\w-]+)+$/"},"telphone": {"name": "手机","regExp": "/^1\\d{10}$/"},"idcard": {"name": "身份证号","regExp": "/(^\\d{15}$)|(^\\d{18}$)|(^\\d{17}(\\d|X|x)$)/"},"url": {"name": "网址URL","regExp": "/(http|ftp|https):\\/\\/[\\w\\-_]+(\\.[\\w\\-_]+)+([\\w\\-\\.,@?^=%&amp;:/~\\+#]*[\\w\\-\\@?^=%&amp;/~\\+#])?/"}},
    is_edit: false,
    is_edit_moda: true,
    _alph: ['A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'],
    _color: ["#FFFFFF", "#5D762A", "#FF0000", "#800080", "#008000", "#855B00", "#000000", "#FFFF00", "#990000", "#FFA500", "#E4E4E4", "#D2691E", "#1EDDFF", "#FFFFB1", "#98FB98", "#BDB76B", "#666666", "#4B0082", "#041690", "#FFB6C1", "#DDA0DD", "#0000FF", "url(/assets/img/mixed.png);", "url(/assets/img/trans.png);"],
    comClass: {},//控件实例类
    _components: {},
    _componentConfig: [],//控件后台配置信息
    _componentIndex: [],//
    _componentLoadedIndex: [],
    _baseRules: [],
    _readonly:false,
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
    /* 前台不需要这个方法，展示注释 by WRL 2015/11/13
    pageTo: function(el) {
        var h = $(el).attr("data_id");
        h = $("#question-box li[data_id='" + h + "']");
        window.scrollTo(0, h.offset().top - 70);
        $(el).siblings().removeClass("tabNav_c").addClass("tabNav");
        $(el).addClass("tabNav_c");
        return false;
    },
    */
    saveTjx_fj: function() {
		
		var _tz_app_ins_id=SurveyBuild.appInsId;
		var _Url = SurveyBuild.tzGeneralURL + "?tzParams=";
		var param = "{'ComID':'TZ_GD_TJX_COM','PageID':'TZ_SEND_REF_STD','OperateType':'DELETE','comParams':{'rec_app_ins_id':'"+_tz_app_ins_id+"'}}";
		var _email_tx = $("#yincang_tx").val();
		//$("#"+data.itemId+child[cins].itemId+"Attch").text(obj.msg.fileName);
		$.ajax({
			type: "post",
			url: _Url + encodeURIComponent(param),
			dataType: "json",
			async: false,
			success: function(result){
				if (result.comContent=="SUCCESS"){
				}else {
					//alert(result.comContent);
				}
			}
		});    
		
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
    /** 前台不需要这个方法，展示注释 by WRL 2015/11/13
    saveAttr: function(el, attrName) {
        if (!el || !attrName) return;

        var instanceId = $("#question-edit").attr("data_id");
        var val = this._getAttrVal(el);
        var data = {};
        if (SurveyBuild.isDHContainer) {
            data = this._items[SurveyBuild.currentDHID]["children"][instanceId];
        } else {
            data = this._items[instanceId];
        }

        data[attrName] = val;
        var rules = data["rules"];
        var _rules = this._componentConfig[$.inArray(data["classname"], this._componentIndex)]["rules"];
        var $activeLi = $("#question-box li.active");
        if (attrName == "title") {
            if(data["classname"]=='TextExplain')
            {
                $activeLi.find(".question-answer").html(val);
            } else
            {
                $activeLi.find(".question-question").first().html(val);
            }

        } else if (attrName == "suffix") {
            $activeLi.find(".suffix").html(val);
        } else if (attrName == "format") {
            $activeLi.find(".format").removeClass().addClass("format format" + val)
        } else if (attrName == "wzsm") {
            //文字说明
            $activeLi.find(".question-answer").html(val);
        } else if(attrName == "dateformate"){
            var format="";
            if (val == "1"){
                format = "MM-DD-YYYY";
            } else if(val == "2"){
                format = "MM/DD/YYYY";
            } else if(val == "3"){
                format = "YYYY-MM-DD";
            } else if(val == "4"){
                format = "YYYY/MM/DD";
            }
            $activeLi.find(".question-answer").find("#"+instanceId+"Format").html(format);
        } else if (attrName == "itemMs") {
            if (val) {
                $activeLi.find(".edu_exper_desc").css("display","");
            } else {
                $activeLi.find(".edu_exper_desc").css("display","none");
            }
            $activeLi.find(".edu_exper_desc").html(val);
        } else if (attrName == "itemId") {
            //$activeLi.find(".question-code").html(val);
            //设置 必填、字符串长度、数值取值校验的联动显示
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
                        $("#is_checknumsize").prop("checked", $(el).prop("checked"));
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
                    }else {
                        if (attrName == "toCheck") {
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
                                $("#is_toCheck").prop("checked", $(el).prop("checked"));
                            }
                        }else{
                            if (attrName == "maxLines") {
                                var linesNo = [];
                                for(var i = 1; i < val; i++){
                                    linesNo.push(i);
                                }
                                data["linesNo"] = linesNo;
                            }

                        }
                    }
                }
            }
        }
        //$activeLi.html(SurveyBuild._html(instanceId))
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
    saveLevel1Attr2: function(el, attrNameLevel1,recommend) {
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
            if (attrNameLevel1=="useby")
            {
                if (val=="Y")
                {
                    $("#" + $tr.attr("data-id")+"_1").css("display","block");
                }else{
                    $("#" + $tr.attr("data-id")+"_1").css("display","none");
                }

            }
        } catch(e) {};
        this.is_edit = true;
    },
    checkAttrVal: function() {

        var $liActive = $("#question-box li.active");
        var checkBz = true;
        var tipObj;
        if ($liActive.length == 0) {
            return checkBz
        }
        var d = $liActive.attr("data_id");
        if (!SurveyBuild.isDHContainer && this._items[d] && (this._items[d]["classname"] == "Page" || this._items[d]["classname"] == "Separator" )) {
            return checkBz;
        }
        var $edit_box = $("#question-edit");
        var $itemId = $edit_box.find(".edit_itemId");
        var $itemName = $edit_box.find(".edit_itemName");
        var has = false,data;
        if (SurveyBuild.isDHContainer) {
            data = this._items[SurveyBuild.currentDHID]["children"];
        } else {
            data = this._items;
        }

        if (!$itemId.val()) {
            checkBz = false;
            msg = "信息项编号不能为空";
            tipObj = $itemId;
        } else if(!/^\w+$/g.test($itemId.val())){
            //信息项编号只允许字母、数字、下划线
            checkBz = false;
            msg = "信息项编号只允许字母数字以及下划线";
            tipObj = $itemId;
        } else if (!$itemName.val().length > 30) {
            checkBz = false;
            msg = "信息项编号长度不能超过30";
            tipObj = $itemId;
        } else if (!$itemName.val()) {
            checkBz = false;
            msg = "信息项名称不能为空";
            tipObj = $itemName;
        } else {
            //信息项编号是否重复
            for (var instaceId in data) {
                if (d != instaceId) {
                    if (data[instaceId]["itemId"] == $itemId.val()) {
                        has = true;
                    }
                }
            }
            if (has) {
                checkBz = false;
                msg = "信息项编号已存在";
                tipObj = $itemId;
            } else {
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
                        msg = "可选值名称重复";
                        tipObj = $("#question-edit tr[data-id='" + trId + "']");
                    }
                }
            }
        }

        $(".popover").hide();
        if (checkBz == false) {
            this.fail(tipObj, msg);
            return checkBz;
        };

        if (data.hasOwnProperty(d) && data[d]._validatorAttr) {
            return data[d]._validatorAttr(data[d]);
        } else {
            return checkBz;
        }
    },
    */
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
    /** 前台不需要这个方法，展示注释 by WRL 2015/11/13
    DynamicBindValCallBack :function(td){
        var regId=$(td).closest('tr').attr('reg-id');
        redId ="{%BIND{"+regId+"}}";
        $("#question-edit #defaultval").val(redId);
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

                        pageHtml += '<div id="modal-question-advanced" class="modal-body">';
                        pageHtml += '	<fieldset>';
                        pageHtml += '		<table id="table-dynbindval" class="table table-hover table-bordered">';
                        pageHtml += '			<tr><th width="50px">注册项ID</th><th width="100px">名称</th><th>是否启用</th><th>注册项类型</th><th>是否必填</th><th>绑定</th></tr>';
                        $.each(_items, function(index, item) {
                            pageHtml += '<tr reg-id="' + item.regId+ '">';
                            pageHtml += '<td>'+item.regId+'</td>' ;
                            pageHtml += '<td>'+item.regName+'</td>' ;
                            pageHtml += '<td><input type="checkbox" readonly="rdadonly" '+(item.isEnable?'checked="checked"':"")+'</td>' ;
                            pageHtml += '<td>'+getRegFieldTypeDesc (item.regFieldType)+'</td>' ;
                            pageHtml += '<td><input type="checkbox" readonly="rdadonly" '+(item.isRequired?'checked="checked"':"")+'</td>' ;
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
    */
    /*控件属性--高级属性--End*/

    /* 前台不需要这个方法，展示注释 by WRL 2015/11/13
    //推荐信控件选择推荐信模板和推荐信发送邮件模板(中文)
    RulesZHS: function(el) {
        //var _url=document.location.href;
         //var _url=_url.split("?");
         //var _url1=_url[1].split("=");
         //var tz_app_id=_url1[1];
        var tz_app_id=SurveyBuild._tid;
        var _url=SurveyBuild.tzGeneralURL;
        var _bmb_xz="";
        var _email_desc="";
        var _tjx_qy="";
        var ruleSetPage = "";
        $.ajax({
            type: "post",
            url: _url+"?tzParams={'ComID':'TZ_GD_TJX_COM','PageID':'TZ_GD_TJX_PZ_STD','OperateType':'QF','comParams':{'tz_app_id':'"+tz_app_id+"','tz_language':'C'}}",
            dataType: "json",
            async: false,
            success: function(result){
                _bmb_xz=result.comContent.formData.tjx_xz;
                _email_desc=result.comContent.formData.email_desc;
                _tjx_qy=result.comContent.formData.tjx_qy;
            }
        });
        ruleSetPage = '<div class="modal-header"><h4>中文推荐信设置</h4></div>';
        //是否启用中文推荐信
        ruleSetPage += '<div class="edit_item_warp" style="margin-top:15px;margin-left:20px;">';
        ruleSetPage += '<input type="checkbox" ' + (_tjx_qy == "Y" ? "checked='checked'" : "") + ' id="zhs_qy" onclick="SurveyBuild.tjx_zhschange()"/> 启用中文推荐信';
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
            url: _url+"?tzParams={'ComID':'TZ_GD_TJX_COM','PageID':'TZ_GD_TJX_PZ_STD','OperateType':'U','comParams':{'update':[{'tz_app_id':'"+id+"','tz_language':'C','bmb_id':'"+_bmb_id+"','email_desc':'"+_email_desc+"','tjx_qy':'"+_zhs_qy+"'}]}}",
            dataType: "json",
            async: false,
            success: function(result){
                $.fancybox.close();
            }
        });
    },
    //推荐信控件选择推荐信模板和推荐信发送邮件模板(英文)
    RulesENG: function(el) {
        //var _url=document.location.href;
        //var _url=_url.split("?");
        //var _url1=_url[1].split("=");
        //var tz_app_id=_url1[1];
        var tz_app_id=SurveyBuild._tid;
        var _url=SurveyBuild.tzGeneralURL;
        var _bmb_xz="";
        var _email_desc="";
        var _tjx_qy="";
        var ruleSetPage = "";
        $.ajax({
            type: "post",
            url: _url+"?tzParams={'ComID':'TZ_GD_TJX_COM','PageID':'TZ_GD_TJX_PZ_STD','OperateType':'QF','comParams':{'tz_app_id':'"+tz_app_id+"','tz_language':'E'}}",
            dataType: "json",
            async: false,
            success: function(result){
                _bmb_xz=result.comContent.formData.tjx_xz;
                _email_desc=result.comContent.formData.email_desc;
                _tjx_qy=result.comContent.formData.tjx_qy;
            }
        });
        ruleSetPage = '<div class="modal-header"><h4>英文推荐信设置</h4></div>';
        //是否启用英文推荐信
        ruleSetPage += '<div class="edit_item_warp" style="margin-top:15px;margin-left:20px;">';
        ruleSetPage += '<input type="checkbox" ' + (_tjx_qy == "Y" ? "checked='checked'" : "") + ' id="eng_qy" onclick="SurveyBuild.tjx_engchange()"/> 启用英文推荐信';
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
            url: _url+"?tzParams={'ComID':'TZ_GD_TJX_COM','PageID':'TZ_GD_TJX_PZ_STD','OperateType':'U','comParams':{'update':[{'tz_app_id':'"+id+"','tz_language':'E','bmb_id':'"+_bmb_id+"','email_desc':'"+_email_desc+"','tjx_qy':'"+_eng_qy+"'}]}}",
            dataType: "json",
            async: false,
            success: function(result){
                $.fancybox.close();
            }
        });
    },
    */
    add: function(f, a) {
        $("#y").show();

        this.is_edit = true;
        this._count == 0 && $("#question-new").css("display", "none");

        SurveyBuild.isDHContainer != true && ++this._count;
        var me = this;
        var callback = function(f) {
            var d = "A" + ( + new Date());
            var _childrenLen = 0;
            if (SurveyBuild.isDHContainer) {
                $.each(me._items[SurveyBuild.currentDHID]["children"],
                    function(i) {
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
                $("#question-box>li").each(function(e) {
                    if ($(this).attr("data-classname")) {
                        b = e;
                        $(this).remove();
                        return
                    }
                });
                _itemHtml = me._html(d);
                if (SurveyBuild.isDHContainer == true) {
                    $(_itemHtml).appendTo($(a)).click();
                } else {
                    if (b > 0) {
                        $(_itemHtml).insertAfter($("#question-box>li").eq(b - 1)).click();
                    } else {
                        $(_itemHtml).prependTo($("#question-box")).click();
                    }
                }

            } else {
                $("#question-box").append(me._html(d));
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
            var _eventbindEditor = component._eventbindEditor;
            if (_eventbindEditor && typeof _eventbindEditor == "function") {
                _eventbindEditor(component);
            }
        };
        this.loadScript(f, callback);
        //f == "Page" && this._initTab();    // 模板设置页面，触发初始化导航菜单（是否可以删除） by WRL 2015/08/04

    },
    /** 前台不需要这个方法，展示注释 by WRL 2015/08/13
    changeDelChl: function(a) {
        if (a) {
            for (var b in a) {
                if (!a[b].hasOwnProperty("add")) {
                    this._qid.push(a[b].qid)
                }
            }
        }
    },
     */
    /** 前台不需要这个方法，展示注释 by WRL 2015/08/13
    changeDelOpt: function(b) {
        if (b) {
            for (var a in b) {
                if (!b[a].hasOwnProperty("add")) {
                    this._oid.push(b[a].oid)
                }
            }
        }
    },
     */
    /** 前台不需要这个方法，展示注释 by WRL 2015/08/13
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
        if (this._items[h] && this._items[h]["classname"] == "Page") {
            this.pageTo($("#tabNav div[data_id='" + h + "']"));
        }
        event && event.preventDefault && event.preventDefault();
        event && event.stopPropagation && event.stopPropagation();
        SurveyBuild._optionMove();
        return false;
    },
     */
    /** 前台不需要这个方法，展示注释 by WRL 2015/08/13
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
        //拖拽元素，更新orderby的值
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
    */
    /** 前台不需要这个方法，展示注释 by WRL 2015/08/13
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
        e += '<span class="edit_item_label">名称：</span>';
        e += '<input type="text" class="medium edit_itemName" onkeyup="SurveyBuild.saveAttr(this,\'itemName\')" value="' + data.itemName + '"/>';

        e += '<div id="editor-bar" style=" margin-top:20px; width:300px;"><button class="btn btn-primary btn-mini" onclick="SurveyBuild.editor(\'' + data.instanceId + '\')"><i class="icon-font"></i> 编辑文字或插入图片</button></div>';
        e += '<textarea id="'+ data.instanceId +'" class="question-text" style="height:180px" onkeyup="SurveyBuild.saveAttr(this,\'title\')">' + data.title + '</textarea>';

        e = (data["_CommonField"] == "Y" ? e: "") + data._edit(data);

        e += '<div class="edit_item_warp" style="text-align: right;">';
        e += '  <button class="btn btn-small" onclick="SurveyBuild.remove(event,\'' + d + '\')"><i class="icon-trash"></i>删除</button>';
        e += '</div>';
        return e;
    },
    */
    /** 前台不需要这个方法，展示注释 by WRL 2015/08/13
    uploadSave: function(c) {
        this.is_edit = true;
        var b = "A" + ( + new Date()),
            a = 0;
        $("#option-box .ocode").each(function() {
            var d = parseInt($(this).val());
            if (d > a) {
                a = d
            }
        });
        if (this._items[c].option instanceof Array) {
            this._items[c].option = {}
        }
        $("#modal-question-upload a.active").each(function(d) {++a;
            SurveyBuild._items[c].option[b + "_" + d] = {
                qid: b,
                add: 1,
                code: a,
                txt: "",
                orderby: a,
                is_rand: 0,
                settings: {
                    pic: $(this).find("img").attr("src")
                }
            }
        });
        $(this._html(c)).find(".question-answer").replaceAll("#q" + c + " .question-answer");
        $("#question-edit").html(this._edit(c));
        $.fancybox.close()
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
     */
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

        c = '<li data_id="' + d + '" id="q' + d + '" onclick="SurveyBuild.edit(this,event)" style="' + _style + '">';
        if (_currentData["_CommonField"] == "Y") {
            c += '<div class="question-title"><b class="question-code">' + _currentData.orderby + '.</b><div class="question-question">' + _currentData.title + '</div></div>';
        }
        //通过实例对象中的类名，找到类并调用_gethtml方法
        c += _currentData._getHtml(_currentData) || "";
        c += '<div class="question-action"><a class="build-icon-minus" title="删除" onclick="return SurveyBuild.remove(event,\'' + d + '\')"></a><a class="build-icon-copy" title="复制" onclick="SurveyBuild.copy(event,\'' + d + '\')"></a><i class="build-icon-arrow"></i></div></li>'
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
    /** 前台不需要这个方法，展示注释 by WRL 2015/11/13
    allRand: function(b, d) {
        var a = d ? "crand": "rand";
        $("." + a).prop("checked", $(b).prop("checked"))
    },
    allMandatory: function(a) {
        $(".mandatory").prop("checked", $(a).prop("checked"))
    },
     */
    /** 前台不需要这个方法，展示注释 by WRL 2015/08/13
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
    */
    /** 前台不需要这个方法，展示注释 by WRL 2015/08/13
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
    */
    /** 前台不需要这个方法，展示注释 by WRL 2015/08/13
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
     */
    /** 前台不需要这个方法，展示注释 by WRL 2015/08/13
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
     */
    /** 前台不需要这个方法，展示注释 by WRL 2015/08/13
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
    */
    /** 前台不需要这个方法，展示注释 by WRL 2015/08/13
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

        a += '<div id="modal-question-option" class="modal-body">';
        a += '	<textarea id="' + e + '-batch" class="option-text">';

        //option列表
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
     */
    /** 前台不需要这个方法，展示注释 by WRL 2015/08/13
    plusChild: function(f) {
        this.is_edit = true;
        var n = $(f).parents("tr").attr("id").split("-"),
            k = n[0],
            h = n[1],
            l = 0,
            o = "A" + ( + new Date()),
            b = {},
            g = 0;
        var d = $("#build-right").height();
        $("#build-right").height(d + 39);
        $(f).parents("tr").after('<tr id="' + k + "-" + o + '"><td><input type="text" value="" class="ccode" oncontextmenu="return false;" maxlength="8" ondragenter="return false" onpaste="return false" onkeyup="SurveyBuild.saveCCode(this)"></td><td><input class="option-txt" value="" onkeyup="SurveyBuild.saveChild(this)" type="text"></td><td><a href="javascript:void(0);" class="text-success" onclick="SurveyBuild.plusChild(this)"><i class="icon-plus-sign"></i></a>&nbsp;<a href="javascript:void(0);" onclick="SurveyBuild.minusChild(this)" class="text-warning"><i class="icon-minus-sign"></i></a>&nbsp;<a href="javascript:void(0);" class="text-info"><i class="icon-move"></i></a></td></tr>');
        this._items[k].sort = 1;
        var m = "";
        for (var e in this._items[k].child) {++g;
            b[e] = cloneObj(this._items[k].child[e]);
            b[e].orderby = g;
            m += '<option id="first' + e + '" value="' + e + '">' + b[e].question + "</option>";
            if (e == h) {++g;
                b[o] = {
                    add: 1,
                    parent_id: k,
                    code: "",
                    question: "",
                    orderby: g,
                    is_rand: 0,
                    settings: {
                        mandatory: 1,
                        other: 0,
                        fixedwidth: ""
                    }
                };
                m += '<option id="first' + o + '" value="' + o + '" selected></option>'
            }
        }
        this._items[k].child = b;
        if (this._items[k].qtype == 17) {
            $("#selectTitle").html(m);
            var l = 1,
                a = 1;
            for (var e in this._items[k].option) {
                var j = 1 * this._items[k].option[e].code;
                a = 1 + this._items[k].option[e].orderby;
                if (j > l) {
                    l = j
                }
            }
            var p = "o" + o;
            this._items[k].option[p] = {
                qid: k,
                add: 1,
                code: ++l,
                txt: "选项1",
                orderby: a,
                is_rand: 0,
                settings: {
                    other: 0,
                    cid: o
                }
            };
            this._changeTitle(k, o)
        }
        $(this._html(k)).find(".question-answer").replaceAll("#q" + k + " .question-answer")
    },
    /*
    minusChild: function(f) {
        this.is_edit = true;
        if ($(f).parents("tr").siblings().length == 0) {
            this._error("至少要有一个子问题")
        } else {
            var e = $(f).parents("tr").attr("id").split("-"),
                g = e[0],
                h = e[1];
            if (this._items[g].child.hasOwnProperty(h)) {
                var b = $("#build-right").height();
                $("#build-right").height(b - 39);
                $(f).parents("tr").remove();
                if (this._items[g].settings.direction == "Y") {
                    var a = $("#q" + g + " tr th#c" + h).index();
                    $("#q" + g + " tr th#c" + h).remove();
                    $("#q" + g + " tr").each(function() {
                        $(this).find("td").eq(a).remove()
                    })
                } else {
                    $("#c" + h).parent().remove()
                }
                if (!this._items[g].child[h].hasOwnProperty("add")) {
                    this._qid.push(this._items[g].child[h].qid)
                }
                delete this._items[g].child[h];
                if (this._items[g].qtype == 17) {
                    $("#first" + h).remove();
                    var d = this._items[g].option;
                    for (var c in d) {
                        if ($option.settings["cid"] == h || "C" + d[c].settings.cid == h) {
                            delete this._items[g].option[c]
                        }
                    }
                }
            } else {
                this._error("非法操作")
            }
        }
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
        var a = ++l;
        data.sort = 1;
        for (var e in data.option) {
            h++;
            g[e] = cloneObj(data.option[e]);
            if (e == b) {++h;
                g[n] = {
                    code: h,
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
            tr += '<td><input type="text" onkeyup="SurveyBuild.saveLevel1Attr(this,\'code\')" value="' + h + '" oncontextmenu="return false;" ondragenter="return false" onpaste="return false" class="ocode"></td>';
            tr += '<td><input type="text" onkeyup="SurveyBuild.saveLevel1Attr(this,\'txt\')" value="" oncontextmenu="return false;" ondragenter="return false" onpaste="return false" class="option-txt"></td>';
            tr += '<td><a onclick="SurveyBuild.plusOption(this,\'autocpl\');return false;" class="text-success" href="javascript:void(0);"><i class="icon-plus-sign"></i> </a><a onclick="SurveyBuild.minusOption(this);return false;" class="text-warning" href="javascript:void(0);"><i class="icon-minus-sign"></i> </a><a href="javascript:void(0);" class="text-info option-move"><i class="icon-move"></i> </a></td>';
            tr += '</tr>';
        }else{
            tr += '<tr class="read-radio" data-id="' + k + '-' + n + '">';
            tr += '<td><input type="checkbox" onchange="$(\'.defaultval\').not(this).prop(\'checked\',false);SurveyBuild.saveLevel1Attr(this,\'defaultval\')" class="defaultval" value="1"></td>';
            tr += '<td><input type="text" onkeyup="SurveyBuild.saveLevel1Attr(this,\'code\')" value="' + h + '" oncontextmenu="return false;" ondragenter="return false" onpaste="return false" class="ocode"></td>';
            tr += '<td><input type="text" onkeyup="SurveyBuild.saveLevel1Attr(this,\'txt\')" value="" oncontextmenu="return false;" ondragenter="return false" onpaste="return false" class="option-txt"></td>';
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
        var a = ++l;
        data.sort = 1;
        for (var e in data.option) {
            h++;
            g[e] = cloneObj(data.option[e]);
            if (e == b) {++h;
                g[n] = {
                    code: h,
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
        tr += '<td><input type="checkbox"  class="other"  value="" oncontextmenu="return false;" ondragenter="return false" onpaste="return false" onchange="$(\'.other\').not(this).prop(\'checked\',false);SurveyBuild.saveLevel1Attr(this,\'other\')></td>';
        //值
        tr += '<td><input type="text" onkeyup="SurveyBuild.saveLevel1Attr(this,\'code\')" oncontextmenu="return false;" ondragenter="return false" onpaste="return false" class="ocode"></td>';

        //描述(重复一列：值！)
        tr += '<td><input type="text" onkeyup="SurveyBuild.saveLevel1Attr(this,\'code\')" value="" oncontextmenu="return false;" ondragenter="return false" onpaste="return false" class="ocode"></td>';

        tr += '<td><input type="text" onkeyup="SurveyBuild.saveLevel1Attr(this,\'txt\')" value="" oncontextmenu="return false;" ondragenter="return false" onpaste="return false" class="option-txt"></td>';
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
                var b = $("#build-right").height();
                //$("#build-right").height(b - 37);
                $(e).parents("tr").remove();
                $("#s" + d).remove();
                delete data.option[d]
            } else {
                this._error("非法操作")
            }
        }
    },
    saveTitle: function(a) {
        this.is_edit = true;
        var c = $(a).attr("data-id"),
            b = $(a).val();
        this._items[c].edit = 1;
        this._items[c].settings.title = b;
        $("#q" + c).find(".title").html(b)
    },
    saveSubTitle: function(a) {
        this.is_edit = true;
        var c = $(a).attr("data-id"),
            b = $(a).val();
        this._items[c].edit = 1;
        this._items[c].settings.subtitle = b;
        $("#q" + c).find(".subtitle").html(b)
    },
    changeTitle: function(a) {
        var c = $(a).attr("data-id"),
            b = $(a).val();
        this._changeTitle(c, b)
    },
    _changeTitle: function(e, d) {
        this.is_edit = true;
        this._items[e].edit = 1;
        var c = this._items[e].option,
            b = "";
        for (var a in c) {
            if (c[a].settings.cid == d || "C" + c[a].settings.cid == d) {
                b += '<tr id="' + e + "-" + a + '"><td><input type="text" class="ocode" onpaste="return false" ondragenter="return false" oncontextmenu="return false;" maxlength="5" value="' + c[a]["code"] + '" onkeyup="SurveyBuild.saveOCode(this)"/></td><td><input type="text" class="option-txt" value="' + htmlentities(c[a]["txt"]) + '" onkeyup="SurveyBuild.saveTxt(this)" /></td><td><a href="javascript:void(0);" class="text-success" onclick="SurveyBuild.plusOption(this);return false;"><i class="icon-plus-sign"></i></a>&nbsp;<a href="javascript:void(0);" class="text-warning" onclick="SurveyBuild.minusOption(this);return false;"><i class="icon-minus-sign"></i></a>&nbsp;<a href="javascript:void(0);" class="text-info option-move"><i class="icon-move"></i></a></td></tr>'
            }
        }
        $("#" + e + "-sub").html(b)
    },
    /*修改问题后，自动保存*/
    /** 前台不需要这个方法，展示注释 by WRL 2015/08/13
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
    */
    /** 前台不需要这个方法，展示注释 by WRL 2015/11/13
    saveBuild: function() {
        if (!this.checkAttrVal()) {
            return
        }
        var a = {},
            b = true,
            d = "Page",
            c = 0,
            pageno = 0;
        $("#question-box>li").each(function(f) {
            var g = $(this),
                h = g.attr("data_id"),
                e = SurveyBuild._items[h]["classname"];
            e == "Page" && ++c && ++pageno;
            SurveyBuild._items[h]["pageno"] = pageno;
            if (d == "Page" && e == "Page" && c != 1) {
                SurveyBuild.fail($(this), "第" + c + "页不能没有问题", "top");
                b = false;
                return false
            } else {
                d = e;
                a[h] = SurveyBuild._items[h]
            }

        });

        SurveyBuild._data['items'] = a;
        var params = '{"ComID":"TZ_ONLINE_REG_COM","PageID":"TZ_ONREG_OTHER_STD","OperateType":"U","comParams":{"update":[{"tid":"' + SurveyBuild._tid + '","data":' + $.toJSON(SurveyBuild._data) + '}]}}';
        if (b) {
            loading($("#build-box"));
            $("#build-save button").text("保存中...");
            $.ajax({
                type: "POST",
                url:SurveyBuild.tzGeneralURL,
                data: {
                    tzParams:params
                },
                dataType: "JSON",
                success: function(f) {
                    loaded($("#build-box"));
                    $("#build-save button").html('保存<span style="font-size:12px;">(建议10分钟一次)</span>');
                    if (f.state.errcode == 0) {
                        noteing("保存成功");
                        SurveyBuild.is_edit = false;
                        var e = $("#question-box>li.active").index() - 1;
                    } else {
                        noteing("保存失败", 2)
                    }
                }
            });
            return true
        }
    },
     */
    /** 前台不需要这个方法，展示注释 by WRL 2015/08/13
    saveEmptyText: function(a) {
        this.is_edit = true;
        var b = $(a).attr("data_id");
        this._items[b].edit = 1;
        this._items[b].emptyText = $(a).val();
    },
    */
    /** 前台不需要这个方法，展示注释 by WRL 2015/11/14
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
            receive: function(f, d) {
                if (SurveyBuild.isDHForTwo != true) {
                    SurveyBuild.isDHContainer = false;
                    SurveyBuild.add(d.item.attr("data-classname"), 1);
                } else {
                    SurveyBuild.isDHForTwo = false;
                    $("#question-box>li").each(function(e) {
                        if ($(this).attr("data-classname")) {
                            $(this).remove();
                            return
                        }
                    });
                }

            },
            update: function(c, b) {
                SurveyBuild.sort()
            }
        });

        window.onbeforeunload = function() {
            if (SurveyBuild.is_edit) {
                return "当前页面有未保存内容\n选择“离开页面” 会丢失当前编辑的内容\n选择“留在此面” 可以继续编辑问题然后保存离开"
            }
        };

        //this._initHelpDesc();     //控件属性描述信息在Answer.js（前台）中暂时注释 by WRL 2015/08/04
        //this._htmlSet();			//模板设置在Answer.js（前台）中暂时注释 by WRL 2015/08/04

        var events = '{"SA_A": "保存事件后","LO_A": "加载事件后","SU_A": "提交事件后"}';
        this._events = JSON.parse(events);
        this._items = this._data["items"];
        $("#question-edit").on("keypress", ".qcode,.ocode,.ccode",
            function(c) {
                var b = c.which;
                return b == 8 || b == 127 || b == 0 || b == 95 || (b >= 48 && b <= 57) || (b >= 65 && b <= 90) || (b >= 97 && b <= 122)
            });
        $("#question-edit").on("keypress", ".datemax,.datemin,.timertime",
            function(c) {
                var b = c.which;
                return b == 8 || b == 127 || b == 0 || b >= 48 && b <= 57
            });
        SurveyBuild._tid = a;

        if (SurveyBuild._items) {
            SurveyBuild._load();
            loaded($("#question-wrap"))
        }
    },
     */
    _load: function (b) {
        this._count = 0;
        var me = this;
        var callback = function (params) {
            var classname = params[0];
            var data = params[1];
            var d = data["instanceId"];
            //data转换成项对应的对象
            var component = me._items[d] = new me.comClass[classname](data);
            if (component.isDoubleLine == "Y" && component.fixedContainer != "Y") {
                $.each(component.children,function (i, rec) {
                    $.each(rec,function (instanceId, obj) {
                        component.children[i][instanceId] = new me.comClass[obj.classname](obj);
                    })
                })
            }
            /* _init方法在后台设计页面用户初始化控件属性，前台无需调用 By WRL @2015-11-14
            component._init && component._init.call(component, d);
            */
            //获取对应信息项的HTML
            data = me._items[d];
            var _c = data._getHtml(data, true);

            if (data["isDoubleLine"] == "Y" && data.children && data.children.length > 0 && data["fixedContainer"] != "Y") {
                var _co = "";
                for (var i = 0; i < data.children.length; i++) {
                    _co += me._addOneRec(data.children, i);
                    this.ArrShift(data.children[i],d);
                }
                _c = $("<div class='dhcontainer page" + data.pageno + "' data-instancid='" + data.instanceId + "'>" + _c + "</div>").find(".main_inner_content").prepend(_co).parents('.dhcontainer');
            } else if (data["fixedContainer"] && data["fixedContainer"] == "Y") {
                //固定多行控件
                _c = "<div class='dhcontainer page" + data.pageno + "' data-instancid='" + data.instanceId + "'>" + (_c || "") + "</div>";
            } else {
                //单行控件
                _c = "<div class='page" + data.pageno + "' data-instancid='" + data.instanceId + "'>" + (_c || "") + "</div>";
            }
            $("#main_list").append(_c);


            //控件绑定事件(固定多行容器存在问题)
            var _eventbind = data._eventbind;
            if (_eventbind && typeof _eventbind == "function") {
                _eventbind(data);
            }
            if (data["isDoubleLine"] == "Y" && data.children && data.children.length > 0) {
                $.each(data.children,function (i, child) {
                    $.each(child,function (instanceId, obj) {
                        if (obj._eventbind && typeof obj._eventbind == "function") {
                            obj._eventbind(obj);
                        }
                        if(data["fixedContainer"] != "Y" && obj.classname == "CheckBox" && obj.hasOwnProperty("rules")){
                            $.each(obj["rules"],function(classname, classObj) {
                                if ($.inArray(classname, me._baseRules) == -1 && obj["rules"][classname]["isEnable"] == "Y") {
                                    var _ruleClass = ValidationRules[classname];
                                    if (_ruleClass && _ruleClass._eventList && $.trim(classObj["messages"])!="") {
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
                            });
                        }
                    });
                });
            }

            //如果是多行控件，判断是否可以继续添加行信息
            if (data.children && data.maxLines >= 1 && data.maxLines == data.children.length) {
                $(_c).find(".addnextbtn").hide();
            }
        };

        /*加载信息项对应的JavaScript文件以及事件绑定 Begin*/
        $.each(this._items, function (c) {
            ++me._count;
            d = true;
            var item = me._items[c];
            if (item.isDoubleLine == "Y") {
                if (!$.isArray(item.children)) {
                    item.children = [item.children];
                }
                $.each(item.children, function (i, rec) {
                    $.each(rec, function (instanceId, obj) {
                        me.loadScript(obj["classname"]);
                    });
                })
            }
            me.loadScript(item["classname"], callback, [item["classname"], item]);
        });
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
                if (item.hasOwnProperty("rules") && item.isSingleLine != "Y") {
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
            a = "right"
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
				var tarItemId = "";
                if(obj["isSingleLine"]&& obj["isSingleLine"] == "Y"){
                    tarItemId = obj["children"][0]["itemId"];
                }else{
                    tarItemId = obj["itemId"];
                }
				if(tarItemId && tarItemId.substr(-2,1) == "_"){
					lineno = parseInt(tarItemId.substr(-1));
					
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
					if(ch.hasOwnProperty("isHidden")){
						ch["isHidden"] = "N";
					}
                });
            }else{
                //_fc[ins]["itemId"] += "_" + _children.length;
                _fc[ins]["itemId"] += "_" + suffix;
				if(obj.hasOwnProperty("isHidden")){
					obj["isHidden"] = "N";
				}
				//附件上传,清空附件信息
				if (_fc[ins]["classname"]=="AttachmentUpload" || _fc[ins]["classname"]=="imagesUpload"){
                    if(_fc[ins].hasOwnProperty("children")){
                        var _fileChildren = _fc[ins].children;
                        if (_fileChildren.length>1){
                            _fileChildren.splice(1,_fileChildren.length-1);
                        }
                        _fileChildren[0].fileName = "";
                        _fileChildren[0].sysFileName = "";
                        _fileChildren[0].orderby = "";
                        _fileChildren[0].path = "";
                        _fileChildren[0].viewFileName = "";
                    }
				}
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
            // $(this._addOneRec(_children, _children.length - 1)).insertBefore($(btnEl).parents(".main_inner_content_info"));
			//this.ArrShift(_children[_children.length - 1],dhid);
            $(this._addOneRec(_children, _children.length - 1)).animate({height: 'hide',opacity: 'hide'},'slow',function() {
                $(SurveyBuild._addOneRec(_children, _children.length - 1)).insertBefore($(btnEl).parents(".main_inner_content_info"));
            });

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
                                $inputObject.trigger('click');
                                $inputObject.trigger('click');
                            }
                        }
                    }
                });
            }
        });
    },
	
	addTjx: function(btnEl, instanceId) {
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
					if(ch.hasOwnProperty("isHidden")){
						ch["isHidden"] = "N";
					}
                });
            }else{
                //_fc[ins]["itemId"] += "_" + _children.length;
                _fc[ins]["itemId"] += "_" + suffix;
				if(obj.hasOwnProperty("isHidden")){
					obj["isHidden"] = "N";
				}
				//附件上传,清空附件信息
				if (_fc[ins]["classname"]=="refLetterFile"){
					_fc[ins]["filename"] = "";
					_fc[ins]["sysFileName"] = "";
					_fc[ins]["orderby"] = "";
					_fc[ins]["path"] = "";
					_fc[ins]["accessPath"] = "";
					_fc[ins]["viewFileName"] = "";
					
				}
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
			var objOneTjx = $(this._items[instanceId]._getHtmlOne(this._items[instanceId],_children.length));
            objOneTjx.insertBefore($(btnEl).parents(".main_inner_content_info"));
			/*行信息中的Select格式化*/
            var selectObj = objOneTjx.find("select");
            $.each(selectObj,function(i,sObj){
                $("#" + $(sObj).attr("id")).chosen();
            });
            /*行信息中的Select格式化
            var selectObj = $(this._items[instanceId]._getHtml(this._items[instanceId],true)).find("select");
            $.each(selectObj,function(i,sObj){
                $("#" + $(sObj).attr("id")).chosen();
            });*/
            if (this._items[instanceId]._eventbind && typeof this._items[instanceId]._eventbind == "function") {
                this._items[instanceId]._eventbind(this._items[instanceId]);
            }
        } else {
            // $(this._addOneRec(_children, _children.length - 1)).insertBefore($(btnEl).parents(".main_inner_content_info"));
			//this.ArrShift(_children[_children.length - 1],dhid);
            $(this._addOneRec(_children, _children.length - 1)).animate({height: 'hide',opacity: 'hide'},'slow',function() {
                $(SurveyBuild._addOneRec(_children, _children.length - 1)).insertBefore($(btnEl).parents(".main_inner_content_info"));
            });

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
        if (_children.length >= maxLines) {
            $(btnEl).hide();
        }

        /*子信息项事件绑定*/
		
        $.each(_children[_children.length - 1],function(d, obj) {
			
            if (obj._eventbind && typeof obj._eventbind == "function") {
				
                obj._eventbind(obj);
            }
        });
        this._setValidator(_fc);
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
            $(el).closest(".main_inner_content_para").animate({height: 'hide',opacity: 'hide'},'slow',function() {
                    $(el).closest(".main_inner_content_para").remove();
            });
            $("html,body").animate({scrollTop: $(el).closest(".dhcontainer").find(".main_inner_content_para").eq(index - 1).offset().top},1000);
            $(el).closest(".dhcontainer").find(".addnextbtn").show();
			this.ArrPush(SurveyBuild._items[instanceId]["children"][index],instanceId);
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
	deleteTjx: function(el) {
        //if (confirm("是否删除该条信息？")) {
		
        var index = $(el).closest(".main_inner_content_para").index();
        var instanceId = $(el).closest(".dhcontainer").attr("data-instancid");
        if (index > 0) {
            $(el).closest(".main_inner_content_para").animate({height: 'hide',opacity: 'hide'},'slow',function() {
                    $(el).closest(".main_inner_content_para").remove();
            });
            $("html,body").animate({scrollTop: $(el).closest(".dhcontainer").find(".main_inner_content_para").eq(index - 1).offset().top},1000);
			//console.log($(el).closest(".dhcontainer"));
			//console.log($(el).closest(".dhcontainer").find(".main_inner_content_info_add"));
            $(el).closest(".dhcontainer").find(".addnextbtn").show();
			this.ArrPush(SurveyBuild._items[instanceId]["children"][index],instanceId);
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
		//console.log($(el).closest(".main_inner_content_para").siblings(".main_inner_content_para"));
        //}
		var paraObject = $(el).closest(".main_inner_content_para").siblings(".main_inner_content_para");
		$.each(paraObject,function(i,paraObj){
			$(paraObj).find(".main_inner_content_title").find(".reg_title_grey_17px").html(MsgSet["REFFER"] + ' ' +(i+1)+ ' :' + SurveyBuild._items[instanceId].title);
		})
		
		
    },
    /* 保存Level 0级属性  前台不需要改方法，暂时隐藏  by WRL 2015/08/18
    save0Attr: function(el, attrName) {
        if (!el || !attrName) return;
        data = this._data;
        data[attrName] = this._getAttrVal(el);

        this.is_edit = true;
    },
    */
    /*==================================================
     +功能描述：报名表图片、附件控件上传
     +开发人：张浪
     ===================================================*/
    uploadAttachment: function(el,instanceId){
        var appInsId = SurveyBuild.appInsId;//报名表实例ID
		var refLetterId = SurveyBuild.refLetterId;//推荐信编号
        var data;
		var index = "";
        var $isDhContainer = $(el).closest(".dhcontainer");
        if ($isDhContainer.length == 0){
            data = SurveyBuild._items[instanceId];
        } else {
            var dhIns = $isDhContainer.attr("data-instancid");
            index = $(el).closest(".main_inner_content_para").index();
            data = SurveyBuild._items[dhIns].children[index][instanceId];
        }
        var itemId = data.itemId;
        var itemName = data.itemName;
        var className = data.classname;
        var multiFlag = data.allowMultiAtta;
		var isOnlineShow = data.isOnlineShow;//PDF在线预览
        var _children = data["children"];
        var path = $("#"+itemId).val();
		if(path){
			//文件名
			filename = path.substring(path.lastIndexOf("\\") + 1,path.length);
			//文件后缀
			var sysfileSuffix = (filename.substring(filename.lastIndexOf(".") + 1)).toLowerCase();
			var allowFileType = data.fileType;
			var allowSize = data.fileSize;
			//允许上传的文件类型
			var typeArr = allowFileType.split(",");
			var isAllow = false;
			if (sysfileSuffix && allowFileType != "" && typeArr.length > 0){
				for(var i=0; i<typeArr.length; i++){
					if(sysfileSuffix == typeArr[i].toLowerCase()){
						isAllow = true;
						break;
					}}
				if(!isAllow){
					var formatMsg = MsgSet["FILETYPE"].replace("【TZ_FILE_TYPE】",allowFileType);
					//formatMsg = formatMsg.substring(0,formatMsg.length-1);
					alert(formatMsg+"!");
					return;	
				}
			}
			//最多只能上传10个附件
			if(_children.length >= 10){
				alert(MsgSet["FILE_COUNT"])
				return;
			}

			try{
				loading();/*上传进度条*/
				var $form = document.getElementById("main_list");
				$form.encoding = "multipart/form-data";
				$form.action = TzUniversityContextPath + "/UpdWebServlet?filePath=appFormAttachment";
				$("#main_list").ajaxSubmit({
					dataType:'json',
					type:'POST',
					url:TzUniversityContextPath + "/UpdWebServlet?filePath=appFormAttachment",
					success: function(obj) {
						if(obj.success){
							//清空file控件的Value
							if(isIE = navigator.userAgent.indexOf("MSIE")!=-1) { //IE浏览器
								var file = $("#"+itemId); 
								file.after(file.clone().val("")); 
								file.remove(); 
								
								var $fileInput = $("#"+itemId);
								var $uplBtn = $fileInput.prev(".bt_blue");
								$fileInput.mousemove(function(e){
									$uplBtn.css("opacity","0.8");	
								});
								$fileInput.mouseout(function(e) {
									$uplBtn.css("opacity","1");
								});
							} else {//非IE浏览器
								$("#"+itemId).val("");	
							}
							
							var fileSize = obj.msg.size;
							fileSize = fileSize.substring(0,fileSize.length-1);
							if(allowSize !="" && fileSize/1024 > allowSize){
								layer.close(layer.index);/*关闭上传进度条*/
								alert(MsgSet["FILE_SIZE_CRL"].replace("【TZ_FILE_SIZE】",allowSize));
							} else {
								this.is_edit = true;
								var maxOrderBy;
								if (multiFlag == "Y"){
									maxOrderBy = _children[_children.length-1].orderby;//已存在最大顺序编号
								} else {
									maxOrderBy = 0;
								}
								//上传成功后将文件存储到数据库
								$.ajax({
									type: "post",
									url: SurveyBuild.tzGeneralURL+'?tzParams={"ComID":"TZ_GD_FILEUPD_COM","PageID":"TZ_GD_FILEUPD_STD","OperateType":"EJSON","comParams":{"tz_app_ins_id":"'+appInsId+'","itemId":"'+itemId+'","itemName":"'+SurveyBuild.specialCharReplace(itemName)+'","filename":"'+obj.msg.filename+'","sysFileName":"'+obj.msg.sysFileName+'","path":"'+obj.msg.path+'","maxOrderBy":"'+maxOrderBy+'","dhIndex":"'+index+'","refLetterId":"'+refLetterId+'"}}',									dataType: "json",
									async: false,
									success: function(rst){
										var state = rst.state;
										var rstObj = rst.comContent;
										if(state.errcode == 0){
											if(rstObj.result="success"){
												var c = "";
												if(multiFlag == "Y"){
													if (_children.length == 1 && _children[0].fileName == ""){
														_children[0].fileName = rstObj.fileName;
														_children[0].sysFileName = rstObj.sysFileName;
														_children[0].orderby = rstObj.index;
														_children[0].path = obj.msg.path;
														_children[0].accessPath = obj.msg.accessPath;
														_children[0].viewFileName = rstObj.viewFileName;
													} else {
														_fc = cloneObj(_children[0]);
														_fc["itemId"] += "_"+rstObj.index;
														_fc["itemName"] += "_"+rstObj.index;
														_fc["fileName"] = rstObj.fileName;
														_fc["sysFileName"] = rstObj.sysFileName;
														_fc["orderby"] = rstObj.index;
														_fc["path"] = obj.msg.path;
														_fc["accessPath"] = obj.msg.accessPath;
														_fc["viewFileName"] = rstObj.viewFileName;
														_children.push(_fc);
													}
													if (className == "imagesUpload"){
														c = '<li><a class="main_inner_filelist_a" onclick=SurveyBuild.viewImageSet(this,\"'+instanceId+'\") file-index="'+rstObj.index+'">'+rstObj.viewFileName+'</a><div class="main_inner_file_del" onclick=SurveyBuild.deleteFile(this,\"'+instanceId+'\")><img width="15" height="15" src="' + TzUniversityContextPath + '/statics/images/appeditor/del.png" title="'+MsgSet["DEL"]+'">&nbsp;' + MsgSet["DEL"] + '</div></li>';
													} else {
														
														c = '<li><a class="main_inner_filelist_a" onclick=SurveyBuild.downLoadFile(this,\"'+instanceId+'\") file-index="'+rstObj.index+'">'+rstObj.viewFileName+'</a><div class="main_inner_file_del" onclick=SurveyBuild.deleteFile(this,\"'+instanceId+'\")><img width="15" height="15" src="' + TzUniversityContextPath + '/statics/images/appeditor/del.png" title="'+MsgSet["DEL"]+'">&nbsp;' + MsgSet["DEL"] + '</div>'+(sysfileSuffix == "pdf" && isOnlineShow == "Y" ? "<div class='main_inner_pdf_reader' onclick=SurveyBuild.PDFpreview(this,\""+instanceId+"\") file-index='"+rstObj.index+"'><img src='" + TzUniversityContextPath + "/statics/images/appeditor/preview.png' title='"+MsgSet["PDF_VIEW"]+"'/>&nbsp;</div>":"")+'</li>';
													}
													$("#"+itemId+"_AttList").children("ul").append(c);
												}else{
													_children[0].fileName = rstObj.fileName;
													_children[0].sysFileName = rstObj.sysFileName;
													_children[0].orderby = rstObj.index;
													_children[0].path = obj.msg.path;
													_children[0].accessPath = obj.msg.accessPath;
													_children[0].viewFileName = rstObj.viewFileName;
	
													$("#"+itemId+"_A").text(rstObj.viewFileName);
													var $delEl = $("#"+itemId+"_A").next(".main_inner_file_del");
													if ($delEl.css("display") == "none"){
														$delEl.css("display","");
													}
													var $list = $("#"+itemId+"_A").closest("li");
													var $pdfReader = $("#"+itemId+"_A").closest("li").children(".main_inner_pdf_reader");
													
													if($pdfReader) $pdfReader.remove();
													if(sysfileSuffix == "pdf" && isOnlineShow == "Y") $list.append("<div class='main_inner_pdf_reader' onclick=SurveyBuild.PDFpreview(this,\""+instanceId+"\") file-index='1'><img src='" + TzUniversityContextPath + "/statics/images/appeditor/preview.png' title='"+MsgSet["PDF_VIEW"]+"'/>&nbsp;</div>");
													
												}
												//提示隐藏
												var $errorTip = $("#"+itemId+"Tip");
												if ($errorTip.hasClass("onError")){
													$errorTip.removeClass().addClass("onCorrect");
													$errorTip.children("div").removeClass().addClass("onCorrect");	
													$errorTip.children("div").attr("tips","");
												}
											}else{
												alert(rst.resultDesc);
											}
										}else{
											alert(state.errdesc);
										}
									}
								})
								layer.close(layer.index);/*关闭上传进度条*/
							}
						}else{
							noteing(MsgSet["FILE_UPL_FAILED"], 2);
							layer.close(layer.index);/*关闭上传进度条*/
						}
					}
				});
			}catch(e){
				alert(e);
			}
		} 
    },
    /*报名表附件下载*/
    downLoadFile: function(el, instanceId) {
        var appInsId = SurveyBuild.appInsId; //报名表实例ID
        var data;
        var $isDhContainer = $(el).closest(".dhcontainer");
        if ($isDhContainer.length == 0) {
            data = SurveyBuild._items[instanceId];
        } else {
            var dhIns = $isDhContainer.attr("data-instancid");
            var index = $(el).closest(".main_inner_content_para").index();
            data = SurveyBuild._items[dhIns].children[index][instanceId];
        }
        var _children = data.children;
        var index = $(el).parent("li").index();
        /*********************判断图片***START****************************/
        var type;
        var sysFileName = _children[index].sysFileName;
        var accessPath = _children[index].accessPath;
        //图片格式
        var picType = ['BMP', 'JPG', 'JPEG', 'PNG', 'GIF'];
        //文件后缀
        var count = 0,imgPos;
        var attHtml = "";
        var fileSuffix = (sysFileName.substring(sysFileName.lastIndexOf(".") + 1)).toUpperCase();
        if (picType.indexOf(fileSuffix) != -1) {
            type = "IMG"; //图片
            for (var i = 0; i < _children.length; i++) {
                var _sysFilename = _children[i].sysFileName;
                var _fileSuffix = (_sysFilename.substring(_sysFilename.lastIndexOf(".") + 1)).toUpperCase();
                if (picType.indexOf(_fileSuffix) != -1) {
                    if (sysFileName == _sysFilename) imgPos = count;
                    count++;
                    attHtml += "<li><a class='fancybox-thumbs' data-fancybox-group='thumb' href='" + TzUniversityContextPath + _children[i].accessPath + _sysFilename + "' title='" + SurveyBuild.specialCharReplace(_children[i].fileName) + "'>" + SurveyBuild.specialCharReplace(_children[i].fileName) + "</a></li>";
                }
            }
        } else {
            type = "ATTACHMENT"; //附件
            attHtml = TzUniversityContextPath + accessPath + sysFileName;
        }
        /*********************判断图片***END****************************/
        if (type == "ATTACHMENT") {
            window.open(attHtml);
        } else if (type = "IMG") {
            var $ul = $("#fancybox-main").children("ul");
            $ul.html(attHtml);
            var $li = $($ul.children("li")[imgPos]);
            $li.children("a").click();
        }
    },

    /*查看后台评委上传的附件-下载*/
    downLoadBmbFile: function(d,sysFileName,accessPath){
        var appInsId = SurveyBuild.appInsId;//报名表实例ID
        var data = SurveyBuild._items[d];
        var _children = data.children;
        /*********************判断图片***START****************************/
        var type;
        //图片格式
        var picType = ['BMP','JPG','JPEG','PNG','GIF'];
        //文件后缀
        var count = 0,imgPos;
        var attHtml = "";
        var fileSuffix = (sysFileName.substring(sysFileName.lastIndexOf(".") + 1)).toUpperCase();
        if (picType.indexOf(fileSuffix) != -1){
            type="IMG";//图片
            for (var i=0;i<_children.length;i++){
                var _sysFilename = _children[i].sysFileName;
                var _fileSuffix = (_sysFilename.substring(_sysFilename.lastIndexOf(".") + 1)).toUpperCase();
                if (picType.indexOf(_fileSuffix) != -1){
                    if(sysFileName == _sysFilename) imgPos=count;
                    count ++;
					attHtml += "<li><a class='fancybox-thumbs' data-fancybox-group='thumb' href='" + TzUniversityContextPath + _children[i].accessPath + _sysFilename + "' title='" + SurveyBuild.specialCharReplace(_children[i].fileName) + "'>" + SurveyBuild.specialCharReplace(_children[i].fileName) + "</a></li>";
                }
            }
        }else{
            type="ATTACHMENT";//附件
			attHtml = TzUniversityContextPath + accessPath + sysFileName;
        }
        /*********************判断图片***END****************************/
		if (type=="ATTACHMENT"){
			window.open(attHtml);
		}else if(type="IMG"){
			var $ul = $("#fancybox-main").children("ul");
			$ul.html(attHtml);
			var $li = $($ul.children("li")[imgPos]);
			$li.children("a").click();
		}
    },
	/*FDP预览*/
	PDFpreview : function(el,instanceId){
		var appInsId = SurveyBuild.appInsId;//报名表实例ID
        var data;
		var winWidth,winHeight;
        var $isDhContainer = $(el).closest(".dhcontainer");
        if ($isDhContainer.length == 0){
            data = SurveyBuild._items[instanceId];
        } else {
            var dhIns = $isDhContainer.attr("data-instancid");
            var index = $(el).closest(".main_inner_content_para").index();
            data = SurveyBuild._items[dhIns].children[index][instanceId];
        }
        var itemId = data.itemId;
		var _children = data.children;
        var orderby = $(el).attr("file-index");
		var index = $(el).parent("li").index();
		var sysFileName = _children[index].sysFileName;
		var accessPath = _children[index].accessPath;
		//获取浏览器窗口宽度
		if (window.innerWidth){
			winWidth = window.innerWidth;
		}else if ((document.body) && (document.body.clientWidth)){
			winWidth = document.body.clientWidth;
		}
		// 获取浏览器窗口高度 
		if (window.innerHeight){
			winHeight = window.innerHeight;
		}else if ((document.body) && (document.body.clientHeight)){
			winHeight = document.body.clientHeight;
		}
		if(winWidth<900){
			winWidth = 900;	
		}else{
			winWidth = winWidth - 20;
		}
		if(winHeight<550){
			winHeight = 550;	
		}else{
			winHeight = winHeight - 5;
		}
		var pdfReaderUrl = SurveyBuild.tzGeneralURL + '?tzParams=';
		var params = '{"ComID":"TZ_GD_FILEUPD_COM","PageID":"TZ_GD_PDFVIEW_STD","OperateType":"HTML","comParams":{"tz_app_ins_id":"'+appInsId+'","itemId":"'+itemId+'","orderby":"'+orderby+'","winWidth":"'+winWidth+'","winHeight":"'+winHeight+'","fileDate":{"sysFileName":"'+sysFileName+'","accessPath":"' + accessPath + '"}}}';
		pdfReaderUrl = pdfReaderUrl + window.escape(params);

		$.layer({
			type: 2,
			title: false,
			fix: true,
			closeBtn: false,
			shadeClose: false,
			shade : [0.3 , '#000' , true],
			border : [3 , 0.3 , '#000', true],
			offset: ['0%',''],
			area: [winWidth+'px',winHeight+'px'],
			move : true,
			iframe: {src: pdfReaderUrl}
		});
	},
    /*报名表图片查看*/
    viewImageSet: function(el, instanceId) {
        var appInsId = SurveyBuild.appInsId; //报名表实例ID
        var data;
        var $isDhContainer = $(el).closest(".dhcontainer");
        if ($isDhContainer.length == 0) {
            data = SurveyBuild._items[instanceId];
        } else {
            var dhIns = $isDhContainer.attr("data-instancid");
            var index = $(el).closest(".main_inner_content_para").index();
            data = SurveyBuild._items[dhIns].children[index][instanceId];
        }
        var _children = data.children;
        var index = $(el).parent("li").index();

        var imgHtmls = "";
        for (var i = 0; i < _children.length; i++) {
            imgHtmls += "<li><a class='fancybox-thumbs' data-fancybox-group='thumb' href='" + TzUniversityContextPath + _children[i].accessPath + _children[i].sysFileName + "' title='" + SurveyBuild.specialCharReplace(_children[i].fileName) + "'>" + SurveyBuild.specialCharReplace(_children[i].fileName) + "</a></li>";
        }

        var $ul = $("#fancybox-main").children("ul");
        $ul.html(imgHtmls);
        var $li = $($ul.children("li")[index]);
        $li.children("a").click();
    },
    /*报名表附件删除*/
    deleteFile: function(el,instanceId){
        var appInsId = SurveyBuild.appInsId;//报名表实例ID
        var data;
        var $isDhContainer = $(el).closest(".dhcontainer");
        if ($isDhContainer.length == 0){
            data = SurveyBuild._items[instanceId];
        } else {
            var dhIns = $isDhContainer.attr("data-instancid");
            var index = $(el).closest(".main_inner_content_para").index();
            data = SurveyBuild._items[dhIns].children[index][instanceId];
        }
        var itemId = data.itemId;
        var _children = data.children;
        //var $del = $(el);
        //var orderby = $del.prev("a").attr("file-index");
        //var index = $(el).parent("li").index();

        var multiFlag = data.allowMultiAtta;//是否允许多附件上传
		var Require = data.isRequire;//必填
        var liNum = $(el).parent("li").index();
        if (_children.length > 1){
            _children.splice(liNum, 1);
        } else {
            _children[0].fileName = "";
            _children[0].sysFileName = "";
            _children[0].orderby = "";
			_children[0].path = "";
			_children[0].viewFileName = "";
			
			if(Require == "Y"){
				var $errorTip = $("#"+itemId+"Tip");
				$errorTip.removeClass().addClass("onError");
				$errorTip.children("div").removeClass().addClass("onError");	
				$errorTip.children("div").attr("tips",MsgSet["FILE_UPL_REQUIRE"]);
			}
        }
        if(multiFlag == "Y"){
            $(el).parent("li").remove();
        } else {
            $("#"+itemId+"_A").text("");
            var $delEl = $("#"+itemId+"_A").next(".main_inner_file_del");
            $delEl.css("display","none");
			
			var $pdfReader = $("#"+itemId+"_A").closest("li").children(".main_inner_pdf_reader");
			if($pdfReader) $pdfReader.remove();
        }
    },

    //教育经历扫描件上传
    eduImgUpload: function(el,cins){
        var instanceId = $(el).closest(".dhcontainer").attr("data-instancid");
        var data = SurveyBuild._items[instanceId];
        var index = $(el).closest(".main_inner_content_para").index();
        var child = data.children[index];
        try{
            var $form = document.getElementById("main_list");
            $form.encoding = "multipart/form-data";
            $form.action = TzUniversityContextPath + "/UpdWebServlet?filePath=/linkfile/FileUpLoad/appFormAttachment";
            $("#main_list").ajaxSubmit({
                dataType:'json',
                url:TzUniversityContextPath + "/UpdWebServlet?filePath=/linkfile/FileUpLoad/appFormAttachment",
                success: function(obj) {
                    if(obj.success){
                        var fileSize = obj.msg.size;

                        this.is_edit = true;
                        $("#"+data.itemId+child[cins].itemId+"Attch").attr("href",obj.msg.accessPath + "/" + obj.msg.sysFileName);
                        $("#"+data.itemId+child[cins].itemId+"Attch").text(obj.msg.filename);

                        child[cins]["filename"] = obj.msg.filename;
                        child[cins]["sysFileName"] = obj.msg.sysFileName;
                        child[cins]["path"] = obj.msg.path;
                        child[cins]["accessPath"] = obj.msg.accessPath;

                    }else{
                        noteing("附件上传失败！", 2);
                    }
                }
            });
        }catch(e){
            alert(e);
        }
    },
    //推荐信上传附件
	TjxUpload: function(el,cins,num){
		var instanceId = $(el).closest(".dhcontainer").attr("data-instancid");
		var data = SurveyBuild._items[instanceId];
		var appInsId = SurveyBuild.appInsId;
		/***当删除行之后，num值就不正确了，需要动态的回去当前行数***/
		var index = num;
		var indexJson = parseInt($(el).closest(".main_inner_content_para").index());
		//console.log(num2+"--"+num)
		var child = data.children[indexJson];
		//生成日期

		var allowSize=5;
		var fileType = "jpg,png,pdf,doc,docx";
		try{
			loading();/*上传进度条*/
			var $form = document.getElementById("main_list");
			$form.encoding = "multipart/form-data";
			$form.action = TzUniversityContextPath + "/UpdWebServlet?filePath=appFormAttachment";
			$("#main_list").ajaxSubmit({
				dataType:'json',
				url:TzUniversityContextPath + "/UpdWebServlet?filePath=appFormAttachment",
				success: function(obj) {
					if(obj.success){
						//清空file控件的Value
						var file = $(el); 
						var fileInput_Id = $(el).attr("id");
						if(isIE = navigator.userAgent.indexOf("MSIE")!=-1) { //IE浏览器
							file.after(file.clone().val("")); 
							file.remove(); 
							
							var $fileInput = $("#"+fileInput_Id);
							var $uplBtn = $fileInput.prev(".bt_blue");
							$fileInput.mousemove(function(e){
								$uplBtn.css("opacity","0.8");	
							});
							$fileInput.mouseout(function(e) {
								$uplBtn.css("opacity","1");
							});
						} else {//非IE浏览器
							$("#"+fileInput_Id).val("");	
						}
						
						layer.close(layer.index);/*关闭上传进度条*/
						var fileSize = obj.msg.size;
						var _filename = obj.msg.filename;
						var _filename1 = _filename.substring(_filename.lastIndexOf("\\") + 1,_filename.length);
						var sysfileSuffix = (_filename1.substring(_filename1.lastIndexOf(".") + 1)).toLowerCase();
						var typeArr = fileType.split(",");
						var isAllow = false;
						if (fileType != "" && typeArr.length > 0){
							for(var i=0; i<typeArr.length; i++){
								if(sysfileSuffix == typeArr[i].toLowerCase()){
									isAllow = true;	
							}}
							if(!isAllow){
								var formatMsg = MsgSet["FILETYPE"].replace("【TZ_FILE_TYPE】",fileType);
								formatMsg = formatMsg.substring(0,formatMsg.length-1);
								alert(formatMsg+"!");
								return;	}	
						}

						fileSize = fileSize.substring(0,fileSize.length-1);
						if (fileSize/1024>allowSize)
						{
							alert(MsgSet["FILE_SIZE_CRL"].replace("【TZ_FILE_SIZE】",allowSize));
						}else{
							this.is_edit = true;
							var maxOrderBy = 0;
							$.ajax({
								type: "post",
								url: SurveyBuild.tzGeneralURL+"?tzParams={'ComID':'TZ_GD_FILEUPD_COM','PageID':'TZ_GD_FILEUPD_STD','OperateType':'EJSON','comParams':{'tz_app_ins_id':'"+appInsId+"','itemId':'"+data.itemId+"','filename':'"+obj.msg.filename+"','sysFileName':'"+obj.msg.sysFileName+"','path':'"+obj.msg.path+"','maxOrderBy':''}}",
								dataType: "json",
								async: false,
								success: function(rst){
									var state = rst.state;
									var rstObj = rst.comContent;
									if(state.errcode == 0){
										if(rstObj.result="success"){
											child[cins]["filename"] = rstObj.fileName;
											child[cins]["sysFileName"] = rstObj.sysFileName;
											child[cins]["path"] = obj.msg.path;
											child[cins]["accessPath"] = obj.msg.accessPath;
											child[cins]["viewFileName"] = rstObj.viewFileName;
											var c = "";
											c = '<ul><li>';
											c +='	<a class="main_inner_filelist_a" onclick=SurveyBuild.TjxdownLoad(this,\"'+cins+'\",'+indexJson+') file-index="'+index+'">'+rstObj.viewFileName+'</a>';
											c +='	<div class="main_inner_file_del" onclick=SurveyBuild.Tjxdelete(this,\"'+cins+'\",'+indexJson+')><img width="15" height="15" src="' + TzUniversityContextPath + '/statics/images/appeditor/del.png" title="'+MsgSet["DEL"]+'">'+MsgSet["DEL"]+'</div>';
											c +='</li></ul>';
											//$("#"+data.itemId+index+"_AttList").children("ul").append(c);
											$("#"+data.itemId+index+"_AttList").html(c);
										}else{
											alert(rst.resultDesc);	
										}
									}else{
										alert(state.errdesc);	
									}
								}
							});
						}
					}else{
						layer.close(layer.index);/*关闭上传进度条*/
						noteing("附件上传失败！", 2);
					}
				}
			});
		}catch(e){
			alert(e);	
		}
	},
    //推荐信附件下载
    TjxdownLoad: function(el, cins, num) {
        var appInsId = SurveyBuild.appInsId; //报名表实例ID
        var instanceId = $(el).closest(".dhcontainer").attr("data-instancid");
        var indexJson = parseInt($(el).closest(".main_inner_content_para").index());
        var data = SurveyBuild._items[instanceId];
        var index = num;
        var child = data.children[indexJson];

        var index = $(el).parent("li").index();
        var sysFileName = child[cins].sysFileName;
        var accessPath = child[cins].accessPath;
        /*********************判断图片***START****************************/
        var type;
        //图片格式
        var picType = ['BMP', 'JPG', 'JPEG', 'PNG', 'GIF'];
        //文件后缀
        var count = 0,imgPos;
        var attHtml = "";
        var fileSuffix = (sysFileName.substring(sysFileName.lastIndexOf(".") + 1)).toUpperCase();
        if (picType.indexOf(fileSuffix) != -1) {
            type = "IMG"; //图片
            attHtml = "<li><a class='fancybox-thumbs' data-fancybox-group='thumb' href='" + TzUniversityContextPath + accessPath + sysFileName + "' title='" + SurveyBuild.specialCharReplace(child[cins].fileName) + "'>" + SurveyBuild.specialCharReplace(child[cins].fileName) + "</a></li>";
        } else {
            type = "ATTACHMENT"; //附件
            attHtml = TzUniversityContextPath + accessPath + sysFileName;
        }
        /*********************判断图片***END****************************/
        if (type == "ATTACHMENT") {
            window.open(attHtml);
        } else if (type = "IMG") {
            var $ul = $("#fancybox-main").children("ul");
            $ul.html(attHtml);
            var $li = $($ul.children("li")[0]);
            $li.children("a").click();
        }
    },
	/*报名表附件删除*/
	Tjxdelete: function(el,cins,num){
		var appInsId = SurveyBuild.appInsId;//报名表实例ID
		var instanceId = $(el).closest(".dhcontainer").attr("data-instancid");
		var indexJson = parseInt($(el).closest(".main_inner_content_para").index());
		var data = SurveyBuild._items[instanceId];
		var index = num;
		var child = data.children[indexJson];
		var itemId = data.itemId;

		
		//var itemId = data.itemId;
		
		var liNum = $(el).parent("li").index();
		child[cins].filename = "";
		child[cins].sysFileName = "";
		child[cins].orderby = "";
		child[cins].viewFileName = "";
		$("#"+data.itemId+index+"_AttList").html("");

		/*if(multiFlag == "Y"){
			$(el).parent("li").remove();
		} else {
			$("#"+itemId+"_A").text("");
			var $delEl = $("#"+itemId+"_A").next(".main_inner_file_del");
			$delEl.css("display","none");	
		}*/
	},
	//推荐信单选按钮
	clickOnRadio : function(el){
		if ($(el).prop("checked")){
			$(el).closest(".main_inner_content_info_right").children(".tz_radio_div").removeClass("on_check");
			$(el).closest(".tz_radio_div").addClass("on_check");
		}
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
        s = s.replace(/\t/g,"\\t");
        s = s.replace(/\f/g,"\\f");
        s = s.replace(/\//g,"\\/");
        s = s.replace(/\n/g,"\\n");
        return s;
    },
    htmlCharReplace: function(str){
        var s = "";
        if(str == undefined) {
            return str;
        }
        if(str.length == 0) return "";
        s = $.trim(str.toString());
        s = s.replace(/\"/g,"&quot;");

        return s;
    },
	ArrShift : function(children,instancid){
        var lineno = 0;
        $.each(children,function(d, obj) {
            if(lineno > 0){
                return true
            }else{
				var tarItemId = "";
                if(obj["isSingleLine"]&& obj["isSingleLine"] == "Y"){
                    tarItemId = obj["children"][0]["itemId"];
                }else{
                    tarItemId = obj["itemId"];
                }
				if(tarItemId && tarItemId.substr(-2,1) == "_"){
					lineno = parseInt(tarItemId.substr(-1));
				}
            }
        });
		if(lineno > 0){
			var indexof = $.inArray(lineno,this._items[instancid]["linesNo"]);
			indexof >= 0 && this._items[instancid]["linesNo"].splice(indexof,1);
		}
	},
	ArrPush : function(children,instancid){
        var lineno = 0;
        $.each(children,function(d, obj) {
            if(lineno > 0){
                return true
            }else{
				var tarItemId = "";
                if(obj["isSingleLine"]&& obj["isSingleLine"] == "Y"){
                    tarItemId = obj["children"][0]["itemId"];
                }else{
                    tarItemId = obj["itemId"];
                }
				if(tarItemId && tarItemId.substr(-2,1) == "_"){
					lineno = parseInt(tarItemId.substr(-1));
				}
            }
        });
		this._items[instancid]["linesNo"].push(lineno);
	}
};
var MsgSet = {};