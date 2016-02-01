SurveyBuild.extend("Page","baseComponent",{
	itemName: "Untitled Page",	
	tapWidth: "100",
	tapHeight: "26",
	tapStyle:'width:100px;height:26px',
	_CommonField:"N",
    	_getHtml : function(data,previewmode){	
		if(previewmode){
			return ;
		}
	    var c = '<div class="question-title"><div class="question-split"><hr><div class="pagename">'+data.itemName+'</div></div></div>';
	    return c;
	}
}); 