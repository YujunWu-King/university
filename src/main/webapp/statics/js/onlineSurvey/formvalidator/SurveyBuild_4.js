var SurveyBuild = {
    _sid: 0,
    _data: {},
    _count: 0,
    _qid: [],
    _oid: [],
    _preg: [["^\\d+$", "整数"], ["^\\d+(\\.\\d+)?$", "小数"], ["^[\\w-]+(\\.[\\w-]+)*@[\\w-]+(\\.[\\w-]+)+$", "邮箱"], ["^1[3|4|5|8]\\d{9}$", "手机"], ["^[A-Za-z]+$", "字母"], ["^\\w+$", "数字字母下划线"], ["(http://|https://|ftp://)?[a-zA-Z0-9\\.\\-]+\\.[a-zA-Z]{2,4}(:[a-zA-Z0-9]*)?/?([a-zA-Z0-9\\-\\._\\?\\,\\'/\\\\\\+&%\\$#\\=~])*$", "网址URL"]],
    is_edit: false,
    is_edit_moda:true,
    _alph:['A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z'],
    _color: ["#FFFFFF", "#5D762A", "#FF0000", "#800080", "#008000", "#855B00", "#000000", "#FFFF00", "#990000", "#FFA500", "#E4E4E4", "#D2691E", "#1EDDFF", "#FFFFB1", "#98FB98", "#BDB76B", "#666666", "#4B0082", "#041690", "#FFB6C1", "#DDA0DD", "#0000FF", "url(/assets/img/mixed.png);", "url(/assets/img/trans.png);"],
    comClass:{},
    _components:{},   
    _componentConfig:[],//记录控件后台配置信息
	_componentIndex:[],
	_componentLoadedIndex:[],
	_baseRules:["RequireValidator","CharLenValidator","NumSizeValidator"],
	_define:function(clsassname,source){
    	var me = this;
		this["comClass"][clsassname] = function(source){
    		source = $.extend(true,{},me["comClass"][clsassname],source);
			for (var p in source) {
  		        if (source.hasOwnProperty(p)) {
  		        	this[p] = source[p];		            
  		        }
  		    }
		};;
		for (var p in source) {
	        if (source.hasOwnProperty(p)) {
	        	if(typeof source[p] == "function"){
	        		if(!this["comClass"][clsassname])this["comClass"][clsassname] =function(){};
	        		this["comClass"][clsassname].prototype[p] = source[p];
	        	}else{
	        		me["comClass"][clsassname][p] = source[p];		
	        	}  		            
	        }
	    }
		me["comClass"][clsassname]["classname"]= clsassname;
    },
	define:function (clsassname, source, fun) {
    	if(source && typeof source == "object"){    		
    		this._define(clsassname,source);
    		if(fun && typeof fun == "function"){
    			fun.apply(source);
    		}
    	}	
    	return this["comClass"][clsassname];
	},
	extend:function(classname,target, source){
		if(classname && target && source){
		  if(typeof target == "string"){
			  target =  cloneObj(new this["comClass"][target]());
		  }
		  var source = $.extend(true,{},target,source);  
		  this._define(classname,source);
		}
	},
	loadScript: function(className,callBack,params){
		var isLoadad = true;
		var me = this;
		if(!className)return false;
		var i = $.inArray(className,this._componentLoadedIndex);
		if(i>-1){
			if(callBack && typeof callBack == "function"){
				if(!params)params = [className];
				if(params && !$.isArray(params))params = [className];
				callBack.call(me,params);
			}	
			return isLoadad;
		}			
		i = $.inArray(className,this._componentIndex);
		var fileUrl = $.trim(this._componentConfig[i]["jsfileUrl"]);
		if(i>-1){
			$.ajax({
			    type:"get",
			    dataType:"script",
			    data:{},
			    async:false,
			    cache:false,
			    url:fileUrl,
			    success:function(){
			    	me._componentLoadedIndex.push(className);
					if(callBack && typeof callBack == "function"){
						if(!params)params = [className];
						if(params && !$.isArray(params))params = [className];
						callBack.call(me,params);
					}	
			    },
			    complete : function(xhr, status){
					if(status=="success"){
					}else{
						alert(fileUrl+"加载失败！");
					}	
			  }
			});    

		}		
	},
	_getAttrVal:function(el){
		var instancdId = $("#question-edit").attr("data_id");
		var val = "";
		if(el.tagName == "INPUT"){
			val = $(el).val();
			if($(el).attr("type")=="radio" || $(el).attr("type")=="checkbox" ){
				val = $(el).prop("checked")? "Y" :"N";
			}
		}else if(el.tagName == "TEXTAREA"){
			val = $(el).html();
		}else if(el.tagName == "SELECT"){
			val = $(el).val();
		}
		return val;
	},
	saveAttr:function(el,attrName){
		if(!el || !attrName)return;
		var instanceId = $("#question-edit").attr("data_id");
		var val = this._getAttrVal(el);		
		this._data[instanceId][attrName] = val;
		var rules = this._data[instanceId]["rules"];
		var $activeLi = $("#question-box li.active");
		if(attrName == "itemName"){
			$activeLi.find(".question-question").html(val);
		}else if(attrName == "itemId"){
			$activeLi.find(".question-code").html(val);
			//设置 必填、字符串长度、数值取值校验的联动显示
		}else if(attrName == "isRequire"){
			var RequireValidatorObj = this._data[instanceId]["rules"]["RequireValidator"];
			if(!rules["RequireValidator"] && RequireValidatorObj){
				rules["RequireValidator"] = RequireValidatorObj;				
			}
			if(rules["RequireValidator"]){
				if(val == "Y"){
					rules["RequireValidator"]["isEnable"] = "Y"
				}else{
					rules["RequireValidator"]["isEnable"] = "N"
				}
				$("#is_require").prop("checked",$(el).prop("checked"));
			}
		}else {
			if(attrName == "isCheckStrLen"){
				var CharLenValidatorObj = this._data[instanceId]["rules"]["CharLenValidator"]
				if(!rules["CharLenValidator"] && CharLenValidatorObj){
					rules["CharLenValidator"] = CharLenValidatorObj;					
				}
				if(rules["CharLenValidator"]){
					if(val == "Y"){
						rules["CharLenValidator"]["isEnable"] = "Y"
					}else{
						rules["CharLenValidator"]["isEnable"] = "N"
					}
					$("#is_checkstrlen").prop("checked",$(el).prop("checked"));
				}
			}else{ 
				if(attrName == "isNumSize"){
					var NumSizeValidatorObj = this._data[instanceId]["rules"]["NumSizeValidator"]
					if(!rules["NumSizeValidator"] && NumSizeValidatorObj){
						rules["NumSizeValidator"] = NumSizeValidatorObj;
					}
					if(rules["NumSizeValidator"]){
						if(val == "Y"){							
							rules["NumSizeValidator"]["isEnable"] = "Y"
						}else{
							rules["NumSizeValidator"]["isEnable"] = "N"
						}
						$("#is_checknumsize").prop("checked",$(el).prop("checked"));
					}
				}
			}	
		}
		this.is_edit=true;
	},
	setRuleParams:function(classname){
		if(ValidationRules[classname] && ValidationRules[classname]._getConfigData){
			if( ValidationRules[classname]._getConfigDataValidator &&  ValidationRules[classname]._getConfigDataValidator() == false){
				return false;
			}
			var params = ValidationRules[classname]._getConfigData();
			var instanceId = $("#question-edit").attr("data_id");
			var rules = this._data[instanceId]["rules"];
			if(!params)params={};
			rules[classname]["params"]=params;
		}
		
		this.RulesSet(classname);
	},
	//校验规则参数设置页面
	setRuleConfigPage:function(el){
		var $tr = $(el).closest("tr");
		var classname = $tr.attr("data-classname");
		var instanceId = $("#question-edit").attr("data_id");
		var rules = this._data[instanceId]["rules"];
		var itemName = this._data[instanceId].itemName;
		var me = this;
		var currentRule = rules[classname],
			messages = currentRule["messages"],
			pageHtml = '<div class="modal-header"><h4>规则参数、提示信息设置</h4></div><div id="modal-question-advanced" class="modal-body"><fieldset>信息项名称：'+itemName+'</fieldset>';
		if(ValidationRules[classname] && ValidationRules[classname]["is_configpage"] == true){
			pageHtml += ValidationRules[classname]._getConfigPage(currentRule["params"]||{});
		}
		pageHtml += '<fieldset><table class="table table-hover table-bordered"><tr><th width="180px">提示信息说明</th><th>提示信息内容</th></tr>';
		for( var key in messages){
			if(key != "len"){
				pageHtml += '<tr data-classname="'+classname+'"><td>'+messages[key]["desc"]+'</td><td><input type="text" value="'+messages[key]["value"]+'" style="width:85%;" onkeyup="SurveyBuild.saveRuleMsg(this,\''+key+'\')" class="adv_first_msg"></td></tr>';
			}
		}
		pageHtml+= '</table></fieldset></div><div class="modal-footer clearfix"><button class="btn pull-left" onclick="$.fancybox.close();"><i class="icon-ban-circle"></i>取消</button><button class="btn btn-warning pull-right" onclick="SurveyBuild.setRuleParams(\''+classname+'\');"><i class="icon-ok"></i>确定</button></div>';
		var callback = function(){
			setTimeout(function(){
				$(".fancybox-close").unbind("click").click(function(){
					me.RulesSet();
				})
			},600);
			
		}
		this.openMoadal(pageHtml,callback);
	},
	//设置当前行的状态
	_setRuleStatu: function(el){
		var $tr = $(el).closest("tr");
		var val = this._getAttrVal(el);	
		var className = $tr.attr("data-classname");
		var configpagebz = $tr.attr("data-configpagebz");
		if(val == "Y"){
			$tr.find("input.adv_first_msg").attr("disabled",null);
			if(configpagebz == "Y"){
				$tr.find("a.adv_rul_set").css("color","#0088cc").click(function(){
					SurveyBuild.setRuleConfigPage(this)
				});
			}
		}else{
			$tr.find("input.adv_first_msg").attr("disabled","disabled");		
			$tr.find("a.adv_rul_set").css("color","#eee").unbind("click");
			$tr.find("a.adv_rul_set").removeAttr("onclick");
		}
		
	},
	//设置rule的提示信息
	saveRuleMsg:function(el,key){
		if(!key)return;
		var instanceId = $("#question-edit").attr("data_id");
		var rules = this._data[instanceId]["rules"];
		var val = this._getAttrVal(el);
		var className = $(el).closest("tr").attr("data-classname");	
		rules[className]["messages"][key]["value"] =val ; 
	},
	//设置常用控件的启用标识
	saveCommonRulesBz:function(el,key){
		this.saveAttr(el, key);
		this._setRuleStatu(el);
	},
	//设置rules启用标识
	saveRulesBz:function(el,ruleClassName){
		if(!el || !ruleClassName)return;
		var instanceId = $("#question-edit").attr("data_id");
		var val = this._getAttrVal(el);
		var rules = this._data[instanceId]["rules"];
		var currentRule = this._data[instanceId]["rules"][ruleClassName];
		if(!rules[ruleClassName] && currentRule){
			rules[ruleClassName] = currentRule;
		}
		if(rules[ruleClassName]){
			if(val == "Y"){		
				rules[ruleClassName]["isEnable"] = "Y"
			}else{
				rules[ruleClassName]["isEnable"] = "N"
			}	
			this._setRuleStatu(el);
		}			
	},
	saveLevel1Attr:function(el,attrNameLevel0,rownumber,attrNameLevel1){
		if(!el || !attrNameLevel0 || !rownumber || !attrNameLevel1)return;
		var instancdId = $("#question-edit").attr("data_id");
		var val = this._getAttrVal(el);				
		try{
			this._data[instancdId][attrNameLevel0][rownumber][attrNameLevel1] = val;
		}catch(e){};
		this.is_edit=true;
	},
	checkAttrVal:function(){
		var $liActive = $("#question-box>li.active");
		var checkBz = true;
		var tipObj;
		if($liActive.length == 0) {
            return checkBz
        }
		var d = $liActive.attr("data_id");
		var $edit_box = $("#question-edit");
		var $itemId = $edit_box.find(".edit_itemId");
		var $itemName = $edit_box.find(".edit_itemName");
		if(!$itemId.val()){
			checkBz = false;
			msg = "信息项编号不能为空"
			tipObj = $itemId;				
		}else if(!$itemName.val().length > 30){
			checkBz = false;
			msg = "信息项编号长度不能超过30";
			tipObj = $itemId;
		}else if(!$itemName.val()){
			checkBz = false;
			msg = "信息项名称不能为空"
			tipObj = $itemName;
		}
		if(checkBz == false){
			this.fail(tipObj,msg);
			return checkBz;
		};
		if(this._data[d]._validator){
			return this._data[d]._validator(this._data[d]);
		}else{
			return checkBz;
		}
	},
	_getRules:function(instanceId){
		var rules  = [] ;
		var i = $.inArray(this._data[instanceId]["classname"],this._componentIndex);
		if(i>-1){
			rules = this._componentConfig[i]["rules"];
		}
		return rules;
	},
	openMoadal:function(content,CallBack){
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
	      })
	     if(CallBack && typeof CallBack == "function"){CallBack()} ;
	},
	_getMessagesFirstKey:function(mes){
		for(var key in mes){
			if(key!="len"){
				break;
			}
		}
		return key;
	},
	RulesSet:function(el){
		var instanceId = $("#question-edit").attr("data_id");
		var rules = this._data[instanceId]["rules"];
		var itemName = this._data[instanceId].itemName;
		var me = this,onchange="",inputDisabled,linkDiasabled;
		var ruleSetPage = "";
		ruleSetPage = '<div class="modal-header"><h4>规则高级设置</h4></div>';
		ruleSetPage +='<div id="modal-question-advanced" class="modal-body"><fieldset>信息项名称：'+itemName+'</fieldset>';
		ruleSetPage +='<fieldset><table id="table-advanced-relus" class="table table-hover table-bordered"><tr><th width="50px">启用</th><th width="100px">规则名称</th><th>提示信息</th><th width="50px">设置</th></tr>';
		$.each(rules,function(classname,rec){
			var msgFirstKey = me._getMessagesFirstKey(rec["messages"]);
			var msg = rec["messages"]["len"] > 0 ? rec["messages"][msgFirstKey]["value"] : "";
			//校验规则是否启用，后台配置信息中是否启用，当前实例对象中是否启用了该校验规则
			var checked =  rec["isEnable"]=="Y"?"checked='checked'":"";
			var _data_rule = me._data[instanceId][classname];
			if(_data_rule){
				if(_data_rule["isEnable"] == "Y"){checked = "checked='checked'";}else{checked == ""};
			}
			
			if(!checked){
				inputDisabled="disabled='disabled'";
			}else{
				inputDisabled = "";
			}
			
			//校验规则启用属性的设置
			if(classname == "RequireValidator"){
				onchange="SurveyBuild.saveCommonRulesBz(this,\'isRequire\')";
			}else if(classname == "CharLenValidator"){
				onchange="SurveyBuild.saveCommonRulesBz(this,\'isCheckStrLen\')";
			}else if(classname == "NumSizeValidator"){
				onchange="SurveyBuild.saveCommonRulesBz(this,\'isNumSize\')";
			}else{
				onchange="SurveyBuild.saveRulesBz(this,\'"+classname+"\')";
			}
			
			//设置连接是否可用，提示信息小于2、配置了该校验类参数设置页面的标识为false、或没有配置该校验类
			var configpagebz = "Y";
			if(rec["messages"]["len"]<2 && ValidationRules && (!ValidationRules[classname] || (ValidationRules[classname] && ValidationRules[classname].is_configpage == false))){
				configpagebz = "N";
			}
			
			ruleSetPage += '<tr rule-id="'+rec.ruleId+'" data-classname="'+classname+'" data-configpagebz="'+configpagebz+'" ><td><label class="checkbox inline" ><input type="checkbox" '+(checked)+' onchange="'+onchange+'"></label></td>';
			ruleSetPage += '<td>'+rec.ruleName+'</td><td><input type="text" value="'+msg+'" style="width:85%;" onkeyup="SurveyBuild.saveRuleMsg(this,\''+msgFirstKey+'\')" class="adv_first_msg" '+inputDisabled+'></td><td><a href="javascript:;"  class="adv_rul_set" '+(checked !="" && configpagebz =="Y" ?' onclick="SurveyBuild.setRuleConfigPage(this)" ':'style="color:#eee;"')+'>设置</a></td>';
			ruleSetPage += '</tr>'
		});
		ruleSetPage += "</table><fieldset></div>"
		//ruleSetPage	+= '<div class="modal-footer clearfix"><button class="btn btn-warning pull-right" onclick="$.fancybox.close();"><i class="icon-ok"></i>关闭</button></div>';
		this.openMoadal(ruleSetPage);
	},
    add: function(f, a) {
		$("#y").show();
        this.is_edit = true;
        this._count == 0 && $("#question-new").css("display", "none");
        ++this._count;
        var me = this;
        var callback = function(f){
        	var d = "A" + ( + new Date());
        	var component = me._data[d] = new me.comClass[f]();
        	if(!component["instanceId"]){component["instanceId"] = d}
        	if(!component["orderby"]){component["orderby"] = me._count+""}
        	if(!component["itemId"]){component["itemId"] = "TZ_"+me._count}
        	//设置控件默认包含的校验规则
        	var rules =  this._getRules(d);
        	$.each(rules,function(classname,rec){
        		if(rec["isEnable"]=="Y"){
        			component["rules"][classname] =rec;
        			if(classname == "RequireValidator"){
        				component["isRequire"]="Y";
        			}else if(classname == "CharLenValidator"){
        				component["isCheckStrLen"]="Y";
        			}else if(classname == "NumSizeValidator"){
	        			component["isNumSize"]="Y";
	        		}
        		}
        	});
        	//把校验规则Copy到实例对象中
        	component["rules"] = cloneObj(rules);
        	component._init && component._init(d);
        	 if (a) {
     			setTimeout(function() {$("#y").animate({top:'250px'},"slow",function(){$("#y").hide();$("#y").css("top","0");})},650);
                var b = -1;
                $("#question-box>li").each(function(e) {
                    if ($(this).attr("data-classname")) {
                        b = e;
                        $(this).remove();
                        return
                    }
                });
                if (b > 0) {
                    $(me._html(d)).insertAfter($("#question-box>li").eq(b - 1)).click();
                } else {
                    $(me._html(d)).prependTo($("#question-box")).click();
                }
             } else {
                $("#question-box").append(me._html(d));
                $("html,body").scrollTop($(document).height());
     			setTimeout(function() {$("#y").animate({top:'250px'},"slow",function(){$("#y").hide();$("#y").css("top","0");})},650);

             }
        };
        this.loadScript(f,callback);
      
    },
    changeDelChl: function(a) {
        if (a) {
            for (var b in a) {
                if (!a[b].hasOwnProperty("add")) {
                    this._qid.push(a[b].qid)
                }
            }
        }
    },
    changeDelOpt: function(b) {
        if (b) {
            for (var a in b) {
                if (!b[a].hasOwnProperty("add")) {
                    this._oid.push(b[a].oid)
                }
            }
        }
    },
    changeType: function(h, d) {
        this.is_edit = true;
        var j = $(d).val(),
        k = "A" + ( + new Date()),
        a = null,
        g = null;
        if (this._data[h].hasOwnProperty("option")) {
            var a = this._data[h].option;
            delete this._data[h].option
        }
        if (this._data[h].hasOwnProperty("child")) {
            var g = this._data[h].child;
            delete this._data[h].child
        }
        this._data[h].edit = 1;
        if (j == "select") {
            var f = {};
            if (a) {
                for (var e in a) {
                    f[e] = {
                        qid: h,
                        code: a[e]["code"],
                        txt: a[e]["txt"],
                        orderby: a[e]["orderby"],
                        is_rand: a[e]["is_rand"]
                    };
                    if (a[e].hasOwnProperty("add")) {
                        f[e].add = 1
                    } else {
                        f[e].oid = a[e].oid;
                        f[e].edit = 1
                    }
                }
            } else {
                for (var e = 1; e <= 3; ++e) {
                    f[k + "_" + e] = {
                        qid: h,
                        add: 1,
                        code: e,
                        txt: "选项" + e,
                        orderby: e,
                        is_rand: 0
                    }
                }
            }
            this._data[h].qtype = 1;
            this._data[h].option = f;
            delete this._data[h].settings
        } else {
            if (j == "S") {
                var f = {},
                c = {
                    format: 1
                };
                if (a) {
                    for (var e in a) {
                        f[e] = {
                            qid: h,
                            code: a[e]["code"],
                            txt: a[e]["txt"],
                            orderby: a[e]["orderby"],
                            is_rand: a[e]["is_rand"],
                            settings: {
                                other: a[e]["settings"] && a[e].settings.other ? a[e].settings.other: 0
                            }
                        };
                        if (a[e].hasOwnProperty("add")) {
                            f[e].add = 1
                        } else {
                            f[e].oid = a[e].oid;
                            f[e].edit = 1
                        }
                    }
                } else {
                    for (var e = 1; e <= 3; ++e) {
                        f[k + "_" + e] = {
                            qid: h,
                            add: 1,
                            code: e,
                            txt: "选项" + e,
                            orderby: e,
                            is_rand: 0,
                            settings: {
                                other: 0
                            }
                        }
                    }
                }
                if (this._data[h].settings && this._data[h].settings.format) {
                    c.format = this._data[h].settings.format
                }
                this._data[h].qtype = 2;
                this._data[h].option = f;
                this._data[h].settings = c
            } else {
                if (j == "M") {
                    var f = {},
                    c = {
                        format: 1,
                        min: 0,
                        max: 0
                    };
                    if (a) {
                        for (var e in a) {
                            f[e] = {
                                qid: h,
                                code: a[e]["code"],
                                txt: a[e]["txt"],
                                orderby: a[e]["orderby"],
                                is_rand: a[e]["is_rand"],
                                settings: {
                                    other: a[e]["settings"] && a[e].settings.other ? a[e].settings.other: 0,
                                    exclusive: a[e]["settings"] && a[e]["settings"]["exclusive"] ? a[e]["settings"]["exclusive"] : 0
                                }
                            };
                            if (a[e].hasOwnProperty("add")) {
                                f[e].add = 1
                            } else {
                                f[e].oid = a[e].oid;
                                f[e].edit = 1
                            }
                        }
                    } else {
                        for (var e = 1; e <= 3; ++e) {
                            f[k + "_" + e] = {
                                qid: h,
                                add: 1,
                                code: e,
                                txt: "选项" + e,
                                orderby: e,
                                is_rand: 0,
                                settings: {
                                    other: 0,
                                    exclusive: 0
                                }
                            }
                        }
                    }
                    if (this._data[h].settings && this._data[h].settings.format) {
                        c.format = this._data[h].settings.format
                    }
                    this._data[h].qtype = 3;
                    this._data[h].option = f;
                    this._data[h].settings = c
                } else {
                    if (j == "GS") {
                        var b = {},
                        f = {},
                        c = {
                            direction: "X",
                            type: "S",
                            fixed: 0,
                            mean: 0
                        };
                        if (g) {
                            for (var e in g) {
                                b[e] = {
                                    parent_id: h,
                                    code: g[e].code,
                                    question: g[e].question,
                                    orderby: g[e].orderby,
                                    is_rand: g[e].is_rand,
                                    settings: {
                                        mandatory: 1,
                                        other: 0,
                                        fixedwidth: ""
                                    }
                                };
                                if (g[e].hasOwnProperty("add")) {
                                    b[e].add = 1
                                } else {
                                    b[e].edit = 1;
                                    b[e].qid = g[e].qid
                                }
                            }
                        } else {
                            for (var e = 1; e <= 3; ++e) {
                                b[k + "_" + e] = {
                                    add: 1,
                                    parent_id: h,
                                    code: e,
                                    question: "子问题" + e,
                                    orderby: e,
                                    is_rand: 0,
                                    settings: {
                                        mandatory: 1,
                                        other: 0,
                                        fixedwidth: ""
                                    }
                                }
                            }
                        }
                        if (a) {
                            for (var e in a) {
                                f[e] = {
                                    qid: h,
                                    code: a[e]["code"],
                                    txt: a[e]["txt"],
                                    orderby: a[e]["orderby"],
                                    is_rand: a[e]["is_rand"],
                                    settings: {
                                        other: a[e].settings.hasOwnProperty("other") ? a[e].settings.other: 0,
                                        fixedwidth: a[e].settings.hasOwnProperty("fixedwidth") ? a[e].settings.fixedwidth: ""
                                    }
                                };
                                if (a[e].hasOwnProperty("add")) {
                                    f[e].add = 1
                                } else {
                                    f[e].oid = a[e].oid;
                                    f[e].edit = 1
                                }
                            }
                        } else {
                            for (var e = 1; e <= 3; ++e) {
                                f[k + "_" + e] = {
                                    qid: k,
                                    add: 1,
                                    code: e,
                                    txt: "选项" + e,
                                    orderby: e,
                                    is_rand: 0,
                                    settings: {
                                        other: 0,
                                        fixedwidth: ""
                                    }
                                }
                            }
                        }
                        if (this._data[h].settings && this._data[h].settings.fixed) {
                            c.fixed == this._data[h].settings.fixed
                        }
                        this._data[h].qtype = 5;
                        this._data[h].settings = c;
                        this._data[h].child = b;
                        this._data[h].option = f
                    } else {
                        if (j == "GM") {
                            var b = {},
                            f = {},
                            c = {
                                direction: "X",
                                type: "M",
                                fixed: 0
                            };
                            if (g) {
                                for (var e in g) {
                                    b[e] = {
                                        parent_id: h,
                                        code: g[e].code,
                                        question: g[e].question,
                                        orderby: g[e].orderby,
                                        is_rand: g[e].is_rand,
                                        settings: {
                                            mandatory: 1,
                                            other: 0,
                                            fixedwidth: ""
                                        }
                                    };
                                    if (g[e].hasOwnProperty("add")) {
                                        b[e].add = 1
                                    } else {
                                        b[e].edit = 1;
                                        b[e].qid = g[e].qid
                                    }
                                }
                            } else {
                                for (var e = 1; e <= 3; ++e) {
                                    b[k + "_" + e] = {
                                        add: 1,
                                        parent_id: h,
                                        code: e,
                                        question: "子问题" + e,
                                        orderby: e,
                                        is_rand: 0,
                                        settings: {
                                            mandatory: 1,
                                            other: 0,
                                            fixedwidth: ""
                                        }
                                    }
                                }
                            }
                            if (a) {
                                for (var e in a) {
                                    f[e] = {
                                        qid: h,
                                        code: a[e]["code"],
                                        txt: a[e]["txt"],
                                        orderby: a[e]["orderby"],
                                        is_rand: a[e]["is_rand"],
                                        settings: {
                                            other: a[e].settings.hasOwnProperty("other") ? a[e].settings.other: 0,
                                            exclusive: a[e]["settings"] && a[e]["settings"]["exclusive"] ? a[e]["settings"]["exclusive"] : 0,
                                            fixedwidth: a[e].settings.hasOwnProperty("fixedwidth") ? a[e].settings.fixedwidth: ""
                                        }
                                    };
                                    if (a[e].hasOwnProperty("add")) {
                                        f[e].add = 1
                                    } else {
                                        f[e].oid = a[e].oid;
                                        f[e].edit = 1
                                    }
                                }
                            } else {
                                for (var e = 1; e <= 3; ++e) {
                                    f[k + "_" + e] = {
                                        qid: h,
                                        add: 1,
                                        code: e,
                                        txt: "选项" + e,
                                        orderby: e,
                                        is_rand: 0,
                                        settings: {
                                            other: 0,
                                            exclusive: 0,
                                            fixedwidth: ""
                                        }
                                    }
                                }
                            }
                            if (this._data[h].settings && this._data[h].settings.fixed) {
                                c.fixed == this._data[h].settings.fixed
                            }
                            this._data[h].qtype = 5;
                            this._data[h].settings = c;
                            this._data[h].child = b;
                            this._data[h].option = f
                        } else {
                            if (j == "GO") {
                                var b = {},
                                f = {},
                                c = {
                                    direction: "X",
                                    type: "O",
                                    fixed: 0
                                };
                                if (g) {
                                    for (var e in g) {
                                        b[e] = {
                                            parent_id: h,
                                            code: g[e].code,
                                            question: g[e].question,
                                            orderby: g[e].orderby,
                                            is_rand: g[e].is_rand,
                                            settings: {
                                                mandatory: 1,
                                                other: 0,
                                                fixedwidth: ""
                                            }
                                        };
                                        if (g[e].hasOwnProperty("add")) {
                                            b[e].add = 1
                                        } else {
                                            b[e].edit = 1;
                                            b[e].qid = g[e].qid
                                        }
                                    }
                                } else {
                                    for (var e = 1; e <= 3; ++e) {
                                        b[k + "_" + e] = {
                                            add: 1,
                                            parent_id: h,
                                            code: e,
                                            question: "子问题" + e,
                                            orderby: e,
                                            is_rand: 0,
                                            settings: {
                                                mandatory: 1,
                                                other: 0,
                                                fixedwidth: ""
                                            }
                                        }
                                    }
                                }
                                if (a) {
                                    for (var e in a) {
                                        f[e] = {
                                            qid: h,
                                            code: a[e]["code"],
                                            txt: a[e]["txt"],
                                            orderby: a[e]["orderby"],
                                            is_rand: a[e]["is_rand"],
                                            settings: {
                                                other: a[e]["settings"] && a[e].settings.other ? a[e].settings.other: 0,
                                                fixedwidth: a[e].settings.hasOwnProperty("fixedwidth") ? a[e].settings.fixedwidth: ""
                                            }
                                        };
                                        if (a[e].hasOwnProperty("add")) {
                                            f[e].add = 1
                                        } else {
                                            f[e].oid = a[e].oid;
                                            f[e].edit = 1
                                        }
                                    }
                                } else {
                                    for (var e = 1; e <= 3; ++e) {
                                        f[k + "_" + e] = {
                                            qid: h,
                                            add: 1,
                                            code: e,
                                            txt: "选项" + e,
                                            orderby: e,
                                            is_rand: 0,
                                            settings: {
                                                other: 0,
                                                fixedwidth: ""
                                            }
                                        }
                                    }
                                }
                                if (this._data[h].settings && this._data[h].settings.fixed) {
                                    c.fixed == this._data[h].settings.fixed
                                }
                                this._data[h].qtype = 5;
                                this._data[h].settings = c;
                                this._data[h].child = b;
                                this._data[h].option = f
                            }
                        }
                    }
                }
            }
        }
        $(this._html(h)).find(".question-answer").replaceAll("#q" + h + " .question-answer");
        $("#question-edit").html(this._edit(h))
    },
    edit: function(f) {
        if (!this.checkAttrVal()){
        	return;
        }
        $("#href2").click();
        var e = $(f),
        h = e.attr("data_id");
        e.siblings().removeClass("active");
        e.addClass("active");
        $("#build-right").css("height", "auto");

        var a = this._edit(h);
        var b = $(window).scrollTop(),
        d = $(f).offset().top - $("#question-wrap").offset().top - 150;
		$("#question-edit").html(a);
		$("#question-edit").attr("data_id",h);
		
        SurveyBuild._optionMove()
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
                c = a.item.attr("id").split("-")[0];
                a.item.parent().find("tr").each(function(e) {
                    var f = $(this).attr("id").split("-")[1];
                    d[f] = SurveyBuild._data[c]["child"][f];
                    d[f].edit = 1;
                    d[f]["orderby"] = e + 1
                });
                SurveyBuild._data[c].child = d;
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
                d = b.item.attr("id").split("-")[0];
                b.item.parent().find("tr").each(function(e) {
                    var f = $(this).attr("id").split("-")[1];
                    a[f] = SurveyBuild._data[d].option[f];
                    a[f].edit = 1;
                    a[f]["orderby"] = e + 1
                });
                SurveyBuild._data[d].option = a;
                $(SurveyBuild._html(d)).find(".question-answer").replaceAll("#q" + d + " .question-answer")
            }
        })
    },
    _edit: function(d) {
    	var data = this._data[d];
    	var e= '<fieldset><legend><span class="edit_item_label ">信息项编号：</span><input type="text" onkeyup="SurveyBuild.saveAttr(this,\'itemId\')" value="' + data.itemId + '" class="medium edit_itemId" onpaste="return false" ondragenter="return false" oncontextmenu="return false;" maxlength="30" /></legend></fieldset><span class="edit_item_label">信息项名称：</span><input type="text" class="medium edit_itemName" onkeyup="SurveyBuild.saveAttr(this,\'itemName\')" value="' + data.itemName + '"/>';
      	return e+this._data[d]._edit(this._data[d]);
    },
    changeMobile: function(a, b) {
        this.is_edit = true;
        this._data[b].edit = 1;
        this._data[b].settings.mobile = $(a).prop("checked") ? 1 : 0
    },
    upload: function(b) {
        var a = '<div class="modal-header"><h4>插入图片</h4></div>';
        a += '<div id="modal-question-upload" class="modal-body"><div class="upload-search"><form target="iframe' + b + '" action="/ajax/build" enctype="multipart/form-data" method="post"><input id="' + b + '-file" type="file" name="file" /><input type="hidden" name="qid" value="' + b + '" /><input type="hidden" name="op" value="upload" /><button type="submit" class="btn btn-primary btn-small" onclick="loading($(\'#modal-question-upload\'))"><i class="icon-upload-alt"></i>上传图片</button></form><iframe id="iframe' + b + '" width="1" scrolling="no" height="1" style="display:none;" name="iframe' + b + '"></iframe></div><div class="upload-gallery clearfix"></div></div>';
        a += '<div class="modal-footer clearfix"><button class="btn pull-left" onclick="$.fancybox.close()"><i class="icon-ban-circle"></i>取消</button><button class="btn btn-warning pull-right" onclick="SurveyBuild.uploadSave(\'' + b + '\')"><i class="icon-ok"></i>确定</button></div>';
        $.fancybox.open({
            content: a,
            afterShow: function() {
                $.post("/ajax/build", {
                    op: "loadGallery"
                },
                function(f) {
                    var e = "";
                    for (var d in SurveyBuild._data[b].option) {
                        var c = $.inArray(SurveyBuild._data[b].option[d].settings.pic, f);
                        if (c != -1) {
                            f.splice(c, 1)
                        }
                    }
                    for (var d in f) {
                        e += '<a class="thumbnail" href="javascript:void(0);"><img src="' + f[d] + '" /></a>'
                    }
                    $("#modal-question-upload>.upload-gallery").html(e)
                },
                "JSON");
                $("#modal-question-upload").on("click", "a.thumbnail",
                function() {
                    $(this).toggleClass("active")
                })
            }
        })
    },
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
        if (this._data[c].option instanceof Array) {
            this._data[c].option = {}
        }
        $("#modal-question-upload a.active").each(function(d) {++a;
            SurveyBuild._data[c].option[b + "_" + d] = {
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
    copy: function(g) {
        this.is_edit = true;
        ++this._count;
        var d = "A" + ( + new Date());
        var data = cloneObj(this._data[g]);
        var me = this;
        data["instanceId"] = d;
        data["itemId"] = "TZ_"+this._count;
        var component = me._data[d] = new me.comClass[data["classname"]](data);
    	component._init && component._init(d);
    	var $instance= $(this._html(d));
    	$instance.insertAfter("#question-box>li.active").effect("highlight", {},
        300,
        function() {
    		$instance.click();
            SurveyBuild.sort()
        });
        return false
    },
    remove: function(a, b) {
        if (confirm("确定删除吗？")) {
            this.is_edit = true;
            if (this._data.hasOwnProperty(b)) {
                 --this._count;
                 $("#question-box li[data_id='"+b+"']").remove()
               // if (!this._data[b].hasOwnProperty("add") && this._data[b].qtype != 100) {
               //     this._qid.push(this._data[b].qid)
               // }
                delete this._data[b];
                $("#question-edit").empty();
                if ($("#question-box>li").length == 0) {
                    $("#question-new").css("display", "block")
                } else {
                    this.sort()
                }
            } else {
                this._error("非法操作")
            }
        }
        if (a.stopPropagation) {
            a.stopPropagation()
        }
        a.cancelBubble = true;
        return false
    },
    sort: function() {
        this.is_edit = true;
        var b = 0,
        a = 0;
        $("#question-box>li").each(function(c) {
            var e = $(this).attr("data_id");
            ++a;
            SurveyBuild._data[e].orderby = a
        })
    },
    _html: function(d) {
    	var c = '<li data_id="' + d + '" onclick="SurveyBuild.edit(this)">';
    	//通过实例对象中的类名，找到类并调用_gethtml方法
    	//c += this.comClass[this._data[d].classname]._getHtml(this._data[d]);
    	c +=this._data[d]._getHtml(this._data[d])
    	c +='<div class="question-action"><a class="build-icon-minus" title="删除" onclick="return SurveyBuild.remove(event,\''+d+'\')"></a><a class="build-icon-copy" title="复制" onclick="SurveyBuild.copy(\''+d+'\')"></a><i class="build-icon-arrow"></i></div></li>'
		return c;
    },
    editor: function(a) {
        this.is_edit = true;
        var b = "K_" + a;
        $.fancybox.open({
            content: '<textarea id="' + b + '">' + SurveyBuild._data[a].question + "</textarea>",
            minWidth: 1002,
            minHeight: 482,
            beforeShow: function() {
                window.editor = KindEditor.create("#" + b, {
                    items: ["source", "fontname", "fontsize", "|", "forecolor", "hilitecolor", "bold", "italic", "underline", "strikethrough", "removeformat", "|", "justifyleft", "justifycenter", "justifyright", "insertorderedlist", "insertunorderedlist", "|", "emoticons", "image", "multiimage", "flash", "link", "wordpaste"],
                    width: "1000px",
                    height: "480px",
                    allowFileManager: true,
                    filterMode: false
                })
            },
            beforeClose: function() {
                var c = editor.html();
                SurveyBuild._data[a].edit = 1;
                SurveyBuild._data[a].question = c;
                $("#" + a).val(c);
                $("#q" + a).find(".question-question").html(c);
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
        var b = this._data[g].qtype,
        d = this._data[g].option;
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
                e += "<tr><td>" + d[c]["txt"] + '</td><td><input type="checkbox" class="rand" data-id="' + c + '" value="1"' + (d[c]["is_rand"] == 1 ? " checked": "") + ' /></td><td><input type="checkbox" onchange="$(\'.defaultval\').not(this).prop(\'checked\',false);" class="defaultval" data-id="' + c + '" value="1"' + (this._data[g].settings.hasOwnProperty("defaultval") && $.inArray(d[c].code, this._data[g].settings.defaultval) > -1 ? " checked": "") + " /></td></tr>"
            }
        } else {
            if (b == 2) {
                e += '<th style="width:50px;">其它&nbsp;&nbsp;<a href="#" class="popo" data-placement="left" data-content="勾选后该选项后出现文本框。常用于其它，请注明_____"><i class="icon-question-sign"></i></a></th><th style="width:55px;">默认选择</th></tr>';
                for (var c in d) {
                    e += "<tr><td>" + d[c]["txt"] + '</td><td><input type="checkbox" class="other" data-id="' + c + '" value="1"' + (d[c].settings.other == 1 ? " checked": "") + ' /></td><td><input type="checkbox" onchange="$(\'.defaultval\').not(this).prop(\'checked\',false);" class="defaultval" data-id="' + c + '" value="1"' + (this._data[g].settings.hasOwnProperty("defaultval") && $.inArray(d[c].code, this._data[g].settings.defaultval) > -1 ? " checked": "") + " /></td></tr>"
                }
            } else {
                if (b == 3) {
                    e += '<th style="width:50px;">其它&nbsp;&nbsp;<a href="#" class="popo" data-placement="left" data-content="勾选后该选项后出现文本框。常用于其它，请注明_____"><i class="icon-question-sign"></i></a></th><th style="width:30px;">排他</th><th style="width:55px;">默认选择</th></tr>';
                    for (var c in d) {
                        e += "<tr><td>" + d[c]["txt"] + '</td><td><input type="checkbox" class="rand" data-id="' + c + '" value="1"' + (d[c]["is_rand"] == 1 ? " checked": "") + ' /></td><td><input type="checkbox" class="other" data-id="' + c + '" value="1"' + (d[c].settings.other == 1 ? " checked": "") + ' /></td><td><input type="checkbox" class="exclusive" data-id="' + c + '" value="1"' + (d[c]["settings"]["exclusive"] == 1 ? " checked": "") + ' /></td><td><input type="checkbox" class="defaultval" data-id="' + c + '" value="1"' + (this._data[g].settings.hasOwnProperty("defaultval") && $.inArray(d[c].code, this._data[g].settings.defaultval) > -1 ? " checked": "") + " /></td></tr>"
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
        var g = this._data[j].option;
        var a = this._data[j].child;
        var b = '<div class="modal-header"><h4>问题题目设置</h4></div><div id="modal-question-advanced" class="modal-body"><fieldset><label class="checkbox inline" for="is_mandatory"><input type="checkbox"' + (this._data[j]["is_mandatory"] == 1 ? " checked": "") + ' value="1" id="is_mandatory"/> 是否必填&nbsp;&nbsp;<a href="#" class="popo" data-content="用户需要填写这个信息项。"><i class="icon-question-sign"></i></a></label>';
        if (this._data[j].qtype == 5 && this._data[j].settings.type != "G") {
            b += '<label class="checkbox inline" for="fixed"><input type="checkbox"' + (this._data[j].settings.fixed == 1 ? " checked": "") + ' value="1" id="fixed"> 固定表头&nbsp;&nbsp;<a href="#" class="popo" data-content="表头会随着滚动条移动"><i class="icon-question-sign"></i></a></label><label class="checkbox inline" for="direction"><input type="checkbox" value="1" id="direction"' + (this._data[j].settings.direction == "Y" ? " checked": "") + " onchange=\"SurveyBuild.changeDirection('" + j + '\')"/> 矩阵翻转&nbsp;&nbsp;<a href="#" class="popo" data-content="转为列矩阵，每列单选、多选"><i class="icon-question-sign"></i></a></label>';
            if (this._data[j].settings.type == "S") {
                b += '<label class="checkbox inline" for="mean"><input type="checkbox" value="1" id="mean"' + (this._data[j].settings.hasOwnProperty("mean") && this._data[j].settings.mean == 1 ? " checked": "") + "/> 报表统计计算平均值</label>"
            }
        }
        b += "</fieldset>";
        if (this._data[j].qtype == 17) {
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
            if (this._data[j].qtype == 5) {
                b += '<fieldset><table id="table-advanced-child" class="table table-hover table-bordered"><tr><th>子问题</th><th style="width:75px;"><input type="checkbox" onchange="SurveyBuild.allRand(this,1)" /> 随机&nbsp;&nbsp;<a href="#" class="popo" data-placement="left" data-content="选中的选项进行随机，未选中的不变"><i class="icon-question-sign"></i></a></th><th style="width:50px;"><input type="checkbox" onchange="SurveyBuild.allMandatory(this)" /> 必答</th><th style="width:50px;">其它&nbsp;&nbsp;<a href="#" class="popo" data-placement="left" data-content="勾选后该选项后出现文本框。常用于其它，请注明_____"><i class="icon-question-sign"></i></a></th>';
                if (this._data[j].settings.direction == "Y") {
                    b += '<th style="width:75px">列宽（px）</th>'
                }
                b += "</tr>";
                var d = 1;
                for (var f in a) {
                    b += "<tr><td>" + a[f]["question"] + '</td><td><input type="checkbox" class="crand" data-id="' + f + '" value="1"' + (a[f]["is_rand"] == 1 ? " checked": "") + ' /></td><td><input type="checkbox" class="mandatory" data-id="' + f + '" value="1"' + (a[f].settings.mandatory == 1 ? " checked": "") + ' /></td><td><input type="checkbox" class="cother" data-id="' + f + '" value="1"' + (a[f].settings.other == 1 ? " checked": "") + " /></td>";
                    if (this._data[j].settings.direction == "Y") {
                        b += '<td><input type="text" data-id="' + f + '" value="' + (a[f].settings.hasOwnProperty("fixedwidth") ? a[f].settings.fixedwidth: "") + '" onfocus="this.select()" onkeypress="return SurveyBuild.keypressNum(event)" class="input-mini fixedwidth" maxlength="3" tabindex="' + d + '"/></td>'; ++d
                    }
                    b += "</tr>"
                }
                b += "</table></fieldset>";
                if (this._data[j].settings.type == "G") {
                    b += '<fieldset><p>最高多少分：<select id="max" onchange="SurveyBuild.changeGG(\'' + j + "')\">";
                    for (var f = 2; f <= 10; ++f) {
                        b += '<option value="' + f + '"' + (f == this._data[j].settings.max ? " selected": "") + ">" + f + "</option>"
                    }
                    b += '</select></p><table id="table-advanced-option" class="table table-hover table-bordered"><thead><tr><th style="width:60px;text-align:center;">分值</th><th style="text-align:left;">分值描述（可为空）</th></tr></thead><tbody>';
                    for (var f in g) {
                        b += '<tr><td style="text-align:center">' + g[f].code + '</td><td style="text-align:left"><input type="text" value="' + g[f].txt + '" tabindex="' + d + '"/></td></tr>'; ++d
                    }
                    b += "</tbody></table></fieldset>"
                } else {
                    d = 1;
                    var n = '<fieldset><table id="table-advanced-option" class="table table-hover table-bordered"><tr><th>选项</th>';
                    if (this._data[j].settings.type != "O") {
                        n += '<th style="width:50px;">其它&nbsp;&nbsp;<a href="#" class="popo" data-placement="left" data-content="勾选后该选项后出现文本框。常用于其它，请注明_____"><i class="icon-question-sign"></i></a></th>'
                    }
                    if (this._data[j].settings.type == "M") {
                        n += '<th style="width:30px;">排他</th>';
                        if (this._data[j].settings.direction == "X") {
                            n += '<th style="width:75px;">列宽（px）</th>'
                        }
                        n += "</tr>";
                        for (var f in g) {
                            n += "<tr><td>" + g[f]["txt"] + '</td><td><input type="checkbox" class="other" data-id="' + f + '" value="1"' + (g[f].settings.other == 1 ? " checked": "") + ' /></td><td><input type="checkbox" class="exclusive" data-id="' + f + '" value="1"' + (g[f]["settings"]["exclusive"] == 1 ? " checked": "") + " /></td>";
                            if (this._data[j].settings.direction == "X") {
                                n += '<td><input type="text" data-id="' + f + '" value="' + (g[f].settings.hasOwnProperty("fixedwidth") ? g[f].settings.fixedwidth: "") + '" onfocus="this.select()" onkeypress="return SurveyBuild.keypressNum(event)" class="input-mini fixedwidth" maxlength="3" tabindex="' + d + '"/></td>'; ++d
                            }
                            n += "</tr>"
                        }
                    } else {
                        if (this._data[j].settings.direction == "X") {
                            n += '<th style="width:75px;">列宽（px）</th>'
                        }
                        n += "</tr>";
                        for (var f in g) {
                            n += "<tr><td>" + g[f]["txt"] + '</td>';
                            if (this._data[j].settings.type != "O") {
                                n += '<td><input type="checkbox" class="other" data-id="' + f + '" value="1"' + (g[f].settings.other == 1 ? " checked": "") + " /></td>"
                            }
                            if (this._data[j].settings.direction == "X") {
                                n += '<td><input type="text" data-id="' + f + '" value="' + (g[f].settings.hasOwnProperty("fixedwidth") ? g[f].settings.fixedwidth: "") + '" onfocus="this.select()" onkeypress="return SurveyBuild.keypressNum(event)" class="input-mini fixedwidth" maxlength="3" tabindex="' + d + '"/></td>'; ++d
                            }
                            n += "</tr>"
                        }
                    }
                    n += "</table></fieldset>";
                    b += n
                }
            } else {
                if (this._data[j].hasOwnProperty("option")) {
                    b += this._advancedOption(j)
                }
            }
        }
        if (this._data[j].qtype == 3) {
            var h = 0,
            e = "",
            m = "";
            for (var f in g) {++h
            }
            var c = 0,
            l = 0;
            if (this._data[j].settings.min > 0) {
                c = this._data[j].settings.min
            }
            if (this._data[j].settings.max > 0) {
                l = this._data[j].settings.max
            }
            for (var f = 0; f <= h; ++f) {
                e += '<option value="' + f + '"' + (f == c ? " selected": "") + ">" + f + "</option>";
                m += '<option value="' + f + '"' + (f == l ? " selected": "") + ">" + f + "</option>"
            }
            b += '<fieldset>最少选择的答案数<select onchange="SurveyBuild.minSelect()" id="min">' + e + "</select></fieldset>";
            b += '<fieldset>最多选择的答案数<select onchange="SurveyBuild.maxSelect()" id="max">' + m + "</select></fieldset>"
        } else {
            if (this._data[j].qtype == 4 && !this._data[j].settings.hasOwnProperty("type")) {
                b += '<fieldset><p>字数范围</p><p>最少：<input type="text" class="input-mini" id="min" maxlength="5" value="' + this._data[j].settings.min + '">&nbsp;&nbsp;&nbsp;&nbsp;最多：<input type="text" class="input-mini" id="max" maxlength="5" value="' + this._data[j].settings.max + '"></p></fieldset>';
                b += '<fieldset><p>后缀：<input type="text" class="input-mini" id="suffix" value="' + (this._data[j].settings.hasOwnProperty("suffix") ? this._data[j].settings.suffix: "") + '">&nbsp;&nbsp;&nbsp;&nbsp;默认值：<input type="text" id="defaultval" value="' + (this._data[j].settings.hasOwnProperty("defaultval") ? this._data[j].settings.defaultval: "") + '"></p></fieldset>';
                b += '<fieldset><p>使用正则匹配<select id="preg" onchange="SurveyBuild.selectedPreg(this)"><option value="">不使用</option>';
                for (var f in this._preg) {
                    b += '<option value="' + this._preg[f][0] + '"' + (this._data[j].settings.preg == this._preg[f][0] ? ' selected="selected"': "") + ">" + this._preg[f][1] + "</option>"
                }
                b += '</select>&nbsp;&nbsp;<span id="minmaxnum" style="display:' + (this._data[j].settings.preg == "^\\d+$" || this._data[j].settings.preg == "^\\d+(\\.\\d+)?$" ? "inline": "none") + '">最小：<input type="text" class="input-mini" id="mininum" maxlength="9" value="' + (this._data[j].settings.hasOwnProperty("mininum") ? this._data[j].settings.mininum: "") + '">&nbsp;&nbsp;&nbsp;&nbsp;最大：<input type="text" class="input-mini" id="maxinum" maxlength="9" value="' + (this._data[j].settings.hasOwnProperty("maxinum") ? this._data[j].settings.maxinum: "") + '"></span></p></fieldset>'
            } else {
                if (this._data[j].qtype == 8) {
                    var h = 0;
                    for (var f in g) {++h
                    }
                    var k = this._data[j].settings.max != 0 ? this._data[j].settings.max: 0;
                    b += '<fieldset>排序的个数<select id="max"><option value="0">不限制</option>';
                    for (var h; h > 1; --h) {
                        b += '<option value="' + h + '"' + (h == k ? " selected": "") + ">" + h + "</option>"
                    }
                    b += "</select></fieldset>"
                } else {
                    if (this._data[j].qtype == 9) {
                        b += '<fieldset><p>所有答案总和等于：<input type="text" onkeypress="return SurveyBuild.keypressNum(event)" class="input-small" id="total" value="' + this._data[j].settings.total + '" maxlength="11">&nbsp;<span style="color:#f60">注：设置此项后，各项答案的值必须为数字</span></p></fieldset>'
                    } else {
                        if (this._data[j].qtype == 11) {
                            var h = 4,
                            k = "";
                            for (var f = 1; f <= h; ++f) {
                                k += '<option value="' + f + '"' + (f == this._data[j].settings.max ? " selected": "") + ">" + f + "</option>"
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
            e = this._data[d].child;
            for (var b in e) {
                $("#table-advanced-child").find("tr:eq(" + a + ")").append('<td><input type="text" data-id="' + b + '" value="' + (e[b].settings.hasOwnProperty("fixedwidth") ? e[b].settings.fixedwidth: "") + '" onfocus="this.select()" onkeypress="return SurveyBuild.keypressNum(event)" class="input-mini fixedwidth" maxlength="3" tabindex="' + a + '"/></td>'); ++a
            }
        } else {
            $("#table-advanced-child").find("tr th:last-child,tr td:last-child").remove();
            $("#table-advanced-option").find("tr:first").append('<th style="width:75px">列宽（px）</th>');
            var a = 1,
            c = this._data[d].option;
            for (var b in c) {
                $("#table-advanced-option").find("tr:eq(" + a + ")").append('<td><input type="text" data-id="' + b + '" value="' + (c[b].settings.hasOwnProperty("fixedwidth") ? c[b].settings.fixedwidth: "") + '" onfocus="this.select()" onkeypress="return SurveyBuild.keypressNum(event)" class="input-mini fixedwidth" maxlength="3" tabindex="' + a + '"/></td>'); ++a
            }
        }
    },
    optionBatch: function(e, d) {
        var a = '<div class="modal-header"><h4>批量编辑选项' + (d && this._data[e].settings.type != "G" ? "子问题": "选项") + '<span class="muted">(以回车分隔)</span></h4></div>';
        a += '<div id="modal-question-option" class="modal-body"><textarea id="' + e + '-batch" class="option-text">';
        if (d) {
            var f = this._data[e].child;
            for (var b in f) {
                a += f[b]["question"] + "\n"
            }
        } else {
            var c = this._data[e].option;
            for (var b in c) {
                a += c[b]["txt"] + "\n"
            }
        }
        a += "</textarea></div>";
        a += '<div class="modal-footer clearfix"><button class="btn pull-left" onclick="$.fancybox.close()"><i class="icon-ban-circle"></i>取消</button><button class="btn btn-warning pull-right" onclick="SurveyBuild.optionBatchSave(\'' + e + "'," + d + ')"><i class="icon-ok"></i>确认</button><label class="pull-right checkbox" style="margin-top:15px"><input type="checkbox" id="autocode" checked="checked" />自动编码</label></div>';
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
    optionBatchSave: function(l, o) {
        this.is_edit = true;
        var m = $.trim($("#" + l + "-batch").val()),
        c = $("#autocode").prop("checked") ? 1 : 0,
        a = m.split("\n"),
        e = a.length,
        n = "A" + ( + new Date()),
        f = 0,
        h = {},
        d = null;
        if (o) {
            var b = this._data[l].child;
            for (var g in b) {
                if (f >= e) {
                    if (!b[g].hasOwnProperty("add")) {
                        this._qid.push(b[g].qid)
                    }
                } else {
                    h[g] = b[g];
                    h[g].edit = 1;
                    h[g].orderby = f + 1;
                    if (c) {
                        h[g].code = f + 1
                    }
                    h[g].question = a[f]
                }++f
            }
            if (e > f) {
                var k = e - f;
                for (var g = 0; g < k; ++g) {
                    h[n + g] = {
                        parent_id: l,
                        add: 1,
                        code: c ? f + 1 : "",
                        question: a[f],
                        orderby: f + 1,
                        is_rand: 0,
                        settings: {
                            mandatory: 1,
                            other: 0,
                            fixedwidth: ""
                        }
                    }; ++f
                }
            }
            this._data[l].child = h
        } else {
            var b = this._data[l].option;
            for (var g in b) {
                if (f >= e) {
                    if (!b[g].hasOwnProperty("add")) {
                        this._oid.push(b[g].oid)
                    }
                } else {
                    h[g] = b[g];
                    h[g].edit = 1;
                    if (c) {
                        h[g].code = f + 1
                    }
                    h[g].txt = a[f]
                }++f
            }
            if (e > f) {
                var k = e - f;
                for (var g = 0; g < k; ++g) {++f;
                    h[n + g] = {
                        qid: l,
                        add: 1,
                        code: c ? f: "",
                        txt: a[f - 1],
                        orderby: f,
                        is_rand: 0
                    };
                    if (this._data[l].qtype == 2) {
                        h[n + g].settings = {
                            other: 0
                        }
                    } else {
                        if (this._data[l].qtype == 3) {
                            h[n + g].settings = {
                                other: 0,
                                exclusive: 0
                            }
                        } else {
                            if (this._data[l].qtype == 5) {
                                if (this._data[l].settings.type == "M") {
                                    h[n + g].settings = {
                                        other: 0,
                                        exclusive: 0,
                                        fixedwidth: ""
                                    }
                                } else {
                                    h[n + g].settings = {
                                        other: 0,
                                        fixedwidth: ""
                                    }
                                }
                            } else {
                                if (this._data[l].qtype == 9) {
                                    h[n + g].settings = {
                                        mandatory: 1,
                                        other: 0,
                                        preg: "",
                                        suffix: ""
                                    }
                                }
                            }
                        }
                    }
                }
            }
            this._data[l].option = h
        }
        $(this._html(l)).find(".question-answer").replaceAll("#q" + l + " .question-answer");
        $("#q" + l).click();
        $.fancybox.close()
    },
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
        this._data[k].sort = 1;
        var m = "";
        for (var e in this._data[k].child) {++g;
            b[e] = cloneObj(this._data[k].child[e]);
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
        this._data[k].child = b;
        if (this._data[k].qtype == 17) {
            $("#selectTitle").html(m);
            var l = 1,
            a = 1;
            for (var e in this._data[k].option) {
                var j = 1 * this._data[k].option[e].code;
                a = 1 + this._data[k].option[e].orderby;
                if (j > l) {
                    l = j
                }
            }
            var p = "o" + o;
            this._data[k].option[p] = {
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
    minusChild: function(f) {
        this.is_edit = true;
        if ($(f).parents("tr").siblings().length == 0) {
            this._error("至少要有一个子问题")
        } else {
            var e = $(f).parents("tr").attr("id").split("-"),
            g = e[0],
            h = e[1];
            if (this._data[g].child.hasOwnProperty(h)) {
                var b = $("#build-right").height();
                $("#build-right").height(b - 39);
                $(f).parents("tr").remove();
                if (this._data[g].settings.direction == "Y") {
                    var a = $("#q" + g + " tr th#c" + h).index();
                    $("#q" + g + " tr th#c" + h).remove();
                    $("#q" + g + " tr").each(function() {
                        $(this).find("td").eq(a).remove()
                    })
                } else {
                    $("#c" + h).parent().remove()
                }
                if (!this._data[g].child[h].hasOwnProperty("add")) {
                    this._qid.push(this._data[g].child[h].qid)
                }
                delete this._data[g].child[h];
                if (this._data[g].qtype == 17) {
                    $("#first" + h).remove();
                    var d = this._data[g].option;
                    for (var c in d) {
                        if ($option.settings["cid"] == h || "C" + d[c].settings.cid == h) {
                            delete this._data[g].option[c]
                        }
                    }
                }
            } else {
                this._error("非法操作")
            }
        }
    },
    plusOption: function(f) {
        this.is_edit = true;
        var m = $(f).parents("tr").attr("id").split("-"),
        k = m[0],
        b = m[1],
        l = 0,
        n = "A" + ( + new Date()),
        g = {},
        h = 0;
        for (var e in this._data[k].option) {
            var j = 1 * this._data[k].option[e].code;
            if (j > l) {
                l = j
            }
        }
        var a = ++l;
        var d = $("#build-right").height();
        $("#build-right").height(d + 39);
        $(f).parents("tr").after('<tr id="' + k + "-" + n + '"><td><input class="ocode" onpaste="return false" ondragenter="return false" oncontextmenu="return false;" maxlength="5" value="' + a + '" onkeyup="SurveyBuild.saveOCode(this)" type="text"></td><td><input class="option-txt" value="" onkeyup="SurveyBuild.saveTxt(this)" type="text"></td><td><a href="javascript:void(0);" class="text-success" onclick="SurveyBuild.plusOption(this);return false;"><i class="icon-plus-sign"></i></a>&nbsp;<a href="javascript:void(0);" class="text-warning" onclick="SurveyBuild.minusOption(this);return false;"><i class="icon-minus-sign"></i></a>&nbsp;<a href="javascript:void(0);" class="text-info option-move"><i class="icon-move"></i></a></td></tr>');
        this._data[k].sort = 1;
        for (var e in this._data[k].option) {
            h++;
            g[e] = cloneObj(this._data[k].option[e]);
            g[e].orderby = h;
            if (e == b) {++h;
                if (SurveyBuild._data[k]["qtype"] == 2) {
                    g[n] = {
                        qid: k,
                        add: 1,
                        code: a,
                        txt: "",
                        orderby: h,
                        is_rand: 0,
                        settings: {
                            other: 0
                        }
                    }
                } else {
                    if (SurveyBuild._data[k]["qtype"] == 3) {
                        g[n] = {
                            qid: k,
                            add: 1,
                            code: a,
                            txt: "",
                            orderby: h,
                            is_rand: 0,
                            settings: {
                                other: 0,
                                exclusive: 0
                            }
                        }
                    } else {
                        if (SurveyBuild._data[k]["qtype"] == 5) {
                            if (SurveyBuild._data[k]["settings"]["type"] == "M") {
                                g[n] = {
                                    qid: k,
                                    add: 1,
                                    code: a,
                                    txt: "",
                                    orderby: h,
                                    is_rand: 0,
                                    settings: {
                                        other: 0,
                                        exclusive: 0,
                                        fixedwidth: ""
                                    }
                                }
                            } else {
                                g[n] = {
                                    qid: k,
                                    add: 1,
                                    code: a,
                                    txt: "",
                                    orderby: h,
                                    is_rand: 0,
                                    settings: {
                                        other: 0,
                                        fixedwidth: ""
                                    }
                                }
                            }
                        } else {
                            if (SurveyBuild._data[k]["qtype"] == 9) {
                                g[n] = {
                                    qid: k,
                                    add: 1,
                                    code: a,
                                    txt: "",
                                    orderby: h,
                                    is_rand: 0,
                                    settings: {
                                        mandatory: 1,
                                        other: 0,
                                        preg: "",
                                        suffix: ""
                                    }
                                }
                            } else {
                                if (SurveyBuild._data[k]["qtype"] == 17) {
                                    g[n] = {
                                        qid: k,
                                        add: 1,
                                        code: a,
                                        txt: "",
                                        orderby: h,
                                        is_rand: 0,
                                        settings: {
                                            other: 0,
                                            cid: $("#selectTitle").val()
                                        }
                                    }
                                } else {
                                    g[n] = {
                                        qid: k,
                                        add: 1,
                                        code: a,
                                        txt: "",
                                        orderby: h,
                                        is_rand: 0
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        this._data[k].option = g;
        $(this._html(k)).find(".question-answer").replaceAll("#q" + k + " .question-answer");
    },
    minusOption: function(e) {
        this.is_edit = true;
        if ($(e).parents("tr").siblings().length == 0) {
            this._error("至少要有一个选项")
        } else {
            var c = $(e).parents("tr").attr("id").split("-"),
            f = c[0],
            d = c[1];
            if (this._data[f].option.hasOwnProperty(d)) {
                var b = $("#build-right").height();
                $("#build-right").height(b - 39);
                $(e).parents("tr").remove();
                if (this._data[f].qtype == "5") {
                    if (this._data[f].settings.direction == "Y") {
                        $("#o" + d).parent().remove()
                    } else {
                        var a = $("#q" + f + " tr td#o" + d).index();
                        $("#q" + f + " tr").each(function() {
                            $(this).find("td").eq(a).remove()
                        })
                    }
                } else {
                    $("#o" + d).remove()
                }
                if (!this._data[f].option[d].hasOwnProperty("add")) {
                    this._oid.push(this._data[f].option[d].oid)
                }
                delete this._data[f].option[d]
            } else {
                this._error("非法操作")
            }
        }
    },
    saveTitle: function(a) {
        this.is_edit = true;
        var c = $(a).attr("data-id"),
        b = $(a).val();
        this._data[c].edit = 1;
        this._data[c].settings.title = b;
        $("#q" + c).find(".title").html(b)
    },
    saveSubTitle: function(a) {
        this.is_edit = true;
        var c = $(a).attr("data-id"),
        b = $(a).val();
        this._data[c].edit = 1;
        this._data[c].settings.subtitle = b;
        $("#q" + c).find(".subtitle").html(b)
    },
    changeTitle: function(a) {
        var c = $(a).attr("data-id"),
        b = $(a).val();
        this._changeTitle(c, b)
    },
    _changeTitle: function(e, d) {
        this.is_edit = true;
        this._data[e].edit = 1;
        var c = this._data[e].option,
        b = "";
        for (var a in c) {
            if (c[a].settings.cid == d || "C" + c[a].settings.cid == d) {
                b += '<tr id="' + e + "-" + a + '"><td><input type="text" class="ocode" onpaste="return false" ondragenter="return false" oncontextmenu="return false;" maxlength="5" value="' + c[a]["code"] + '" onkeyup="SurveyBuild.saveOCode(this)"/></td><td><input type="text" class="option-txt" value="' + htmlentities(c[a]["txt"]) + '" onkeyup="SurveyBuild.saveTxt(this)" /></td><td><a href="javascript:void(0);" class="text-success" onclick="SurveyBuild.plusOption(this);return false;"><i class="icon-plus-sign"></i></a>&nbsp;<a href="javascript:void(0);" class="text-warning" onclick="SurveyBuild.minusOption(this);return false;"><i class="icon-minus-sign"></i></a>&nbsp;<a href="javascript:void(0);" class="text-info option-move"><i class="icon-move"></i></a></td></tr>'
            }
        }
        $("#" + e + "-sub").html(b)
    },
    /*修改问题后，自动保存*/
    saveQuestion: function(a) {
        this.is_edit = true;
        var c = $(a).attr("id"),
        b = $(a).val();
        this._data[c].edit = 1;
        this._data[c].question = b;
        b = b.replace(/<script/ig, "&lt;script").replace(/<\/script>/ig, "&lt;script&gt;");
        $("#q" + c).find(".question-question").html(b)
    },
    saveFormat: function(a) {
        this.is_edit = true;
        var c = $(a).attr("data-id"),
        b = $(a).val();
        this._data[c].edit = 1;
        this._data[c].settings.format = b;
        $("#q" + c).find(".format").removeClass().addClass("format format" + b)
    },
    saveSliderMax: function(a, c) {
        this.is_edit = true;
        this._data[c].edit = 1;
        var b = $(a).val();
        this._data[c].settings.max = b;
        $("#q" + c + " .slider-max").text(Math.ceil(b * 0.2))
    },
    saveSliderStart: function(a, c) {
        this.is_edit = true;
        this._data[c].edit = 1;
        var b = $(a).val();
        this._data[c].settings.start = b;
        $("#q" + c + " .slider-start").text(b)
    },
    saveSliderEnd: function(a, c) {
        this.is_edit = true;
        this._data[c].edit = 1;
        var b = $(a).val();
        this._data[c].settings.end = b;
        $("#q" + c + " .slider-end").text(b)
    },
   
    saveQCode: function(a) {
        this.is_edit = true;
        var b = $(a).attr("id").substr(1);
        this._data[b].edit = 1;
        this._data[b].code = $(a).val();
        $("#q" + b + " b.question-code").text(this._data[b].code + ".")
    },
    saveQName: function(a) {
        this.is_edit = true;
        var b = $(a).attr("id").substr(1);
        this._data[b].edit = 1;
        this._data[b].code = $(a).val();
        $("#q" + b + " b.question-code").text(this._data[b].code + ".")
    },
    saveCCode: function(b) {
        this.is_edit = true;
        var a = $(b).parents("tr").attr("id").split("-"),
        c = a[0],
        d = a[1];
        this._data[c].child[d].edit = 1;
        this._data[c].child[d].code = $(b).val()
    },
    saveOCode: function(c) {
        this.is_edit = true;
        var a = $(c).parents("tr").attr("id").split("-"),
        d = a[0],
        b = a[1];
        this._data[d].option[b].edit = 1;
        this._data[d].option[b].code = $(c).val()
    },
    saveTxt: function(c) {
        this.is_edit = true;
        var a = $(c).parents("tr").attr("id").split("-"),
        e = a[0],
        b = a[1],
        d = $(c).val();
        this._data[e].option[b].edit = 1;
        this._data[e].option[b].txt = d;
        d = d.replace(/<script/ig, "&lt;script").replace(/<\/script>/ig, "&lt;script&gt;");
        if (this._data[e].option[b].hasOwnProperty("settings") && this._data[e].option[b].settings.other == 1) {
            d += '<b class="read-input"></b>'
        }
        if (this._data[e].qtype != 10) {
            $("#o" + b).html(d)
        }
    },
    saveChild: function(b) {
        this.is_edit = true;
        var a = $(b).parents("tr").attr("id").split("-"),
        d = a[0],
        e = a[1],
        c = $(b).val();
        this._data[d].child[e].edit = 1;
        this._data[d].child[e].question = c;
        if (this._data[d].qtype == 17) {
            $("#first" + e).text(c)
        }
        c = c.replace(/<script/ig, "&lt;script").replace(/<\/script>/ig, "&lt;script&gt;");
        if (this._data[d].child[e].hasOwnProperty("settings") && this._data[d].child[e].settings.other == 1) {
            c += '<b class="read-input"></b>'
        }
        $("#c" + e).html(c)
    },
    saveTimer: function(d) {
        var c = $("#timertime" + d).val(),
        a = c == "" ? 0 : parseInt(c),
        b;
        this._data[d].settings.time = a;
        if ($("#timerauto" + d).prop("checked")) {
            this._data[d].settings.auto = 1;
            b = "页面将在<span>" + a + "</span>秒后自动提交"
        } else {
            this._data[d].settings.auto = 0;
            b = "页面将在<span>" + a + "</span>秒后显示下一页按钮"
        }
        this._data[d].edit = 1;
        this._data[d].question = b;
        $("#q" + d).find(".question-question").html(b)
    },
    saveColor: function(c) {
        this.is_edit = true;
        var h = $(c).attr("id").split("-"),
        g = h[0],
        b = h[1],
        a = $(c).val(),
        f = 0,
        d = {};
        if ($(c).prop("checked")) {
            var e = $(c).parents("li").prevAll().find(":checked").last();
            var i;
            if (e.length == 0) {
                i = 0
            } else {
                i = e.attr("id").split("-")[1]
            }
            if (i == 0) {++f;
                d[b] = {
                    qid: g,
                    add: 1,
                    code: 0,
                    txt: a,
                    orderby: f,
                    is_rand: 0
                }
            }
            $.each(this._data[g].option,
            function(l, j) {++f;
                j.orderby = f;
                d[l] = j;
                if (l == i) {++f;
                    d[b] = {
                        qid: g,
                        add: 1,
                        code: 0,
                        txt: a,
                        orderby: f,
                        is_rand: 0
                    }
                }
            });
            this._data[g].option = d;
            $(this._html(g)).find(".question-answer").replaceAll("#q" + g + " .question-answer")
        } else {
            $("#o" + b).hide();
            if (!this._data[g].option[b].hasOwnProperty("add")) {
                this._oid.push(this._data[g].option[b].oid)
            }
            delete this._data[g].option[b]
        }
    },
    saveSettings: function(g) {
        this.is_edit = true;
        var m = this._data[g].qtype;
        this._data[g].edit = 1;
        this._data[g].is_mandatory = $("#is_mandatory").prop("checked") ? 1 : 0;
        this._data[g].is_display = $("#is_display").prop("checked") ? 1 : 0;
        $("input.rand").each(function() {
            var i = $(this).attr("data-id");
            SurveyBuild._data[g].option[i].edit = 1;
            SurveyBuild._data[g].option[i].is_rand = $(this).prop("checked") ? 1 : 0
        });
        $("input.exclusive").each(function() {
            var i = $(this).attr("data-id");
            SurveyBuild._data[g].option[i].settings.exclusive = $(this).prop("checked") ? 1 : 0
        });
        if (this._data[g].hasOwnProperty("settings")) {
            if (this._data[g].settings.hasOwnProperty("min")) {
                this._data[g].settings.min = $("#min").val()
            }
            if (this._data[g].settings.hasOwnProperty("max")) {
                var h = $("#max").val(),
                e = parseInt(this._data[g].settings.max);
                h = h ? parseInt(h) : 0;
                this._data[g].settings.max = h;
                if (m == 8) {
                    var a = "";
                    if (h == 0) {
                        for (var c in this._data[g].option) {++h
                        }
                    }
                    for (var c = 1; c <= h; ++c) {
                        a += "<p>" + c + "</p>"
                    }
                    $("#q" + g + " .sort-right").html(a)
                } else {
                    if (m == 5) {
                        var l = "A" + ( + new Date());
                        if (h > e) {
                            var k = h - e;
                            for (var c = 1; c <= k; ++c) {
                                this._data[g].option[l + c] = {
                                    qid: g,
                                    add: 1,
                                    code: e + c,
                                    txt: "",
                                    orderby: e + c,
                                    is_rand: 0,
                                    settings: {
                                        other: 0,
                                        fixedwidth: ""
                                    }
                                }
                            }
                        } else {
                            if (h < e) {
                                var b = 1;
                                for (var c in this._data[g].option) {
                                    if (b > h) {
                                        if (!this._data[g].option[c].hasOwnProperty("add")) {
                                            this._oid.push(this._data[g].option[c].oid)
                                        }
                                        delete this._data[g].option[c]
                                    }++b
                                }
                            }
                        }
                        for (var c in this._data[g].option) {
                            if (!this._data[g].option[c].hasOwnProperty("add")) {
                                this._data[g].option[c].edit = 1
                            }
                            this._data[g].option[c].txt = $("#table-advanced-option input:text:eq(" + (this._data[g].option[c].orderby - 1) + ")").val()
                        }
                    }
                }
            }
        }
        if (m == 4) {
            var n = $("#suffix").val();
            this._data[g].settings.suffix = n;
            this._data[g].settings.preg = $("#preg").val();
            this._data[g].settings.mininum = $("#mininum").val();
            this._data[g].settings.maxinum = $("#maxinum").val();
            this._data[g].settings.defaultval = $("#defaultval").val();
            $("#q" + g + " .suffix").html(n)
        } else {
            if (m == 5) {
                $("input.mandatory").each(function() {
                    var i = $(this).attr("data-id");
                    SurveyBuild._data[g].child[i].settings.mandatory = $(this).prop("checked") ? 1 : 0
                });
                $("input.crand").each(function() {
                    var i = $(this).attr("data-id");
                    SurveyBuild._data[g].child[i].edit = 1;
                    SurveyBuild._data[g].child[i].is_rand = $(this).prop("checked") ? 1 : 0
                });
                $("input.cother").each(function() {
                    var i = $(this).attr("data-id");
                    if ($(this).prop("checked")) {
                        SurveyBuild._data[g]["child"][i].settings.other = 1;
                        $("#c" + i).html(SurveyBuild._data[g]["child"][i].question + '<b class="read-input"></b>')
                    } else {
                        SurveyBuild._data[g]["child"][i].settings.other = 0;
                        $("#c" + i + " .read-input").remove()
                    }
                });
                $("input.other").each(function() {
                    var i = $(this).attr("data-id");
                    if ($(this).prop("checked")) {
                        SurveyBuild._data[g].option[i].settings.other = 1;
                        $("#o" + i).html(SurveyBuild._data[g].option[i]["txt"] + '<b class="read-input"></b>')
                    } else {
                        SurveyBuild._data[g].option[i].settings.other = 0;
                        $("#o" + i + " .read-input").remove()
                    }
                });
                if (this._data[g].settings.type != "G") {
                    this._data[g].settings.fixed = $("#fixed").prop("checked") ? 1 : 0;
                    this._data[g].settings.direction = $("#direction").prop("checked") ? "Y": "X";
                    if (this._data[g].settings.direction == "Y") {
                        $("input.fixedwidth").each(function() {
                            var j = $(this).attr("data-id"),
                            i = $(this).val();
                            SurveyBuild._data[g].child[j].settings.fixedwidth = i
                        })
                    } else {
                        $("input.fixedwidth").each(function() {
                            var i = $(this).attr("data-id"),
                            j = $(this).val();
                            SurveyBuild._data[g].option[i].settings.fixedwidth = j
                        })
                    }
                    if (this._data[g].settings.type == "S") {
                        this._data[g].settings.mean = $("#mean").prop("checked") ? 1 : 0
                    }
                }
                $("#q" + g).html(this._html(g))
            } else {
                if (m == 17) {
                    $("input.crand").each(function() {
                        var i = $(this).attr("data-id");
                        SurveyBuild._data[g].child[i].edit = 1;
                        SurveyBuild._data[g].child[i].is_rand = $(this).prop("checked") ? 1 : 0
                    });
                    $("input.other").each(function() {
                        var i = $(this).attr("data-id");
                        SurveyBuild._data[g].option[i].settings.other = $(this).prop("checked") ? 1 : 0
                    })
                } else {
                    $("input.other").each(function() {
                        var i = $(this).attr("data-id");
                        if ($(this).prop("checked")) {
                            SurveyBuild._data[g].option[i].settings.other = 1;
                            $("#o" + i).html(SurveyBuild._data[g].option[i]["txt"] + '<b class="read-input"></b>')
                        } else {
                            SurveyBuild._data[g].option[i].settings.other = 0;
                            $("#o" + i + " .read-input").remove()
                        }
                    });
                    if (m <= 3) {
                        var d = [];
                        $("input.defaultval:checked").each(function() {
                            var i = $(this).attr("data-id");
                            d.push(SurveyBuild._data[g].option[i].code)
                        });
                        this._data[g].settings.defaultval = d;
                        if (m == 3) {
                            var f = "";
                            if (this._data[g].settings.min != 0 && this._data[g].settings.min == this._data[g].settings.max) {
                                f = '<span class="question-select">[限选' + this._data[g].settings.min + "项]</span>"
                            } else {
                                if (this._data[g].settings.min > 1 && this._data[g].settings.max > 0) {
                                    f = '<span class="question-select">[限选' + this._data[g].settings.min + " - " + this._data[g].settings.max + "项]</span>"
                                } else {
                                    if (this._data[g].settings.min > 1) {
                                        f = '<span class="question-select">[最少选' + this._data[g].settings.min + "项]</span>"
                                    } else {
                                        if (this._data[g].settings.max > 0) {
                                            f = '<span class="question-select">[最多选' + this._data[g].settings.max + "项]</span>"
                                        }
                                    }
                                }
                            }
                            this._data[g].question = this._data[g].question.replace(/<span\s+class="question-select">.*<\/span>/, "");
                            this._data[g].question += f;
                            $("#q" + g + " .question-question").html(this._data[g].question);
                            $("#" + g).val(this._data[g].question)
                        }
                    }
                    if (m == 9) {
                        $("input.mandatory").each(function() {
                            var i = $(this).attr("data-id");
                            SurveyBuild._data[g].option[i].settings.mandatory = $(this).prop("checked") ? 1 : 0
                        });
                        $("select[name=preg]").each(function() {
                            var i = $(this).attr("data-id");
                            SurveyBuild._data[g].option[i].settings.preg = $(this).val()
                        });
                        $("input.suffix").each(function() {
                            var i = $(this).attr("data-id"),
                            j = $(this).val();
                            SurveyBuild._data[g].option[i].settings.suffix = j;
                            $("#o" + i).siblings(".suffix").html(j)
                        });
                        var o = parseFloat($("#total").val());
                        this._data[g].settings.total = isNaN(o) ? "": o
                    }
                }
            }
        }
        $.fancybox.close()
    },
    saveStype: function(a) {
        this.is_edit = true;
        var c = $(a).attr("data-id"),
        b = $(a).val();
        this._data[c].edit = 1;
        this._data[c].settings.type = b
    },
    saveBuild: function() {
        if (!this.checkAttrVal()) {
            return
        }
        var a = {},
        b = true,
        d = 100,
        c = 0;
        $("#question-box>li").each(function(f) {
            var g = $(this),
            h = g.attr("data_id"),
            e = SurveyBuild._data[h]["classname"];
            e == "page" && ++c;
            if (d == 100 && e == "page") {
                SurveyBuild.fail( h, "第" + c + "页不能没有问题", "top");
                b = false;
                return false
            } else {
                d = e;
                a[h] = SurveyBuild._data[h]
            }
        });
      //  console.log(JSON.stringify(a));
        if (b) {
            loading($("#build-box"));
            $("#build-save button").text("保存中...");
            $.ajax({
                type: "POST",
                url: "http://202.120.24.169:8050/psc/ACEMDEV/EMPLOYEE/CRM/s/WEBLIB_SURVEY.TZ_EXAM_TPL.FieldFormula.IScript_createQuestionTpl",
                data: {
                    op: "save",
                    sid: SurveyBuild._sid,
                    data: $.toJSON(a),
                    oid: SurveyBuild._oid,
                    qid: SurveyBuild._qid
                },
                dataType: "JSON",
                success: function(f) {
                    loaded($("#build-box"));
                    $("#build-save button").html('保存<span style="font-size:12px;">(建议10分钟一次)</span>');
                    if (f.code == 0) {
                        noteing("保存成功");
                        /*console.log(f.content);*/
                        SurveyBuild.is_edit = false;
                        var e = $("#question-box>li.active").index() - 1;
                        /*
                        SurveyBuild._data = f.content;
                        $("#question-box>li").remove();
                        SurveyBuild._load(true);
                        if (e >= 0) {
                            var g = $("#question-box>li").eq(e).addClass("active").attr("id").substr(1);
                            $("#question-edit").html(SurveyBuild._edit(g));
                            SurveyBuild._optionMove()
                        }
                        */
                    } else {
                        noteing("保存失败", 2)
                    }
                }
            });
            return true
        }
    },
    saveEmptyText : function(a){
    	 this.is_edit = true;
         var b = $(a).attr("data_id");
         this._data[b].edit = 1;
         this._data[b].emptyText = $(a).val();
    },
    preview: function(b) {
        /*console.log(this.is_edit);*/
        if (this.is_edit) {
            var a = '<div class="modal-header"><h4>提示</h4></div>';
            a += '<div id="modal-confirm" class="modal-body">当前问卷有未保存的操作，预览前请保存</div>';
            a += "<div class=\"modal-footer clearfix\"><button class=\"btn btn-info pull-left\" onclick=\"$('#modal-confirm').html('问题保存中...');if(SurveyBuild.saveBuild()){$.fancybox.close();window.open('/pc/p/" + b + '\');}"><i class="icon-ok"></i>保存并访问</button><button class="btn btn-warning pull-right" onclick="$.fancybox.close();window.open(\'/pc/p/' + b + '\');"><i class="icon-external-link"></i>继续访问</button></div>';
            $.fancybox.open({
                content: a,
                helpers: {
                    overlay: {
                        closeClick: false
                    }
                }
            })
        } else {
            window.open("/pc/p/" + b)
        }
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
            placeholder: "place-holder",
            axis: "y",
            opacity: 0.6,
            revert: true,
            cancel: "#question-new",
            receive: function(f, d) {
                SurveyBuild.add(d.item.attr("data-classname"), 1); 
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
        /*
        var me = this;
        if(this._componentConfig && this._componentConfig.length>0){
        	$.each(this._componentConfig,function(i,rec){
        		me.loadScript(rec.className);
        	})
        }*/
        $("#question-edit").on("keypress", ".qcode,.ccode",
        function(c) {
            var b = c.which;
            return b == 8 || b == 127 || b == 0 || b == 95 || (b >= 48 && b <= 57) || (b >= 65 && b <= 90) || (b >= 97 && b <= 122)
        });
        $("#question-edit").on("keypress", ".ocode,.timertime",
        function(c) {
            var b = c.which;
            return b == 8 || b == 127 || b == 0 || b >= 48 && b <= 57
        });
        SurveyBuild._sid = a;
  
        if(SurveyBuild._data){
        	 SurveyBuild._load();
             loaded($("#question-wrap"))
        }       
    },
    _load: function(b) {
        var a = "",
        d = false;
        this._count = 0;
        var me = this;
        var callback = function(params){
        	var classname = params[0];
        	var data = params[1];
        	var d = data["instanceId"];
        	var component = me._data[d] = new me.comClass[classname](data);
        	component._init && component._init(d);
        	if(me.is_edit_moda){
        		$("#question-box").append(me._html(d));
        	}else{
        		$("#main_form").append(me._data[d]._getHtml(me._data[d],true));
        	}	
        };
        var _min=1,_max = 99999999999,_onError="",_result = "";;
        for (var c in this._data) {
        	++this._count;
        	 d = true
        	this.loadScript(this._data[c]["classname"],callback,[this._data[c]["classname"],this._data[c]]);
        	//formValidator校验
        	var item = this._data[c];
        	if(item["isRequire"] == "Y"){
        		_onError = item["rules"]["RequireValidator"]["messages"]["FAL1"]["value"];
        		if(item["isCheckStrLen"] == "Y" ||item["isNumSize"] == "Y" ){
        			_min = Math.max(item["min"],1);
        			_max = item["max"];
        			if(item["isCheckStrLen"] == "Y") _onError = item["rules"]["CharLenValidator"]["messages"]["FAL1"]["value"];
        			if(item["isNumSize"] == "Y") _onError = item["rules"]["NumSizeValidator"]["messages"]["FAL1"]["value"];        			      			            
        		}     
        		
        		
        		$("#"+item["itemId"])
				.formValidator({tipID:item["itemId"]+'Tip',onShow: item["onShowMessage"]||"",onFocus: item["onFoucsMessage"]||"&nbsp;",onCorrect: item["onCorrectMessage"]||"&nbsp;"})			
				.inputValidator({min:_min,max: _max,onError: _onError})
				.functionValidator({
					fun:function(){
						if(ValidationRules){
							
							$.each(item["rules"],function(classname,classObj){
								if($.inArray(classname,me._baseRules) == -1){
									var _ruleClass = ValidationRules[classname];
									if(_ruleClass && _ruleClass._Validator){
										_result = _ruleClass._Validator(item["itemId"],classObj["messages"],classObj["params"]||{},item);
										if(_result != true){
											return false;
										}
									}
								}
							});
							return _result
						}
					}
				});
        		
	        	//事件绑定
	    		$.each(item["rules"],function(classname,classObj){
	    			if($.inArray(classname,me._baseRules) == -1){
						var _ruleClass = ValidationRules[classname];
						if(_ruleClass && _ruleClass._eventList){
							$.each( _ruleClass._eventList,function(eventname,fun){
						  		$("#"+item["itemId"]).bind(eventname,function(){
						  			if(fun && typeof fun == "function"){
						  				fun(item["itemId"],classObj["messages"],classObj["params"]||{},item);
						  			}
						  		});
							});
						}
					}
	    		});
        	}  
        }
        if (!d && me.is_edit_moda) {
        		$("#question-new").show()
        } else {
            
        }
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
    }
};
function upload_call(json) {
    var obj = eval(json);
    if (obj.code > 0) {
        noteing("上传失败 ERROR:" + obj.code, 2)
    } else {
        $("#modal-question-upload .upload-gallery").prepend('<a class="thumbnail" href="javascript:void(0);"><img src="' + obj.url + '" /></a>');
        $("#" + obj.qid + "-file").val("")
    }
    loaded($("#modal-question-upload"))
}
function upload_relation(d, f, a) {
    for (var e in SurveyBuild._data[d].child) {
        SurveyBuild._qid.push(SurveyBuild._data[d].child[e].qid)
    }
    for (var b in SurveyBuild._data[d].option) {
        SurveyBuild._oid.push(SurveyBuild._data[d].option[b].oid)
    }
    SurveyBuild._data[d].child = f;
    SurveyBuild._data[d].option = a;
    loaded($("#build-box"));
    $("#q" + d).click()
};

