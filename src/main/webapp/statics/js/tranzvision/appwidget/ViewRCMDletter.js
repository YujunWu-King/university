/*====================================================================
+ 功能描述：查看报名表对应的推荐信内容									++
+ 开发人：王瑞立														++
+ 开发日期：2017-05-08												++
=====================================================================*/
SurveyBuild.extend("ViewRCMDletter", "baseComponent", {
	itemName: "查看推荐信",
	title: "查看推荐信",

	_getHtml: function(data, previewmode) {
		var c = '';
		if (previewmode) {
            //编辑模式
			var letters = {};
            var insid = SurveyBuild.appInsId;
            if(insid != "" & insid != "0"){
                var params = '{"ComID":"TZ_ONLINE_REG_COM","PageID":"TZ_ONREG_OTHER_STD","OperateType":"EJSON","comParams":{"OType":"RCMD","INSID":"' + insid + '"}}';
                $.ajax({
                    type: "get",
                    dataType: "JSON",
                    data:{
                        tzParams:params
                    },
                    async:false,
                    url:SurveyBuild.tzGeneralURL,
                    success: function(f) {
                        if(f.state.errcode == "0"){
                        	letters = f.comContent;
                        	console.log(f.comContent);
                        }
                    }
                });
            }
            if(letters.length > 0){
                for(var i=0; i<letters.length; i++){
                	var tzParams = "";
                	tjxInsId = letters[i].tjxInsId;
                	letterId=letters[i].letterId;
                	tjrId = letters[i].tjrId;
    				attUserFile = letters[i].attUserFile;
    				attAccLink = TzUniversityContextPath + letters[i].attAccLink;
    				tjxType = letters[i].tjxType;
    				mtplId = letters[i].mtplId;
    				tjrHTMl = "";
    				if(tjxType == "S"){
    					/*上传附件*/
    					tjrHTMl += '<div class="input-list-upload">';
    					tjrHTMl += '	<a class="input-list-uploadcon-list-a" target="_blank" href="' + attAccLink + '">' + attUserFile + '</a>';
    					tjrHTMl += '</div>';
    					tjrHTMl += '<div class="clear"></div>';
    				}else{
    					/*发送邮件*/
                    	if(mtplId == ""){
                    		tzParams = '{"TZ_APP_INS_ID":"' + tjxInsId + '","TZ_REF_LETTER_ID":"' + letterId + '","TZ_MANAGER":"Y"}';
                    	}else{
                    		tzParams = '{"TZ_APP_TPL_ID":"' + mtplId + '","TZ_APP_INS_ID":"' + tjxInsId + '","TZ_REF_LETTER_ID":"' + letterId + '","TZ_MANAGER":"Y"}';
                    	}
                    	tzParams = '{"ComID":"TZ_ONLINE_REG_COM","PageID":"TZ_ONLINE_APP_STD","OperateType":"HTML","comParams":' + tzParams + '}';
                    	url = TzUniversityContextPath + '/dispatcher?tzParams=' + encodeURIComponent(tzParams);
                    	tjrHTMl = '<iframe name="' + tjxInsId + '" src="' + url + '" width="100%"  height="' + data.height + 'px" frameborder="0" scrolling="no"></iframe>'
    				}
    				
    				
                    
                	c += '<div class="mainright-title">';
                    c += '	<span class="title-line"></span> ' + MsgSet["REFFER"] + tjrId;
                    c += '</div>';
                    c += '<div class="main_content_box">';
                    c += '	<div class="mainright-box pos-rela">';
                    c += tjrHTMl;
                    c += '	</div>';
                    c += '</div>';
                    // onload="SetCwinHeight(this)"
                }
            }else{
            	c += '<div class="main_content_box"><div class="mainright-box pos-rela"><center>无推荐信</center></div></div>';
            }
		} else {
			c += '<div class="question-answer">';
			c += '  <div class="format">';
			c += '      <b class="read-input"></b>';
			c += '  </div>';
			c += '</div>'
		}
		return c;
	},
	_edit: function(data) {
		e = "";
		e += '  <div class="edit_item_warp">';
		e += '      <span class="edit_item_label">高度：</span>';
		e += '      <input type="text" maxlength="4" class="medium minLen" data_id="' + data.instanceId + '" onkeyup="SurveyBuild.saveAttr(this,\'height\')" value="' + data.height + '" style="width: 170px;"/>&nbsp;px';
		e += '  </div>';
		return e;
	}
})