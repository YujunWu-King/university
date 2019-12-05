/*加载formvalidator信息*/
$.formValidator.initConfig({	
	formID:"main_list",		
	debug:false,	
	submitOnce:true,
	onError:function(msg,obj,errorlist){
		$("#errorlist").empty();
		$.map(errorlist,function(msg){	
			$("#errorlist").append("<li>" + msg + "</li>")	
		});
	},
	submitAfterAjaxPrompt : '有数据正在异步验证，请稍等...'	
});

//处理formvalidator的tips
var $flowtips = $("#flowtips");

/*加载页面信息*/	
SurveyBuild["_items"] = data.items;
SurveyBuild.is_edit_moda = false;
SurveyBuild._load();
SurveyBuild._initAssociatedShowHide();

//console.log(JSON.stringify(data.items));

/*处理select的样式*/
var config = {
				  '.chosen-select'           : {},
				  '.chosen-select-deselect'  : {allow_single_deselect:true},
				  '.chosen-select-no-single' : {disable_search_threshold:10},
				  '.chosen-select-no-results': {no_results_text:'Oops, nothing found!'},
				  '.chosen-select-width'     : {width:"95%"}
			};
for (var selector in config) {
  $(selector).chosen(config[selector]);
}
/*默认点击选中的Tab页签*/
$(".menu-active").click();
//console.log(appReadOnly);

//if(!currentPageId){
//	$('#app_save').show(); 
//	$('#app_submit').show();
//}

layer.closeAll();
//单选框、复选框、多选框，其他
//$(".sur_other_box").change(function(){
//	if($(this).attr("type") == "radio"){
//		if($(this).val() == $("input[name='"+$(this).attr("name")+"']:checked").val()){
//			$(this).next("input").show()
//		}else{
//			$(this).next("input").hide()
//		}
//	}else{					
//		$(this).prop("checked")?$(this).next("input").show():$(this).next("input").hide();
//	}
//});
//$(".sur_radio_box").change(function(){
//	$(this).closest("ul").find(".sur_other_box").next("input").hide();
//});

/*
	Custom checkbox and radio button - Jun 18, 2013
	(c) 2013 @ElmahdiMahmoud 
	license: http://www.opensource.org/licenses/mit-license.php
*/   
//$('input[type="radio"]').wrap('<div class="radio-btn"><i></i></div>');
//$(".radio-btn").on('click', function () {
//    var _this = $(this),block = _this.parent().parent();
//    block.find('input:radio').attr('checked', false);
//    block.find(".radio-btn").removeClass('checkedRadio');
//    _this.addClass('checkedRadio');
//    _this.find('input:radio').attr('checked', true);
//});
//$('input[type="checkbox"]').wrap('<div class="check-box"><i></i></div>');
//$.fn.toggleCheckbox = function () {
//    this.attr('checked', !this.attr('checked'));
//}
//$('.check-box').on('click', function () {
//    $(this).find(':checkbox').toggleCheckbox();
//    $(this).toggleClass('checkedBox');
//});
