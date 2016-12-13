SurveyBuild.extend("DateComboBox","baseComponent",{
	itemName: "开始结束日期",	
	todatebz:"Y",
	children:[{"id":"com_startdate","name":"开始日期"},{"id":"com_enddate","name":"结束日期"},{"id":"com_todate","name":"至今"}],
  	_getHtml : function(data,previewmode){ 
	    var c = "",val = data.value?data.value:data.defaultval;	    
	    if(previewmode){	    	
	    	c = '<div class="main_inner_content_info_autoheight">'
	    	c += '<div class="main_inner_connent_info_left"><span class="reg_title_star">'+(data.isRequire =="Y" ?"*":"")+'</span>'+data.itemName+'：</div>'
	    	c += '<div class="main_inner_content_info_right"><input name="'+children[0]["id"]+'" type="text" class="input_97px" id="'+children[0]["id"]+'" readonly="readonly" onchange="reFocus(\''+children[0]["id"]+'\');" title="'+children[0]["name"]+'" />&nbsp<input name="'+children[1]["id"]+'" type="text" class="input_97px" id="'+children[1]["id"]+'" readonly="readonly" onchange="reFocus(\''+children[1]["id"]+'\');" title="'+children[0]["name"]+'" /><input type="checkbox" id="'+children[2]["id"]+'" name="'+children[2]["id"]+'" onclick="nowWorking();" />至今';
	    	c += '<div style="margin-top:-40px;margin-left:280px"><span id="'+data.itemId+'Tip" class="onShow" ></span></div>';
	    	c += '</div></div>'
		
	    }else{
	    	 c = '<div class="question-answer"><div class="format "><b class="read-input" style="min-width:150px;"></b><b class="read-input" style="min-width:150px;"></b>'+(data.todatebz == "Y"?'<b style="padding: 15px 0px 15px 15px;color: #666;background: url("/assets/img/read-check.gif") no-repeat scroll -4px 4px;margin-left: 10px;"></b>至今':'')+'<span class="suffix">' + (data["suffix"] ? data.suffix: "") + "</span></div></div>"
	    }	    
	    return c;
	},
	edit : function(data){
		var e = '<div class="edit_jygz"><span>校验规则：</span><div class="edit_item_warp" style="margin-top:5px;"><input type="checkbox" onchange="SurveyBuild.saveAttr(this,\'isRequire\')"'+(data.isRequire == "Y" ? "checked='checked'" :"")+' id="is_require"> 是否必填，必填则勾选&nbsp;&nbsp</div>';
		return e;
	}
})