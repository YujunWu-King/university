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
		 /*
		 <div class="btn1">
         c += '<div class="btn-pre">'+ MsgSet["PRE"]+ '&nbsp;&nbsp;';
         
         <img src="' + TzUniversityContextPath + '/statics/images/appeditor/new/btn-save.png" />
         
         <li>下一步<img src="images/next.png"></li>
         </div>
         
         
          <div class="btn">
	     	 <div class="btn_left">保存<img src="images/save.png"></div>
	     	 <div class="btn_right">下一步<img src="images/next.png"></div>
	     </div>         
		 */
		if (previewmode) {
			
			if(SurveyBuild.accessType == "M"){
//				console.log(SurveyBuild.accessType);
				if(SurveyBuild._readonly!=true){
			         c += '<div class="btn1" id="app_preButtom">';
			          
		        	 c += 	'<li>'+MsgSet["PRE"]+'<img src="' + TzUniversityContextPath + '/statics/images/appeditor/new/btn-save.png"></li>';
		        	 c += '</div>'
		         } else {
		        	 c += '<div class="btn1">';
		        	 c += 	'<li>'+MsgSet["PRE"]+'<img src="' + TzUniversityContextPath + '/statics/images/appeditor/new/btn-save.png"></li>';
		        	 c += '</div>'
				}
			}else{
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
			}
			
		} else {
			c += '<span class="edu_item_label" style="width:150px;">'+MsgSet["PRE"]+'：</span><button class="btn btn-small"><i class="icon-upload-alt"></i>'+MsgSet["PRE"]+'</button>';
		}

		return c;
	},
	 _eventbind: function(data) {
		 if(SurveyBuild.accessType == "M"){
			 var $inputBox = $("#app_preButtom");
			 
			 $.each([$inputBox],function(i, el) {
					el.click(function(e) { 
						console.log("test");
					});
				});
		 	}
	    }



});