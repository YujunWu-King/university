/*====================================================================
+ 功能描述：表单填写说明控件，用于展示说明性文字，文字格式可富文本编辑						++
+ 开发人：曹阳														++
+ 开发日期：2017-02-17												++
=====================================================================*/
SurveyBuild.extend("FormDesc", "baseComponent", {
	itemName: "表单填写说明",
	title: "表单填写说明",
	"StorageType": "L",
	"isDownLoad": "N", //是否导出

	_getHtml: function(data, previewmode) {
		var c = '';
		if (previewmode) {
			//c += '<div id="' + data.itemId + '">' + data.title + '</div>';
			c +='<div class="readme" id="' + data.itemId + '">'
			c +=' <div class="form_sm" ><img class="tips" src="' + TzUniversityContextPath + '/statics/images/appeditor/new/tips2.png">'+MsgSet["FORM_DES"]+'</div>';
            c +=' <div class="form_input" id="' + data.itemId + '_DIV">';
            c +=  data.title;
             c +=' </div>';
             c +='</div>';
		} else {
			c += '<div class="question-answer"></div>';
		}
		return c;
	},
	_edit: function(data) {
		var e = "";

		e += '<div class="edit_paramSet mt10"><span class="title"><i class="icon-info-sign"></i> 参数设置</span>';
		e += '	<div class="groupbox">';
		e += '		<div class="edit_item_warp" style="margin-top:5px;"><input class="mbIE" type="checkbox" onchange="SurveyBuild.saveAttr(this,\'isDownLoad\')"' + (data.isDownLoad == "Y" ? "checked='checked'": "") + ' id="is_isDownLoad"> <label for="is_isDownLoad">是否导出</label></div>';
		e += '	</div>';
		e += '</div>';

		return e;
	},
	_eventbind: function(data) {
		 $(".form_input").hide();
		var desc = $("#" + data["itemId"]);
		desc.hover(
				function () {
				    $(this).children(".form_sm").css("background-color","#0070c6");
				    $(this).children(".form_sm").css("color","#fff");
				    $(this).children(".form_input").css("display","block");
				    //$(this).css("background-color","#0070c6");
				    //$(this).css("color","#fff");
				    //$("#" + data["itemId"]+"_DIV").css("display","block");
				  },
				  function () {
				    $(this).children(".form_sm").css("background-color","transparent");
				    $(this).children(".form_sm").css("color","#000");
				    $(this).children(".form_input").css("display","none");
					  //$(this).css("background-color","#fff");
					  //  $(this).css("color","#000");
					  //  $("#" + data["itemId"]+"_DIV").css("display","none");
				  });
	},
})