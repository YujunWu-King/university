var SurveyBuild = {
    _tid: 0,
    _data: {},
    _items: {},
    _count: 0,
    curPageNo:1,
    tzGeneralURL:"",
    _logic:{},//问卷控制逻辑
    _qid: [],
    _oid: [],
    _preg: {"email": {"name": "邮箱","regExp": "/^[\\w-]+(\\.[\\w-]+)*@[\\w-]+(\\.[\\w-]+)+$/"},"telphone": {"name": "手机","regExp": "/^1\\d{10}$/"},"idcard": {"name": "身份证号","regExp": "/(^\\d{15}$)|(^\\d{18}$)|(^\\d{17}(\\d|X|x)$)/"},"url": {"name": "网址URL","regExp": "/(http|ftp|https):\\/\\/[\\w\\-_]+(\\.[\\w\\-_]+)+([\\w\\-\\.,@?^=%&amp;:/~\\+#]*[\\w\\-\\@?^=%&amp;/~\\+#])?/"}},
    is_edit: false,
    is_edit_moda: true,
    comClass: {},//控件实例类
    _components: {},
    _componentConfig: [],//控件后台配置信息
    _componentIndex: [],//
    _componentLoadedIndex: [],
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
    allRand: function(b, d) {
        var a = d ? "crand": "rand";
        $("." + a).prop("checked", $(b).prop("checked"))
    },
    allMandatory: function(a) {
        $(".mandatory").prop("checked", $(a).prop("checked"))
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
        window.onbeforeunload = function() {
            if (SurveyBuild.is_edit) {
                return "当前页面有未保存内容\n选择“离开页面” 会丢失当前编辑的内容\n选择“留在此面” 可以继续编辑问题然后保存离开"
            }
        };

        this._items = this._data["items"];
        $("#question-edit").on("keypress", ".qcode,.ocode,.ccode",
            function(c) {
                var b = c.which;
                return b == 8 || b == 127 || b == 0 || b == 95 || (b >= 48 && b <= 57) || (b >= 65 && b <= 90) || (b >= 97 && b <= 122)
            });
        $("#question-edit").on("keypress", ".maxYear,.minYear,.timertime",
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
    _load: function (b) {
        var a = "", d = false;
        /*暂时隐藏 By WRL @20160108
        var isModify=true;
        if(SurveyBuild.appInsId &&  SurveyBuild.appInsId.length > 0){
            var tzPassParams = '{"ComID":"TZ_ZXDC_WJGL_COM","PageID":"TZ_SURVEY_APP_STD","OperateType":"EJSON","comParams":{"EType":"ISMODIFY","SURVEY_WJ_ID":' + templId + ',"SURVEY_INS_ID":' + SurveyBuild.appInsId + '}}';
            $.ajax({
                type: "POST",
                url:SurveyBuild.tzGeneralURL,
                data: {
                    tzParams:tzPassParams
                },
                async:false,
                dataType: "JSON",
                success: function(f) {
                    if (f.state.errcode == "0") {
                        if(f.comContent.code && f.comContent.code == "Y"){
                            isModify = true;
                        }else{
                            isModify = false;
                        }
                    }else{
                        alert("系统错误！")
                    }
                }
            });
        }
         */
        /*加载页面前获取最新的显示逻辑*/
        var displayLogic = {};
         if(SurveyBuild.appInsId &&  SurveyBuild.appInsId.length > 0){
            var tzPassParams = '{"ComID":"TZ_ZXDC_WJGL_COM","PageID":"TZ_SURVEY_APP_STD","OperateType":"EJSON","comParams":{"EType":"DISPLAY","SURVEY_INS_ID":' + SurveyBuild.appInsId + ',"PAGE_NO":"' + SurveyBuild.curPageNo + '"}}';
            $.ajax({
                type: "POST",
                url:SurveyBuild.tzGeneralURL,
                data: {
                    tzParams:tzPassParams
                },
                async:false,
                dataType: "JSON",
                success: function(f) {
                    if (f.state.errcode == "0") {
                        if(f.comContent.code && f.comContent.code == "0"){
                            displayLogic = f.comContent.value;
                           // console.log(displayLogic);
                        }
                    }else{
                        alert("系统错误！")
                    }
                }
            });
         }
        this._count = 0;
        var me = this;
        var callback = function (params) {
            var classname = params[0];
            var data = params[1];
            if (classname == "PageNav"){
                return;
            }
            if((data.pageno + 1) != me.curPageNo){
                return;
            }
            var d = data["instanceId"];
            //data转换成项对应的对象
            var component = me._items[d] = new me.comClass[classname](data);

            component._init && component._init.call(component, d);

            var _c = me._items[d]._getHtml(me._items[d], true), data = me._items[d];
            _c = "<div class='page" + data.pageno + " line' data-instancid='" + data.instanceId + "' >" + (_c || "") + "</div>";
            if(displayLogic.hasOwnProperty(data["itemId"])){
                var isHide = displayLogic[data["itemId"]]["display"];
                if(isHide == "N"){
                    _c = $(_c).hide();
                }
            }
            $("#main_list").append(_c);

            var _eventbind = me._items[d]._eventbind;
            if (_eventbind && typeof _eventbind == "function") {
                _eventbind(me._items[d]);
            }
            if($("[data-instancid='"  + data.instanceId +  "']").is(":hidden")){
                $("[data-instancid='" + data.instanceId + "']").find("input").attr("disabled", true).unFormValidator(true);
                $("[data-instancid='" + data.instanceId + "']").find("select").attr("disabled", true).unFormValidator(true);
                $("[data-instancid='" + data.instanceId + "']").find("textarea").attr("disabled", true).unFormValidator(true);
            }
        };
        $("#main_list").html("");
        /*加载信息项对应的JavaScript文件以及事件绑定 Begin*/
        $.each(this._items, function (c) {
            ++me._count;
            d = true;
            var item = me._items[c];

            me.loadScript(item["classname"], callback, [item["classname"], item]);

            //预览模式、有校验规则、非单行组合框
            if (!me.is_edit_moda && item.hasOwnProperty("rules") && item.isSingleLine != "Y") {
                //事件绑定
                $.each(item["rules"], function (classname, classObj) {
                    if ($.inArray(classname, me._baseRules) == -1 && item["rules"][classname]["isEnable"] == "Y") {
                        var _ruleClass = ValidationRules[classname];
                        if (_ruleClass && _ruleClass._eventList) {
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
                });
            }
        });
        /*加载信息项对应的JavaScript文件以及事件绑定 End*/
        if (!d && me.is_edit_moda) {
            $("#question-new").show()
        } else {

        }
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

    saveApp: function() {
        if ($("#app_save").length > 0) {
            $("#app_save").click();
        }
    },

    /*==================================================
     +功能描述：图片上传
     +开发人：张浪
     ===================================================*/
    uploadAttachment: function(el,instanceId){
        var appInsId = SurveyBuild.appInsId;//报名表实例ID
		var index = "", c = "";
        var data = SurveyBuild._items[instanceId];

        var itemId = data.itemId;
        var itemName = data.itemName;
        var className = data.classname;
        var multiFlag = data.allowMultiAtta;

        var _children = data["children"];
        var path = $("#"+itemId).val();
		if(path){
			filename = path.substring(path.lastIndexOf("\\") + 1,path.length);
			//文件后缀
			var sysfileSuffix = (filename.substring(filename.lastIndexOf(".") + 1)).toLowerCase();

			var allowFileType = data.fileType;
			var allowSize = data.fileSize;
			//允许上传的文件类型
			var typeArr = allowFileType.split(",");
			var isAllow = false;
			if (allowFileType != "" && typeArr.length > 0){
				for(var i=0; i<typeArr.length; i++){
					if(sysfileSuffix == typeArr[i].toLowerCase()){
						isAllow = true;
					}}
				if(!isAllow){
					var formatMsg = MsgSet["FILETYPE"].replace("【TZ_FILE_TYPE】",allowFileType);
					//formatMsg = formatMsg.substring(0,formatMsg.length-1);
                    if (className == "AttachUpload"){
                        alert(formatMsg+"!");
						//noteing("只能上传"+allowFileType+"附件类型！",2);

                    }else{
                        alert(formatMsg+"!");
						//noteing("只能上传"+allowFileType+"格式图片！",2);
                    }
					$("#"+itemId).val("");
					return;	}
			}
			//最多只能上传10个附件
			if(_children.length >= 10){
				//noteing("最多只能上传10个附件！",2);
				alert(MsgSet["FILE_COUNT"])
				return;
			}
			var date = new Date();
			var m = (date.getMonth()+1).toString().length == 1 ? "0"+(date.getMonth()+1).toString() : (date.getMonth()+1).toString();
			var d = date.getDate().toString().length == 1 ? "0"+date.getDate().toString() : date.getDate().toString();
			var dateString = date.getFullYear().toString() + m + d;
			try{
				layer.load(2);//上传进度条
				var $form = document.getElementById("main_list");
				$form.encoding = "multipart/form-data";
				$form.action = TzUniversityContextPath + "/UpdServlet?filePath=/linkfile/FileUpLoad/appFormAttachment/"+dateString;
				$("#main_list").ajaxSubmit({
					dataType:'json',
					type:'POST',
					url:TzUniversityContextPath + "/UpdServlet?filePath=/linkfile/FileUpLoad/appFormAttachment/"+dateString,
					success: function(obj) {
						if(obj.success){
							//清空file控件的Value
							if(isIE = navigator.userAgent.indexOf("MSIE")!=-1) { //IE浏览器
								var file = $("#"+itemId); 
								file.after(file.clone().val("")); 
								file.remove(); 
							} else {//非IE浏览器
								$("#"+itemId).val("");	
							}
							
							var fileSize = obj.msg.size;
							fileSize = fileSize.substring(0,fileSize.length-1);
							if(allowSize !="" && fileSize/1024 > allowSize){
								layer.close(layer.index);/*关闭上传进度条*/
								alert(MsgSet["FILE_SIZE_CRL"].replace("【TZ_FILE_SIZE】",allowSize));
								//noteing("上传的图片大小不能超过"+allowSize+"M!",2);
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
									url: SurveyBuild.tzGeneralURL+'?tzParams={"ComID":"TZ_ZXDC_UPD_COM","PageID":"TZ_IMG_UPD_STD","OperateType":"EJSON","comParams":{"tz_app_ins_id":"'+appInsId+'","itemId":"'+itemId+'","itemName":"'+SurveyBuild.specialCharReplace(itemName)+'","filename":"'+obj.msg.filename+'","sysFileName":"'+obj.msg.sysFileName+'","path":"'+obj.msg.path+'","maxOrderBy":"'+maxOrderBy+'"}}',
									dataType: "json",
									async: false,
									success: function(rst){
										var state = rst.state;
										var rstObj = rst.comContent;
										if(state.errcode == 0){
											if(rstObj.result="success"){
												if(SurveyBuild.accessType == "P" && className == "ImgUpload"){
													c = '<li><a onclick=SurveyBuild.viewImageSet(this,\"'+instanceId+'\") file-index="'+rstObj.index+'">'+rstObj.viewFileName+'</a><div class="img_del" onclick=SurveyBuild.deleteFile(this,\"'+instanceId+'\")><img width="15" height="15" src="' + TzUniversityContextPath + '/statics/images/appeditor/del.png" title="'+MsgSet["DEL"]+'">&nbsp;'+MsgSet["DEL"]+'</div></li>';
												} else  if(SurveyBuild.accessType == "M" && className == "ImgUpload"){
													c = '<li><a onclick=SurveyBuild.viewImageSet(this,\"'+instanceId+'\") file-index="'+rstObj.index+'">'+rstObj.viewFileName+'</a><img src="' + TzUniversityContextPath + '/statics/images/appeditor/del.png" onclick=SurveyBuild.deleteFile(this,\"'+instanceId+'\") title="'+MsgSet["DEL"]+'"></li>';
												}else if(SurveyBuild.accessType == "P" && className == "AttachUpload"){
                                                   c = '<li><a onclick=SurveyBuild.downLoadFile(this,\"'+instanceId+'\") file-index="'+rstObj.index+'">'+rstObj.viewFileName+'</a><div class="img_del" onclick=SurveyBuild.deleteFile(this,\"'+instanceId+'\")><img width="15" height="15" src="' + TzUniversityContextPath + '/statics/images/appeditor/del.png" title="'+MsgSet["DEL"]+'">&nbsp;'+MsgSet["DEL"]+'</div></li>';
                                                }else{
                                                    c = '<li><a onclick=SurveyBuild.downLoadFile(this,\"'+instanceId+'\") file-index="'+rstObj.index+'">'+rstObj.viewFileName+'</a><img  src="' + TzUniversityContextPath + '/statics/images/appeditor/del.png" onclick=SurveyBuild.deleteFile(this,\"'+instanceId+'\") title="'+MsgSet["DEL"]+'"></li>';
                                                }
												if(multiFlag == "Y"){
													if (_children.length == 1 && _children[0].fileName == ""){
														_children[0].fileName = rstObj.fileName;
														_children[0].sysFileName = rstObj.sysFileName;
														_children[0].orderby = rstObj.index;
														_children[0].path = obj.msg.path;
														_children[0].viewFileName = rstObj.viewFileName;
													} else {
														_fc = cloneObj(_children[0]);
														_fc["itemId"] += "_"+rstObj.index;
														_fc["itemName"] += "_"+rstObj.index;
														_fc["fileName"] = rstObj.fileName;
														_fc["sysFileName"] = rstObj.sysFileName;
														_fc["orderby"] = rstObj.index;
														_fc["path"] = obj.msg.path;
														_fc["viewFileName"] = rstObj.viewFileName;
														_children.push(_fc);
													}

													$("#"+itemId +"_attList").children("ul").append(c);
												}else{
													_children[0].fileName = rstObj.fileName;
													_children[0].sysFileName = rstObj.sysFileName;
													_children[0].orderby = rstObj.index;
													_children[0].path = obj.msg.path;
													_children[0].viewFileName = rstObj.viewFileName;
	
													$("#"+itemId +"_attList").children("ul").html(c);
												}
												
												if(SurveyBuild.accessType == "P"){
													if($("#"+itemId +"_attList").hasClass("nobackground")){
														$("#"+itemId +"_attList").removeClass("nobackground");	
													}
												} else {
													if($("#"+itemId +"_attList").hasClass("noUpload")){
														$("#"+itemId +"_attList").removeClass("noUpload");	
													}
												}

												var $errorTip = $("#"+itemId+"Tip");
												if ($errorTip.hasClass("onError")){
													$errorTip.removeClass().addClass("onCorrect");
													$errorTip.children("div").removeClass().addClass("onCorrect");	
													$errorTip.children("div").html("");
												}
											}else{
												//alert(rst.resultDesc);
												noteing(rst.resultDesc,2);
											}
										}else{
											//alert(state.errdesc);
											noteing(state.errdesc,2);
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
				alert(e)
			}
		}
    },
    /*报名表附件下载*/
    downLoadFile: function(el,instanceId){

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
        var orderby = $(el).attr("file-index");
        var index = $(el).parent("li").index();
        /*********************判断图片***START****************************/
        var type;
        var sysFileName = _children[index].sysFileName;
        //图片格式
        var picType = ['BMP','JPG','JPEG','PNG','GIF'];
        //文件后缀
        var count = 0,imgPos;
        var imgDate="";
        var fileSuffix = (sysFileName.substring(sysFileName.lastIndexOf(".") + 1)).toUpperCase();
        if (picType.indexOf(fileSuffix) != -1){
            type="IMG";//图片
            for (var i=0;i<_children.length;i++){
                var _sysFilename = _children[i].sysFileName;
                var _fileSuffix = (_sysFilename.substring(_sysFilename.lastIndexOf(".") + 1)).toUpperCase();
                if (picType.indexOf(_fileSuffix) != -1){
                    if(sysFileName == _sysFilename) imgPos=count;
                    count ++;
                    imgDate += "{'fileName':'"+SurveyBuild.specialCharReplace(_children[i].fileName)+"','sysFileName':'"+_sysFilename+"'},"
                }
            }
            if (imgDate != ""){
                imgDate = 	imgDate.substring(0,imgDate.length-1);
            }

            tzParams = "?tzParams={'ComID':'TZ_ZXDC_UPD_COM','PageID':'TZ_IMG_VIEW_STD','OperateType':'EJSON','comParams':{'tz_app_ins_id':'"+appInsId+"','itemId':'"+itemId+"','orderby':'"+orderby+"','imgDate':["+imgDate+"]}}";
        }else{
            type="ATTACHMENT";//附件
            tzParams = "?tzParams={'ComID':'TZ_ZXDC_UPD_COM','PageID':'TZ_IMG_VIEW_STD','OperateType':'EJSON','comParams':{'tz_app_ins_id':'"+appInsId+"','itemId':'"+itemId+"','orderby':'"+orderby+"','fileDate':{'sysFileName':'"+sysFileName+"'}}}";
        }
        /******* **************判断图片***END****************************/
        $.ajax({
            type: "post",
            url: SurveyBuild.tzGeneralURL+tzParams,
            dataType: "json",
            async: false,
            success: function(rst){
                var rstObj = rst.comContent;
                if (rstObj.result == "success"){
                    //  window.location.href = rstObj.resultDesc;
                    if (type=="ATTACHMENT"){
                        window.open(rstObj.resultDesc);
                    }else if(type="IMG"){
                        var $ul = $("#fancybox-main").children("ul");
                        $ul.html(rstObj.resultDesc);
                        var $li = $($ul.children("li")[imgPos]);
                        $li.children("a").click();
                    }
                }else{
                    alert(rstObj.resultDesc)
                }
            }
        })

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
        var params = "{'ComID':'TZ_ZXDC_UPD_COM','PageID':'TZ_IMG_UPD_STD','OperateType':'HTML','comParams':{'tz_app_ins_id':'"+appInsId+"','itemId':'"+itemId+"','orderby':'"+orderby+"','winWidth':'"+winWidth+"','winHeight':'"+winHeight+"','fileDate':{'sysFileName':'"+sysFileName+"'}}}";
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
    viewImageSet : function(el,instanceId){
        var appInsId = SurveyBuild.appInsId;//报名表实例ID
        var data = SurveyBuild._items[instanceId];

        var itemId = data.itemId;
        var _children = data.children;
        var orderby = $(el).attr("file-index");
        var index = $(el).parent("li").index();

        var fileDate = "";
        for(var i=0; i<_children.length; i++){
            fileDate += '{"fileName":"'+SurveyBuild.specialCharReplace(_children[i].fileName)+'","sysFileName":"'+_children[i].sysFileName+'"},'
        }
        if (fileDate != ""){
            fileDate = 	fileDate.substring(0,fileDate.length-1);
        }
		try{
        $.ajax({
            type: "post",
            url: SurveyBuild.tzGeneralURL+'?tzParams={"ComID":"TZ_ZXDC_UPD_COM","PageID":"TZ_IMG_VIEW_STD","OperateType":"HTML","comParams":{"tz_app_ins_id":"'+appInsId+'","itemId":"'+itemId+'","accessType":"'+ SurveyBuild.accessType +'","fileDate":['+fileDate+']}}',
            dataType: "html",
            success: function(imgLiHtml){
				if(SurveyBuild.accessType == "P"){
					var $ul = $("#fancybox-main").children("ul");
					$ul.html(imgLiHtml);
					var $li = $($ul.children("li")[index]);
					$li.children("a").click();
				} else {
					var $galleryBox = $("#unite_gallery_box");
					$galleryBox.html(imgLiHtml);
					$("#unite_gallery_box").unitegallery();
					//alert($("#unite_gallery_box").html());
					$("#unite_gallery_box").attr("style","display:none");
					$("#unite_gallery_box .ug-thumb-wrapper.ug-tile.ug-tile-fixed.ug-tile-clickable").eq(index).click();
				}
            }
        })
		}catch(e){
			alert(e);	
		} 
    },

    /*报名表附件删除*/
    deleteFile: function(el,instanceId){
        var appInsId = SurveyBuild.appInsId;//报名表实例ID
        var data = SurveyBuild._items[instanceId];

        $.each(data["rules"],function(classname, classObj) {
            if ($.inArray(classname, SurveyBuild._baseRules) == -1 && data["rules"][classname]["isEnable"] == "Y") {
                //必填校验
                if(classname == "RequireValidator" && data.isRequire == "Y"){
                    allowEmpty = false;
                    ReqErrorMsg = classObj["messages"];

                }
            }
        });
        var itemId = data.itemId;
        var _children = data.children;
        var className = data.classname;
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
				//$errorTip.children("div").html("请上传图片！");
                //区分PC和M
                var tips=$errorTip.children("div").attr("tips");
                if(undefined==tips){//不包含该属性
                    if (className=="ImgUpload" ){
                      $errorTip.children("div").html(ReqErrorMsg);
                    } else{
                        $errorTip.children("div").html(ReqErrorMsg);
                    }
                }else{
                    $errorTip.children("div").html("&nbsp;");
                    if (className=="ImgUpload"){
                    $errorTip.children("div").attr("tips",ReqErrorMsg);
                    }else{
                      $errorTip.children("div").attr("tips",ReqErrorMsg);
                    }
                }
			}
			if(SurveyBuild.accessType == "P"){
				if(!$("#"+itemId +"_attList").hasClass("nobackground")){
					$("#"+itemId +"_attList").addClass("nobackground");	
				}
			} else {
				if(!$("#"+itemId +"_attList").hasClass("noUpload")){
					$("#"+itemId +"_attList").addClass("noUpload");	
				}
        }
    }
    $(el).parent("li").remove();
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
handleInput: function(el){
    /*
     * Conditions[i]["Filter"]条件类型（1.选中，2.未选中，3.填写,4.未填写）
     *
     *
     *
     * */
    var instancid = $(el).closest("div .line").attr("data-instancid")
    var itemId = this._items[instancid]["itemId"];
    var storage = this._items[instancid]["StorageType"];

    if(!SurveyBuild._logic.hasOwnProperty(itemId)){
        return;
    }
    var Anslogics = SurveyBuild._logic[itemId];          //问题规则组
    $.each(Anslogics,function(i, logic) {
        //单个规则
        var isMeet = true;                                  //是否满足条件
        var showOrhide = logic["Type"];                     //动作类型（1显示、2隐藏）

        $.each(logic["Conditions"],function(j,condition){
            console.log("Conditions:    " + j);
            //规则条件
            if(condition.hasOwnProperty("SubQuestion") && condition["SubQuestion"]){
                //表格题
                var subQuestion = condition["SubQuestion"];
                var subOption = condition["option"];
                var subOfilter = condition["Filter"];
                var tempId = "#R" + itemId + "_" + subQuestion + "_" + subOption;
                //if($(el).attr("id") != tempId){
                //    isMeet = false;
                //    return true;
                //}
                if($(tempId).is(':checked')){
                    if(subOfilter == "2"){
                        isMeet = false;
                        return false;
                    }
                    isMeet = true;
                }else{
                    if(subOfilter == "1"){
                        isMeet = false;
                        return false;
                    }
                    isMeet = true;
                }
            }else if(condition.hasOwnProperty("option") && condition["option"]){
                //单选、多选、下拉
                var option = condition["option"];
                var filter = condition["Filter"];
                if(storage == "S"){
                    //下拉框
                    if($(el).val() == option){
                        if(filter == "2"){
                            isMeet = false;
                            return false;
                        }
                        isMeet = true;
                    }else{
                        if(filter == "1"){
                            isMeet = false;
                            return false;
                        }
                        isMeet = true;
                    }
                }else if (storage == "D"){
                    //单选、多选
                    if($("#o"+ itemId + option).is(':checked')){
                        if(filter == "2"){
                            isMeet = false;
                            return false;
                        }
                        isMeet = true;
                    }else{
                        if(filter == "1"){
                            isMeet = false;
                            return false;
                        }
                        isMeet = true;
                    }
                }
            }else{
                //填空题、问答题
                var filter = condition["Filter"];
                if($(el).val()){
                    //填写
                    if(filter == "4"){
                        isMeet = false;
                        return false;
                    }
                    isMeet = true;
                }else{
                    //未填写
                    if(filter == "3"){
                        isMeet = false;
                        return false;
                    }
                    isMeet = true;
                }
            }
        });

        $.each(logic["Settings"],function(k,targetObj){
            var oCons = targetObj["oConditions"];
            var targetItemId = targetObj["QuestName"];
            if($.isEmptyObject(oCons)){

                //无其他条件限制
                if(showOrhide == "2"){
                    isMeet && $("#" + targetItemId).attr("disabled", true).unFormValidator(true).closest("div .line").hide();
                    isMeet && $("#" + targetItemId).find("input").attr("disabled", true).unFormValidator(true).closest("div .line").hide();
                    isMeet && $("#" + targetItemId).find("select").attr("disabled", true).unFormValidator(true).closest("div .line").hide();
                    isMeet && $("#" + targetItemId).val("").change();
                    isMeet && $("#" + targetItemId).find("input").val("").change();
                    isMeet && $("#" + targetItemId).find("select").val("").change();

                    !isMeet && $("#" + targetItemId).attr("disabled", false).unFormValidator(false).closest("div .line").show();
                    !isMeet && $("#" + targetItemId).find("input").attr("disabled", false).unFormValidator(false).closest("div .line").show();
                    !isMeet && $("#" + targetItemId).find("select").attr("disabled", false).unFormValidator(false).closest("div .line").show();
                }
                if(showOrhide == "1"){
                    isMeet && $("#" + targetItemId).attr("disabled", false).unFormValidator(false).closest("div .line").show();
                    isMeet && $("#" + targetItemId).find("input").attr("disabled", false).unFormValidator(false).closest("div .line").show();
                    isMeet && $("#" + targetItemId).find("select").attr("disabled", false).unFormValidator(false).closest("div .line").show();
                    !isMeet && $("#" + targetItemId).attr("disabled", true).unFormValidator(true).closest("div .line").hide();
                    !isMeet && $("#" + targetItemId).find("input").attr("disabled", true).unFormValidator(true).closest("div .line").hide();
                    !isMeet && $("#" + targetItemId).find("select").attr("disabled", true).unFormValidator(true).closest("div .line").hide();
                    !isMeet && $("#" + targetItemId).val("").change();
                    !isMeet && $("#" + targetItemId).find("input").val("").change();
                    !isMeet && $("#" + targetItemId).find("select").val("").change();
                }
            }else{
                //有其他条件限制
                if(isMeet){
                   isMeet = SurveyBuild.checkOtherLogic(isMeet,showOrhide,oCons);
                }

                if(showOrhide == "2"){
                    isMeet && $("#" + targetItemId).attr("disabled", true).unFormValidator(true).closest("div .line").hide();
                    isMeet && $("#" + targetItemId).find("input").attr("disabled", true).unFormValidator(true).closest("div .line").hide();
                    isMeet && $("#" + targetItemId).find("select").attr("disabled", true).unFormValidator(true).closest("div .line").hide();
                    isMeet && $("#" + targetItemId).val("").change();
                    isMeet && $("#" + targetItemId).find("input").val("").change();
                    isMeet && $("#" + targetItemId).find("select").val("").change();
                    !isMeet && $("#" + targetItemId).attr("disabled", false).unFormValidator(false).closest("div .line").show();
                    !isMeet && $("#" + targetItemId).find("input").attr("disabled", false).unFormValidator(false).closest("div .line").show();
                    !isMeet && $("#" + targetItemId).find("select").attr("disabled", false).unFormValidator(false).closest("div .line").show();
                }
                if(showOrhide == "1"){
                    isMeet && $("#" + targetItemId).attr("disabled", false).unFormValidator(false).closest("div .line").show();
                    isMeet && $("#" + targetItemId).find("input").attr("disabled", false).unFormValidator(false).closest("div .line").show();
                    isMeet && $("#" + targetItemId).find("select").attr("disabled", false).unFormValidator(false).closest("div .line").show();
                    !isMeet && $("#" + targetItemId).attr("disabled", true).unFormValidator(true).closest("div .line").hide();
                    !isMeet && $("#" + targetItemId).find("input").attr("disabled", true).unFormValidator(true).closest("div .line").hide();
                    !isMeet && $("#" + targetItemId).find("select").attr("disabled", true).unFormValidator(true).closest("div .line").hide();
                    !isMeet && $("#" + targetItemId).val("").change();
                    !isMeet && $("#" + targetItemId).find("input").val("").change();
                    !isMeet && $("#" + targetItemId).find("select").val("").change();
                }
            }
        });
    });

    },
    checkOtherLogic:function(isMeet,showhide,logics){
        $.each(logics,function(key,logicId){
            var logic = {};
            /*获取逻辑规则*/
            $.each(SurveyBuild._logic[key],function(i,subLogic){
                if(subLogic["logicId"] == logicId && showhide == subLogic["Type"]){
                    //逻辑规则编号相同，操作类型相同（显示或隐藏）
                    logic = subLogic;
                    return false;
                }
            });

            $.each(logic["Conditions"],function(j,condition){
                if(condition.hasOwnProperty("SubQuestion") && condition["SubQuestion"]){
                    //表格题
                    var subQuestion = condition["SubQuestion"];
                    var subOption = condition["option"];
                    var subOfilter = condition["Filter"];
                    var tempId = "#R" + itemId + "_" + subQuestion + "_" + subOption;
                    if($(tempId).is(':checked')){
                        if(subOfilter == "2"){
                            isMeet = false;
                            return false;
                        }
                        isMeet = true;
                    }else{
                        if(subOfilter == "1"){
                            isMeet = false;
                            return false;
                        }
                        isMeet = true;
                    }
                }else if(condition.hasOwnProperty("option") && condition["option"]){
                    //单选、多选、下拉
                    var option = condition["option"];
                    var filter = condition["Filter"];
                    if($("#" + key).is("Select")){
                        //下拉框
                        if($("#" + key).val() == option){
                            if(filter == "2"){
                                isMeet = false;
                                return false;
                            }
                            isMeet = true;
                        }else{
                            if(filter == "1"){
                                isMeet = false;
                                return false;
                            }
                            isMeet = true;
                        }
                    }else{
                        //单选、多选
                        if($("#o"+ key + option).is(':checked')){
                            if(filter == "2"){
                                isMeet = false;
                                return false;
                            }
                            isMeet = true;
                        }else{
                            if(filter == "1"){
                                isMeet = false;
                                return false;
                            }
                            isMeet = true;
                        }
                    }
                }else{
                    //填空题、问答题
                    var filter = condition["Filter"];
                    if($("#" + key).val()){
                        //填写
                        if(filter == "4"){
                            isMeet = false;
                            return false;
                        }
                        isMeet = true;
                    }else{
                        //未填写
                        if(filter == "3"){
                            isMeet = false;
                            return false;
                        }
                        isMeet = true;
                    }
                }
            });
            if(!isMeet){
                return false;
            }
        });
        return isMeet;
    }
};
var MsgSet = {};