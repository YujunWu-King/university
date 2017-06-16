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
	    	if(SurveyBuild.accessType == "M"){
	    		c += '<div class="se_tit1">';
		    	c +=  data.title;
		    	c += '</div>';
		    	c += '<div class="index_body">';
		    	
		    	c += '</div>';
	    	} else {
	    	c += '<div class="mainright-title">';
	    	c += '	<span class="title-line"></span>' + data.title;
	    	c += '</div>';
	    	c += '<div class="main_content_box">';
	    	
	    	c += '</div>';
	    	}
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