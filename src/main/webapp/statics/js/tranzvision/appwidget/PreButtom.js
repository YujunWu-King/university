/*====================================================================
 + 功能描述：预提交按钮        									++
 + 开发人： caoy 						                           ++
 + 开发日期：2017-1-23											++
 =====================================================================*/
SurveyBuild.extend("PreButtom", "baseComponent", {
	itemName: "预提交",
	title: "预提交",
	_getHtml: function(data, previewmode) {
		 var c = "";
		if (previewmode) {
			if(SurveyBuild._readonly!=true){
				c += '<div class="operation-btn" style="margin: 20px auto;">';
				c += '<a href="#" id="app_preButtom">';
				c += '<div class="btn-pre">'+ MsgSet["PRE"]+ '&nbsp;&nbsp;';
				c += '<span class="operation-btn-icon">';
				c += '<img src="' + TzUniversityContextPath + '/statics/images/appeditor/new/btn-save.png" />';
				c += '</span>';
				c += '</div>';
				c += '</a>';
				c += '</div>';
			} else {
				c += '<div class="operation-btn" style="margin: 20px auto;">';
				//c += '<a href="#" id="app_preButtom">';
				c += '<div class="btn-pre">'+ MsgSet["PRE"]+ '&nbsp;&nbsp;';
				c += '<span class="operation-btn-icon">';
				c += '<img src="' + TzUniversityContextPath + '/statics/images/appeditor/new/btn-save.png" />';
				c += '</span>';
				c += '</div>';
				//c += '</a>';
				c += '</div>';
			}
		} else {
			c += '<span class="edu_item_label" style="width:150px;">'+MsgSet["PRE"]+'：</span><button class="btn btn-small"><i class="icon-upload-alt"></i>'+MsgSet["PRE"]+'</button>';
		}

		return c;
	}



});