//用于材料评审显示标准、说明、参考资料
var TZShowTipsWin = {
	_data:{},	
	init: function(obj){
		var i = obj.id;
		$("#pop-"+i).remove();
		
		this._data[i] = obj;
		$("#"+i).bind("click",function(){
			TZShowTipsWin.show(i);
		});
	},
	
	show: function(ins){
		var showData = this._data[ins];
		var target = showData.position.target;
		var scrollTop = target.scrollTop();
		
		var addTop = 20;
		if(target.length > 0 && target[0].id == "tz_evaluation_main"){
			addTop = -20;
		}
		
		var obj = $("#"+ins);
		var offset = obj.offset();
        var top = offset.top + scrollTop + addTop;
        var left = offset.left;
        var wid=$(window).width();
        var right = wid-left-20;
        
        var type = showData.type == "" || showData.type == "undefined" ? "1" : showData.type;
        var title  = showData.title;
        var content  = showData.content.text;
        //隐藏所有窗口
        target.children(".pop").hide();
        
        var tipId = "pop-"+ins;
        if(target.children("#"+tipId).length == 0){
        	var html = this._html(type,ins,title,content,top,right,target);
        	target.append(html);
        }else{
        	
        	target.children("#"+tipId).show();
        }
	},
	
	_html: function(type,ins,title,content,top,right,target){
		var tipId = "pop-"+ins;
		var widthCss = type != "1" ? "width:700px;" : "";
		var str = '<div class="pop" id="'+ tipId +'" style="display:block;top:'+ top +'px;right:'+ right +'px;'+ widthCss +'">';
		
		if(type == "1"){//直接显示内容
			str = str + '<img class="pop_close" onclick=TZShowTipsWin.hidden("'+ ins +'") src="'+ ContextPath +'/statics/images/evaluation/close.png">';
		}else{//显示确认弹框
			str = str + '<img class="pop_close" onclick=TZShowTipsWin.close("'+tipId+'") src="'+ ContextPath +'/statics/images/evaluation/close.png">';
		}
		//标题	
		str = str + '<p class="pop_tit">'+ title +'</p>';
		
		if(type == "1"){
			str = str + content;
		}else{
			//
			var okText="",cancelText="";
			var c = this._data[ins].content;
			okText = (c.okBtn.text == "" || c.okBtn.text == "undefined") ? "确认" : c.okBtn.text;
			cancelText = (c.cancelBtn.text == "" || c.cancelBtn.text == "undefined") ? "取消" : c.cancelBtn.text;
			
			str = str + '<p class="tips_lag">'+ content +'</p><div class="btn">'
				+ '<a href="#" class="btn_left" onclick=TZShowTipsWin.ensure("'+ins+'")>'+ okText +'</a>'
				+ '<a href="#" class="btn_right" onclick=TZShowTipsWin.close("'+tipId+'")>'+ cancelText +'</a></div>';
		}

		str = str + "</div>";
		
		return str;
	},
	
	hidden: function(ins){
		var showData = this._data[ins];
		var target = showData.position.target;
		target.children(".pop").hide();
	},
	
	ensure: function(ins){
		var showData = this._data[ins];
		var callbackFun = showData.content.okBtn.callback;
		
		$("#pop-"+ins).remove();
		if(typeof callbackFun === "function"){
			callbackFun();
		}
	},
	
	close: function(id){
		$("#"+id).remove();
	},
	
	autoFixPosition: function(){
		var data = this._data;
		$.each(data, function(id,insObj){
			var tipsObj = $("#pop-"+id);
			if(tipsObj.length > 0){
				var target = insObj.position.target;
				var scrollTop = target.scrollTop();

				var obj = $("#"+id);
				if(obj.length > 0){
					var addTop = 20;
					if(target.length > 0 && target[0].id == "tz_evaluation_main"){
						addTop = -20;
					}
					
					var offset = obj.offset();
			        var top = offset.top + scrollTop + addTop;
			        var left = offset.left;
			        var wid=$(window).width();
			        var right = wid-left-20;
			        
			        $("#pop-"+id).css("top",top);
			        $("#pop-"+id).css("right",right);
				}
			}
		});
	}
}