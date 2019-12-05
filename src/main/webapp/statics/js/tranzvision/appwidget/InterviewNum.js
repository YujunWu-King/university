/*====================================================================
 + 功能描述：面试申请号												++
 + 开发人：曹阳											    ++
 + 开发日期：2017-02-13											++
 =====================================================================*/
SurveyBuild.extend("InterviewNum", "baseComponent", {
    itemName: "面试申请号",
    title:"面试申请号",
    "StorageType":"S",
    _getHtml: function(data, previewmode) {
        var c = "";
        if (previewmode) {
            SurveyBuild.appInsId == "0" && (data.defaultval = "{%BIND:TZ_MSH_ID}") ;
            SurveyBuild.appInsId == "0" && this._getDefaultVal(data);
            SurveyBuild.appInsId == "0" && (data.wzsm = data.value);
        	//data.defaultval = "{%BIND:TZ_MSH_ID}";
        	//this._getDefaultVal(data);
        	//data.wzsm = data.value;
            
            if (data.wzsm =="") {
            	data.defaultval = "{%BIND:TZ_MSH_ID}";
            	this._getDefaultVal(data);
            	data.wzsm = data.value;      	
            }
        	
            if(SurveyBuild.accessType == "M"){
            	
					//填写模式
					SurveyBuild.appInsId == "0" && this._getDefaultVal(data);
					var regular = "";
					if (data.preg && SurveyBuild._preg.hasOwnProperty(data.preg)) {
						regular = SurveyBuild._preg[data.preg]["regExp"];
					}
					c += '<div class="item">';
					c += '<p>'+data.title+'<span>*</span></p>';		
					c += '<div class="text-box"><input readonly="true" type="text" class="text1"  name="' + data.itemId + '" value="' + data.value + '" title="' + data.itemName + '" data-regular="' + regular + '"  placeholder="无需填写，系统自动生成"/></div>';
					 c += '<input id="' + data.itemId + '" type="hidden" name="' + data.itemId + '" value = "' + data.value + '">';
					if ($.trim(data.onShowMessage) != "") {
						c += '<p style="color:#666;font-size:0.56rem;">' + data.onShowMessage + '</p>';
					}
					c += '</div>';
            }else{
            	 c += '<div class="input-list">';
                 c += '	<div class="input-list-info left"><span class="red">*</span>' + data.title + '</div>';
                 c += '	<div class="input-list-text left">' + data.value + '</div>';
                 c += '  <div class="input-list-suffix left">' + (data["suffix"] ? data.suffix + '<span class="input-list-suffix-span">&nbsp;</span>': "") + '</div>';
                 //c += '	<div class="input-list-suffix left" style="display:' + (SurveyBuild._readonly?'none':'') + '"><a class="blue" target="' + (data.suffixUrl ? "_blank" : "") + '" href="' + (data.suffixUrl ? data.suffixUrl : "javascript:void(0);") + '">' + data.suffix + '</a></div>';
                 c += '	<div class="clear"></div>';
                 c += '</div>';
                 c += '<input id="' + data.itemId + '" type="hidden" name="' + data.itemId + '" value = "' + data.value + '" placeholder="系统自动生成">';
                 if ($.trim(data.onShowMessage) != "") {
     				c += '<div class="input-list-blank" id="' + data.itemId + 'msg">';
     				c += '	<div class="input-list-info-blank left"><span class="red-star"></div>';
     				c += '	<div class="input-list-wz left"><span class="blue">' + data.onShowMessage + '</span></div>';
     				c += '	<div class="input-list-suffix-blank left"></div>';
     				c += '	<div class="clear"></div>';
     				c += '</div>';
     			}
            }
        } else {
            c += '<div class="question-answer">';
            c +=         '<div class="format">';
            c +=             '<b class="read-input"  style="width: 200px;">Jack</b>';
            c +=             '<span class="suffix">' + (data["suffix"] ? data.suffix: "") + '</span>';
            c +=         '</div>';
            c += '</div>';
        }

        return c;
    },
    _edit: function(data) {
        var e = "";
        
        e += '<div class="edit_item_warp">';
		e += '  <span class="edit_item_label">提示信息：</span>';
		e += '  <input type="text" class="medium" onkeyup="SurveyBuild.saveAttr(this,\'onShowMessage\')" value="' + data.onShowMessage + '"/>';
		e += '</div>';
		e += '<div class="edit_item_warp">';
		e += '  <span class="edit_item_label">后缀：<a href="#" data-for-id="help_suffix" onclick="SurveyBuild.showMsg(this,event)" class="big-link" data-reveal-id="myModal" data-animation="fade">(?)</a></span>';
		e += '  <input type="text" class="medium" onkeyup="SurveyBuild.saveAttr(this,\'suffix\')" value="' + data.suffix + '"/>';
		e += '</div>';


        return e;
    },
    _eventbind:function(data){
        /*$("#" + data.itemId + "edit").click(function(){
            var menuId = $("#menuId").val();
            var siteId = $("#siteId").val();
            if(menuId && siteId){
                JumpToColu(siteId,menuId);
            }
        });*/
    }
});