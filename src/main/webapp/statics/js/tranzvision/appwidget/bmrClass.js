/**
 * Created by wrl on 2015/6/18.
 * 报名人班级
 */
SurveyBuild.extend("bmrClass","baseComponent",{
    itemName: "报考班级",
    title:"报考班级",
    "StorageType":"S",
    wzsm:"",
    _getHtml : function(data,previewmode){
        var c = "", val = data.value,desc = "";
        if(previewmode){
            //if(val.length < 1){
                var classid = $("#ClassId").val();
                var params = '{"ComID":"TZ_ONLINE_REG_COM","PageID":"TZ_ONREG_OTHER_STD","OperateType":"EJSON","comParams":{"OType":"CLASSINFO","CLASSID":' + classid + '}}';
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
                            val = f.comContent.classCode;
                            desc = f.comContent.className;
                            data.value = val;
                            data.wzsm = desc;
                        }
                    }
                });
            //}

            c += '<div class="main_inner_content_info_autoheight" style="white-space:normal;">';
            c += '	<div class="main_inner_connent_info_left"><span class="reg_title_star">*</span>' + data.title + '</div>';
            c += '	<div class="main_inner_content_info_right">';
            c += '		<div class="main_inner_content_info_right_option_251px" style="width:400px;">' + data.wzsm + '</div>';
            c += '     <input id="' + data.itemId + '" type="hidden" name="' + data.itemId + '" value="' + data.value + '">';
            c += '		<div class="main_inner_content_info_edit">';
            c += '			<a class="blue" target="' + (data.suffixUrl ? "_blank" : "") + '" href="' + (data.suffixUrl ? data.suffixUrl : "javascript:void(0);") + '">' + data.suffix + '</a>';
            c += '		</div>';
            c += '	</div>';
            c += '</div>';

        }else{
            c += '<div class="question-answer">';
            c += '	<div class="format">';
            c += '		<b class="read-input"></b>';
            c += '		<span class="suffix">' + (data["suffix"] ? data.suffix: "") + '</span>';
            c += '	</div>';
            c += '</div>';
        }
        return c;
    },
    _edit : function(data){
        var e = '';
        //后缀
        e += '<fieldset>';
        e += '	<legend>';
        e += '      <span class="edit_item_label">后缀：</span>';
        e += '     <input type="text" class="medium" onkeyup="SurveyBuild.saveAttr(this,\'suffix\')" value="' + data.suffix + '"/>';
        e += '	</legend>';
        e += '</fieldset>';

        //后缀链接
        e += '<fieldset>';
        e += '	<legend>';
        e += '      <span class="edit_item_label">后缀链接：</span>';
        e += '     <input type="text" class="medium" onkeyup="SurveyBuild.saveAttr(this,\'suffixUrl\')" value="' + data.suffixUrl + '"/>';
        e += '	</legend>';
        e += '</fieldset>';

        return e;
    },
	_eventbind: function(data) {
		//自动换行后高度调整
		var $Input = $("#" + data.itemId);	
		var $classDiv = $Input.prev(".main_inner_content_info_right_option_251px");
		var offHeight = $classDiv.get(0).offsetHeight;
		var scrHeight = $classDiv.get(0).scrollHeight;
		if(offHeight < scrHeight){
			$Input.closest(".main_inner_content_info_autoheight").css("padding-bottom","53px");	
		}
	}
});