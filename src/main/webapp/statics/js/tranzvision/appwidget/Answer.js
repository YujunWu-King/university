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
    _preg: {"email": {"name": "邮箱","regExp": "/^[\\w-]+(\\.[\\w-]+)*@[\\w-]+(\\.[\\w-]+)+$/"},"telphone": {"name": "手机","regExp": "/^1\\d{10}$/"},"idcard": {"name": "身份证号","regExp": "/(^\\d{15}$)|(^\\d{18}$)|(^\\d{17}(\\d|X|x)$)/"},"url": {"name": "网址URL","regExp": "/(http|ftp|https):\\/\\/[\\w\\-_]+(\\.[\\w\\-_]+)+([\\w\\-\\.,@?^=%&amp;:/~\\+#]*[\\w\\-\\@?^=%&amp;/~\\+#])?/"},"certNo": {"name": "证书编号","regExp": "/^[A-Za-z0-9\-]+$/"}},
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
    saveTjx_fj: function() {
		
		var _tz_app_ins_id=SurveyBuild.appInsId;
		var _Url = SurveyBuild.tzGeneralURL + "?tzParams=";
		var param = '{"ComID":"TZ_GD_TJX_COM","PageID":"TZ_SEND_REF_STD","OperateType":"DELETE","comParams":{"rec_app_ins_id":"'+_tz_app_ins_id+'"}}';
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
                        } else if (classname == "RowLenValidator") {
                        	 component["isCheckRows"] = "Y";
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
				/*张彬彬添加,如果设置有默认行数，则比较默认行数和现有行数作为显示行数 start*/
				var _defaultLines = data.defaultLines;
				if(_defaultLines>1){
				}else{
					_defaultLines = 1;
				}
				var dhShowLines = Math.max(data.children.length,_defaultLines);
                for (var i = 0; i < dhShowLines; i++) {
					if(i >= data.children.length){
						/*添加之前需要Clone数据*/
						var maxLines = data["maxLines"];
						var isFixedCon = data.fixedContainer;    //是否为固定多行容器
						var _children = data.children, _fc = cloneObj(_children[0]);

						var suffix = data["linesNo"].shift();

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
										_fileChildren[0].accessPath = "";
										_fileChildren[0].viewFileName = "";
									}else{
										_fc[ins]["filename"] = "";
										_fc[ins]["sysFileName"] = "";
										_fc[ins]["path"] = "";
										_fc[ins]["accessPath"] = "";
										_fc[ins]["value"] = "";
									}
								}
							}
							if (!isFixedCon || isFixedCon != "Y"){
								_fc[ins] = new me.comClass[obj.classname](obj);
							}
							if(obj.hasOwnProperty("option")){
								$.each(obj.option,function(ii, opt) {
									_fc[ins]["option"][ii]["defaultval"] = "N";
									_fc[ins]["option"][ii]["other"] = "N";
									_fc[ins]["option"][ii]["checked"] = "N";
								});
							}
						});
						_children.push(_fc);
					}
					/*张彬彬添加,如果设置有默认行数，则比较默认行数和现有行数作为显示行数 结束*/
                    _co += me._addOneRec(data.children, i ,_defaultLines);
					
					if(i > 0 && i < data.children.length){
						this.ArrShift(data.children[i],d);
					}
                }
                _c = $("<div class='dhcontainer page" + data.pageno + "' data-instancid='" + data.instanceId + "'>" + _c + "</div>").find(".main_content_box").prepend(_co).parents('.dhcontainer');
            } else if (data["fixedContainer"] && data["fixedContainer"] == "Y") {
                //固定多行控件
                _c = "<div class='dhcontainer page" + data.pageno + "' data-instancid='" + data.instanceId + "'>" + (_c || "") + "</div>";
            } else {
                //单行控件
                _c = "<div class='page" + data.pageno + "' style='display:block;' data-instancid='" + data.instanceId + "'>" + (_c || "") + "</div>";
            }
            $("#main_list").append(_c);


            //控件绑定事件(固定多行容器存在问题)
           
            var _eventbind = data._eventbind;
            if (_eventbind && typeof _eventbind == "function") {
            	//console.log("2222");
                _eventbind(data);
            }
            if (data["isDoubleLine"] == "Y" && data.children && data.children.length > 0) {
                $.each(data.children,function (i, child) {
                    $.each(child,function (instanceId, obj) {
                        if (obj._eventbind && typeof obj._eventbind == "function") {
                        	//console.log("1111");
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
        //console.log("2222");
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
        
        //清华特有的改动
        var ProjectId = $("#ProjectId").val(); 
//		console.log(ProjectId);
		if (ProjectId =="PRJ_57") {		
		} else {
			$("#TZ_TZ_3_4").hide(); 
			$("#TZ_TZ_3_3").parent().parent().hide(); 
		}
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
                if(obj["rules"]==undefined){
                	return;
                }
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
                    if (obj["isCheckRows"] == "Y") {
                        _min = Math.max(obj["minRow"], _min);
                        if (obj["maxRow"] > 0) _max = obj["maxRow"];
                        if (_max > 1 || obj["minRow"] > 0) {
                            if (obj["isCheckRows"] == "Y") _onError = obj["rules"]["RowLenValidator"]["messages"];
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
    _addOneRec: function(children, i ,j) {
        var _co = "",del = ""; //容器行信息、容器行删除按钮,添加下一行
        //容器中行删除按钮源码
        del += '<div class="btn-addcon"><a href="javascript:void(0);" onclick="SurveyBuild.deleteFun(this);"><div class="input-delbtn">删除&nbsp;&nbsp;<span class="input-btn-icon"><img src="' + TzUniversityContextPath + '/statics/images/appeditor/new/add-delete.png"></span></div></a></div>';

        /*容器行信息 begin*/
        _co += '<div class="mainright-box pos-rela" ' + (i > 0 ? 'style="margin-top:15px;"':'') + '>';

        var lineno = 0;
        $.each(children[i],function(d, obj) {
            _co += obj._getHtml(obj, true);
            if(lineno > 0){
                return true
            }else{
				var tarItemId = "";
				//console.dir(obj);
				
                if(obj["isSingleLine"]&& obj["isSingleLine"] == "Y"){
                	if (obj["children"][0]  == undefined) {
                		tarItemId = obj["children"]["itemId"];
                	} else {
                		tarItemId = obj["children"][0]["itemId"];
                	}
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
		var notDelLinesCount = 0;
		if(j>0){
			notDelLinesCount = j - 1;
		}
        if (i > notDelLinesCount) {
            //为除第一行之外的行添加删除功能
            //将删除按钮添加在行信息中，首个信息项的后面（该信息项必须包含main_inner_content_info_autoheight）
            _co = $(_co).prepend(del).closest(".mainright-box").get(0).outerHTML;
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
		var _defaultLines = this._items[instanceId]["defaultLines"];
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
                        _fileChildren[0].accessPath = "";
                        _fileChildren[0].viewFileName = "";
                    }else{
						_fc[ins]["filename"] = "";
						_fc[ins]["sysFileName"] = "";
						_fc[ins]["path"] = "";
						_fc[ins]["accessPath"] = "";
						_fc[ins]["value"] = "";
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
            $(this._items[instanceId]._getHtmlOne(this._items[instanceId],_children.length)).insertBefore($(btnEl).parents(".mainright-box"));

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
            $(this._addOneRec(_children, _children.length - 1, _defaultLines)).animate({height: 'hide',opacity: 'hide'},'slow',function() {
                $(SurveyBuild._addOneRec(_children, _children.length - 1,_defaultLines)).insertBefore($(btnEl).parents(".addNext"));
            });

            /*行信息中的Select格式化*/
            var selectObj = $(this._addOneRec(_children, _children.length - 1, _defaultLines)).find("select");
            $.each(selectObj,function(i,sObj){
                $("#" + $(sObj).attr("id")).chosen();
            });
        }
		
		/*新增一行动态效果*/
		var $newRow = $(btnEl).parents(".addNext").prev(".mainright-box");
		$("html,body").animate({scrollTop: $newRow.offset().top}, 1000);

        //行数大于等于最大行数时，隐藏“Add One +”按钮
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
					_fc[ins]["accessPath"] = "";
					_fc[ins]["viewFileName"] = "";
				}else if("imagesUpload"==_fc[ins]["classname"]){
					_fc[ins]["filename"] = "";
					_fc[ins]["sysFileName"] = "";
					_fc[ins]["orderby"] = "";
					_fc[ins]["accessPath"] = "";
					_fc[ins]["viewFileName"] = "";
					if(_fc[ins].hasOwnProperty("children")){
						_fc[ins]["children"]=[{"itemId":"attachment_Upload","itemName":"图片上传","title":"图片上传","orderby":"","fileName":"","sysFileName":"","accessPath":"","viewFileName":""}]
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
			var objOneTjx = $(this._items[instanceId]._getHtmlOne(this._items[instanceId],_children.length));
            objOneTjx.insertBefore($(btnEl).parents(".addNext"));
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
                $(SurveyBuild._addOneRec(_children, _children.length - 1)).insertBefore($(btnEl).parents(".addNext"));
            });

            /*行信息中的Select格式化*/
            var selectObj = $(this._addOneRec(_children, _children.length - 1)).find("select");
            $.each(selectObj,function(i,sObj){
                $("#" + $(sObj).attr("id")).chosen();
            });
        }
		
		/*新增一行动态效果*/
		var $newRow = $(btnEl).parents(".addNext").prev(".main_inner_content_para");

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
    },
    deleteFun: function(el) {
        //if (confirm("是否删除该条信息？")) {
        var index = $(el).closest(".mainright-box").index();
        var instanceId = $(el).closest(".dhcontainer").attr("data-instancid");
        if (index > 0) {
            $(el).closest(".mainright-box").animate({height: 'hide',opacity: 'hide'},'slow',function() {
                    $(el).closest(".mainright-box").remove();
            });
            $("html,body").animate({scrollTop: $(el).closest(".dhcontainer").find(".mainright-box").eq(index - 1).offset().top},1000);
            $(el).closest(".dhcontainer").find(".input-addbtn").show();
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
            $(el).closest(".dhcontainer").find(".input-addbtn").show();
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
		//console.log($(el).closest(".mainright-box").siblings(".mainright-box"));
        //}
		var paraObject = $(el).closest(".main_inner_content_para").siblings(".main_inner_content_para");
		$.each(paraObject,function(i,paraObj){
			$(paraObj).find(".mainright-title").html("<span class='title-line'></span>" + MsgSet["REFFER"] + ' ' +(i+1)+ ' :' + SurveyBuild._items[instanceId].title);
		})
		
		
    },
    //删除英语水平
    deleteEngLev: function(el) {
        //if (confirm("是否删除该条信息？")) {
		
        var index = $(el).closest(".main_inner_content_para").index();
        //console.log("index:"+index);
        var instanceId = $(el).closest(".dhcontainer").attr("data-instancid");
        if (index > 0) {
            $(el).closest(".main_inner_content_para").animate({height: 'hide',opacity: 'hide'},'slow',function() {
                    $(el).closest(".main_inner_content_para").remove();
            });
            $("html,body").animate({scrollTop: $(el).closest(".dhcontainer").find(".main_inner_content_para").eq(index - 1).offset().top},1000);
			//console.log($(el).closest(".dhcontainer"));
			//console.log($(el).closest(".dhcontainer").find(".main_inner_content_info_add"));
            $(el).closest(".main_inner_content").find(".addNext").find(".main_inner_content_info_add ").show();
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
		//console.log($(el).closest(".mainright-box").siblings(".mainright-box"));
        //}
		var paraObject = $(el).closest(".main_inner_content_para").siblings(".main_inner_content_para");
		$.each(paraObject,function(i,paraObj){
			$(paraObj).find(".mainright-title").html("<span class='title-line'></span>" + MsgSet["ENG_LEV"] + ' ' +(i+1));
		})
		
		
    },
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
            index = $(el).closest(".mainright-box").index();
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
			var allowFileType = $.trim(data.fileType);
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
				var tzParam = "?filePath=appFormAttachment&keyName=" + $(el).attr("name") + "&" + Math.random();
				$form.action = TzUniversityContextPath + "/SingleUpdWebServlet" + tzParam;
				$("#main_list").ajaxSubmit({
					dataType:'json',
					type:'POST',
					url:TzUniversityContextPath + "/SingleUpdWebServlet" + tzParam,
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
								var _Url = SurveyBuild.tzGeneralURL + "?tzParams=";
								var param = '{"ComID":"TZ_GD_FILEUPD_COM","PageID":"TZ_GD_FILEUPD_STD","OperateType":"EJSON","comParams":{"tz_app_ins_id":"'+appInsId+'","itemId":"'+itemId+'","itemName":"'+SurveyBuild.specialCharReplace(itemName)+'","filename":"'+obj.msg.filename+'","sysFileName":"'+obj.msg.sysFileName+'","maxOrderBy":"'+maxOrderBy+'","dhIndex":"'+index+'","refLetterId":"'+refLetterId+'","isAdmin":"'+SurveyBuild.appManager+'"}}';
								$.ajax({
									type: "post",
									url: _Url + encodeURIComponent(param), 									
									dataType: "json",
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
														_children[0].accessPath = obj.msg.accessPath;
														_children[0].viewFileName = rstObj.viewFileName;
													} else {
														_fc = cloneObj(_children[0]);
														_fc["itemId"] += "_"+rstObj.index;
														_fc["itemName"] += "_"+rstObj.index;
														_fc["fileName"] = rstObj.fileName;
														_fc["sysFileName"] = rstObj.sysFileName;
														_fc["orderby"] = rstObj.index;
														_fc["accessPath"] = obj.msg.accessPath;
														_fc["viewFileName"] = rstObj.viewFileName;
														_children.push(_fc);
													}
													if (className == "imagesUpload"){
								        				c += '<div class="input-list-uploadcon-list">';
								        				c += '	<div class="input-list-uploadcon-listl left"><a class="input-list-uploadcon-list-a" onclick=SurveyBuild.viewImageSet(this,"' + instanceId + '") file-index="' + rstObj.index + '">' + rstObj.viewFileName + '</a></div>';
								        				/*c += '	<div class="input-list-uploadcon-listr left"><button class="upload-del" onclick="SurveyBuild.deleteFile(this,\'' + instanceId + '\')">' + MsgSet["DEL"] + '</button></div>';*/
														c += '<div class="input-list-uploadcon-listr left" style="display: inline-block;line-height:46px;" onclick="SurveyBuild.deleteFile(this,\'' + instanceId + '\')"><img src="' + TzUniversityContextPath + '/statics/images/appeditor/del.png" title="' + MsgSet["DEL"] + '"/>&nbsp;</div>';
								        				c += '	<div class="clear"></div>';
								        				c += '</div>';
													} else {
							        	   				c += '<div class="input-list-uploadcon-list">';
							        	   				c += '	<div class="input-list-uploadcon-listl left">';
							        	   				c += '		<a class="input-list-uploadcon-list-a" onclick=SurveyBuild.downLoadFile(this,"' + instanceId + '") file-index = "' + rstObj.index + '">' + rstObj.viewFileName + '</a>';
							        	   				if(sysfileSuffix == "pdf" && isOnlineShow == "Y"){
							        	   					c += '<div class="input-list-uploadcon-list-pdf" onclick="SurveyBuild.PDFpreview(this,\'' + instanceId + '\')" file-index = "' + rstObj.index + '">&nbsp;&nbsp;<img src="' + TzUniversityContextPath + '/statics/images/appeditor/preview.png" title="' + MsgSet["PDF_VIEW"] + '"/>&nbsp;</div>';
							        	   				}
							        	   				c += '	</div>';
							        	   				/*c += '	<div class="input-list-uploadcon-listr left"><button class="upload-del" onclick="SurveyBuild.deleteFile(this,\"' + instanceId + '\")">' + MsgSet["DEL"] + '</button></div>';*/
														c += '<div class="input-list-uploadcon-listr left" style="display: inline-block;line-height:46px;" onclick="SurveyBuild.deleteFile(this,\'' + instanceId + '\')"><img src="' + TzUniversityContextPath + '/statics/images/appeditor/del.png" title="' + MsgSet["DEL"] + '"/>&nbsp;</div>';
							        	   				c += '	<div class="clear"></div>';
							        	   				c += '</div>';
													}
													$("#"+itemId+"_AttList").append(c);
												}else{
													/*
													if (_children.length == 1 && _children[0].fileName == ""){
														_children[0].fileName = rstObj.fileName;
														_children[0].sysFileName = rstObj.sysFileName;
														_children[0].orderby = rstObj.index;
														_children[0].accessPath = obj.msg.accessPath;
														_children[0].viewFileName = rstObj.viewFileName;
													} else {
														_fc = cloneObj(_children[0]);
														_fc["itemId"] += "_"+rstObj.index;
														_fc["itemName"] += "_"+rstObj.index;
														_fc["fileName"] = rstObj.fileName;
														_fc["sysFileName"] = rstObj.sysFileName;
														_fc["orderby"] = rstObj.index;
														_fc["accessPath"] = obj.msg.accessPath;
														_fc["viewFileName"] = rstObj.viewFileName;
														_children.push(_fc);
													}*/
													_children[0].fileName = rstObj.fileName;
													_children[0].sysFileName = rstObj.sysFileName;
													_children[0].orderby = rstObj.index;
													_children[0].accessPath = obj.msg.accessPath;
													_children[0].viewFileName = rstObj.viewFileName;
													if (className == "imagesUpload"){
								        				c += '<div class="input-list-uploadcon-list">';
								        				c += '	<div class="input-list-uploadcon-listl left"><a class="input-list-uploadcon-list-a" onclick=SurveyBuild.viewImageSet(this,"' + instanceId + '") file-index="' + rstObj.index + '">' + rstObj.viewFileName + '</a></div>';
								        				/*c += '	<div class="input-list-uploadcon-listr left"><button class="upload-del" onclick="SurveyBuild.deleteFile(this,\'' + instanceId + '\')">' + MsgSet["DEL"] + '</button></div>';*/
														c += '<div class="input-list-uploadcon-listr left" style="display: inline-block;line-height:46px;" onclick="SurveyBuild.deleteFile(this,\'' + instanceId + '\')"><img src="' + TzUniversityContextPath + '/statics/images/appeditor/del.png" title="' + MsgSet["DEL"] + '"/>&nbsp;</div>';
								        				c += '	<div class="clear"></div>';
								        				c += '</div>';
													} else {
							        	   				c += '<div class="input-list-uploadcon-list">';
							        	   				c += '	<div class="input-list-uploadcon-listl left">';
							        	   				c += '		<a class="input-list-uploadcon-list-a" onclick=SurveyBuild.downLoadFile(this,"' + instanceId + '") file-index = "' + rstObj.index + '">' + rstObj.viewFileName + '</a>';
							        	   				if(sysfileSuffix == "pdf" && isOnlineShow == "Y"){
							        	   					c += '<div class="input-list-uploadcon-list-pdf" onclick="SurveyBuild.PDFpreview(this,\'' + instanceId + '\')" file-index = "' + rstObj.index + '">&nbsp;&nbsp;<img src="' + TzUniversityContextPath + '/statics/images/appeditor/preview.png" title="' + MsgSet["PDF_VIEW"] + '"/>&nbsp;</div>';
							        	   				}
							        	   				c += '	</div>';
							        	   				/*c += '	<div class="input-list-uploadcon-listr left"><button class="upload-del" onclick="SurveyBuild.deleteFile(this,\"' + instanceId + '\")">' + MsgSet["DEL"] + '</button></div>';*/
														c += '<div class="input-list-uploadcon-listr left" style="display: inline-block;line-height:46px;" onclick="SurveyBuild.deleteFile(this,\'' + instanceId + '\')"><img src="' + TzUniversityContextPath + '/statics/images/appeditor/del.png" title="' + MsgSet["DEL"] + '"/>&nbsp;</div>';
							        	   				c += '	<div class="clear"></div>';
							        	   				c += '</div>';
													}
													$("#"+itemId+"_AttList").html(c);
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
            var index = $(el).closest(".mainright-box").index();
            data = SurveyBuild._items[dhIns].children[index][instanceId];
        }
        var _children = data.children;
        var index = $(el).parents(".input-list-uploadcon-list").index();
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
        if (picType.toString().indexOf(fileSuffix) != -1) {
            type = "IMG"; //图片
            for (var i = 0; i < _children.length; i++) {
                var _sysFilename = _children[i].sysFileName;
                var _fileSuffix = (_sysFilename.substring(_sysFilename.lastIndexOf(".") + 1)).toUpperCase();
                if (picType.toString().indexOf(_fileSuffix) != -1) {
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
        if (picType.toString().indexOf(fileSuffix) != -1){
            type="IMG";//图片
            for (var i=0;i<_children.length;i++){
                var _sysFilename = _children[i].sysFileName;
                var _fileSuffix = (_sysFilename.substring(_sysFilename.lastIndexOf(".") + 1)).toUpperCase();
                if (picType.toString().indexOf(_fileSuffix) != -1){
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
            var index = $(el).closest(".mainright-box").index();
            data = SurveyBuild._items[dhIns].children[index][instanceId];
        }
        var itemId = data.itemId;
		var _children = data.children;
        var orderby = $(el).attr("file-index");
		var index = $(el).parents(".input-list-uploadcon-list").index();
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
            var index = $(el).closest(".mainright-box").index();
            data = SurveyBuild._items[dhIns].children[index][instanceId];
        }
        var _children = data.children;
        var index = $(el).parents(".input-list-uploadcon-list").index();

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
            var index = $(el).closest(".mainright-box").index();
            data = SurveyBuild._items[dhIns].children[index][instanceId];
        }
        var itemId = data.itemId;
        var _children = data.children;
        //var $del = $(el);
        //var orderby = $del.prev("a").attr("file-index");
        //var index = $(el).parent("li").index();

        var multiFlag = data.allowMultiAtta;//是否允许多附件上传
		var Require = data.isRequire;//必填
        var liNum = $(el).parents(".input-list-uploadcon-list").index();
        if (_children.length > 1){
            _children.splice(liNum, 1);
        } else {
            _children[0].fileName = "";
            _children[0].sysFileName = "";
            _children[0].orderby = "";
			_children[0].accessPath = "";
			_children[0].viewFileName = "";
			
			if(Require == "Y"){
				var $errorTip = $("#"+itemId+"Tip");
				$errorTip.removeClass().addClass("onError");
				$errorTip.children("div").removeClass().addClass("onError");	
				$errorTip.children("div").attr("tips",MsgSet["FILE_UPL_REQUIRE"]);
			}
        }
        if(multiFlag == "Y"){
        	$(el).parents(".input-list-uploadcon-list").remove();
        }else{
        	$(el).parents(".input-list-uploadcon-list").remove();
        }
//        else {
//            $("#"+itemId+"_A").text("");
//            var $delEl = $("#"+itemId+"_A").next(".main_inner_file_del");
//            $delEl.css("display","none");
//			
//			var $pdfReader = $("#"+itemId+"_A").closest("li").children(".main_inner_pdf_reader");
//			if($pdfReader) $pdfReader.remove();
//        }
    },

    //教育经历扫描件上传
    eduImgUpload: function(el,cins){
        var instanceId = $(el).closest(".dhcontainer").attr("data-instancid");
        var data = SurveyBuild._items[instanceId];
        var index = $(el).closest(".mainright-box").index();
        var child = data.children[index];
        try{

        	
        	
            var $form = document.getElementById("main_list");
            $form.encoding = "multipart/form-data";
			var tzParam = "?filePath=appFormAttachment&keyName=" + $(el).attr("name") + "&" + Math.random();
			$form.action = TzUniversityContextPath + "/SingleUpdWebServlet" + tzParam;
            $("#main_list").ajaxSubmit({
                dataType:'json',
                url:TzUniversityContextPath + "/SingleUpdWebServlet" + tzParam,
                success: function(obj) {
                    if(obj.success){
						//清空file控件的Value
						if(isIE = navigator.userAgent.indexOf("MSIE")!=-1) { //IE浏览器
							var file = $(el); 
							file.after(file.clone().val("")); 
							file.remove(); 
							
							var $fileInput = $(el);
							var $uplBtn = $fileInput.prev(".bt_blue");
							$fileInput.mousemove(function(e){
								$uplBtn.css("opacity","0.8");	
							});
							$fileInput.mouseout(function(e) {
								$uplBtn.css("opacity","1");
							});
						} else {//非IE浏览器
							$(el).val("");	
						}
                        var fileSize = obj.msg.size;

                        this.is_edit = true;
                        $("#"+data.itemId+child[cins].itemId+"Attch").attr("href",TzUniversityContextPath + obj.msg.accessPath + "/" + obj.msg.sysFileName);
                        $("#"+data.itemId+child[cins].itemId+"Attch").text(obj.msg.filename.substring(0,20) + "...");


                        child[cins]["filename"] = obj.msg.filename;
                        child[cins]["sysFileName"] = obj.msg.sysFileName;
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
		var indexJson ="";
		//修改判断判断是否是手机版
    	if(SurveyBuild.accessType=='M'){
    		indexJson=parseInt($(el).closest(".next_record").index());
    		
    	}else{
    		indexJson=parseInt($(el).closest(".main_inner_content_para").index());
    		
    	}
			
			
		//console.log(num2+"--"+num)
		var child = data.children[indexJson];
		//生成日期

		var allowSize=5;
		var fileType = "jpg,png,pdf,doc,docx";
		try{
			loading();/*上传进度条*/
			var $form = document.getElementById("main_list");
			$form.encoding = "multipart/form-data";
			var tzParam = "?filePath=appFormAttachment&keyName=" + $(el).attr("name") + "&" + Math.random();
			$form.action = TzUniversityContextPath + "/SingleUpdWebServlet" + tzParam;
			$("#main_list").ajaxSubmit({
				dataType:'json',
				url:TzUniversityContextPath + "/SingleUpdWebServlet" + tzParam,
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
								url: encodeURI(SurveyBuild.tzGeneralURL+'?tzParams={"ComID":"TZ_GD_FILEUPD_COM","PageID":"TZ_GD_FILEUPD_STD","OperateType":"EJSON","comParams":{"tz_app_ins_id":"'+appInsId+'","itemId":"'+data.itemId+'","filename":"'+obj.msg.filename+'","sysFileName":"'+obj.msg.sysFileName+'","path":"'+obj.msg.path+'","maxOrderBy":""}}'),
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
											c = '<div class="input-list-uploadcon-list">';
											c += '  <div class="input-list-uploadcon-listl left">';
											c +='	<a class="input-list-uploadcon-list-a" onclick=SurveyBuild.TjxdownLoad(this,\"'+cins+'\",'+indexJson+') file-index="'+index+'">'+rstObj.viewFileName+'</a>';
											c +='	</div>';
											c +='	<div class="input-list-uploadcon-listr left" style="display: block;padding-top:8px;" onclick=SurveyBuild.Tjxdelete(this,\"'+cins+'\",'+indexJson+')><img src="' + TzUniversityContextPath + '/statics/images/appeditor/del.png" title="'+MsgSet["DEL"]+'"/></div>';
											c +='</div>';
											c +='	<div class="clear"></div>';
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
        if (picType.toString().indexOf(fileSuffix) != -1) {
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
		var indexJson ="";
	
		//修改判断判断是否是手机版
    	if(SurveyBuild.accessType=='M'){
    		indexJson=parseInt($(el).closest(".next_record").index());
    		
    	}else{
    		indexJson=parseInt($(el).closest(".main_inner_content_para").index());
    		
    	}
		
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
		/*
		if ($(el).prop("checked")){
			$(el).closest(".main_inner_content_info_right").children(".tz_radio_div").removeClass("on_check");
			$(el).closest(".tz_radio_div").addClass("on_check");
		}*/
		
		var _this = $(el),block = $(el).parent().parent();
		var readOnly = $(el).attr("readonlyflag");
		if(readOnly!="Y"){
			block.find('input:radio').attr('checked', false);
			block.find(".radio-btn").removeClass('checkedRadio');
			_this.addClass('checkedRadio');
			_this.find('input:radio').attr('checked', true);
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
	},
	//验证码
	changeImgCode : function(el){
		var _captchaURL = TzUniversityContextPath + "/captcha";
		var imgObj = $(el).find("img").attr("src",_captchaURL + "?" + Math.random());
	},
	//----------------------------
	oldDeleteFun:function(el){
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
	},
	//--------原版本图片上传:还原方法->>
	   engUploadAttachment: function(el,instanceId,engIntanceId){
		    //根据当前的句柄，获取id后坠
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


	            data = SurveyBuild._items[dhIns].children[index][engIntanceId];
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
				var allowFileType = $.trim(data.fileType);
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
					var tzParam = "?filePath=appFormAttachment&keyName=" + $(el).attr("name") + "&" + Math.random();
					$form.action = TzUniversityContextPath + "/SingleUpdWebServlet" + tzParam;
					$("#main_list").ajaxSubmit({
						dataType:'json',
						type:'POST',
						url:TzUniversityContextPath + "/SingleUpdWebServlet" + tzParam,
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

									var _Url = SurveyBuild.tzGeneralURL + "?tzParams=";
									var param = '{"ComID":"TZ_GD_FILEUPD_COM","PageID":"TZ_GD_FILEUPD_STD","OperateType":"EJSON","comParams":{"tz_app_ins_id":"'+appInsId+'","itemId":"'+itemId+'","itemName":"'+SurveyBuild.specialCharReplace(itemName)+'","filename":"'+obj.msg.filename+'","sysFileName":"'+obj.msg.sysFileName+'","maxOrderBy":"'+maxOrderBy+'","dhIndex":"'+index+'","refLetterId":"'+refLetterId+'"}}';
									$.ajax({
										type: "post",
										url: _Url + encodeURIComponent(param), 									
										dataType: "json",
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
															_children[0].accessPath = obj.msg.accessPath;
															_children[0].viewFileName = rstObj.viewFileName;
														} else {
															_fc = cloneObj(_children[0]);
															_fc["itemId"] += "_"+rstObj.index;
															_fc["itemName"] += "_"+rstObj.index;
															_fc["fileName"] = rstObj.fileName;
															_fc["sysFileName"] = rstObj.sysFileName;
															_fc["orderby"] = rstObj.index;
															_fc["accessPath"] = obj.msg.accessPath;
															_fc["viewFileName"] = rstObj.viewFileName;
															_children.push(_fc);
														}
														if (className == "imagesUpload"){
									        				c += '<div class="input-list-uploadcon-list">';
									        				c += '	<div class="input-list-uploadcon-listl left"><a class="input-list-uploadcon-list-a" onclick=SurveyBuild.engViewImageSet(this,\'' + instanceId + '\',\'' + engIntanceId + '\') file-index="' + rstObj.index + '">' + rstObj.viewFileName + '</a></div>';
									        				/*c += '	<div class="input-list-uploadcon-listr left"><button class="upload-del" onclick="SurveyBuild.deleteFile(this,\'' + instanceId + '\')">' + MsgSet["DEL"] + '</button></div>';*/
															c += '<div class="input-list-uploadcon-listr left" style="display: inline-block;line-height:46px;" onclick="SurveyBuild.oldDeleteFile(this,\'' + instanceId + '\',\'' + engIntanceId + '\')"><img src="' + TzUniversityContextPath + '/statics/images/appeditor/del.png" title="' + MsgSet["DEL"] + '"/>&nbsp;</div>';
									        				c += '	<div class="clear"></div>';
									        				c += '</div>';
														} else {
								        	   				c += '<div class="input-list-uploadcon-list">';
								        	   				c += '	<div class="input-list-uploadcon-listl left">';

								        	   				c += '		<a class="input-list-uploadcon-list-a" onclick=SurveyBuild.downLoadFile(this,"' + instanceId + '") file-index = "' + rstObj.index + '">' + rstObj.viewFileName + '</a>';
								        	   				if(sysfileSuffix == "pdf" && isOnlineShow == "Y"){
								        	   					c += '<div class="input-list-uploadcon-list-pdf" onclick="SurveyBuild.PDFpreview(this,\'' + instanceId + '\')" file-index = "' + rstObj.index + '">&nbsp;&nbsp;<img src="' + TzUniversityContextPath + '/statics/images/appeditor/preview.png" title="' + MsgSet["PDF_VIEW"] + '"/>&nbsp;</div>';
								        	   				}
								        	   				c += '	</div>';
								        	   				/*c += '	<div class="input-list-uploadcon-listr left"><button class="upload-del" onclick="SurveyBuild.deleteFile(this,\"' + instanceId + '\")">' + MsgSet["DEL"] + '</button></div>';*/
															c += '<div class="input-list-uploadcon-listr left" style="display: inline-block;line-height:46px;" onclick="SurveyBuild.oldDeleteFile(this,\'' + instanceId + '\',\'' + engIntanceId + '\')"><img src="' + TzUniversityContextPath + '/statics/images/appeditor/del.png" title="' + MsgSet["DEL"] + '"/>&nbsp;</div>';
								        	   				c += '	<div class="clear"></div>';
								        	   				c += '</div>';
														}
														$("#"+itemId+"_AttList").append(c);
													}else{
														/*
														if (_children.length == 1 && _children[0].fileName == ""){
															_children[0].fileName = rstObj.fileName;
															_children[0].sysFileName = rstObj.sysFileName;
															_children[0].orderby = rstObj.index;
															_children[0].accessPath = obj.msg.accessPath;
															_children[0].viewFileName = rstObj.viewFileName;
														} else {
															_fc = cloneObj(_children[0]);
															_fc["itemId"] += "_"+rstObj.index;
															_fc["itemName"] += "_"+rstObj.index;
															_fc["fileName"] = rstObj.fileName;
															_fc["sysFileName"] = rstObj.sysFileName;
															_fc["orderby"] = rstObj.index;
															_fc["accessPath"] = obj.msg.accessPath;

															_fc["viewFileName"] = rstObj.viewFileName;
															_children.push(_fc);
														}*/
														_children[0].fileName = rstObj.fileName;
														_children[0].sysFileName = rstObj.sysFileName;
														_children[0].orderby = rstObj.index;
														_children[0].accessPath = obj.msg.accessPath;
														_children[0].viewFileName = rstObj.viewFileName;
														if (className == "imagesUpload"){
									        				c += '<div class="input-list-uploadcon-list">';
									        				c += '	<div class="input-list-uploadcon-listl left"><a class="input-list-uploadcon-list-a" onclick=SurveyBuild.engViewImageSet(this,\'' + instanceId + '\',\'' + engIntanceId + '\') file-index="' + rstObj.index + '">' + rstObj.viewFileName + '</a></div>';
									        				/*c += '	<div class="input-list-uploadcon-listr left"><button class="upload-del" onclick="SurveyBuild.deleteFile(this,\'' + instanceId + '\')">' + MsgSet["DEL"] + '</button></div>';*/
															c += '<div class="input-list-uploadcon-listr left" style="display: inline-block;line-height:46px;" onclick="SurveyBuild.oldDeleteFile(this,\'' + instanceId + '\',\'' + engIntanceId + '\',\'' + index + '\')"><img src="' + TzUniversityContextPath + '/statics/images/appeditor/del.png" title="' + MsgSet["DEL"] + '"/>&nbsp;</div>';
									        				c += '	<div class="clear"></div>';
									        				c += '</div>';
														} else {
								        	   				c += '<div class="input-list-uploadcon-list">';
								        	   				c += '	<div class="input-list-uploadcon-listl left">';
								        	   				c += '		<a class="input-list-uploadcon-list-a" onclick=SurveyBuild.downLoadFile(this,"' + instanceId + '") file-index = "' + rstObj.index + '">' + rstObj.viewFileName + '</a>';
								        	   				if(sysfileSuffix == "pdf" && isOnlineShow == "Y"){
								        	   					c += '<div class="input-list-uploadcon-list-pdf" onclick="SurveyBuild.PDFpreview(this,\'' + instanceId + '\')" file-index = "' + rstObj.index + '">&nbsp;&nbsp;<img src="' + TzUniversityContextPath + '/statics/images/appeditor/preview.png" title="' + MsgSet["PDF_VIEW"] + '"/>&nbsp;</div>';
								        	   				}
								        	   				c += '	</div>';
								        	   				/*c += '	<div class="input-list-uploadcon-listr left"><button class="upload-del" onclick="SurveyBuild.deleteFile(this,\"' + instanceId + '\")">' + MsgSet["DEL"] + '</button></div>';*/
															c += '<div class="input-list-uploadcon-listr left" style="display: inline-block;line-height:46px;" onclick="SurveyBuild.deleteFile(this,\'' + instanceId + '\')"><img src="' + TzUniversityContextPath + '/statics/images/appeditor/del.png" title="' + MsgSet["DEL"] + '"/>&nbsp;</div>';
								        	   				c += '	<div class="clear"></div>';
								        	   				c += '</div>';



														}
														$("#"+itemId+"_AttList").html(c);





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
    //------------------------------->>
    oldDeleteFile: function(el,instanceId,comInstanceId){
    	//alert("delete");
    	 var appInsId = SurveyBuild.appInsId;//报名表实例ID
         var data;
         var $isDhContainer = $(el).closest(".dhcontainer");
         if ($isDhContainer.length == 0){
             data = SurveyBuild._items[instanceId];
         } else {
             var dhIns = $isDhContainer.attr("data-instancid");
             var index = $(el).closest(".main_inner_content_para").index();
             data = SurveyBuild._items[dhIns].children[index][comInstanceId];
         }
         var itemId = data.itemId;
         var _children = data.children;
         //var $del = $(el);
         //var orderby = $del.prev("a").attr("file-index");
         //var index = $(el).parent("li").index();

         var multiFlag = data.allowMultiAtta;//是否允许多附件上传
 		var Require = data.isRequire;//必填
         var liNum = $(el).parents(".input-list-uploadcon-list").index();
         if (_children.length > 1){
             _children.splice(liNum, 1);
         } else {
             _children[0].fileName = "";
             _children[0].sysFileName = "";
             _children[0].orderby = "";
 			_children[0].accessPath = "";
 			_children[0].viewFileName = "";
 			
 			if(Require == "Y"){
 				var $errorTip = $("#"+itemId+"Tip");
 				$errorTip.removeClass().addClass("onError");
 				$errorTip.children("div").removeClass().addClass("onError");	
 				$errorTip.children("div").attr("tips",MsgSet["FILE_UPL_REQUIRE"]);
 			}
         }
         if(multiFlag == "Y"){
         	$(el).parents(".input-list-uploadcon-list").remove();
         }else{
         	$(el).parents(".input-list-uploadcon-list").remove();
         }

    },
    //----------------------
    engViewImageSet: function(el, instanceId,comInstanceId) {
        var appInsId = SurveyBuild.appInsId; //报名表实例ID
        //var data;
        var $isDhContainer = $(el).closest(".dhcontainer");
        if ($isDhContainer.length == 0) {
            data = SurveyBuild._items[instanceId];
        } else {
            var dhIns = $isDhContainer.attr("data-instancid");
            var index = $(el).closest(".main_inner_content_para").index();

            data = SurveyBuild._items[dhIns].children[index][comInstanceId];
        }
        var _children = data.children;
        var index = $(el).parents(".input-list-uploadcon-list").index();

        var imgHtmls = "";
        var type = "";
        var sysFileName = _children[index].sysFileName;
        var accessPath = _children[index].accessPath;
        var picType = ['JPG', 'JPEG', 'PNG'];
        var fileSuffix = (sysFileName.substring(sysFileName.lastIndexOf(".") + 1)).toUpperCase();
        if (picType.toString().indexOf(fileSuffix) != -1) {
        	type = "IMG"; //图片
            for (var i = 0; i < _children.length; i++) {
                imgHtmls += "<li><a class='fancybox-thumbs' data-fancybox-group='thumb' href='" + TzUniversityContextPath + _children[i].accessPath + _children[i].sysFileName + "' title='" + SurveyBuild.specialCharReplace(_children[i].fileName) + "'>" + SurveyBuild.specialCharReplace(_children[i].fileName) + "</a></li>";
            }
        }else{
        	type = "PDF"; //PDF
        	attHtml = TzUniversityContextPath + accessPath + sysFileName;
        }
        if (type == "PDF") {
        	
        	window.open(attHtml);
        }else{
        	
        	 var $ul = $("#fancybox-main").children("ul");
             $ul.html(imgHtmls);
             var $li = $($ul.children("li")[index]);
             $li.children("a").click();
        }

       
    }
};
var MsgSet = {};