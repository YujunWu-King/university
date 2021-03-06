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
			 if(SurveyBuild.accessType == "M"){
				 c +='<div class="readme" id="' + data.itemId + '">';
				 c +=' <div class="form_sm" ><img class="mtips" src="' + TzUniversityContextPath + '/statics/images/appeditor/new/tips2.png">'+MsgSet["FORM_DES"]+'</div>';
				 c +='</div>';
				 c +='<div class="shade" style="display:none"></div>';
				 c +='<img class="pop_close" id="' + data.itemId + '_img" style="display:none" src="' + TzUniversityContextPath + '/statics/images/appeditor/m/rl_btn.png'+'">';
				 c +='<div class="pop_body" style="display:none" id="' + data.itemId + '_body">';
				 c +=	'<div class="pop_inner" id="' + data.itemId + '_DIV"></div>';
				 c +='</div>';				 
			 }else{
				//c += '<div id="' + data.itemId + '">' + data.title + '</div>';
				c +='<div class="readme" id="' + data.itemId + '">'
				c +=' <div class="form_sm" ><img class="tips" src="' + TzUniversityContextPath + '/statics/images/appeditor/new/tips2.png">'+MsgSet["FORM_DES"]+'</div>';
	            c +=' <div class="form_input" id="' + data.itemId + '_DIV">';
	            c +=  data.title;
	            c +=' </div>';
	            c +='</div>';
				 
			 }
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
		if(SurveyBuild.accessType == "M"){
			$(".shade").hide();
			$(".pop_close").hide();
			$(".pop_body").hide();
				function initStyles(id) {
				 var allHeight=$(window).height();
				     var popheight=$("#"+id).height();
				     $("#"+id).css("top",allHeight/2-popheight/2-10+"px");
//				     $(".pop_inner").css("padding-bottom",allHeight/2-popheight/2-10+"-25px");
				     $(".pop_close").css("top",allHeight/2-popheight/2-20+"px");
				}	
			var desc = $("#" + data["itemId"]);
			var myscroll;
			desc.click(function(){
				$(".shade").show();
				$("#"+data["itemId"]+"_img").show();
				$("#"+data["itemId"]+"_body").show();
				$(".pop_inner").html(data.title+"<br/>");
				initStyles(data["itemId"] + "_body");
				myscroll = new iScroll(data["itemId"] + "_body",{hideScrollbar:false});
				myscroll.refresh();
			});
			$(".pop_close").click(function(){ 
				$(".pop_body").hide();
				$(".shade").hide();
				$(".pop_close").hide();
				$(".pop_inner").html("");
				myscroll.destroy();
				myscroll = null;
			});
		}else{
			$(".form_input").hide();
			var desc = $("#" + data["itemId"]);
			desc.hover(
					function () {
					    $(this).children(".form_sm").css("background-color","#0070c6");
					    $(this).children(".form_sm").css("color","#fff");
					    $(this).children(".form_input").css("display","block");
					    
					  },
					  function () {
					    $(this).children(".form_sm").css("background-color","transparent");
					    $(this).children(".form_sm").css("color","#000");
					    $(this).children(".form_input").css("display","none");
						  //$(this).css("background-color","#fff");
						  //  $(this).css("color","#000");
						  //  $("#" + data["itemId"]+"_DIV").css("display","none");
					  });
		}
		 
	},
})