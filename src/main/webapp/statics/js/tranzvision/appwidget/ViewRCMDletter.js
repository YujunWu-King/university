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
            
            for(var i=0; i<letters.length; i++){
            	var tzParams = "";
            	tjxInsId = letters[i].tjxInsId;
            	letterId=letters[i].letterId;
            	tjrId = letters[i].tjrId;
            	/**/
            	mtplId = letters[i].mtplId;
            	
            	if(mtplId == ""){
            		tzParams = '{"TZ_APP_INS_ID":"' + tjxInsId + '","TZ_REF_LETTER_ID":"' + letterId + '","TZ_MANAGER":"Y"}';
            	}else{
            		tzParams = '{"TZ_APP_TPL_ID":"' + mtplId + '","TZ_APP_INS_ID":"' + tjxInsId + '","TZ_REF_LETTER_ID":"' + letterId + '","TZ_MANAGER":"Y"}';
            	}
            	
            	tzParams = '{"ComID":"TZ_ONLINE_REG_COM","PageID":"TZ_ONLINE_APP_STD","OperateType":"HTML","comParams":' + tzParams + '}';
            	url = TzUniversityContextPath + '/dispatcher?tzParams=' + encodeURIComponent(tzParams);
                
            	c += '<div class="mainright-title">';
                c += '	<span class="title-line"></span> ' + MsgSet["REFFER"] + tjrId;
                c += '</div>';
                c += '<div class="main_content_box">';
                c += '	<div class="mainright-box pos-rela">';
                c += '		<iframe name="' + tjxInsId + '" src="' + url + '" width="100%"  height="500" frameborder="0" scrolling="no" onload="SetCwinHeight(this)"></iframe>';
                c += '	</div>';
                c += '</div>';
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
		return "";
	}
})