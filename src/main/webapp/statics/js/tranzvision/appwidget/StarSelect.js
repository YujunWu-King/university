/**
 * Created by CAOY on 2019/2/14.
 */
SurveyBuild.extend("StarSelect", "baseComponent", {
    itemName: "星选题",
    title: "星选题",
    StorageType:"S",
    qCode:"Q1",
    maxLen: "10",
	isAvg:"N",

    _getHtml: function(data, previewmode) {
		console.log("星选题");
        var c = "",e = "";

        if (previewmode) {
        	
        	console.log(SurveyBuild.appInsId);
            if(SurveyBuild.accessType == "P"){  //PC
				var maxStar = data.maxLen; //最大星数目
                var maxStarValue= data.value; //已选择星数目
                
                e +='<div class="starchoose">';
                
                
                if (SurveyBuild.appInsId=='') { //填写问卷
                	for (var i=1;i<=maxStar;i++){
                		if (i <= maxStarValue) {//已经选中
                			e +='<a href="javascript:;" class="starchoose-a A'+data.itemId+'" style="background-position: 0px 0px;"></a>';
                		} else { //没有选中
                			e +='<a href="javascript:;" class="starchoose-a A'+data.itemId+'" style="background-position: 0px -20px;"></a>';
                		}
                	}
                	e +='</div>';
            		e +='<p class="starchoose-p" id="p'+data.itemId+'"></p>';
                } else{ //查看问卷
                	 for (var i=1;i<=maxStar;i++){
                     	if (i <= maxStarValue) {//已经选中
                     		e +='<a href="javascript:;" class="starchoose-a" style="background-position: 0px 0px;"></a>';
                     	} else { //没有选中
                     		e +='<a href="javascript:;" class="starchoose-a" style="background-position: 0px -20px;"></a>';
                     	}
                     }
                	 e +='</div>';
             		e +='<p class="starchoose-p" >'+data.value + ' 颗星</p>';
                }
        		

                c += '<div class="listcon">';
                c += '  <div id="' + data.itemId + 'Tip" class="onShow">';
                c += '      <div class="onShow"></div>';
                c += '  </div>';
                c += '	<div class="question">';
                c += '		<span class="fontblue-blod">' + data.qCode + '.</span>' +  data.title;
                c += '	</div>';
                c += '	<div class="answer">';
                c += 		e;
                c += '<input  type="hidden" id="' + data.itemId + '" name="' + data.itemId + '" value="' + data.value + '" />';
                c += '	</div>';
                c += '</div>';

            }else{
            	//手机
            	var maxStar = data.maxLen; //最大星数目
                var maxStarValue= data.value; //已选择星数目
                
                e +='<div class="starchoose">';
                
                
                if (SurveyBuild.appInsId=='') { //填写问卷
                	for (var i=1;i<=maxStar;i++){
                		if (i <= maxStarValue) {//已经选中
                			e +='<a href="javascript:;" class="starchoose-a A'+data.itemId+'" style="background-position: 0px 0px;"></a>';
                		} else { //没有选中
                			e +='<a href="javascript:;" class="starchoose-a A'+data.itemId+'" style="background-position: 0px -20px;"></a>';
                		}
                	}
                	e +='</div>';
            		e +='<p class="starchoose-p" id="p'+data.itemId+'"></p>';
                } else{ //查看问卷
                	 for (var i=1;i<=maxStar;i++){
                     	if (i <= maxStarValue) {//已经选中
                     		e +='<a href="javascript:;" class="starchoose-a" style="background-position: 0px 0px;"></a>';
                     	} else { //没有选中
                     		e +='<a href="javascript:;" class="starchoose-a" style="background-position: 0px -20px;"></a>';
                     	}
                     }
                	 e +='</div>';
             		e +='<p class="starchoose-p" >'+data.value + ' 颗星</p>';
                }
        		

                c += '<div class="listcon">';
                c += '  <div id="' + data.itemId + 'Tip" class="onShow">';
                c += '      <div class="onShow"></div>';
                c += '  </div>';
                c += '	<div class="question">';
                c += '		<span class="fontblue-blod">' + data.qCode + '.</span>' +  data.title;
                c += '	</div>';
                c += '	<div class="answer">';
                c += 		e;
                c += '<input  type="hidden" id="' + data.itemId + '" name="' + data.itemId + '" value="' + data.value + '" />';
                c += '	</div>';
                c += '</div>';
                
            }
        } else {
            c += '<div class="question-answer">';
            c += '  <div class="format">';
            for (var i=1;i<=10;i++){
            	c +='<a href="javascript:;" class="starchoose-a" style="background-position: 0px 0px;"></a>';
            	
            }
            c += '  </div>';
            c += '</div>'
        }
        return c;
    },
    _edit: function(data) {
        var e = '';
        
		//设置		
        e += '<div class="edit_jygz">';
        e += '	<span class="title"><i class="icon-cog"></i> 设置</span>';
        e += '  <div class="groupbox">';
		e += '		<div class="edit_item_warp" style="margin-top:5px;">';
		e += '			<input class="mbIE" onchange="SurveyBuild.saveAttr(this,\'isAvg\')" ' + (data.isAvg == "Y" ? "checked='checked'": "") + ' id="isAvg" type="checkbox">';
		e += '			<label for="isAvg">是否计算平均分</label>';
		e += '		</div>';
        e += '      <div class="edit_item_warp">';
        e += '            <span class="edit_item_label">星数目</span>';
        e += '          <input type="text" maxlength="11" class="medium minLen" data_id="' + data.instanceId + '" onkeyup="SurveyBuild.saveAttr(this,\'maxLen\')" value="' + data.maxLen + '"/>';
        e += '      </div>';
		e += '	</div>';
		e += '</div>';

        //校验规则
        e += '<div class="edit_jygz">';
        e += '	<span class="title"><i class="icon-cog"></i> 校验规则</span>';
        e += '  <div class="groupbox">';
        e += '	    <div class="edit_item_warp" style="margin-top:5px;">';
        e += '		    <input class="mbIE" type="checkbox" onchange="SurveyBuild.saveAttr(this,\'isRequire\')"' + (data.isRequire == "Y" ? "checked='checked'": "") + ' id="is_require"> <label for="is_require">是否必填</label>';
        e += '	    </div>';
        e += '	</div>';
        //高级设置
        e += '	<div class="edit_item_warp">';
        e += '		<a href="javascript:void(0);" onclick="SurveyBuild.RulesSet(this);"><i class="icon-cogs"></i> 高级设置</a>';
        e += '	</div>';
        e += '</div>';
        return e;
    },
    
    
    _eventbind: function(data) {
    	
    	//鼠标滑动操作

    	var $star =$(".A"+ data["itemId"]);
    	
    	
    	var $inputBox = $("#" + data.itemId);
    	
    	console.log($star);
    	var max = data.maxLen;
    	

    	console.log("max:"+max);
    	
		$star.each(function(index){
    		$(this).mouseover(function(){
				
    			console.log("mouseover");
				console.log(index);
    			for(var i = 0;  i < max; i++){
    				$star[i].style.backgroundPosition = '0 -20px';
    			}
    			for(var j = 0; j < index + 1; j++){
    				$star[j].style.backgroundPosition = '0 0';
    			}
    		});
    		
    		$(this).mouseout(function(){
    			console.log("onmouseout");
    			for(var j = 0; j < index + 1; j++){
    				$star[j].style.backgroundPosition = '0 -20px';
    			}
    			var temp=$inputBox.val();
    			if ($inputBox.val()=='') {
    				temp=0;
    			}
    			console.log("temp:"+temp);
    			
    			for(var i = 0; i < temp; i++){
    				$star[i].style.backgroundPosition = '0 0';
    			}
    			
    		});
    		
    		
    		$(this).click(function(){
    			console.log("onclick");
    			temp = index + 1;
    			console.log("temp:"+temp);
    			$("#p"+ data["itemId"]).html( temp + ' 颗星');
    			$inputBox.val(temp);
    			for(var i = 0; i < temp; i++){
    				$star[i].style.backgroundPosition = '0 0';
    			}

    		}); 
    		
    	});


		$inputBox.formValidator({tipID: (data["itemId"] + 'Tip'),onShow: "&nbsp;",onFocus: "&nbsp;",onCorrect: "&nbsp;"});
		$inputBox.functionValidator({
			fun: function(val, elem) {

				//执行高级设置中的自定义规则
				/*********************************************\
                 ** 注意：自定义规则中不要使用formValidator **
                 \*********************************************/
				var _result = true;
				if (ValidationRules) {
					$.each(data["rules"],
					function(classname, classObj) {
						if ($.inArray(classname, SurveyBuild._baseRules) == -1 && data["rules"][classname]["isEnable"] == "Y") {
							var _ruleClass = ValidationRules[classname];
							if (_ruleClass && _ruleClass._Validator) {
								_result = _ruleClass._Validator(data["itemId"], classObj["messages"], data);
								if (_result !== true) {
									return false;
								}
							}
						}
					});
					if (_result !== true) {
						return _result;
					}
				}
				return _result;
			}
		});
    }
});