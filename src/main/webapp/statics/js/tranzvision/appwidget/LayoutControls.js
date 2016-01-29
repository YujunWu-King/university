SurveyBuild.extend("LayoutControls","baseComponent",{
	itemName:"分组框",
	title:"分组框",
	isDoubleLine:"Y",
	"minLines": 1,
	"maxLines": 1,
	children:{},
  	_getHtml : function(data,previewmode){
	    var c = "",children = data.children;
	    if(previewmode){
			c = '<div class="main_inner_content_title">';
			c += '	<span class="reg_title_star">' + (data.isRequire == "Y" ?"*":"") + '</span>';
			c += '	<span class="reg_title_grey_17px">' + data.title + '</span>';
			c += '</div>';

			c += '<div class="main_inner_content_top"></div>';
			c += '<div class="main_inner_content">';

			c += '</div>';
			c += '<div class="main_inner_content_foot"></div>';
	    }else{
	    	c = '<ul class="DHContainer DHSort" data-dhbz="Y" style="min-height:150px;"></ul>';
	    }
	    return c;
	},
	_eventbindEditor:function(data){
		$("#question-box li[data_id='" + data.instanceId + "']").find(".DHContainer").droppable({
		  drop: function(event,obj) {
			var _classname = $(obj.draggable).attr("data-classname");
			if($.inArray(_classname,["Page","DHContainer"]) > -1){
				SurveyBuild.isDHForTwo = true;
				return;
			}
			if(!SurveyBuild.isDHForTwo){
				SurveyBuild.isDHForTwo = true;
				SurveyBuild.isDHContainer = true;
				SurveyBuild.currentDHID = data.instanceId;
			    SurveyBuild.add(_classname, $(event.target));
			}
		  }
		});
	}
})