var baseField = {add: 1,
        code: "",
        question: "在这里输入信息项名称",	
        emptyText:"",
        orderby: "",
}

var RegFormComponents = {
	"radio" : $.extend(true,{},baseField,{
					question: "信息项名称（单选框）",	
					qtype: 2,
			        settings: {
			            format: 1,
			            defaultval: []
			        },
			        option:[],
			        ajaxUrl:"",
                    _init : function(d){
						var e = {};
						for (var c = 1; c <= 3; ++c) {
							e[d + "_" + c] = {
								qid: d,
								add: 1,
								code: c,
								txt: "选项" + c,
								orderby: c,
								is_rand: 0,
								settings: {
									other: 0
								}
							}
						}
						this.option = e;
                    },
					_getHtml: function(data,d){
						    var c = '<li id="q' + d + '" onclick="SurveyBuild.edit(this)">';
							c += '<div class="question-title"><b class="question-code">' + data.code + '.</b><div class="question-question">' + data.question + '</div></div><div class="question-answer"><ul class="format format' + data.settings.format + '">';
							var options = data.option
							for (var e in options) {
								c += '<li id="o' + e + '" class="read-radio">' + options[e]["txt"];
								if (options[e].settings.other == 1) {
									c += '<b class="read-input"></b>'
								}
								c += "</li>"
							}
							c += "</ul></div>";
							return c;
					},
					_edit: function(data,d){
						var e = '<fieldset><legend><span style="width:90px;display: inline-block;">信息项编号：</span><input type="text" id="p' + d + '" onkeyup="SurveyBuild.saveQCode(this)" value="' + data.code + '" class="input-mini qcode" onpaste="return false" ondragenter="return false" oncontextmenu="return false;" maxlength="30" /></legend></fieldset><span style="width:90px;display: inline-block;">信息项名称：</span><input type="text" class="medium" id="' + d + '" onkeyup="SurveyBuild.saveQuestion(this)" value="' + data.question + '"/>';
						//e += '<div style="margin-top:10px;"><span style="width:90px;display: inline-block;">空值信息：</span><input type="text" class="medium" data_id="' + d + '" onkeyup="SurveyBuild.saveEmptyText(this)" value="' + data.emptyText + '"/></div>';
						var b = '<fieldset style="text-align:center;"><button class="btn btn-small btn-danger" onclick="SurveyBuild.questionAdvanced(\'' + d + '\')"><i class="icon-cogs"></i>高级设置</button>&nbsp;&nbsp;<button class="btn btn-small" onclick="SurveyBuild.copy(\'' + d  + '\')"><i class="icon-plus"></i>复制</button>&nbsp;&nbsp;<button class="btn btn-small" onclick="SurveyBuild.remove(event,\'' + d  + '\')"><i class="icon-trash"></i>删除</button></fieldset>';
						if (data.hasOwnProperty("option")) {
							var g = data.option;
							var n = data.settings.format;
							var a = "";
							for (var f in g) {
								a += '<tr id="' + d + "-" + f + '"><td><input type="text" class="ocode" onpaste="return false" ondragenter="return false" oncontextmenu="return false;" maxlength="5" value="' + g[f]["code"] + '" onkeyup="SurveyBuild.saveOCode(this)"/></td><td><input type="text" class="option-txt" value="' + htmlentities(g[f]["txt"]) + '" onkeyup="SurveyBuild.saveTxt(this)" /></td><td><a href="javascript:void(0);" class="text-success" onclick="SurveyBuild.plusOption(this);return false;"><i class="icon-plus-sign"></i></a>&nbsp;<a href="javascript:void(0);" class="text-warning" onclick="SurveyBuild.minusOption(this);return false;"><i class="icon-minus-sign"></i></a>&nbsp;<a href="javascript:void(0);" class="text-info option-move"><i class="icon-move"></i></a></td></tr>'
							}
							var o = e + '<div class="question-type clearfix" style="margin-top:10px;">';
							o += '<fieldset><span style="width:90px;display: inline-block;">选项排列：</span><select data-id="' + d + '" onchange="SurveyBuild.saveFormat(this)"><option value="1"' + (n == 1 ? " selected": "") + '>垂直1列</option><option value="2"' + (n == 2 ? " selected": "") + '>垂直2列</option><option value="3"' + (n == 3 ? " selected": "") + '>垂直3列</option><option value="4"' + (n == 4 ? " selected": "") + '>垂直4列</option><option value="5"' + (n == 5 ? " selected": "") + '>垂直5列</option><option value="6"' + (n == 6 ? " selected": "") + '>垂直6列</option><option value="7"' + (n == 7 ? " selected": "") + '>垂直7列</option><option value="8"' + (n == 8 ? " selected": "") + '>垂直8列</option><option value="9"' + (n == 9 ? " selected": "") + '>水平</option></select></fieldset></div><fieldset id="option-box"><table class="table table-bordered data-table"><thead><tr><th>编码</th><th>描述<button class="btn btn-primary btn-mini pull-right" onclick="SurveyBuild.optionBatch(\'' + d + '\',false)">批量编辑选项</button></th><th width="45">操作</th></tr></thead><tbody>' + a + "</tbody></table></fieldset>"
						}
						return o +b;
					}
				}
		),
		
		"input_text" : $.extend(true,{},baseField,{
			question :"信息项名称（输入框）",
			qtype: 4,
	        settings: {
				 format: 1,
	             preg: "",
	             min: 0,
	             max: 0,
	             mininum: "",
	             maxinum: "",
	             suffix: "",
	             defaultval: ""
	        },
            _init : function(d){
				
            },
			_getHtml: function(data,d){
            		
				    var c = '<li id="q' + d + '" onclick="SurveyBuild.edit(this)">';
				    c += '<div class="question-title"><b class="question-code">' + data.code + '.</b><div class="question-question">' + data.question + '</div></div><div class="question-answer"><div class="format format' +data.settings.format + '"><b class="read-input"></b><span class="suffix">' + (data.settings.hasOwnProperty("suffix") ? data.settings.suffix: "") + "</span></div></div>"
					c +='<div class="question-action"><a class="build-icon-minus" title="删除" onclick="return SurveyBuild.remove(event,'+d+')"></a><a class="build-icon-copy" title="复制" onclick="SurveyBuild.copy('+d+')"></a><i class="build-icon-arrow"></i></div>'
				    return c;
			},
			_edit: function(data,d){
				var n = data.settings.format
				var e = '<fieldset><legend><span style="width:90px;display: inline-block;">信息项编号：</span><input type="text" id="p' + d + '" onkeyup="SurveyBuild.saveQCode(this)" value="' + data.code + '" class="input-mini qcode" onpaste="return false" ondragenter="return false" oncontextmenu="return false;" maxlength="30" /></legend></fieldset><span style="width:90px;display: inline-block;">信息项名称：</span><input type="text" class="medium" id="' + d + '" onkeyup="SurveyBuild.saveQuestion(this)" value="' + data.question + '"/>';
				e +='<div style="margin-top:10px;height:40px;"><span style="width:90px;display: inline-block;">空值信息：</span><input type="text" class="medium" data_id="' + d + '" onkeyup="SurveyBuild.saveEmptyText(this)" value="' + data.emptyText + '"/></div>';
				//e +='<div class="question-type clearfix" style="margin-top:10px;"><fieldset><span style="width:90px;display: inline-block;">文本框大小:</span><select data-id="' +d + '" onchange="SurveyBuild.saveFormat(this)"><option value="1"' + (n == 1 ? " selected": "") + '>单行 - 小</option><option value="2"' + (n == 2 ? " selected": "") + '>单行 - 中</option><option value="3"' + (n == 3 ? " selected": "") + ">单行 - 大</option>";
				e +='<div style="text-align:center;"><button class="btn btn-small btn-danger" onclick="SurveyBuild.questionAdvanced(\'' + d + '\')"><i class="icon-cogs"></i>高级设置</button>&nbsp;&nbsp;<button class="btn btn-small" onclick="SurveyBuild.copy(\'' + d  + '\')"><i class="icon-plus"></i>复制</button>&nbsp;&nbsp;<button class="btn btn-small" onclick="SurveyBuild.remove(event,\'' + d  + '\')"><i class="icon-trash"></i>删除</button></div>';
				
				return e;
			}
		}
)
}

RegFormComponents["gender"] = $.extend(true,{},RegFormComponents["radio"],{
	_init : function(d){
		var e = {};
		e[d + "_" + 1] = {
			qid: d,
			add: 1,
			code: "F",
			txt: "男",
			orderby: "",
			is_rand: 0,
			settings: {
				other: 0
			}
		}
		e[d + "_" + 2] = {
			qid: d,
			add: 1,
			code: "M",
			txt:"女",
			orderby: "",
			is_rand: 0,
			settings: {
				other: 0
			}
			}
		this.option = e;
	}						
});

var _relation ={
	"2" : RegFormComponents["radio"],
	"4" : RegFormComponents["input_text"],
	"gender": RegFormComponents["gender"]
}
$.extend(SurveyBuild._plug,_relation);